package com.euphor.paperpad.utils.actionsPrices;


import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.AutoResizeTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * QuickAction dialog, shows action list as icon and text like the one in Gallery3D app. Currently supports vertical 
 * and horizontal layout.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * Contributors:
 * - Kevin Peck <kevinwpeck@gmail.com>
 */
public class QuickAction extends PopupWindows implements OnDismissListener {
	private View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater mInflater;
	private ViewGroup mTrack;
	private ScrollView mScroller;
	private OnActionItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;
	
	private List<ActionItem> actionItems = new ArrayList<ActionItem>();

	private boolean mDidAction;
	
	private int mChildPos;
    private int mInsertPos;
    private int mAnimStyle;
    private int mOrientation;
    private int rootWidth=0;
	private Colors colors;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;
	
    /**
     * Constructor for default vertical layout
     * 
     * @param context  Context
     */
    public QuickAction(Context context) {
        this(context, VERTICAL);
    }

    /**
     * Constructor allowing orientation override
     * 
     * @param context    Context
     * @param orientation Layout orientation, can be vartical or horizontal
     */
    public QuickAction(Context context, int orientation) {
        super(context);
        
        mOrientation = orientation;
        
        mInflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mOrientation == HORIZONTAL) {
            setRootViewId(R.layout.popup_horizontal);
        } else {
            setRootViewId(R.layout.popup_vertical);
        }

