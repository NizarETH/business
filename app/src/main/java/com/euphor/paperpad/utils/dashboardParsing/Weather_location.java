package com.euphor.paperpad.utils.dashboardParsing;



import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

public class Weather_location extends RealmObject {

/*@JsonProperty("latitude")*/
private double latitude;
/*@JsonProperty("longitude")*/
private double longitude;
/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

/*@JsonProperty("latitude")*/
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

/*@JsonAnyGetter*/
/*public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}*/

/*@JsonAnySetter*/
/*public void setAdditionalProperties(String name, Object value) {
this.additionalProperties.put(name, value);
}*/

   /* public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/

    public Weather_location() {
    }

    public Weather_location(double latitude, double longitude/*, Map<String, Object> additionalProperties*/) {
        this.latitude = latitude;
        this.longitude = longitude;
       /* this.additionalProperties = additionalProperties;*/
    }
}