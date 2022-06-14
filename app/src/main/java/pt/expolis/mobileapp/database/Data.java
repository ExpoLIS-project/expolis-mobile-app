package pt.expolis.mobileapp.database;

import org.jetbrains.annotations.NotNull;

import pt.expolis.mobileapp.R;

/**
 * Which data is shown in a graph.
 *
 * <p>This enum contains information about sensor data collected by sensor nodes.  The user can
 * view this data in either a map or a plot graph.  This data is also displayed in the online
 * data screen.</p>
 *
 * <p>This enum contains information about:
 * <ul>
 *    <li>sensor value range, which is used in the graph keys;</li>
 *    <li>sensor value units, which is used in the plot axis;</li>
 *    <li>XML resource that contains a user readable description, used in the online data screen
 *    and graphs;</li>
 *    <li>a description used in debugging instructions.</li>
 * </ul>
 * </p>
 *
 * @see pt.expolis.mobileapp.online.OnlineDataPlotView
 * @see pt.expolis.mobileapp.plots.chart.ViewChartPlotFragment
 * @see pt.expolis.mobileapp.plots.map.ViewMapPlotFragment
 */
public enum Data
{
	CO ("CO", 0, 50, R.string.data_co, "µg/㎥"),
	NO2 ("NO2", 0, 60, R.string.data_no2, "µg/㎥"),
	PM1f ("PM 1", 0, 100, R.string.data_pm1, "µg/㎥"),
	PM25f ("PM 2.5", 0, 40, R.string.data_pm2_5, "µg/㎥"),
	PM10f ("PM 10", 0, 60, R.string.data_pm10, "µg/㎥"),
	TEMPERATURE ("Temperature", -10, 50, R.string.data_temperature, "℃"),
	PRESSURE ("Pressure", 0, 2, R.string.data_pressure, "Pa"),
	HUMIDITY ("Humidity", 0, 100, R.string.data_humidity, "%"),
	;

	final String description;
	/**
	 * Minimum value shown in a graph.
	 */
	final public float minValue;
	/**
	 * Maximum value shown in a graph.
	 */
	final public float maxValue;
	/**
	 * XML resource identification of the string that contains a localised user readable description.
	 */
	final public int resourceId;
	/**
	 * Sensor data SI units.
	 */
	final public String units;

	Data (String description, float minValue, float maxValue, int resourceId, String units)
	{
		this.description = description;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.resourceId = resourceId;
		this.units = units;
	}

	@NotNull
	public String toString ()
	{
		return description;
	}
}
