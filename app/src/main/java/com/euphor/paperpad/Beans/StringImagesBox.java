/**
 * 
 */
package com.euphor.paperpad.Beans;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class StringImagesBox extends RealmObject {

    @PrimaryKey
	private int id;
	private MyBox myBox;
	private String string;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyBox getMyBox() {
        return myBox;
    }

    public void setMyBox(MyBox myBox) {
        this.myBox = myBox;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public StringImagesBox() {
    }
    public StringImagesBox(Illustration myString) {
    }

    public StringImagesBox(int id, MyBox myBox, String string) {
        this.id = id;
        this.myBox = myBox;
        this.string = string;
    }
}
