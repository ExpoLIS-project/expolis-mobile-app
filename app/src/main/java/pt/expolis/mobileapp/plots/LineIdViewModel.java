package pt.expolis.mobileapp.plots;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Cache;
import pt.expolis.mobileapp.database.Line;

/**
 * Interface used by all graph view models that use a bus line to filter data.
 */
public interface LineIdViewModel
{
	/**
	 * Gets the identification of the current bus line.
	 * @return the identification of the bus line.
	 */
	int getLineId ();

	/**
	 * Sets the identification of the bus line.
	 * @param value the new identification of the bus line.
	 */
	void setLineId (int value);

	/**
	 * Setups the widget where the user can select the the bus line.
	 * Initialise the widget with the bus line the model has, and add a listener to update the
	 * model when the user changes the widget.
	 */
	class Setup
	{
		public static void setupLineIdSpinner (View view, LineIdViewModel model)
		{
			Line[] lines = new Line [Cache.busLines.size ()];
			Cache.busLines.toArray (lines);
			Spinner lineDescriptionSpinner = view.findViewById (R.id.line_spinner);
			ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<> (
			  view.getContext (),
			  android.R.layout.simple_spinner_dropdown_item,
			  lines);
			lineDescriptionSpinner.setAdapter (arrayAdapter);
			for (int index = 0; index < lines.length; index++) {
				if (lines [index].id == model.getLineId ()) {
					lineDescriptionSpinner.setSelection (index);
					break;
				}
			}
			lineDescriptionSpinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ()
			{
				@Override
				public void onItemSelected (AdapterView<?> adapterView, View view, int position, long l)
				{
					int lineId = lines [position].id;
					model.setLineId (lineId);

				}

				@Override
				public void onNothingSelected (AdapterView<?> adapterView)
				{

				}
			});
		}
	}
}
