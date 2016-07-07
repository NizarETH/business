package com.euphor.paperpad.widgets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author euphordev04
 *
 */
public class BubbleDrawable extends Drawable {

    // Public Class Constants
    ////////////////////////////////////////////////////////////

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    // Private Instance Variables
    ////////////////////////////////////////////////////////////

    private Paint mPaint, pPaint;
    private int mColor, pColor;

    private RectF mBoxRect;
    private int mBoxWidth;
    private int mBoxHeight;
    private float mCornerRad;
    private Rect mBoxPadding = new Rect();

    private Path mPointer;
    private int mPointerWidth;
    private int mPointerHeight;
    private int mPointerXAlignment, mPointerYAlignment;

    private float yPointer;
    // Constructors
    ////////////////////////////////////////////////////////////

    public BubbleDrawable(int pointerXAlignment, int pointerYAlignment, int bubbleColor, int pointerColor) {
    	mColor = bubbleColor;
    	pColor = pointerColor;
        setPointerAlignment(pointerXAlignment, pointerYAlignment);
        initBubble();
    }

    // Setters
    ////////////////////////////////////////////////////////////

    public void setPadding(int left, int top, int right, int bottom) {
        mBoxPadding.left = left;
        mBoxPadding.top = top;
        mBoxPadding.right = right + mPointerWidth;
        mBoxPadding.bottom = bottom ;
    }

    public void setCornerRadius(float cornerRad) {
        mCornerRad = cornerRad;
    }

    public void setPointerAlignment(int pointerXAlignment, int pointerYAlignment) {
        if (pointerXAlignment < 0 || pointerXAlignment > 4 || pointerYAlignment < 0 || pointerYAlignment > 4) {
            Log.e("BubbleDrawable", "Invalid pointerAlignment argument");
        } else {
            mPointerXAlignment = pointerXAlignment;
            mPointerYAlignment = pointerYAlignment;
        }
    }

    public void setPointerWidth(int pointerWidth) {
        mPointerWidth = pointerWidth;
    }

    public void setPointerHeight(int pointerHeight) {
        mPointerHeight = pointerHeight;
    }
    
    public void setYPointer(float yPointer){
    	this.yPointer = yPointer;
    }

    // Private Methods
    ////////////////////////////////////////////////////////////

    private void initBubble() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        
        if(mColor != pColor){
        pPaint = new Paint();
        pPaint.setAntiAlias(true);
        pPaint.setColor(pColor);
        }else{
        	pPaint = mPaint;
        }
        mCornerRad = 0;
        setPointerWidth(30);
        setPointerHeight(20);
    }

    private void updatePointerPath() {
        mPointer = new Path();
        mPointer.setFillType(Path.FillType.EVEN_ODD);

        // Set the starting point
        mPointer.moveTo(pointerHorizontalStart(), pointerVerticalStart());//mBoxHeight);

        // Define the lines
//        mPointer.rLineTo(mPointerWidth, 0);
//        mPointer.rLineTo(-(mPointerWidth / 2), mPointerHeight);
//        mPointer.rLineTo(-(mPointerWidth / 2), -mPointerHeight);
//        mPointer.close();
        
        switch (mPointerYAlignment) {
        case TOP:
            mPointer.rLineTo(mPointerWidth, 0);
            mPointer.rLineTo(-(mPointerWidth / 2), -mPointerHeight);
            mPointer.rLineTo(-(mPointerWidth / 2), +mPointerHeight);
            break;
        case CENTER:
        	if(mPointerXAlignment == LEFT){
            mPointer.rLineTo(0, 0);
            mPointer.rLineTo(-mCornerRad , mPointerHeight/ 2);
            mPointer.rLineTo(mCornerRad, mPointerHeight/ 2);
        	}
        	else if(mPointerXAlignment == RIGHT){
                mPointer.rLineTo(mCornerRad, 0);
                mPointer.rLineTo(mCornerRad, mPointerHeight/2);
                mPointer.rLineTo(-mCornerRad, mPointerHeight/2);
        	}
            break;
        case BOTTOM: 
            mPointer.rLineTo(mPointerWidth, 0);
            mPointer.rLineTo(-(mPointerWidth / 2), mPointerHeight);
            mPointer.rLineTo(-(mPointerWidth / 2), -mPointerHeight);
            break;
        }
        
        mPointer.close();
    }

    private float pointerHorizontalStart() {
        float x = 0;
        switch (mPointerXAlignment) {
        case LEFT:
            x = mCornerRad;
            break;
        case CENTER:
            x = (mBoxWidth / 2) - (mPointerWidth / 2);
            break;
        case RIGHT:
            x = mBoxWidth - mCornerRad - mPointerWidth;
        }
        return x;
    }
    
    private float pointerVerticalStart() {
        float y = 0;
        if(this.yPointer != 0){
        	if( mBoxHeight / 2 > this.yPointer)return mBoxHeight - this.yPointer; //(mBoxHeight / 2) - (mPointerHeight / 2) - mCornerRad - mPointerWidth;	
        }
        switch (mPointerYAlignment) {
        case TOP:
            y = mPointerHeight;
            break;
        case CENTER:
            y = (mBoxHeight / 2) - (mPointerHeight / 2);
            break;
        case BOTTOM:
            y = mBoxHeight;
        }
        return y;
    }

    // Superclass Override Methods
    ////////////////////////////////////////////////////////////

    @Override
    public void draw(Canvas canvas) {
        mBoxRect = new RectF(0.0f, 0.0f, mBoxWidth , mBoxHeight);
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRoundRect(mBoxRect, mCornerRad, mCornerRad, paint);
        updatePointerPath();
        
        canvas.drawPath(mPointer, pPaint);
        
        
        mBoxRect = new RectF(mCornerRad, mCornerRad, mBoxWidth - mPointerWidth, mBoxHeight);
        canvas.drawRoundRect(mBoxRect, mCornerRad, mCornerRad, mPaint);
        
        mBoxRect = new RectF(mCornerRad, mCornerRad + marge, mBoxWidth - mPointerWidth, mBoxHeight);
        canvas.drawRoundRect(mBoxRect, 0, 0, pPaint);
    }
    
    private int marge = 38;

    @Override
    public int getOpacity() {
        return 255;
    }

    @Override
    public void setAlpha(int alpha) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getPadding(Rect padding) {
        padding.set(mBoxPadding);

        // Adjust the padding to include the height of the pointer
        padding.bottom += mPointerHeight;
        return true;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mBoxWidth = bounds.width() ;
        mBoxHeight = getBounds().height() - mPointerHeight;
        super.onBoundsChange(bounds);
    }

	/**
	 * @return the marge
	 */
	public int getMarge() {
		return marge;
	}

	/**
	 * @param marge the marge to set
	 */
	public void setMarge(int marge) {
		this.marge = marge;
	}


}
