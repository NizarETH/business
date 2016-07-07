/**
 * 
 */
package com.euphor.paperpad.widgets;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontFitTextView extends TextView {

    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        testPaint = new Paint();
        testPaint.set(this.getPaint());
        //max size defaults to the intially specified text size unless it is too small
        maxTextSize = this.getTextSize();
        if (maxTextSize < 21) {
            maxTextSize = 21;
        }
        minTextSize = 20;
    }

    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth) { 
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            float trySize = maxTextSize;

            testPaint.setTextSize(trySize);
            while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
            }

            this.setTextSize(trySize);
        }
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }

    //Getters and Setters
    public float getMinTextSize() {
        return minTextSize;
    }

    public void setMinTextSize(int minTextSize) {
        this.minTextSize = minTextSize;
    }

    public float getMaxTextSize() {
        return maxTextSize;
    }

    public void setMaxTextSize(int minTextSize) {
        this.maxTextSize = minTextSize;
    }

    //Attributes
    private Paint testPaint;
    private float minTextSize;
    private float maxTextSize;
	/* (non-Javadoc)
	 * @see android.widget.TextView#setTextSize(float)
	 */
	@Override
	public void setTextSize(float size) {
		// TODO Auto-generated method stub
		super.setTextSize(size);
//		initialise();
	}

	/* (non-Javadoc)
	 * @see android.widget.TextView#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		/*final int oldWidth = getMeasuredWidth();
		final int oldHeight = getMeasuredHeight();
		int newWidth;
		float size = this.getTextSize();

		do {

		setTextSize(size--);
		measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		newWidth = getMeasuredWidth();

		} while(newWidth > oldWidth);

		setMeasuredDimension(oldWidth, oldHeight);*/
	}

}