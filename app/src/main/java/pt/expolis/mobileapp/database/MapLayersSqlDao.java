package pt.expolis.mobileapp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Data Access Object that fetches sensor data shown on the map in the online data fragment.
 *
 * <p>This class contains the connections to the ExpoLIS server that fetch the data shown on the map
 * in the online data fragment.  This data is the aggregated pollution data from the <em>last
 * day</em>. The purpose of this data is to show to user how good is the air in respect to the monitored
 * pollutants.</p>
 *
 * <p>The pollutants that are shown on the online data fragment are: nitrogen dioxide, carbon
 * monoxide, PM1, PM2.5 and PM10.</p>
 *
 * <p>The data that is fetched is the daily average data computed over a grid with a cell size of
 * one hundred meters.  This means the data is fetched from the tables {@code
 * aggregation_avg_daily_hundred_meters_co}, {@code aggregation_avg_daily_hundred_meters_no},
 * {@code aggregation_avg_daily_hundred_meters_pm1f}, {@code
 * aggregation_avg_daily_hundred_meters_pm25f}, and {@code
 * aggregation_avg_daily_hundred_meters_pm10f}.</p>
 *
 * <p>When processing the results from the sensor data server, if an error occurs, nothing is
 * reported by the method.  Only the information that was processed without problems is returned.
 * As this is done when the app initialises, we could provide a feedback to the user.</p>
 *
 * TODO Provide feedback to the class user in case an error occurs.
 *
 * @see Cache#mapLayerCO
 * @see Cache#mapLayerNO2
 * @see Cache#mapLayerPM1
 * @see Cache#mapLayerPM25
 * @see Cache#mapLayerPM10
 * @see PostGisServerDatabase
 */
public class MapLayersSqlDao
{
	/**
	 * The SQL statement that is used to fetch the latest nitrogen dioxide aggregated data.
	 */
	private final PreparedStatement preparedStatementNO2;
	/**
	 * The SQL statement that is used to fetch the latest carbon monoxide aggregated data.
	 */
	private final PreparedStatement preparedStatementCO;
	/**
	 * The SQL statement that is used to fetch the latest PM<sub>1</sub> aggregated data.
	 */
	private final PreparedStatement preparedStatementPM1;
	/**
	 * The SQL statement that is used to fetch the latest PM<sub>2.5</sub> aggregated data.
	 */
	private final PreparedStatement preparedStatementPM25;
	/**
	 * The SQL statement that is used to fetch the latest PM<sub>10</sub> aggregated data.
	 */
	private final PreparedStatement preparedStatementPM10;

	/**
	 * Sole constructor that initialiss the SQL statements given the connection to the sensor data
	 * server.
	 * @param connection used to initialise the SQL statement.
	 * @throws SQLException if an error occurs during initialisation.
	 */
	@SuppressWarnings("SpellCheckingInspection")
	MapLayersSqlDao (Connection connection) throws SQLException
	{
		this.preparedStatementCO = connection.prepareStatement (
		  "SELECT st_x (long_lat) as longitude, st_y (long_lat) as latitude, value " +
			 "FROM aggregation_avg_daily_hundred_meters_co_1 " +
			 "WHERE when_ BETWEEN " +
			 "date_trunc ('day', current_timestamp)  - interval '1 day' AND " +
			 "date_trunc ('day', current_timestamp)"
		);
		this.preparedStatementNO2 = connection.prepareStatement (
		  "SELECT st_x (long_lat) as longitude, st_y (long_lat) as latitude, value " +
			 "FROM aggregation_avg_daily_hundred_meters_no_1 " +
			 "WHERE when_ BETWEEN " +
			 "date_trunc ('day', current_timestamp)  - interval '1 day' AND " +
			 "date_trunc ('day', current_timestamp)"
		);
		this.preparedStatementPM1 = connection.prepareStatement (
		  "SELECT st_x (long_lat) as longitude, st_y (long_lat) as latitude, value " +
			 "FROM aggregation_avg_daily_hundred_meters_pm1f " +
			 "WHERE when_ BETWEEN " +
			 "date_trunc ('day', current_timestamp)  - interval '1 day' AND " +
			 "date_trunc ('day', current_timestamp)"
		);
		this.preparedStatementPM25 = connection.prepareStatement (
		  "SELECT st_x (long_lat) as longitude, st_y (long_lat) as latitude, value " +
			 "FROM aggregation_avg_daily_hundred_meters_pm25f " +
			 "WHERE when_ BETWEEN " +
			 "date_trunc ('day', current_timestamp)  - interval '1 day' AND " +
			 "date_trunc ('day', current_timestamp)"
		);
		this.preparedStatementPM10 = connection.prepareStatement (
		  "SELECT st_x (long_lat) as longitude, st_y (long_lat) as latitude, value " +
			 "FROM aggregation_avg_daily_hundred_meters_pm10f " +
			 "WHERE when_ BETWEEN " +
			 "date_trunc ('day', current_timestamp)  - interval '1 day' AND " +
			 "date_trunc ('day', current_timestamp)"
		);
	}

