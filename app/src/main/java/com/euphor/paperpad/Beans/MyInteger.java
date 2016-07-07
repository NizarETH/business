package com.euphor.paperpad.Beans;

import io.realm.RealmObject;

/**
 * Created by Euphor on 10/03/2015.
 */
public class MyInteger extends RealmObject {


    /*@PrimaryKey*/
    private int id;
    private int myInt;


    public MyInteger(int id, int myInt) {
        this.id = id;
        this.myInt = myInt;
    }
    public MyInteger() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }
}
