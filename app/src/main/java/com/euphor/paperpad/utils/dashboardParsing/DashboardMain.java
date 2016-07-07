package com.euphor.paperpad.utils.dashboardParsing;



import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"dashboard"
})*/
public class DashboardMain extends RealmObject {
    @PrimaryKey
    private int id_DashboardMain;

/*@JsonProperty("dashboard")*/
private Dashboard dashboard;
/*private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

/*@JsonProperty("dashboard")*/

public Dashboard getDashboard() {
return dashboard;
}

    public int getId_DashboardMain() {
        return id_DashboardMain;
    }

    public void setId_DashboardMain(int id_DashboardMain) {
        this.id_DashboardMain = id_DashboardMain;
    }

    public DashboardMain(int id_DashboardMain, Dashboard dashboard) {
        this.id_DashboardMain = id_DashboardMain;
        this.dashboard = dashboard;
    }

    /*@JsonProperty("dashboard")*/
public void setDashboard(Dashboard dashboard) {
this.dashboard = dashboard;
}


/*@JsonAnyGetter*/
/*public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}*/

/*@JsonAnySetter*/
/*public void setAdditionalProperties(String name, Object value) {
this.additionalProperties.put(name, value);
}

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/

    public DashboardMain() {
    }

    public DashboardMain(Dashboard dashboard, Map<String, Object> additionalProperties) {
        this.dashboard = dashboard;
       /* this.additionalProperties = additionalProperties;*/
    }
}