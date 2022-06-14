package pt.expolis.mobileapp.plots;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.Serializable;

import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.Statistics;

/**
 * Interface used by all graph view models to represent the get and set operations
 * on the statistics that is applied to the data on all graphs.
 */
public interface StatisticsViewModel
		  extends Serializable
{
	/**
	 * Get the statistics of the view model.
	 * @return the statistics of the view model.
	 */
	Statistics getStatistics ();

	/**
	 * Set the statistics of the view model.
	 * @param value the new statistics of the view model.
	 */
	void setStatistics (Statistics value);

	class Setup
	{
		public static void setupStatistics (View view, StatisticsViewModel model)
		{
			Spinner spinner = view.findViewById (R.id.statistics_spinner);
			String[] names = new String[Statistics.values ().length];
			Resources r = view.getResources ();
			for (Statistics s : Statistics.values ()) {
				names [s.ordinal ()] = r.getString (s.resourceId);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<> (
			  view.getContext (),
			  android.R.layout.simple_spinner_dropdown_item,
			  names);
			spinner.setAdapter (adapter);
			spinner.setSelection (model.getStatistics ().ordinal ());
			spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ()
			{
				@Override
				public void onItemSelected (AdapterView<?> adapterView, View view, int index, long l)
				{
					model.setStatistics (Statistics.values () [index]);
				}

				@Override
				public void onNothingSelected (AdapterView<?> adapterView)
				{
				}
			});
		}
	}
}
