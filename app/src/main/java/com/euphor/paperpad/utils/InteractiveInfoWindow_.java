package com.euphor.paperpad.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.WebViewFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Location;
import com.euphor.paperpad.Beans.interactiveMap.Point;

import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.utils.quickAction.PopupWindows;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;

/**
 * 
 *  EuphorDev04
 * 
 */

public class InteractiveInfoWindow_ extends PopupWindows implements OnDismissListener{



    public InteractiveInfoWindow_(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    private Point point;
    //private View mRootView;
    private FragmentActivity activity;
    private Context context;
    private Location location;

    private LayoutInflater mInflater;
    //	private ScrollView mScroller;
//	private OnActionItemClickListener mItemClickListener;
    private com.euphor.paperpad.utils.actionsPrices.QuickAction.OnDismissListener mDismissListener;


//	private boolean mDidAction;



    private static String POLICE, POLICE_DISCRIPT;
    private int width, height;


    //   private int mOrientation;
//    private int rootWidth=0;
    private Colors colors;

//	private boolean mDidAction;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_REFLECT = 4;
    public static final int ANIM_AUTO = 5;

    private View mView;
//
//	public MyLocationInfos(Context context){
//		super(context);
//	}


    public InteractiveInfoWindow_(Context context, Point point, Colors colors) {
        super(context);

        this.context = context;
        this.point = point;
        this.colors = colors;
//		mOrientation = orientation;

        mInflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // setContentView(R.layout.info_dialog_map);
        setRootViewId(R.layout.info_dialog_map);


    }


    /**
     * Set root view.
     *
     * @param id Layout resource id
     */
    public void setRootViewId(int id) {

        mView	= (ViewGroup) mInflater.inflate(id, null, false);
        mView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        setContentView(mView);
    }

    private boolean showItineraryButton;

    public void setItineraryButton(boolean showItineraryButton) {

        this.showItineraryButton = showItineraryButton;

    }
    public boolean showItineraryButton() {

        return this.showItineraryButton;
    }

    public void setDimensionByMarkerLocation(Point point){



        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(metrics);

        if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
            width = 380;
            height = 415;
        }else if(metrics.densityDpi == 240) {
            width = 380;
            height = 415;
        }
        else if(metrics.densityDpi > 219 && metrics.densityDpi < 480) {
            width = 500;
            height = 580;
        }else if(metrics.densityDpi >= 480) {
            width = 650;
            height = 850;
        }else {
            width = 400;
            height = 320;

        }

        if(point.getIllustration() != null  && point.getIllustration().getLink() != null) {
            height += (int) context.getResources().getDimension(R.dimen.infoWindow_height);
        }


        if(point.getText() == null || point.getText().isEmpty())
            height -= 50;

        if (point.getLink() ==null || point.getLink().isEmpty())
            height -= 50;

            height -= 50;

        setWidht(width);
        setHeight(height);


    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidht(int width) {
        this.width = width;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public void setInfoDialogMarkerView(Point point, int BubbleDrection){


//		Typeface font = Typeface.createFromAsset(context.getAssets(), POLICE);

//		color_txt_dscript	= new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}},
//				new int[]{colors.getColor(colors.getBackground_color()),
//				colors.getColor(colors.getBody_color())});
//
//        Typeface font_discript = Typeface.createFromAsset(context.getAssets(), POLICE_DISCRIPT);

        //LayoutInflater inflater = LayoutInflater.from(context);
        //mView = mInflater.inflate(R.layout.info_window, null, false);
        LinearLayout backHolder = (LinearLayout)mView.findViewById(R.id.backHolder);
        Drawable popover = null;// = context.getResources().getDrawable(R.drawable.popover);

        switch (BubbleDrection) {

            case 0:
                popover = context.getResources().getDrawable(R.drawable.right_bubble_);

                break;

            case 90:
                popover = context.getResources().getDrawable(R.drawable.top_bubble_);

                break;

            case 180:
                popover = context.getResources().getDrawable(R.drawable.left_bubble_);

                break;


            case 270:
                popover = context.getResources().getDrawable(R.drawable.bottom_bubble_);

                break;

            default:
                popover = context.getResources().getDrawable(R.drawable.left_bubble_);
                break;
        }

        popover.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color()), PorterDuff.Mode.MULTIPLY));

