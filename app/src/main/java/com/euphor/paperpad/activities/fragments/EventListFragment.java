package com.euphor.paperpad.activities.fragments;

import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.CalendarListEventadapter;
import com.euphor.paperpad.Beans.AgendaGroup;
import com.euphor.paperpad.Beans.Event;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class EventListFragment extends Fragment {

	private RealmResults<Event> events;

	
	private ListView listEvents;


	protected LinearLayout choiceHolder;
	
	private CalendarListEventadapter adapter;
    public Realm realm;

	//private AgendaEventAdapter adapter;

	List<String> childPagesKeys, dayKeys;
	HashMap<String, List<Event>> map;


	private Colors colors;

	private List<AgendaGroup> agendaGroups;

	private long time;
	
	String title;
	
	@Override
	public void onAttach(Activity activity) {
	//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
        events =realm.where(Event.class).findAllSorted("date"); //appController.getEventDao().queryForAll();
        RealmResults<Section> section = realm.where(Section.class).equalTo("id_s", getArguments().getInt("Section_id")).findAll();// appController.getSectionsDao().queryForEq("id_section", getArguments().getInt("Section_id"));
        if(section != null && section.size() > 0) {
            title = section.get(0).getName();
        }
        colors = ((MainActivity)activity).colors;

        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

		((MainActivity)activity).bodyFragment = "EventListFragment";

		((MainActivity)activity).extras = new Bundle();
		((MainActivity)activity).extras.putInt("id_section", getArguments().getInt("Section_id"));
		//setAdapter(new CalendarPagerAdapter(getChildFragmentManager()));
		time = System.currentTimeMillis();
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		map = getMapEvents(events);
		adapter = new CalendarListEventadapter(getActivity().getApplicationContext(), events, colors, realm/*appController*/);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.calendar_list_event, container, false);
        view.setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));
		listEvents = (ListView)view.findViewById(R.id.listEvent);
		
		choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
		
		TextView titleEvents = (TextView)view.findViewById(R.id.titleListevent);
        titleEvents.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
        titleEvents.setTypeface(MainActivity.FONT_TITLE);
		titleEvents.setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color()));
		titleEvents.setTextColor(colors.getColor(colors.getBackground_color()));
		titleEvents.setText(title);

		
		agendaGroups = getAvailableAgendaGroup();
		realm.beginTransaction();
		for(int i = 0; i < agendaGroups.size(); i++) {
			agendaGroups.get(i).setSelected(true);
			fillNavigationBar(agendaGroups.get(i));
		}
		realm.commitTransaction();
		View v =new View(getActivity());
		v.setAlpha(0);
		v.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, view.findViewById(R.id.SVchoicesHolder).getLayoutParams().height));
		listEvents.addFooterView(v);
		

		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		listEvents.setAdapter(adapter);	
		listEvents.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				
				Event event = (Event)v.getTag();
				WebViewFragment  webViewFragment= new WebViewFragment();
				Bundle extras = new Bundle();
				extras.putString("link", event.getLink());
				webViewFragment.setArguments(extras);
				((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
				getActivity().getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, webViewFragment).addToBackStack("WebViewFragment").commit();
				
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}
	
	public List<Event> getOrderedListEvent(HashMap<String, List<Event>> map){
		return events;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if(map != null)
			map = null;
		super.onDestroy();
	}
	
	
	private List<AgendaGroup> getAvailableAgendaGroup(){
		List<AgendaGroup> agendaGrps = new ArrayList<AgendaGroup>();
        RealmResults<AgendaGroup> agendaGroups = realm.where(AgendaGroup.class).findAll();//appController.getAgendaGroupDao().queryForAll();
        for(int j = 0; j < agendaGroups.size(); j++) {
        for(int i = 0; i < events.size(); i++) {

            if(events.get(i).getAgenda_group_id() == agendaGroups.get(j).getId()) {
                agendaGrps.add(agendaGroups.get(j));
                break;
            }
        }
        }

        return agendaGrps;
	}
	
	private void fillNavigationBar(AgendaGroup agendaGroup) {
		LinearLayout container = new LinearLayout(getActivity());
		ImageView img = new ImageView(getActivity());
		TextView categoryTxt = new TextView(getActivity());
		categoryTxt.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setGravity(Gravity.CENTER);
//		LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(20, 20);
//		imgParams.gravity = Gravity.CENTER;
//		img.setLayoutParams(imgParams);
		LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        containerParams.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int icon_size = (int) Utils.dpToPixels(getActivity(),  25f);

        //txtParams.gravity = Gravity.CENTER;
		if (agendaGroup.isSelected()) {
            containerParams.setMargins(10, 10, 0, 10);
            txtParams.setMargins(10, 0, 10, 0);
			//categoryTxt.setPadding(5, 5, 5, 5);
			categoryTxt.setGravity(Gravity.CENTER);
			categoryTxt.setText(agendaGroup.getTitle().toUpperCase());
			categoryTxt.setTextColor(colors.getColor(colors.getBackground_color()));
			categoryTxt.setTextSize(16);
//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
//			selectDrawable.setColorFilter(colors.getColor(agendaGroup.getColor()), Mode.MULTIPLY);
//			categoryTxt.setBackgroundDrawable(selectDrawable);
			Glide.with(getActivity()).load(new File("android_asset/"+agendaGroup.getIcon())).into(img);
			container.setTag(agendaGroup);
			//img.setPadding(3, 0, 5, 0);
			container.addView(img, icon_size, icon_size);
			container.addView(categoryTxt, txtParams);
			container.setBackgroundColor(colors.getColor(agendaGroup.getColor()));
            container.setPadding(10, 0, 10, 0);
            choiceHolder.addView(container, containerParams);
		}else {
            containerParams.setMargins(10, 10, 0, 10);
            txtParams.setMargins(10, 0, 10, 0);
			//categoryTxt.setPadding(5, 5, 5, 5);
			categoryTxt.setGravity(Gravity.CENTER);
			categoryTxt.setText(agendaGroup.getTitle().toUpperCase());
			categoryTxt.setTextColor(colors.getColor(agendaGroup.getColor()));
			categoryTxt.setTextSize(16);
			Bitmap bm = null;
			try{
				bm = BitmapFactory.decodeStream(getActivity().getAssets().open(agendaGroup.getIcon()));
			}catch (IOException e) {
				e.printStackTrace();
			}
			Drawable drawable = new BitmapDrawable(bm);//(new File("android_asset/"+agendaGroup.getIcon()).getAbsolutePath());
			drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(agendaGroup.getColor()),PorterDuff.Mode.MULTIPLY));
			img.setBackgroundDrawable(drawable);