        mAnimStyle 	= ANIM_AUTO;
        mChildPos 	= 0;
    }
    
    /**
     * Constructor allowing orientation override
     * 
     * @param context    Context
     * @param orientation Layout orientation, can be vartical or horizontal
     * @param colors 
     */
    public QuickAction(Context context, int orientation, Colors colors) {
        super(context);
        
        mOrientation = orientation;
        
        mInflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mOrientation == HORIZONTAL) {
            setRootViewId(R.layout.popup_horizontal);
        } else {
            setRootViewId(R.layout.prices_popup_vertical);
        }
        Drawable drawable = mScroller.getBackground();
        drawable.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
        mScroller.setBackgroundDrawable(drawable);
        
        Drawable drawableArrowDown = mArrowDown.getDrawable();
        if (drawableArrowDown != null) {
			drawableArrowDown.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
//			drawableArrowDown.setAlpha(64);
			mArrowDown.setBackgroundDrawable(drawableArrowDown);
		}
        Drawable drawableArrowUp = mArrowUp.getDrawable();
        if (drawableArrowUp != null) {
			drawableArrowUp.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
			mArrowUp.setBackgroundDrawable(drawableArrowUp);
		}
		mAnimStyle 	= ANIM_AUTO;
        mChildPos 	= 0;
        this.colors = colors;
    }

    /**
     * Get action item at an index
     * 
     * @param index  Index of item (position from callback)
     * 
     * @return  Action Item at the position
     */
    public ActionItem getActionItem(int index) {
        return actionItems.get(index);
    }
    
	/**
	 * Set root view.
	 * 
	 * @param id Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView	= (ViewGroup) mInflater.inflate(id, null);
		mTrack 		= (ViewGroup) mRootView.findViewById(R.id.tracks);

		mArrowDown 	= (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp 	= (ImageView) mRootView.findViewById(R.id.arrow_up);

		mScroller	= (ScrollView) mRootView.findViewById(R.id.scroller);
		
		//This was previously defined on show() method, moved here to prevent force close that occured
		//when tapping fastly on a view to show quickaction dialog.
		//Thanx to zammbi (github.com/zammbi)
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		setContentView(mRootView);
	}
	
	/**
	 * Set animation style
	 * 
	 * @param mAnimStyle animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(int mAnimStyle) {
		this.mAnimStyle = mAnimStyle;
	}
	
	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener Listener
	 */
	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mItemClickListener = listener;
	}
	
	/**
	 * Add action item
	 * 
	 * @param action  {@link ActionItem}
	 */
	public void addActionItem(ActionItem action) {
		actionItems.add(action);
		String title 	= action.getTitle();
		Drawable icon 	= action.getIcon();
		
		View container = null;
		if (action.isTitle()) {
			container = mInflater.inflate(R.layout.quick_action_title_strip, null);
			TextView text 	= (AutoResizeTextView) container.findViewById(R.id.TitleTV);
			text.setText(title);
			container.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
			text.setTextColor(colors.getColor(colors.getBackground_color()));
			container.setFocusable(false);
			container.setClickable(false);
		}else {


			if (mOrientation == HORIZONTAL) {
				container = mInflater.inflate(R.layout.price_action_item_horizontal, null);
			} else {
				container = mInflater.inflate(R.layout.price_action_item_horizontal, null);
			}

			final TextView text 	= (TextView) container.findViewById(R.id.tv_title);
			ImageView img_plus 	= (ImageView) container.findViewById(R.id.iv_plus);
			TextView text_price 	= (TextView) container.findViewById(R.id.tv_price);
			Drawable plusD;
			if (action.isShowCart()) {
				plusD = mContext.getResources().getDrawable(R.drawable.icon_cart);
				
			}else {
				plusD = mContext.getResources().getDrawable(R.drawable.multi_select_d);
				
			}
			plusD.setColorFilter(colors.getColor(colors.getBackground_color()), PorterDuff.Mode.MULTIPLY);
			
			img_plus.setImageDrawable(plusD);
			text_price.setText(action.getPrice().getAmount()+" "+action.getPrice().getCurrency());
			text_price.setTextColor(colors.getColor(colors.getBackground_color()));
			text.setPadding(10, 0, 10, 0);
			text.setTextColor(colors.getColor(colors.getBackground_color()));

			ViewTreeObserver vto = text.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			    private int maxLines = -1;
			    @Override
			    public void onGlobalLayout() {
			        if (maxLines < 0 && text.getHeight() > 0 && text.getLineHeight() > 0) {
			            int height = text.getHeight();
			            int lineHeight = text.getLineHeight();
			            maxLines = height / lineHeight;
			            text.setMaxLines(maxLines);
			        }
			    }
			});
			if (title != null) {
				text.setText(title);
			} else {
				text.setVisibility(View.GONE);
			}

			final int pos 		=  mChildPos;
			final int actionId 	= action.getActionId();

			container.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mItemClickListener != null) {
						mItemClickListener.onItemClick(QuickAction.this, pos, actionId);
					}

					if (!getActionItem(pos).isSticky()) {  
						mDidAction = true;

						dismiss();
					}
				}
			});
			
			container.setFocusable(true);
			container.setClickable(true);
			
			/*
			 * prices quantity 
			 */
			final TextView quantity = (AutoResizeTextView)container.findViewById(R.id.quantity);
			quantity.setTextColor(colors.getColor(colors.getBackground_color()));
			quantity.setText(action.getQuantity()+"");
			ImageView quantityLess = (ImageView)container.findViewById(R.id.quantityLess);
			ImageView quantityMore = (ImageView)container.findViewById(R.id.quantityMore);
			if (action.getQuantity() == 1) {
				quantityLess.setAlpha(0.5f);
			}else {
				quantityLess.setAlpha(1f);
			}
			final ActionItem itemTmp = action;
			quantityLess.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int quant = itemTmp.getQuantity();

					/*try {
						cartItem = appController.getCartItemDao().queryForId(itemTmp.getId());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}*/
					//				CartItem arg0 = itemTmp;
					if (quant > 1) {
						if (quant == 2) {
							v.setAlpha(0.5f);
						}
						itemTmp.setQuantity(quant -1);
						quantity.setText(quant -1+"");

						/*boolean done = false;
						try {
							appController.getCartItemDao().createOrUpdate(cartItem);
							done  = true;
						} catch (SQLException e) {
							e.printStackTrace();
						}
						if (done) {
							fillCart();
						}*/
					}else if (quant == 1) {
						v.setAlpha(0.5f);
					} else {

					}

				}
			});

			quantityMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int quant = itemTmp.getQuantity();
					itemTmp.setQuantity(quant +1);
					quantity.setText(quant +1+"");

				}
			});
			
			if (mOrientation == HORIZONTAL && mChildPos > 1) {
	            View separator = mInflater.inflate(R.layout.horiz_separator, null);
	            
	            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
	            
	            separator.setLayoutParams(params);
	            separator.setPadding(5, 0, 5, 0);
	            
	            mTrack.addView(separator, mInsertPos);
	            
	            mInsertPos++;
	        }
			
			if (mOrientation == VERTICAL && mChildPos > 1) {
	            View separator = mInflater.inflate(R.layout.vert_separator, null);
	            
	            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, LayoutParams.WRAP_CONTENT );
	            
	            separator.setLayoutParams(params);
	            separator.setPadding(0, 5, 0, 5);
	            
	            mTrack.addView(separator, mInsertPos);
	            
	            mInsertPos++;
	        }
			
		}
		
		
		mTrack.addView(container, mInsertPos);
		
		mChildPos++;
		mInsertPos++;
	}
	
	/**
	 * Add action item
	 * 
	 * @param action  {@link ActionItem}
	 */
	public void addViewItem(ActionItem action) {
		actionItems.add(action);
		
		String title 	= action.getTitle();
		Drawable icon 	= action.getIcon();
		
		View container;
		
		if (mOrientation == HORIZONTAL) {
            container = mInflater.inflate(R.layout.price_actions_title, null);
        } else {
            container = mInflater.inflate(R.layout.price_actions_title, null);
        }
		View quickActionItemRL = container.findViewById(R.id.quickActionItemRL);
//		StateListDrawable drawable = new StateListDrawable();
//		drawable.addState(StateSet.WILD_CARD, new ColorDrawable(Color.TRANSPARENT));
////		drawable.addState(new int[]{-android.R.attr.state_focused}, new ColorDrawable(Color.TRANSPARENT));
//		drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
//		drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
//		drawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
		
//		quickActionItemRL.setBackgroundDrawable(drawable);
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape_rounded_corners);
		drawable.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
		quickActionItemRL.setBackgroundColor(colors.getColor(colors.getTitle_color()));
