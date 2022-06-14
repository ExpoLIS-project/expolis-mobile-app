package pt.expolis.mobileapp.tutorial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pt.expolis.mobileapp.R;

/**
 * The fragment that displays the tutorial.
 *
 * <p>The tutorial consists in a series of pages, each with an app screen shot and a description.
 * </p>
 */
public class TutorialFragment extends Fragment
{
	/**
	 * The index of the page that is being shown.
	 */
	private int pageIndex;
	private TextView pageTextView;
	/**
	 * The image view that shows the app screen shot.
	 */
	private HighlightImageView highlightImageView;
	/**
	 * The description of what is shown on the image.
	 */
	private TextView descriptionTextView;
	public TutorialFragment ()
	{
		this.pageIndex = 0;
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 * @return A new instance of fragment TutorialFragment.
	 */
	public static TutorialFragment newInstance ()
	{
		return new TutorialFragment ();
	}

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View result = inflater.inflate (R.layout.fragment_tutorial, container, false);
		this.pageTextView = result.findViewById (R.id.tutorialPageTextView);
		// this.imageView = result.findViewById (R.id.tutorialImageView);
		this.highlightImageView = result.findViewById (R.id.highlightImageView);
		this.descriptionTextView = result.findViewById (R.id.tutorialDescriptionTextView);
		this.updatePage ();
		Button nextButton = result.findViewById (R.id.tutorialNextButton);
		Button previousButton = result.findViewById (R.id.tutorialPreviousButton);
		nextButton.setOnClickListener (v -> {
			if (pageIndex < Page.values ().length - 1) {
				pageIndex++;
				updatePage ();
				nextButton.setEnabled (pageIndex < Page.values ().length - 1);
				previousButton.setEnabled (true);
			}

		});
		previousButton.setOnClickListener (v -> {
			if (pageIndex > 0) {
				pageIndex--;
				updatePage ();
				previousButton.setEnabled (pageIndex > 0);
				nextButton.setEnabled (true);
			}
		});
		int v = Page.values ().length > 0 ? View.VISIBLE : View.GONE;
		nextButton.setVisibility (v);
		nextButton.setEnabled (this.pageIndex < Page.values ().length - 1);
		previousButton.setVisibility (v);
		previousButton.setEnabled (this.pageIndex > 0);
		return result;
	}

	private void updatePage ()
	{
		Page p = Page.values () [this.pageIndex];
		pageTextView.setText (this.getResources ().getString (
		  R.string.tutorial_page,
		  this.pageIndex + 1,
		  Page.values ().length
		));
		this.highlightImageView.setDrawable (Page.values () [this.pageIndex].image);
		if (p.tap_x != -1) {
			this.highlightImageView.animateHand (p.tap_x, p.tap_y);
		}
		if (p.highlight != null) {
			this.highlightImageView.highlightArea (p.highlight);
		}
		descriptionTextView.setText (Page.values () [this.pageIndex].description);
	}
}