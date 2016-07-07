package com.euphor.paperpad.utils.dashboardParsing;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "outer_margin", "inner_padding", "widgets" })*/
public class Dashboard extends RealmObject {

/*	@JsonProperty("outer_margin")*/
	private int outer_margin;
/*	@JsonProperty("inner_padding")*/
	private int inner_padding;
	/*@JsonProperty("widgets")*/
	private RealmList<Widget> widgets = new RealmList<Widget>();
	/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

/*	@JsonProperty("outer_margin")*/
	public int getOuter_margin() {
		return outer_margin;
	}

/*	@JsonProperty("outer_margin")*/
	public void setOuter_margin(int outer_margin) {
		this.outer_margin = outer_margin;
	}

/*	@JsonProperty("inner_padding")*/
	public int getInner_padding() {
		return inner_padding;
	}

/*	@JsonProperty("inner_padding")*/
	public void setInner_padding(int inner_padding) {
		this.inner_padding = inner_padding;
	}

/*	@JsonProperty("widgets")*/
	public RealmList<Widget> getWidgets() {
		return widgets;
	}

/*	@JsonProperty("widgets")*/
	public void setWidgets(RealmList<Widget> widgets) {
		this.widgets = widgets;
	}

/*	@JsonAnyGetter*/
	/*public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}*/

	/*@JsonAnySetter*/
	/*public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
*/
    public Dashboard() {
    }

    public Dashboard(int outer_margin, int inner_padding, RealmList<Widget> widgets) {
        this.outer_margin = outer_margin;
        this.inner_padding = inner_padding;
        this.widgets = widgets;
      /*  this.additionalProperties = additionalProperties;*/
    }
}