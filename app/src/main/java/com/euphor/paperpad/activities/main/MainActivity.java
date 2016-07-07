/**
 *
 */
package com.euphor.paperpad.activities.main;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.euphor.paperpad.Beans.MyBox;
import com.euphor.paperpad.Beans.Parameters_;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.AgendaFragment;
import com.euphor.paperpad.activities.fragments.AgendaSplitFragment;
import com.euphor.paperpad.activities.fragments.AlbumsGridFragment;
import com.euphor.paperpad.activities.fragments.CalendarHebdoPagerFragment;
import com.euphor.paperpad.activities.fragments.CategorieGridFragment;
import com.euphor.paperpad.activities.fragments.CategoryFragment;
import com.euphor.paperpad.activities.fragments.CategoryFragment_;
import com.euphor.paperpad.activities.fragments.CategoryIndexFragment;
import com.euphor.paperpad.activities.fragments.CollectionGridFragment;
import com.euphor.paperpad.activities.fragments.CollumnScrollFragment;
import com.euphor.paperpad.activities.fragments.ColonnePageFragment;
import com.euphor.paperpad.activities.fragments.ContactsFragment;
import com.euphor.paperpad.activities.fragments.DashboardFragment;
import com.euphor.paperpad.activities.fragments.DashboardFragmentPhone;
import com.euphor.paperpad.activities.fragments.DesignCategoryFlowFragment;
import com.euphor.paperpad.activities.fragments.DirectoryFragment;
import com.euphor.paperpad.activities.fragments.EventListFragment;
import com.euphor.paperpad.activities.fragments.EventPageFragment;
import com.euphor.paperpad.activities.fragments.FilteredCategoriesFragment;
import com.euphor.paperpad.activities.fragments.FormCartFragment;
import com.euphor.paperpad.activities.fragments.FormContactFragment;
import com.euphor.paperpad.activities.fragments.FreeFormulaFragment;
import com.euphor.paperpad.activities.fragments.FullscreenCategoryFragment;
import com.euphor.paperpad.activities.fragments.GaleryFragment;
import com.euphor.paperpad.activities.fragments.GalerySliderFragment;
import com.euphor.paperpad.activities.fragments.HomeGridFragment;
import com.euphor.paperpad.activities.fragments.InteractiveMapFragment;
import com.euphor.paperpad.activities.fragments.ListOfPageFragment;
import com.euphor.paperpad.activities.fragments.MapV2Fragment;
import com.euphor.paperpad.activities.fragments.MultiSelectPagesFragment;
import com.euphor.paperpad.activities.fragments.MyBoxGridFragment;
import com.euphor.paperpad.activities.fragments.MyBoxPageFragment;
import com.euphor.paperpad.activities.fragments.PagesFragment;
import com.euphor.paperpad.activities.fragments.PanoramaFragment;
import com.euphor.paperpad.activities.fragments.PanoramaFragment.Refresher;
import com.euphor.paperpad.activities.fragments.PlayerYouTubeFrag;
import com.euphor.paperpad.activities.fragments.RSSFragment;
import com.euphor.paperpad.activities.fragments.RadioPlayerFragment;
import com.euphor.paperpad.activities.fragments.RadiosSectionFragment;
import com.euphor.paperpad.activities.fragments.SliderCategoryFragment_;
import com.euphor.paperpad.activities.fragments.SplitFilteredCategoriesFragment;
import com.euphor.paperpad.activities.fragments.SplitListCategoryFragment;
import com.euphor.paperpad.activities.fragments.SplitListCategoryFragment_;
import com.euphor.paperpad.activities.fragments.SplitListFragment;
import com.euphor.paperpad.activities.fragments.SurveyFragment;
import com.euphor.paperpad.activities.fragments.SwipperFragment;
import com.euphor.paperpad.activities.fragments.TabsSupportFragment;
import com.euphor.paperpad.activities.fragments.TabsSupportFragment.ActionCallBack;
import com.euphor.paperpad.activities.fragments.WebHomeFragment;
import com.euphor.paperpad.activities.fragments.WebViewFragment;
import com.euphor.paperpad.adapters.RadioCoverFlowAdapter;
import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.Cart;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Disable_period;
import com.euphor.paperpad.Beans.Field;
import com.euphor.paperpad.Beans.Formule;
import com.euphor.paperpad.Beans.FormuleElement;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.PeriodString;
import com.euphor.paperpad.Beans.Radio;
import com.euphor.paperpad.Beans.Related;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.Beans.Tab;

import com.euphor.paperpad.utils.AlertDialogManager;
import com.euphor.paperpad.utils.CategoryTo;
import com.euphor.paperpad.utils.Colors;

import com.euphor.paperpad.utils.ConnectionDetector;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.InfoActivity;
import com.euphor.paperpad.utils.Installation;
import com.euphor.paperpad.utils.RandomString;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.dashboardParsing.Dashboard;
import com.euphor.paperpad.utils.dashboardParsing.DashboardMain;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.utils.jsonUtils.AppJsonWriter;
import com.euphor.paperpad.utils.jsonUtils.AppSession;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.panoramagl.PLView;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.euphor.paperpad.utils.CommonUtilities.SERVER_URL;


/**
 * @author euphordev02
 *
 */
public class MainActivity extends PLView implements ActionCallBack, Refresher, SlidingMenu.OnOpenListener, SlidingMenu.OnCloseListener/*, OnStreetViewPanoramaReadyCallback*/ {

    //	public DisplayImageOptions options;

    private TabsSupportFragment tabsFragment;
    public String bodyFragment;
    public Bundle extras;

    public Colors colors;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    //	public ImageLoader imageLoader;
    SharedPreferences wmbPreference;
    private OnSharedPreferenceChangeListener listner;
    public static InfoActivity infoActivity;
    private boolean passedOncreate = false;
    public SlidingMenu menu;
    public LinearLayout cartTagContainer;
    public TextView totalSum;
    public Double total = 0.0;
    protected PopupWindow pw;
    //	private RelativeLayout RLMainActivity;
    //	private OnClickListener listenerForm;
    protected Dialog dialog;
    private LayoutInflater inflater;
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    GoogleMap googleMap;
    //	public ImageLoaderConfiguration configuration;
    private int mStackLevel = 0;
    private boolean decorated;
    protected boolean done;
    //public static  boolean bottomNav = false;
    public static short positionNav = -1;
    protected ViewPager mViewPager;
    private boolean ifSwipe;
    public static Parameters params;
    public static boolean isTablet;
    private String session_id;
    protected int LANGUAGE_ID;
    private PriceCallBack priceCallBack;
    private ConnectionDetector cd;
    protected AlertDialogManager alert = new AlertDialogManager();
    public Timer timer;

    /**
     * cart view
     */
    private View menuView;
    private DownloadParseAsyncTask asyncTask;
    public Realm realm;
    public static int id_Cart = 0;
    /**
     * is payment with stripe or normal payment ? <br>
     * 1 for stripe <br>
     * 0 for normal cart <br>
     * -1 for no choices yet
     */
    public static int stripe_or_not = -1;
    int permissionCheck = 1 ;


    public void onCheckedChanged() {

        // When a FAB is toggled, log the action.
        Toast.makeText(getActivity(), "Instant Run rocks!",Toast.LENGTH_SHORT).show();

        Log.d(getPackageName(), String.format("FAB 1 was %s.", "checked" ));


    }



    /*
      * (non-Javadoc)
      *
      * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
      */
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
       /* Fabric.with(getApplicationContext(), new Crashlytics());*/
        prod_or_sand = getString(R.string.prod_or_sand);
        if (realm != null) {
            realm.close();
            Realm.deleteRealmFile(getApplicationContext());
        }
        realm = Realm.getInstance(getApplicationContext());
        /*onCheckedChanged();*/
        //view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


        tsLong = System.currentTimeMillis() / 1000;
        RandomString rd = new RandomString(16);

