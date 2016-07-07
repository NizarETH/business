package com.euphor.paperpad.adapters;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.AgendaGroup;
import com.euphor.paperpad.Beans.Event;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.realm.Realm;

public class CalendarListEventadapter extends BaseAdapter {

	private Context context;
	private Colors colors;
	private List<Event> events;
	//private int firstDayOfMonth, lastDayOfMonth, numberOfDayInMonth, count;

	private List<AgendaGroup> agendaGrps;
	private int count;
    private Realm realm;
	//private int layout_item;


	private ColorStateList colorStateList;
	private ColorStateList colorStateListDiscript;


	public CalendarListEventadapter(Context context, List<Event> events, Colors colors, Realm realm) {



		this.context = context;
		this.colors = colors;
		this.events = events;

		this.count = events.size();
		//this.layout_item = layout_item;


		colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getTitle_color())});

		colorStateListDiscript = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color(),"50"), 
				colors.getColor(colors.getBody_color(),"80")});


		this.realm = realm;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Event getItem(int position) {
		// TODO Auto-generated method stub
		return events.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		String tmp = "";
		if(events == null)return view;

		Event event = events.get(position); // getItem(position);
		RecordHolder holder = new RecordHolder();

		//		if(view == null) {


		//				if (view==null) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//					layout_item = R.layout.categories_list_item;
		view = inflater.inflate(R.layout.item_list_event, parent,false);

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
		holder.txtTitle.setTypeface(MainActivity.FONT_TITLE);
		//holder.txtTitle.setTextSize(txtSize);
		holder.txtTitle.setTextColor(colorStateList);



		holder.txtDiscript = (TextView)view.findViewById(R.id.content_event_discript);
		//holder.txtDiscript.setTextSize(txtSize - 4);
		holder.txtDiscript.setTypeface(MainActivity.FONT_BODY);
		holder.txtDiscript.setTextColor(colorStateListDiscript);



		//holder.imageEvent = (ImageView)view.findViewById(R.id.imageEvent);
		holder.dateLayout = (RelativeLayout)view.findViewById(R.id.imageLayout);

		holder.DayMonth = (TextView)view.findViewById(R.id.dayMonth);
		holder.DayMonth.setTypeface(MainActivity.FONT_BODY);
		holder.DayNumber = (TextView)view.findViewById(R.id.dayNumber);
		holder.DayNumber.setTypeface(MainActivity.FONT_TITLE);
		holder.DayHour = (TextView)view.findViewById(R.id.dayHour);
		holder.DayHour.setTypeface(MainActivity.FONT_BODY);
		//holder.DayNumber.setTextSize(txtSize - 3);
		//holder.DayNumber.setTextColor(colors.getColor(colors.getTitle_color()));
		//holder.DayNumber.setBackgroundColor(colors.getColor(colors.getBackground_color()));


		holder.addEvent = ( LinearLayout)view.findViewById(R.id.addEvent);
		holder.addEvent.setBackgroundColor(colors.getColor(colors.getTitle_color()));

		view.setTag(event);

		List<AgendaGroup> agendaGroup = null;
        agendaGroup = realm.where(AgendaGroup.class).equalTo("id", event.getAgenda_group_id()).findAll();
        //(List<AgendaGroup>) appController.getAgendaGroupDao().queryForEq("id", event.getAgenda_group_id());

        if(agendaGroup != null && agendaGroup.size()>0 && !isValidGrp(agendaGroup.get(0))){
			((LinearLayout)view).removeAllViewsInLayout();
			((LinearLayout)view).requestLayout();
			return view;
		}

		if(agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) {
			ColorStateList color_txt = new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
					new int[]{colors.getColor(colors.getTitle_color()),  colors.getColor(colors.getTitle_color()), 
					colors.getColor(colors.getTitle_color()),  colors.getColor(colors.getTitle_color()), 
					colors.getColor(colors.getBackground_color())});
			
			holder.DayMonth.setTextColor(color_txt);
			holder.DayNumber.setTextColor(color_txt);
			holder.DayHour.setTextColor(color_txt);
			//holder.dateLayout.setBackgroundColor(colors.getColor(agendaGroup.get(0).getColor()));

			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
			view.findViewById(R.id.imageLayout).setBackgroundDrawable(drawable);


		}else if(event.getAgenda_group_id()>0 ) {
			
			ColorStateList color_txt = new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
					new int[]{colors.getColor(colors.getTitle_color()),  colors.getColor(colors.getTitle_color()), 
					colors.getColor(colors.getTitle_color()),  colors.getColor(colors.getTitle_color()), 
					colors.getColor(colors.getBackground_color())});
					
			holder.DayMonth.setTextColor(color_txt);
			holder.DayNumber.setTextColor(color_txt);
			holder.DayHour.setTextColor(color_txt);

			//holder.dateLayout.setBackgroundColor(colors.getColor(colors.getTitle_color()));


			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
			view.findViewById(R.id.imageLayout).setBackgroundDrawable(drawable);

			//holder.dateLayout.setBackgroundColor(colors.getColor(colors.getTitle_color()));

		}
		
		holder.addEvent.setTag(event);
		holder.addEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Event event = (Event)v.getTag();
				if(event != null) {

					Calendar mydate = new GregorianCalendar();
					Date date = null;
					try {
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(event.getDate());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					mydate.setTime(date);

//					int year = mydate.get(Calendar.YEAR); //date.getYear();
//					int month = mydate.get(Calendar.MONTH); //date.getMonth();
//					int day = mydate.get(Calendar.DAY_OF_MONTH); //date.getDay();
//
//					int hours_ = mydate.get(Calendar.HOUR_OF_DAY);
//					int minutes_ = mydate.get(Calendar.MINUTE);;

					long beginTime = mydate.getTimeInMillis();

					String duration = event.getDuration();
					if(duration != null && duration.contains("H")) {
						duration = event.getDuration().split("H")[0]+":"+event.getDuration().split("H")[1];
					}
					else {
						duration = "";
					}
					try {
						date = new SimpleDateFormat("HH:mm").parse(duration);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					mydate.setTime(date);
					
					long endTime =  mydate.getTimeInMillis();

					Intent intent;

					if (Build.VERSION.SDK_INT >= 14) {
						intent = new Intent(Intent.ACTION_INSERT)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						.setData(Events.CONTENT_URI)
						.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)//
						.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)// hour+":"+minute)
						.putExtra(Events.TITLE, event.getTitle())
						.putExtra(Events.DESCRIPTION, event.getIntro())
						//.putExtra(Events.EVENT_LOCATION, event.getLink_page_identifier())
						.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
						//.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");

						//Toast.makeText(context, " NEW VERSION : Event Title : "+event.getTitle(), 1000).show();
					}

					else {
						//Calendar cal = Calendar.getInstance();              
						intent = new Intent(Intent.ACTION_EDIT)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						.setType("vnd.android.cursor.item/event")
						.putExtra("beginTime", beginTime)
						.putExtra("allDay", false)
						.putExtra("rrule", "FREQ=DAILY")
						.putExtra("endTime", endTime)// hour+":"+minute)
						.putExtra("title", event.getTitle());

						//Toast.makeText(context, " Old VERSION : Event Title : "+event.getTitle(), 1000).show();
					}
					context.startActivity(intent);

				}

			}
		});



		//		}else {
		//
		//			holder = (RecordHolder)view.getTag();
		//		}



		//		if (agendaGroup.get(0) != null && agendaGroup.get(0).getColor().isEmpty()) {
		//			holder.dateLayout.setBackgroundColor(colors.getColor(agendaGroup.get(0).getColor()));
		//		}
		//		else {
		//			//holder.imageEvent.setVisibility(View.GONE);
		//		}

		Calendar mydate = new GregorianCalendar();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(event.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mydate.setTime(date);

		int year = mydate.get(Calendar.YEAR); //date.getYear();
		int month = mydate.get(Calendar.MONTH); //date.getMonth();
		int day = mydate.get(Calendar.DAY_OF_MONTH); //date.getDay();

		String hours = ""+mydate.get(Calendar.HOUR_OF_DAY);
		String minutes = ""+mydate.get(Calendar.MINUTE);
		if (hours.length() == 1) {
			hours = "0"+hours;
		}
		if (minutes.length() == 1) {
			minutes = "0"+minutes;
		}

		SharedPreferences wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(context);

		String lang = wmbPreference.getString(Utils.LANG, "fr");
		if(lang.compareTo("en") == 0)
			tmp = DateFormatSymbols.getInstance(Locale.ENGLISH).getShortMonths()[month];//+"\n"+day+"\n"+hours+":"+minutes;
		else
			tmp = DateFormatSymbols.getInstance(Locale.FRENCH).getShortMonths()[month];//+"\n"+day+"\n"+hours+":"+minutes;
//		Spannable span = new SpannableString(tmp);
//		span.setSpan(new RelativeSizeSpan(0.8f), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		span.setSpan(new AbsoluteSizeSpan(20, true), 5, 7,0);
		
		holder.DayMonth.setText(tmp);
		holder.DayNumber.setText(""+day);
		holder.DayHour.setText(""+hours+":"+minutes);

		holder.txtTitle.setText(event.getTitle());
		holder.txtDiscript.setText(event.getIntro());
		//holder.DayNumber.setText(((Child_pages)element).getDay()); /** for testing **/
		//((LinearLayout)view).requestLayout();
		return view;
	}


	public void updateCalendarGridView(List<AgendaGroup> agendaGroups) {
		this.agendaGrps = agendaGroups;
		//		List<Event> evts = new ArrayList<Event>();
		//		for(int i = 0; i < events.size(); i++) {
		//			List<AgendaGroup> agendaGroup = null;
		//			try {
		//				agendaGroup = (List<AgendaGroup>) appcontroller.getAgendaGroupDao().queryForEq("id", events.get(i).getAgenda_group_id());
		//
		//			} catch (SQLException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//				if(agendaGroup != null && agendaGroup.size() < 0 && !isValidGrp(agendaGroup.get(0))) {
		//					events.add(events.get(i));
		//				}
		//			
		//		}
		//		count = evts.size();
		//		events = evts;
		this.notifyDataSetChanged();
	}

	private boolean isValidGrp(AgendaGroup agendaGrp) {

		if(agendaGrps == null)return true;

		for(int i = 0; i < agendaGrps.size(); i++) {
			if(agendaGrps.get(i).isSelected() && agendaGrps.get(i).getId() == agendaGrp.getId())
				return true;
		}
		return false;
	}


	static class RecordHolder {
		TextView DayMonth;
		TextView DayNumber;
		TextView DayHour;
		//ImageView imageEvent;
		RelativeLayout dateLayout;
		TextView txtTitle;
		TextView txtDiscript;
		LinearLayout addEvent;

	}

}
