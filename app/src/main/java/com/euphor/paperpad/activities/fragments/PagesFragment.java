/**
 *
 */
package com.euphor.paperpad.activities.fragments;

import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applidium.headerlistview.HeaderListView;

//import com.euphor.paperpad.BeansCEP.CallbackRelatedLinks_Realm_Bean;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.Beans.RelatedContactForm;
import com.euphor.paperpad.Beans.RelatedLocation;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.activities.main.YoutubePlayerActivity;
import com.euphor.paperpad.adapters.FormulePageHAdapter;
//import com.euphor.paperpad.beans.CartItem;
import com.euphor.paperpad.Beans.CartItem;
//import com.euphor.paperpad.beans.Category;
import com.euphor.paperpad.Beans.Category;
//import com.euphor.paperpad.beans.Child_pages;
import com.euphor.paperpad.Beans.Child_pages;
//import com.euphor.paperpad.beans.Contact;
import com.euphor.paperpad.Beans.Contact;
//import com.euphor.paperpad.beans.ExtraField;
import com.euphor.paperpad.Beans.ExtraField;
//import com.euphor.paperpad.beans.Formule;
import com.euphor.paperpad.Beans.Formule;
//import com.euphor.paperpad.beans.FormuleElement;
import com.euphor.paperpad.Beans.FormuleElement;
//import com.euphor.paperpad.beans.Illustration;
import com.euphor.paperpad.Beans.Illustration;
//import com.euphor.paperpad.beans.ImagePage;
//import com.euphor.paperpad.beans.Link;
import com.euphor.paperpad.Beans.Link;
//import com.euphor.paperpad.beans.Linked;
import com.euphor.paperpad.Beans.Linked;
//import com.euphor.paperpad.beans.Location;
import com.euphor.paperpad.Beans.Location;
//import com.euphor.paperpad.beans.Locations_group;
import com.euphor.paperpad.Beans.Locations_group;
//import com.euphor.paperpad.beans.Parameters;
import com.euphor.paperpad.Beans.Parameters;
//import com.euphor.paperpad.beans.Price;
import com.euphor.paperpad.Beans.Price;
//import com.euphor.paperpad.beans.Related;
import com.euphor.paperpad.Beans.Related;
//import com.euphor.paperpad.beans.RelatedCatIds;
//import com.euphor.paperpad.beans.RelatedContactForm;
//import com.euphor.paperpad.beans.RelatedLocation;
/*import com.euphor.paperpad.controllers.AppController;*/
//import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.ColorTransformation;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.RelatedItem1;

import com.euphor.paperpad.utils.RelatedTitle1;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.actionsPrices.ActionItem;
import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.utils.actionsPrices.QuickAction.OnActionItemClickListener;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.AViewFlipper;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.euphor.paperpad.widgets.PopupBackround;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import com.euphor.paperpad.adapters.FormulePageHAdapter.CallbackRelatedLinks_;
import com.euphor.paperpad.adapters.FormulePagesAdapter.CallbackRelatedLinks;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * @author euphordev02
 *
 */
public class PagesFragment extends Fragment implements CallbackRelatedLinks, CallbackRelatedLinks_ {


    private static final int PREVIOUS = -1;
    private static final int NEXT = 1;
    private static int WIDTH_SWIPE_PICS = 600;
    private static String ALPHA = "CC";
    private String design;
    //  private AppController appController;
    private int page_id;
    public Child_pages page;
    // public RealmResults<Child_pages> page1;
    private Colors colors;
    private RealmList<Child_pages> pages;
    private int indexCurrent;
    private Integer categoryId;
    private StateListDrawable stateListDrawable;
    protected LayoutInflater layoutInflater;
    private PopupWindow pw;
    private View view;
    private LinearLayout llRelated;
    private MainActivity mainActivity;
    private String path;
    private Illustration illust;
    private Parameters parameters;
    private boolean isTablet;
    protected AlertDialog alertDialog;
    protected PopupWindow pwShare;
   /* public CallbackRelatedLinks_Realm_Bean callbackRelatedLinks_realm_bean;*/




    private int current;
    //	private ViewFlipper viewFlipper;
    private AViewFlipper viewFlipper;
    private int downX,upX;

    //private Callbacks mCallbacks = mDetailCallbacks;

    public static int currentDetailPosition = 1;
    public Realm realm;


