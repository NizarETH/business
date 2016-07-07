package com.euphor.paperpad.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.WebViewFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.interactiveMap.Point;

import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;

/**
 * 
 *  EuphorDev04
 * 
 */

public class InteractiveSmartInfoWindow extends AlertDialog {

    public InteractiveSmartInfoWindow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setContentView(mView);
         activity = (MainActivity)context;
        super.onCreate(savedInstanceState);
    }



    //	private View mRootView;
    private FragmentActivity activity;
    private Context context;
    private Point point;

    private LayoutInflater mInflater;
    //	private ScrollView mScroller;
//	private OnActionItemClickListener mItemClickListener;
    private com.euphor.paperpad.utils.actionsPrices.QuickAction.OnDismissListener mDismissListener;


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


    public InteractiveSmartInfoWindow(Context context, Point point, Colors colors) {
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
    }

    private boolean showItineraryButton;

    public void setItineraryButton(boolean showItineraryButton) {

        this.showItineraryButton = showItineraryButton;

    }
    public boolean showItineraryButton() {

        return this.showItineraryButton;
    }


    public void setInfoDialogMarkerView(Point point) {


        LinearLayout backHolder = (LinearLayout)mView.findViewById(R.id.backHolder);

        backHolder.setBackgroundColor(colors.getColor(colors.getBackground_color()));


        String color = point.getColor();
        if(color == null || color.isEmpty()){
            color = "000000";
        }

        TextView infoWinTV = (TextView)mView.findViewById(R.id.mapBubbleTV);
        infoWinTV.setText(point.getTitle());
        infoWinTV.setTypeface(MainActivity.FONT_BOLD);
        infoWinTV.setTextColor(colors.getColor(color));


        ImageView closeInfoWindow = (ImageView)mView.findViewById(R.id.closeInfoWindow);
        closeInfoWindow.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(color), PorterDuff.Mode.MULTIPLY));
        closeInfoWindow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InteractiveSmartInfoWindow.this.dismiss();
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
            txt_descript.setTextColor(colors.getColor(color));

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
            btnTV.setTextColor(colors.getColor(color));

            ArrowImageView arrowIW = (ArrowImageView)mView.findViewById(R.id.arrowInfoWindow);

            buttonHolder.setVisibility(View.VISIBLE);
            btnTV.setTextColor(colors.getColor(color));
            btnTV.setText("Visit site...");
            Paint paint = new Paint();
            paint.setColor(colors.getColor(color));
            arrowIW.setPaint(paint);
            buttonHolder.setTag(point);
            buttonHolder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                            Point point = (Point)v.getTag();
                            String link = point.getLink();
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)activity).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)activity).extras = new Bundle();
                             if (link.contains(".pdf")) {
                               link = "http://docs.google.com/gview?embedded=true&url=" + link;
                                            }
                            ((MainActivity)activity).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)activity).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            ((MainActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();



                    dismiss();
                }

            });

        }else {
            buttonHolder.setVisibility(View.GONE);
        }


//		imgItinerence.setBackgroundDrawable(drawable_);


        mView.findViewById(R.id.itinerenceLayout).setVisibility(View.GONE);


        setView(mView);

    }



}
