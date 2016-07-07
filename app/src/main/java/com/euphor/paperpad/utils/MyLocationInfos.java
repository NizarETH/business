package com.euphor.paperpad.utils;


/**
 * 
 *  EuphorDev04
 * 
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

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
import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.utils.quickAction.PopupWindows;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.google.android.gms.maps.model.Marker;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class MyLocationInfos extends PopupWindows implements OnDismissListener{
	
	private View mView;
	private View mRootView;
	private FragmentActivity activity;
	private Context context;
	private Location location;

    private Realm realm;
	private LayoutInflater mInflater;
//	private ScrollView mScroller;
//	private OnActionItemClickListener mItemClickListener;
	private com.euphor.paperpad.utils.actionsPrices.QuickAction.OnDismissListener mDismissListener;
	
	private int mAnimStyle;
 //   private int mOrientation;
//    private int rootWidth=0;
	private Colors colors;
	public RealmList<Locations_group> locations_group;
	
//	private boolean mDidAction;
	
	public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;
	
	private static String POLICE, POLICE_DISCRIPT;
	private int width, height; 
//	
//	public MyLocationInfos(Context context){
//		super(context);
//	}

	
	public MyLocationInfos(FragmentActivity activity/*, Context context, int orientation*/, Realm  realm, Colors colors, RealmList<Locations_group> locationsGroup) {
		super(activity);
       this.locations_group = locationsGroup;
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.realm = realm;
		this.colors = colors;
//		mOrientation = orientation;
        
        mInflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //if (mOrientation == HORIZONTAL) {
            setRootViewId(R.layout.info_window);
//        } else {
//            setRootViewId(R.layout.info_window);
//        }

        mAnimStyle 	= ANIM_AUTO;
        
        POLICE = "fonts/gill-sans-light.ttf";
		POLICE_DISCRIPT = "fonts/gill-sans-mt-italic.ttf";
        
		
	}
	
	
	/**
	 * Set root view.
	 * 
	 * @param id Layout resource id
	 */
	public void setRootViewId(int id) {
		
		mRootView	= (ViewGroup) mInflater.inflate(id, null, false);
		mView = mRootView;
		//mScroller	= (ScrollView) mRootView.findViewById(R.id.scroller);
		
		//This was previously defined on show() method, moved here to prevent force close that occured
		//when tapping fastly on a view to show quickaction dialog.
		//Thanx to zammbi (github.com/zammbi)
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		setContentView(mRootView);
		
	}
	
	
	public void setViewByMarkerLocation(Marker marker, int BubbleDrection){
        if(activity !=null)
        realm=Realm.getInstance(activity.getApplicationContext());
        location=realm.where(Location.class).equalTo("id",Integer.parseInt(marker.getSnippet())).findFirst();
		

			/*location = appController.getLocationDao().queryForId(Integer.parseInt(marker.getSnippet()));*/


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


		/*popover.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()), PorterDuff.Mode.MULTIPLY));*/
	   popover.setColorFilter(new PorterDuffColorFilter(activity.getResources().getColor(R.color.background_color_popover), PorterDuff.Mode.MULTIPLY));

		if(BubbleDrection != -1)
			backHolder.setBackgroundDrawable(popover);
		else {
			backHolder.setBackgroundColor(colors.getColor(colors.getTitle_color()));
		}
		
		
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		//		mLocationInfos.dismiss();
		DisplayMetrics metrics = new DisplayMetrics(); 
		//int orientation = display.getOrientation();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics); 
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
			
		
		TextView infoWinTV = (TextView)mView.findViewById(R.id.mapBubbleTV);
		infoWinTV.setText(marker.getTitle());
		infoWinTV.setTypeface(MainActivity.FONT_BODY);
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
		infoWinTV.setTextColor(colors.getColor(color));
		
		//height += infoWinTV.getMeasuredHeight();
	 	
		
		
//		Glide.with(activity).load(new File("android_asset/icon/group_38/icon_0_38.png")).into(closeInfoWindow);


			ImageView closeInfoWindow = (ImageView)mView.findViewById(R.id.closeInfoWindow);
//			Bitmap bm  = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_exit);
//			Drawable drawable = new BitmapDrawable(bm);//(new File("android_asset/"+agendaGroup.getIcon()).getAbsolutePath());
//			drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()),PorterDuff.Mode.OVERLAY));
//			closeInfoWindow.setBackgroundDrawable(drawable);
			closeInfoWindow.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
			closeInfoWindow.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					mWindow.dismiss();
				}
			});

			
		
		String imageUrl = location.getImage();
		ImageView infoWinImg = (ImageView)mView.findViewById(R.id.mapBubbleImg);
		infoWinImg.setScaleType(ScaleType.CENTER_CROP);
		Illustration illustration = location.getIllustration();
		
		if (illustration!=null) {
			infoWinImg.setVisibility(View.VISIBLE);
			height +=  (int) context.getResources().getDimension(R.dimen.infoWindow_height);

//			if (!illustration.getPath().isEmpty()) {
//				imageUrl = "file:///" + illustration.getPath();
//			}
			//infoWinImg.setImageURI(Uri.parse(imageUrl));
			String path;
			if (!illustration.getPath().isEmpty()) {
				path = illustration.getPath();
				Glide.with(context).load(new File(path)).into(infoWinImg);
			}else {
				path = illustration.getLink();
				Glide.with(context).load(path).into(infoWinImg);
			}
			//			height += infoWinImg.getMeasuredHeight();
		}else {
			infoWinImg.setVisibility(View.GONE);
			//height -=  (int) context.getResources().getDimension(R.dimen.infoWindow_height);	
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
			height -= 50;
		}
		
		
