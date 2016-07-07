package com.euphor.paperpad.utils.dashboardParsing;



import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "artist", "song", "cover", "mp3" })*/
public class Playlist extends RealmObject {

	/*@JsonProperty("id")*/
    @PrimaryKey
	private int id;
	/*@JsonProperty("artist")*/
	private String artist;
	/*@JsonProperty("song")*/
	private String song;
/*	@JsonProperty("cover")*/
	private String cover;
	/*@JsonProperty("mp3")*/
	private String mp3;
	/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/
/*
	@JsonProperty("id")*/
	public int getId() {
		return id;
	}

/*	@JsonProperty("id")*/
	public void setId(int id) {
		this.id = id;
	}

	/*@JsonProperty("artist")*/
	public String getArtist() {
		return artist;
	}

/*	@JsonProperty("artist")*/
	public void setArtist(String artist) {
		this.artist = artist;
	}

/*	@JsonProperty("song")*/
	public String getSong() {
		return song;
	}

/*	@JsonProperty("song")*/
	public void setSong(String song) {
		this.song = song;
	}

	/*@JsonProperty("cover")*/
	public String getCover() {
		return cover;
	}

/*	@JsonProperty("cover")*/
	public void setCover(String cover) {
		this.cover = cover;
	}

/*	@JsonProperty("mp3")*/
	public String getMp3() {
		return mp3;
	}

	/*@JsonProperty("mp3")*/
	public void setMp3(String mp3) {
		this.mp3 = mp3;
	}

	/*@JsonAnyGetter*/
/*	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}*/

	/*@JsonAnySetter*/
	/*public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}*/

   /* public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/

    public Playlist() {
    }

    public Playlist(int id, String artist, String song, String cover, String mp3/*, Map<String, Object> additionalProperties*/) {
        this.id = id;
        this.artist = artist;
        this.song = song;
        this.cover = cover;
        this.mp3 = mp3;
        /*this.additionalProperties = additionalProperties;*/
    }
}