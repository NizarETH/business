package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ExtraField extends RealmObject {
	
    @PrimaryKey
   private 	int id_extrafield;
	

	private String multiline_title;
	
	private String auto_open_contact_form;

	private String auto_open_section_id;

	private String combine_images_animation;

	private String always_show_price_popover;
	private String formule_supplements_category_id;
	private String show_carts_in_price_popover;
	private String default_overlay_hides_content;
	private String scale_image_to_fit;
	private String details_button_title;

    private String latitude;
    private String longitude;
    private String address;
    private String featured;

    public int getId_extrafield() {
        return id_extrafield;
    }

    public void setId_extrafield(int id_extrafield) {
        this.id_extrafield = id_extrafield;
    }

    public String getMultiline_title() {
        return multiline_title;
    }

    public void setMultiline_title(String multiline_title) {
        this.multiline_title = multiline_title;
    }

    public String getAuto_open_contact_form() {
        return auto_open_contact_form;
    }

    public void setAuto_open_contact_form(String auto_open_contact_form) {
        this.auto_open_contact_form = auto_open_contact_form;
    }

    public String getAuto_open_section_id() {
        return auto_open_section_id;
    }

    public void setAuto_open_section_id(String auto_open_section_id) {

        this.auto_open_section_id = auto_open_section_id;
    }


    public String getCombine_images_animation() {
        return combine_images_animation;
    }

    public void setCombine_images_animation(String combine_images_animation) {
        this.combine_images_animation = combine_images_animation;
    }

    public String getAlways_show_price_popover() {
        return always_show_price_popover;
    }

    public void setAlways_show_price_popover(String always_show_price_popover) {
        this.always_show_price_popover = always_show_price_popover;
    }

    public String getFormule_supplements_category_id() {
        return formule_supplements_category_id;
    }

    public void setFormule_supplements_category_id(String formule_supplements_category_id) {
        this.formule_supplements_category_id = formule_supplements_category_id;
    }

    public String getShow_carts_in_price_popover() {
        return show_carts_in_price_popover;
    }

    public void setShow_carts_in_price_popover(String show_carts_in_price_popover) {
        this.show_carts_in_price_popover = show_carts_in_price_popover;
    }

    public String getDefault_overlay_hides_content() {
        return default_overlay_hides_content;
    }

    public void setDefault_overlay_hides_content(String default_overlay_hides_content) {
        this.default_overlay_hides_content = default_overlay_hides_content;
    }

    public String getScale_image_to_fit() {
        return scale_image_to_fit;
    }

    public void setScale_image_to_fit(String scale_image_to_fit) {
        this.scale_image_to_fit = scale_image_to_fit;
    }

    public String getDetails_button_title() {
        return details_button_title;
    }

    public void setDetails_button_title(String details_button_title) {
        this.details_button_title = details_button_title;
    }



    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        id_extrafield = Increment.Primary_ExtraFiels(address);
        this.address = address;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public ExtraField() {
    }

    public ExtraField(int id_extrafield, String multiline_title, String auto_open_contact_form, String auto_open_section_id, String combine_images_animation, String always_show_price_popover, String formule_supplements_category_id, String show_carts_in_price_popover, String default_overlay_hides_content, String scale_image_to_fit, String details_button_title) {
        this.id_extrafield = id_extrafield;
        this.multiline_title = multiline_title;
        this.auto_open_contact_form = auto_open_contact_form;
        this.auto_open_section_id = auto_open_section_id;
        this.combine_images_animation = combine_images_animation;
        this.always_show_price_popover = always_show_price_popover;
        this.formule_supplements_category_id = formule_supplements_category_id;
        this.show_carts_in_price_popover = show_carts_in_price_popover;
        this.default_overlay_hides_content = default_overlay_hides_content;
        this.scale_image_to_fit = scale_image_to_fit;
        this.details_button_title = details_button_title;
    }
}
