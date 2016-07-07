/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.euphor.paperpad.Beans.Event;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.RSSAdapter;
import com.euphor.paperpad.rss.RssService;
import com.euphor.paperpad.utils.Colors;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import nl.matshofman.saxrssreader.RssFeed;

/**
 * @author euphordev02
 *
 */
public class RSSFragment extends Fragment {

	private static int IS_HEADER_ADDED = 0;
	public static RSSAdapter adapter;
	public Colors colors;

	private String titleInStrip = null;
	RssFeed feed;
	private int section_id;
	public ListView listRSS;
    public Realm realm;

	@Override
	public void onAttach(Activity activity) {
		//new AppController(getActivity());

				realm = Realm.getInstance(getActivity());

		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }


        if (getArguments() != null) {
			section_id = getArguments().getInt("Section_id");
			((MainActivity) getActivity()).bodyFragment = "RSSFragment";
			((MainActivity) getActivity()).extras = new Bundle();
			((MainActivity) getActivity()).extras.putInt("Section_id", section_id);
		}else {
            List<Section> sections = realm.where(Section.class).equalTo("type", "rss").findAll();//appController.getSectionsDao().queryForEq("type", "rss");
            if (sections.size()>0) {
                section_id = sections.get(0).getId_s();
                ((MainActivity) getActivity()).extras.putInt("Section_id", section_id);
            }
        }
		
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}

	private void init() {
		Section section = null;
		section = realm.where(Section.class).equalTo("id_s",section_id).findFirst();
		//appController.getSectionsDao().queryForId(section_id);
		if (section != null)
		{
			titleInStrip = section.getTitle();
			//RssService service = new RssService(new RSSFragment());
			RssService service = new RssService(RSSFragment.this);

			if (section.getRoot_url() != null && !section.getRoot_url().isEmpty()) {
				service.execute(section.getRoot_url());
			}
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		boolean readd = true;
		View view = inflater.inflate(R.layout.rss_list, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		view.findViewById(R.id.llSVFeedChoicesHolder).setVisibility(View.GONE);
		listRSS = (ListView)view.findViewById(R.id.ListRSS);
		if (adapter == null || adapter.isEmpty()) {
			init();
			readd = false;
		}
		listRSS.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Toast.makeText(getActivity(), "click finally :"+ arg2, Toast.LENGTH_SHORT).show();
				
			}
		});
		
		//Add Header view to show the category title!
		if (titleInStrip!=null && !titleInStrip.isEmpty()) {

			View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
			viewTitle.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
			TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
			titleContactsTV.setText(titleInStrip);
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			viewTitle.setClickable(false);
			listRSS.addHeaderView(viewTitle, null, false);
			IS_HEADER_ADDED = 1;

		}else {
			IS_HEADER_ADDED = 0;
		}
		
		listRSS.setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		listRSS.setDividerHeight(1);
		if(readd) {
			listRSS.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

		return view;
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
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}


}
