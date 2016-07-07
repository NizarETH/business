/**
 * 
 */
package com.euphor.paperpad.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/** a class that draw an Arrow like this ">"
 * @author euphordev02
 *
 */
public class ArrowDrawable extends ShapeDrawable {

	private int a;
	private int d;

	/**
	 * 
	 */
	public ArrowDrawable() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param width width of the rectangle
	 * @param lenght lenght of the triagle
	 */
	public ArrowDrawable(int width, int lenght, String color) {
		this.a = width;
		this.d = lenght;
		getPaint().setColor(Color.parseColor(color));
	}
	
	/**
	 * @param width width of the rectangle
	 * @param lenght lenght of the triagle
	 */
	public ArrowDrawable(int width, int lenght) {
		this.a = width;
		this.d = lenght;
		getPaint().setColor(Color.BLACK);
	}

	/**
	 * @param s
	 */
	public ArrowDrawable(Shape s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.graphics.drawable.ShapeDrawable#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		
		
		Path path = new Path();
		Point p1 = new Point();
		Point p2 = new Point();
		Point p3 = new Point();
		Point p4 = new Point();
		Point p5 = new Point();
		Point p6 = new Point();
		p1.x = a;
		p1.y = 0;
		p2.x = a+d;
		p2.y = a+d;
		p3.x = a;
		p3.y = 2*d;
		p4.x = 0;
		p4.y = 2*d-a;
		p5.x = d-a;
		p5.y = d;
		p6.x = 0;
		p6.y = a;
		
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p4.x, p4.y);
		path.lineTo(p5.x, p5.y);
		path.lineTo(p6.x, p6.y);
		path.close();
		canvas.drawPath(path, getPaint());
//		super.draw(canvas);
	}

	/* (non-Javadoc)
	 * @see android.graphics.drawable.ShapeDrawable#onDraw(android.graphics.drawable.shapes.Shape, android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
		
		super.onDraw(shape, canvas, paint);
	}

}
