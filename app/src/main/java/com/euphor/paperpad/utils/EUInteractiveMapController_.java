package com.euphor.paperpad.utils;

import android.content.Context;
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
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.interactiveMap.Point;
import com.euphor.paperpad.Beans.interactiveMap.Position;

import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.widgets.PinImageView;
import com.euphor.paperpad.widgets.zoomableview.ZoomViewTouchable;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Euphor on 20/11/14.
 */
public class EUInteractiveMapController_ implements LocationListener {

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
    //private InteractiveSmartInfoWindow interactiveSmartInfoWindow;

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


    public EUInteractiveMapController_(){
        }

    public EUInteractiveMapController_(RelativeLayout container, ZoomViewTouchable mapImage, List<Point> points, LatLng coordinates, float horizontalMeter, int myLocationColor) {
        super();
        ratio = 1;
        lastLocationPoint = new PointF(0, 0);
        this.container = container;
        this.mapImage = mapImage;
        this.points = points;
        this.originCoordinates = coordinates;
        this.horizontalMeter = horizontalMeter;
        this.myLocationColor = myLocationColor;

        this.locationPropertiesList = new ArrayList<LocationProperties>();

         //new AppController(getActivity());

            /*try {
                colors = new Colors(appController.getParametersDao().queryForId(1));
            } catch (SQLException e) {
                colors = new Colors();
                e.printStackTrace();
            }*/
        }


    public void startMonitoringUserLocation() {

        locationManager = getLocationManager();

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            Log.i(" startMonitoringUserLocation Log ","Location GPS_PROVIDER service disabled");
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == false) {
            Log.i(" startMonitoringUserLocation Log ","Location NETWORK_PROVIDER Service disabled");
        }

        //PointF point = percentageDistanceFromOrigin(new LatLng(46.139736, 4.699315));//new PointF(38f, 43f);//
        //Log.i(" Percentage Log ","  x : " + point.x + "  y : " + point.y);

