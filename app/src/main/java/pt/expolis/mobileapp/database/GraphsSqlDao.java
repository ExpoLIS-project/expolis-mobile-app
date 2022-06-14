package pt.expolis.mobileapp.database;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class GraphsSqlDao
{
	private final PreparedStatement psMapDataRawAverageCO;
	private final PreparedStatement psMapDataRawAverageNO2;
	private final PreparedStatement psMapDataRawAveragePM1f;
	private final PreparedStatement psMapDataRawAveragePM25f;
	private final PreparedStatement psMapDataRawAveragePM10f;
	private final PreparedStatement psMapDataRawAverageTemperature;
	private final PreparedStatement psMapDataRawAveragePressure;
	private final PreparedStatement psMapDataRawAverageHumidity;
	private final PreparedStatement psMapDataRawMaximumCO;
	private final PreparedStatement psMapDataRawMaximumNO2;
	private final PreparedStatement psMapDataRawMaximumPM1f;
	private final PreparedStatement psMapDataRawMaximumPM25f;
	private final PreparedStatement psMapDataRawMaximumPM10f;
	private final PreparedStatement psMapDataRawMaximumTemperature;
	private final PreparedStatement psMapDataRawMaximumPressure;
	private final PreparedStatement psMapDataRawMaximumHumidity;
	private final PreparedStatement psMapDataRawMinimumCO;
	private final PreparedStatement psMapDataRawMinimumNO2;
	private final PreparedStatement psMapDataRawMinimumPM1f;
	private final PreparedStatement psMapDataRawMinimumPM25f;
	private final PreparedStatement psMapDataRawMinimumPm10f;
	private final PreparedStatement psMapDataRawMinimumTemperature;
	private final PreparedStatement psMapDataRawMinimumPressure;
	private final PreparedStatement psMapDataRawMinimumHumidity;
	private final PreparedStatement psCellDataRawAverageCO;
	private final PreparedStatement psCellDataRawAverageNO2;
	private final PreparedStatement psCellDataRawAveragePM1f;
	private final PreparedStatement psCellDataRawAveragePM25f;
	private final PreparedStatement psCellDataRawAveragePM10f;
	private final PreparedStatement psCellDataRawAverageTemperature;
	private final PreparedStatement psCellDataRawAveragePressure;
	private final PreparedStatement psCellDataRawAverageHumidity;
	private final PreparedStatement psCellDataRawMaximumCO;
	private final PreparedStatement psCellDataRawMaximumNO2;
	private final PreparedStatement psCellDataRawMinimumCO;
	private final PreparedStatement psCellDataRawMinimumNO2;
	private final PreparedStatement psCellDataRawMaximumPM1f;
	private final PreparedStatement psCellDataRawMinimumPM1f;
	private final PreparedStatement psCellDataRawMaximumPM25f;
	private final PreparedStatement psCellDataRawMinimumPM25f;
	private final PreparedStatement psCellDataRawMaximumPM10f;
	private final PreparedStatement psCellDataRawMinimumPM10f;
	private final PreparedStatement psCellDataRawMaximumTemperature;
	private final PreparedStatement psCellDataRawMinimumTemperature;
	private final PreparedStatement psCellDataRawMaximumPressure;
	private final PreparedStatement psCellDataRawMinimumPressure;
	private final PreparedStatement psCellDataRawMaximumHumidity;
	private final PreparedStatement psCellDataRawMinimumHumidity;
	private final PreparedStatement psLineDataRawAverageCO;
	private final PreparedStatement psLineDataRawMaximumCO;
	private final PreparedStatement psLineDataRawMinimumCO;
	private final PreparedStatement psLineDataRawAverageNO2;
	private final PreparedStatement psLineDataRawMaximumNO2;
	private final PreparedStatement psLineDataRawMinimumNO2;
	private final PreparedStatement psLineDataRawAveragePM1f;
	private final PreparedStatement psLineDataRawMaximumPM1f;
	private final PreparedStatement psLineDataRawMinimumPM1f;
	private final PreparedStatement psLineDataRawAveragePM25f;
	private final PreparedStatement psLineDataRawMaximumPM25f;
	private final PreparedStatement psLineDataRawMinimumPM25f;
	private final PreparedStatement psLineDataRawAveragePM10f;
	private final PreparedStatement psLineDataRawMaximumPM10f;
	private final PreparedStatement psLineDataRawMinimumPM10f;
	private final PreparedStatement psLineDataRawAverageTemperature;
	private final PreparedStatement psLineDataRawMaximumTemperature;
	private final PreparedStatement psLineDataRawMinimumTemperature;
	private final PreparedStatement psLineDataRawAveragePressure;
	private final PreparedStatement psLineDataRawMaximumPressure;
	private final PreparedStatement psLineDataRawMinimumPressure;
	private final PreparedStatement psLineDataRawAverageHumidity;
	private final PreparedStatement psLineDataRawMaximumHumidity;
	private final PreparedStatement psLineDataRawMinimumHumidity;
	private final PreparedStatement psInterpolatedDataMapCO;
	private final PreparedStatement psInterpolatedDataMapNO2;
	private final PreparedStatement psInterpolatedDataMapPM1f;
	private final PreparedStatement psInterpolatedDataMapPM25f;
	private final PreparedStatement psInterpolatedDataMapPM10f;
	private final PreparedStatement psInterpolatedDataMapTemperature;
	private final PreparedStatement psInterpolatedDataMapPressure;
	private final PreparedStatement psInterpolatedDataMapHumidity;
	public GraphsSqlDao (Connection connection)
	  throws SQLException
	{
		this.psMapDataRawAverageCO = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_co_1 (?, ?, ?)");
		this.psMapDataRawMaximumCO = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_co_1 (?, ?, ?)");
		this.psMapDataRawMinimumCO = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_co_1 (?, ?, ?)");
		this.psMapDataRawAverageNO2 = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_no2_1 (?, ?, ?)");
		this.psMapDataRawMaximumNO2 = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_no2_1 (?, ?, ?)");
		this.psMapDataRawMinimumNO2 = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_no2_1 (?, ?, ?)");
		this.psMapDataRawAveragePM1f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_pm1f (?, ?, ?)");
		this.psMapDataRawMaximumPM1f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_pm1f (?, ?, ?)");
		this.psMapDataRawMinimumPM1f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_pm1f (?, ?, ?)");
		this.psMapDataRawAveragePM25f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_pm25f (?, ?, ?)");
		this.psMapDataRawMaximumPM25f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_pm25f (?, ?, ?)");
		this.psMapDataRawMinimumPM25f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_pm25f (?, ?, ?)");
		this.psMapDataRawAveragePM10f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_pm10f (?, ?, ?)");
		this.psMapDataRawMaximumPM10f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_pm10f (?, ?, ?)");
		this.psMapDataRawMinimumPm10f = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_pm10f (?, ?, ?)");
		this.psMapDataRawAverageTemperature = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_temperature (?, ?, ?)");
		this.psMapDataRawMaximumTemperature = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_temperature (?, ?, ?)");
		this.psMapDataRawMinimumTemperature = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_temperature (?, ?, ?)");
		this.psMapDataRawAveragePressure = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_pressure (?, ?, ?)");
		this.psMapDataRawMaximumPressure = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_pressure (?, ?, ?)");
		this.psMapDataRawMinimumPressure = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_pressure (?, ?, ?)");
		this.psMapDataRawAverageHumidity = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_avg_humidity (?, ?, ?)");
		this.psMapDataRawMaximumHumidity = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_max_humidity (?, ?, ?)");
		this.psMapDataRawMinimumHumidity = connection.prepareStatement ("SELECT * FROM graph_map_data_raw_min_humidity (?, ?, ?)");
		this.psCellDataRawAverageCO = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_co_1 (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumCO = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_co_1 (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumCO = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_co_1 (?, ?, ?, ?, ?)");
		this.psCellDataRawAverageNO2 = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_no2_1 (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumNO2 = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_no2_1 (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumNO2 = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_no2_1 (?, ?, ?, ?, ?)");
		this.psCellDataRawAveragePM1f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_pm1f (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumPM1f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_pm1f (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumPM1f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_pm1f (?, ?, ?, ?, ?)");
		this.psCellDataRawAveragePM25f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_pm25f (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumPM25f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_pm25f (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumPM25f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_pm25f (?, ?, ?, ?, ?)");
		this.psCellDataRawAveragePM10f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_pm10f (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumPM10f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_pm10f (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumPM10f = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_pm10f (?, ?, ?, ?, ?)");
		this.psCellDataRawAverageTemperature = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_temperature (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumTemperature = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_temperature (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumTemperature = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_temperature (?, ?, ?, ?, ?)");
		this.psCellDataRawAveragePressure = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_pressure (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumPressure = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_pressure (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumPressure = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_pressure (?, ?, ?, ?, ?)");
		this.psCellDataRawAverageHumidity = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_avg_humidity (?, ?, ?, ?, ?)");
		this.psCellDataRawMaximumHumidity = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_max_humidity (?, ?, ?, ?, ?)");
		this.psCellDataRawMinimumHumidity = connection.prepareStatement ("SELECT * FROM graph_cell_data_raw_min_humidity (?, ?, ?, ?, ?)");
		this.psLineDataRawAverageCO = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_co_1 (?, ?, ?, ?)");
		this.psLineDataRawMaximumCO = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_co_1 (?, ?, ?, ?)");
		this.psLineDataRawMinimumCO = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_co_1 (?, ?, ?, ?)");
		this.psLineDataRawAverageNO2 = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_no2_1 (?, ?, ?, ?)");
		this.psLineDataRawMaximumNO2 = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_no2_1 (?, ?, ?, ?)");
		this.psLineDataRawMinimumNO2 = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_no2_1 (?, ?, ?, ?)");
		this.psLineDataRawAveragePM1f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_pm1f (?, ?, ?, ?)");
		this.psLineDataRawMaximumPM1f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_pm1f (?, ?, ?, ?)");
		this.psLineDataRawMinimumPM1f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_pm1f (?, ?, ?, ?)");
		this.psLineDataRawAveragePM25f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_pm25f (?, ?, ?, ?)");
		this.psLineDataRawMaximumPM25f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_pm25f (?, ?, ?, ?)");
		this.psLineDataRawMinimumPM25f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_pm25f (?, ?, ?, ?)");
		this.psLineDataRawAveragePM10f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_pm10f (?, ?, ?, ?)");
		this.psLineDataRawMaximumPM10f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_pm10f (?, ?, ?, ?)");
		this.psLineDataRawMinimumPM10f = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_pm10f (?, ?, ?, ?)");
		this.psLineDataRawAverageTemperature = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_temperature (?, ?, ?, ?)");
		this.psLineDataRawMaximumTemperature = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_temperature (?, ?, ?, ?)");
		this.psLineDataRawMinimumTemperature = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_temperature (?, ?, ?, ?)");
		this.psLineDataRawAveragePressure = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_pressure (?, ?, ?, ?)");
		this.psLineDataRawMaximumPressure = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_pressure (?, ?, ?, ?)");
		this.psLineDataRawMinimumPressure = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_pressure (?, ?, ?, ?)");
		this.psLineDataRawAverageHumidity = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_avg_humidity (?, ?, ?, ?)");
		this.psLineDataRawMaximumHumidity = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_max_humidity (?, ?, ?, ?)");
		this.psLineDataRawMinimumHumidity = connection.prepareStatement ("SELECT * FROM graph_line_data_raw_min_humidity (?, ?, ?, ?)");
		this.psInterpolatedDataMapCO = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_co_1 (?, ?)");
		this.psInterpolatedDataMapNO2 = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_no2_1 (?, ?)");
		this.psInterpolatedDataMapPM1f = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_pm1f (?, ?)");
		this.psInterpolatedDataMapPM25f = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_pm25f (?, ?)");
		this.psInterpolatedDataMapPM10f = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_pm10f (?, ?)");
		this.psInterpolatedDataMapTemperature = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_temperature (?, ?)");
		this.psInterpolatedDataMapPressure = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_pressure (?, ?)");
		this.psInterpolatedDataMapHumidity = connection.prepareStatement ("SELECT * FROM graph_interpolated_data_map_kriging_daily_humidity (?, ?)");
	}
	/**
	 * Query to compute the average of carbon monoxide 1/2 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of carbon monoxide 1/2.
	 */
	public LinkedList<MapDataCell> getAverageCo_1RawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAverageCO.setFloat (1, cellSize);
			this.psMapDataRawAverageCO.setTimestamp (2, fromDate);
			this.psMapDataRawAverageCO.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAverageCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of carbon monoxide 1/2 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of carbon monoxide 1/2.
	 */
	public LinkedList<MapDataCell> getMaximumCo_1RawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumCO.setFloat (1, cellSize);
			this.psMapDataRawMaximumCO.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumCO.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of carbon monoxide 1/2 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of carbon monoxide 1/2.
	 */
	public LinkedList<MapDataCell> getMinimumCo_1RawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumCO.setFloat (1, cellSize);
			this.psMapDataRawMinimumCO.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumCO.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the average of nitric oxide 1/2 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of nitric oxide 1/2.
	 */
	public LinkedList<MapDataCell> getAverageNo2_1RawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAverageNO2.setFloat (1, cellSize);
			this.psMapDataRawAverageNO2.setTimestamp (2, fromDate);
			this.psMapDataRawAverageNO2.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAverageNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of nitric oxide 1/2 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of nitric oxide 1/2.
	 */
	public LinkedList<MapDataCell> getMaximumNo2_1RawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumNO2.setFloat (1, cellSize);
			this.psMapDataRawMaximumNO2.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumNO2.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of nitric oxide 1/2 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of nitric oxide 1/2.
	 */
	public LinkedList<MapDataCell> getMinimumNo2_1RawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumNO2.setFloat (1, cellSize);
			this.psMapDataRawMinimumNO2.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumNO2.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the average of filtered PM 1 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of filtered PM 1.
	 */
	public LinkedList<MapDataCell> getAveragePm1fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAveragePM1f.setFloat (1, cellSize);
			this.psMapDataRawAveragePM1f.setTimestamp (2, fromDate);
			this.psMapDataRawAveragePM1f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAveragePM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of filtered PM 1 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of filtered PM 1.
	 */
	public LinkedList<MapDataCell> getMaximumPm1fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumPM1f.setFloat (1, cellSize);
			this.psMapDataRawMaximumPM1f.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumPM1f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumPM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of filtered PM 1 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of filtered PM 1.
	 */
	public LinkedList<MapDataCell> getMinimumPm1fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumPM1f.setFloat (1, cellSize);
			this.psMapDataRawMinimumPM1f.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumPM1f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumPM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the average of filtered PM 2.5 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of filtered PM 2.5.
	 */
	public LinkedList<MapDataCell> getAveragePm25fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAveragePM25f.setFloat (1, cellSize);
			this.psMapDataRawAveragePM25f.setTimestamp (2, fromDate);
			this.psMapDataRawAveragePM25f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAveragePM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of filtered PM 2.5 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of filtered PM 2.5.
	 */
	public LinkedList<MapDataCell> getMaximumPm25fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumPM25f.setFloat (1, cellSize);
			this.psMapDataRawMaximumPM25f.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumPM25f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumPM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of filtered PM 2.5 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of filtered PM 2.5.
	 */
	public LinkedList<MapDataCell> getMinimumPm25fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumPM25f.setFloat (1, cellSize);
			this.psMapDataRawMinimumPM25f.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumPM25f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumPM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the average of filtered PM 10 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of filtered PM 10.
	 */
	public LinkedList<MapDataCell> getAveragePm10fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAveragePM10f.setFloat (1, cellSize);
			this.psMapDataRawAveragePM10f.setTimestamp (2, fromDate);
			this.psMapDataRawAveragePM10f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAveragePM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of filtered PM 10 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of filtered PM 10.
	 */
	public LinkedList<MapDataCell> getMaximumPm10fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumPM10f.setFloat (1, cellSize);
			this.psMapDataRawMaximumPM10f.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumPM10f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumPM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of filtered PM 10 for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of filtered PM 10.
	 */
	public LinkedList<MapDataCell> getMinimumPm10fRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumPm10f.setFloat (1, cellSize);
			this.psMapDataRawMinimumPm10f.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumPm10f.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumPm10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the average of temperature for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of temperature.
	 */
	public LinkedList<MapDataCell> getAverageTemperatureRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAverageTemperature.setFloat (1, cellSize);
			this.psMapDataRawAverageTemperature.setTimestamp (2, fromDate);
			this.psMapDataRawAverageTemperature.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAverageTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of temperature for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of temperature.
	 */
	public LinkedList<MapDataCell> getMaximumTemperatureRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumTemperature.setFloat (1, cellSize);
			this.psMapDataRawMaximumTemperature.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumTemperature.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of temperature for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of temperature.
	 */
	public LinkedList<MapDataCell> getMinimumTemperatureRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumTemperature.setFloat (1, cellSize);
			this.psMapDataRawMinimumTemperature.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumTemperature.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the average of pressure for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of pressure.
	 */
	public LinkedList<MapDataCell> getAveragePressureRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAveragePressure.setFloat (1, cellSize);
			this.psMapDataRawAveragePressure.setTimestamp (2, fromDate);
			this.psMapDataRawAveragePressure.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAveragePressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of pressure for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of pressure.
	 */
	public LinkedList<MapDataCell> getMaximumPressureRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumPressure.setFloat (1, cellSize);
			this.psMapDataRawMaximumPressure.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumPressure.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumPressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of pressure for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of pressure.
	 */
	public LinkedList<MapDataCell> getMinimumPressureRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumPressure.setFloat (1, cellSize);
			this.psMapDataRawMinimumPressure.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumPressure.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumPressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the average of humidity for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with average of humidity.
	 */
	public LinkedList<MapDataCell> getAverageHumidityRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawAverageHumidity.setFloat (1, cellSize);
			this.psMapDataRawAverageHumidity.setTimestamp (2, fromDate);
			this.psMapDataRawAverageHumidity.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawAverageHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the maximum of humidity for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with maximum of humidity.
	 */
	public LinkedList<MapDataCell> getMaximumHumidityRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMaximumHumidity.setFloat (1, cellSize);
			this.psMapDataRawMaximumHumidity.setTimestamp (2, fromDate);
			this.psMapDataRawMaximumHumidity.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMaximumHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute the minimum of humidity for graph map data.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @param cellSize the cell size.
	 * @return a list with {@code MapDataCell} objects representing points in the map
	 * with minimum of humidity.
	 */
	public LinkedList<MapDataCell> getMinimumHumidityRawForGraphMapData (float cellSize, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psMapDataRawMinimumHumidity.setFloat (1, cellSize);
			this.psMapDataRawMinimumHumidity.setTimestamp (2, fromDate);
			this.psMapDataRawMinimumHumidity.setTimestamp (3, toDate);
			return GraphsSqlDao.getMapDataCells (this.psMapDataRawMinimumHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of carbon monoxide 1/2 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAverageCo_1RawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAverageCO.setDouble (1, cellLongitude);
			this.psCellDataRawAverageCO.setDouble (2, cellLatitude);
			this.psCellDataRawAverageCO.setFloat (3, cellRadius);
			this.psCellDataRawAverageCO.setTimestamp (4, fromDate);
			this.psCellDataRawAverageCO.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAverageCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of carbon monoxide 1/2 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumCo_1RawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumCO.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumCO.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumCO.setFloat (3, cellRadius);
			this.psCellDataRawMaximumCO.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumCO.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of carbon monoxide 1/2 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumCo_1RawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumCO.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumCO.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumCO.setFloat (3, cellRadius);
			this.psCellDataRawMinimumCO.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumCO.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of nitric oxide 1/2 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAverageNo2_1RawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAverageNO2.setDouble (1, cellLongitude);
			this.psCellDataRawAverageNO2.setDouble (2, cellLatitude);
			this.psCellDataRawAverageNO2.setFloat (3, cellRadius);
			this.psCellDataRawAverageNO2.setTimestamp (4, fromDate);
			this.psCellDataRawAverageNO2.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAverageNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of nitric oxide 1/2 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumNo2_1RawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumNO2.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumNO2.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumNO2.setFloat (3, cellRadius);
			this.psCellDataRawMaximumNO2.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumNO2.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of nitric oxide 1/2 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumNo2_1RawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumNO2.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumNO2.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumNO2.setFloat (3, cellRadius);
			this.psCellDataRawMinimumNO2.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumNO2.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of filtered PM 1 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAveragePm1fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAveragePM1f.setDouble (1, cellLongitude);
			this.psCellDataRawAveragePM1f.setDouble (2, cellLatitude);
			this.psCellDataRawAveragePM1f.setFloat (3, cellRadius);
			this.psCellDataRawAveragePM1f.setTimestamp (4, fromDate);
			this.psCellDataRawAveragePM1f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAveragePM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of filtered PM 1 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumPm1fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumPM1f.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumPM1f.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumPM1f.setFloat (3, cellRadius);
			this.psCellDataRawMaximumPM1f.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumPM1f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumPM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of filtered PM 1 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumPm1fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumPM1f.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumPM1f.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumPM1f.setFloat (3, cellRadius);
			this.psCellDataRawMinimumPM1f.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumPM1f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumPM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of filtered PM 2.5 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAveragePm25fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAveragePM25f.setDouble (1, cellLongitude);
			this.psCellDataRawAveragePM25f.setDouble (2, cellLatitude);
			this.psCellDataRawAveragePM25f.setFloat (3, cellRadius);
			this.psCellDataRawAveragePM25f.setTimestamp (4, fromDate);
			this.psCellDataRawAveragePM25f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAveragePM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of filtered PM 2.5 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumPm25fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumPM25f.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumPM25f.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumPM25f.setFloat (3, cellRadius);
			this.psCellDataRawMaximumPM25f.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumPM25f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumPM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of filtered PM 2.5 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumPm25fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumPM25f.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumPM25f.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumPM25f.setFloat (3, cellRadius);
			this.psCellDataRawMinimumPM25f.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumPM25f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumPM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of filtered PM 10 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAveragePm10fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAveragePM10f.setDouble (1, cellLongitude);
			this.psCellDataRawAveragePM10f.setDouble (2, cellLatitude);
			this.psCellDataRawAveragePM10f.setFloat (3, cellRadius);
			this.psCellDataRawAveragePM10f.setTimestamp (4, fromDate);
			this.psCellDataRawAveragePM10f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAveragePM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of filtered PM 10 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumPm10fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumPM10f.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumPM10f.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumPM10f.setFloat (3, cellRadius);
			this.psCellDataRawMaximumPM10f.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumPM10f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumPM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of filtered PM 10 for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumPm10fRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumPM10f.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumPM10f.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumPM10f.setFloat (3, cellRadius);
			this.psCellDataRawMinimumPM10f.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumPM10f.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumPM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of temperature for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAverageTemperatureRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAverageTemperature.setDouble (1, cellLongitude);
			this.psCellDataRawAverageTemperature.setDouble (2, cellLatitude);
			this.psCellDataRawAverageTemperature.setFloat (3, cellRadius);
			this.psCellDataRawAverageTemperature.setTimestamp (4, fromDate);
			this.psCellDataRawAverageTemperature.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAverageTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of temperature for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumTemperatureRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumTemperature.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumTemperature.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumTemperature.setFloat (3, cellRadius);
			this.psCellDataRawMaximumTemperature.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumTemperature.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of temperature for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumTemperatureRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumTemperature.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumTemperature.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumTemperature.setFloat (3, cellRadius);
			this.psCellDataRawMinimumTemperature.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumTemperature.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of pressure for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAveragePressureRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAveragePressure.setDouble (1, cellLongitude);
			this.psCellDataRawAveragePressure.setDouble (2, cellLatitude);
			this.psCellDataRawAveragePressure.setFloat (3, cellRadius);
			this.psCellDataRawAveragePressure.setTimestamp (4, fromDate);
			this.psCellDataRawAveragePressure.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAveragePressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of pressure for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumPressureRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumPressure.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumPressure.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumPressure.setFloat (3, cellRadius);
			this.psCellDataRawMaximumPressure.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumPressure.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumPressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of pressure for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumPressureRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumPressure.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumPressure.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumPressure.setFloat (3, cellRadius);
			this.psCellDataRawMinimumPressure.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumPressure.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumPressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of humidity for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getAverageHumidityRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawAverageHumidity.setDouble (1, cellLongitude);
			this.psCellDataRawAverageHumidity.setDouble (2, cellLatitude);
			this.psCellDataRawAverageHumidity.setFloat (3, cellRadius);
			this.psCellDataRawAverageHumidity.setTimestamp (4, fromDate);
			this.psCellDataRawAverageHumidity.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawAverageHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of humidity for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMaximumHumidityRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMaximumHumidity.setDouble (1, cellLongitude);
			this.psCellDataRawMaximumHumidity.setDouble (2, cellLatitude);
			this.psCellDataRawMaximumHumidity.setFloat (3, cellRadius);
			this.psCellDataRawMaximumHumidity.setTimestamp (4, fromDate);
			this.psCellDataRawMaximumHumidity.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMaximumHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of humidity for graph cell data versus time.
	 * @param cellLatitude the latitude of the position to be analysed.
	 * @param cellLongitude the longitude of the position to be analysed.
	 * @param cellRadius the radius of the position to be analysed.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code Value} objects that represent plot points.
	 */
	public LinkedList<ValueTime> getMinimumHumidityRawForGraphCellData (double cellLongitude, double cellLatitude, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psCellDataRawMinimumHumidity.setDouble (1, cellLongitude);
			this.psCellDataRawMinimumHumidity.setDouble (2, cellLatitude);
			this.psCellDataRawMinimumHumidity.setFloat (3, cellRadius);
			this.psCellDataRawMinimumHumidity.setTimestamp (4, fromDate);
			this.psCellDataRawMinimumHumidity.setTimestamp (5, toDate);
			return GraphsSqlDao.getValueTimes (this.psCellDataRawMinimumHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of carbon monoxide 1/2 for graph line data.
	 * This a map of carbon monoxide 1/2 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAverageCo_1RawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAverageCO.setInt (1, lineId);
			this.psLineDataRawAverageCO.setFloat (2, cellRadius);
			this.psLineDataRawAverageCO.setTimestamp (3, fromDate);
			this.psLineDataRawAverageCO.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAverageCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of carbon monoxide 1/2 for graph line data.
	 * This a map of carbon monoxide 1/2 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumCo_1RawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumCO.setInt (1, lineId);
			this.psLineDataRawMaximumCO.setFloat (2, cellRadius);
			this.psLineDataRawMaximumCO.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumCO.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of carbon monoxide 1/2 for graph line data.
	 * This a map of carbon monoxide 1/2 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumCo_1RawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumCO.setInt (1, lineId);
			this.psLineDataRawMinimumCO.setFloat (2, cellRadius);
			this.psLineDataRawMinimumCO.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumCO.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of nitric oxide 1/2 for graph line data.
	 * This a map of nitric oxide 1/2 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAverageNo2_1RawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAverageNO2.setInt (1, lineId);
			this.psLineDataRawAverageNO2.setFloat (2, cellRadius);
			this.psLineDataRawAverageNO2.setTimestamp (3, fromDate);
			this.psLineDataRawAverageNO2.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAverageNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of nitric oxide 1/2 for graph line data.
	 * This a map of nitric oxide 1/2 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumNo2_1RawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumNO2.setInt (1, lineId);
			this.psLineDataRawMaximumNO2.setFloat (2, cellRadius);
			this.psLineDataRawMaximumNO2.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumNO2.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of nitric oxide 1/2 for graph line data.
	 * This a map of nitric oxide 1/2 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumNo2_1RawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumNO2.setInt (1, lineId);
			this.psLineDataRawMinimumNO2.setFloat (2, cellRadius);
			this.psLineDataRawMinimumNO2.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumNO2.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of filtered PM 1 for graph line data.
	 * This a map of filtered PM 1 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAveragePm1fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAveragePM1f.setInt (1, lineId);
			this.psLineDataRawAveragePM1f.setFloat (2, cellRadius);
			this.psLineDataRawAveragePM1f.setTimestamp (3, fromDate);
			this.psLineDataRawAveragePM1f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAveragePM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of filtered PM 1 for graph line data.
	 * This a map of filtered PM 1 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumPm1fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumPM1f.setInt (1, lineId);
			this.psLineDataRawMaximumPM1f.setFloat (2, cellRadius);
			this.psLineDataRawMaximumPM1f.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumPM1f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumPM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of filtered PM 1 for graph line data.
	 * This a map of filtered PM 1 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumPm1fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumPM1f.setInt (1, lineId);
			this.psLineDataRawMinimumPM1f.setFloat (2, cellRadius);
			this.psLineDataRawMinimumPM1f.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumPM1f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumPM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of filtered PM 2.5 for graph line data.
	 * This a map of filtered PM 2.5 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAveragePm25fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAveragePM25f.setInt (1, lineId);
			this.psLineDataRawAveragePM25f.setFloat (2, cellRadius);
			this.psLineDataRawAveragePM25f.setTimestamp (3, fromDate);
			this.psLineDataRawAveragePM25f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAveragePM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of filtered PM 2.5 for graph line data.
	 * This a map of filtered PM 2.5 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumPm25fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumPM25f.setInt (1, lineId);
			this.psLineDataRawMaximumPM25f.setFloat (2, cellRadius);
			this.psLineDataRawMaximumPM25f.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumPM25f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumPM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of filtered PM 2.5 for graph line data.
	 * This a map of filtered PM 2.5 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumPm25fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumPM25f.setInt (1, lineId);
			this.psLineDataRawMinimumPM25f.setFloat (2, cellRadius);
			this.psLineDataRawMinimumPM25f.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumPM25f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumPM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of filtered PM 10 for graph line data.
	 * This a map of filtered PM 10 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAveragePm10fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAveragePM10f.setInt (1, lineId);
			this.psLineDataRawAveragePM10f.setFloat (2, cellRadius);
			this.psLineDataRawAveragePM10f.setTimestamp (3, fromDate);
			this.psLineDataRawAveragePM10f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAveragePM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of filtered PM 10 for graph line data.
	 * This a map of filtered PM 10 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumPm10fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumPM10f.setInt (1, lineId);
			this.psLineDataRawMaximumPM10f.setFloat (2, cellRadius);
			this.psLineDataRawMaximumPM10f.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumPM10f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumPM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of filtered PM 10 for graph line data.
	 * This a map of filtered PM 10 along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumPm10fRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumPM10f.setInt (1, lineId);
			this.psLineDataRawMinimumPM10f.setFloat (2, cellRadius);
			this.psLineDataRawMinimumPM10f.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumPM10f.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumPM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of temperature for graph line data.
	 * This a map of temperature along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAverageTemperatureRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAverageTemperature.setInt (1, lineId);
			this.psLineDataRawAverageTemperature.setFloat (2, cellRadius);
			this.psLineDataRawAverageTemperature.setTimestamp (3, fromDate);
			this.psLineDataRawAverageTemperature.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAverageTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of temperature for graph line data.
	 * This a map of temperature along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumTemperatureRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumTemperature.setInt (1, lineId);
			this.psLineDataRawMaximumTemperature.setFloat (2, cellRadius);
			this.psLineDataRawMaximumTemperature.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumTemperature.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of temperature for graph line data.
	 * This a map of temperature along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumTemperatureRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumTemperature.setInt (1, lineId);
			this.psLineDataRawMinimumTemperature.setFloat (2, cellRadius);
			this.psLineDataRawMinimumTemperature.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumTemperature.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of pressure for graph line data.
	 * This a map of pressure along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAveragePressureRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAveragePressure.setInt (1, lineId);
			this.psLineDataRawAveragePressure.setFloat (2, cellRadius);
			this.psLineDataRawAveragePressure.setTimestamp (3, fromDate);
			this.psLineDataRawAveragePressure.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAveragePressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of pressure for graph line data.
	 * This a map of pressure along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumPressureRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumPressure.setInt (1, lineId);
			this.psLineDataRawMaximumPressure.setFloat (2, cellRadius);
			this.psLineDataRawMaximumPressure.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumPressure.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumPressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of pressure for graph line data.
	 * This a map of pressure along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumPressureRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumPressure.setInt (1, lineId);
			this.psLineDataRawMinimumPressure.setFloat (2, cellRadius);
			this.psLineDataRawMinimumPressure.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumPressure.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumPressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute average of humidity for graph line data.
	 * This a map of humidity along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getAverageHumidityRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawAverageHumidity.setInt (1, lineId);
			this.psLineDataRawAverageHumidity.setFloat (2, cellRadius);
			this.psLineDataRawAverageHumidity.setTimestamp (3, fromDate);
			this.psLineDataRawAverageHumidity.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawAverageHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute maximum of humidity for graph line data.
	 * This a map of humidity along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMaximumHumidityRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMaximumHumidity.setInt (1, lineId);
			this.psLineDataRawMaximumHumidity.setFloat (2, cellRadius);
			this.psLineDataRawMaximumHumidity.setTimestamp (3, fromDate);
			this.psLineDataRawMaximumHumidity.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMaximumHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute minimum of humidity for graph line data.
	 * This a map of humidity along the locations where a bus line passes.
	 * @param lineId the identification of the bus line.
	 * @param cellRadius the size of the graph points.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getMinimumHumidityRawForGraphLineData (int lineId, float cellRadius, Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psLineDataRawMinimumHumidity.setInt (1, lineId);
			this.psLineDataRawMinimumHumidity.setFloat (2, cellRadius);
			this.psLineDataRawMinimumHumidity.setTimestamp (3, fromDate);
			this.psLineDataRawMinimumHumidity.setTimestamp (4, toDate);
			return GraphsSqlDao.getMapDataCells (this.psLineDataRawMinimumHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of carbon monoxide 1/2 for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getCo_1ForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapCO.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapCO.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapCO);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of nitric oxide 1/2 for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getNo2_1ForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapNO2.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapNO2.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapNO2);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of filtered PM 1 for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getPm1fForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapPM1f.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapPM1f.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapPM1f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of filtered PM 2.5 for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getPm25fForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapPM25f.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapPM25f.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapPM25f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of filtered PM 10 for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getPm10fForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapPM10f.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapPM10f.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapPM10f);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of temperature for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getTemperatureForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapTemperature.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapTemperature.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapTemperature);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of pressure for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getPressureForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapPressure.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapPressure.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapPressure);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	/**
	 * Query to compute interpolation of humidity for <i>interpolated data map</i> plot.
	 * @param fromDate the minimum date to filter the data.
	 * @param toDate the maximum date to filter the data.
	 * @return a list with {@code MapDataCell} that represent graph points.
	 */
	public LinkedList<MapDataCell> getHumidityForInterpolatedDataMap (Timestamp fromDate, Timestamp toDate)
	{
		try {
			this.psInterpolatedDataMapHumidity.setTimestamp (1, fromDate);
			this.psInterpolatedDataMapHumidity.setTimestamp (2, toDate);
			return GraphsSqlDao.getMapDataCells (this.psInterpolatedDataMapHumidity);
		}
		catch (SQLException ex) {
			throw new Error (ex);
		}
	}
	void close ()
	  throws SQLException
	{

		this.psMapDataRawAverageCO.close ();
		this.psMapDataRawMaximumCO.close ();
		this.psMapDataRawMinimumCO.close ();
		this.psMapDataRawAverageNO2.close ();
		this.psMapDataRawMaximumNO2.close ();
		this.psMapDataRawMinimumNO2.close ();
		this.psMapDataRawAveragePM1f.close ();
		this.psMapDataRawMaximumPM1f.close ();
		this.psMapDataRawMinimumPM1f.close ();
		this.psMapDataRawAveragePM25f.close ();
		this.psMapDataRawMaximumPM25f.close ();
		this.psMapDataRawMinimumPM25f.close ();
		this.psMapDataRawAveragePM10f.close ();
		this.psMapDataRawMaximumPM10f.close ();
		this.psMapDataRawMinimumPm10f.close ();
		this.psMapDataRawAverageTemperature.close ();
		this.psMapDataRawMaximumTemperature.close ();
		this.psMapDataRawMinimumTemperature.close ();
		this.psMapDataRawAveragePressure.close ();
		this.psMapDataRawMaximumPressure.close ();
		this.psMapDataRawMinimumPressure.close ();
		this.psMapDataRawAverageHumidity.close ();
		this.psMapDataRawMaximumHumidity.close ();
		this.psMapDataRawMinimumHumidity.close ();
		this.psMapDataRawAverageCO.close ();
		this.psMapDataRawMaximumCO.close ();
		this.psMapDataRawMinimumCO.close ();
		this.psMapDataRawAverageNO2.close ();
		this.psMapDataRawMaximumNO2.close ();
		this.psMapDataRawMinimumNO2.close ();
		this.psMapDataRawAveragePM1f.close ();
		this.psMapDataRawMaximumPM1f.close ();
		this.psMapDataRawMinimumPM1f.close ();
		this.psMapDataRawAveragePM25f.close ();
		this.psMapDataRawMaximumPM25f.close ();
		this.psMapDataRawMinimumPM25f.close ();
		this.psMapDataRawAveragePM10f.close ();
		this.psMapDataRawMaximumPM10f.close ();
		this.psMapDataRawMinimumPm10f.close ();
		this.psMapDataRawAverageTemperature.close ();
		this.psMapDataRawMaximumTemperature.close ();
		this.psMapDataRawMinimumTemperature.close ();
		this.psMapDataRawAveragePressure.close ();
		this.psMapDataRawMaximumPressure.close ();
		this.psMapDataRawMinimumPressure.close ();
		this.psMapDataRawAverageHumidity.close ();
		this.psMapDataRawMaximumHumidity.close ();
		this.psMapDataRawMinimumHumidity.close ();
		this.psLineDataRawAverageCO.close ();
		this.psLineDataRawMaximumCO.close ();
		this.psLineDataRawMinimumCO.close ();
		this.psLineDataRawAverageNO2.close ();
		this.psLineDataRawMaximumNO2.close ();
		this.psLineDataRawMinimumNO2.close ();
		this.psLineDataRawAveragePM1f.close ();
		this.psLineDataRawMaximumPM1f.close ();
		this.psLineDataRawMinimumPM1f.close ();
		this.psLineDataRawAveragePM25f.close ();
		this.psLineDataRawMaximumPM25f.close ();
		this.psLineDataRawMinimumPM25f.close ();
		this.psLineDataRawAveragePM10f.close ();
		this.psLineDataRawMaximumPM10f.close ();
		this.psLineDataRawMinimumPM10f.close ();
		this.psLineDataRawAverageTemperature.close ();
		this.psLineDataRawMaximumTemperature.close ();
		this.psLineDataRawMinimumTemperature.close ();
		this.psLineDataRawAveragePressure.close ();
		this.psLineDataRawMaximumPressure.close ();
		this.psLineDataRawMinimumPressure.close ();
		this.psLineDataRawAverageHumidity.close ();
		this.psLineDataRawMaximumHumidity.close ();
		this.psLineDataRawMinimumHumidity.close ();
		this.psInterpolatedDataMapCO.close ();
		this.psInterpolatedDataMapNO2.close ();
		this.psInterpolatedDataMapPM1f.close ();
		this.psInterpolatedDataMapPM25f.close ();
		this.psInterpolatedDataMapPM10f.close ();
		this.psInterpolatedDataMapTemperature.close ();
		this.psInterpolatedDataMapPressure.close ();
		this.psInterpolatedDataMapHumidity.close ();	}

	private static LinkedList<MapDataCell> getMapDataCells (PreparedStatement ps)
	{
		LinkedList<MapDataCell> result = new LinkedList<> ();
		try (ResultSet rs = ps.executeQuery ()) {
			while (rs.next ()) {
				MapDataCell mdc = new MapDataCell ();
				mdc.latitude = rs.getDouble ("latitude");
				mdc.longitude = rs.getDouble ("longitude");
				mdc.value = rs.getDouble ("value");
				result.add (mdc);
			}
			ps.clearParameters ();
		}
		catch (SQLException e) {
			e.printStackTrace (System.err);
		}
		return result;
	}
	private static LinkedList<ValueTime> getValueTimes (PreparedStatement ps)
	{
		LinkedList<ValueTime> result = new LinkedList<> ();
		try (ResultSet rs = ps.executeQuery ()) {
			while (rs.next ()) {
				ValueTime vt = new ValueTime ();
				vt.value = rs.getDouble ("value");
				vt.date_time = rs.getTimestamp ("when_").getTime ();
				result.add (vt);
			}
			ps.clearParameters ();
		}
		catch (SQLException e) {
			e.printStackTrace (System.err);
		}
		return result;
	}
}