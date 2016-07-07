package com.euphor.paperpad.Beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mounia on 01/06/2015.
 */
public class Street_view_default_position extends RealmObject {
    @PrimaryKey
    private  int id;
    private double street_view_latitude;
    private double street_view_longitude;
    private int street_view_orientation;
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Street_view_default_position() {
    }

    public Street_view_default_position(int id, double street_view_latitude, double street_view_longitude, int street_view_orientation/*, Map<String, Object> additionalProperties*/) {
        this.id = id;
        this.street_view_latitude = street_view_latitude;
        this.street_view_longitude = street_view_longitude;
        this.street_view_orientation = street_view_orientation;
        /*this.additionalProperties = additionalProperties;*/
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStreet_view_latitude() {
        return street_view_latitude;
    }

    public void setStreet_view_latitude(double street_view_latitude) {
        this.street_view_latitude = street_view_latitude;
    }

    public double getStreet_view_longitude() {
        return street_view_longitude;
    }

    public void setStreet_view_longitude(double street_view_longitude) {
        this.street_view_longitude = street_view_longitude;
    }

    public int getStreet_view_orientation() {
        return street_view_orientation;
    }

    public void setStreet_view_orientation(int street_view_orientation) {
        this.street_view_orientation = street_view_orientation;
    }

    /*public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/
}
