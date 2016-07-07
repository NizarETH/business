package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.AgendaEventAdapter;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Event;

import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.LinearLayoutOutlined;


import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

//import android.graphics.Typeface;

public class AgendaSplitFragment extends Fragment {


	private List<CommunElements1> elements;
	private Colors colors;

	private String titleInStrip = null;
	private List<Event> events;
    public Realm realm;
	//private Child_pages page;
	private List<Child_pages> pages;
	
	protected int id_cat = 0;
	protected LinearLayout choiceHolder;
	//private MainActivity mainActivity;
	public static ListView list;
	/** Uness Modif **/
	public static int mActivatedPosition = 1;
//	View view;
	View view;
	int index;
	private AgendaEventAdapter adapter;
	
	List<String> childPagesKeys, dayKeys;
	HashMap<String, List<Event>> map;
	//private String key;
	
	/**
	 * 
	 */
	public AgendaSplitFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//	        super.onConfigurationChanged(newConfig); 
//
//	   if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//	        Toast.makeText(this.getActivity(), "landscape", Toast.LENGTH_SHORT).show();
//	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//	        Toast.makeText(this.getActivity(), "portrait", Toast.LENGTH_SHORT).show();
//	    }
//
//	//Your fragment animation layout changing code
//
//	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//this.inflater = inflater;
		

			view = inflater.inflate(R.layout.agenda_acceuil, container, false);
			view.findViewById(R.id.agendaListContainer).setBackgroundColor(colors.getColor(colors.getBackground_color(), "E5"));

			ImageView img = (ImageView)view.findViewById(R.id.imageAgendaAcceuil);
			if (section != null && section.getIllustration() != null && !section.getIllustration().isEmpty()) {
				Glide.with(getActivity()).load(section.getIllustration()).into(img);
			}
			if(!isTablet) {
				view.findViewById(R.id.startView).setVisibility(View.GONE);
				view.findViewById(R.id.endView).setVisibility(View.GONE);
			}



		if(list != null) list = null;
		//titleInStrip = categories.get(index).getTitle();
		ListView listView = (ListView)view.findViewById(android.R.id.list);

        TextView agendaTitle = (TextView)view.findViewById(R.id.agendaTitle);

        agendaTitle.setTypeface(MainActivity.FONT_TITLE);
//			titleContactsTV.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/gill-sans-light.ttf"));
        agendaTitle.setTextSize(22);
        agendaTitle.setTextColor(colors.getColor(colors.getTitle_color(), "90"));
        Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
        selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color(),"08"), Mode.MULTIPLY);
        agendaTitle.setBackgroundDrawable(selectDrawable);
        //agendaTitle.setBackgroundColor(colors.getColor(colors.getBackground_color(), "CC"));
		
		if (titleInStrip!=null && !titleInStrip.isEmpty()) 
		{

            agendaTitle.setText(titleInStrip);
		}else{
            agendaTitle.setText("Agenda");
        }
		
		(listView).setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		(listView).setDividerHeight(1);
		list = listView;


		view.findViewById(R.id.backChoices).setBackgroundColor(colors.getColor(colors.getTitle_color(), "30"));;
//		if (categories.size()>0) {
			choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
			
			map = getMapEvents(events);
			
			if(map.size() > 0) {
			List<Event> listEvent = map.get(childPagesKeys.get(index));
                realm.beginTransaction();
				for (int i = 0; i < map.size(); i++) {
					Event event = map.get(childPagesKeys.get(i)).get(0);
					event.setSelected(false);

				}

				 listEvent.get(0).setSelected(true);
                realm.commitTransaction();

			
			for(int i = 0; i < map.size(); i++) {
					fillNavigationBar(childPagesKeys.get(i));
			}
			
			if (childPagesKeys != null) 
			{
				List<Event> listEvents = map.get(childPagesKeys.get(index));
				String key = childPagesKeys.get(index);
                realm.beginTransaction();
				fillEventsScroller(key, listEvents);
                realm.commitTransaction();
			}
						

			
		}
		return view;
		
	}
	