	/**
	 * Get the aggregated carbon monoxide data that is shown on the map in the online data fragment.
	 * <p>Every call of this method returns the latest data in the ExpoLIS server.</p>
	 * @return the aggregated carbon monoxide data that is shown on the map in the online data fragment.
	 */
	public LinkedList<MapDataCell> getCO ()
	{
		return MapLayersSqlDao.getMapDataCells (this.preparedStatementCO);
	}
	/**
	 * Get the aggregated nitrogen dioxide data that is shown on the map in the online data
	 * fragment.
	 * <p>Every call of this method returns the latest data in the ExpoLIS server.</p>
	 * @return the aggregated nitrogen monoxide data that is shown on the map in the online data
	 * fragment.
	 */
	public LinkedList<MapDataCell> getNO2 ()
	{
		return MapLayersSqlDao.getMapDataCells (this.preparedStatementNO2);
	}
	/**
	 * Get the aggregated PM<sub>1</sub> data that is shown on the map in the online data fragment.
	 * <p>Every call of this method returns the latest data in the ExpoLIS server.</p>
	 * @return the aggregated PM<sub>1</sub> data that is shown on the map in the online data fragment.
	 */
	public LinkedList<MapDataCell> getPM1 ()
	{
		return MapLayersSqlDao.getMapDataCells (this.preparedStatementPM1);
	}
	/**
	 * Get the aggregated PM<sub>2.5</sub> data that is shown on the map in the online data fragment.
	 * <p>Every call of this method returns the latest data in the ExpoLIS server.</p>
	 * @return the aggregated PM<sub>2.5</sub> data that is shown on the map in the online data fragment.
	 */
	public LinkedList<MapDataCell> getPM25 ()
	{
		return MapLayersSqlDao.getMapDataCells (this.preparedStatementPM25);
	}
	/**
	 * Get the aggregated PM<sub>10</sub> data that is shown on the map in the online data fragment.
	 * <p>Every call of this method returns the latest data in the ExpoLIS server.</p>
	 * @return the aggregated PM<sub>10</sub> data that is shown on the map in the online data
	 * fragment.
	 */
	public LinkedList<MapDataCell> getPM10 ()
	{
		return MapLayersSqlDao.getMapDataCells (this.preparedStatementPM10);
	}

	/**
	 * Closes the <i>statements</i> that are used to fetch the pollution data that is shown on the
	 * map in the online data fragment.
	 * @throws SQLException if an exception occurs while closing the <i>statements</i>.
	 */
	void close () throws SQLException
	{
		this.preparedStatementCO.close ();
		this.preparedStatementNO2.close ();
		this.preparedStatementPM1.close ();
		this.preparedStatementPM25.close ();
		this.preparedStatementPM10.close ();
	}

	/**
	 * Executes the query of the given <i>statements</i> and returns a list of map data cells.
	 * If there is a SQL exception, an empty list is returned.
	 * @param ps the <i>statements</i> to execute.
	 * @return a list with the result of the query.
	 */
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
}
