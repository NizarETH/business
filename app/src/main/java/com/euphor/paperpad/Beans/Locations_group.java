package com.euphor.paperpad.Beans;




import com.euphor.paperpad.utils.Increment;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Locations_group extends RealmObject {

    @PrimaryKey
    private int id;
	private  int id_locations_group;
	private Section section;
	//private int id;
	private String title;
    private String pin_icon;
	private String pin_color;
	private boolean display_in_captions;
	private boolean hide;
	private boolean is_itinerary;

	private RealmList<Location> locations = new RealmList<Location>();

   public int getId_locations_group() {
        return id_locations_group;
    }


    public void setId_locations_group(int id_locations_group) {
        this.id_locations_group = id_locations_group;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_locations_group = Increment.Primary_Key(id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPin_icon() {
        return pin_icon;
    }

    public void setPin_icon(String pin_icon) {
        this.pin_icon = pin_icon;
    }

    public String getPin_color() {
        return pin_color;
    }

    public void setPin_color(String pin_color) {
        this.pin_color = pin_color;
    }

    public boolean isDisplay_in_captions() {
        return display_in_captions;
    }

    public void setDisplay_in_captions(boolean display_in_captions) {
        this.display_in_captions = display_in_captions;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isIs_itinerary() {
        return is_itinerary;
    }

    public void setIs_itinerary(boolean is_itinerary) {
        this.is_itinerary = is_itinerary;
    }

    public RealmList<Location> getLocations() {
        return locations;
    }

    public void setLocations(RealmList<Location> locations) {
        this.locations = locations;
    }

    public Locations_group() {
    }

    public Locations_group(/*int id_locations_group,*/ Section section, int id, String title, String pin_icon, String pin_color, boolean display_in_captions, boolean hide, boolean is_itinerary, RealmList<Location> locations) {
       // this.id_locations_group = id_locations_group;
        this.section = section;
        this.id = id;
        this.title = title;
        this.pin_icon = pin_icon;
        this.pin_color = pin_color;
        this.display_in_captions = display_in_captions;
        this.hide = hide;
        this.is_itinerary = is_itinerary;
        this.locations = locations;
    }
}
