package com.euphor.paperpad.Beans.interactiveMap;



import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class map extends RealmObject {

    @PrimaryKey
   private int key_id;

    private Section section;
    private int id;
    private String title;
    private String image;
    private Illustration illustration;
    private Origin origin;
    private Distance distance;
    private RealmList<Point> points = new RealmList<Point>();

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public RealmList<Point> getPoints() {
        return points;
    }

    public void setPoints(RealmList<Point> points) {
        this.points = points;
    }

    public map() {
    }

    public map(int key_id, Section section, int id, String title, String image, Illustration illustration, Origin origin, Distance distance, RealmList<Point> points) {
        this.key_id = key_id;
        this.section = section;
        this.id = id;
        this.title = title;
        this.image = image;
        this.illustration = illustration;
        this.origin = origin;
        this.distance = distance;
        this.points = points;
    }
}
