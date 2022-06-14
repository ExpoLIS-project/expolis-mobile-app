package pt.expolis.mobileapp.online;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;
import org.osmdroid.views.overlay.simplefastpoint.StyledLabelledGeoPoint;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import pt.expolis.mobileapp.AirQualityIndex;
import pt.expolis.mobileapp.InitialisationThread;
import pt.expolis.mobileapp.PollutionProperties;
import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Cache;
import pt.expolis.mobileapp.database.MapDataCell;
import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.planner.OSRMClient;
import utils.Debug;

public class OnlineDataFragment
  extends Fragment
  implements
	SelectBusNumberFragment.SelectBusNumberListener
{
	private static final String STATE = "__STATE_ONLINE_DATA_FRAGMENT__";
	/**
	 * The state of this fragment that is saved when it goes to the background.
	 */
	private State state;
	public final OnlineDataHandler handler = new OnlineDataHandler (this);
	private OnlineDataListener listener;
	private ImageView overallStateImageView;
	private TextView temperatureTextView, humidityTextView, timestampLastMeasurementTextView, selectedSensorNodeTextView;
	private TextView unhandledMQTTMessageTextView;
	private ImageButton selectBusButton;
	private ImageButton showPlannerButton;
	private OnlineDataPlotView onlineDataPlotView;
	private ImageButton editPlotChartButton;
	private ImageButton editPlotMapButton;
	/**
	 * The map that displays sensor nodes locations and aggregate sensor data.
	 * Each data is stored in a map overlay, the first n overlays contain sensor node locations,
	 * while the remaining overlays contain aggregate sensor data.
	 */
	private MapView map = null;
	private ImageButton layerCOButton, layerNO2Button, layerPM1Button, layerPM2_5Button,
	  layerPM10Button;
	private PollutionProperties shownPollutionLayer;
	/**
	 * The index of the first map overlay with pollution data.
	 */
	private int indexAggregateSensorDataOverlay;
	private static final GeoPoint DEFAULT_LOCATION = new GeoPoint (38.749917, -9.154347);
	transient private final SimpleDateFormat timestampFormatter = new SimpleDateFormat ("H:mm",
	  Locale.getDefault ());

	public static OnlineDataFragment newInstance ()
	{
		return new OnlineDataFragment ();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                          @Nullable Bundle savedInstanceState)
	{
		Debug.toast (
		  getContext (),
		  "onCreateView(\nLayoutInflater:" + inflater + ",\nViewGroup:" + container
		    + ",\nBundle:" + savedInstanceState + ")"
		);
		View result = inflater.inflate (R.layout.online_data_fragment, container, false);
		this.unhandledMQTTMessageTextView = result.findViewById (R.id.unhandledMQTTMessageTextView);
		this.overallStateImageView = result.findViewById (R.id.overallStateImageView);
		this.temperatureTextView = result.findViewById (R.id.temperaturePlainText);
		this.humidityTextView = result.findViewById (R.id.humidityPlainText);
		this.timestampLastMeasurementTextView =
		  result.findViewById (R.id.timestampLastMeasurementPlainText);
		this.selectedSensorNodeTextView = result.findViewById (R.id.selectedSensorNodeTextView);
		this.onlineDataPlotView = result.findViewById (R.id.onlineDataPlotView);
		this.map = result.findViewById (R.id.onlineMapView);
		this.map.setTileSource (TileSourceFactory.MAPNIK);
		this.map.setMultiTouchControls (true);
		SensorNode.initialise (this.map, DEFAULT_LOCATION, this);
		for (SensorNode sd: SensorNode.sensorNodeMap.values ()) {
			this.map.getOverlays ().add (sd.center);
		}
		if (MQTTClientSubscriber.instance != null) {
			MQTTClientSubscriber.instance.subscribeTo (this.handler);
		}
		this.indexAggregateSensorDataOverlay = this.map.getOverlays ().size ();
		this.map.setOnTouchListener ((v, event) -> {
			if (OnlineDataFragment.this.state.followingSensorNode && event.getAction () == MotionEvent.ACTION_MOVE) {
				OnlineDataFragment.this.state.followingSensorNode = false;
			}
			return false;
		});
		IMapController mapController = this.map.getController ();
		mapController.zoomToSpan (1 / 3600d, 1 / 3600d);
		mapController.setCenter (DEFAULT_LOCATION);
		ImageButton b;
		this.selectBusButton = result.findViewById (R.id.selectBusButton);
		this.selectBusButton.setEnabled (MQTTClientSubscriber.instance != null);
		this.selectBusButton.setOnClickListener (v -> {
			SelectBusNumberFragment selectBusNumberFragment = SelectBusNumberFragment.newInstance (this);
			selectBusNumberFragment.show (this.getParentFragmentManager (), null);
		});
		this.editPlotChartButton = result.findViewById (R.id.editPlotChartPropertiesButton);
		this.editPlotChartButton.setEnabled (PostGisServerDatabase.instance != null);
		this.editPlotChartButton.setOnClickListener (
		  v -> this.listener.showEditPlotChartPropertiesFragment ());
		this.editPlotMapButton = result.findViewById (R.id.editPlotMapPropertiesButton);
		this.editPlotMapButton.setEnabled (PostGisServerDatabase.instance != null);
		this.editPlotMapButton.setOnClickListener (
		  v -> this.listener.showEditPlotMapPropertiesFragment ());
		b = result.findViewById (R.id.showPlannerButton);
		b.setEnabled (OSRMClient.serversAvailable);
		b.setOnClickListener (v -> this.listener.showPlannerFragment ());
		this.showPlannerButton = b;
		b = result.findViewById (R.id.tutorButton);
		b.setOnClickListener (v ->
			this.listener.showTutorialFragment ()
		);
		b = result.findViewById (R.id.followBusImageButton);
		b.setOnClickListener (v -> OnlineDataFragment.this.state.followingSensorNode = true);
		this.layerCOButton = result.findViewById (R.id.showCORadioButton);
		this.layerCOButton.setEnabled (PostGisServerDatabase.instance != null);
		this.layerCOButton.setOnClickListener (
		  v -> this.setupMapLayerData (PollutionProperties.CO, Cache.mapLayerCO));
		this.layerNO2Button = result.findViewById (R.id.showNO2RadioButton);
		this.layerNO2Button.setEnabled (PostGisServerDatabase.instance != null);
		this.layerNO2Button.setOnClickListener (
		  v -> this.setupMapLayerData (PollutionProperties.NO2, Cache.mapLayerNO2));
		this.layerPM1Button = result.findViewById (R.id.showPM1RadioButton);
		this.layerPM1Button.setEnabled (PostGisServerDatabase.instance != null);
		this.layerPM1Button.setOnClickListener (
		  v -> this.setupMapLayerData (PollutionProperties.PM1, Cache.mapLayerPM1));
		this.layerPM2_5Button = result.findViewById (R.id.showPM25RadioButton);
		this.layerPM2_5Button.setEnabled (PostGisServerDatabase.instance != null);
		this.layerPM2_5Button.setOnClickListener (
		  v -> this.setupMapLayerData (PollutionProperties.PM2_5, Cache.mapLayerPM25));
		this.layerPM10Button = result.findViewById (R.id.showPM10RadioButton);
		this.layerPM10Button.setEnabled (PostGisServerDatabase.instance != null);
		this.layerPM10Button.setOnClickListener (
		  v -> this.setupMapLayerData (PollutionProperties.PM10, Cache.mapLayerPM10));
		if (savedInstanceState != null) {
			state = (State) savedInstanceState.getSerializable (STATE);
			if (state != null && state.sensorData != null) {
				SensorNode sensorNode = SensorNode.sensorNodeMap.get (state.selectedSensorNodeID);
				if (sensorNode != null) {
					sensorNode.sensorData = state.sensorData;
					this.updateSensorNode (sensorNode);
					this.selectedSensorNodeTextView.setText (String.valueOf (state.selectedSensorNodeID));
				}
			}
		}
		else {
			state = new State ();
			if (Cache.sensorNodes != null && Cache.sensorNodes.size () > 0 && MQTTClientSubscriber.instance != null) {
				this.selectSensorNodeId (Cache.sensorNodes.get (0));
			}
		}
		return result;
	}

	@Override
	public void onResume ()
	{
		super.onResume ();
		// needed for compass, my location overlays, v6.0.0 and up
		this.map.onResume ();
	}

	@Override
	public void onPause ()
	{
		super.onPause ();
		// needed for compass, my location overlays, v6.0.0 and up
		this.map.onPause ();
		if (MQTTClientSubscriber.instance != null)
			MQTTClientSubscriber.instance.unsubscribe ();
	}

	@Override
	public void onSaveInstanceState (Bundle outState)
	{
		outState.putSerializable (STATE, state);
		super.onSaveInstanceState (outState);
	}

	@Override
	public void onAttach (@NotNull Context context)
	{
		super.onAttach (context);
		try {
			this.listener = (OnlineDataFragment.OnlineDataListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException (
			  context +
				 " must implement OnlineDataFragment.OnlineDataListener");
		}
	}

	@Override
	public void selectSensorNodeId (@NotNull pt.expolis.mobileapp.database.SensorNode sensorNode)
	{
		// MQTTClientSubscriber.instance.subscribeTo (sensorNode.id, this.handler);
		state.followingSensorNode = true;
		state.selectedSensorNodeID = sensorNode.id;
		this.selectedSensorNodeTextView.setText (String.valueOf (sensorNode.id));
		//noinspection ConstantConditions
		this.updateSensorNode (SensorNode.sensorNodeMap.get (sensorNode.id));
	}

	private void updateSensorNode (@NotNull SensorNode sensorNode)
	{
		if (!sensorNode.hasData) {
			return;
		}
		if (this.getContext () == null) {
			// fragment is not visible, but a mqtt message was received
			return;
		}
		GeoPoint point = null;
		if (sensorNode.sensorData.latitude != -1) {
			point = new GeoPoint (
			  sensorNode.sensorData.latitude,
			  sensorNode.sensorData.longitude);
			sensorNode.center.setPosition (point);
		}
		if (sensorNode.sensorNode.id == this.state.selectedSensorNodeID) {
			this.state.sensorData = sensorNode.sensorData;
			Locale l = Locale.getDefault ();
			AirQualityIndex aqi = sensorNode.sensorData.getAirQuality ();
			this.overallStateImageView.setImageDrawable (aqi.getVectorDrawableOnlineDataFragment ());
			this.overallStateImageView.setContentDescription (this.getResources ().getString (aqi.resourceID));
			this.temperatureTextView.setText (String.format (l, "%.1f", sensorNode.sensorData.temperature));
			this.humidityTextView.setText (String.format (l, "%.1f", sensorNode.sensorData.humidity));
			this.timestampLastMeasurementTextView.setText (timestampFormatter.format (sensorNode.sensorData.when));
			this.onlineDataPlotView.setPollutionLevels (sensorNode.sensorData);
			if (this.state.followingSensorNode && point != null) {
				IMapController mapController = this.map.getController ();
				mapController.setCenter (point);
			} else {
				this.map.invalidate ();
			}
		}
	}

	/**
	 * Setup the map layer to show the given pollution data.  This data is presented as coloured
	 * squares.  The colours used belong to the air quality index scale.
	 *
	 * <p>The pollution data shown is the aggregate data from the last day.  Check class {@code
	 * MapLayersSqlDao}</p>
	 *
	 * <p>The first <i>n</i> overlays of the map contains the locations of the sensor nodes. The
	 * remaining layers contain pollution data.  Attribute {@code indexAggregateSensorDataOverlay}
	 * contains the index of the first layer with pollution data.</p>
	 *
	 * <p>Depending on the number of data to show, we use two methods to display the data: as a
	 * set of polygons or using a fast overlay.  Check the page
	 * <a href="https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons#how-much-stuff-can-i-put-on-the-map">Markers, Lines and Polygons</a>
	 * for a discussion on how much data can be added to a map.</p>
	 *
	 * @see pt.expolis.mobileapp.database.MapLayersSqlDao
	 * @see #indexAggregateSensorDataOverlay
	 * @see <a href="https://aqicn.org/scale/">Air Quality Index Scale and Color Legend</a>
	 * @param pp the properties of the pollution data to show.
	 * @param mapDataCells the map data cells with pollution data.
	 */
	private void setupMapLayerData (PollutionProperties pp, LinkedList<MapDataCell> mapDataCells)
	{
		if (this.shownPollutionLayer != null) {
			while (this.map.getOverlays ().size () > this.indexAggregateSensorDataOverlay) {
				Overlay overlay =
				  this.map.getOverlayManager ().remove (this.indexAggregateSensorDataOverlay);
				if (overlay instanceof OverlayWithIW) {
					((OverlayWithIW) overlay).closeInfoWindow ();
				}
			}
		}
		if (this.shownPollutionLayer != pp) {
			switch (pp) {
				case CO:
					this.layerCOButton.setImageResource (R.drawable.ic_od_layer_gas_co_on);
					break;
				case NO2:
					this.layerNO2Button.setImageResource (R.drawable.ic_od_layer_gas_no2_on);
					break;
				case PM1:
					this.layerPM1Button.setImageResource (R.drawable.ic_od_layer_pm1_on);
					break;
				case PM2_5:
					this.layerPM2_5Button.setImageResource (R.drawable.ic_od_layer_pm2_5_on);
					break;
				case PM10:
					this.layerPM10Button.setImageResource (R.drawable.ic_od_layer_pm10_on);
					break;
			}
		}
		if (this.shownPollutionLayer != null) {
			switch (this.shownPollutionLayer) {
				case CO:
					this.layerCOButton.setImageResource (R.drawable.ic_od_layer_gas_co_off);
					break;
				case NO2:
					this.layerNO2Button.setImageResource (R.drawable.ic_od_layer_gas_no2_off);
					break;
				case PM1:
					this.layerPM1Button.setImageResource (R.drawable.ic_od_layer_pm1_off);
					break;
				case PM2_5:
					this.layerPM2_5Button.setImageResource (R.drawable.ic_od_layer_pm2_5_off);
					break;
				case PM10:
					this.layerPM10Button.setImageResource (R.drawable.ic_od_layer_pm10_off);
					break;
			}
		}
		if (this.shownPollutionLayer != pp) {
			if (mapDataCells.size () < 20000) {
				this.setupMapLayerDataAsPolygons (pp, mapDataCells);
			}
			else if (mapDataCells.size () < 500000) {
				this.setupMapLayerDataAsFastOverlay (pp, mapDataCells);
			}
			this.shownPollutionLayer = pp;
		}
		else {
			this.shownPollutionLayer = null;
		}
	}

	/**
	 * The map layer in the online fragment shows data from the tables
	 * aggregation_avg_daily_fifty_meters_DATA.  This field contains the cell size in
	 * radians.
	 */
	private static final double CELL_SIZE_MAP_LAYER = 0.000557031249997;

	/**
	 * Adds pollution data as a set of polygon layers.  The polygons when clicked shown a box with
	 * the pollution value.
	 * @param pp the pollution properties to use when computing a polygon colour.
	 * @param mapDataCells the pollution data to show.
	 */
	private void setupMapLayerDataAsPolygons (PollutionProperties pp, LinkedList<MapDataCell> mapDataCells)
	{
		String dataAsString = this.getString (pp.data.resourceId);
		for (MapDataCell mdc : mapDataCells) {
			List<GeoPoint> gps = Arrays.asList (
			  new GeoPoint (mdc.latitude, mdc.longitude),
			  new GeoPoint (mdc.latitude + CELL_SIZE_MAP_LAYER, mdc.longitude),
			  new GeoPoint (mdc.latitude + CELL_SIZE_MAP_LAYER, mdc.longitude + CELL_SIZE_MAP_LAYER),
			  new GeoPoint (mdc.latitude, mdc.longitude + CELL_SIZE_MAP_LAYER),
			  new GeoPoint (mdc.latitude, mdc.longitude)
			);
			Polygon polygon = new Polygon (this.map);
			int color = this.getValueColorForMapLayerCell (pp, (float) mdc.value);
			polygon.getFillPaint ().setColor (color);
			polygon.getOutlinePaint ().setStrokeWidth (0);
			polygon.getOutlinePaint ().setColor (Color.TRANSPARENT);
			polygon.setPoints (gps);
			polygon.setTitle (String.format (
			  Locale.getDefault (), "%s = %.1f", dataAsString, mdc.value));
			this.map.getOverlayManager ().add (polygon);
		}
	}

	/**
	 * Adds pollution as a fast overlay.
	 * @param pp the pollution properties to use when computing a polygon colour.
	 * @param mapDataCells the pollution data to show.
	 */
	private void setupMapLayerDataAsFastOverlay (PollutionProperties pp, LinkedList<MapDataCell> mapDataCells)
	{
		List<IGeoPoint> points = new ArrayList<> (mapDataCells.size ());
		for (MapDataCell mdc : mapDataCells) {
			StyledLabelledGeoPoint p = new StyledLabelledGeoPoint (
			  mdc.latitude,
			  mdc.longitude);
			Paint pointStyle = new Paint ();
			pointStyle.setColor (this.getValueColorForMapLayerCell (pp, (float) mdc.value));
			p.setPointStyle (pointStyle);
			points.add (p);
		}
		// set some visual options for the overlay
		// we use here MAXIMUM_OPTIMIZATION algorithm, which works well with >100k points
		SimpleFastPointOverlayOptions style =
		  SimpleFastPointOverlayOptions.getDefaultStyle ()
			 .setAlgorithm (SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
			 .setRadius (7)
			 .setIsClickable (false)
			 .setCellSize (15);
		// wrap them in a theme
		SimplePointTheme pt = new SimplePointTheme (points);
		SimpleFastPointOverlay overlay = new SimpleFastPointOverlay (pt, style);
		this.map.getOverlayManager ().add (overlay);
	}

	/**
	 * Compute the colour to display the given pollution value.
	 * <p>The colours used belong to the air quality index scale.</p>
	 * @see <a href="https://aqicn.org/scale/">Air Quality Index Scale and Color Legend</a>
	 * @param pp the properties of the pollution value.
	 * @param value the pollution value to compute a colour.
	 * @return a colour in the air quality index scale for the given value.
	 */
	private int getValueColorForMapLayerCell (PollutionProperties pp, float value)
	{
		float red, green, blue;
		if (value < pp.data.minValue) {
			red = 0;
			green = 1;
			blue = 0;
		}
		else if (value > pp.data.maxValue) {
			red = 1;
			green = 0;
			blue = 1;
		}
		else if (value < pp.thresholdGreenYellow) {
			red = (value - pp.data.minValue) / (pp.thresholdGreenYellow - pp.data.minValue);
			green = 1;
			blue = 0;
		}
		else if (value < pp.thresholdYellowRed) {
			red = 1;
			green =
			  (pp.thresholdYellowRed - value) / (pp.thresholdYellowRed - pp.thresholdGreenYellow);
			blue = 0;
		}
		else {
			red = 1;
			green = 0;
			blue = (value - pp.thresholdYellowRed) / (pp.data.maxValue - pp.thresholdYellowRed);
		}
		return Color.argb (0.5f, red, green, blue);
	}
	static class State
	  implements Serializable
	{
		/**
		 * Indicates if we are following a bus/sensor node or not.
		 * <p>Dragging the map cancels this features.  The user can enable if he taps the
		 * <i>follow bus</i> button.</p>
		 */
		boolean followingSensorNode;
		int selectedSensorNodeID;
		SensorData sensorData;
		@NotNull
		@Override
		public String toString ()
		{
			return
			  (followingSensorNode ? "following bus" : "not following sensor node")
			  + (sensorData != null ? ", " + sensorData + " @" + selectedSensorNodeID : "")
			  ;
		}
	}

	public static class OnlineDataHandler
	  extends Handler
	{
		private final WeakReference<OnlineDataFragment> onlineDataWeakReference;
		OnlineDataHandler (OnlineDataFragment onlineDataComponent)
		{
			super (Looper.getMainLooper ());
			onlineDataWeakReference = new WeakReference<> (onlineDataComponent);
		}
		@Override
		public void handleMessage (@NotNull android.os.Message inputMessage)
		{
			OnlineDataFragment od = onlineDataWeakReference.get ();
			if (od != null) {
				switch (inputMessage.what) {
					case Message.NEW_SENSOR_DATA:
						od.updateSensorNode ((SensorNode) inputMessage.obj);
						break;
					case Message.LOST_CONNECTION:
						break;
					case Message.UNRECOGNIZED_DATA:
						od.unhandledMQTTMessageTextView.setText (inputMessage.obj.toString ());
						break;
					case InitialisationThread.MESSAGE_STATUS_EXPOLIS_SERVER: {
						final boolean e = PostGisServerDatabase.instance != null;
						od.editPlotChartButton.setEnabled (e);
						od.editPlotMapButton.setEnabled (e);
						od.layerCOButton.setEnabled (e);
						od.layerNO2Button.setEnabled (e);
						od.layerPM1Button.setEnabled (e);
						od.layerPM2_5Button.setEnabled (e);
						od.layerPM10Button.setEnabled (e);
						break;
					}
					case InitialisationThread.MESSAGE_STATUS_EXPOLIS_MQTT:
						if (od.selectBusButton != null) {
							od.selectBusButton.setEnabled (MQTTClientSubscriber.instance != null);
						}
						break;
					case InitialisationThread.MESSAGE_STATUS_EXPOLIS_PLANNER:
						if (od.showPlannerButton != null) {
							od.showPlannerButton.setEnabled (OSRMClient.serversAvailable);
						}
						break;
				}
			}
		}
	}

	/**
	 * Interface that the hosting activity implements.
	 */
	public interface OnlineDataListener
	{
		void showEditPlotMapPropertiesFragment ();
		void showEditPlotChartPropertiesFragment ();
		void showPlannerFragment ();
		void showTutorialFragment ();
	}
}
