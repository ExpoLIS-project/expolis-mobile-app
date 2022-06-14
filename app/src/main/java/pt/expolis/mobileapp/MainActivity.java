package pt.expolis.mobileapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.config.Configuration;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import pt.expolis.mobileapp.database.MapDataCell;
import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.database.ValueTime;
import pt.expolis.mobileapp.online.MQTTClientSubscriber;
import pt.expolis.mobileapp.online.OnlineDataFragment;
import pt.expolis.mobileapp.planner.OSRMClient;
import pt.expolis.mobileapp.planner.Options;
import pt.expolis.mobileapp.planner.PlannerFragment;
import pt.expolis.mobileapp.planner.PlannerViewModel;
import pt.expolis.mobileapp.plots.chart.ChartPlotViewModel;
import pt.expolis.mobileapp.plots.chart.EditChartPlotPropertiesFragment;
import pt.expolis.mobileapp.plots.chart.ViewChartPlotFragment;
import pt.expolis.mobileapp.plots.map.EditMapPlotPropertiesFragment;
import pt.expolis.mobileapp.plots.map.MapPlotViewModel;
import pt.expolis.mobileapp.plots.map.ViewMapPlotFragment;
import pt.expolis.mobileapp.tutorial.TutorialFragment;
import utils.Debug;

