package com.euphor.paperpad.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Locations_group;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.PagesFragment;
import com.euphor.paperpad.activities.fragments.PanoramaFragment;
import com.euphor.paperpad.activities.fragments.WebViewFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Location;

import com.euphor.paperpad.utils.MyLocation.LocationResult;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.google.android.gms.maps.model.Marker;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class MyPhoneLocationInfo extends AlertDialog {

	public MyPhoneLocationInfo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		  //setContentView(mView);

		super.onCreate(savedInstanceState);
	}
	

	
//	private View mRootView;
	private FragmentActivity activity;
	private Context context;
	private Location location;

	private LayoutInflater mInflater;
//	private ScrollView mScroller;
//	private OnActionItemClickListener mItemClickListener;
	private com.euphor.paperpad.utils.actionsPrices.QuickAction.OnDismissListener mDismissListener;
	private Realm realm;

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
	public RealmList<Locations_group> locations_group;
	private View mView;
//	
//	public MyLocationInfos(Context context){
//		super(context);
//	}

	
	public MyPhoneLocationInfo(FragmentActivity activity/*, Context context, int orientation*/, Realm realm, Colors colors, RealmList<Locations_group> locationsGroup) {
		super(activity);
		//this(activity.getApplicationContext());
		this.locations_group = locationsGroup;
		this.activity = activity;
		this.context = activity.getApplicationContext();

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
	
	
	public void setInfoDialogMarkerView(Marker marker) {

        realm=Realm.getInstance(getContext());

			location =  realm.where(Location.class).equalTo("id",Integer.parseInt(marker.getSnippet())).findFirst();//appController.getLocationDao().queryForId(Integer.parseInt(marker.getSnippet()));
		Iterator itr = locations_group.iterator();
		String color = null;
		while(itr.hasNext()) {
			Locations_group element = (Locations_group)itr.next();
			for (int i = 0; i <element.getLocations().size() ; i++) {
				if(element.getLocations().get(i).getTitle().equals(marker.getTitle()))
					color = element.getPin_color() ;
			}

		}
		if(color == null)
			color = colors.getBackground_color();
        LinearLayout backHolder = (LinearLayout)mView.findViewById(R.id.backHolder);

		//backHolder.setBackgroundColor(colors.getColor(colors.getBackground_color()));
	
			backHolder.setBackgroundColor(activity.getResources().getColor(R.color.background_color_popover));
		
		
		TextView infoWinTV = (TextView)mView.findViewById(R.id.mapBubbleTV);
		infoWinTV.setText(marker.getTitle());
		infoWinTV.setTypeface(MainActivity.FONT_BODY);
		infoWinTV.setTextColor(colors.getColor(color));
		

			ImageView closeInfoWindow = (ImageView)mView.findViewById(R.id.closeInfoWindow);
			closeInfoWindow.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
			closeInfoWindow.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					MyPhoneLocationInfo.this.dismiss();
				}


			});

			
		
		ImageView infoWinImg = (ImageView)mView.findViewById(R.id.mapBubbleImg);
		infoWinImg.setScaleType(ScaleType.CENTER_CROP);
		Illustration illustration = location.getIllustration();
		
		if (illustration!=null) {
			infoWinImg.setVisibility(View.VISIBLE);
			String path;
			if (!illustration.getPath().isEmpty()) {
				path = illustration.getPath();
				Glide.with(context).load(new File(path)).into(infoWinImg);
			}else {
				path = illustration.getLink();
				Glide.with(context).load(path).into(infoWinImg);
			}
		}else {
			infoWinImg.setVisibility(View.GONE);
		}

		 
		
		
		TextView txt_descript = (TextView)mView.findViewById(R.id.mapBubbleDiscript);
		
		if(location.getText() != null && !location.getText().isEmpty()) {
//			height += 100;
		txt_descript.setVisibility(View.VISIBLE);
		txt_descript.setText(location.getText());
		txt_descript.setTypeface(MainActivity.FONT_BODY);
		txt_descript.setTextColor(colors.getColor(color));
		
		//height += txt_descript.getMeasuredHeight();
		}
		else {
			txt_descript.setVisibility(View.GONE);
			//height -= 50;
		}
		
		
		LinearLayout buttonHolder = (LinearLayout)mView.findViewById(R.id.buttonHolder);
		
		if (location.getLink_type()!=null && !location.getLink_type().isEmpty()) {
			
			
			TextView btnTV = (TextView)mView.findViewById(R.id.tvButton);
			btnTV.setTypeface(MainActivity.FONT_TITLE);
			btnTV.setTextColor(colors.getColor(color));
			
			ArrowImageView arrowIW = (ArrowImageView)mView.findViewById(R.id.arrowInfoWindow);

			buttonHolder.setVisibility(View.VISIBLE);
			btnTV.setTextColor(colors.getColor(color));
			btnTV.setText(location.getButton_text());
			Paint paint = new Paint();
			paint.setColor(colors.getColor(color));
			arrowIW.setPaint(paint);
			buttonHolder.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					String type = location.getLink_type();
					
					if (type!=null) { 
						if (type.equals("web")) {
							String link = location.getLink_web_url();
							WebViewFragment webViewFragment = new WebViewFragment();
							((MainActivity)activity).bodyFragment = "WebViewFragment";
							// In case this activity was started with special instructions from an Intent,
							// pass the Intent's extras to the fragment as arguments
							((MainActivity)activity).extras = new Bundle();
							((MainActivity)activity).extras.putString("link", link);
							webViewFragment.setArguments(((MainActivity)activity).extras);
							// Add the fragment to the 'fragment_container' FrameLayout
							((MainActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

						}else if (type.equals("page")) {
							List<Child_pages> tmpList = new ArrayList<Child_pages>();
							try {
								tmpList = realm.where(Child_pages.class).equalTo("id", Integer.parseInt(location.getLink_page_id())).findAll();
								//appController.getChildPageDao().queryForEq("id", Integer.parseInt(location.getLink_page_id()));
							} catch (NumberFormatException  e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

                            if (tmpList.size()>0) {
								int page_id = tmpList.get(0).getId_cp();
								if (tmpList.get(0).getDesign().equals("panoramic")) {
									FragmentTransaction fragmentTransaction =
											((MainActivity)activity).getSupportFragmentManager().beginTransaction();
									Fragment prev = ((MainActivity)activity).getSupportFragmentManager().findFragmentByTag("panorama");
									if (prev != null) {
										fragmentTransaction.remove(prev);
									}
									Fragment panoFragment = new PanoramaFragment();
									((MainActivity) activity).extras.putInt("page_id", page_id);
									((MainActivity) activity).bodyFragment = "PanoramaFragment";
									panoFragment.setArguments(((MainActivity) activity).extras);
									fragmentTransaction.addToBackStack(null);
									fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
									fragmentTransaction.commit();
								}else {
									((MainActivity)activity).extras = new Bundle();
									((MainActivity) activity).extras.putInt(
											"page_id", page_id);
									PagesFragment pagesFragment = new PagesFragment();
									((MainActivity) activity).bodyFragment = "PagesFragment";
									pagesFragment
									.setArguments(((MainActivity) activity).extras);
									((MainActivity)activity)
									.getSupportFragmentManager()
									.beginTransaction()
									.setCustomAnimations(
											R.anim.slide_in_left,
											R.anim.slide_out_right)
											.replace(R.id.fragment_container,
													pagesFragment)
													.addToBackStack(null).commit();
								}

							}
						}
					}
					
					dismiss();
				}

			});
			
		}else {
			buttonHolder.setVisibility(View.GONE);
		}	
		
	
		if(showItineraryButton()) {
		
		TextView txtItinerence = (TextView)mView.findViewById(R.id.mapBubbleItinerence);
		txtItinerence.setTypeface(MainActivity.FONT_BODY);
		txtItinerence.setTextColor(colors.getColor(color));
		//txtItinerence.setText("Afficher l'itinéraire");
		ImageView imgItinerence = (ImageView)mView.findViewById(R.id.imgItinerence);
		//Glide.with(activity).load(new File("android_asset/icon/group_35/icon_1_35.png")).into(imgItinerence);
//		Bitmap bm_  = BitmapFactory.decodeResource(activity.getResources(), R.drawable.compass);
//		Drawable drawable_ = new BitmapDrawable(bm_);//(new File("android_asset/"+agendaGroup.getIcon()).getAbsolutePath());
//		drawable_.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()),PorterDuff.Mode.MULTIPLY));
		imgItinerence.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(color),PorterDuff.Mode.MULTIPLY));
//		imgItinerence.setBackgroundDrawable(drawable_);

		txtItinerence.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Log.i("  Inside infoWindow ","  YES");
				LocationResult locationResult = new LocationResult(){

					@Override
					public void gotLocation(android.location.Location _location) {
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
								Uri.parse("http://maps.google.com/maps?saddr="+_location.getLatitude()+","+_location.getLongitude()+"&daddr="+location.getCoordinates().getLatitude()+","+location.getCoordinates().getLongitude()));
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addCategory(Intent.CATEGORY_LAUNCHER );     
						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
						context.startActivity(intent);

					}


				};
				MyLocation myLocation = new MyLocation();
				dismiss();
				myLocation.getLocation(activity, locationResult);



			}

		});
		
		}else {
			mView.findViewById(R.id.itinerenceLayout).setVisibility(View.GONE);
		}
		
		setView(mView);

	}
	


}
