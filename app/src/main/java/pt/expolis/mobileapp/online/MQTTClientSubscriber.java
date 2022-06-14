package pt.expolis.mobileapp.online;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import pt.expolis.mobileapp.ExpolisPreferences;

/**
 * This class handles the subscription to the MQTT server where sensor data is published.
 *
 * <p>The main methods of this class are:
 * <ul>
 *    <li>{@code connectMQTTServer} which connects to the MQTT server and setups the callback to reconnect
 *        to the MQTT server;</li>
 *    <li>{@code subscribeTo} that subscribes to the MQTT topic associated with sensor data.</li>
 * </ul>
 * </p>
 *
 * <p>Whenever a MQTT message arrives, we use the {@code Handler} provided as
 * parameter of method {@code void subscribeTo (Handler)} to communicate new sensor
 * data to the {@code OnlineData} instance.</p>
 *
 * <p>TODO: check exceptions when trying to connect.</p>
 */
public class MQTTClientSubscriber
{
	// region fields used in mqtt message
	private static final int FIELD_DATE = 2;
	private static final int FIELD_TIME = 3;
	private static final int FIELD_LATITUDE = 4;
	private static final int FIELD_LONGITUDE = 5;
	private static final int FIELD_CO = 6;
	private static final int FIELD_NO2 = 8;
	private static final int FIELD_PM_1 = 13;
	private static final int FIELD_PM_2_5 = 14;
	private static final int FIELD_PM_10 = 15;
	private static final int FIELD_TEMPERATURE = 16;
	private static final int FIELD_HUMIDITY = 18;
	// endregion
	private static final SimpleDateFormat TIMESTAMP_FORMATTER_WITH_DOT = new SimpleDateFormat ("yyyy-M-d'T'H:m:s.S", Locale.US);
	private static final SimpleDateFormat TIMESTAMP_FORMATTER_NO_DOT = new SimpleDateFormat ("yyyy-M-d'T'H:m:s", Locale.US);
	private MqttClient mqttClient;
	private boolean subscribed = false;
	/**
	 * The single instance of this class.
	 */
	public static MQTTClientSubscriber instance = null;

	public static void connect ()
	  throws MqttException
	{
		if (instance == null) {
			instance = new MQTTClientSubscriber ();
		}
	}

	public static void disconnect ()
	{
		if (instance != null) {
			instance.closeConnectionMQTTServer ();
			instance = null;
		}
	}

	/**
	 * Constructor is private as class is a singleton
	 */
	private MQTTClientSubscriber ()
	  throws MqttException
	{
		this.connectMQTTServer ();
	}

	void connectMQTTServer ()
	  throws MqttException
	{
		this.mqttClient = new MqttClient (
		  UrlOptions.mqttServerHost (),
		  MqttClient.generateClientId (),
		  new MemoryPersistence ()
		);
		this.mqttClient.connect ();
		this.setMQTTCallBack ();
	}

	private void setMQTTCallBack ()
	{
		this.mqttClient.setCallback (new MqttCallback ()
		{
			@Override
			public void connectionLost (Throwable cause)
			{
				try {
					MQTTClientSubscriber.this.connectMQTTServer ();
				}
				catch (MqttException ignored) {
				}
			}

			@Override
			public void messageArrived (String topic, MqttMessage message)
			{
			}

			@Override
			public void deliveryComplete (IMqttDeliveryToken token)
			{
			}
		});
	}
	/**
	 * Subscribe to all sensor nodes.
	 * @param handler the handler that is used to communicate new data to the online data
	 * fragment.
	 */
	void subscribeTo (Handler handler)
	{
		if (subscribed) {
			return ;
		}
		unsubscribe ();
		for (SensorNode sensorNode: SensorNode.sensorNodeMap.values ()) {
			try {
				mqttClient.subscribe (getSensorNodeDataTopic (sensorNode.sensorNode.id), 2,
				  (ignored, message) -> {
					updateOnlineDataWidget (handler, message, sensorNode);
//					System.out.println (new String (message.getPayload ()));
				});
				subscribed = true;
			}
			catch (MqttException ignored) {
			}
		}
	}

	void closeConnectionMQTTServer ()
	{
		unsubscribe ();
		try {
			this.mqttClient.close ();
		} catch (MqttException ignored) {
		}
		this.mqttClient = null;
	}

