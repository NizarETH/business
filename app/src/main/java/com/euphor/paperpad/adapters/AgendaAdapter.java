/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Event;
import com.euphor.paperpad.utils.Colors;

import com.euphor.paperpad.widgets.AutoResizeTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author euphordev02
 *
 */
public class AgendaAdapter extends BaseAdapter {

	private Context context;
	private List<Event> elements;
	private Colors/*Colors*/ colors;
	private MainActivity mainActivity;

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
	public Event getItem(int position) {
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
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.agenda_list_item, parent,false);
		ViewHolder holder = new ViewHolder();
		holder.position = position;
		Event event = getItem(holder.position);
		holder.monthTV = (TextView)view.findViewById(R.id.TVMonth);
		holder.dayTV = (TextView)view.findViewById(R.id.TVDay);
		holder.timeTV = (TextView)view.findViewById(R.id.TVTime);
		holder.titleTV = (AutoResizeTextView)view.findViewById(R.id.TVTitleAgenda);
		Date date = parseDate(event.getDate());
		Calendar c = parseDateBeta(event.getDate());
		String txt = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.FRENCH);;
		holder.monthTV.setText(txt);
		holder.monthTV.setTextColor(colors.getColor(colors.getBackground_color()));
		ColorStateList txtStates = new ColorStateList(
				new int[][] {new int[] { android.R.attr.state_pressed }, new int[] {} },
				new int[] {colors.getColor(colors.getTitle_color()), colors.getColor(colors.getBackground_color()) });
		holder.monthTV.setTextColor(txtStates);
		SimpleDateFormat month_date = new SimpleDateFormat("DD", Locale.FRENCH);
		txt = month_date.format(date);
		txt = date.getDay()+"";
		if (txt.length()==1) {
			txt = "0"+txt;
		}
//		txt = c.get(Calendar.DAY_OF_MONTH)+"";
		holder.dayTV.setText(txt);
		holder.dayTV.setTextColor(txtStates);
		month_date = new SimpleDateFormat("HH:mm", Locale.FRENCH);
		txt = month_date.format(date);
//		txt = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
		holder.timeTV.setText(txt);
		holder.timeTV.setTextColor(txtStates);
		holder.titleTV.setText(event.getTitle());
		ColorStateList titleStates = new ColorStateList(
				new int[][] {new int[] { android.R.attr.state_pressed }, new int[] {} },
				new int[] {colors.getColor(colors.getBackground_color()), colors.getColor(colors.getTitle_color()) });
		holder.titleTV.setTextColor(titleStates);
		
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		StateListDrawable backItem = new StateListDrawable();
		backItem.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
		backItem.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color()))); 
		view.setBackgroundDrawable(backItem);
		LinearLayout dateHolder = (LinearLayout)view.findViewById(R.id.DateHolder);
		dateHolder.setBackgroundDrawable(colors.getForePD());
//		StateListDrawable backDate = new StateListDrawable();
//		backDate.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
//		backDate.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color()))); 
//		backDate.addState(new int[]{}, colors.getForePD()); 
//		dateHolder.setBackgroundDrawable(backDate);
		
		return view;
	}

	private Date parseDate(String date) {
		String yearB = date.substring(0, 4);
		int year = Integer.parseInt(yearB);
		String monthB = date.substring(5, 7);
		int month = Integer.parseInt(monthB);
		String dayB = date.substring(8, 10);
		int day = Integer.parseInt(dayB);
		String hourB = date.substring(11, 13);
		int hour = Integer.parseInt(hourB);
		String minB = date.substring(14, 16);
		int min = Integer.parseInt(minB);
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, day, hour, min);
		Date a = new Date(year-1900, month-1, day, hour, min);
		return a;
	}
	
	public static Calendar parseDateBeta(String date) {
		String yearB = date.substring(0, 4);
		int year = Integer.parseInt(yearB);
		String monthB = date.substring(5, 7);
		int month = Integer.parseInt(monthB);
		String dayB = date.substring(8, 10);
		int day = Integer.parseInt(dayB);
		String hourB = date.substring(11, 13);
		int hour = Integer.parseInt(hourB);
		String minB = date.substring(14, 16);
		int min = Integer.parseInt(minB);
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, day, hour, min);
		Date a = new Date(year-1900, month-1, day, hour, min);
		return c;
	}

	/**
	 * @param imageLoader 
	 * @param options 
	 * 
	 */
	public AgendaAdapter(MainActivity activity, List<Event> elements, /*Colors*/ Colors colors) {
		this.mainActivity = activity;
		this.context = activity.getApplicationContext();
		this.elements = elements;
		 this.colors = colors;
		 
	}

	/**
	 * @return the elements
	 */
	public List<Event> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<Event> elements) {
		this.elements = elements;
	}
	
	static class ViewHolder{
		TextView monthTV;
		TextView dayTV; 
		TextView timeTV;
		AutoResizeTextView titleTV;
		int position;
	}

}
