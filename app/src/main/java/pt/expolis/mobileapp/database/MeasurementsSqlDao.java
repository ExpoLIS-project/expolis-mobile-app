package pt.expolis.mobileapp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Data Access Object that fetches the earliest and latest sensor measurement date.  These values
 * are used in the date components where the user can select a date range to filter in sensor
 * data timestamps.
 *
 * <p>If there are sensor measurements nothing is reported to the user, and we use the current
 * timestamp.  If there is an error while fetching the data from the sensor data server, nothing
 * is reported to the user, and the value zero is used as either earliest or latest sensor
 * measurement date.</p>
 *
 * <p>The values computed by this DAO object are stored in the {@code Cache} object instance.</p>
 *
 * @see Cache#minMeasurementDate
 * @see Cache#maxMeasurementDate
 * @see PostGisServerDatabase
 */
public class MeasurementsSqlDao
{
	/**
	 * The SQL statement that is used to fetch the earliest sensor measurement date.
	 */
	private final PreparedStatement psMinDate;
	/**
	 * The SQL statement that is used to fetch the latest sensor measurement date.
	 */
	private final PreparedStatement psMaxDate;

	/**
	 * Sole constructor that initialises the SQL statements given the connection to the sensor
	 * data server.
	 * @param connection used to initialise the SQL statement.
	 * @throws SQLException if an error occurs during initialisation.
	 */
	MeasurementsSqlDao (@org.jetbrains.annotations.NotNull Connection connection)
	  throws SQLException
	{
		this.psMinDate = connection.prepareStatement (
		  "SELECT min (when_) AS value FROM measurement_properties"
		);
		this.psMaxDate = connection.prepareStatement (
		  "SELECT max (when_) AS value FROM measurement_properties"
		);
	}

	/**
	 * Closes the SQL statements that are used to fetch the earliest and latest sensor measurement
	 * dates.
	 * @throws SQLException if an exception occurs while closing the SQL statements.
	 */
	void close ()
	  throws SQLException
	{
		this.psMinDate.close ();
		this.psMaxDate.close ();
	}

	/**
	 * Returns the minimum measurement data in milliseconds precision.
	 * @return the minimum measurement data in milliseconds precision.
	 */
	public long minDate ()
	{
		try {
			ResultSet rs = this.psMinDate.executeQuery ();
			rs.next ();
			Timestamp result = rs.getTimestamp ("value");
			if (result != null) {
				return result.getTime ();
			}
			else {
				// TODO: handle the case when there are no measurements.
				return System.currentTimeMillis ();
			}
		}
		catch (SQLException e) {
			e.printStackTrace ();
			return 0;
		}
	}

	/**
	 * Returns the maximum measurement data in milliseconds precision.
	 * @return the maximum measurement data in milliseconds precision.
	 */
	public long maxDate ()
	{
		try {
			ResultSet rs = this.psMaxDate.executeQuery ();
			rs.next ();
			Timestamp result = rs.getTimestamp ("value");
			if (result != null) {
				return result.getTime ();
			}
			else {
				// TODO: handle the case when there are no measurements.
				return System.currentTimeMillis ();
			}
		}
		catch (SQLException e) {
			e.printStackTrace ();
			return System.currentTimeMillis ();
		}
	}
}
