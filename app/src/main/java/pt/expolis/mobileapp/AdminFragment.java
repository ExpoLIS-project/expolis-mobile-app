package pt.expolis.mobileapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.online.MQTTClientSubscriber;
import pt.expolis.mobileapp.planner.Options;

/**
 * Fragment where the administrator can specify the ExpoLIS servers.  This fragment is shown on the
 * welcome screen after the user taps the ExpoLIS logo seven times.
 */
public class AdminFragment extends Fragment
{
	private EditText expolisSensorDatabaseHost;
	private EditText expolisSensorDatabasePort;
	private EditText expolisMqttBrokerHost;
	private EditText expolisMqttBrokerPort;
	private RadioButton expolisSensorNodeMqttBrokerRadioButton;
	private EditText expolisRoutePlannerHostEditText;
	private AdminListener listener;

	public AdminFragment ()
	{
		// Required empty public constructor
	}

	@Override
	public void onAttach (@NotNull Context context)
	{
		super.onAttach (context);
		try {
			this.listener = (AdminFragment.AdminListener) context;
		} catch (ClassCastException e) {
			throw new Error (context + " must implement AdminFragment.AdminListener");
		}
	}

	@SuppressLint("SetTextI18n")
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View result = inflater.inflate (R.layout.fragment_admin, container, false);
		this.expolisSensorDatabaseHost = result.findViewById (R.id.expolis_sensor_database_host);
		this.expolisSensorDatabaseHost.setText (PostGisServerDatabase.UrlOptions.host);
		this.expolisSensorDatabaseHost.addTextChangedListener (new AdminFragment.MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				PostGisServerDatabase.UrlOptions.host = s.toString ();
			}
		});
		this.expolisSensorDatabasePort = result.findViewById (R.id.expolis_sensor_database_port);
		this.expolisSensorDatabasePort.setText (Integer.toString (PostGisServerDatabase.UrlOptions.port));
		this.expolisSensorDatabasePort.addTextChangedListener (new MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				try {
					PostGisServerDatabase.UrlOptions.port = Integer.parseInt (s.toString ());
				}
				catch (NumberFormatException ignored) { }
			}
		});
		this.expolisMqttBrokerHost = result.findViewById (R.id.expolis_mqtt_broker_host);
		this.expolisMqttBrokerHost.setText (MQTTClientSubscriber.UrlOptions.host);
		this.expolisMqttBrokerHost.addTextChangedListener (new AdminFragment.MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				MQTTClientSubscriber.UrlOptions.host = s.toString ();
			}
		});
		this.expolisMqttBrokerHost.setEnabled (!MQTTClientSubscriber.UrlOptions.useServerNode);
		this.expolisMqttBrokerPort = result.findViewById (R.id.expolis_mqtt_broker_port);
		this.expolisMqttBrokerPort.setText (Integer.toString (MQTTClientSubscriber.UrlOptions.port));
		this.expolisMqttBrokerPort.addTextChangedListener (new AdminFragment.MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				try {
					MQTTClientSubscriber.UrlOptions.port = Integer.parseInt (s.toString ());
				}
				catch (NumberFormatException ignored) { }
			}
		});
		this.expolisMqttBrokerPort.setEnabled (!MQTTClientSubscriber.UrlOptions.useServerNode);
		this.expolisRoutePlannerHostEditText = result.findViewById (R.id.expolis_route_planner_host);
		this.expolisRoutePlannerHostEditText.setText (Options.host);
		this.expolisRoutePlannerHostEditText.addTextChangedListener (new AdminFragment.MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				Options.host = s.toString ();
			}
		});
		((EditText) result.findViewById (R.id.expolis_route_planner_port))
		  .setText (Integer.toString (Options.PROFILE_PORT));
		TextView sensorNodeBoxTextView = result.findViewById (R.id.sensor_node_box_text_view);
		sensorNodeBoxTextView.setEnabled (MQTTClientSubscriber.UrlOptions.useServerNode);
		RadioButton rb1;
		rb1 = result.findViewById (R.id.userDefinedMQTTBrokerRadioButton);
		rb1.setChecked (!MQTTClientSubscriber.UrlOptions.useServerNode);
		this.expolisSensorNodeMqttBrokerRadioButton = result.findViewById (R.id.expolis_sensor_node_mqtt_broker_RB);
		this.expolisSensorNodeMqttBrokerRadioButton.setChecked (MQTTClientSubscriber.UrlOptions.useServerNode);
		rb1.setOnClickListener (ignored -> {
			MQTTClientSubscriber.UrlOptions.useServerNode = false;
			this.expolisSensorNodeMqttBrokerRadioButton.setChecked (false);
			AdminFragment.this.expolisMqttBrokerHost.setEnabled (true);
			AdminFragment.this.expolisMqttBrokerPort.setEnabled (true);
			sensorNodeBoxTextView.setEnabled (false);
		});
		this.expolisSensorNodeMqttBrokerRadioButton.setOnClickListener (ignored -> {
			MQTTClientSubscriber.UrlOptions.useServerNode = true;
			rb1.setChecked (false);
			AdminFragment.this.expolisMqttBrokerHost.setEnabled (false);
			AdminFragment.this.expolisMqttBrokerPort.setEnabled (false);
			sensorNodeBoxTextView.setEnabled (true);
		});
		Button button;
		button = result.findViewById (R.id.relaunch_app_button);
		button.setOnClickListener (v -> restartExpolisMobileApp ());
		return result;
	}
	private void restartExpolisMobileApp ()
	{
		PostGisServerDatabase.UrlOptions.host = this.expolisSensorDatabaseHost.getText ().toString ();
		try {
			PostGisServerDatabase.UrlOptions.port = Integer.parseInt (
			  this.expolisSensorDatabasePort.getText ().toString ());
		}
		catch (NumberFormatException ignored) { }
		MQTTClientSubscriber.UrlOptions.host = this.expolisMqttBrokerHost.getText ().toString ();
		try {
			MQTTClientSubscriber.UrlOptions.port =
			  Integer.parseInt (this.expolisMqttBrokerPort.getText ().toString ());
		}
		catch (NumberFormatException ignored) { }
		MQTTClientSubscriber.UrlOptions.useServerNode = this.expolisSensorNodeMqttBrokerRadioButton.isChecked ();
		Options.host = this.expolisRoutePlannerHostEditText.getText ().toString ();
		Context context = this.getContext ();
		if (context != null) {
			SharedPreferences preferences = context.getSharedPreferences (
			  MainActivity.class.getName (),
			  Context.MODE_PRIVATE);
			PostGisServerDatabase.UrlOptions.save (preferences);
			MQTTClientSubscriber.UrlOptions.save (preferences);
			Options.save (preferences);
		}
		this.listener.restartExpolisMobileApp ();
	}

	/**
	 * Listener that the host activity has to implement. When the user clicks the relaunch app, we
	 * need to start the initialisation thread again.
	 */
	interface AdminListener
	{
		void restartExpolisMobileApp ();
	}

	private static abstract class MyTextWatcher
	  implements TextWatcher
	{

		@Override
		final public void beforeTextChanged (CharSequence s, int start, int count, int after)
		{
		}

		@Override
		final public void onTextChanged (CharSequence s, int start, int before, int count)
		{
		}
	}
}