package com.euphor.paperpad.Beans;

import com.euphor.paperpad.utils.Increment;

import io.realm.RealmObject;

/**
 * Created by Euphor on 10/03/2015.
 */
public class MyString extends RealmObject {

   /* @PrimaryKey*/
    private int id_s;
    private String myString;

    public MyString() {
    }

    public MyString(int id_s, String  myString) {
        this.id_s = id_s;
        this.myString = myString;
    }

    public int getid_s() {
        return id_s;
    }

    public void setid_s(int id_s) {
        id_s= Increment.Primary_MyString(id_s);
        this.id_s = id_s;
    }

    public String  getMyString() {
        return myString;
    }

    public void setMyString(String  myString) {
        this.myString = myString;
    }
}
