package pt.expolis.mobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * This fragment is displayed to the user when the app is querying the Expolis database server.
 * For most queries this can take a couple of seconds.
 */
public class QueryingDatabaseFragment extends Fragment
{
	@Override
	public View onCreateView (
			  @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate (R.layout.fragment_querying_database, container, false);
	}
}
