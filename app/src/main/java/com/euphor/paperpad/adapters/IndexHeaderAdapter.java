package com.euphor.paperpad.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.applidium.headerlistview.SectionAdapter;
import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.ArrowImageView;


public class IndexHeaderAdapter extends SectionAdapter {
	
	private Context context;
	private List<CommunElements1> elements;
	HashMap<String, List<Child_pages>> map;
	private List<String> childPagesKeys;
//	private String POLICE;
//	private ImageLoader imageLoader;
//	DisplayImageOptions opts ;
//	private DisplayImageOptions options;
	private Colors colors;
	private MainActivity mainActivity;
	private int layout_item;
	
	private ColorStateList color_txt;
	
	public IndexHeaderAdapter(MainActivity activity, HashMap<String, List<Child_pages>> map, List<String> childPagesKeys, Colors colors, int layout_item) {
		//this.options = options;
		this.mainActivity = activity;
		this.context = activity.getApplicationContext();
		//this.elements = elements;
		this.map = map;
		this.childPagesKeys = childPagesKeys;
		 this.colors = colors;
		 this.layout_item = layout_item;

		 //this.isNewDesign = isNewDesign;
//			POLICE = "fonts/gill-sans-light.ttf";

		 color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
					new int[]{colors.getColor(colors.getBackground_color()),
					colors.getColor(colors.getTitle_color())});
		 

