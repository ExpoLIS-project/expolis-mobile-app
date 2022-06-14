package pt.expolis.mobileapp.plots.map;

import android.content.SharedPreferences;
import android.content.res.Resources;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;

import pt.expolis.mobileapp.ExpolisPreferences;
import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Cache;
import pt.expolis.mobileapp.database.Data;
import pt.expolis.mobileapp.database.GraphsSqlDao;
import pt.expolis.mobileapp.database.Line;
import pt.expolis.mobileapp.database.MapDataCell;
import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.database.Statistics;
import pt.expolis.mobileapp.plots.CellSizeViewModel;
import pt.expolis.mobileapp.plots.DataViewModel;
import pt.expolis.mobileapp.plots.LineIdViewModel;
import pt.expolis.mobileapp.plots.StatisticsViewModel;
import pt.expolis.mobileapp.plots.TimePeriodViewModel;
import utils.GeographyUtils;
import utils.MathUtils;

/**
 * The view model that holds the properties of the map plot.  This plots shows sensor data as a
 * grid lay on a map.
 *
 * <p>The user can select which sensor data to display, how it is aggregated, the time period,
 * if it wants to display interpolated data or use the raw data, and in the later case if it
 * wants to filter to show only on a single bus line.</p>

 * <p>These properties are stored in the app private key-value pairs file.</p>
 */
