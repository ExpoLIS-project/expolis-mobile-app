package pt.expolis.mobileapp.tutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import pt.expolis.mobileapp.R;

/**
 * A component that displays an image with an optional highlighted area and an optional animated
 * tapping hand.
 *
 * <p>This component is used by the tutorial fragment to display screenshots of the app and
 * explain its functionalities.</p>
 */
public class HighlightImageView
	extends AppCompatImageView
{
	/**
	 * The image that is displayed by this view.
	 */
	private Drawable drawable;
	/**
	 * Whether we animate the tapping hand or not.
	 */
	private boolean animateHand;
	/**
	 * In which state of the tapping hand animation we are.
	 */
	private DrawHand drawHand;
	/**
	 * The hand tapping drawables per state.
	 *
	 * <p>Only applicable if the corresponding state has any drawable.</p>
	 */
	private final Drawable[] handDrawable;
	/**
	 * The hand tapping matrix used when drawing the corresponding drawable.
	 *
	 * <p>Only applicable if the corresponding state has any drawable.</p>
	 */
	private final Matrix[] handMatrix;
	private final Semaphore handMutex;
	private boolean highlightArea;
	private final RectF highlightedArea;
	private final Paint highlightColour;

	private final float density;

	public HighlightImageView (Context context)
	{
		this (context, null, 0);
	}
	public HighlightImageView (Context context, AttributeSet attrs)
	{
		this (context, attrs, 0);
	}
	public HighlightImageView (Context context, @Nullable AttributeSet attrs, int defStyle)
	{
		super (context, attrs, defStyle);
		int n = DrawHand.values ().length;
		this.handDrawable = new Drawable [n];
		this.handMatrix = new Matrix [n];
		HashMap<Integer, Drawable> drawables = new HashMap<> ();
		for (DrawHand dh : DrawHand.values ()) {
			if (dh.draw) {
				int i = dh.ordinal ();
				this.handMatrix [i] = new Matrix ();
				if (drawables.containsKey (dh.resourceId)) {
					this.handDrawable [i] = drawables.get (dh.resourceId);
				}
				else {
					Drawable d = ResourcesCompat.getDrawable (
					  context.getResources (),
					  dh.resourceId,
					  context.getTheme ());
					if (d != null) {
						int w = d.getMinimumWidth ();
						int h = d.getMinimumHeight ();
						d.setBounds (0, 0, w, h);
						this.handDrawable[i] = d;
						drawables.put (dh.resourceId, d);
					}
				}
			}
		}
		this.drawHand = DrawHand.NO;
		this.handMutex = new Semaphore (1);
		this.highlightArea = false;
		this.highlightedArea = new RectF ();
		this.highlightColour = new Paint ();
		this.highlightColour.setStrokeWidth (5);
		this.highlightColour.setColor (Color.YELLOW);
		this.highlightColour.setAlpha (127);
		this.density = context.getResources ().getDisplayMetrics ().density;
	}

	public void setDrawable (int resourceId)
	{
		this.setImageResource (resourceId);
		this.disableHandAnimation ();
		this.disableHighlightedArea ();
		this.drawable = ResourcesCompat.getDrawable (
		  this.getResources (), resourceId, this.getContext ().getTheme ());
	}

	public void animateHand (int x, int y)
	{
		for (DrawHand dh: DrawHand.values ()) {
			if (dh.draw) {
				int i = dh.ordinal ();
				this.handMatrix [i].reset ();
				this.handMatrix [i].postTranslate (
				  (x + dh.delta_x) * this.density,
				  (y + dh.delta_y) * this.density);
			}
		}
		if (!this.animateHand) {
			new Thread (new Animate ()).start ();
		}
		this.animateHand = true;
	}

	public void disableHandAnimation ()
	{
		this.animateHand = false;
		invalidate ();
	}

	public void highlightArea (float left, float top, float right, float bottom)
	{
		this.highlightArea = true;
		this.highlightedArea.left = left * this.density;
		this.highlightedArea.top = top * this.density;
		this.highlightedArea.right = right * this.density;
		this.highlightedArea.bottom = bottom * this.density;
		invalidate ();
	}

	public void highlightArea (RectF rect)
	{
		this.highlightArea (rect.left, rect.top, rect.right, rect.bottom);
	}


	public void disableHighlightedArea ()
	{
		this.highlightArea = false;
		invalidate ();
//		requestLayout ();
	}

	@Override
	protected void onDraw (Canvas canvas)
	{
		super.onDraw (canvas);
		if (this.drawable == null) {
			return;
		}
//		this.drawable.draw (canvas);
		this.handMutex.acquireUninterruptibly ();
		if (animateHand && this.drawHand.draw) {
			System.out.println (this.drawHand);
			System.out.println (this.drawHand.ordinal ());
			int i = this.drawHand.ordinal ();
			canvas.save ();
			canvas.setMatrix (this.handMatrix [i]);
			this.handDrawable [i].draw (canvas);
			canvas.restore ();
		}
		this.handMutex.release ();
		if (this.highlightArea) {
			canvas.drawRoundRect (this.highlightedArea, 7.5f, 7.5f, this.highlightColour);
		}
	}

	class Animate
	implements Runnable
	{
		final AnimateHandler handler = new AnimateHandler (HighlightImageView.this);
		@Override
		public void run ()
		{
			while (animateHand) {
				try {
					//noinspection BusyWait
					Thread.sleep (drawHand.deltaTime);
				} catch (InterruptedException ignored) {
				}
				handMutex.acquireUninterruptibly ();
				drawHand = DrawHand.values () [(drawHand.ordinal () + 1) % DrawHand.values ().length];
				handMutex.release ();
				handler.sendEmptyMessage (1);
			}
		}
	}

	private enum DrawHand
	{
		NO (2000),
		MOVE_1 (600, R.drawable.animated_image_hand_only, -36, -26),
		MOVE_2 (700, R.drawable.animated_image_hand_only, -31, -21),
		MOVE_3 (850, R.drawable.animated_image_hand_only, -26, -16),
		TOUCH_1 (500, R.drawable.animated_image_hand_touch, -26, -16),
		FLASH (100),
		TOUCH_2 (500, R.drawable.animated_image_hand_touch, -26, -16),
		;

		final long deltaTime;
		final boolean draw;
		final int resourceId;
		final int delta_x, delta_y;

		DrawHand (long deltaTime)
		{
			this.deltaTime = deltaTime;
			this.draw = false;
			this.resourceId = this.delta_x = this.delta_y = 0;
		}

		DrawHand (long deltaTime, int resourceId, int delta_x, int delta_y)
		{
			this.deltaTime = deltaTime;
			this.draw = true;
			this.resourceId = resourceId;
			this.delta_x = delta_x;
			this.delta_y = delta_y;
		}
	}


	private static class AnimateHandler
	  extends Handler
	{
		private final WeakReference<HighlightImageView> reference;
		AnimateHandler (HighlightImageView hiv)
		{
			super (Looper.getMainLooper ());
			this.reference = new WeakReference<> (hiv);
		}
		@Override
		public void handleMessage (@NotNull android.os.Message inputMessage)
		{
			HighlightImageView hiv = this.reference.get ();
			if (hiv != null) {
				hiv.invalidate ();
				hiv.requestLayout ();
			}
		}
	}
}
