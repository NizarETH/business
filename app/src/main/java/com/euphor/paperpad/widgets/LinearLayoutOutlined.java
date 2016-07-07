package com.euphor.paperpad.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.euphor.paperpad.utils.Colors;

public class LinearLayoutOutlined extends LinearLayout {
	
	private Colors colors;
	


	public LinearLayoutOutlined(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false) ;
	}
	
	
	public void setColor(Colors colors) {
		this.colors = colors;
	}
	
	@SuppressLint("DrawAllocation") 
	@Override
    protected void onDraw(Canvas canvas) {
        /*
        Paint fillPaint = new Paint();
        fillPaint.setARGB(255, 0, 255, 0);
        fillPaint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(fillPaint) ;
        */

        Paint strokePaint = new Paint();
        strokePaint.setColor(colors.getColor(colors.getForeground_color())); //.setARGB(255, 255, 0, 0);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(2);  
        Rect r = canvas.getClipBounds() ;
        Rect outline = new Rect( 2,2,r.right-2, r.bottom-2) ;
        canvas.drawRect(outline, strokePaint) ;
    }

}