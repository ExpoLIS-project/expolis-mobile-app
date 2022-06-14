package pt.expolis.mobileapp.planner;

import android.content.SharedPreferences;

import org.osmdroid.util.GeoPoint;

import pt.expolis.mobileapp.ExpolisPreferences;

public class PlannerViewModel
{
	final GeoPoint startLocation;
	final GeoPoint endLocation;
	boolean avoidPollution;
	RouteMedium routeMedium;

	public PlannerViewModel (SharedPreferences preferences)
	{
		this.startLocation = new GeoPoint (
		  preferences.getFloat (
		    ExpolisPreferences.KEY_PLANNER_START_LOCATION_LATITUDE,
		    38.7477861f),
		  preferences.getFloat (
			 ExpolisPreferences.KEY_PLANNER_START_LOCATION_LONGITUDE,
		    -9.1489615f)
		);
		this.endLocation = new GeoPoint (
		  preferences.getFloat (
			 ExpolisPreferences.KEY_PLANNER_END_LOCATION_LATITUDE,
		    38.8043751f),
		  preferences.getFloat (
			 ExpolisPreferences.KEY_PLANNER_END_LOCATION_LONGITUDE,
		    -9.0922689f)
		);
		this.avoidPollution = preferences.getBoolean (
		  ExpolisPreferences.KEY_PLANNER_AVOID_POLLUTION,
		  true
		);
		this.routeMedium = RouteMedium.values () [preferences.getInt (
		  ExpolisPreferences.KEY_PLANNER_ROUTE_MEDIUM,
		  0
		)];
	}

	public void save (SharedPreferences preferences)
	{
		SharedPreferences.Editor editor = preferences.edit ();
		editor.putFloat (
		  ExpolisPreferences.KEY_PLANNER_START_LOCATION_LATITUDE,
		  (float) this.startLocation.getLatitude ());
		editor.putFloat (
		  ExpolisPreferences.KEY_PLANNER_START_LOCATION_LONGITUDE,
		  (float) this.startLocation.getLongitude ());
		editor.putFloat (
		  ExpolisPreferences.KEY_PLANNER_END_LOCATION_LATITUDE,
		  (float) this.endLocation.getLatitude ());
		editor.putFloat (
		  ExpolisPreferences.KEY_PLANNER_END_LOCATION_LONGITUDE,
		  (float) this.endLocation.getLongitude ());
		editor.putBoolean (
		  ExpolisPreferences.KEY_PLANNER_AVOID_POLLUTION,
		  this.avoidPollution
		);
		editor.putInt (
		  ExpolisPreferences.KEY_PLANNER_ROUTE_MEDIUM,
		  this.routeMedium.ordinal ()
		);
		editor.apply ();
	}
}
