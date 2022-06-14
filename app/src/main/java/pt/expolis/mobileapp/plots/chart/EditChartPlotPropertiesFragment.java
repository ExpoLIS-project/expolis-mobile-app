package pt.expolis.mobileapp.plots.chart;

import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import pt.expolis.mobileapp.MainActivity;
import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.plots.InfoDialogFragment;
import pt.expolis.mobileapp.plots.SelectDataFragment;
import pt.expolis.mobileapp.plots.StatisticsViewModel;
import pt.expolis.mobileapp.plots.TimePeriodViewModel;

/**
 * Fragment where the user can edit the properties of the chart plot.  This plot shows the data
 * recorded by the ExpoLIS network sensor in a circular region during a time period.
 *
 * <p>The user can specify the location and radius of the circular region, which data to plot,
 * how the data is aggregated and the aggregation time period, and the time period.</p>
 */
public class EditChartPlotPropertiesFragment extends Fragment
{
	/**
	 * Map widget where the user can select the location to plot data.
	 */
	private MapView map;
	/**
	 * Text view that shows details about the location to plot data.
	 */
	private TextView locationDetailsTextView;
	/**
	 * Listener that the activity has to implement in order to plot the chart.
	 */
	private ShowChartPlotListener listener;


	public static EditChartPlotPropertiesFragment newInstance ()
	{
		return new EditChartPlotPropertiesFragment ();
	}

