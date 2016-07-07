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


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Illustration;

import com.euphor.paperpad.widgets.TouchImageView;


import java.io.File;
import java.sql.SQLException;

import io.realm.Realm;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link } and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class ScreenSlidePageFragment extends android.support.v4.app.Fragment {

   private Illustration illustration;
   private Realm realm;


    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     * @param illustration
     * @param id_album
     */
    public static ScreenSlidePageFragment create(Illustration illustration, int id_album) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("id_illustration", illustration.getId_i());
        args.putInt("id_album", id_album);
        fragment.setArguments(args);
        return fragment;
    }
    


    public ScreenSlidePageFragment() {
    	
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        		realm = Realm.getInstance(getActivity());
        int id_illustration = getArguments().getInt("id_illustration");
        illustration = realm.where(Illustration.class).equalTo("id_i",id_illustration).findFirst();


    }


    //    ViewGroup rootView;
//    ImageView imag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        if(illustration == null)return  rootView;

        /// construct the view
        final ImageView imag = (TouchImageView)rootView.findViewById(R.id.Image);

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
		/*if(!Glide.isSetup()){
			GlideBuilder builder =  new GlideBuilder(getActivity());
			builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
			Glide.setup(builder);
		}*/
        if (isFile) {


            Glide.with(getActivity()).load(new File(path)).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imag.setImageBitmap(resource);
                }
            });
        }else {
            Glide.with(getActivity()).load(path).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imag.setImageBitmap(resource);
                }
            });
        }



        return rootView;
    }



    /**
     * @return the illustration
     */
    public Illustration getIllustration() {
        return illustration;
    }



    /**
     * @param illustration the illustration to set
     */
    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

}
