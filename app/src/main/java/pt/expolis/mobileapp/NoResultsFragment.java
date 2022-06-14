package pt.expolis.mobileapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

/**
 * A fragment shown when a plot query returns no data.
 */
public class NoResultsFragment extends Fragment
{
	private NoResultsListener listener;

	public NoResultsFragment ()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment.
	 *
	 * @return A new instance of fragment NoResultsFragment.
	 */
	public static NoResultsFragment newInstance ()
	{
		return new NoResultsFragment ();
	}

	@Override
	public View onCreateView (
	  LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View result = inflater.inflate (R.layout.fragment_no_results, container, false);
		Button b = result.findViewById (R.id.no_result_button);
		b.setOnClickListener (v -> listener.goBackEditProperties ());
		return result;
	}

	@Override
	public void onAttach (@NotNull Context context)
	{
		super.onAttach (context);
		try {
			this.listener = (NoResultsFragment.NoResultsListener) context;
		} catch (ClassCastException e) {
			throw new Error (
			  context + " must implement NoResultsFragment.NoResultsListener");
		}
	}

	interface NoResultsListener
	{
		/**
		 * Method invoked when the user presses the ok button in the <i>no results</i> fragment.
		 */
		void goBackEditProperties ();
	}
}
