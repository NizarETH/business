/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import io.realm.Realm;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MainActivity.RadioSectionCallback;
import com.euphor.paperpad.adapters.RadioCoverFlowAdapter;
import com.euphor.paperpad.Beans.Radio;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;


/**
 * @author euphordev02
 *
 */
public class RadiosSectionFragment extends Fragment implements RadioSectionCallback{

	private int id;

	private Section section;
	protected boolean play;
	private RadioCoverFlowAdapter adapter;
	private Colors colors;
	List<Radio> radios = new ArrayList<Radio>();
    public Realm realm;

	/**
	 * 
	 */
	public RadiosSectionFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
				realm = Realm.getInstance(getActivity());
		if (getArguments() != null) {
			id = getArguments().getInt("Section_id");
		}
		;//new AppController(getActivity());

        section = realm.where(Section.class).equalTo("id",id).findFirst();
        //appController.getSectionsDao().queryForId(id);
        colors = ((MainActivity)activity).colors;

        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
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

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.radios_section_fragment, container, false);
		ImageView img_back_radio = (ImageView)view.findViewById(R.id.img_back_radio);
      /*fcf:maxRotation="45"
        fcf:scaleDownGravity="0.5"
        fcf:unselectedAlpha="0.5"
        fcf:unselectedSaturation="0.0"
        fcf:unselectedScale="0.4"*/

		FancyCoverFlow fancyCoverFlow = new FancyCoverFlow(getActivity());/*(FancyCoverFlow)view.findViewById(R.id.fancyCoverFlow);*/
        LinearLayout.LayoutParams parames = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

         fancyCoverFlow.setLayoutParams(parames);

       fancyCoverFlow.setUnselectedAlpha(1.0f);
       fancyCoverFlow.setUnselectedSaturation(0.0f);
       fancyCoverFlow.setUnselectedScale(0.5f);
       fancyCoverFlow.setSpacing(50);
      fancyCoverFlow.setMaxRotation(0);
        fancyCoverFlow.setScaleDownGravity(0.2f);
        fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
       /* fancyCoverFlow.setMaxRotation(45);
        fancyCoverFlow.setSelected(true);
        fancyCoverFlow.setScaleDownGravity(0.5f);
        fancyCoverFlow.setUnselectedAlpha(0.5f);
        fancyCoverFlow.setUnselectedSaturation(0.0f);
        fancyCoverFlow.setUnselectedScale(0.4f);*/
        LinearLayout radio_container = (LinearLayout) view.findViewById(R.id.radio_container);
		if (section != null && section.getRadios().size()>0) {
			if (section.getIllustrationObj() != null) {
				try {
					if (!section.getIllustrationObj().getPath().isEmpty()) {
						Glide.with(getActivity()).load(new File(section.getIllustrationObj().getPath())).into(img_back_radio);
					}else {
						Glide.with(getActivity()).load(section.getIllustrationObj().getLink()).into(img_back_radio);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}


            radio_container.addView(fancyCoverFlow, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

			radios.addAll(section.getRadios());
			adapter = new RadioCoverFlowAdapter(((MainActivity)getActivity()), radios, colors);
			fancyCoverFlow.setAdapter(adapter);
		}
		return view;
	}

	@Override
	public void showProgress(boolean show) {
//		if (show) {
//			progressBar1.setVisibility(View.VISIBLE);
//			play_stop.setVisibility(View.VISIBLE);
//		}else {
//			progressBar1.setVisibility(View.GONE);
//			play_stop.setVisibility(View.GONE);
//		}
		
		
	}
	
	public void refreshListRadios(){
		try {
			for (Iterator<Radio> iterator = radios.iterator(); iterator.hasNext();) {
				Radio radio = (Radio) iterator.next();
				radio.setSelected(false);
			}
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void selectRadio(int id, boolean play){
		try {
			for (Iterator<Radio> iterator = radios.iterator(); iterator.hasNext();) {
				Radio radio = (Radio) iterator.next();
				if (radio.getId() == id) {
					if (play) {
						radio.setPlaying(true);
					}else {
						radio.setPlaying(false);
					}
					
				}
				
			}
			
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