	public void unsubscribe ()
	{
		if (!subscribed) {
			return;
		}
		for (SensorNode sd: SensorNode.sensorNodeMap.values ()) {
			try {
				this.mqttClient.unsubscribe (getSensorNodeDataTopic (sd.sensorNode.id));
			} catch (MqttException ignored) {
			}
		}
		subscribed = false;
	}

	private static String getSensorNodeDataTopic (int sensorId)
	{
//		return "expolis_debug/sensor_nodes/sn_" + sensorId;
		return "expolis_project/sensor_nodes/sn_" + sensorId;
	}

	private static void updateOnlineDataWidget (
	  Handler handler, MqttMessage message, SensorNode sensorNode)
	{
		String payload = new String (message.getPayload ());
		String[] fields = payload.split (" ");
		if (fields.length != 31) {
			android.os.Message.obtain (handler, Message.UNRECOGNIZED_DATA, fields)
			  .sendToTarget ();
			return;
		}
		try {
			String sWhen = fields [FIELD_DATE] + "T" + fields [FIELD_TIME];
			sensorNode.sensorData.when =
			  fields [FIELD_TIME].indexOf ('.') == -1 ?
			  TIMESTAMP_FORMATTER_NO_DOT.parse (sWhen) :
			  TIMESTAMP_FORMATTER_WITH_DOT.parse (sWhen);
			sensorNode.sensorData.co = Math.round (Float.parseFloat (fields [FIELD_CO]));
			sensorNode.sensorData.no2 = Math.round (Float.parseFloat (fields [FIELD_NO2]));
			sensorNode.sensorData.pm1 = Math.round (Float.parseFloat (fields [FIELD_PM_1]));
			sensorNode.sensorData.pm2_5 = Math.round (Float.parseFloat (fields [FIELD_PM_2_5]));
			sensorNode.sensorData.pm10 = Math.round (Float.parseFloat (fields [FIELD_PM_10]));
			sensorNode.sensorData.temperature = Float.parseFloat (fields [FIELD_TEMPERATURE]);
			sensorNode.sensorData.humidity = Float.parseFloat (fields [FIELD_HUMIDITY]);
			sensorNode.sensorData.longitude = Float.parseFloat (fields [FIELD_LONGITUDE]);
			sensorNode.sensorData.latitude = Float.parseFloat (fields [FIELD_LATITUDE]);
			sensorNode.hasData = true;
			android.os.Message.obtain (handler, Message.NEW_SENSOR_DATA, sensorNode)
			  .sendToTarget ();
		} catch (NumberFormatException | ParseException ignored) {
		}
	}

	/**
	 * This class contains the options to build the url to access the ExpoLIST MQTT broker.
	 */
	public static class UrlOptions
	{
		private static final String DEFAULT_HOST = "mqtt.expolis.pt";
		public static String host = DEFAULT_HOST;
		private static final int DEFAULT_PORT = 1883;
		public static int port = DEFAULT_PORT;
		public static boolean useServerNode = false;
		public static void load (SharedPreferences preferences)
		{
			UrlOptions.host = preferences.getString (
			  ExpolisPreferences.KEY_MQTT_URL_HOST,
			  DEFAULT_HOST
			);
			UrlOptions.port = preferences.getInt (
			  ExpolisPreferences.KEY_MQTT_URL_PORT,
			  DEFAULT_PORT
			);
			UrlOptions.useServerNode = preferences.getBoolean (
			  ExpolisPreferences.KEY_MQTT_USE_SENSOR_NODE,
			  false
			);
		}
		public static void save (SharedPreferences preferences)
		{
			SharedPreferences.Editor editor = preferences.edit ();
			editor.putString (
			  ExpolisPreferences.KEY_MQTT_URL_HOST,
			  UrlOptions.host
			);
			editor.putInt (
			  ExpolisPreferences.KEY_MQTT_URL_PORT,
			  UrlOptions.port
			);
			editor.putBoolean (
			  ExpolisPreferences.KEY_MQTT_USE_SENSOR_NODE,
			  UrlOptions.useServerNode
			);
			editor.apply ();
		}
		/**
		 * The name of the MQTT server where we subscribe real time sensor data.
		 */
		@SuppressLint("DefaultLocale")
		static String mqttServerHost ()
		{
			return UrlOptions.useServerNode ?
						"tcp://172.20.0.1:1883" :
						String.format (
						  "tcp://%s:%d",
						  UrlOptions.host,
						  UrlOptions.port
						);
		}
	}
}
