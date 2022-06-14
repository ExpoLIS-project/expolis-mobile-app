package pt.expolis.mobileapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.online.MQTTClientSubscriber;
import utils.Debug;


/**
 * Welcome screen fragment where the initialisation phase is displayed.  The app shows a welcome
 * message, checks internet connection, connects to the database, MQTT and routing servers, and
 * caches data shown on the online data.  This fragment displays three status icons for each of
 * the three servers.  A text label is used to display initialisation progress.
 *
 * <p>If any of the three servers are unavailable, the corresponding functionalities are
 * disabled.  These functionalities are accessed through buttons in the online data fragment.</p>
 *
 * <p>The user can skip the welcome screen if the initialisation is taken too much time.  The
 * online data fragment will be shown, connection to the database, MQTT and routing servers will
 * proceed, and a corresponding functionality will be available once connection to the matching
 * server is established.</p>
 */
public class WelcomeScreenFragment extends Fragment
{
	private static final String STATE = "state";

	private TextView expoLISServerTextView;
	private TextView expoLISMQTTTextView;
	private TextView expoLISPlannerTextView;
	private TextView statusTextView;
	private TextView userAdviceTextView;
	private ImageView logoView;
	private Button proceedButton;
	private Button skipButton;
	private State state = State.START;
	private WelcomeScreenListener listener;
	final WelcomeScreenHandler handler = new WelcomeScreenHandler (this);
	private boolean internetAvailable = true;
	private DisplayedButton displayedButton = DisplayedButton.NONE;

