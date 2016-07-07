package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Location extends RealmObject {


    @PrimaryKey
    private int id;
    //private int id_location;
	
	private Locations_group locations_group;

	private String title;
	private String image;
	private Coordinates coordinates;
	private String button_text;
	private String text;
	private String postal_code;
	private boolean	display_details;
	private boolean is_default;
	private String link_type;
	private String link_page_id;
	private String link_web_url;
	private Illustration illustration;

/*    public int getId_location() {
        return id_location;
    }

    public void setId_location(int id_location) {
        this.id_location = id_location;
    }*/

    public Locations_group getLocations_group() {
        return locations_group;
    }

    public void setLocations_group(Locations_group locations_group) {
        this.locations_group = locations_group;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        try {
            illustration= Utils1.Download_images(image,illustration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.image = image;

    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public boolean isDisplay_details() {
        return display_details;
    }

    public void setDisplay_details(boolean display_details) {
        this.display_details = display_details;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getLink_page_id() {
        return link_page_id;
    }

    public void setLink_page_id(String link_page_id) {
        this.link_page_id = link_page_id;
    }

    public String getLink_web_url() {
        return link_web_url;
    }

    public void setLink_web_url(String link_web_url) {
        this.link_web_url = link_web_url;
    }

   public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }
   public Location() {
    }

    public Location(/*int id_location,*/ Locations_group locations_group, int id, String title, String image, Coordinates coordinates, String button_text, String text, String postal_code, boolean display_details, boolean is_default, String link_type, String link_page_id, String link_web_url, Illustration illustration) {
       /* this.id_location = id_location;*/
        this.locations_group = locations_group;
        this.id = id;
        this.title = title;
        this.image = image;
        this.coordinates = coordinates;
        this.button_text = button_text;
        this.text = text;
        this.postal_code = postal_code;
        this.display_details = display_details;
        this.is_default = is_default;
        this.link_type = link_type;
        this.link_page_id = link_page_id;
        this.link_web_url = link_web_url;
      //  this.illustration = illustration;
    }
}
