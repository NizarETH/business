package com.euphor.paperpad.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.interactiveMap.Point;
import com.euphor.paperpad.Beans.interactiveMap.Position;
/*import com.euphor.paperpad.controllers.AppController;*/
import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.widgets.PinImageView;
import com.euphor.paperpad.widgets.zoomableview.ZoomViewTouchable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Euphor on 20/11/14.
 */
public class EUInteractiveMapController implements LocationListener {

    private static final String LOCATION_SERVICE = "Location";
    private  Colors colors;
    private  RelativeLayout container;
    //EUInteractiveMapView* mapView;
    ZoomViewTouchable mapImage;
    LocationManager locationManager;
    float horizontalMeter;
    LatLng originCoordinates;
    List<Point> points;
    List<LocationProperties> locationPropertiesList;
    float ratio;
    float meterUnit;
    private PointF lastLocationPoint;
    private int myLocationColor;
    private InteractiveInfoWindow interactiveInfoWindow;


    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    float zoomLevel;

    class LocationProperties{
        PointF point;
        int color;

        public LocationProperties(PointF point, int color){
            this.point = point;
            this.color = color;
        }
    }


    public EUInteractiveMapController(){
    }

    public EUInteractiveMapController(RelativeLayout container, ZoomViewTouchable mapImage, List<Point> points, LatLng coordinates, float horizontalMeter, int myLocationColor) {
        super();
        ratio = 1;
        lastLocationPoint = new PointF(-1, -1);
        this.container = container;
        this.mapImage = mapImage;
        this.points = points;
        this.originCoordinates = coordinates;
        this.horizontalMeter = horizontalMeter;
        this.myLocationColor = myLocationColor;

        this.locationPropertiesList = new ArrayList<LocationProperties>();

       // AppController appController = ((MyApplication)((MainActivity)mapImage.getContext()).getApplication()).getAppController();//new AppController(getActivity());
        Realm   realm = Realm.getInstance( mapImage.getContext());


        Parameters ParamColor = realm.where(Parameters.class).findFirst();

        colors = new Colors(ParamColor);

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private Bitmap bitmap;
    public void startMonitoringUserLocation(Bitmap bitmap) {

        locationManager = getLocationManager();

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            Log.i(" startMonitoringUserLocation Log ","Location GPS_PROVIDER service disabled");
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == false) {
            Log.i(" startMonitoringUserLocation Log ","Location NETWORK_PROVIDER Service disabled");
        }

        //PointF point = percentageDistanceFromOrigin(new LatLng(46.139736, 4.699315));//new PointF(38f, 43f);//
        //Log.i(" Percentage Log ","  x : " + point.x + "  y : " + point.y);
        this.bitmap = bitmap;
        drawMyLocation(this.bitmap);
        //mapImage.setPoint(point, Color.BLUE);

    }
            public double getPlanPosition(double percentPos) {

            DisplayMetrics metrics = mapImage.getContext().getResources().getDisplayMetrics();

            float densityDpi = 1 / metrics.density;// * (metrics.densityDpi / 160);



            if (mapImage.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                  if (Utils.isTablet(mapImage.getContext())) {

                            return densityDpi * (percentPos * ((mapImage.getMeasuredHeight() - 45) / (mapRatio() * 100.00f)));

                        } else {

                        float density = metrics.density;

                        if (density > 2.0f) {

                               density = 0.75f;

                               //return density  percentPos  ((mapImage.getMeasuredHeight() - 45) /(100.00f));

                    }else if (density == 2.0f) {

                           density = 1.25f;

                       } else if (density == 1.5f) {

                            density = 1.95f;

                        }

                    return density * percentPos * ((mapImage.getMeasuredHeight() - 45) / (mapRatio() * 100.00f));

                    //return density  percentPos  ((mapImage.getMeasuredHeight() - 45) /(100.00f));

                           }

                  }else{

                    return densityDpi * percentPos * ((mapImage.getMeasuredWidth()) /(mapRatio() * 100.00f));

                }

            }



    public void drawMyLocation(Bitmap bitmap){

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        //Bitmap bitmap = ((BitmapDrawable)mapImage.getDrawable()).getBitmap();//BitmapFactory.decodeResource(mapImage.getContext().getResources(), R.drawable.plan,myOptions);
        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);//.createScaledBitmap(bitmap, widthBitmap, bitmap.getHeight(), true);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        final Canvas canvas = new Canvas(mutableBitmap);
        Paint pt = new Paint();
        pt.setAntiAlias(true);
        pt.setColor(Color.WHITE);

