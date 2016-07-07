package com.euphor.paperpad.Beans.interactiveMap;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//import javax.annotation.Generated;

/**
 * Created by uness on 21/11/14.
 */

public class Origin extends RealmObject {

        @PrimaryKey
       private int key_id;


    private double longitude;

    private double latitude;
    private map map;

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public map getMap() {
        return map;
    }

    public void setMap(map map) {
        this.map = map;
    }

    public Origin() {
    }

    public Origin(int key_id, double longitude, double latitude, map map) {
        this.key_id = key_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.map = map;
    }
}