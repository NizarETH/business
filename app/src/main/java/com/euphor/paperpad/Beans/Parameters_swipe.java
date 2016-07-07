/**
 * 
 */
package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Parameters_swipe extends RealmObject {

    @PrimaryKey
	private int id;
	private Home_swipe home_swipe;
	private Illustration illustration ;//=new Illustration();
	private String logo;
	private String color;
	private int section_id;

	private int category_id;
	private int page_id;
	private String logo_position;
	private String button_title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Home_swipe getHome_swipe() {
        return home_swipe;
    }

    public void setHome_swipe(Home_swipe home_swipe) {
        this.home_swipe = home_swipe;
    }

    public String getLogo() {
        return logo;
    }


    public void setLogo(String logo) {

        try {
            illustration= Utils1.Download_images(logo,illustration);
        } catch (IOException e) {
            e.printStackTrace();
        }


            this.logo = logo;
        //}
    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public String getLogo_position() {
        return logo_position;
    }

    public void setLogo_position(String logo_position) {
        this.logo_position = logo_position;
    }

    public String getButton_title() {
        return button_title;
    }

    public void setButton_title(String button_title) {
        this.button_title = button_title;
    }

    public Parameters_swipe() {
    }

    public Parameters_swipe(int id, Home_swipe home_swipe, /*Illustration illustration,*/ String logo, String color, int section_id, int category_id, int page_id, String logo_position, String button_title) {
        this.id = id;
        this.home_swipe = home_swipe;
       // this.illustration = illustration;
        this.logo = logo;
        this.color = color;
        this.section_id = section_id;
        this.category_id = category_id;
        this.page_id = page_id;
        this.logo_position = logo_position;
        this.button_title = button_title;
    }
}