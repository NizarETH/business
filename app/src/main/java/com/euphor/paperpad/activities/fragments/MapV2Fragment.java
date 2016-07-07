package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
//import com.crashlytics.android.Crashlytics;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.MyCustomListLocations;
import com.euphor.paperpad.adapters.MyCustomListLocations.FromMyCustomListLocations;
import com.euphor.paperpad.Beans.Coordinates;
import com.euphor.paperpad.Beans.Location;
import com.euphor.paperpad.Beans.Locations_group;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.Beans.Street_view_default_position;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.MyLocationInfos;
import com.euphor.paperpad.utils.MyPhoneLocationInfo;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.MapWrapperLayout;
import com.euphor.paperpad.widgets.PinImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;


public class MapV2Fragment extends Fragment implements FromMyCustomListLocations/*, OnStreetViewPanoramaReadyCallback*/ /*implements TextWatcher*/ {

	private View view;
	private MapView mMapView;
	private GoogleMap gMap;
	private Bundle mBundle;  
	String TAG = "MapV2Fragment";



	private Colors colors;
	public static Section sectionMap;
	private Collection<Locations_group> locationGroups, loc_groups;
	private ArrayList<Location> locations;
	private SparseArray<List<Marker>> array;
	private Marker lastOpened;
	private MapWrapperLayout mapHolder;
	LinearLayout list_search_icon_container;


	public MyCustomListLocations mHAdapter = null;
	public HeaderListView HLview;
    public Realm realm;


	private TextView search_label; 
	private LinearLayout search_layout; 
	private EditText editTxt;
	private ArrowImageView list_search_icon;


	ImageView infoWinImg;
	private MyLocationInfos mLocationInfos;
	private MyPhoneLocationInfo mPhoneLocationInfos;
	private boolean isClicked = false;
	private Marker markerTmp = null;

	private boolean b;
	private Bundle bundle;
	private int Section_id;

    SupportStreetViewPanoramaFragment svp;
    private float bearing;


	//	protected void setOrientation() {
	//	    int current = getActivity().getRequestedOrientation();
	//	    // only switch the orientation if not in portrait
	//	    if ( current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) {
	//	        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
	//	    }
	//	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		if(mLocationInfos != null)
			mLocationInfos.dismiss();
		super.onConfigurationChanged(newConfig);
	}




	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try { 
			MapsInitializer.initialize(getActivity());
		} catch (Exception/*GooglePlayServicesNotAvailableException*/ e) {
			e.printStackTrace();
            //Crashlytics.log("MapsInitializer exception");
		}


		Log.d("TAG", "launching MapV2Fragment");
		view = inflater.inflate(R.layout.map_fragment, container, false);



		/** Uness Modif **/
		//view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		Log.i("Activity attached", getActivity().toString()); 
		//List<Section> sections = null;
