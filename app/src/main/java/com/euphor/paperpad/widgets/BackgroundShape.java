/**
 * 
 */
package com.euphor.paperpad.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * This class provide a shape drawable similare to the ressource : 
 * <pre>
 * {@code 
 * <shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid 
        android:color="@android:color/transparent" 
    />
    <stroke 
        android:width="1px" 
        android:color="@color/list_divider" 
    />
</shape> 
 *   }
 *   </pre>
 * @author euphordev02
 *
 */
public class BackgroundShape extends ShapeDrawable {

	private static float WIDTH = 1;
	private Paint fillpaint;
	private Paint strokepaint;

	/**
	 * 
	 */
	public BackgroundShape() {
		// TODO Auto-generated constructor stub
//	    fillpaint = this.getPaint();
//	    strokepaint = new Paint(fillpaint);
//	    strokepaint.setARGB(255, 255, 255, 255);

	}
	
	/**
	 * 
	 */
	public BackgroundShape(float width, int colorBody, int colorStroke) {
		fillpaint = new Paint();
		fillpaint.setColor(colorBody);
		strokepaint = new Paint();
		strokepaint.setColor(colorStroke);
		WIDTH = width;
	}

	/**
	 * @param s
	 */
	public BackgroundShape(Shape s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
	    // TODO Auto-generated method stub
	//  super.onDraw(shape, canvas, paint);
	    fillpaint = this.getPaint();
	    strokepaint = new Paint(fillpaint);
	    strokepaint.setStyle(Paint.Style.STROKE);
	    //to set stroke width and color instead of <stroke android:width="2dp" android:color="#FFFFFFFF" /> 
	    strokepaint.setStrokeWidth(WIDTH);
	    strokepaint.setARGB(255, 255, 255, 255);

	    shape.draw(canvas, fillpaint);
	    shape.draw(canvas, strokepaint);

	}

}
