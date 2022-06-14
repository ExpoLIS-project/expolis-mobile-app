package pt.expolis.mobileapp.online;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import org.jetbrains.annotations.NotNull;

import pt.expolis.mobileapp.AirQualityIndex;
import pt.expolis.mobileapp.PollutionProperties;
import pt.expolis.mobileapp.R;

/**
 * The view where pollution levels are displayed as bar charts.  This view draws two bar plots:
 * one with the CO and NO2 levels and another with the PM levels (particles with 1, 2.5, and 10 ㎛
 * of diameter).
 *
 * <p>There are a set of graphical elements </p>
 */
@SuppressWarnings("FieldCanBeLocal")
public class OnlineDataPlotView extends View
{
	private final SensorData sensorData = new SensorData ();

	// region graphical elements
	private final int plotText_color = Color.BLACK;
	// region label at the y axis of the plots
	private final float plots_YAxisLabel_textSize;
	private final Paint plots_YAxisLabel_paint = new Paint ();
	// label at the y axis of the CO and NO2 plot
	private final String plotGases_YAxisLabel_text = " ㎍/㎥";
	private final Matrix plotGases_YAxisLabel_matrix = new Matrix ();
	private final int plotGases_YAxisLabel_width;
	// label at the y axis of the PMs plot
	private final String plotPMs_YAxisLabel_text = " ㎍/㎥";
	private final Matrix plotPMs_YAxisLabel_matrix = new Matrix ();
	private final int plotPMs_YAxisLabel_width;
	// endregion
	// region x axis of the plots
	private float plots_XAxis_startStop;
	private final int plots_XAxis_color = Color.BLACK;
	private final int plots_XAxis_strokeWidth;
	private final Paint plots_XAxis_paint = new Paint ();
	// x axis of the CO and NO2 plot
	private float plotGases_XAxis_startX;
	private float plotGases_XAxis_stopX;
	// x axis of the PMs plot
	private float plotPMs_XAxis_startX;
	private float plotPMs_XAxis_stopX;
	// endregion
	// vertical grid lines of the plots
	private final int[] plots_VerticalGridLines_xs = new int[5];
	// region x tic icons of the plots
	private final VectorDrawable[] plots_XTicIcons_drawable = new VectorDrawable[5];
	private final Matrix[] plots_XTicIcons_matrices = new Matrix[]{new Matrix (), new Matrix (),
	  new Matrix (), new Matrix (), new Matrix (),};
	private final Rect plots_XTicIcons_bound = new Rect ();
	private final float plots_XTicIcons_lengthFactor = (float) (2 / (1 + Math.sqrt (5)));
	// endregion
	// region x tic labels of the plots
	private final String[] plots_XTicLabels_text = new String[]{"CO", "NO₂", "PM1", "PM2.5", "PM10"};
	private final Paint plots_XTicLabels_paint = new Paint ();
	private final int[] plots_XTicLabels_xs = plots_VerticalGridLines_xs;
	private int plots_XTicLabels_y;
	private final float plots_XTicLabels_textSize;
	private final int plots_XTicLabels_height;
	// endregion
	// region annotations of the plots
	private final String[] plots_Annotations_text = new String[5];
	private final int[] plots_Annotations_points_xs = plots_VerticalGridLines_xs;
	private final int[] plots_Annotations_points_ys = new int[5];
	private final Paint plots_Annotations_paint = new Paint ();
	private final int plots_Annotations_height;
	private final float plots_Annotations_textSize;
	// endregion
	// region air quality index of the plots
	private final VectorDrawable[] plots_AQIs_drawable = new VectorDrawable[5];
	private final Matrix[] plots_AQIs_matrices = new Matrix[]{new Matrix (), new Matrix (),
	  new Matrix (), new Matrix (), new Matrix (),};
	private int plots_AQIs_length;
	private final Rect plots_AQIs_bound = new Rect ();
	private final float plots_AQIs_lengthFactor = (float) (2 / (1 + Math.sqrt (5)));
	private final int[] plots_AQIs_xs = this.plots_VerticalGridLines_xs;
	// endregion
	// region bars of the plots
	private final int[] plots_Bars_lefts = new int[5];
	private final int[] plots_Bars_rights = new int[5];
	private final int[] plots_Bars_tops = new int[5];
	private int plots_Bars_bottom;
	private int plots_Bars_width;
	private int plots_Bars_maxHeight;
	private final Paint[] plots_Bars_fillPaints = new Paint[]{new Paint (), new Paint (), new Paint (), new Paint (), new Paint (),};
	private final Paint plots_Bars_outlinePaint = new Paint ();
	// endregion
	private final float[] plots_MaxValues = new float[2];
	private final Paint plots_Background = new Paint ();
	// endregion

