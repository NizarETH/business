package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.AgendaAdapter;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Event;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;

import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;

public class AgendaFragment extends ListFragment{

	private static int IS_HEADER_ADDED = 0;
	private Collection<Child_pages> elements;
	private AgendaAdapter adapter;
//	private Colors colors;
    private Colors colors;

	private ImageLoader imageLoader;
	private String titleInStrip = null;
	private long time;
	private int id;
    public Realm realm;
	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
       // realm.getInstance(getActivity().getApplicationContext());
		View view = inflater.inflate(R.layout.categories_list, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		//Add Header view to show the category title!
		if (titleInStrip!=null && !titleInStrip.isEmpty()) {
			View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
			viewTitle.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
			TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
			titleContactsTV.setText(titleInStrip);
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
			viewTitle.setClickable(false);
			((ListView)view).addHeaderView(viewTitle, null, false);
			IS_HEADER_ADDED = 1;
		}else {
			IS_HEADER_ADDED = 0;
		}
		
		((ListView)view).setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		((ListView)view).setDividerHeight(1);
		
		return view;
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
	//new AppController(getActivity());
		colors= ((MainActivity)activity).colors;
        realm=Realm.getInstance(getActivity());
		Parameters ParamColor = realm.where(Parameters.class).findFirst();
		if (colors==null) {
            /*try {
                colors = new Colors(appController.getParametersDao().queryForId(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
             colors= new Colors(ParamColor);
        }
		
		time = System.currentTimeMillis();
		((MainActivity)activity).bodyFragment = "AgendaFragment";
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		List<Section> sections = new ArrayList<Section>();
        sections = realm.where(Section.class).equalTo("type","agenda").findAll();
        ////appController.getSectionsDao().queryForEq("type","agenda");
        if (sections.size()>0) {
			Section section = sections.get(0);
			titleInStrip = section.getName();
		}

        List<Event> events = realm.where(Event.class).findAll();
        //appController.getEventDao().queryForAll();
        adapter = new AgendaAdapter((MainActivity)getActivity(), events, colors);
        setListAdapter(adapter);


    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#getSelectedItemId()
	 */
	@Override
	public long getSelectedItemId() {
		// TODO Auto-generated method stub
		return super.getSelectedItemId();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#getSelectedItemPosition()
	 */
	@Override
	public int getSelectedItemPosition() {
		// TODO Auto-generated method stub
		return super.getSelectedItemPosition();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Event element = adapter.getElements().get(position-IS_HEADER_ADDED); // because of the listView header we subtract 1
		if (!element.getLink().isEmpty()) {
			WebViewFragment  webViewFragment= new WebViewFragment();
			((MainActivity)getActivity()).extras = new Bundle();
			((MainActivity)getActivity()).extras.putString("link", element.getLink());
			webViewFragment.setArguments(((MainActivity)getActivity()).extras);
			((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
			((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragment_container, webViewFragment).addToBackStack("WebViewFragment").commit();
			if (((MainActivity)getActivity()).timer != null) {((MainActivity)getActivity()).timer.cancel();}
			((MainActivity)getActivity()).findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
			((MainActivity)getActivity()).findViewById(R.id.swipe_container).setVisibility(View.GONE);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#setListAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setListAdapter(adapter);
	}

	/**
	 * 
	 */
	public AgendaFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_events_section", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}

	
	

	
}
