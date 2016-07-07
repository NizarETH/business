package com.euphor.paperpad.utils.dashboardParsing;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "playlist", "weather_location", "rss_path", "category_id", "text", "timezone_code" })*/
public class Content extends RealmObject {


/*	@JsonProperty("weather_location")*/
	private Weather_location weather_location;

    /*public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/

    /*	@JsonProperty("feeds")*/
	private RealmList<Feed> feeds = new RealmList<Feed>();

/*	@JsonProperty("feeds")*/
	public RealmList<Feed> getFeeds() {
	return feeds;
	}

/*	@JsonProperty("feeds")*/
	public void setFeeds(RealmList<Feed> feeds) {
	this.feeds = feeds;
	}
	
	/*@JsonProperty("section_id")*/
	private int section_id;
	
/*	@JsonProperty("contact_form_id")*/
	private int contact_form_id;
	

/*	@JsonProperty("category_id")*/
	private int category_id;

/*	@JsonProperty("text")*/
	private String text;
	
	/*@JsonProperty("timezone_code")*/
	private String timezone_code;

/*	@JsonProperty("playlist")*/
	private RealmList<Playlist> playlist = new RealmList<Playlist>();
	/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

/*	@JsonProperty("weather_location")*/
	public Weather_location getWeather_location() {
		return weather_location;
	}

	/*@JsonProperty("weather_location")*/
	public void setWeather_location(Weather_location weather_location) {
		this.weather_location = weather_location;
	}


/*	@JsonProperty("category_id")*/
	public int getCategory_id() {
		return category_id;
	}

	/*@JsonProperty("category_id")*/
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	/*@JsonProperty("text")*/
	public String getText() {
		return text;
	}

/*	@JsonProperty("text")*/
	public void setText(String text) {
		this.text = text;
	}
	
	/*@JsonProperty("timezone_code")*/
	public String getTimezone_code() {
	return timezone_code;
	}

/*	@JsonProperty("timezone_code")*/
	public void setTimezone_code(String timezone_code) {
	this.timezone_code = timezone_code;
	}

/*	@JsonProperty("playlist")*/
	public RealmList<Playlist> getPlaylist() {
		return playlist;
	}

/*	@JsonProperty("playlist")*/
	public void setPlaylist(RealmList<Playlist> playlist) {
		this.playlist = playlist;
	}

	/*@JsonAnyGetter*/
	/*public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

*//*	@JsonAnySetter*//*
	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}*/

	/**
	 * @return the section_id
	 */
	public int getSection_id() {
		return section_id;
	}

	/**
	 * @param section_id the section_id to set
	 */
	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	/**
	 * @return the contact_form_id
	 */
	public int getContact_form_id() {
		return contact_form_id;
	}

	/**
	 * @param contact_form_id the contact_form_id to set
	 */
	public void setContact_form_id(int contact_form_id) {
		this.contact_form_id = contact_form_id;
	}

    public Content() {
    }

    public Content(Weather_location weather_location, RealmList<Feed> feeds, int section_id, int contact_form_id, int category_id, String text, String timezone_code, RealmList<Playlist> playlist, Map<String, Object> additionalProperties) {
        this.weather_location = weather_location;
        this.feeds = feeds;
        this.section_id = section_id;
        this.contact_form_id = contact_form_id;
        this.category_id = category_id;
        this.text = text;
        this.timezone_code = timezone_code;
        this.playlist = playlist;
        /*this.additionalProperties = additionalProperties;*/
    }
}
