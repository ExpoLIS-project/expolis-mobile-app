package pt.expolis.mobileapp.database;

import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

import pt.expolis.mobileapp.ExpolisPreferences;

/**
 * Class responsible for managing the connections to the sensor data server.  These connections
 * are used to fetch data shown on the map and plot graphs, the earliest and latest sensor
 * measurement dates, bus line information, and existing sensor nodes.
 *
 * <p>The sensor data server is a Postgres database.  The database name is {@code sensor_data}.
 * The role that has permission to access the database is {@code expolis_app}.  The default
 * sensor data server name is {@code sensor.expolis.pt} while the port number is 5432.  Details
 * about the connection are stored in inner class {@link UrlOptions UrlOptions}.</p>
 *
 * <p>The sensor data server name and port can be configured by the user in the administration
 * screen.  This screen is only accessible when the application initialises.</p>
 *
 * <p>This class is a singleton.  Its instance is created by method {@link #connect() connect} and
 * destroyed in static method {@link #disconnect() disconnect}.</p>
 *
 * @see pt.expolis.mobileapp.WelcomeScreenFragment
 * @see pt.expolis.mobileapp.InitialisationThread
 * @see pt.expolis.mobileapp.AdminFragment
 */
public class PostGisServerDatabase
{
	/**
	 * Connection to the sensor data database server.
	 */
	final private Connection connection;
	/**
	 * Data Access Object used to fetch data to the map and plot graphs.
	 */
	final private GraphsSqlDao graphsSqlDao;
	/**
	 * Data Access Object used to fetch earliest and latest sensor measurement date.
	 */
	final private MeasurementsSqlDao measurementsDAO;
	/**
	 * Data Access Object used to fetch bus line information.
	 */
	final private LineSqlDao linesSqlDao;
	/**
	 * Data Access Object used to fetch information about existing sensor nodes.
	 */
	final private SensorNodeSqlDao sensorNodesSqlDao;
	/**
	 * The DAO to fetch the pollutant data that is shown on the map on the online data fragment.
	 */
	final private MapLayersSqlDao mapLayersSqlDao;
	/**
	 * Class singleton.  Initialised in method {@link #connect() connect} and destroyed in method
	 * {@link #disconnect() disconnect}.
	 */
	public static PostGisServerDatabase instance = null;

	/**
	 * Connects to the ExpoLIS database and initialises the Data Access Objects that are used to
	 * fetch data.  Initialises the class singleton, if its {@code null}.
	 * @throws SQLException If there is a problem connecting to the Expolis database.
	 */
	public static void connect ()
	  throws SQLException
	{
		if (instance == null) {
			instance = new PostGisServerDatabase ();
		}
	}

	/**
	 * Closes the connection to the sensor data server and Data Access Objects.  Destroys the
	 * class singleton.
	 */
	public static void disconnect ()
	{
		if (instance != null) {
			instance.close ();
			instance = null;
		}
	}

	/**
	 * Create a connection to the sensor data server.
	 * @throws SQLException If there is a problem connecting to the sensor data server.
	 */
	private PostGisServerDatabase ()
	  throws SQLException
	{
		String url = String.format (
		  Locale.ENGLISH,
		  "jdbc:postgresql://%s:%d/%s",
		  UrlOptions.host,
		  UrlOptions.port,
		  UrlOptions.DATABASE
		);
		Properties info = new Properties ();
		info.setProperty ("user", UrlOptions.ROLE);
		this.connection = DriverManager.getConnection (url, info);
		this.graphsSqlDao = new GraphsSqlDao (this.connection);
		this.measurementsDAO = new MeasurementsSqlDao (connection);
		this.linesSqlDao = new LineSqlDao (connection);
		this.sensorNodesSqlDao = new SensorNodeSqlDao (connection);
		this.mapLayersSqlDao = new MapLayersSqlDao (connection);
	}

	public MeasurementsSqlDao measurementsDAO ()
	{
		return this.measurementsDAO;
	}
	public GraphsSqlDao graphsDAO ()
	{
		return this.graphsSqlDao;
	}
	public LineSqlDao busesLinesDAO ()
	{
		return this.linesSqlDao;
	}
	public SensorNodeSqlDao sensorNodesDAO ()
	{
		return this.sensorNodesSqlDao;
	}
	public MapLayersSqlDao mapLayersSqlDao ()
	{
		return this.mapLayersSqlDao;
	}

	/**
	 * Closes the connection to the sensor data server and the Data Access Objects used to fetch
	 * data.
	 */
	public void close ()
	{
		try {
			this.graphsSqlDao.close ();
			this.measurementsDAO.close ();
			this.linesSqlDao.close ();
			this.sensorNodesSqlDao.close ();
			this.mapLayersSqlDao.close ();
			this.connection.close ();
		}
		catch (SQLException e) {
			e.printStackTrace ();
		}
	}

	/**
	 * Contains the options to build the url to access the sensor data server.
	 *
	 * <p>Sensor data is stored in Postgres database.  The default server name is {@code sensor
	 * .expolis.pt} and the default port number is the usual Postgres number, 5432.  The database
	 * name and role are {@code sensor_data} and {@code expolis_app}, respectively.  This role
	 * allows read only access to the information stored in the database.</p>
	 *
	 * <p>The user can edit the server name and port number in the administration screen.  This
	 * screen is only accessible during app initialisation.  These editable URL options are
	 * stored in the app shared preference file.</p>
	 *
	 * @see pt.expolis.mobileapp.AdminFragment
	 * @see pt.expolis.mobileapp.WelcomeScreenFragment
	 * @see <a href="https://developer.android.com/training/data-storage/shared-preferences>App shared preference file</a>
	 */
	public static class UrlOptions
	{
		/**
		 * Default sensor data server name.
		 */
		private static final String DEFAULT_HOST = "sensor.expolis.pt";
		/**
		 * Editable sensor data server name.
		 */
		public static String host = DEFAULT_HOST;
		/**
		 * Default sensor data server port number.
		 */
		public static final int DEFAULT_PORT = 5432;
		/**
		 * Editable sensor data server port number.
		 */
		public static int port = DEFAULT_PORT;
		/**
		 * Database within postgres where sensor data is stored.
		 */
		private static final String DATABASE = "sensor_data";
		/**
		 * Role to access the sensor data postgres database.
		 */
		private static final String ROLE = "expolis_app";

		/**
		 * Loads the editable URL options from the app shared preference file.
		 * @param preferences the app shared preference file.
		 */
		public static void load (SharedPreferences preferences)
		{
			UrlOptions.host = preferences.getString (
			  ExpolisPreferences.KEY_DATABASE_URL_HOST,
			  DEFAULT_HOST
			  );
			UrlOptions.port = preferences.getInt (
			  ExpolisPreferences.KEY_DATABASE_URL_PORT,
			  DEFAULT_PORT
			);
		}

		/**
		 * Saves the editable URL options to the app shared preference file.
		 * @param preferences the app shared preference file.
		 */
		public static void save (SharedPreferences preferences)
		{
			SharedPreferences.Editor editor = preferences.edit ();
			editor.putString (
			  ExpolisPreferences.KEY_DATABASE_URL_HOST,
			  UrlOptions.host);
			editor.putInt (
			  ExpolisPreferences.KEY_DATABASE_URL_PORT,
			  UrlOptions.port
			);
			editor.apply ();
		}
	}
}