@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
	adapter = new AgendaEventAdapter((MainActivity) getActivity(), elements, colors, R.layout.item_event, 18);
	list.setAdapter(adapter);

	super.onViewCreated(view, savedInstanceState);
}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		list = (ListView)getActivity().findViewById(android.R.id.list);
		adapter = new AgendaEventAdapter((MainActivity) getActivity(), elements, colors, R.layout.item_event, 18); 		
		list.setAdapter(adapter);

		
		list.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				
				
				CommunElements1 element = adapter.getItem(position);  //getElements().get(position-IS_HEADER_ADDED); // because of the listView header we subtract 1

					Child_pages pg = ((Child_pages) element);
					if(pg == null || pg.getId_cp() == 0) return;
					

				
//						Calendar mydate = new GregorianCalendar();
//						Date date = null;
//						try {
//							date = new SimpleDateFormat("dd MMMM yyyy").parse(pg.getLocalDate()/*pg.getDay().split(" ")[0]*/);
//							mydate.setTime(date);
//
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						
//						String weekDay;
//						//Calendar c = AgendaAdapter.parseDateBeta(pg.getLocalDate());
//						//Calendar c = Calendar.getInstance();
//						weekDay = mydate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());


				if(isTablet) {
					((MainActivity) getActivity()).extras = new Bundle();
					((MainActivity) getActivity()).extras.putInt("page_id", pg.getId_cp());
					
					SimplePageFragment pagesFragment = new SimplePageFragment();
					pagesFragment.setArguments(((MainActivity) getActivity()).extras);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navitem_detail_container, pagesFragment).commit();
					

				}else {
					
					view.findViewById(R.id.agendaListContainer).setVisibility(View.GONE);
					view.findViewById(R.id.eventDate).setVisibility(View.GONE);
					
					((MainActivity) getActivity()).extras = new Bundle();
					((MainActivity) getActivity()).extras.putInt("page_id", pg.getId_cp());
					((MainActivity) getActivity()).extras.putString("design", "horizontal");
					PagesFragment pagesFragment = new PagesFragment();
					pagesFragment.setArguments(((MainActivity) getActivity()).extras);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navitem_detail_container, pagesFragment).commit();
					

				}

				view.findViewById(R.id.startView).setVisibility(View.GONE);
				view.findViewById(R.id.endView).setVisibility(View.GONE);
				view.findViewById(R.id.simplePage).setVisibility(View.VISIBLE);	
				
			TextView txt = (TextView) view.findViewById(R.id.eventDate);
			txt.setBackgroundColor(colors.getColor(colors.getForeground_color()));
			txt.setTextColor(colors.getColor(colors.getBackground_color()));
			txt.setText(pg.getLocalDate());
			txt.setTypeface(MainActivity.FONT_BODY);



				if(mActivatedPosition != position){
			if(list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()) != null)
			{
				if(position >1 && position < 4) {
					list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
					((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.content_event)).setTextColor(colors.getColor(colors.getTitle_color()));
					((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.content_event_discript)).setTextColor(colors.getColor(colors.getTitle_color(),"80"));

				}
				if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.content_event)) != null) {
				list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
				((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.content_event)).setTextColor(colors.getColor(colors.getTitle_color()));
				((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.content_event_discript)).setTextColor(colors.getColor(colors.getTitle_color(),"80"));

				
				}
			}
			
			mActivatedPosition = position;

			list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundDrawable(colors.makeGradientToColor(colors.getForeground_color()));//Color(colors.getColor(colors.getTitle_color()));
			((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.content_event)).setTextColor(colors.getColor(colors.getBackground_color()));
			((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.content_event_discript)).setTextColor(colors.getColor(colors.getBackground_color(),"50"));

				}


				
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	int category_id, section_id;
	String display_type;
	Section section;
	private boolean isTablet;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		//new AppController(getActivity());
		colors = ((MainActivity)activity).colors;
		realm = Realm.getInstance(getActivity());
		Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
