package com.euphor.paperpad.Beans;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Extra_fields extends RealmObject {


    @PrimaryKey
    private int id;


	private Child_pages child_pages;
	private String auto_open_contact_form;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Child_pages getChild_pages() {
        return child_pages;
    }

    public void setChild_pages(Child_pages child_pages) {
        this.child_pages = child_pages;
    }

    public String getAuto_open_contact_form() {
        return auto_open_contact_form;
    }

    public void setAuto_open_contact_form(String auto_open_contact_form) {
        this.auto_open_contact_form = auto_open_contact_form;
    }

    public Extra_fields() {
    }

    public Extra_fields(int id, Child_pages child_pages, String auto_open_contact_form) {
        this.id = id;
        this.child_pages = child_pages;
        this.auto_open_contact_form = auto_open_contact_form;
    }
}
