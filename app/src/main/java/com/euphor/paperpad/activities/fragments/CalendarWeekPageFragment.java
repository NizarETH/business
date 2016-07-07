package com.euphor.paperpad.activities.fragments;



import android.R.style;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.CalendarInfoWindow;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.CalendarEventAdapter;
import com.euphor.paperpad.Beans.AgendaGroup;
import com.euphor.paperpad.Beans.Event;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.actionsPrices.QuickAction.OnDismissListener;
import com.euphor.paperpad.widgets.ObservableScrollView;
import com.euphor.paperpad.widgets.ScrollViewListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

/**
 * @author euphordev04
 *
 */
public class CalendarWeekPageFragment extends Fragment implements ScrollViewListener {


	private List<Event> events;
	private CalendarEventAdapter adapter;
	private Colors colors;
	private int firstDayOfMonth, month, numberOfDayInMonth;
	private String theWeek;

	private String tag;
	private boolean isTablet;
	private int[] days;
	private List<AgendaGroup> agendaGrps;
	private static int[] ids = new int[]{R.id.textView1, R.id.textView2, R.id.textView3};
	private static int[] imgIds = new int[]{R.id.img_column00, R.id.img_column01, R.id.img_column02};
    public Realm realm;

	//	LinearLayout monday_events;
	//	LinearLayout tuesday_events;
	//	LinearLayout wednesday_events;
	//	LinearLayout thursday_events;
	//	LinearLayout friday_events;
	//	LinearLayout saturday_events;
	//	LinearLayout sunday_events;
	private View view;

	public void setCustomTag(String tag)
	{
		this.tag = tag;
	}

	public String getCustomTag()
	{
		return tag;
	}

	public static CalendarWeekPageFragment create(List<Event> events, String string, List<AgendaGroup> agendaGrps){
		CalendarWeekPageFragment fragment = new CalendarWeekPageFragment();
		fragment.setListEvent(events);
		fragment.setTheWeek(string);
		fragment.setAgendaGrps(agendaGrps);
		return fragment;
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		realm = Realm.getInstance(getActivity());
	//new AppController(getActivity());
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//		POLICE = "gill-sans-light.ttf"; //"fonts/OpenSans-Regular.ttf";
		//		font = Typeface.createFromAsset(getActivity().getAssets(), POLICE); 

        Date dateOfMonth = null;
		try {
			if(events.size() > 0)
				dateOfMonth = new SimpleDateFormat("yyyy-MM-dd").parse(events.get(0).getDate());
			//adapter = new CalendarEventAdapter((MainActivity)getActivity(), events, dateOfMonth, colors, appController);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getTypeOfEvents();
		isTablet = Utils.isTablet(getActivity()); //getResources().getBoolean(R.bool.isTablet);

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View view;
		//		if (isTablet) {

		view = inflater.inflate(R.layout.calendar_week_layout, container, false); 


		TextView monday = (TextView)view.findViewById(R.id.monday);
		TextView tuesday = (TextView)view.findViewById(R.id.tuesday);
		TextView wednesday = (TextView)view.findViewById(R.id.wednesday);
		TextView thursday = (TextView)view.findViewById(R.id.thursday);
		TextView friday = (TextView)view.findViewById(R.id.friday);
		TextView saturday = (TextView)view.findViewById(R.id.saturday);
		TextView sunday = (TextView)view.findViewById(R.id.sunday);

		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(getTheWeek());
		makeMatch.find();
		String firstDay = makeMatch.group();
		makeMatch = intsOnly.matcher(getTheWeek().split("\n")[1]);
		makeMatch.find();
		String lastDay = makeMatch.group();
		Log.e("firstDay :  "+ firstDay, " lastDay : "+ lastDay);
		//System.out.println(inputInt);
		Integer firstDayInWeek = Integer.valueOf(firstDay);
		Integer lastDayInWeek = Integer.valueOf(lastDay);
		days = getIndexDayOfWeek(firstDayInWeek, lastDayInWeek);
		//int diffDayOfThisWeek = lastDayInWeek - firstDayInWeek;


		monday.setText(monday.getText().toString()+" "+days[0]);
		tuesday.setText(tuesday.getText().toString()+" "+days[1]);
		wednesday.setText(wednesday.getText().toString()+" "+days[2]);
		thursday.setText(thursday.getText().toString()+" "+days[3]);
		friday.setText(friday.getText().toString()+" "+days[4]);
		saturday.setText(saturday.getText().toString()+" "+days[5]);
		sunday.setText(sunday.getText().toString()+" "+days[6]);



		view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

	        @Override
	        public void onGlobalLayout() {
	            // Ensure you call it only once :
	            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	    		fillSpecificDayEvents(view);
	            // Here you can get the size :)
	        }
	    });


