/**
 * 
 */
package com.euphor.paperpad.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.activities.main.MainActivity;

import com.euphor.paperpad.utils.Colors;

import com.nostra13.universalimageloader.core.ImageLoader;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class PPBaseFragment extends Fragment {

	public ImageLoader imageLoader;

	public Colors colors;
	public Activity activity;
    public Realm realm;

    /**
	 * 
	 */
	public PPBaseFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
//		init();
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		realm = Realm.getInstance(getActivity());
//		init();
		return super.onCreateView(inflater, container, savedInstanceState);
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
//		freeMemory();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
//		freeMemory();
		super.onStop();
	}
	
	/**Let us initialize all required instances like the ImageLoader, Colors
	 * Database accesses 
	 */
	public void init() {
//		((MainActivity)activity).initImageLoader();


		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
	}
	
	/**Let us free device memory from all heavy instances like the ImageLoader,
	 * Database accesses 
	 */
	public void freeMemory() {
		if (realm != null) {
		/*	OpenHelperManager.releaseHelper();*/
			realm = null;
		}
		
		if (imageLoader != null && imageLoader.isInited()) {
//			imageLoader.clearMemoryCache();
//			imageLoader.destroy();
//			imageLoader = null;
		}
		if (imageLoader != null ) {
//			imageLoader.destroy();
//			imageLoader = null;
		}
		if (colors != null) {
			colors = null; 
		}
		Runtime.getRuntime().gc();
	}

}
