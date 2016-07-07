package com.euphor.paperpad.widgets;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Rect;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

//interface EUKenBurnsView{
//	Timer timer;
//	int currentIndex;
//	int count;
//	int currentAnimationIndex;
//	FrameLayout currentLayer;
//	FrameLayout appearingLayer;
//}

public class KenBurnsView extends FrameLayout {

	private Context context;
	private android.view.ViewGroup.LayoutParams selfParams;
	
	public static final float  MIX_RATIO = 0.2f;
	public static final float  IMAGE_SIZE_RATIO = 1.2f;
	public static final float  ZOOM_RATIO = 1.2f;
	
	private Timer timer;
	private int currentIndex;
	private int count;
	private int currentAnimationIndex;
	private FrameLayout currentLayer;
	private FrameLayout appearingLayer;
	
	private int interval = 5;
	private boolean animateNextImageFirst = false;
	
	private ImageView[] imgs;

	public KenBurnsView(Context context, ImageView[] images) {
		super(context);
		this.context = context;
		this.imgs = images;
		prepareAnimation(images);
		this.selfParams = new LayoutParams(400, 300);
		startAnimation();
	}
	
	void startAnimation() {
		//int i = 0;
		timer = new Timer();
		for(int i = 0; i < imgs.length; i++) {
		final int animRes = i;
		
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				animateNextImageFirst(false);
			}
		}, interval * i, interval * (1 + i));
	    
		animateNextImageFirst = true;
		}
	}
	
	public void prepareAnimation(ImageView[] images) {

//	    NSAssert(self.dataSource, @"EUKenBurnsView must have a dataSource");
//	    NSAssert([self.dataSource numberOfImagesInKenBurnsView:self] > 0, @"EUKenBurnsView's dataSource must return at least one image in numberOfImagesInKenBurnsView:.");
	    
	 this.currentLayer = this;//new FrameLayout(context);
	 this.appearingLayer = this;// new FrameLayout(context);
	    
	    this.count = images.length;
	    
	    this.currentAnimationIndex = 0;
	    this.currentIndex = 0;

	}
	
	public FrameLayout layerForIndex(int index){
	    
	    ImageView image = imgs[index];
	    
	    FrameLayout imageLayer = new FrameLayout(context);
	    imageLayer.setBackgroundColor(0xff000000);
//	    imageLayer.removeAllViewsInLayout();
//	    image.getRootView().removeCallbacks(null);
//	    imageLayer.addView(image, image.getLayoutParams());
	    android.view.ViewGroup.LayoutParams params = new LayoutParams(initialFrameForSize(imageSizeForOriginalSize(image.getLayoutParams())).width()
	    		,initialFrameForSize(imageSizeForOriginalSize(image.getLayoutParams())).height());
	    imageLayer.addView(new ImageView(context), params);
	    imageLayer.setLayoutParams(params);
	    imageLayer.setAlpha(0);
	    return imageLayer;
	    
	}
	
	public LayoutParams imageSizeForOriginalSize(android.view.ViewGroup.LayoutParams layoutParams) {
        
	    float viewRatio = selfParams.width / selfParams.height;

	    float ratio = layoutParams.width / layoutParams.height;
	    LayoutParams newSize = new LayoutParams(0, 0);

	    if (ratio > viewRatio) {
	        newSize.height = (int) (selfParams.height * IMAGE_SIZE_RATIO);
	        newSize.width = (int) (newSize.height * ratio);
	    } else {
	        newSize.width = (int) (selfParams.width * IMAGE_SIZE_RATIO);
	        newSize.height = (int) (newSize.width / ratio);
	    }
	    
	    return newSize;
	    
	}
	
	
	public Rect initialFrameForSize(LayoutParams size) {
	    
	    switch (this.currentAnimationIndex) {
	        case 0: // bottom-right >> top-left
	            return new Rect(0, 0, size.width, size.height);

	        case 1: // bottom-left >> top-right
	            return new Rect(- size.width + selfParams.width, 0, size.width, size.height);


	        case 2: // top-right >> bottom-left
	            return new Rect(0, selfParams.height - size.height, size.width, size.height);


	        default:
	            return new Rect(0, 0, 0, 0);

	    }
	    
	}
	
	private void incrementCurrentAnimationIndex() {
	    this.currentAnimationIndex++;
	    if (this.currentAnimationIndex > 2) this.currentAnimationIndex = 0;
	}
	
	//(CATransform3D)
	public float[] translateForSize(android.view.ViewGroup.LayoutParams layoutParams) {
	    

	    float widthDiff = 0;
	    float heightDiff = 0;

	    switch (this.currentAnimationIndex) {
	        case 0: // bottom-right >> top-left
	            widthDiff = 0 - (layoutParams.width - selfParams.width);
	            heightDiff = 0 - (layoutParams.height - selfParams.height);
	            break;
	            
	        case 1: // bottom-left >> top-right
	            widthDiff = layoutParams.width - selfParams.width;
	            heightDiff = 0 - (layoutParams.height - selfParams.height);
	            break;
	            
	        case 2: // top-right >> bottom-left
	            widthDiff = 0 - (layoutParams.width - selfParams.width);
	            widthDiff = layoutParams.height - selfParams.height;

	            break;
	            
	        default:
	            widthDiff = 0;
	            heightDiff = 0;
	            break;
	    }
	    
	    
	    return new float[]{widthDiff, widthDiff};
	
	}
	
	public void animateNextImageFirst(boolean first) {

	    this.appearingLayer = layerForIndex(this.currentIndex);
	    
	    float duration = this.interval + this.interval*MIX_RATIO + (first ? this.interval*MIX_RATIO : 0);
	    
	    //FrameLayout block_layer = this.appearingLayer;
	    
	    float[] sizeDiff = translateForSize(appearingLayer.getLayoutParams());
	    
//	    [CATransaction begin];
	    TranslateAnimation transformAnimation = new TranslateAnimation(0.0f, sizeDiff[0], 0.0f, sizeDiff[1]);
	    transformAnimation.setFillEnabled(true);
	    transformAnimation.setRepeatCount(Animation.INFINITE);
	    transformAnimation.setDuration((long) duration);
	    
	    ScaleAnimation scaleT = new ScaleAnimation(0, ZOOM_RATIO, 0, ZOOM_RATIO);
	    scaleT.setFillEnabled(true);
	    scaleT.setRepeatCount(Animation.INFINITE);
	    scaleT.setDuration((long) duration);

	    this.appearingLayer.startAnimation(transformAnimation);
	    this.appearingLayer.startAnimation(scaleT);

	    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
	    alphaAnimation.setAnimationListener(new AlphaAnimation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
				alphaAnimation.setStartOffset(600);
				alphaAnimation.setDuration(200);
				
			}
		});
	    
	    alphaAnimation.setDuration(200);
	    
//	    alphaAnimation.values = @[@0, @1, @1, @0];
//	    alphaAnimation.keyTimes = @[@0, @0.2, @0.8, @1];

	    this.appearingLayer.startAnimation(alphaAnimation);

	   // this.addView(this.appearingLayer);
	    
	    this.currentIndex++;
	    if (this.currentIndex >= this.count) {
	    	this.currentIndex = 0;
	    }
	    
	    incrementCurrentAnimationIndex();
	    
	}
	
	
	private void timerTarget(Timer timer) {
	    animateNextImageFirst(false);
	}
	

	private void stopAnimation() {
	   this.timer.cancel();
	   this.timer.purge();
	}
}
