package pt.expolis.mobileapp.tutorial;

import android.graphics.RectF;

import pt.expolis.mobileapp.R;

/**
 * Represents a page that is shown on the tutorial fragment.
 *
 * <p>Each page is composed of an image and a description. The image is an app screen capture,
 * with an highlighted area.</p>
 */
enum Page
{
	PAGE_01 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_overview
	),
	PAGE_ONLINE_DATA_OVERALL_STATE (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_overall_state,
	  70, 68, 70 + 57, 68 + 58
	),
	PAGE_02 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_time_bus,
	  132, 75, 132 + 49, 75 + 44
	),
	PAGE_03 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_gases,
	  11, 136, 11 + 69, 136 + 89
	),
	PAGE_04 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_pms,
	  90, 136, 90 + 96, 136 + 89
	),
	PAGE_05 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_temperature_humidity,
	  20, 75, 20 + 49, 75 + 44
	),
	PAGE_06 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_button_map_plot
	),
	PAGE_07 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_button_chart_plot
	),
	PAGE_08 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_button_planner,
	  152, 355
	),
	PAGE_09 (
	  R.drawable.tutorial_online_data,
	  R.string.tutorial_online_data_button_select_bus,
	  152, 355
	),
	PAGE_10 (
	  R.drawable.tutorial_map_plot_edit,
	  R.string.tutorial_map_plot_edit
	),
	PAGE_11 (
	  R.drawable.tutorial_map_plot_view,
	  R.string.tutorial_map_plot_view
	),
	PAGE_12 (
	  R.drawable.tutorial_chart_plot_edit,
	  R.string.tutorial_chart_plot_edit
	),
	PAGE_13 (
	  R.drawable.tutorial_chart_plot_view,
	  R.string.tutorial_chart_plot_view
	),
	PAGE_14 (
	  R.drawable.tutorial_route_planner,
	  R.string.tutorial_route_planner
	),
	;

	/**
	 * The resource ID of the image that is shown.
	 */
	final int image;
	/**
	 * The resource ID of the string that contains the description of this page.
	 */
	final int description;
	final int tap_y;
	final int tap_x;
	final RectF highlight;

	Page (int image, int description)
	{
		this (image, description, -1, -1);
	}

	Page (int image, int description, int tap_x, int tap_y)
	{
		this.image = image;
		this.description = description;
		this.tap_x = tap_x;
		this.tap_y = tap_y;
		this.highlight = null;
	}

	Page (int image, int description, float left, float top, float right,
	      float bottom)
	{
		this.image = image;
		this.description = description;
		this.tap_x = -1;
		this.tap_y = -1;
		this.highlight = new RectF (left, top, right, bottom);
	}

	Page (int image, int description, int tap_x, int tap_y, float left, float top, float right,
	      float bottom)
	{
		this.image = image;
		this.description = description;
		this.tap_x = tap_x;
		this.tap_y = tap_y;
		this.highlight = new RectF (left, top, right, bottom);
	}
}
