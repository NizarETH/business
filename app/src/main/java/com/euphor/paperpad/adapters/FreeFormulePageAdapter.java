package com.euphor.paperpad.adapters;

import android.R.style;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.applidium.headerlistview.SectionAdapter;
import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.FreeFormulaFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;


import java.io.File;
import java.util.List;

public class FreeFormulePageAdapter extends SectionAdapter {

	private Context context;
	private List<Category> elements;
	private Colors colors;
	private CallbackRelatedLinks_ callBack;
	public CallbackRelatedLinks_ getCallBack() {
		return callBack;
	}


	public void setCallBack(CallbackRelatedLinks_ callBack) {
		this.callBack = callBack;
	}

	private ColorStateList color_txt;
	
	
	public FreeFormulePageAdapter(Context context, List<Category> elements, Colors colors, FreeFormulaFragment fragment) {
		this.context = context;
		

		this.elements = elements;
		 this.colors = colors;
		this.callBack = (CallbackRelatedLinks_) fragment;
		 
			color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
					new int[]{colors.getColor(colors.getBackground_color()), Color.GRAY});//colors.getBackMixColor(colors.getTitle_color(), 0.40f)});

	}
	
	
	@Override
	public void onRowItemClick(AdapterView<?> parent, View view, int section,
			int row, long id) {
		super.onRowItemClick(parent, view, section, row, id);
		callBack.onChildPageClick((Child_pages) getRowItem(section, row));

	}
	
	
	@Override
	public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
		TextView title = null ;
		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.free_formule_item_list, parent,false);
			convertView.findViewById(R.id.img_formule).setVisibility(View.GONE);
			/** define colors **/
			convertView.setBackgroundColor(colors.getColor(colors.getForeground_color(), "CC"));//(colors.getForePD());
			convertView.setMinimumHeight(30);



		}
		title = ((TextView) convertView.findViewById(R.id.tv_formule));
		//title.setLayoutParams(txtParams);
		title.setTextAppearance(context, style.TextAppearance_Medium);
		title.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
		DisplayMetrics metrics = new DisplayMetrics(); 
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics); 

		if(((Activity)context).getResources().getBoolean(R.bool.isTablet)){
			if(metrics.densityDpi >= 213 )
				title.setTextSize(title.getTextSize() - 3);
			else
				title.setTextSize(title.getTextSize());
		}
		else{
			if(metrics.densityDpi <= 240 )
				title.setTextSize(title.getTextSize() - 14);
			else
				title.setTextSize(title.getTextSize() - 20);

		}
		//title.setTextSize(title.getTextSize() - 4);
		title.setTextColor(colors.getColor(colors.getBackground_color()));
		
		title.setText(getSectionHeaderItem(section).getCommunTitle1());
		
		
		return convertView;
	}

	@Override
	public Category getSectionHeaderItem(int section) {
		return  elements.get(section);
	}

	@Override
	protected int getSection(int position) {
		
		return super.getSection(position);
	}

	@Override
	public int numberOfSections() {
		
		return elements.size();
	}

	@Override
	public int numberOfRows(int section) {
		Category category  = (Category) elements.get(section);
		return category.getChildren_pages().size();
	}

	@Override
	public View getRowView(int section, int row, View convertView,
			ViewGroup parent) {
		View view = convertView;
		
		final CommunElements1 element = getRowItem(section, row);
		ViewHolder holder = new ViewHolder();
		
		if (view==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.free_formule_item_list, parent,false);
			
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(Color.parseColor("#AAffffff")));

			//view.setMinimumHeight(100);
			view.setBackgroundDrawable(drawable);
			
			holder.title = (TextView)view.findViewById(R.id.tv_formule);
			holder.title.setTextAppearance(context, style.TextAppearance_Medium);
			DisplayMetrics metrics = new DisplayMetrics(); 
			((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics); 

			if(Utils.isTablet(((Activity)context))){
				if(metrics.densityDpi >= 213 )
					holder.title.setTextSize(holder.title.getTextSize() - 3);
				else
					holder.title.setTextSize(holder.title.getTextSize());
			}
			else{
				if(metrics.densityDpi <= 240 )
					holder.title.setTextSize(holder.title.getTextSize() - 14);
				else
					holder.title.setTextSize(holder.title.getTextSize() - 20);

			}
			//holder.title.setTextSize(holder.title.getTextSize() - 2);
			holder.title.setTypeface(MainActivity.FONT_BODY);
			holder.imageItem = (ImageView)view.findViewById(R.id.img_formule);
			
		view.setTag(holder);

		}else {
			holder = (ViewHolder)view.getTag();			
		}
			

		holder.title.setText(element.getCommunTitle1());
		holder.title.setTextColor(color_txt);


		if (element.getIllustration1() != null) {
			
			holder.imageItem.setScaleType(ScaleType.CENTER_CROP);
			Illustration illustration = element.getIllustration1();
			//String path = !illustration.getPath().isEmpty()?"file:///"+illustration.getPath():illustration.getLink();
			//imageLoader.displayImage(path, holder.imageItem, options);
			if(!illustration.getPath().isEmpty())
				Glide.with(context).load(new File(illustration.getPath())).into(holder.imageItem);
			else
				Glide.with(context).load(illustration.getLink()).into(holder.imageItem);
		}
		else{
			holder.imageItem.setVisibility(View.GONE);
		}
		
		return view;
	}

	@Override
	public CommunElements1 getRowItem(int section, int row) {
//		Category category  = elements.get(section);
//		List<CommunElements> pages = new ArrayList<CommunElements>();
//		pages.addAll(category.getChildren_pages1());

		return elements.get(section).getChildren_pages().get(row); //pages.get(row);
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

	
	public List<Category> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<Category> elements) {
		this.elements = elements;
	}
	
	static class ViewHolder{
		TextView title;
		ImageView imageItem;
		int position;
	}
	
	public interface CallbackRelatedLinks_{
        void onChildPageClick(Child_pages child_pages);




    }

}
