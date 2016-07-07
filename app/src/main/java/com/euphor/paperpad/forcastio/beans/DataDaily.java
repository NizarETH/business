package com.euphor.paperpad.forcastio.beans;



import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"time",
"summary",
"icon",
"sunriseTime",
"sunsetTime",
"precipIntensity",
"precipIntensityMax",
"precipIntensityMaxTime",
"precipProbability",
"precipType",
"temperatureMin",
"temperatureMinTime",
"temperatureMax",
"temperatureMaxTime",
"apparentTemperatureMin",
"apparentTemperatureMinTime",
"apparentTemperatureMax",
"apparentTemperatureMaxTime",
"dewPoint",
"humidity",
"windSpeed",
"windBearing",
"visibility",
"cloudCover",
"pressure",
"ozone"
})*/
public class DataDaily extends RealmObject {

/*@JsonProperty("time")*/
private int time;
/*@JsonProperty("summary")*/
private String summary;
/*@JsonProperty("icon")*/
private String icon;
/*@JsonProperty("sunriseTime")*/
private int sunriseTime;
/*@JsonProperty("sunsetTime")*/
private int sunsetTime;
/*@JsonProperty("precipIntensity")*/
private double precipIntensity;
/*@JsonProperty("precipIntensityMax")*/
private double precipIntensityMax;
/*@JsonProperty("precipIntensityMaxTime")*/
private int precipIntensityMaxTime;
/*@JsonProperty("precipProbability")*/
private double precipProbability;
/*@JsonProperty("precipType")*/
private String precipType;
/*@JsonProperty("temperatureMin")*/
private double temperatureMin;
/*@JsonProperty("temperatureMinTime")*/
private int temperatureMinTime;
/*@JsonProperty("temperatureMax")*/
private double temperatureMax;
/*@JsonProperty("temperatureMaxTime")*/
private int temperatureMaxTime;
/*@JsonProperty("apparentTemperatureMin")*/
private double apparentTemperatureMin;
/*@JsonProperty("apparentTemperatureMinTime")*/
private int apparentTemperatureMinTime;
/*@JsonProperty("apparentTemperatureMax")*/
private double apparentTemperatureMax;
/*@JsonProperty("apparentTemperatureMaxTime")*/
private int apparentTemperatureMaxTime;
/*@JsonProperty("dewPoint")*/
private double dewPoint;
/*@JsonProperty("humidity")*/
private double humidity;
/*@JsonProperty("windSpeed")*/
private double windSpeed;
/*@JsonProperty("windBearing")*/
private int windBearing;
/*@JsonProperty("visibility")*/
private int visibility;
/*@JsonProperty("cloudCover")*/
private double cloudCover;
/*@JsonProperty("pressure")*/
private double pressure;
/*@JsonProperty("ozone")*/
private double ozone;
/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

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

/*@JsonProperty("sunriseTime")*/
public int getSunriseTime() {
return sunriseTime;
}

/*@JsonProperty("sunriseTime")*/
public void setSunriseTime(int sunriseTime) {
this.sunriseTime = sunriseTime;
}

/*@JsonProperty("sunsetTime")*/
public int getSunsetTime() {
return sunsetTime;
}

/*@JsonProperty("sunsetTime")*/
public void setSunsetTime(int sunsetTime) {
this.sunsetTime = sunsetTime;
}

/*@JsonProperty("precipIntensity")*/
public double getPrecipIntensity() {
return precipIntensity;
}

/*@JsonProperty("precipIntensity")*/
public void setPrecipIntensity(double precipIntensity) {
this.precipIntensity = precipIntensity;
}

/*@JsonProperty("precipIntensityMax")*/
public double getPrecipIntensityMax() {
return precipIntensityMax;
}

/*@JsonProperty("precipIntensityMax")*/
public void setPrecipIntensityMax(double precipIntensityMax) {
this.precipIntensityMax = precipIntensityMax;
}

/*@JsonProperty("precipIntensityMaxTime")*/
public int getPrecipIntensityMaxTime() {
return precipIntensityMaxTime;
}

/*@JsonProperty("precipIntensityMaxTime")*/
public void setPrecipIntensityMaxTime(int precipIntensityMaxTime) {
this.precipIntensityMaxTime = precipIntensityMaxTime;
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

/*@JsonProperty("temperatureMin")*/
public double getTemperatureMin() {
return temperatureMin;
}

/*@JsonProperty("temperatureMin")*/
public void setTemperatureMin(double temperatureMin) {
this.temperatureMin = temperatureMin;
}

/*@JsonProperty("temperatureMinTime")*/
public int getTemperatureMinTime() {
return temperatureMinTime;
}

/*@JsonProperty("temperatureMinTime")*/
public void setTemperatureMinTime(int temperatureMinTime) {
this.temperatureMinTime = temperatureMinTime;
}

/*@JsonProperty("temperatureMax")*/
public double getTemperatureMax() {
return temperatureMax;
}

/*@JsonProperty("temperatureMax")*/
public void setTemperatureMax(double temperatureMax) {
this.temperatureMax = temperatureMax;
}

/*@JsonProperty("temperatureMaxTime")*/
public int getTemperatureMaxTime() {
return temperatureMaxTime;
}

/*@JsonProperty("temperatureMaxTime")*/
public void setTemperatureMaxTime(int temperatureMaxTime) {
this.temperatureMaxTime = temperatureMaxTime;
}

/*@JsonProperty("apparentTemperatureMin")*/
public double getApparentTemperatureMin() {
return apparentTemperatureMin;
}

/*@JsonProperty("apparentTemperatureMin")*/
public void setApparentTemperatureMin(double apparentTemperatureMin) {
this.apparentTemperatureMin = apparentTemperatureMin;
}

/*@JsonProperty("apparentTemperatureMinTime")*/
public int getApparentTemperatureMinTime() {
return apparentTemperatureMinTime;
}

/*@JsonProperty("apparentTemperatureMinTime")*/
public void setApparentTemperatureMinTime(int apparentTemperatureMinTime) {
this.apparentTemperatureMinTime = apparentTemperatureMinTime;
}

/*@JsonProperty("apparentTemperatureMax")*/
public double getApparentTemperatureMax() {
return apparentTemperatureMax;
}

/*@JsonProperty("apparentTemperatureMax")*/
public void setApparentTemperatureMax(double apparentTemperatureMax) {
this.apparentTemperatureMax = apparentTemperatureMax;
}

/*@JsonProperty("apparentTemperatureMaxTime")*/
public int getApparentTemperatureMaxTime() {
return apparentTemperatureMaxTime;
}

/*@JsonProperty("apparentTemperatureMaxTime")*/
public void setApparentTemperatureMaxTime(int apparentTemperatureMaxTime) {
this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
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
public int getVisibility() {
return visibility;
}

/*@JsonProperty("visibility")*/
public void setVisibility(int visibility) {
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

    public DataDaily() {
    }

    public DataDaily(int time, String summary, String icon, int sunriseTime, int sunsetTime, double precipIntensity, double precipIntensityMax, int precipIntensityMaxTime, double precipProbability, String precipType, double temperatureMin, int temperatureMinTime, double temperatureMax, int temperatureMaxTime, double apparentTemperatureMin, int apparentTemperatureMinTime, double apparentTemperatureMax, int apparentTemperatureMaxTime, double dewPoint, double humidity, double windSpeed, int windBearing, int visibility, double cloudCover, double pressure, double ozone, Map<String, Object> additionalProperties) {
        this.time = time;
        this.summary = summary;
        this.icon = icon;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.precipIntensity = precipIntensity;
        this.precipIntensityMax = precipIntensityMax;
        this.precipIntensityMaxTime = precipIntensityMaxTime;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.temperatureMin = temperatureMin;
        this.temperatureMinTime = temperatureMinTime;
        this.temperatureMax = temperatureMax;
        this.temperatureMaxTime = temperatureMaxTime;
        this.apparentTemperatureMin = apparentTemperatureMin;
        this.apparentTemperatureMinTime = apparentTemperatureMinTime;
        this.apparentTemperatureMax = apparentTemperatureMax;
        this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
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