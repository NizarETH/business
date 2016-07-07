/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.PriceCallBack;
import com.euphor.paperpad.activities.main.SplashActivity;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.Cart;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.Beans.Tab;

import com.euphor.paperpad.utils.ColorTransformation;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.TabComparator;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.actionsSummary.ActionItem;
import com.euphor.paperpad.utils.actionsSummary.ElementSummary;
import com.euphor.paperpad.utils.actionsSummary.QuickAction;
import com.euphor.paperpad.utils.actionsSummary.QuickAction.OnActionItemClickListener;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.euphor.paperpad.widgets.HorizontalScrollViewExt;
import com.euphor.paperpad.widgets.ScrollViewExt;
import com.euphor.paperpad.widgets.ScrollViewExt.ScrollViewListener;


import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class TabsSupportFragment extends Fragment implements PriceCallBack, ScrollViewListener, com.euphor.paperpad.widgets.HorizontalScrollViewExt.ScrollViewListener	 {


    QuickAction quickAction;

    public interface ActionCallBack {
        void onTabClick(Tab tab, int index);
        void onLanguageChanged();
    }


    private ActionCallBack mCallBack = new ActionCallBack() {

        @Override
        public void onTabClick(Tab tab, int index) {
            Log.i(TAG, "Dumb implementation");

        }

        @Override
        public void onLanguageChanged() {
            // TODO Auto-generated method stub

        }
    };

    public Tab seletedTab;
    private static final String TAG = "TabsFragment";
    private Activity activity;
    public LinearLayout tabsContainer;
    public List<Tab> tabs;
    private MainActivity mainActivity;
    private BitmapDrawable mDrawable;
    //private ImageView tabImg;
    private Bitmap bm;
    private View scrollView; //View
    public int indexSeletedTab;
    private Realm realm;

    //	private ImageLoader imageLoader;



    //	private DisplayImageOptions options;

    private Colors colors;

    public LayoutInflater inflater;

    //private boolean bottomNav = false;
    public static short positionNav = -1;

    protected PopupWindow pw;

    private boolean showFrench = false;
    private boolean showEnglish = false;
    private boolean showSpanish = false;
    private boolean showRussian = false;

    private ImageView flag;

    ArrowImageView tabUpArrow, tabDownArrow;

    /**
     *
     */
    public TabsSupportFragment() {
        // TODO Auto-generated constructor stub
    }


    /*
     * (non-Javadoc)
     *
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);


    }

    SharedPreferences wmbPreference;

    private String lang;

    private boolean isTablet;

    private TextView totalPriceTV;
    private TextView nmbrProducts;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        mCallBack = (ActionCallBack)activity;
        mainActivity = (MainActivity)activity;
//new AppController(getActivity());
        		realm = Realm.getInstance(getActivity());
        colors = ((MainActivity)activity).colors;
        Parameters paramColor =realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(paramColor);
        }
        Parameters parameters = null;
        parameters = realm.where(Parameters.class).findFirst();


        if (parameters != null) {
            if (parameters.getNavigation_type()!= null) {
                if(parameters.getNavigation_type().contains("top_wide")){
                    positionNav = 0;
                }
                else if (parameters.getNavigation_type().contains("bottom")) {
                    positionNav = 1;
                    //bottomNav = true;
                }
                else if (parameters.getNavigation_type().contains("top_narrow")) {
                    positionNav = 2;
                    //bottomNav = true;
                }else {

                    //bottomNav = false;
                    positionNav = 3;
                }
            }else {
                //bottomNav = true;
                positionNav = 1;
            }

        }

        wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        lang = wmbPreference.getString(Utils.LANG, "fr");
        isTablet = Utils.isTablet(activity);
        super.onAttach(activity);
    }



    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;

        Parameters params = null;

        params = realm.where(Parameters.class).findFirst();// appController.getParametersDao().queryForId(1);


        //		bottomNav = true;
        LinearLayout svContainerParent = null;
        if (isTablet) {
            if (positionNav == 0 || positionNav == 1 || positionNav == 2) {
                view = inflater.inflate(R.layout.horizontal_tabs_fragment, container, false);
                //if(positionNav == 0)
                //    view.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 60));
            }else {
                view = inflater.inflate(R.layout.tabs_fragment, container, false);
            }
            svContainerParent = (LinearLayout)view.findViewById(R.id.svContainerParent);
            svContainerParent.setBackgroundColor(mainActivity.colors.getColor(mainActivity.colors.getSide_tabs_background_color()));

        }else {
            view = inflater.inflate(R.layout.horizontal_tabs_fragment, container, false);
            svContainerParent = (LinearLayout)view.findViewById(R.id.svContainerParent);
            svContainerParent.setBackgroundColor(mainActivity.colors.getColor(mainActivity.colors.getTabs_background_color()));

        }


        //		LinearLayout svContainer = (LinearLayout)view.findViewById(R.id.svContainer);
        //		svContainerParent.setBackgroundDrawable(mainActivity.colors.getBackTabsLG());
        Log.e(" Yab Color Background : ", " mainActivity.colors.getTabs_background_color() : "+mainActivity.colors.getSide_tabs_background_color()+" ,  colors.getTabs_background_color() : "+colors.getSide_tabs_background_color());
        final LayoutInflater tmpInflater = inflater;

        tabUpArrow = (ArrowImageView)view.findViewById(R.id.tabUpArrow);
        //tabUpArrow.setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
        tabUpArrow.setDiffOfColorCode(colors.getColor(colors.getSide_tabs_foreground_color(),"80"), colors.getColor(colors.getSide_tabs_foreground_color(),"80"));
        tabUpArrow.setBackgroundColor(colors.getColor(colors.getSide_tabs_foreground_color(), "00"));

        tabDownArrow = (ArrowImageView)view.findViewById(R.id.tabDownArrow);
        //tabDownArrow.setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
        tabDownArrow.setDiffOfColorCode(colors.getColor(colors.getSide_tabs_foreground_color(),"80"), colors.getColor(colors.getSide_tabs_foreground_color(), "80"));
        tabDownArrow.setBackgroundColor(colors.getColor(colors.getSide_tabs_foreground_color(), "00"));

        tabUpArrow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(positionNav == 0 || positionNav == 1 || positionNav == 2 || !isTablet)
                    ((HorizontalScrollViewExt)scrollView).setScrollX(((HorizontalScrollViewExt)scrollView).getScrollX() - 120);
                else
                    ((ScrollViewExt)scrollView).setScrollY(((ScrollViewExt)scrollView).getScrollY() - 120);
            }
        });

        tabDownArrow.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

                if(positionNav == 0 || positionNav == 1 || positionNav == 2 || !isTablet)
                    ((HorizontalScrollViewExt)scrollView).setScrollX(((HorizontalScrollViewExt)scrollView).getScrollX() + 120);
                else
                    ((ScrollViewExt)scrollView).setScrollY(((ScrollViewExt)scrollView).getScrollY() + 120);
            }
        });

        //		if(bottomNav || !isTablet){
        //			tabUpArrow.setRotation(180);
        //			tabDownArrow.setRotation(0);
        //		}

        // Start language task
        LinearLayout languageContainer = (LinearLayout)view.findViewById(R.id.LanguageContainer);
        if ( !isTablet) {
            languageContainer.setVisibility(View.GONE);
        }
        Parameters ParamLang=realm.where(Parameters.class).findFirst();
        List</*Language*/ MyString> languages = new ArrayList</*Language*/MyString>();
        languages =ParamLang.getList() ;// appController.getLanguageDao().queryForAll();

        if (languages.size()>1) {

            flag = (ImageView)view.findViewById(R.id.FlagLanguage);
            int icon_size = (int) Utils.dpToPixels(getActivity(), 25f);
            flag.getLayoutParams().width = icon_size;
            flag.getLayoutParams().height = icon_size;
            if (lang.equals("fr")) {
                flag.setImageDrawable(getResources().getDrawable(R.drawable.english_2x));
                // change the locale to use other languages
                Utils.changeLocale("fr", getActivity());
            } else if (lang.equals("en")) {
                flag.setImageDrawable(getResources().getDrawable(R.drawable.french_2x));

                Utils.changeLocale("en",getActivity());
            }
            else if (lang.equals("es")) {
                flag.setImageDrawable(getResources().getDrawable(R.drawable.spanish_2x));

                Utils.changeLocale("es",getActivity());
            }
            else if (lang.equals("ru")) {
                flag.setImageDrawable(getResources().getDrawable(R.drawable.russian_2x));

                Utils.changeLocale("ru",getActivity());
            }

            for (Iterator</*Language*/MyString> iterator = languages.iterator(); iterator
                    .hasNext();) {
                /*Language*/MyString language = (/*Language*/MyString) iterator.next();
                if (language.getMyString().equals("fr")) {
                    showFrench  = true;
                }else if (language.getMyString().equals("en")) {
                    showEnglish = true;
                }else if (language.getMyString().equals("es")) {
                    showSpanish = true;
                }
                else if (language.getMyString().equals("ru")) {
                    showRussian = true;
                }

            }

            languageContainer.setOnClickListener(new OnClickListener() {



                @Override
                public void onClick(View v) {
                    //					mCallBack.onLanguageChanged();
                    View layout = tmpInflater.inflate(
                            R.layout.language_dialog, null);
                    LinearLayout frenchContainer = (LinearLayout) layout
                            .findViewById(R.id.frenchContainer);
                    if (!showFrench) {
                        frenchContainer.setVisibility(View.GONE);
                    }
                    LinearLayout englishContainer = (LinearLayout) layout
                            .findViewById(R.id.englishContainer);

                    LinearLayout spanishContainer = (LinearLayout) layout
                            .findViewById(R.id.spanishContainer);

                    LinearLayout russianContainer = (LinearLayout) layout
                            .findViewById(R.id.russianContainer);

                    if (lang.equals("fr")) {
                        frenchContainer.setAlpha(0.7F/*0.7 valeur existante*/);
                        englishContainer.setAlpha(1F);
                        spanishContainer.setAlpha(1F);
                    } else if (lang.equals("en")) {
                        frenchContainer.setAlpha(1F);
                        englishContainer.setAlpha(0.7F);
                        spanishContainer.setAlpha(1F);
                    }else if (lang.equals("es")) {
                        frenchContainer.setAlpha(1F);
                        englishContainer.setAlpha(1F);
                        spanishContainer.setAlpha(0.7F);
                    }else if (lang.equals("ru")) {
                        frenchContainer.setAlpha(1F);
                        englishContainer.setAlpha(1F);
                        spanishContainer.setAlpha(1F);
                        russianContainer.setAlpha(0.7F);
                    }

                    if (showFrench) {
                        frenchContainer.setVisibility(View.VISIBLE);

                        if(frenchContainer.getAlpha() == 1.0f)
                            frenchContainer
                                    .setOnClickListener(new OnClickListener() {


                                        @Override
                                        public void onClick(View v) {
                                            flag.setImageDrawable(getResources()
                                                    .getDrawable(
                                                            R.drawable.french_2x));
                                            //langTxt.setTypeface(FONT_REGULAR);
                                            SharedPreferences.Editor editor = wmbPreference
                                                    .edit();
                                            editor.putString(Utils.LANG, "fr");
                                            editor.commit();
                                            //String lang = wmbPreference.getString(Utils.LANG,"fr");
                                            //										LANGUAGE_ID = Integer.parseInt(lang);
                                            Intent intent = new Intent(getActivity(),SplashActivity.class);
                                            Bundle b = new Bundle();
                                            b.putString(Utils.LANG, "fr");
                                            int id = 0;
                                            List<Parameters> parameters = realm.where(Parameters.class).findAll();
                                            //appController.getParametersDao().queryForAll();
                                            if (parameters.size()>0) {
                                                Parameters params = parameters.get(0);
                                                id = params.getId();
                                            }
                                            b.putInt("ID_MENU", id);
                                            intent.putExtras(b);
                                            startActivity(intent);
                                            pw.dismiss();
                                        }


                                    });
                    } else {
                        frenchContainer.setVisibility(View.GONE);
                    }

                    if (showEnglish) {
                        englishContainer.setVisibility(View.VISIBLE);

                        if(englishContainer.getAlpha() == 1.0f)
                            englishContainer
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            flag.setImageDrawable(getResources()
                                                    .getDrawable(
                                                            R.drawable.english_2x));
                                            SharedPreferences.Editor editor = wmbPreference.edit();
                                            editor.putString(Utils.LANG, "en");
                                            editor.commit();

                                            //String lang = wmbPreference.getString(Utils.LANG,"fr");
                                            //										LANGUAGE_ID = Integer
                                            //												.parseInt(lang);
                                            //										Intent intent = new Intent();
                                            Intent intent = new Intent(getActivity(),SplashActivity.class);
                                            Bundle b = new Bundle();
                                            b.putString(Utils.LANG, "en");
                                            int id = 0;
                                            List<Parameters> parameters = realm.where(Parameters.class).findAll();//appController.getParametersDao().queryForAll();
                                            if (parameters.size()>0) {
                                                Parameters params = parameters.get(0);
                                                id = params.getId();
                                            }
                                            b.putInt("ID_MENU", id);
                                            intent.putExtras(b);
                                            startActivity(intent);
                                            pw.dismiss();
                                        }
                                    });
                    } else {
                        englishContainer.setVisibility(View.GONE);
                    }

                    if (showSpanish) {
                        spanishContainer.setVisibility(View.VISIBLE);

                        if(spanishContainer.getAlpha() == 1.0f)
                            spanishContainer
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            flag.setImageDrawable(getResources()
                                                    .getDrawable(
                                                            R.drawable.spanish_2x));
                                            SharedPreferences.Editor editor = wmbPreference.edit();
                                            editor.putString(Utils.LANG, "es");
                                            editor.commit();

                                            //String lang = wmbPreference.getString(Utils.LANG,"fr");
                                            //										LANGUAGE_ID = Integer
                                            //												.parseInt(lang);
                                            //										Intent intent = new Intent();
                                            Intent intent = new Intent(getActivity(),SplashActivity.class);
                                            Bundle b = new Bundle();
                                            b.putString(Utils.LANG, "es");
                                            int id = 0;
                                            List<Parameters> parameters = realm.where(Parameters.class).findAll(); //appController.getParametersDao().queryForAll();
                                            if (parameters.size()>0) {
                                                Parameters params = parameters.get(0);
                                                id = params.getId();
                                            }
                                            b.putInt("ID_MENU", id);
                                            intent.putExtras(b);
                                            startActivity(intent);
                                            pw.dismiss();
                                        }
                                    });
                    } else {
                        spanishContainer.setVisibility(View.GONE);
                    }


                    if (showRussian) {
                        russianContainer.setVisibility(View.VISIBLE);

                        if(russianContainer.getAlpha() == 1.0f)
                            russianContainer
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            flag.setImageDrawable(getResources()
                                                    .getDrawable(
                                                            R.drawable.russian_2x));
                                            SharedPreferences.Editor editor = wmbPreference.edit();
                                            editor.putString(Utils.LANG, "ru");
                                            editor.commit();

                                            //String lang = wmbPreference.getString(Utils.LANG,"fr");
                                            //										LANGUAGE_ID = Integer
                                            //												.parseInt(lang);
                                            //										Intent intent = new Intent();
                                            Intent intent = new Intent(getActivity(),SplashActivity.class);
                                            Bundle b = new Bundle();
                                            b.putString(Utils.LANG, "ru");
                                            int id = 0;
                                            List<Parameters> parameters = realm.where(Parameters.class).findAll();//appController.getParametersDao().queryForAll();
                                            if (parameters.size()>0) {
                                                Parameters params = parameters.get(0);
                                                id = params.getId();
                                            }
                                            b.putInt("ID_MENU", id);
                                            intent.putExtras(b);
                                            startActivity(intent);
                                            pw.dismiss();
                                        }
                                    });
                    } else {
                        russianContainer.setVisibility(View.GONE);
                    }


                    pw = new PopupWindow(layout,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    // display the popup in the center
                    pw.setOutsideTouchable(true);
                    pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pw.setFocusable(true);
                    pw.setTouchInterceptor(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                pw.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });
                    if (positionNav == 0 || positionNav == 1 || positionNav == 2) {
                        layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bonuspack_bubble_white_mod_horizontal));
                        pw.showAtLocation(v, Gravity.LEFT|Gravity.BOTTOM, 0, (int) flag.getY() + (int) Utils.dpToPixels(getActivity(), 15f));
                    }else {
                        layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bonuspack_bubble_white_mod2));
                        pw.showAsDropDown(v);
                    }

                    //					pw.showAtLocation(layout, Gravity.LEFT, -500, 100);
                    //
                }
            });
        }else {
            SharedPreferences.Editor editor = wmbPreference
                    .edit();
            editor.putString(Utils.LANG, "fr");
            editor.commit();
            Utils.changeLocale("fr", getActivity());
            languageContainer.setVisibility(View.GONE);
        }
        // End language task

        tabsContainer = new LinearLayout(activity);
        tabsContainer.setGravity(Gravity.CENTER);
        android.widget.LinearLayout.LayoutParams lParamsTabsContainer = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (isTablet) {
            if (positionNav == 0 || positionNav == 1 || positionNav == 2) {
                scrollView = (HorizontalScrollViewExt)view.findViewById(R.id.scrollViewTabs);
                ((HorizontalScrollViewExt)scrollView).setScrollViewListener(this);
                ((HorizontalScrollViewExt)scrollView).setSmoothScrollingEnabled(true);
                //((android.widget.RelativeLayout.LayoutParams)scrollView.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL);
                //				android.widget.RelativeLayout.LayoutParams relativeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                //				relativeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                //				scrollView.setLayoutParams(relativeParam);

                tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
                lParamsTabsContainer = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                lParamsTabsContainer.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
            }else {
                scrollView = (ScrollViewExt)view.findViewById(R.id.scrollViewTabs);
                ((ScrollViewExt)scrollView).setScrollViewListener(this);
                ((ScrollViewExt)scrollView).setSmoothScrollingEnabled(true);
                tabsContainer.setPadding(0, 40, 0, 80);
                tabsContainer.setOrientation(LinearLayout.VERTICAL);
                scrollView.setVerticalScrollBarEnabled(false);
                scrollView.setVerticalFadingEdgeEnabled(false);
                scrollView.setHorizontalFadingEdgeEnabled(false);
            }
        }
        else {
            scrollView = (HorizontalScrollViewExt)view.findViewById(R.id.scrollViewTabs);
            ((HorizontalScrollViewExt)scrollView).setScrollViewListener(this);
            ((HorizontalScrollViewExt)scrollView).setSmoothScrollingEnabled(true);
            tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
            lParamsTabsContainer = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            lParamsTabsContainer.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
        }

        scrollView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (isTablet) {
                    if (positionNav == 0 || positionNav == 1 || positionNav == 2) {
//						if(scrollView.getWidth() < ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight())//scrollView.getScrollX())
//							tabDownArrow.setVisibility(View.VISIBLE);

                        int diff = ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight() - (scrollView.getWidth() + scrollView.getScrollX());

                        if (diff == ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight() - scrollView.getWidth()) {
                            tabUpArrow.setVisibility(View.GONE);
                            if(scrollView.getWidth() < ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight())//scrollView.getScrollX())
                                tabDownArrow.setVisibility(View.VISIBLE);
                        }else if(diff ==  0){
                            tabDownArrow.setVisibility(View.GONE);
                            if(scrollView.getWidth() < ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight())//scrollView.getScrollX())
                                tabUpArrow.setVisibility(View.VISIBLE);
                        }else{
                            tabUpArrow.setVisibility(View.VISIBLE);
                            tabDownArrow.setVisibility(View.VISIBLE);
                        }

                    }else {
//						if(scrollView.getHeight() < ((ScrollViewExt)scrollView).getChildAt(0).getBottom()/*.getScrollY()*/)
//							tabDownArrow.setVisibility(View.VISIBLE);

                        int diff = ((ScrollViewExt)scrollView).getChildAt(0).getBottom() - (scrollView.getHeight() + scrollView.getScrollY());

                        if (diff == ((ScrollViewExt)scrollView).getChildAt(0).getBottom() - scrollView.getHeight()) {
                            tabUpArrow.setVisibility(View.GONE);
                            if(scrollView.getHeight() < ((ScrollViewExt)scrollView).getChildAt(0).getBottom())//scrollView.getScrollX())
                                tabDownArrow.setVisibility(View.VISIBLE);
                        }else if(diff ==  0){
                            tabDownArrow.setVisibility(View.GONE);
                            if(scrollView.getHeight() < ((ScrollViewExt)scrollView).getChildAt(0).getBottom())//scrollView.getScrollX())
                                tabUpArrow.setVisibility(View.VISIBLE);
                        }else{
                            tabUpArrow.setVisibility(View.VISIBLE);
                            tabDownArrow.setVisibility(View.VISIBLE);
                        }
                    }
                }else {


                    int diff = ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight() - (scrollView.getWidth() + scrollView.getScrollX());

                    if (diff == ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight() - scrollView.getWidth()) {
                        tabUpArrow.setVisibility(View.GONE);
                        if(scrollView.getWidth() < ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight())//scrollView.getScrollX())
                            tabDownArrow.setVisibility(View.VISIBLE);
                    }else if(diff ==  0){
                        tabDownArrow.setVisibility(View.GONE);
                        if(scrollView.getWidth() < ((HorizontalScrollViewExt)scrollView).getChildAt(0).getRight())//scrollView.getScrollX())
                            tabUpArrow.setVisibility(View.VISIBLE);
                    }else{
                        tabUpArrow.setVisibility(View.VISIBLE);
                        tabDownArrow.setVisibility(View.VISIBLE);
                    }


                }
            }
        });
        //		tabsContainer.setPadding(0, 40, 0, 80);
        //		tabsContainer.setOrientation(LinearLayout.VERTICAL);
        //		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tabsContainer.setLayoutParams(lParamsTabsContainer);

        tabs = new ArrayList<Tab>();
        List<Tab> tabsTmp = new ArrayList<Tab>();
        tabsTmp = realm.where(Tab.class).findAllSorted("order");//.findAll(); //appController.getTabDao().queryForAll();



        tabs = tabsTmp;
        realm.beginTransaction();
        tabs.get(0).setIsHomeGrid(true);
        tabsContainer.setTag(tabs);
        realm.commitTransaction();
        Bundle bundle = getArguments();
        this.inflater = inflater;
        if(bundle != null) {
            int index_selected = getArguments().getInt("highlighted_tab");
            indexSeletedTab = index_selected;
            boolean orientation = getArguments().getBoolean("orientation");
            if (orientation) {
                if(isTablet)
                    redrawIcons(inflater, index_selected);
                else
                    redrawSmartIcons(inflater, index_selected);
            } else {
                if(isTablet)
                    redrawIcons(inflater, Constants.DEFAULT_TAB_VALUE);
                else
                    redrawSmartIcons(inflater, Constants.DEFAULT_TAB_VALUE);
            }
        }else {

            if(isTablet)
                redrawIcons(inflater, Constants.DEFAULT_TAB_VALUE);
            else
                redrawSmartIcons(inflater, Constants.DEFAULT_TAB_VALUE);
        }
        if (isTablet) {
            if (positionNav == 0 || positionNav == 1 || positionNav == 2) {
                //				android.widget.RelativeLayout.LayoutParams relativeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                //				relativeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ((HorizontalScrollViewExt)scrollView).addView(tabsContainer, lParamsTabsContainer);//, relativeParam);

                //((android.widget.RelativeLayout.LayoutParams)scrollView.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL);
            }else {
                ((ScrollViewExt)scrollView).addView(tabsContainer);
            }
        }else {
            ((HorizontalScrollViewExt)scrollView).addView(tabsContainer, lParamsTabsContainer);
        }

        final Parameters paramsFinal = params;
        ImageView sommaireImg = (ImageView)view.findViewById(R.id.imageView1);
        Drawable mDrawableSom = sommaireImg.getBackground();
        mDrawableSom.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#80" + mainActivity.colors.getSide_tabs_foreground_color()), PorterDuff.Mode.MULTIPLY));
        sommaireImg.setBackgroundDrawable(mDrawableSom);
        LinearLayout somaireLL = (LinearLayout)view.findViewById(R.id.sommaire);
        somaireLL.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                List<ElementSummary> summaries = fillElementsSummary(tabs);
                if (quickAction != null) {
                    quickAction.dismiss();
                }
                quickAction = new QuickAction(getActivity(), QuickAction.VERTICAL, colors);
                ActionItem itemTitle = new ActionItem(paramsFinal!=null?paramsFinal.getTitle():"", 0, true);
                quickAction.addViewItem(itemTitle);
                for (int j = 0; j < summaries.size(); j++) {
                    ActionItem item = new ActionItem("", j+1, summaries.get(j));
                    quickAction.addActionItem(item);
                }
                quickAction.show(v);
                quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {

                    @Override
                    public void onItemClick(QuickAction source, int pos, int actionId) {
                        if (actionId>0) {
                            realm.beginTransaction();
                            ActionItem item = source.getActionItem(actionId);
                            int display = item.getElement().getDisplay();
                            if (display == 1) {
                                //								mainActivity.openSection(item.getElement().getTab(), (Section)item.getElement().getObject());
                                //								if (item.getElement().getTab() != null && item.getElement().getTab().getId() == 1) {
                                //									item.getElement().getTab().setIsHomeGrid(true);
                                //								}
                                mainActivity.onTabClick(item.getElement().getTab(), pos);
                            }else if (display != 1) {

                                if (item.getElement().getType().contains("contents")) {
                                    if (item.getElement().getObject() instanceof Category) {
                                        mainActivity.openCategory((Category)item.getElement().getObject());
                                    }else if (item.getElement().getObject() instanceof Child_pages) {
                                        mainActivity.openPage((Child_pages)item.getElement().getObject());
                                    }

                                }else if (item.getElement().getType().contains("gallery")) {
                                    mainActivity.findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                                    mainActivity.findViewById(R.id.swipe_container).setVisibility(View.GONE);
                                    GaleryFragment galeryFragment = new GaleryFragment();
                                    ((MainActivity)getActivity()).bodyFragment = "AlbumsGridFragment";
                                    ((MainActivity)getActivity()).extras.putInt("Album_id", ((Album)item.getElement().getObject()).getId_album());
                                    galeryFragment.setArguments(((MainActivity)getActivity()).extras);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();
                                }


                            }/*else if (display == 3) {
								mainActivity.openPage((Child_pages)item.getElement().getObject());
							}*/
                            realm.commitTransaction();
                        }


                    }
                });
            }
        });




        LinearLayout command = (LinearLayout)view.findViewById(R.id.command);
        NumberFormat nf = NumberFormat.getInstance(); // get instance
        nf.setMaximumFractionDigits(2); // set decimal places
        nf.setMinimumFractionDigits(2);
        String s = nf.format(mainActivity.total);
        totalPriceTV = (TextView)view.findViewById(R.id.cartPrice);
        if (totalPriceTV != null) {
//						totalPriceTV.setText(s);
            totalPriceTV.setTypeface(MainActivity.FONT_BOLD, Typeface.BOLD);
            totalPriceTV.setTextColor(Color.parseColor("#"+mainActivity.colors.getBackground_color()));
        }
        nmbrProducts = (TextView)view.findViewById(R.id.cartNmbr);
        String str = getResources().getString(R.string.cart_nmbr, 0);
        nmbrProducts.setText(str);
        if (nmbrProducts != null) {
//						totalPriceTV.setText(s);
            nmbrProducts.setTypeface(MainActivity.FONT_BOLD, Typeface.BOLD);
            nmbrProducts.setTextColor(Color.parseColor("#"+mainActivity.colors.getBackground_color()));
        }
        Application application = null;
        Cart cart = null;
        application = realm.where(Application.class).findFirst(); /*appController.getApplicationDataDao().queryForId(1);*/
        if (isTablet) {
            if (application != null && application.getParameters() != null && !application.getParameters().isShow_cart()) {
                command.setVisibility(View.GONE);
            }else {

                cart = realm.where(Cart.class).findFirst();//appController.getCartDao().queryForId(1);
                if (cart == null) {
                    command.setVisibility(View.GONE);
                }
            }
        }else {
            if (application != null && application.getParameters() != null && !application.getParameters().isShow_cart_smartphone()) {
                command.setVisibility(View.GONE);
            }else {

                cart = realm.where(Cart.class).findFirst();//appController.getCartDao().queryForId(1);
                if (cart == null) {
                    command.setVisibility(View.GONE);
                }
            }
        }
        if (cart != null) {
            TextView cartName = (TextView)view.findViewById(R.id.cartName);
            cartName.setText(cart.getTab_title());
            cartName.setTextColor(Color.parseColor("#"+mainActivity.colors.getBackground_color()));
            ImageView cartImg = (ImageView) view.findViewById(R.id.cartImage);
            bm = null;
            try {
                bm = BitmapFactory.decodeStream(activity.getAssets().open(
                        "icon/"+cart.getTab_icon()));
            } catch (IOException e) {
                Log.e("TabFragment", e.getMessage());
                e.printStackTrace();
            }
            mDrawable = new BitmapDrawable(bm);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color
                    .parseColor("#"
                            + mainActivity.colors
                            .getBackground_color()),
                    PorterDuff.Mode.MULTIPLY));
            cartImg.setImageDrawable(mDrawable);
        }

        command.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mainActivity.menu.showMenu();
                AppJsonWriter appJsonWriter = new AppJsonWriter(realm);
                List<CartItem> items = new ArrayList<CartItem>();
                items = realm.where(CartItem.class).findAll();//appController.getCartItemDao().queryForAll();
                appJsonWriter.writeListCartItems(items);

            }
        });

        return view;
    }



    protected List<ElementSummary> fillElementsSummary(List<Tab> pTabs) {
        List<ElementSummary> results = new ArrayList<ElementSummary>();
        for (int i = 0; i < pTabs.size(); i++) {
            Tab tab = pTabs.get(i);

            Section section = null;
            List<Section> sections = realm.where(Section.class).equalTo("id", tab.getSection_id()).findAll(); //appController.getSectionsDao().queryForEq("id", tab.getSection_id());
            if (sections.size() > 0) {
                section = sections.get(0);
            }
            if (section != null) {
                ElementSummary elm = new ElementSummary(tab.getTitle(), section.getType(), 1, section.getId(), section, tab);
                results.add(elm);
				if (section.getType().equals("gallery")) {
                    for (Iterator<Album> iterator = section.getAlbums().iterator(); iterator.hasNext();) {
                        Album album = (Album) iterator.next();
                        ElementSummary elmSub = new ElementSummary(album.getTitle(), section.getType(), 2, album.getId(), album);
                        results.add(elmSub);
                    }

                }/*else if (section.getType().equals("contact")) {

				}*/else if (section.getType().equals("contents")) {
                    for (Iterator<Category> iterator = section.getCategories().iterator(); iterator.hasNext();) {

                        Category category = (Category) iterator.next();
                        ElementSummary elmsub = new ElementSummary(category.getTitle(), section.getType(), 2, category.getId(), category);
                        results.add(elmsub);

                        if (category.getChildren_categories() != null && category.getChildren_categories().size()>0) {

                            for (Iterator<Category> iterator2 = category.getChildren_categories().iterator(); iterator2.hasNext();) {

                                Category category2 = (Category) iterator2.next();
                                ElementSummary elmsubsub = new ElementSummary(category2.getTitle(), section.getType(), 3, category2.getId(), category2);
                                results.add(elmsubsub);
                            }
                        }else if (category.getChildren_pages() != null && category.getChildren_pages().size() > 0) {
                            for (Iterator<Child_pages> iterator2 = category.getChildren_pages().iterator(); iterator2.hasNext();) {
                                Child_pages p = (Child_pages) iterator2.next();

                                ElementSummary elmsubsub = new ElementSummary(p.getTitle(), section.getType(), 3, p.getId(), p);
                                results.add(elmsubsub);
                            }
                        }
                    }

                }



            }
        }
        return results;
    }


    public void redrawIcons(LayoutInflater pInflater, int highlighted) {
        tabsContainer.removeAllViews();
        if (bm != null) {
            bm.recycle();
            bm = null;
        }
        Runtime.getRuntime().gc();

        if (pInflater != null) {
            this.inflater = pInflater;
        }

        for (int i = 0; i < tabs.size(); i++) {
            final Tab tab = tabs.get(i);
            LinearLayout tabLayout;
            if (isTablet) {
                if (positionNav == 1 || positionNav == 2) {
                    tabLayout  = (LinearLayout)inflater.inflate(R.layout.horizontal_tab_horizontal, null, false);
                }else if (positionNav == 0) {
                    tabLayout  = (LinearLayout)inflater.inflate(R.layout.horizontal_tab_vertical, null, false);
                }else {
                    tabLayout  = (LinearLayout)inflater.inflate(R.layout.tab, null, false);
                }
            }else {
                tabLayout = (LinearLayout)inflater.inflate(R.layout.tab_horizontal, null, false);
            }

            tabLayout.setTag(tab);
            ImageView tabImg = (ImageView)tabLayout.findViewById(R.id.actionImage);
            bm = null;
            try {
                bm = BitmapFactory.decodeStream(activity.getAssets().open(tab.getIcon()));
            } catch (IOException e) {
                Log.e("TabFragment", e.getMessage());
                e.printStackTrace();
            }
			/*
			 * color the white illumination back image with some color
			 */
            TextView actionTV = (AutoResizeTextView)tabLayout.findViewById(R.id.actionName);
            actionTV.setText(tab.getTitle());
            actionTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);

            if(!showRussian)
                actionTV.setTextSize(actionTV.getTextSize() + 2);
            else
                actionTV.setTextSize(actionTV.getTextSize() - 3);
            /** Uness Modif **/
            DisplayMetrics metrics = new DisplayMetrics();
            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if(metrics.densityDpi >= 213){
                actionTV.setTextSize(13);
            }
            else{
                actionTV.setTextSize(14);
            }


            //			actionTV.setHorizontallyScrolling(true);
            actionTV.setMaxLines(3);
            mDrawable = new BitmapDrawable(bm);
            ImageView illumination = (ImageView)tabLayout.findViewById(R.id.illumination);
            if (i==highlighted) {
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color
                        .parseColor("#" + mainActivity.colors.getSide_tabs_foreground_color()), PorterDuff.Mode.MULTIPLY));
                actionTV.setTextColor(Color.parseColor("#"+mainActivity.colors.getSide_tabs_foreground_color()));

                Drawable drawableIllumination = activity.getResources().getDrawable(R.drawable.illumin_back);
                //drawableIllumination.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff"+mainActivity.colors.getSide_tabs_foreground_color()),PorterDuff.Mode.MULTIPLY));
                illumination.setBackgroundDrawable(drawableIllumination);

            }else {
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color
                        .parseColor("#80" + mainActivity.colors.getSide_tabs_foreground_color()), PorterDuff.Mode.MULTIPLY));
                actionTV.setTextColor(Color.parseColor("#80"+mainActivity.colors.getSide_tabs_foreground_color()));
                illumination.setBackgroundColor(Color.TRANSPARENT);
            }
            tabImg.setImageDrawable(mDrawable);

            tabsContainer.addView(tabLayout);
            LinearLayout v = new LinearLayout(getActivity());


            if (positionNav == 0 || positionNav == 1 || positionNav == 2) {
                tabsContainer.addView(v, 25, LayoutParams.MATCH_PARENT);
                tabsContainer.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            }
            else{

                View line = new View(getActivity());
                if(i < tabs.size() - 1)
                {
                    line.setBackgroundColor(colors.getColorDefault(colors.getSide_tabs_separators_color(), "000000","44"));
                    line.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    line.setScaleY(0.6f);
                    v.setGravity(Gravity.CENTER_VERTICAL);
                    v.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 25));

                    v.addView(line);
                    v.setPadding(3, 15, 0, 15);
                    tabsContainer.addView(v, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
                }

            }

            scrollView.setFocusable(false);
            tabsContainer.setFocusable(false);
            tabLayout.setClickable(true);
            tabLayout.setFocusable(true);
            final int j = i;
            tabLayout.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View v) {
                    realm.beginTransaction();
                    seletedTab = tabs.get(j);
                    indexSeletedTab = j;
                    /*Collections.sort(tabs, new TabComparator());à voir aprés */
                    tabs.get(0).setIsHomeGrid(true);
                    mCallBack.onTabClick(tab, indexSeletedTab);
                    for (int j = 0; j < tabsContainer.getChildCount(); j++) {
                        tabsContainer.getChildAt(j).setSelected(false);
                    }
                    v.setSelected(true);
                    //ImageView tabImg = (ImageView)v.findViewById(R.id.actionImage);
                    try {
                        bm = null;
                        bm = BitmapFactory.decodeStream(activity.getAssets().open(tab.getIcon()));
                    } catch (IOException e) {
                        Log.e("TabFragment", e.getMessage());
                        e.printStackTrace();
                    }
                    redrawIcons(inflater, j);
                    realm.commitTransaction();

                }
            });
        }
    }


    public void redrawSmartIcons(LayoutInflater pInflater, int highlighted) {
        tabsContainer.removeAllViews();

        if (pInflater != null) {
            this.inflater = pInflater;
        }

        for (int i = 0; i < tabs.size(); i++) {
            final Tab tab = tabs.get(i);
            LinearLayout tabLayout = (LinearLayout)inflater.inflate(R.layout.tab_horizontal, null, false);


            tabLayout.setTag(tab);
            ImageView tabImg = (ImageView)tabLayout.findViewById(R.id.actionImage);
            TextView actionTV = (AutoResizeTextView)tabLayout.findViewById(R.id.actionName);
            actionTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            actionTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
            actionTV.setText(tab.getTitle());

            actionTV.setMaxLines(3);
            if (i == highlighted) {
                int transformationColor = mainActivity.colors.getColor(mainActivity.colors.getTabs_selected_foreground_color());
                //Glide.with(mainActivity).load(new File("android_asset/"+tab.getIcon())).asBitmap().transform(new ColorTransformation(getActivity(), transformationColor)).diskCacheStrategy(DiskCacheStrategy.SOURCE).priority(Priority.IMMEDIATE).into(tabImg);
                try {
                    Bitmap bm = BitmapFactory.decodeStream(getActivity().getAssets().open(tab.getIcon()));
                    Drawable drawable = new BitmapDrawable(bm);
                    drawable.setColorFilter(new PorterDuffColorFilter(transformationColor, PorterDuff.Mode.MULTIPLY));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tabImg.setBackground(drawable);
                    }else{
                        tabImg.setBackgroundDrawable(drawable);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                actionTV.setTextColor(Color.parseColor("#" + mainActivity.colors.getTabs_selected_foreground_color()));
                tabLayout.setBackgroundColor(mainActivity.colors.getColor(mainActivity.colors.getTabs_selected_background_color()));

            }else {
                int transformationColor = mainActivity.colors.getColor(mainActivity.colors.getSide_tabs_foreground_color());
                //Glide.with(mainActivity).load(new File("android_asset/"+tab.getIcon())).asBitmap().transform(new ColorTransformation(getActivity(), transformationColor)).diskCacheStrategy(DiskCacheStrategy.SOURCE).priority(Priority.IMMEDIATE).into(tabImg);
                try {
                    Bitmap bm = BitmapFactory.decodeStream(getActivity().getAssets().open(tab.getIcon()));
                    Drawable drawable = new BitmapDrawable(bm);
                    drawable.setColorFilter(new PorterDuffColorFilter(transformationColor, PorterDuff.Mode.MULTIPLY));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tabImg.setBackground(drawable);
                    }else{
                        tabImg.setBackgroundDrawable(drawable);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                actionTV.setTextColor(transformationColor);
                tabLayout.setBackgroundColor(mainActivity.colors.getColor(mainActivity.colors.getTabs_background_color()));

            }

            tabsContainer.addView(tabLayout, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            LinearLayout v = new LinearLayout(getActivity());
            tabsContainer.addView(v, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            tabsContainer.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

            scrollView.setFocusable(false);
            tabsContainer.setFocusable(false);
            tabLayout.setClickable(true);
            tabLayout.setFocusable(true);
            final int j = i;
            tabLayout.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View v) {
                    realm.beginTransaction();
                    seletedTab = tabs.get(j);
                    indexSeletedTab = j;
                    //Collections.sort(tabs, new TabComparator());

                    tabs.get(0).setIsHomeGrid(true);
                    mCallBack.onTabClick(tab, indexSeletedTab);
                    for (int j = 0; j < tabsContainer.getChildCount(); j++) {
                        tabsContainer.getChildAt(j).setSelected(false);
                    }
                    v.setSelected(true);
                    redrawSmartIcons(inflater, j);
                    realm.commitTransaction();
                }
            });
        }
    }



    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void notify(String s) {
        if (totalPriceTV != null) {
            totalPriceTV.setText(s);
            //			totalPriceTV.setTextColor(Color.parseColor("#"+mainActivity.colors.getSide_tabs_foreground_color()));
        }

    }


    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y,
                                int oldx, int oldy) {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == (view.getBottom() - scrollView.getHeight())) {
            tabUpArrow.setVisibility(View.GONE);
        }else if(diff ==  0){
            tabDownArrow.setVisibility(View.GONE);
        }else{
            tabUpArrow.setVisibility(View.VISIBLE);
            tabDownArrow.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onScrollChanged(HorizontalScrollViewExt scrollView, int x,
                                int y, int oldx, int oldy) {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getRight() - (scrollView.getWidth() + scrollView.getScrollX()));

        // if diff is zero, then the right has been reached
        if (diff == (view.getRight() - scrollView.getWidth())) {
            tabUpArrow.setVisibility(View.GONE);
        }else if(diff ==  0){
            tabDownArrow.setVisibility(View.GONE);
        }else{
            tabUpArrow.setVisibility(View.VISIBLE);
            tabDownArrow.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void notifyNumber(int nmbr) {
        if (nmbrProducts != null) {
            String str = getResources().getString(R.string.cart_nmbr, nmbr);
            nmbrProducts.setText(str);
        }

    }



}
