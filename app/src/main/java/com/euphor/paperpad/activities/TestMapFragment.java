/**
 * 
 */
package com.euphor.paperpad.activities;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author euphordev02
 *
 */
public class TestMapFragment extends Fragment{

	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	GoogleMap googleMap;
	private MapView mapView;
	
	/**
	 * 
	 */
	public TestMapFragment() {
		// TODO Auto-generated constructor stub
	}
/*
	 (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	 (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	 (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fragment, container, false);
		mapView = (MapView)view.findViewById(R.id.map);
		setUpMap();
//		MapView mapView = (MapView) super.onCreateView(inflater, container, savedInstanceState);
		googleMap = mapView.getMap();
//		googleMap = getMap();
//		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
//		        .getMap();
		    Marker hamburg = googleMap.addMarker(new MarkerOptions().position(HAMBURG)
		        .title("Hamburg"));
		    Marker kiel = googleMap.addMarker(new MarkerOptions()
		        .position(KIEL)
		        .title("Kiel")
		        .snippet("Kiel is cool")
		        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

		    // Move the camera instantly to hamburg with a zoom of 15.
		    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

		    // Zoom in, animating the camera.
		    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		    return view;
	}
	

	
	private MapView mMapView;
	private GoogleMap mMap;
	private HashMap<MyMapPoint, Marker> mPoints = new HashMap<MyMapPoint, Marker>();

	public static MyMapFragment newInstance(ArrayList<MyMapPoint> points) {
	    MyMapFragment fragment = new MyMapFragment();
	    Bundle args = new Bundle();
	    args.putParcelableArrayList(KEY_POINTS, points);
	    fragment.setArguments(args);
	    return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    mMapView.onSaveInstanceState(outState);
	    MyMapPoint[] points = mPoints.keySet().toArray(new MyMapPoint[mPoints.size()]);
	    outState.putParcelableArray(KEY_POINTS, points);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        Bundle extras = getArguments();
	        if ((extras != null) && extras.containsKey(KEY_POINTS)) {
	            for (MapFragment pointP : extras.getParcelableArrayList(KEY_POINTS)) {
	                mPoints.put((MyMapPoint) pointP, null);
	            }
	        }
	    } else {
	        MyMapPoint[] points = (MyMapPoint[]) savedInstanceState.getParcelableArray(KEY_POINTS);
	        for (MyMapPoint point : points) {
	            mPoints.put(point, null);
	        }
	    }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View layout = inflater.inflate(R.layout.map_fragment, container, false);

	    mMapView = (MapView) layout.findViewById(R.id.map);

	    return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    mMapView.onCreate(savedInstanceState);
	    setUpMapIfNeeded();
	    addMapPoints();
	}

	@Override
	public void onPause() {
	    mMapView.onPause();
	    super.onPause();
	}

	@Override
	public void onResume() {
	    super.onResume();
	    setUpMapIfNeeded();
	    mMapView.onResume();
	}

	@Override
	public void onDestroy() {
	    mMapView.onDestroy();
	    super.onDestroy();
	}

	public void onLowMemory() {
	    super.onLowMemory();
	    mMapView.onLowMemory();
	};

	private void setUpMapIfNeeded() {
	    if (mMap == null) {
	        mMap = ((MapView) getView().findViewById(R.id.map)).getMap();
	        if (mMap != null) {
	            setUpMap();
	        }
	    }
	}

	private void setUpMap() {
	    mMap.setOnInfoWindowClickListener(this);
	    addMapPoints();
	}

	private void addMapPoints() {
	    if (mMap != null) {
	        HashMap<MyMapPoint, Marker> toAdd = new HashMap<MyMapPoint, Marker>();
	        for (Entry<MyMapPoint, Marker> entry : mPoints.entrySet()) {
	            Marker marker = entry.getValue();
	            if (marker == null) {
	                MyMapPoint point = entry.getKey();
	                marker = mMap.addMarker(point.getMarkerOptions());
	                toAdd.put(point, marker);
	            }
	        }
	        mPoints.putAll(toAdd);
	    }
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
	    Fragment fragment = DetailsFragment.newInstance();
	    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentPane, fragment).addToBackStack(null).commit();
	}
	
	
*/
}
