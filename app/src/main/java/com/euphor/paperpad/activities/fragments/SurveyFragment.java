/**
 * 
 */
package com.euphor.paperpad.activities.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.SurveyFragmentsAdapter;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.NonSwipeableViewPager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class SurveyFragment extends Fragment {


	private Colors colors;
	private boolean isTablet;
	private int section_id;
	private Section surveySection;
	private SurveyFragmentsAdapter adapter;
	private NonSwipeableViewPager pager;
    public Realm realm;

    /**
	 * 
	 */
	public SurveyFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unused")
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View result=inflater.inflate(R.layout.survey_layout, container, false);
		
		if (surveySection != null) {
			result.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
			TextView titleContactsTV = (TextView)result.findViewById(R.id.TitleTV);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			if(getArguments().getString("title") != null){
				titleContactsTV.setText(getArguments().getString("title"));
			}
			else if (!surveySection.getTitle().isEmpty()) {
				titleContactsTV.setText(surveySection.getTitle());
			}else if (!surveySection.getName().isEmpty()) {
				titleContactsTV.setText(surveySection.getName());
			}
			else {
				result.findViewById(R.id.TitleHolder).setVisibility(View.GONE);
								
			}
		}
		
		if (isTablet) {

		}else {
			result.findViewById(R.id.surveyImg).setVisibility(View.GONE);
		}
		pager=(NonSwipeableViewPager)result.findViewById(R.id.pager);
		
		int id_survey = 0;
		if (surveySection.getSurveys() != null && surveySection.getSurveys().size()>0) {
			id_survey = surveySection.getSurveys().iterator().next().getId();
		}
//		SurveySubmitFragment sSubmitFragment = SurveySubmitFragment.newInstance(id_survey, pager, section_id, SurveyFragment.this);
		SurveySubmitFragment sSubmitFragment = new SurveySubmitFragment();
		sSubmitFragment.setId_survey(id_survey);
		sSubmitFragment.setPager(pager);
		sSubmitFragment.setSurveyFragment(SurveyFragment.this);
//		SurveyPageFragment sPageFragment = SurveyPageFragment.newInstance(1, pager, id_survey, SurveyFragment.this);
		SurveyPageFragment sPageFragment = new SurveyPageFragment();
		sPageFragment.setId_survey(id_survey);
		sPageFragment.setPager(pager);
		sPageFragment.setSurveyFragment(SurveyFragment.this);
		SurveyThanksFragment thanksFragment = SurveyThanksFragment.newInstance();
		adapter = new SurveyFragmentsAdapter(getActivity(), getChildFragmentManager(), sPageFragment, sSubmitFragment, thanksFragment);
		pager.setAdapter(adapter);
		if (result.findViewById(R.id.surveyImg) != null) {
			ImageView imageSurvey = (ImageView) result.findViewById(R.id.surveyImg);
			Illustration illustration = surveySection.getIllustrationObj();
			try {
				if (illustration != null) {
					if (!illustration.getPath().isEmpty()) {
						Glide.with(getActivity()).load(new File(illustration.getPath())).into(imageSurvey);
					}else {
						Glide.with(getActivity()).load(illustration.getLink()).into(imageSurvey);
					}
					
				}
				else {
					imageSurvey.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				result.findViewById(R.id.surveyImg).setVisibility(View.GONE);
				e.printStackTrace();
			}
		}
		

		return(result);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
	//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

        ((MainActivity)getActivity()).bodyFragment = "SurveyFragment";
        isTablet = Utils.isTablet(activity);
		
		List<Section> sections = new ArrayList<Section>();
        sections = realm.where(Section.class).equalTo("type", "survey").findAll();
        //appController.getSectionsDao().queryForEq("type", "survey");
        if (sections.size()>0) {
			surveySection = sections.get(0)!= null ?sections.get(0) : null;
			section_id = sections.get(0)!= null ?sections.get(0).getId() : 0;
		}
			
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);

	}

	
	public Fragment findFragmentByPosition(int position) {
		SurveyFragmentsAdapter fragmentPagerAdapter = adapter;
	    return getChildFragmentManager().findFragmentByTag(
	            "android:switcher:" + pager.getId() + ":"
	                    + fragmentPagerAdapter.getItemId(position));
	}
	

}
