package com.euphor.paperpad.utils.dashboardParsing;



import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "rss_path", "title" })*/
public class Feed extends RealmObject {

	/*@JsonProperty("rss_path")*/
	private String rss_path;
	
	
/*	@JsonProperty("title")*/
	private String title;
	
	private boolean isSelected;
	/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

	/*@JsonProperty("rss_path")*/
	public String getRss_path() {
		return rss_path;
	}

/*	@JsonProperty("rss_path")*/
	public void setRss_path(String rss_path) {
		this.rss_path = rss_path;
	}

/*	@JsonProperty("title")*/
	public String getTitle() {
		return title;
	}

/*	@JsonProperty("title")*/
	public void setTitle(String title) {
		this.title = title;
	}

	/*@JsonAnyGetter*/
	/*public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	*//*@JsonAnySetter*//*
	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}*/

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

 /*   public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/

    public Feed() {
    }

    public Feed(String rss_path, String title, boolean isSelected/*, Map<String, Object> additionalProperties*/) {
        this.rss_path = rss_path;
        this.title = title;
        this.isSelected = isSelected;
       /* this.additionalProperties = additionalProperties;*/
    }
}