    private GoogleMap gMap;
    private SupportMapFragment mMapFragment;
    private static LatLng CENTER_LOCATION = new LatLng(0.0, 0.0);
    private String address = "";

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #gMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMapFragment = new SupportMapFragment() {
                @Override
                public void onActivityCreated(Bundle savedInstanceState) {
                    super.onActivityCreated(savedInstanceState);
                    gMap = mMapFragment.getMap();
                    if (gMap != null) {
                        setUpMapIfNeeded();
                        setUpMap();
                    }
                }
            };
            getChildFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();
            //gMap = mMapFragment.getMap();//((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if(gMap != null) {
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                gMap.getUiSettings().setZoomControlsEnabled(false);
            }

        }else{
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            gMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #gMap} is not null.
     */
    private void setUpMap() {
        if (gMap != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER_LOCATION, 10));
            Marker marker = gMap.addMarker(new MarkerOptions().position(CENTER_LOCATION).title(address));
            marker.showInfoWindow();
        }
        //gMap.addCircle(new CircleOptions().center(CENTER_LOCATION).fillColor(Color.WHITE).radius(50)).setStrokeColor(Color.GREEN);
    }


    /**
     *
     */
    public PagesFragment() {

    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        // appController = ((MyApplication)getActivity().getApplication()).getAppController();//new AppController(getActivity());

        design = getArguments().getString("design","random");
        colors = ((MainActivity)activity).colors;
        		realm = Realm.getInstance(getActivity());
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            // colors = new Colors(appController.getParametersDao().queryForId(1));
            colors = new Colors(ParamColor);
        }
        mainActivity = (MainActivity)activity;
        isTablet = Utils.isTablet(activity);
        //page=realm.where(Child_pages.class).equalTo("id",page_id).findFirst();
        //  page1=realm.where(Child_pages.class).findAll();
        super.onAttach(activity);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);

        page_id = getArguments().getInt("page_id");

        if(((MainActivity) getActivity()).extras == null)
            ((MainActivity) getActivity()).extras = new Bundle();
        ((MainActivity) getActivity()).extras.putInt("page_id", page_id);
        ((MainActivity) getActivity()).bodyFragment = "PagesFragment";
        //
        time = System.currentTimeMillis() ;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = new View(getActivity());
        //design = "horizontal"; // for testing remove when production
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int deviceWidth = dm.widthPixels - (int) getResources().getDimension(R.dimen.width_tab_fragment);
        WIDTH_SWIPE_PICS = (int) ((float) deviceWidth * (70f / 100f));
        //get the child page by its id

        //page = appController.getChildPageDao().queryForId(page_id);


        page = realm.where(Child_pages.class).equalTo("id_cp", page_id).findFirst();

        //for (int i=0; i<page.size();i++) {


        if (page != null && design.compareTo("horizontal") != 0) {
            if (isTablet) {
                design = page.getDesign();
            } else {
                design = page.getDesign_smartphone();
            }


        } else {
            design = "horizontal";
            ((MainActivity) getActivity()).extras.putString("design", "horizontal");
        }


			/*
             * choose which template to use to show the child_page
			 */
        if (isTablet) {


            if (design.equals("single_image")) {
                view = inflater.inflate(R.layout.single_image_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";


                view = inflater.inflate(R.layout.single_image_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";

                ImageView imageView = (ImageView) view.findViewById(R.id.ProductIMG);
                imageView.setScaleType(ScaleType.CENTER_INSIDE);

                Illustration illust = page.getIllustration();


                if (!illust.getFullPath().isEmpty()) {
                    path = illust.getFullPath();
                    Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                } else if (!illust.getPath().isEmpty()) {
                    path = illust.getPath();
                    Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                } else if (!illust.getFullLink().isEmpty()) {
                    path = illust.getFullLink();
                    Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                } else {
                    path = illust.getLink();
                    Glide.with(getActivity()).load(path).dontAnimate().into(imageView);
                }

                return view;

            } else if (design.equals("horizontal")) { // Horizontal display of a product
                view = inflater.inflate(R.layout.horizontal_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";
            } else if (design.equals("vertical")) {
                view = inflater.inflate(R.layout.vertical_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                view.findViewById(R.id.separator).setBackgroundColor(colors.getColor(colors.getForeground_color(), ALPHA));
                ALPHA = "FF";
            }else if (design.equals("details_map")) {
                view = inflater.inflate(R.layout.details_map, container, false);
                view.setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));
                view.findViewById(R.id.Info_holder).setBackgroundColor(colors.getColor(colors.getBackground_color()));
                view.findViewById(R.id.emptySpace_).setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));

                ALPHA = "FF";

                ExtraField extraField = page.getExtra_fields();

                if (extraField != null) {

                    if(extraField.getLatitude() != null && extraField.getLongitude() != null) {
                        String lat = extraField.getLatitude();
                        String lon = extraField.getLongitude();

                        try {
                            if(!lat.isEmpty() && !lon.isEmpty()){
                                CENTER_LOCATION = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    if(extraField.getAddress() != null && !extraField.getAddress().isEmpty()){
                        address = extraField.getAddress();
                    }

                    if(extraField.getFeatured() != null && !extraField.getFeatured().isEmpty()){
                        String featured = ""+extraField.getFeatured();
                        TextView featured_label = (TextView) view.findViewById(R.id.featured_label);
                        view.findViewById(R.id.featured_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.featured_container).setBackgroundColor(colors.getColor(colors.getBackground_color(), "40"));
                        featured_label.setTextAppearance(getActivity(), style.TextAppearance_DeviceDefault_Medium);
                        featured_label.setTypeface(MainActivity.FONT_TITLE);
                        //featured_label.setBackgroundColor(colors.getColor(colors.getBackground_color(), "40"));
                        featured_label.setTextColor(colors.getColor(colors.getTitle_color()));
                        featured_label.setText(featured);

                        ImageView featured_image = (ImageView) view.findViewById(R.id.featured_image);
                        Glide.with(getActivity()).load(R.drawable.heart).transform(new ColorTransformation(getActivity(), Color.parseColor("#e76597"))).into(featured_image);
                    }
                }
                mMapFragment = new SupportMapFragment() {
                    @Override
                    public void onActivityCreated(Bundle savedInstanceState) {
                        super.onActivityCreated(savedInstanceState);
                        gMap = mMapFragment.getMap();
                        if (gMap != null) {
                            setUpMapIfNeeded();
                            setUpMap();
                        }
                    }
                };
                getChildFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();
            } else if (design.equals("transparent")) {
                view = inflater.inflate(R.layout.transparent_page, container, false);
                ALPHA = "CC";
                RelativeLayout close_open = (RelativeLayout) view.findViewById(R.id.close_open);
                view.findViewById(R.id.close_infos).setVisibility(View.VISIBLE);
                Drawable drawExit = getResources().getDrawable(R.drawable.ic_exit);
                drawExit.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color(), "AA"), Mode.MULTIPLY));
                view.findViewById(R.id.close_infos).setBackgroundDrawable(drawExit);
                ArrowImageView toggleImg = (ArrowImageView) view.findViewById(R.id.toggle_infos);
                toggleImg.setPaint(getNewPaint());

                ExtraField extraField = page.getExtra_fields();
                final boolean toggle_;
                if (extraField != null && "ok".equals(extraField.getDefault_overlay_hides_content())) {
                    if (view.findViewById(R.id.navigationHolderForStroke) != null) {
                        view.findViewById(R.id.navigationHolderForStroke).setVisibility(View.GONE);
                    }
                    if (view.findViewById(R.id.navigationHolderForStrokePort) != null) {
                        view.findViewById(R.id.navigationHolderForStrokePort).setVisibility(View.GONE);
                    }
                    if (view.findViewById(R.id.informationHolder) != null) {
                        view.findViewById(R.id.informationHolder).setVisibility(View.GONE);
                    }
                    close_open.findViewById(R.id.close_infos).setVisibility(View.GONE);
                    close_open.findViewById(R.id.toggle_infos).setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((LinearLayout) view.findViewById(R.id.Info_holder)).getLayoutParams();
                    params.bottomMargin = 0;
                    view.findViewById(R.id.Info_holder).setLayoutParams(params);
                    //								v.setRotation(-90);
                    toggle_ = false;
                } else {
                    toggle_ = true;
                }

                close_open.setOnClickListener(new OnClickListener() {
                    boolean toggle = toggle_;

                    @Override
                    public void onClick(View v) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((LinearLayout) view.findViewById(R.id.Info_holder)).getLayoutParams();
                        // navigationHolderForStroke navigationHolderForStrokePort informationHolder
                        if (toggle) {
                            if (view.findViewById(R.id.navigationHolderForStroke) != null) {
                                view.findViewById(R.id.navigationHolderForStroke).setVisibility(View.GONE);
                            }
                            if (view.findViewById(R.id.navigationHolderForStrokePort) != null) {
                                view.findViewById(R.id.navigationHolderForStrokePort).setVisibility(View.GONE);
                            }
                            if (view.findViewById(R.id.informationHolder) != null) {
                                view.findViewById(R.id.informationHolder).setVisibility(View.GONE);
                            }
                            v.findViewById(R.id.close_infos).setVisibility(View.GONE);
                            v.findViewById(R.id.toggle_infos).setVisibility(View.VISIBLE);
                            params.bottomMargin = 0;
                            view.findViewById(R.id.Info_holder).setLayoutParams(params);
                            //								v.setRotation(-90);
                            toggle = !toggle;
                        } else {
                            if (view.findViewById(R.id.navigationHolderForStroke) != null) {
                                view.findViewById(R.id.navigationHolderForStroke).setVisibility(View.VISIBLE);
                            }
                            if (view.findViewById(R.id.navigationHolderForStrokePort) != null) {
                                view.findViewById(R.id.navigationHolderForStrokePort).setVisibility(View.VISIBLE);
                            }
                            if (view.findViewById(R.id.informationHolder) != null) {
                                view.findViewById(R.id.informationHolder).setVisibility(View.VISIBLE);
                            }
                            v.findViewById(R.id.close_infos).setVisibility(View.VISIBLE);
                            v.findViewById(R.id.toggle_infos).setVisibility(View.GONE);
                            params.bottomMargin = 50;
                            view.findViewById(R.id.Info_holder).setLayoutParams(params);
                            //								v.setRotation(90);
                            toggle = !toggle;
                        }


                    }
                });


            } else if (design.equals("panoramic")) {

                FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("panorama");
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }
                Fragment panoFragment = new PanoramaFragment();
                ((MainActivity) getActivity()).extras.putInt("page_id", page_id);
                ((MainActivity) getActivity()).bodyFragment = "PanoramaFragment";
                panoFragment.setArguments(((MainActivity) getActivity()).extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
                fragmentTransaction.commit();
                return view;

            } else if (design.equals("swipe")) {
                view = inflater.inflate(R.layout.swipe_aligned_page, container, false);
                view.setBackgroundDrawable(colors.getBackPD());
                view.findViewById(R.id.svInformationSpace).setBackgroundColor(colors.getColor(colors.getBackground_color()));
                view.findViewById(R.id.SVforRelated).setBackgroundColor(colors.getColor(colors.getBackground_color()));

                //view.findViewById(R.id.Category).setVisibility(View.GONE);

                ALPHA = "FF";
                LinearLayout swipeHolder = (LinearLayout) view.findViewById(R.id.swipeViewContainer);
                RealmList<Illustration> images = new RealmList<>();
                if(page.getIllustration()!=null) {


                        images = page.getImagePages();
//                    images.add(page.getIllustration());

                    for (Iterator<Illustration> iterator = images.iterator(); iterator.hasNext(); ) {

                        illust = (Illustration) iterator.next();//page.getIllustration();// imagePage.getIllustration();
                        LinearLayout imageHolder = new LinearLayout(getActivity());
                        LayoutParams params = new LayoutParams(WIDTH_SWIPE_PICS, LayoutParams.MATCH_PARENT);
                        params.setMargins(10, 10, 10, 10);
                        imageHolder.setLayoutParams(params);
                        imageHolder.setPadding(5, 5, 5, 5);
                        imageHolder.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_with_shadow));
                        ImageView imageView = new ImageView(getActivity());
                        LayoutParams paramsImg = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                        imageView.setScaleType(ScaleType.CENTER_CROP);
                        imageView.setLayoutParams(paramsImg);
                        imageHolder.addView(imageView);
                        if (!illust.getFullPath().isEmpty()) {
                            path = illust.getFullPath();
                            Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                        } else if (!illust.getPath().isEmpty()) {
                            path = illust.getPath();
                            Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                        } else if (!illust.getFullLink().isEmpty()) {
                            path = illust.getFullLink();
                            Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                        } else {
                            path = illust.getLink();
                            Glide.with(getActivity()).load(path).dontAnimate().into(imageView);
                        }

                        swipeHolder.addView(imageHolder);
                    }
                }

            } else if (design.equals("swipe_aligned")) {
                view = inflater.inflate(R.layout.swipe_page, container, false);
                view.setBackgroundDrawable(colors.getBackPD());
                ALPHA = "FF";

                //view.findViewById(R.id.Category).setVisibility(View.GONE);

                view.findViewById(R.id.svInformationSpace).setBackgroundColor(colors.getColor(colors.getBackground_color()));
                view.findViewById(R.id.SVforRelated).setBackgroundColor(colors.getColor(colors.getBackground_color()));
                LinearLayout swipeHolder = (LinearLayout) view.findViewById(R.id.swipeViewContainer);
                RealmList<Illustration> images = new RealmList<>();
                if(page.getIllustration()!=null) {


                    images = page.getImagePages();
//                    images.add(page.getIllustration());
                for (Iterator<Illustration> iterator = images.iterator(); iterator.hasNext(); ) {

                    illust = iterator.next();// imagePage.getIllustration();
                    LinearLayout imageHolder = new LinearLayout(getActivity());
                    LayoutParams params = new LayoutParams(WIDTH_SWIPE_PICS, LayoutParams.MATCH_PARENT);
                    params.setMargins(10, 10, 10, 10);
                    imageHolder.setLayoutParams(params);
                    imageHolder.setPadding(5, 5, 5, 5);
                    imageHolder.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_with_shadow));
                    ImageView imageView = new ImageView(getActivity());
                    LayoutParams paramsImg = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(paramsImg);
                    imageHolder.addView(imageView);
                    //						path = !illust.getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
                    //						imageLoader.displayImage(path, imageView);
                    if (!illust.getFullPath().isEmpty()) {
                        path = illust.getFullPath();
                        Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                    } else if (!illust.getPath().isEmpty()) {
                        path = illust.getPath();
                        Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                    } else if (!illust.getFullLink().isEmpty()) {
                        path = illust.getFullLink();
                        Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                    } else {
                        path = illust.getLink();
                        Glide.with(getActivity()).load(path).dontAnimate().into(imageView);
                    }

                    swipeHolder.addView(imageHolder);
                }
            }
            } else if (design.equals("formule")) {
                view = inflater.inflate(R.layout.formule_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";
                //view.findViewById(R.id.separator).setBackgroundColor(colors.getColor(colors.getForeground_color(),ALPHA));
                view.findViewById(R.id.separator).setVisibility(View.GONE);
            } else {
                Random r = new Random();
                int a = r.nextInt(10);
                if (a < 5) {
                    view = inflater.inflate(R.layout.horizontal_page, container, false);
                    view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                    ALPHA = "FF";
                } else {
                    view = inflater.inflate(R.layout.transparent_page, container, false);
                    ALPHA = "CC";
                }
            }
        } else {


            if (design.equals("single_image")) {
                view = inflater.inflate(R.layout.single_image_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";

                ImageView imageView = (ImageView) view.findViewById(R.id.ProductIMG);
                imageView.setScaleType(ScaleType.CENTER_CROP);

                Illustration illust = page.getIllustration();


                if (!illust.getFullPath().isEmpty()) {
                    path = illust.getFullPath();
                    Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                } else if (!illust.getPath().isEmpty()) {
                    path = illust.getPath();
                    Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                } else if (!illust.getFullLink().isEmpty()) {
                    path = illust.getFullLink();
                    Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                } else {
                    path = illust.getLink();
                    Glide.with(getActivity()).load(path).dontAnimate().into(imageView);
                }

                return view;

            } else if (design.equals("horizontal")) { // Horizontal display of a product

                view = inflater.inflate(R.layout.horizontal_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";

            }else if (design.equals("details_map")) {
                view = inflater.inflate(R.layout.horizontal_detail_map, container, false);
                view.setBackgroundColor(colors.getBackMixColor(colors.getBackground_color(), 0.10f));
                ALPHA = "FF";

                ExtraField extraField = page.getExtra_fields();

                if (extraField != null && extraField.getLatitude() != null) {

                    if(extraField.getLatitude() != null && extraField.getLongitude() != null) {
                        String lat = extraField.getLatitude();
                        String lon = extraField.getLongitude();

                        try {
                            if(!lat.isEmpty() && !lon.isEmpty()){
                                CENTER_LOCATION = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    if(extraField.getAddress() != null && !extraField.getAddress().isEmpty()){
                        address = extraField.getAddress();
                    }


                    if(extraField.getFeatured() != null && !extraField.getFeatured().isEmpty()){
                        String featured = ""+extraField.getFeatured();
                        TextView featured_label = (TextView) view.findViewById(R.id.featured_label);
                        view.findViewById(R.id.featured_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.featured_container).setBackgroundColor(colors.getColor(colors.getBackground_color(), "40"));
                        featured_label.setTextAppearance(getActivity(), style.TextAppearance_DeviceDefault_Medium);
                        featured_label.setTypeface(MainActivity.FONT_TITLE);
                        //featured_label.setBackgroundColor(colors.getColor(colors.getBackground_color(), "40"));
                        featured_label.setTextColor(colors.getColor(colors.getTitle_color()));
                        featured_label.setText(featured);

                        ImageView featured_image = (ImageView) view.findViewById(R.id.featured_image);
                        Glide.with(getActivity()).load(R.drawable.heart).transform(new ColorTransformation(getActivity(), Color.parseColor("#e76597"))).dontAnimate().into(featured_image);
                    }
                }
                mMapFragment = new SupportMapFragment() {
                    @Override
                    public void onActivityCreated(Bundle savedInstanceState) {
                        super.onActivityCreated(savedInstanceState);
                        gMap = mMapFragment.getMap();
                        if (gMap != null) {
                            setUpMapIfNeeded();
                            setUpMap();
                        }
                    }
                };
                getChildFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();

            } else if (design.equals("product")) {
                view = inflater.inflate(R.layout.page_produit, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";

            } else if (design.equals("panoramic")) {

                FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("panorama");
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }
                Fragment panoFragment = new PanoramaFragment();
                ((MainActivity) getActivity()).extras.putInt("page_id", page_id);
                ((MainActivity) getActivity()).bodyFragment = "PanoramaFragment";
                panoFragment.setArguments(((MainActivity) getActivity()).extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
                fragmentTransaction.commit();
                return view;

            } else if (design.equals("formule")) {
                view = inflater.inflate(R.layout.formule_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";
                //view.findViewById(R.id.separator).setBackgroundColor(colors.getColor(colors.getForeground_color(),ALPHA));
                view.findViewById(R.id.separator).setVisibility(View.GONE);
            } else {
                view = inflater.inflate(R.layout.horizontal_page, container, false);
                view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                ALPHA = "FF";
            }
        }



			/*
			 * COMMUN TASKS
			 */
        page = realm.where(Child_pages.class).equalTo("id_cp", page_id).findFirst();
        //Title product
        TextView titleTV = (TextView) view.findViewById(R.id.titleProductTV);
        titleTV.setTextAppearance(getActivity(), style.TextAppearance_DeviceDefault_Large);
        titleTV.setTypeface(MainActivity.FONT_TITLE);
        if(page !=null)
        titleTV.setText(page.getTitle());

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        if (isTablet) {
            if (metrics.densityDpi >= 213)
                titleTV.setTextSize(titleTV.getTextSize() + 4);
            else
                titleTV.setTextSize(titleTV.getTextSize() + 8);

        } else {

        }
        titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
        //titleTV.setGravity(Gravity.CENTER);
        ((RelativeLayout.LayoutParams) titleTV.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        titleTV.setPadding(0, 15, 0, 15);
        titleTV.setGravity(Gravity.CENTER_VERTICAL);
        if (design.equals("horizontal") || design.equals("product")) {
            //((android.widget.RelativeLayout.LayoutParams)titleTV.getLayoutParams()).alignWithParent = true;
            //((android.widget.RelativeLayout.LayoutParams)titleTV.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);

            ((RelativeLayout.LayoutParams) titleTV.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ((RelativeLayout.LayoutParams) titleTV.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
            titleTV.setGravity(Gravity.CENTER);
            view.findViewById(R.id.Category).setVisibility(View.GONE);
        }

        view.findViewById(R.id.titleProductHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));

        //Image Product
        // final RealmList<ImagePage> images = new RealmList<>();
        if(page !=null && page.getIllustration()!=null){
            final RealmList<Illustration> images = new RealmList<>();

            images.add(page.getIllustration());


            if (images.size() > 0 && (ImageView) view.findViewById(R.id.ProductIMG) != null) {

                ///ImageSwitcher
                viewFlipper = (AViewFlipper) view.findViewById(R.id.viewFlipper);


                current = 0;


                String[] paths = new String[images.size()];
                Collections.reverse(images);

                for (Iterator<Illustration> iterator = images.iterator(); iterator.hasNext(); ) {

                    Illustration imagePage = iterator.next();
                    illust = page.getIllustration();//imagePage.getIllustration();

                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                    //imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                    viewFlipper.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                    if (!illust.getFullPath().isEmpty()) {
                        path = illust.getFullPath();
                        Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                    } else if (!illust.getPath().isEmpty()) {
                        path = illust.getPath();
                        Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                    } else if (!illust.getFullLink().isEmpty()) {
                        path = illust.getFullLink();
                        Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imageView);
                    } else {
                        path = illust.getLink();
                        Glide.with(getActivity()).load(path).dontAnimate().into(imageView);
                    }


                    current++;

                }
                if (images.size() > 1) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (viewFlipper != null) {
                                try {
                                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.effect_slide_in_left));
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                viewFlipper.showNext();
                            }
                        }
                    }, 1000);
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //							viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.effect_slide_out_right));
                            viewFlipper.showPrevious();
                        }
                    }, 1500);

                }


                if (current > 1)
                    viewFlipper.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {


                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                downX = (int) event.getX();
                                return true;
                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                upX = (int) event.getX();

                                if (upX - downX > 100) {

                                    current--;
                                    if (current < 0) {
                                        current = images.size() - 1;
                                    }

                                    try {
                                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_left));
                                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_right));
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    viewFlipper.showPrevious(); //.setImageResource(imgs[current]);
                                } else if (downX - upX > -100) {

                                    current++;
                                    if (current > images.size() - 1) {
                                        current = 0;
                                    }

                                    try {
                                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_right));
                                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_left));
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    viewFlipper.showNext(); //.setImageResource(imgs[current]);
                                }
                                return true;
                            }

                            return false;

                        }

                    });

            }
        }
        // Arrows next and previous coloring and actions !
        ArrowImageView previousIMG = (ArrowImageView) view.findViewById(R.id.PreviousIMG);
        Paint paint = getNewPaint();
        previousIMG.setPaint(paint);
        ArrowImageView nextIMG = (ArrowImageView) view.findViewById(R.id.NextIMG);
        nextIMG.setPaint(paint);

        //category = appController.getCategoryDao().queryForId(category.getId_category());
        Category category = null;
        if (page != null) {
            category = realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();
        }
        //    Collection<Child_pages> pagesTmp = category.getChildren_pages1();
        if(category !=null) {
            RealmList<Child_pages> pagesTmp = category.getChildren_pages();

            LinearLayout navButtons = (LinearLayout) view.findViewById(R.id.navButtons);
            LinearLayout backPrev = (LinearLayout) view.findViewById(R.id.backPrevious);
            LinearLayout backNext = (LinearLayout) view.findViewById(R.id.backNext);
            if (pagesTmp.size() > 1) {
                pages = new RealmList<>();
                removeInvisiblePages(pagesTmp);
                indexCurrent = indexOfPage();
                backPrev.setOnClickListener(getNavigationBtnListener(PREVIOUS));
                backNext.setOnClickListener(getNavigationBtnListener(NEXT));
            } else {
                navButtons.setVisibility(View.GONE);
            }

            //category parent
            RelativeLayout navigationHolder = (RelativeLayout) view.findViewById(R.id.navigationHolder);
            TextView categoryTv = (TextView) view.findViewById(R.id.categorieTV);
            categoryTv.setTypeface(MainActivity.FONT_TITLE);

            if (design.equals("horizontal")) {
                view.findViewById(R.id.Category).setVisibility(View.GONE);
            }


            if (design.equals("formule")) {
                navigationHolder.setVisibility(View.GONE);
            } else {
                categoryTv.setText(category.getTitle());
                categoryTv.setTextColor(colors.getColor(colors.getForeground_color()));
                LinearLayout categoryHolder = (LinearLayout) view.findViewById(R.id.Category);
                categoryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
                categoryId = category.getId();
                categoryHolder.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //  Category category = appController.getCategoryById(categoryId);
                        Category category = realm.where(Category.class).equalTo("id", categoryId).findFirst();

                    ((MainActivity)getActivity()).openCategory_(category);


                    }
                });
            }


            ImageView iconZoom = (ImageView) view.findViewById(R.id.zoomIMG);
            Drawable drawZoom = getResources().getDrawable(R.drawable.icon_0_26);
            drawZoom.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY));
            iconZoom.setImageDrawable(drawZoom);
            TextView textZoom = (TextView) view.findViewById(R.id.tvGalery);
            textZoom.setTextColor(colors.getColor(colors.getForeground_color()));
            LinearLayout galeryHolder = (LinearLayout) view.findViewById(R.id.galeryHolder);
            galeryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
            galeryHolder.setVisibility(View.GONE);


            LinearLayout shareHolder = (LinearLayout) view.findViewById(R.id.shareHolder);
            if (category.getShare_button().equals("1")) {
                final View layout = inflater.inflate(R.layout.popup_share, null, false);

                shareHolder.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        pwShare = new PopupWindow(layout,
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT, true);
                        // display the popup in the center
                        pwShare.setOutsideTouchable(true);
                        pwShare.setBackgroundDrawable(new ColorDrawable(
                                Color.TRANSPARENT));
                        pwShare.setFocusable(true);
                        pwShare.setTouchInterceptor(new OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                    pwShare.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });
                        pwShare.showAtLocation(layout, Gravity.CENTER, 0, 0);


                        ImageView fb = (ImageView) layout.findViewById(R.id.fb);
                        ImageView others = (ImageView) layout.findViewById(R.id.others);
                        fb.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                pwShare.dismiss();
                            mainActivity.fbShare(illust.getLink(), page);

                            }
                        });
                        others.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                pwShare.dismiss();
                                Intent share = new Intent(Intent.ACTION_SEND);
                                MimeTypeMap map = MimeTypeMap.getSingleton(); //mapping from extension to mimetype
                                String ext = path.substring(path.lastIndexOf('.') + 1);
                                String mime = map.getMimeTypeFromExtension(ext);
                                share.setType(mime); // might be text, sound, whatever
                                Uri uri;
                                //					    if (path.contains("Paperpad/")) {
                                //							uri = Uri.fromFile(new File(path));
                                //						}else {
                                uri = Uri.parse(illust.getLink());
                                //						}
                                share.putExtra(Intent.EXTRA_SUBJECT, page.getTitle());
                                share.putExtra(Intent.EXTRA_TEXT, page.getIntro());
                                share.putExtra(Intent.EXTRA_STREAM, uri);//using a string here didnt work for me
                                Log.d("", "share " + uri + " ext:" + ext + " mime:" + mime);
                                startActivity(Intent.createChooser(share, "share"));

                            }
                        });
                        //				String path = "/mnt/sdcard/dir1/sample_1.jpg";


                    }
                });
            } else {
                shareHolder.setVisibility(View.GONE);
            }

            // Long description under the product image
            WebView longdescWV = (WebView) view.findViewById(R.id.LongDescWV);
            StringBuilder htmlString = new StringBuilder();
            int[] colorText = Colors.hex2Rgb(colors.getBody_color());
            longdescWV.setBackgroundColor(Color.TRANSPARENT);
            if (view.findViewById(R.id.tempLL) != null) {
                view.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
            } else {
                longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
            }
            //		int sizeText = getResources().getDimensionPixelSize(R.dimen.priceTV);
            //		int sizeText = (int) Utils.spToPixels(getActivity(), (float) 14);
            if (!design.equals("formule")) {

                htmlString.append(Utils.paramBodyHTML(colorText));
                //			String body = page.getBody().replace("[break]", "");
                String body = (page.getBody().isEmpty()) ? page.getIntro() : page.getBody();
                body.replaceAll(". ", ".\n\n");
                if (body.contains("[break]")) {
                    String[] bodies = body.split("(\\[)break(\\])");
                    body = bodies[0].replace("(/[break])", "");
                    ImageView truncateIcon = (ImageView) view.findViewById(R.id.truncate_icon);
                    if (truncateIcon != null) {
                        view.findViewById(R.id.truncate_holder).setVisibility(View.VISIBLE);
                        truncateIcon.setVisibility(View.VISIBLE);
                        truncateIcon.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"), Mode.MULTIPLY));

                        truncateIcon.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                int[] colorText = Colors.hex2Rgb(colors.getBody_color());
                                AlertDialog.Builder builder;

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater.inflate(R.layout.dialog_truncate_body, null, false);
                                layout.setBackgroundColor(colors.getColor(colors.getBackground_color()));

                                ImageView image = (ImageView) layout.findViewById(R.id.truncate_body_icon);
                                image.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"), Mode.MULTIPLY));

                                WebView completeBodyWB = (WebView) layout.findViewById(R.id.LongDescWV_truncate);
                                completeBodyWB.setBackgroundColor(Color.TRANSPARENT);
                                StringBuilder htmlStringTruncate = new StringBuilder();
                                htmlStringTruncate.append(Utils.paramBodyHTML(colorText));
                                String bodyComplet = page.getBody().replace("[break]", "");
                                htmlStringTruncate.append(bodyComplet);
                                htmlStringTruncate.append("</div></body></html>");
                                completeBodyWB.getSettings().setDefaultFontSize(18);
                                completeBodyWB.loadDataWithBaseURL(null, htmlStringTruncate.toString(), "text/html", "UTF-8", null);

                                builder = new AlertDialog.Builder(getActivity());
                                builder.setView(layout);
                                alertDialog = builder.create();
                                alertDialog.show();
                                image.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();

                                    }
                                });
                            }
                        });
                    }
                }
                htmlString.append(body);

                if (view.findViewById(R.id.divider) != null)
                    view.findViewById(R.id.divider).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                if (view.findViewById(R.id.divider2) != null)
                    view.findViewById(R.id.divider2).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                if (view.findViewById(R.id.divider3) != null)
                    view.findViewById(R.id.divider3).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                if (view.findViewById(R.id.divider4) != null)
                    view.findViewById(R.id.divider4).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                if (view.findViewById(R.id.divider5) != null)
                    view.findViewById(R.id.divider5).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                if (view.findViewById(R.id.divider6) != null)
                    view.findViewById(R.id.divider6).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                Drawable cadreDrawable = getResources().getDrawable(R.drawable.cadre);
                cadreDrawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY));
                view.findViewById(R.id.navigationHolderForStroke).setBackgroundDrawable(cadreDrawable);

                shareHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
                LinearLayout portNavigationHolderForStroke = (LinearLayout) view.findViewById(R.id.navigationHolderForStrokePort);
                if (portNavigationHolderForStroke != null) {
                    portNavigationHolderForStroke.setBackgroundDrawable(cadreDrawable);
                    view.findViewById(R.id.dividerExtra).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                    view.findViewById(R.id.dividerExtra1).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                    view.findViewById(R.id.navigationHolderPort).setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
                }
            } else {
                //			sizeText = (int) Utils.spToPixels(getActivity(), (float) 16);
                htmlString.append(Utils.paramBodyHTML(colorText));
                String body = (page.getBody().isEmpty()) ? page.getIntro() : page.getBody();
                body.replaceAll(". ", ".\n");
                htmlString.append(body);
                if (view.findViewById(R.id.divider) != null)
                    view.findViewById(R.id.divider).setVisibility(View.GONE);
                if (view.findViewById(R.id.divider2) != null)
                    view.findViewById(R.id.divider2).setVisibility(View.GONE);
                if (view.findViewById(R.id.divider3) != null)
                    view.findViewById(R.id.divider3).setVisibility(View.GONE);
                if (view.findViewById(R.id.divider4) != null)
                    view.findViewById(R.id.divider4).setVisibility(View.GONE);
                if (view.findViewById(R.id.divider5) != null)
                    view.findViewById(R.id.divider5).setVisibility(View.GONE);
                if (view.findViewById(R.id.divider6) != null)
                    view.findViewById(R.id.divider6).setVisibility(View.GONE);
                if (view.findViewById(R.id.navigationHolderForStroke) != null)
                    view.findViewById(R.id.navigationHolderForStroke).setVisibility(View.GONE);
                LinearLayout portNavigationHolderForStroke = (LinearLayout) view.findViewById(R.id.navigationHolderForStrokePort);
                if (portNavigationHolderForStroke != null) {
                    portNavigationHolderForStroke.setVisibility(View.GONE);
                }
            }
            htmlString.append("</div></body></html>");

            longdescWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null);

            //prices related to this product
            parameters = null;
            // parameters = appController.getParametersDao().queryForId(1);
            parameters = realm.where(Parameters.class).findFirst();

            // Collection<Price> prices = page.getPrices1();
            RealmList<Price> prices = page.getPrices();
            LinearLayout pricesContainer = (LinearLayout) view.findViewById(R.id.PricesHolder);
            if (prices.size() > 0 && !design.equals("formule")) {

                for (Iterator<Price> iterator = prices.iterator(); iterator.hasNext(); ) {
                    final Price price = (Price) iterator.next();
                    View priceElement = inflater.inflate(R.layout.price, container, false);
                    LinearLayout btnPrice = (LinearLayout) priceElement.findViewById(R.id.btnPrice);
                    Drawable drawable = btnPrice.getBackground();
                    drawable.setColorFilter(colors.getColor(colors.getBody_color(), "55"), Mode.MULTIPLY);
                    btnPrice.setBackgroundDrawable(drawable);
                    TextView label = (TextView) priceElement.findViewById(R.id.priceLabelTV);
                    label.setTypeface(MainActivity.FONT_BODY);
                    if (price != null && price.getLabel() != null && !price.getLabel().isEmpty()) {
                        label.setText(price.getLabel());
                        label.setTextColor(colors.getColor(colors.getBody_color()));
                    } else {
                        label.setVisibility(View.GONE);
                    }
                    TextView value = (TextView) priceElement.findViewById(R.id.priceValueTV);
                    value.setTypeface(MainActivity.FONT_BODY);
                    value.setText((price != null && price.getAmount() != null) ? price.getAmount() + price.getCurrency() : "");
                    value.setTextColor(colors.getColor(colors.getTitle_color()));
                    priceElement.setTag(price);

                    if (parameters != null) {
                        if (!parameters.isShow_cart()) {
                            priceElement.findViewById(R.id.imgPlus).setVisibility(View.GONE);
                        }
                    }


                    priceElement.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (parameters != null && parameters.isShow_cart_smartphone()) {
                                Category category = null;
                                if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
                                    category = page.getCategory();
                                } else {
                            /* AppController controller = new AppController(mainActivity);
                        category = controller.getCategoryById(page.getCategory_id());*/
                                    category=  realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();

                                }
                                if (category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
                                        (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))) {
                                    if (category.isNeeds_stripe_payment()) {
                                        MainActivity.stripe_or_not = 1;
                                    } else {
                                        MainActivity.stripe_or_not = 0;
                                    }
                                    if ((page).getExtra_fields() != null &&
                                            page.getExtra_fields().getAlways_show_price_popover().equalsIgnoreCase("oui")) {
                                        QuickAction quickAction = new QuickAction(getActivity(), QuickAction.VERTICAL, colors);
                                        ActionItem title = new ActionItem("Choisissez une option", 0, true);
                                        quickAction.addViewItem(title);
                                        Price price = (Price) v.getTag();
                                        String titleIem = price.getLabel();
                                        if (titleIem != null && titleIem.isEmpty()) {
                                            titleIem = null;
                                        }
                                        boolean isShowIconCart = false;
                                        if (page.getExtra_fields() != null && page.getExtra_fields().getShow_carts_in_price_popover() != null && !page.getExtra_fields().getShow_carts_in_price_popover().isEmpty()) {
                                            isShowIconCart = true;
                                        }
                                        ActionItem item = new ActionItem(titleIem, 1, price, isShowIconCart);
                                        quickAction.addActionItem(item);
                                        quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
                                        quickAction.show(v);
                                        quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {

                                            @Override
                                            public void onItemClick(QuickAction source, int pos, int actionId) {
                                                if (actionId > 0) {
                                                    ActionItem item = source.getActionItem(actionId);
                                                    Price price = item.getPrice();

                                                    mainActivity.addItemToDB(price.getId_price(), page, null, 0, item.getQuantity(), null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
                                                    mainActivity.fillCart();
                                                    mainActivity.getMenu().showMenu();
                                                }
                                            }
                                        });
                                    } else {

                                        mainActivity.addItemToDB(price.getId_price(), page, null, 0, 1, null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
                                        mainActivity.fillCart();
                                        mainActivity.getMenu().showMenu();
                                    }
                                } else {
                                    if (category != null) {
                                        if (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                                            builder.setTitle(mainActivity.getString(R.string.payment_stripe_title)).setMessage(mainActivity.getString(R.string.payment_stripe_msg))
                                                    .setPositiveButton(mainActivity.getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            builder.create().show();
                                        } else if (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                                            builder.setTitle(mainActivity.getString(R.string.payment_stripe_title)).setMessage(mainActivity.getString(R.string.no_payment_stripe_msg))
                                                    .setPositiveButton(mainActivity.getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            builder.create().show();
                                        }
                                    }

                                }


                            }


                        }
                    });

                    // espacement
                    View espace = new View(getActivity());
                    LayoutParams params = new LayoutParams(5, LayoutParams.WRAP_CONTENT);
                    pricesContainer.addView(espace, params);
                    pricesContainer.addView(priceElement);
                }

            } else {
                pricesContainer.setVisibility(View.GONE);
            }
            if (design.equals("formule")) {

                if ((parameters != null && parameters.isShow_cart()) && prices.size() > 0) {
                    Price price = prices.iterator().next();
                    View priceElement = inflater.inflate(R.layout.formule_price, container, false);
                    TextView label = (TextView) priceElement.findViewById(R.id.priceLabelTV);
                    label.setTypeface(MainActivity.FONT_BODY);
                    //	label.setText((price!=null && price.getLabel()!=null)?price.getLabel():"");
                    label.setTextColor(colors.getColor(colors.getBackground_color()));
                    TextView value = (TextView) priceElement.findViewById(R.id.priceValueTV);
                    value.setTypeface(MainActivity.FONT_BODY, Typeface.NORMAL);
                    value.setText((price != null && price.getAmount() != null) ? price.getAmount() + price.getCurrency() : "");
                    value.setTextColor(colors.getColor(colors.getBackground_color()));
                    priceElement.findViewById(R.id.priceLabel).setBackgroundColor(colors.getColor(colors.getTitle_color()));
                    priceElement.findViewById(R.id.priceValue).setBackgroundColor(colors.getColor(colors.getTitle_color(), "80"));
                    ((android.widget.FrameLayout.LayoutParams) pricesContainer.getLayoutParams()).leftMargin = 20;
                    ((android.widget.FrameLayout.LayoutParams) pricesContainer.getLayoutParams()).bottomMargin = 20;
                    pricesContainer.addView(priceElement);
                    pricesContainer.setVisibility(View.VISIBLE);
                    final int price_id = price.getId_price();

                    priceElement.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (checkFilledFormula()) {
                                Category category = null;

                                if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
                                    category = page.getCategory();
                                } else {
                                    // AppController controller = new AppController(mainActivity);
                                    //category = controller.getCategoryById(page.getCategory_id());
                                    category = realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();
                                }
                                if (category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
                                        (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))) {
                                    if (category.isNeeds_stripe_payment()) {
                                        MainActivity.stripe_or_not = 1;
                                    } else {
                                        MainActivity.stripe_or_not = 0;
                                    }
                                    Formule formule = FillFormula();
                                    int id=0;
                                    int size =realm.where(CartItem.class).findAll().size();
                                    if(size >0)
                                        id=realm.where(CartItem.class).findAllSorted("id").last().getId()+1;
                                    else id++;


                                CartItem cartItem = new CartItem(id,price_id, null, null, 0, 1, formule, formule.getPrice(), page.getTitle(),"", new RealmList<CartItem>());
                                addItemToDB(cartItem);
                                    mainActivity.fillCart();
                                    //mainActivity.getMenu().showMenu();

                                    Handler handler = new Handler();
                                    long delay = 3000;
                                    mainActivity.getMenu().showMenu();
                                    if (page.getExtra_fields() != null && page.getExtra_fields().getFormule_supplements_category_id() != null) {
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mainActivity.getMenu().toggle();
                                                // List<Category> cat = appController.getCategoryDao().queryForEq("id", page.getExtraField().getFormule_supplements_category_id());
                                                RealmResults<Category> cat = realm.where(Category.class).equalTo("id", page.getExtra_fields().getFormule_supplements_category_id()).findAll();
                                                if (cat != null && cat.size() > 0) {
                                                mainActivity.openCategory(cat.get(0));
                                                }

                                            }
                                        }, delay);
                                    }


                                } else {

                                    //									Toast.makeText(getActivity(), "not allowed to add because mode :"+MainActivity.stripe_or_not, Toast.LENGTH_LONG).show();
                                    if (category != null) {
                                        if (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle(getString(R.string.payment_stripe_title)).setMessage(getString(R.string.payment_stripe_msg))
                                                    .setPositiveButton(getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            builder.create().show();
                                        } else if (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle(getString(R.string.payment_stripe_title)).setMessage(getString(R.string.no_payment_stripe_msg))
                                                    .setPositiveButton(getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            builder.create().show();
                                        }
                                    }


                                }
                            } else {

                                Toast.makeText(getActivity(), getResources().getString(R.string.choix_incomplet), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }

            //Utilities
            ShapeDrawable shape = getNewShape();
            //--state drawables
            stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
            //		stateListDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff"+mainActivity.colors.getSide_tabs_foreground_color()),PorterDuff.Mode.MULTIPLY));


            //Add Related products links
            Linked linked = page.getLinked();
            Related related = page.getRelated();
            llRelated = (LinearLayout) view.findViewById(R.id.LLRelatedItems);

            //		Collection<RelatedCatIds> relatedCats = related.getRelatedCatIds();
            //		Collection<RelatedPageId> relatedPages = related.getPages1();
            //		Collection<Link> links = linked.getLinks1();
            //		ArrayList<View> views = new ArrayList<View>();

            //		addRelatedElements(llRelated, related, linked, relatedCats, relatedPages, links, inflater);
            objects = getAllRelatedElements(related, linked);
            drawRelatedElements(objects, inflater);

            /*** Coloring ***/
            navigationHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
            navButtons.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
            TextView shareLabelTV = (TextView) view.findViewById(R.id.shareLabelTV);
            shareLabelTV.setTextColor(colors.getColor(colors.getForeground_color()));
            shareLabelTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
            ImageView shareIMG = (ImageView) view.findViewById(R.id.shareIMG);
            Drawable drawableShare = getResources().getDrawable(R.drawable.feed_icon);
            drawableShare.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY));
            shareIMG.setBackgroundDrawable(drawableShare);


            /** To modify Uness add DrawableList to llRelated**/
            view.findViewById(R.id.PricesHolderAbove).setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
            llRelated.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
            /*** END Coloring ***/

			/*
			 * END COMMUN TASKS
			 */


            //		for (int i = 0; i < 5; i++) {
            //			addItemToCart(null);
            //		}
        }


        return view;
    }


    private void removeInvisiblePages(RealmList<Child_pages> pagesTmp) {
        for (Iterator<Child_pages> iterator = pagesTmp.iterator(); iterator.hasNext();) {
            Child_pages child_pages =  iterator.next();
            if (child_pages.isVisible()) {
                pages.add(child_pages);
            }
        }

    }

    protected void addItemToDB(CartItem cartItem) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(cartItem);
        realm.commitTransaction();
        //   appController.getCartItemDao().createOrUpdate(cartItem);

    }
