package com.euphor.paperpad.adapters;


/**
 * 
 *  euphorDev 04
 * 
 */

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.applidium.headerlistview.SectionAdapter;
import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Location;
import com.euphor.paperpad.Beans.Locations_group;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;


import java.io.File;
import java.util.List;

public class MyCustomListLocations extends SectionAdapter /* implements OnLongClickListener  implements OnClickListener */{

	private List<Locations_group> locations_group;
	private Activity activity;
	private Colors colors;
	private FromMyCustomListLocations Myinterface;

	private boolean hideList;

	ColorFilter imgColorFilter;
	//StateListDrawable drawable1;
	ColorStateList color_txt, color_txt_dscript;
	Typeface font, font_discript;


	public interface FromMyCustomListLocations{
		public void drawMarkerAtLocation(Location loc, int section, boolean hideList);
	}



	public MyCustomListLocations(List<Locations_group> locationGroups, Fragment fragment, Colors colors) {


		this.colors = colors;
		//this.gMap = _gMap;
		this.locations_group = locationGroups;
		this.activity = fragment.getActivity();

		Myinterface = (FromMyCustomListLocations) fragment;  


		imgColorFilter = new PorterDuffColorFilter(colors
				.getColor(colors.getBackground_color()), PorterDuff.Mode.MULTIPLY);




		color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getTitle_color())});
		
		color_txt_dscript = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getBody_color())});

		font = MainActivity.FONT_TITLE;

		font_discript = MainActivity.FONT_BODY;



//		drawable1 = new StateListDrawable();
//		drawable1.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
//		drawable1.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
//		drawable1.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getForeground_color())));

		DisplayMetrics metrics = new DisplayMetrics(); 
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics); 

		if(metrics.densityDpi >= 213 ){
			hideList = true;
		}
		else{
//			WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//			Display display = wm.getDefaultDisplay();
//
//			int orientation = display.getOrientation();
//			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics); 
//	
//  
//
//			if(orientation == Configuration.ORIENTATION_PORTRAIT ) {
//				hideList = true;
//			}
//			else
				hideList = false;
		}

//		Log.e( " Metric Density ", " "+metrics.densityDpi);
//
//		switch(metrics.densityDpi)
//		{ 
//		case DisplayMetrics.DENSITY_LOW: 
//			Log.e( " Metric DENSITY_LOW ", " "+metrics.densityDpi);
//			break; 
//		case DisplayMetrics.DENSITY_MEDIUM: 
//			Log.e( " Metric DENSITY_MEDIUM ", " "+metrics.densityDpi);
//			break; 
//		case DisplayMetrics.DENSITY_HIGH: 
//			Log.e( " Metric DENSITY_HIGH ", " "+metrics.densityDpi);
//			break;
//		}
		//CustomContentProvider myContentPrevider = new CustomContentProvider(activity.getApplicationContext());
	}


	public void setNewLocationsGroup(List<Locations_group> _loc_grp){
		this.locations_group = _loc_grp;
	}

	/* (non-Javadoc)
	 * @see com.applidium.headerlistview.SectionAdapter#numberOfSections()
	 */
	@Override
	public int numberOfSections() {
		// TODO Auto-generated method stub
		return locations_group.size();
	}

	/* (non-Javadoc)
	 * @see com.applidium.headerlistview.SectionAdapter#numberOfRows(int)
	 */
	@Override 
	public int numberOfRows(int section) {
		//Log.i(" numberOfRows ==> "," " + section);

		return locations_group.get(section).getLocations().size(); 

	}

	/* (non-Javadoc)
	 * @see com.applidium.headerlistview.SectionAdapter#getRowView(int, int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getRowView(int section, int row, View convertView,
			ViewGroup parent) {
		TextView txt = null , txt_discript = null;
		//ImageView img = null;
		//List<Location> loc = new ArrayList<Location>(locations_group.get(section).getLocations1());
		if (convertView == null) {
			convertView = (View) activity.getLayoutInflater().inflate(R.layout.item_element_carte, null, false); 
			/** define colors **/
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

			convertView.setMinimumHeight(100);
			convertView.setBackgroundDrawable(drawable);
			 
			
			//txt = (TextView)(((LinearLayout)convertView).findViewById(R.id.layoutContentLieu)).findViewById(R.id.content_lieu);

			txt = (TextView) convertView.findViewById(R.id.content_lieu);
			txt.setTextColor(color_txt);
			txt.setTypeface(font);

			//imgBtn = ((ImageButton)(((LinearLayout)convertView).findViewById(R.id.layoutContentLieu)).findViewById(R.id.itineraireBtn));
			//imgBtn.setColorFilter(imgColorFilter);
