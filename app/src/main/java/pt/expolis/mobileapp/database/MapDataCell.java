package pt.expolis.mobileapp.database;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Locale;

/**
 * A data point in the map graph.  A map graph consists of a map with coloured rectangles.
 * Information is represented with colour.
 *
 * <p>In class {@code GraphsSqlDao} the methods that fetch data to a map graph, return a
 * collection of {@code MapDataCell} instances.</p>
 *
 * <p>Instances of this class are serializable as the collection of data points is
 * transmitted to the fragment that displays it in a {@code android.os.Bundle} object.</p>
 *
 * @see GraphsSqlDao
 * @see pt.expolis.mobileapp.plots.map.ViewMapPlotFragment#newInstance(LinkedList)
 */
public class MapDataCell
  implements Serializable
{
	public double latitude;
	public double longitude;
	/**
	 * Data point value.  This value is used as the colour of the rectangle.
	 */
	public double value;
	@NotNull
	@Override
	public String toString ()
	{
		return String.format (
		  Locale.getDefault (),
		  "%.1f @ (%f, %f)",
		  this.value,
		  this.longitude,
		  this.latitude
		);
	}
}