//		quickActionItemRL.setBackgroundDrawable(drawable);
		
		TextView text 	= (TextView) container.findViewById(R.id.tv_title);
		text.setTextColor(colors.getColor(colors.getBackground_color()));
		
		if (title != null) {
			text.setText(title);
		} else {
			text.setVisibility(View.GONE);
		}
		
		final int pos 		=  mChildPos;
		final int actionId 	= action.getActionId();
		
		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(QuickAction.this, pos, actionId);
                }
				
                if (!getActionItem(pos).isSticky()) {  
                	mDidAction = true;
                	
                    dismiss();
                }
			}
		});
		
		container.setFocusable(true);
		container.setClickable(true);
			 
		if (mOrientation == HORIZONTAL && mChildPos != 0) {
            View separator = mInflater.inflate(R.layout.horiz_separator, null);
            
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            
            separator.setLayoutParams(params);
            separator.setPadding(5, 0, 5, 0);
            
            mTrack.addView(separator, mInsertPos);
            
            mInsertPos++;
        }
		
		mTrack.addView(container, mInsertPos);
		
		mChildPos++;
		mInsertPos++;
	}
	
	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or bottom of anchor view.
	 * 
	 */
	public void show (View anchor) {
		preShow();
	
		
		int xPos, yPos, arrowPos;
		
		mDidAction 			= false;
		
		int[] location 		= new int[2];
	
		anchor.getLocationOnScreen(location);

		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] 
		                	+ anchor.getHeight());

		//mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
		int rootHeight 		= mRootView.getMeasuredHeight();
		
		if (rootWidth == 0) {
			rootWidth		= mRootView.getMeasuredWidth();
		}
		
		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight	= mWindowManager.getDefaultDisplay().getHeight();
		
		//automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos 		= anchorRect.left - (rootWidth-anchor.getWidth());			
			xPos 		= (xPos < 0) ? 0 : xPos;
			
			arrowPos 	= anchorRect.centerX()-xPos;
			
		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth/2);
			} else {
				xPos = anchorRect.left;
			}
			
			arrowPos = anchorRect.centerX()-xPos;
		}
		
		int dyTop			= anchorRect.top;
		int dyBottom		= screenHeight - anchorRect.bottom;

		boolean onTop		= (dyTop > dyBottom) ? true : false;

		if (onTop) {
			if (rootHeight > dyTop) {
				yPos 			= 15;
				LayoutParams l 	= mScroller.getLayoutParams();
				l.height		= dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
			
			if (rootHeight > dyBottom) { 
				LayoutParams l 	= mScroller.getLayoutParams();
				l.height		= dyBottom;
			}
		}
		
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}
	
	/**
	 * Set animation style
	 * 
	 * @param screenWidth screen width
	 * @param requestedX distance from left edge
	 * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view
	 * 		  and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;

		switch (mAnimStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			break;
					
		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			break;
					
		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
		break;
			
		case ANIM_REFLECT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
		break;
		
		case ANIM_AUTO:
			if (arrowPos <= screenWidth/4) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth/4 && arrowPos < 3 * (screenWidth/4)) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			}
					
			break;
		}
	}
	
	/**
	 * Show arrow
	 * 
	 * @param whichArrow arrow type resource id
	 * @param requestedX distance from left screen
	 */
	private void showArrow(int whichArrow, int requestedX) {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

        final int arrowWidth = mArrowUp.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);
        
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)showArrow.getLayoutParams();
       
        param.leftMargin = requestedX - arrowWidth / 2;
        
        hideArrow.setVisibility(View.INVISIBLE);
    }
	
	/**
	 * Set listener for window dismissed. This listener will only be fired if the quicakction dialog is dismissed
	 * by clicking outside the dialog or clicking on sticky item.
	 */
	public void setOnDismissListener(QuickAction.OnDismissListener listener) {
		setOnDismissListener(this);
		
		mDismissListener = listener;
	}
	
	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}
	
	
	private LayerDrawable buildPinIcon() {
		/*
		 * Construct the pin icon
		 */
		
		
		
		LayerDrawable result = null;
		
		BitmapFactory.Options myOptions = new BitmapFactory.Options();
	    myOptions.inDither = true;
	    myOptions.inScaled = false;
	    myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
	    myOptions.inPurgeable = true;

	    Bitmap bitmap = null;
//		bitmap = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.icon_cart), null, myOptions);
//		bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_cart);
		Options options2 = new BitmapFactory.Options();
		options2.outHeight = 24;
		options2.outWidth = 24;
		bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_cart, options2 );
		bitmap = Bitmap.createScaledBitmap(bitmap, 25, 25, false);
		
		Bitmap workingBitmap = Bitmap.createBitmap(25, 25, bitmap.getConfig()); //Bitmap.createBitmap(bitmap);
	    Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setColor(colors.getColor(colors.getTitle_color()));
	    paint.setAntiAlias(true);
	    
	    Canvas canvas = new Canvas(mutableBitmap);
	    canvas.drawCircle(bitmap.getWidth()/2 + 10, bitmap.getHeight()/2 + 10, 15, paint);
//	    canvas.drawBitmap(bitmap, 10, 10, null);
//	    canvas.drawBitmap(bitmap, 10, 10, null);
		

//			Drawable pincolor = getResources().getDrawable(R.drawable.pin);
//			pincolor.setColorFilter(new PorterDuffColorFilter(colors
//					.getColor(pinColor), PorterDuff.Mode.MULTIPLY));
			try {
				result = new LayerDrawable( new Drawable[] { new BitmapDrawable(mutableBitmap),
								new BitmapDrawable(BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.icon_cart))) });					
								
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
//			result = new BitmapDrawable(mutableBitmap);
	    
		return result;

	}
	
	
	/**
	 * Listener for item click
	 *
	 */
	public interface OnActionItemClickListener {
		public abstract void onItemClick(QuickAction source, int pos, int actionId);
	}
	
	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
	
	
	
}