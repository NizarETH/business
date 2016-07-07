package com.euphor.paperpad.Beans.interactiveMap;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by uness on 21/11/14.
 */

public class Distance extends RealmObject {

        @PrimaryKey
       private int key_id;

    private map map;
    private int horizontal;
    private int vertical;

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public map getMap() {
        return map;
    }

    public void setMap(map map) {
        this.map = map;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public Distance() {
    }

    public Distance(int key_id,map map, int horizontal, int vertical) {
        this.key_id = key_id;
        this.map = map;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
