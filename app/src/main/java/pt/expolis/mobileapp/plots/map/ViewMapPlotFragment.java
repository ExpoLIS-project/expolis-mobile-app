package pt.expolis.mobileapp.plots.map;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;
import org.osmdroid.views.overlay.simplefastpoint.StyledLabelledGeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import pt.expolis.mobileapp.MainActivity;
import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Data;
import pt.expolis.mobileapp.database.MapDataCell;
import pt.expolis.mobileapp.plots.GradientLegendView;
import pt.expolis.mobileapp.plots.Options;

import static pt.expolis.mobileapp.plots.Options.DATA_MAP_MARGIN;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewMapPlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMapPlotFragment extends Fragment
{
	private static final String KEY_MAP_DATA = "mapData";

	private LinkedList<MapDataCell> mapDataCells;

	private MapView map;

	public ViewMapPlotFragment ()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param mapDataCells the data to shown in the map.
	 * @return A new instance of fragment ViewMapPlotFragment.
	 */
	public static ViewMapPlotFragment newInstance (LinkedList<MapDataCell> mapDataCells)
	{
		ViewMapPlotFragment fragment = new ViewMapPlotFragment ();
		Bundle args = new Bundle ();
		args.putSerializable (KEY_MAP_DATA, mapDataCells);
		fragment.setArguments (args);
		return fragment;
	}

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		if (getArguments () != null) {
			//noinspection unchecked
			this.mapDataCells = (LinkedList<MapDataCell>) getArguments ().getSerializable (KEY_MAP_DATA);
		}
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View result = inflater.inflate (R.layout.fragment_view_map_plot, container, false);
		this.map = result.findViewById (R.id.map);
		this.map.setTileSource (TileSourceFactory.MAPNIK);
		this.map.setMultiTouchControls (true);
		Data sensorData = MainActivity.mapPlotViewModel.getData ();
		GradientLegendView gradientLegendView = result.findViewById (R.id.gradientLegendView);
		Resources r = this.getResources ();
		gradientLegendView.setTitle (
		  String.format (
		    "%s (%s)",
		    r.getString (sensorData.resourceId),
		    sensorData.units
		  ));
		gradientLegendView.setMinValue (Math.round (sensorData.minValue));
		gradientLegendView.setMaxValue (Math.round (sensorData.maxValue));
		double cellSize = MainActivity.mapPlotViewModel.getCellSizeForMap ();
		this.setupMapData (cellSize, gradientLegendView);
		this.zoomToMapData (mapDataCells);
		TextView propertiesTextView = result.findViewById (R.id.propertiesTextView);
		propertiesTextView.setText (MainActivity.mapPlotViewModel.getProperties (r));
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
	}

	private void setupMapData (double cellSize, GradientLegendView gradientLegendView)
	{
		this.map.getOverlayManager ().clear ();
		if (mapDataCells.size () < 2000) {
			this.addDataAsPolygons (cellSize, gradientLegendView);
		}
		else if (mapDataCells.size () < 500000) {
			this.addDataAsFastOverlay (gradientLegendView);
		}
	}

	/**
	 * Adds map data cells to the osmdroid map using a collection of {@code Polygon}s.
	 * @param cellSize the length of the cells. Each cell is drawn as a square.
	 * @param gradientLegendView the gradient legend used to obtain a map data cell colour.
	 */
	private void addDataAsPolygons (double cellSize, GradientLegendView gradientLegendView)
	{
		for (MapDataCell mdc : mapDataCells) {
			List<GeoPoint> gps = Arrays.asList (
			  new GeoPoint (mdc.latitude, mdc.longitude),
			  new GeoPoint (mdc.latitude + cellSize, mdc.longitude),
			  new GeoPoint (mdc.latitude + cellSize, mdc.longitude + cellSize),
			  new GeoPoint (mdc.latitude, mdc.longitude + cellSize),
			  new GeoPoint (mdc.latitude, mdc.longitude)
			);
			Polygon polygon = new Polygon (this.map);
			int color = gradientLegendView.valueColor ((int) Math.round (mdc.value));
			polygon.getFillPaint ().setColor (color);
			polygon.getOutlinePaint ().setStrokeWidth (0);
			polygon.getOutlinePaint ().setColor (Color.TRANSPARENT);
			polygon.setPoints (gps);
			polygon.setTitle (String.format (Locale.getDefault (), "%.1f", mdc.value));
			this.map.getOverlayManager ().add (polygon);
		}
	}

	/**
	 * Adds map data cells to the osmdroid map using a {@code SimpleFastPointOverlay}.
	 *
	 * @param gradientLegendView the gradient legend used to obtain a map data cell colour.
	 */
	private void addDataAsFastOverlay (GradientLegendView gradientLegendView)
	{
		List<IGeoPoint> points = new ArrayList<> (mapDataCells.size ());
		for (MapDataCell mdc : mapDataCells) {
			StyledLabelledGeoPoint p = new StyledLabelledGeoPoint (
			  mdc.latitude,
			  mdc.longitude);
			Paint pointStyle = new Paint ();
			pointStyle.setColor (gradientLegendView.valueColor ((int) Math.round (mdc.value)));
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
		this.map.getOverlays ().add (overlay);
	}


	private void zoomToMapData (LinkedList<MapDataCell> mapDataCells)
	{
		double avgLatitude = 0, avgLongitude = 0;
		double minLatitude = 90, minLongitude = 180;
		double maxLatitude = -90, maxLongitude = -180;
		for (MapDataCell mdc : mapDataCells) {
			avgLatitude += mdc.latitude;
			avgLongitude += mdc.longitude;
			minLatitude = Math.min (minLatitude, mdc.latitude);
			minLongitude = Math.min (minLongitude, mdc.longitude);
			maxLatitude = Math.max (maxLatitude, mdc.latitude);
			maxLongitude = Math.max (maxLongitude, mdc.longitude);
		}
		avgLatitude /= mapDataCells.size ();
		avgLongitude /= mapDataCells.size ();
		maxLatitude += DATA_MAP_MARGIN;
		minLatitude -= DATA_MAP_MARGIN;
		maxLongitude += DATA_MAP_MARGIN;
		minLongitude -= DATA_MAP_MARGIN;
		switch (Options.focus) {
			case DATA_LISBON:
				minLatitude = Math.max (minLatitude, 38.4711821);
				minLongitude = Math.max (minLongitude, -9.3849384);
				maxLatitude = Math.max (minLatitude + DATA_MAP_MARGIN, Math.min (maxLatitude, 39.2387952));
				maxLongitude = Math.max (minLongitude + DATA_MAP_MARGIN, Math.min (maxLongitude, -8.6333987));
				break;
			case LISBON:
				minLatitude = 38.4711821;
				minLongitude = -9.3849384;
				maxLatitude = 39.2387952;
				maxLongitude = -8.6333987;
				break;
		}
		IMapController mapController = map.getController();
		mapController.zoomToSpan ((maxLatitude - minLatitude) / 6, (maxLongitude - minLongitude) / 6);
		GeoPoint startPoint = new GeoPoint (avgLatitude, avgLongitude);
		mapController.setCenter (startPoint);
	}
}