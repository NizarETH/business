package com.euphor.paperpad.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class AViewFlipper extends ViewFlipper {

	public AViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	Paint paint = new Paint();

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		float margin = 3;
		float radius = 5;
		float cx = width / 2 - (2 * (radius + margin)  * getChildCount());
		float cy = height - 20;

		canvas.save();
		if (paint != null) {
			paint.setAntiAlias(true);
		}
		if (getChildCount() <= 1)
			return;
		for (int i = 0; i < getChildCount(); i++) {
			if (i == getDisplayedChild()) {
				paint.setColor(Color.WHITE);
				canvas.drawCircle(cx, cy, radius, paint);
				/** For circle indicators **/
				// RectF rect = new RectF(cx, cy, radius * 4, radius * 4);
				// canvas.drawRect(rect, paint);

			} else {
				paint.setColor(0x55000000 + Color.WHITE);
				canvas.drawCircle(cx, cy, radius, paint);
			}
			cx += 4 * (radius + margin);
		}

		canvas.restore();
	}

}
