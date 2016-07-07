/**
 * 
 */
package com.euphor.paperpad.Beans;


import io.realm.RealmObject;

/**
 * @author euphordev02
 *
 */

public class Illustration extends RealmObject {

/*    @PrimaryKey*/
    private int id_i;

	private int id;

	private String link;
	private String path;
	private String fullLink;
	private String fullPath;
	private int originalHeight;
	private int originalWidth;
	private boolean downloaded;

	private MyBox myBox;

    public int getId_i() {
        return id_i;
    }

    public void setId_i(int id_i) {
        this.id_i = id_i;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
          // id++;
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
      //id_i= Increment.Primary_i(link);
        this.link = link;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullLink() {
        return fullLink;
    }

    public void setFullLink(String fullLink) {
        this.fullLink = fullLink;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public MyBox getMyBox() {
        return myBox;
    }

    public void setMyBox(MyBox myBox) {
        this.myBox = myBox;
    }

    public Illustration() {
        super();
    }

    public Illustration(String link, String path, String fullLink, String fullPath, boolean downloaded) {
        super();
        this.link = link;
        this.path = path;
        this.fullLink = fullLink;
        this.fullPath = fullPath;
        this.downloaded = downloaded;
    }

    public Illustration(int id, String link, String path, String fullLink, String fullPath, boolean downloaded) {
        this.id = id;
        this.link = link;
        this.path = path;
        this.fullLink = fullLink;
        this.fullPath = fullPath;
        this.downloaded = downloaded;
    }

    public Illustration(String link, String path, int originalHeight, int originalWidth) {
        this.link = link;
        this.path = path;
        this.originalHeight = originalHeight;
        this.originalWidth = originalWidth;
    }

    public Illustration(int id_i, String link, String path, int originalHeight, int originalWidth) {
        this.id_i = id_i;
        this.link = link;
        this.path = path;
        this.originalHeight = originalHeight;
        this.originalWidth = originalWidth;
    }

    public Illustration(int id, String link, String path, String fullLink, String fullPath, int originalHeight, int originalWidth, boolean downloaded, MyBox myBox) {
        this.id = id;
        this.link = link;
        this.path = path;
        this.fullLink = fullLink;
        this.fullPath = fullPath;
        this.originalHeight = originalHeight;
        this.originalWidth = originalWidth;
        this.downloaded = downloaded;
        this.myBox = myBox;
    }

    public Illustration( String link, String path, boolean downloaded) {

        this.link = link;
        this.path = path;
        this.downloaded = downloaded;
    }
}
