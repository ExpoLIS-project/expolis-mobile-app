package pt.expolis.mobileapp.plots;

public class Options
{
	/**
	 * Extra radians used when zooming to span the entire data points.
	 */
	public final static double DATA_MAP_MARGIN = 0; //0.000557031249997;

	public static Focus focus = Focus.DATA;

	public enum Focus
	{
		LISBON,
		ZURICH,
		DATA,
		DATA_LISBON,
	}
}
