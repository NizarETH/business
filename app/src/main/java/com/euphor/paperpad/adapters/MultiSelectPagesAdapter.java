/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Price;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.AutoResizeTextView;

import java.util.Collection;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class MultiSelectPagesAdapter extends BaseAdapter {

	private Context context;
	private List<CommunElements1> elements;
//	private ImageLoader imageLoader;
//	DisplayImageOptions opts ;
//	private DisplayImageOptions options;
	private Colors colors;
	private String checkBoxes_color;
	private int txtSize;

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
	public CommunElements1 getItem(int position) {
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
		if (view==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.multi_select_list_item, parent,false);
		}
		//if (view==null) { // this has been removed due to problem loading views that are not shown first
			CommunElements1 element = getItem(position);
			ViewHolder holder = new ViewHolder();
			holder.position = position;
			
			holder.title = (AutoResizeTextView)view.findViewById(R.id.TVTitleCategory);
//			titleTV.setMinTextSize(24);
			holder.title.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
			holder.title.setTextSize(txtSize - 2);
			holder.title.setText(element.getCommunTitle1());
			holder.title.setTextColor(colors.getColor(colors.getTitle_color()));

			//Color color_checkBox;
			/*
			 * show the price if it's a list of child categories
			 */
			if (element instanceof Child_pages) {
				Child_pages child_page= (Child_pages)element;
				Collection<Price> prices = child_page.getPrices();
				if (prices.size()>0) {
					Price price = prices.iterator().next();
					TextView value = (AutoResizeTextView)view.findViewById(R.id.priceValueTV);
					value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+" "+price.getCurrency():"");
					value.setTextColor(colors.getColor(colors.getTitle_color()));
					value.setTypeface(MainActivity.FONT_BODY);
					value.setTextSize(txtSize);
				}else {
					LinearLayout priceContainer = (LinearLayout)view.findViewById(R.id.priceTVContainer);
					priceContainer.setVisibility(View.GONE);
				}
				
				
			}
			
			holder.imageItem = (ImageView)view.findViewById(R.id.imgCategory);
			Drawable drawable = context.getResources().getDrawable(R.drawable.multi_select_d);
			if (checkBoxes_color != null && !checkBoxes_color.isEmpty()) {
				drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#"+checkBoxes_color), PorterDuff.Mode.MULTIPLY));
			}else {
				drawable.setColorFilter(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY));
			}
			
			holder.imageItem.setBackgroundDrawable(drawable);
			

		return view;
	}

	/**
	 * @param imageLoader
	 * @param options
	 * @param checkBoxes_color
	 *
	 */
	public MultiSelectPagesAdapter(Context context, List<CommunElements1> elements, Colors colors, String checkBoxes_color) {
		this.context = context;
		this.elements = elements;
		 this.colors = colors;
		 this.checkBoxes_color = checkBoxes_color;
		 
			DisplayMetrics metrics = new DisplayMetrics(); 
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics); 
			
			if(!Utils.isTablet(((Activity) context))){
				this.txtSize = 18;
			}else if(metrics.densityDpi >= 213 ){

				if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					this.txtSize = 24;
				} else if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					this.txtSize = 18;
				}


			}
			else{
				if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					this.txtSize = 28;
				} else if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					this.txtSize = 26;
				}
			}
		 
	}

	/**
	 * @return the elements
	 */
	public List<CommunElements1> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<CommunElements1> elements) {
		this.elements = elements;
	}
	
	static class ViewHolder{
		AutoResizeTextView title;
		ImageView imageItem;
		int position;
	}

}
