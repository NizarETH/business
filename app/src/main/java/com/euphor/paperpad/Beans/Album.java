package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Album extends RealmObject {

    @PrimaryKey
    private int id_album;

    private int id;

	private Section section;
	private String title;
	private RealmList<Photo> photos = new RealmList<Photo>();
    private boolean selected ;
    private Illustration illustration;


    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
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

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public void setId(int id) {
        id_album= Increment.Primary_Album(id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(RealmList<Photo> photos) {
        this.photos = photos;

    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Album() {
    }

    public Album(int id_album, Section section, int id, String title, RealmList<Photo> photos, boolean selected) {
        this.id_album = id_album;
        this.section = section;
        this.id = id;
        this.title = title;
        this.photos = photos;
        this.selected = selected;
    }
}