        for (int i = 0 ; i < points.size(); i++){
            final Position p = points.get(i).getPosition();
            String color =  points.get(i).getColor();
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            if(color == null || color.isEmpty()){
                paint.setColor(Color.BLACK);
                color = "000000";
            }else{
                paint.setColor(Color.parseColor("#"+color));
            }

            Bitmap icon = null;
            try {
                icon = new BitmapDrawable(BitmapFactory.decodeStream(container.getContext().getAssets().open(points.get(i).getIcon()))).getBitmap();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //canvas.drawBitmap(getSingleDrawable(buildPinIcon(points.get(i).getIcon(), Color.parseColor("#"+color))), (float)getPlanPosition(p.getX())  , (float)(getPlanPosition(p.getY()) / mapRatio()) , paint);

  /*          if (Utils.isTablet(mapImage.getContext()) && mapImage.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                canvas.drawCircle((float)getPlanPosition(p.getX()) + 50, (float)(getPlanPosition(p.getY()) / mapRatio()) + 120, 45 , paint);
                canvas.drawBitmap(icon, (float)getPlanPosition(p.getX()) + 50 - 26  , (float)(getPlanPosition(p.getY()) / mapRatio()) + 120 - 26 , pt);

            }else{*/

            canvas.drawCircle((float)getPlanPosition(p.getX()) /*1414.26*/, (float)(getPlanPosition(p.getY()) / mapRatio()), 45 , paint);
            if(icon != null)
            canvas.drawBitmap(icon, (float)getPlanPosition(p.getX()) - 26  /*1397.2366*/, (float)(getPlanPosition(p.getY()) / mapRatio()) - 26 , pt);
//            }

        }

        mapImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float x = motionEvent.getX();// - _Xoffset;  // or getRawX();
                float y = motionEvent.getY();

                switch(action){
                    case MotionEvent.ACTION_DOWN:
                        //float[] absoluteOrigine = new float[2];
                        float[] absolutePos1 = new float[2];
                        float[] absolutePos2 = new float[2];



                        for (int i = 0 ; i < points.size(); i++) {
                            Position p = points.get(i).getPosition();

 /*                           if (Utils.isTablet(mapImage.getContext()) && mapImage.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                                matrix.mapPoints(absolutePos1, 0, new float[]{(float) getPlanPosition(p.getX()) + 50 - 20, (float) getPlanPosition(p.getY()) / mapRatio() + 120 - 20}, 0, 1);
                                matrix.mapPoints(absolutePos2, 0, new float[]{(float) getPlanPosition(p.getX()) + 50 + 20, (float) getPlanPosition(p.getY()) / mapRatio() + 120 + 20}, 0, 1);

                            }else {*/
                            matrix.mapPoints(absolutePos1, 0, new float[]{(float) getPlanPosition(p.getX()) - 20, (float) getPlanPosition(p.getY()) / mapRatio() - 20}, 0, 1);
                            matrix.mapPoints(absolutePos2, 0, new float[]{(float) getPlanPosition(p.getX()) + 20, (float) getPlanPosition(p.getY()) / mapRatio() + 20}, 0, 1);
                            //}


                            if (x >= absolutePos1[0] && x < absolutePos2[0]
                                    && y >= absolutePos1[1] && y < absolutePos2[1]) {

                                if(Utils.isTablet(mapImage.getContext())) {
                                    interactiveInfoWindow = new InteractiveInfoWindow(mapImage.getContext(), points.get(i), colors);
                                    getInfoWindowZone(mapImage.getContext(), x + 90, y);
                                }else{
                                    InteractiveSmartInfoWindow interactiveSmartInfoWindow = new InteractiveSmartInfoWindow(mapImage.getContext(), points.get(i), colors);
                                    interactiveSmartInfoWindow.setInfoDialogMarkerView(points.get(i));
                                    interactiveSmartInfoWindow.show();
                                }


                                //tada, if this is true, you've started your click inside your bitmap
                                //Log.e(" Points ", " Point (" + p.getX() + ", " + p.getY() + ")");
                                //Toast.makeText(mapImage.getContext(), " point (" + p.getX() + ", " + p.getY() + ")", Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                }
                return false;
            }
        });
        //meterUnit = mapImage.getMeasuredWidth() / horizontalMeter;
        //meterUnit = (float) ((mapImage.getMeasuredHeight() / horizontalMeter) * 2.7);
        //meterUnit = horizontalMeter / 100.00f;// ((mapImage.getMeasuredHeight() * mapRatio())/ horizontalMeter);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(myLocationColor);
        canvas.drawCircle((float)getPlanPosition(lastLocationPoint.x * 100.00f / (illustration.getOriginalWidth() * mapRatio())) , (float)(getPlanPosition(lastLocationPoint.y * 100.00f / illustration.getOriginalHeight()) / mapRatio()), 20 , paint);
        //canvas.drawCircle(lastLocationPoint.x * meterUnit , lastLocationPoint.y * meterUnit / mapRatio(), 20, paint);

        mapImage.setAdjustViewBounds(true);
        mapImage.setMap(mutableBitmap);
    }


