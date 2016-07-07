package com.euphor.paperpad.activities.fragments;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.CalendarEventAdapter;
import com.euphor.paperpad.Beans.AgendaGroup;
import com.euphor.paperpad.Beans.Event;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.ObservableScrollView;
import com.euphor.paperpad.widgets.ScrollViewListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import io.realm.Realm;

/**
 * @author euphordev04
 *
 */
public class CalendarMonthFragment extends Fragment implements ScrollViewListener {
	

	private List<Event> events;
	private CalendarEventAdapter adapter;
	private GridView grid;
	//private Typeface font;
	private Colors colors;
	private List<AgendaGroup> agendaGrps;
    public Realm realm;
	//private static String POLICE;
	
    private String tag;
	private boolean isTablet;

    public void setCustomTag(String tag)
    {
        this.tag = tag;
    }

    public String getCustomTag()
    {
        return tag;
    }
    
	public static CalendarMonthFragment create(List<Event> events, List<AgendaGroup> agendaGrps){
		CalendarMonthFragment fragment = new CalendarMonthFragment();
		fragment.setListEvent(events);
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
//		if (getArguments() != null) {
//			int event_id = getArguments().getInt("event_id");
//			
////			if (event_id != 0) {
////				try {
////					event = appController.getEventDao().queryForId(event_id);
////				} catch (SQLException e) {
////					e.printStackTrace();
////				}
////			}
//		}
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		POLICE = "gill-sans-light.ttf"; //"fonts/OpenSans-Regular.ttf";
//		font = Typeface.createFromAsset(getActivity().getAssets(), POLICE); 
		Date dateOfMonth = null;


		try {
       if(events!=null)
		if(events.size() > 0)
			dateOfMonth = new SimpleDateFormat("yyyy-MM-dd").parse(events.get(0).getDate());
//		else {
//			
//				dateOfMonth = new SimpleDateFormat("yyyy-MM-dd").parse(events.get(0).getDate());
//			
//		}
		adapter = new CalendarEventAdapter((MainActivity)getActivity(), events, agendaGrps, dateOfMonth, colors, realm);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        isTablet = Utils.isTablet(getActivity());// getResources().getBoolean(R.bool.isTablet);

		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		if (isTablet) {
			view = inflater.inflate(R.layout.calendar_month_grid, container, false); 
		}else {
			view = inflater.inflate(R.layout.calendar_month_grid_smartphone, container, false); 
		}
		
		
		grid = (GridView)view.findViewById(R.id.grid);
		grid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		grid.setAdapter(adapter);

		return view;
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
		adapter.updateCalendarGridView(agendaGrps);
		adapter.notifyDataSetChanged();
		adapter.notifyDataSetInvalidated();
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