		return view;
	}


	public void fillSpecificDayEvents(View view){

		LinearLayout monday_allDayEvent = (LinearLayout)view.findViewById(R.id.monday_allDayEvent);
		LinearLayout tuesday_allDayEvent = (LinearLayout)view.findViewById(R.id.tuesday_allDayEvent);
		LinearLayout wednesday_allDayEvent = (LinearLayout)view.findViewById(R.id.wednesday_allDayEvent);
		LinearLayout thursday_allDayEvent = (LinearLayout)view.findViewById(R.id.thursday_allDayEvent);
		LinearLayout friday_allDayEvent = (LinearLayout)view.findViewById(R.id.friday_allDayEvent);
		LinearLayout saturday_allDayEvent = (LinearLayout)view.findViewById(R.id.saturday_allDayEvent);
		LinearLayout sunday_allDayEvent = (LinearLayout)view.findViewById(R.id.sunday_allDayEvent);

		((View)monday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		((View)tuesday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		((View)wednesday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		((View)thursday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		((View)friday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		((View)saturday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		((View)sunday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color()));

		monday_allDayEvent.removeAllViewsInLayout();
		tuesday_allDayEvent.removeAllViewsInLayout();
		wednesday_allDayEvent.removeAllViewsInLayout();
		thursday_allDayEvent.removeAllViewsInLayout();
		friday_allDayEvent.removeAllViewsInLayout();
		saturday_allDayEvent.removeAllViewsInLayout();
		sunday_allDayEvent.removeAllViewsInLayout();

		if(allDayEvents.size() == 0){
//			((View)monday_allDayEvent.getParent()).setVisibility(View.GONE);
//			((View)tuesday_allDayEvent.getParent()).setVisibility(View.GONE);
//			((View)wednesday_allDayEvent.getParent()).setVisibility(View.GONE);
//			((View)thursday_allDayEvent.getParent()).setVisibility(View.GONE);
//			((View)friday_allDayEvent.getParent()).setVisibility(View.GONE);
//			((View)saturday_allDayEvent.getParent()).setVisibility(View.GONE);
//			((View)sunday_allDayEvent.getParent()).setVisibility(View.GONE);
			
			((View)monday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
			((View)tuesday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
			((View)wednesday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
			((View)thursday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
			((View)friday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
			((View)saturday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
			((View)sunday_allDayEvent.getParent()).setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
		}

		for (int i = 0; i < allDayEvents.size(); i++) {
			LinearLayout container;
			Calendar allDay = getDateEvent(allDayEvents.get(i).getDate());

			if (allDay != null) {
				int dayofweek = allDay.get(Calendar.DAY_OF_WEEK);
				//				if (dayofweek == Calendar.SUNDAY) {
				//					addEventAllDay(sunday_allDayEvent, allDayEvents.get(i));
				//				}else if (dayofweek == Calendar.MONDAY) {
				//					addEventAllDay(monday_allDayEvent, allDayEvents.get(i));
				//				}else if (dayofweek == Calendar.TUESDAY) {
				//					addEventAllDay(tuesday_allDayEvent, allDayEvents.get(i));
				//				}else if (dayofweek == Calendar.WEDNESDAY) {
				//					addEventAllDay(wednesday_allDayEvent, allDayEvents.get(i));					
				//				}else if (dayofweek == Calendar.THURSDAY) {
				//					addEventAllDay(thursday_allDayEvent, allDayEvents.get(i));
				//				}else if (dayofweek == Calendar.FRIDAY) {
				//					addEventAllDay(friday_allDayEvent, allDayEvents.get(i));
				//				}else if (dayofweek == Calendar.SATURDAY) {
				//					saturday_allDayEvent.removeAllViewsInLayout();
				//					addEventAllDay(saturday_allDayEvent, allDayEvents.get(i));
				//				}

				switch (dayofweek) {

				case Calendar.MONDAY:
					container = monday_allDayEvent;
					break;

				case Calendar.TUESDAY:
					container = tuesday_allDayEvent;
					break;

				case Calendar.WEDNESDAY:
					container = wednesday_allDayEvent;
					break;

				case Calendar.THURSDAY:
					container = thursday_allDayEvent;
					break;

				case Calendar.FRIDAY:
					container = friday_allDayEvent;
					break;

				case Calendar.SATURDAY:
					container = saturday_allDayEvent;
					break;

				case Calendar.SUNDAY:
					container = sunday_allDayEvent;
					break;
				default :
					container = new LinearLayout(getActivity());
					break;
				}				
				//container.removeAllViewsInLayout();
				addEventAllDay(container, allDayEvents.get(i));
			}

		}

		RelativeLayout monday_events = (RelativeLayout)view.findViewById(R.id.monday_events);
		RelativeLayout tuesday_events = (RelativeLayout)view.findViewById(R.id.tuesday_events);
		RelativeLayout wednesday_events = (RelativeLayout)view.findViewById(R.id.wednesday_events);
		RelativeLayout thursday_events = (RelativeLayout)view.findViewById(R.id.thursday_events);
		RelativeLayout friday_events = (RelativeLayout)view.findViewById(R.id.friday_events);
		RelativeLayout saturday_events = (RelativeLayout)view.findViewById(R.id.saturday_events);
		RelativeLayout sunday_events = (RelativeLayout)view.findViewById(R.id.sunday_events);

		monday_events.setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
		tuesday_events.setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
		wednesday_events.setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
		thursday_events.setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
		friday_events.setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
		saturday_events.setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));
		sunday_events.setBackgroundColor(colors.getColor(colors.getBackground_color(), "80"));

		Map<Integer, List<List<Event>>> map = getSimultaneousEvents();


		for (int i = 0; i < days.length; i++) {
			RelativeLayout layout;// = new LinearLayout(getActivity()); 
			if(map.containsKey(days[i])){
				switch (i) {

				case 0:
					layout = monday_events;
					break;

				case 1:
					layout = tuesday_events;
					break;

				case 2:
					layout = wednesday_events;
					break;

				case 3:
					layout = thursday_events;
					break;

				case 4:
					layout = friday_events;
					break;

				case 5:
					layout = saturday_events;
					break;

				case 6:
					layout = sunday_events;
					break;

				default:
					layout = new RelativeLayout(getActivity());
					break;
				}


				layout.removeAllViewsInLayout();
				for(int j = 0; j < map.get(days[i]).size(); j++){
					addSpecificEventDay(layout, map.get(days[i]).get(j));
					//layout.requestLayout();
				}
			}
		}

	}


	public int[] getIndexDayOfWeek(int firstDayInWeek, int lastDayInWeek){
		int days[]=new int[7];
		int j = 6;
		int offset = lastDayInWeek - firstDayInWeek;
		while(j >= 0){
			if(offset > 0){
			if(lastDayInWeek-- >= 1)
				days[6 - j] = firstDayInWeek + (6 - j);
			else
				days[6 - j] ++;
			}else{
				
				if(lastDayInWeek >= 1){
					days[j] = lastDayInWeek--;
				}else{
					days[j] = firstDayInWeek + j;
				}
			}
			j--;
		}
		return days;
	}
	
	

	public Map<Integer,List<List<Event>>> getSimultaneousEvents() {
		List<List<Event>> result = new ArrayList<List<Event>>();
		Map<Integer,List<List<Event>>> compactMap = new HashMap<Integer, List<List<Event>>>();
		Map<Integer, List<Event>> map = getMapDayEvents();
		Date date = new Date();
		for (int k = 0; k < days.length; k++) {
			if(map.get(days[k]) != null){
				result = getCollectionOfSimultaneousEvents(map.get(days[k]));
				compactMap.put(days[k], result);
			}
		}
		return compactMap;

	}

	public Map<Integer, List<Event>> getMapDayEvents() {
		//List<List<Event>> result = new ArrayList<List<Event>>();
		Map<Integer, List<Event>> map = new HashMap<Integer, List<Event>>();
		Date date = new Date();

		for (int i = 0; i < preciseEvents.size(); i++) {
			List<Event> intersectEvents = new ArrayList<Event>();
			Event tmpEvent = preciseEvents.get(i);


			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(tmpEvent.getDate());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (Exception e) {
				// TODO: handle exception
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			int month = c.get(Calendar.MONTH); //date.getMonth();
			int day = c.get(Calendar.DAY_OF_MONTH); //date.getDay();
			int year = c.get(Calendar.YEAR);



			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			if(!map.containsKey(day)){

				intersectEvents.add(tmpEvent);
				for (int j = i + 1; j < preciseEvents.size(); j++) {
					Date date_ = null;
					Event event = preciseEvents.get(j);
					try {
						date_ = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(event.getDate());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}catch (Exception e) {
						// TODO: handle exception
					}
					Calendar c_ = Calendar.getInstance();
					c_.setTime(date_);

					int month_ = c_.get(Calendar.MONTH); //date.getMonth();
					int day_ = c_.get(Calendar.DAY_OF_MONTH); //date.getDay();
					int year_ = c_.get(Calendar.YEAR);

					int hour_ = c_.get(Calendar.HOUR_OF_DAY);
					int minute_ = c_.get(Calendar.MINUTE);

					if( year == year_ && month == month_ && day == day_){
						intersectEvents.add(event);
					}

				}
				map.put(day, intersectEvents);
			}

			//result.add(intersectEvents);


		}
		return map;

	}

	private CalendarInfoWindow calendarEventInfos;

	public void addEventAllDay(LinearLayout layout, Event evnt){
		if(getActivity() == null)return;
		//layout.setPadding(5, 10, 5, 10);
		TextView titleEvent = new TextView(getActivity());
		titleEvent.setPadding(5, 5, 5, 5);
		titleEvent.setTextAppearance(getActivity(), style.TextAppearance_Small);
		titleEvent.setTypeface(MainActivity.FONT_BODY);
		titleEvent.setTextSize(13);
		List<AgendaGroup> agendaGroup = null;
        agendaGroup = (List<AgendaGroup>)realm.where(AgendaGroup.class).equalTo("id", evnt.getAgenda_group_id()).findAll();
        //appController.getAgendaGroupDao().queryForEq("id", evnt.getAgenda_group_id());

        if(agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) {

			titleEvent.setBackgroundColor(colors.getColor(agendaGroup.get(0).getColor()));
			titleEvent.setTextColor(colors.getColor(colors.getBackground_color()));
			titleEvent.setText(evnt.getTitle());

			titleEvent.setTag(evnt);
			final String grpcolor = agendaGroup.get(0).getColor();
			titleEvent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Event event = (Event)v.getTag();
					if(event != null) {
						calendarEventInfos = new CalendarInfoWindow(getActivity(), event, colors, grpcolor);
						((TextView)v).setSelected(true);
						getInfoWindowZone(v); 

					}
				}
			});

			LayoutParams params;
			
			if(layout.getChildCount() > 0){
				View v = new View(getActivity());
				v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
				params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
				layout.getChildAt(0).setLayoutParams(params);
				layout.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}else
				params = new LayoutParams(LayoutParams.MATCH_PARENT, ((View)layout.getParent()).getHeight() , 1);
			params.gravity = Gravity.CENTER_VERTICAL;
			params.setMargins(0, 5, 0, 5);
			titleEvent.setLayoutParams(params);
			titleEvent.setGravity(Gravity.CENTER_VERTICAL);
			layout.addView(titleEvent);
		}else if(evnt.getAgenda_group_id() == 0) {

			titleEvent.setBackgroundColor(colors.getColor(colors.getTitle_color()));
			titleEvent.setTextColor(colors.getColor(colors.getBackground_color()));
			//titleEvent.setTextSize(12);
			titleEvent.setText(evnt.getTitle());

			titleEvent.setTag(evnt);
			titleEvent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Event event = (Event)v.getTag();
					if(event != null) {
						calendarEventInfos = new CalendarInfoWindow(getActivity(), event, colors, colors.getTitle_color());
						((TextView)v).setSelected(true);

						getInfoWindowZone(v); 
					}
				}
			});
			LayoutParams params;
			
			if(layout.getChildCount() > 0){
				View v = new View(getActivity());
				v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
				params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
				layout.getChildAt(0).setLayoutParams(params);
				layout.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}else
				params = new LayoutParams(LayoutParams.MATCH_PARENT, ((View)layout.getParent()).getHeight() , 1);

			params.gravity = Gravity.CENTER_VERTICAL;
			params.setMargins(0, 5, 0, 5);
			titleEvent.setLayoutParams(params);
			titleEvent.setGravity(Gravity.CENTER_VERTICAL);
			layout.addView(titleEvent);
		}
		else {
			//titleEvent.setVisibility(View.GONE);
			layout.removeView(titleEvent);
		}
	}
	
	public void addSpecificEventDay(RelativeLayout layout, List<Event> list){

//		int resId = getResources().getIdentifier("status_bar_height",
//				"dimen",
//				"android");
//		int heightElement = 0;
//		if (resId > 0) {
//			heightElement = getResources().getDimensionPixelSize(resId);
//		}

		for(int k = 0; k < list.size(); k++){
			List<AgendaGroup> agendaGroups = null;
            agendaGroups = (List<AgendaGroup>) realm.where(AgendaGroup.class).equalTo("id", list.get(k).getAgenda_group_id()).findAll();
            //appController.getAgendaGroupDao().queryForEq("id", list.get(k).getAgenda_group_id());

            if(agendaGroups != null && agendaGroups.size()>0 && isValidGrp(agendaGroups.get(0))){
				
			}else if(list.get(k).getAgenda_group_id() == 0){
				
			}else{
				list.remove(list.get(k));
				//layout.removeAllViewsInLayout();
			}
		}


		if(list.size() > 0) {

			int specificHeight, duration;
			specificHeight = duration = 0;
			int pixelBlocs = 1400;
			int widthElement, heightElement ;
			
			heightElement = layout.getHeight() - (layout.getPaddingTop() * 2);//size.y - (tmp + 350);
			widthElement = layout.getWidth() -  (layout.getPaddingLeft() * 2);//(size.x - 350) / 7;


			for(int i = 0 ; i < list.size(); i++) {
				List<AgendaGroup> agendaGroup = null;
                agendaGroup = (List<AgendaGroup>) realm.where(AgendaGroup.class).equalTo("id", list.get(i).getAgenda_group_id()).findAll();
                //appController.getAgendaGroupDao().queryForEq("id", list.get(i).getAgenda_group_id());

                if(getActivity() == null)return;
				RelativeLayout container = new RelativeLayout(getActivity());
				container.setId(list.get(i).getId());

				TextView title_event = new TextView(getActivity());
				title_event.setId(ids[i]);//list.get(i).getId());
				TextView start_date = new TextView(getActivity());
				TextView end_date = new TextView(getActivity());

				title_event.setTextSize(11);
				start_date.setTextSize(11);
				end_date.setTextSize(11);
				
				title_event.setTypeface(MainActivity.FONT_BODY);
				start_date.setTypeface(MainActivity.FONT_BODY);
				end_date.setTypeface(MainActivity.FONT_BODY);
				
//				title_event.setTextAppearance(getActivity(), style.TextAppearance_Small);
//				start_date.setTextAppearance(getActivity(), style.TextAppearance_Small);
//				end_date.setTextAppearance(getActivity(), style.TextAppearance_Small);

				Date date = new Date();
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(list.get(i).getDate());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}catch (Exception e) {
					// TODO: handle exception
				}
				Calendar c = Calendar.getInstance();
				c.setTime(date);

				//				int month = c.get(Calendar.MONTH); //date.getMonth();
				//				int day = c.get(Calendar.DAY_OF_MONTH); //date.getDay();
				//				int year = c.get(Calendar.YEAR);
				//
				//				int hour = c.get(Calendar.HOUR_OF_DAY);
				String start_hour = ""+c.get(Calendar.HOUR_OF_DAY);
				String start_minutes = ""+c.get(Calendar.MINUTE);
				if (start_hour.length() == 1) {
					start_hour = "0"+start_hour;
				}
				if (start_minutes.length() == 1) {
					start_minutes = "0"+start_minutes;
				}
				
				//specificHeight = c.get(Calendar.HOUR_OF_DAY);
				specificHeight = Integer.parseInt(start_hour+start_minutes);
				
				String[] s = list.get(i).getDuration().split("H");
				String duration_hour, duration_minutes;
				int h, min;
				if(s!= null && s.length > 1){
//					h = Integer.parseInt(s[0]);
//					min = Integer.parseInt(s[1]);
					duration_hour = s[0];
					duration_minutes = s[1];
					if (duration_hour.length() == 1) {
						duration_hour = "0"+duration_hour;
					}
					if (duration_minutes.length() == 1) {
						duration_minutes = "0"+duration_minutes;
					}
					h = Integer.parseInt(duration_hour);
					min = Integer.parseInt(duration_minutes);
					//specificHight = c.get(Calendar.HOUR_OF_DAY);
				}
				else
				{
					h = 0;
					min = 0;
					duration_hour =  "0";
					duration_minutes = "0";
				}

				c.add(Calendar.HOUR_OF_DAY, h);
				c.add(Calendar.MINUTE, min);

				String end_hour = ""+c.get(Calendar.HOUR_OF_DAY);
				String end_minutes = ""+c.get(Calendar.MINUTE);
				if (end_hour.length() == 1) {
					end_hour = "0"+end_hour;
				}
				if (end_minutes.length() == 1) {
					end_minutes = "0"+end_minutes;
				}
				
				duration = Integer.parseInt(end_hour + end_minutes) - specificHeight;
				//duration = Integer.parseInt(duration_hour+duration_minutes);

				container.setPadding(5, 5, 5, 5);
				layout.addView(container);



				/** Uness Test Container Color **/
				if(agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) {
					//view.setBackgroundColor(colors.getBackMixColor(agendaGroup.get(0).getColor(), 0.20f));
					container.setBackgroundColor(colors.getBackMixColor(agendaGroup.get(0).getColor(), 0.20f));
					title_event.setTextColor(colors.getColor(agendaGroup.get(0).getColor()));
					start_date.setTextColor(colors.getColor(agendaGroup.get(0).getColor()));
					end_date.setTextColor(colors.getColor(agendaGroup.get(0).getColor()));

					if (list.size() == 1 ) {
						start_date.setText(start_hour+":"+start_minutes);
						title_event.setText(list.get(i).getTitle());
						end_date.setText(end_hour+":"+end_minutes);
						container.setLayoutParams(new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ((heightElement * duration) / pixelBlocs)));
						container.addView(start_date);
						container.addView(title_event);
						container.addView(end_date);

						((RelativeLayout.LayoutParams)start_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
						((RelativeLayout.LayoutParams)title_event.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
						((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

						container.setY((heightElement * (specificHeight - 800)) / pixelBlocs);


					}else if (list.size() == 2) {


						start_date.setText(start_hour+":"+start_minutes);
						title_event.setText(list.get(i).getTitle());
						end_date.setText(end_hour+":"+end_minutes);
						container.setLayoutParams(new  RelativeLayout.LayoutParams(widthElement / 2, ((heightElement * duration) / pixelBlocs)));
						container.addView(start_date);
						container.addView(title_event);
						container.addView(end_date);

						((RelativeLayout.LayoutParams)start_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
						((RelativeLayout.LayoutParams)title_event.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
						((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						
						if(i > 0){
							((RelativeLayout.LayoutParams)container.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, list.get(i - 1).getId());
						}
						container.setY((heightElement * (specificHeight - 800)) / pixelBlocs);

					}else if (list.size() == 3) {


						start_date.setText(start_hour+":"+start_minutes);

						MyImageView img = new MyImageView(getActivity());
						img.setId(imgIds[i]);//list.get(i).getId());
						container.setTag(imgIds[i], img);
						StateListDrawable drawable = new StateListDrawable();
						drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
						drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
						drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
						drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
						drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
						drawable.addState(new int[]{}, new ColorDrawable(colors.getBackMixColor(agendaGroup.get(0).getColor(), 0.20f)));
						img.setBackgroundDrawable(drawable);						
						
						img.setDiffOfColorCode(colors.getColor(agendaGroup.get(0).getColor()), colors.getColor(colors.getBackground_color()));
						
						end_date.setText(end_hour+":"+end_minutes);
						container.setLayoutParams(new  RelativeLayout.LayoutParams(widthElement / 3, ((heightElement * duration) / pixelBlocs)));

						container.addView(start_date);
						container.addView(img, 22, 22);
						container.addView(end_date);


						((RelativeLayout.LayoutParams)start_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
						((RelativeLayout.LayoutParams)img.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
						((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						//((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.BELOW, (i + 1));


						if(i > 0){
							((RelativeLayout.LayoutParams)container.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, list.get(i - 1).getId());
						}
						//container.setY((layout.getLayoutParams().height * h) / 16);
						container.setY((heightElement * (specificHeight - 800)) / pixelBlocs);

					}

					start_date.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
							new int[]{colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()),  
							colors.getColor(agendaGroup.get(0).getColor())}));

					title_event.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
							new int[]{colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()),  
							colors.getColor(agendaGroup.get(0).getColor())}));

					end_date.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
							new int[]{colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()),  
							colors.getColor(agendaGroup.get(0).getColor())}));

					StateListDrawable drawable = new StateListDrawable();
					drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
					drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
					drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
					drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
					drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
					drawable.addState(new int[]{}, new ColorDrawable(colors.getBackMixColor(agendaGroup.get(0).getColor(), 0.20f)));
					container.setBackgroundDrawable(drawable);

					//					StateListDrawable drawable = new StateListDrawable();
					//					drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					//					drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					//					drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					//					drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					//					drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					//					drawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
					if(list.size() < 3)
						container.setTag(ids[i], title_event);
					container.setTag(list.get(i));
					final String grpcolor = agendaGroup.get(0).getColor();
					container.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Event event = (Event)v.getTag();
							if(event != null) {
								calendarEventInfos = new CalendarInfoWindow(getActivity(), event, colors, (grpcolor != null) ? grpcolor : colors.getTitle_color());
								((RelativeLayout)v).setSelected(true);

								for(int i = 0; i < ids.length; i++){
									View view = ((View)v.getTag(ids[i]) == null) ? (View)v.getTag(imgIds[i]) : (View)v.getTag(ids[i]) ;
									if(view != null){
											getInfoWindowZone(view, v); 
									}
								}
								
								

							}
						}
					});



				}else if(list.get(i).getAgenda_group_id() == 0) {
					//view.setBackgroundColor(colors.getBackMixColor(colors.getTitle_color(), 0.20f));
					container.setBackgroundColor(colors.getBackMixColor(colors.getTitle_color(), 0.20f));
					title_event.setTextColor(colors.getColor(colors.getTitle_color()));
					start_date.setTextColor(colors.getColor(colors.getTitle_color()));
					end_date.setTextColor(colors.getColor(colors.getTitle_color()));



					if (list.size() == 1 ) {
						//					Drawable drawableTest1 = getColoredDrawable(evnts, i, agendaGroup);
						//					ImageView img = new ImageView(getActivity());
						//					LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
						//					paramsPic.gravity = Gravity.CENTER;
						//					img.setLayoutParams(paramsPic);
						//					img.setImageDrawable(drawableTest1);

						start_date.setText(start_hour+":"+start_minutes);
						title_event.setText(list.get(i).getTitle());
						end_date.setText(end_hour+":"+end_minutes);
						//container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (layout.getLayoutParams().height * specificHight) / 16));
						//container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100));
						//container.setLayoutParams(new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ((height * Integer.parseInt(duration_hour+duration_minutes)) / pixelBlocs)));
						container.setLayoutParams(new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ((heightElement * duration) / pixelBlocs)));
						
						container.addView(start_date);
						container.addView(title_event);
						container.addView(end_date);

						((RelativeLayout.LayoutParams)start_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
						((RelativeLayout.LayoutParams)title_event.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
						((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						//((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.BELOW, (i + 1));

//						RelativeLayout rel = (RelativeLayout) layout.getChildAt((layout.getChildCount() > 0) ? layout.getChildCount() - 1 : 0);
//						if(rel != null)
//							((RelativeLayout.LayoutParams)container.getLayoutParams()).addRule(RelativeLayout.BELOW, rel.getId());
//						else
						container.setY((heightElement * (specificHeight - 800)) / pixelBlocs);

						//start_date.ad

					}else if (list.size() == 2) {


						start_date.setText(start_hour+":"+start_minutes);
						title_event.setText(list.get(i).getTitle());
						end_date.setText(end_hour+":"+end_minutes);
						container.setLayoutParams(new  RelativeLayout.LayoutParams(widthElement / 2, ((heightElement * duration) / pixelBlocs)));
						container.addView(start_date);
						container.addView(title_event);
						container.addView(end_date);

						((RelativeLayout.LayoutParams)start_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
						((RelativeLayout.LayoutParams)title_event.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
						((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);




						if(i > 0){
							((RelativeLayout.LayoutParams)container.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, list.get(i - 1).getId());
						}
						//container.setY((layout.getLayoutParams().height * h) / 16);
						container.setY((heightElement * (specificHeight - 800)) / pixelBlocs);

					}else if (list.size() == 3) {


						start_date.setText(start_hour+":"+start_minutes);

						//Drawable drawableTest3 = getColoredDrawable(evnts, i, colors.getTitle_color());
						MyImageView img = new MyImageView(getActivity());
						img.setId(ids[i]);
						container.setTag(ids[i], img);
						//img.setImageDrawable(drawableTest3);
						StateListDrawable drawable = new StateListDrawable();
						drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
						drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
						drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
						drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
						drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
						drawable.addState(new int[]{}, new ColorDrawable(colors.getBackMixColor(colors.getTitle_color(), 0.20f)));
						img.setBackgroundDrawable(drawable);						
						
						img.setDiffOfColorCode(colors.getColor(colors.getTitle_color()), colors.getColor(colors.getBackground_color()));
						end_date.setText(end_hour+":"+end_minutes);
						//container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (layout.getLayoutParams().height * specificHight) / 16));
						//container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 100));
						container.setLayoutParams(new  RelativeLayout.LayoutParams(widthElement / 3, ((heightElement * duration) / pixelBlocs)));
						container.addView(start_date);
						container.addView(img, 22, 22);
						container.addView(end_date);
						((RelativeLayout.LayoutParams)start_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
						((RelativeLayout.LayoutParams)img.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
						((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						//((RelativeLayout.LayoutParams)end_date.getLayoutParams()).addRule(RelativeLayout.BELOW, (i + 1));

						if(i > 0){
							((RelativeLayout.LayoutParams)container.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, list.get(i - 1).getId());
						}
						//container.setY((layout.getLayoutParams().height * h) / 16);
						container.setY((heightElement * (specificHeight - 800)) / pixelBlocs);

					}

					start_date.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
							new int[]{colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()),  
							colors.getColor(colors.getTitle_color())}));

					title_event.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
							new int[]{colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()),  
							colors.getColor(colors.getTitle_color())}));

					end_date.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
							new int[]{colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()), 
							colors.getColor(colors.getBackground_color()),  
							colors.getColor(colors.getTitle_color())}));


					StateListDrawable drawable = new StateListDrawable();
					drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
					drawable.addState(new int[]{}, new ColorDrawable(colors.getBackMixColor(colors.getTitle_color(), 0.20f)));
					container.setBackgroundDrawable(drawable);

					if(list.size() < 3)
						container.setTag(ids[i], title_event);
					container.setTag(list.get(i));
					container.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Event event = (Event)v.getTag();
							if(event != null) {
								calendarEventInfos = new CalendarInfoWindow(getActivity(), event, colors, colors.getTitle_color());//(grpcolor != null) ? grpcolor : colors.getTitle_color());
								((RelativeLayout)v).setSelected(true);
								for(int i = 0; i < ids.length; i++){
									View view = ((View)v.getTag(ids[i]) == null) ? (View)v.getTag(imgIds[i]) : (View)v.getTag(ids[i]) ;
									if(view != null){
											getInfoWindowZone(view, v); 
									}
								}

								//getInfoWindowZone((View)v.getTag(event.getId())); 

							}
						}
					});

				}else{
					
					//container.setVisibility(View.GONE);
					layout.removeView(container);
				}

				/** **/

				Log.e(" event date : "+list.get(i).getDate() +" specifiqueHeight (begin event) : "+specificHeight+"  heightElement  :  "+heightElement, " index : "+i+"    <==> duration : "+Integer.parseInt(duration_hour+duration_minutes)+"  <==> height : "+((heightElement * Integer.parseInt(duration_hour+duration_minutes)) / pixelBlocs)+ "  <==> YPosition : "+((heightElement * (specificHeight - 800)) / pixelBlocs));
				//Log.i(" event date : "+evnts.get(i).getDate(), " index : "+i+"    <==> duration : "+Integer.parseInt(duration_hour+duration_minutes)+"  <==> height : "+((int)(heightElement * Integer.parseInt(duration_hour+duration_minutes)) / pixelBlocs)+ "  <==> YPosition : "+((int)(heightElement * (specificHeight - 800)) / pixelBlocs));

			}

			//layout.addView(specificContainer);

		}

	}