//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
//			selectDrawable.setColorFilter(colors.getColor(agendaGroup.getColor()), Mode.MULTIPLY);
//			categoryTxt.setBackgroundColor(Color.TRANSPARENT);//Drawable(selectDrawable);
			container.setTag(agendaGroup);
			//img.setPadding(3, 0, 5, 0);
			container.addView(img, icon_size, icon_size);
			container.addView(categoryTxt, txtParams);
			container.setBackgroundColor(Color.TRANSPARENT);
            container.setPadding(10, 0, 10, 0);
			choiceHolder.addView(container, containerParams);
		}
		
		container.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AgendaGroup agendaGroup = (AgendaGroup)v.getTag();

				//agendaGroup.setSelected(false);
				choiceHolder.removeAllViews();
				realm.beginTransaction();
				for (int i = 0; i < agendaGroups.size(); i++) {
					if(agendaGroups.get(i).getId() == agendaGroup.getId())
						agendaGroups.get(i).setSelected(agendaGroups.get(i).isSelected()?false:true);
						fillNavigationBar(agendaGroups.get(i));
				}		
				realm.commitTransaction();
//					CalendarMonthFragment frag1= (CalendarMonthFragment)mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem());
//					frag1.updateAdapter(agendaGroups);
				adapter.updateCalendarGridView(agendaGroups);
				adapter.notifyDataSetChanged();

			}
		});

	}


	
	public HashMap<String, List<Event>> getMapEvents(List<Event> events){
		HashMap<String,  List<Event>> map = new HashMap<String, List<Event>>();
		
		
		String tmp, comparTag = "", dateTag = "";
		int j,som = 0;
		
		Event event = null;
		
		j = events.size() - 1;
		
		if(j > 0) {
			//fastTri(events, j - 1, j);
			//quicksort(events, 0, j);
		}

			
		int year = 0;
		int month = 0;
		int day = 0;
		
		List<String> list = new ArrayList<String>();
		
//		pages = new ArrayList<Child_pages>();
		dayKeys = new ArrayList<String>();
		childPagesKeys= new ArrayList<String>();
		
		List<Event> listEvent= new ArrayList<Event>();
		Calendar mydate = new GregorianCalendar();
		boolean end = false;
		boolean anotherDate = false;
		String hours = "", minutes = "";
		j = 0;
		while(!end) {
		/*listEvent = new RealmResults<Event>();*/
 
		for(int i = j ; i<events.size(); i++) {
			event = events.get(i);
			
			try {

				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(event.getDate());
				mydate.setTime(date);
				dateTag = ""+(mydate.get(Calendar.MONTH) + 1)+mydate.get(Calendar.YEAR)+"";
				
				// if( comparTag.compareTo(dateTag) != 0)
				
				
				if(!dateTag.isEmpty() && !isAlreadyChecked(list, dateTag)) {
					if(anotherDate) break;
//					if(isLooperFinished) {
					list.add(dateTag);
					listEvent.add(event);
					comparTag = dateTag; //list.get(j);
					year = mydate.get(Calendar.YEAR); //date.getYear();
					month = mydate.get(Calendar.MONTH); //date.getMonth();
					day = mydate.get(Calendar.DAY_OF_MONTH); //date.getDay();
					dayKeys.add(""+day);
//					}
					anotherDate = true;
					hours = ""+mydate.get(Calendar.HOUR_OF_DAY);
					minutes = ""+mydate.get(Calendar.MINUTE);
					if (hours.length() == 1) {
						hours = "0"+hours;
					}
					if (minutes.length() == 1) {
						minutes = "0"+minutes;
					}
					//isLooperFinished = false;
					j++;
				}else if(comparTag.compareTo(dateTag) == 0){
					
					listEvent.add(event);
					dayKeys.add(""+day);
					j++;
				}
				
	
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		anotherDate = false;

		SharedPreferences wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		String lang = wmbPreference.getString(Utils.LANG, "fr");
		if(lang.compareTo("en") == 0)
			tmp = DateFormatSymbols.getInstance(Locale.ENGLISH).getShortMonths()[month]+"\n"+year;
		else
			tmp = DateFormatSymbols.getInstance(Locale.FRENCH).getShortMonths()[month]+"\n"+year;
		
		if(!map.keySet().contains(tmp)) {
			map.put(tmp, listEvent);
			childPagesKeys.add(tmp);
		}
		
		
		som += listEvent.size();
		
		if(som >= events.size()) {
			end = true;
		}
		
		}
		
		return map;
		
	}



	
	public boolean isAlreadyChecked(List<String> list, String value) {
		if(list.size() == 0)return false;

		for(int i = 0; i< list.size(); i++) {
			if(list.get(i).compareTo(value) == 0)
				return true;
		}

		return false;
	}


	public void fastTri(List<Event> events, int pivotIndex, int lastIndex){

		int j = 0;

		if(pivotIndex < lastIndex) 
		{
			j = partition(events, pivotIndex, lastIndex);
			fastTri(events, pivotIndex, j);
			fastTri(events, j+1 , lastIndex);
		}	

	}	



	public static void quicksort(List<Event> events, int debut, int fin) {
		if (debut < fin) {
			int indicePivot = partition(events, debut, fin);
			quicksort(events, debut, indicePivot-1);
			quicksort(events, indicePivot+1, fin);
		}
	}

	public static int partition (List<Event> events, int debut, int fin) {
		Event pivotEvent = events.get(debut);
		String valeurPivot = events.get(debut).getDate(); //t[d�but];
		int d = debut+1;
		int f = fin;
		while (d < f) {
			while(d < f && events.get(f).getDate().compareTo(valeurPivot) >= 0) f--;
			while(d < f && events.get(d).getDate().compareTo(valeurPivot) <= 0) d++;


			Event tmp = events.get(d);
			events.set(d, events.get(f));
			events.set(f, tmp);

		}
		if (events.get(d).getDate().compareTo(valeurPivot) >= 0) d--;
		events.set(debut, events.get(d));
		events.set(d, pivotEvent);
		return d;
	}

	
	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "events_section", 0);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}
}