	// region gaps between graphical elements...
	// ...horizontal
	private final int gap_MarginLeft_YAxisLabel;
	private final int gap_YAxisLabel_PlotBar;
	private final int gap_PlotBars;
	private final int gap_PlotBar_MarginRight;
	private final int gap_Plots;
	// ...vertical
	private final int gap_PlotAQI_PlotAnnotation;
	private final int gap_PlotAnnotation_PlotBar;
	private final int gap_XAxis_XTicIcon;
	private final int gap_XTicIcon_XTicLabel;
	// endregion

	private int paddingLeft, paddingTop, paddingRight, paddingBottom, contentWidth, contentHeight;
	private final Rect tBounds = new Rect ();


	public OnlineDataPlotView (Context context)
	{
		this (context, null, 0);
	}

	public OnlineDataPlotView (Context context, AttributeSet attrs)
	{
		this (context, attrs, 0);
	}

	public OnlineDataPlotView (Context context, AttributeSet attrs, int defStyle)
	{
		super (context, attrs, defStyle);
		int v;
		Resources.Theme t = context.getTheme ();
		Resources r = context.getResources ();
		AirQualityIndex.loadVectorDrawable (r, t);
		// region Load attributes
		final TypedArray a = getContext ().obtainStyledAttributes (
		  attrs, R.styleable.OnlineDataPlotView, defStyle, 0);
		this.sensorData.co = a.getInteger (R.styleable.OnlineDataPlotView_CO, 0);
		this.sensorData.no2 = a.getInteger (R.styleable.OnlineDataPlotView_NO2, 0);
		this.sensorData.pm1 = a.getInteger (R.styleable.OnlineDataPlotView_PM1, 0);
		this.sensorData.pm2_5 = a.getInteger (R.styleable.OnlineDataPlotView_PM2_5, 0);
		this.sensorData.pm10 = a.getInteger (R.styleable.OnlineDataPlotView_PM10, 0);
		// x tic labels of the plots
		this.plots_XTicLabels_textSize = a.getDimension (
		  R.styleable.OnlineDataPlotView_plots_x_tic_label_text_size, adv (12, context));
		this.plots_Annotations_textSize = a.getDimension (
		  R.styleable.OnlineDataPlotView_plots_annotations_text_size, adv (12, context));
		// y axis title
		this.plots_YAxisLabel_textSize = a.getDimension (
		  R.styleable.OnlineDataPlotView_plots_y_axis_title_text_size, adv (14, context));
		// horizontal gaps between graphical elements
		this.gap_MarginLeft_YAxisLabel = a.getInteger (
		  R.styleable.OnlineDataPlotView_gap_margin_left_y_axis_label, adv (4, context));
		this.gap_YAxisLabel_PlotBar = a.getInteger (
		  R.styleable.OnlineDataPlotView_gap_y_axis_label_plot_bar, adv (4, context));
		this.gap_PlotBars = a.getInteger (
		  R.styleable.OnlineDataPlotView_gap_plot_bars, adv (32, context));
		this.gap_PlotBar_MarginRight = a.getInteger (
		  R.styleable.OnlineDataPlotView_gap_plot_bar_margin_right, adv (24, context));
		this.gap_Plots = a.getInteger (
		  R.styleable.OnlineDataPlotView_gap_plots, adv (44, context));
		// vertical gaps between graphical elements
		this.gap_PlotAQI_PlotAnnotation = a.getDimensionPixelSize (
		  R.styleable.OnlineDataPlotView_gap_plot_aqi_plot_annotation, adv (4, context));
		this.gap_PlotAnnotation_PlotBar = a.getDimensionPixelSize (
		  R.styleable.OnlineDataPlotView_gap_plot_annotation_plot_bar, adv (4, context));
		this.gap_XAxis_XTicIcon = a.getDimensionPixelSize (
		  R.styleable.OnlineDataPlotView_gap_x_axis_x_tic_icon, adv (4, context));
		this.gap_XTicIcon_XTicLabel = a.getDimensionPixelSize (
		  R.styleable.OnlineDataPlotView_gap_x_tic_icon_x_tic_label, adv (4, context));
		a.recycle ();
		// endregion
		// region Graphical elements
		// label at the y axis of the plots
		this.plots_YAxisLabel_paint.setTextAlign (Paint.Align.LEFT);
		this.plots_YAxisLabel_paint.setTextSize (this.plots_YAxisLabel_textSize);
		this.plots_YAxisLabel_paint.setColor (this.plotText_color);
		// label at the y axis of the CO and NO2 plot
		this.plotGases_YAxisLabel_width = this.computeTextHeight (
		  this.plotGases_YAxisLabel_text,
		  this.plots_YAxisLabel_paint);
		// label at the y axis of the PMs plot
		this.plotPMs_YAxisLabel_width = this.computeTextHeight (
		  this.plotPMs_YAxisLabel_text,
		  this.plots_YAxisLabel_paint);
		// x axis of the plots
		this.plots_XAxis_paint.setColor (plots_XAxis_color);
		this.plots_XAxis_strokeWidth = adv (3, context);
		this.plots_XAxis_paint.setStrokeWidth (this.plots_XAxis_strokeWidth);
		// x tic icons of the plots
		this.plots_XTicIcons_drawable[0] =
		  (VectorDrawable) ResourcesCompat.getDrawable (r, R.drawable.ic_od_gas, t);
		this.plots_XTicIcons_drawable[1] = this.plots_XTicIcons_drawable[0];
		this.plots_XTicIcons_drawable[2] =
		  (VectorDrawable) ResourcesCompat.getDrawable (r, R.drawable.ic_od_pm1, t);
		this.plots_XTicIcons_drawable[3] =
		  (VectorDrawable) ResourcesCompat.getDrawable (r, R.drawable.ic_od_pm2_5, t);
		this.plots_XTicIcons_drawable[4] =
		  (VectorDrawable) ResourcesCompat.getDrawable (r, R.drawable.ic_od_pm10, t);
		// x tic labels of the plots
		this.plots_XTicLabels_paint.setTextAlign (Paint.Align.CENTER);
		this.plots_XTicLabels_paint.setTextSize (this.plots_XTicLabels_textSize);
		this.plots_XTicLabels_paint.setColor (this.plotText_color);
		v = 0;
		for (String s : this.plots_XTicLabels_text)
			v = Math.max (v, this.computeTextHeight (s, plots_XTicLabels_paint));
		this.plots_XTicLabels_height = v;
		// annotations of the plots
		this.plots_Annotations_paint.setTextAlign (Paint.Align.CENTER);
		this.plots_Annotations_paint.setTextSize (this.plots_Annotations_textSize);
		this.plots_Annotations_height = this.computeTextHeight (
		  "0123456789", this.plots_Annotations_paint);
		// other properties
		this.plots_Background.setColor (0xFFEEEEEE);
		// endregion
		this.computeGraphicalElementsPositionGradient ();
		this.updateGraphicalElementsPositionGradient ();
	}

