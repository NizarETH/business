package com.euphor.paperpad.Beans.interactiveMap;



import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Point extends RealmObject {


    @PrimaryKey
    private int id;

    private map map;

    private String title;
     private String image;
    private Illustration illustration;

    private String icon;
    private Position position;

    private String text;
    private String link;
     private String color;

  /*  public int getPoint_id() {
        return point_id;
    }

    public void setPoint_id(int point_id) {
        this.point_id = point_id;
    }*/

    public map getMap() {
        return map;
    }

    public void setMap(map map) {
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

      /*  point_id= Increment.Primary_Point(id);*/
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        try {

            illustration= Utils1.Download_images(image, illustration);

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Point() {
    }

    public Point(/*int point_id,*/ map map, int id, String title, String image, Illustration illustration, String icon, Position position, String text, String link, String color) {
      /*  this.point_id = point_id;*/
        this.map = map;
        this.id = id;
        this.title = title;
        this.image = image;
        this.illustration = illustration;
        this.icon = icon;
        this.position = position;
        this.text = text;
        this.link = link;
        this.color = color;
    }
}