	@Override
	public void onAttach (@NotNull Context context)
	{
		super.onAttach (context);
		try {
			this.listener = (WelcomeScreenListener) context;
		} catch (ClassCastException e) {
			throw new Error (context + " must implement WelcomeScreenListener");
		}
	}

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		Debug.toast (this.getContext (), String.format ("WelcomeScreenFragment.onCreate (%s)", savedInstanceState));
		super.onCreate (savedInstanceState);
		if (savedInstanceState != null) {
			this.state = State.values ()[savedInstanceState.getInt (STATE)];
		}
		if (PostGisServerDatabase.instance == null || MQTTClientSubscriber.instance == null) {
			new Thread (new WelcomeScreenTimeoutThread (this.handler))
			  .start ();
		}
		else {
			this.listener.closeWelcomeScreen ();
		}
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View result = inflater.inflate (R.layout.fragment_welcome_screen, container, false);
		this.expoLISServerTextView = result.findViewById (R.id.expoLISServerTextView);
		this.expoLISMQTTTextView = result.findViewById (R.id.expoLISMQTTTextView);
		this.expoLISPlannerTextView = result.findViewById (R.id.expoLISPlannerTextView);
		this.statusTextView = result.findViewById (R.id.statusTextView);
		this.statusTextView.setText (R.string.welcome_expolis);
		this.userAdviceTextView = result.findViewById (R.id.userAdviceTextView);
		this.logoView = result.findViewById (R.id.expolisLogoImageView);
		this.proceedButton = result.findViewById (R.id.proceedButton);
		this.skipButton = result.findViewById (R.id.skipButton);
		return result;
	}

	@Override
	public void onSaveInstanceState (Bundle outState)
	{
		outState.putInt (STATE, this.state.ordinal ());
		super.onSaveInstanceState (outState);
	}

	/**
	 * If the welcome screen background thread does not detect an internet connection, then the
	 * app is going to detect when this happens in order to start again the welcome screen
	 * background thread.
	 */
	private void waitForInternetConnection ()
	{
		Context c = this.getContext ();
		if (c == null) {
			return;
		}
		this.internetAvailable = false;
		ConnectivityManager cm =
		  (ConnectivityManager) c.getSystemService (Context.CONNECTIVITY_SERVICE);
		ConnectivityManager.NetworkCallback nc = new ConnectivityManager.NetworkCallback () {
			@Override
			public void onAvailable (Network network)
			{
				WelcomeScreenFragment.this.listener.restartInitialisationThread ();
				WelcomeScreenFragment.this.internetAvailable = true;
				new Thread (new WelcomeScreenTimeoutThread (WelcomeScreenFragment.this.handler))
				  .start ();
			}
		};
		cm.registerDefaultNetworkCallback (nc);
	}

	private void statusExpoLISServer (boolean status)
	{
		WelcomeScreenFragment.statusServer (this.expoLISServerTextView, status);
	}

	private void statusExpoLISMQTT (boolean status)
	{
		WelcomeScreenFragment.statusServer (this.expoLISMQTTTextView, status);
	}

	private void statusExpoLISPlanner (boolean status)
	{
		WelcomeScreenFragment.statusServer (this.expoLISPlannerTextView, status);
	}

	private static void statusServer (TextView textView, boolean status)
	{
		textView.setCompoundDrawablesRelativeWithIntrinsicBounds (
		  0, 0,
		  status ?
		    R.drawable.dr_server_on:
		    R.drawable.dr_server_off,
		  0);
	}
	synchronized private void enableProceedButton ()
	{
		if (this.displayedButton == DisplayedButton.NONE) {
			this.displayedButton = DisplayedButton.PROCEED;
			this.proceedButton.setVisibility (View.VISIBLE);
			this.proceedButton.setOnClickListener (v ->
			                                         WelcomeScreenFragment.this.listener.closeWelcomeScreen ()
			);
		}
	}

	private void enableAdmin ()
	{
		this.logoView.setOnClickListener (new View.OnClickListener ()
		{
			int remainingClicks = 7;
			final int THRESHOLD_FEEDBACK = 3;
			boolean launched = false;
			@Override
			public void onClick (View v)
			{
				if (launched) return ;
				remainingClicks--;
				if (remainingClicks == 0) {
					WelcomeScreenFragment.this.listener.launchAdmin ();
					launched = true;
				}
				else if (remainingClicks <= THRESHOLD_FEEDBACK) {
					String text = v.getResources ().getQuantityString (
					  R.plurals.steps_to_admin,
					  remainingClicks,
					  remainingClicks
					);
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText (v.getContext (), text, duration);
					toast.show ();
				}
			}
		});
	}

	synchronized private void enableSkip ()
	{
		if (this.internetAvailable && this.displayedButton == DisplayedButton.NONE) {
			this.displayedButton = DisplayedButton.SKIP;
			this.skipButton.setVisibility (View.VISIBLE);
			this.skipButton.setOnClickListener (
			  v -> WelcomeScreenFragment.this.listener.closeWelcomeScreen ());
		}
	}

	enum State
	{
		START,
		CONNECTED,
		FAILED,
	}

	/**
	 * Which button is displayed  if there is a problem with initialisation.
	 *
	 * <p>If initialisation takes too long, the skip button is showed that allows the user to skip
	 * the initialisation phase and go to the online data screen.  If any of the ExpoLIS servers
	 * is unavailable, a message is displayed warning the its corresponding functionalities will be
	 * disable. The user can then go to the online data screen. The user can also go the
	 * administration window if he taps seven times in the logo.</p>
	 */
	enum DisplayedButton
	{
		NONE,
		SKIP,
		PROCEED,
	}

	interface WelcomeScreenListener
	{
		void closeWelcomeScreen ();
		void restartInitialisationThread ();
		void launchAdmin ();
	}

	/**
	 * The handler used by the welcome screen background thread to communicate with the welcome
	 * screen graphical user interface.
	 *
	 * <p>Three types of messages are sent by the welcome screen background thread:
	 * <ol>
	 *    <li>update the status text view to inform the user of the background thread progress;</li>
	 *    <li>enable the administration mode so that the user can change app preferences;</li>
	 *    <li>close the welcome screen so that the app can proceed to the next step.</li>
	 * </ol></p>
	 */
	static class WelcomeScreenHandler
	  extends Handler
	{
		/**
		 * Reference to the welcome screen fragment used to update the graphical user interface,
		 * when a message is received or to close the welcome screen when the welcome screen
		 * background thread finishes.
		 */
		private final WeakReference<WelcomeScreenFragment> welcomeScreenFragmentWeakReference;

		WelcomeScreenHandler (WelcomeScreenFragment welcomeScreenFragment)
		{
			super (Looper.getMainLooper ());
			this.welcomeScreenFragmentWeakReference = new WeakReference<> (welcomeScreenFragment);
		}

		@Override
		public void handleMessage (@NotNull Message inputMessage)
		{
			WelcomeScreenFragment welcomeScreenFragment = welcomeScreenFragmentWeakReference.get ();
			if (welcomeScreenFragment != null) {
				switch (inputMessage.what) {
				case InitialisationThread.MESSAGE_PRINT:
					if (inputMessage.obj != null) {
						welcomeScreenFragment.statusTextView.setText ((String) inputMessage.obj);
					}
					else {
						welcomeScreenFragment.statusTextView.setText (inputMessage.arg1);
					}
					break;
				case InitialisationThread.MESSAGE_WAIT_FOR_INTERNET:
					welcomeScreenFragment.waitForInternetConnection ();
					break;
				case InitialisationThread.MESSAGE_STATUS_EXPOLIS_SERVER:
					welcomeScreenFragment.statusExpoLISServer (inputMessage.arg1 != 0);
					break;
				case InitialisationThread.MESSAGE_STATUS_EXPOLIS_MQTT:
					welcomeScreenFragment.statusExpoLISMQTT (inputMessage.arg1 != 0);
					break;
				case InitialisationThread.MESSAGE_STATUS_EXPOLIS_PLANNER:
					welcomeScreenFragment.statusExpoLISPlanner (inputMessage.arg1 != 0);
					break;
				case InitialisationThread.MESSAGE_ADVICE:
					if (inputMessage.obj != null) {
						welcomeScreenFragment.userAdviceTextView.setText ((String) inputMessage.obj);
					} else {
						welcomeScreenFragment.userAdviceTextView.setText (inputMessage.arg1);
					}
					welcomeScreenFragment.userAdviceTextView.setVisibility (View.VISIBLE);
					break;
				case InitialisationThread.MESSAGE_CLOSE:
					welcomeScreenFragment.listener.closeWelcomeScreen ();
					break;
				case InitialisationThread.MESSAGE_ENABLE_PROCEED:
					welcomeScreenFragment.enableProceedButton ();
					break;
				case InitialisationThread.MESSAGE_ENABLE_ADMIN:
					welcomeScreenFragment.enableAdmin ();
					break;
				case InitialisationThread.MESSAGE_ENABLE_SKIP:
					welcomeScreenFragment.enableSkip ();
					break;
				}
			}
		}
	}

	private static class WelcomeScreenTimeoutThread
	  implements Runnable
	{
		/**
		 * Handler used by thread to communicate its progress to the welcome screen graphical user
		 * interface.
		 */
		private final WelcomeScreenHandler handler;

		WelcomeScreenTimeoutThread (
		  WelcomeScreenHandler handler)
		{
			this.handler = handler;
		}

		@Override
		public void run ()
		{
			try {
				Thread.sleep (3000);
			}
			catch (InterruptedException ignored) {}
			Message.obtain (handler, InitialisationThread.MESSAGE_ENABLE_SKIP)
			  .sendToTarget ();
		}
	}
}
