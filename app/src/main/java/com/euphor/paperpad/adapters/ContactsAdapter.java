/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.utils.Colors;

import java.io.IOException;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class ContactsAdapter extends BaseAdapter {

	private Context context;
	private List<Contact> elements;
//	private ImageLoader imageLoader;
//	DisplayImageOptions opts ;
//	private DisplayImageOptions options;
	private Colors colors;
	private MainActivity mainActivity;
//	private Parameters parameters;
	private Bitmap bm;

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return elements.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Contact getItem(int position) {
		// TODO Auto-generated method stub
		return elements.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
//		if (view==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.contact_list_item, parent,false);
//		}
		//if (view==null) { // this has been removed due to problem loading views that are not shown first
			Contact element = getItem(position);
			ViewHolder holder = new ViewHolder();
			holder.position = position;
			
			holder.title = (TextView)view.findViewById(R.id.TVTitleContact);
//			titleTV.setMinTextSize(24);
//			holder.title.setTextSize(context.getResources().getDimension(R.dimen.subtitleSize));
			holder.title.setText(element.getTitle());
			holder.title.setTextColor(colors.getColor(colors.getTitle_color()));

			
			
			holder.desc = (TextView)view.findViewById(R.id.TVDescContact);
//			holder.desc.setTextSize(context.getResources().getDimension(R.dimen.subtitleSize));
			holder.desc.setTextColor(colors.getColor(colors.getBody_color(), "AA"));
//			descTV.setMinTextSize(context.getResources().getDimension(R.dimen.subtitleSize)-4f);
			if (element.getDetails().isEmpty()) {
				holder.desc.setVisibility(View.GONE);
			}else {
				holder.desc.setText(element.getDetails());
			}
			
			holder.imageItem = (ImageView)view.findViewById(R.id.imgContact);
			if (element.getIcon() != null && !element.getIcon().isEmpty()) {
				bm = null;
				try {
					bm = BitmapFactory.decodeStream(mainActivity.getAssets().open(element.getIcon()));
				} catch (IOException e) {
					Log.e("Contact Fragment", e.getMessage());
					e.printStackTrace();
				}
			}else {
				bm = getSpecifiedTypeBitmap(element.getType());
			}
			BitmapDrawable mDrawableLeft = new BitmapDrawable(bm);
			mDrawableLeft.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
			holder.imageItem.setImageDrawable(mDrawableLeft);
			
			/*
			 * show the price if it's a list of child categories
			 */
//			parameters = null;
//			try {
//				parameters = mainActivity.appController.getParametersDao().queryForId(1);
//				
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		return view;
	}

	/**
	 * @param imageLoader 
	 * @param options 
	 * 
	 */
	public ContactsAdapter(MainActivity activity, List<Contact> elements, Colors colors) {
		this.mainActivity = activity;
		this.context = activity.getApplicationContext();
		this.elements = elements;
		 this.colors = colors;
		 
	}

	/**
	 * @return the elements
	 */
	public List<Contact> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<Contact> elements) {
		this.elements = elements;
	}
	
	static class ViewHolder{
		TextView title;
		TextView desc; 
		ImageView imageItem;
		LinearLayout btnPrice;
		LinearLayout btnPrice2;
		LinearLayout btnPrice3;
		int position;
	}
	
	
	public Bitmap getSpecifiedTypeBitmap(String type) {
		TypeContact contact = null;
		try {
			contact = TypeContact.valueOf(type);
		} catch (Exception e1) {
			Log.e("Verify or add this type :", type);
			e1.printStackTrace();
		}
		try {
			switch (contact.id) {
			case 1: // email icon/group_12/icon_0_12.png

				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_12/icon_0_12.png"));

				break;

			case 2: // phone
					// http://backoffice.paperpad.fr/icon/group_0/icon_0_0.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_0/icon_0_0.png"));
				break;
			case 3: // location
					// http://backoffice.paperpad.fr/icon/group_8/icon_0_8.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_8/icon_0_8.png"));
				break;
			case 4: // reservation
					// http://backoffice.paperpad.fr/icon/group_1/icon_0_1.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_1/icon_0_1.png"));
				break;
			case 5: // facebook
					// http://backoffice.paperpad.fr/icon/group_22/icon_0_22.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_22/icon_0_22.png"));
				break;
			case 6: // boutique icon/group_38/icon_0_38.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_38/icon_0_38.png"));
				break;
			case 7: // gift
					// http://backoffice.paperpad.fr/icon/group_23/icon_0_23.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_23/icon_0_23.png"));
				break;
			case 8: // michelin icon/group_38/icon_0_38.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_38/icon_0_38.png"));
				break;
			case 9: // trip_advisor
					// http://backoffice.paperpad.fr/icon/group_22/icon_2_22.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_22/icon_2_22.png"));
				break;
			case 10: // twitter
						// http://backoffice.paperpad.fr/icon/group_22/icon_1_22.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_22/icon_1_22.png"));
				break;

			default:// http://backoffice.paperpad.fr/icon/group_38/icon_0_38.png
				bm = BitmapFactory.decodeStream(mainActivity.getAssets().open("icon/group_38/icon_0_38.png"));
				break;
			}
		} catch (IOException e) {
			bm = null;
			e.printStackTrace();
		}
		return bm;

	}
	
public enum TypeContact{
		
		email("email",1),
		phone("phone",2),
		location("location",3),
		reservation("reservation",4),
		facebook("facebook",5),
		boutique("boutique",6),
		gift("gift",7),
		michelin("michelin",8),
		trip_advisor("trip_advisor",9),
		twitter("twitter",10),
		form("form",11);
		
		
		private String name;
		private int id;
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}
		
		private TypeContact(){
			
		}
		/**
		 * @param name
		 * @param ordinal
		 */
		private TypeContact(String name, int ordinal) {
			this.name = name;
			this.id = ordinal;
		}
		

	}

}
