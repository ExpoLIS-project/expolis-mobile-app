package pt.expolis.mobileapp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Object that fetches sensor node information stored in the sensor data
 * server.
 *
 * <p>Sensor node information is stored in table {@code node_sensors}.  This table contains the
 * following columns: primary key; official description as determined by the ExpoLIS project;
 * date and time when it was deployed.</p>
 *
 * <p>When processing the results from the sensor data server, if an error occurs, nothing is
 * reported by the method.  Only the information that was processed without problems is returned.
 * As this is done when the app initialises, we could provide a feedback to the user.</p>
 *
 * TODO Provide feedback to the class user in case an error occurs.
 *
 * @see SensorNode
 * @see Cache#sensorNodes
 * @see PostGisServerDatabase
 */
public class SensorNodeSqlDao
{
	/**
	 * The SQL select statement that is used to fetch information.
	 */
	private final PreparedStatement psLine;

	/**
	 * Sole constructor that initialises the SQL statement given the connection to the sensor data
	 * server.
	 * @param connection used to initialise the SQL statement.
	 * @throws SQLException if an error occurs during initialisation.
	 */
	SensorNodeSqlDao (Connection connection)
	  throws SQLException
	{
		this.psLine = connection.prepareStatement (
				"SELECT id, serial_description FROM node_sensors ORDER BY serial_description"
		);
	}

	/**
	 * Closes the SQL select statement.
	 * @throws SQLException if an error occurs.
	 */
	void close ()
	  throws SQLException
	{
		this.psLine.close ();
	}

	/**
	 * Executes the SQL statement and returns a collection of sensor nodes.  If a SQL error
	 * occurs while processing the results, only the results processed so far are returned.  <b>No
	 * error</b> is reported to the user.
	 * @return a collection of sensor nodes.
	 */
	public ArrayList<SensorNode> getSensorNodes ()
	{
		ArrayList<SensorNode> result = new ArrayList<> ();
		try {
			ResultSet rs = this.psLine.executeQuery ();
			while (rs.next ()) {
				SensorNode sn = new SensorNode (
				  rs.getInt ("id"),
				  rs.getString ("serial_description"));
				result.add (sn);
			}
		}
		catch (SQLException e) {
			e.printStackTrace (System.err);
		}
		return result;
	}
}
