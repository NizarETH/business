package com.euphor.paperpad.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class HorizontalScrollViewExt extends HorizontalScrollView {
	
    private ScrollViewListener scrollViewListener = null;
    
    public HorizontalScrollViewExt(Context context) {
        super(context);
    }

    public HorizontalScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HorizontalScrollViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
    
    public interface ScrollViewListener {
        void onScrollChanged(HorizontalScrollViewExt scrollView, 
                             int x, int y, int oldx, int oldy);
    }
}