package pt.expolis.mobileapp.online;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;

import pt.expolis.mobileapp.database.Cache;

/**
 * Instances of this class represent information and data structures about a sensor node.
 * Each instance contains a reference to a {@code SensorNode} that represents a record in the
 * postgres database.  This record contains the sensor node id (used in computing the MQTT
 * message topic) and sensor node description (used by the GUI when it needs to display available
 * sensor nodes).  There is also a reference to a {@code SensorData} that represents the
 * latest sensor data received.  This class also contains a reference to a {@code Marker} that is
 * used to indicate the latest position of the sensor node in a osmdroid {@code MapView}.
 */
class SensorNode
{
	/**
	 * Represents the record in the sensor node table in the postgres database.
	 */
	final pt.expolis.mobileapp.database.SensorNode sensorNode;
	/**
	 * Latest sensor data received.
	 */
	SensorData sensorData;
	/**
	 * Indicates if we have received any data from the sensor node.
	 */
	boolean hasData;
	/**
	 * Marker in a osmdroid {@code MapView} that represents the latest known position of the
	 * sensor node.
	 */
	final Marker center;
	static HashMap<Integer, SensorNode> sensorNodeMap;

	static void initialise (MapView map, GeoPoint defaultLocation,
	                        SelectBusNumberFragment.SelectBusNumberListener listener)
	{
		SensorNode.sensorNodeMap = new HashMap<> ();
		for (pt.expolis.mobileapp.database.SensorNode sensorNode : Cache.sensorNodes) {
			SensorNode.sensorNodeMap.put (
			  sensorNode.id,
			  new SensorNode (
			    sensorNode, map, defaultLocation, listener));
		}
	}

	private SensorNode (pt.expolis.mobileapp.database.SensorNode sensorNode, MapView map,
	         GeoPoint defaultLocation,
	                    SelectBusNumberFragment.SelectBusNumberListener listener)
	{
		this.sensorNode = sensorNode;
		this.sensorData = new SensorData ();
		this.hasData = false;
		this.center = new Marker (map);
		this.center.setPosition (defaultLocation);
		this.center.setAnchor (Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
		this.center.setInfoWindow (null);
		this.center.setOnMarkerClickListener ((marker, mapView) -> {
			listener.selectSensorNodeId (SensorNode.this.sensorNode);
			return false;
		});
	}
}