//		try {
//			//sections = appController.getSectionsDao().queryForEq("type", "map");
//			sectionMap = appController.getSectionsDao().queryForId(getArguments().getInt("Section_id"));
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		//		Log.i("  sections.get(0).getShow_left_panel() ", ""+sections.get(0).getShow_left_panel()); 
//		Log.i("  sectionMap.getShow_left_panel() ", ""+sectionMap.getShow_left_panel()); 

		if(sectionMap != null && sectionMap.isShow_left_panel()) {

			b = true;
			list_search_icon_container = (LinearLayout) view.findViewById(R.id.list_search_icon_container);
			view.findViewById(R.id.list_search_layout).setBackgroundColor(colors.getColor(colors.getBackground_color()));
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
			list_search_icon_container.setBackgroundDrawable(drawable); //664


			list_search_icon = (ArrowImageView) view.findViewById(R.id.list_search_icon);

			search_label = (TextView)view.findViewById(R.id.search_label);
			if(search_label != null){
				search_label.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
						new int[]{colors.getColor(colors.getTitle_color()), 
						colors.getColor(colors.getBackground_color())}));
			}

			Paint paint1 = new Paint();

			paint1.setColor(colors.getColor(colors.getBackground_color()));
			list_search_icon.setPaint(paint1);

			//((LinearLayout)view.findViewById(R.id.arrowContainer)).setLayoutParams(new LinearLayout.LayoutParams(, ));
			list_search_icon_container.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {

					Paint paint1 = new Paint();
					paint1.setColor(colors.getColor(colors.getTitle_color()));
					list_search_icon.setPaint(paint1);
				}
			});



			list_search_icon_container.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!isListSearchShown()) {

						showListSearch();
						//list_search_icon.setRotationY(180);

					}else {

						hideListSearch();
						//list_search_icon.setRotationY(0);

					}


				}
			});
		}
		else {
			b = false;
			view.findViewById(R.id.list_search_layout).setVisibility(View.GONE);
			view.findViewById(R.id.list_search_icon_container).setVisibility(View.GONE);

		}

		/** Fin Modif **/

        if(sectionMap.isEnable_street_view()){

/*
            Toast.makeText(getActivity(),
                    " StreetView latitude : "+sectionMap.getStreet_view_default_position().getStreet_view_latitude()
                             +", longitude : "+sectionMap.getStreet_view_default_position().getStreet_view_longitude()
                             +", orientation : "+sectionMap.getStreet_view_default_position().getStreet_view_orientation(), Toast.LENGTH_LONG).show();
*/


            TextView streetviewBtn = (TextView)view.findViewById(R.id.streetviewBtn);
            //Drawable streetview = getResources().getDrawable(R.drawable.streetview);
            //streetviewBtn.setCompoundDrawables(streetview, null, null, null);
            streetviewBtn.setVisibility(View.VISIBLE);
            streetviewBtn.setBackgroundColor(colors.getColor(colors.getForeground_color()));
			streetviewBtn.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
			streetviewBtn.setTypeface(MainActivity.FONT_ITALIC);
            streetviewBtn.setTextColor(colors.getColor(colors.getBackground_color()));

            //svp = new SupportStreetViewPanoramaFragment();
            //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, svp)/*.hide(svp)*/.addToBackStack("SupportStreetViewPanoramaFragment").commit();

            streetviewBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Street_view_default_position strViewDefaultPosition = sectionMap.getStreet_view_default_position();
                    float bearing = strViewDefaultPosition.getStreet_view_orientation();

                    StreetViewFragment streetViewFragment = StreetViewFragment.newInstance(strViewDefaultPosition.getStreet_view_latitude(), strViewDefaultPosition.getStreet_view_longitude(), bearing);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, streetViewFragment, "StreetViewFragment").addToBackStack("StreetViewFragment").commit();
                }
            });

        }

		mapHolder = (MapWrapperLayout) view.findViewById(R.id.mapHolder);

		View v;
		v = getMapViewFromConstructor();
		if (mMapView != null)
			mMapView.onCreate(savedInstanceState);

		gMap = mMapView.getMap();
		// MapWrapperLayout initialization
		// 39 - default marker height
		// 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
		mapHolder.init(gMap, getPixelsFromDp(getActivity(), 39 + 20)); 



		mapHolder.addView(v);
		return view;
	}

	private View getMapViewFromConstructor() {
		GoogleMapOptions opt = new GoogleMapOptions();
		mMapView = new MapView(getActivity(), opt);
		return (mMapView);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);


        if (getArguments() != null) {
			Section_id = getArguments().getInt("Section_id");
		}
		
		mBundle = savedInstanceState;
		setRetainInstance(true);
		((MainActivity) getActivity()).bodyFragment = "MapV2Fragment";
		bundle =  getArguments();
		if (bundle != null && !bundle.isEmpty()) {
			cameraPosition = bundle.getParcelable("position");
			((MainActivity) getActivity()).extras = bundle;
		}else if (mBundle!=null && mBundle.getParcelable("position") != null) {
			cameraPosition = mBundle.getParcelable("position");
			((MainActivity) getActivity()).extras = mBundle;
		}
		
		else {
			((MainActivity) getActivity()).extras = new Bundle();
		}

        if (Section_id != 0) {
            sectionMap = realm.where(Section.class).equalTo("id",Section_id).findFirst();
            //appController.getSectionsDao().queryForId(Section_id);
        }else {
            try {
                sectionMap = realm.where(Section.class).equalTo("type", "map").findFirst();
                //appController.getSectionsDao().queryForEq("type", "map").get(0);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        if (sectionMap != null /*sections.size() > 0*/) {
			
			//			sectionMap = sections.get(0);
			locationGroups = sectionMap.getLocations_groups();
			locations = new ArrayList<Location>();
			for (Iterator<Locations_group> iterator = locationGroups.iterator(); iterator
					.hasNext();) {
				Locations_group group = (Locations_group) iterator.next();
				locations.addAll(group.getLocations());

			}
			CENTER_LOCATION = new LatLng(sectionMap.getCenter_location().getLatitude(), sectionMap.getCenter_location().getLongitude());

            if (((MainActivity) getActivity()).extras != null) {
                ((MainActivity) getActivity()).extras.putInt("Section_id", sectionMap.getId());
            }else {
                ((MainActivity) getActivity()).extras = new Bundle();
                ((MainActivity) getActivity()).extras.putInt("Section_id", sectionMap.getId());
            }

        }

        isTablet = Utils.isTablet(getActivity());


	}

	public void onResume() {
		super.onResume();
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception/*GooglePlayServicesNotAvailableException*/ e) {
            e.printStackTrace();
            //Crashlytics.log("MapsInitializer exception");
        }

		((MainActivity) getActivity()).bodyFragment = "MapV2Fragment";
		Bundle bundle =  getArguments();
		if (bundle != null && !bundle.isEmpty()) {
			cameraPosition = bundle.getParcelable("position");
			((MainActivity) getActivity()).extras = bundle;
		}else if (mBundle!=null && mBundle.getParcelable("position") != null) {
			cameraPosition = mBundle.getParcelable("position");
			((MainActivity) getActivity()).extras = mBundle;
		}
		else {
			((MainActivity) getActivity()).extras = new Bundle(); 
		}
		//		setRetainInstance(true); 
		//		List<Section> sections = new ArrayList<Section>();
        if (getArguments() != null && getArguments().getInt("Section_id") != 0) {
            sectionMap = realm.where(Section.class).equalTo("id",getArguments().getInt("Section_id")).findFirst();
            //appController.getSectionsDao().queryForId(getArguments().getInt("Section_id"));
        }else {
            try {
                sectionMap = realm.where(Section.class).equalTo("type", "map").findFirst();
                //appController.getSectionsDao().queryForEq("type", "map").get(0);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (sectionMap != null /*sections.size() > 0*/) {
			//sectionMap = sections.get(0);
			locationGroups = sectionMap.getLocations_groups();
			locations = new ArrayList<Location>();
			for (Iterator<Locations_group> iterator = locationGroups.iterator(); iterator
					.hasNext();) {
				Locations_group group = (Locations_group) iterator.next();
				locations.addAll(group.getLocations());

			}
			CENTER_LOCATION = new LatLng(sectionMap.getCenter_location().getLatitude(), sectionMap.getCenter_location().getLongitude());
		}
		mMapView.onResume();
		mMapView.bringToFront();
        //mMapView.setStreetView(true);
		gMap = mMapView.getMap();
        //gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		final View mapView = mMapView; //getSupportFragmentManager().findFragmentById(R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					// remove the listener
					// ! before Jelly Bean:
					mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					// ! for Jelly Bean and later:
					//mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					// set map viewport
					// CENTER is LatLng object with the center of the map
					if(markerTmp != null) {
						gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerTmp.getPosition(), gMap.getCameraPosition().zoom));
						// ! you can query Projection object here

						Projection projection = gMap.getProjection();
						Point markerCenter = projection.toScreenLocation(markerTmp.getPosition());

						float tiltFactor = (90 - gMap.getCameraPosition().tilt) / 90;


						WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
						Display display = wm.getDefaultDisplay();


						int width = display.getWidth();  
						int height = display.getHeight(); 

						DisplayMetrics metrics = new DisplayMetrics(); 

						getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 

						if(height > width) 
						{ 
							if((metrics.densityDpi >= 213 && metrics.densityDpi <= 219) || metrics.densityDpi == 240) {
								markerCenter.y -= 205 * tiltFactor;
							}else if(metrics.densityDpi > 219 && metrics.densityDpi < 480) {
								markerCenter.y -= 300 * tiltFactor;
							}else if(metrics.densityDpi >= 480) {
								markerCenter.y -= 400 * tiltFactor;
							}

							hideListSearch();
						}
						//					else
						//						marketCenter.y -= 20 * tiltFactor;
						LatLng fixLatLng = projection.fromScreenLocation(markerCenter);

						//				    markerTmp.setPosition(fixLatLng);

						gMap.animateCamera(CameraUpdateFactory.newLatLng(fixLatLng), null);
						// ! example output in my test code: (356, 483)
						System.out.println(markerCenter);
						Log.e("   Screen Point by Projection : ", " "+markerCenter);
					}
				}
			});
		}
		// MapWrapperLayout initialization
		// 39 - default marker height
		// 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
		mapHolder.init(gMap, getPixelsFromDp(getActivity(), 39 + 20)); 

		if(isTablet) {
			mLocationInfos = new MyLocationInfos(getActivity(),	realm, colors , sectionMap.getLocations_groups());
			mLocationInfos.setItineraryButton(sectionMap.isShow_itinerary_button());
		}else {
			//mPhoneLocationInfos = new MyPhoneLocationInfo(getActivity());
			mPhoneLocationInfos = new MyPhoneLocationInfo(getActivity(),realm, colors, sectionMap.getLocations_groups());

			mPhoneLocationInfos.setItineraryButton(sectionMap.isShow_itinerary_button());
		}

		/** Modif Uness **/
		//String postal_code = getActivity().getSharedPreferences("Prefs",getActivity().MODE_MULTI_PROCESS).getString("postal_code", "");
		editTxt = (EditText)getActivity().findViewById(R.id.code_postal_search_bar);
		Drawable drawable = editTxt.getBackground();
		drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "AA"),PorterDuff.Mode.MULTIPLY));
		editTxt.setBackgroundDrawable(drawable);
		
		editTxt.setHintTextColor(colors.getColor(colors.getTitle_color(),"88")); 
		String POLICE = "fonts/gill-sans-light.ttf";
		editTxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), POLICE), Typeface.ITALIC);
		editTxt.setTextColor(colors.getColor(colors.getTitle_color()));
		//editTxt.setTextSize(22);

		//		GradientDrawable bgShape = (GradientDrawable)editTxt.getBackground();
		//		bgShape.setColor(Color.BLACK);

		editTxt.setSelection(editTxt.getText().toString().length());
		//editTxt.addTextChangedListener(this);
		//if(postal_code.isEmpty())	

		editTxt.addTextChangedListener(new TextWatcher() {


			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				UpdateListByPostalCode(cloneList(fromCollectionToList(loc_groups)), s.toString());
			}


		}); 



		editTxt.setText(getActivity().getSharedPreferences("Prefs",getActivity().MODE_MULTI_PROCESS).getString("postal_code", ""));
		//		boolean b = getActivity().getSharedPreferences("Prefs",getActivity().MODE_MULTI_PROCESS).getBoolean("isListShown", false);
		//
		if(b)
		{
			//list_search_icon.setRotationY(180); //setImageDrawable(getActivity().getResources().getDrawable(R.drawable.left_flech));
			//Log.e("  locationGroups "+locationGroups, "  sections.get(0).getShow_left_panel() "+sections.get(0).getShow_left_panel()); 
			Log.e("  locationGroups "+locationGroups, "  sections.get(0).getShow_left_panel() "+sectionMap.isShow_left_panel());

			RemovePostalCodeEditor(fromCollectionToList(locationGroups));
			//UpdateListByPostalCode(fromCollectionToList(locationGroups), "dever" );
			//view.findViewById(R.id.code_postal_search_bar).setVisibility(View.GONE);

			showListSearch();

		}else {

		}


		if(!isTablet)
			hideListSearch();