/*
    *//**
     * fill the cart with the {@link com.euphor.paperpad.Beans.CartItem} taken from the database
     *//*
    private void fillCart(){
        RealmResults<CartItem> cartItems;// = new ArrayList<CartItem>();
        //  cartItems = appController.getCartItemDao().queryForAll();
        cartItems =realm.where(CartItem.class).findAll();
        LinearLayout menuView = mainActivity.cartTagContainer;
        if (cartItems.size()>0) {
            menuView.removeAllViews();
            menuView.addView(getNewDivider());
            total = 0.0;
            for (int i = 0; i < cartItems.size(); i++) {
                mainActivity.addItemToCart(cartItems.get(i));
            }
        }else {
            menuView.removeAllViews();
        }
        mainActivity.totalSum.setText(total+"");
    }


    *//**add a {@link com.euphor.paperpad.Beans.CartItem} to the cart
     * @param item
     *//*
    protected void addItemToCart(CartItem item) {
        SlidingMenu menu = mainActivity.getMenu();
        LinearLayout menuView = mainActivity.cartTagContainer;
        View cartItemView = layoutInflater.inflate(R.layout.cart_tag, null, false);
        //define common graphic elements
        TextView titleProduct = (AutoResizeTextView)cartItemView.findViewById(R.id.TitleProduit);
        titleProduct.setTypeface(MainActivity.FONT_TITLE);
        TextView quantity = (AutoResizeTextView)cartItemView.findViewById(R.id.quantity);
        quantity.setTypeface(MainActivity.FONT_BODY);
        AutoResizeTextView relativeSum = (AutoResizeTextView)cartItemView.findViewById(R.id.relativeSum);
        relativeSum.setTypeface(MainActivity.FONT_BODY);
        LinearLayout deleteContainer = (LinearLayout)cartItemView.findViewById(R.id.deleteContainer);
        ImageView imgFormule = (ImageView)cartItemView.findViewById(R.id.imgCartItem);


        String prix = item.getPrice();
        Double rSum = (item.getQuantity())*(Double.parseDouble(prix));
        relativeSum.setText(rSum+"");
        total = total + rSum;

        if (item.getFormule()!= null) {
            Formule formule = item.getFormule();
            titleProduct.setText(formule.getName());
            relativeSum.setText(formule.getPrice());

            if (formule.getIllustration() != null) {
                Illustration illust = formule.getIllustration();
                if (!illust.getFullPath().isEmpty()) {
                    path = illust.getFullPath();
                    Glide.with(getActivity()).load(new File(path)).into(imgFormule);
                }else if (!illust.getPath().isEmpty()) {
                    path = illust.getPath();
                    Glide.with(getActivity()).load(new File(path)).into(imgFormule);
                }else if (!illust.getFullLink().isEmpty()) {
                    path = illust.getFullLink();
                    Glide.with(getActivity()).load(new File(path)).into(imgFormule);
                }else {
                    path = illust.getLink();
                    Glide.with(getActivity()).load(path).into(imgFormule);
                }


            }else {
                imgFormule.setVisibility(View.GONE);
            }
            menuView.addView(cartItemView);
            menuView.addView(getNewDivider());
            for (Iterator<FormuleElement> iterator = formule.getElements().iterator(); iterator.hasNext();) {
                View elementsView = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
                FormuleElement element = (FormuleElement) iterator.next();
                TextView tvElement = (AutoResizeTextView)elementsView.findViewById(R.id.TitleProduit);
                tvElement.setTypeface(MainActivity.FONT_BODY);
                tvElement.setText(element.getName());
                elementsView.findViewById(R.id.relativeSum).setVisibility(View.GONE);
                elementsView.findViewById(R.id.deleteContainer).setVisibility(View.GONE);
                menuView.addView(elementsView);
                menuView.addView(getNewDivider());
            }

        }else if (item.getCartItems().size()>0 ) {

            titleProduct.setText(item.getName());
            relativeSum.setText(item.getPrice());
            imgFormule.setVisibility(View.GONE);
            menuView.addView(cartItemView);
            menuView.addView(getNewDivider());
            Collection<CartItem> items = item.getCartItems();
            for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
                CartItem cartItem = (CartItem) iterator.next();
                View subItem = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
                TextView tvElement = (AutoResizeTextView)subItem.findViewById(R.id.TitleProduit);
                tvElement.setTypeface(MainActivity.FONT_BODY);
                tvElement.setText(cartItem.getName());
                TextView subSum = (AutoResizeTextView)subItem.findViewById(R.id.relativeSum);
                subSum.setText(cartItem.getName());
                LinearLayout subDelete = (LinearLayout)subItem.findViewById(R.id.deleteContainer);
                menuView.addView(subItem);
                menuView.addView(getNewDivider());
            }

        } else if (item.getParentItem() == null) {
            titleProduct.setText(item.getName());
            relativeSum.setText(item.getPrice());
            imgFormule.setVisibility(View.GONE);
            menuView.addView(cartItemView);
            menuView.addView(getNewDivider());
        }else {

        }

        quantity.setText(item.getQuantity()+"");
        final CartItem itemTmp = item;
        deleteContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean done = false;
                //appController.getCartItemDao().delete(itemTmp);
                realm.beginTransaction();
                itemTmp.removeFromRealm();
                *//*realm.where(CartItem.class).findAll().removeLast(*//**//*itemTmp*//**//*);*//*
                realm.commitTransaction();
                done  = true;
                if (done) {
                    //					String prix = itemTmp.getPrice();
                    //					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
                    //					total = total - rSum;
                    mainActivity.fillCart();
                }
            }
        });
        ImageView quantityLess = (ImageView)cartItemView.findViewById(R.id.quantityLess);
        ImageView quantityMore = (ImageView)cartItemView.findViewById(R.id.quantityMore);
        quantityLess.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int quant = itemTmp.getQuantity();
                CartItem arg0 = itemTmp;
                if (quant > 1) {
                    arg0.setQuantity(quant -1);

                    boolean done = false;
                    //  appController.getCartItemDao().update(arg0);
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(arg0);
                    realm.commitTransaction();

                    done  = true;
                    if (done) {
                        //					String prix = itemTmp.getPrice();
                        //					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
                        //					total = total - Double.parseDouble(prix);
                        mainActivity.fillCart();
                    }
                }

            }
        });

        quantityMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int quant = itemTmp.getQuantity();
                CartItem arg0 = itemTmp;
                arg0.setQuantity(quant +1);
                boolean done = false;
                //  appController.getCartItemDao().update(arg0);
                realm.copyToRealmOrUpdate(arg0);
                done  = true;
                if (done) {

                    mainActivity.fillCart();
                }

            }
        });

    }*/


    public Double total = 0.0;

    protected Formule FillFormula() {
        RealmList<FormuleElement> elements = new RealmList<FormuleElement>();
        Formule result = null;
        int id=0;
        int id_f=0;
        if(page.getPrices() !=null) {
    Price a = page.getPrices().iterator().next();
    String price = a.getAmount();
    result = new Formule(page.getTitle(), elements, price);
            int size_f = realm.where(Formule.class).findAll().size();
            if (size_f > 0)
                id_f = realm.where(Formule.class).findAllSorted("id").last().getId() + 1;
            else id_f++;
    result = new Formule(id_f,elements, price, page.getTitle(), page.getIllustration());
    for (int i = 0; i < objects.size(); i++) {
            /* realm.where(FormuleElement.class).findAll()*/
        if (objects.get(i) instanceof Child_pages) {
            page = (Child_pages) objects.get(i);
            int size = realm.where(FormuleElement.class).findAll().size();
            if (size > 0)
                id = realm.where(FormuleElement.class).findAllSorted("id").last().getId() + 1;
            else id++;

            FormuleElement element = new FormuleElement(id, price, result, page.getTitle(), page.getId());
            elements.add(element);
            // appController.getFormuleElementDao().createOrUpdate(element);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(element);
            realm.commitTransaction();
        }
    }

    result.setElements(elements);
    //		result.setIllustration(page.getIllustration());
}

        return result;
    }

    protected boolean checkFilledFormula() {
        boolean result = true;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Category) {
                result = false;
                return result;
            }
        }


        return result;
    }

    private ArrayList<Object> getAllRelatedElements(Related related, Linked linked){
        ArrayList<Object> objects = new ArrayList<Object>();
        RealmList<MyInteger> relatedCats = related.getList();
        RealmList<MyString> relatedPages = related.getList_pages();
        RealmList<Link> links = linked.getLinks();


        if (page != null && page.getRelated() != null && page.getRelated().getContact_form() != null && !page.getRelated().getContact_form().getTitle().isEmpty()) {


            RelatedItem1 item = (RelatedItem1)page.getRelated().getContact_form(); //list.get(0);
            objects.add(item);

        }

        if (page != null && page.getRelated() != null && page.getRelated().getRelatedLocation() != null && page.getRelated().getRelatedLocation().getId() != 0) {


            RelatedItem1 item = (RelatedItem1)page.getRelated().getRelatedLocation(); //list.get(0);
            objects.add(item);
        }

        if (!related.getTitle().isEmpty()) {

            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedPages.size() > 0) {

                objects.add(relatedTitle);
            }

        }
        if (relatedPages.size() > 0) {
            for (Iterator<MyString> iterator = relatedPages.iterator(); iterator.hasNext(); ) {


                MyString relatedPageId = iterator.next();
                RealmResults<Child_pages> child_pages;
                //child_pages = appController.getChildPageDao().queryForEq("id",relatedPageId.getLinked_id());

                child_pages = realm.where(Child_pages.class).equalTo("id",Integer.parseInt(relatedPageId.getMyString())).findAll();

                if (child_pages.size() > 0) {
                    RelatedItem1 item = (RelatedItem1)child_pages.get(0);
                    objects.add(item);
                }
            }
        }
        if (!related.getTitle_categories().isEmpty()) {

            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedCats.size() > 0) {

                objects.add(relatedTitle);
            }

        }
        if (relatedCats.size() > 0) {
            for (Iterator<MyInteger> iterator = relatedCats.iterator(); iterator
                    .hasNext(); ) {
                MyInteger relatedCatIds = (MyInteger) iterator
                        .next();
                final int id_related_cat = relatedCatIds.getMyInt();
                RealmResults<Category> categories;
                //categories = appController.getCategoryDao().queryForEq("id", relatedCatIds.getLinked_id());
                categories = realm.where(Category.class).equalTo("id", id_related_cat).findAll();
                if (categories.size() > 0) {

                    RelatedItem1 item = (RelatedItem1)categories.get(0);
                    objects.add(item);
                }
            }
        }


        if (!linked.getTitle().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)linked;
            if (links.size() > 0) {

                objects.add(relatedTitle);
            }


        }
        if (links.size()>0) {

            for (Iterator<Link> iterator = links.iterator(); iterator
                    .hasNext(); ) {
                Link link = (Link) iterator.next();

                RelatedItem1 item = (RelatedItem1) link;
                objects.add(item);

            }

        }
        return objects;

    }

    ArrayList<Object> objects;
    private View clickedView;
    protected int indexClicked;
    private long time;
    private FormulePageHAdapter mHAdapter;


    public void drawRelatedElements(ArrayList<Object> objects, LayoutInflater inflater){

        //		objects = getAllRelatedElements(related, linked);
        boolean toggle = false;
        boolean add_or_not = true;
        for (int i = 0; i < objects.size(); i++) {

            if (objects.get(i) instanceof RelatedTitle1) {

                RelatedTitle1 item = (RelatedTitle1)objects.get(i);
                if (toggle) {
                    // item.setPage(false);

                }
                View relatedTitleView = inflater.inflate(R.layout.related_title, null, false);
                TextView relatedTitleTV = (TextView) relatedTitleView.findViewById(R.id.title_related);
               /* TextView relatedTitleTV1 = (TextView) relatedTitleView.findViewById(R.id.title_related);*/
                relatedTitleTV.setTextAppearance(getActivity(), style.TextAppearance_Medium);
                relatedTitleTV.setTypeface(MainActivity.FONT_TITLE);

                if(!isTablet){
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    if(metrics.densityDpi <= 240 )
                        relatedTitleTV.setTextSize(relatedTitleTV.getTextSize() - 12);
                    else
                        relatedTitleTV.setTextSize(relatedTitleTV.getTextSize() - 20);
                }


                if (item.getRelatedTitle() != null ) {

                    relatedTitleTV.setText(item.getRelatedTitle());


                } /*else if (item.getLinked() != null ) {
                    if(item.isLink())
                        relatedTitleTV1.setText(item.getLinked().getTitle() *//***//*); // view pour linked

                }*/
                /*if (item.getRelatedTitle() != null) {
                    relatedTitleTV.setText(item.getRelatedTitle());
                }*/
                else{
                    relatedTitleView.setVisibility(View.GONE);
                }

                if(design.contains("formule")){
                    relatedTitleTV.setTextColor(colors.getColor(colors.getTitle_color()));
                    relatedTitleView.setBackgroundColor(colors.getColor(colors.getForeground_color(), "44"));//titleRelatedLL.setBackgroundDrawable(getNewShape());
                }
                else{
                    relatedTitleTV.setTextColor(colors.getColor(colors.getBackground_color()));
                    relatedTitleView.setBackgroundDrawable(colors.getForePD());//colors.makeGradientToColor(colors.getForeground_color()));
                    //relatedTitleView.setBackgroundColor(colors.getColor(colors.getForeground_color()));
                }

                toggle = true;
                llRelated.addView(relatedTitleView);
            }

            if (objects.get(i) instanceof RelatedItem1 ) {
                if (add_or_not) {
                    llRelated.addView(getNewDivider());
                }
                add_or_not = false;

                View relatedView = createRelatedView(inflater, objects.get(i), i);
                llRelated.addView(relatedView);
                llRelated.addView(getNewDivider());

            }
        }
    }



    @SuppressWarnings("deprecation")
    private View createRelatedView(LayoutInflater inflater, Object object, int index) {
        final RelatedItem1 item = (RelatedItem1) object;
        //		final Child_pages pageRelated = child_pages.get(0);
        boolean noImageUse = false;
        View relatedView = inflater.inflate(
                R.layout.related_elements_list_item, null,
                false);

        relatedView.setClickable(true);
        ColorStateList colorSelector = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}},
                new int[]{
                        colors.getColor(colors
                                .getBackground_color()),
                        colors.getColor(colors.getTitle_color())});

        ColorStateList colorSelector_ = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}},
                new int[]{
                        colors.getColor(colors
                                .getBackground_color()),
                        colors.getColor(colors.getBody_color())});
        stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTabs_background_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTabs_background_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));

        relatedView.setBackgroundDrawable(stateListDrawable);
        AutoResizeTextView titleTv = (AutoResizeTextView) relatedView.findViewById(R.id.TVTitleCategory);
        titleTv.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
        TextView relatedDesc = (TextView) relatedView.findViewById(R.id.TVrelatedDesc);
        relatedDesc.setTypeface(MainActivity.FONT_BODY);
        ArrowImageView arrowImg = (ArrowImageView) relatedView.findViewById(R.id.imgArrow);
        arrowImg.setLayoutParams(new LayoutParams(26, 26));
        ImageView imgPage = (ImageView) relatedView.findViewById(R.id.imgCategory);

        imgPage.setPadding(5, 5, 5, 5);

        if (design.equals("formule")) {
            relatedView.setPadding(30, 0, 30, 0);
            arrowImg.setVisibility(View.GONE);
            LinearLayout imgArrowContainer = (LinearLayout) relatedView.findViewById(R.id.imgArrowContainer);
            ImageView imgFormule = new ImageView(getActivity());
            imgFormule.setLayoutParams(new LayoutParams(32, 32));
            imgFormule.setScaleType(ScaleType.CENTER_CROP);
            Drawable stylo = getResources().getDrawable(R.drawable.icon_0_15);
            stylo.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color(), "AA"), Mode.MULTIPLY));
            imgFormule.setBackgroundDrawable(stylo);
            imgArrowContainer.addView(imgFormule);

            if (item instanceof Child_pages) {
                Child_pages pageItem =(Child_pages)item;
                Category categoryItem = pageItem.getCategory();
                titleTv.setVisibility(View.GONE);
                relatedDesc.setVisibility(View.VISIBLE);
                ((LayoutParams) relatedDesc.getLayoutParams()).leftMargin = 30;
                relatedDesc.setText(pageItem.getCommunTitle1());
                relatedDesc.setTextAppearance(getActivity(), style.TextAppearance_Medium);
                relatedDesc.setTypeface(MainActivity.FONT_BODY);
                relatedDesc.setTextColor(colors.getColor(colors.getTitle_color()));
            } else {
                noImageUse = true;
                Drawable plus_formule = getResources().getDrawable(R.drawable.multi_select_d);
                plus_formule.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBody_color(), "55"), Mode.MULTIPLY));

                imgPage.setBackgroundDrawable(plus_formule);

                //((LayoutParams)imgPage.getLayoutParams()).leftMargin = 30;
                int width_ = (int) Utils.dpToPixels(getActivity(), 42f);
                int height_ = (int) Utils.dpToPixels(getActivity(), 42f);
                ((LayoutParams) imgPage.getLayoutParams()).width = width_;
                ((LayoutParams) imgPage.getLayoutParams()).height = height_;
                imgFormule.setVisibility(View.GONE);
                //titleTv.setVisibility(View.GONE);
                ((LayoutParams) relatedDesc.getLayoutParams()).leftMargin = 30;
                if(!item.getItemIntro1().isEmpty())
                    relatedDesc.setText(item.getItemIntro1());
                else
                    relatedDesc.setText(getString(R.string.product_choices));

                //relatedDesc.setText(getString(R.string.product_choices));//item.getItemIntro());
                relatedDesc.setTextAppearance(getActivity(), style.TextAppearance_Medium);
                relatedDesc.setTypeface(mainActivity.FONT_BODY);
                relatedDesc.setTextColor(colors.getColor(colors.getTitle_color(), "CC"));
                titleTv.setVisibility(View.GONE);

            }

        } else {
            relatedDesc.setVisibility(View.GONE);
            titleTv.setText(item.getItemTitle1());
            relatedView.setPadding(10, 0, 10, 0);
        }

        titleTv.setTextColor(colorSelector);



        if (item instanceof Link) {
            titleTv.setText(item.getItemTitle1());
            relatedDesc.setText(item.getItemIntro1());
            relatedDesc.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            relatedDesc.setTypeface(MainActivity.FONT_BODY);
            relatedDesc.setTextColor(colorSelector_);//colors.getColor(colors.getBody_color()));
            relatedDesc.setVisibility(View.VISIBLE);
            //relatedDesc.setTypeface(relatedDesc.getTypeface(), Typeface.NORMAL);
        }else if(item instanceof RelatedContactForm) {
            RealmResults<Contact> contacts = realm.where(Contact.class).equalTo("id", ((RelatedContactForm) item).getId()).findAll();
            //  List<Contact> contacts = appController.getContactDao().queryForEq("id", ((RelatedContactForm)item).getId());
            if (contacts != null && contacts.size() > 0) {
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getId() == ((RelatedContactForm)item).getId()) {
                        noImageUse = true;
                        relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.VISIBLE);
                        imgPage.setVisibility(View.VISIBLE);
                        BitmapDrawable contactIcon = null;
                        try {
                            contactIcon = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(contacts.get(i).getIcon())));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (contactIcon != null) {
                            contactIcon.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY));
                        }
                        int size = (int) Utils.dpToPixels(getActivity(), 32f);
                        LayoutParams params = new LayoutParams(size, size);
                        imgPage.setLayoutParams(params);
                        imgPage.setBackgroundDrawable(contactIcon);
                    }
                }
            }


        } else if (item instanceof RelatedLocation) {
            try {
                //  List<Location> locations = appController.getLocationDao().queryForEq("id", ((RelatedLocation)item).getId());
                RealmResults<Location> locations = realm.where(Location.class).equalTo("id", (item.getRelatedLocation().getId())).findAll();
                if (locations != null && locations.size() > 0) {
                    titleTv.setText(locations.get(0).getTitle());

                    relatedDesc.setText("See on the map...");
                    relatedDesc.setTextAppearance(getActivity(), style.TextAppearance_Small);
                    relatedDesc.setTextColor(colorSelector_);//colors.getColor(colors.getBody_color()));
                    relatedDesc.setVisibility(View.VISIBLE);
                    relatedDesc.setTypeface(relatedDesc.getTypeface(), Typeface.NORMAL);

                    //  List<Locations_group> locationGrps = appController.getLocationGroupDao().queryForAll();
                    RealmResults<Locations_group> locationGrps = realm.where(Locations_group.class).findAll();
                    if (locationGrps != null && locationGrps.size() > 0) {

                        BitmapDrawable contactIcon = null;
                        for (int i = 0; i < locationGrps.size(); i++) {
                            RealmList<Location> locations_ = new RealmList<>(/*locationGrps.get(i).getLocations()*/);
                            if (locations_.size() > 0) {
                                if (locations_.get(0).getId() == locations.get(0).getId()) {
                                    noImageUse = true;
                                    relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.VISIBLE);
                                    imgPage.setVisibility(View.VISIBLE);
                                    contactIcon = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(locationGrps.get(i).getPin_icon())));
                                    contactIcon.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"), Mode.MULTIPLY));
                                    int size = (int) Utils.dpToPixels(getActivity(), 32f);
                                    LayoutParams params = new LayoutParams(size, size);
                                    imgPage.setLayoutParams(params);
                                    imgPage.setBackgroundDrawable(contactIcon);
                                }
                            }


                        }

                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (!noImageUse) {
            Object illustration = item.getItemIllustration1();
            if (illustration != null) {

                if (illustration instanceof Illustration) {
                    Illustration image = (Illustration) illustration;
                    String path = "";
                    if (!image.getPath().isEmpty()) {

                        if (isTablet) {
                            int width_ = (int) Utils.dpToPixels(getActivity(), 60f);
                            int height_ = (int) Utils.dpToPixels(getActivity(), 40f);
                            ((LayoutParams) imgPage.getLayoutParams()).width = width_;
                            ((LayoutParams) imgPage.getLayoutParams()).height = height_;
                            ((LayoutParams) ((View) imgPage.getParent()).getLayoutParams()).height = width_;
                        } else {
                            int width_ = (int) Utils.dpToPixels(getActivity(), 70f);
                            int height_ = (int) Utils.dpToPixels(getActivity(), 60f);
                            ((LayoutParams) imgPage.getLayoutParams()).width = width_;
                            ((LayoutParams) imgPage.getLayoutParams()).height = height_;
                            ((LayoutParams) ((View) imgPage.getParent()).getLayoutParams()).height = width_;
                        }
                        path = image.getPath();
                        Glide.with(getActivity()).load(new File(path)).dontAnimate().into(imgPage);
                    /**/
                    } else {
                        path = image.getLink();
                        Glide.with(getActivity()).load(path).dontAnimate().into(imgPage);
                    }
                    //					imageLoader.displayImage(path, imgPage, options);

                } else if (illustration instanceof String) {

                    String icon = (String) illustration;
                    int size = (int) Utils.dpToPixels(getActivity(), 32f);
                    LayoutParams params = new LayoutParams(size, size);
                    imgPage.setLayoutParams(params);

                    try {


                        BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(icon)));

                        //						stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawable1);
                        StateListDrawable stateListDrawable = new StateListDrawable();

                        drawable.setColorFilter(new PorterDuffColorFilter(
                                colors.getColor(colors.getForeground_color()),
                                PorterDuff.Mode.MULTIPLY));

                        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
                        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getBackground_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
                        //						stateListDrawable.addState(new int[]{}, drawable2);

                        imgPage.setBackgroundDrawable(stateListDrawable);
                        imgPage.setImageDrawable(drawable);


                        //						BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(icon)));
                        //						drawable.setColorFilter(new PorterDuffColorFilter(
                        //								colors.getColor(colors.getForeground_color()),
                        //								PorterDuff.Mode.MULTIPLY));
                        //						imgPage.setImageDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
               /* }*/
            } else {
                if (relatedView.findViewById(R.id.imgCategoryContainer) != null)
                    relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.GONE);
                else
                    imgPage.setVisibility(View.GONE);
            }
        }

            PackageManager pm = getActivity()
                    .getPackageManager();
            final boolean hasTelephony = pm
                    .hasSystemFeature(PackageManager.FEATURE_TELEPHONY);


            arrowImg.setPaint(getNewPaint());

            if (item instanceof Link) {

                if (((Link)item).getUrl().startsWith("tel://")) {
                    if (!hasTelephony) {
                        titleTv.setTextColor(colors.getColor(colors.getTitle_color()));
                        relatedDesc.setTextColor(colors.getColor(colors.getBody_color()));
                        relatedView.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                        arrowImg.setVisibility(View.GONE);
                    }
                }
            }


            final View viewFinal = relatedView;
            layoutInflater = inflater;
            final int indexTmp = index;
            relatedView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    long time = System.currentTimeMillis();
                    clickedView = v; /*realm.where(Category.class).equalTo("id",1159).findAll().get(0).getChildren_pages()*/
                    indexClicked = indexTmp;
                    if (item instanceof Link) {
                        Link link = (Link)item;

                        String url = link.getUrl();
                        if (url.startsWith("http")) {
                            if (url.contains("youtube")) {

                                Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
                                intent.putExtra("InfoActivity", ((MainActivity) getActivity()).infoActivity);
                                intent.putExtra("link", url);
                                startActivity(intent);
                            } else if (url.contains(".pdf")) {
                                /** Uness Modif **/
                                if (url.compareToIgnoreCase("http://backoffice.paperpad.fr/pdf/55/Menu_Paxton_Nouveau_Format_2.pdf") == 0) {

                                    new DownloadPdfFile(getActivity()).execute(url);

                                } else {
                                    Log.e(" PageFragment <=== url ", " : " + url);


                                    ((MainActivity) getActivity()).extras = new Bundle();
                                    //String url = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getAuto_open_url();
                                    ((MainActivity) getActivity()).extras.putString("link", /* URL to GoogleDoc => */"http://docs.google.com/gview?embedded=true&url=" +  /* URL to PDF => */url);
                                    //((MainActivity) getActivity()).extras.putString("link", *//** URL to GoogleDoc => **//*"http://docs.google.com/gview?embedded=true&url=" +  *//** URL to PDF => **//* "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf");
                                    WebViewFragment webFragment = new WebViewFragment();
                                    webFragment.setArguments(((MainActivity) getActivity()).extras);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, webFragment).addToBackStack(null).commit();

                                /*QPDFViewerFragment qpdfViewerFragment = QPDFViewerFragment.create(url);
                                //webFragment.setArguments(((MainActivity) getActivity()).extras);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, qpdfViewerFragment).addToBackStack(null).commit();*/

                                }

                            } else {
                                WebViewFragment webViewFragment = new WebViewFragment();
                                ((MainActivity) getActivity()).bodyFragment = "WebViewFragment";
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments
                                ((MainActivity) getActivity()).extras = new Bundle();
                                ((MainActivity) getActivity()).extras
                                        .putString("link", url);
                                webViewFragment
                                        .setArguments(((MainActivity) getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container,
                                                webViewFragment)
                                        .setTransition(
                                                FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .addToBackStack(null).commit();
                            }


                        } else if (url.startsWith("mailto:")) {
                            Intent mailer = new Intent(Intent.ACTION_SEND);
                            mailer.setType("message/rfc822");//text/plain
                            String email = url.replaceAll("mailto:", "");

                            mailer.putExtra(Intent.EXTRA_EMAIL,
                                    new String[]{email});
                            startActivity(Intent.createChooser(mailer,
                                    "Send email..."));
                        } else if (url.startsWith("tel:")) {
                            //						PackageManager pm = getActivity()
                            //								.getPackageManager();
                            //						boolean hasTelephony = pm
                            //								.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
                            if (hasTelephony) {
                                Intent intent = new Intent(
                                        Intent.ACTION_CALL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        }


                    }else if (item instanceof Category) {
                        Category category = (Category)item;
                        if (!design.equals("formule")) {

                            if (!category.getDisplay_type().equals("multi_select")) {
                            ((MainActivity)getActivity()).openCategory(category);

                            } else {
                                MultiSelectPagesFragment categoryFragment = new MultiSelectPagesFragment();
                                ((MainActivity) getActivity()).bodyFragment = "MultiSelectPagesFragment";
                                // In case this activity was started with special instructions from an Intent,
                                // pass the Intent's extras to the fragment as arguments
                                ((MainActivity) getActivity()).extras = new Bundle();
                                ((MainActivity) getActivity()).extras.putInt(
                                        "Category_id", category.getId());
                                categoryFragment
                                        .setArguments(((MainActivity) getActivity()).extras);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction( )
                                        .replace(R.id.fragment_container,
                                                categoryFragment)
                                        .setTransition(
                                                FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                                        .addToBackStack(null).commit();
                            }
                        } else {
                            //showPopup(category.getId(), viewFinal, layoutInflater);

                            List<MyInteger> list = page.getRelated().getList();

                            showPopup_(list, category.getId(), viewFinal, layoutInflater);

                        }
                    }else if (item instanceof Child_pages) {
                        Child_pages page  =(Child_pages)item;
                        realm.beginTransaction();
                        page.setVisible(true);
                        realm.commitTransaction();
                        if (!design.equals("formule")) {
                        ((MainActivity)getActivity()).openPage(page);

                        } else {
                            //Category category = appController.getCategoryByIdDB(page.getCategory().getId_category());

                            Category category = realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();
                            showPopup(category.getId(), viewFinal, layoutInflater);
                            //						List<RelatedCatIds> list = new ArrayList<RelatedCatIds>(page.getRelated().getRelatedCatIds());
                            //						showPopup_(list, viewFinal, layoutInflater);
                        }
                    } else if (item instanceof Contact) {
                        int sid = 0;


                        if (page.getCategory() != null && page.getCategory().getSection() != null) {
                            sid = page.getCategory().getSection().getId();// getId_section();
                        }

                        FormContactFragment forFrag = FormContactFragment.newInstance();
                        ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                        // In case this activity was started with special instructions from an Intent,
                        // pass the Intent's extras to the fragment as arguments
                        ((MainActivity) getActivity()).extras = new Bundle();
                        ((MainActivity) getActivity()).extras.putInt("Section_id_form", sid);
                        ((MainActivity) getActivity()).extras.putInt("Contact",((Contact)item).getId());// getId_contact());
                        forFrag.setArguments(((MainActivity) getActivity()).extras);
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        forFrag)
                                .setTransition(
                                        FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                                .addToBackStack(null).commit();
                    } else if (item instanceof RelatedContactForm) {
                        //					long time2 = System.currentTimeMillis();
                        //					time = System.currentTimeMillis();

                        int sid = 0;

                        if (page.getCategory() != null && page.getCategory().getSection() != null) {
                            sid = page.getCategory().getSection().getId();
                        }

                        FormContactFragment forFrag = FormContactFragment.newInstance();
                        ((MainActivity) getActivity()).bodyFragment = "FormFragment";//CategoryFragment";
                        // In case this activity was started with special instructions from an Intent,
                        // pass the Intent's extras to the fragment as arguments
                        RealmResults<Contact> list = null;
                        //					time = System.currentTimeMillis();
                        Log.i("PageFrag" + " time initialise frag", time + "");
                        // list = appController.getContactDao().queryForEq("id", ((RelatedContactForm)item).getId());

                        list = realm.where(Contact.class).equalTo("id",((RelatedContactForm)item).getId()).findAll();

                        ((MainActivity) getActivity()).extras = new Bundle();
                        ((MainActivity) getActivity()).extras.putInt("Section_id_form", sid);
                        ((MainActivity) getActivity()).extras.putInt("Contact", list.get(0).getId());
                        forFrag.setArguments(((MainActivity) getActivity()).extras);
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        forFrag)
                                .setTransition(
                                        FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        //Runtime.getRuntime().gc();
                        //					time2 = System.currentTimeMillis() - time2;
                        //					Log.i("PageFrag"+" time load formulaire", time2+"");
                    } else if (item instanceof RelatedLocation) {


                        ((MainActivity) getActivity()).extras = new Bundle();
                        //((RelatedLocation)item).getId()
                        RealmResults<Locations_group> locationGrps = null;
                        // locationGrps = appController.getLocationGroupDao().queryForAll();

                        locationGrps = realm.where(Locations_group.class).findAll();
                        Fragment mMapFragment = new MapV2Fragment();
                        ((MainActivity) getActivity()).extras.putInt("Section_id", locationGrps.get(0).getSection().getId());
                        mMapFragment.setArguments(((MainActivity) getActivity()).extras);
                        ((MainActivity) getActivity()).bodyFragment = "MapV2Fragment";

                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        mMapFragment).addToBackStack(null).commit();
                    }

                    time = time - System.currentTimeMillis();
                    Log.i("PageFrag" + "time", time + "");
                }
            });


        return relatedView;
    }


    public class DownloadPdfFile extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog dialog;

        public DownloadPdfFile(Activity activity) {
            dialog = new ProgressDialog(activity);

        }

        @Override
        protected Integer doInBackground(String... params) {
            downloadfile(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getResources().getString(R.string.waiting));
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {

            File file = new File(Environment.getExternalStorageDirectory(), "specificPDF.pdf");
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);//,Uri.parse("file://" +file));
            intent.setDataAndType(uri ,"application/pdf");
            //intent.setType("application/pdf");
            PackageManager pm = getActivity().getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (activities.size() > 0) {
                startActivity(intent);
            } else {
                ((MainActivity) getActivity()).extras = new Bundle();
                //String url = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getAuto_open_url();
                ((MainActivity) getActivity()).extras.putString("link", /** URL to GoogleDoc => **/"http://docs.google.com/gview?embedded=true&url=http://backoffice.paperpad.fr/pdf/55/Menu_Paxton_Nouveau_Format_2.pdf");
                //((MainActivity) getActivity()).extras.putString("link", /** URL to GoogleDoc => **/"http://docs.google.com/gview?embedded=true&url=" +  /** URL to PDF => **/ "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf");
                WebViewFragment webFragment = new WebViewFragment();
                webFragment.setArguments(((MainActivity) getActivity()).extras);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, webFragment).addToBackStack(null).commit();
            }
            dialog.hide();;
            super.onPostExecute(result);
        }


        public void downloadfile(String url_){
            try {
                //set the download URL, a url that points to a file on the internet
                //this is the file to be downloaded
                URL url = new URL(url_);

                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //and connect!
                urlConnection.connect();

                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
                File SDCardRoot = Environment.getExternalStorageDirectory();
                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File file = new File(SDCardRoot,"specificPDF.pdf");

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    publishProgress(totalSize/downloadedSize);
                    //updateProgress(downloadedSize, totalSize);

                }
                //close the output stream when done
                fileOutput.close();

                //catch some possible errors...
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // see http://androidsnippets.com/download-an-http-file-to-sdcard-with-progress-notification
        }
    }


    public void showPopup(int id_cat, View relatedView, LayoutInflater inflater) {

        // List<Category> categories = new ArrayList<Category>();
        RealmList<Category> categories = new RealmList<>();

        RealmResults<Category> cat = realm.where(Category.class).equalTo("id",id_cat).findAll(); //appController.getCategoryDao().queryForEq("id", id_cat);

        if(cat != null && cat.size() > 0) {
            if(cat.get(0).getId() == id_cat){
                RealmList<Child_pages> pages =(cat.get(0).getChildren_pages()); //fromCollectionToList(cat.get(0).getChildren_pages());
               // cat.get(0).setChildren_pages(pages);
                categories.add(cat.get(0));
            }

        }
        if (categories.size()>0) {

            View formulelist = inflater.inflate(R.layout.formule_list, null, false);

            mHAdapter = new FormulePageHAdapter(getActivity(),categories , colors, PagesFragment.this);

            HeaderListView HLview = (HeaderListView)formulelist.findViewById(R.id.formule_list_element);
            HLview.setBackgroundColor(Color.parseColor("#80ffffff"));//Color.parseColor("#80ffffff"));//colors.getColor(colors.getTitle_color(), "CC"));
            HLview.setAdapter(mHAdapter);


            pw = new PopupWindow(formulelist, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

            pw.setOutsideTouchable(true);
            //pw.setBackgroundDrawable(null);
            pw.setBackgroundDrawable(new PopupBackround(colors));
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

            Rect location = locateView(relatedView);
            if(isTablet){
                pw.setWidth(350);
                //pw.setHeight(450);
                pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top - (location.top - location.bottom)/2);
            }
            else {
                pw.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
            //pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top - (location.top - location.bottom)/2);
        }

    }


   /* public RealmList<Child_pages> fromCollectionToList(Collection<Child_pages> collectionPage){
        RealmList<Child_pages> pages = new RealmList<Child_pages>();
        Child_pages page = null;
        //		loc_grp.addAll(locationGroups);
        //		List<Location> loc = null;
        int i = 0;

        for (Iterator<Child_pages> iterator = collectionPage.iterator(); iterator.hasNext();) {
            page = (Child_pages) iterator.next();
            if(page.isVisible()) {
                pages.add(i,page);
                i++;
            }
        }
        return pages;
    }*/

    public void showPopup_(List<MyInteger> ids_cat, int id_cat, View relatedView, LayoutInflater inflater) {

        RealmList<Category> categories = new RealmList<Category>();
        for(int i = 0; i< ids_cat.size(); i++) {
            //List<Category> cat = appController.getCategoryDao().queryForEq("id", ids_cat.get(i).getLinked_id());
            RealmResults<Category> cat=realm.where(Category.class).equalTo("id", ids_cat.get(i).getMyInt()).findAll();

            for(int j = 0; j < cat.size(); j++) {
                if(cat.get(j).getId() == id_cat){
                    RealmList<Child_pages> pages = cat.get(j).getChildren_pages();//fromCollectionToList(cat.get(j).getChildren_pages());

                   // cat.get(j).setChildren_pages(pages);
                    categories.add(cat.get(j));

                }
            }
        }
        if (categories.size()>0) {

            View formulelist = inflater.inflate(R.layout.formule_list, null, false);

            mHAdapter = new FormulePageHAdapter(getActivity(),categories , colors, PagesFragment.this);
            //mHAdapter.setCallBack((com.euphor.paperpad.adapters.FreeFormulePageAdapter.CallbackRelatedLinks) FreeFormulaFragment.this);
            HeaderListView HLview = (HeaderListView)formulelist.findViewById(R.id.formule_list_element);
            HLview.setBackgroundColor(colors.getColor(colors.getTitle_color(), "CC"));
            HLview.setAdapter(mHAdapter);


            pw = new PopupWindow(formulelist, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

            pw.setOutsideTouchable(true);
            //pw.setBackgroundDrawable(null);
            pw.setBackgroundDrawable(new PopupBackround(colors));
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

            Rect location = locateView(relatedView);
            if(isTablet){
                pw.setWidth(350);
                //pw.setHeight(450);
                pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top - (location.top - location.bottom)/2);
            }
            else {
                pw.showAtLocation(view, Gravity.CENTER, 0, 0);
            }

        }

    }


    private View getNewDivider() {
        View divider = new View(getActivity());
        divider.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(colors.getColor(colors
                .getForeground_color()));
        return divider;
    }

    private Paint getNewPaint() {
        Paint paint = new Paint();
        paint.setColor(colors.getColor(colors.getForeground_color(),"AA"));
        return paint;
    }

    private ShapeDrawable getNewShape() {
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        int color1 = colors.getColor(colors.getForeground_color(), "FF");
        int color2 = colors.getColor(colors.getForeground_color(), "AA");
        Shader shader1 = new LinearGradient(0, 0, 0, 50, new int[] {
                color1, color2, color1  }, null, Shader.TileMode.CLAMP);
        shape.getPaint().setShader(shader1);
        shape.getPaint().setColor(Color.WHITE);
        shape.getPaint().setStyle(Paint.Style.FILL);
        return shape;
    }

    private int indexOfPage() {
        int index = -1;
        for (int i = 0; i < pages.size(); i++) {

            if (page.getId() == pages.get(i).getId()) {
                index = i;
            }

        }
        return index;
    }

    private OnClickListener getNavigationBtnListener(final int which) {
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Child_pages pageTo = null ;
                if (which == PREVIOUS) {

                    pageTo = getPreviousPage(indexCurrent);

                    if (pageTo!=null) {
                        //((MainActivity) getActivity()).extras = new Bundle();
                        ((MainActivity) getActivity()).extras.putInt("page_id",
                                pageTo.getId_cp());
                        PagesFragment pagesFragment = new PagesFragment();
                        ((MainActivity) getActivity()).bodyFragment = "PagesFragment";
                        pagesFragment
                                .setArguments(((MainActivity) getActivity()).extras);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment_container, pagesFragment)
                                .addToBackStack(null).commit();
                    }

                }else if (which == NEXT) {
                    if (indexCurrent == pages.size()-1) {
                        pageTo = pages.get(0);
                    }else {
                        pageTo = pages.get(indexCurrent+NEXT);
                    }
                    pageTo = getNextPage(indexCurrent);

                    if (pageTo!=null) {
                        //((MainActivity) getActivity()).extras = new Bundle();
                        ((MainActivity) getActivity()).extras.putInt("page_id",
                                pageTo.getId_cp());
                        PagesFragment pagesFragment = new PagesFragment();
                        ((MainActivity) getActivity()).bodyFragment = "PagesFragment";
                        pagesFragment
                                .setArguments(((MainActivity) getActivity()).extras);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                .replace(R.id.fragment_container, pagesFragment)
                                .addToBackStack(null).commit();
                    }

                }


            }
        };
        return listener;
    }

    protected Child_pages getNextPage(int pIndexCurrent) {
        Child_pages pageTo;
        if (pIndexCurrent >= pages.size()-1) {
            pageTo = pages.get(0);
        }else {
            pageTo = pages.get(pIndexCurrent+NEXT);
        }
        while (pageTo == null || (pageTo != null && !pageTo.isVisible())) {
            pageTo = getNextPage(pIndexCurrent+NEXT);

        }
        return pageTo;
    }

    protected Child_pages getPreviousPage(int pIndexCurrent) {
        Child_pages pageTo;
        if (pIndexCurrent>0) {
            pageTo = pages.get(pIndexCurrent+PREVIOUS);
        }else {
            pageTo = pages.get(pages.size()-1);
        }
        while (pageTo == null || (pageTo != null && !pageTo.isVisible())) {
            pageTo = getPreviousPage(pIndexCurrent+PREVIOUS);
        }
        return pageTo;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        //		imageLoader.clearMemoryCache();
        super.onDestroy();
    }

    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    @Override
    public void onChildPageClick(Child_pages child_pages) {


        Log.i("child_pages", ""+child_pages.toString());
        pw.dismiss();
        objects.remove(indexClicked);
        objects.add(indexClicked, child_pages);
        llRelated.removeAllViews();
        drawRelatedElements(objects, layoutInflater);
        //		clickedView.refreshDrawableState();
    }




    @Override
    public void onStop() {
        		realm = Realm.getInstance(getActivity());
        page=realm.where(Child_pages.class).findFirst();

        AppHit hit = new AppHit(System.currentTimeMillis() / 1000, time / 1000, "sales_page", page.getId());
        ((MyApplication) getActivity().getApplication()).hits.add(hit);

        super.onStop();
    }



}
