package com.euphor.paperpad.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.euphor.paperpad.CalendarInfoWindow;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.WebViewFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.AgendaGroup;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Event;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.actionsPrices.QuickAction.OnDismissListener;

import android.R.style;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import io.realm.Realm;

/**
 * @author euphordev04
 *
 */
public class CalendarEventAdapter extends BaseAdapter{


	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}


	private Context context;
	private Colors colors;
	private List<Event> events;
	private int firstDayOfMonth, month, numberOfDayInMonth, count;
	private List<AgendaGroup> agendaGrps;
	private CalendarInfoWindow calendarEventInfos;
	private boolean isTablet;
	private MainActivity activity;
	protected PopupWindow pw;
    private Realm realm;

	//	interface updateMonthView{
	//		public void updateMonth();
	//	};


	public CalendarEventAdapter(MainActivity activity, List<Event> events, List<AgendaGroup> agendaGroups, Date dateOfMonth, Colors colors, Realm realm) {
		

		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.colors = colors;
		this.events = events;

		this.agendaGrps = agendaGroups;
		
		Calendar calendar = Calendar.getInstance();
        if(dateOfMonth!=null)
		calendar.setTime(dateOfMonth);
		month = calendar.get(Calendar.MONTH);
		
		this.numberOfDayInMonth = getLastDayOfMonth(dateOfMonth);
		try {
			this.firstDayOfMonth = getFirstDay(dateOfMonth) - 1;
			if(firstDayOfMonth == 0)
				this.firstDayOfMonth = 7;
			//this.lastDayOfMonth = getLastDay(dateOfMonth) - 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        isTablet = Utils.isTablet(activity);
		this.count = numberOfDayInMonth + firstDayOfMonth - 1;
		
		

		//		if(firstDayOfMonth > 5 && firstDayOfMonth < 7) {
		//			if(numberOfDayInMonth > 30)
		//				lastDayOfMonth += 35;
		//			else
		//				lastDayOfMonth += 28;
		//		}else if(numberOfDayInMonth > 28) {		
		//			lastDayOfMonth += 28;
		//		}else{
		//			lastDayOfMonth += 21;
		//		}

		//this.calendarEventInfos = new CalendarInfoWindow(context, events.get(0), colors);
	}

	public void updateCalendarGridView(List<AgendaGroup> agendaGroups) {
		this.agendaGrps = agendaGroups;
		//this.notifyDataSetChanged();
	}

	private boolean isValidGrp(AgendaGroup agendaGrp) {

		if(agendaGrps == null)return true;

		for(int i = 0; i < agendaGrps.size(); i++) {
			if(agendaGrps.get(i).isSelected() && agendaGrps.get(i).getId() == agendaGrp.getId())
				return true;
		}
		return false;
	}

	@Override
	public int getCount() {
		return count;//42;
	}


	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//LinearLayout view = (LinearLayout) convertView;
		TextView eventDay;


		//		if(convertView == null) {
		//		}
		if (isTablet) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grid_week_item, parent,false);

			if(position >= firstDayOfMonth - 1  /*&& position <= lastDayOfMonth - 1*/) {

				convertView.setBackgroundColor(colors.getColor(colors.getTitle_color(), "20"));
				eventDay = (TextView)convertView.findViewById(R.id.dayNumber);
				eventDay.setText(""+(position - firstDayOfMonth + 2));
				eventDay.setTextColor(colors.getColor(colors.getTitle_color()));

				List<Event> evnts = getDayEvent(position - firstDayOfMonth + 2);

				if(evnts.size() > 0) {
					convertView.setBackgroundColor(colors.getColor(colors.getBackground_color()));
					for(int i = 0 ; i < evnts.size(); i++) {
						TextView titleEvent = new TextView(context);
						titleEvent.setPadding(10, 0, 0, 0);
						titleEvent.setTextAppearance(context, style.TextAppearance_Small);
						//titleEvent.setTextSize(11);
						titleEvent.setTypeface(MainActivity.FONT_BODY);
						List<AgendaGroup> agendaGroup = null;
                        agendaGroup = (List<AgendaGroup>) activity.realm.where(AgendaGroup.class).equalTo("id", evnts.get(i).getAgenda_group_id()).findAll();
                        // appController.getAgendaGroupDao().queryForEq("id", evnts.get(i).getAgenda_group_id());

                        if(agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) {
							titleEvent.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
									new int[]{colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(agendaGroup.get(0).getColor())}));

							StateListDrawable drawable = new StateListDrawable();
							drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{}, new ColorDrawable(Color.RED /*it was transparent here*/));
							titleEvent.setBackgroundDrawable(drawable);

							//titleEvent.setTextSize(12);
							titleEvent.setText(evnts.get(i).getTitle());

							titleEvent.setTag(evnts.get(i));
							final String grpcolor = agendaGroup.get(0).getColor();
							titleEvent.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Event event = (Event)v.getTag();
									if(event != null) {
										//Toast.makeText(context, " Event Title : "+event.getTitle(), 1000).show();
										calendarEventInfos = new CalendarInfoWindow(activity, event, colors, grpcolor);
										((TextView)v).setSelected(true);
										//((TextView)v).setActivated(true);
										getInfoWindowZone(v); 

										//getInfoWindowZone(new Point((int)v.getPivotX(), (int)v.getPivotY()), 180); 
									}
									//								else
									//									Toast.makeText(context, " Event is : "+event, 1000).show();
								}
							});

							((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(titleEvent, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						}else if(evnts.get(i).getAgenda_group_id() == 0) {
							titleEvent.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
									new int[]{colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(colors.getTitle_color())}));

							StateListDrawable drawable = new StateListDrawable();
							drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{}, new ColorDrawable(Color.RED /*TRANSPARENT*/));
							titleEvent.setBackgroundDrawable(drawable);

							//titleEvent.setTextSize(12);
							titleEvent.setText(evnts.get(i).getTitle());

							titleEvent.setTag(evnts.get(i));
							titleEvent.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Event event = (Event)v.getTag();
									if(event != null) {
										//Toast.makeText(context, " Event Title : "+event.getTitle(), 1000).show();
										calendarEventInfos = new CalendarInfoWindow(activity, event, colors, colors.getTitle_color());
										((TextView)v).setSelected(true);
										//((TextView)v).setActivated(true);
										getInfoWindowZone(v); 
									}
									//else
									//Toast.makeText(context, " Event is : "+event, 1000).show();
								}
							});

							((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(titleEvent, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						}
						else {
							((LinearLayout)convertView.findViewById(R.id.eventContainer)).removeView(titleEvent);
						}

					}

				}

				if(position == getIndexByTodayDate()){

					ShapeDrawable border = new ShapeDrawable(new RectShape());
					border.getPaint().setStyle(Style.STROKE);
					border.getPaint().setStrokeWidth(5);
					border.getPaint().setColor(colors.getColor(colors.getTitle_color(), "AA"));
					convertView.setBackgroundDrawable(border);
		    		    }
			        }
		         }else {
			
			

			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grid_week_item_smartphone, parent,false);

			if(position >= firstDayOfMonth - 1  /*&& position <= lastDayOfMonth - 1*/) {
				
				
				convertView.setBackgroundColor(colors.getColor(colors.getTitle_color(), "20"));
				eventDay = (TextView)convertView.findViewById(R.id.dayNumber);
				eventDay.setText(""+(position - firstDayOfMonth + 2));
				eventDay.setTextColor(colors.getColor(colors.getTitle_color()));

				final List<Event> evnts = getDayEvent(position - firstDayOfMonth + 2);
				
				if(evnts.size() > 0) {
					convertView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							LayoutInflater inflater =  activity.getLayoutInflater();
							final View layout = inflater.inflate(R.layout.event_bottom_popup, null);
							
							final ListView listView = (ListView)layout.findViewById(R.id.listEvents);
							SmartphoneGridEventAdapter adapter = new SmartphoneGridEventAdapter(activity, evnts, colors, R.id.listEvents, 15);
							listView.setAdapter(adapter);
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									Event event = (Event) listView.getItemAtPosition(position);
									if (!event.getLink().isEmpty()) {
										WebViewFragment  webViewFragment= new WebViewFragment();
										activity.extras = new Bundle();
										activity.extras.putString("link", event.getLink());
										webViewFragment.setArguments(activity.extras);
										activity.bodyFragment = "WebViewFragment";
										activity.getSupportFragmentManager().beginTransaction()
										.replace(R.id.fragment_container, webViewFragment).addToBackStack("WebViewFragment").commit();
										if (activity.timer != null) {activity.timer.cancel();}
										activity.findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
										activity.findViewById(R.id.swipe_container).setVisibility(View.GONE);
										pw.dismiss();
									} else if (event.getLink_page_identifier() != 0) {

										realm = Realm.getInstance(activity);
                                        List<Child_pages> pages = realm.where(Child_pages.class).equalTo("id", event.getLink_page_identifier()).findAll();
                                        //appController.getChildPageDao().queryForEq("id", event.getLink_page_identifier());
                                        if (pages.size()>0) {
                                            activity.openPage(pages.get(0)); /**/
                                            pw.dismiss();
                                        }
                                    }
									
									
								}
							});
							layout.findViewById(R.id.TitleEventLLayout).setBackgroundColor(colors.getColor(colors.getForeground_color(), "40"));
							TextView titleCart = (TextView)layout.findViewById(R.id.TitleEvent_sp);
							SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");  
							Date d = new Date();
							titleCart.setText(sdf.format(d));
							titleCart.setTextColor(colors.getColor(colors.getTitle_color()));
							View empty = layout.findViewById(R.id.emptySpace);
							empty.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									pw.dismiss();
									
								}
							});
							empty.setOnTouchListener(new OnTouchListener() {
								
								@Override
								public boolean onTouch(View v, MotionEvent event) {
									pw.dismiss();
									return false;
								}
							});
							View dummySpace = layout.findViewById(R.id.dummySpace);
							dummySpace.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									pw.dismiss();
									
								}
							});
							dummySpace.setOnTouchListener(new OnTouchListener() {
								
								@Override
								public boolean onTouch(View v, MotionEvent event) {
									pw.dismiss();
									return false;
								}
							});
							pw = new PopupWindow(layout , LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
							pw.setOutsideTouchable(true);
							pw.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
							pw.setFocusable(true);
							pw.setTouchInterceptor(new OnTouchListener() {
								@Override
								public boolean onTouch(View v, MotionEvent event) {
									if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
										pw.dismiss();
										

										return true;
									}
									return false;
								}
							});
							pw.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
								
								@Override
								public void onDismiss() {
									
//									empty.getForeground().setAlpha( 0); // restore

									
								}
							});

							pw.showAtLocation(activity.findViewById(R.id.fragment_container), Gravity.BOTTOM, 0, 50);
							
						}
					});
					convertView.setBackgroundColor(colors.getColor(colors.getBackground_color()));
					
					LinearLayout layoutUp = new LinearLayout(context);
					LinearLayout layoutDown = new LinearLayout(context);
					for(int i = 0 ; i < evnts.size(); i++) {
						
						List<AgendaGroup> agendaGroup = null;

                        agendaGroup = (List<AgendaGroup>) activity.realm.where(AgendaGroup.class).equalTo( "id",evnts.get(i).getAgenda_group_id()).findAll();
                        // appController.getAgendaGroupDao().queryForEq("id", evnts.get(i).getAgenda_group_id());

                        LinearLayout.LayoutParams pppp = (LayoutParams) ((LinearLayout)convertView.findViewById(R.id.eventContainer)).getLayoutParams();
						pppp.gravity = Gravity.CENTER;
                        if (evnts.size() == 1 & ((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id() ==0)) {
							Drawable drawableTest1 = getColoredDrawable(evnts, i, agendaGroup);
							ImageView img = new ImageView(context);
							LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
							paramsPic.gravity = Gravity.CENTER;
							img.setLayoutParams(paramsPic);
							img.setImageDrawable(drawableTest1);
							((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(img);
						}else if (evnts.size() == 2) {
							((LinearLayout)convertView.findViewById(R.id.eventContainer)).setWeightSum(2);
							LayoutParams paramsT = (LayoutParams) ((LinearLayout)convertView.findViewById(R.id.eventContainer)).getLayoutParams();
							if (i==0)
							if (((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id() == 0)) {
								Drawable drawableTest1 = getColoredDrawable(evnts, i, agendaGroup);
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 22, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(drawableTest1);
								((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(img);
							}else {
								((LinearLayout)convertView.findViewById(R.id.eventContainer)).setWeightSum(1);
								paramsT.gravity = Gravity.CENTER;
							}
							if (i == 1)
							if ( ((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id() == 0)) {
								Drawable drawableTest2 = getColoredDrawable(evnts, i, agendaGroup);
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 22, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(drawableTest2);
								((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(img);
							}else {
								((LinearLayout)convertView.findViewById(R.id.eventContainer)).setWeightSum(1);
								paramsT.gravity = Gravity.CENTER;
							}
							
						}else if (evnts.size() == 3) {
							((LinearLayout)convertView.findViewById(R.id.eventContainer)).setOrientation(LinearLayout.VERTICAL);
							((LinearLayout)convertView.findViewById(R.id.eventContainer)).setWeightSum(2);
							LinearLayout.LayoutParams paramL = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
							paramL.gravity = Gravity.CENTER;
							layoutUp.setOrientation(LinearLayout.HORIZONTAL);
							layoutUp.setWeightSum(2);
							layoutDown.setWeightSum(1);
							layoutDown.setOrientation(LinearLayout.HORIZONTAL);
							layoutUp.setLayoutParams(paramL);
							layoutDown.setLayoutParams(paramL);
							if (i==0)
							if (((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id()>0)) {
								Drawable drawableTest1 = getColoredDrawable(evnts, i, agendaGroup);
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(22, LinearLayout.LayoutParams.MATCH_PARENT, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(drawableTest1);
								layoutUp.addView(img);
							}else {
								layoutUp.setWeightSum(1);
								paramL.gravity = Gravity.CENTER;
							}
							if (i == 1)
							if (((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id()>0)) {
								Drawable drawableTest2 = getColoredDrawable(evnts, i, agendaGroup);
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(22, LinearLayout.LayoutParams.MATCH_PARENT, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(drawableTest2);
								layoutUp.addView(img);
							}else {
								layoutUp.setWeightSum(1);
								paramL.gravity = Gravity.CENTER;
							}
							
							if (i==2  & ((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id() >0)) {
								Drawable drawableTest3 = getColoredDrawable(evnts, i, agendaGroup);
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(22, LinearLayout.LayoutParams.MATCH_PARENT, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(drawableTest3);
								layoutDown.addView(img);
							}
							layoutUp.setLayoutParams(paramL);
							layoutDown.setLayoutParams(paramL);
							
							
							
						}else if (evnts.size() > 3) {

							((LinearLayout)convertView.findViewById(R.id.eventContainer)).setOrientation(LinearLayout.VERTICAL);
							((LinearLayout)convertView.findViewById(R.id.eventContainer)).setWeightSum(2);
							
							LinearLayout.LayoutParams paramL = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
							paramL.gravity = Gravity.CENTER;
							layoutUp.setWeightSum(2);
							layoutDown.setWeightSum(2);
							layoutUp.setOrientation(LinearLayout.HORIZONTAL);
							layoutDown.setOrientation(LinearLayout.HORIZONTAL);
							layoutUp.setLayoutParams(paramL);
							layoutDown.setLayoutParams(paramL);
							if (i==0)
							if (((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id()>0)) {
								Drawable drawableTest1 = getColoredDrawable(evnts, i, agendaGroup);
								Drawable clone = drawableTest1.getConstantState().newDrawable();
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(22, LinearLayout.LayoutParams.MATCH_PARENT, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(clone);
								layoutUp.addView(img);
							}else {
								layoutUp.setWeightSum(1);
								paramL.gravity = Gravity.CENTER;
							}
							if (i == 1)
							if (((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id() == 0)) {
								Drawable drawableTest2 = getColoredDrawable(evnts, i, agendaGroup);
								Drawable clone = drawableTest2.getConstantState().newDrawable();
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(22, LinearLayout.LayoutParams.MATCH_PARENT, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(clone);
								layoutUp.addView(img);
							}else {
								layoutUp.setWeightSum(1);
								paramL.gravity = Gravity.CENTER;
							}
							if (i==2)
							if (((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id() == 0)) {
								Drawable drawableTest3 = getColoredDrawable(evnts, i, agendaGroup);
								Drawable clone = drawableTest3.getConstantState().newDrawable();
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(22, LinearLayout.LayoutParams.MATCH_PARENT, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(clone);
								layoutDown.addView(img);
							}else {
								layoutDown.setWeightSum(1);
								paramL.gravity = Gravity.CENTER;
							}
							if (i == 3)
							if (((agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) || evnts.get(i).getAgenda_group_id() == 0)) {
								Drawable drawableTest4 = getColoredDrawable(evnts, i, agendaGroup);
								ImageView img = new ImageView(context);
								LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(22, LinearLayout.LayoutParams.MATCH_PARENT, 1);
								paramsPic.gravity = Gravity.CENTER;
								img.setLayoutParams(paramsPic);
								img.setImageDrawable(drawableTest4);
								layoutDown.addView(img);
							}else {
								layoutDown.setWeightSum(1);
								paramL.gravity = Gravity.CENTER;
							}
							layoutUp.setLayoutParams(paramL);
							layoutDown.setLayoutParams(paramL);
							
						
							
						}
						/*TextView titleEvent = new TextView(context);
						titleEvent.setPadding(10, 0, 0, 0);
						titleEvent.setTextAppearance(context, style.TextAppearance_Small);
						List<AgendaGroup> agendaGroup = null;
						try {
							agendaGroup = (List<AgendaGroup>) appController.getAgendaGroupDao().queryForEq("id", evnts.get(i).getAgenda_group_id());

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if(agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) {
							titleEvent.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, 
									{android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
									new int[]{colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(agendaGroup.get(0).getColor())}));

							StateListDrawable drawable = new StateListDrawable();
							drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(agendaGroup.get(0).getColor())));
							drawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
							titleEvent.setBackgroundDrawable(drawable);

							//titleEvent.setTextSize(12);
							titleEvent.setText(evnts.get(i).getTitle());

							titleEvent.setTag(evnts.get(i));
							final String grpcolor = agendaGroup.get(0).getColor();
							titleEvent.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Event event = (Event)v.getTag();
									if(event != null) {
										//Toast.makeText(context, " Event Title : "+event.getTitle(), 1000).show();
										calendarEventInfos = new CalendarInfoWindow(context, event, colors, grpcolor);
										((TextView)v).setSelected(true);
										//((TextView)v).setActivated(true);
										getInfoWindowZone(v); 

										//getInfoWindowZone(new Point((int)v.getPivotX(), (int)v.getPivotY()), 180); 
									}
									//								else
									//									Toast.makeText(context, " Event is : "+event, 1000).show();
								}
							});

							((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(titleEvent, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						}else if(evnts.get(i).getAgenda_group_id() == null) {
							titleEvent.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_activated}, {android.R.attr.state_checked}, 
									{android.R.attr.state_selected}, {android.R.attr.state_pressed},{}}, 
									new int[]{colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(colors.getBackground_color()),  colors.getColor(colors.getBackground_color()), 
									colors.getColor(colors.getTitle_color())}));

							StateListDrawable drawable = new StateListDrawable();
							drawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
							drawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
							titleEvent.setBackgroundDrawable(drawable);

							//titleEvent.setTextSize(12);
							titleEvent.setText(evnts.get(i).getTitle());

							titleEvent.setTag(evnts.get(i));
							titleEvent.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Event event = (Event)v.getTag();
									if(event != null) {
										//Toast.makeText(context, " Event Title : "+event.getTitle(), 1000).show();
										calendarEventInfos = new CalendarInfoWindow(context, event, colors, colors.getTitle_color());
										((TextView)v).setSelected(true);
										//((TextView)v).setActivated(true);
										getInfoWindowZone(v); 
									}
									//else
									//Toast.makeText(context, " Event is : "+event, 1000).show();
								}
							});

							((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(titleEvent, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						}
						else {
							((LinearLayout)convertView.findViewById(R.id.eventContainer)).removeView(titleEvent);
						}*/

					}
					((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(layoutDown);
					((LinearLayout)convertView.findViewById(R.id.eventContainer)).addView(layoutUp);

				}

				if(position == getIndexByTodayDate()){

					ShapeDrawable border = new ShapeDrawable(new RectShape());
					border.getPaint().setStyle(Style.STROKE);
					border.getPaint().setStrokeWidth(5);
					border.getPaint().setColor(colors.getColor(colors.getTitle_color(), "AA"));
					convertView.setBackgroundDrawable(border);
				}
			}
		
			
		}

		return convertView;
	}

	/**
	 * @param evnts
	 * @param i
	 * @param agendaGroup 
	 * @param agendaGroup
	 * @return
	 */
	private Drawable getColoredDrawable(List<Event> evnts, int i, List<AgendaGroup> agendaGroup) {
		ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicHeight( 20 );
        biggerCircle.setIntrinsicWidth( 20);
        biggerCircle.setBounds(new Rect(0, 0, 20, 20));
        	
		
		if(agendaGroup != null && agendaGroup.size()>0 && isValidGrp(agendaGroup.get(0))) {
			biggerCircle.getPaint().setColor(colors.getColor(agendaGroup.get(0).getColor()));
		}else if(evnts.get(i).getAgenda_group_id() == 0) {
			biggerCircle.getPaint().setColor(colors.getColor(colors.getTitle_color()));
		}
		return biggerCircle;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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

		Log.e("DAY_OF_WEEK month:   ","" + calendar.get(Calendar.DAY_OF_WEEK));
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

	public void getInfoWindowZone(final View v) {

		//		Point position = new Point((int)v.getX(), (int)v.getY());

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int zone = 0;
		//
		int width = display.getWidth();  
		int height = display.getHeight(); 

		int[] location = new int[2];
		v.getLocationOnScreen(location);
		float x = location[0] ;//v.getX() + CalendarMonthFragment.gridItemX;//itemPos.x;
		float y = location[1] ;//v.getY() + CalendarMonthFragment.gridItemY;//itemPos.y;

		calendarEventInfos.setDimensionByGridLocation();

//		if(height > width) {
//			if((x > calendarEventInfos.getWidth()/2) && ((x + calendarEventInfos.getWidth()/2) < width))
//			{
//				if(y > (calendarEventInfos.getHeight()) && y + calendarEventInfos.getHeight() > height) {
//
//					zone = 270;
//
//				}else if(y < calendarEventInfos.getHeight() && y + calendarEventInfos.getHeight() < height) {
//
//					zone = 90;
//
//				}
//			}
//
//			if((x > calendarEventInfos.getWidth()) /*&& (position.x + mLocationInfos.getWidth()) >= width - 50*/) {
//				if((y > (calendarEventInfos.getHeight()/2)) && (height > (y + ( calendarEventInfos.getHeight()/2 ) ))) {
//					zone = 0;
//				}			
//			}
//			else if((x + calendarEventInfos.getWidth() < width) && ((y > calendarEventInfos.getHeight()/2)  && (y + calendarEventInfos.getHeight()/2 < height))) {
//				zone = 180;
//			}
//
//		}
//		else {
//			if((x < calendarEventInfos.getWidth()/2 + 50) && ((x + calendarEventInfos.getWidth()/2) < width ))
//			{
//				if(y > (calendarEventInfos.getHeight() - 50) /*&& (y + calendarEventInfos.getHeight()) > height*/) {
//
//					zone = 270;
//
//				}else if(y < calendarEventInfos.getHeight() /*&& (y + calendarEventInfos.getHeight()) < height */) {
//
//					zone = 90;
//
//				}
//			}
//
//			else if((y >  calendarEventInfos.getHeight())  && (y + calendarEventInfos.getHeight()) >= height - 100) {
//				if(x + calendarEventInfos.getWidth() > width - 100) {
//					zone = 0;
//				}else if(x < calendarEventInfos.getWidth()) {
//					zone = 180;
//				}
//
//			}
////			else if((x + calendarEventInfos.getWidth() < width - 150) && ((y > calendarEventInfos.getHeight()/2 - 50)  && (y+ calendarEventInfos.getHeight()/2 < height - 50))) {
////				zone = 180;
////			}
//	}

			if((x > calendarEventInfos.getWidth()/2) && ((x + calendarEventInfos.getWidth()/2) < width))
			{
				if(y > (calendarEventInfos.getHeight()) && y + calendarEventInfos.getHeight() > height) {

					zone = 270;

				}else if(y < calendarEventInfos.getHeight() && y + calendarEventInfos.getHeight() < height) {

					zone = 90;

				}
			}

			if((x > calendarEventInfos.getWidth()) /*&& (position.x + mLocationInfos.getWidth()) >= width - 50*/) {
				if((y > (calendarEventInfos.getHeight()/2)) && (height > (y + ( calendarEventInfos.getHeight()/2 ) ))) {
					zone = 0;
				}			
			}
			else{
				zone = 180;
			}



		calendarEventInfos.setViewByGridLocation(zone);



		calendarEventInfos.show(v, zone, height);
		
		calendarEventInfos.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				((TextView)v).setSelected(false);
				
			}
		});
	}





}
