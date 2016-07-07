package com.euphor.paperpad.Beans;


import io.realm.RealmList;
import io.realm.RealmObject;


public class Tablet_home_grid extends RealmObject {

  //
	private int id;

//	private Application application;
	private Parameters_ parameters;
	private RealmList<Tile> tiles = new RealmList<Tile>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

 /*   public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
*/
    public Parameters_ getParameters() {
        return parameters;
    }

    public void setParameters(Parameters_ parameters) {
        this.parameters = parameters;
    }

    public RealmList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(RealmList<Tile> tiles) {
        this.tiles = tiles;
    }

    public Tablet_home_grid() {
    }

    public Tablet_home_grid(int id,/* Application application,*/ Parameters_ parameters, RealmList<Tile> tiles) {
        this.id = id;
      //  this.application = application;
        this.parameters = parameters;
        this.tiles = tiles;
    }
}
