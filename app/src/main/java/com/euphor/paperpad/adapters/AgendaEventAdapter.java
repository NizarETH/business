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
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.utils.Colors;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


import java.io.File;
import java.util.List;

public class AgendaEventAdapter extends ArrayAdapter<CommunElements1>  {


	private Context context;
	private List<CommunElements1> elements;

	DisplayImageOptions opts ;

	private Colors colors;

	private int layout_item;

	private int txtSize;
	private ColorStateList colorStateList;
	private ColorStateList colorStateListDiscript;



	@Override
	public int getCount() {

		return elements.size();
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
	public int getPosition(CommunElements1 item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

@Override
public CommunElements1 getItem(int position) {
//	if(super.getItem(position) != null)
//		return super.getItem(position);
		return elements.get(position);
}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView; 

		if(elements == null)return view;
		
		CommunElements1 element = elements.get(position); // getItem(position);
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


			
			holder.DayNumber = (TextView)view.findViewById(R.id.dayNumber);
			holder.DayNumber.setTextSize(txtSize - 3);
			holder.DayNumber.setTextColor(colors.getColor(colors.getTitle_color()));
			holder.DayNumber.setBackgroundColor(colors.getColor(colors.getBackground_color()));




			view.setTag(holder);
		}else {

			holder = (RecordHolder)view.getTag();
		}
		
		
		
		if (((Child_pages)element) != null && ((Child_pages)element).getIllustration() != null) {
			holder.imageEvent.setVisibility(View.VISIBLE);
			holder.imageEvent.setScaleType(ScaleType.CENTER_CROP);
			
			String path = ((Child_pages)element).getIllustration().getPath(); //.getIllustration();
			if (path != null && !path.isEmpty()) {

				Glide.with(context).load(new File(path)).into(holder.imageEvent);
			}else {
                path = (element.getIllustration1().getLink() == null || element.getIllustration1().getLink().isEmpty()) ? "http://" : element.getIllustration1().getLink();
				Glide.with(context).load(path).into(holder.imageEvent);
			}
		}
		else {
			holder.imageEvent.setVisibility(View.GONE);
		}	

		//                  holder.txtTitle.setTextColor(colors.getColor(colors.getTitle_color()));
		holder.txtTitle.setText(((Child_pages)element).getTitle());
		holder.txtDiscript.setText(((Child_pages)element).getIntro());
		holder.DayNumber.setText(((Child_pages)element).getDay()); /** for testing **/
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


	public AgendaEventAdapter(MainActivity activity, List<CommunElements1> elements, Colors colors, int layout_item, int txtSize) {

		super(activity, layout_item, elements);
		this.txtSize = txtSize;
		this.context = activity.getApplicationContext();
		this.elements = elements;
		this.colors = colors;
		this.layout_item = layout_item;
		
		
		colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getTitle_color())});
		
		colorStateListDiscript = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color(),"50"), 
				colors.getColor(colors.getTitle_color(),"80")});

	}


	public void setEvents(List<CommunElements1> elements) {
		this.elements = elements;
	}


	static class RecordHolder {
		TextView DayNumber;
		ImageView imageEvent;
		TextView txtTitle;
		TextView txtDiscript;

	}

}
