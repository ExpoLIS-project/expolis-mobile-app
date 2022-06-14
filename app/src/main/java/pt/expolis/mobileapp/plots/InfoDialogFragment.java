package pt.expolis.mobileapp.plots;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pt.expolis.mobileapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoDialogFragment extends DialogFragment
{

	private static final String ARG_MESSAGE_ID = "KEY_MESSAGE_ID";

	private int messageId;

	public InfoDialogFragment ()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param messageId message identifier.
	 * @return A new instance of fragment InfoDialogFragment.
	 */
	public static InfoDialogFragment newInstance (int messageId)
	{
		InfoDialogFragment fragment = new InfoDialogFragment ();
		Bundle args = new Bundle ();
		args.putInt (ARG_MESSAGE_ID, messageId);
		fragment.setArguments (args);
		return fragment;
	}

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		if (getArguments () != null) {
			this.messageId = getArguments ().getInt (ARG_MESSAGE_ID);
		}
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View result = inflater.inflate (R.layout.fragment_info_dialog, container, false);
		TextView infoTextView = result.findViewById (R.id.infoTextView);
		infoTextView.setText (this.messageId);
		Button okButton = result.findViewById (R.id.okInfoButton);
		okButton.setOnClickListener (v -> InfoDialogFragment.this.dismiss ());
		return result;
	}
}