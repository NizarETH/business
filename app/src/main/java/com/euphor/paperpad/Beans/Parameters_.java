package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmObject;


public class Parameters_ extends RealmObject {

//
    private int id;
    //private int id_params;
	
	private Tablet_home_grid tablet_home_grid;

	private int tiles_inner_padding;
	private int tiles_outer_margin;
	private String background_image;
	private Illustration illustration;

 /*   public int getId_params() {
        return id_params;
    }

    public void setId_params(int id_params) {
        this.id_params = id_params;
    }*/

    public Tablet_home_grid getTablet_home_grid() {
        return tablet_home_grid;
    }

    public void setTablet_home_grid(Tablet_home_grid tablet_home_grid) {
        this.tablet_home_grid = tablet_home_grid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTiles_inner_padding() {
        return tiles_inner_padding;
    }

    public void setTiles_inner_padding(int tiles_inner_padding) {
        this.tiles_inner_padding = tiles_inner_padding;
    }

    public int getTiles_outer_margin() {
        return tiles_outer_margin;
    }

    public void setTiles_outer_margin(int tiles_outer_margin) {
        this.tiles_outer_margin = tiles_outer_margin;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
        try {
            illustration= Utils1.Download_images(background_image,illustration);
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

    public Parameters_() {
    }

    public Parameters_(/*int id_params,*/ Tablet_home_grid tablet_home_grid, int id, int tiles_inner_padding, int tiles_outer_margin, String background_image/*, Illustration illustration*/) {
      //  this.id_params = id_params;
        this.tablet_home_grid = tablet_home_grid;
        this.id = id;
        this.tiles_inner_padding = tiles_inner_padding;
        this.tiles_outer_margin = tiles_outer_margin;
        this.background_image = background_image;
       // this.illustration = illustration;
    }
}
