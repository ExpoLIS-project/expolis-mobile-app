package pt.expolis.mobileapp.database;

import com.jjoe64.graphview.series.DataPointInterface;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * A data point in the plot graph.  A plot graph is a sensor data versus time plot.
 *
 * <p>In class {@code GraphsSqlDao} the methods that fetch data to a plot graph return a
 * collection of {@code ValueTime} instances.</p>
 *
 * <p>Instances of this class are serializable as the collection of data points are transmitted
 * to the fragment that displays it in a {@code android.os.Bundle} object.</p>
 *
 * @see GraphsSqlDao
 * @see pt.expolis.mobileapp.plots.chart.ViewChartPlotFragment#newInstance(LinkedList)
 */
public class ValueTime
  implements
  DataPointInterface,
  Serializable
{
	public long date_time;
	public double value;

	@Override
	public double getX ()
	{
		return this.date_time;
	}

	@Override
	public double getY ()
	{
		return this.value;
	}
}
