package pt.expolis.mobileapp.plots;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import pt.expolis.mobileapp.database.Cache;
import utils.TimeUtils;

/**
 * A fragment that displays a calendar where the user can select a date to filter data.
 * The calendar dates are restricted to the minimum and maximum measurement dates.
 */
public class DatePickerFragment
  extends DialogFragment
{
	/**
	 * Listener used to communicate the date selected by the user.
	 */
	private DateSelectedListener dateSelectedListener;
	/**
	 * Which time to use in selected date.
	 */
	private Time time;
	/**
	 * The initial date to display in this dialog.
	 */
	private long initialTimestamp;

	public DatePickerFragment ()
	{
	}

	static public DatePickerFragment newInstance (
	  DateSelectedListener dateSelectedListener,
	  Time time, long initialTimestamp)
	{
		DatePickerFragment result = new DatePickerFragment ();
		result.dateSelectedListener = dateSelectedListener;
		result.time = time;
		result.initialTimestamp = initialTimestamp;
		return result;
	}

	@NotNull
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis (this.initialTimestamp);
		DatePickerDialog.OnDateSetListener event = (view, year, month, dayOfMonth) -> {
			long timestamp = 0;
			switch (DatePickerFragment.this.time) {
				case START_OF_DAY:
					timestamp = TimeUtils.utcTime (year, month, dayOfMonth, 0, 0, 0);
					break;
				case END_OF_DAY:
					timestamp = TimeUtils.utcTime (year, month, dayOfMonth, 23, 59, 59);
					break;
				}
			if (DatePickerFragment.this.dateSelectedListener != null) {
				DatePickerFragment.this.dateSelectedListener.setDate (
				  Math.max (
				    Cache.minMeasurementDate, Math.min (timestamp, Cache.maxMeasurementDate)));
			}
		};
		return new DatePickerDialog (
		  getActivity(), event,
		  calendar.get (Calendar.YEAR),
		  calendar.get (Calendar.MONTH),
		  calendar.get (Calendar.DAY_OF_MONTH)
		);
	}

	/**
	 * This interface is used to communicate the date chosen by this date picker fragment.
	 */
	public interface DateSelectedListener
	{
		/**
		 * Set the date.
		 * @param timestampMillis number of milliseconds from the epoch.
		 */
		void setDate (long timestampMillis);
	}

	/**
	 * Which time to use in selected date.
	 */
	public enum Time {
		START_OF_DAY,
		END_OF_DAY
	}
}
