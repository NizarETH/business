/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.euphor.paperpad.adapters;

import java.util.Iterator;
import java.util.List;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.RadioPlayerFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Radio;
import com.euphor.paperpad.utils.Colors;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;
import io.realm.Realm;

public class RadioCoverFlowAdapter extends FancyCoverFlowAdapter {

    // =============================================================================
    // Private members
    // =============================================================================
	private Colors colors;
	MainActivity mainActivity;
	List<Radio> radios;
	boolean toggle;
    private Realm realm;

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    public int getCount() {
        return radios.size();
    }

    @Override
    public Radio getItem(int i) {
        return radios.get(i); 
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {

    	if(mainActivity !=null)
			realm = Realm.getInstance(mainActivity);

		realm.beginTransaction();
    	LayoutInflater inflater = (LayoutInflater)mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	Radio radio = radios.get(i);
    	final View radio_view = inflater.inflate(R.layout.radio_layout, viewGroup, false);
    	final LinearLayout cadre_interieur  = (LinearLayout)radio_view.findViewById(R.id.cadre_interieur);
    	RelativeLayout vue_contenu = (RelativeLayout)radio_view.findViewById(R.id.vue_contenu);
    	vue_contenu.setBackgroundColor(colors.getColor(colors.getBackground_color(), "88"));
    	vue_contenu.setTag(radio);
		ImageView radio_img = (ImageView) radio_view.findViewById(R.id.radio_img);
		TextView radio_name = (TextView)radio_view.findViewById(R.id.radio_name);
		Glide.with(mainActivity).load(radio.getCover()).into(radio_img);
		radio_name.setText(radio.getTitle());
		final ImageView play_stop = (ImageView) radio_view.findViewById(R.id.play_stop);
		play_stop.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
		play_stop.setImageDrawable(play_stop.getDrawable());
		radio_name.setTextColor(colors.getColor(colors.getTitle_color()));
		if (radio.isSelected()) {
			vue_contenu.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			for (Iterator<Radio> iterator = radios.iterator(); iterator.hasNext();) {
				Radio r = (Radio) iterator.next();
				r.setSelected(false);
			}
			radio.setSelected(true);
			play_stop.setVisibility(View.VISIBLE); 
			radio_view.setBackgroundColor(colors.getColor(colors.getForeground_color()));
			cadre_interieur.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		}else {
			play_stop.setVisibility(View.GONE);
		}
		
		if (!radio.isPlaying()) {
			Drawable drawable = mainActivity.getResources().getDrawable(R.drawable.play_sound);
			drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
			play_stop.setImageDrawable(drawable);
		}else {
			Drawable drawable = mainActivity.getResources().getDrawable(R.drawable.pause_sound);
			drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
			play_stop.setImageDrawable(drawable);
		}
		
		vue_contenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                realm.beginTransaction();
				Radio radio = (Radio) v.getTag();
				((MainActivity)mainActivity.getActivity()).initializeMediaPlayer(radio.getUrl());
//				if (true) {
					
				mainActivity.startPlaying(radios, radio, (ProgressBar)v.findViewById(R.id.progressBar1), RadioCoverFlowAdapter.this);
//				} else {
//					((MainActivity)getActivity()).stopPlaying();
//				}
				
				try {
					for (Iterator<Radio> iterator = radios.iterator(); iterator.hasNext();) {
						Radio r = (Radio) iterator.next();
						r.setSelected(false);
					}
					radio.setSelected(true);
					radio_view.setBackgroundColor(colors.getColor(colors.getForeground_color()));
					cadre_interieur.setBackgroundColor(colors.getColor(colors.getBackground_color()));
//					RadioCoverFlowAdapter.this.notifyDataSetChanged();	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				v.findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
				play_stop.setVisibility(View.VISIBLE);
//				notifyDataSetChanged();
                realm.commitTransaction();

			}
		});
       /* ImageView imageView = null;

        if (reuseableView != null) {
            imageView = (ImageView) reuseableView;
        } else {
            imageView = new ImageView(viewGroup.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(300, 400));

        }

        imageView.setImageResource(this.getItem(i));*/
		toggle = false;
		play_stop.setTag(radio);
		play_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (toggle) {
					Drawable drawable = mainActivity.getResources().getDrawable(R.drawable.play_sound);
					drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
					((ImageView)v).setImageDrawable(drawable);
					toggle = false;
				}else {
					Drawable drawable = mainActivity.getResources().getDrawable(R.drawable.pause_sound);
					drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
					((ImageView)v).setImageDrawable(drawable);
					toggle = true;
				}
				
				RadioPlayerFragment fragment = (RadioPlayerFragment) mainActivity.getSupportFragmentManager().findFragmentByTag("RadioPlayerFragment");
				if (fragment == null) {
					Radio radio = (Radio) v.getTag();
					((MainActivity)mainActivity.getActivity()).initializeMediaPlayer(radio.getUrl());
//					if (true) {
						
					mainActivity.startPlaying(radios, radio, (ProgressBar)v.findViewById(R.id.progressBar1), RadioCoverFlowAdapter.this);
//					} else {
//						((MainActivity)getActivity()).stopPlaying();
//					}
					
					try {
						for (Iterator<Radio> iterator = radios.iterator(); iterator.hasNext();) {
							Radio r = (Radio) iterator.next();
							r.setSelected(false);
						}
						radio.setSelected(true);
						radio_view.setBackgroundColor(colors.getColor(colors.getForeground_color()));
						cadre_interieur.setBackgroundColor(colors.getColor(colors.getBackground_color()));
//						RadioCoverFlowAdapter.this.notifyDataSetChanged();	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					fragment.pausePlay(toggle);
				}
				
			}
		});
		radio_view.setFocusable(false);
        realm.commitTransaction();
        return radio_view;
    }

	public RadioCoverFlowAdapter(MainActivity act, List<Radio> radios, Colors colors) {
		super();
		this.mainActivity = act;
		this.radios = radios;
		this.colors = colors;
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
