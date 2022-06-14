package pt.expolis.mobileapp.plots.chart;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

/**
 * The location overlay.
 * Draws a circle in the map.
 */
class LocationOverlay
  extends Overlay
{
	private final Paint paintCircle;
	private final Paint paintCircumference;
	LocationOverlay ()
	{
		this.paintCircle = new Paint ();
		this.paintCircle.setColor (Color.argb (31, 255, 0, 0));
		this.paintCircle.setStyle (Paint.Style.FILL);
		this.paintCircumference = new Paint ();
		this.paintCircumference.setColor (Color.argb (191, 255, 0, 0));
		this.paintCircumference.setStyle (Paint.Style.STROKE);
		this.paintCircumference.setStrokeWidth (2);
	}
	@Override
	public void draw (Canvas canvas, MapView map, boolean shadow)
	{
		int width = canvas.getWidth ();
		int height = canvas.getHeight ();
		final float cx = width / 2f;
		final float cy = height / 2f;
		final float radius = Math.min (width, height) * 0.45f;
		canvas.drawCircle (cx, cy, radius, this.paintCircle);
		canvas.drawArc (
		  cx + radius, cy - radius, cx - radius, cy + radius,
		  0, 360,
		  false,
		  this.paintCircumference);
	}
}
