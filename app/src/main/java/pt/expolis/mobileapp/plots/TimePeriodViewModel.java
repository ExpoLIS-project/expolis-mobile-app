package pt.expolis.mobileapp.plots;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.DateFormat;
import java.util.Date;

import pt.expolis.mobileapp.R;

/**
 * Interface used by the view models of the plots to represent the get and set operations
 * on the to time period filter.
 * <p>All units are milliseconds.</p>
 * <p>All values in the interface methods parameter or interface method result are the number of
 * milliseconds since the epoch.</p>
 * <p>This interface has an inner class that contains a static method that setups the widgets
 * responsible for editing an object that implements this interface.</p>
 * @see java.util.Date
 */
public interface TimePeriodViewModel
{
	/**
	 * Gets the initial date of the time period.
	 * <p>Units are milliseconds. The returned value is the number of milliseconds since the
	 * epoch.</p>
	 * @return the initial date of the time period.
	 */
	long getFromDate ();
	/**
	 * Sets the initial date of the time period.
	 * <p>Units are milliseconds. The value should be the number of milliseconds since the
	 * epoch.</p>
	 * @param value the new initial date of the time period.
	 */
	void setFromDate (long value);
	/**
	 * Gets the last date of the time period.
	 * <p>Units are milliseconds. The returned value is the number of milliseconds since the
	 * epoch.</p>
	 * @return the last date of the time period.
	 */
	long getToDate ();

	/**
	 * Sets the last date of the time period.
	 * <p>Units are milliseconds. The value should be the number of milliseconds since the
	 * epoch.</p>
	 * @param value the new last date of the time period.
	 */
	void setToDate (long value);

	/**
	 * This class contains a single public static method that is responsible for setup the widgets
	 * that allows editing an object that implements the {@code TimePeriodViewModel} interface.
	 * There are two {@code TextView} widgets that display the initial and last date of the time
	 * period. Clicking on these widgets displays the {@code DatePickerFragment} which is a dialog
	 * fragment.  In it the user can pick a date.
	 * @see pt.expolis.mobileapp.plots.DatePickerFragment
	 */
	class Setup
	{
		/**
		 * If we are setting up the text fields that hold the time period initial and last date for
		 * the first time, we do not fill it. In this case the tool tip is visible.
		 */
		public static boolean firstSetup = true;

		/**
		 * Single method of this class to setup the widgets to edit a time period.
		 * @param parentFragment the fragment where the user can edit the time period.
		 * @param view the view that contains the widgets.
		 * @param model the time period view model.
		 */
		public static void setupWidgets (
		  Fragment parentFragment,
		  View view,
		  TimePeriodViewModel model)
		{
			TextView fromDateTextView = view.findViewById (R.id.fromDateEditText);
			TextView toDateTextView = view.findViewById (R.id.toDateEditText);
			DateFormat df = DateFormat.getDateInstance ();
			if (firstSetup) {
				firstSetup = false;
			}
			else {
				fromDateTextView.setText (df.format (new Date (model.getFromDate ())));
				toDateTextView.setText (df.format (new Date (model.getToDate ())));
			}
			fromDateTextView.setFocusable (false);
			toDateTextView.setFocusable (false);
			DatePickerFragment.DateSelectedListener fromDateListener = timestampMillis -> {
				model.setFromDate (timestampMillis);
				fromDateTextView.setText (df.format (new Date (timestampMillis)));
			};
			fromDateTextView.setOnClickListener (v -> {
				FragmentManager fragmentManager = parentFragment.getParentFragmentManager ();
				DatePickerFragment datePickerFragment = DatePickerFragment.newInstance (
				  fromDateListener,
				  DatePickerFragment.Time.START_OF_DAY,
				  model.getFromDate ()
				);
				datePickerFragment.show (fragmentManager, null);
			});
			DatePickerFragment.DateSelectedListener toDateListener = timestampMillis -> {
				model.setToDate (timestampMillis);
				toDateTextView.setText (df.format (new Date (timestampMillis)));
			};
			toDateTextView.setOnClickListener (v -> {
				FragmentManager fragmentManager = parentFragment.getParentFragmentManager ();
				DatePickerFragment datePickerFragment = DatePickerFragment.newInstance (
				  toDateListener,
				  DatePickerFragment.Time.END_OF_DAY,
				  model.getToDate ()
				);
				datePickerFragment.show (fragmentManager, null);
			});
		}
	}
}
