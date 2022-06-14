package pt.expolis.mobileapp;

import android.content.SharedPreferences;

/**
 * This class contains the keys that are used in the app private key-value pairs file.
 *
 * <p>The data that is saved in this file are: the properties of the map and chart plots.</p>
 */
@SuppressWarnings("SpellCheckingInspection")
public class ExpolisPreferences
{
	public static final String KEY_MAP_PLOT_STATISTICS = "ymjUf8wX4V";
	public static final String KEY_MAP_PLOT_DATA = "fMmYApVr9W";
	public static final String KEY_MAP_PLOT_FROM_DATE = "0F5NPYC74n";
	public static final String KEY_MAP_PLOT_TO_DATE = "SAt0MLBbcP";
	public static final String KEY_MAP_PLOT_USE_RAW_DATA = "hfjshbUH8v";
	public static final String KEY_MAP_PLOT_FILTER_LINE = "Mf2Fv3aEMx";
	public static final String KEY_MAP_PLOT_LINE_ID = "Gi8nqE403u";
	public static final String KEY_MAP_PLOT_DISTANCE = "kw3S9M9dtG";

	public static final String KEY_CHART_PLOT_STATISTICS = "1MfRKa45sZ";
	public static final String KEY_CHART_PLOT_DATA = "ov61exKyxM";
	public static final String KEY_CHART_PLOT_FROM_DATE = "zU6PkAGGy2";
	public static final String KEY_CHART_PLOT_TO_DATE = "fyarvenH7r";
	public static final String KEY_CHART_PLOT_LOCATION_LATITUDE = "KQGZhmqBIw";
	public static final String KEY_CHART_PLOT_LOCATION_LONGITUDE = "DEuqBgrB3Q";
	public static final String KEY_CHART_PLOT_LOCATION_RADIUS = "mEds9PavoR";

	public static final String KEY_DATABASE_URL_HOST = "lndaUISQPB";
	public static final String KEY_DATABASE_URL_PORT = "1nxaUISQPB";

	public static final String KEY_MQTT_URL_HOST = "E6Gk8uthOT";
	public static final String KEY_MQTT_URL_PORT = "7dp6TStgus";
	public static final String KEY_MQTT_USE_SENSOR_NODE = "xlkjfdslk";

	public static final String KEY_PLANNER_START_LOCATION_LATITUDE = "a8iseQxt5B";
	public static final String KEY_PLANNER_START_LOCATION_LONGITUDE = "PTNWWowC7q";
	public static final String KEY_PLANNER_END_LOCATION_LATITUDE = "2hrkUYzuyZ";
	public static final String KEY_PLANNER_END_LOCATION_LONGITUDE = "y1vonjT8xG";
	public static final String KEY_PLANNER_AVOID_POLLUTION = "5pap41";
	public static final String KEY_PLANNER_ROUTE_MEDIUM = "5prm42";
	public static final String KEY_PLANNER_HOST = "fN47oaEaxf";

	public static void debug (SharedPreferences p)
	{
		System.out.println (KEY_DATABASE_URL_HOST + " => " + p.getString (KEY_DATABASE_URL_HOST,
		  "NA"));
		System.out.println (KEY_MQTT_URL_HOST + " => " + p.getString (KEY_MQTT_URL_HOST, "NA"));
		System.out.println (KEY_MQTT_URL_PORT + " => " + p.getInt (KEY_MQTT_URL_PORT, -1));
	}
}
