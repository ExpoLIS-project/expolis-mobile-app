package pt.expolis.mobileapp.database;

import androidx.annotation.NonNull;

/**
 * Represents a sensor node.
 *
 * <p>The sensor node description is the official description as determined by the ExpoLIS
 * project team.  As such it is not subject to translation.</p>
 *
 * <p>Sensor nodes are sorted by their description.  They are listed in alphabetical order when
 * the user can select a sensor node to monitor its data.</p>
 *
 * <p>Instances of this class correspond to rows in table {@code node_sensors}.  Instances are
 * created when the app initialises.  {@link SensorNodeSqlDao SensorNodeSqlDao} is the Data Access
 * Object responsible for fetching the data. They are stored in the cache.</p>
 *
 * @see Cache#sensorNodes
 * @see SensorNodeSqlDao
 */
public class SensorNode
	implements Comparable<SensorNode>
{
	/**
	 * Primary key in table {@code node_sensors}.
	 */
	public final int id;
	/**
	 * Official sensor node description. Corresponds to column {@code serial_description}.
	 */
	public final String description;

	/**
	 * Sole constructor used to create an instance from data returned by the SQL query done at
	 * {@link SensorNodeSqlDao SensorNodeSqlDao} class.
	 * @param id primary key in table {@code node_sensors}.
	 * @param description official sensor node description.
	 */
	SensorNode (int id, String description)
	{
		this.id = id;
		this.description = description;
	}

	@NonNull
	@Override
	public String toString ()
	{
		return this.description;
	}

	@Override
	public int compareTo (SensorNode o)
	{
		return description.compareTo (o.description);
	}
}