//			img = (ImageView)convertView.findViewById(R.id.itineraireBtn);
//
//			//img.setBackgroundDrawable(drawable);
//			img.setTag(getRowItem(section, row));
//			img.setOnClickListener(this);
			
			txt_discript = (TextView) convertView.findViewById(R.id.content_lieu_discript);
			txt_discript.setTextColor(color_txt_dscript);
//			txt_discript.setTypeface(font_discript, Typeface.NORMAL);
//			if(hideList || !activity.getResources().getBoolean(R.bool.isTablet)){
//				//txt.setTypeface(font, Typeface.BOLD);
//				txt.setTextSize(20);
//				txt_discript.setTextSize(13);
//
//			}else{
//				//txt_discript.setTypeface(font_discript, Typeface.NORMAL);
//			}


//			txt.setWidth(40);
//			txt.setGravity(Gravity.RIGHT); // attempt at justifying text 
//			txt.setMaxLines(1);
//			txt.setText("Hi");
//			Log.e(" ConvertView ===> null ", " <<>>");

		}
		else
		{

			txt = (TextView) convertView.findViewById(R.id.content_lieu);
			txt.setTextColor(color_txt);
			txt_discript = (TextView) convertView.findViewById(R.id.content_lieu_discript);
			txt_discript.setTextColor(color_txt_dscript);
			//		txt_discript.setTypeface(font_discript);

		}
		//convertView.setMinimumHeight(70);
		//		if(locations_group.get(section) != null /*&& row < locations_group.get(section).getLocations().size() */){ 
		txt.setText(getRowItem(section, row).getTitle()/* + " Link Web URL : " + locations_group.get(section).getLocations().get(row).getLink_web_url()*/);
		if(getRowItem(section, row).getText().isEmpty())
			txt_discript.setVisibility(View.GONE);
		
		else {
			txt_discript.setVisibility(View.VISIBLE);
			txt_discript.setText(getRowItem(section, row).getText().replace("\n", " "));
		}
		//Log.i(" getRowItem(section, row).getTitle() ==> ",getRowItem(section, row).getTitle());
		convertView.setTag(getRowItem(section, row));
		//		}


		return convertView;
	} 

	@Override
	public Object getSectionHeaderItem(int section) {


		return locations_group.get(section);
	}

	/* (non-Javadoc)
	 * @see com.applidium.headerlistview.SectionAdapter#getRowItem(int, int) 
	 */
	@Override
	public Location getRowItem(int section, int row) { 
		//Log.i(" getRowItem ==> "," section " + section +" row " + row);
		return locations_group.get(section).getLocations().get(row); 
	}

	@Override
	public int getSectionHeaderViewTypeCount() {
		return 2;
	}

	@Override
	public int getSectionHeaderItemViewType(int section) {
		return section % 2;
	}

	@Override
	public boolean hasSectionHeaderView(int section) {
		return true;
	}


	@Override
	public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
		TextView title = null;
		ImageView img = null;
		if (convertView == null) 
		{

			convertView = activity.getLayoutInflater().inflate(activity.getResources().getLayout(R.layout.item_section_carte), null, false);
			/** define colors **/
			//convertView.setBackgroundDrawable(colors.getForePD());

			//convertView.setBackgroundColor(colors.getColor(colors.getForeground_color()));

			img = ((ImageView) convertView.findViewById(R.id.carte_imageView));
			img.setColorFilter(imgColorFilter);

			title = ((TextView) convertView.findViewById(R.id.type_lieu));
			title.setTypeface(font);
			title.setTextColor(colors.getColor(colors.getBackground_color()));


		}
		else
		{
			img = ((ImageView) convertView.findViewById(R.id.carte_imageView));
			title = ((TextView) convertView.findViewById(R.id.type_lieu));
		}



		//Glide.with(activity).load("content://com.euphor.listmapcarte.CustomContentProvider/"+locations_group.get(section).getPin_icon()).into((ImageView) convertView.findViewById(R.id.carte_imageView));
		Glide.with(activity).load(new File("android_asset/"+locations_group.get(section).getPin_icon())).into(img);
		//((ImageView) convertView.findViewById(R.id.carte_imageView)).setImageURI(Uri.fromFile(new File("android_asset/"+locations_group.get(section).getPin_icon())));//.setImageResource(R.drawable.ic_launcher);

		//if(locations_group.get(section) != null){

        convertView.setBackgroundColor(colors.getColor(locations_group.get(section).getPin_color()));

		title.setText(locations_group.get(section).getTitle()); 


		//  convertView.setBackgroundColor(new Color().rgb(64, 0, 0));


		return convertView;
	}


	@Override
	public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
		super.onRowItemClick(parent, view, section, row, id);
		Location location = (Location) view.getTag();
		Myinterface.drawMarkerAtLocation(location, section, hideList || !Utils.isTablet(activity));

	}

}
