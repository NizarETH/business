package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.JsonSI;
import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Tile_ extends RealmObject {

   /*@PrimaryKey*/
   private int id;
  // private 	int id_tile_;
	

	private Phone_home_grid phone_home_grid;
	private String title;
	private int category_id;
	private String category_display;

	private int x;
	private int y;
	private int width;
	private int height;
	private int image_id;
	private String image;

	private String images ;
    private RealmList<MyString> listes_images = new  RealmList<MyString>();
	private String transition;
	private String type;
	private int opacity;
	private int padding;
	private String title_color;
	private boolean zoom_animation_effect;
    private Illustration illustration;

  /*  public int getId_tile_() {
        return id_tile_;
    }

    public void setId_tile_(int id_tile_) {
        this.id_tile_ = id_tile_;
    }*/

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {

        this.illustration = illustration;
    }

    public Phone_home_grid getPhone_home_grid() {
        return phone_home_grid;
    }

    public void setPhone_home_grid(Phone_home_grid phone_home_grid) {
        this.phone_home_grid = phone_home_grid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_display() {
        return category_display;
    }

    public void setCategory_display(String category_display) {
        this.category_display = category_display;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        listes_images = JsonSI.JsonToString(images);
        this.images = images;
    }

    public RealmList<MyString> getListes_images() {
        return listes_images;
    }

    public void setListes_images(RealmList<MyString> listes_images) {

        this.listes_images = listes_images;
        if (listes_images.size() != 0) {
            for (MyString l : listes_images) {
                if(l.getMyString() !=null) {
                    try {

                        if (!l.getMyString().isEmpty()) {

                            illustration= Utils1.Download_images(l.getMyString(), illustration);

                        }
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public String getTitle_color() {
        return title_color;
    }

    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }

    public boolean isZoom_animation_effect() {
        return zoom_animation_effect;
    }

    public void setZoom_animation_effect(boolean zoom_animation_effect) {
        this.zoom_animation_effect = zoom_animation_effect;
    }

    public Tile_() {
    }

    public Tile_(/*int id_tile_,*/ Phone_home_grid phone_home_grid, String title, int category_id, String category_display, int id, int x, int y, int width, int height, int image_id, String image, String images, String transition, String type, int opacity, int padding, String title_color, boolean zoom_animation_effect) {
       // this.id_tile_ = id_tile_;
        this.phone_home_grid = phone_home_grid;
        this.title = title;
        this.category_id = category_id;
        this.category_display = category_display;
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image_id = image_id;
        this.image = image;
        this.images = images;
        this.transition = transition;
        this.type = type;
        this.opacity = opacity;
        this.padding = padding;
        this.title_color = title_color;
        this.zoom_animation_effect = zoom_animation_effect;
    }
}