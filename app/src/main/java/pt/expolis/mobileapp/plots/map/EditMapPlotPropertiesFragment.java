package pt.expolis.mobileapp.plots.map;

import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;

import pt.expolis.mobileapp.MainActivity;
import pt.expolis.mobileapp.R;
import pt.expolis.mobileapp.plots.CellSizeViewModel;
import pt.expolis.mobileapp.plots.InfoDialogFragment;
import pt.expolis.mobileapp.plots.LineIdViewModel;
import pt.expolis.mobileapp.plots.SelectDataFragment;
import pt.expolis.mobileapp.plots.StatisticsViewModel;
import pt.expolis.mobileapp.plots.TimePeriodViewModel;

public class EditMapPlotPropertiesFragment extends Fragment
{

	private ShowMapPlotListener listener;

	public static EditMapPlotPropertiesFragment newInstance ()
	{
		return new EditMapPlotPropertiesFragment ();
	}

	@Override
	public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                          @Nullable Bundle savedInstanceState)
	{
		View result = inflater.inflate (R.layout.fragment_edit_properties_map_plot, container, false);

		// initialise the widgets that contain the state of this fragment
		StatisticsViewModel.Setup.setupStatistics (result, MainActivity.mapPlotViewModel);
		SelectDataFragment.setupSpinner (result, MainActivity.mapPlotViewModel);
		CellSizeViewModel.Setup.setupCellSize (result, MainActivity.mapPlotViewModel);
		TimePeriodViewModel.Setup.setupWidgets (this, result, MainActivity.mapPlotViewModel);
		LineIdViewModel.Setup.setupLineIdSpinner (result, MainActivity.mapPlotViewModel);
		setupDataPresentation (result);
		// info buttons
		ImageButton ib;
		FragmentManager fragmentManager = this.getParentFragmentManager ();
		ib = result.findViewById (R.id.infoShowButton);
		ib.setOnClickListener (v -> {
			InfoDialogFragment infoDialogFragment =
			  InfoDialogFragment.newInstance (R.string.info_map_plot_show_statistic);
			infoDialogFragment.show (fragmentManager, null);
		});
		ib = result.findViewById (R.id.infoWhatDataButton);
		ib.setOnClickListener (v -> {
			InfoDialogFragment infoDialogFragment =
			  InfoDialogFragment.newInstance (R.string.info_map_plot_what_data);
			infoDialogFragment.show (fragmentManager, null);
		});
		ib = result.findViewById (R.id.infoFocusButton);
		ib.setOnClickListener (v -> {
			InfoDialogFragment infoDialogFragment =
			  InfoDialogFragment.newInstance (R.string.info_map_plot_focus);
			infoDialogFragment.show (fragmentManager, null);
		});
		ib = result.findViewById (R.id.infoCellSizeButton);
		ib.setOnClickListener (v -> {
			InfoDialogFragment infoDialogFragment =
			  InfoDialogFragment.newInstance (R.string.info_map_plot_cell_size);
			infoDialogFragment.show (fragmentManager, null);
		});
		// ok button
		Button b = result.findViewById (R.id.viewPlotButton);
		b.setOnClickListener (v -> {
			MainActivity.mapPlotViewModel.save (requireActivity ().getPreferences (Context.MODE_PRIVATE));
			listener.showMapPlotFragment ();
		});
		return result;
	}

	private void setupDataPresentation (View view)
	{
		Spinner whatDataSpinner = view.findViewById (R.id.what_spinner);
		Spinner locationsSpinner = view.findViewById (R.id.focus_spinner);
		Spinner lineSpinner = view.findViewById (R.id.line_spinner);
		EditText cellSizeEditText = view.findViewById (R.id.cellSizeEditText);
		whatDataSpinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected (AdapterView<?> adapterView, View view, int position, long l)
			{
				switch (position) {
					case 0:
						MainActivity.mapPlotViewModel.useRawData = true;
						break;
					case 1:
						MainActivity.mapPlotViewModel.useRawData = false;
						break;
					default:
						return;
				}
				locationsSpinner.setEnabled (MainActivity.mapPlotViewModel.useRawData);
				lineSpinner.setEnabled (MainActivity.mapPlotViewModel.useRawData && MainActivity.mapPlotViewModel.isFilterLine ());
				cellSizeEditText.setEnabled (MainActivity.mapPlotViewModel.useRawData);
			}
			@Override
			public void onNothingSelected (AdapterView<?> adapterView)
			{
			}
		});
		locationsSpinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected (AdapterView<?> adapterView, View view, int index, long l)
			{
				MainActivity.mapPlotViewModel.setFilterLine (index == 1);
				lineSpinner.setEnabled (MainActivity.mapPlotViewModel.isFilterLine ());
			}

			@Override
			public void onNothingSelected (AdapterView<?> adapterView)
			{
			}
		});
		lineSpinner.setEnabled (MainActivity.mapPlotViewModel.useRawData && MainActivity.mapPlotViewModel.isFilterLine ());
		cellSizeEditText.setEnabled (MainActivity.mapPlotViewModel.useRawData);
	}

	@Override
	public void onAttach (@NotNull Context context)
	{
		super.onAttach (context);
		try {
			this.listener = (ShowMapPlotListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException (context + " must implement ShowMapPlotListener");
		}
	}

	public interface ShowMapPlotListener
	{
		void showMapPlotFragment ();
	}
}
