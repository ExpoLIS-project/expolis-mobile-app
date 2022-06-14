package pt.expolis.mobileapp.planner;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.osmdroid.util.GeoPoint;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * This class contains methods to query the Open Service Routing Machine running on the ExpoLIS
 * server.
 */
public class OSRMClient
{
	public static boolean serversAvailable = false;
	/**
	 * Get the name of the nearest road to the given location within the given distance threshold.
	 * If there is no road within the given distance, the function returns {@code null}.
	 * @param location the location to search for a road.
	 * @param threshold the maximum distance to look for a road from the given location.
	 * @return the name of the nearest road.
	 * @throws OSRMException if there is an error while connecting to the ExpoLIS OSRM service.
	 */
	static String getNearestRoad (@NotNull GeoPoint location, double threshold)
	  throws OSRMException
	{
		String spec = String.format (
		  Locale.UK,
		  "http://%s:%d/nearest/v1/foot/%f,%f.json?number=1",
		  Options.host,
		  Options.PROFILE_PORT,
		  location.getLongitude (),
		  location.getLatitude ()
		);
		JSONObject response = OSRMClient.downloadURL (spec);
		String result = null;
		try {
			JSONArray wayPoints = response.getJSONArray ("waypoints");
			JSONObject nearestWayPoint = wayPoints.getJSONObject (0);
			double distance = nearestWayPoint.getDouble ("distance");
			if (distance <= threshold) {
				result = nearestWayPoint.getString ("name");
			}
		} catch (JSONException e) {
			// if we reach this point, then OSRM has changed the format of the response.
			return "--";
		}
		return result;
	}

	/**
	 * Checks if all the OSRM servers are running an listening in its ports.  Every route profile
	 * has a specific process running in the ExpoLIS route planner server.  They may be down in
	 * order to update the routing data.
	 *
	 * <p>This method is used in the welcome screen to warn the user that route feature may not
	 * be available.</p>
	 * @return {@code true} if all processes are running and listening for connections.
	 */
	public static boolean areServersAvailable ()
	{
		for (boolean avoidPollution: new boolean[]{false, true}) {
			for (RouteMedium routeMedium: RouteMedium.values ()) {
				try {
					String spec = String.format (
					  Locale.UK,
					  "http://%s:%d/",
					  Options.host,
					  OSRMClient.portProfile (avoidPollution, routeMedium)
					);
					URL url = new URL (spec);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection ();
					conn.setRequestMethod ("GET");
					try {
						conn.connect ();
					} catch (IOException ignored) {
						OSRMClient.serversAvailable = false;
						return false;
					}
					conn.disconnect ();
				}
				catch (IOException ignored) {
				}
			}
		}
		OSRMClient.serversAvailable = true;
		return true;
	}

	/**
	 * Compute the shortest route between the given locations.
	 *
	 * <p>We use the foot profile of te ExpoLIS OSRM service.</p>
	 *
	 * @param startLocation the route starting location.
	 * @param endLocation the route end location.
	 * @param avoidPollution whether to avoid pollution or not.
	 * @param routeMedium which medium is going to be used during the trip.
	 * @return a list of the location of the geographical locations that represent the route.
	 * @throws OSRMException if there is an error while connecting to the ExpoLIS OSRM service.
	 */
	static ArrayList<GeoPoint> getRoute (GeoPoint startLocation, GeoPoint endLocation, boolean avoidPollution, RouteMedium routeMedium)
	  throws OSRMException
	{
		String spec = String.format (
		  Locale.UK,
		  "http://%s:%d/route/v1/foot/%f,%f;%f,%f?" +
		    "alternatives=false" +
		    "&steps=true" +
		    "&geometries=geojson" +
		    "&overview=full" +
		    "&annotations=false",
		  Options.host,
		  OSRMClient.portProfile (avoidPollution, routeMedium),
		  startLocation.getLongitude (),
		  startLocation.getLatitude (),
		  endLocation.getLongitude (),
		  endLocation.getLatitude ()
		);
		JSONObject response = OSRMClient.downloadURL (spec);
		System.out.println (response);
		try {
			JSONArray routes = response.getJSONArray ("routes");
			JSONObject bestRoute = routes.getJSONObject (0);
			JSONObject geometry = bestRoute.getJSONObject ("geometry");
			JSONArray coordinates = geometry.getJSONArray ("coordinates");
			ArrayList<GeoPoint> result = new ArrayList<> (coordinates.length ());
			for (int i = 0; i < coordinates.length (); i++) {
				JSONArray c = coordinates.getJSONArray (i);
				result.add (new GeoPoint (c.getDouble (1), c.getDouble (0)));
			}
			return result;
		}
		catch (JSONException ignored) {
			// if we reach this point, then OSRM has changed the format of the response.
			return new ArrayList<> (0);
		}
	}

	/**
	 * Return port where the OSRM server whose profile matches the given parameters
	 * is listening.
	 *
	 * <p>The OSRM profiles depend on whether we want to avoid pollution or not, and on the route
	 * medium that is going to be used.
	 * </p>
	 * @param avoidPollution whether to avoid pollution or not.
	 * @param routeMedium which medium is going to be used during the trip.
	 * @return the port the OSRM server is listening.
	 */
	private static int portProfile (boolean avoidPollution, RouteMedium routeMedium)
	{
		return Options.PROFILE_PORT + (avoidPollution ? 300 : 100) + routeMedium.ordinal ();
	}
	/**
	 * Establish a connection to the ExpoLIS OSRM service and download the url object as a json
	 * object.
	 * @param spec the url of the object to download.
	 * @return a {@code JSONObject} representing the downloaded url.
	 * @throws OSRMException if there is an error while connecting to the ExpoLIS OSRM service.
	 */
	private static JSONObject downloadURL (String spec)
	  throws OSRMException
	{
		try {
			URL url = new URL (spec);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection ();
			conn.setRequestMethod ("GET");
			try {
				conn.connect ();
			} catch (IOException ignored) {
				throw new OSRMException (OSRMException.Type.SERVER_NOT_AVAILABLE);
			}
			// Getting the response code
			int responseCode = conn.getResponseCode ();
			if (responseCode != 200) {
				throw new OSRMException (OSRMException.Type.SERVER_NOT_AVAILABLE);
			} else {
				StringBuilder inline = new StringBuilder ();
				Scanner scanner = new Scanner (url.openStream ());
				// Write all the JSON data into a string using a scanner
				while (scanner.hasNext ()) {
					inline.append (scanner.nextLine ());
				}
				// Close the scanner
				scanner.close ();
				//Using the JSON simple library parse the string into a json object
				return new JSONObject (inline.toString ());
			}
		} catch (MalformedURLException | ProtocolException ignored) {
			throw new Error ("Not reached");
		} catch (IOException ignored) {
			throw new OSRMException (OSRMException.Type.IO_ERROR);
		} catch (JSONException ignored) {
			throw new OSRMException (OSRMException.Type.INVALID_RESPONSE);
		}
	}

}
