/**
 * 
 */
package com.euphor.paperpad.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author euphordev02
 *
 */
public class PinImageView extends ImageView {

    int color = -1;
	private Paint paint;

	/**
	 * @param context
	 */
	public PinImageView(Context context) {
		super(context);
		paint = new Paint();
		paint.setColor(Color.BLACK);
	}

    /**
     * @param context
     */
    public PinImageView(Context context, int color) {
        super(context);
        this.color = color;
        paint = new Paint();
        paint.setColor(color);
    }
	
	/**
	 * @param context
	 */
	public PinImageView(Context context, Paint paint) {
		super(context);
		this.paint = paint;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public PinImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public PinImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}


    /* (non-Javadoc)
         * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
         */
	@Override
	protected void onDraw(Canvas canvas) {
		Rect rect = canvas.getClipBounds();
		Log.i(" bounds of arrow", rect.left+" bottom "+rect.bottom+" top "+rect.top + " right "+rect.right);
		
		
		if (paint == null) {
			paint = new Paint();
			paint.setColor(Color.BLUE);
		}
		paint.setAntiAlias(true);
		paint.setDither(false);
//		rect.
		float radius = 0;
        if(color != -1){
            if (rect.right < rect.bottom) {
                radius = rect.right / 2;
            } else {
                radius = rect.bottom / 2;
            }
        }else {
            if (rect.right < rect.bottom) {
                radius = rect.right / 2;
            } else {
                radius = rect.bottom / 2;
            }
        }
		canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(), radius, paint);
		super.onDraw(canvas);
	}

	/**
	 * @return the paint
	 */
	public Paint getPaint() {
		return paint;
	}

	/**
	 * @param paint the paint to set
	 */
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setColor(int color) {
		this.color = color;
		if (paint == null) {
			paint = new Paint();
		}
		this.paint.setColor(color);
	}
}
