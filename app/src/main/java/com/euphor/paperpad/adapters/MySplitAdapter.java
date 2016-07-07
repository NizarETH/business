package com.euphor.paperpad.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.util.List;

public class MySplitAdapter extends ArrayAdapter<CommunElements1> {

	private Context context;
	private List<CommunElements1> elements;
	//	private ImageLoader imageLoader;
	///private DisplayImageOptions opts ;
	//	private DisplayImageOptions options;
	private Colors colors;
	//	private MainActivity mainActivity;
	//	private Parameters parameters;
	private int layout_item;

	//private ColorStateList color_txt;

	private int txtSize;

	

	
	@Override
	public int getCount() {

		return elements.size();
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		//Log.i(" getDropDownView ("+position+", "+convertView+", "+parent+")  : ",	""+convertView  );
		return super.getDropDownView(position, convertView, parent);
	}

	@Override
	public long getItemId(int position) {

		return super.getItemId(position);
	}

	//	@Override
	//	public int getPosition(String item) {
	//		
	//		return super.getPosition(item);
	//	}

	@Override
	public int getPosition(CommunElements1 item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

//	public void setItemSelected(int position){
//		mItemSelected=position;
//		Log.e(" setItemSelected : ",""+mItemSelected);
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.e(" getView ("+position+", "+convertView+", "+parent+")  : ",	""+convertView  );
		// assign the view we are converting to a local variable
		View view = convertView; 

		CommunElements1 element = getItem(position);
		RecordHolder holder = new RecordHolder();

		if(view == null) {


			//				if (view==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//					layout_item = R.layout.categories_list_item;
			view = inflater.inflate(layout_item, parent,false);

			/** Uness Modif **/

			StateListDrawable d = new StateListDrawable();
			d.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			//					d.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
			//					d.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));//colors.makeGradientToColor(colors.getForeground_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color(),"CC")));
			d.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			view.setBackgroundDrawable(d);
			view.setMinimumHeight(90);
			holder.txtTitle = (TextView)view.findViewById(R.id.TVTitleCategory);
			holder.txtTitle.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Large);
			//holder.txtTitle.setTextSize(txtSize);
			holder.txtTitle.setTextColor(/*colors.getColor(colors.getTitle_color()));*/
										new ColorStateList(new int[][]{{android.R.attr.state_activated},{}}, 
										new int[]{colors.getColor(colors.getBackground_color()), 
										colors.getColor(colors.getTitle_color())})/*color_txt*/ /** Uness_Modif colors.getColor(colors.getTitle_color())**/);


			holder.arrowImg = (ArrowImageView)view.findViewById(R.id.imgArrow);
			holder.arrowImg.setLayoutParams(new LinearLayout.LayoutParams(txtSize+3, txtSize+2));
			
			StateListDrawable d_ = new StateListDrawable();
			d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
			d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			
			holder.arrowImg.setDiffOfColorCode(colors.getColor(colors.getForeground_color()), colors.getColor(colors.getBackground_color()));
			//holder.arrowImg.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			holder.arrowImg.setBackgroundDrawable(d_);

			holder.imageItem = (ImageView)view.findViewById(R.id.imgCategory);
			view.setTag(holder);
		}else {

			holder = (RecordHolder)view.getTag();
			
		}
		
		if(position != 1 /*&& SplitListCategoryFragment_.mActivatedPosition - 2 != position*/){
			
			StateListDrawable d = new StateListDrawable();
			d.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
			d.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

			view.setBackgroundDrawable(d);
			
			holder.txtTitle.setTextColor(/*colors.getColor(colors.getTitle_color()));*/
					new ColorStateList(new int[][]{{android.R.attr.state_activated},{}}, 
					new int[]{colors.getColor(colors.getBackground_color()), 
					colors.getColor(colors.getTitle_color())})/*color_txt*/ /** Uness_Modif colors.getColor(colors.getTitle_color())**/);

			
			StateListDrawable d_ = new StateListDrawable();
			d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
			d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

			
			holder.arrowImg.setDiffOfColorCode(colors.getColor(colors.getForeground_color()), colors.getColor(colors.getBackground_color()));
			holder.arrowImg.setBackgroundDrawable(d_);
		}

		//                  holder.txtTitle.setTextColor(colors.getColor(colors.getTitle_color()));
		
		if (element.getIllustration1() != null) {
			holder.imageItem.setVisibility(View.VISIBLE);
			holder.imageItem.setScaleType(ScaleType.CENTER_CROP);
			Illustration illustration = element.getIllustration1();
			try {
				
				if (illustration.getPath() != null && !illustration.getPath().isEmpty()) {
					Glide.with(context).load(new File(illustration.getPath())).into(holder.imageItem);
				}else {
					Glide.with(context).load(illustration.getLink()).into(holder.imageItem);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			holder.imageItem.setVisibility(View.GONE);
		}	

		holder.txtTitle.setText(element.getCommunTitle1());



		return view;
	}



	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public void setDropDownViewResource(int resource) {
		super.setDropDownViewResource(resource);
	}

	@Override
	public void setNotifyOnChange(boolean notifyOnChange) {
		super.setNotifyOnChange(notifyOnChange);
	}


	public MySplitAdapter(MainActivity activity, List<CommunElements1> elements, Colors colors, int layout_item, int txtSize) {
		
		super(activity, layout_item, elements);
		this.txtSize = txtSize;
		this.context = activity.getApplicationContext();
		this.elements = elements;
		this.colors = colors;
		this.layout_item = layout_item;
		
	}





	static class RecordHolder {
		TextView txtTitle;
		ImageView imageItem;
		ArrowImageView arrowImg;

	}

}
