package pt.expolis.mobileapp.database;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a bus line where a bus equipped with a sensor can travel.
 *
 * <p>The bus line description is the official description as given by Carris.  As such it is not
 * subject to translation.</p>
 *
 * <p>Bus lines can be sorted on their description.  They are listed in alphabetical order when
 * the user see a map graph focused on data collected near a specific bus line.</p>
 *
 * <p>Instances of this class correspond to rows in table {@code line_}.  Instances are created
 * when the app initialises.  They are stored in the cache.</p>
 *
 * @see Cache#busLines
 * @see LineSqlDao
 */
public class Line
	implements Comparable<Line>
{
	/**
	 * Primary key in table {@code line_}.
	 */
	final public int id;
	/**
	 * Official bus line description. Correspond to column {@code description} in table {@code
	 * line_}.
	 */
	final public String description;

	/**
	 * Sole constructor used to create an instance from data returned by the SQL query done at
	 * {@code LineSqlDao} class.
	 * @param id primary key in table {@code line_}.
	 * @param description official bus line description.
	 */
	public Line (int id, String description)
	{
		this.id = id;
		this.description = description;
	}

	@Override
	public int compareTo (Line line)
	{
		return this.description.compareTo (line.description);
	}

	@NotNull
	@Override
	public String toString ()
	{
		return this.description;
	}
}