//	/**
//	 * @param evnts
//	 * @param i
//	 * @param agendaGroup 
//	 * @param agendaGroup
//	 * @return
//	 */
//	private Drawable getColoredDrawable(List<Event> evnts, int i, String color){//List<AgendaGroup> agendaGroup) {
//		
//		ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
//		biggerCircle.setIntrinsicHeight( 20 );
//		biggerCircle.setIntrinsicWidth( 20);
//		biggerCircle.setBounds(new Rect(0, 0, 20, 20));
//
//		biggerCircle.getPaint().setColor(colors.getColor(color));
//		return biggerCircle;
//	}
	
	class MyImageView extends ImageView{

		private int colorCode1;
		private int colorCode2;
		private Paint paint;
		private int color = 0x0000FF;

		public MyImageView(Context context) {
			super(context);
			paint = new Paint();
			paint.setColor(Color.BLACK);
			// TODO Auto-generated constructor stub
		}
		
		public void setDiffOfColorCode(int colorCode1, int colorCode2){

			this.colorCode1 = colorCode1;
			this.colorCode2 = colorCode2;
		}

		/* (non-Javadoc)
		 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
		 */
		@Override
		protected void onDraw(Canvas canvas) {
			Rect rect = canvas.getClipBounds();
			Log.i(" bounds of arrow", rect.left+" bottom "+rect.bottom+" top "+rect.top + " right "+rect.right);
			
			
			if (paint == null) {
				paint = new Paint();
				paint.setColor(color);
			} 

			float radius = 0;
			if (rect.right<rect.bottom) {
				radius= rect.right/2;
			}else {
				radius= rect.bottom/2;
			}

			if(this.getBackground() != null){

			ColorDrawable colorD = (ColorDrawable) getBackground().getCurrent();
			if(colorD.getColor() == colorCode1){
				paint.setColor(colorCode2); 
			}else {
				paint.setColor(colorCode1); 
			}
			
			}
			paint.setAntiAlias(true);
			paint.setDither(true);
			
			canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(), radius, paint); 
			//canvas.drawPaint(paint);
			super.onDraw(canvas);
		}
		
		
		
	}

	private List<Event> allDayEvents;
	private ArrayList<Event> preciseEvents;

	public void getTypeOfEvents() {
		allDayEvents = new ArrayList<Event>();
		preciseEvents = new ArrayList<Event>();
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getDuration() == null || events.get(i).getDuration().isEmpty()) {
				allDayEvents.add(events.get(i));
			}else {
				preciseEvents.add(events.get(i));
			}
		}

	}




	public Calendar getDateEvent(String str) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str));
			return calendar;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //"2013-10-23 00:00"
		return null;

	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//		grid.setOnItemClickListener(new OnItemClickListener() {
		//
		//			@Override
		//			public void onItemClick(AdapterView<?> arg0, View view, int position,
		//					long arg3) {
		//				
		//				Toast.makeText(getActivity(), ((TextView)adapter.getItem(position)).getText().toString(), 1000).show();
		//			}
		//		});
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onScrollChanged(ObservableScrollView observableScrollView,
			int x, int y, int oldx, int oldy) {
		//		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		//        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


	}

	public void setListEvent(List<Event> events) {
		this.events = events;
	}

	public void updateAdapter(List<AgendaGroup> agendaGrps) {
		this.agendaGrps = agendaGrps;
		fillSpecificDayEvents(view);

		//		adapter.updateCalendarGridView(agendaGrps);
		//		adapter.notifyDataSetChanged();
		//		adapter.notifyDataSetInvalidated();

	}



	private boolean isValidGrp(AgendaGroup agendaGrp) {

		if(agendaGrps == null)return true;

		for(int i = 0; i < agendaGrps.size(); i++) {
			if(agendaGrps.get(i).isSelected() && agendaGrps.get(i).getId() == agendaGrp.getId())
				return true;
		}
		return false;
	}		

	private List<Event> getDayEvent(int day) {
		List<Event> evnts = new ArrayList<Event>();
		for(int i = 0; i < events.size(); i++) {
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(events.get(i).getDate());
				if( day == getDayOfMonth(date))
					evnts.add(events.get(i));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return evnts;
	}

	public int getFirstDay(Date d) throws Exception  
	{  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(d);  
		calendar.set(Calendar.DAY_OF_MONTH, 1);  

		Log.e("DAY_OF_WEEK month:       ","" + calendar.get(Calendar.DAY_OF_WEEK));
		//        Date dddd = calendar.getTime(); 
		//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");  
		//        return sdf1.format(dddd);  
		return calendar.get(Calendar.DAY_OF_WEEK);
	}  

	public int getLastDay(Date d) throws Exception  
	{  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(d);  
		//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
		//        Date dddd = calendar.getTime();  
		//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");  
		//        return sdf1.format(dddd);  
		return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}  

	public String getDate(Date d)  
	{  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");  
		return sdf.format(d);  
	}  

	public int getLastDayOfMonth(Date date)  
	{  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);  
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
	}


	public int getDayOfMonth(Date d) {
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(d);  
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public int getIndexByTodayDate(){
		Calendar calendar = Calendar.getInstance();	
		if(month == calendar.get(Calendar.MONTH))
			return calendar.get(Calendar.DAY_OF_MONTH) + firstDayOfMonth - 2;
		return -1;
	}

	/**
	 * @return the theWeek
	 */
	public String getTheWeek() {
		return theWeek;
	}

	/**
	 * @param theWeek the theWeek to set
	 */
	public void setTheWeek(String theWeek) {
		this.theWeek = theWeek;
	}


	public void getInfoWindowZone(final View... v) {

		
		//		Point position = new Point((int)v.getX(), (int)v.getY());

		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int zone = 0;
		//
		int width = display.getWidth();  
		int height = display.getHeight(); 

		int[] location = new int[2];
		v[0].getLocationOnScreen(location);
		float x = location[0] ;//v.getX() + CalendarMonthFragment.gridItemX;//itemPos.x;
		float y = location[1] ;//v.getY() + CalendarMonthFragment.gridItemY;//itemPos.y;

		calendarEventInfos.setDimensionByGridLocation();


		if((x > calendarEventInfos.getWidth()/2) && ((x + calendarEventInfos.getWidth()/2) < width))
		{
			if(y > (calendarEventInfos.getHeight()) && (y + calendarEventInfos.getHeight()) > height) {

				zone = 270;

			}else if(y < calendarEventInfos.getHeight() && (y + calendarEventInfos.getHeight()) < height) {

				zone = 90;

			}
		}

		if((x > calendarEventInfos.getWidth()) /*&& (position.x + mLocationInfos.getWidth()) >= width - 50*/) 
		{
			if((y > (calendarEventInfos.getHeight()/2)) && (height > (y + ( calendarEventInfos.getHeight()/2 )))) {
				zone = 0;
			}			
		}
		else{
			zone = 180;
		}



		calendarEventInfos.setViewByGridLocation(zone);


		calendarEventInfos.show(v[0], zone, height);

		calendarEventInfos.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if(v.length > 1 && v[1] instanceof RelativeLayout)
					((RelativeLayout)v[1]).setSelected(false);
				else
					((TextView)v[0]).setSelected(false);
			}
		});
	}

	public int getMonthNumber(String month){

		if (month.equalsIgnoreCase("January")) {
			return 0;
		}else if (month.equalsIgnoreCase("February")) {
			return 1;
		}else if (month.equalsIgnoreCase("March")) {
			return 2;
		}else if (month.equalsIgnoreCase("April")) {
			return 3;
		}else if (month.equalsIgnoreCase("May")) {
			return 4;
		}else if (month.equalsIgnoreCase("June")) {
			return 5;
		}else if (month.equalsIgnoreCase("July")) {
			return 6;
		}else if (month.equalsIgnoreCase("August")) {
			return 7;
		}else if (month.equalsIgnoreCase("September")) {
			return 8;
		}else if (month.equalsIgnoreCase("October")) {
			return 9;
		}else if (month.equalsIgnoreCase("November")) {
			return 10;
		}else if (month.equalsIgnoreCase("December")) {
			return 11;
		}else if (month.equalsIgnoreCase("Janvier")) {
			return 0;
		}else if (month.equalsIgnoreCase("Février")) {
			return 1;
		}else if (month.equalsIgnoreCase("Mars")) {
			return 2;
		}else if (month.equalsIgnoreCase("Avril")) {
			return 3;
		}else if (month.equalsIgnoreCase("Mai")) {
			return 4;
		}else if (month.equalsIgnoreCase("Juin")) {
			return 5;
		}else if (month.equalsIgnoreCase("Juillet")) {
			return 6;
		}else if (month.equalsIgnoreCase("Août")) {
			return 7;
		}else if (month.equalsIgnoreCase("Septembre")) {
			return 8;
		}else if (month.equalsIgnoreCase("Octobre")) {
			return 9;
		}else if (month.equalsIgnoreCase("Novembre")) {
			return 10;
		}else if (month.equalsIgnoreCase("Décembre")) {
			return 11;
		}

		return 0;

	}

	public List<List<Event>> getCollectionOfSimultaneousEvents(List<Event> events){
		HashMap<String,  List<Event>> map = new HashMap<String, List<Event>>();
		List<List<Event>> result = new ArrayList<List<Event>>();


		String tmp, tmp2 = null, comparTag = "", dateTag = "";
		int j,som = 0;

		Event tmpEvent = null;

		j = events.size() - 1;



		int year = 0;
		int month = 0;
		int day = 0;
		
		int comparHour = 0;
		int comparMinute = 0;
		int comparDuration = 0;

		List<String> list = new ArrayList<String>();

		List<Event> listEvent = null; // = new ArrayList<Event>();
		Calendar c = new GregorianCalendar();
		boolean end = false;
		boolean changeTag = false;
		j = 0;
		int k = 0;
		while(!end) {
			if(k >= events.size())break;
			listEvent = new ArrayList<Event>();

			for(int i = j ; i<events.size(); i++) {
				tmpEvent = events.get(i);

				try {

					Date date = new SimpleDateFormat("yyyy-MM-dd HH:MM").parse(tmpEvent.getDate());
					c.setTime(date);
					
					if(!changeTag)
						dateTag = ""+c.get(Calendar.DAY_OF_MONTH)+"";
					else{
						dateTag = ""+k+""+c.get(Calendar.DAY_OF_MONTH)+"";
						changeTag = false;
					}
					
					month = c.get(Calendar.MONTH); 
					day = c.get(Calendar.DAY_OF_MONTH); 
					year = c.get(Calendar.YEAR);



					int hour = c.get(Calendar.HOUR_OF_DAY);
					int minute = c.get(Calendar.MINUTE);
					int duration = Integer.parseInt(tmpEvent.getDuration().isEmpty() ? "0" : tmpEvent.getDuration().split("H")[0]);

					if(!dateTag.isEmpty() && !isAlreadyChecked(list, dateTag)) {
						//if(anotherDate) break;
						//					if(isLooperFinished) {
						list.add(dateTag);
						listEvent.add(tmpEvent);
						comparTag = dateTag; //list.get(j);
						year = c.get(Calendar.YEAR); //date.getYear();
						month = c.get(Calendar.MONTH); //date.getMonth();
						day = c.get(Calendar.DAY_OF_MONTH) + 1; //date.getDay();
						
						comparHour = hour;
						comparMinute = minute;
						comparDuration = duration;
						//					}
						//anotherDate = true;
						//isLooperFinished = false;
						j++;
					}else if(comparTag.compareTo(dateTag) == 0){
							if(hour >= comparHour && hour <= (comparHour + comparDuration)){
								listEvent.add(tmpEvent);
								j++;
							}else{
							
								changeTag = true;
							}
						//listEvent.add(tmpEvent);
						//j++;
					}



				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//anotherDate = false;

//			if(!result.contains(listEvent)){
//				result.add(listEvent);
//			}
				
            tmp = dateTag;//+""+c.get(Calendar.DAY_OF_MONTH)+""+(c.get(Calendar.MONTH) + 1)+c.get(Calendar.YEAR)+"";
//
			if(!map.keySet().contains(tmp)) {
				map.put(tmp, listEvent);
				result.add(listEvent);
			}


			som += listEvent.size();

			if(som >= events.size()) {
				end = true;
			}
			
			k++;

		}

		return result;

	}
	
	public boolean isAlreadyChecked(List<String> list, String value) {
		if(list.size() == 0)return false;

		for(int i = 0; i< list.size(); i++) {
			if(list.get(i).compareTo(value) == 0)
				return true;
		}

		return false;
	}

	/**
	 * @return the agendaGrps
	 */
	public List<AgendaGroup> getAgendaGrps() {
		return agendaGrps;
	}

	/**
	 * @param agendaGrps the agendaGrps to set
	 */
	public void setAgendaGrps(List<AgendaGroup> agendaGrps) {
		this.agendaGrps = agendaGrps;
	}



}
