package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.interactiveMap.map;
import com.euphor.paperpad.Beans.interactiveMap.Point;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.EUInteractiveMapController;
import com.euphor.paperpad.widgets.zoomableview.ZoomViewTouchable;
import com.google.android.gms.maps.model.LatLng;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Euphor on 24/11/14.
 */
public class InteractiveMapFragment extends Fragment {


    private List<map> maps;
    static ZoomViewTouchable mapImage;

    private Colors colors;
    public Realm realm;
    private View view;

    public static InteractiveMapFragment create(List<map> maps){
        InteractiveMapFragment interactiveMapFragment = new InteractiveMapFragment();
        interactiveMapFragment.setMaps(maps);
        return interactiveMapFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        		realm = Realm.getInstance(getActivity());
   //new AppController(getActivity());
        colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.interactive_map, container, false);
        view.setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.90f));
        mapImage = (ZoomViewTouchable) view.findViewById(R.id.zoomable);
/*        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(480, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mapImage.setLayoutParams(params);*/

        //zoom_view = (ZoomView_) findViewById(R.id.zoom_view);
        //mapImage.setBackgroundResource(R.drawable.plan);
        /*maps=realm.where(map.class).findAll();*/

        if(maps.size() > 0) {


            String path = "http";
            final Illustration illustration = maps.get(0).getIllustration();

            /*Callback callback = new Callback() {
                @Override
                public void onSuccess() {


                    List<Point> pointsList = new ArrayList<Point>(maps.get(0).getPoints());



                    RelativeLayout relativeContainer = (RelativeLayout) view.findViewById(R.id.relativeContainer);
                    EUInteractiveMapController euInteractiveMapController = new EUInteractiveMapController(relativeContainer,
                            mapImage, pointsList, new LatLng(maps.get(0).getOrigin().getLatitude(), maps.get(0).getOrigin().getLongitude()),
                            (maps.get(0).getDistance().getHorizontal() == 0) ? 250 : maps.get(0).getDistance().getHorizontal(), Color.BLUE);
                    euInteractiveMapController.illustration = illustration;
                    euInteractiveMapController.startMonitoringUserLocation();


                    mapImage.setInteractiveMap(euInteractiveMapController);
                    mapImage.setMap();


                }

                @Override
                public void onError() {

                }
            };*/




            if(illustration != null){
                /*SimpleTarget target = new SimpleTarget<Bitmap>(illustration.getOriginalWidth(), illustration.getOriginalHeight()) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // do something with the bitmap
                        // for demonstration purposes, let's just set it to an ImageView

                        mapImage.setImageBitmap( bitmap );

                        Illustration illustration = maps.get(0).getIllustration();
                        List<Point> pointsList = new ArrayList<Point>(maps.get(0).getPoints());



                        RelativeLayout relativeContainer = (RelativeLayout) view.findViewById(R.id.relativeContainer);
                        EUInteractiveMapController euInteractiveMapController = new EUInteractiveMapController(relativeContainer,
                                mapImage, pointsList, new LatLng(maps.get(0).getOrigin().getLatitude(), maps.get(0).getOrigin().getLongitude()),
                                (maps.get(0).getDistance().getHorizontal() == 0) ? 250 : maps.get(0).getDistance().getHorizontal(), Color.BLUE);
                        euInteractiveMapController.illustration = illustration;
                        euInteractiveMapController.startMonitoringUserLocation();


                        mapImage.setInteractiveMap(euInteractiveMapController);
                        mapImage.setMap();
                        mapImage.invalidate();
                    }
                };*/
                RequestListener requestListener = new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        List<Point> pointsList = new ArrayList<Point>(maps.get(0).getPoints());

//mapImage.setImageBitmap(((BitmapDrawable)resource.getCurrent()).getBitmap());
                        Bitmap bitmap = ((GlideBitmapDrawable) resource).getBitmap();
                        if (bitmap != null) {
                            mapImage.setImageBitmap(bitmap);
                            Illustration illustration = maps.get(0).getIllustration();

                            RelativeLayout relativeContainer = (RelativeLayout) view.findViewById(R.id.relativeContainer);
                            EUInteractiveMapController euInteractiveMapController = new EUInteractiveMapController(relativeContainer,
                                    mapImage, pointsList, new LatLng(maps.get(0).getOrigin().getLatitude(), maps.get(0).getOrigin().getLongitude()),
                                    (maps.get(0).getDistance().getHorizontal() == 0) ? 250 : maps.get(0).getDistance().getHorizontal(), Color.BLUE);
                            euInteractiveMapController.illustration = illustration;

                            euInteractiveMapController.startMonitoringUserLocation(bitmap);


                            mapImage.setInteractiveMap(euInteractiveMapController);
                            mapImage.setMap();
                        }

                        return false;
                    }
                };
                if(illustration.getPath().isEmpty()){
                    path = (illustration.getLink().isEmpty()) ? "http" : illustration.getLink();
                    Glide.with(getActivity())
                            .load(path)
                            .listener(requestListener)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(mapImage);//.into(target);//mapImage, callback);
                }else{
                    path = illustration.getPath();
                    Glide.with(getActivity())
                            .load(new File(path))
                            .listener(requestListener)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(mapImage)/*.asBitmap().into(target)*/;//mapImage, callback);
                }
            }
        }





        return view;
    }


    public List<map> getMaps() {
        return maps;
    }

    public void setMaps(List<map> maps) {
        this.maps = maps;
    }
}
