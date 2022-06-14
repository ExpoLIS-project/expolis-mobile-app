package pt.expolis.mobileapp.database;

import pt.expolis.mobileapp.R;

/**
 * Which statistics are shown in a graph.
 *
 * <p>This enum corresponds to the functions used to aggregate sensor data.  The sensor data
 * database server aggregates data both temporarily and spatially.</p>
 */
public enum Statistics
{
	AVERAGE (R.string.statistics_average),
	MINIMUM (R.string.statistics_minimum),
	MAXIMUM (R.string.statistics_maximum),
	;

	/**
	 * XML resource identification of the string that contains a localised user readable description.
	 */
	public final int resourceId;

	Statistics (int resourceId)
	{
		this.resourceId = resourceId;
	}
}