	@Override
	public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                          @Nullable Bundle savedInstanceState)
	{
		View result = inflater.inflate (R.layout.fragment_edit_properties_chart_plot, container,
		  false);
		StatisticsViewModel.Setup.setupStatistics (result, MainActivity.chartPlotViewModel);
		SelectDataFragment.setupSpinner (result, MainActivity.chartPlotViewModel);
		TimePeriodViewModel.Setup.setupWidgets (this, result, MainActivity.chartPlotViewModel);
		setupMapLocation (result);
		this.locationDetailsTextView = result.findViewById (R.id.locationDetailsTextView);
		// info buttons
		ImageButton ib;
		FragmentManager fragmentManager = this.getParentFragmentManager ();
		ib = result.findViewById (R.id.infoShowButton);
		ib.setOnClickListener (v -> {
			InfoDialogFragment infoDialogFragment =
			  InfoDialogFragment.newInstance (R.string.info_chart_plot_show_statistic);
			infoDialogFragment.show (fragmentManager, null);
		});
		ib = result.findViewById (R.id.infoLocationButton);
		ib.setOnClickListener (v -> {
			InfoDialogFragment infoDialogFragment =
			  InfoDialogFragment.newInstance (R.string.info_chart_plot_location);
			infoDialogFragment.show (fragmentManager, null);
		});
		// ok button
		Button b = result.findViewById (R.id.view_graph_button);
		b.setOnClickListener (v -> {
			MainActivity.chartPlotViewModel.save (requireActivity ().getPreferences (Context.MODE_PRIVATE));
			listener.showChartPlotFragment ();
		});
		return result;
	}

	@Override
	public void onAttach (@NotNull Context context)
	{
		super.onAttach (context);
		try {
			this.listener = (ShowChartPlotListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException (context + " must implement ShowChartPlotListener");
		}
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

	/**
	 * Setup the osmdroid map widget to edit the cell location and radius.
	 * @param view the view where the widget is located.
	 */
	private void setupMapLocation (View view)
	{
		this.map = view.findViewById (R.id.locationMapView);
		this.map.setTileSource (TileSourceFactory.MAPNIK);
		this.map.setVerticalMapRepetitionEnabled (false);
		this.map.setHorizontalMapRepetitionEnabled (false);
		this.map.setMultiTouchControls(true);
		this.map.post (() -> {
			double offset = MainActivity.chartPlotViewModel.getOffset ();
			if (offset == -1)
				offset = 0.001;
			BoundingBox bb = new BoundingBox (
			  MainActivity.chartPlotViewModel.getLocationLatitude () + offset,
			  MainActivity.chartPlotViewModel.getLocationLongitude () + offset,
			  MainActivity.chartPlotViewModel.getLocationLatitude () - offset,
			  MainActivity.chartPlotViewModel.getLocationLongitude () - offset);
			this.map.zoomToBoundingBox (bb, false);
		});
		this.map.getOverlayManager ().clear ();
		this.map.getOverlayManager ().add (new LocationOverlay ());
		// add a scale in the top
		final Context context = view.getContext ();
		final DisplayMetrics dm = context.getResources().getDisplayMetrics();
		ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay (this.map);
		mScaleBarOverlay.setCentred (true);
		mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
		this.map.getOverlayManager ().add (mScaleBarOverlay);
		this.map.addMapListener (new MapListener ()
		{
			@Override
			public boolean onScroll (ScrollEvent event)
			{
				updateLocationDetails ();
				commitValues ();
				return false;
			}

			@Override
			public boolean onZoom (ZoomEvent event)
			{
				updateLocationDetails ();
				commitValues ();
				return false;
			}
		});
	}

	/**
	 * Update the details of the location after the user has scrolled the map or zoomed in/out the
	 * map.
	 */
	private void updateLocationDetails ()
	{
		@SuppressLint("DefaultLocale")
		String text = String.format (
		  "%f,%f %.1fm",
		  this.map.getMapCenter ().getLatitude (),
		  this.map.getMapCenter ().getLongitude (),
		  this.getLocationRadius ()
		);
		this.locationDetailsTextView.setText (text);
	}
	/**
	 * Compute the radius of the circular region.
	 * <p>The circular region is displayed as a red circle in the map of this fragment layout.
	 * The radius of this circle is 90% of the smallest dimension of the rectangular map.</p>
	 * @return the radius of the circular region.
	 */
	private double getLocationRadius ()
	{
		BoundingBox bb = this.map.getBoundingBox ();
		GeoPoint north = new GeoPoint (bb.getLatNorth (), bb.getCenterLongitude ());
		GeoPoint south = new GeoPoint (bb.getLatSouth (), bb.getCenterLongitude ());
		GeoPoint east = new GeoPoint (bb.getCenterLatitude (), bb.getLonEast ());
		GeoPoint west = new GeoPoint (bb.getCenterLatitude (), bb.getLonWest ());
		double distanceNorthSouth = north.distanceToAsDouble (south);
		double distanceEastWest = east.distanceToAsDouble (west);
		return Math.min (distanceEastWest, distanceNorthSouth) * 0.45;
	}
	/**
	 * Set the model attributes with the data that
	 * the user has entered or selected in this fragment {@code TextView} widgets.
	 */
	public void commitValues ()
	{
//		final View view = this.getView ();
//		assert view != null;
		// set model cell location
		IGeoPoint cellLocation = this.map.getMapCenter ();
		System.out.println (cellLocation);
		MainActivity.chartPlotViewModel.setLocationLatitude (this.map.getMapCenter ().getLatitude ());
		MainActivity.chartPlotViewModel.setLocationLongitude (this.map.getMapCenter ().getLongitude ());
		// set model cell radius
		BoundingBox bb = this.map.getBoundingBox ();
		GeoPoint north = new GeoPoint (bb.getLatNorth (), bb.getCenterLongitude ());
		GeoPoint south = new GeoPoint (bb.getLatSouth (), bb.getCenterLongitude ());
		GeoPoint east = new GeoPoint (bb.getCenterLatitude (), bb.getLonEast ());
		GeoPoint west = new GeoPoint (bb.getCenterLatitude (), bb.getLonWest ());
		double distanceNorthSouth = north.distanceToAsDouble (south);
		double distanceEastWest = east.distanceToAsDouble (west);
		MainActivity.chartPlotViewModel.setCellRadius ((float) (Math.min (distanceEastWest, distanceNorthSouth) / 2 * 0.9));
		double offset = Math.min (bb.getLatitudeSpan (), bb.getLongitudeSpanWithDateLine ()) / 2;
		MainActivity.chartPlotViewModel.setOffset (offset);
	}

	public interface ShowChartPlotListener
	{
		void showChartPlotFragment ();
	}
}