	/**
	 * Adjusted default value in pixels.
	 *
	 * @param value the value in density units.
	 * @param c     the context where the view is created.
	 * @return Adjusted default value in pixels.
	 */
	private int adv (int value, Context c)
	{
		return Math.max (1, Math.round (value * c.getResources ().getDisplayMetrics ().density));
	}

	/**
	 * Compute the positions, matrix transformations and gradients of the graphical elements of the
	 * gases and PMs plots.
	 * This occurs when the object is constructed, when the view size changes, when the text size
	 * of an element is modified.
	 */
	private void computeGraphicalElementsPositionGradient ()
	{
		this.plots_Bars_width = (
		  this.contentWidth -
			 2 * this.gap_MarginLeft_YAxisLabel - 2 * this.gap_YAxisLabel_PlotBar
			 - 3 * this.gap_PlotBars - 2 * this.gap_PlotBar_MarginRight
			 - this.gap_Plots
			 - plotGases_YAxisLabel_width - plotPMs_YAxisLabel_width
		) / 5;
		this.plots_AQIs_length =
		  Math.round (this.plots_Bars_width * this.plots_AQIs_lengthFactor);
		final int plots_XTicIcons_length =
		  Math.round (this.plots_Bars_width * this.plots_XTicIcons_lengthFactor);
		this.plots_Bars_maxHeight =
		  this.contentHeight
			 - this.plots_AQIs_length - this.gap_PlotAQI_PlotAnnotation
			 - this.plots_Annotations_height - this.gap_PlotAnnotation_PlotBar
			 - this.plots_XAxis_strokeWidth
			 - this.gap_XAxis_XTicIcon - plots_XTicIcons_length - this.gap_XTicIcon_XTicLabel
			 - this.plots_XTicLabels_height;
		// region label at the y axis of the CO and NO2 plot
		this.plotGases_YAxisLabel_matrix.reset ();
		this.plotGases_YAxisLabel_matrix.postRotate (270);
		this.plotGases_YAxisLabel_matrix.postTranslate (
		  this.paddingLeft + this.gap_MarginLeft_YAxisLabel
			 + this.plotGases_YAxisLabel_width
			 - this.plots_YAxisLabel_paint.getFontMetrics ().descent,
		  this.paddingTop + this.plots_AQIs_length + this.gap_PlotAQI_PlotAnnotation
			 + this.plots_Annotations_height + this.gap_PlotAnnotation_PlotBar
			 + this.plots_Bars_maxHeight
		);
		// endregion
		// region label at the y axis of the PMs plot
		this.plotPMs_YAxisLabel_matrix.reset ();
		this.plotPMs_YAxisLabel_matrix.postRotate (270);
		this.plotPMs_YAxisLabel_matrix.postTranslate (
		  this.paddingLeft + this.gap_MarginLeft_YAxisLabel
			 + this.plotGases_YAxisLabel_width + this.gap_YAxisLabel_PlotBar
			 + 2 * this.plots_Bars_width + this.gap_PlotBars + this.gap_PlotBar_MarginRight
			 + this.gap_Plots + this.gap_MarginLeft_YAxisLabel
			 + this.plotPMs_YAxisLabel_width
			 - this.plots_YAxisLabel_paint.getFontMetrics ().descent,
		  this.paddingTop + this.plots_AQIs_length + this.gap_PlotAQI_PlotAnnotation
			 + this.plots_Annotations_height + this.gap_PlotAnnotation_PlotBar
			 + this.plots_Bars_maxHeight
		);
		// endregion
		// region x axis of the CO and NO2 plot
		this.plotGases_XAxis_startX = this.paddingLeft;
		this.plotGases_XAxis_stopX =
		  this.plotGases_XAxis_startX
			 + this.gap_MarginLeft_YAxisLabel + this.gap_YAxisLabel_PlotBar + this.gap_PlotBars
			 + this.gap_PlotBar_MarginRight
			 + this.plotGases_YAxisLabel_width
			 + 2 * this.plots_Bars_width;
		// endregion
		// region x axis of the PMs plot
		this.plotPMs_XAxis_startX =
		  this.plotGases_XAxis_stopX + this.gap_Plots;
		this.plotPMs_XAxis_stopX =
		  this.plotPMs_XAxis_startX
			 + this.gap_MarginLeft_YAxisLabel + this.plotGases_YAxisLabel_width
			 + this.gap_YAxisLabel_PlotBar + 2 * this.gap_PlotBars
			 + 3 * this.plots_Bars_width + this.gap_PlotBar_MarginRight;
		// endregion
		// region x axis of the plots
		this.plots_XAxis_startStop =
		  this.paddingTop + this.plots_AQIs_length + this.gap_PlotAQI_PlotAnnotation
			 + this.plots_Annotations_height + this.gap_PlotAnnotation_PlotBar
			 + this.plots_Bars_maxHeight
			 + this.plots_XAxis_strokeWidth / 2f;
		// endregion
		// region region vertical grid lines of the plots
		this.plots_VerticalGridLines_xs[0] =
		  this.paddingLeft + this.gap_MarginLeft_YAxisLabel + this.plotGases_YAxisLabel_width
			 + this.gap_YAxisLabel_PlotBar + this.plots_Bars_width / 2;
		this.plots_VerticalGridLines_xs[1] =
		  this.plots_VerticalGridLines_xs[0] + this.gap_PlotBars + this.plots_Bars_width;
		this.plots_VerticalGridLines_xs[2] =
		  this.plots_VerticalGridLines_xs[1] + this.gap_PlotBar_MarginRight + this.gap_Plots
			 + this.gap_MarginLeft_YAxisLabel + this.plotPMs_YAxisLabel_width
			 + this.gap_YAxisLabel_PlotBar + this.plots_Bars_width;
		this.plots_VerticalGridLines_xs[3] =
		  this.plots_VerticalGridLines_xs[2] + this.gap_PlotBars + this.plots_Bars_width;
		this.plots_VerticalGridLines_xs[4] =
		  this.plots_VerticalGridLines_xs[3] + this.gap_PlotBars + this.plots_Bars_width;
		// endregion
		// region x tic icons of the plots
		this.plots_XTicIcons_bound.bottom =
		  this.plots_XTicIcons_bound.right = plots_XTicIcons_length;
		for (int i = 0; i < 5; i++) {
			this.plots_XTicIcons_drawable[i].setBounds (this.plots_XTicIcons_bound);
			this.plots_XTicIcons_matrices[i].reset ();
			this.plots_XTicIcons_matrices[i].postTranslate (
			  this.plots_VerticalGridLines_xs[i] - plots_XTicIcons_length / 2f,
			  this.paddingTop + this.plots_AQIs_length + this.gap_PlotAQI_PlotAnnotation
				 + this.plots_Annotations_height + this.gap_PlotAnnotation_PlotBar
				 + this.plots_Bars_maxHeight
				 + this.plots_XAxis_strokeWidth + this.gap_XAxis_XTicIcon
			);
		}
		// endregion
		// region x tic labels of the plots
		this.plots_XTicLabels_y =
		  this.paddingTop + plots_AQIs_length + this.gap_PlotAQI_PlotAnnotation
			 + this.plots_Annotations_height + this.gap_PlotAnnotation_PlotBar
			 + this.plots_Bars_maxHeight + this.plots_XAxis_strokeWidth + this.gap_XAxis_XTicIcon
			 + plots_XTicIcons_length + this.gap_XTicIcon_XTicLabel
			 + this.plots_XTicLabels_height
			 - Math.round (this.plots_XTicLabels_paint.getFontMetrics ().descent)
		;
		// endregion
		// region air quality index of the plots
		this.plots_AQIs_bound.bottom = this.plots_AQIs_bound.right = plots_AQIs_length;
		for (AirQualityIndex aqi : AirQualityIndex.values ())
			aqi.getVectorDrawableOnlineDataBarPlot ().setBounds (this.plots_AQIs_bound);
		// endregion
		// region bars of the plots
		this.plots_Bars_lefts[0] =
		  this.paddingLeft + this.gap_MarginLeft_YAxisLabel + this.plotGases_YAxisLabel_width
			 + this.gap_YAxisLabel_PlotBar;
		this.plots_Bars_lefts[1] =
		  this.plots_Bars_lefts[0] + this.plots_Bars_width + this.gap_PlotBars;
		this.plots_Bars_lefts[2] =
		  this.plots_Bars_lefts[1] + this.plots_Bars_width + this.gap_PlotBar_MarginRight
			 + this.gap_Plots + this.gap_MarginLeft_YAxisLabel + this.plotPMs_YAxisLabel_width
			 + this.gap_YAxisLabel_PlotBar;
		this.plots_Bars_lefts[3] =
		  this.plots_Bars_lefts[2] + this.plots_Bars_width + this.gap_PlotBars;
		this.plots_Bars_lefts[4] =
		  this.plots_Bars_lefts[3] + this.plots_Bars_width + this.gap_PlotBars;
		for (int i = 0; i < 5; i++)
			this.plots_Bars_rights[i] = this.plots_Bars_lefts[i] + this.plots_Bars_width;
		this.plots_Bars_bottom =
		  this.paddingTop + plots_AQIs_length + this.gap_PlotAQI_PlotAnnotation
			 + this.plots_Annotations_height + this.gap_PlotAnnotation_PlotBar
			 + this.plots_Bars_maxHeight;
		for (PollutionProperties pp : PollutionProperties.values ()) {
			this.plots_MaxValues[pp.onlineDataPlot] = Math.max (
			  this.plots_MaxValues[pp.onlineDataPlot],
			  pp.data.maxValue);
		}
		for (PollutionProperties pp : PollutionProperties.values ()) {
			int i = pp.ordinal ();
			LinearGradient barGradient = new LinearGradient (
			  0, this.plots_Bars_bottom,
			  0, this.plots_Bars_bottom - this.plots_Bars_maxHeight,
			  new int[]{
			    AirQualityIndex.VERY_GOOD.colour,
			    AirQualityIndex.VERY_GOOD.colour,
			    AirQualityIndex.MEDIUM.colour,
			    AirQualityIndex.BAD.colour,
			    AirQualityIndex.BAD.colour,
			  },
			  new float[]{
			    0f,
			    pp.thresholdGreenYellow / this.plots_MaxValues[pp.onlineDataPlot],
				 pp.thresholdYellowRed / this.plots_MaxValues[pp.onlineDataPlot],
			    pp.data.maxValue  / this.plots_MaxValues[pp.onlineDataPlot],
			    1f
			  },
			  Shader.TileMode.CLAMP
			);
			this.plots_Bars_fillPaints[i].setShader (barGradient);
		}
		// endregion
	}

