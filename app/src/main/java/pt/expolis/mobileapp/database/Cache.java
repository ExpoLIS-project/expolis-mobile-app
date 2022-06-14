package pt.expolis.mobileapp.database;

import android.content.Context;
import android.content.res.Resources;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import pt.expolis.mobileapp.R;

/**
 * This class contains data cached from the sensor data server.  This data is initialised when
 * the app starts in the {@code InitialisationThread} class.  Part of this data is saved in a
 * local file and used when the app cannot establish a connection with the sensor data server.
 * @see pt.expolis.mobileapp.InitialisationThread
 */
public class Cache
{
	/**
	 * Newest measurement data stored in the sensor data server at app initialisation.  This is
	 * used to constrain the user interface element where the user can select a date.
	 */
	static public long maxMeasurementDate;
	/**
	 * Oldest measurement data stored in the sensor data server at app initialisation.  This is
	 * used to constrain the user interface element where the user can select a date.
	 */
	static public long minMeasurementDate;
	/**
	 * Bus lines stored in the sensor data server at app initialisation.  Used in the graph map
	 * where the user can restrict displayed data to a specific bus line.
	 */
	static public TreeSet<Line> busLines = new TreeSet<> ();
	/**
	 * Available sensor nodes information stored in the sensor data server at app initialisation.
	 * This is used to show which sensor nodes are available.
	 */
	static public ArrayList<SensorNode> sensorNodes = new ArrayList<> ();
	/**
	 * The latest nitrogen dioxide aggregated data that is shown on the map on the online data
	 * fragment. This data is obtained when the activity starts.
	 */
	static public LinkedList<MapDataCell> mapLayerCO;
	/**
	 * The latest carbon monoxide aggregated data that is shown on the map on the online data
	 * fragment. This data is obtained when the activity starts.
	 */
	static public LinkedList<MapDataCell> mapLayerNO2;
	/**
	 * The latest PM<sub>1</sub> aggregated data that is shown on the map on the online data
	 * fragment. This data is obtained when the activity starts.
	 */
	static public LinkedList<MapDataCell> mapLayerPM1;
	/**
	 * The latest PM<sub>2.5</sub> aggregated data that is shown on the map on the online data
	 * fragment. This data is obtained when the activity starts.
	 */
	static public LinkedList<MapDataCell> mapLayerPM25;
	/**
	 * The latest PM<sub>10</sub> aggregated data that is shown on the map on the online data
	 * fragment. This data is obtained when the activity starts.
	 */
	static public LinkedList<MapDataCell> mapLayerPM10;
	/**
	 * Name of the local file where sensor node information is stored.
	 */
	static final private String SENSOR_NODES_FILENAME = "SensorNodes.data";

	/**
	 * Loads stored sensor node information in the local file.  If the file does not exist,
	 * returns {@code false}, and creates a set of default sensor nodes.  The latter is to enable
	 * to user to select some sensor nodes.
	 * @param r used to access XML resources.
	 * @param context used to open the local file.
	 * @return {@code true} if information was loaded.
	 */
	static public boolean loadSensorNodes (Resources r, Context context)
	{
		boolean ok = true;
		try (DataInputStream dis = new DataInputStream (context.openFileInput (SENSOR_NODES_FILENAME))) {
			int numberSensorNodes = dis.readInt ();
			while (numberSensorNodes > 0) {
				SensorNode sn = new SensorNode (dis.readInt (), dis.readUTF ());
				sensorNodes.add (sn);
				numberSensorNodes--;
			}
		} catch (IOException e) {
			createDefaultSensorNodes (r);
			ok = false;
		}
		return ok;
	}

	/**
	 * Creates five sensor nodes so that the user can select a sensor node to view information
	 * that it publishes.
	 * @param r used to access XML resources.
	 */
	static private void createDefaultSensorNodes (Resources r)
	{
		sensorNodes.clear ();
		for (int i = 1; i <= 5; i++) {
			SensorNode sn = new SensorNode (i, r.getString (R.string.default_sensor_box_i, i));
			sensorNodes.add (sn);
		}
	}

	/**
	 * Saves information about sensor nodes that was fetched from the sensor data server in a
	 * local file.
	 * @param context used to open the local file.
	 */
	static public void saveSensorNodes (Context context)
	{
		try (FileOutputStream fos = context.openFileOutput (
		  SENSOR_NODES_FILENAME, Context.MODE_PRIVATE)) {
			DataOutputStream dos = new DataOutputStream (fos);
			dos.writeInt (sensorNodes.size ());
			for (SensorNode sn: sensorNodes) {
				dos.writeInt (sn.id);
				dos.writeUTF (sn.description);
			}
		} catch (IOException ignored) {
		}
	}
}
