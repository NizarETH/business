/**
 * 
 */
package com.euphor.paperpad.Beans;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ImagePage extends RealmObject {

    @PrimaryKey
	private int id;
	

	private Child_pages child_pages;
	private String string;
	private Illustration illustration;
	private Tile tile;
	private Tile_ tile_;

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

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Tile_ getTile_() {
        return tile_;
    }

    public void setTile_(Tile_ tile_) {
        this.tile_ = tile_;
    }

    public ImagePage() {
    }

    public ImagePage(int id, Child_pages child_pages, String string, /*Illustration illustration,*/ Tile tile, Tile_ tile_) {
        this.id = id;
        this.child_pages = child_pages;
        this.string = string;
       // this.illustration = illustration;
        this.tile = tile;
        this.tile_ = tile_;
    }
}