//		if(getArguments().containsKey("display_type")) {
//			display_type = getArguments().getString("display_type");
//		}
		
		
		index = getArguments().getInt("index", 0);
		Log.e("Index is ", " : "+index);
		//mainActivity = (MainActivity)activity;
		category_id = getArguments().getInt("Category_id");
		section_id = getArguments().getInt("Section_id");
		((MainActivity)getActivity()).bodyFragment = "AgendaSplitFragment";
		((MainActivity)getActivity()).extras = new Bundle();
		((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
		((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
		((MainActivity)getActivity()).extras.putInt("index", index);
		
		isTablet = Utils.isTablet(activity);//getResources().getBoolean(R.bool.isTablet);
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		setRetainInstance(true);
		realm = Realm.getInstance(getActivity());
		section_id = getArguments().getInt("Section_id");

        section = realm.where(Section.class).equalTo("id_s",section_id).findFirst();
        //appController.getSectionsDao().queryForId(section_id);

        if (section!=null) {
            titleInStrip = section.getTitle();

            if(section.getEvents().size() > 0) {

                events = new ArrayList<Event>();
                for (Iterator<Event> iterator = section.getEvents()
                        .iterator(); iterator.hasNext();) {
                    Event event = (Event) iterator.next();
                    events.add(event);
                }


            }
        }


        super.onCreate(savedInstanceState);
	}
	
	
	public HashMap<String, List<Event>> getMapEvents(List<Event> events){
		HashMap<String,  List<Event>> map = new HashMap<String, List<Event>>();
		
		
		String tmp, comparTag = "", dateTag = "";
		int j = 0,som = 0;
		
		if(events != null)
			j = events.size() - 1;
		
		Event event = null;

		
		if(j > 0) {
			//fastTri(events, j - 1, j);
			quicksort(events, 0, j);
		}

			
		int year = 0;
		int month = 0;
		int day = 0;
		
		List<String> list = new ArrayList<String>();
		
//		pages = new ArrayList<Child_pages>();
		dayKeys = new ArrayList<String>();
		childPagesKeys= new ArrayList<String>();
		
		List<Event> listEvent = null; // = new ArrayList<Event>();
		Calendar mydate = new GregorianCalendar();
		boolean end = false;
		boolean anotherDate = false;
		j = 0;
		while(!end) {
		listEvent = new ArrayList<Event>();
 
		for(int i = j ; i<events.size(); i++) {
			event = events.get(i);
			
			try {

				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
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
					day = mydate.get(Calendar.DAY_OF_MONTH) + 1; //date.getDay();
					dayKeys.add(""+day);
//					}
					anotherDate = true;
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
	
	
//	public int partition(List<Event> events, int pivotIndex, int lastIndex) {
//		
//		if(events == null || events.size() == 0)return -1;
//		
//		int i,j;
//
//		String pivot = events.get(pivotIndex).getDate();
//		i = pivotIndex - 1;
//		j = lastIndex + 1;
//		do {
//			
//			do {
//				j--;
//			}while(events.get(j).getDate().compareTo(pivot) > 0);
//			
//			do {
//				i++;
//			}while(events.get(i).getDate().compareTo(pivot) < 0);
//
//		if(i < j)
//		{
//			Event tmp = events.get(i);
//			events.set(i, events.get(j));
//			events.set(j, tmp);
//		}
//		}while(i<j);
//		
//		return j;
//	}
	
	
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

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	/** a method to fill the upper bar where we choose the {@link CategoriesMyBox}
	 * @param childPageKey
	 */
	private void fillNavigationBar(String childPageKey) {
		TextView childPageTxt = new TextView(getActivity());
		childPageTxt.setTypeface(MainActivity.FONT_BODY);
		int width = 110;
		
		if(isTablet) {
			width = 80;
		}
		
		if (map.get(childPageKey).get(0).isSelected() ) {
			
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.gravity = Gravity.CENTER_VERTICAL;
			childPageTxt.setGravity(Gravity.CENTER);
			childPageTxt.setText(childPageKey);
			childPageTxt.setTextColor(colors.getColor(colors.getTitle_color()));
			//view.findViewById(R.id.SVchoicesHolder).setBackgroundColor(colors.getColor(colors.getTitle_color()));
			com.euphor.paperpad.widgets.LinearLayoutOutlined lyt = new LinearLayoutOutlined(getActivity(), null);
			lyt.setColor(colors);
			lyt.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));
			lyt.setBackgroundColor(colors.getColor(colors.getBackground_color()));
//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
//			selectDrawable.setColorFilter(colors.getColor(colors.getBackground_color()), Mode.MULTIPLY);
//			childPageTxt.setBackgroundDrawable(selectDrawable);
//			childPageTxt.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			childPageTxt.setTextSize(15);
			childPageTxt.setTag(childPageKey);
			lyt.setGravity(Gravity.CENTER);
			lyt.addView(childPageTxt);
			choiceHolder.addView(lyt, txtParams);
			
		}else {
			
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(width , LinearLayout.LayoutParams.MATCH_PARENT);
//			txtParams.setMargins(10, 0, 10, 5);
			txtParams.setMargins(0, 7, 0, 0);
			txtParams.gravity = Gravity.CENTER_VERTICAL;
			childPageTxt.setGravity(Gravity.CENTER);
			txtParams.gravity = Gravity.CENTER;
			childPageTxt.setText(childPageKey);
			childPageTxt.setTextSize(12);
			childPageTxt.setTextColor(colors.getColor(colors.getTitle_color(), "CC"));
			view.findViewById(R.id.SVchoicesHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(), "CC"));
			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
			selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color(),"10"), Mode.MULTIPLY);
			childPageTxt.setBackgroundDrawable(selectDrawable);
//			childPageTxt.setBackgroundColor(colors.getColor(colors.getTitle_color(),"20"));
			childPageTxt.setTag(childPageKey);
			choiceHolder.addView(childPageTxt, txtParams);
		}
		
		childPageTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String childPageKey = (String)v.getTag();

				for (int i = 0; i < map.size(); i++) {
					Event event = map.get(childPagesKeys.get(i)).get(0);
                    realm.beginTransaction();
					event.setSelected(false);
                    realm.commitTransaction();
				}
                realm.beginTransaction();
				 map.get(childPageKey).get(0).setSelected(true);
                realm.commitTransaction();
				choiceHolder.removeAllViews();
				for (int i = 0; i < childPagesKeys.size(); i++) {
					
					fillNavigationBar(childPagesKeys.get(i));
				}
				
				if (childPageKey!=null && map.get(childPageKey).size()>0) {
                    realm.beginTransaction();
					fillEventsScroller(childPageKey, map.get(childPageKey));
                    realm.commitTransaction();
					mActivatedPosition = 0;
				}
				
				for(int i= 0; i< map.size(); i++) {
					if(map.get(childPagesKeys.get(i)).get(0).isSelected()) {
						mActivatedPosition = 0;
						((MainActivity) getActivity()).extras.putInt("index", i);
					}
				}			
				
			}
		});
	}

	protected View selectedBefore;