        session_id = "" + tsLong + rd.nextString();
        //		setContentView(R.layout.frame_container);
        //		Log.i("wher_iam", ""+getResources().getDimension(R.dimen.where_iam));
        context = getApplicationContext();
        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);

            final SharedPreferences prefs = getGCMPreferences(context);
            // checks if the information is not stale
            //		    long expirationTime =
            //		            prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
            //		    return System.currentTimeMillis() > expirationTime;

            if (isRegistrationExpired()) {
                registerInBackground();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, System.currentTimeMillis());
                editor.commit();
            } else {
                regid = getRegistrationId(context);
                //            registerInBackground();
                if (regid.isEmpty()) {
                    registerInBackground();
                }
            }

        } else {
            Log.i(LOG_TAG, "No valid Google Play Services APK found.");
        }


        passedOncreate = true;
        wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(this);

        listner = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(
                    SharedPreferences sharedPreferences, String key) {
                if (key.equals("input_id")) {

                    Intent intent = new Intent(getApplicationContext(),  SplashActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("REQUEST_UPDATE", true);
                    intent.putExtras(b);
                    startActivity(intent);

                }

            }
        };
        wmbPreference.registerOnSharedPreferenceChangeListener(listner);
        String lang = wmbPreference.getString(Utils.LANG, "fr");
        Utils.changeLocale(lang, getApplicationContext());
        //Utils.changeLocale("fr",getBaseContext());


        params = realm.where(Parameters.class).findFirst();
        //appController.getParametersDao().queryForId(1);
        if (params != null)
            initializeFonts(getApplicationContext(), params);
        //		options = ((MyApplication)getApplication()).options;
        //		imageLoader = ((MyApplication)getApplication()).imageLoader;
        Parameters parameters = null;
        parameters = realm.where(Parameters.class).findFirst();
        if (parameters != null) {
             /*colors = new Colors(params);*/
            colors = new Colors(parameters);
            if (parameters.getNavigation_type() != null) {
                if (parameters.getNavigation_type().contains("top_wide")) {
                    positionNav = 0;
                } else if (parameters.getNavigation_type().contains("bottom")) {
                    positionNav = 1;
                    //bottomNav = true;
                } else if (parameters.getNavigation_type().contains("top_narrow")) {
                    positionNav = 2;
                    //bottomNav = true;
                } else {

                    //bottomNav = false;
                    positionNav = 3;
                }
            } else {
                //bottomNav = true;
                positionNav = 1;
            }

        }
        isTablet = Utils.isTablet(getActivity());
        //		ifSwipe = params.
        //		bottomNav = true;
        if (isTablet) {
            if (positionNav == 0 || positionNav == 2) {

                setContentView(R.layout.top_frame_container);
            } else if (positionNav == 1) {
                setContentView(R.layout.bottom_frame_container);
            } else {
                setContentView(R.layout.frame_container);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (positionNav == 0 || positionNav == 2) {
                setContentView(R.layout.top_frame_container);
            } else
                setContentView(R.layout.bottom_frame_container);
        }
        params = realm.where(Parameters.class).findFirst();

       if(params != null && params.getHome_type() != null) {
           if (params.getHome_type().equals("swipe")) {
               ifSwipe = true;
               findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
               findViewById(R.id.swipe_container).setVisibility(View.GONE);
           } else {
               ifSwipe = false;
               findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
               findViewById(R.id.swipe_container).setVisibility(View.GONE);
           }
       }
        ActionBar actionBar = getActionBar();
        actionBar.hide(); // hided actionbar
        infoActivity = new InfoActivity(0, 0, "");
        extras = new Bundle();
        menu = new SlidingMenu(this);
        menu.setOnCloseListener(MainActivity.this);
        menu.setOnOpenListener(MainActivity.this);
        //		menu.setOnOpenedListener(MainActivity.this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //        menu.setShadowWidthRes(R.dimen.shadow_width);
        //        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.setFadeEnabled(true);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        inflater = LayoutInflater.from(getApplicationContext());
        menuView = inflater.inflate(R.layout.cart_layout_new, null, false);
        instantiateCart(menuView);
        cartTagContainer = (LinearLayout) menuView.findViewById(R.id.cartTagContainer);
        menuView.findViewById(R.id.linearLayoutSummery).setBackgroundColor(colors.getColor(colors.getForeground_color(), "BB"));

        /**On click on validate my command  **/
        LinearLayout validateCart = (LinearLayout) menuView.findViewById(R.id.validateCartLL);


        OnClickListener cartClick = new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!verifyNotAllowed()) {
                    showDialog();
                    getMenu().toggle();
                } else {

                }

            }
        };
        validateCart.setOnClickListener(cartClick);


        totalSum = (AutoResizeTextView) menuView.findViewById(R.id.sum);
        ((TextView) menuView.findViewById(R.id.TotalLabel)).setTextColor(colors.getColor(colors.getTabs_foreground_color()));
        menu.setMenu(menuView);
        Resources r = getResources();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r.getDimension(R.dimen.cart_width), r.getDisplayMetrics());
        if (dm.widthPixels > 800) {
            px = (float) ((float) ((float) dm.widthPixels * (float) 40) / 100.0);
        }/*else if (dm.widthPixels >600 && dm.widthPixels < 800) {
             px = (float) ((float)((float)dm.widthPixels*(float)50)/100.0);
         }*/ else {
            px = (float) ((float) ((float) dm.widthPixels * (float) 75) / 100.0);
        }

        menu.setBehindWidth((int) px);
        fillCart();


        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);

        if (timer != null) {
            timer.cancel();
        }
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        findViewById(R.id.swipe_container).setVisibility(View.GONE);

        Map<String, Object> map = (HashMap<String, Object>) getLastCustomNonConfigurationInstance();
        if (map == null) {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                String a = b.getString("extra");
                a = "" + a;
                String page_id = b.getString("page_id");
                //				if (page_id != null && !page_id.isEmpty()) {
                //					List<Child_pages> list_tmp = new ArrayList<Child_pages>();
                //					try {
                //						list_tmp = appController.getChildPageDao().queryForEq("id", page_id);
                //					} catch (SQLException e) {
                //						// TODO Auto-generated catch block
                //						e.printStackTrace();
                //					}
                //					if (list_tmp.size()>0) {
                //						Child_pages page = list_tmp.get(0);
                //						openPage(page );
                //						return;
                //					}
                //
                //				}
            }
        }

        if (frameLayout != null) {

            String type = null;


            // Check if we are restoring the activity from an orientation change
            // for example
            if (map == null || map.isEmpty()) {

                Bundle bundle = getIntent().getExtras();
                tabsFragment = new TabsSupportFragment();
                priceCallBack = (PriceCallBack) tabsFragment;
                if (bundle != null) {
                    type = bundle.getString("extra");
                    if (bundle.getParcelable("InfoActivity") != null) {
                        infoActivity = bundle.getParcelable("InfoActivity");
                        Bundle extrasTabsFrag = new Bundle();
                        extrasTabsFrag.putInt("highlighted_tab", infoActivity.getIdCurrentTab());
                        extrasTabsFrag.putBoolean("orientation", true);
                        tabsFragment.setArguments(extrasTabsFrag);
                    }
                }
                MainActivity.this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tabsFragment, tabsFragment).commit();

                if (bundle != null) {
                    String page_id = bundle.getString("page_id");
                    if (page_id != null && !page_id.isEmpty()) {

                        RealmResults<Child_pages> list_tmp;
                        list_tmp = realm.where(Child_pages.class).equalTo("id", page_id).findAll();//appController.getChildPageDao().queryForEq("id", page_id);
                        if (list_tmp.size() > 0) {
                            Child_pages page = list_tmp.get(0);
                            if (timer != null) {
                                timer.cancel();
                            }
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.swipe_container).setVisibility(View.GONE);
                            findViewById(R.id.videoView).setVisibility(View.GONE);
                            openPage(page);
                            return;
                        }
                    }
                }

                if (type != null) {
                    extras = getIntent().getExtras();
                    // filter based on the type of the section

                    if (type.equals("contents") || type.isEmpty()) { // type
                        // Contents
                        // or
                        // empty
                        // sent!
                        // However, if we're being restored from a previous
                        // state,
                        // then we don't need to do anything and should return
                        // or else
                        // we could end up with overlapping fragments.

                        if (arg0 != null) {
                            return;
                        }
                        boolean isHomeGrid = bundle.getBoolean("isHomeGrid");
                        if (isHomeGrid) {
                            //bodyFragment = "HomeGridFragment";
                            if (params.getHome_type().equals("web")) {
                                WebHomeFragment webHomeFragment = new WebHomeFragment();
                                bodyFragment = "WebHomeFragment";
                                Bundle extra = new Bundle();
                                extra.putString("link", params.getWeb_home_test_url());
                                webHomeFragment.setArguments(extra);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container,
                                                webHomeFragment).commit();
                            } else if (ifSwipe) {
                                //								buildSwipe();
                                //								bodyFragment = "HomeGridFragment";

                                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                                findViewById(R.id.swipe_container).setVisibility(View.GONE);

                                Log.e(" swipperFragment :", " Yes it is " + " ");
                                SwipperFragment swipperFragment = new SwipperFragment();
                                ((MainActivity) getActivity()).bodyFragment = "SwipperFragment";
                                swipperFragment.setArguments(((MainActivity) getActivity()).extras);

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container,
                                                swipperFragment)
                                        .commit();


                            } else {
                                HomeGridFragment homeGridFragment = new HomeGridFragment();
                                MainActivity.this.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, homeGridFragment)
                                        .addToBackStack("HomeGridFragment").commit();
                            }

                        }
                        //						else if(false){
                        //							 x
                        //						}
                        else {

                            // Create an instance of ExampleFragment
                            CategoryFragment categoryFragment = new CategoryFragment();
                            // In case this activity was started with special
                            // instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            categoryFragment.setArguments(extras);
                            bodyFragment = "CategoryFragment";
                            // Add the fragment to the 'fragment_container'
                            // FrameLayout
                            MainActivity.this
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,
                                            categoryFragment).addToBackStack("CategoryFragment").commit();
                        }


                    } else if (type.equals("CollumnScrollFragment")) {
                        CollumnScrollFragment collumnScrollFragment = new CollumnScrollFragment();
                        bodyFragment = "CollumnScrollFragment";

                        extras = (Bundle) map.get("EXTRAS");
                        if (extras != null) {
                            collumnScrollFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, collumnScrollFragment)
                                    .addToBackStack(bodyFragment).commit();
                        }
                    }
                     /*else if(type.compareToIgnoreCase("DirectoryFragment") == 0) {

                         DirectoryFragment directoryFragment = new DirectoryFragment();
                         extras = (Bundle) map.get("EXTRAS");
                         directoryFragment.setArguments(extras);
                         bodyFragment = "DirectoryFragment";

                         MainActivity.this
                         .getSupportFragmentManager()
                         .beginTransaction()
                         .replace(R.id.fragment_container,
                                 directoryFragment).addToBackStack("DirectoryFragment").commit();

                     }*/
                    else if (type.equals("DesignCategoryFlowFragment")) {

                        extras = (Bundle) map.get("EXTRAS");
                        Category category = null;
                        category = realm.where(Category.class).equalTo("id", extras.getInt("category_id")).findFirst();
                        DesignCategoryFlowFragment designCategoryFlowFragment = DesignCategoryFlowFragment.create(category);
                        bodyFragment = "DesignCategoryFlowFragment";

                        designCategoryFlowFragment
                                .setArguments(extras);
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, designCategoryFlowFragment)
                                .addToBackStack(bodyFragment).commit();
                    } else if (type.equals("InteractiveMapFragment")) {
                        extras = (Bundle) map.get("EXTRAS");
                        Section section = null;
                        section = realm.where(Section.class).equalTo("id", extras.getInt("section_id")).findFirst(); //appController.getSectionsDao().queryForId(extras.getInt("section_id"));
                        if (section.getMaps() != null) {
                            List<com.euphor.paperpad.Beans.interactiveMap.map> maps = new ArrayList<com.euphor.paperpad.Beans.interactiveMap.map>();
                            InteractiveMapFragment interactiveMapFragment = InteractiveMapFragment.create(maps);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, interactiveMapFragment).addToBackStack("InteractiveMapFragment").commit();
                            if (timer != null) {
                                timer.cancel();
                            }
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.swipe_container).setVisibility(View.GONE);
                        }

                    } else if (type.equals("web")) {

                        WebViewFragment webViewFragment = new WebViewFragment();
                        bodyFragment = "WebViewFragment";
                        extras = (Bundle) map.get("EXTRAS");
                        webViewFragment.setArguments(extras);
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, webViewFragment).addToBackStack("WebViewFragment").commit();

                    } else if (type.equals("contact")) { // Type Contacts

                        if (arg0 != null) {
                            return;
                        }
                        ContactsFragment contactsFragment = new ContactsFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        contactsFragment.setArguments(extras);
                        bodyFragment = "ContactsFragment";
                        // Add the fragment to the 'fragment_container'
                        // FrameLayout
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        contactsFragment)
                                .setTransition(
                                        FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack("ContactsFragment").commit();
                    } else if (type.equals("gallery")) { // Type Gallery

                        RealmResults<Section> sections;
                        sections = realm.where(Section.class).equalTo("type", "gallery").findAll();//appController.getSectionsDao().queryForEq("type", "gallery");

                        if (sections.size() > 0) {
                            Section section = sections.get(0);
                            extras.putInt("Section_id", section.getId_s());

                            if (section.getGallery_design() != null && section.getGallery_design().equals("slider")) {
                                bodyFragment = "GalerySliderFragment";
                                GalerySliderFragment galerySliderFragment = new GalerySliderFragment();
                                galerySliderFragment.setArguments(extras);
                                MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, galerySliderFragment).addToBackStack("GalerySliderFragment").commit();

                            } else {
                                bodyFragment = "AlbumsGridFragment";
                                AlbumsGridFragment albumsGridFragment = new AlbumsGridFragment();
                                albumsGridFragment.setArguments(extras);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumsGridFragment).addToBackStack("AlbumsGridFragment").commit();
                            }

                        }

                         /*bodyFragment = "GaleryFragment";
                         GaleryFragment galeryFragment = new GaleryFragment();
                         galeryFragment.setArguments(extras);
                         MainActivity.this.getSupportFragmentManager().beginTransaction()
                         .replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();*/
                    }/*else if(type.equals("FreeFormulaFragment")){
                         extras = (Bundle) map.get("EXTRAS");
                         bodyFragment = "FreeFormulaFragment";
                         FreeFormulaFragment formulaFragment = new FreeFormulaFragment();
                         formulaFragment.setArguments(extras);
                         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, formulaFragment).addToBackStack(null).commit();
                     }*/ else if (type.equals("GaleryFragment")) {
                        bodyFragment = "GaleryFragment";
                        extras = (Bundle) map.get("EXTRAS");
                        GaleryFragment galeryFragment = new GaleryFragment();
                        galeryFragment.setArguments(extras);
                        MainActivity.this.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();
                    }
                     /*else if(type.compareTo("ListOfPageFragment") == 0) {
                         extras = (Bundle) map.get("EXTRAS");
                         Category cat = appController.getCategoryById(extras.getInt("Category_id", 0));
                         ListOfPageFragment listOfPageFragment = ListOfPageFragment.newInstance(cat, colors);
                         bodyFragment = "ListOfPageFragment";

                         //						listOfPageFragment
                         //						.setArguments(extras);
                         // Add the fragment to the 'fragment_container' FrameLayout
                         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,listOfPageFragment)
                         .addToBackStack("ListOfPageFragment").commit();
                     }*/
                    else if (type.equals("map")) {

                        Fragment mMapFragment = new MapV2Fragment();
                        bodyFragment = "MapV2Fragment";
                        FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
                        fragmentTransaction.addToBackStack("MapV2Fragment").commit();

                    } else if (type.equals("rss")) {
                        List<Section> sections = realm.where(Section.class).equalTo("type", "rss").findAll();
                        if(sections.size() >0) {
                        Fragment rssFragment = new RSSFragment();
                        bodyFragment = "RSSFragment";
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container,
                                rssFragment);
                        fragmentTransaction.addToBackStack("RSSFragment").commit();
                    }
                    } else if (type.equals("video")) {
                        bodyFragment = "PlayerYouTubeFrag";
                        extras = (Bundle) map.get("EXTRAS");
                        PlayerYouTubeFrag myFragment = PlayerYouTubeFrag.newInstance(extras.getInt("section_id"));
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment, bodyFragment).addToBackStack(bodyFragment).commit();


                         /*Intent intent = new Intent(getApplicationContext(), YoutubePlayerActivity.class);
                         intent.putExtra("InfoActivity", infoActivity);
                         startActivity(intent);*/

                    } else if (type.equals("agenda")) {

                        AgendaFragment agendaFrag = new AgendaFragment();
                        extras = new Bundle();
                        //						extras.putString("link", section.getRoot_url());
                        //						agendaFrag.setArguments(extras);
                        bodyFragment = "AgendaFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, agendaFrag).commit();

                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);
                    } else if (type.compareTo("CalendarPagerFragment") == 0) {

                        //if(isTablet) {
                        CalendarHebdoPagerFragment calendarPagerFragment = new CalendarHebdoPagerFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        calendarPagerFragment.setArguments(extras);
                        bodyFragment = "CalendarPagerFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, calendarPagerFragment).addToBackStack("CalendarPagerFragment").commit();
                        //						}
                        //						else {
                        //							CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
                        //							extras = (Bundle) map.get("EXTRAS");
                        //							calendarPagerFragment.setArguments(extras);
                        //							bodyFragment = "CalendarPagerFragment";
                        //							MainActivity.this
                        //							.getSupportFragmentManager()
                        //							.beginTransaction()
                        //							.replace(R.id.fragment_container, calendarPagerFragment).addToBackStack("CalendarPagerFragment").commit();
                        //
                        ////							EventListFragment eventListFragment = new EventListFragment();
                        ////							extras = (Bundle) map.get("EXTRAS");
                        ////							eventListFragment.setArguments(extras);
                        ////							bodyFragment = "EventListFragment";
                        ////							MainActivity.this
                        ////							.getSupportFragmentManager()
                        ////							.beginTransaction()
                        ////							.replace(R.id.fragment_container, eventListFragment).addToBackStack("EventListFragment").commit();
                        //						}
                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);


                    } else if (type.compareTo("AgendaSplitFragment") == 0) {
                        AgendaSplitFragment agendaSplitFragment = new AgendaSplitFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        agendaSplitFragment.setArguments(extras);
                        bodyFragment = "AgendaSplitFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, agendaSplitFragment).addToBackStack(null).commit();

                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                    }
                } else {
                    bodyFragment = "HomeGridFragment";
                    if (params.getHome_type().equals("web")) {
                        WebHomeFragment webHomeFragment = new WebHomeFragment();
                        bodyFragment = "WebHomeFragment";
                        Bundle extra = new Bundle();
                        extra.putString("link", params.getWeb_home_test_url());
                        webHomeFragment.setArguments(extra);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        webHomeFragment).commit();
                    } else if (ifSwipe) {
                        //						buildSwipe();
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                        Log.e(" swipperFragment :", " Yes it is " + " ");
                        SwipperFragment swipperFragment = new SwipperFragment();
                        ((MainActivity) getActivity()).bodyFragment = "SwipperFragment";
                        swipperFragment.setArguments(((MainActivity) getActivity()).extras);

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        swipperFragment)
                                .commit();

                    } else {
                        HomeGridFragment homeGridFragment = new HomeGridFragment();
                        MainActivity.this.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, homeGridFragment)
                                .addToBackStack("HomeGridFragment").commit();

                    }


                }

            } else {

                tabsFragment = new TabsSupportFragment();
                priceCallBack = (PriceCallBack) tabsFragment;
                fillCart();
                //				while (!tabsFragment.isAdded()) { // while loop here
                //					MainActivity.this.getSupportFragmentManager().executePendingTransactions();
                //				}
                Bundle extrasTabsFrag = new Bundle();
                if (map.get("SelectedTab") != null) {
                    int index = (Integer) map.get("SelectedTab");
                    extrasTabsFrag.putInt("highlighted_tab", index);
                    extrasTabsFrag.putBoolean("orientation", true);
                    if (infoActivity.getIdCurrentTab() != Constants.DEFAULT_TAB_VALUE) {
                        infoActivity.switchCurrentPrev();
                        infoActivity.setIdCurrentTab(index);
                    } else {
                        infoActivity.setIdCurrentTab(index);
                    }

                } else {
                    extrasTabsFrag.putInt("highlighted_tab", Constants.DEFAULT_TAB_VALUE);
                    extrasTabsFrag.putBoolean("orientation", false);
                    infoActivity.setIdCurrentTab(Constants.DEFAULT_TAB_VALUE);
                }
                tabsFragment.setArguments(extrasTabsFrag);
                MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.tabsFragment, tabsFragment).commit();
                type = ((String) map.get("body"));

                if (type != null) {
                    if (type.equals("HomeGridFragment")) {
                        bodyFragment = "HomeGridFragment";
                        if (params.getHome_type().equals("web")) {
                            WebHomeFragment webHomeFragment = new WebHomeFragment();
                            bodyFragment = "WebHomeFragment";
                            Bundle extra = new Bundle();
                            extra.putString("link", params.getWeb_home_test_url());
                            webHomeFragment.setArguments(extra);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,
                                            webHomeFragment).commit();
                        } else if (ifSwipe) {
                            //							buildSwipe();
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.swipe_container).setVisibility(View.GONE);

                            Log.e(" swipperFragment :", " Yes it is " + " ");
                            SwipperFragment swipperFragment = new SwipperFragment();
                            ((MainActivity) getActivity()).bodyFragment = "SwipperFragment";
                            swipperFragment.setArguments(((MainActivity) getActivity()).extras);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,
                                            swipperFragment)
                                    .commit();

                        } else {
                            HomeGridFragment homeGridFragment = new HomeGridFragment();
                            MainActivity.this
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,
                                            homeGridFragment)
                                    .addToBackStack(null).commit();
                        }

                    } else if (type.equals("CollumnScrollFragment")) {
                        CollumnScrollFragment collumnScrollFragment = new CollumnScrollFragment();
                        bodyFragment = "CollumnScrollFragment";

                        extras = (Bundle) map.get("EXTRAS");
                        if (extras != null) {
                            collumnScrollFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, collumnScrollFragment)
                                    .addToBackStack(bodyFragment).commit();
                        }
                    } else if (type.compareToIgnoreCase("DirectoryFragment") == 0) {

                        DirectoryFragment directoryFragment = new DirectoryFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        directoryFragment.setArguments(extras);
                        bodyFragment = "DirectoryFragment";

                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        directoryFragment).addToBackStack("DirectoryFragment").commit();

                    } else if (type.compareTo("ListOfPageFragment") == 0) {
                        extras = (Bundle) map.get("EXTRAS");
                        Category cat = realm.where(Category.class).equalTo("id", extras.getInt("Category_id", 0)).findFirst();//appController.getCategoryById(extras.getInt("Category_id", 0));
                        ListOfPageFragment listOfPageFragment = ListOfPageFragment.newInstance(cat, colors);
                        bodyFragment = "ListOfPageFragment";
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listOfPageFragment)
                                .addToBackStack("ListOfPageFragment").commit();
                    } else if (type.equals("CategorieGridFragment")) {
                        CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
                        bodyFragment = "CategorieGridFragment";
                        extras = (Bundle) map.get("EXTRAS");
                        coategorieGridFragment.setArguments(extras);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();
                    } else if (type.equals("FilteredCategoriesFragment")) {
                        FilteredCategoriesFragment groupedCategoriesFragment = new FilteredCategoriesFragment();
                        bodyFragment = "FilteredCategoriesFragment";
                        extras = (Bundle) map.get("EXTRAS");
                        groupedCategoriesFragment.setArguments(((MainActivity) getActivity()).extras);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, groupedCategoriesFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                    } else if (type.equals("ColonnePageFragment")) {
                        extras = (Bundle) map.get("EXTRAS");
                        bodyFragment = "ColonnePageFragment";
                        ColonnePageFragment colonnePageFragment = new ColonnePageFragment();
                        colonnePageFragment.setArguments(extras);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, colonnePageFragment).addToBackStack(null).commit();
                    } else if (type.equals("ContactsFragment")) {
                        ContactsFragment contactsFragment = new ContactsFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        contactsFragment.setArguments(extras);
                        bodyFragment = "ContactsFragment";
                        // Add the fragment to the 'fragment_container'
                        // FrameLayout
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        contactsFragment).addToBackStack(null)
                                .commit();
                    } else if (type.equals("CategoryFragment")) {
                        CategoryFragment categoryFragment = new CategoryFragment();
                        // In case this activity was started with special
                        // instructions from an Intent,
                        // pass the Intent's extras to the fragment as arguments
                        extras = (Bundle) map.get("EXTRAS");
                        categoryFragment.setArguments(extras);
                        bodyFragment = "CategoryFragment";
                        // Add the fragment to the 'fragment_container'
                        // FrameLayout
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        categoryFragment).addToBackStack(null)
                                .commit();
                    } else if (type.equals("InteractiveMapFragment")) {
                        extras = (Bundle) map.get("EXTRAS");
                        Section section = null;
                        section = realm.where(Section.class).equalTo("id", extras.getInt("section_id")).findFirst();//appController.getSectionsDao().queryForEq("id", ""+extras.getInt("section_id")).get(0);
                        if (section.getMaps() != null) {
                            List<com.euphor.paperpad.Beans.interactiveMap.map> maps = new ArrayList<com.euphor.paperpad.Beans.interactiveMap.map>(section.getMaps());
                            InteractiveMapFragment interactiveMapFragment = InteractiveMapFragment.create(maps);
                            bodyFragment = "InteractiveMapFragment";
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, interactiveMapFragment).addToBackStack("InteractiveMapFragment").commit();
                            if (timer != null) {
                                timer.cancel();
                            }
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.swipe_container).setVisibility(View.GONE);
                        }

                    } else if (type.equals("WebViewFragment")) {
                        WebViewFragment webViewFragment = new WebViewFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        webViewFragment.setArguments(extras);
                        bodyFragment = "WebViewFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        webViewFragment).commit();
                    } else if (type.equals("DesignCategoryFlowFragment")) {

                        extras = (Bundle) map.get("EXTRAS");
                        Category category = null;
                        category = realm.where(Category.class).equalTo("id",  extras.getInt("category_id")).findFirst();
                        DesignCategoryFlowFragment designCategoryFlowFragment = DesignCategoryFlowFragment.create(category);
                        bodyFragment = "DesignCategoryFlowFragment";

                        designCategoryFlowFragment
                                .setArguments(extras);
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, designCategoryFlowFragment)
                                .addToBackStack(bodyFragment).commit();
                    } else if (type.equals("MultiSelectPagesFragment")) {
                        MultiSelectPagesFragment multiSelectPagesFragment = new MultiSelectPagesFragment();
                        // In case this activity was started with special
                        // instructions from an Intent,
                        // pass the Intent's extras to the fragment as arguments
                        extras = (Bundle) map.get("EXTRAS");
                        multiSelectPagesFragment.setArguments(extras);
                        bodyFragment = "MultiSelectPagesFragment";
                        // Add the fragment to the 'fragment_container'
                        // FrameLayout
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        multiSelectPagesFragment)
                                .setTransition(
                                        FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                    } else if (type.equals("PagesFragment")) {
                        extras = (Bundle) map.get("EXTRAS");
                        PagesFragment pagesFragment = new PagesFragment();
                        bodyFragment = "PagesFragment";
                        pagesFragment.setArguments(extras);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, pagesFragment)
                                .addToBackStack(null).commit();

                    } else if (type.equals("PanoramaFragment")) {
                        extras = (Bundle) map.get("EXTRAS");
                        bodyFragment = "PanoramaFragment";
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        Fragment prev = getSupportFragmentManager()
                                .findFragmentByTag("panorama");
                        if (prev != null) {
                            fragmentTransaction.remove(prev);
                        }
                        Fragment panoFragment = new PanoramaFragment();
                        panoFragment.setArguments(extras);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.fragment_container,
                                panoFragment, "panorama");
                        fragmentTransaction.commit();

                    } else if (type.equals("MapV2Fragment")) {
                        extras = (Bundle) map.get("EXTRAS");
                        Fragment mMapFragment = new MapV2Fragment();
                        bodyFragment = "MapV2Fragment";
                        mMapFragment.setArguments(extras);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container,
                                mMapFragment);
                        fragmentTransaction.addToBackStack(null).commit();

                    } else if (type.equals("RSSFragment")) {
                        List<Section> sections = realm.where(Section.class).equalTo("type", "rss").findAll();
                        if(sections.size() >0) {
                            Fragment rssFragment = new RSSFragment();
                            extras = (Bundle) map.get("EXTRAS");
                           // Fragment rssFragment = new RSSFragment();
                            bodyFragment = "RSSFragment";
                            rssFragment.setArguments(extras);
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                    .beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container,
                                    rssFragment);
                            fragmentTransaction.addToBackStack(null).commit();
                        }

                    } else if (type.equals("YouTubePlayerSupportFragment")) {

                        extras = (Bundle) map.get("EXTRAS");
                        YouTubePlayerSupportFragment fragment = new YouTubePlayerSupportFragment();
                        OnInitializedListener listnerOnVid = new OnInitializedListener() {

                            @Override
                            public void onInitializationSuccess(Provider arg0,
                                                                YouTubePlayer player, boolean wasRestored) {
                                if (!wasRestored) {
                                    player.cueVideo("wKJ9KzGQq0w");
                                } else {
                                    player.play();
                                }

                            }

                            @Override
                            public void onInitializationFailure(Provider arg0,
                                                                YouTubeInitializationResult arg1) {
                                // TODO Auto-generated method stub

                            }
                        };
                        fragment.initialize(Constants.DEVELOPER_KEY,
                                listnerOnVid);
                        fragment.setRetainInstance(true);
                        bodyFragment = "YouTubePlayerSupportFragment";
                        fragment.setArguments(extras);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container,
                                fragment);
                        fragmentTransaction.addToBackStack(null).commit();

                    } else if (type.equals("FormDialogFragment")) {

                        extras = (Bundle) map.get("EXTRAS");
                        bodyFragment = "FormDialogFragment";

                        mStackLevel++;

                        // DialogFragment.show() will take care of adding the fragment
                        // in a transaction.  We also want to remove any currently showing
                        // dialog, so make our own transaction and take care of that here.
                        FragmentTransaction ft = getSupportFragmentManager()
                                .beginTransaction();
                        Fragment prev = getSupportFragmentManager()
                                .findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        // Create and show the dialog.
                        FormCartFragment newFragment = FormCartFragment
                                .newInstance(mStackLevel);
                        newFragment.setArguments(extras);
                        newFragment.setCancelable(false);
                        newFragment.show(ft, "dialog");

                    } else if (type.equals("FormFragment")) {

                        extras = (Bundle) map.get("EXTRAS");
                        bodyFragment = "FormFragment";

                        // DialogFragment.show() will take care of adding the fragment
                        // in a transaction.  We also want to remove any currently showing
                        // dialog, so make our own transaction and take care of that here.
                        FragmentTransaction ft = getSupportFragmentManager()
                                .beginTransaction();

                        ft.addToBackStack(null);

                        // Create and show the dialog.
                        FormContactFragment newFragment = FormContactFragment
                                .newInstance();
                        newFragment.setArguments(extras);
                        ft.replace(R.id.fragment_container,
                                newFragment).commit();

                    } else if (type.equals("MyBoxGridFragment")) {

                        extras = (Bundle) map.get("EXTRAS");
                        bodyFragment = "MyBoxGridFragment";
                        MyBoxGridFragment myBoxGridFragment = new MyBoxGridFragment();
                        //					extras = new Bundle();
                        //					extras.putString("link", section.getRoot_url());
                        myBoxGridFragment.setArguments(extras);
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        myBoxGridFragment).commit();

                    } else if (type.equals("MyBoxPageFragment")) {

                        MyBoxPageFragment myBoxPageFragment = new MyBoxPageFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        //				extras.putString("link", section.getRoot_url());
                        myBoxPageFragment.setArguments(extras);
                        bodyFragment = "MyBoxPageFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, myBoxPageFragment).commit();

                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                    } else if (type.equals("AgendaFragment")) {

                        AgendaFragment agendaFrag = new AgendaFragment();
                        extras = new Bundle();
                        //						extras.putString("link", section.getRoot_url());
                        agendaFrag.setArguments(extras);
                        bodyFragment = "AgendaFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, agendaFrag).commit();

                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);
                    } else if (type.compareTo("CalendarPagerFragment") == 0) {
                        //if(isTablet) {
                        CalendarHebdoPagerFragment calendarPagerFragment = new CalendarHebdoPagerFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        calendarPagerFragment.setArguments(extras);
                        bodyFragment = "CalendarPagerFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();
                        //						}
                        //						else {
                        //							CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
                        //							extras = (Bundle) map.get("EXTRAS");
                        //							calendarPagerFragment.setArguments(extras);
                        //							bodyFragment = "CalendarPagerFragment";
                        //							MainActivity.this
                        //							.getSupportFragmentManager()
                        //							.beginTransaction()
                        //							.replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();
                        //
                        ////							EventListFragment eventListFragment = new EventListFragment();
                        ////							extras = (Bundle) map.get("EXTRAS");
                        ////							eventListFragment.setArguments(extras);
                        ////							bodyFragment = "EventListFragment";
                        ////							MainActivity.this
                        ////							.getSupportFragmentManager()
                        ////							.beginTransaction()
                        ////							.replace(R.id.fragment_container, eventListFragment).addToBackStack(null).commit();
                        //						}
                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);


                    } else if (type.compareTo("EventListFragment") == 0) {
                        EventListFragment eventListFragment = new EventListFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        eventListFragment.setArguments(extras);
                        bodyFragment = "EventListFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, eventListFragment).addToBackStack(null).commit();
                    } else if (type.compareTo("AgendaSplitFragment") == 0) {
                        AgendaSplitFragment agendaSplitFragment = new AgendaSplitFragment();
                        extras = (Bundle) map.get("EXTRAS");
                        agendaSplitFragment.setArguments(extras);
                        bodyFragment = "AgendaSplitFragment";
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, agendaSplitFragment).addToBackStack(null).commit();

                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                    } else if (type.equals("SliderCategoryFragment")) {

                        extras = (Bundle) map.get("EXTRAS");
                        bodyFragment = "SliderCategoryFragment";
                        SliderCategoryFragment_ fragment = new SliderCategoryFragment_();
                        //					extras = new Bundle();
                        //					extras.putString("link", section.getRoot_url());
                        fragment.setArguments(extras);
                        MainActivity.this
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        fragment).commit();

                    } else if (type.equals("FullscreenCategoryFragment")) {
                        extras = (Bundle) map.get("EXTRAS");

                        int category_id = extras.getInt("Category_id");
                        Category category = realm.where(Category.class).equalTo("id", category_id).findFirst();//appController.getCategoryById(category_id);

                        if (category.getChildren_categories().size() == 0) {
                            RealmList<Child_pages> pages1 = category.getChildren_pages();
                            if (!pages1.isEmpty()) {
                                Child_pages page = pages1.iterator().next();
                                extras = new Bundle();
                                extras.putInt("page_id", page.getId_cp());
                                PagesFragment pagesFragment = new PagesFragment();
                                bodyFragment = "PagesFragment";
                                pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container,
                                                pagesFragment).addToBackStack(null)
                                        .commit();
                            }
                        }

                        if (category.getChildren_categories().size() == 0) {
                            RealmList<Child_pages> pages = category.getChildren_pages();
                            if (!pages.isEmpty()) {
                                Child_pages page = pages.iterator().next();
                                extras = new Bundle();
                                extras.putInt("page_id", page.getId_cp());
                                PagesFragment pagesFragment = new PagesFragment();
                                bodyFragment = "PagesFragment";
                                pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container,
                                                pagesFragment).addToBackStack(null)
                                        .commit();
                            }


                        } else {


                            FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                            bodyFragment = "FullscreenCategoryFragment";
                            categoryFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                        }

                    } else if (type.equals("SurveyFragment")) {

                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                        SurveyFragment fragment = new SurveyFragment();
                        extras = new Bundle();
                        extras = (Bundle) map.get("EXTRAS");
                        fragment.setArguments(extras);
                        bodyFragment = "SurveyFragment";
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


                    } else if (type.equals("SplitFilteredCategoriesFragment")) {
                        //						CategoryFragment.isNewDesign = true;
                        //						Log.e(" Condition v?rifi? ", "  avec category.getParentCategory().getGroup_children_categories : " +category.getDisplay_type());

                        SplitFilteredCategoriesFragment fragment = new SplitFilteredCategoriesFragment();
                        if (extras == null)
                            extras = new Bundle();
                        extras = (Bundle) map.get("EXTRAS");
                        fragment.setArguments(extras);
                        bodyFragment = "SplitFilteredCategoriesFragment";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


                    } else if (type.equals("SplitListCategoryFragment")) {
                        if (extras == null)
                            extras = new Bundle();
                        extras = (Bundle) map.get("EXTRAS");
                        if (extras.getBoolean("isSorted", false)) {
                            SplitListCategoryFragment_ splitListCategorieFragment = new SplitListCategoryFragment_();
                            extras.putBoolean("isSorted", true);
                            splitListCategorieFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                    .addToBackStack(null).commit();
                        } else {

                            SplitListCategoryFragment splitListCategorieFragment = new SplitListCategoryFragment();
                            splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null).commit();
                        }

                    } else if (type.equals("SwipperFragment")) {

                        //						if (timer != null) {timer.cancel();}
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                        SwipperFragment fragment = new SwipperFragment();
                        extras = new Bundle();
                        extras = (Bundle) map.get("EXTRAS");
                        fragment.setArguments(extras);
                        bodyFragment = "SwipperFragment";
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


                    } else if (type.equals("RadiosSectionFragment")) {

                        RadiosSectionFragment fragment = new RadiosSectionFragment();
                        setRadioCallback(fragment);
                        extras = new Bundle();
                        extras = (Bundle) map.get("EXTRAS");
                        fragment.setArguments(extras);
                        bodyFragment = "RadiosSectionFragment";
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, bodyFragment).addToBackStack(bodyFragment).commit();

                        if (timer != null) {
                            timer.cancel();
                        }
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                    } else if (type.equals("DashboardFragment")) {
                        cd = new ConnectionDetector(getApplicationContext());

                        // Check if Internet present
                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            alert.showAlertDialog(MainActivity.this, getResources().getString(R.string.error_connecting),
                                    getResources().getString(R.string.error_connecting_message), false);
                            // stop executing code by return
                        } else {
                            params = realm.where(Parameters.class).findFirst();
                            if (timer != null) {
                                timer.cancel();
                            }
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.swipe_container).setVisibility(View.GONE);
                            extras = (Bundle) map.get("EXTRAS");
                            int section_id = 0;
                            if (extras != null) {
                                section_id = extras.getInt("Section_id", 0);
                            }
                            DownloadParseAsyncTask asyncTask = new DownloadParseAsyncTask(section_id);
                            String urlBase = Constants.PROD + "/api/application/dashboard/id/" + params.getId() + "/lang/" + wmbPreference.getString(Utils.LANG, "fr");
                            asyncTask.execute(urlBase/*"http://192.168.1.15/dash/28dash.json"*/);
                        }
                    } else if (type.equals("FreeFormulaFragment")) {
                        extras = (Bundle) map.get("EXTRAS");
                        bodyFragment = "FreeFormulaFragment";
                        FreeFormulaFragment formulaFragment = new FreeFormulaFragment();
                        formulaFragment.setArguments(extras);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, formulaFragment).addToBackStack(null).commit();
                    } else if (type.equals("AlbumsGridFragment")) { // Type Gallery
                        AlbumsGridFragment albumsGridFragment = new AlbumsGridFragment();
                        bodyFragment = "AlbumsGridFragment";
                        RealmResults<Section> sections = realm.where(Section.class).equalTo("type", "gallery").findAll();
                        // sections = appController.getSectionsDao().queryForEq("type", "gallery");
                        if (sections.size() > 0) {
                            Section section = sections.get(0);
                            extras.putInt("Section_id", section.getId_s());
                        }
                        albumsGridFragment.setArguments(extras);
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumsGridFragment).addToBackStack("AlbumsGridFragment").commit();

                    } else if (type.equals("GalerySliderFragment")) { // Type Gallery

                        RealmResults<Section> sections = realm.where(Section.class).equalTo("type", "gallery").findAll();

                        if (sections.size() > 0) {
                            Section section = sections.get(0);
                            extras.putInt("Section_id", section.getId_s());

                            bodyFragment = "GalerySliderFragment";
                            GalerySliderFragment galerySliderFragment = new GalerySliderFragment();
                            galerySliderFragment.setArguments(extras);
                            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, galerySliderFragment).addToBackStack("GalerySliderFragment").commit();


                        }
                    } else if (type.equals("GaleryFragment")) {
                        bodyFragment = "GaleryFragment";
                        extras = (Bundle) map.get("EXTRAS");
                        GaleryFragment galeryFragment = new GaleryFragment();
                        galeryFragment.setArguments(extras);
                        MainActivity.this.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();
                    } else {

                        bodyFragment = "HomeGridFragment";
                        if (params.getHome_type().equals("web")) {
                            WebHomeFragment webHomeFragment = new WebHomeFragment();
                            bodyFragment = "WebHomeFragment";
                            Bundle extra = new Bundle();
                            extra.putString("link", params.getWeb_home_test_url());
                            webHomeFragment.setArguments(extra);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,
                                            webHomeFragment).commit();
                        } else if (ifSwipe) {
                            //							buildSwipe();
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            findViewById(R.id.swipe_container).setVisibility(View.GONE);

                            Log.e(" swipperFragment :", " Yes it is " + " ");
                            SwipperFragment swipperFragment = new SwipperFragment();
                            ((MainActivity) getActivity()).bodyFragment = "SwipperFragment";
                            swipperFragment.setArguments(((MainActivity) getActivity()).extras);

                            //							getSupportFragmentManager().saveFragmentInstanceState(swipperFragment);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,
                                            swipperFragment)
                                    .commit();

                        } else {
                            HomeGridFragment homeGridFragment = new HomeGridFragment();
                            MainActivity.this.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, homeGridFragment)
                                    .addToBackStack(null).commit();
                        }

                    }

                } else {
                    bodyFragment = "HomeGridFragment";
                    if (params.getHome_type().equals("web")) {
                        WebHomeFragment webHomeFragment = new WebHomeFragment();
                        bodyFragment = "WebHomeFragment";
                        Bundle extra = new Bundle();
                        extra.putString("link", params.getWeb_home_test_url());
                        webHomeFragment.setArguments(extra);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        webHomeFragment).commit();
                    } else if (ifSwipe) {
                        //						buildSwipe();
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.swipe_container).setVisibility(View.GONE);

                        Log.e(" swipperFragment :", " Yes it is " + " ");
                        SwipperFragment swipperFragment = new SwipperFragment();
                        ((MainActivity) getActivity()).bodyFragment = "SwipperFragment";
                        swipperFragment.setArguments(((MainActivity) getActivity()).extras);

                        //						getSupportFragmentManager().saveFragmentInstanceState(swipperFragment);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        swipperFragment)
                                .commit();

                    } else {
                        HomeGridFragment homeGridFragment = new HomeGridFragment();
                        MainActivity.this.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, homeGridFragment)
                                .addToBackStack(null).commit();
                    }
                }

            }
        }

        infoActivity.setType(bodyFragment);
        //		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.intro);
        //		mediaPlayer.start(); // no need to call prepare(); create() does that for you

        final VideoView videoHolder = (VideoView) findViewById(R.id.videoView);
        if (params.isShow_intro_video()) {
            //if you want the controls to appear
            videoHolder.setMediaController(null);
            //			Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro); //do not add any extension
            //if your file is named sherif.mp4 and placed in /raw
            //use R.raw.sherif
            //			videoHolder.setVideoURI(video);
            //		setContentView(videoHolder);
            videoHolder.start();
            videoHolder.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoHolder.setVisibility(View.GONE);

                }
            });
        } else {
            videoHolder.setVisibility(View.GONE);
        }
        videoHolder.setVisibility(View.GONE);


    }

    /**
     *
     */


    /**Test if passing a command is allowed in the current time
     * @return
     */
    protected boolean verifyNotAllowed() {
        boolean dayNotAllowed = false;
        boolean timeNotAllowed = false;
        realm = Realm.getInstance(getApplicationContext());
        Calendar c = Calendar.getInstance();
        String dayOW = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        Disable_period disable_period = null;
        Collection<PeriodString> days = new ArrayList<PeriodString>();
        Collection<Disable_period> disable_periods = new ArrayList<Disable_period>();
        disable_periods = realm.where(Disable_period.class).findAll();
        //appController.getDisablePeriodDao().queryForAll();
        if (disable_periods.size() > 0) {
            disable_period = disable_periods.iterator().next();
        }
        if (disable_period != null) {

            days = realm.where(PeriodString.class).findAll();
            //appController.getPeriodStringDao().queryForAll();
            if (days.size() > 0) {
                for (Iterator<PeriodString> iterator2 = days.iterator(); iterator2.hasNext(); ) {
                    PeriodString periodString = (PeriodString) iterator2.next();
                    if (periodString.getDay().compareToIgnoreCase(dayOW) == 0) {
                        dayNotAllowed = true;
                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Impossible de valider la commande");
                        alertDialog.setMessage("Vous ne pouvez pas envoyer votre commande aujourd'hui.");
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        };
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", listener);
                        alertDialog.show();
                        return dayNotAllowed;
                    } else {
                        dayNotAllowed = false;
                    }


                }
            } else {
                dayNotAllowed = false;
            }

            if (!dayNotAllowed) {
                if (!disable_period.getStart().isEmpty() && !disable_period.getEnd().isEmpty()) {
                    timeNotAllowed = isNowBetweenDateTime(dateFromHourMinSec(disable_period.getStart()), dateFromHourMinSec(disable_period.getEnd()));

                    if (timeNotAllowed) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Impossible de valider la commande");
                        alertDialog.setMessage("Vous pouvez envoyer votre commande avant " + disable_period.getStart() + " et après " + disable_period.getEnd() + ". Merci de votre compréhension.");
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        };
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", listener);
                        alertDialog.show();
                    }
                    return timeNotAllowed;
                }
            }

        }

        return dayNotAllowed && timeNotAllowed;
    }

    public boolean correctTime(String hhmmss) {
        if (hhmmss.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$")) {
            return true;
        } else {
            return false;
        }
    }

    private Date dateFromHourMinSec(final String hhmm) {
        if (hhmm.matches("^[0-2][0-9]:[0-5][0-9]$")) {
            final String[] hms = hhmm.split(":");
            final GregorianCalendar gc = new GregorianCalendar();
            gc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hms[0]));
            gc.set(Calendar.MINUTE, Integer.parseInt(hms[1]));
            gc.set(Calendar.SECOND, 0);
            gc.set(Calendar.MILLISECOND, 0);
            return gc.getTime();
        } else {
            throw new IllegalArgumentException(hhmm + " is not a valid time, expecting HH:MM:SS format");
        }
    }

    boolean isNowBetweenDateTime(final Date s, final Date e) {
        final Date now = new Date();
        if (s.after(e)) {
            return now.before(s) && now.after(e);
        } else {
            return now.after(s) && now.before(e);
        }

    }

    public void instantiateCart(View menuView) {
        decorated = false;
        Application application = null;
        application = realm.where(Application.class).findFirst()
        ;
        //appController.getApplicationDataDao().queryForId(1);
        if (application != null && application.getParameters() != null && application.getParameters().isShow_cart()) {
            if (!decorated) {
                decorateCart(menuView);
                decorated = true;
            }
        }


    }

    private void decorateCart(View menuView) {
        Cart cart = null;
        cart = realm.where(Cart.class).findFirst();
        //appController.getCartDao().queryForId(1);
        if (cart == null) {
            decorated = true;
        }

        if (cart != null) {
            TextView titleCart = (TextView) menuView.findViewById(R.id.titleCart);
            titleCart.setText(cart.getTitle());
            titleCart.setTypeface(FONT_TITLE);
            titleCart.setTextColor(Color.parseColor("#" + colors.getBackground_color()));
            ImageView imgTitleCart = (ImageView) menuView.findViewById(R.id.imgTitleCart);
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(getAssets().open(
                        "icon/" + cart.getTab_icon()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            BitmapDrawable mDrawable = new BitmapDrawable(bm);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color
                    .parseColor("#" + colors.getBackground_color()),
                    PorterDuff.Mode.MULTIPLY));
            imgTitleCart.setImageDrawable(mDrawable);

            TextView validate_cartTV = (TextView) menuView.findViewById(R.id.validate_cartTV);
            validate_cartTV.setText(cart.getValidation_button());
            validate_cartTV.setTypeface(FONT_TITLE);
            ColorStateList txtStates = new ColorStateList(
                    new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{}},
                    new int[]{colors.getColor(colors.getTitle_color()), colors.getColor(colors.getBackground_color())});
            //			validate_cartTV.setTextColor(colors.getColor(colors.getBackground_color()));
            validate_cartTV.setTextColor(txtStates);
            LinearLayout subValidateCartLL = (LinearLayout) menuView.findViewById(R.id.subValidateCartLL);
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
            drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
            drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
            subValidateCartLL.setBackgroundDrawable(drawable);

            //divider:
            menuView.findViewById(R.id.test_view).setBackgroundColor(colors.getColor(colors.getTitle_color()));
            menuView.findViewById(R.id.viewDivider2).setBackgroundColor(colors.getColor(colors.getTitle_color()));
            menuView.findViewById(R.id.viewDivider).setBackgroundColor(colors.getColor(colors.getTitle_color()));

        }

        menuView.setBackgroundDrawable(colors.getForePD());

    }

    /*
      * (non-Javadoc)
      *
      * @see android.support.v4.app.FragmentActivity#onDestroy()
      */
    @Override
    protected void onDestroy() {
        //imageLoader.clearMemoryCache();
        //		imageLoader.destroy();
        Runtime.getRuntime().gc();
        if (timer != null) {
            timer.purge();
        }
        //		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
        //		.permitAll().build();
        //		StrictMode.setThreadPolicy(policy);
        //		sendRegistrationIdToBackend();
        sendInBackground();

        super.onDestroy();
    }




    /* @Override
     public void onTabClick(Tab tab, int index) {
         infoActivity.switchCurrentPrev();
         infoActivity.setIdCurrentTab(index);
         findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
         findViewById(R.id.swipe_container).setVisibility(View.GONE);

         //		if (params.getHome_type().equals("swipe")) {
         //			ifSwipe = true;
         //			findViewById(R.id.fragment_container).setVisibility(View.GONE);
         //			findViewById(R.id.swipe_container).setVisibility(View.VISIBLE);
         //		}else {
         //			ifSwipe = false;
         //			findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
         //			findViewById(R.id.swipe_container).setVisibility(View.GONE);
         //		}
         // get the section from the tab
         Section section = null;
         RealmResults<Section> sections = realm.where(Section.class).equalTo("id", tab.getSection_id()).findAll();
         //appController.getSectionsDao().queryForEq("id", tab.getSection_id());
         if (sections.size() > 0) {
             section = sections.get(0);
         }
         if (section != null) {

             openSection(tab, section);


         }


     }*/

    /*
      * (non-Javadoc)
      *
      * @see android.support.v4.app.FragmentActivity#
      * onRetainCustomNonConfigurationInstance()
      */
    @Override
    public Map<String, Object> onRetainCustomNonConfigurationInstance() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("SelectedTab", tabsFragment.indexSeletedTab);
        map.put("body", bodyFragment);
        map.put("EXTRAS", extras);
        return map;
    }

     /* @Override
      public void onConfigurationChanged(Configuration newConfig) {
          super.onConfigurationChanged(newConfig);

          if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
              if (isTablet) {
                  if (bottomNav) {

                      setContentView(R.layout.bottom_frame_container);
                  }else {
                      setContentView(R.layout.frame_container);
                  }
              }else {
                  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                  setContentView(R.layout.bottom_frame_container);
              }

          } else {
              if (isTablet) {
                  if (bottomNav) {

                      setContentView(R.layout.bottom_frame_container);
                  }else {
                      setContentView(R.layout.frame_container);
                  }
              }else {
                  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                  setContentView(R.layout.bottom_frame_container);
              }
          }
      }*/

    /* (non-Javadoc)
      * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
      */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {

            case android.R.id.home:
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(),
                        Settings.class);
                startActivity(intent);
                return true;
            default:
                break;
        }

        return true;
    }

    /* (non-Javadoc)
      * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
      */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    //	public void initImageLoader() {
    //		imageLoader = ImageLoader.getInstance();
    //		options = new DisplayImageOptions.Builder()
    //		.showStubImage(R.drawable.ic_empty)
    //		.showImageForEmptyUri(R.drawable.ic_empty)
    //		.showImageOnFail(R.drawable.ic_empty)
    //		.resetViewBeforeLoading()
    //		.cacheOnDisc().cacheInMemory()
    //		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
    //		.bitmapConfig(Bitmap.Config.RGB_565) // default
    //		.displayer(new SimpleBitmapDisplayer()) // default
    //		.handler(new Handler()) // default
    //		.build();
    //
    //		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
    //		configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
    //		.memoryCacheExtraOptions(500, 500) // default = device screen dimensions
    //		.discCacheExtraOptions(500, 500, CompressFormat.PNG, 0)
    //		.taskExecutor(AsyncTask.SERIAL_EXECUTOR)
    //		.taskExecutorForCachedImages(AsyncTask.SERIAL_EXECUTOR)
    //		.threadPoolSize(3) // default
    //		.threadPriority(Thread.MAX_PRIORITY - 1) // default
    //		.tasksProcessingOrder(QueueProcessingType.FIFO) // default
    //		//        .denyCacheImageMultipleSizesInMemory()
    //		.discCache(new UnlimitedDiscCache(cacheDir)) // default
    //		.discCacheSize(100 * 1024 * 1024)
    //		.discCacheFileCount(100)
    //		.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
    //		.imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
    //		.imageDecoder(new BaseImageDecoder()) // default
    //		.defaultDisplayImageOptions(options) // default
    //		.enableLogging()
    //		.build();
    //		imageLoader.init(configuration);
    //	}
    //
     /* (non-Javadoc)
      * @see android.support.v4.app.FragmentActivity#onResume()
      */
    @Override
    protected void onResume() {

        super.onResume();

        if (!passedOncreate) {
             /*tabsFragment = new TabsSupportFragment();
             Bundle extrasTabsFrag =  new Bundle();
             extrasTabsFrag.putInt("highlighted_tab", infoActivity.getIdCurrentTab());
             extrasTabsFrag.putBoolean("orientation", true);
             tabsFragment.setArguments(extrasTabsFrag);
             MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.tabsFragment, tabsFragment).commit();*/
        }
        passedOncreate = false;


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /*mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                0, mLocationListener);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000,
                0, mLocationListener);*/
        //  mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

                    return;
                }
            }*/
            if (Build.VERSION.SDK_INT >= 23) {
                permissionCheck =  checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

                if(permissionCheck == PackageManager.PERMISSION_GRANTED)
                {
                    if (checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No explanation needed, we can request the permission.
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},   REQUEST_CODE_ASK_PERMISSIONS);
                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
                    }
                }
            }else{
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            }
        }

    }

    /**
     * @return the menu
     */
    public SlidingMenu getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(SlidingMenu menu) {
        this.menu = menu;
    }



    /**
     * fill the cart with the {@link CartItem} taken from the database
     */
    public void fillCart(){
        nmbrProducts = 0;
        List<CartItem> cartItems = new ArrayList<CartItem>();
        // les 2 produits
        realm = Realm.getInstance(getApplicationContext());
        cartItems =  realm.where(CartItem.class).findAll();//appController.getCartItemDao().queryForAll();
        total = 0.0;
        LinearLayout linearLayoutSummery = (LinearLayout) menuView.findViewById(R.id.linearLayoutSummery);

        TextView emptyTxt = (TextView) menuView.findViewById(R.id.empty_cart_text);
        View viewDivider = menuView.findViewById(R.id.viewDivider);
        if (cartItems.size()>0) {
            linearLayoutSummery.setVisibility(View.VISIBLE);
            emptyTxt.setVisibility(View.INVISIBLE);
            viewDivider.setVisibility(View.VISIBLE);
            cartTagContainer.removeAllViews();

            for (int i = 0; i < cartItems.size(); i++) {
                addItemToCart(cartItems.get(i));
            }

            View space_dummy = new View(getActivity());
            cartTagContainer.addView(space_dummy, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.order_dim)));
            addToCartContainer(getNewDivider());
        }else {
            stripe_or_not = -1;
            viewDivider.setVisibility(View.INVISIBLE);
            emptyTxt.setVisibility(View.VISIBLE);
            emptyTxt.setTextColor(colors.getColor(colors.getBackground_color(), "AA"));
            linearLayoutSummery.setVisibility(View.INVISIBLE);
            cartTagContainer.removeAllViews();
            View space_dummy = new View(getActivity());
            cartTagContainer.addView(space_dummy, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.order_dim)));
        }
        totalSum.setTextColor(colors.getColor(colors.getTabs_foreground_color()));
        NumberFormat nf = NumberFormat.getInstance(); // get instance
        nf.setMaximumFractionDigits(2); // set decimal places
        nf.setMinimumFractionDigits(2);
        String s = nf.format(total);
        totalSum.setText(s+" "+getString(R.string.euro));
        totalSum.setTypeface(FONT_BODY);
        if (priceCallBack != null) {
            priceCallBack.notify(s+" "+getString(R.string.euro));
            priceCallBack.notifyNumber(nmbrProducts);
        }

    }
    public int nmbrProducts = 0;

    /**add a {@link CartItem} to the cart
     * @param item
     */
    public void addItemToCart(CartItem item) {
        NumberFormat nf = NumberFormat.getInstance(); // get instance
        nf.setMaximumFractionDigits(2); // set decimal places
        nf.setMinimumFractionDigits(2);
        String s;

        //	LinearLayout menuView = cartTagContainer;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View cartItemView = layoutInflater.inflate(R.layout.cart_tag, null, false);
        //define common graphic elements
        TextView titleProduct = (AutoResizeTextView)cartItemView.findViewById(R.id.TitleProduit);
        titleProduct.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
        titleProduct.setTypeface(FONT_TITLE, Typeface.BOLD);

        TextView typeProduit = (AutoResizeTextView)cartItemView.findViewById(R.id.typeProduit);
        TextView quantity = (AutoResizeTextView)cartItemView.findViewById(R.id.quantity);
        AutoResizeTextView relativeSum = (AutoResizeTextView)cartItemView.findViewById(R.id.relativeSum);
        relativeSum.setTypeface(FONT_BODY);
        LinearLayout deleteContainer = (LinearLayout)cartItemView.findViewById(R.id.deleteContainer);
        ImageView imgFormule = (ImageView)cartItemView.findViewById(R.id.imgCartItem);
        ImageView closeCmd = (ImageView)cartItemView.findViewById(R.id.closeCmd);
        Drawable drawcloseCmd = getResources().getDrawable(R.drawable.ic_exit);
        drawcloseCmd.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()) ,PorterDuff.Mode.MULTIPLY));

        closeCmd.setImageDrawable(drawcloseCmd);;

        //coloring
        titleProduct.setTextColor(colors.getColor(colors.getBackground_color()));
        typeProduit.setTextColor(colors.getColor(colors.getBackground_color(), "AA"));
        quantity.setTextColor(colors.getColor(colors.getBackground_color()));
        relativeSum.setTextColor(colors.getColor(colors.getBackground_color()));

        String prix = item.getPrice();

        /** Uness Modif **/
        if (item.getFormule()!= null) {
            nmbrProducts++;
            Formule formule = item.getFormule();
            String duplicate = ""; /* realm.where(Formule.class).findAll()*/
            if (item.getDuplicate() > 1) {
                duplicate = " #" + item.getDuplicate();
            }
            titleProduct.setText(formule.getName() + duplicate);
            titleProduct.setTypeface(FONT_BODY, Typeface.BOLD);

            String price1=formule.getPrice();
                       if(price1.isEmpty())
                          price1="0";
                 Double rSum = (item.getQuantity())*(Double.parseDouble(price1));
            s = nf.format(rSum);
            relativeSum.setText(s+" "+getString(R.string.euro));

            total = total + rSum;
            //			relativeSum.setText(rSum+"");
            //typeProduit.setText(getResources().getString(R.string.formule));
            typeProduit.setTypeface(FONT_BODY);
            if (formule.getIllustration() != null) {
                if(formule.getIllustration().getPath().isEmpty()) {
                    Glide.with(context).load(formule.getIllustration().getLink()).into(imgFormule);
                }else {

                    Glide.with(context).load(new File(formule.getIllustration().getPath())).into(imgFormule);
                }
            }else {
                imgFormule.setVisibility(View.GONE);
            }
            addToCartContainer(cartItemView);
            addToCartContainer(getNewDivider());
            for (Iterator<FormuleElement> iterator = formule.getElements().iterator(); iterator.hasNext();) {
                View elementsView = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
                FormuleElement element = (FormuleElement) iterator.next();
                TextView tvElement = (AutoResizeTextView)elementsView.findViewById(R.id.TitleProduit);
                tvElement.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
                titleProduct.setTypeface(FONT_BODY);
                tvElement.setTextColor(colors.getColor(colors.getBackground_color(),"AA"));//colors.getColor(colors.getBackground_color())
                tvElement.setText(element.getName());
                //tvElement.setTypeface(FONT_BODY);
                elementsView.findViewById(R.id.relativeSum).setVisibility(View.GONE);
                elementsView.findViewById(R.id.deleteContainer).setVisibility(View.GONE);
                cartTagContainer.addView(getNewDivider());
                cartTagContainer.addView(elementsView);

                //				addToCartContainer(elementsView);
                addToCartContainer(getNewDivider());
            }

        }else if (item.getCartItems().size()>0 ) {
            nmbrProducts++;
            String duplicate = "";
            if (item.getDuplicate() > 1) {
                duplicate = " #" + item.getDuplicate();
            }

            titleProduct.setText(item.getName()+duplicate);
            titleProduct.setTypeface(FONT_TITLE, Typeface.BOLD);
            typeProduit.setVisibility(View.GONE);
            imgFormule.setVisibility(View.GONE);
            addToCartContainer(cartItemView);
            addToCartContainer(getNewDivider());
            RealmList<CartItem> items = item.getCartItems();
            Double prixAccumul = 0.0;
            for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
                final CartItem cartItem = (CartItem) iterator.next();
                View subItem = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
                TextView tvElement = (AutoResizeTextView)subItem.findViewById(R.id.TitleProduit);
                tvElement.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
                titleProduct.setTypeface(FONT_BODY);
                tvElement.setTextColor(colors.getColor(colors.getBackground_color(),"AA"));
                tvElement.setText(cartItem.getName());
                //tvElement.setTypeface(FONT_BODY);
                TextView subSum = (AutoResizeTextView)subItem.findViewById(R.id.relativeSum);
                //				s = nf.format(cartItem.getPrice());
                subSum.setText(cartItem.getPrice());
                subSum.setTypeface(FONT_BODY);
                subSum.setTextColor(colors.getColor(colors.getBackground_color(),"AA"));
                String price2=cartItem.getPrice();
                if(price2.isEmpty())
                    price2="0";

                prixAccumul = prixAccumul+ Double.parseDouble(price2);
                LinearLayout subDelete = (LinearLayout)subItem.findViewById(R.id.deleteContainer);
                subDelete.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        realm.beginTransaction();
                        cartItem.removeFromRealm();
                        realm.commitTransaction();
                        //appController.getCartItemDao().delete(cartItem);
                        fillCart();
                    }
                });

                cartTagContainer.addView(getNewDivider());
                cartTagContainer.addView(subItem);
                //				addToCartContainer(subItem);
                //				//				menuView.addView(subItem);
                //				addToCartContainer(getNewDivider());
            }
            if(prix.isEmpty())
                prix="0";
            Double rSum = (item.getQuantity())*(Double.parseDouble(prix)) + (item.getQuantity())*prixAccumul;
            s = nf.format(rSum);
            relativeSum.setText(s+" "+getString(R.string.euro));
            relativeSum.setTypeface(FONT_BODY);
            total = total + rSum;

        } else if (item.getParentItem() == null) {
            nmbrProducts++;
            try {
                if (item.getChild_page() != null && item.getChild_page().getItemIllustration1() != null) {
                    if(item.getChild_page().getItemIllustration1()!=null) {
                        Glide.with(context).load(String.valueOf(item.getChild_page().getItemIllustration1().getLink())).into(imgFormule);

                    }else {

                        Glide.with(context).load(new File(String.valueOf(item.getChild_page().getItemIllustration1().getPath()))).into(imgFormule);
                        //imageLoader.displayImage(path, imgFormule, options);
                    }
                }else{
                    imgFormule.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String duplicate = "";
            if (item.getDuplicate() > 1) {
                duplicate = " #" + item.getDuplicate();
            }
            titleProduct.setText(item.getName()+ duplicate);
            String prixx = item.getPrice();
            Double rSum=0.0;
            if(!prixx.isEmpty())
                rSum = (item.getQuantity())*(Double.parseDouble(prixx));
            s = nf.format(rSum);
            relativeSum.setText(s+" "+getString(R.string.euro));
            total = total + rSum;
            //			imgFormule.setVisibility(View.GONE);
            if (item.getPrice_label() != null && !item.getPrice_label().isEmpty()) {
                typeProduit.setText(item.getPrice_label());
            }else {
                typeProduit.setVisibility(View.GONE);
            }

            addToCartContainer(cartItemView);
            addToCartContainer(getNewDivider());
        }else {

        }

        quantity.setText(item.getQuantity()+"");
        final CartItem itemTmp = item;
        deleteContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(
                        MainActivity.this).create();
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Êtes-vous sur de procéder à la suppression ");
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == AlertDialog.BUTTON_POSITIVE) {
                            alertDialog.dismiss();
                        }else if (which == AlertDialog.BUTTON_NEGATIVE) {
                            try {
                                deleteFromRoots(itemTmp);
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            //								appController.getCartItemDao().delete(itemTmp);
                            fillCart();
                            alertDialog.dismiss();
                        }

                    }
                };
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Supprimer", listener );
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Annuler", listener);
                alertDialog.show();

            }
        });
        ImageView quantityLess = (ImageView)cartItemView.findViewById(R.id.quantityLess);
        ImageView quantityMore = (ImageView)cartItemView.findViewById(R.id.quantityMore);
        if (item.getQuantity() == 1) {
            quantityLess.setAlpha(0.5f);
        }else {
            quantityLess.setAlpha(1f);
        }
        quantityLess.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int quant = itemTmp.getQuantity();
                CartItem cartItem = itemTmp;
                cartItem = realm.where(CartItem.class).equalTo("id",itemTmp.getId()).findFirst();
                //appController.getCartItemDao().queryForId(itemTmp.getId());
                //				CartItem arg0 = itemTmp;
                if (quant > 1) {
                    realm.beginTransaction();
                    if (quant == 2) {
                        v.setAlpha(0.5f);
                    }
                    cartItem.setQuantity(quant -1);

                    boolean done = false;

                    realm.copyToRealmOrUpdate(cartItem);

                    //appController.getCartItemDao().createOrUpdate(cartItem);
                    done  = true;
                    if (done) {
                        fillCart();
                    }
                    realm.commitTransaction();
                }else if (quant == 1) {
                    v.setAlpha(0.5f);
                } else {

                }

            }
        });

        quantityMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                int quant = itemTmp.getQuantity();
                CartItem cartItem = itemTmp;
                cartItem = realm.where(CartItem.class).equalTo("id",itemTmp.getId()).findFirst();
                //appController.getCartItemDao().queryForId(itemTmp.getId());
                cartItem.setQuantity(quant +1);
                boolean done = false;

                realm.copyToRealmOrUpdate(cartItem);

                //appController.getCartItemDao().createOrUpdate(cartItem);
                done  = true;
                if (done) {
                    //					String prix = itemTmp.getPrice();
                    //					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
                    //					relativeSum.setText(rSum+"");
                    //					total = total + Double.parseDouble(prix);
                    fillCart();
                }
                realm.commitTransaction();
            }
        });

    }

    private void addToCartContainer(View subItem) {
        //		int size = cartTagContainer.getChildCount();
        //		if (size >1) {
        //			cartTagContainer.addView(subItem, size - 2);
        //
        //		}else {
        //		if (cartTagContainer.getChildCount()-2 >= 0) {
        //			cartTagContainer.addView(subItem, cartTagContainer.getChildCount()-2);
        //		}else {
        cartTagContainer.addView(subItem);
        //		}


        //		}

    }

    public void deleteFromRoots(CartItem item) throws SQLException {
        realm.beginTransaction();
        RealmList<CartItem> childItems = item.getCartItems();
        while (item.getCartItems().size()>0) {

            for (Iterator<CartItem> iterator = childItems.iterator(); iterator
                    .hasNext();) {
                CartItem cartItem = (CartItem) iterator.next();
            cartItem.removeFromRealm();
                //appController.getCartItemDao().delete(cartItem);
            }

            //appController.getCartItemDao().refresh(item);
        }
        // appController.getCartItemDao().delete(item);


       /* CartItem c= realm.where(CartItem.class).findAll().get(item.getId());*//*remove(item);*/
        item.removeFromRealm();
       /* c.removeFromRealm();*/
        realm.commitTransaction();


    }

    private View getNewDivider() {
        View divider = new View(getActivity());
        divider.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(colors.getColor(colors
                .getForeground_color()));
        return divider;
    }

    public void addItemToDB(int price_id, Child_pages child_page,
                            CartItem parentItem, int order, int quantity, Formule formule,
                            String price, String name, String price_label, RealmList<CartItem> cartItems) {
        /*realm=Realm.getInstance(getActivity());*/

        boolean addToDB = true;
/*        id_Cart= realm.where(CartItem.class).findAll().last().getId()+1;*/
       /* CartItem cartItem=new CartItem(id_Cart,price_id, child_page, parentItem, order, quantity, formule, price, name, price_label,cartItems);*/
        realm.beginTransaction();
        CartItem cartItem = realm.createObject(CartItem.class);
      /*  cartItem.setId(id_Cart);cartItem.setPrice_id(price_id); cartItem.setChild_page(child_page);cartItem.setParentItem(parentItem); cartItem.setOrder(order);
        cartItem.setQuantity(quantity); cartItem.setFormule(formule);cartItem.setPrice(price); cartItem.setName(name);cartItem.setPrice_label(price_label);cartItem.setCartItems(cartItems);
     */   // le produit existant et cartitems size =0
        List<CartItem> itemsCart = realm.where(CartItem.class).findAll();//appController.getCartItemDao().queryForAll();
        Category category=realm.where(Category.class).equalTo("id",child_page.getCategory_id()).findFirst();


        for (int i = itemsCart.size()-1; i >= 0; i--) {
            if (itemsCart.get(i).getName().equalsIgnoreCase(name) && ((category!= null && category.getDisplay_type()!= null && category.getDisplay_type().equalsIgnoreCase("multi_select")) || !(((itemsCart.get(i).getParentItem() == null || (itemsCart.get(i).getCartItems()!= null && itemsCart.get(i).getCartItems().size()>0)) || itemsCart.get(i).getFormule() == null)))) {

                cartItem.setDuplicate(itemsCart.get(i).getDuplicate()+1);

          /*   en commentaire c'est l existant  break;*/
            }

         /* if */ else if (itemsCart.get(i).getName().equalsIgnoreCase(name)/*itemsCart.get(i).getPrice_id()==(price_id) && ((itemsCart.get(i).getParentItem() == null || (itemsCart.get(i).getCartItems()!= null && itemsCart.get(i).getCartItems().size()>0)) || itemsCart.get(i).getFormule() == null)*/){
                itemsCart.get(i).setQuantity(itemsCart.get(i).getQuantity()+1/*cartItem.getQuantity()*/);



                /*cartItem.setId(id_Cart);cartItem.setPrice_id(price_id); cartItem.setChild_page(child_page);cartItem.setParentItem(parentItem); cartItem.setOrder(order);
                cartItem.setQuantity(quantity); cartItem.setFormule(formule);cartItem.setPrice(price); cartItem.setName(name);cartItem.setPrice_label(price_label);cartItem.setCartItems(cartItems);
*/
             /*  realm.copyToRealmOrUpdate(itemsCart.get(i)); */// duplicate =1 et id de position

                //appController.getCartItemDao().update(itemsCart.get(i));
                addToDB = false;
            }
            if(itemsCart.get(i).getId()==0 ) {
                id_Cart= realm.where(CartItem.class).findAllSorted("id").last().getId()+1;
                itemsCart.get(i).setId(id_Cart);
            }

            }

        if (addToDB ) {


            cartItem.setId(id_Cart);cartItem.setPrice_id(price_id); cartItem.setChild_page(child_page);cartItem.setParentItem(parentItem); cartItem.setOrder(order);
            cartItem.setQuantity(quantity); cartItem.setFormule(formule);cartItem.setPrice(price); cartItem.setName(name);cartItem.setPrice_label(price_label);cartItem.setCartItems(cartItems);


            // appController.getCartItemDao().createOrUpdate(cartItem);
        }
        for (int i = 0; i <realm.where(CartItem.class).findAll().size() ; i++) {

          if(realm.where(CartItem.class).findAll().get(i).getQuantity()==0)
            realm.where(CartItem.class).findAll().removeLast();
        }
        realm.commitTransaction();
    }



    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    AlertDialog beforeComandDialog;

    public void showDialog() {
        List<Cart> carts = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        carts = realm.where(Cart.class).findAll();
        //appController.getCartDao().queryForAll();
        fields = realm.where(Field.class).findAll();
        //appController.getFieldDao().queryForAll();


        if (params.getShop_url()==null || params.getShop_url().isEmpty()) {
            if (carts.size()>0 && fields.size()>0) {

                String alert_msg = carts.iterator().next().getAlert_message();
                if (alert_msg != null && !alert_msg.isEmpty()) {
                    beforeComandDialog = new AlertDialog.Builder(getActivity()).create();

                    //				 beforeComandDialog.setTitle("Impossible de valider la commande");
                    beforeComandDialog.setMessage(alert_msg);
                    beforeComandDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.continue_command), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            mStackLevel++;

                            // DialogFragment.show() will take care of adding the fragment
                            // in a transaction.  We also want to remove any currently showing
                            // dialog, so make our own transaction and take care of that here.
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);
                            bodyFragment = "FormDialogFragment";
                            // Create and show the dialog.
                            FormCartFragment newFragment = FormCartFragment.newInstance(mStackLevel );
                            newFragment.setCancelable(false);
                            //							newFragment.setStyle( DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog );
                            newFragment.show(ft, "dialog");
                        }
                    } );
                    beforeComandDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_cart), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    } );
                    beforeComandDialog.show();
                }else {
                    mStackLevel++;

                    // DialogFragment.show() will take care of adding the fragment
                    // in a transaction.  We also want to remove any currently showing
                    // dialog, so make our own transaction and take care of that here.
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    bodyFragment = "FormDialogFragment";
                    // Create and show the dialog.
                    FormCartFragment newFragment = FormCartFragment.newInstance(mStackLevel );
                    newFragment.setCancelable(false);
                    newFragment.show(ft, "dialog");
                }


            }
        }else {
            String shopUrl = params.getShop_url();
            List<CartItem> items = new ArrayList<CartItem>();
            items = realm.where(CartItem.class).findAll();
            // appController.getCartItemDao().queryForAll();
            if (items.size()>0) {
                for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
                    CartItem cartItem = (CartItem) iterator.next();
                    String store_id = "";
                    String quantity = "";
                    String label = "";
                    if (cartItem.getChild_page()!=null) {
                        if (cartItem.getChild_page().getStore_product_id()!=0) {
                            store_id = cartItem.getChild_page().getStore_product_id()+"";
                        }
                    }
                    if (cartItem.getQuantity()!=0) {
                        quantity = ","+cartItem.getQuantity()+"";
                    }
                    if (cartItem.getPrice_label()!=null && !cartItem.getPrice_label().isEmpty()) {
                        label = ","+cartItem.getPrice_label();
                    }
                    shopUrl = shopUrl+"&product[]="+store_id+quantity+label;
                }

                WebViewFragment  webViewFragment= new WebViewFragment();
                extras = new Bundle();
                extras.putString("link", shopUrl);
                webViewFragment.setArguments(extras);
                bodyFragment = "WebViewFragment";
                MainActivity.this
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, webViewFragment).commit();

            }
        }


    }

    @Override
    public void onRefreshRequested(int id) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("panorama");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        Fragment panoFragment = new PanoramaFragment();
        extras.putInt("page_id", id);
        bodyFragment = "PanoramaFragment";
        panoFragment.setArguments(extras);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
        //		 fragmentTransaction.replace(R.id.fragment_container, panoFragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onTabClick(com.euphor.paperpad.Beans.Tab tab, int index) {
        infoActivity.switchCurrentPrev();
        infoActivity.setIdCurrentTab(index);
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        findViewById(R.id.swipe_container).setVisibility(View.GONE);

        //		if (params.getHome_type().equals("swipe")) {
        //			ifSwipe = true;
        //			findViewById(R.id.fragment_container).setVisibility(View.GONE);
        //			findViewById(R.id.swipe_container).setVisibility(View.VISIBLE);
        //		}else {
        //			ifSwipe = false;
        //			findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        //			findViewById(R.id.swipe_container).setVisibility(View.GONE);
        //		}
        // get the section from the tab
        Section section = null;
        realm = Realm.getInstance(getApplicationContext());
        RealmResults<Section> sections = realm.where(Section.class).equalTo("id", tab.getSection_id()).findAll();
        //appController.getSectionsDao().queryForEq("id", tab.getSection_id());
        if (sections.size() > 0) {
            section = sections.get(0);
        }
        if (section != null) {

            openSection(tab, section);


        }

    }

    @Override
    public void onLanguageChanged() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(
                R.layout.language_dialog, null);
        LinearLayout frenchContainer = (LinearLayout) layout
                .findViewById(R.id.frenchContainer);
        LinearLayout englishContainer = (LinearLayout) layout
                .findViewById(R.id.englishContainer);


        if (true) {
            frenchContainer.setVisibility(View.VISIBLE);

            frenchContainer
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //							flag.setImageDrawable(getResources()
                            //									.getDrawable(
                            //											R.drawable.french_r));
                            //langTxt.setTypeface(FONT_REGULAR);
                            SharedPreferences.Editor editor = wmbPreference
                                    .edit();
                            editor.putString("LANGUAGE_ID", "2");
                            editor.commit();


                            String lang = wmbPreference
                                    .getString("LANGUAGE_ID",
                                            "2");
                            LANGUAGE_ID = Integer.parseInt(lang);

                            pw.dismiss();
                        }


                    });
        } else {
            frenchContainer.setVisibility(View.GONE);
        }

        if (true) {
            englishContainer.setVisibility(View.VISIBLE);

            englishContainer
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //							flag.setImageDrawable(getResources()
                            //									.getDrawable(
                            //											R.drawable.english_r));
                            SharedPreferences.Editor editor = wmbPreference
                                    .edit();
                            editor.putString("LANGUAGE_ID", "1");
                            editor.commit();

                            String lang = wmbPreference
                                    .getString("LANGUAGE_ID",
                                            "2");
                            LANGUAGE_ID = Integer
                                    .parseInt(lang);

                            pw.dismiss();
                        }
                    });
        } else {
            englishContainer.setVisibility(View.GONE);
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
        //		pw.showAsDropDown(langContainer);

    }

    public void openChildPage(Child_pages page, boolean ignoreHide) {
        if (!isMultiSelect(page)) {


            if (!page.isHide_product_detail()) {
                openPage(page);

            }else if (ignoreHide) {
                openPage(page);
            } else {

            }
        }else {
            openPage(page);
        }
    }

    public void openPage(Child_pages page) {
        if (page.isVisible()) {
            if (isTablet) {
                realm = Realm.getInstance(getApplicationContext());
                if(page != null && page.getAuto_open_url() != null && page.getAuto_open_url().contains("http://")) {

                    WebViewFragment  webViewFragment= new WebViewFragment();
                    extras = new Bundle();
                    extras.putString("link", page.getAuto_open_url());
                    webViewFragment.setArguments(extras);
                    bodyFragment = "WebViewFragment";
                    MainActivity.this
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, webViewFragment).commit();

                }
                else if (page.getDesign().equals("panoramic")) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction(); //.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("panorama");
                    if (prev != null) {
                        fragmentTransaction.remove(prev);
                    }
                    Fragment panoFragment = new PanoramaFragment();
                    extras.putInt("page_id", page.getId_cp());
                    bodyFragment = "PanoramaFragment";
                    panoFragment.setArguments(extras);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
                    fragmentTransaction.commit();
                }else if(isTablet &&  realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst() !=null && realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst().getDisplay_type().compareTo("split_list")==0
                         /*appController.getCategoryByIdDB(page.getCategory().getId_c()).getDisplay_type().compareTo("split_list") == 0*/
                         /*&& (page.getDesign().compareToIgnoreCase("horizontal") == 0 || page.getDesign().compareToIgnoreCase("vertical") == 0)*/){
                    //				CategoryFragment.isNewDesign = true;
                    //				Log.e(" MainActivity ", "  isNewDesign : " +CategoryFragment.isNewDesign);
                    //				bodyFragment = "SplitListFragment";
                    if(extras == null)
                        extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    Category category= realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();
                    extras.putString("split_list", category.getDisplay_type());
                    ((MainActivity)getActivity()).extras.putInt("Category_id", page.getCategory_id());
                    SplitListFragment splitListFragment = new SplitListFragment();
                    splitListFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.navitem_detail_container, splitListFragment)
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).commit();

                }
                else if (page.getDesign().equalsIgnoreCase("column")) {
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    bodyFragment = "ColonnePageFragment";
                    ColonnePageFragment colonnePageFragment = new ColonnePageFragment();
                    colonnePageFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, colonnePageFragment).addToBackStack(null).commit();
                }else if (page.getDesign().equalsIgnoreCase("free_formule")) {
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    bodyFragment = "FreeFormulaFragment";
                    FreeFormulaFragment formulaFragment = new FreeFormulaFragment();
                    formulaFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, formulaFragment).addToBackStack(null).commit();
                }else if(isTablet &&  realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst()!=null &&
                        realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst().getTitle().compareTo("Contenu Agenda")==0
                 /*appController.getCategoryByIdDB(page.getCategory().getId_c()) != null &&

                         appController.getCategoryByIdDB(page.getCategory().getId_c()).getTitle().compareTo("Contenu Agenda") == 0*/ ){
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    extras.putString("AgendaDesign", page.getCategory().getTitle());
                    ((MainActivity)getActivity()).extras.putInt("Category_id", page.getCategory().getId());
                    EventPageFragment eventPageFragment = new EventPageFragment();
                    eventPageFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.navitem_detail_container, eventPageFragment).addToBackStack(null).commit();

                }
                else {
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    PagesFragment pagesFragment = new PagesFragment();
                    pagesFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pagesFragment).addToBackStack(null).commit();
                }

            }else{

                if(page != null && page.getAuto_open_url() != null && page.getAuto_open_url().contains("http://")) {

                    WebViewFragment  webViewFragment= new WebViewFragment();
                    extras = new Bundle();
                    extras.putString("link", page.getAuto_open_url());
                    webViewFragment.setArguments(extras);
                    bodyFragment = "WebViewFragment";

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, webViewFragment).commit();

                }
                else if (page.getDesign_smartphone().equals("panoramic")) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction(); //.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("panorama");
                    if (prev != null) {
                        fragmentTransaction.remove(prev);
                    }
                    Fragment panoFragment = new PanoramaFragment();
                    extras.putInt("page_id", page.getId_cp());
                    bodyFragment = "PanoramaFragment";
                    panoFragment.setArguments(extras);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
                    fragmentTransaction.commit();
                }else if(isTablet &&realm.where(Category.class).equalTo("id",page.getCategory_id()) !=null &&
                        realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst().getDisplay_type().compareTo("split_list")==0 /*&& appController.getCategoryByIdDB(page.getCategory().getId_c()) != null &&
                         appController.getCategoryByIdDB(page.getCategory().getId_c()).getDisplay_type().compareTo("split_list") == 0*/
                        ){

                    if(extras == null)
                        extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    extras.putString("split_list", page.getCategory().getDisplay_type());
                    ((MainActivity)getActivity()).extras.putInt("Category_id", page.getCategory().getId());
                    SplitListFragment splitListFragment = new SplitListFragment();
                    splitListFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.navitem_detail_container, splitListFragment)
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).commit();

                }
                else if (page.getDesign_smartphone().equalsIgnoreCase("column") && isTablet) {
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    bodyFragment = "ColonnePageFragment";
                    ColonnePageFragment colonnePageFragment = new ColonnePageFragment();
                    colonnePageFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, colonnePageFragment).addToBackStack(null).commit();
                }else if (page.getDesign_smartphone().equalsIgnoreCase("free_formule")) {
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    bodyFragment = "FreeFormulaFragment";
                    FreeFormulaFragment formulaFragment = new FreeFormulaFragment();
                    formulaFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, formulaFragment).addToBackStack(null).commit();
                }else if(isTablet && realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst()!=null && realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst().getTitle().compareTo("Contenu Agenda")==0
                        /* appController.getCategoryByIdDB(page.getCategory().getId_c()) != null &&
                         appController.getCategoryByIdDB(page.getCategory().getId_c()).getTitle().compareTo("Contenu Agenda") == 0*/ ){
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    extras.putString("AgendaDesign", page.getCategory().getTitle());
                    ((MainActivity)getActivity()).extras.putInt("Category_id", page.getCategory().getId());
                    EventPageFragment eventPageFragment = new EventPageFragment();
                    eventPageFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.navitem_detail_container, eventPageFragment).addToBackStack(null).commit();

                }
                else {
                    extras = new Bundle();
                    extras.putInt("page_id", page.getId_cp());
                    PagesFragment pagesFragment = new PagesFragment();
                    pagesFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pagesFragment).addToBackStack(null).commit();
                }

            }

        }


    }
    private LocationManager mLocationManager;
    private Location lastLocation;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            lastLocation = location;
            //mLocationListener.onLocationChanged(location);
            mLocationManager.removeUpdates(mLocationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void openCategory(Category category) {

        //category = appController.getCategoryById(6342);

        if (category != null) {

            if(category.getParameters().contains("geolock")){

                if (lastLocation != null) {
                    String[] spliter = category.getParameters().split("[geolock\\,\\[\\]]+");
                    double lat, lon, rayon;
                    lat = lon = rayon = 0;

                    if(spliter.length == 4){
                        lat = Double.parseDouble(spliter[1]);
                        lon = Double.parseDouble(spliter[2]);
                        rayon = Double.parseDouble(spliter[3]);
                    }
                    Location geoLocklocation = new Location("geoLock");
                    geoLocklocation.setLatitude(lat);
                    geoLocklocation.setLongitude(lon);

                    if(lastLocation.distanceTo(geoLocklocation) > rayon){
                        //Toast.makeText(getApplicationContext(), getString(R.string.geoLockMsg), Toast.LENGTH_LONG).show();
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.geoLockMsg));
                        // Setting alert dialog icon
                        alertDialog.setIcon(android.R.drawable.ic_dialog_info);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                        return;
                    }
                    //locationManager.onLocationChanged(lastLocation);
                }
            }
            //Log.e(" Categorie size "+category.toString()+"   ","   oyYeaaah, size of location_groups object ==> not yet"+category.getLocation_group_id());
            if(category.getLocation_group_id()>0 && !(""+category.getLocation_group_id()).isEmpty()){

                ((MainActivity) getActivity()).extras = new Bundle();
                ((MainActivity) getActivity()).extras.putInt("location_group_id", category.getLocation_group_id());

                Fragment mMapFragment = new MapV2Fragment();
                mMapFragment.setArguments(((MainActivity)getActivity()).extras);
                bodyFragment = "MapV2Fragment";
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
                fragmentTransaction.addToBackStack(null).commit();

                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);

            }else if((isTablet && category.getDisplay_type().equalsIgnoreCase("coverflow")) || (!isTablet && category.getDisplay_type_smartphone().equalsIgnoreCase("coverflow")) ){
                DesignCategoryFlowFragment designCategoryFlowFragment = DesignCategoryFlowFragment.create(category);
                bodyFragment = "DesignCategoryFlowFragment";
                extras.putInt("category_id", category.getId());
                designCategoryFlowFragment
                        .setArguments(((MainActivity) getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, designCategoryFlowFragment)
                        .addToBackStack(bodyFragment).commit();
            }else{

                RealmList<Category> categories = category.getChildren_categories();//getCategories();
                if (categories!=null && categories.size()>0){
                    Category cat = categories.iterator().next();

                    if (categories.size() == 1) {
                        //Log.e(" Categorie size "+categories.size()+" ==  1  ","   oYeaaah ");
                        openCategory(cat);
                        return;
                    }
                    if(isTablet) {
                        if(category.getDisplay_type().equals("column_scroll")){
                            Section section = null;
                           /* RealmResults<Section> sections = realm.where(Section.class).findAll();

                            Iterator<Section> iterator = sections.iterator();
                            while (iterator.hasNext()) {
                               Section s = iterator.next();
                                for (int i = 0; i <s.getCategories().size() ; i++) {
                                    int id = s.getCategories().get(i).getId();
                                    if(id == category.getId())
                                    section = realm.where(Section.class).equalTo("id",s.getId()).findFirst();
                                 }
                            }*/
                           section = CategoryTo.Sections(category,getApplicationContext());
                          /**********/
                            CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(category.getTitle(), categories);//new CollumnScrollFragment();
                            bodyFragment = "CollumnScrollFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Section_id", section.getId_s());
                            extras.putInt("index", 0);
                            collumnScrollFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, collumnScrollFragment)
                                    .addToBackStack(bodyFragment).commit();

                        }
                        else if(category.getDisplay_type().compareTo("horizontal_index") == 0) {
                            DirectoryFragment directoryFragment = new DirectoryFragment();

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "DirectoryFragment";

                            directoryFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, directoryFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type().compareTo("horizontal") == 0) {

                            ListOfPageFragment listOfPageFragment = ListOfPageFragment.newInstance(category, colors);

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "ListOfPageFragment";

                            listOfPageFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,listOfPageFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type().compareTo("split_list") == 0  && category.isGroup_children_categories() && category.getChildren_categories().size() > 0){
                            //					CategoryFragment.isNewDesign = true;
                            //					Log.e(" Condition v?rifi? ", "  avec category.getParentCategory().getGroup_children_categories : " +category.getDisplay_type());
                            //bodyFragment = "SplitListCategoryFragment";
                            if(((MainActivity) getActivity()).extras == null)
                                ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id_", category.getId());
                            SplitFilteredCategoriesFragment groupedCategoriesFragment = new SplitFilteredCategoriesFragment();
                            //((MainActivity) getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";

                            groupedCategoriesFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                    .addToBackStack(null).commit();

                        }
                        else if(category.getDisplay_type().compareTo("split_list") == 0  && !category.isGroup_children_categories()){
                            //					CategoryFragment.isNewDesign = true;
                            //					Log.e(" Condition v?rifi? ", "  avec category.getDisplay_type() : " +category.getDisplay_type());
                            //bodyFragment = "SplitListCategoryFragment";
                            if(((MainActivity) getActivity()).extras == null)
                                ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).extras.putString("split_list", category.getDisplay_type());

                            if(category.getAlphabetic_index().contains("true")){
                                SplitListCategoryFragment_ splitListCategorieFragment = new SplitListCategoryFragment_();
                                ((MainActivity) getActivity()).extras.putBoolean("isSorted", true);
                                splitListCategorieFragment
                                        .setArguments(((MainActivity) getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                        .addToBackStack(null).commit();
                            }else{

                                SplitListCategoryFragment splitListCategorieFragment = new SplitListCategoryFragment();
                                splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null).commit();
                            }

                        }
                        else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("fullscreen")) {



                            if (category.getChildren_categories().size() == 0) {
                                RealmList<Child_pages> pages = category.getChildren_pages();
                                if(!pages.isEmpty()) {
                                    Child_pages page = pages.iterator().next();
                                    extras = new Bundle();
                                    extras.putInt("page_id", page.getId_cp());
                                    PagesFragment pagesFragment = new PagesFragment();
                                    bodyFragment = "PagesFragment";
                                    pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container,
                                                    pagesFragment).addToBackStack(null)
                                            .commit();
                                }


                            }else{


                                FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                bodyFragment = "FullscreenCategoryFragment";
                                ((MainActivity)getActivity()).extras = new Bundle();
                                extras.putInt("Category_id", category.getId());
                                categoryFragment.setArguments(extras);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                            }

                        }
                        //List<Category> cats = new ArrayList<Category>(category.getCategories1());
                        else if(category.isGroup_children_categories() && category.getChildren_categories().size() > 0 &category.getChildren_categories().get(0).getDisplay_type().compareTo("split_list") == 0){
                            bodyFragment = "SplitFilteredCategoriesFragment";
                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id_", category.getId());
                            ((MainActivity) getActivity()).extras.putString("split_list", "split_list");
                            SplitFilteredCategoriesFragment splitListCategorieFragment = new SplitFilteredCategoriesFragment();
                            splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                             /*.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)*/.addToBackStack(null).commit();


                        }else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("grid")) {
                            //Log.e(" section.getDisplay_type().equals(grid) : "+category.getDisplay_type().equals("grid"), " CategorieGridFragment will be called ");
                            CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
                            bodyFragment = "CategorieGridFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Category_id", category.getId());
                            Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                            if(section != null)
                                extras.putInt("Section_id", section.getId_s());
                            //extras.putInt("Section_id", category.getId_section());
                            coategorieGridFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();

                        }
                        else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("collection")) {
                            //					 try {
                            //						List<Locations_group> groups = appController.getLocationGroupDao().queryForEq("id",category.getId_category()/* 182*/);
                            //						if (groups.size()>0) {
                            //							Locations_group group = groups.get(0);
                            //						}
                            //					} catch (SQLException e) {
                            //						// TODO Auto-generated catch block
                            //						e.printStackTrace();
                            //					}
                            CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                            bodyFragment = "CollectionGridFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Category_id", category.getId());
                            Section section = CategoryTo.Sections(category,getApplicationContext());/* category.getSection();*/
                            if(section != null)
                                extras.putInt("Section_id", section.getId_s());
                            collectionGridFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                            //					openCategory(cat);
                        }else {

                            if (cat.getDisplay_type()!=null && cat.getDisplay_type().equals("bottom_slider")) {
                                SliderCategoryFragment_ fragment = new SliderCategoryFragment_();
                                ((MainActivity)getActivity()).bodyFragment = "SliderCategoryFragment";
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments
                                ((MainActivity)getActivity()).extras = new Bundle();
                                ((MainActivity)getActivity()).extras.putInt("Category_id", category.getId());
                                fragment.setArguments(((MainActivity)getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                            }else if (category.isGroup_children_categories()) {
                                /** condition ? v?rifier avec driss **/

                                //						Log.e(" OpenCategory ==> category.getChildren_pages() ",""+category.getChildren_pages().size());


                                {
                                    FilteredCategoriesFragment groupedCategoriesFragment = new FilteredCategoriesFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "FilteredCategoriesFragment";

                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());

                                    groupedCategoriesFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();

                                }
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments

                            }
                            else {


                                CategoryFragment categoryFragment = new CategoryFragment();
                                ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments
                                ((MainActivity) getActivity()).extras = new Bundle();
                                ((MainActivity) getActivity()).extras.putInt(
                                        "Category_id", category.getId());
                                categoryFragment
                                        .setArguments(((MainActivity) getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();

                            }
                        }
                    }else {

                        if(category.getDisplay_type().equals("column_scroll")){
                            Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                            CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(category.getTitle(), categories);//new CollumnScrollFragment();
                            bodyFragment = "CollumnScrollFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Section_id", section.getId_s());
                            extras.putInt("index", 0);
                            collumnScrollFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, collumnScrollFragment)
                                    .addToBackStack(bodyFragment).commit();
                        }else if(category.getDisplay_type_smartphone().compareTo("horizontal_index") == 0) {
                            DirectoryFragment directoryFragment = new DirectoryFragment();

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "DirectoryFragment";

                            directoryFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, directoryFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type_smartphone().compareTo("horizontal") == 0) {

                            ListOfPageFragment listOfPageFragment = ListOfPageFragment.newInstance(category, colors);

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "ListOfPageFragment";

                            listOfPageFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,listOfPageFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type().compareTo("split_list") == 0  && category.isGroup_children_categories() && category.getChildren_categories().size() > 0){
                            //					CategoryFragment.isNewDesign = true;
                            //					Log.e(" Condition v?rifi? ", "  avec category.getParentCategory().getGroup_children_categories : " +category.getDisplay_type());
                            bodyFragment = "SplitFilteredCategoriesFragment";
                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id_", category.getId());
                            SplitFilteredCategoriesFragment groupedCategoriesFragment = new SplitFilteredCategoriesFragment();
                            //((MainActivity) getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";

                            groupedCategoriesFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                    .addToBackStack(null).commit();




                        }
                        else if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("collection")) {
                            //					 try {
                            //						List<Locations_group> groups = appController.getLocationGroupDao().queryForEq("id",category.getId_category()/* 182*/);
                            //						if (groups.size()>0) {
                            //							Locations_group group = groups.get(0);
                            //						}
                            //					} catch (SQLException e) {
                            //						// TODO Auto-generated catch block
                            //						e.printStackTrace();
                            //					}
                            CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                            bodyFragment = "CollectionGridFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Category_id", category.getId());
                            Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                            if(section != null)
                                extras.putInt("Section_id", section.getId_s());
                            collectionGridFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                            //					openCategory(cat);
                        }else {

                            if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("fullscreen")) {

                                if (category.getChildren_categories().size() == 0) {
                                    Collection<Child_pages> pages = category.getChildren_pages();
                                    if(!pages.isEmpty()) {
                                        Child_pages page = pages.iterator().next();
                                        extras = new Bundle();
                                        extras.putInt("page_id", page.getId_cp());
                                        PagesFragment pagesFragment = new PagesFragment();
                                        bodyFragment = "PagesFragment";
                                        pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container,
                                                        pagesFragment).addToBackStack(null)
                                                .commit();
                                    }


                                }else{


                                    FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                    bodyFragment = "FullscreenCategoryFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    extras.putInt("Category_id", category.getId());
                                    categoryFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                                }

                            }else if (category.isGroup_children_categories()) {
                                /** condition ? v?rifier avec driss **/

                                //						Log.e(" OpenCategory ==> category.getChildren_pages() ",""+category.getChildren_pages().size());


                                {
                                    FilteredCategoriesFragment groupedCategoriesFragment = new FilteredCategoriesFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "FilteredCategoriesFragment";

                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());

                                    groupedCategoriesFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();

                                }
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments

                            }
                            else {

                                if(category.getDisplay_type_smartphone()!=null && category.getAlphabetic_index().compareTo("true") == 0 && category.getDisplay_type_smartphone().equals("list")) {
                                    CategoryIndexFragment categoryIndexFragment = new CategoryIndexFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "CategoryIndexFragment";
                                    // In case this activity was started with special instructions from an Intent,
                                    // pass the Intent's extras to the fragment as arguments
                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());
                                    categoryIndexFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryIndexFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                }else {
                                    CategoryFragment categoryFragment = new CategoryFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                    // In case this activity was started with special instructions from an Intent,
                                    // pass the Intent's extras to the fragment as arguments
                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId()); /// restauration : 2516
                                    categoryFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                }
                            }
                        }


                    }

                }else {

                    Collection<Child_pages> listPages;

                    listPages = category.getChildren_pages();
                    //Log.e(" listPages   ==> "+listPages," <==========>  listPages.sizee : "+listPages.size());

                    //				try {
                    //					listPages = appController.getChildPageDao().queryForEq("id", category.getId());
                    //				} catch (SQLException e1) {
                    //					// TODO Auto-generated catch block
                    //					e1.printStackTrace();
                    //				}
                    //				 Log.e(" listPages   ==> "+listPages," <==========>  listPages.sizee : "+listPages.size());

                    //				 Collection<Child_pages> listPages = category.getChildren_pages1();
                    //				 Log.e(" OpenCategory Methode else ==> else if"," <==========>  category : "+category.getCommunTitle()+"  category.getDisplay_type() :  "+category.getDisplay_type());

                    if (listPages.size()>0) {
                        if (listPages.size()==1) {
                            Child_pages page = listPages.iterator().next();

                            if(page != null && page.getExtra_fields() != null && page.getExtra_fields().getAuto_open_section_id() != null && !page.getExtra_fields().getAuto_open_section_id().isEmpty()){
                                Section section = null;
                                // List<Section> sections = appController.getSectionsDao().queryForEq("id", page.getExtraField().getAuto_open_section_id());
                                RealmResults<Section> sections = realm.where(Section.class).equalTo("id", Integer.parseInt(page.getExtra_fields().getAuto_open_section_id())).findAll();
                                if (sections.size() > 0) {
                                    section = sections.get(0);
                                }
                                if (section != null) {

                                    openSection(new Tab(false), section);

                                }
                            }
                            else
                                openChildPage(page,true);
                        }else {
                            Category parent = null;
                            if (category.getParentCategory() != null) {
                                parent = realm.where(Category.class).equalTo("id",category.getParentCategory().getId_c()).findFirst();//appController.getCategoryByIdDB(category.getParentCategory().getId_c());
                            }
                            if(isTablet) {
                                if (category.getDisplay_type()!=null && category.getDisplay_type().equals("fullscreen")) {

                                    if (category.getChildren_categories().size() == 0) {
                                        Collection<Child_pages> pages = category.getChildren_pages();
                                        if(!pages.isEmpty()) {
                                            Child_pages page = pages.iterator().next();
                                            extras = new Bundle();
                                            extras.putInt("page_id", page.getId_cp());
                                            PagesFragment pagesFragment = new PagesFragment();
                                            bodyFragment = "PagesFragment";
                                            pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                            getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_container,
                                                            pagesFragment).addToBackStack(null)
                                                    .commit();
                                        }


                                    }else{


                                        FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                        bodyFragment = "FullscreenCategoryFragment";
                                        ((MainActivity)getActivity()).extras = new Bundle();
                                        extras.putInt("Category_id", category.getId());
                                        categoryFragment.setArguments(extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                                    }
                                }

                                else if(category.getDisplay_type().compareTo("split_list") == 0  && (category.isGroup_children_categories() || (category.getChildren_categories().size() > 0 || (parent!= null && parent.isGroup_children_categories())))){

                                    //								Log.e(" Condition v?rifi? ", "  avec category.getDisplay_type() : " +category.getDisplay_type());

                                    //bodyFragment = "SplitListCategoryFragment";
                                    if(((MainActivity) getActivity()).extras == null)
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    ((MainActivity) getActivity()).extras.putString("split_list", category.getDisplay_type());

                                    if(category.getAlphabetic_index().contains("true")){
                                        SplitListCategoryFragment_ splitListCategorieFragment = new SplitListCategoryFragment_();
                                        ((MainActivity) getActivity()).extras.putBoolean("isSorted", true);
                                        splitListCategorieFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .addToBackStack(null).commit();
                                    }else{

                                        SplitListCategoryFragment splitListCategorieFragment = new SplitListCategoryFragment();
                                        splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null).commit();
                                    }



                                }
                                else if(category.getDisplay_type().compareTo("split_list") == 0){
                                    //								CategoryFragment.isNewDesign = true;

                                    //bodyFragment = "SplitListCategoryFragment";
                                    if(((MainActivity) getActivity()).extras == null)
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    ((MainActivity) getActivity()).extras.putString("split_list", category.getDisplay_type());

                                    if(category.getAlphabetic_index().contains("true")){
                                        SplitListCategoryFragment_ splitListCategorieFragment = new SplitListCategoryFragment_();
                                        ((MainActivity) getActivity()).extras.putBoolean("isSorted", true);
                                        splitListCategorieFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .addToBackStack(null).commit();
                                    }else{

                                        SplitListCategoryFragment splitListCategorieFragment = new SplitListCategoryFragment();
                                        splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null).commit();
                                    }

                                    //								}


                                }
                                else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("collection")) {

                                    CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                                    bodyFragment = "CollectionGridFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    extras.putInt("Category_id", category.getId());
                                    Section section = CategoryTo.Sections(category,getApplicationContext()); /*category.getSection();*/
                                    if(section != null)
                                        extras.putInt("Section_id", section.getId_s());
                                    collectionGridFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                                }
                                else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("grid")) {
                                    CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
                                    bodyFragment = "CategorieGridFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());

                                    Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                                    if(section != null)
                                        extras.putInt("Section_id", section.getId_s());

                                    coategorieGridFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();

                                }
                                else if(category.getDisplay_type().compareTo("horizontal_index") == 0) {
                                    DirectoryFragment directoryFragment = new DirectoryFragment();

                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    ((MainActivity) getActivity()).bodyFragment = "DirectoryFragment";

                                    directoryFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, directoryFragment)
                                            .addToBackStack(null).commit();
                                }else if(category.getDisplay_type().compareTo("horizontal") == 0) {

                                    ListOfPageFragment listOfPageFragment = ListOfPageFragment.newInstance(category, colors);
                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    ((MainActivity) getActivity()).bodyFragment = "ListOfPageFragment";

                                    listOfPageFragment.setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,listOfPageFragment)
                                            .addToBackStack(null).commit();
                                }else {

                                    CategoryFragment categoryFragment = new CategoryFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                    // In case this activity was started with special instructions from an Intent,
                                    // pass the Intent's extras to the fragment as arguments
                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    categoryFragment.setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();


                                }
                            }else {


                                if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("fullscreen")) {

                                    if (category.getChildren_categories().size() == 0) {
                                        Collection<Child_pages> pages = category.getChildren_pages();
                                        if(!pages.isEmpty()) {
                                            Child_pages page = pages.iterator().next();
                                            extras = new Bundle();
                                            extras.putInt("page_id", page.getId_cp());
                                            PagesFragment pagesFragment = new PagesFragment();
                                            bodyFragment = "PagesFragment";
                                            pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                            getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_container,
                                                            pagesFragment).addToBackStack(null)
                                                    .commit();
                                        }


                                    }else{


                                        FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                        bodyFragment = "FullscreenCategoryFragment";
                                        ((MainActivity)getActivity()).extras = new Bundle();
                                        extras.putInt("Category_id", category.getId());
                                        categoryFragment.setArguments(extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                                    }
                                }
                                else if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("collection")) {

                                    CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                                    bodyFragment = "CollectionGridFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    extras.putInt("Category_id", category.getId());
                                    Section section =CategoryTo.Sections(category,getApplicationContext()); /*category.getSection();*/
                                    if(section != null)
                                        extras.putInt("Section_id", section.getId_s());
                                    collectionGridFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                                }

                                else {
                                    if(category.getDisplay_type_smartphone()!=null && category.getAlphabetic_index().compareTo("true") == 0 && category.getDisplay_type_smartphone().equals("list")) {
                                        CategoryIndexFragment categoryIndexFragment = new CategoryIndexFragment();
                                        ((MainActivity) getActivity()).bodyFragment = "CategoryIndexFragment";
                                        // In case this activity was started with special instructions from an Intent,
                                        // pass the Intent's extras to the fragment as arguments
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                        ((MainActivity) getActivity()).extras.putInt(
                                                "Category_id", category.getId());
                                        categoryIndexFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryIndexFragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                    }else {
                                        CategoryFragment categoryFragment = new CategoryFragment();
                                        ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                        // In case this activity was started with special instructions from an Intent,
                                        // pass the Intent's extras to the fragment as arguments
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                        ((MainActivity) getActivity()).extras.putInt(
                                                "Category_id", category.getId());
                                        categoryFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                    }

                                }


                            }
                        }
                    }


                }

            }


        }
    }

    public void openCategory_(Category category) {

        if (category != null) {

            if(category.getParameters().contains("geolock")){
                if (lastLocation != null) {
                    String[] spliter = category.getParameters().split("[geolock\\,\\[\\]]+");
                    double lat, lon, rayon;
                    lat = lon = rayon = 0;

                    if(spliter.length == 4){
                        lat = Double.parseDouble(spliter[1]);
                        lon = Double.parseDouble(spliter[2]);
                        rayon = Double.parseDouble(spliter[3]);
                    }
                    Location geoLocklocation = new Location("geoLock");
                    geoLocklocation.setLatitude(lat);
                    geoLocklocation.setLongitude(lon);

                    if(lastLocation.distanceTo(geoLocklocation) > rayon){
                        //Toast.makeText(getApplicationContext(), getString(R.string.geoLockMsg), Toast.LENGTH_LONG).show();
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.geoLockMsg));
                        // Setting alert dialog icon
                        alertDialog.setIcon(android.R.drawable.ic_dialog_info);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                        return;
                    }
                    //locationManager.onLocationChanged(lastLocation);
                }
            }

            //Log.e(" Categorie size "+category.toString()+"   ","   oyYeaaah, size of location_groups object ==> not yet"+category.getLocation_group_id());
            if(category.getLocation_group_id() >0 && !(""+category.getLocation_group_id()).isEmpty()){

                ((MainActivity) getActivity()).extras = new Bundle();
                ((MainActivity) getActivity()).extras.putInt("location_group_id", category.getLocation_group_id());

                Fragment mMapFragment = new MapV2Fragment();
                mMapFragment.setArguments(((MainActivity)getActivity()).extras);
                bodyFragment = "MapV2Fragment";
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
                fragmentTransaction.addToBackStack(null).commit();

                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);

            }else if((isTablet && category.getDisplay_type().equalsIgnoreCase("coverflow")) || (!isTablet && category.getDisplay_type_smartphone().equalsIgnoreCase("coverflow")) ){
                DesignCategoryFlowFragment designCategoryFlowFragment = DesignCategoryFlowFragment.create(category);
                bodyFragment = "DesignCategoryFlowFragment";
                extras.putInt("category_id", category.getId());
                designCategoryFlowFragment
                        .setArguments(((MainActivity) getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, designCategoryFlowFragment)
                        .addToBackStack(bodyFragment).commit();
            }else{

                Collection<Category> categories = category.getChildren_categories();
                //			Log.e(" OpenCategory Methode else "," <==========>  category : "+category.getCommunTitle()+"  category.getDisplay_type() :  "+category.getDisplay_type());
                if (categories!=null && categories.size()>0) {
                    Category cat = categories.iterator().next();


                    if(isTablet) {
                        if(category.getDisplay_type().equals("column_scroll")){
                            CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(category.getTitle(), categories);//new CollumnScrollFragment();
                            bodyFragment = "CollumnScrollFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("index", 0);
                            collumnScrollFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, collumnScrollFragment)
                                    .addToBackStack(bodyFragment).commit();
                        }else if(category.getDisplay_type().compareTo("horizontal_index") == 0) {
                            DirectoryFragment directoryFragment = new DirectoryFragment();

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "DirectoryFragment";

                            directoryFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, directoryFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type().compareTo("horizontal") == 0) {

                            ListOfPageFragment listOfPageFragment = ListOfPageFragment.newInstance(category, colors);

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "ListOfPageFragment";

                            listOfPageFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,listOfPageFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type().compareTo("split_list") == 0  && category.isGroup_children_categories() && category.getChildren_categories().size() > 0){
                            //					CategoryFragment.isNewDesign = true;
                            //					Log.e(" Condition v?rifi? ", "  avec category.getParentCategory().getGroup_children_categories : " +category.getDisplay_type());
                            //bodyFragment = "SplitListCategoryFragment";
                            if(((MainActivity) getActivity()).extras == null)
                                ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id_", category.getId());
                            SplitFilteredCategoriesFragment groupedCategoriesFragment = new SplitFilteredCategoriesFragment();
                            //((MainActivity) getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";

                            groupedCategoriesFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                    .addToBackStack(null).commit();




                        }
                        else if(category.getDisplay_type().compareTo("split_list") == 0  && !category.isGroup_children_categories()){
                            //					CategoryFragment.isNewDesign = true;
                            //					Log.e(" Condition v?rifi? ", "  avec category.getDisplay_type() : " +category.getDisplay_type());
                            //bodyFragment = "SplitListCategoryFragment";
                            if(((MainActivity) getActivity()).extras == null)
                                ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).extras.putString("split_list", category.getDisplay_type());


                            if(category.getAlphabetic_index().contains("true")){
                                SplitListCategoryFragment_ splitListCategorieFragment = new SplitListCategoryFragment_();
                                ((MainActivity) getActivity()).extras.putBoolean("isSorted", true);
                                splitListCategorieFragment
                                        .setArguments(((MainActivity) getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                        .addToBackStack(null).commit();
                            }else{

                                SplitListCategoryFragment splitListCategorieFragment = new SplitListCategoryFragment();
                                splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null).commit();
                            }

                        }else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("fullscreen")) {

                            if (category.getChildren_categories().size() == 0) {
                                Collection<Child_pages> pages = category.getChildren_pages();
                                if(!pages.isEmpty()) {
                                    Child_pages page = pages.iterator().next();
                                    extras = new Bundle();
                                    extras.putInt("page_id", page.getId_cp());
                                    PagesFragment pagesFragment = new PagesFragment();
                                    bodyFragment = "PagesFragment";
                                    pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container,
                                                    pagesFragment).addToBackStack(null)
                                            .commit();
                                }


                            }else{


                                FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                bodyFragment = "FullscreenCategoryFragment";
                                ((MainActivity)getActivity()).extras = new Bundle();
                                extras.putInt("Category_id", category.getId());
                                categoryFragment.setArguments(extras);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                            }
                        }
                        //List<Category> cats = new ArrayList<Category>(category.getCategories1());
                        else if(category.isGroup_children_categories() && category.getChildren_categories().size() > 0 && category.getChildren_categories().get(0).getDisplay_type().compareTo("split_list") == 0){
                            bodyFragment = "SplitFilteredCategoriesFragment";
                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id_", category.getId());
                            ((MainActivity) getActivity()).extras.putString("split_list", "split_list");
                            SplitFilteredCategoriesFragment splitListCategorieFragment = new SplitFilteredCategoriesFragment();
                            splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                             /*.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)*/.addToBackStack(null).commit();


                        }else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("grid")) {
                            //Log.e(" section.getDisplay_type().equals(grid) : "+category.getDisplay_type().equals("grid"), " CategorieGridFragment will be called ");
                            CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
                            bodyFragment = "CategorieGridFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Category_id", category.getId());
                            Section section = CategoryTo.Sections(category,getApplicationContext()); /*category.getSection();*/
                            if(section != null)
                                extras.putInt("Section_id", section.getId_s());
                            //extras.putInt("Section_id", category.getId_section());
                            coategorieGridFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();

                        }

                        else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("collection")) {
                            //					 try {
                            //						List<Locations_group> groups = appController.getLocationGroupDao().queryForEq("id",category.getId_category()/* 182*/);
                            //						if (groups.size()>0) {
                            //							Locations_group group = groups.get(0);
                            //						}
                            //					} catch (SQLException e) {
                            //						// TODO Auto-generated catch block
                            //						e.printStackTrace();
                            //					}
                            CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                            bodyFragment = "CollectionGridFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Category_id", category.getId());
                            Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                            if(section != null)
                                extras.putInt("Section_id", section.getId());
                            collectionGridFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                            //					openCategory(cat);
                        }else {

                            if (cat.getDisplay_type()!=null && cat.getDisplay_type().equals("bottom_slider")) {
                                SliderCategoryFragment_ fragment = new SliderCategoryFragment_();
                                ((MainActivity)getActivity()).bodyFragment = "SliderCategoryFragment";
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments
                                ((MainActivity)getActivity()).extras = new Bundle();
                                ((MainActivity)getActivity()).extras.putInt("Category_id", category.getId());
                                fragment.setArguments(((MainActivity)getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                            }else if (category.isGroup_children_categories()) {
                                {
                                    FilteredCategoriesFragment groupedCategoriesFragment = new FilteredCategoriesFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "FilteredCategoriesFragment";

                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());

                                    groupedCategoriesFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();

                                }
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments

                            }
                            else {


                                CategoryFragment_ categoryFragment = new CategoryFragment_();
                                ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments
                                ((MainActivity) getActivity()).extras = new Bundle();
                                ((MainActivity) getActivity()).extras.putInt(
                                        "Category_id", category.getId());
                                categoryFragment
                                        .setArguments(((MainActivity) getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();

                            }
                        }
                    }else {

                        if(category.getDisplay_type().equals("column_scroll")){
                            CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(category.getTitle(), categories);//new CollumnScrollFragment();
                            bodyFragment = "CollumnScrollFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("index", 0);
                            collumnScrollFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, collumnScrollFragment)
                                    .addToBackStack(bodyFragment).commit();
                        }else if(category.getDisplay_type_smartphone().compareTo("horizontal_index") == 0) {
                            DirectoryFragment directoryFragment = new DirectoryFragment();

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "DirectoryFragment";

                            directoryFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, directoryFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type_smartphone().compareTo("horizontal") == 0) {

                            ListOfPageFragment listOfPageFragment = ListOfPageFragment.newInstance(category, colors);

                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                            ((MainActivity) getActivity()).bodyFragment = "ListOfPageFragment";

                            listOfPageFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,listOfPageFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if(category.getDisplay_type().compareTo("split_list") == 0  && category.isGroup_children_categories() && category.getChildren_categories().size() > 0){
                            //					CategoryFragment.isNewDesign = true;
                            //					Log.e(" Condition v?rifi? ", "  avec category.getParentCategory().getGroup_children_categories : " +category.getDisplay_type());
                            bodyFragment = "SplitFilteredCategoriesFragment";
                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putInt("Category_id_", category.getId());
                            SplitFilteredCategoriesFragment groupedCategoriesFragment = new SplitFilteredCategoriesFragment();
                            //((MainActivity) getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";

                            groupedCategoriesFragment
                                    .setArguments(((MainActivity) getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                    .addToBackStack(null).commit();
                        }
                        else if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("collection")) {
                            //					 try {
                            //						List<Locations_group> groups = appController.getLocationGroupDao().queryForEq("id",category.getId_category()/* 182*/);
                            //						if (groups.size()>0) {
                            //							Locations_group group = groups.get(0);
                            //						}
                            //					} catch (SQLException e) {
                            //						// TODO Auto-generated catch block
                            //						e.printStackTrace();
                            //					}
                            CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                            bodyFragment = "CollectionGridFragment";
                            ((MainActivity)getActivity()).extras = new Bundle();
                            extras.putInt("Category_id", category.getId());
                            Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                            if(section != null)
                                extras.putInt("Section_id", section.getId_s());
                            collectionGridFragment.setArguments(extras);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                            //					openCategory(cat);
                        }else {

                            if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("fullscreen")) {
                                if (category.getChildren_categories().size() == 0) {
                                    Collection<Child_pages> pages = category.getChildren_pages();
                                    if(!pages.isEmpty()) {
                                        Child_pages page = pages.iterator().next();
                                        extras = new Bundle();
                                        extras.putInt("page_id", page.getId_cp());
                                        PagesFragment pagesFragment = new PagesFragment();
                                        bodyFragment = "PagesFragment";
                                        pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container,
                                                        pagesFragment).addToBackStack(null)
                                                .commit();
                                    }


                                }else{


                                    FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                    bodyFragment = "FullscreenCategoryFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    extras.putInt("Category_id", category.getId());
                                    categoryFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                                }
                            }else if (category.isGroup_children_categories()) {
                                /** condition ? v?rifier avec driss **/

                                //						Log.e(" OpenCategory ==> category.getChildren_pages() ",""+category.getChildren_pages().size());


                                {
                                    FilteredCategoriesFragment groupedCategoriesFragment = new FilteredCategoriesFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "FilteredCategoriesFragment";

                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());

                                    groupedCategoriesFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,groupedCategoriesFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();

                                }
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments

                            }
                            else {

                                if(category.getDisplay_type_smartphone()!=null && category.getAlphabetic_index().compareTo("true") == 0 && category.getDisplay_type_smartphone().equals("list")) {
                                    CategoryIndexFragment categoryIndexFragment = new CategoryIndexFragment();
                                    ((MainActivity) getActivity()).bodyFragment = "CategoryIndexFragment";
                                    // In case this activity was started with special instructions from an Intent,
                                    // pass the Intent's extras to the fragment as arguments
                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());
                                    categoryIndexFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryIndexFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                }else {
                                    CategoryFragment_ categoryFragment = new CategoryFragment_();
                                    ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                    // In case this activity was started with special instructions from an Intent,
                                    // pass the Intent's extras to the fragment as arguments
                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());
                                    categoryFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                }
                            }
                        }


                    }

                }else {

                    Collection<Child_pages> listPages;

                    listPages = category.getChildren_pages();
                    //Log.e(" listPages   ==> "+listPages," <==========>  listPages.sizee : "+listPages.size());

                    //				try {
                    //					listPages = appController.getChildPageDao().queryForEq("id", category.getId());
                    //				} catch (SQLException e1) {
                    //					// TODO Auto-generated catch block
                    //					e1.printStackTrace();
                    //				}
                    //				 Log.e(" listPages   ==> "+listPages," <==========>  listPages.sizee : "+listPages.size());

                    //				 Collection<Child_pages> listPages = category.getChildren_pages1();
                    //				 Log.e(" OpenCategory Methode else ==> else if"," <==========>  category : "+category.getCommunTitle()+"  category.getDisplay_type() :  "+category.getDisplay_type());

                    if (listPages.size()>0) {
                        if (listPages.size()==1) {
                            Child_pages page = listPages.iterator().next();

                            if(page != null && page.getExtra_fields() != null && page.getExtra_fields().getAuto_open_section_id() != null && !page.getExtra_fields().getAuto_open_section_id().isEmpty()){
                                Section section = null;
                                List<Section> sections = realm.where(Section.class).equalTo("id", page.getExtra_fields().getAuto_open_section_id()).findAll();//appController.getSectionsDao().queryForEq("id", page.getExtraField().getAuto_open_section_id());
                                if (sections.size() > 0) {
                                    section = sections.get(0);
                                }
                                if (section != null) {

                                    openSection(new Tab(false), section);

                                }
                            }
                            else{
                                CategoryFragment_ categoryFragment = new CategoryFragment_();
                                ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments
                                ((MainActivity) getActivity()).extras = new Bundle();
                                ((MainActivity) getActivity()).extras.putInt(
                                        "Category_id", page.getCategory_id());
                                categoryFragment
                                        .setArguments(((MainActivity) getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                            }
                        }else {
                            Category parent = null;
                            if (category.getParentCategory() != null) {
                                parent = realm.where(Category.class).equalTo("id",category.getParentCategory().getId_c()).findFirst();
                                //appController.getCategoryByIdDB(category.getParentCategory().getId_category());
                            }
                            if(isTablet) {
                                if (category.getDisplay_type()!=null && category.getDisplay_type().equals("fullscreen")) {

                                    if (category.getChildren_categories().size() == 0) {
                                        Collection<Child_pages> pages = category.getChildren_pages();
                                        if(!pages.isEmpty()) {
                                            Child_pages page = pages.iterator().next();
                                            extras = new Bundle();
                                            extras.putInt("page_id", page.getId_cp());
                                            PagesFragment pagesFragment = new PagesFragment();
                                            bodyFragment = "PagesFragment";
                                            pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                            getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_container,
                                                            pagesFragment).addToBackStack(null)
                                                    .commit();
                                        }


                                    }else{


                                        FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                        bodyFragment = "FullscreenCategoryFragment";
                                        ((MainActivity)getActivity()).extras = new Bundle();
                                        extras.putInt("Category_id", category.getId());
                                        categoryFragment.setArguments(extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                                    }
                                }

                                else if(category.getDisplay_type().compareTo("split_list") == 0  && (category.isGroup_children_categories() || (category.getChildren_categories().size() > 0 || (parent!= null && parent.isGroup_children_categories())))){

                                    //								Log.e(" Condition v?rifi? ", "  avec category.getDisplay_type() : " +category.getDisplay_type());

                                    //bodyFragment = "SplitListCategoryFragment";
                                    if(((MainActivity) getActivity()).extras == null)
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    ((MainActivity) getActivity()).extras.putString("split_list", category.getDisplay_type());

                                    if(category.getAlphabetic_index().contains("true")){
                                        SplitListCategoryFragment_ splitListCategorieFragment = new SplitListCategoryFragment_();
                                        ((MainActivity) getActivity()).extras.putBoolean("isSorted", true);
                                        splitListCategorieFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .addToBackStack(null).commit();
                                    }else{

                                        SplitListCategoryFragment splitListCategorieFragment = new SplitListCategoryFragment();
                                        splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null).commit();
                                    }



                                }
                                else if(category.getDisplay_type().compareTo("split_list") == 0){
                                    //								CategoryFragment.isNewDesign = true;

                                    //bodyFragment = "SplitListCategoryFragment";
                                    if(((MainActivity) getActivity()).extras == null)
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    ((MainActivity) getActivity()).extras.putString("split_list", category.getDisplay_type());

                                    if(category.getAlphabetic_index().contains("true")){
                                        SplitListCategoryFragment_ splitListCategorieFragment = new SplitListCategoryFragment_();
                                        ((MainActivity) getActivity()).extras.putBoolean("isSorted", true);
                                        splitListCategorieFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .addToBackStack(null).commit();
                                    }else{

                                        SplitListCategoryFragment splitListCategorieFragment = new SplitListCategoryFragment();
                                        splitListCategorieFragment.setArguments(((MainActivity) getActivity()).extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, splitListCategorieFragment)
                                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null).commit();
                                    }

                                    //								}


                                }
                                else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("collection")) {

                                    CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                                    bodyFragment = "CollectionGridFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    extras.putInt("Category_id", category.getId());
                                    Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                                    if(section != null)
                                        extras.putInt("Section_id", section.getId_s());
                                    collectionGridFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                                }
                                else if (category.getDisplay_type()!=null && category.getDisplay_type().equals("grid")) {
                                    CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
                                    bodyFragment = "CategorieGridFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                                    if(section != null)
                                        extras.putInt("Section_id", section.getId_s());
                                    coategorieGridFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();

                                }
                                else if(category.getDisplay_type().compareTo("horizontal_index") == 0) {
                                    DirectoryFragment directoryFragment = new DirectoryFragment();

                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt("Category_id", category.getId());
                                    ((MainActivity) getActivity()).bodyFragment = "DirectoryFragment";

                                    directoryFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, directoryFragment)
                                            .addToBackStack(null).commit();
                                }
                                else {

                                    CategoryFragment_ categoryFragment = new CategoryFragment_();
                                    ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                    // In case this activity was started with special instructions from an Intent,
                                    // pass the Intent's extras to the fragment as arguments
                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    ((MainActivity) getActivity()).extras.putInt(
                                            "Category_id", category.getId());
                                    categoryFragment
                                            .setArguments(((MainActivity) getActivity()).extras);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();


                                }
                            }else {


                                if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("fullscreen")) {

                                    if (category.getChildren_categories().size() == 0) {
                                        Collection<Child_pages> pages = category.getChildren_pages();
                                        if(!pages.isEmpty()) {
                                            Child_pages page = pages.iterator().next();
                                            extras = new Bundle();
                                            extras.putInt("page_id", page.getId_cp());
                                            PagesFragment pagesFragment = new PagesFragment();
                                            bodyFragment = "PagesFragment";
                                            pagesFragment.setArguments(((MainActivity) getActivity()).extras);
                                            getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_container,
                                                            pagesFragment).addToBackStack(null)
                                                    .commit();
                                        }


                                    }else{


                                        FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                                        bodyFragment = "FullscreenCategoryFragment";
                                        ((MainActivity)getActivity()).extras = new Bundle();
                                        extras.putInt("Category_id", category.getId());
                                        categoryFragment.setArguments(extras);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                                    }
                                }
                                else if (category.getDisplay_type_smartphone()!=null && category.getDisplay_type_smartphone().equals("collection")) {

                                    CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                                    bodyFragment = "CollectionGridFragment";
                                    ((MainActivity)getActivity()).extras = new Bundle();
                                    extras.putInt("Category_id", category.getId());
                                    Section section = CategoryTo.Sections(category,getApplicationContext());/*category.getSection();*/
                                    if(section != null)
                                        extras.putInt("Section_id", section.getId_s());
                                    collectionGridFragment.setArguments(extras);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

                                }

                                else {
                                    if(category.getDisplay_type_smartphone()!=null && category.getAlphabetic_index().compareTo("true") == 0 && category.getDisplay_type_smartphone().equals("list")) {
                                        CategoryIndexFragment categoryIndexFragment = new CategoryIndexFragment();
                                        ((MainActivity) getActivity()).bodyFragment = "CategoryIndexFragment";
                                        // In case this activity was started with special instructions from an Intent,
                                        // pass the Intent's extras to the fragment as arguments
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                        ((MainActivity) getActivity()).extras.putInt(
                                                "Category_id", category.getId());
                                        categoryIndexFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryIndexFragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                    }else {
                                        CategoryFragment_ categoryFragment = new CategoryFragment_();
                                        ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                                        // In case this activity was started with special instructions from an Intent,
                                        // pass the Intent's extras to the fragment as arguments
                                        ((MainActivity) getActivity()).extras = new Bundle();
                                        ((MainActivity) getActivity()).extras.putInt(
                                                "Category_id", category.getId());
                                        categoryFragment
                                                .setArguments(((MainActivity) getActivity()).extras);
                                        // Add the fragment to the 'fragment_container' FrameLayout
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoryFragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit();
                                    }

                                }


                            }
                        }
                    }


                }

            }


        }
    }

    /**
     * @param section
     */
    public void openSectionFromWebHomeView(Section section) {
        String type = section.getType();
        extras = new Bundle();
        // filter based on the type of the section
        if (type.equals("contents") || type.isEmpty()) { // type Contents or
            // empty sent!


            Collection<Category> categories = section.getCategories();
            if (categories!=null && categories.size()>0){
                Category cat = categories.iterator().next();
                if (categories.size() == 1) {
                    //Log.e(" Categorie size "+categories.size()+" ==  1  ","   oYeaaah ");
                    openCategory(cat);
                } else if(section.getDisplay_type()!=null && section.getDisplay_type().equals("column_scroll")){
                    CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(section.getName(), categories);//new CollumnScrollFragment();
                    bodyFragment = "CollumnScrollFragment";
                    ((MainActivity)getActivity()).extras = new Bundle();
                    extras.putInt("Section_id", section.getId_s());
                    extras.putInt("index", 0);
                    collumnScrollFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, collumnScrollFragment)
                            .addToBackStack(bodyFragment).commit();
                }
                else if (section.getDisplay_type()!=null && section.getDisplay_type().equals("fullscreen")){

                    FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                    bodyFragment = "FullscreenCategoryFragment";
                    ((MainActivity)getActivity()).extras = new Bundle();
                    extras.putInt("Section_id", section.getId_s());
                    categoryFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, categoryFragment)
                            .addToBackStack(null).commit();

                }else if (section.getDisplay_type()!=null && section.getDisplay_type().equals("collection")) {

                    CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                    bodyFragment = "CollectionGridFragment";
                    ((MainActivity)getActivity()).extras = new Bundle();
                    extras.putInt("Section_id", section.getId_s());
                    collectionGridFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, collectionGridFragment)
                            .addToBackStack(null).commit();

                }
                else if (isTablet && section.getDisplay_type()!=null && section.getDisplay_type().equals("grid")) {
                    //Log.e(" section.getDisplay_type().equals(grid) : "+section.getDisplay_type().equals("grid"), " CategorieGridFragment will be called ");
                    CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
                    bodyFragment = "CategorieGridFragment";
                    ((MainActivity)getActivity()).extras = new Bundle();
                    extras.putInt("Section_id", section.getId_s());
                    coategorieGridFragment.setArguments(extras);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();

                }else if (cat.getDisplay_type()!=null && cat.getDisplay_type().equals("bottom_slider") && isTablet) {
                    SliderCategoryFragment_ fragment = new SliderCategoryFragment_();
                    bodyFragment = "SliderCategoryFragment";
                    // In case this activity was started with special instructions from an Intent,
                    // pass the Intent's extras to the fragment as arguments
                    ((MainActivity)getActivity()).extras = new Bundle();
                    //extras.putInt("Section_id", section.getId_section());
                    extras.putInt("Category_id", cat.getId());
                    fragment.setArguments(extras);
                    // Add the fragment to the 'fragment_container' FrameLayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    CategoryFragment categoryFragment = new CategoryFragment();
                    bodyFragment = "CategoryFragment";
                    // In case this activity was started with special instructions from an Intent,
                    // pass the Intent's extras to the fragment as arguments
                    ((MainActivity)getActivity()).extras = new Bundle();
                    extras.putInt("Section_id", section.getId_s());
                    categoryFragment.setArguments(extras);
                    // Add the fragment to the 'fragment_container' FrameLayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                }
            }else {
                CategoryFragment categoryFragment = new CategoryFragment();
                bodyFragment = "CategoryFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                extras.putInt("Section_id", section.getId_s());
                categoryFragment.setArguments(extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
            }

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);


 /*                if(params.getHome_type().equals("web")){
                     WebHomeFragment webHomeFragment = new WebHomeFragment();
                     bodyFragment = "WebHomeFragment";
                     Bundle extra = new Bundle();
                     extra.putString("link", params.getWeb_home_test_url());
                     webHomeFragment.setArguments(extra);
                     getSupportFragmentManager()
                             .beginTransaction()
                             .replace(R.id.fragment_container,
                                     webHomeFragment).commit();
                 }else */
            if(section.getDisplay_type()!=null && section.getDisplay_type().equals("column_scroll")){
                CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(section.getName(), section.getCategories());//new CollumnScrollFragment();
                bodyFragment = "CollumnScrollFragment";
                ((MainActivity)getActivity()).extras = new Bundle();
                extras.putInt("Section_id", section.getId_s());
                extras.putInt("index", 0);
                collumnScrollFragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, collumnScrollFragment)
                        .addToBackStack(bodyFragment).commit();
            }
        }else if (type.equals("agenda")) {
            if(section.getDisplay_type().compareTo("month_grid") == 0) {

                //if(isTablet) {
                //CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
                CalendarHebdoPagerFragment calendarPagerFragment = new CalendarHebdoPagerFragment();
                extras = new Bundle();
                extras.putString("display_type", section.getDisplay_type());
                //extras.putInt("Section_id", section.getId_section());
                extras.putInt("index", 0);
                calendarPagerFragment.setArguments(extras);
                bodyFragment = "CalendarPagerFragment";
                MainActivity.this
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();
                //				}else {
                //					CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
                //					extras = new Bundle();
                //					extras.putString("display_type", section.getDisplay_type());
                //					//extras.putInt("Section_id", section.getId_section());
                //					extras.putInt("index", 0);
                //					calendarPagerFragment.setArguments(extras);
                //					bodyFragment = "CalendarPagerFragment";
                //					MainActivity.this
                //					.getSupportFragmentManager()
                //					.beginTransaction()
                //					.replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();
                //				}
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
            }else if(section.getDisplay_type().compareTo("month_calendar") == 0) {
                AgendaSplitFragment  agendaSplitFragment= new AgendaSplitFragment();
                extras = new Bundle();
                extras.putString("display_type", section.getDisplay_type());
                extras.putInt("Section_id", section.getId_s());
                extras.putInt("index", 0);
                agendaSplitFragment.setArguments(extras);
                bodyFragment = "AgendaSplitFragment";
                MainActivity.this
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, agendaSplitFragment).addToBackStack(null).commit();
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
            }else if (section.getDisplay_type().compareTo("list") == 0) {

                EventListFragment eventListFragment = new EventListFragment();
                extras = new Bundle();
                extras.putString("display_type", section.getDisplay_type());
                extras.putInt("Section_id", section.getId_s());
                //					extras.putInt("index", 0);
                eventListFragment.setArguments(extras);
                bodyFragment = "CalendarPagerFragment";
                MainActivity.this
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, eventListFragment).addToBackStack(null).commit();

            }


        }else if (type.equals("interactive_map")) {
            if(section.getMaps() != null){
                List<com.euphor.paperpad.Beans.interactiveMap.map> maps = new ArrayList<com.euphor.paperpad.Beans.interactiveMap.map>(section.getMaps());
                extras = new Bundle();
                extras.putInt("section_id", section.getId());
                bodyFragment = "InteractiveMapFragment";
                InteractiveMapFragment interactiveMapFragment = InteractiveMapFragment.create(maps);
                interactiveMapFragment.setArguments(extras);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, interactiveMapFragment).addToBackStack("InteractiveMapFragment").commit();
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
            }
        }else if (type.equals("web")) {

            WebViewFragment  webViewFragment= new WebViewFragment();
            extras = new Bundle();
            extras.putString("link", section.getRoot_url());
            webViewFragment.setArguments(extras);
            bodyFragment = "WebViewFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, webViewFragment).addToBackStack("WebViewFragment").commit();
            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        } else if (type.equals("contact")) { // Type Contacts

            ContactsFragment contactsFragment = new ContactsFragment();
            bodyFragment = "ContactsFragment";
            extras.putInt("Section_id", section.getId_s());
            contactsFragment.setArguments(extras);
            // Add the fragment to the 'fragment_container' FrameLayout
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, contactsFragment)
                    .addToBackStack(null).commit();
            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        } else if (type.equals("gallery")) { // Type Gallery


            extras.putInt("Section_id", section.getId_s());

            if(section.getGallery_design() != null && section.getGallery_design().equals("slider")){
                bodyFragment = "GalerySliderFragment";
                GalerySliderFragment galerySliderFragment = new GalerySliderFragment();
                galerySliderFragment.setArguments(extras);
                MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, galerySliderFragment).addToBackStack("GalerySliderFragment").commit();

            }else{
                bodyFragment = "AlbumsGridFragment";
                AlbumsGridFragment albumsGridFragment = new AlbumsGridFragment();
                albumsGridFragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumsGridFragment).addToBackStack("AlbumsGridFragment").commit();
            }

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }else if (type.equals("map")){

            MapV2Fragment mMapFragment = new MapV2Fragment();
            bodyFragment = "MapV2Fragment";

            extras.putInt("Section_id", section.getId());
            mMapFragment.setArguments(extras);

            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
            fragmentTransaction.addToBackStack(null).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }else if (type.equals("rss")) {
            List<Section> sections = realm.where(Section.class).equalTo("type", "rss").findAll();
            if(sections.size() >0) {
                Fragment rssFragment = new RSSFragment();
           // Fragment rssFragment = new RSSFragment();
            bodyFragment = "RSSFragment";
            extras.putInt("Section_id", section.getId_s());
            rssFragment.setArguments(extras);
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, rssFragment);
            fragmentTransaction.addToBackStack(null).commit();

            if (timer != null) {
                timer.cancel();
            }
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);
        }

        }else if (type.equals("video")) {
            bodyFragment = "PlayerYouTubeFrag";
            PlayerYouTubeFrag myFragment = PlayerYouTubeFrag.newInstance(section.getId_s());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment, bodyFragment).addToBackStack(bodyFragment).commit();
            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

             /*Intent intent = new Intent(getApplicationContext(), YoutubePlayerActivity.class);
             intent.putExtra("InfoActivity", infoActivity);
             startActivity(intent);
             overridePendingTransition(0, 0);*/

        }else if (type.equals("mybox")) {

            MyBoxGridFragment  myBoxGridFragment= new MyBoxGridFragment();
            extras = new Bundle();
            //				extras.putString("link", section.getRoot_url());
            myBoxGridFragment.setArguments(extras);
            bodyFragment = "MyBoxGridFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, myBoxGridFragment).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }else if (type.equals("agenda")) {

            AgendaFragment  agendaFrag= new AgendaFragment();
            extras = new Bundle();
            agendaFrag.setArguments(extras);
            bodyFragment = "AgendaFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, agendaFrag).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);
        }/*else if(type.compareTo("CalendarPagerFragment") == 0) {
             CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
             extras = new Bundle();
             extras.putString("display_type", "month_grid");
             //extras.putInt("Section_id", section.getId_section());
             extras.putInt("index", 0);
             calendarPagerFragment.setArguments(extras);
             bodyFragment = "CalendarPagerFragment";
             MainActivity.this
             .getSupportFragmentManager()
             .beginTransaction()
             .replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();

             if (timer != null) {timer.cancel();}
             findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
             findViewById(R.id.swipe_container).setVisibility(View.GONE);
         }else if(type.compareTo("AgendaSplitFragment") == 0) {
             AgendaSplitFragment  agendaSplitFragment= new AgendaSplitFragment();
             extras = new Bundle();
             extras.putString("display_type", "month_calendar");
             extras.putInt("Section_id", section.getId_section());
             extras.putInt("index", 0);
             agendaSplitFragment.setArguments(extras);
             bodyFragment = "AgendaSplitFragment";
             MainActivity.this
             .getSupportFragmentManager()
             .beginTransaction()
             .replace(R.id.fragment_container, agendaSplitFragment).addToBackStack(null).commit();

             if (timer != null) {timer.cancel();}
             findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
             findViewById(R.id.swipe_container).setVisibility(View.GONE);
         }*/else if (type.equals("survey")) {
            SurveyFragment  fragment= new SurveyFragment();
            extras = new Bundle();
            extras.putString("title", section.getTitle());
            fragment.setArguments(extras);
            bodyFragment = "SurveyFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        } else if (type.equals("dashboard") || type.equals("room_service") ) {
            params = realm.where(Parameters.class).findFirst();
            cd = new ConnectionDetector(getApplicationContext());

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                alert.showAlertDialog(MainActivity.this, getResources().getString(R.string.error_connecting),
                        getResources().getString(R.string.error_connecting_message), false);
                // stop executing code by return
            }else {
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
                //DownloadParseAsyncTask
                asyncTask = new DownloadParseAsyncTask(section.getId_s());
                String urlBase = Constants.PROD+"/api/application/dashboard/id/"+params.getId()+"/lang/"+wmbPreference.getString(Utils.LANG, "fr");
                asyncTask.execute(urlBase/*"http://192.168.1.15/dash/28dash.json"*/);
            }

        }else if (type.equals("radio")) {

            RadiosSectionFragment  fragment= new RadiosSectionFragment();
            setRadioCallback(fragment);
            extras = new Bundle();
            extras.putInt("Section_id", section.getId_s());
            fragment.setArguments(extras);
            bodyFragment = "RadiosSectionFragment";
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, bodyFragment).addToBackStack(bodyFragment).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }

        if (!(type.equals("dashboard") || type.equals("room_service"))){
            if(asyncTask != null)
                asyncTask.cancel(true);
        }
    }

    /**
     * @param tab
     * @param section
     */
    public void openSection(Tab tab, Section section) {
        String type = section.getType();
        extras = new Bundle();
        // filter based on the type of the section
        if (type.equals("contents") || type.isEmpty()) { // type Contents or
            // empty sent!
            if (!tab.getIsHomeGrid()) {

                RealmList<Category> categories = section.getCategories();
                if (categories!=null && categories.size()>0){
                    Category cat = categories.iterator().next();
                    if (categories.size() == 1) {
                        //Log.e(" Categorie size "+categories.size()+" ==  1  ","   oYeaaah ");
                        openCategory(cat);
                    } else if(section.getDisplay_type()!=null && section.getDisplay_type().equals("column_scroll")){
                        CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(section.getName(), categories);//new CollumnScrollFragment();
                        bodyFragment = "CollumnScrollFragment";
                        ((MainActivity)getActivity()).extras = new Bundle();
                        extras.putInt("Section_id", section.getId()); /* section **/
                        extras.putInt("index", 0);
                        collumnScrollFragment.setArguments(extras);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, collumnScrollFragment)
                                .addToBackStack(bodyFragment).commit();
                    }
                    else if (section.getDisplay_type()!=null && section.getDisplay_type().equals("fullscreen")){

                        FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
                        bodyFragment = "FullscreenCategoryFragment";
                        ((MainActivity)getActivity()).extras = new Bundle();
                        extras.putInt("Section_id", section.getId_s());
                        categoryFragment.setArguments(extras);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, categoryFragment)
                                .addToBackStack(null).commit();

                    }else if (section.getDisplay_type()!=null && section.getDisplay_type().equals("collection")) {

                        CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
                        bodyFragment = "CollectionGridFragment";
                        ((MainActivity)getActivity()).extras = new Bundle();
                        extras.putInt("Section_id", section.getId_s());
                        collectionGridFragment.setArguments(extras);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, collectionGridFragment)
                                .addToBackStack(null).commit();

                    }
                    else if (isTablet && section.getDisplay_type()!=null && section.getDisplay_type().equals("grid")) {
                        //Log.e(" section.getDisplay_type().equals(grid) : "+section.getDisplay_type().equals("grid"), " CategorieGridFragment will be called ");
                        CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
                        bodyFragment = "CategorieGridFragment";
                        ((MainActivity)getActivity()).extras = new Bundle();
                        extras.putInt("Section_id", section.getId_s());
                        coategorieGridFragment.setArguments(extras);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();

                    }else if (cat.getDisplay_type()!=null && cat.getDisplay_type().equals("bottom_slider") && isTablet) {
                        SliderCategoryFragment_ fragment = new SliderCategoryFragment_();
                        bodyFragment = "SliderCategoryFragment";
                        // In case this activity was started with special instructions from an Intent,
                        // pass the Intent's extras to the fragment as arguments
                        ((MainActivity)getActivity()).extras = new Bundle();
                        //extras.putInt("Section_id", section.getId_section());
                        extras.putInt("Category_id", cat.getId());
                        fragment.setArguments(extras);
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                    }else {
                        CategoryFragment categoryFragment = new CategoryFragment();
                        bodyFragment = "CategoryFragment";
                        // In case this activity was started with special instructions from an Intent,
                        // pass the Intent's extras to the fragment as arguments
                        ((MainActivity)getActivity()).extras = new Bundle();
                        extras.putInt("Section_id", section.getId_s());
                        categoryFragment.setArguments(extras);
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                    }
                }else {
                    CategoryFragment categoryFragment = new CategoryFragment();
                    bodyFragment = "CategoryFragment";
                    // In case this activity was started with special instructions from an Intent,
                    // pass the Intent's extras to the fragment as arguments
                    ((MainActivity)getActivity()).extras = new Bundle();
                    extras.putInt("Section_id", section.getId_s());
                    categoryFragment.setArguments(extras);
                    // Add the fragment to the 'fragment_container' FrameLayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
                }

                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);

            } else if(tab.getIsHomeGrid()) {
                bodyFragment = "HomeGridFragment";
                params = realm.where(Parameters.class).findFirst(); /***** pr eviter params null*****/

                if(params.getHome_type().equals("web")){
                    WebHomeFragment webHomeFragment = new WebHomeFragment();
                    bodyFragment = "WebHomeFragment";
                    Bundle extra = new Bundle();
                    extra.putString("link", params.getWeb_home_test_url());
                    webHomeFragment.setArguments(extra);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    webHomeFragment).commit();
                }
                else if (ifSwipe) {

                    //buildSwipe();

                    findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.swipe_container).setVisibility(View.GONE);

                    Log.e(" swipperFragment :", " Yes it is "+" ");
                    SwipperFragment swipperFragment = new SwipperFragment();
                    ((MainActivity)getActivity()).bodyFragment = "SwipperFragment";
                    swipperFragment.setArguments(((MainActivity)getActivity()).extras);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    swipperFragment).addToBackStack(null)
                            .commit();


                }else {
                    HomeGridFragment homeGridFragment = new HomeGridFragment();
                    MainActivity.this
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, homeGridFragment)
                            .addToBackStack(null).commit();
                }

            }
            else if(section.getDisplay_type()!=null && section.getDisplay_type().equals("column_scroll")){
                CollumnScrollFragment collumnScrollFragment = CollumnScrollFragment.create(section.getName(), section.getCategories());//new CollumnScrollFragment();
                bodyFragment = "CollumnScrollFragment";
                ((MainActivity)getActivity()).extras = new Bundle();
                extras.putInt("Section_id", section.getId_s());
                extras.putInt("index", 0);
                collumnScrollFragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, collumnScrollFragment)
                        .addToBackStack(bodyFragment).commit();
            }
        }else if (type.equals("agenda")) {
            if(section.getDisplay_type().compareTo("month_grid") == 0) {

                //if(isTablet) {
                //CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
                CalendarHebdoPagerFragment calendarPagerFragment = new CalendarHebdoPagerFragment();
                extras = new Bundle();
                extras.putString("display_type", section.getDisplay_type());
                //extras.putInt("Section_id", section.getId_section());
                extras.putInt("index", 0);
                calendarPagerFragment.setArguments(extras);
                bodyFragment = "CalendarPagerFragment";
                MainActivity.this
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();
                //				}else {
                //					CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
                //					extras = new Bundle();
                //					extras.putString("display_type", section.getDisplay_type());
                //					//extras.putInt("Section_id", section.getId_section());
                //					extras.putInt("index", 0);
                //					calendarPagerFragment.setArguments(extras);
                //					bodyFragment = "CalendarPagerFragment";
                //					MainActivity.this
                //					.getSupportFragmentManager()
                //					.beginTransaction()
                //					.replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();
                //				}
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
            }else if(section.getDisplay_type().compareTo("month_calendar") == 0) {
                AgendaSplitFragment  agendaSplitFragment= new AgendaSplitFragment();
                extras = new Bundle();
                extras.putString("display_type", section.getDisplay_type());
                extras.putInt("Section_id", section.getId_s());
                extras.putInt("index", 0);
                agendaSplitFragment.setArguments(extras);
                bodyFragment = "AgendaSplitFragment";
                MainActivity.this
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, agendaSplitFragment).addToBackStack(null).commit();
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
            }else if (section.getDisplay_type().compareTo("list") == 0) {

                EventListFragment eventListFragment = new EventListFragment();
                extras = new Bundle();
                extras.putString("display_type", section.getDisplay_type());
                extras.putInt("Section_id", section.getId_s());
                //					extras.putInt("index", 0);
                eventListFragment.setArguments(extras);
                bodyFragment = "CalendarPagerFragment";
                MainActivity.this
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, eventListFragment).addToBackStack(null).commit();

            }


        }else if (type.equals("interactive_map")) {
            if(section.getMaps() != null){
                List<com.euphor.paperpad.Beans.interactiveMap.map> maps = new ArrayList<com.euphor.paperpad.Beans.interactiveMap.map>(section.getMaps());
                extras = new Bundle();
                extras.putInt("section_id", section.getId());
                bodyFragment = "InteractiveMapFragment";
                InteractiveMapFragment interactiveMapFragment = InteractiveMapFragment.create(maps);
                interactiveMapFragment.setArguments(extras);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, interactiveMapFragment).addToBackStack("InteractiveMapFragment").commit();
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
            }
        }else if (type.equals("web")) {

            WebViewFragment  webViewFragment= new WebViewFragment();
            extras = new Bundle();
            extras.putString("link", section.getRoot_url());
            webViewFragment.setArguments(extras);
            bodyFragment = "WebViewFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, webViewFragment).addToBackStack("WebViewFragment").commit();
            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        } else if (type.equals("contact")) { // Type Contacts

            ContactsFragment contactsFragment = new ContactsFragment();
            bodyFragment = "ContactsFragment";
            extras.putInt("Section_id", section.getId_s());
            contactsFragment.setArguments(extras);
            // Add the fragment to the 'fragment_container' FrameLayout
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, contactsFragment)
                    .addToBackStack(null).commit();
            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        } else if (type.equals("gallery")) { // Type Gallery


            extras.putInt("Section_id", section.getId_s());

            if(section.getGallery_design() != null && section.getGallery_design().equals("slider")){
                bodyFragment = "GalerySliderFragment";
                GalerySliderFragment galerySliderFragment = new GalerySliderFragment();
                galerySliderFragment.setArguments(extras);
                MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, galerySliderFragment).addToBackStack("GalerySliderFragment").commit();

            }else{
                bodyFragment = "AlbumsGridFragment";
                AlbumsGridFragment albumsGridFragment = new AlbumsGridFragment();
                albumsGridFragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumsGridFragment).addToBackStack("AlbumsGridFragment").commit();
            }

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }else if (type.equals("map")){  /*realm.where(MyBox.class).findAll()*/

            MapV2Fragment mMapFragment = new MapV2Fragment();
            bodyFragment = "MapV2Fragment";

            extras.putInt("Section_id", section.getId());
            mMapFragment.setArguments(extras);

            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
            fragmentTransaction.addToBackStack(null).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }else if (type.equals("rss")) {
            List<Section> sections = realm.where(Section.class).equalTo("type", "rss").findAll();
            if(sections.size() >0) {

            Fragment rssFragment = new RSSFragment();
            bodyFragment = "RSSFragment";
            extras.putInt("Section_id", section.getId_s());
            rssFragment.setArguments(extras);
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, rssFragment);
            fragmentTransaction.addToBackStack(null).commit();

            if (timer != null) {
                timer.cancel();
            }
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);
        }

        }else if (type.equals("video")) {
            bodyFragment = "PlayerYouTubeFrag";
            PlayerYouTubeFrag myFragment = PlayerYouTubeFrag.newInstance(section.getId_s());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment, bodyFragment).addToBackStack(bodyFragment).commit();
            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

            /* Intent intent = new Intent(getApplicationContext(), YoutubePlayerActivity.class);
             intent.putExtra("InfoActivity", infoActivity);
             startActivity(intent);
             overridePendingTransition(0, 0);*/

        }else if (type.equals("mybox")) {

            MyBoxGridFragment  myBoxGridFragment= new MyBoxGridFragment();
            extras = new Bundle();
            /*extras.putInt("Section_id",section.getId());*/
            //				extras.putString("link", section.getRoot_url());
            myBoxGridFragment.setArguments(extras);
            bodyFragment = "MyBoxGridFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, myBoxGridFragment).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }else if (type.equals("agenda")) {

            AgendaFragment  agendaFrag= new AgendaFragment();
            extras = new Bundle();
            agendaFrag.setArguments(extras);
            bodyFragment = "AgendaFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, agendaFrag).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);
        }/*else if(type.compareTo("CalendarPagerFragment") == 0) {
             CalendarPagerFragment calendarPagerFragment = new CalendarPagerFragment();
             extras = new Bundle();
             extras.putString("display_type", "month_grid");
             //extras.putInt("Section_id", section.getId_section());
             extras.putInt("index", 0);
             calendarPagerFragment.setArguments(extras);
             bodyFragment = "CalendarPagerFragment";
             MainActivity.this
             .getSupportFragmentManager()
             .beginTransaction()
             .replace(R.id.fragment_container, calendarPagerFragment).addToBackStack(null).commit();

             if (timer != null) {timer.cancel();}
             findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
             findViewById(R.id.swipe_container).setVisibility(View.GONE);
         }else if(type.compareTo("AgendaSplitFragment") == 0) {
             AgendaSplitFragment  agendaSplitFragment= new AgendaSplitFragment();
             extras = new Bundle();
             extras.putString("display_type", "month_calendar");
             extras.putInt("Section_id", section.getId_section());
             extras.putInt("index", 0);
             agendaSplitFragment.setArguments(extras);
             bodyFragment = "AgendaSplitFragment";
             MainActivity.this
             .getSupportFragmentManager()
             .beginTransaction()
             .replace(R.id.fragment_container, agendaSplitFragment).addToBackStack(null).commit();

             if (timer != null) {timer.cancel();}
             findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
             findViewById(R.id.swipe_container).setVisibility(View.GONE);
         }*/else if (type.equals("survey")) {
            SurveyFragment  fragment= new SurveyFragment();
            extras = new Bundle();
            extras.putString("title", tab.getTitle());
            fragment.setArguments(extras);
            bodyFragment = "SurveyFragment";
            MainActivity.this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        } else if (type.equals("dashboard") || type.equals("room_service") ) {
            params = realm.where(Parameters.class).findFirst();
            cd = new ConnectionDetector(getApplicationContext());

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                alert.showAlertDialog(MainActivity.this, getResources().getString(R.string.error_connecting),
                        getResources().getString(R.string.error_connecting_message), false);
                // stop executing code by return
            }else {
                if (timer != null) {timer.cancel();}
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.swipe_container).setVisibility(View.GONE);
                //DownloadParseAsyncTask
                asyncTask = new DownloadParseAsyncTask(section.getId());
                String urlBase = Constants.PROD+"/api/application/dashboard/id/"+params.getId()+"/lang/"+wmbPreference.getString(Utils.LANG, "fr");
                asyncTask.execute(urlBase/*"http://192.168.1.15/dash/28dash.json"*/);
            }

        }else if (type.equals("radio")) {

            RadiosSectionFragment  fragment= new RadiosSectionFragment();
            setRadioCallback(fragment);
            extras = new Bundle();
            extras.putInt("Section_id", section.getId());
            fragment.setArguments(extras);
            bodyFragment = "RadiosSectionFragment";
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, bodyFragment).addToBackStack(bodyFragment).commit();

            if (timer != null) {timer.cancel();}
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.swipe_container).setVisibility(View.GONE);

        }

            if (!(type.equals("dashboard") || type.equals("room_service"))){
            if(asyncTask != null)
                asyncTask.cancel(true);
        }
    }

    private boolean isMultiSelect(Child_pages page2) {
        Related related = page2.getRelated();
        if (related.getRelatedCatIds().size()>0) {
            Category category = realm.where(Category.class).equalTo("id",related.getRelatedCatIds().iterator().next().getLinked_id()).findFirst(); //appController.getCategoryById(related.getRelatedCatIds().iterator().next().getLinked_id());
            if (category!=null && category.getDisplay_type().equals("multi_select")) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }


    }

    public static Typeface FONT_REGULAR;
    public static Typeface FONT_BOLD;
    public static Typeface FONT_TITLE;
    public static Typeface FONT_ITALIC;
    public static Typeface FONT_BODY;

    public static void initializeFonts(final Context context, Parameters params2) {
        /**  Uness Modif Fonts **/
        if (params2.getTitle_font_android() == null) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat/Montserrat-Regular.otf");
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat/Montserrat-Light.otf");
            return;
        }

        if (params2.getTitle_font_android().contains("Droid")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        }else if (params2.getTitle_font_android().contains("Aclonica")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Aclonica/Aclonica.ttf");
        }else if (params2.getTitle_font_android().contains("Gilda_Display")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Gilda_Display/GildaDisplay-Regular.ttf");
        }else if (params2.getTitle_font_android().contains("McLaren")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/McLaren/McLaren-Regular.ttf");
        }else if (params2.getTitle_font_android().contains("Medula_One")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Medula_One/MedulaOne-Regular.ttf");
        }else if (params2.getTitle_font_android().contains("Nova_Square")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Nova_Square/NovaSquare.ttf");
        }else if (params2.getTitle_font_android().contains("Oswald")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald/Oswald-Regular.ttf");//Oswald-Light
        }else if (params2.getTitle_font_android().contains("Overlock")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Overlock/Overlock-Regular.ttf");//Overlock-Italic
        }else if (params2.getTitle_font_android().contains("Playfair_Display_SC")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Playfair_Display_SC/PlayfairDisplaySC-Regular.ttf");//PlayfairDisplaySC-Italic
        }else if (params2.getTitle_font_android().contains("Ruluko")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Ruluko/Ruluko-Regular.ttf");
        }else if (params2.getTitle_font_android().contains("Sniglet")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Sniglet/Sniglet-ExtraBold.ttf");//Sniglet-Regular
        }else if (params2.getTitle_font_android().contains("Tauri")) {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Tauri/Tauri-Regular.ttf");
        }else if (params2.getTitle_font_android().contains("Open Sans Bold")) {
            	FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/Open_Sans/OpenSans-Bold.ttf");
        }else {
            FONT_TITLE = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat/Montserrat-Regular.otf");
        }



        if (params2.getBody_font_android().contains("Droid")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        }else if (params2.getBody_font_android().contains("Aclonica")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Aclonica/Aclonica.ttf");
            Utils.fontPath = "fonts/Aclonica/Aclonica.ttf";
        }else if (params2.getBody_font_android().contains("Gilda_Display")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Gilda_Display/GildaDisplay-Regular.ttf");
            Utils.fontPath = "fonts/Gilda_Display/GildaDisplay-Regular.ttf";
        }else if (params2.getBody_font_android().contains("McLaren")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/McLaren/McLaren-Regular.ttf");
            Utils.fontPath = "fonts/McLaren/McLaren-Regular.ttf";
        }else if (params2.getBody_font_android().contains("Medula_One")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Medula_One/MedulaOne-Regular.ttf");
            Utils.fontPath = "fonts/Medula_One/MedulaOne-Regular.ttf";
        }else if (params2.getBody_font_android().contains("Nova_Square")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Nova_Square/NovaSquare.ttf");
            Utils.fontPath = "fonts/Nova_Square/NovaSquare.ttf";
        }else if (params2.getBody_font_android().contains("Oswald")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald/Oswald-Light.ttf");//Oswald-Light
            Utils.fontPath = "fonts/Oswald/Oswald-Light.ttf";
        }else if (params2.getBody_font_android().contains("Overlock")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Overlock/Overlock-Italic.ttf");//Overlock-Italic
            Utils.fontPath = "fonts/Overlock/Overlock-Italic.ttf";
        }else if (params2.getBody_font_android().contains("Playfair_Display_SC")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Playfair_Display_SC/PlayfairDisplaySC-Italic.ttf");//PlayfairDisplaySC-Italic
            Utils.fontPath = "fonts/Playfair_Display_SC/PlayfairDisplaySC-Italic.ttf";
        }else if (params2.getBody_font_android().contains("Ruluko")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Ruluko/Ruluko-Regular.ttf");
            Utils.fontPath = "fonts/Ruluko/Ruluko-Regular.ttf";
        }else if (params2.getBody_font_android().contains("Sniglet")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Sniglet/Sniglet-Regular.ttf");//Sniglet-Regular
            Utils.fontPath = "fonts/Sniglet/Sniglet-Regular.ttf";
        }else if (params2.getBody_font_android().contains("Tauri")) {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Tauri/Tauri-Regular.ttf");
            Utils.fontPath = "fonts/Tauri/Tauri-Regular.ttf";
        }else if (params2.getBody_font_android().contains("Open Sans Regular")) {
            			FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/Open_Sans/OpenSans-Regular.ttf");
            		}else {
            FONT_BODY = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat/Montserrat-Light.otf");
        }


    }


    /*
      * all methods here are for the GCM function
      */
    private final String LOG_TAG = getClass().getSimpleName();
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "expiration_time";

    GoogleCloudMessaging gcm;
    public String regid;
    Context context;
    private long tsLong;
    public static String prod_or_sand = "sandbox";

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(LOG_TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(LOG_TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    int index = 1;

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(getString(R.string.sender_id));//SENDER_ID);

                    msg = "Device registered, registration ID=" + regid;
                    Log.i("MainActivity", ""+msg);

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    Looper.prepare();
                    Handler handler = new Handler();
                    if (index < 70) {
                        index = index*2;
                        int delayMillis = (index * 1000);
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                registerInBackground();

                            }
                        }, delayMillis);
                    }else {
                        registerReceiver(receiver, filter);
                    }
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //                mDisplay.append(msg + "\n");
                Log.i(LOG_TAG, msg);
            }
        }.execute(null, null, null);
    }

    IntentFilter filter = new IntentFilter("com.google.android.c2dm.intent.REGISTRATION");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG + " GCM", "Got into onReceive for manual GCM retrieval. "
                    + intent.getExtras());
            regid = intent.getStringExtra("registration_id");

            if (regid != null && !regid.isEmpty()) {
                sendRegistrationIdToBackend();

            } else {
                //                theResult.add("");
            }
        }
    };

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void sendInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                // You should send the registration ID to your server over HTTP, so it
                // can use GCM/HTTP or CCS to send messages to your app.
                sendRegistrationIdToBackend();

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //                mDisplay.append(msg + "\n");
                Log.i(LOG_TAG, msg);
            }
        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(LOG_TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app.
     */
    private void sendRegistrationIdToBackend() {
        int id_menu = 0;
        realm=Realm.getInstance(getApplicationContext());
        params=new Parameters();
        if (params == null) {

            params =  realm.where(Parameters.class).findFirst();//appController.getParametersDao().queryForId(1);

        }else {
            id_menu = params.getId();
        }

        String application_unique_identifier = Installation.id(getApplicationContext());
        String application_version = "0.2.8";
        try {
            application_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {

            e.printStackTrace();
        };

        String device_type = "";
        if (isTablet) {
            device_type = "tablet";
        }else {
            device_type = "smartphone";
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            display.getSize(size);
        }else {
            size.x = display.getWidth();  // deprecated
            size.y = display.getHeight();
        }


        String device_screen_resolution = size.x+"x"+size.y;

        ArrayList<AppHit> hits = new ArrayList<AppHit>();
        try {
            hits = (ArrayList<AppHit>) ((MyApplication)getApplication()).hits.clone();
        } catch (ConcurrentModificationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        AppSession appSession = new AppSession(id_menu,
                prod_or_sand ,
                "sales",
                application_unique_identifier,
                application_version ,
                regid,
                android.os.Build.MANUFACTURER,
                android.os.Build.MODEL,
                "android",
                device_screen_resolution,
                5,
                android.os.Build.VERSION.SDK_INT+"",
                device_type,
                session_id,
                tsLong,
                System.currentTimeMillis()/1000 ,
                hits );
        ArrayList<AppSession> appSessions = new ArrayList<AppSession>();
        appSessions.add(appSession);
        AppJsonWriter appJsonWriter = new AppJsonWriter();
        String str = appJsonWriter.writeJson(appSessions);
        String endpoint = SERVER_URL;
        String body = str;
        int status = 0;
        try {
            status = AppJsonWriter.post(endpoint, body);
        } catch (IOException e) {
            Log.e(LOG_TAG, "request couldn't be sent "+status);
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
         /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
             .permitAll().build();
             StrictMode.setThreadPolicy(policy);
             sendRegistrationIdToBackend();*/
        sendInBackground();
        super.onStop();
    }

    @Override
    protected void onPause() {
        sendInBackground();
        super.onPause();
    }



 /*    @Override
     public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
         Street_view_default_position strViewDefaultPosition = MapV2Fragment.sectionMap.getStreet_view_default_position();
         LatLng position = new LatLng(strViewDefaultPosition.getStreet_view_latitude(), strViewDefaultPosition.getStreet_view_longitude());
         float bearing = 180f;//strViewDefaultPosition.getStreet_view_orientation();
         position = new LatLng(37.765927, -122.449972);
         streetViewPanorama.setPosition(position);
         StreetViewPanoramaOrientation orientation = new StreetViewPanoramaOrientation(0f, bearing);
         StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder().bearing(bearing).orientation(orientation).build();
         streetViewPanorama.animateTo(camera, 1);
     }*/

    public class DownloadParseAsyncTask extends AsyncTask<String, String, Boolean>{

        /** progress dialog to show user that the backup is processing. */
        public ProgressDialog dialog;
        boolean erreur = false;
        private Dashboard dashboard;
        private int section_id_dashboard;

        @Override
        protected Boolean doInBackground(String... params) {


            String dashString = Utils.retrieveJson(params[0]);
            Realm r = Realm.getInstance(getApplicationContext());
            r.beginTransaction();
            r.createOrUpdateObjectFromJson(DashboardMain.class,dashString);
            r.commitTransaction();
             /*    DashboardMain dashboardMain = mapper.readValue(dashString, DashboardMain.class);*/

            return erreur;
        }


        /* (non-Javadoc)
          * @see android.os.AsyncTask#onPreExecute()
          */
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.waiting));
            this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.dialog.setIndeterminate(false);
            this.dialog.setCancelable(false);
            this.dialog.show();


            super.onPreExecute();
        }

        /* (non-Javadoc)
          * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
          */
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        /* (non-Javadoc)
          * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
          */
        @Override
        protected void onPostExecute(Boolean result) {


            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

            //Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


            Realm r = Realm.getInstance(getApplicationContext());
            DashboardMain dashboardMain = r.where(DashboardMain.class).findFirst();
            if(dashboardMain!=null) {
                dashboard = dashboardMain.getDashboard();
          /*   Log.i(LOG_TAG, dashboardMain.getDashboard().getInner_padding()+"");*/
                Log.i(LOG_TAG, dashboard.getInner_padding() + "");
            }

            if(!isTablet || ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) && metrics.densityDpi >= 213 && metrics.densityDpi <= 219)) {
                DashboardFragmentPhone dashboardFragment = DashboardFragmentPhone.newInstance(dashboard);
                bodyFragment = "DashboardFragment";
                extras.putInt("Section_id", section_id_dashboard);
                dashboardFragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dashboardFragment).addToBackStack(null).commit();

            }else {
                DashboardFragment dashboardFragment = DashboardFragment.newInstance(dashboard);
                bodyFragment = "DashboardFragment";
                extras.putInt("Section_id", section_id_dashboard);
                dashboardFragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dashboardFragment).addToBackStack(null).commit();
            }
            //			drawLayouts(deviceHeight, deviceWidth);
            //			getActivity().setRequestedOrientation(prevOrientation);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            super.onPostExecute(result);
        }


        public DownloadParseAsyncTask(int section_id_dashboard) {
            super();
            this.section_id_dashboard = section_id_dashboard;
            dialog = new ProgressDialog(getActivity());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && data != null) {
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
    }


    /**
     * @param page2
     *
     */
    public void fbShare(final String photo, final Child_pages page2) {
        Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    publishFeedDialog(photo, page2);
                }
            }
        });
    }
    private void publishFeedDialog(String photo, Child_pages page2) {
         /*byte[] data = null;

         Bitmap bi = BitmapFactory.decodeFile(photoToPost);
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         data = baos.toByteArray();

         Bundle params = new Bundle();
         params.putString("method", "photos.upload");
         params.putByteArray("picture", data);

         Facebook facebook = new Facebook(getResources().get);
         AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook );
         mAsyncRunner.request(null, params, "POST", new SampleUploadListener(), null);*/


        //		byte[] data = null;
        //
        //		Bitmap bi = BitmapFactory.decodeFile(photo);
        //		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //		bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //		data = baos.toByteArray();

        Bundle params = new Bundle();
        params.putString("name", page2.getTitle());
        params.putString("caption", page2.getIntro());
        params.putString("description", page2.getBody());
        //		params.putByteArray("picture", data);
        //		params.putString("link", "http://www.google.com");
        params.putString("picture", photo);

        WebDialog feedDialog = new WebDialog.FeedDialogBuilder(getActivity(), Session.getActiveSession(), params)
                .setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Bundle values, FacebookException error) {
                        if(error == null){
                            final String postId = values.getString("post_id");
                            if(postId != null){
                                Toast.makeText(getActivity(), "Posted story, id: " + postId, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Publish cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }else if(error instanceof FacebookOperationCanceledException){
                            // User clicked the "x" button
                            Toast.makeText(getActivity(), "Publish cancelled", Toast.LENGTH_SHORT).show();
                        }else{
                            // Generic, ex: network error
                            Toast.makeText(getActivity(), "Error posting story", Toast.LENGTH_SHORT).show();
                        }
                    }

                }).build();

        feedDialog.show();
    }



    public interface RadioSectionCallback{
        void showProgress(boolean show);
    }

    RadioSectionCallback radioCallback = new RadioSectionCallback() {

        @Override
        public void showProgress(boolean show) {
            // TODO Auto-generated method stub

        }


    };

    public MediaPlayer player;

    public void startPlaying(final List<Radio> radios, final Radio radio, final ProgressBar progressBar, final RadioCoverFlowAdapter adapter) {

        try {
            // playSeekBar.setVisibility(View.VISIBLE);
            radioCallback.showProgress(true);
            player.prepareAsync();

            player.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    realm=Realm.getInstance(getApplicationContext());
                    realm.beginTransaction();
                    player.start();
                    radio.setSelected(true);
                    findViewById(R.id.ff_radio_fragment).setVisibility(View.VISIBLE);
                    RadioPlayerFragment fragment = new RadioPlayerFragment();

                    fragment.setRadios(radios);
                    Bundle bundle = new Bundle();
                    bundle.putInt("radio_id", radio.getId());
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.ff_radio_fragment, fragment, "RadioPlayerFragment").addToBackStack("RadioPlayerFragment").commitAllowingStateLoss();
                    //					radioCallback.showProgress(false);
                    RadiosSectionFragment fragment2 = (RadiosSectionFragment)getSupportFragmentManager().findFragmentByTag("RadiosSectionFragment");
                    if (fragment2 != null) {
                        fragment2.selectRadio(radio.getId(), true);
                    }
                    try {
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    realm.commitTransaction();
                }
            });
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    public void initializeMediaPlayer(String link) {
        stopPlaying();
        player = new MediaPlayer();
        //        link = "http://74.208.98.253:8220";// test remove it!
        try {
            player.setDataSource(link);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }


    public void stopPlaying() {
        try {
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
                //            initializeMediaPlayer("");
            }
            radioCallback.showProgress(false);
            //			RadioPlayerFragment fragment = (RadioPlayerFragment) getSupportFragmentManager().findFragmentByTag("RadioPlayerFragment");
            //			if (fragment != null) {
            //				getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            //				findViewById(R.id.ff_radio_fragment).setVisibility(View.GONE);
            //			}

            //        playSeekBar.setVisibility(View.INVISIBLE);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pausePlaying() {
        if (player != null && player.isPlaying()) {
            try {
                player.pause();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //            initializeMediaPlayer("");
        }
    }

    public void resumePlaying() {
        if (player != null) {
            try {
                player.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //            initializeMediaPlayer("");
        }
    }

    /**
     * @return the radioCallback
     */
    public RadioSectionCallback getRadioCallback() {
        return radioCallback;
    }

    /**
     * @param radioCallback the radioCallback to set
     */
    public void setRadioCallback(RadioSectionCallback radioCallback) {
        this.radioCallback = radioCallback;
    }

    /**
     * Checks if the registration has expired.
     *
     * <p>To avoid the scenario where the device sends the registration to the
     * server but the server loses it, the app developer may choose to re-register
     * after REGISTRATION_EXPIRY_TIME_MS.
     *
     * @return true if the registration has expired.
     */
    private boolean isRegistrationExpired() {
        final SharedPreferences prefs = getGCMPreferences(context);
        // checks if the information is not stale
        long expirationTime =
                prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
        return System.currentTimeMillis() > expirationTime+ (604800000);// 7*24*3600*1000 = 604800000
    }

    @Override
    public void onClose() {
        findViewById(R.id.RLMainActivity_dim).setBackgroundColor(Color.TRANSPARENT);
        //		findViewById(R.id.RLMainActivity).setAlpha(0.5f);

    }

    @Override
    public void onOpen() {
        findViewById(R.id.RLMainActivity_dim).setBackgroundColor(Color.BLACK);
        findViewById(R.id.RLMainActivity_dim).setAlpha(0.7f);

    }

     /*@Override
     public void onOpened() {
         findViewById(R.id.RLMainActivity_dim).setBackgroundColor(Color.BLACK);
         findViewById(R.id.RLMainActivity_dim).setAlpha(0.7f);

     }*/
     @Override
     public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         switch (requestCode) {
             case REQUEST_CODE_ASK_PERMISSIONS:
                 if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     // Permission Granted

                 } else {
                     // Permission Denied
                     Toast.makeText(this, "You don't have permission !   ", Toast.LENGTH_SHORT)
                             .show();
                 }
                 break;
             default:
                 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         }
     }

}

/**
 *
 */