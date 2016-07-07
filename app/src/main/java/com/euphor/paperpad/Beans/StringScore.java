package com.euphor.paperpad.Beans;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class StringScore extends RealmObject {
	
   @PrimaryKey
	private int id;
	private Score score;
	private String string;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public StringScore() {
    }

    public StringScore(int id, Score score, String string) {
        this.id = id;
        this.score = score;
        this.string = string;
    }
}