    public Matrix matrix;
    public Illustration illustration;



    Rect baseMapFrame() {

        int width = container.getMeasuredWidth();
        int height = container.getMeasuredHeight();

        float mapRatio = mapRatio();
        int mapWidth;
        int mapHeight;

        if (mapRatio > 1) {
            mapWidth = width;
            mapHeight = (int) (width/mapRatio);
        } else {
            mapHeight = height;
            mapWidth = (int) (height*mapRatio);
        }

        return new Rect(width/2 - mapWidth/2, height/2 - mapHeight/2, mapWidth, mapHeight);

    }
    public float mapRatio() {
        if(this.mapImage == null || 0 == illustration.getOriginalHeight()) return mapImage.getMeasuredWidth() / mapImage.getMeasuredHeight();

        return (float)this.illustration.getOriginalWidth() / this.illustration.getOriginalHeight();


    }

    public PointF percentageDistanceFromOrigin(LatLng to) {
        return percentageDistanceFromCoordinates(this.originCoordinates, to);
    }

    public PointF percentageDistanceFromCoordinates(LatLng from, LatLng to) {
        PointF realDistance = distanceFromCoordinates(from, to);
        PointF percentageDistance = new PointF(realDistance.x * this.horizontalMeter / 100.00f  , realDistance.y * this.getVerticalMeters() / 100.00f );
        return percentageDistance;
    }

    public PointF distanceFromCoordinates(LatLng from, LatLng to) {
        double horizontalDistance = horizontalDistanceFromCoordinates(from , to);
        double verticalDistance = verticalDistanceFromCoordinates(from, to);
        PointF distance = new PointF((int)horizontalDistance, (int)verticalDistance);
        return distance;
    }

    public double horizontalDistanceFromCoordinates(LatLng from , LatLng to) {
        Location fromLocation = new Location(LOCATION_SERVICE);
        fromLocation.setLatitude(from.latitude);
        fromLocation.setLongitude(from.longitude);
        Location toLocation = new Location(LOCATION_SERVICE);
        toLocation.setLatitude(from.latitude);
        toLocation.setLongitude(to.longitude);
        return fromLocation.distanceTo(toLocation);
    }

    public double verticalDistanceFromCoordinates(LatLng from , LatLng to) {
        Location fromLocation = new Location(LOCATION_SERVICE);
        fromLocation.setLatitude(from.latitude);
        fromLocation.setLongitude(from.longitude);
        Location toLocation = new Location(LOCATION_SERVICE);
        toLocation.setLatitude(to.latitude);
        toLocation.setLongitude(from.longitude);
        return fromLocation.distanceTo(toLocation);
    }

    public LocationManager getLocationManager() {
        if (this.locationManager == null) {
            this.locationManager = (LocationManager) mapImage.getContext().getSystemService(Context.LOCATION_SERVICE);
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1,  this);
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, this);
        }
        return this.locationManager;
    }

    public float getVerticalMeters() {
        if (this.mapImage == null) {
            Log.i(" VerticalMeters Log ","!!! No image");
            return 0;
        }
        ratio = (float)this.illustration.getOriginalWidth() / this.illustration.getOriginalHeight();
        return this.horizontalMeter / ratio;
    }

