package pt.expolis.mobileapp.plots;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import pt.expolis.mobileapp.R;

/**
 * Displays a horizontal gradient legend.  This legend is used by the raw data map and
 * interpolated data map plots.  The data in these plots is presented as squares drawn in
 * different colours, with each colour representing a specific value.
 *
 * <p>This component displays a gradient legend.  The attributes that can be controlled are:
 *
 * <ul>
 *    <li>the title text and font size;</li>
 *    <li>minimum and maximum values displayed in the legend;</li>
 *    <li>the number of tics in the legend. This is in turn affects which colours are used;</li>
 *    <li>tic label font size;</li>
 *    <li>tic mark dimension.</li>
 * </ul>
 * </p>
 *
 * <p>There is no attribute to control the height of the gradient bar.  This is computed after
 * factoring the height of the title, the tic mark dimension, the tic label height, and the top
 * and bottom padding.</p>
 *
 * <p>The number of colours used to draw the gradient is equal to the number of tics.  Which
 * colours are used depend on the number of tics:
 * <ul>
 *    <li>with two tics, pure red, {@code RGB=(1,0,0)}, is used for the minimum value and pure
 *    green,  {@code (0,1,0)}, is used for the maximum value;</li>
 *    <li>with three tics, pure red, {@code (1,0,0)}, is used for the minimum value, pure
 *    yellow, {@code (1,1,0)}, is used for the middle value, and pure
 *    green, {@code (0,1,0)}, is used for the maximum value;</li>
 *    <li>with four tics, pure red, {@code (1,0,0)}, is used for the minimum value, pure
 *    yellow, {@code (1,1,0)}, is used at the position equal to one third of the range of values,
 *    pure blue is used at the position equal to two thirds of the range of values, and pure
 *    green, {@code (0,1,0)}, is used for the maximum value;</li>
 * </ul>
 * </p>
 */
