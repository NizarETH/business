package com.euphor.paperpad.utils.dashboardParsing;



import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "title", "landscape_layout", "portait_layout",
		"type", "content" })*/
public class Widget extends RealmObject {

	/*@JsonProperty("id")*/
    @PrimaryKey
	private int id;
/*	@JsonProperty("title")*/
	private String title;
	/*@JsonProperty("landscape_layout")*/
	private Landscape_layout landscape_layout;
/*	@JsonProperty("portrait_layout")*/
	private Portrait_layout portrait_layout;
	/*@JsonProperty("type")*/
	private String type;
/*	@JsonProperty("content")*/
	private Content content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Landscape_layout getLandscape_layout() {
        return landscape_layout;
    }

    public void setLandscape_layout(Landscape_layout landscape_layout) {
        this.landscape_layout = landscape_layout;
    }

    public Portrait_layout getPortrait_layout() {
        return portrait_layout;
    }

    public void setPortrait_layout(Portrait_layout portrait_layout) {
        this.portrait_layout = portrait_layout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Widget() {
    }

    public Widget(int id, String title, Landscape_layout landscape_layout, Portrait_layout portrait_layout, String type, Content content) {
        this.id = id;
        this.title = title;
        this.landscape_layout = landscape_layout;
        this.portrait_layout = portrait_layout;
        this.type = type;
        this.content = content;
    }
}