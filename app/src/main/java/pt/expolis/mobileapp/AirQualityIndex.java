package pt.expolis.mobileapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;

import androidx.core.content.res.ResourcesCompat;

/**
 * Used to represent the quality of a sensor value and the general air quality.
 * Each enum is associated with a bitmap that is shown to the user.
 */
public enum AirQualityIndex
  implements Comparable<AirQualityIndex>
{
	VERY_GOOD (
	  Color.GREEN,
	  R.string.air_quality_index_1_very_good,
	  R.drawable.ic_od_overall_state_1_good),
	MEDIUM (
	  Color.YELLOW,
	  R.string.air_quality_index_2_medium,
	  R.drawable.ic_od_overall_state_2_neutral),
	BAD (
	  Color.RED,
	  R.string.air_quality_index_3_bad,
	  R.drawable.ic_od_overall_state_3_bad),
	;

	/**
	 * The colour that represents this air quality index.
	 */
	public final int colour;
	/**
	 * The resource string with the description of this air quality index.
	 */
	public final int resourceID;
	/**
	 * The resource drawable with the SVG that characterises this air quality index.
	 */
	private final int resourceDrawableID;

	static private VectorDrawable[] drawablesOnlineDataBarPlot;
	static private VectorDrawable[] drawablesOnlineDataFragment;

	AirQualityIndex (int colour, int resourceStringID, int resourceDrawableID)
	{
		this.colour = colour;
		this.resourceID = resourceStringID;
		this.resourceDrawableID = resourceDrawableID;
	}

	static public void loadVectorDrawable (Resources r, Resources.Theme t)
	{
		int n = AirQualityIndex.values ().length;
		AirQualityIndex.drawablesOnlineDataBarPlot = new VectorDrawable[n];
		AirQualityIndex.drawablesOnlineDataFragment = new VectorDrawable[n];
		while (n > 0) {
			n--;
			AirQualityIndex aqi = AirQualityIndex.values () [n];
			AirQualityIndex.drawablesOnlineDataBarPlot [n] =
			  (VectorDrawable) ResourcesCompat.getDrawable (r, aqi.resourceDrawableID, t);
			AirQualityIndex.drawablesOnlineDataFragment [n] =
			  (VectorDrawable) ResourcesCompat.getDrawable (r, aqi.resourceDrawableID, t);
		}
	}

	/**
	 * Get the vector drawable that is used in the online data plot view.  This drawable is used
	 * to display the severity of a specific pollution.
	 * @return the vector drawable that is used in the online data plot view.
	 */
	public VectorDrawable getVectorDrawableOnlineDataBarPlot ()
	{
		return AirQualityIndex.drawablesOnlineDataBarPlot [this.ordinal ()];
	}

	/**
	 * Get the vector drawable that is used in the online data fragment.  This drawable is used to
	 * display the overall air quality index.
	 * @return the vector drawable that is used in the online data fragment.
	 */
	public VectorDrawable getVectorDrawableOnlineDataFragment ()
	{
		return AirQualityIndex.drawablesOnlineDataFragment [this.ordinal ()];
	}
}
