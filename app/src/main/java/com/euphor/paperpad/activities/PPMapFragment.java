/**
 * 
 */
package com.euphor.paperpad.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.euphor.paperpad.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author euphordev02
 *
 */
public class PPMapFragment extends Fragment {

	/**
	 * 
	 */
	public PPMapFragment() {
		// TODO Auto-generated constructor stub
	}


		private SupportMapFragment fragment;
		private GoogleMap map;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.map_fragment, container, false);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
	/*		FragmentManager fm = getFragmentManager();
			fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
			if (fragment == null) {
				fragment = SupportMapFragment.newInstance();
				fm.beginTransaction().replace(R.id.map, fragment).commit();
			}*/
		}
		
		@Override
		public void onResume() {
			super.onResume();
			if (map == null) {
				map = fragment.getMap();
				map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
			}
		}
	

}
