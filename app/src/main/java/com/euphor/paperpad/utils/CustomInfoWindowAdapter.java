/**
 * 
 */
package com.euphor.paperpad.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Location;

import com.euphor.paperpad.widgets.ArrowImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nostra13.universalimageloader.core.ImageLoader;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


	private Context context;
	private Location location;
	private ImageLoader imageLoader;
	private Colors colors;
	private View mView;
    private Realm realm;

	private static String POLICE, POLICE_DISCRIPT;
	
	/**
	 * @param context
	 */
	public CustomInfoWindowAdapter(Context context, Realm realm, ImageLoader imageLoader, Colors colors) {
		super();
		this.context = context;
		this.realm = realm;
		this.imageLoader = imageLoader;
		this.colors = colors;

		/** Uness Modif **/
		POLICE = "fonts/gill-sans-light.ttf";
		POLICE_DISCRIPT = "fonts/gill-sans-mt-italic.ttf";
	}


	public int getInfoWindowHeight(){
	    if (mView != null){
	        return mView.getMeasuredHeight();
	    }
	    return 0;
	}
	
	
	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		try {
			location = realm.where(Location.class).equalTo("id",Integer.parseInt(marker.getSnippet())).findFirst();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        /** Uness Modif **/
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), POLICE); 
		
//		color_txt_dscript	= new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
//				new int[]{colors.getColor(colors.getBackground_color()), 
//				colors.getColor(colors.getBody_color())});
//		
        Typeface font_discript = Typeface.createFromAsset(context.getAssets(), POLICE_DISCRIPT);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		mView = inflater.inflate(R.layout.google_info_window, null, false);
		LinearLayout backHolder = (LinearLayout)mView.findViewById(R.id.backHolder);
		Drawable popover = context.getResources().getDrawable(R.drawable.popover);
		popover.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color()), PorterDuff.Mode.MULTIPLY));
		backHolder.setBackgroundDrawable(popover);
		
		TextView infoWinTV = (TextView)mView.findViewById(R.id.mapBubbleTV);
		infoWinTV.setText(marker.getTitle());
		infoWinTV.setTypeface(font);
		infoWinTV.setTextColor(colors.getColor(colors.getBackground_color()));
		
		String imageUrl = location.getImage();
		ImageView infoWinImg = (ImageView)mView.findViewById(R.id.mapBubbleImg);
		infoWinImg.setScaleType(ScaleType.CENTER_CROP);
		Illustration illustration = location.getIllustration();
		if (illustration!=null) {
			if (!illustration.getPath().isEmpty()) {
				imageUrl = "file:///" + illustration.getPath();
			}
			infoWinImg.setImageURI(Uri.parse(imageUrl));
		}else {
			infoWinImg.setVisibility(View.GONE);
		}
		
		TextView txt_descript = (TextView)mView.findViewById(R.id.mapBubbleDiscript);
		txt_descript.setText(location.getText());
		txt_descript.setTypeface(font_discript);
		infoWinTV.setTextColor(colors.getColor(colors.getBackground_color()));
		
		
		LinearLayout buttonHolder = (LinearLayout)mView.findViewById(R.id.buttonHolder);
		TextView btnTV = (TextView)mView.findViewById(R.id.tvButton);
		btnTV.setTypeface(font);
		ArrowImageView arrowIW = (ArrowImageView)mView.findViewById(R.id.arrowInfoWindow);
		if (location.getLink_type()!=null && !location.getLink_type().isEmpty() && location.getButton_text().isEmpty()) {
//			mView.findViewById(R.id.itinerenceLayout).setVisibility(View.GONE);
			
			btnTV.setTextColor(colors.getColor(colors.getBackground_color()));
			btnTV.setText(location.getButton_text());
			Paint paint = new Paint();
			paint.setColor(colors.getColor(colors.getBackground_color()));
			arrowIW.setPaint(paint);
			
		}else {
			buttonHolder.setVisibility(View.GONE);
		}	
		
		
//		LinearLayout itinerenceLayout = (LinearLayout)view.findViewById(R.id.itinerenceLayout);
//		TextView txtItinerence = (TextView)view.findViewById(R.id.mapBubbleItinerence);
//		txtItinerence.setTypeface(font);
//		txtItinerence.setTextColor(colors.getColor(colors.getBackground_color()));
//		txtItinerence.setText("Afficher l'itinairance");
//		itinerenceLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				WebViewFragment webViewFragment = new WebViewFragment();
////				((MainActivity)context).bodyFragment = "WebViewFragment";
////				// In case this activity was started with special instructions from an Intent,
////				// pass the Intent's extras to the fragment as arguments
////				((MainActivity)context).extras = new Bundle();
////				((MainActivity)context).extras.putString("link", link);
////				webViewFragment.setArguments(((MainActivity)context).extras);
////				// Add the fragment to the 'fragment_container' FrameLayout
////				((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
////
//				Log.i("  Inside infoWindow ","  YES");
//				LocationResult locationResult = new LocationResult(){
//
//					@Override
//					public void gotLocation(android.location.Location _location) {
//						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
//							    Uri.parse("http://maps.google.com/maps?saddr="+_location.getLatitude()+","+_location.getLongitude()+"&daddr="+location.getCoordinates().getLatitude()+","+location.getCoordinates().getLongitude()));
//							    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							    intent.addCategory(Intent.CATEGORY_LAUNCHER );     
//							    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//							    context.startActivity(intent);
//					}
//				   
//					
//				};
//				MyLocation myLocation = new MyLocation();
//				myLocation.getLocation(context, locationResult);
//				
//				
//				
//			}
//		});
		
		return mView;
	}

}