        drawMyLocation();
        //mapImage.setPoint(point, Color.BLUE);

        }

    public double getPlanPosition(double percentPos){

        //return (Utils.isTablet(mapImage.getContext()) && mapImage.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? (percentPos * (mapImage.getMeasuredWidth())/ 100.00) : (percentPos * (mapImage.getMeasuredWidth() / mapRatio())/ 100.00);
        return percentPos * (illustration.getOriginalWidth()/horizontalMeter);
    }

    public double getPlanPosition(double percentPos, int width){

        //return (Utils.isTablet(mapImage.getContext()) && mapImage.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? (percentPos * (mapImage.getMeasuredWidth())/ 100.00) : (percentPos * (mapImage.getMeasuredWidth() / mapRatio())/ 100.00);
        return percentPos * (width/horizontalMeter);
    }

    public void drawLocations(){

    //locationPropertiesList.add(new LocationProperties(point, color));

    BitmapFactory.Options myOptions = new BitmapFactory.Options();
    myOptions.inDither = true;
    myOptions.inScaled = false;
    myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
    myOptions.inPurgeable = true;

    Bitmap bitmap = ((BitmapDrawable)mapImage.getDrawable()).getBitmap();//BitmapFactory.decodeResource(mapImage.getContext().getResources(), R.drawable.plan, myOptions);
    Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
    Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

    Canvas canvas = new Canvas(mutableBitmap);

    for (int i = 0 ; i < points.size(); i++){
       Position p = points.get(i).getPosition();
       String color =  points.get(i).getColor();
       Paint paint = new Paint();
       paint.setAntiAlias(true);

       if(color == null || color.isEmpty()){
           paint.setColor(Color.BLACK);
       }else{
           paint.setColor(Color.parseColor("#"+color));
       }

       canvas.drawCircle((float)getPlanPosition(p.getX()) , (float)(getPlanPosition(p.getY()) / mapRatio()) , 30, paint);
    }

    mapImage.setAdjustViewBounds(true);
    mapImage.setMap(mutableBitmap);

    }
    public void drawMyLocation(){

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = ((BitmapDrawable)mapImage.getDrawable()).getBitmap();//BitmapFactory.decodeResource(mapImage.getContext().getResources(), R.drawable.plan,myOptions);
        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
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

/*            if (Utils.isTablet(mapImage.getContext()) && mapImage.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                canvas.drawCircle((float)getPlanPosition(p.getX()) + 50, (float)(getPlanPosition(p.getY()) / mapRatio()) + 120, 45 , paint);
                canvas.drawBitmap(icon, (float)getPlanPosition(p.getX()) + 50 - 26  , (float)(getPlanPosition(p.getY()) / mapRatio()) + 120 - 26 , pt);

            }else{

                canvas.drawCircle((float)getPlanPosition(p.getX()) , (float)(getPlanPosition(p.getY()) / mapRatio()), 45 , paint);
                canvas.drawBitmap(icon, (float)getPlanPosition(p.getX()) - 26  , (float)(getPlanPosition(p.getY()) / mapRatio()) - 26 , pt);
            }*/

            canvas.drawCircle((float)getPlanPosition(p.getX(), mutableBitmap.getWidth()) , (float)(getPlanPosition(p.getY() , mutableBitmap.getWidth()) / mapRatio()), 45 , paint);
            canvas.drawBitmap(icon, (float)getPlanPosition(p.getX(), mutableBitmap.getWidth()) - 26  , (float)(getPlanPosition(p.getY(), mutableBitmap.getWidth()) / mapRatio()) - 26 , pt);







        }

        mapImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float _Xoffset = mapImage.getMeasuredWidth() / 2 - mapImage.getMeasuredWidth() * mapRatio() / 2;
                float x = motionEvent.getX();// - _Xoffset;  // or getRawX();
                float y = motionEvent.getY();

                switch(action){
                    case MotionEvent.ACTION_DOWN:
                        //float[] absoluteOrigine = new float[2];
                        float[] absolutePos1 = new float[2];
                        float[] absolutePos2 = new float[2];

/*                        float[] f = new float[9];
                        mapImage.getImageMatrix().getValues(f);

                        Drawable d = mapImage.getDrawable();
                        Rect r = d.getBounds();

                        float transX = f[Matrix.MTRANS_X];
                        float transY = f[Matrix.MTRANS_Y];

                        float scaleX = f[Matrix.MSCALE_X];
                        float scaleY = f[Matrix.MSCALE_Y];

                        // rect coordinates
                        float x0 = transX;
                        float y0 = transY;
                        float x1 = scaleX * (r.right - r.left);
                        float y1 = scaleY * (r.bottom - r.top);*/

                        for (int i = 0 ; i < points.size(); i++) {
                            Position p = points.get(i).getPosition();

    /*                        if (Utils.isTablet(mapImage.getContext()) && mapImage.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                                matrix.mapPoints(absolutePos1, 0, new float[]{(float) getPlanPosition(p.getX()) + 50 - 20, (float) getPlanPosition(p.getY()) / mapRatio() + 120 - 20}, 0, 1);
                                matrix.mapPoints(absolutePos2, 0, new float[]{(float) getPlanPosition(p.getX()) + 50 + 20, (float) getPlanPosition(p.getY()) / mapRatio() + 120 + 20}, 0, 1);

                            }else {
                                matrix.mapPoints(absolutePos1, 0, new float[]{(float) getPlanPosition(p.getX()) - 20, (float) getPlanPosition(p.getY()) / mapRatio() - 20}, 0, 1);
                                matrix.mapPoints(absolutePos2, 0, new float[]{(float) getPlanPosition(p.getX()) + 20, (float) getPlanPosition(p.getY()) / mapRatio() + 20}, 0, 1);
                            }*/


                            matrix.mapPoints(absolutePos1, 0, new float[]{(float) getPlanPosition(p.getX()) - 20, (float) getPlanPosition(p.getY()) / mapRatio() - 20}, 0, 1);
                            matrix.mapPoints(absolutePos2, 0, new float[]{(float) getPlanPosition(p.getX()) + 20, (float) getPlanPosition(p.getY()) / mapRatio() + 20}, 0, 1);
                            //matrix.mapPoints(absoluteOrigine, 0, new float[]{x, y}, 0, 1);

                            //Log.e(" Point  numéro : "+ i , " <==================>  Title : "+points.get(i).getTitle());

                            //Log.e(" Points  X : " + absoluteOrigine[0] + ",  Y : " + absoluteOrigine[1], " p.X : " + absolutePos1[0] + ", " + absolutePos2[0]+ " ===>  p.Y  : "+absolutePos1[1] + ", " + absolutePos2[1]);


                            //Log.w(" Point  numéro : " + i, " <==================>  Title : " + points.get(i).getTitle());

                            //Log.w(" Points  X : " + x + ",  Y : " + y, " p.X : " + (getPlanPosition(p.getX()) - 20) + ", " + (getPlanPosition(p.getX()) + 20) + " ===>  p.Y  : " + (getPlanPosition(p.getY()) / mapRatio() - 20) + ", " + (getPlanPosition(p.getY()) / mapRatio() + 20));
/*
                            Log.e(" Points  X : " + x + ",  Y : " + y, " getPlanPosition(p.getX()) : " + getPlanPosition(p.getX()) + ", " + (getPlanPosition(p.getX()) + 30 )
                                    + " (getPlanPosition(p.getY()) * mapRatio()) : "+(getPlanPosition(p.getY()) * mapRatio())+ "   (getPlanPosition(p.getY()) * mapRatio() + 30)"+(getPlanPosition(p.getY()) * mapRatio() + 30));
*/
/*                            if (absoluteOrigine[0] >= absolutePos1[0] && absoluteOrigine[0] < absolutePos2[0]
                                    && absoluteOrigine[1] >= absolutePos1[1] && absoluteOrigine[1] < absolutePos2[1]) {
                                interactiveInfoWindow = new InteractiveInfoWindow(mapImage.getContext(), points.get(i), colors);
                                getInfoWindowZone(mapImage.getContext(), absolutePos1[0] + 45, absolutePos1[1] + 45);*/

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

        meterUnit = (float) ((mutableBitmap.getWidth() / horizontalMeter) * 2.7);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(myLocationColor);
        canvas.drawCircle(lastLocationPoint.x * meterUnit , lastLocationPoint.y * meterUnit / mapRatio(), 20, paint);

        mapImage.setAdjustViewBounds(true);
        mapImage.setMap(mutableBitmap);

    }


    public Matrix matrix;
    public Illustration illustration;


    public void drawInContainer(){


        for(int i = 1; i < container.getChildCount(); i++){
            container.removeViewAt(i);
        }



        for (int i = 0 ; i < points.size(); i++){
            Position p = points.get(i).getPosition();
            String color =  points.get(i).getColor();

            if(color == null || color.isEmpty()){
                color = "000000";
            }
            PinImageView pinIcon = new PinImageView(mapImage.getContext(), Color.parseColor("#"+color));

/*            Rect rect = frameForPositionX((float)getPlanPosition(p.getX()) * scaleX, (float)getPlanPosition(p.getY()) * scaleY, mapImage.getCurrentZoomLevel());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rect.width(), rect.height());
            params.topMargin = rect.top;//(int)(lastLocationPoint.x * meterUnit * scaleX);
            params.leftMargin = rect.left;*/

            float[] absolutePos = new float[2];// = getAbsolutePosition((float) getPlanPosition(p.getX()), (float) getPlanPosition(p.getY()));
            matrix.mapPoints(absolutePos, new float[]{(float) getPlanPosition(p.getX()), (float) getPlanPosition(p.getY())});

            //Rect rect = frameForPositionX(absolutePos[0], absolutePos[1], zoomLevel);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
/*            params.topMargin = rect.top;//(int)absolutePos[0];//((getPlanPosition(p.getX()) * zoomLevel ) + (transX ));
            params.leftMargin = rect.left;//(int)absolutePos[1];//((getPlanPosition(p.getY()) * zoomLevel) *//*//* ratio*//*  + (transY ));*/
            params.topMargin = baseMapFrame().top + (int)absolutePos[0];//((getPlanPosition(p.getX()) * zoomLevel ) + (transX ));
            params.leftMargin = baseMapFrame().left + (int)absolutePos[1];//((getPlanPosition(p.getY()) * zoomLevel) //* ratio  + (transY ));
            pinIcon.setLayoutParams(params);


            pinIcon.setImageBitmap(getSingleDrawable(buildPinIcon(points.get(i).getIcon(), Color.parseColor("#" + color))));
            pinIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(" PinIcon Log ","icon position : X = "+view.getX()+"  Y = "+view.getY());
                }
            });
            container.addView(pinIcon);
        }




        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(myLocationColor);
        PinImageView pinIcon = new PinImageView(mapImage.getContext(), myLocationColor);



