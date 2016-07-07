/**
 * 
 */
package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StringValidityBox extends RealmObject {

    @PrimaryKey
	private int id;
	private MyBox myBox;
	private String string;
    private RealmList<MyString> myStrings=new RealmList<>();

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
        id= Increment.Primary_StringValidity(myBox);
        this.myBox = myBox;
    }

    public RealmList<MyString> getMyStrings() {
        return myStrings;
    }

    public void setMyStrings(RealmList<MyString> myStrings) {
        this.myStrings = myStrings;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public StringValidityBox() {
    }

    public StringValidityBox(String string) {
        this.string = string;
    }

    public StringValidityBox(int id, MyBox myBox, String string) {
        this.id = id;
        this.myBox = myBox;
        this.string = string;
    }
}
