package com.euphor.paperpad.Beans;



import io.realm.RealmObject;

public class Radio extends RealmObject {
	
    /*@PrimaryKey*/
    private int id;

    //private int id_radio;

    private Section section;
	private String title;
	private String url;
	private String cover;
    private boolean selected;
    private boolean isPlaying;

   /* public int getId_radio() {
        return id_radio;
    }

    public void setId_radio(int id_radio) {
        this.id_radio = id_radio;
    }
*/
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Radio() {
    }

    public Radio(/*int id_radio,*/ int id, Section section, String title, String url, String cover, boolean selected, boolean isPlaying) {
       // this.id_radio = id_radio;
        this.id = id;
        this.section = section;
        this.title = title;
        this.url = url;
        this.cover = cover;
        this.selected = selected;
        this.isPlaying = isPlaying;
    }
}