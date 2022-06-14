package pt.expolis.mobileapp.online;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Cache;
import pt.expolis.mobileapp.database.SensorNode;

/**
 * A dialog fragment that allows the user to select the bus number (actually the sensor node) to receive sensor data.
 */
public class SelectBusNumberFragment extends DialogFragment
{
	SelectBusNumberListener listener;

	public SelectBusNumberFragment ()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment SelectBusNumberFragment.
	 */
	public static SelectBusNumberFragment newInstance (SelectBusNumberListener listener)
	{
		SelectBusNumberFragment result = new SelectBusNumberFragment ();
		result.listener = listener;
		return result;
	}

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate (R.layout.fragment_select_bus_number, container, false);
	}

	@NotNull
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder (getActivity ());
		builder.setTitle (R.string.select_bus_number)
		  .setItems (
		    getBusNumbers (),
		    (dialog, which) -> listener.selectSensorNodeId (Cache.sensorNodes.get (which)));
		return builder.create ();
	}

	private String[] getBusNumbers ()
	{
		String []result = new String [Cache.sensorNodes.size ()];
		int index = 0;
		for (SensorNode s: Cache.sensorNodes) {
			result [index++] = s.description;
		}
		return result;
	}

	interface SelectBusNumberListener
	{
		void selectSensorNodeId (@NotNull SensorNode sensorNode);
	}
}