//	private int indexCurrent;
//	private Parameters parameters;
//	private Child_pages page;
	/**
	 * @param key
     * @param selectedList
	 */
	public void fillEventsScroller(String key, List<Event> selectedList) {

		elements = new ArrayList<CommunElements1>();
		for(int i = 0; i<selectedList.size(); i++) {
			
			try {

				if(selectedList.get(i).getLink_page_identifier() != 0) {

				//List<Child_pages> 
				pages = realm.where(Child_pages.class).equalTo("id", selectedList.get(i).getLink_page_identifier()).findAll();
				//appController.getChildPageDao().queryForEq("id", selectedList.get(i).getLink_page_identifier());
				ArrayList<Illustration> images =  new ArrayList<Illustration>();
				if(pages != null && pages.size() > 0) {
				images.add(pages.get(0).getIllustration());
				Child_pages page = pages.get(0);

				if(images != null && images.size() > 0)
                 page.setIllustration(images.get(images.size() - 1));
				page.setDay(selectedList.get(i).getDate().split("-")[2].substring(0, 3));
				
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(selectedList.get(i).getDate());
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				page.setLocalDate(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "+DateUtils.formatDateTime(getActivity(), date.getTime(), 20));
				page.setDesign("horizontal");
//				Log.e(" Time : ", "  <<==>> "+DateUtils.formatDateTime(getActivity(), date.getTime(), 16));
				page.setTitle(selectedList.get(i).getTitle());
				page.setIntro(selectedList.get(i).getIntro());
				//if(pages != null && pages.size()>0)
					elements.add(page);

				}else {
					Child_pages page = realm.createObject(Child_pages.class); /*new Child_pages();*/
                    int id=realm.where(Child_pages.class).findAllSorted("id").last().getId()+1;
                    page.setId(id);
                    int id_cp=realm.where(Child_pages.class).findAllSorted("id_cp").last().getId_cp()+1;
                    page.setId_cp(id_cp);
					page.setTitle(selectedList.get(i).getTitle());// map.get(key).get(i).getTitle());
					page.setIntro(selectedList.get(i).getIntro());//map.get(key).get(i).getIntro());
					page.setDay(selectedList.get(i).getDate().split("-")[2].substring(0, 3));
                    page.setIllustration(map.get(key).get(i).getIllustration());
					
					
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(selectedList.get(i).getDate());
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					page.setLocalDate(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "+DateUtils.formatDateTime(getActivity(), date.getTime(), 20));
					page.setDesign("horizontal");
					
//					Log.e(" Time : ", "  <<==>> "+DateUtils.formatDateTime(getActivity(), date.getTime(), 16));

					page.setTitle(selectedList.get(i).getTitle());
					page.setIntro(selectedList.get(i).getIntro());
						elements.add(page);

				}
				
				}else {

					Child_pages page = realm.createObject(Child_pages.class);/*new Child_pages();*/
                    int id=realm.where(Child_pages.class).findAllSorted("id").last().getId()+1;
                    page.setId(id);
                    int id_cp=realm.where(Child_pages.class).findAllSorted("id_cp").last().getId_cp()+1;
                    page.setId_cp(id_cp);
					page.setTitle(map.get(key).get(i).getTitle());
					page.setIntro(map.get(key).get(i).getIntro());
					page.setDay(selectedList.get(i).getDate().split("-")[2].substring(0, 3));
					page.setIllustration(map.get(key).get(i).getIllustration());
					
					
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(selectedList.get(i).getDate());
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					page.setLocalDate(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "+DateUtils.formatDateTime(getActivity(), date.getTime(), 20));
					page.setDesign("horizontal");
					
//					Log.e(" Time : ", "  <<==>> "+DateUtils.formatDateTime(getActivity(), date.getTime(), 16));

					page.setTitle(selectedList.get(i).getTitle());
					page.setIntro(selectedList.get(i).getIntro());
						elements.add(page);

				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		if(adapter != null) {
		adapter.setEvents(elements);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		}
		else {
			adapter = new AgendaEventAdapter((MainActivity) getActivity(), elements, colors, R.layout.item_event, 18);
			list.setAdapter(adapter);
		}
		//elements.addAll(getChildrenPages(cat));

	}
	
	@Override
	public void onDestroyView() {
		list.removeAllViewsInLayout();
		super.onDestroyView();
	}

	
}
