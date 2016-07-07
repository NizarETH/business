package com.euphor.paperpad.forcastio.beans;


import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"latitude",
"longitude",
"timezone",
"offset",
"currently",
"minutely",
"hourly",
"daily",
"flags"
})*/
public class Forecast extends RealmObject{

@PrimaryKey
private int id;
/*@JsonProperty("latitude")*/
private double latitude;
/*@JsonProperty("longitude")*/
private double longitude;
/*@JsonProperty("timezone")*/
private String timezone;
/*@JsonProperty("offset")*/
private int offset;
/*@JsonProperty("currently")*/
private Currently currently;
/*@JsonProperty("daily")*/
private Daily daily;
/*@JsonProperty("flags")*/
/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

/*@JsonProperty("latitude")*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
return latitude;
}

/*@JsonProperty("latitude")*/
public void setLatitude(double latitude) {
this.latitude = latitude;
}

/*@JsonProperty("longitude")*/
public double getLongitude() {
return longitude;
}

/*@JsonProperty("longitude")*/
public void setLongitude(double longitude) {
this.longitude = longitude;
}

/*@JsonProperty("timezone")*/
public String getTimezone() {
return timezone;
}

/*@JsonProperty("timezone")*/
public void setTimezone(String timezone) {
this.timezone = timezone;
}

/*@JsonProperty("offset")*/
public int getOffset() {
return offset;
}

/*@JsonProperty("offset")*/
public void setOffset(int offset) {
this.offset = offset;
}

/*@JsonProperty("currently")*/
public Currently getCurrently() {
return currently;
}

/*@JsonProperty("currently")*/
public void setCurrently(Currently currently) {
this.currently = currently;
}

/*@JsonProperty("daily")*/
public Daily getDaily() {
return daily;
}

/*@JsonProperty("daily")*/
public void setDaily(Daily daily) {
this.daily = daily;
}

/*@JsonAnyGetter*/
/*public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

*//*@JsonAnySetter*//*
public void setAdditionalProperties(String name, Object value) {
this.additionalProperties.put(name, value);
}

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/

    public Forecast() {
    }

    public Forecast(double latitude, double longitude, String timezone, int offset, Currently currently, Daily daily, Map<String, Object> additionalProperties) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.offset = offset;
        this.currently = currently;
        this.daily = daily;
        /*this.additionalProperties = additionalProperties;*/
    }
}