public class MainActivity
  extends
    AppCompatActivity
  implements
    WelcomeScreenFragment.WelcomeScreenListener,
    AdminFragment.AdminListener,
	 OnlineDataFragment.OnlineDataListener,
    EditMapPlotPropertiesFragment.ShowMapPlotListener,
    EditChartPlotPropertiesFragment.ShowChartPlotListener,
	 NoResultsFragment.NoResultsListener
{
	private static final String STATE = "_STATE_MAIN_ACTIVITY_";
	private State state = State.START;

	private MainActivityHandler handler;
	private InitialisationThread initialisationThread;
	private WelcomeScreenFragment welcomeScreenFragment;
	private OnlineDataFragment onlineDataFragment;

	public static MapPlotViewModel mapPlotViewModel;
	public static ChartPlotViewModel chartPlotViewModel;
	public static PlannerViewModel plannerViewModel;

	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		Debug.toast (this, "MainActivity(\n" + savedInstanceState + ")");
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_main);
		if (savedInstanceState != null) {
			state = State.values ()[savedInstanceState.getInt (STATE)];
		}
		Toast.makeText (this.getApplicationContext (), state.toString (), Toast.LENGTH_LONG)
		  .show ();

		SharedPreferences preferences = this.getSharedPreferences (
		  MainActivity.class.getName (),
		  MODE_PRIVATE);
		PostGisServerDatabase.UrlOptions.load (preferences);
		MQTTClientSubscriber.UrlOptions.load (preferences);
		Options.load (preferences);
		if (state == State.START ||
		      PostGisServerDatabase.instance == null ||
		      MQTTClientSubscriber.instance == null ||
		      !OSRMClient.serversAvailable
		) {
			addWelcomeScreenFragment ();
		}
		else {
			this.onlineDataFragment = new OnlineDataFragment ();
		}
		this.loadInitializeOsmdroidConfiguration ();
		handler = new MainActivityHandler (this);
		MainActivity.plannerViewModel = new PlannerViewModel (preferences);
		// region setup the action bar
		Toolbar myToolbar = this.findViewById (R.id.toolbar);
		setSupportActionBar (myToolbar);
		myToolbar.setLogo (R.drawable.logo_expolis_actionbar);
		ActionBar ab = this.getSupportActionBar ();
		assert ab != null;
		ab.setDisplayHomeAsUpEnabled (state != State.START);
		// endregion
	}

	private void loadInitializeOsmdroidConfiguration ()
	{
		Context ctx = getApplicationContext ();
		Configuration.getInstance ().load (ctx, PreferenceManager.getDefaultSharedPreferences (ctx));
	}

	@Override
	public void onSaveInstanceState (Bundle outState)
	{
		outState.putInt (STATE, this.state.ordinal ());
		super.onSaveInstanceState (outState);
		SharedPreferences preferences = this.getSharedPreferences (
		  MainActivity.class.getName (),
		  MODE_PRIVATE);
		if (MainActivity.mapPlotViewModel != null)
			MainActivity.mapPlotViewModel.save (preferences);
		if (MainActivity.chartPlotViewModel != null)
			MainActivity.chartPlotViewModel.save (preferences);
		MainActivity.plannerViewModel.save (preferences);
	}

	@Override
	public boolean onSupportNavigateUp ()
	{
		SharedPreferences preferences = this.getSharedPreferences (
		  MainActivity.class.getName (),
		  MODE_PRIVATE);
		switch (this.state) {
			case START:
			case ONLINE_DATA:
				return true;
			case EDIT_PLOT_MAP_PROPERTIES:
				MainActivity.mapPlotViewModel.save (preferences);
				this.addOnlineDataFragment ();
				return true;
			case EDIT_PLOT_CHART_PROPERTIES:
				MainActivity.chartPlotViewModel.save (preferences);
			case PLANNER:
				MainActivity.plannerViewModel.save (preferences);
			case TUTORIAL:
				this.addOnlineDataFragment ();
				return true;
			case SHOW_PLOT_MAP:
				this.showEditPlotMapPropertiesFragment ();
				return true;
			case SHOW_PLOT_CHART:
				this.showEditPlotChartPropertiesFragment ();
				return true;
		}
		return super.onSupportNavigateUp ();
	}

	@Override
	public void onBackPressed ()
	{
		SharedPreferences preferences = this.getSharedPreferences (
		  MainActivity.class.getName (),
		  MODE_PRIVATE);
		switch (this.state) {
			case START:
			case ADMIN:
				break;
			case ONLINE_DATA:
				super.finish ();
				break;
			case EDIT_PLOT_MAP_PROPERTIES:
				MainActivity.mapPlotViewModel.save (preferences);
				this.addOnlineDataFragment ();
				break;
			case EDIT_PLOT_CHART_PROPERTIES:
				MainActivity.chartPlotViewModel.save (preferences);
			case PLANNER:
				MainActivity.plannerViewModel.save (preferences);
			case TUTORIAL:
				this.addOnlineDataFragment ();
				break;
			case SHOW_PLOT_MAP:
				this.showEditPlotMapPropertiesFragment ();
				break;
			case SHOW_PLOT_CHART:
				this.showEditPlotChartPropertiesFragment ();
				break;
		}
	}

	@Override
	protected void onDestroy ()
	{
		super.onDestroy ();
		if (this.isFinishing ()) {
			new Thread (() -> {
				PostGisServerDatabase.disconnect ();
				MQTTClientSubscriber.disconnect ();

			}).start ();
		}
	}

	private void addWelcomeScreenFragment ()
	{
		this.state = State.START;
		this.onlineDataFragment = new OnlineDataFragment ();
		this.welcomeScreenFragment = new WelcomeScreenFragment ();
		FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, this.welcomeScreenFragment);
		transaction.commit ();
		this.initialisationThread = new InitialisationThread (
		  this.getApplicationContext (),
		  this.getResources (),
		  this.welcomeScreenFragment.handler,
		  this.onlineDataFragment.handler
		);
		new Thread (this.initialisationThread).start ();
	}

	private void addOnlineDataFragment ()
	{
		this.state = State.ONLINE_DATA;
		FragmentManager fm = getSupportFragmentManager ();
		if (!fm.isDestroyed () && !fm.isStateSaved ()) {
			FragmentTransaction transaction = fm.beginTransaction ();
			transaction.replace (R.id.fragment_container, this.onlineDataFragment);
			transaction.commit ();
		}
		ActionBar ab = this.getSupportActionBar ();
		//noinspection ConstantConditions
		ab.setDisplayHomeAsUpEnabled (false);
	}

	@Override
	public void closeWelcomeScreen ()
	{
		this.welcomeScreenFragment = null;
		this.initialisationThread.sendToOnlineData ();
		// the models can only be initialised after establishing a connection with the expolis
		// postgresql database
		SharedPreferences preferences = this.getSharedPreferences (
		  MainActivity.class.getName (),
		  MODE_PRIVATE);
		MainActivity.mapPlotViewModel = new MapPlotViewModel (preferences);
		MainActivity.chartPlotViewModel = new ChartPlotViewModel (preferences);
		switch (state) {
			case START:
			case ONLINE_DATA:
				this.addOnlineDataFragment ();
				break;
			case PLANNER:
				this.showPlannerFragment ();
				break;
			case TUTORIAL:
				this.showTutorialFragment ();
				break;
			case EDIT_PLOT_MAP_PROPERTIES:
				this.showEditPlotMapPropertiesFragment ();
				break;
			case EDIT_PLOT_CHART_PROPERTIES:
				this.showEditPlotChartPropertiesFragment ();
				break;
			case SHOW_PLOT_MAP:
			case SHOW_PLOT_CHART:
				FragmentManager fm = getSupportFragmentManager ();
				fm.popBackStack ();
				break;
			case ADMIN:
				break;
		}
	}

	@Override
	public void restartInitialisationThread ()
	{
		this.initialisationThread = new InitialisationThread (
		  this.getApplicationContext (),
		  this.getResources (),
		  this.welcomeScreenFragment.handler,
		  this.onlineDataFragment.handler
		);
		new Thread (this.initialisationThread).start ();
	}

	@Override
	public void launchAdmin ()
	{
		this.state = State.ADMIN;
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, new AdminFragment ());
		transaction.commit ();
	}

	@Override
	public void restartExpolisMobileApp ()
	{
		addWelcomeScreenFragment ();
	}

	@Override
	public void showEditPlotMapPropertiesFragment ()
	{
		this.state = State.EDIT_PLOT_MAP_PROPERTIES;
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, EditMapPlotPropertiesFragment.newInstance ());
		transaction.commit ();
		ActionBar ab = this.getSupportActionBar ();
		//noinspection ConstantConditions
		ab.setDisplayHomeAsUpEnabled (true);
	}

	@Override
	public void showEditPlotChartPropertiesFragment ()
	{
		this.state = State.EDIT_PLOT_CHART_PROPERTIES;
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, EditChartPlotPropertiesFragment.newInstance ());
		transaction.commit ();
		ActionBar ab = this.getSupportActionBar ();
		//noinspection ConstantConditions
		ab.setDisplayHomeAsUpEnabled (true);
	}
	public void showPlannerFragment ()
	{
		this.state = State.PLANNER;
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, PlannerFragment.newInstance ());
		transaction.commit ();
		ActionBar ab = this.getSupportActionBar ();
		//noinspection ConstantConditions
		ab.setDisplayHomeAsUpEnabled (true);
	}
	@Override
	public void showTutorialFragment ()
	{
		this.state = State.TUTORIAL;
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, TutorialFragment.newInstance ());
		transaction.commit ();
		ActionBar ab = this.getSupportActionBar ();
		//noinspection ConstantConditions
		ab.setDisplayHomeAsUpEnabled (true);
	}

	private static final int MESSAGE_SHOW_MAP_PLOT = 1;
	private static final int MESSAGE_SHOW_CHART_PLOT = 2;

	/**
	 * Class used by threads that communicate their results to the {@code MainActivity}.
	 * <p>Solves the leak context problem.</p>
	 *
	 * @see <a href="https://www.androiddesignpatterns.com/2013/01/inner-class-handler-memory-leak.html">How to Leak a Context: Handlers & Inner Classes</a>
	 */
	private static class MainActivityHandler
	  extends Handler
	{
		private final WeakReference<MainActivity> mainActivityWeakReference;
		MainActivityHandler (MainActivity mainActivity)
		{
			super (Looper.getMainLooper ());
			mainActivityWeakReference = new WeakReference<> (mainActivity);
		}
		@Override
		public void handleMessage (@NotNull android.os.Message inputMessage)
		{
			MainActivity od = mainActivityWeakReference.get ();
			if (od != null) {
				switch (inputMessage.what) {
					case MESSAGE_SHOW_MAP_PLOT:
						//noinspection unchecked
						od.showMapPlotFragmentReal ((LinkedList<MapDataCell>) inputMessage.obj);
						break;
					case MESSAGE_SHOW_CHART_PLOT:
						//noinspection unchecked
						od.showChartPlotFragmentReal ((LinkedList<ValueTime>) inputMessage.obj);
						break;
				}
			}
		}
	}

	/**
	 * Method called by the edit map plot properties fragment when the user presses the
	 * button to view the plot.
	 */
	@Override
	public void showMapPlotFragment ()
	{
		// display the querying database message
		this.showQueryingDatabaseFragment ();
		// setup the thread to query the database
		Thread t = new Thread (() -> {
			LinkedList<MapDataCell> mapDataCells = MainActivity.mapPlotViewModel.queryDatabase ();
			android.os.Message.obtain (handler, MESSAGE_SHOW_MAP_PLOT, mapDataCells)
			  .sendToTarget ();
		});
		t.start ();
	}
	private void showMapPlotFragmentReal (LinkedList<MapDataCell> mapDataCells)
	{
		Fragment fragment;
		if (mapDataCells.isEmpty ()) {
			fragment = NoResultsFragment.newInstance ();
		}
		else {
			this.state = State.SHOW_PLOT_MAP;
			fragment = ViewMapPlotFragment.newInstance (mapDataCells);
		}
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, fragment);
		transaction.commit ();
	}

	/**
	 * Uses the values that the user has entered to query the ExpoLIS database to fetch the data
	 * to the chart plot.
	 */
	@Override
	public void showChartPlotFragment ()
	{
		// display the querying database message
		this.showQueryingDatabaseFragment ();
		// setup the thread to query the database
		Thread t = new Thread (() -> {
			LinkedList<ValueTime> valuesTime = MainActivity.chartPlotViewModel.queryDatabase ();
			android.os.Message.obtain (handler, MESSAGE_SHOW_CHART_PLOT, valuesTime)
			  .sendToTarget ();
		});
		t.start ();
	}
	private void showChartPlotFragmentReal (LinkedList<ValueTime> valuesTime)
	{
		Fragment fragment;
		if (valuesTime.isEmpty ()) {
			fragment = NoResultsFragment.newInstance ();
		}
		else {
			this.state = State.SHOW_PLOT_CHART;
			fragment = ViewChartPlotFragment.newInstance (valuesTime);
		}
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, fragment);
		transaction.commit ();
	}

	/**
	 * Display the <i>querying database</i> fragment.
	 */
	private void showQueryingDatabaseFragment ()
	{
		QueryingDatabaseFragment fragment = new QueryingDatabaseFragment ();
		FragmentTransaction transaction = this.getSupportFragmentManager ().beginTransaction ();
		transaction.replace (R.id.fragment_container, fragment);
		transaction.commit ();
	}

	@Override
	public void goBackEditProperties ()
	{
		switch (this.state) {
			case START:
			case ONLINE_DATA:
			case PLANNER:
			case SHOW_PLOT_MAP:
			case SHOW_PLOT_CHART:
			case TUTORIAL:
				break;
			case EDIT_PLOT_MAP_PROPERTIES:
				this.showEditPlotMapPropertiesFragment ();
				break;
			case EDIT_PLOT_CHART_PROPERTIES:
				this.showEditPlotChartPropertiesFragment ();
				break;
		}
	}

	enum State
	{
		START,
		ONLINE_DATA,
		EDIT_PLOT_MAP_PROPERTIES,
		EDIT_PLOT_CHART_PROPERTIES,
		PLANNER,
		TUTORIAL,
		SHOW_PLOT_MAP,
		SHOW_PLOT_CHART,
		ADMIN,
	}
}
