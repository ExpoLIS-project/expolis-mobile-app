package pt.expolis.mobileapp.online;

/**
 * Identifications of messages sent by the MQTT callbacks.
 *
 * Communication between the thread that processes MQTT callbacks and UI thread are done using
 * the {@code android.os.Message} class.
 *
 * @see android.os.Message
 */
class Message
{
	static final int NEW_SENSOR_DATA = 981;
	static final int LOST_CONNECTION = 101;
	static final int UNRECOGNIZED_DATA = 654;
}