        if(BubbleDrection != -1)
            backHolder.setBackgroundDrawable(popover);
        else {
            backHolder.setBackgroundColor(colors.getColor(colors.getTitle_color()));
        }


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(metrics);
        if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
            width = 380;
            height = 415;
        }else if(metrics.densityDpi == 240) {
            width = 380;
            height = 415;
        }
        else if(metrics.densityDpi > 219 && metrics.densityDpi < 480) {
            width = 500;
            height = 580;
        }else if(metrics.densityDpi >= 480) {
            width = 650;
            height = 850;
        }else {
            width = 400;
            height = 320;

        }


        backHolder.setBackgroundColor(colors.getColor(colors.getTitle_color()));




        TextView infoWinTV = (TextView)mView.findViewById(R.id.mapBubbleTV);
        infoWinTV.setText(point.getTitle());
        infoWinTV.setTypeface(MainActivity.FONT_BODY);
        infoWinTV.setTextColor(colors.getColor(colors.getBackground_color()));


        ImageView closeInfoWindow = (ImageView)mView.findViewById(R.id.closeInfoWindow);
        closeInfoWindow.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()), PorterDuff.Mode.MULTIPLY));
        closeInfoWindow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InteractiveInfoWindow_.this.dismiss();
            }


        });



        ImageView infoWinImg = (ImageView)mView.findViewById(R.id.mapBubbleImg);
        infoWinImg.setScaleType(ScaleType.CENTER_CROP);
        Illustration illustration = point.getIllustration();


        String path;


        if(illustration != null) {
            infoWinImg.setVisibility(View.VISIBLE);
            if (!illustration.getPath().isEmpty()) {
                path = illustration.getPath();
                Glide.with(context).load(new File(path)).into(infoWinImg);
            } else if (illustration.getLink() != null && !illustration.getLink().isEmpty()) {
                path = illustration.getLink();
                Glide.with(context).load((path.isEmpty()) ? "http" : path).into(infoWinImg);
            }else {
                infoWinImg.setVisibility(View.GONE);
            }
        }else {
            infoWinImg.setVisibility(View.GONE);
        }




        TextView txt_descript = (TextView)mView.findViewById(R.id.mapBubbleDiscript);

        if(point.getText() != null && !point.getText().isEmpty()) {
//			height += 100;
            txt_descript.setVisibility(View.VISIBLE);
            txt_descript.setText(point.getText());
            txt_descript.setTypeface(MainActivity.FONT_BODY);
            txt_descript.setTextColor(colors.getColor(colors.getBackground_color()));

            //height += txt_descript.getMeasuredHeight();
        }
        else {
            txt_descript.setVisibility(View.GONE);
            //height -= 50;
        }


        LinearLayout buttonHolder = (LinearLayout)mView.findViewById(R.id.buttonHolder);

        if (point.getLink()!=null && !point.getLink().isEmpty()) {


            TextView btnTV = (TextView)mView.findViewById(R.id.tvButton);
            btnTV.setTypeface(MainActivity.FONT_TITLE);
            btnTV.setTextColor(colors.getColor(colors.getBackground_color()));

            ArrowImageView arrowIW = (ArrowImageView)mView.findViewById(R.id.arrowInfoWindow);

            buttonHolder.setVisibility(View.VISIBLE);
            btnTV.setTextColor(colors.getColor(colors.getBackground_color()));
            btnTV.setText("Visit site...");
            Paint paint = new Paint();
            paint.setColor(colors.getColor(colors.getBackground_color()));
            arrowIW.setPaint(paint);
            buttonHolder.setTag(point);
            buttonHolder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Point point = (Point)v.getTag();
                    String link = point.getLink();
                    WebViewFragment webViewFragment = new WebViewFragment();
                    ((MainActivity)context).bodyFragment = "WebViewFragment";
                    // In case this activity was started with special instructions from an Intent,
                    // pass the Intent's extras to the fragment as arguments
                    ((MainActivity)context).extras = new Bundle();
                    ((MainActivity)context).extras.putString("link", link);
                    webViewFragment.setArguments(((MainActivity)context).extras);
                    // Add the fragment to the 'fragment_container' FrameLayout
                    ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();



                    dismiss();
                }

            });

        }else {
            buttonHolder.setVisibility(View.GONE);
        }


//		imgItinerence.setBackgroundDrawable(drawable_);


        mView.findViewById(R.id.itinerenceLayout).setVisibility(View.GONE);
        //mRootView.setLayoutParams(new LayoutParams(width, height));
        mView.setLayoutParams(new LayoutParams(width, height));

        //setView(mView);

    }

    public void hide(){
        mWindow.dismiss();
    }

    public void show (View anchor) {

        preShow();

        mWindow.setWidth(width);
        mWindow.setHeight(height);
        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)anchor.getX(), (int)anchor.getY());

    }

    public void showInCenter(View anchor) {
        preShow();
        mWindow.setWidth(350);
        mWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);

//		alert = new AlertDialog.Builder(activity).create();
//		alert.setView(mView);
////		alert.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
////			@Override
////			public void onClick(DialogInterface dialog, int which){
////				dialog.dismiss();
////			}
////		});
//		alert.show();

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
        if (mDismissListener != null) {
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

    public boolean isShown(){
        return mWindow.isShowing();
    }



}