//		if (location.getLink_type()!=null && !location.getLink_type().isEmpty()) {
//			   btnTV.setTextColor(colors.getColor(colors.getBackground_color()));
//			   btnTV.setText(location.getButton_text());
//			   Paint paint = new Paint();
//			   paint.setColor(colors.getColor(colors.getBackground_color()));
//			   arrowIW.setPaint(paint);
//			   buttonHolder.setOnClickListener(new OnClickListener() {
//			    
//			    @Override
//			    public void onClick(View v) {
//			     
//			    }
//			   });
//			   
//			  }else {
//			   buttonHolder.setVisibility(View.GONE);
//			  }
		LinearLayout buttonHolder = (LinearLayout)mView.findViewById(R.id.buttonHolder);
		
		if (location.getLink_type()!=null && !location.getLink_type().isEmpty()) {
			
			//height -= 90; // += buttonHolder.getMeasuredHeight(); //
			
			
			TextView btnTV = (TextView)mView.findViewById(R.id.tvButton);
			btnTV.setTypeface(MainActivity.FONT_BODY);
			btnTV.setTextColor(colors.getColor(color/*colors.getBackground_color()*/));
			
			ArrowImageView arrowIW = (ArrowImageView)mView.findViewById(R.id.arrowInfoWindow);

			buttonHolder.setVisibility(View.VISIBLE);
			btnTV.setTextColor(colors.getColor(color/*colors.getBackground_color()*/));
			btnTV.setText(location.getButton_text());
			Paint paint = new Paint();
			/*String color = null;
			while(itr.hasNext()) {
				Locations_group element = (Locations_group)itr.next();
				for (int i = 0; i <element.getLocations().size() ; i++) {
					if(element.getLocations().get(i).getTitle().equals(marker.getTitle()))
						color = element.getPin_color() ;
				}

			}*/


			paint.setColor(colors.getColor(color));
			arrowIW.setPaint(paint);
			buttonHolder.setOnClickListener(new OnClickListener() {
				
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
                             tmpList= realm.where(Child_pages.class).equalTo("id",Integer.parseInt(location.getLink_page_id())).findAll();

								/*tmpList = appController.getChildPageDao().queryForEq("id", Integer.parseInt(location.getLink_page_id()));*/


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
			height -= 50;
		}	
		
	
		if(showItineraryButton()) {
		
		TextView txtItinerence = (TextView)mView.findViewById(R.id.mapBubbleItinerence);
		txtItinerence.setTypeface(MainActivity.FONT_BODY);
		txtItinerence.setTextColor(colors.getColor(color));
		//txtItinerence.setText("Afficher l'itinéraire");
		ImageView imgItinerence = (ImageView)mView.findViewById(R.id.imgItinerence);
		
		height += 50;
		//Glide.with(activity).load(new File("android_asset/icon/group_35/icon_1_35.png")).into(imgItinerence);
		
//		Bitmap bm_  = BitmapFactory.decodeResource(activity.getResources(), R.drawable.compass);
//		Drawable drawable_ = new BitmapDrawable(bm_);//(new File("android_asset/"+agendaGroup.getIcon()).getAbsolutePath());
//		drawable_.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()),PorterDuff.Mode.MULTIPLY));
//		imgItinerence.setBackgroundDrawable(drawable_);
		
		imgItinerence.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(color),PorterDuff.Mode.MULTIPLY));

		txtItinerence.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Log.i("  Inside infoWindow ","  YES");
				LocationResult locationResult = new LocationResult(){

					@Override
					public void gotLocation(android.location.Location _location) {
						if (_location != null) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
									Uri.parse("http://maps.google.com/maps?saddr="+_location.getLatitude()+","+_location.getLongitude()+"&daddr="+location.getCoordinates().getLatitude()+","+location.getCoordinates().getLongitude()));
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							intent.addCategory(Intent.CATEGORY_LAUNCHER );     
//							intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
							context.startActivity(intent);
						}else {
							Toast.makeText(mContext, "Position actuelle inconnue", Toast.LENGTH_SHORT).show();
						}
						
							    
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
		
		mRootView.setLayoutParams(new LayoutParams(width, height));
		mView.setLayoutParams(new LayoutParams(width, height));

	}
	
	private boolean showItineraryButton;
	
	public void setItineraryButton(boolean showItineraryButton) {
		
		this.showItineraryButton = showItineraryButton;
		
	}
	public boolean showItineraryButton() {
		
		return this.showItineraryButton;
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
	
	
	
public void setDimensionByMarkerLocation(Marker marker){
		

            location = realm.where(Location.class).equalTo("id",Integer.parseInt(marker.getSnippet())).findFirst();
			/*location = appController.getLocationDao().queryForId(Integer.parseInt(marker.getSnippet()));*/


    WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		//		mLocationInfos.dismiss();
		DisplayMetrics metrics = new DisplayMetrics(); 
		//int orientation = display.getOrientation();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics); 

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
		
		if(location.getIllustration() != null) {
			height += (int) context.getResources().getDimension(R.dimen.infoWindow_height);
		}
		
		
		if(location.getText() == null || location.getText().isEmpty())
			height -= 50;
		
		if (location.getLink_type() ==null || location.getLink_type().isEmpty()) 
			height -= 50;
		
		if(!showItineraryButton)
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
	/**
	 * Set animation style
	 * 
	 * @param screenWidth screen width
	 * @param requestedX distance from left edge
	 * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view
	 * 		  and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
//		int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;

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
//			if (arrowPos <= screenWidth/4) {
//				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
//			} else if (arrowPos > screenWidth/4 && arrowPos < 3 * (screenWidth/4)) {
//				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
//			} else {
//				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
//			}
					
			break;
		}
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
