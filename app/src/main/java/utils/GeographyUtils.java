package utils;

/**
 * A class with methods to handle geography positions.
 */
public class GeographyUtils
{
	/**
	 * Convert a latitude value to a string.
	 * The returned string has the given format {@code DD° MM′ SS″}.
	 * @param latitude a value between -90 and 90.
	 * @return a string representation.
	 */
	public static String latitudeToString (double latitude)
	{
		return degreeToString (latitude, 'S','N');
	}

	/**
	 * Convert a longitude value to a string.
	 * The returned string has the given format {@code DD° MM′ SS″}.
	 * @param longitude a value between -180 and 180.
	 * @return a string representation.
	 */
	public static String longitudeToString (double longitude)
	{
		return degreeToString (longitude, 'W', 'E');
	}

	private static String degreeToString (double value, char negative, char positive)
	{
		int seconds = (int) Math.abs (Math.round (value * 3600));
		int minutes = (seconds / 60) % 60;
		int degrees = seconds / 3600;
		seconds = seconds % 60;
		char side = value > 0 ? positive : negative;
		return degrees + "° " + minutes + "′ " + seconds + "″ " + side;
	}

	/**
	 * Convert a distance on the surface of the planet to degrees.
	 *
	 * <p>The distance is computed using one of several conversion factors.  Each factor was
	 * computed by calculating two geodesic along an origin point. The latitude and longitude
	 * difference between the origin point and the projected point was computed.  The minimum
	 * angle was used as the factor.</p>
	 *
	 * <p>The origin point corresponds to the average latitude and longitude of all points in the
	 * expolis sensor database.  The two geodesic used where the meridian and the circle of
	 * latitude that pass through this point.</p>
	 *
	 * <p>We computed factors using points projected with the given distances: 1, 10, 100, 1000,
	 * 10000.</p>
	 *
	 * @param distance the distance to convert to degrees.
	 * @return the degrees corresponding to the distance.
	 */
	static public float metersToDegree (float distance)
	{
		for (int i = 0; i < CONVERSION_METER_DEGREE_FACTOR.length; i++) {
			if (distance < CONVERSION_METER_DEGREE_THRESHOLD [i])
				return distance * CONVERSION_METER_DEGREE_FACTOR [i] / CONVERSION_METER_DEGREE_THRESHOLD [i];
		}
		return Float.NaN;
	}

	static public double degreeToMeters (double degree)
	{
		return degree * CONVERSION_METER_DEGREE_THRESHOLD [0] / CONVERSION_METER_DEGREE_FACTOR [0];
	}

	/**
	 * Conversion factors used to convert a distance on the surface of the planet to degrees.
	 *
	 * <p>Each factor corresponds to a displacement on the surface.  They were computed using
	 * <em>postgis</em> function {@code ST_PROJECT}.  This function returns a point projected from a
	 * start point along a geodesic using a given distance and azimuth (bearing). This is known as
	 * the direct geodesic problem.</p>
	 *
	 * @see <a href="https://postgis.net/docs/ST_Project.html">ST_PROJECT documentation</a>
	 * @see #metersToDegree(float)
	 */
	static final private float[] CONVERSION_METER_DEGREE_FACTOR = new float[] {
	  9.010032847811544e-06f,
	  9.010032799494638e-05f,
	  0.000901003218018559f,
	  0.009010025979307557f,
	  0.09009963953399591f,
	};
	/**
	 * Distance thresholds used when converting a distance on the surface of the planet to degrees.
	 *
	 * @see #metersToDegree(float)
	 */
	static final private float[] CONVERSION_METER_DEGREE_THRESHOLD = new float[]{
	  5,
	  50,
	  500,
	  5000,
	  Float.MAX_VALUE
	};
}