	/**
	 * Compute the positions, and matrix transformations of the animated graphical elements of the
	 * gases and PMs plots.
	 * This occurs when the object is constructed, when the view size changes, and when the sensor
	 * data is updated.
	 */
	private void updateGraphicalElementsPositionGradient ()
	{
		for (PollutionProperties pp : PollutionProperties.values ()) {
			int i = pp.ordinal ();
			int value = sensorData.getBarValue (pp);
			// bars of the plots
			this.plots_Bars_tops[i] =
			  Math.round (
				 this.plots_Bars_bottom
					- Math.min (value, this.plots_MaxValues[pp.onlineDataPlot])
					    * this.plots_Bars_maxHeight / this.plots_MaxValues[pp.onlineDataPlot]);
			// annotations of the plots
			this.plots_Annotations_text[i] = computeAnnotationText (
			  value,
			  this.plots_MaxValues[pp.onlineDataPlot]);
			this.plots_Annotations_points_ys[i] =
			  this.plots_Bars_tops[i] - this.gap_PlotAnnotation_PlotBar;
			// air quality index of the plots
			this.plots_AQIs_drawable[i] = pp.getQuality (sensorData).getVectorDrawableOnlineDataBarPlot ();
			this.plots_AQIs_matrices[i].reset ();
			this.plots_AQIs_matrices[i].postTranslate (
			  this.plots_AQIs_xs[i] - this.plots_AQIs_length / 2f,
			  this.plots_Annotations_points_ys[i]
				 - this.plots_AQIs_length
				 - this.gap_PlotAQI_PlotAnnotation
				 - this.plots_Annotations_height
			);
		}
	}

