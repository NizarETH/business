package com.euphor.paperpad.forcastio.beans;


import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"time",
"summary",
"icon",
"precipIntensity",
"precipIntensityError",
"precipProbability",
"precipType",
"temperature",
"apparentTemperature",
"dewPoint",
"humidity",
"windSpeed",
"windBearing",
"visibility",
"cloudCover",
"pressure",
"ozone"
})*/
public class Currently extends RealmObject{

/*@JsonProperty("time")*/
private int time;
/*@JsonProperty("summary")*/
private String summary;
/*@JsonProperty("icon")*/
private String icon;
/*@JsonProperty("precipIntensity")*/
private double precipIntensity;
/*@JsonProperty("precipIntensityError")*/
private double precipIntensityError;
/*@JsonProperty("precipProbability")*/
private double precipProbability;
/*@JsonProperty("precipType")*/
private String precipType;
/*@JsonProperty("temperature")*/
private double temperature;
/*@JsonProperty("apparentTemperature")*/
private double apparentTemperature;
/*@JsonProperty("dewPoint")*/
private double dewPoint;
/*@JsonProperty("humidity")*/
private double humidity;
/*@JsonProperty("windSpeed")*/
private double windSpeed;
/*@JsonProperty("windBearing")*/
private int windBearing;
/*@JsonProperty("visibility")*/
private double visibility;
/*@JsonProperty("cloudCover")*/
private double cloudCover;
/*@JsonProperty("pressure")*/
private double pressure;
/*@JsonProperty("ozone")*/
private double ozone;
//private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/*@JsonProperty("time")*/
public int getTime() {
return time;
}

/*@JsonProperty("time")*/
public void setTime(int time) {
this.time = time;
}

/*@JsonProperty("summary")*/
public String getSummary() {
return summary;
}

/*@JsonProperty("summary")*/
public void setSummary(String summary) {
this.summary = summary;
}

/*@JsonProperty("icon")*/
public String getIcon() {
return icon;
}

/*@JsonProperty("icon")*/
public void setIcon(String icon) {
this.icon = icon;
}

/*@JsonProperty("precipIntensity")*/
public double getPrecipIntensity() {
return precipIntensity;
}

/*@JsonProperty("precipIntensity")*/
public void setPrecipIntensity(double precipIntensity) {
this.precipIntensity = precipIntensity;
}

/*@JsonProperty("precipIntensityError")*/
public double getPrecipIntensityError() {
return precipIntensityError;
}

/*@JsonProperty("precipIntensityError")*/
public void setPrecipIntensityError(double precipIntensityError) {
this.precipIntensityError = precipIntensityError;
}

/*@JsonProperty("precipProbability")*/
public double getPrecipProbability() {
return precipProbability;
}

/*@JsonProperty("precipProbability")*/
public void setPrecipProbability(double precipProbability) {
this.precipProbability = precipProbability;
}

/*@JsonProperty("precipType")*/
public String getPrecipType() {
return precipType;
}

/*@JsonProperty("precipType")*/
public void setPrecipType(String precipType) {
this.precipType = precipType;
}

/*@JsonProperty("temperature")*/
public double getTemperature() {
return temperature;
}

/*@JsonProperty("temperature")*/
public void setTemperature(double temperature) {
this.temperature = temperature;
}

/*@JsonProperty("apparentTemperature")*/
public double getApparentTemperature() {
return apparentTemperature;
}

/*@JsonProperty("apparentTemperature")*/
public void setApparentTemperature(double apparentTemperature) {
this.apparentTemperature = apparentTemperature;
}

/*@JsonProperty("dewPoint")*/
public double getDewPoint() {
return dewPoint;
}

/*@JsonProperty("dewPoint")*/
public void setDewPoint(double dewPoint) {
this.dewPoint = dewPoint;
}

/*@JsonProperty("humidity")*/
public double getHumidity() {
return humidity;
}

/*@JsonProperty("humidity")*/
public void setHumidity(double humidity) {
this.humidity = humidity;
}

/*@JsonProperty("windSpeed")*/
public double getWindSpeed() {
return windSpeed;
}

/*@JsonProperty("windSpeed")*/
public void setWindSpeed(double windSpeed) {
this.windSpeed = windSpeed;
}

/*@JsonProperty("windBearing")*/
public int getWindBearing() {
return windBearing;
}

/*@JsonProperty("windBearing")*/
public void setWindBearing(int windBearing) {
this.windBearing = windBearing;
}

/*@JsonProperty("visibility")*/
public double getVisibility() {
return visibility;
}

/*@JsonProperty("visibility")*/
public void setVisibility(double visibility) {
this.visibility = visibility;
}

/*@JsonProperty("cloudCover")*/
public double getCloudCover() {
return cloudCover;
}

/*@JsonProperty("cloudCover")*/
public void setCloudCover(double cloudCover) {
this.cloudCover = cloudCover;
}

/*@JsonProperty("pressure")*/
public double getPressure() {
return pressure;
}

/*@JsonProperty("pressure")*/
public void setPressure(double pressure) {
this.pressure = pressure;
}

/*@JsonProperty("ozone")*/
public double getOzone() {
return ozone;
}

/*@JsonProperty("ozone")*/
public void setOzone(double ozone) {
this.ozone = ozone;
}

/*@JsonAnyGetter*//*
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

*//*@JsonAnySetter*//*
public void setAdditionalProperties(String name, Object value) {
this.additionalProperties.put(name, value);
}

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
*/
    public Currently() {
    }

    public Currently(int time, String summary, String icon, double precipIntensity, double precipIntensityError, double precipProbability, String precipType, double temperature, double apparentTemperature, double dewPoint, double humidity, double windSpeed, int windBearing, double visibility, double cloudCover, double pressure, double ozone/*, Map<String, Object> additionalProperties*/) {
        this.time = time;
        this.summary = summary;
        this.icon = icon;
        this.precipIntensity = precipIntensity;
        this.precipIntensityError = precipIntensityError;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.dewPoint = dewPoint;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windBearing = windBearing;
        this.visibility = visibility;
        this.cloudCover = cloudCover;
        this.pressure = pressure;
        this.ozone = ozone;
      /*  this.additionalProperties = additionalProperties;*/
    }
}