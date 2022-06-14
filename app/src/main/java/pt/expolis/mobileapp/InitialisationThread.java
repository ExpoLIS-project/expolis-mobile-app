package pt.expolis.mobileapp;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.SQLException;

import pt.expolis.mobileapp.database.Cache;
import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.online.MQTTClientSubscriber;
import pt.expolis.mobileapp.online.OnlineDataFragment;

/**
 * Initialisation thread that connects to the ExpoLIS sensor database, the ExpoLIS MQTT broker,
 * and the ExpoLIS open routing server and that caches sensor and bus data.
 *
 * <p>The initialisation thread runs in the background when the application starts.  Initially
 * feedback is shown on the welcome screen (handled by class {@code WelcomeFragment}.  This
 * screen displays three red/green icons representing the (un)availability of the three
 * ExpoLIS servers.  It also displays the actions that the initialisation thread is performing.</p>
 *
 * <p>If the connection to the ExpoLIS servers is taking too long, the user can skip the welcome
 * screen.  The user is presented with the online data screen.  Since this screen does not have
 * any status label, feedback is minimal.  When the servers are available the corresponding
 * functionalities in the online data screen are enabled.</p>
 *
 * <p>This thread communicates its progress to the welcome screen graphical user interface via
 * the {@code WelcomeScreenHandler}. Three types of messages are sent by this thread:
 * <ol>
 *    <li>update the status text view to inform the user of the background thread progress;</li>
 *    <li>enable the administration mode so that the user can change app preferences;</li>
 *    <li>close the welcome screen so that the app can proceed to the next step.</li>
 * </ol></p>
 *
 * @see pt.expolis.mobileapp.WelcomeScreenFragment.WelcomeScreenHandler
 */
