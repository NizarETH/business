/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.AlbumsAdapter;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.GridFragment;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class AlbumsGridFragment extends GridFragment {

    private Colors colors;
    private List<Album> albums;
    private long time;
    private int id;
    Realm realm;
    public static boolean isTablet;

    /**
     *
     */
    public AlbumsGridFragment() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.euphor.paperpad.widgets.GridFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_grid_fragment, container, false);
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

        int section_id = getArguments().getInt("Section_id");
        Section section = realm.where(Section.class).equalTo("id_s",section_id).findFirst();
        //appController.getSectionsDao().queryForId(section_id);
        if (section != null) {
            TextView titleContactsTV = (TextView)view.findViewById(R.id.TitleTV);
            titleContactsTV.setTypeface(MainActivity.FONT_TITLE);

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int txtSize = 20;
            if(metrics.densityDpi >= 213 ){

                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    txtSize = 26;
                } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    txtSize = 20;
                }


            }
            else{
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    txtSize = 30;
                } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    txtSize = 26;
                }
            }
            titleContactsTV.setTextSize(txtSize);

            if (section.getTitle().isEmpty()) {
                //					view.findViewById(R.id.TitleHolder).setVisibility(View.GONE);
                view.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color()));

                titleContactsTV.setText(getActivity().getResources().getString(R.string.galeryPhotos));
                titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));

            }else {
                view.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color()));
                titleContactsTV.setText(section.getTitle());
                titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
            }
            /*if(isTablet)*/
             addImageForList(view, section);

        }

        return view;
    }

    /* (non-Javadoc)
     * @see com.euphor.paperpad.widgets.GridFragment#setGridAdapter(android.widget.ListAdapter)
     */
    @Override
    public void setGridAdapter(ListAdapter adapter) {
        // TODO Auto-generated method stub
        super.setGridAdapter(adapter);
    }

    private void addImageForList(View view, Object obj) {
        ImageView imageCat = (ImageView) view.findViewById(R.id.image_category);
        Illustration illustration = null;
        if (obj instanceof Category) {
            illustration = ((Category)obj).getIllustration();
        }else if (obj instanceof Section){
            illustration = ((Section)obj).getIllustrationObj();
        }
        if (illustration != null && !illustration.getLink().isEmpty()) {
            if (!illustration.getPath().isEmpty()) {
                Glide.with(getActivity()).load(new File(illustration.getPath())).dontAnimate().into(imageCat);
            }else {
                Glide.with(getActivity()).load(illustration.getLink()).dontAnimate().into(imageCat);
            }
        }else {
            view.findViewById(R.id.image_category_container).setVisibility(View.GONE);//.setBackgroundColor(colors.getColor(colors.getForeground_color(), "AA"));
        }
         if(!isTablet){
            view.findViewById(R.id.image_category_container).setVisibility(View.GONE);
        }

    }
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        realm = Realm.getInstance(getActivity());
       //new AppController(getActivity());
        colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

        time = System.currentTimeMillis();

        if (((MainActivity) getActivity()).bodyFragment == null) {
            ((MainActivity) getActivity()).bodyFragment = "AlbumsGridFragment";
        }
        if (((MainActivity) getActivity()).extras == null) {
            int Section_id = getArguments().getInt("Section_id");
            ((MainActivity) getActivity()).extras = new Bundle();
            ((MainActivity) getActivity()).extras.putInt("Section_id", Section_id);
        }
        isTablet = Utils.isTablet(getActivity());
        super.onAttach(activity);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        albums = new ArrayList<Album>();
        albums = realm.where(Album.class).findAll();
        //albums = appController.getAlbumDao().queryForAll();

        if(albums.size() == 1) {
            GaleryFragment galeryFragment = new GaleryFragment();
            ((MainActivity)getActivity()).bodyFragment = "AlbumsGridFragment";
            ((MainActivity)getActivity()).extras.putInt("Album_id", albums.get(0).getId_album());
            galeryFragment.setArguments(((MainActivity)getActivity()).extras);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();
        }else {
            AlbumsAdapter albumsAdapter = new AlbumsAdapter(albums , getActivity(), colors);
            setGridAdapter(albumsAdapter);
        }

    }

    /* (non-Javadoc)
     * @see com.euphor.paperpad.widgets.GridFragment#onGridItemClick(android.widget.GridView, android.view.View, int, long)
     */
    @Override
    public void onGridItemClick(GridView g, View v, int position, long id) {
        GaleryFragment galeryFragment = new GaleryFragment();
        ((MainActivity)getActivity()).bodyFragment = "AlbumsGridFragment";
        ((MainActivity)getActivity()).extras.putInt("Album_id", albums.get(position).getId_album());
        galeryFragment.setArguments(((MainActivity)getActivity()).extras);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();
        super.onGridItemClick(g, v, position, id);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_album", id);
        ((MyApplication)getActivity().getApplication()).hits.add(hit);
        super.onStop();
    }


}