	/**
	 * Return the vertical span in pixels that the given text takes when drawn with the given paint
	 * object.  Does not take into account possible rotations and scales.
	 *
	 * @param text  the text object.
	 * @param paint the paint object.
	 * @return the vertical span in pixels that the given text takes when drawn with the given paint
	 * object.
	 */
	private int computeTextHeight (String text, @NotNull Paint paint)
	{
		paint.getTextBounds (text, 0, text.length (), this.tBounds);
		return this.tBounds.height ();
	}

	@Override
	protected void onSizeChanged (int width, int height, int oldWidth, int oldHeight)
	{
		super.onSizeChanged (width, height, oldWidth, oldHeight);
		this.paddingLeft = this.getPaddingLeft ();
		this.paddingRight = this.getPaddingRight ();
		this.paddingTop = this.getPaddingTop ();
		this.paddingBottom = this.getPaddingBottom ();
		this.contentWidth = width - this.paddingLeft - this.paddingRight;
		this.contentHeight = height - this.paddingTop - this.paddingBottom;
		this.computeGraphicalElementsPositionGradient ();
		this.updateGraphicalElementsPositionGradient ();
	}

	@Override
	protected void onDraw (Canvas canvas)
	{
		super.onDraw (canvas);
		canvas.drawRect (
		  0, 0,
		  this.paddingLeft + this.contentWidth + this.paddingRight,
		  this.paddingTop + this.contentHeight + this.paddingBottom,
		  this.plots_Background);
		for (PollutionProperties pp : PollutionProperties.values ()) {
			int index = pp.ordinal ();
			// region x tic icons of the plots
			canvas.save ();
			canvas.setMatrix (this.plots_XTicIcons_matrices[index]);
			this.plots_XTicIcons_drawable[index].draw (canvas);
			canvas.restore ();
			// endregion
			// region x tic labels of the plots
			canvas.drawText (
			  this.plots_XTicLabels_text[index],
			  this.plots_XTicLabels_xs[index], this.plots_XTicLabels_y,
			  this.plots_XTicLabels_paint);
			// endregion
			// region annotations of the plots
			canvas.drawText (
			  this.plots_Annotations_text[index],
			  this.plots_Annotations_points_xs[index], this.plots_Annotations_points_ys[index],
			  this.plots_Annotations_paint);
			// endregion
			// region air quality index of the plots
			canvas.save ();
			canvas.setMatrix (this.plots_AQIs_matrices[index]);
			this.plots_AQIs_drawable[index].draw (canvas);
			canvas.restore ();
			// endregion
			// region bars of the plots
			canvas.drawRect (
			  this.plots_Bars_lefts[index], this.plots_Bars_tops[index],
			  this.plots_Bars_rights[index], this.plots_Bars_bottom,
			  this.plots_Bars_fillPaints[index]);
			canvas.drawLine (
			  this.plots_Bars_lefts[index], this.plots_Bars_tops[index],
			  this.plots_Bars_rights[index], this.plots_Bars_tops[index],
			  this.plots_Bars_outlinePaint);
			canvas.drawLine (
			  this.plots_Bars_rights[index], this.plots_Bars_tops[index],
			  this.plots_Bars_rights[index], this.plots_Bars_bottom,
			  this.plots_Bars_outlinePaint);
			canvas.drawLine (
			  this.plots_Bars_rights[index], this.plots_Bars_bottom,
			  this.plots_Bars_lefts[index], this.plots_Bars_bottom,
			  this.plots_Bars_outlinePaint);
			canvas.drawLine (
			  this.plots_Bars_lefts[index], this.plots_Bars_bottom,
			  this.plots_Bars_lefts[index], this.plots_Bars_tops[index],
			  this.plots_Bars_outlinePaint);
			canvas.drawLine (
			  this.plots_Bars_lefts[index], this.plots_Bars_bottom - this.plots_Bars_maxHeight,
			  this.plots_Bars_rights[index], this.plots_Bars_bottom - this.plots_Bars_maxHeight,
			  this.plots_Bars_outlinePaint);
			canvas.drawLine (
			  this.plots_Bars_rights[index], this.plots_Bars_bottom - this.plots_Bars_maxHeight,
			  this.plots_Bars_rights[index], this.plots_Bars_bottom,
			  this.plots_Bars_outlinePaint);
			canvas.drawLine (
			  this.plots_Bars_rights[index], this.plots_Bars_bottom,
			  this.plots_Bars_lefts[index], this.plots_Bars_bottom,
			  this.plots_Bars_outlinePaint);
			canvas.drawLine (
			  this.plots_Bars_lefts[index], this.plots_Bars_bottom,
			  this.plots_Bars_lefts[index], this.plots_Bars_bottom - this.plots_Bars_maxHeight,
			  this.plots_Bars_outlinePaint);

			// endregion
		}
		// y axis label
		canvas.save ();
		canvas.setMatrix (this.plotGases_YAxisLabel_matrix);
		canvas.drawText (this.plotGases_YAxisLabel_text, 0, 0, this.plots_YAxisLabel_paint);
		canvas.setMatrix (this.plotPMs_YAxisLabel_matrix);
		canvas.drawText (this.plotPMs_YAxisLabel_text, 0, 0, this.plots_YAxisLabel_paint);
		canvas.restore ();
		// x axis
		//noinspection SuspiciousNameCombination
		canvas.drawLine (
		  this.plotGases_XAxis_startX, this.plots_XAxis_startStop,
		  this.plotGases_XAxis_stopX, this.plots_XAxis_startStop,
		  this.plots_XAxis_paint);
		//noinspection SuspiciousNameCombination
		canvas.drawLine (
		  this.plotPMs_XAxis_startX, this.plots_XAxis_startStop,
		  this.plotPMs_XAxis_stopX, this.plots_XAxis_startStop,
		  this.plots_XAxis_paint);
	}

	/**
	 * Sets the pollution levels that are displayed in this view.
	 *
	 * @param sensorData the sensor data with the new pollution levels.
	 */
	void setPollutionLevels (SensorData sensorData)
	{
		this.sensorData.co = sensorData.co;
		this.sensorData.no2 = sensorData.no2;
		this.sensorData.pm1 = sensorData.pm1;
		this.sensorData.pm2_5 = sensorData.pm2_5;
		this.sensorData.pm10 = sensorData.pm10;
		this.updateGraphicalElementsPositionGradient ();
		invalidate ();
		requestLayout ();
	}

	private String computeAnnotationText (int value, float maxValue)
	{
		return value > maxValue ? "> " + Math.round (maxValue) : String.valueOf (value);
	}
}