/*public EUInteractiveMapView  getMapView() {
        if (this.mapView == null) {
        this.mapView = new EUInteractiveMapView() ;
        }
        return this.mapView;
        }*/

    @Override
    public void onLocationChanged(Location location) {
        //originCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        lastLocationPoint = percentageDistanceFromOrigin(new LatLng(location.getLatitude(), location.getLongitude()));

        /*Bitmap bitmap = mapImage.getMap();//this.getSingleDrawable((LayerDrawable)drawable);
        if(bitmap != null){
            drawMyLocation(bitmap);
        }*/
        drawMyLocation(this.bitmap);
        //drawInContainer();


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void stopLocation(){
        locationManager.removeUpdates(this);
    }

    private LayerDrawable buildPinIcon(String iconPath, int iconColor) {
		/*
		 * Construct the pin icon
		 */

        LayerDrawable result = null;

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(container.getContext().getAssets().open(iconPath), null, myOptions);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (Exception e) {
            //Crashlytics.logException(e);

        }


        Bitmap workingBitmap = Bitmap.createBitmap(100, 100, bitmap.getConfig()); //Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(false);

        paint.setColor(iconColor);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(bitmap.getWidth()/2 + 10, bitmap.getHeight()/2 + 10, 32, paint);

        //	    canvas.drawBitmap(bitmap, 10, 10, null);

        if (!iconPath.isEmpty()) {

            try {
                result = new LayerDrawable(
                        new Drawable[] {
								/*pincolor*/ new BitmapDrawable(mutableBitmap),
                                new BitmapDrawable(BitmapFactory.decodeStream(container.getContext().getAssets().open(iconPath)))});

            } catch (Resources.NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }



        return result;

    }

    public Bitmap getSingleDrawable(LayerDrawable layerDrawable) {

        int resourceBitmapHeight = 22, resourceBitmapWidth = 22;
        int widthInDp = 48;

        resourceBitmapHeight = 50;
        resourceBitmapWidth = 50;

        // float widthInInches = 0.3f;
        //		widthInDp = SIZE_ICON_PIN;
        int widthInPixels = (int) (widthInDp
                * container.getContext().getResources().getDisplayMetrics().density + 0.5);
        int heightInPixels = (int) (widthInPixels * resourceBitmapHeight / resourceBitmapWidth);

        int insetLeft = 7, insetTop = 7, insetRight = 7, insetBottom = 7;

        layerDrawable.setLayerInset(1, insetLeft, insetTop, insetRight,
                insetBottom);


        Bitmap bitmap = Bitmap.createBitmap(widthInPixels, heightInPixels,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, widthInPixels, heightInPixels);
        layerDrawable.draw(canvas);

        // BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
        // bitmap);
        // bitmapDrawable.setBounds(0, 0, widthInPixels, heightInPixels);

        return bitmap;
    }

    public void getInfoWindowZone(Context context, float x, float y) {

        //		Point position = new Point((int)v.getX(), (int)v.getY());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int zone = 0;
        //
        int width = display.getWidth();
        int height = display.getHeight();

/*
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        float x = location[0] ;//v.getX() + CalendarMonthFragment.gridItemX;//itemPos.x;
        float y = location[1] ;//v.getY() + CalendarMonthFragment.gridItemY;//itemPos.y;
*/

        interactiveInfoWindow.setDimensionByGridLocation();

        if((x > interactiveInfoWindow.getWidth()/2) && ((x + interactiveInfoWindow.getWidth()/2) < width))
        {
            if(y > (interactiveInfoWindow.getHeight()) && y + interactiveInfoWindow.getHeight() > height) {

                zone = 270;

            }else if(y < interactiveInfoWindow.getHeight() && y + interactiveInfoWindow.getHeight() < height) {

                zone = 90;

            }
        }

        if((x > interactiveInfoWindow.getWidth()) && (x + interactiveInfoWindow.getWidth()) >= width - 50) {
            if((y > (interactiveInfoWindow.getHeight()/2)) && (height > (y + ( interactiveInfoWindow.getHeight()/2 ) ))) {
                zone = 0;
            }
        }
        else{
            zone = 180;
        }



        interactiveInfoWindow.setViewByGridLocation(zone);



        interactiveInfoWindow.show(mapImage, x, y, zone, height);

        interactiveInfoWindow.setOnDismissListener(new QuickAction.OnDismissListener() {

            @Override
            public void onDismiss() {


            }
        });
    }

}

