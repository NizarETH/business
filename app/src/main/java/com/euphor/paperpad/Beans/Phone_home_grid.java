package com.euphor.paperpad.Beans;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Phone_home_grid extends RealmObject {

    @PrimaryKey
	private int id;
	private Application application;
	private Parameters__ parameters;
	private RealmList<Tile_> tiles = new RealmList<Tile_>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Parameters__ getParameters() {
        return parameters;
    }

    public void setParameters(Parameters__ parameters) {
        this.parameters = parameters;
    }

    public RealmList<Tile_> getTiles() {
        return tiles;
    }

    public void setTiles(RealmList<Tile_> tiles) {
        this.tiles = tiles;
    }

    public Phone_home_grid() {
    }

    public Phone_home_grid(int id, Application application, Parameters__ parameters, RealmList<Tile_> tiles) {
        this.id = id;
        this.application = application;
        this.parameters = parameters;
        this.tiles = tiles;
    }
}