public class InitialisationThread
	implements Runnable
{
	// region messages sent by this thread
	public static final int MESSAGE_PRINT = 1;
	public static final int MESSAGE_ENABLE_ADMIN = 2;
	public static final int MESSAGE_ADVICE = 3;
	public static final int MESSAGE_WAIT_FOR_INTERNET = 5;
	public static final int MESSAGE_ENABLE_PROCEED = 6;
	public static final int MESSAGE_CLOSE = 4;
	public static final int MESSAGE_STATUS_EXPOLIS_SERVER = 10;
	public static final int MESSAGE_STATUS_EXPOLIS_MQTT = 11;
	public static final int MESSAGE_STATUS_EXPOLIS_PLANNER = 12;
//	public static final int MESSAGE_START_BACKGROUND_THREAD = 100;
	public static final int MESSAGE_ENABLE_SKIP = 7;
	// endregion

	private final Context context;
	private final Resources resources;
	/**
	 * Handler that is used to report the state of this thread to the welcome screen fragment.
	 */
	private final WelcomeScreenFragment.WelcomeScreenHandler welcomeScreenHandler;
	/**
	 * Handler that is used to report the state of this thread to the online data fragment.
	 */
	private final OnlineDataFragment.OnlineDataHandler onlineDataHandler;
	private boolean sendingToWelcomeScreen = true;
	private boolean close = true;
	private boolean enableAdmin = false;
	private int numberOfflineServers = 0;
	InitialisationThread (
	  Context context, Resources resources,
	  WelcomeScreenFragment.WelcomeScreenHandler welcomeScreenHandler,
	  OnlineDataFragment.OnlineDataHandler onlineDataHandler)
	{
		this.context = context;
		this.resources = resources;
		this.welcomeScreenHandler = welcomeScreenHandler;
		this.onlineDataHandler = onlineDataHandler;
	}

	/**
	 * Sets the flag {@code sendingToWelcomeScreen} to {@code false}. Messages sent from this
	 * thread will be delivered to the {@code OnlineDataFragment} handler.
	 */
	synchronized void sendToOnlineData ()
	{
		this.sendingToWelcomeScreen = false;
	}

	@Override
	public void run ()
	{
		if (this.checkInternetConnection ()) {
			this.checkExpolisSensorDatabaseServer ();
			this.checkExpolisMqttBroker ();
			this.checkExpolisRouteServer ();
			this.finishInitialisation ();
		}
	}

	private boolean checkInternetConnection ()
	{
		ConnectivityManager cm =
		  (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnected ();
		if (!isConnected) {
			this.sendMessagePrint (R.string.no_internet_connection);
			this.sendMessageAdvice (R.string.advice_turn_on_internet);
			this.sendMessage (MESSAGE_WAIT_FOR_INTERNET);
			return false;
		}
		return true;
	}

	private void checkExpolisSensorDatabaseServer ()
	{
		boolean serverStatus;
		this.sendMessagePrint (R.string.connecting_postgres);
		try {
			PostGisServerDatabase.connect ();
			this.sendMessagePrint (R.string.connected_postgres);
			serverStatus = true;
		} catch (SQLException ignored) {
			this.sendMessagePrint (this.resources.getString (
			  R.string.connection_postgres_failed,
			  PostGisServerDatabase.UrlOptions.host
			));
			serverStatus = false;
			close = false;
			enableAdmin = true;
			numberOfflineServers++;
		}
		this.sendMessageStatus (MESSAGE_STATUS_EXPOLIS_SERVER, serverStatus);
		// region cache sensor data
		if (serverStatus) {
			this.sendMessagePrint (R.string.fetching_measurement_timestamps);
			Cache.minMeasurementDate = PostGisServerDatabase.instance.measurementsDAO ().minDate ();
			Cache.maxMeasurementDate = PostGisServerDatabase.instance.measurementsDAO ().maxDate ();
			this.sendMessagePrint (R.string.fetching_measurement_bus_lines);
			Cache.busLines = PostGisServerDatabase.instance.busesLinesDAO ().getLines ();
			this.sendMessagePrint (R.string.fetching_sensor_nodes);
			Cache.sensorNodes = PostGisServerDatabase.instance.sensorNodesDAO ().getSensorNodes ();
			this.sendMessagePrint (R.string.fetching_online_data_map_layers);
			Cache.mapLayerCO = PostGisServerDatabase.instance.mapLayersSqlDao ().getCO ();
			Cache.mapLayerNO2 = PostGisServerDatabase.instance.mapLayersSqlDao ().getNO2 ();
			Cache.mapLayerPM1 = PostGisServerDatabase.instance.mapLayersSqlDao ().getPM1 ();
			Cache.mapLayerPM25 = PostGisServerDatabase.instance.mapLayersSqlDao ().getPM25 ();
			Cache.mapLayerPM10 = PostGisServerDatabase.instance.mapLayersSqlDao ().getPM10 ();
			this.sendMessagePrint (R.string.saving_bus_lines);
			Cache.saveSensorNodes (this.context);
		}
		else {
			this.sendMessagePrint (R.string.loading_bus_lines);
			if (!Cache.loadSensorNodes (this.resources, this.context))
				this.sendMessagePrint (R.string.using_default_bus_lines);
		}
		// endregion
	}

	private void checkExpolisMqttBroker ()
	{
		boolean serverStatus;
		this.sendMessagePrint (R.string.connecting_mqtt_broker);
		try {
			MQTTClientSubscriber.connect ();
			this.sendMessagePrint (R.string.connected_mqtt_broker);
			serverStatus = true;
		} catch (MqttException ignored) {
			this.sendMessagePrint (this.resources.getString (
			  R.string.connection_mqtt_broker_failed,
			  PostGisServerDatabase.UrlOptions.host
			));
			serverStatus = false;
			close = false;
			enableAdmin = true;
			numberOfflineServers++;
		}
		this.sendMessageStatus (MESSAGE_STATUS_EXPOLIS_MQTT, serverStatus);
	}

	private void checkExpolisRouteServer ()
	{
		boolean serverStatus;
		this.sendMessagePrint (R.string.checking_route_planner);
		serverStatus = pt.expolis.mobileapp.planner.OSRMClient.areServersAvailable ();
		if (!serverStatus) {
			this.sendMessagePrint (R.string.connection_route_planner_failed);
			enableAdmin = true;
			close = false;
			numberOfflineServers++;
		}
		this.sendMessageStatus (MESSAGE_STATUS_EXPOLIS_PLANNER, serverStatus);
	}

	private void finishInitialisation ()
	{
		if (numberOfflineServers == 3) {
			this.sendMessageAdvice (R.string.advice_all_servers_offline);
			this.readPause ();
		}
		else if (numberOfflineServers > 0) {
			String text = this.resources.getQuantityString (
			  R.plurals.advice_offline_servers,
			  numberOfflineServers
			);
			this.sendMessageAdvice (text);
			this.readPause ();
		}
		this.sendMessagePrint (R.string.welcome_expolis);
		this.sendMessage (close ? MESSAGE_CLOSE : MESSAGE_ENABLE_PROCEED);
		if (enableAdmin) {
			this.sendMessage (MESSAGE_ENABLE_ADMIN);
		}
	}

	synchronized private void sendMessage (int type)
	{
		Handler handler =
		  this.sendingToWelcomeScreen ?
		    this.welcomeScreenHandler :
		    this.onlineDataHandler;
		Message.obtain (handler, type)
		  .sendToTarget ();
	}

	synchronized private void sendMessageStatus (int type, boolean status)
	{
		Handler handler =
		  this.sendingToWelcomeScreen ?
			 this.welcomeScreenHandler :
			 this.onlineDataHandler;
		Message.obtain (handler, type, status ? 1 : 0, -1)
		  .sendToTarget ();
	}

	/**
	 * Send a message to the welcome screen handler to show a status message.
	 * @param info the index in the string resource file of the message to show.
	 */
	synchronized private void sendMessagePrint (int info)
	{
		if (this.sendingToWelcomeScreen) {
			Message.obtain (this.welcomeScreenHandler, MESSAGE_PRINT, info, -1)
			  .sendToTarget ();
			this.readPause ();
		}
	}

	/**
	 * Send a message to the welcome screen handler to show a status message.
	 * @param msg the message to show.
	 */
	synchronized private void sendMessagePrint (String msg)
	{
		if (this.sendingToWelcomeScreen) {
			Message.obtain (this.welcomeScreenHandler, MESSAGE_PRINT, msg)
			  .sendToTarget ();
			this.readPause ();
		}
	}

	/**
	 * Send a message to the welcome screen handler to show a user advice message.
	 * @param info the index in the string resource file of the message to show.
	 */
	private void sendMessageAdvice (int info)
	{
		if (this.sendingToWelcomeScreen) {
			Message.obtain (this.welcomeScreenHandler, MESSAGE_ADVICE, info, -1)
			  .sendToTarget ();
		}
	}


	/**
	 * Send a message to the welcome screen handler to show a user advice message.
	 * @param msg the message to show.
	 */
	private void sendMessageAdvice (String msg)
	{
		if (this.sendingToWelcomeScreen) {
			Message.obtain (this.welcomeScreenHandler, MESSAGE_ADVICE, msg)
			  .sendToTarget ();
		}
	}

	/**
	 * Pause the welcome screen background thread for a few milliseconds so that the user can
	 * read the status message.
	 */
	private void readPause ()
	{
		try {
			Thread.sleep (100);
		}
		catch (InterruptedException ignored) {}
	}

}
