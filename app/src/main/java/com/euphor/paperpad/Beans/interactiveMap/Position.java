package com.euphor.paperpad.Beans.interactiveMap;




import com.euphor.paperpad.utils.Increment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Position extends RealmObject {

     //@PrimaryKey
     private double x;

     private int key_id;


    private Point point;
   // private double x;
    private double y;

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {

        this.key_id = key_id;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        //key_id= Increment.Primary_Key(x);
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Position() {
    }

    public Position(int key_id, Point point, double x, double y) {
        this.key_id = key_id;
        this.point = point;
        this.x = x;
        this.y = y;
    }
}