public class MapPlotViewModel
  implements
    StatisticsViewModel,
	 DataViewModel,
    TimePeriodViewModel,
	 CellSizeViewModel,
	 LineIdViewModel
{
	private Statistics statistics;
	private Data data;
	private long fromDate;
	private long toDate;
	private float cellSizeInRadians;
	private float cellSizeInMeters;
	public boolean useRawData;
	private boolean filterLine;
	private int lineId;

	/**
	 * Construct an instance of this class from the data stored in the app private key-value pairs
	 * file.
	 * @param preferences the app private key-value pairs file.
	 */
	public MapPlotViewModel (SharedPreferences preferences)
	{
		this.statistics = Statistics.values ()[MathUtils.range (
		  preferences.getInt (ExpolisPreferences.KEY_MAP_PLOT_STATISTICS, 0),
		  0,
		  Statistics.values ().length - 1)];
		this.data = Data.values ()[MathUtils.range (
		  preferences.getInt (ExpolisPreferences.KEY_MAP_PLOT_DATA, 0),
		  0,
		  Data.values ().length - 1)];
		if (Cache.maxMeasurementDate > 0) {
			this.fromDate = Math.max (
			  Cache.minMeasurementDate,
			  preferences.getLong (
				 ExpolisPreferences.KEY_MAP_PLOT_FROM_DATE,
				 Cache.minMeasurementDate));
			this.toDate = Math.min (
			  Cache.maxMeasurementDate,
			  preferences.getLong (
				 ExpolisPreferences.KEY_MAP_PLOT_TO_DATE,
				 Cache.maxMeasurementDate));
		}
		else {
			long t = System.currentTimeMillis ();
			this.fromDate = preferences.getLong (ExpolisPreferences.KEY_MAP_PLOT_FROM_DATE, t);
			this.toDate = preferences.getLong (ExpolisPreferences.KEY_MAP_PLOT_TO_DATE, t);
		}
		this.useRawData = preferences.getBoolean (ExpolisPreferences.KEY_MAP_PLOT_USE_RAW_DATA, true);
		this.filterLine = preferences.getBoolean (ExpolisPreferences.KEY_MAP_PLOT_FILTER_LINE, false);
		this.lineId = preferences.getInt (
		  ExpolisPreferences.KEY_MAP_PLOT_LINE_ID,
		  Cache.busLines.size () > 0 ? Cache.busLines.first ().id : -1);
		this.cellSizeInMeters = preferences.getFloat (ExpolisPreferences.KEY_MAP_PLOT_DISTANCE, 1000);
		this.cellSizeInRadians = GeographyUtils.metersToDegree (this.cellSizeInMeters);
	}

	/**
	 * Save the properties of the map plot to the app private key-value pairs file.
	 * @param preferences the app private key-value pairs file.
	 */
	public void save (SharedPreferences preferences)
	{
		SharedPreferences.Editor editor = preferences.edit ();
		editor.putInt (ExpolisPreferences.KEY_MAP_PLOT_STATISTICS, this.statistics.ordinal ());
		editor.putInt (ExpolisPreferences.KEY_MAP_PLOT_DATA, this.data.ordinal ());
		editor.putLong (ExpolisPreferences.KEY_MAP_PLOT_FROM_DATE, this.fromDate);
		editor.putLong (ExpolisPreferences.KEY_MAP_PLOT_TO_DATE, this.toDate);
		editor.putBoolean (ExpolisPreferences.KEY_MAP_PLOT_USE_RAW_DATA, this.useRawData);
		editor.putBoolean (ExpolisPreferences.KEY_MAP_PLOT_FILTER_LINE, this.filterLine);
		editor.putInt (ExpolisPreferences.KEY_MAP_PLOT_LINE_ID, this.lineId);
		editor.putFloat (ExpolisPreferences.KEY_MAP_PLOT_DISTANCE, this.cellSizeInMeters);
		editor.apply ();
	}

	@Override
	public Statistics getStatistics ()
	{
		return this.statistics;
	}

	@Override
	public void setStatistics (Statistics st)
	{
		this.statistics = st;
	}

	@Override
	public Data getData ()
	{
		return this.data;
	}

	@Override
	public void setData (Data dt)
	{
		this.data = dt;
	}

	public long getFromDate ()
	{
		return this.fromDate;
	}

	@Override
	public void setFromDate (long fromDate)
	{
		this.fromDate = fromDate;
	}

	public long getToDate ()
	{
		return this.toDate;
	}

	@Override
	public void setToDate (long toDate)
	{
		this.toDate = toDate;
	}

	@Override
	public float getCellRadius ()
	{
		return this.cellSizeInMeters;
	}

	@Override
	public void setCellRadius (float cellSize)
	{
		this.cellSizeInMeters = cellSize;
		this.cellSizeInRadians = GeographyUtils.metersToDegree (this.cellSizeInMeters);
	}

	private static final double CELL_SIZE_INTERPOLATED_MAP_PLOT = 0.000557031249997;

	double getCellSizeForMap ()
	{
		return this.useRawData ? this.cellSizeInRadians : CELL_SIZE_INTERPOLATED_MAP_PLOT;
	}

	public void setFilterLine (boolean value)
	{
		this.filterLine = Cache.busLines.size () > 0 && value;
	}

	public boolean isFilterLine ()
	{
		return this.filterLine;
	}

	@Override
	public int getLineId ()
	{
		return this.lineId;
	}

	@Override
	public void setLineId (int value)
	{
		this.lineId = value;
	}

	String getProperties (Resources resources)
	{
		DateFormat df = DateFormat.getDateInstance ();
		Date _fromDate = new Date (this.fromDate);
		Date _toDate = new Date (this.toDate);
		if (this.useRawData) {
			if (this.filterLine) {
				String ls = "";
				for (Line l : Cache.busLines) {
					if (l.id == this.lineId) {
						ls = l.description;
						break;
					}
				}
				return resources.getString (
				  R.string.map_plot_properties_line_data,
				  resources.getString (this.statistics.resourceId),
				  resources.getString (this.data.resourceId),
				  ls,
				  Math.round (this.cellSizeInMeters),
				  df.format (_fromDate),
				  df.format (_toDate)
				);
			} else {
				return resources.getString (
				  R.string.map_plot_properties_raw_data,
				  resources.getString (this.statistics.resourceId),
				  resources.getString (this.data.resourceId),
				  this.cellSizeInMeters,
				  df.format (_fromDate),
				  df.format (_toDate)
				);
			}
		} else {
			return resources.getString (
			  R.string.map_plot_properties_interpolated_data,
			  resources.getString (this.statistics.resourceId),
			  resources.getString (this.data.resourceId),
			  Math.round (GeographyUtils.degreeToMeters (CELL_SIZE_INTERPOLATED_MAP_PLOT)),
			  df.format (_fromDate),
			  df.format (_toDate)
			);
		}
	}

	public LinkedList<MapDataCell> queryDatabase ()
	{
		if (this.useRawData) {
			if (this.filterLine) {
				return this.queryDatabaseLine ();
			} else {
				return this.queryDatabaseRaw ();
			}
		} else {
			return this.queryDatabaseInterpolated ();
		}
	}

	private LinkedList<MapDataCell> queryDatabaseInterpolated ()
	{
		GraphsSqlDao dao = PostGisServerDatabase.instance.graphsDAO ();
		switch (this.data) {
			case CO:
				return dao.getCo_1ForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
			case NO2:
				return dao.getNo2_1ForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
			case PM1f:
				return dao.getPm1fForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
			case PM25f:
				return dao.getPm25fForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
			case PM10f:
				return dao.getPm10fForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
			case TEMPERATURE:
				return dao.getTemperatureForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
			case PRESSURE:
				return dao.getPressureForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
			case HUMIDITY:
				return dao.getHumidityForInterpolatedDataMap (new Timestamp (this.fromDate), new Timestamp (this.toDate));
		}
		return null;
	}

	/**
	 * Queries the database using the parameters of this view model.
	 * Returns a list of the map data cells.
	 * These data can be shown in a graph.
	 */
	private LinkedList<MapDataCell> queryDatabaseLine ()
	{
		GraphsSqlDao dao = PostGisServerDatabase.instance.graphsDAO ();
		switch (this.data) {
			case CO:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageCo_1RawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumCo_1RawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumCo_1RawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case NO2:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageNo2_1RawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumNo2_1RawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumNo2_1RawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM1f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm1fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm1fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm1fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM25f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm25fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm25fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm25fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM10f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm10fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm10fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm10fRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case TEMPERATURE:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageTemperatureRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumTemperatureRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumTemperatureRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PRESSURE:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePressureRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPressureRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPressureRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case HUMIDITY:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageHumidityRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumHumidityRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumHumidityRawForGraphLineData (this.lineId, this.cellSizeInMeters, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
		}
		return null;
	}

	private LinkedList<MapDataCell> queryDatabaseRaw ()
	{
		GraphsSqlDao dao = PostGisServerDatabase.instance.graphsDAO ();

		switch (this.data) {
			case CO:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageCo_1RawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumCo_1RawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumCo_1RawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case NO2:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageNo2_1RawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumNo2_1RawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumNo2_1RawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM1f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm1fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm1fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm1fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM25f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm25fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm25fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm25fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PM10f:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePm10fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPm10fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPm10fRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case TEMPERATURE:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageTemperatureRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumTemperatureRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumTemperatureRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case PRESSURE:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAveragePressureRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumPressureRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumPressureRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
			case HUMIDITY:
				switch (this.statistics) {
					case AVERAGE:
						return dao.getAverageHumidityRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MAXIMUM:
						return dao.getMaximumHumidityRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
					case MINIMUM:
						return dao.getMinimumHumidityRawForGraphMapData (this.cellSizeInRadians, new Timestamp (this.fromDate), new Timestamp (this.toDate));
				}
		}
		return null;
	}
}
