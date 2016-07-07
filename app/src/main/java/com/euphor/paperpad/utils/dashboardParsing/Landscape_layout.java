package com.euphor.paperpad.utils.dashboardParsing;



import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "x", "y", "width", "height" })*/
public class Landscape_layout extends RealmObject{

/*	@JsonProperty("x")*/
	private int x;
/*	@JsonProperty("y")*/
	private int y;
/*	@JsonProperty("width")*/
	private int width;
/*	@JsonProperty("height")*/
	private int height;
/*	private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

/*	@JsonProperty("x")*/
	public int getX() {
		return x;
	}

/*	@JsonProperty("x")*/
	public void setX(int x) {
		this.x = x;
	}

/*	@JsonProperty("y")*/
	public int getY() {
		return y;
	}

/*	@JsonProperty("y")*/
	public void setY(int y) {
		this.y = y;
	}

	/*@JsonProperty("width")*/
	public int getWidth() {
		return width;
	}

	/*@JsonProperty("width")*/
	public void setWidth(int width) {
		this.width = width;
	}

/*	@JsonProperty("height")*/
	public int getHeight() {
		return height;
	}

	/*@JsonProperty("height")*/
	public void setHeight(int height) {
		this.height = height;
	}

/*	@JsonAnyGetter*/
	/*public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

*//*	@JsonAnySetter*//*
	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}*/

/*    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/

    public Landscape_layout() {
    }

    public Landscape_layout(int x, int y, int width, int height/*, Map<String, Object> additionalProperties*/) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
       /* this.additionalProperties = additionalProperties;*/
    }
}