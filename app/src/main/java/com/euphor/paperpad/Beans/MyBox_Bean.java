package com.euphor.paperpad.Beans;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Euphor on 03/04/2015.
 */
public class MyBox_Bean extends RealmObject {
   @PrimaryKey
   private int id;

   private RealmList<MyBox> myBox;

    public MyBox_Bean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<MyBox> getMyBox() {
        return myBox;
    }

    public void setMyBox(RealmList<MyBox> myBox) {
        this.myBox = myBox;
    }

    public MyBox_Bean(int id, RealmList<MyBox> myBox) {
        this.id = id;
        this.myBox = myBox;
    }
}
