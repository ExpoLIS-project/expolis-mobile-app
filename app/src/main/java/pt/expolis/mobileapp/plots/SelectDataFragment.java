package pt.expolis.mobileapp.plots;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Data;


/**
 * A class to handle data selection.
 */
public class SelectDataFragment
{
	/**
	 * Setup the spinner to update th model when the user selects a data item.
	 * @param view the view where the spinner is contained.
	 * @param model the model where the data is stored.
	 */
	public static void setupSpinner (View view, DataViewModel model)
	{
		Spinner spinner = view.findViewById (R.id.data_spinner);
		String []names = new String [Data.values ().length];
		Resources r = view.getResources ();
		for (Data d : Data.values ()) {
			names [d.ordinal ()] = r.getString (d.resourceId);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<> (
		  view.getContext (),
		  android.R.layout.simple_spinner_dropdown_item,
		  names);
		spinner.setAdapter (adapter);
		spinner.setSelection (model.getData ().ordinal ());
		spinner.setOnItemSelectedListener (new OnItemSelectedListener (model));

	}
	/**
	 * Listener to update the model when the user selects a statistic item.
	 */
	private static class OnItemSelectedListener
			  implements android.widget.AdapterView.OnItemSelectedListener
	{
		final DataViewModel model;
		OnItemSelectedListener (DataViewModel model)
		{
			this.model = model;
		}
		@Override
		public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
		{
			this.model.setData (Data.values ()[position]);
		}

		@Override
		public void onNothingSelected (AdapterView<?> parent)
		{
		}
	}
}