/*        Rect rect = frameForPositionX(lastLocationPoint.x * meterUnit, lastLocationPoint.y * meterUnit, mapImage.getCurrentZoomLevel());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rect.width(), rect.height());
        params.topMargin = rect.top;//(int)(lastLocationPoint.x * meterUnit * scaleX);
        params.leftMargin = rect.left;//(int)(lastLocationPoint.y * meterUnit * scaleY);
        pinIcon.setLayoutParams(params);*/


        meterUnit = baseMapFrame().left / horizontalMeter;
        float[] absolutePosLocation = new float[2];//getAbsolutePosition((float) getPlanPosition(lastLocationPoint.x * meterUnit), (float) getPlanPosition(lastLocationPoint.y * meterUnit));
        matrix.mapPoints(absolutePosLocation, new float[]{lastLocationPoint.x * meterUnit, lastLocationPoint.y * meterUnit});
        //Rect rect = frameForPositionX(absolutePosLocation[0], absolutePosLocation[1], zoomLevel);



        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
        /*params.topMargin = rect.top;//(int) absolutePosLocation[0];//((getPlanPosition(p.getX()) * zoomLevel ) + (transX ));
        params.leftMargin = rect.left;//(int)absolutePosLocation[1];//((getPlanPosition(p.getY()) * zoomLevel) *//*//* ratio*//*  + (transY ));*/
        params.topMargin = baseMapFrame().top + (int)absolutePosLocation[0];//(lastLocationPoint.x * meterUnit * zoomLevel + (transX ));
        params.leftMargin = baseMapFrame().left + (int)absolutePosLocation[1];//(lastLocationPoint.y * meterUnit * zoomLevel + (transY ));
        pinIcon.setLayoutParams(params);
        container.addView(pinIcon);
        //mapImage.setMap(mutableBitmap);

    }

    public Rect frameForPositionX(float positionX, float positionY, float zoomScale) {

        Rect baseFrame = baseMapFrame();
        float[] matValues = new float[9];
        matrix.getValues(matValues);
        float mapX = baseFrame.left * (zoomScale * 3) -  matValues[Matrix.MTRANS_X];//(baseFrame.width() / 2 - illustration.getOriginalWidth());
        float mapY = baseFrame.top * (zoomScale * 3)  - matValues[Matrix.MTRANS_Y];//(baseFrame.height() / 2 - illustration.getOriginalHeight());// contentOffset.y;
        float mapWidth = baseFrame.width() * (zoomScale * 3) ;
        float mapHeight = baseFrame.height() * (zoomScale * 3);

        int diameter = 30;
        int top = (int) (mapY + mapHeight * positionY  / 100.00 - diameter/2);
        int left = (int) (mapX + mapWidth * positionX / 100.00 - diameter/2);
        Rect frame = new Rect(top, left, diameter, diameter);
        return frame;

    }

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
        if(this.mapImage == null) return 0;
        return (float)this.illustration.getOriginalWidth() / this.illustration.getOriginalHeight();
    }

    public PointF percentageDistanceFromOrigin(LatLng to) {
        return percentageDistanceFromCoordinates(this.originCoordinates, to);
        }

    public PointF percentageDistanceFromCoordinates(LatLng from, LatLng to) {
        PointF realDistance = distanceFromCoordinates(from, to);
        PointF percentageDistance = new PointF(realDistance.x / this.horizontalMeter * 100, realDistance.y / this.getVerticalMeters() * 100);
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
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0,  this);
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, this);
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
        drawMyLocation();
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

