package pt.expolis.mobileapp.planner;

import android.content.SharedPreferences;

import pt.expolis.mobileapp.ExpolisPreferences;

/**
 * Options used by the planner classes.
 */
public class Options
{
	/**
	 * The default host where the ExpoLIS open service routing machine is running.
	 *
	 * <p>In this host there can be a set of OSRM profiles, each one listening in a specific port.</p>
	 */
	private static final String DEFAULT_HOST = "router.expolis.pt";
	/**
	 * The host where the ExpoLIS open service routing machine is running.
	 *
	 * <p>In this host there can be a set of OSRM profiles, each one listening in a specific port.</p>
	 */
	public static String host = DEFAULT_HOST;
	/**
	 * The port where the ExpoLIS open service routing machine is listening.
	 */
	public static final int PROFILE_PORT = 50001;
	/**
	 * Threshold used by the planner fragment when computing the nearest road to a marker.
	 * If a road is found, then its name is displayed on the corresponding text view, otherwise
	 * the geographical coordinates are displayed.
	 */
	static final double THRESHOLD_NEAREST_ROAD = 100;

	/**
	 * Load the planner options from the app private key-value pairs file.
	 * @param preferences the app key-value pairs.
	 */
	public static void load (SharedPreferences preferences)
	{
		Options.host = preferences.getString (
		  ExpolisPreferences.KEY_PLANNER_HOST,
		  Options.DEFAULT_HOST
		);
	}

	/**
	 * Save the planner options to the app private key-value pairs file.
	 * @param preferences the app key-value pairs.
	 */
	public static void save (SharedPreferences preferences)
	{
		SharedPreferences.Editor editor = preferences.edit ();
		editor.putString (
		  ExpolisPreferences.KEY_PLANNER_HOST,
		  Options.host);
		editor.apply ();
	}
}
