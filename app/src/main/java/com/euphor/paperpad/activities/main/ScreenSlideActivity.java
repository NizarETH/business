/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.euphor.paperpad.activities.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Photo;


import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;


/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
public class ScreenSlideActivity extends FragmentActivity {
	


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

	public int id_illustration;

	private int id_album;

    private Realm realm;
	private Album album;

	private ArrayList<Illustration> illustrations;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        getActionBar().hide();
        realm = Realm.getInstance(getApplicationContext());
        Bundle receive = getIntent().getExtras();
        id_album= receive.getInt("id_album");
        id_illustration = receive.getInt("id_illustration");

		album = realm.where(Album.class).equalTo("id_album",id_album).findFirst();
		//appController.getAlbumDao().queryForId(id_album);

        ImageView close_picture = (ImageView) findViewById(R.id.close_picture);
        close_picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });


        illustrations = new ArrayList<Illustration>();
        for (Iterator<Photo> iterator = album.getPhotos().iterator(); iterator.hasNext();) {
			Photo photo = (Photo) iterator.next();
			//if (photo.getIllustration().getOriginalHeight()>0) {
				illustrations.add(photo.getIllustration());
			//}
			
		}

        Illustration item2 = null;
        item2 =  realm.where(Illustration.class).equalTo("id_i",id_illustration ).findFirst();
        //appController.getIllustrationDao().queryForId(id_illustration);
        makeItemFirst(item2);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
//        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
//        mIndicator.setViewPager(mPager);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
    }


    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

		public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(illustrations.get(position),id_album);
        }

        @Override
        public int getCount() {
            return illustrations.size();
        }
    }
    
    public void makeItemFirst(Illustration item2) {
    	
		if (item2 != null) {
			for (int i = 0; i < illustrations.size(); i++) {
				if (item2.getId_i() == illustrations.get(i).getId_i()) {
					illustrations.remove(i);
					illustrations.add(0, item2);
					break;
				}
			}
		}
	}
    
}
