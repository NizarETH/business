/**
 * 
 */
package com.euphor.paperpad.Beans;





import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev04
 * 
 */

public class Event extends RealmObject {


    @PrimaryKey
    private int id;

	private Section section;
	private String title;
	private String intro;
	
	private String body;
	
	private String image;
	private Illustration illustration;
	private int link_page_identifier;
	private String date;
	private String link;
	private int agenda_group_id;
	private String duration;
	
	private boolean selected;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {


        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {

        this.image = image;
        try {
            illustration= Utils1.Download_images(image,illustration);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public int getLink_page_identifier() {
        return link_page_identifier;
    }

    public void setLink_page_identifier(int link_page_identifier) {
        this.link_page_identifier = link_page_identifier;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getAgenda_group_id() {
        return agenda_group_id;
    }

    public void setAgenda_group_id(int agenda_group_id) {
        this.agenda_group_id = agenda_group_id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Event() {
    }

    public Event(/*int id_event,*/ int id, Section section, String title, String intro, String body, String image, Illustration illustration, int link_page_identifier, String date, String link, int agenda_group_id, String duration, boolean selected) {
           /* this.id_event = id_event;*/
        this.id = id;
        this.section = section;
        this.title = title;
        this.intro = intro;
        this.body = body;
        this.image = image;
        this.illustration = illustration;
        this.link_page_identifier = link_page_identifier;
        this.date = date;
        this.link = link;
        this.agenda_group_id = agenda_group_id;
        this.duration = duration;
        this.selected = selected;
    }
}