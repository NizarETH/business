package com.euphor.paperpad.activities.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.euphor.paperpad.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

/**
 * Created by Euphor on 23/02/15.
 */
public class StreetViewFragment extends SupportStreetViewPanoramaFragment implements OnStreetViewPanoramaReadyCallback, GoogleMap.OnMarkerDragListener,StreetViewPanorama.OnStreetViewPanoramaChangeListener {



    private LatLng position;
    private float bearing;

    //SupportStreetViewPanoramaFragment streetViewPanoramaFragment;
    private GoogleMap mMap;
    private Marker marker;
    private StreetViewPanorama svp;

    public static final StreetViewFragment newInstance(double latitude, double longitude, float bearing){
        Bundle args = new Bundle();
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);

        StreetViewFragment fragment =  new StreetViewFragment();
        fragment.setPosition(new LatLng(latitude, longitude));
        fragment.setBearing(bearing);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*View fragmentView = null;
        try {
            fragmentView = inflater.inflate(R.layout.split_street_view_fragment, container, false);//super.onCreateView(inflater, container, savedInstance);
            return fragmentView;
        }catch (InflateException e){
            Log.e(StreetViewFragment.class.getSimpleName(), " "+e.getMessage());
        }finally {
            return super.onCreateView(inflater, container, savedInstanceState);
        }*/
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*SupportStreetViewPanoramaFragment streetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getChildFragmentManager().findFragmentById(R.id.streetviewpanorama);
        if(streetViewPanoramaFragment != null)
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);*/
        getStreetViewPanoramaAsync(this);
/*        setUpStreetViewPanoramaIfNeeded(savedInstanceState);
        setUpMapIfNeeded();*/
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {

        //StreetViewPanoramaOrientation orientation = new StreetViewPanoramaOrientation(0f, bearing);
        float tilt = streetViewPanorama.getPanoramaCamera().tilt + 0;
        tilt = (tilt > 90) ? 90 : tilt;

        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                .zoom(streetViewPanorama.getPanoramaCamera().zoom)
                .tilt(tilt)
                .bearing(/*streetViewPanorama.getPanoramaCamera().bearing -*/ bearing)/*.orientation(orientation)*/.build();
        streetViewPanorama.setPosition(position);
        streetViewPanorama.animateTo(camera, 500);

        /*StreetViewPanoramaOrientation.Builder orientationBuilder = StreetViewPanoramaOrientation.builder();
        Point point = streetViewPanorama.orientationToPoint(orientationBuilder.bearing(bearing).tilt(0).build());
        streetViewPanorama.pointToOrientation(point);*/

    }

    @Override
    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation location) {
        if (location != null) {
            marker.setPosition(location.position);
        }
    }

    private void setUpMap() {
        mMap.setOnMarkerDragListener(this);
        // Creates a draggable marker. Long press to drag.
        marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .draggable(true));
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpStreetViewPanoramaIfNeeded(Bundle savedInstanceState) {
        if (svp == null) {
            svp = ((SupportStreetViewPanoramaFragment)
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama))
                    .getStreetViewPanorama();
            if (svp != null) {
                if (savedInstanceState == null) {
                    svp.setPosition(position);
                }
                svp.setOnStreetViewPanoramaChangeListener(this);
            }
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //streetViewPanoramaFragment.getStreetViewPanorama().setPosition(marker.getPosition(), 150);
        svp.setPosition(marker.getPosition(), 150);
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
