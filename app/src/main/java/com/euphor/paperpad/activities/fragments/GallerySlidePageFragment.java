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

package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Illustration;

import com.euphor.paperpad.widgets.TouchImageView;
//

import java.io.File;

import io.realm.Realm;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link } and {@link
 * com.euphor.paperpad.activities.main.ScreenSlideActivity} samples.</p>
 */
public class GallerySlidePageFragment extends android.support.v4.app.Fragment {

	public static Typeface FONT_REGULAR;
	public static Typeface FONT_BOLD;
	public static Typeface FONT_TITLE;
	public Bitmap bm ;
	private static int LANGUAGE_ID = 2;
	public static Context context;
	public static Activity activity;
  public Realm realm;
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
	private int id_illustration;
	private int id_album;

	private Album album;
	private Illustration illustration;



    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     * @param screenSlideActivity
     */
    public static GallerySlidePageFragment create(Illustration illustration, int id_album, Context ctx, Activity screenSlideActivity) {
    	context = ctx;
        activity = screenSlideActivity;
        GallerySlidePageFragment fragment = new GallerySlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("id_illustration", illustration.getId_i());
        args.putInt("id_album", id_album);
        fragment.setArguments(args);
        return fragment;
    }



    public GallerySlidePageFragment() {
    	
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_illustration = getArguments().getInt("id_illustration");
        id_album = getArguments().getInt("id_album");
        		realm = Realm.getInstance(getActivity());

        album = realm.where(Album.class).equalTo("id_album",id_album).findFirst();

        //appController.getAlbumDao().queryForId(id_album);
        illustration = realm.where(Illustration.class).equalTo("id_i",id_illustration).findFirst();
        //appController.getIllustrationDao().queryForId(id_illustration);


    }

    
//    ViewGroup rootView;
//    ImageView imag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
    	View rootView = inflater
                .inflate(R.layout.fragment_gallery_slide_page, container, false);


        // Set the title view to show the page number.
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                getString(R.string.title_template_step, mPageNumber + 1));
        
        /// construct the view 
    	final ImageView imag = (TouchImageView)rootView.findViewById(R.id.Image);
    	//rootView.findViewById(R.id.container_galerry_img).setPadding(10, 0, 10, 0);
        
//        imag.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				///rootView.findViewById(R.id.container_galerry_img).setPadding(10, 10, 10, 10);
//				Log.e(" OnClickListener : ", " view "+v);
//				imag.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//			
//			}
//		});
        String path = "";
        boolean isFile = true;
        if (!illustration.getFullPath().isEmpty()) {
//        	path = "file:///"+illustration.getFullPath();
        	path = illustration.getFullPath();
		}else if (!illustration.getPath().isEmpty()) {
//			path = "file:///"+illustration.getPath();
			path = illustration.getPath();
		}else if (!illustration.getLink().isEmpty()) {
			path = illustration.getLink();
			isFile = false;
		}else if (!illustration.getFullLink().isEmpty()) {
			path = illustration.getFullLink();
			isFile = false;
		}
        if (isFile) {
        	Glide.with(getActivity()).load(new File(path)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imag.setImageBitmap(resource);
                }
            });
		}else {
			Glide.with(getActivity()).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imag.setImageBitmap(resource);
                }
            });
		}
        
//		imageLoader.displayImage(path , imag);
        
		
        /* TextView txt = (TextView)rootView.findViewById(R.id.titleItem);
        txt.setText(str);
        
       	ImageView closeImg = (ImageView)rootView.findViewById(R.id.unzoom);
        closeImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});*/
        

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
