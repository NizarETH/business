/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.euphor.paperpad.activities.fragments.SwipePage;

import java.util.List;

/**
 * @author euphordev02
 *
 */
public class swipeAdapter extends FragmentPagerAdapter {

	private List<SwipePage> fragments;

	public swipeAdapter(FragmentManager fm, List<SwipePage> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
