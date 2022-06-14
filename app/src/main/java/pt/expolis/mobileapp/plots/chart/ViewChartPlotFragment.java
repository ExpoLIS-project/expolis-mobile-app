package pt.expolis.mobileapp.plots.chart;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;

import pt.expolis.mobileapp.MainActivity;
import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.database.ValueTime;

/**
 * The fragment that shows a chart plot.  Sensor data is plotted against time.
 */
public class ViewChartPlotFragment extends Fragment
{
	// fragment initialization parameter
	private static final String ARG_VALUES_TIME = "ARG_VALUES_TIME";

	public ViewChartPlotFragment ()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param valuesTime@return A new instance of fragment ViewChartPlotFragment.
	 */
	public static ViewChartPlotFragment newInstance (LinkedList<ValueTime> valuesTime)
	{
		ViewChartPlotFragment fragment = new ViewChartPlotFragment ();
		Bundle args = new Bundle ();
		args.putSerializable (ARG_VALUES_TIME, valuesTime);
		fragment.setArguments (args);
		return fragment;
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View result = inflater.inflate (R.layout.fragment_view_chart_plot, container, false);
		this.setupGraph (MainActivity.chartPlotViewModel, result.findViewById (R.id.chartPlotGraphView));
		TextView propertiesTextView = result.findViewById (R.id.propertiesTextView);
		propertiesTextView.setText (MainActivity.chartPlotViewModel.getProperties (this.getResources ()));
		return result;
	}

	private void setupGraph (ChartPlotViewModel model, GraphView graphView)
	{
		Resources r = this.getResources ();
		float textSize = graphView.getGridLabelRenderer ().getTextSize ();
		graphView.setTitle (r.getString (R.string.cell_data_vs_time_title));
		graphView.setTitleTextSize (1.5f * textSize);
		LineGraphSeries<ValueTime> s = this.computeSeries ();
		graphView.addSeries (s);
		// set the vertical axis title
		graphView.getGridLabelRenderer ().setVerticalAxisTitle (
		  r.getString (
			 R.string.cell_data_chart_vertical_axis_title,
			 r.getString (model.getStatistics ().resourceId),
			 r.getString (model.getData ().resourceId),
		    model.getData ().units));
		graphView.getGridLabelRenderer ().setVerticalAxisTitleTextSize (1.25f * graphView.getGridLabelRenderer ().getTextSize ());
		// adjust horizontal axis range
		double minX = s.getLowestValueX ();
		double maxX = s.getHighestValueX ();
		if (maxX - minX > 1000.0 * 60 * 60 * 24 * 27) {
			minX -= 1000.0 * 60 * 60 * 24 * 3;
			maxX += 1000.0 * 60 * 60 * 24 * 3;
		} else if (maxX - minX > 1000.0 * 60 * 60 * 24 * 7) {
			minX -= 1000d * 60 * 60 * 24 * 1;
			maxX += 1000d * 60 * 60 * 24 * 1;
		} else if (maxX - minX > 1000.0 * 60 * 60 * 24) {
			minX -= 1000.0 * 60 * 60 * 12;
			maxX += 1000.0 * 60 * 60 * 12;
		} else {
			minX -= 1000.0 * 60 * 60 * 1;
			maxX += 1000.0 * 60 * 60 * 1;
		}
		graphView.getGridLabelRenderer ().setPadding (50);
		// set date label formatter
		boolean shortDateTimeFlag = maxX - minX > 1000.0 * 60 * 60 * 24;
		String dateTimePattern = android.text.format.DateFormat.getBestDateTimePattern (
		  Locale.getDefault (),
		  shortDateTimeFlag ? "yy/M/d" : "yy/M/d HH:mm");
		DateFormat dateFormat = new SimpleDateFormat (dateTimePattern, Locale.getDefault ());
		graphView.getGridLabelRenderer ().setLabelFormatter (new DateAsXAxisLabelFormatter (this.getContext (), dateFormat));
		graphView.getGridLabelRenderer ().setNumHorizontalLabels (shortDateTimeFlag ? 3 : 2);
		graphView.getViewport ().setXAxisBoundsManual (true);
		graphView.getViewport ().setMaxX (maxX);
		graphView.getViewport ().setMinX (minX);
		// as we use dates as labels, the human rounding to nice readable numbers
		// is not necessary
		graphView.getGridLabelRenderer ().setHumanRounding (false);
	}

	@SuppressWarnings({"unchecked"})
	private LineGraphSeries<ValueTime> computeSeries ()
	{
		Bundle args = this.getArguments ();
		assert args != null;
		LinkedList<ValueTime> valuesTime =
		  (LinkedList<ValueTime>) args.getSerializable (ARG_VALUES_TIME);
		ValueTime[] dp = new ValueTime[valuesTime.size ()];
		valuesTime.toArray (dp);
		LineGraphSeries<ValueTime> result = new LineGraphSeries<> (dp);
		result.setDrawDataPoints (true);
		return result;
	}
}