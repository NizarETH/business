/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import java.util.List;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Radio;

import com.euphor.paperpad.utils.Colors;

import android.app.Activity;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class RadioPlayerFragment extends Fragment {

	private int radio_id;

	private Colors colors;
	private ImageView img_play_pause;
	private ImageView img_stop;
	List<Radio> radios;
    public Realm realm;


    /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
				realm = Realm.getInstance(getActivity());
		if (getArguments() != null) {
			radio_id = getArguments().getInt("radio_id");
		}

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
		View view = inflater.inflate(R.layout.radio_player, container, false);
		TextView txt_radio_name = (TextView)view.findViewById(R.id.txt_radio_name);
		txt_radio_name.setTypeface(MainActivity.FONT_TITLE);
		img_play_pause = (ImageView)view.findViewById(R.id.img_play_pause);
		img_play_pause.getDrawable().setColorFilter(colors.getColor(colors.getBackground_color()), Mode.MULTIPLY);
		img_stop = (ImageView)view.findViewById(R.id.img_stop);
		img_stop.getDrawable().setColorFilter(colors.getColor(colors.getBackground_color()), Mode.MULTIPLY);
		view.setBackgroundColor(colors.getColor(colors.getTitle_color()));
		Radio radio = null;
        radio = realm.where(Radio.class).equalTo("id",radio_id).findFirst();
        //controller.getRadioDao().queryForId(radio_id);
        if (radio != null) {
			
			ImageView radioImg = (ImageView)view.findViewById(R.id.radioImg);
			radioImg.getDrawable().setColorFilter(colors.getColor(colors.getTabs_background_color()), Mode.MULTIPLY);
//			Glide.with(getActivity()).load(radio.getCover()).into(radioImg);
			txt_radio_name.setText(radio.getTitle());
			txt_radio_name.setTextColor(colors.getColor(colors.getBackground_color()));
			img_play_pause.setTag(radio);
			img_play_pause.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RadiosSectionFragment fragment = (RadiosSectionFragment) getActivity().getSupportFragmentManager().findFragmentByTag("RadiosSectionFragment");
					
					if (((MainActivity)getActivity()).player.isPlaying()) {
						((MainActivity)getActivity()).pausePlaying();
						((ImageView)v).setImageResource(R.drawable.ic_av_play);
						if (fragment != null) {
							fragment.selectRadio(radio_id, false);
						}
					}else {
						((MainActivity)getActivity()).resumePlaying();
						((ImageView)v).setImageResource(R.drawable.ic_av_pause);
						if (fragment != null) {
							fragment.selectRadio(radio_id, true);
						}
					}
					
					
				}
			});
			
			img_stop.setTag(radio);
			img_stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RadioPlayerFragment fragment = (RadioPlayerFragment) getActivity().getSupportFragmentManager().findFragmentByTag("RadioPlayerFragment");
					if (fragment != null) {
						((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().remove(fragment).commit();
						((MainActivity)getActivity()).findViewById(R.id.ff_radio_fragment).setVisibility(View.GONE);
						((MainActivity)getActivity()).stopPlaying();
					}
					RadiosSectionFragment fragment2 = (RadiosSectionFragment) getActivity().getSupportFragmentManager().findFragmentByTag("RadiosSectionFragment");
					if (fragment2 != null) {
						fragment2.refreshListRadios();
					}


				}
			});
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
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public RadioPlayerFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void pausePlay(boolean play){
		if (play) {

			if (((MainActivity)getActivity()).player.isPlaying()) {
			}else {
				((MainActivity)getActivity()).resumePlaying();
				
			}
			img_play_pause.setImageResource(R.drawable.ic_av_pause);
			
		
			
		}else {
			((MainActivity)getActivity()).pausePlaying();
			img_play_pause.setImageResource(R.drawable.ic_av_play);
		}
	}

	/**
	 * @return the radios
	 */
	public List<Radio> getRadios() {
		return radios;
	}

	/**
	 * @param radios the radios to set
	 */
	public void setRadios(List<Radio> radios) {
		this.radios = radios;
	}

}
