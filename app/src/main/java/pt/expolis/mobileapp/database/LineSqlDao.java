package pt.expolis.mobileapp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

/**
 * Data Access Object that fetches bus line information from the sensor data server.
 *
 * <p>This information is stored in table {@code line_} which contains two columns: the primary
 * key and the official description as given by Carris.</p>
 *
 * <p>When processing the results from the sensor data server, if an error occurs, nothing is
 * reported by the method.  Only the information that was processed without problems is returned.
 * As this is done when the app initialises, we could provide a feedback to the user.</p>
 *
 * TODO Provide feedback to the class user in case an error occurs.
 *
 * @see Line
 * @see Cache#busLines
 * @see PostGisServerDatabase
 */
public class LineSqlDao
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
	LineSqlDao (Connection connection)
	  throws SQLException
	{
		this.psLine = connection.prepareStatement (
				"SELECT ID, description FROM line_"
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
	 * Executes the SQL statement and returns an ordered collection of bus lines.  If a SQL error
	 * occurs while processing the results, only the results processed so far are returned.  <b>No
	 * error</b> is reported to the user.
	 * @return an ordered collection of bus lines.
	 */
	public TreeSet<Line> getLines ()
	{
		TreeSet<Line> result = new TreeSet<> ();
		try {
			ResultSet rs = this.psLine.executeQuery ();
			while (rs.next ()) {
				Line l = new Line (rs.getInt ("ID"), rs.getString ("description"));
				result.add (l);
			}
		}
		catch (SQLException e) {
			e.printStackTrace (System.err);
		}
		return result;
	}
}