		 fillCommunElements();




	}
	
	public List<CommunElements1> fillCommunElements(){
		
		elements = new ArrayList<CommunElements1>();
		List<Child_pages> pages = new ArrayList<Child_pages>();
		for(int i = 0; i < childPagesKeys.size(); i++) {
			Child_pages page = new Child_pages();
			page.setTitle(childPagesKeys.get(i));
			page.setVisible(true);
			//CommunElements element = page;
			pages.add(page);
			pages.addAll(map.get(childPagesKeys.get(i)));
		}
		for (Iterator<Child_pages> iterator = pages
				.iterator(); iterator.hasNext();) {
			CommunElements1 communElement = (Child_pages) iterator.next();
			if (((Child_pages)communElement).isVisible()) {
				elements.add(communElement);
			}
		}
		return elements;
	}

	@Override
	public int numberOfSections() {
		// TODO Auto-generated method stub
		return map.size();
	}

	@Override
	public int numberOfRows(int section) {
		
		return map.get(childPagesKeys.get(section)).size();
	}

	@Override
	public View getRowView(int section, int row, View convertView,
			ViewGroup parent) {
		
		CommunElements1 element = getRowItem(section, row);
		ViewHolder holder = new ViewHolder();
		
		View view = convertView; 
		if (view==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			layout_item = R.layout.categories_list_item;
			view = inflater.inflate(layout_item, parent,false);
			
			/** Uness Modif **/
			 
			StateListDrawable d = new StateListDrawable();
			d.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
//			d.addState(new int[]{android.R.attr.state_activated},  colors.makeGradientToColor(colors.getTitle_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color(),"CC")));
//			d.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));//colors.makeGradientToColor(colors.getTitle_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color(),"CC")));
			d.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

			view.setBackgroundDrawable(d);
			
			
			//view.getBackground().setDither(true);
//			view.setMinimumHeight(100);
			
//			Log.e(" Position Item "," "+position);
//		}CommunElements element = getItem(position);

			
			
			holder.title = (TextView)view.findViewById(R.id.TVTitleCategory);
			
//			holder.title.setTypeface(Typeface.createFromAsset(mainActivity.getAssets(), POLICE));
			holder.title.setTextColor(color_txt);
			
			holder.title.setText(element.getCommunTitle1());
			
			holder.imageItem = (ImageView)view.findViewById(R.id.imgCategory);
			

			 //view.findViewById(R.id.priceBtnHolder).setVisibility(View.GONE);
		}else {
			holder.title = (TextView)view.findViewById(R.id.TVTitleCategory);
			
//			holder.title.setTypeface(Typeface.createFromAsset(mainActivity.getAssets(), POLICE));
			holder.title.setTextColor(color_txt);
			
			holder.title.setText(element.getCommunTitle1());
			
			holder.imageItem = (ImageView)view.findViewById(R.id.imgCategory);
			

			 //view.findViewById(R.id.priceBtnHolder).setVisibility(View.GONE);
		}

			if (element.getIllustration1() != null) {
				holder.imageItem.setVisibility(View.VISIBLE);
				holder.imageItem.setScaleType(ScaleType.CENTER_CROP);
				Illustration illustration = element.getIllustration1();
				if (!illustration.getPath().isEmpty()) {
					Glide.with(context).load(new File(illustration.getPath())).into(holder.imageItem);
				}else {
					try {
						Glide.with(context).load(illustration.getLink()).into(holder.imageItem);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			else {
				holder.imageItem.setVisibility(View.GONE);
			}



			
			
			holder.desc = (TextView)view.findViewById(R.id.TVDescCategory);
			holder.desc.setTextColor(colors.getColor(colors.getBody_color(), "AA"));
			if (element.getCommunDesc1().isEmpty()) {
				holder.desc.setVisibility(View.GONE);
			}else {
				holder.desc.setText(element.getCommunDesc1());
			}
			


			

		ArrowImageView arrowImg = (ArrowImageView)view.findViewById(R.id.imgArrow);
//		Paint paint = new Paint();
//		paint.setColor(colors.getColor(colors.getForeground_color(),"AA"));
//		arrowImg.setPaint(paint);
		
		StateListDrawable d_ = new StateListDrawable();
		d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
		d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		
		arrowImg.setDiffOfColorCode(colors.getColor(colors.getForeground_color()), colors.getColor(colors.getBackground_color()));
		arrowImg.setBackgroundDrawable(d_);
		
		view.setTag(getRowItem(section, row));
		
		return view;
	}
	
	@Override
	public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
		TextView title = null;

		if (convertView == null) 
		{
			
//			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////			layout_item = R.layout.categories_list_item;
//			convertView = inflater.inflate(R.layout.item_section_carte, null,false);

			convertView = mainActivity.getLayoutInflater().inflate(mainActivity.getResources().getLayout(R.layout.item_section_carte), null, false);
			//convertView = mainActivity.getLayoutInflater().inflate(mainActivity.getResources().getLayout(R.layout.item_section_carte), parent, false); 
			/** define colors **/
			convertView.setBackgroundColor(colors.getColor(colors.getTitle_color(),"44"));

			convertView.findViewById(R.id.carte_imageView).setVisibility(View.GONE);


			title = ((TextView) convertView.findViewById(R.id.type_lieu));
			title.setTextColor(colors.getColor(colors.getBackground_color()));


		}else {
			title = ((TextView) convertView.findViewById(R.id.type_lieu));
			title.setTextColor(colors.getColor(colors.getBackground_color()));
		}
		title.setText(""+childPagesKeys.get(section).charAt(0));


		return convertView;
	}
	
	public CommunElements1 getCommunElementsItem(int position) {
			return elements.get(position);
	}


	@Override
	public CommunElements1 getRowItem(int section, int row) {
		
		return map.get(childPagesKeys.get(section)).get(row);
	}
	
	
	@Override
	public List<Child_pages> getSectionHeaderItem(int section) {
		
		return map.get(section);
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
	
	
	static class ViewHolder{
		TextView title;
		TextView desc; 
		ImageView imageItem;
	}
	
	@Override
	public void onRowItemClick(AdapterView<?> parent, View view, int section,
			int row, long id) {
		CommunElements1 element = getRowItem(section, row); // because of the listView header we subtract 1
		{ 
			if (element instanceof Category) {
				Category category = (Category)element;
				//Log.e(" OpenCategorie  "," <<< === >>> ");

				mainActivity.openCategory(category);

			}else if (element instanceof Child_pages) {

				Child_pages page = (Child_pages)element;
				Log.e(" OpenChild_pages  "," <<< === >>> ");

				mainActivity.openChildPage(page,false);

			}
		}
		super.onRowItemClick(parent, view, section, row, id);
	}

}
