package utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

public class Debug
{
	static public boolean showToast = false;
	/**
	 * Show a toast for a long time.
	 * @param context The context.
	 * @param text the text to display.
	 */
	static public void toast (Context context, String text)
	{
		if (showToast) {
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText (context, text, duration);
			toast.show ();
		}
	}
	static public void sleep (int seconds)
	{
		try
		{
			Thread.sleep (1000L * seconds);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
	}
	static public void canvas (Canvas canvas)
	{
		Paint p = new Paint ();
		p.setColor (Color.BLACK);
		p.setStrokeWidth (1);
		int w = canvas.getWidth ();
		int h = canvas.getHeight ();
		canvas.drawLine (0, 0, w, h, p);
		canvas.drawLine (0, 0, 0, h, p);
		p.setStrokeWidth (2);
		canvas.drawLine (0, h, w, 0, p);
	}
	static public void canvas (Canvas canvas, int top, int bottom, int left, int right)
	{
		Paint p = new Paint ();
		p.setColor (Color.BLUE);
		p.setStrokeWidth (1);
		canvas.drawLine (left, top, right, bottom, p);
		canvas.drawLine (left, bottom, right, top, p);
		canvas.drawLine (left, top, left, bottom, p);
	}
}
