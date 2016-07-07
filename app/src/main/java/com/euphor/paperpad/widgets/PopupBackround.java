/**
 * 
 */
package com.euphor.paperpad.widgets;

import com.euphor.paperpad.utils.Colors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author euphordev02
 *
 */
public class PopupBackround extends Drawable {
	private Colors colors;
	
	public PopupBackround(Colors colors) {
		super();
		this.colors = colors;
	}

	private int padding = 5;
	double DEGREE_SINUS = 0.86602540378;

	@Override
	public void draw(Canvas canvas) {
		setBounds(canvas.getClipBounds());
		Rect bounds = getBounds();
//		Log.i("bounds ", ""+ bounds.top);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
//		Paint paint2 = new Paint();
//		paint2.setStyle(Style.STROKE);
//		paint2.setStrokeWidth(5);
//		paint2.setColor(Color.BLACK);
		Rect rect = new Rect(padding, padding, bounds.right-padding, bounds.bottom-padding);
//		Rect rect2 = new Rect(2*padding, 2*padding, bounds.right-2*padding, bounds.bottom-2*padding);

//		canvas.drawRect(rect2, paint2);
		canvas.drawRect(rect, paint);
		float a = (float) (padding/DEGREE_SINUS);
		
//		Point x = new Point(bounds.right-padding, (int)((float)(bounds.bottom)/(float)2));
//		Point y = new Point(bounds.right-padding, (int)(((float)(bounds.bottom)/(float)2)+a));
//		Point z = new Point(bounds.right, (int)(((float)(bounds.bottom)/(float)2)+(float)a/(float)2));
		Path path = new Path();
		path.moveTo(bounds.right-padding, ((float)(bounds.bottom)/(float)2));
		path.lineTo(bounds.right-padding, (((float)(bounds.bottom)/(float)2)+a));
		path.lineTo(bounds.right, (int)(((float)(bounds.bottom)/(float)2)+(float)a/(float)2));
		path.close();
		Paint paint3 = new Paint();
		paint3.setStyle(Style.FILL);
		paint3.setColor(Color.WHITE);
		canvas.drawPath(path, paint3);
		}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the padding
	 */
	public int getPadding() {
		return padding;
	}

	/**
	 * @param padding the padding to set
	 */
	public void setPadding(int padding) {
		this.padding = padding;
	}

	
}
