package com.euphor.paperpad.utils.actionsSummary;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;

import java.io.IOException;
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
            setRootViewId(R.layout.summary_popup_vertical);
        }
        Drawable drawable = mScroller.getBackground();
        drawable.setColorFilter(colors.getColor("FFFFFF"/*colors.getBackground_color()*/, "E2"), Mode.MULTIPLY);//.getTitle_color()), Mode.MULTIPLY);
        mScroller.setBackgroundDrawable(drawable);
        
        //mScroller.setBackgroundColor(Color.TRANSPARENT);
        
        Drawable drawableArrowDown = mArrowDown.getDrawable();
        if (drawableArrowDown != null) {
			drawableArrowDown.setColorFilter(colors.getColor("FFFFFF"/*colors.getBackground_color()*/, "CC"), Mode.MULTIPLY);//colors.getTitle_color()), Mode.MULTIPLY);
//			drawableArrowDown.setAlpha(64);
			mArrowDown.setBackgroundDrawable(drawableArrowDown);
		}
        Drawable drawableArrowUp = mArrowUp.getDrawable();
        if (drawableArrowUp != null) {
			drawableArrowUp.setColorFilter(colors.getColor("FFFFFF"/*colors.getBackground_color()*/, "CC"), Mode.MULTIPLY);//colors.getTitle_color()), Mode.MULTIPLY);colors.getTitle_color()), Mode.MULTIPLY);
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
		String title 	= action.getElement().getTitle();
		
		String icon = null;
		if (action.getElement().getTab() != null) {
			icon = action.getElement().getTab().getIcon();
		}
		int displayType = action.getElement().getDisplay();
		
		View container = null;
		if (action.isTitle()) {
			container = mInflater.inflate(R.layout.quick_action_title_strip, null);
			TextView text 	= (AutoResizeTextView) container.findViewById(R.id.TitleTV);
			text.setGravity(Gravity.CENTER_VERTICAL);
			text.setText(title);
			container.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getTitle_color(), "B2"));
			text.setTextColor(colors.getColor(colors.getBackground_color(), "E5"));
			container.setFocusable(false);
			container.setClickable(false);

		}else {


			if (mOrientation == HORIZONTAL) {
				container = mInflater.inflate(R.layout.price_action_item_horizontal, null);
			} else {
				container = mInflater.inflate(R.layout.price_action_item_horizontal, null);
				if (displayType == 1) {
					container = mInflater.inflate(R.layout.parent_summary_action_item_horizontal, null);
				}else if (displayType == 2) {
					container = mInflater.inflate(R.layout.category_summary_action_item_horizontal, null);
				}else if (displayType == 3) {
					container = mInflater.inflate(R.layout.subcategory_summary_action_item_horizontal, null);
				}
//				container = mInflater.inflate(R.layout.price_action_item_horizontal, null);
			}

			TextView text 	= (TextView) container.findViewById(R.id.tv_title);
			ArrowImageView arrow = (ArrowImageView) container.findViewById(R.id.arrow);
			Paint paint = new Paint();
			if (displayType == 1) {
				ImageView img_plus = (ImageView) container.findViewById(R.id.iv_plus);
				Drawable plusD = null;
				try {
					plusD = new BitmapDrawable(BitmapFactory.decodeStream(mContext.getAssets().open(icon)));
					plusD.setColorFilter(colors.getColor(colors.getBackground_color(), "E5"), PorterDuff.Mode.MULTIPLY);
					img_plus.setImageDrawable(plusD);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				paint.setColor(colors.getColor(colors.getBackground_color()));
				arrow.setPaint(paint );
				if(container.findViewById(R.id.quickActionItemRL) != null){
					container.findViewById(R.id.quickActionItemRL).setBackgroundColor(colors.getColor(colors.getTitle_color(), "B2"));
				}
				
				text.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
				text.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
				text.setTextColor(colors.getColor(colors.getBackground_color(), "E5"));
				if(container.findViewById(R.id.separator) != null){
					container.findViewById(R.id.separator).setVisibility(View.VISIBLE);
					//container.findViewById(R.id.separator).setBackgroundColor(colors.getColor("FFFFFF"/*colors.getBackground_color()*/, "11"));
				}
			}else {
				paint.setColor(colors.getColor(colors.getForeground_color()));
				arrow.setPaint(paint);
				//container.setBackgroundColor(colors.getColor(colors.getBackground_color(), "CC"));
				if (displayType == 2){
					text.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
					text.setTypeface(MainActivity.FONT_BODY);//, Typeface.BOLD);
				}
				else{
					text.setGravity(Gravity.LEFT);
					text.setTextAppearance(mContext, android.R.style.TextAppearance_Small);
					text.setTypeface(MainActivity.FONT_BODY);
				}
				text.setTextColor(0xFF686868);//colors.getColor(colors.getTitle_color()));
			}
			
			
			text.setPadding(10, 10, 10, 10);
			

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
			
			StateListDrawable stateListDrawable = new StateListDrawable();

							
							stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(0x30686868));
							stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(0x30686868)); 

			container.setBackgroundDrawable(stateListDrawable);
			
			container.setFocusable(true);
			container.setClickable(true);
			
			
			
		/*	if (mOrientation == HORIZONTAL && mChildPos > 1) {
	            View separator = mInflater.inflate(R.layout.horiz_separator, null);
	            
	            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
	            
	            separator.setLayoutParams(params);
	            separator.setPadding(5, 0, 5, 0);
	            
	            mTrack.addView(separator, mInsertPos);
	            
	            mInsertPos++;
	        }
			
			if (mOrientation == VERTICAL && mChildPos > 1) {
				View aseparator = new View(mContext);
				aseparator.setBackgroundColor(Color.parseColor("#88777777"));
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, 1 );
				params.setMargins(10, 0, 10, 0);
				aseparator.setLayoutParams(params);
				mTrack.addView(aseparator, mInsertPos);
				
	            View separator = mInflater.inflate(R.layout.vert_separator, null);
	            
	            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, LayoutParams.WRAP_CONTENT );
	            
	            separator.setLayoutParams(params);
	            separator.setPadding(20, 5, 20, 5);
	            
	            mTrack.addView(separator, mInsertPos);
	            
	            mInsertPos++;
	        }*/
			
			
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
		
		ImageView img = (ImageView) container.findViewById(R.id.iv_plus);
		((android.widget.RelativeLayout.LayoutParams)img.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
		((android.widget.RelativeLayout.LayoutParams)img.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		((android.widget.RelativeLayout.LayoutParams)img.getLayoutParams()).bottomMargin = 5;
		
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_exit);
		drawable.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
		img.setBackgroundDrawable(drawable);
//		Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape_rounded_corners);
//		drawable.setColorFilter(colors.getColor(colors.getBackground_color()), Mode.MULTIPLY);
//		quickActionItemRL.setBackgroundDrawable(drawable);
		
//		quickActionItemRL.setBackgroundColor(colors.getColor("FFFFFF"/*colors.getBackground_color()*/, "CC"));
		TextView text 	= (TextView) container.findViewById(R.id.tv_title);
		((android.widget.RelativeLayout.LayoutParams)text.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		((android.widget.RelativeLayout.LayoutParams)text.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		((android.widget.RelativeLayout.LayoutParams)text.getLayoutParams()).topMargin = 15;
		text.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
		text.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
		text.setTextColor(0xFF686868);//colors.getColor(colors.getTitle_color()));
		
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