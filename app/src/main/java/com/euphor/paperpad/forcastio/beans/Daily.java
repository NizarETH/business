package com.euphor.paperpad.forcastio.beans;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"summary",
"icon",
"data"
})*/
public class Daily extends RealmObject {

/*@JsonProperty("summary")*/
private String summary;
/*@JsonProperty("icon")*/
private String icon;
/*@JsonProperty("data")*/
private RealmList<DataDaily> data = new RealmList<DataDaily>();
/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

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

/*@JsonProperty("data")*/
public RealmList<DataDaily> getData() {
return data;
}

/*@JsonProperty("data")*/
public void setData(RealmList<DataDaily> data) {
this.data = data;
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

    public Daily() {
    }

    public Daily(String summary, String icon, RealmList<DataDaily> data/*, Map<String, Object> additionalProperties*/) {
        this.summary = summary;
        this.icon = icon;
        this.data = data;
       /* this.additionalProperties = additionalProperties;*/
    }
}