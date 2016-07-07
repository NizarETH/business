package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.Increment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Question extends RealmObject {

	@PrimaryKey
    private  int id_question;

    private int id;


	private Survey_ survey;
	private String type;
	private String label;
	private String placeholder;
	private Score score;
	private boolean optional;

    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_question= Increment.Primary_Question(id);
        this.id = id;
    }

    public Survey_ getSurvey() {
        return survey;
    }

    public void setSurvey(Survey_ survey) {
        this.survey = survey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public Question() {
    }

    public Question(/*int id_question,*/ int id, Survey_ survey, String type, String label, String placeholder, Score score, boolean optional) {
      //  this.id_question = id_question;
        this.id = id;
        this.survey = survey;
        this.type = type;
        this.label = label;
        this.placeholder = placeholder;
        this.score = score;
        this.optional = optional;
    }
}