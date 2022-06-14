package pt.expolis.mobileapp.plots.chart;

import android.content.SharedPreferences;
import android.content.res.Resources;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;

import pt.expolis.mobileapp.ExpolisPreferences;
import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Cache;
import pt.expolis.mobileapp.database.Data;
import pt.expolis.mobileapp.database.GraphsSqlDao;
import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.database.Statistics;
import pt.expolis.mobileapp.database.ValueTime;
import pt.expolis.mobileapp.plots.DataViewModel;
import pt.expolis.mobileapp.plots.StatisticsViewModel;
import pt.expolis.mobileapp.plots.TimePeriodViewModel;
import utils.GeographyUtils;
import utils.MathUtils;
import utils.TimeUtils;

/**
 * The view model that holds the properties of the chart plot.
 *
 * <p>The user can select which sensor data to shown on the chart, how it is aggregated, the time
 * period, and the circular region that is to be analysed (geographical coordinates and radius).</p>
 *
 * <p>These properties are stored in the app private key-value pairs file.</p>
 */
public class ChartPlotViewModel
  implements
    StatisticsViewModel,
	 DataViewModel,
    TimePeriodViewModel
{
	private Statistics statistics;
	private Data data;
	private long fromDate;
	private long toDate;
	private double locationLatitude;
	private double locationLongitude;
	private float cellRadius;
	private double offset;

	/**
	 * Construct an instance of this class from the data stored in the app private key-value pairs
	 * file.
	 * @param preferences the app private key-value pairs file.
	 */
	public ChartPlotViewModel (SharedPreferences preferences)
	{
		this.statistics = Statistics.values ()[MathUtils.range (
		  preferences.getInt (
			 ExpolisPreferences.KEY_CHART_PLOT_STATISTICS,
			 0),
		  0,
		  Statistics.values ().length - 1)];
		this.data = Data.values ()[MathUtils.range (
		  preferences.getInt (
			 ExpolisPreferences.KEY_CHART_PLOT_DATA,
			 0),
		  0,
		  Data.values ().length - 1)];
		if (Cache.maxMeasurementDate > 0) {
			this.fromDate = Math.max (
			  Cache.minMeasurementDate,
			  preferences.getLong (
				 ExpolisPreferences.KEY_CHART_PLOT_FROM_DATE,
				 Cache.minMeasurementDate));
			this.toDate = Math.min (
			  Cache.maxMeasurementDate,
			  preferences.getLong (
				 ExpolisPreferences.KEY_CHART_PLOT_TO_DATE,
				 Cache.maxMeasurementDate));
		}
		else {
			long t = System.currentTimeMillis ();
			this.fromDate = preferences.getLong (ExpolisPreferences.KEY_CHART_PLOT_FROM_DATE, t);
			this.toDate = preferences.getLong (ExpolisPreferences.KEY_CHART_PLOT_TO_DATE, t);
		}
		this.locationLatitude = Double.parseDouble (preferences.getString (
		  ExpolisPreferences.KEY_CHART_PLOT_LOCATION_LATITUDE,
		  "38.7431402"));
		this.locationLongitude = Double.parseDouble (preferences.getString (
		  ExpolisPreferences.KEY_CHART_PLOT_LOCATION_LONGITUDE,
		  "-9.1566963"));
		this.cellRadius = preferences.getFloat (
		  ExpolisPreferences.KEY_CHART_PLOT_LOCATION_RADIUS,
		  1 / 60f);
		this.offset = -1;
	}

	/**
	 * Save the properties of the map plot to the app private key-value pairs file.
	 * @param preferences the app private key-value pairs file.
	 */
	public void save (SharedPreferences preferences)
	{
		SharedPreferences.Editor editor = preferences.edit ();
		editor.putInt (ExpolisPreferences.KEY_CHART_PLOT_STATISTICS, this.statistics.ordinal ());
		editor.putInt (ExpolisPreferences.KEY_CHART_PLOT_DATA, this.data.ordinal ());
		editor.putLong (ExpolisPreferences.KEY_CHART_PLOT_FROM_DATE, this.fromDate);
		editor.putLong (ExpolisPreferences.KEY_CHART_PLOT_TO_DATE, this.toDate);
		editor.putString (
		  ExpolisPreferences.KEY_CHART_PLOT_LOCATION_LONGITUDE,
		  Double.toString (this.locationLongitude)
		);
		editor.putString (
		  ExpolisPreferences.KEY_CHART_PLOT_LOCATION_LATITUDE,
		  Double.toString (this.locationLatitude));
		editor.putFloat (ExpolisPreferences.KEY_CHART_PLOT_LOCATION_RADIUS, this.cellRadius);
		editor.apply ();
	}

	@Override
	public Statistics getStatistics ()
	{
		return this.statistics;
	}

	@Override
	public void setStatistics (Statistics value)
	{
		this.statistics = value;
	}

	@Override
	public Data getData ()
	{
		return this.data;
	}

	@Override
	public void setData (Data value)
	{
		this.data = value;
	}

	@Override
	public long getFromDate ()
	{
		return this.fromDate;
	}

	@Override
	public void setFromDate (long value)
	{
		this.fromDate = value;
	}

	@Override
	public long getToDate ()
	{
		return this.toDate;
	}

	@Override
	public void setToDate (long value)
	{
		this.toDate = value;
	}

	double getLocationLatitude ()
	{
		return this.locationLatitude;
	}

	void setLocationLatitude (double value)
	{
		this.locationLatitude = value;
	}

	double getLocationLongitude ()
	{
		return this.locationLongitude;
	}

	void setLocationLongitude (double value)
	{
		this.locationLongitude = value;
	}

	/**
	 * Sets the cell radius of this graph view model.
	 * The graph data is updated.
	 *
	 * @param value the new cell radius.
	 */
	void setCellRadius (float value)
	{
		this.cellRadius = value;
	}

	double getOffset ()
	{
		return this.offset;
	}

	void setOffset (double offset)
	{
		this.offset = offset;
	}

	/**
	 * Return a user friendly description of the properties that the user has selected, in order to
	 * be shown on the view chart plot fragment.
	 *
	 * @param resources the resources to obtain the strings.
	 * @return a user friendly description of the properties that the user has selected.
	 */
	String getProperties (Resources resources)
	{
		DateFormat df = DateFormat.getDateInstance ();
		Date _fromDate = new Date (this.fromDate);
		Date _toDate = new Date (this.toDate);
		return resources.getString (
		  R.string.chart_plot_properties,
		  resources.getString (this.statistics.resourceId),
		  resources.getString (this.data.resourceId),
		  GeographyUtils.longitudeToString (this.locationLongitude),
		  GeographyUtils.latitudeToString (this.locationLatitude),
		  Math.round (this.cellRadius),
		  df.format (_fromDate),
		  df.format (_toDate)
		);
	}

	/**
	 * Computes the cell data with the properties of this model.
	 * <p>This computation is done by the current thread. Callers should set up a task to perform
	 * the database query as the main thread is responsible for handling UI events</p>
	 *
	 * @return a list with the cell data to be shown.
	 */
	public LinkedList<ValueTime> queryDatabase ()
	{
		GraphsSqlDao dao = PostGisServerDatabase.instance.graphsDAO ();

		switch (this.data) {
			case CO:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageCo_1RawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumCo_1RawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumCo_1RawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case NO2:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageNo2_1RawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumNo2_1RawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumNo2_1RawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM1f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm1fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm1fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm1fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM25f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm25fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm25fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm25fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM10f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm10fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm10fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm10fRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case TEMPERATURE:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageTemperatureRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumTemperatureRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumTemperatureRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PRESSURE:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePressureRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPressureRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPressureRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case HUMIDITY:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageHumidityRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumHumidityRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumHumidityRawForGraphCellData (this.locationLongitude, this.locationLatitude, this.cellRadius, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
		}
		return null;
	}

	@NotNull
	@Override
	public String toString ()
	{
		return String.format (
		  "%s of %s from %s to %s position %s %s size %sm",
		  this.statistics.toString (),
		  this.data.toString (),
		  TimeUtils.toString (this.fromDate / 1000.0),
		  TimeUtils.toString (this.toDate / 1000.0),
		  GeographyUtils.latitudeToString (this.locationLatitude),
		  GeographyUtils.longitudeToString (this.locationLongitude),
		  this.cellRadius
		);
	}
}