public class GradientLegendView
  extends View
{
	/**
	 * Legend title.
	 */
	private String title;
	/**
	 * Gradient colors.
	 */
	private final Color[] colors;
	/**
	 * Labels shown at each gradient transition color.
	 */
	private final String[] labels;
	/**
	 * The minimum value shown in the legend.
	 */
	private int minValue;
	/**
	 * The maximum value shown in the legend.
	 */
	private int maxValue;
	/**
	 * Number of tics shown in the legend.
	 */
	private final int numberTics;
	private int titleHeight;
	private int titleBaseline;
	private final Paint titlePaint = new Paint ();
	private int gradientStartX;
	private int gradientHorizontalDivision;
	private int gradientHeight;
	private final Paint[] gradientPaint;
	private int ticTextWidth;
	private int ticTextsHeight;
	private int ticTextBaseline;
	private final int ticLineHeight;
	private final Paint ticPaint = new Paint ();
	private final Rect tBounds = new Rect ();
	private int paddingLeft;
	private int paddingTop;
	private int contentWidth;
	private int contentHeight;

	public GradientLegendView (Context context)
	{
		this (context, null, 0);
	}

	public GradientLegendView (Context context, @Nullable AttributeSet attrs)
	{
		this (context, attrs, 0);
	}

	public GradientLegendView (Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super (context, attrs, defStyleAttr);
		final TypedArray a = getContext ().obtainStyledAttributes (
		  attrs, R.styleable.GradientLegendView, defStyleAttr, 0);
		this.title = a.getString (R.styleable.GradientLegendView_title);
		if (this.title == null)
			this.title = "legend";
		this.numberTics = Math.max (
		  a.getInteger (R.styleable.GradientLegendView_number_tics, 3),
		  2);
		this.minValue = a.getInteger (R.styleable.GradientLegendView_min_value, 0);
		this.maxValue = Math.max (
		  a.getInteger (R.styleable.GradientLegendView_max_value, 10),
		  this.minValue + 1);
		this.titlePaint.setTextSize (a.getDimension (
		  R.styleable.GradientLegendView_title_text_size, adv (12, context)));
		this.ticPaint.setTextSize (a.getDimension (
		  R.styleable.GradientLegendView_tic_label_text_size, adv (10, context)));
		this.ticLineHeight = Math.round (a.getDimension (
		  R.styleable.GradientLegendView_tic_line_height, adv (5, context)));
		this.ticPaint.setTextAlign (Paint.Align.CENTER);
		this.ticPaint.setColor (Color.BLACK);
		a.recycle ();
		this.titlePaint.setTextAlign (Paint.Align.CENTER);
		this.titlePaint.setColor (Color.BLACK);
		this.colors = new Color[this.numberTics];
		this.labels = new String[this.numberTics];
		this.gradientPaint = new Paint[numberTics];
		if (numberTics == 2) {
			this.colors[1] = Color.valueOf (Color.RED);
			this.colors[0] = Color.valueOf (Color.GREEN);
		} else if (numberTics == 3) {
			this.colors[2] = Color.valueOf (Color.RED);
			this.colors[1] = Color.valueOf (Color.YELLOW);
			this.colors[0] = Color.valueOf (Color.GREEN);
		} else if (numberTics == 4) {
			this.colors[3] = Color.valueOf (Color.RED);
			this.colors[2] = Color.valueOf (Color.YELLOW);
			this.colors[1] = Color.valueOf (Color.BLUE);
			this.colors[0] = Color.valueOf (Color.GREEN);
		} else {
			for (int i = 0; i < numberTics; i++) {
				this.colors[i] = Color.valueOf (
				  i / (numberTics - 1f),
				  3 * (numberTics - i - 1f) / (numberTics - 1f) / 4,
				  (numberTics - i - 1f) / (numberTics - 1f) / 2
				);
			}
		}
		this.ticTextsHeight = 0;
		this.ticTextWidth = 0;
		for (int i = 0; i < numberTics; i++) {
			this.gradientPaint[i] = new Paint ();
		}
		this.computeTicLabels ();
	}

	/**
	 * Sets the gradient legend title.
	 *
	 * <p>If the parameter is {@code null}, the string {@code legend} is used.</p>
	 * @param value the legend title.
	 */
	public void setTitle (String value)
	{
		if (value == null)
			this.title = "legend";
		else
			this.title = value;
		this.titleHeight = this.computeTextHeight (this.title, this.titlePaint);
		this.computeGraphicalElementsPositionGradient ();
	}

	/**
	 * Sets the minimum value of the gradient legend.
	 *
	 * <p>If the value is higher than the current maximum value, then the maximum is set to {@code
	 * value+1}.</p>
	 *
	 * @param value the new minimum value of the gradient legend.
	 */
	public void setMinValue (int value)
	{
		this.minValue = value;
		this.maxValue = Math.max (this.maxValue, value + 1);
		this.computeTicLabels ();
		this.computeGraphicalElementsPositionGradient ();
	}

	/**
	 * Sets the maximum value of the gradient legend.
	 *
	 * <p>If the value is lower than the current minimum value, then the minimum value is set to
	 * {@code value-1}.
	 * </p>
	 *
	 * @param value the new maximum value of the gradient legend.
	 */
	public void setMaxValue (int value)
	{
		this.maxValue = value;
		this.minValue = Math.min (this.minValue, value - 1);
		this.computeTicLabels ();
		this.computeGraphicalElementsPositionGradient ();
	}

	/**
	 * Compute the colour that corresponds to the given value, according to this gradient legend.
	 *
	 * <p>Values that are outside the gradient legend, are assigned the lowest and highest colour
	 * in the legend for values lower than the minimum value and for values higher than the
	 * maximum value, respectively.
	 * </p>
	 * @param value the value to compute a colour for.
	 * @return the colour that corresponds to the given value.
	 */
	public int valueColor (int value)
	{
		float red, green, blue;
		if (value <= this.minValue) {
			red = this.colors [0].red ();
			green = this.colors [0].green ();
			blue = this.colors [0].blue ();
		} else if (value >= this.maxValue) {
			red = this.colors [this.numberTics - 1].red ();
			green = this.colors [this.numberTics - 1].green ();
			blue = this.colors [this.numberTics - 1].blue ();
		} else {
			int rangeValues = this.maxValue - this.minValue;
			int index0 = (this.numberTics - 1) * (value - this.minValue) / rangeValues;
			int index1 = index0 + 1;
			int vt0 = index0 * rangeValues / (this.numberTics - 1) + this.minValue;
			int vt1 = index1 * rangeValues / (this.numberTics - 1) + this.minValue;
			float weight0 = (vt1 - value) / (float) (vt1 - vt0);
			Color color0 = this.colors[index0];
			Color color1 = this.colors[index1];
			red = color0.red () * weight0 + color1.red () * (1 - weight0);
			blue = color0.blue () * weight0 + color1.blue () * (1 - weight0);
			green = color0.green () * weight0 + color1.green () * (1 - weight0);
		}
		return Color.argb (1f, red, green, blue);
	}

	private void computeTicLabels ()
	{
		int delta = (maxValue - minValue) / (numberTics - 1);
		int value = this.minValue;
		for (int i = 0; i < numberTics - 1; i++) {
			this.labels[i] = String.valueOf (value);
			value += delta;
		}
		this.labels[numberTics - 1] = String.valueOf (this.maxValue);
	}

	@Override
	protected void onSizeChanged (int width, int height, int oldWidth, int oldHeight)
	{
		super.onSizeChanged (width, height, oldWidth, oldHeight);
		this.paddingLeft = this.getPaddingLeft ();
		int paddingRight = this.getPaddingRight ();
		this.paddingTop = this.getPaddingTop ();
		int paddingBottom = this.getPaddingBottom ();
		this.contentWidth = width - this.paddingLeft - paddingRight;
		this.contentHeight = height - this.paddingTop - paddingBottom;
		this.computeGraphicalElementsPositionGradient ();
	}

	private void computeGraphicalElementsPositionGradient ()
	{
		this.titleHeight = this.computeTextHeight (this.title, this.titlePaint);
		this.titleBaseline =
		  this.paddingTop + this.titleHeight - Math.round (this.titlePaint.getFontMetrics ().descent);
		this.ticTextsHeight = 0;
		this.ticTextWidth = 0;
		for (String aLabel : this.labels) {
			this.ticTextWidth = Math.max (
			  this.ticTextWidth,
			  this.computeTextWidth (aLabel, this.ticPaint));
			this.ticTextsHeight = Math.max (
			  this.ticTextsHeight,
			  this.computeTextHeight (aLabel, this.ticPaint));
		}
		this.ticTextBaseline = this.paddingTop + this.contentHeight;
		int legendWidth = this.contentWidth - this.ticTextWidth;
		this.gradientStartX = this.paddingLeft + this.ticTextWidth / 2;
		this.gradientHorizontalDivision = legendWidth / (numberTics - 1);
		this.gradientHeight =
		  this.contentHeight - this.titleHeight - this.ticLineHeight - this.ticTextsHeight;
		for (int i = 0; i < numberTics - 1; i++) {
			LinearGradient barGradient = new LinearGradient (
			  gradientStartX + i * gradientHorizontalDivision, 0,
			  gradientStartX + (i + 1) * gradientHorizontalDivision, 0,
			  new int[]{this.colors[i].toArgb (), this.colors[i + 1].toArgb ()},
			  null,
			  Shader.TileMode.CLAMP
			);
			this.gradientPaint[i].setShader (barGradient);
		}
	}

	@Override
	protected void onDraw (Canvas canvas)
	{
		super.onDraw (canvas);
		canvas.drawText (
		  this.title,
		  this.paddingLeft + this.contentWidth / 2f,
		  this.titleBaseline,
		  this.titlePaint
		);
		for (int i = 0; i < this.numberTics; i++) {
			canvas.drawText (
			  this.labels[i],
			  this.gradientStartX + i * this.gradientHorizontalDivision,
			  // this.paddingTop + this.titleHeight + this.gradientHeight + this.ticLineHeight,
			  this.ticTextBaseline,
			  this.ticPaint);
			canvas.drawLine (
			  this.gradientStartX + i * this.gradientHorizontalDivision,
			  this.paddingTop + this.titleHeight + this.gradientHeight,
			  this.gradientStartX + i * this.gradientHorizontalDivision,
			  this.paddingTop + this.titleHeight + this.gradientHeight + this.ticLineHeight,
			  this.ticPaint
			);
		}
		for (int i = 0; i < this.numberTics - 1; i++) {
			canvas.drawRect (
			  this.gradientStartX + i * this.gradientHorizontalDivision,
			  this.paddingTop + this.titleHeight,
			  this.gradientStartX + (i + 1) * this.gradientHorizontalDivision,
			  this.paddingTop + this.titleHeight + this.gradientHeight,
			  this.gradientPaint[i]
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
		Paint.FontMetrics fm = paint.getFontMetrics ();
		return (int) Math.floor (Math.max (this.tBounds.height (), fm.top + fm.bottom) + fm.leading);
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
	private int computeTextWidth (String text, @NotNull Paint paint)
	{
		paint.getTextBounds (text, 0, text.length (), this.tBounds);
		return this.tBounds.width ();
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
}