//		if(height > width) {
//			if((x > calendarEventInfos.getWidth()/2) && ((x + calendarEventInfos.getWidth()/2) < width))
//			{
//				if(y > (calendarEventInfos.getHeight()) && y + calendarEventInfos.getHeight() > height) {
//
//					zone = 270;
//
//				}else if(y < calendarEventInfos.getHeight() && y + calendarEventInfos.getHeight() < height) {
//
//					zone = 90;
//
//				}
//			}
//
//			if((x > calendarEventInfos.getWidth()) && (position.x + mLocationInfos.getWidth()) >= width - 50) {
//				if((y > (calendarEventInfos.getHeight()/2)) && (height > (y + ( calendarEventInfos.getHeight()/2 ) ))) {
//					zone = 0;
//				}
//			}
//			else if((x + calendarEventInfos.getWidth() < width) && ((y > calendarEventInfos.getHeight()/2)  && (y + calendarEventInfos.getHeight()/2 < height))) {
//				zone = 180;
//			}
//
//		}
//		else {
//			if((x < calendarEventInfos.getWidth()/2 + 50) && ((x + calendarEventInfos.getWidth()/2) < width ))
//			{
//				if(y > (calendarEventInfos.getHeight() - 50) && (y + calendarEventInfos.getHeight()) > height) {
//
//					zone = 270;
//
//				}else if(y < calendarEventInfos.getHeight() && (y + calendarEventInfos.getHeight()) < height ) {
//
//					zone = 90;
//
//				}
//			}
//
//			else if((y >  calendarEventInfos.getHeight())  && (y + calendarEventInfos.getHeight()) >= height - 100) {
//				if(x + calendarEventInfos.getWidth() > width - 100) {
//					zone = 0;
//				}else if(x < calendarEventInfos.getWidth()) {
//					zone = 180;
//				}
//
//			}
////			else if((x + calendarEventInfos.getWidth() < width - 150) && ((y > calendarEventInfos.getHeight()/2 - 50)  && (y+ calendarEventInfos.getHeight()/2 < height - 50))) {
////				zone = 180;
////			}
//	}

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
