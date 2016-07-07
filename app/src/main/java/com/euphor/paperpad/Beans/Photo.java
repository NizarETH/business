package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Photo extends RealmObject {

    @PrimaryKey
    private String full_path;

	private Album album;
	private String medium_path;

	private Illustration illustration;

    /*public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getMedium_path() {
        return medium_path;
    }

    public void setMedium_path(String medium_path) {
        this.medium_path = medium_path;
    }

    public String getFull_path() {
        return full_path;
    }

    public void setFull_path(String full_path) {
        this.full_path = full_path;
        try {
            illustration= Utils1.Download_images(full_path,illustration);

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

    public Photo() {
    }

    public Photo(/*int id,*/ Album album, String medium_path, String full_path, Illustration illustration) {
     /*   this.id = id;*/
        this.album = album;
        this.medium_path = medium_path;
        this.full_path = full_path;
        this.illustration = illustration;
    }
}