/*        if(svp != null) {
            svp.getStreetViewPanoramaAsync(this);
        }*/

/*        if(svp != null){
            svp.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
                @Override
                public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {

                    Street_view_default_position strViewDefaultPosition = sectionMap.getStreet_view_default_position();
                    LatLng position = new LatLng(strViewDefaultPosition.getStreet_view_latitude(), strViewDefaultPosition.getStreet_view_longitude());
                    float bearing = 180f;//strViewDefaultPosition.getStreet_view_orientation();
                    position = new LatLng(37.765927, -122.449972);
                    streetViewPanorama.setPosition(position);
                    StreetViewPanoramaOrientation orientation = new StreetViewPanoramaOrientation(0f, bearing);
                    StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder().bearing(bearing).orientation(orientation).build();
                    streetViewPanorama.animateTo(camera, 1);
                }
            });
        }*/

        //svp.getStreetViewPanoramaAsync(((MainActivity)getActivity()));



    }

	public void onPause() {
		super.onPause();

		/** Uness Modif **/
		//		getActivity().getSharedPreferences("Prefs",getActivity().MODE_MULTI_PROCESS).edit().putBoolean("isListShown", ((LinearLayout)getActivity().findViewById(R.id.list_search_layout)).isShown()).commit();
		getActivity().getSharedPreferences("Prefs",getActivity().MODE_MULTI_PROCESS).edit().putString("postal_code", editTxt.getText().toString()).commit();

		cameraPosition = gMap.getCameraPosition();
		((MainActivity) getActivity()).extras = new Bundle();
		((MainActivity) getActivity()).extras.putParcelable("position", cameraPosition);
		mMapView.onPause();

	}

	public void onDestroy() {
		super.onDestroy();
		cameraPosition = gMap.getCameraPosition();
		((MainActivity) getActivity()).extras = new Bundle();
		((MainActivity) getActivity()).extras.putParcelable("position", cameraPosition);
		mMapView.onDestroy();
	}

	static LatLng CENTER_LOCATION = new LatLng(34.011795, -6.82457);
	static final LatLng RABAT = new LatLng(34.011795, -6.82457);
	static final LatLng CASABLANCA = new LatLng(33.584987, -7.634811);
	private static final int SIZE_ICON_PIN = 36;


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gMap = mMapView.getMap();
		/** Zoom control **/
		// MapWrapperLayout initialization
		// 39 - default marker height
		// 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
		mapHolder.init(gMap, getPixelsFromDp(getActivity(), 39 + 20)); 


		//		gMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity(),
		//				appController, imageLoader, colors));

		// HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		array = new SparseArray<List<Marker>>();

		loc_groups = new ArrayList<Locations_group>();

		int location_group_id = 0;

		if(getArguments() != null)
			location_group_id = getArguments().getInt("location_group_id",0);

		String markerTmpId = "";

		if(location_group_id != 0 && !(""+location_group_id).isEmpty()){


			if(locationGroups.size() > 0) {

				//listAll = new ArrayList<List<Marker>>();

				for (Iterator<Locations_group> iterator = locationGroups.iterator(); iterator
						.hasNext();) {
					Locations_group group = (Locations_group) iterator.next();

					if(location_group_id == group.getId()){ 

						loc_groups.add(group);

						boolean isHide = group.isHide();
						List<Marker> listMarkers = new ArrayList<Marker>();
						for (Iterator<Location> iterator2 = group.getLocations()
								.iterator(); iterator2.hasNext();) {
							Location location = (Location) iterator2.next();


							Coordinates coordinates = location.getCoordinates();
							Marker marker;
							//isHide = false;
							marker =gMap.addMarker((new MarkerOptions()
							.position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
							.title(location.getTitle())
							.snippet("" + location.getId())
							//							.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(group)))
							.icon(BitmapDescriptorFactory.fromBitmap(getSingleDrawable(buildPinIcon(group))))
							.anchor(0.5f, 0.5f)));

							marker.setVisible(true);   

							/** Uness Modif showInfoWindow if is_Default is set **/


							if((markerTmpId).compareTo(""+location.getId()) == 0)
							{

								isClicked = true;

							}else
								if(location.isIs_default() && isTablet){
									markerTmp = marker;
									isClicked = true;
									Log.e(" MArker Position  << isIs_defaultn  >>", " Latitude : "+markerTmp.getPosition().latitude+ "     Longitude : "+markerTmp.getPosition().longitude);

								}
								else if (isHide) {
									marker.setVisible(false);
								}

						}
						if(group.isIs_itinerary()) {

							for(int i = 0; i< listMarkers.size() - 1; i++) {
								Polyline line = gMap.addPolyline(new PolylineOptions()
								.add(listMarkers.get(i).getPosition(), listMarkers.get(i+1).getPosition())
								.width(3)
								.color(colors.getColorDefault(group.getPin_color(), "000000","CC")));
								line.setVisible(true);
							}
						}
						array.append(group.getId(), listMarkers);
					}

				}


			}
		}

		else if(locationGroups != null && locationGroups.size() > 0) {
			// listAll = new ArrayList<List<Marker>>();
			for (Iterator<Locations_group> iterator = locationGroups.iterator(); iterator
					.hasNext();) {
				Locations_group group = (Locations_group) iterator.next();

				loc_groups.add(group);

				boolean isHide = group.isHide();
				List<Marker> listMarkers = new ArrayList<Marker>();
				for (Iterator<Location> iterator2 = group.getLocations()
						.iterator(); iterator2.hasNext();) {
					Location location = (Location) iterator2.next();



					Coordinates coordinates = location.getCoordinates();
					Marker marker =gMap.addMarker((new MarkerOptions()
					.position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
					.title(location.getTitle())
					.snippet("" + location.getId())
					//					.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(group)))
					.icon(BitmapDescriptorFactory.fromBitmap(getSingleDrawable(buildPinIcon(group))))
					.anchor(0.5f, 0.5f)));   

					/** Uness Modif showInfoWindow if is_Default is set **/


					if(!markerTmpId.isEmpty() && (markerTmpId).compareTo(""+location.getId()) == 0)
					{
						Log.e("   Inside = markerTmpId ===>", markerTmpId+"   == "+location.getId()+" ?");

						isClicked = true;

					}else 
						if(location.isIs_default() && isTablet){


							markerTmp = marker;
							isClicked = true;
							Log.e(" MArker Position  << isIs_defaultn  >>", " Latitude : "+markerTmp.getPosition().latitude+ "     Longitude : "+markerTmp.getPosition().longitude);

						}


					listMarkers.add(marker);
					if (isHide) {
						marker.setVisible(false);
					}
				}

				if(group.isIs_itinerary()) {

					for(int i = 0; i< listMarkers.size() - 1; i++) {
						Polyline line = gMap.addPolyline(new PolylineOptions()
						.add(listMarkers.get(i).getPosition(), listMarkers.get(i+1).getPosition())
						.width(3)
						.color(colors.getColorDefault(group.getPin_color(), "000000","CC")));
						line.setVisible(true);
					}
				}

				array.append(group.getId(), listMarkers);

			}
		}


		lastOpened = null;


		gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {


				if (lastOpened != null) {
					// Close the info window
					//lastOpened.hideInfoWindow();
					if(isTablet)

						mLocationInfos.hide();

					else
						mPhoneLocationInfos.hide();

					// Is the marker the same marker that was already open
					if (lastOpened.equals(marker)) {
						// Nullify the lastOpened object
						lastOpened = null;
						// Return so that the info window isn't opened again
						return true;
					}
				}

				getInfoWindowZone(marker, gMap.getProjection().toScreenLocation(marker.getPosition()), -1);


				lastOpened = marker;

				return true;
			}
		});




		gMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition cam) {

				if(isClicked){
					//Log.e(" MArker Position  << setOnCameraChangeListener  >>", " Latitude : "+markerTmp.getPosition().latitude+ "     Longitude : "+markerTmp.getPosition().longitude);
					
					try {
						WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
						Display display = wm.getDefaultDisplay();

						int width = display.getWidth();  
						int height = display.getHeight(); 
						if(height > width) { //((WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
							//						if(metrics.densityDpi >= 213)
							getInfoWindowZone(markerTmp, gMap.getProjection().toScreenLocation(markerTmp.getPosition()),  270);
							//					else
							//						getInfoWindowZone(markerTmp, gMap.getProjection().toScreenLocation(markerTmp.getPosition()),400, mLocationInfos.getHeight(), 180);

							hideListSearch();
						}
						else
							getInfoWindowZone(markerTmp, gMap.getProjection().toScreenLocation(markerTmp.getPosition()), 180);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					isClicked = false;
				}


			}
		});



		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);


		DisplayMetrics metrics = new DisplayMetrics(); 

		wm.getDefaultDisplay().getMetrics(metrics); 



		if (cameraPosition != null) {
			//cameraPosition.zoom = 15;
			/** Uness Modif **/ 
			gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, cameraPosition.zoom));

		}else {
			int zoom = zoomLevel((sectionMap.getDefault_display_distance()));
			zoom = zoom - 2;
			//zoom = sectionMap.getDefault_display_distance();
			// Move the camera instantly to hamburg with a zoom of 15.
			/** Uness Modif**/ 
			gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER_LOCATION, zoom));

			//			CameraUpdateFactory.newLatLng
			// Zoom in, animating the camera.
			//			gMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
		}

		//		Projection projection = gMap.getProjection();
		//		VisibleRegion visibleArea = projection.getVisibleRegion();
		//		Log.i("TAG VisibleRegion", visibleArea.toString());


		/** Uness Modif Methode 2 **/


		search_layout = (LinearLayout)getActivity().findViewById(R.id.list_search_layout);
		if (!isTablet) {
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			int deviceWidth = dm.widthPixels;
			int width_search_layout = (int) ((float)deviceWidth*85/(float)100);
			RelativeLayout.LayoutParams params_search =(android.widget.RelativeLayout.LayoutParams) search_layout.getLayoutParams();
			params_search.width = width_search_layout;
			//			params_search = new RelativeLayout.LayoutParams(width_search_layout, RelativeLayout.LayoutParams.MATCH_PARENT);
			search_layout.setLayoutParams(params_search);

			//			gMap.getUiSettings().setZoomControlsEnabled(false);

		}

		if(view.findViewById(R.id.forDiff) != null)
			gMap.getUiSettings().setZoomControlsEnabled(false);

		mHAdapter = new MyCustomListLocations(cloneList(fromCollectionToList(loc_groups)), this, colors); 
		HLview = (HeaderListView)search_layout.findViewById(R.id.carte_listView);
		HLview.setBackgroundColor(colors.getColor(colors.getSide_tabs_separators_color(), "40"));
		HLview.setAdapter(mHAdapter);



		LinearLayout titleHolder = (LinearLayout) view
				.findViewById(R.id.mapTitleHolder);
		LinearLayout legend = (LinearLayout) view
				.findViewById(R.id.legend);
		legend.setBackgroundDrawable(colors.getForePD());

		if (loc_groups.size() > 0) {
			for (Iterator<Locations_group> iterator = loc_groups.iterator(); iterator
					.hasNext();) {
				Locations_group group = (Locations_group) iterator.next();
				LinearLayout linearLayout = new LinearLayout(getActivity());
				LayoutParams paramsLL = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				linearLayout.setLayoutParams(paramsLL);
				linearLayout.setOrientation(LinearLayout.HORIZONTAL);
				paramsLL.setMargins(5, 5, 5, 5);

				TextView tView = new TextView(getActivity());
				tView.setTypeface(MainActivity.FONT_BODY);
				LayoutParams paramsTxt = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramsTxt.gravity = Gravity.CENTER_VERTICAL;
				tView.setLayoutParams(paramsTxt);
				tView.setGravity(Gravity.CENTER_VERTICAL);
				tView.setText(group.getTitle());
				tView.setTextColor(colors.getColor(colors.getBackground_color()));

				RelativeLayout layout = new RelativeLayout(getActivity());
				LayoutParams params = new LayoutParams(60, 60);
				layout.setLayoutParams(params);
				layout.setGravity(Gravity.CENTER);
				params.gravity = Gravity.CENTER;

				PinImageView img = new PinImageView(getActivity());
				Paint paint = new Paint();
				paint.setColor(colors.getColorDefault(group.getPin_color(), "000000", "FF"));
				paint.setAntiAlias(true);
				img.setPaint(paint);
				LayoutParams imgParams = new LayoutParams(SIZE_ICON_PIN,
						SIZE_ICON_PIN);
				img.setLayoutParams(imgParams);
				imgParams.gravity = Gravity.CENTER;

				layout.addView(img);

				ImageView img2 = new ImageView(getActivity());
				LayoutParams imgParams2 = new LayoutParams(SIZE_ICON_PIN,
						SIZE_ICON_PIN);
				img2.setLayoutParams(imgParams2);
				imgParams2.gravity = Gravity.CENTER;
				BitmapFactory.Options myOptions = new BitmapFactory.Options();
				myOptions.inDither = true;
				myOptions.inScaled = false;
				myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
				myOptions.inPurgeable = true;
				try {
					img2.setImageBitmap(BitmapFactory.decodeStream(getActivity().getAssets().open(group.getPin_icon()), null, myOptions));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
					// TODO: handle exception
				}
				img2.setPadding(7, 7, 7, 7);
				layout.addView(img2);

				layout.setTag(group);
				linearLayout.addView(layout);
				linearLayout.addView(tView);
				titleHolder.addView(linearLayout);
				if (group.isHide()) {
					layout.setAlpha(0.5f);
				}
				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Locations_group groupp = (Locations_group) v.getTag();
						List<Marker> list = array.get(groupp.getId());
						for (int i = 0; i < list.size(); i++) {

							if (list.get(i).isVisible()) {
								list.get(i).setVisible(false);
								v.setAlpha(0.5f);
							}else {
								list.get(i).setVisible(true);
								v.setAlpha(0.9f);
							}
						}
					}
				});
			} 

		}

        //if(svp != null)
        //    svp.getStreetViewPanoramaAsync((OnStreetViewPanoramaReadyCallback) getActivity());


	}

	public void getInfoWindowZone(Marker marker, Point position,  int zonePreDefined) {


		if(isTablet) {

			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			//		mLocationInfos.dismiss();



			int width = display.getWidth();  
			int height = display.getHeight();  

			int zone = -1;


			int x, y;// OFFSET;

			x = 0; y = 0;		
			//y +=  OFFSET;


			Point origine = new Point(0, 0);



			if(MainActivity.positionNav == 0 || MainActivity.positionNav == 1 || !isTablet) {



				if(view.findViewById(R.id.forDiff)!= null || !isTablet) {
					y = ((LinearLayout) getActivity().findViewById(R.id.legend)).getHeight();
					origine = new Point(0, (int) (height - mapHolder.getMeasuredHeight() - getActivity().getResources().getDimension(R.dimen.height_tab_fragment_bottom)));
				}


			}else {

				if(view.findViewById(R.id.forDiff)!= null) {
					//y = ((LinearLayout) getActivity().findViewById(R.id.legend)).getHeight();
					origine = new Point(width - mapHolder.getMeasuredWidth(), (int) (height -mapHolder.getMeasuredHeight()));// - getActivity().getResources().getDimension(R.dimen.height_tab_fragment_bottom)));
				}else
					origine = new Point(width - mapHolder.getMeasuredWidth(), 0);


			}

			mLocationInfos.setDimensionByMarkerLocation(marker);

			//width = mapHolder.getMeasuredWidth();
			//height = mapHolder.getMeasuredHeight();


			if(height > width) {
				if((position.x > mLocationInfos.getWidth()/2 + 50) && ((position.x + mLocationInfos.getWidth()/2) < width - 50))
				{
					if(position.y > (mLocationInfos.getHeight() + 100) &&position.y + mLocationInfos.getHeight() > height - 100) {

						zone = 270;

					}else if(position.y < mLocationInfos.getHeight() - 100 && position.y + mLocationInfos.getHeight() < height - 100) {

						zone = 90;

					}

				}

				if((position.x > mLocationInfos.getWidth() + 50) /*&& (position.x + mLocationInfos.getWidth()) >= width - 50*/) {
					if((position.y > ( mLocationInfos.getHeight()/2)) && (height > (position.y + ( mLocationInfos.getHeight()/2 + 50) ))) {
						zone = 0;
					}


				}
				else if((position.x + mLocationInfos.getWidth() < width - 100) && ((position.y > mLocationInfos.getHeight()/2 - 50)  && (position.y+ mLocationInfos.getHeight()/2 < height - 50))) {
					zone = 180;
				}

			}
			else {

				if((position.x > mLocationInfos.getWidth() + 50) /*&& (position.x + mLocationInfos.getWidth()) >= width - 50*/) {
					if((position.y > ( mLocationInfos.getHeight()/2)) && (height > (position.y + ( mLocationInfos.getHeight()/2 + 50) ))) {
						zone = 0;
					}

				}
				else if((position.x + mLocationInfos.getWidth() < width - 100) && ((position.y > mLocationInfos.getHeight()/2 - 50)  && (position.y+ mLocationInfos.getHeight()/2 < height - 50))) {
					zone = 180;
				}

				if((position.x > mLocationInfos.getWidth()/2 + 50) && ((position.x + mLocationInfos.getWidth()/2) < width - 50))
				{
					if(position.y > (mLocationInfos.getHeight() + 100) && position.y + mLocationInfos.getHeight() < height - 100) {

						zone = 270;

					}else if(position.y < mLocationInfos.getHeight() + 100 && position.y + mLocationInfos.getHeight() < height - 100) {

						zone = 90;

					}
				}


			}


			if(zonePreDefined  != -1) {
				zone = zonePreDefined;
			}

			DisplayMetrics metrics = new DisplayMetrics(); 
			//		int orientation = display.getOrientation();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 

			if(zone == -1) {
				//zone = 180;

				isClicked = true;
				//			gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15 /* (cameraPosition != null) ? cameraPosition.zoom : gMap.getCameraPosition().zoom*/));
				markerTmp = marker;
				Projection projection = gMap.getProjection();
				Point markerCenter = projection.toScreenLocation(marker.getPosition());
				float tiltFactor = (90 - gMap.getCameraPosition().tilt) / 90;    

				Log.e(" before onCameraChanged  ==> marketCenter : ", " Point x : "+markerCenter.x+" Point y : "+markerCenter.y);

				if(height > width) { //if(((WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
					if((metrics.densityDpi >= 213 && metrics.densityDpi <= 219) || metrics.densityDpi == 240) {
						markerCenter.y -= 205 * tiltFactor;
					}else if(metrics.densityDpi > 219 && metrics.densityDpi < 480) {
						markerCenter.y -= 300 * tiltFactor;
					}else if(metrics.densityDpi >= 480) {
						markerCenter.y -= 400 * tiltFactor;
					}

					hideListSearch();
				}

				LatLng fixLatLng = projection.fromScreenLocation(markerCenter);
				Log.e(" after onCameraChanged  ==> marketCenter : ", " Point x : "+markerCenter.x+" Point y : "+markerCenter.y);

				//	    markerTmp.setPosition(fixLatLng);

				gMap.animateCamera(CameraUpdateFactory.newLatLng(fixLatLng),  null);

				return ;
			}

			mLocationInfos.setViewByMarkerLocation(marker, zone);

			View anchor = new View(getActivity());

			switch (zone) {


			case 0:			



				if((metrics.densityDpi >= 213 && metrics.densityDpi <= 219) || metrics.densityDpi == 240) {

					//				x = 0;
					//				y = 3;

					if(view.findViewById(R.id.forDiff)!= null) {
						x = 0;
						y = 2;

					}else {
						x = 0;
						y = 35;

					}
				}else {
					x = 11;
					y = 3;

				}

				anchor.setX(origine.x + position.x - mLocationInfos.getWidth() - x);// - 10);
				anchor.setY(origine.y + position.y - (mLocationInfos.getHeight()/2) + y);// - 4);


				mLocationInfos.show(anchor);

				break;

			case 90:			

				if((metrics.densityDpi >= 213 && metrics.densityDpi <= 219) || metrics.densityDpi == 240) {

					if(view.findViewById(R.id.forDiff)!= null) {
						x = 13;
						y = 20;

					}else {
						x = 13;
						y = 42;
					}


				}else {
					x = 13;
					y = 15;

				}

				anchor.setX(origine.x + position.x  - mLocationInfos.getWidth()/2 - x);// - 5);
				anchor.setY(origine.y + position.y + y);// + 5);
				mLocationInfos.show(anchor);


				break;

			case 180:			

				if((metrics.densityDpi >= 213 && metrics.densityDpi <= 219) || metrics.densityDpi == 240) {
					if(view.findViewById(R.id.forDiff)!= null) {
						x = 20;
						y = 11;
					}else {

						x = 17;
						y = 40;
					}

				}else {
					x = 14;
					y = 15;

				}

				anchor.setX(origine.x + position.x  + x);
				anchor.setY(origine.y + position.y - (mLocationInfos.getHeight()/2) + y);

				mLocationInfos.show(anchor);


				break;


			case 270:			


				if((metrics.densityDpi >= 213 && metrics.densityDpi <= 219) || metrics.densityDpi == 240) {

					if(view.findViewById(R.id.forDiff)!= null) {
						x = 32;
						y = 10;
					}else {
						x = 34;
						y = - 12;
					}

				}else {
					x = 5;
					y = 10;

				}

				anchor.setX(origine.x + position.x - mLocationInfos.getWidth()/2 + x); // + 20);

				anchor.setY(origine.y + position.y - (mLocationInfos.getHeight()) - y); // -5);
				mLocationInfos.show(anchor);

				break;



			default:
				break;
			}





		}else {
//					mLocationInfos.setDimensionByMarkerLocation(marker);
//					mLocationInfos.setViewByMarkerLocation(marker, -1);
//					mLocationInfos.showInCenter( new View(getActivity()));
			mPhoneLocationInfos.setInfoDialogMarkerView(marker);	
			mPhoneLocationInfos.show();

			;
		}
	}


	private Bitmap getMarkerBitmap(Locations_group group) {


		/** Uness Modif **/ 
		String path = group.getPin_icon(); 
		String pinColor = group.getPin_color();

		BitmapFactory.Options myOptions = new BitmapFactory.Options();
		myOptions.inDither = true;
		myOptions.inScaled = false;
		myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
		myOptions.inPurgeable = true;

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open(path), null, myOptions);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(colors
				.getColorDefault(pinColor, "000000","FF"));
		//paint.setAntiAlias(true);
		//        paint.setStyle(Paint.Style.STROKE);
		//        // set stroke
		//        paint.setStrokeWidth(4.5f);



		Config config = bitmap.getConfig();
		Bitmap workingBitmap = Bitmap.createBitmap(76, 76, config); //Bitmap.createBitmap(bitmap);
		Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


		Canvas canvas = new Canvas(mutableBitmap);
		canvas.drawCircle(bitmap.getWidth()/2 + 10, bitmap.getHeight()/2 + 10, 35, paint);
		canvas.drawBitmap(bitmap, 10, 10, null);

		//	    ImageView imageView = (ImageView)findViewById(R.id.schoolboard_image_view);
		//	    imageView.setAdjustViewBounds(true);
		//	    imageView.setImageBitmap(mutableBitmap);

		/** **/

		//		@SuppressWarnings("deprecation")
		//		LayerDrawable result = new LayerDrawable(
		//				new Drawable[] {
		//						new BitmapDrawable(mutableBitmap),
		//						new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(path))) });					


		return mutableBitmap;
	}


	private LayerDrawable buildPinIcon(Locations_group group) {
		/*
		 * Construct the pin icon
		 */


		String path = group.getPin_icon(); 
		String pinColor = group.getPin_color();
		LayerDrawable result = null;

		BitmapFactory.Options myOptions = new BitmapFactory.Options();
		myOptions.inDither = true;
		myOptions.inScaled = false;
		myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
		myOptions.inPurgeable = true;

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open(path), null, myOptions);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			//Crashlytics.logException(e);
			
		}


		Bitmap workingBitmap = Bitmap.createBitmap(76, 76, bitmap.getConfig()); //Bitmap.createBitmap(bitmap);
		Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		paint.setColor(colors.getColorDefault(pinColor, "000000", "FF"));

		Canvas canvas = new Canvas(mutableBitmap);
		canvas.drawCircle(bitmap.getWidth()/2 + 10, bitmap.getHeight()/2 + 10, 38, paint);

		//	    canvas.drawBitmap(bitmap, 10, 10, null);

		if (!path.isEmpty()) {

			//			Drawable pincolor = getResources().getDrawable(R.drawable.pin);
			//			pincolor.setColorFilter(new PorterDuffColorFilter(colors
			//					.getColor(pinColor), PorterDuff.Mode.MULTIPLY));
			try {
				result = new LayerDrawable(
						new Drawable[] {
								/*pincolor*/ new BitmapDrawable(mutableBitmap),
								new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(path))) });					

			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



		return result;

	}

	private Drawable resize(Drawable image) {
		Bitmap b = ((BitmapDrawable)image).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 32, 32, false);
		return new BitmapDrawable(bitmapResized);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		//mLocationInfos.dismiss();
		cameraPosition = gMap.getCameraPosition();
		((MainActivity) getActivity()).extras = new Bundle();
		((MainActivity) getActivity()).extras.putParcelable("position", cameraPosition);
		super.onDestroyView();
	}

	CameraPosition cameraPosition;
	private long time;
	private boolean isTablet;
	//	private Section section;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {

	//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());

		colors = ((MainActivity) activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

/*
		try {
			((MainActivity) getActivity()).extras = bundle;
			
			if (bundle != null) {
				((MainActivity) getActivity()).extras.putInt("Section_id", sectionMap.getId_section());
			}else {
                ((MainActivity) getActivity()).extras = new Bundle();
				((MainActivity) getActivity()).extras.putInt("Section_id", sectionMap.getId_section());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		((MainActivity) getActivity()).bodyFragment = "MapV2Fragment";
		time = System.currentTimeMillis();

		/** Uness Modif **/

		//mLocationInfos = new MyLocationInfos(getActivity(), appController, colors);



		super.onAttach(activity);
	}

	public Bitmap getSingleDrawable(LayerDrawable layerDrawable) {

		int resourceBitmapHeight = 22, resourceBitmapWidth = 22;
		int widthInDp = SIZE_ICON_PIN;
		if (isTablet) {
			widthInDp = 34;
			resourceBitmapHeight = 36;
			resourceBitmapWidth = 36;
		}else {
			widthInDp = 24;
			resourceBitmapHeight = 26;
			resourceBitmapWidth = 26;
		}
		// float widthInInches = 0.3f;
		//		widthInDp = SIZE_ICON_PIN;
		int widthInPixels = (int) (widthInDp
				* getResources().getDisplayMetrics().density + 0.5);
		int heightInPixels = (int) (widthInPixels * resourceBitmapHeight / resourceBitmapWidth);

		int insetLeft = 7, insetTop = 7, insetRight = 7, insetBottom = 7;

		layerDrawable.setLayerInset(1, insetLeft, insetTop, insetRight,
				insetBottom);


		Bitmap bitmap = Bitmap.createBitmap(widthInPixels, heightInPixels,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		layerDrawable.setBounds(0, 0, widthInPixels, heightInPixels);
		layerDrawable.draw(canvas);

		// BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
		// bitmap);
		// bitmapDrawable.setBounds(0, 0, widthInPixels, heightInPixels);

		return bitmap;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (gMap != null) {
			cameraPosition = gMap.getCameraPosition();
			((MainActivity) getActivity()).extras = new Bundle();
			((MainActivity) getActivity()).extras.putParcelable("position", cameraPosition);
			outState.putParcelable("position", cameraPosition);
		}
		super.onSaveInstanceState(outState);
	}


	/**this method 
	 * @param _distance
	 * @return
	 */
	private int zoomLevel(double _distance) {
		int newzoomlevel = 6;
		if (_distance != 0) {
			double distance = _distance * 1000.0;
			double equatorLength = 40075004; // in meters
			double widthInPixels = screenWidth();
			double metersPerPixel = equatorLength / 256;
			int zoomLevel = 1;
			while ((metersPerPixel * widthInPixels) > distance) {
				metersPerPixel /= 2;
				++zoomLevel;
			}
			Log.i("current zoom level", "zoom level = " + zoomLevel);
			if (zoomLevel <= 19) {
				newzoomlevel = zoomLevel;
			} else {
				newzoomlevel = zoomLevel - 2;
			}
		}else {
			double distance = 3 * 1000.0;
			double equatorLength = 40075004; // in meters
			double widthInPixels = screenWidth();
			double metersPerPixel = equatorLength / 256;
			int zoomLevel = 1;
			while ((metersPerPixel * widthInPixels) > distance) {
				metersPerPixel /= 2;
				++zoomLevel;
			}
			Log.i("current zoom level", "zoom level = " + zoomLevel);
			if (zoomLevel <= 19) {
				newzoomlevel = zoomLevel;
			} else {
				newzoomlevel = zoomLevel - 2;
			}
		}
		return newzoomlevel;

	}

	private double screenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		final int deviceHeight = dm.heightPixels;
		final int deviceWidth = dm.widthPixels - (int)getResources().getDimension(R.dimen.width_tab_fragment);

		return deviceHeight<deviceWidth?deviceHeight:deviceWidth;
	}

	public static int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dp * scale + 0.5f);
	}
	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_map_section", 0);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}


	/** Uness Modif **/


	@SuppressWarnings("unchecked")
	public static <T extends Object> List<T> cloneList(List<T> list) {
		return ((List<T>) ((ArrayList<T>) list).clone());
	}



	public List<Locations_group> fromCollectionToList(Collection<Locations_group> locationGroups){
		List<Locations_group> loc_grp = new ArrayList<Locations_group>();
		Locations_group loc_group = null;
		//		loc_grp.addAll(locationGroups);
		//		List<Location> loc = null;
		int i = 0;

		for (Iterator<Locations_group> iterator = locationGroups.iterator(); iterator.hasNext();) {
			loc_group = (Locations_group) iterator.next();
			loc_grp.add(i,loc_group);
			loc_grp.get(i).setLocations(new RealmList<Location>());

			i++;
		}
		return loc_grp;
	}

	public void UpdateListByPostalCode(List<Locations_group> loc_grp, String postal_code){


		List<Location> locs = null;

		//loc_grp = cloneList(locations_grp);
		//Log.i(" loc_grp   size ", ""+loc_grp.size());

		if(postal_code.length() != 0){

			for(int i = 0; i < loc_grp.size(); i++){

				locs = loc_grp.get(i).getLocations();
				if(locs != null)
				{
					//Log.i(" Loc "+i+"  size ", ""+locs.size());


					for(int j = 0; j < locs.size(); j++){

						if(locs.get(j).getPostal_code() != null && !(locs.get(j).getPostal_code().isEmpty()) && locs.get(j).getPostal_code().startsWith(postal_code)/*loc.get(j).getPostal_code().toString().contains(postal_code)*/){

						} 
						else
						{

							Log.i(" Postal_code != ", ""+locs.get(j).getPostal_code());	
							loc_grp.get(i).getLocations().remove(j);
							j--;


						}
					}


				}

			}


		}



		//search_layout.removeAllViewsInLayout();
		//search_layout.addView(editTxt);
		mHAdapter.setNewLocationsGroup(loc_grp);
		HLview.setAdapter(mHAdapter);
		//search_layout.addView(HLview);
		mHAdapter.notifyDataSetChanged();
		//search_layout.invalidate();

	}


	public void RemovePostalCodeEditor(List<Locations_group> loc_grp){


		List<Location> locs = null;
		int v, w; 
		v = 0;  w = 0;

		for(int i = 0; i < loc_grp.size(); i++){

			locs = loc_grp.get(i).getLocations();
			//				Log.e(" loc_grp.get(i).getLocations() number : ", " <==> "+i);

			if(locs != null)
			{


				for(int j = 0; j < locs.size(); j++){
					v++;
					if(locs.get(j).getPostal_code() != null && !(locs.get(j).getPostal_code().isEmpty())/*loc.get(j).getPostal_code().toString().contains(postal_code)*/){
						//Log.e(" Postal_code ", " <==> "+locs.get(j).getPostal_code().toString());

					} 
					else
					{
						w++;
						//Log.e(" Postal_code != ", "<==> "+locs.get(j).getPostal_code()+"  v  :  "+v+"   w : "+w);	


					}
				}


			}

		}

		if(v == w) {
			//editTxt = null;
			view.findViewById(R.id.code_postal_search_bar).setVisibility(View.GONE);
		}

	}


	public void showListSearch() {

		//LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.list_search_layout);

		//layout.setVisibility(View.VISIBLE);
		Log.i(" showListSearch() "," Toujours");

		/* = (LinearLayout)getLayoutInflater().inflate(R.layout.l, null, false); */


		//		AnimationSet animationSet = new AnimationSet(true);
		//		Animation animFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_out);
		//		Animation animFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);
		//		((RelativeLayout)getActivity().getApplicationContext()).startViewTransition(search_layout); //
		//		
		//		search_layout.startAnimation(animFadeOut); 
		//		
		//		animFadeOut.setAnimationListener(new AnimationListener()
		//		{
		//	    public void onAnimationStart(Animation arg0)
		//	    {
		//	    }
		//	    public void onAnimationRepeat(Animation arg0)
		//	    {
		//	    }
		//
		//	    public void onAnimationEnd(Animation arg0)
		//	    {
		//	    	search_layout.setVisibility(View.VISIBLE);
		//	    }
		//	});

		//		AnimationSet animationSet = new AnimationSet(true);
		//
		//		Animation animation1 = new AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.fade_in);
		//		animation1.setDuration(1000);

		//		AlphaAnimation fade_in = new AlphaAnimation(0.0f, 1.0f);
		//		
		//		fade_in.setDuration(500);
		//		fade_in.setStartTime(0);
		//		fade_in.setAnimationListener(new AnimationListener()
		//		{
		//		    public void onAnimationStart(Animation arg0)
		//		    {
		//		    	search_layout.setVisibility(View.VISIBLE);
		//		    	list_search_icon.setRotationY(180);
		//		    }
		//		    public void onAnimationRepeat(Animation arg0)
		//		    {
		//		    }
		//
		//		    public void onAnimationEnd(Animation arg0)
		//		    {
		//		    	
		//		    }
		//		});
		//		
		//		search_layout.startAnimation(fade_in);

		search_layout.setVisibility(View.VISIBLE);
		if(search_label != null){
			search_label.setVisibility(View.GONE);
		}
		else{
			list_search_icon.setRotationY(180);
		}


		if(editTxt.getText().toString().length() != 0)
			editTxt.setText(editTxt.getText().toString()); 

		if (!isTablet) {
			//			 params_search =(android.widget.RelativeLayout.LayoutParams) search_layout.getLayoutParams();
			RelativeLayout.LayoutParams params_container = (android.widget.RelativeLayout.LayoutParams) list_search_icon_container.getLayoutParams();
			params_container.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			list_search_icon.setRotationY(0);
			//			params_container.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
		}


	} 


	public void hideListSearch() { 


		if(search_layout != null) {
			search_layout.setVisibility(View.GONE);
			if(search_label != null){
				search_label.setVisibility(View.VISIBLE);
			}
			else if(list_search_icon != null){
				list_search_icon.setRotationY(0);
			}
		}

		if (!isTablet && list_search_icon != null) {
			//			 params_search =(android.widget.RelativeLayout.LayoutParams) search_layout.getLayoutParams();
			RelativeLayout.LayoutParams params_container = (android.widget.RelativeLayout.LayoutParams) list_search_icon_container.getLayoutParams();
			//			params_container.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params_container.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
			list_search_icon.setRotationY(180);
		}
	} 

	public boolean isListSearchShown() {

		return ((LinearLayout)getActivity().findViewById(R.id.list_search_layout)).isShown();
	}

	@Override
	public void drawMarkerAtLocation(Location location, int section, boolean hideList) {
		Coordinates coordinates = location.getCoordinates();

		//		gMap.setMyLocationEnabled(true);
		if(hideList){
			hideListSearch();
		} 

		//gMap.getCameraPosition().zoom;

		Marker marker = gMap.addMarker(new MarkerOptions()
		.position(
				new LatLng(coordinates
						.getLatitude(), coordinates
						.getLongitude()))
						.title(location.getTitle())
						.snippet("" + location.getId())
						//						.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(fromCollectionToList(loc_groups).get(section))))
						.icon(BitmapDescriptorFactory.fromBitmap(getSingleDrawable(buildPinIcon(fromCollectionToList(loc_groups).get(section)))))
						.anchor(0.5f, 0.5f));

		marker.setVisible(false);

		markerTmp = marker;
		//markerTmp.setPosition(latlng);
		markerTmp.setSnippet(""+location.getId());
		markerTmp.setTitle(location.getTitle());

		if(isClicked) {
			isClicked = false;
			//			getInfoWindowZone(marker, gMap.getProjection().toScreenLocation(marker.getPosition()),350, mLocationInfos.getHeight(),180);
			getInfoWindowZone(marker, gMap.getProjection().toScreenLocation(marker.getPosition()), 180);
		}else {
			isClicked = true;
			//			gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15 /* (cameraPosition != null) ? cameraPosition.zoom : gMap.getCameraPosition().zoom*/));

			Projection projection = gMap.getProjection();
			Point markerCenter = projection.toScreenLocation(marker.getPosition());
			float tiltFactor = (90 - gMap.getCameraPosition().tilt) / 90;

			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			//						//		mLocationInfos.dismiss();
			DisplayMetrics metrics = new DisplayMetrics(); 
			//						//int orientation = display.getOrientation();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 


			int width = display.getWidth();   
			int height = display.getHeight(); 


			if(height > width) { //if(((WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation() == Configuration.ORIENTATION_PORTRAIT) {

				if((metrics.densityDpi >= 213 && metrics.densityDpi <= 219) || metrics.densityDpi == 240) {
					markerCenter.y -= 205 * tiltFactor;
				}else if(metrics.densityDpi > 219 && metrics.densityDpi < 480) {
					markerCenter.y -= 300 * tiltFactor;
				}else if(metrics.densityDpi >= 480) {
					markerCenter.y -= 400 * tiltFactor;
				}

				hideListSearch();
			}
			//						else
			//							marketCenter.y -= 20 * tiltFactor;
			LatLng fixLatLng = projection.fromScreenLocation(markerCenter);

			//	    markerTmp.setPosition(fixLatLng);

			gMap.animateCamera(CameraUpdateFactory.newLatLng(fixLatLng), null);




		}


	}



/*    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        Street_view_default_position strViewDefaultPosition = sectionMap.getStreet_view_default_position();
        LatLng position = new LatLng(strViewDefaultPosition.getStreet_view_latitude(), strViewDefaultPosition.getStreet_view_longitude());
        float bearing = strViewDefaultPosition.getStreet_view_orientation();
        //position = new LatLng(37.765927, -122.449972);
        streetViewPanorama.setPosition(position);
        StreetViewPanoramaOrientation orientation = new StreetViewPanoramaOrientation(0f, bearing);
        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()*//*.bearing(bearing)*//*.orientation(orientation).build();
        streetViewPanorama.animateTo(camera, 1);
    }*/

}
