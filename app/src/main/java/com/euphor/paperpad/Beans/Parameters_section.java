package com.euphor.paperpad.Beans;


import android.util.Log;

import com.euphor.paperpad.utils.MyBoxCat;
import com.euphor.paperpad.utils.MyBox_App;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Parameters_section extends RealmObject {

    @PrimaryKey
    private int id;

    //private int id_parameters;



	private Section section;

	private String url;
	private String console_url;
	private String console_id;
	private String appstore_url;
	private String schema_url;
	private String language_code;

/*    public int getId_parameters() {
        return id_parameters;
    }

    public void setId_parameters(int id_parameters) {
        this.id_parameters = id_parameters;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getConsole_url() {
        return console_url;
    }

    public void setConsole_url(String console_url) {
        this.console_url = console_url;
    }

    public String getConsole_id() {
        return console_id;
    }

    public void setConsole_id(String console_id) {
        if(console_id!=null && !console_id.isEmpty())
        {
            Log.e("My BOX Catégories:", "loading...");
            //MyBoxCat.getMyBox_Cat(console_id);
            Log.e("My BOX Application:", "loading...");
            MyBox_App.getMyBox_App(console_id);

        }

        this.console_id = console_id;

    }

    public String getAppstore_url() {
        return appstore_url;
    }

    public void setAppstore_url(String appstore_url) {
        this.appstore_url = appstore_url;
    }

    public String getSchema_url() {
        return schema_url;
    }

    public void setSchema_url(String schema_url) {
        this.schema_url = schema_url;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public Parameters_section() {
    }

    public Parameters_section(/*int id_parameters,*/ int id, Section section, String url, String console_url, String console_id, String appstore_url, String schema_url, String language_code) {
        //this.id_parameters = id_parameters;
        this.id = id;
        this.section = section;
        this.url = url;
        this.console_url = console_url;
        this.console_id = console_id;
        this.appstore_url = appstore_url;
        this.schema_url = schema_url;
        this.language_code = language_code;
    }
}
