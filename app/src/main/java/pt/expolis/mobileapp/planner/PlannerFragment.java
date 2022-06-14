package pt.expolis.mobileapp.planner;

import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.lang.ref.WeakReference;
import java.util.List;

import pt.expolis.mobileapp.MainActivity;
import pt.expolis.mobileapp.R;

public class PlannerFragment extends Fragment
{
	/**
	 * Map widget where the user can select the start and end locations and where we can see the
	 * result of the plan.
	 */
	private MapView map;

	UpdatePlanLocationsHandler handler;
	Polyline route;
	boolean addedRoute;

	public static PlannerFragment newInstance ()
	{
		return new PlannerFragment ();
	}

	@Override
	public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                          @Nullable Bundle savedInstanceState)
	{
		View result = inflater.inflate (R.layout.planner_fragment, container, false);
		this.handler = new UpdatePlanLocationsHandler (this);
		Spinner s = result.findViewById (R.id.routingProfileModeSpinner);
		s.setSelection (MainActivity.plannerViewModel.routeMedium.ordinal (), false);
		s.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
			{
				MainActivity.plannerViewModel.routeMedium =
				  RouteMedium.values () [s.getSelectedItemPosition ()];
				PlannerFragment.this.computeRoute ();
			}

			@Override
			public void onNothingSelected (AdapterView<?> parent)
			{
			}
		});
		CheckBox cb = result.findViewById (R.id.routingProfilePollutionCheckBox);
		cb.setChecked (MainActivity.plannerViewModel.avoidPollution);
		cb.setOnClickListener (v -> {
			MainActivity.plannerViewModel.avoidPollution = cb.isChecked ();
			this.computeRoute ();
		});
		this.setupMapLocation (result);
		this.computeRoute ();
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
		MainActivity.plannerViewModel.save (
		  this.requireActivity ().getPreferences (Context.MODE_PRIVATE)
		);
		// needed for compass, my location overlays, v6.0.0 and up
		this.map.onPause ();
	}

	/**
	 * Setup the osmdroid map widget to edit the cell location and radius.
	 *
	 * @param view the view where the widget is located.
	 */
	private void setupMapLocation (View view)
	{
		this.map = view.findViewById (R.id.plannerMapView);
		this.map.setTileSource (TileSourceFactory.MAPNIK);
		this.map.setVerticalMapRepetitionEnabled (false);
		this.map.setHorizontalMapRepetitionEnabled (false);
		this.map.setMultiTouchControls (true);
		this.map.post (() -> {
			double offset = 0.01;
			BoundingBox bb = new BoundingBox (
			  Math.max (
				 MainActivity.plannerViewModel.startLocation.getLatitude (),
				 MainActivity.plannerViewModel.endLocation.getLatitude ())
				 + offset,
			  Math.max (
				 MainActivity.plannerViewModel.startLocation.getLongitude (),
				 MainActivity.plannerViewModel.endLocation.getLongitude ())
				 + offset,
			  Math.min (
				 MainActivity.plannerViewModel.startLocation.getLatitude (),
				 MainActivity.plannerViewModel.endLocation.getLatitude ())
				 - offset,
			  Math.min (
				 MainActivity.plannerViewModel.startLocation.getLongitude (),
				 MainActivity.plannerViewModel.endLocation.getLongitude ())
				 - offset);
			this.map.zoomToBoundingBox (bb, false);
		});
		this.map.getOverlayManager ().clear ();
		// add a scale in the top
		final Context context = view.getContext ();
		final DisplayMetrics dm = context.getResources ().getDisplayMetrics ();
		ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay (this.map);
		mScaleBarOverlay.setCentred (true);
		mScaleBarOverlay.setScaleBarOffset (dm.widthPixels / 2, 10);
		this.map.getOverlayManager ().add (mScaleBarOverlay);
		// initialise the start location marker
		this.setupMarker (
		  R.drawable.ic_p_planner_start,
		  MainActivity.plannerViewModel.startLocation
		);
		  // initialise the end location marker
		this.setupMarker (
		  R.drawable.ic_p_planner_end,
		  MainActivity.plannerViewModel.endLocation
		);
		// initialise the route layer
		this.route = new Polyline ();
		this.addedRoute = false;
	}

	private void setupMarker (int id, GeoPoint point)
	{
		// variables to get markers icon
		Context context = this.getContext ();
		Resources.Theme t = context.getTheme ();
		Resources r = context.getResources ();
		Drawable icon = ResourcesCompat.getDrawable (r, id, t);
		Marker result = new Marker (this.map);
		result.setPosition (point);
		result.setDraggable (true);
		result.setIcon (icon);
		result.setOnMarkerDragListener (new MarkerDragListener (point));
		result.setInfoWindow (null);
		this.map.getOverlays ().add (result);
	}
	/**
	 * Computes the best route between the start and end locations.
	 */
	private void computeRoute ()
	{
		new Thread (() -> {
			try {
				List<GeoPoint> route = OSRMClient.getRoute (
				  MainActivity.plannerViewModel.startLocation,
				  MainActivity.plannerViewModel.endLocation,
				  MainActivity.plannerViewModel.avoidPollution,
				  MainActivity.plannerViewModel.routeMedium
				  );
				Message.obtain (this.handler, MESSAGE_UPDATE_ROUTE, route).sendToTarget ();
			} catch (OSRMException ignored) {

			}
		}).start ();
	}

	/**
	 * Update the best route between the start and end locations.
	 *
	 * @param route the new best route.
	 */
	private void updateRoute (List<GeoPoint> route)
	{
		this.route.setPoints (route);
		if (!this.addedRoute) {
			this.addedRoute = true;
			this.map.getOverlayManager ().add (this.route);
		}
		this.map.invalidate ();
	}

	/**
	 * Listener responsible for updating a location of the planner and the corresponding text view.
	 * Updates are done when the user drags the marker in the map.
	 */
	private class MarkerDragListener
	  implements Marker.OnMarkerDragListener
	{
		/**
		 * The location of the planner to be updated.
		 */
		final GeoPoint location;

		/**
		 * @param location        the location of the planner to be updated.
		 */
		MarkerDragListener (GeoPoint location)
		{
			this.location = location;
		}

		/**
		 * Update the location and the text view after the marker has been dragged by the user.
		 *
		 * <p>Currently we only show the geographical coordinates of the marker.</p>
		 *
		 * @param marker the marker that was dragged.
		 */
		@Override
		public void onMarkerDrag (Marker marker)
		{
			this.location.setLongitude (marker.getPosition ().getLongitude ());
			this.location.setLatitude (marker.getPosition ().getLatitude ());
			PlannerFragment.this.computeRoute ();
		}

		@Override
		public void onMarkerDragEnd (Marker marker)
		{
		}

		@Override
		public void onMarkerDragStart (Marker marker)
		{
		}
	}

	private static final int MESSAGE_UPDATE_ROUTE = 3;

	/**
	 * Handler used by the threads that fetch data from a server to communicate their results to
	 * the user interface thread.
	 */
	private static class UpdatePlanLocationsHandler
	  extends Handler
	{
		private final WeakReference<PlannerFragment> plannerFragmentWeakReference;

		UpdatePlanLocationsHandler (PlannerFragment plannerFragment)
		{
			super (Looper.getMainLooper ());
			this.plannerFragmentWeakReference = new WeakReference<> (plannerFragment);
		}

		@Override
		public void handleMessage (@NotNull Message inputMessage)
		{
			PlannerFragment plannerFragment = this.plannerFragmentWeakReference.get ();
			if (plannerFragment != null) {
				//noinspection unchecked
				plannerFragment.updateRoute ((List<GeoPoint>) inputMessage.obj);
			}
		}
	}
}