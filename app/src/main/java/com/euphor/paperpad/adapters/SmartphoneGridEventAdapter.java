package com.euphor.paperpad.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Event;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


import java.io.File;
import java.util.List;

public class SmartphoneGridEventAdapter extends ArrayAdapter<Event>  {


	private Context context;

	DisplayImageOptions opts ;

	private Colors colors;

	private int layout_item;

	private int txtSize;
	private ColorStateList colorStateList;
	private ColorStateList colorStateListDiscript;
	private List<Event> events;



	@Override
	public int getCount() {

		return events.size();
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		//Log.i(" getDropDownView ("+position+", "+convertView+", "+parent+")  : ",	""+convertView  );
		return super.getDropDownView(position, convertView, parent);
	}

//	@Override
//	public long getItemId(int position) {
//
//		return super.getItemId(position);
//	}


	@Override
	public int getPosition(Event item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

@Override
public Event getItem(int position) {
//	if(super.getItem(position) != null)
//		return super.getItem(position);
		return events.get(position);
}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView; 

		if(events == null)return view;
		
		Event element = events.get(position); // getItem(position);
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
			d.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color(),"60")));
			view.setBackgroundDrawable(d);
//			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			//view.setMinimumHeight(90);


			holder.txtTitle = (TextView)view.findViewById(R.id.content_event);

			holder.txtTitle.setTextSize(txtSize);
			holder.txtTitle.setTextColor(colorStateList);


			
			holder.txtDiscript = (TextView)view.findViewById(R.id.content_event_discript);
			holder.txtDiscript.setTextSize(txtSize - 4);
			holder.txtDiscript.setTextColor(colorStateListDiscript);



			holder.imageEvent = (ImageView)view.findViewById(R.id.imageEvent);


			




			view.setTag(holder);
		}else {

			holder = (RecordHolder)view.getTag();
		}
		
		
		
		if (element != null && element.getIllustration() != null) {
			holder.imageEvent.setVisibility(View.VISIBLE);
			holder.imageEvent.setScaleType(ScaleType.CENTER_CROP);
			
			String path = element.getIllustration().getPath(); //.getIllustration();
			try {
				if (!path.isEmpty()) {
					Glide.with(context).load(new File(path)).into(holder.imageEvent);
				}else if(!element.getIllustration().getLink().isEmpty()){
					Glide.with(context).load(element.getIllustration().getLink()).into(holder.imageEvent);
				}else
					((View) holder.imageEvent.getParent()).setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			((View) holder.imageEvent.getParent()).setVisibility(View.GONE);
		}	

		//                  holder.txtTitle.setTextColor(colors.getColor(colors.getTitle_color()));
		holder.txtTitle.setText(element.getTitle());
		if(element.getIntro() != null && !element.getIntro().isEmpty())
			holder.txtDiscript.setText(element.getIntro());
		else
			holder.txtDiscript.setVisibility(View.GONE);
		
		ArrowImageView arrowImg = (ArrowImageView)view.findViewById(R.id.imgArrow);
		arrowImg.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
		
		StateListDrawable d_ = new StateListDrawable();
		d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
		d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		
		arrowImg.setDiffOfColorCode(colors.getColor(colors.getForeground_color()), colors.getColor(colors.getBackground_color()));
		arrowImg.setBackgroundDrawable(d_);
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


	public SmartphoneGridEventAdapter(MainActivity activity, List<Event> events, Colors colors, int layout_item, int txtSize) {
		
		super(activity, layout_item, events);
		this.txtSize = txtSize;
		this.context = activity.getApplicationContext();
		this.events = events;
		this.colors = colors;
		this.layout_item = R.layout.item_event_sp_grid;
		
		
		colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getTitle_color())});
		
		colorStateListDiscript = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color(),"50"), 
				colors.getColor(colors.getTitle_color(),"80")});

	}


	public void setEvents(List<Event> events) {
		this.events = events;
	}


	static class RecordHolder {
		ImageView imageEvent;
		TextView txtTitle;
		TextView txtDiscript;

	}

}
