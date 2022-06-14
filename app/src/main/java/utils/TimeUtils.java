package utils;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Date and time utility methods.
 */
public class TimeUtils
{
	/**
	 * Return the time in milliseconds from the epoch of the given date.
	 * @param year the year of the date.
	 * @param month the month of the date
	 * @param day the day of the month of the date.
	 * @param hourOfDay hour of day of the date.
	 * @param minute minute of the date.
	 * @param second seconds of the date.
	 * @return the time in milliseconds from the epoch of the given date.
	 */
	static public long utcTime (int year, int month, int day, int hourOfDay, int minute, int second)
	{
		Calendar calendar = Calendar.getInstance ();
		calendar.set (year, month, day, hourOfDay, minute, second);
		return calendar.getTimeInMillis ();
	}

	/**
	 * Return a string representing the given date.
	 *
	 * <p>Timestamps in the database are represented as floating point numbers.  This method
	 * provides a way to obtain a human readable representation.</p>
	 *
	 * @param dateSeconds date in seconds from the epoch
	 *
	 * @return a string representation of the given date.
	 */
	static public String toString (double dateSeconds)
	{
		DateFormat df = DateFormat.getDateInstance (DateFormat.MEDIUM);
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis (Math.round (dateSeconds * 1000));
		return df.format (calendar.getTime ());
	}
}
