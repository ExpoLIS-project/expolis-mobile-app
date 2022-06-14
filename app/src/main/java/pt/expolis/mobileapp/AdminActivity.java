package pt.expolis.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import pt.expolis.mobileapp.database.PostGisServerDatabase;
import pt.expolis.mobileapp.online.MQTTClientSubscriber;
import pt.expolis.mobileapp.planner.Options;

public class AdminActivity extends AppCompatActivity
{

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_admin);
		EditText editText;
		editText = this.findViewById (R.id.expolis_sensor_database_host);
		editText.setText (PostGisServerDatabase.UrlOptions.host);
		editText.addTextChangedListener (new MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				PostGisServerDatabase.UrlOptions.host = s.toString ();
			}
		});
		editText = this.findViewById (R.id.expolis_sensor_database_port);
		editText.setText (Integer.toString (PostGisServerDatabase.UrlOptions.port));
		editText = this.findViewById (R.id.expolis_mqtt_broker_host);
		editText.setText (MQTTClientSubscriber.UrlOptions.host);
		editText.addTextChangedListener (new MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				MQTTClientSubscriber.UrlOptions.host = s.toString ();
			}
		});
		editText.setEnabled (!MQTTClientSubscriber.UrlOptions.useServerNode);
		editText = this.findViewById (R.id.expolis_mqtt_broker_port);
		editText.setText (Integer.toString (MQTTClientSubscriber.UrlOptions.port));
		editText.addTextChangedListener (new MyTextWatcher ()
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
		editText.setEnabled (!MQTTClientSubscriber.UrlOptions.useServerNode);
		editText = this.findViewById (R.id.expolis_route_planner_host);
		editText.setText (Options.host);
		editText.addTextChangedListener (new MyTextWatcher ()
		{
			@Override
			public void afterTextChanged (Editable s)
			{
				Options.host = s.toString ();
			}
		});
		editText = this.findViewById (R.id.expolis_route_planner_port);
		editText.setText (Integer.toString (Options.PROFILE_PORT));
		TextView textView;
		textView = this.findViewById (R.id.sensor_node_box_text_view);
		textView.setEnabled (MQTTClientSubscriber.UrlOptions.useServerNode);
		RadioButton rb1, rb2;
		rb1 = this.findViewById (R.id.userDefinedMQTTBrokerRadioButton);
		rb1.setChecked (!MQTTClientSubscriber.UrlOptions.useServerNode);
		rb2 = this.findViewById (R.id.expolis_sensor_node_mqtt_broker_RB);
		rb2.setChecked (MQTTClientSubscriber.UrlOptions.useServerNode);
		rb1.setOnClickListener (ignored -> {
			MQTTClientSubscriber.UrlOptions.useServerNode = false;
			rb2.setChecked (false);
			EditText et;
			et = findViewById (R.id.expolis_mqtt_broker_host);
			et.setEnabled (true);
			et = findViewById (R.id.expolis_mqtt_broker_port);
			et.setEnabled (true);
			View v;
			v = findViewById (R.id.sensor_node_box_text_view);
			v.setEnabled (false);
		});
		rb2.setOnClickListener (ignored -> {
			MQTTClientSubscriber.UrlOptions.useServerNode = true;
			rb1.setChecked (false);
			EditText et;
			et = findViewById (R.id.expolis_mqtt_broker_host);
			et.setEnabled (false);
			et = findViewById (R.id.expolis_mqtt_broker_port);
			et.setEnabled (false);
			View v;
			v = findViewById (R.id.sensor_node_box_text_view);
			v.setEnabled (true);
		});
		Button button;
		button = this.findViewById (R.id.relaunch_app_button);
		button.setOnClickListener (v -> launchExpoLISMobileApp ());
	}

	private void launchExpoLISMobileApp ()
	{
		this.saveOptions ();
		Intent intent = new Intent (this, MainActivity.class);
		this.startActivity (intent);
	}

	private void saveOptions ()
	{
		EditText editText;
		editText = this.findViewById (R.id.expolis_sensor_database_host);
		PostGisServerDatabase.UrlOptions.host = editText.getText ().toString ();
		editText = this.findViewById (R.id.expolis_mqtt_broker_host);
		MQTTClientSubscriber.UrlOptions.host = editText.getText ().toString ();
		editText = this.findViewById (R.id.expolis_mqtt_broker_port);
		try {
			MQTTClientSubscriber.UrlOptions.port = Integer.parseInt (editText.getText ().toString ());
		}
		catch (NumberFormatException ignored) { }
		RadioButton rb = this.findViewById (R.id.expolis_sensor_node_mqtt_broker_RB);
		MQTTClientSubscriber.UrlOptions.useServerNode = rb.isChecked ();
		editText = this.findViewById (R.id.expolis_route_planner_host);
		Options.host = editText.getText ().toString ();
		SharedPreferences preferences = this.getSharedPreferences (
		  MainActivity.class.getName (),
		  MODE_PRIVATE);
		PostGisServerDatabase.UrlOptions.save (preferences);
		MQTTClientSubscriber.UrlOptions.save (preferences);
		Options.save (preferences);
	}

	@Override
	public void onBackPressed ()
	{
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