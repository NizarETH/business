package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.Increment;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Survey_ extends RealmObject {
	
    @PrimaryKey
    private int id_survey;

    private int id;

	private Section section;

	private String title;
    private String score_image_on;
    private String score_image_off;
	private RealmList<Question> questions = new RealmList<Question>();
	private RealmList<FieldSatisfaction> fields = new RealmList<FieldSatisfaction>();
    private Illustration score_illustration_on;
    private Illustration score_illustration_off;

    public int getId_survey() {
        return id_survey;
    }

    public void setId_survey(int id_survey) {
        this.id_survey = id_survey;
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
        id_survey= Increment.Primary_Survey(id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore_image_on() {
        return score_image_on;
    }

    public void setScore_image_on(String score_image_on) {
        this.score_image_on = score_image_on;
    }

    public String getScore_image_off() {
        return score_image_off;
    }

    public void setScore_image_off(String score_image_off) {
        this.score_image_off = score_image_off;
    }

    public RealmList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(RealmList<Question> questions) {
        this.questions = questions;
    }

    public RealmList<FieldSatisfaction> getFields() {
        return fields;
    }

    public void setFields(RealmList<FieldSatisfaction> fields) {
        this.fields = fields;
    }

    public Illustration getScore_illustration_on() {
        return score_illustration_on;
    }

    public void setScore_illustration_on(Illustration score_illustration_on) {
        this.score_illustration_on = score_illustration_on;
    }

    public Illustration getScore_illustration_off() {
        return score_illustration_off;
    }

    public void setScore_illustration_off(Illustration score_illustration_off) {
        this.score_illustration_off = score_illustration_off;
    }

    public Survey_() {
    }

    public Survey_(/*int id_survey,*/ Section section, int id, String title, String score_image_on, String score_image_off, RealmList<Question> questions, RealmList<FieldSatisfaction> fields, Illustration score_illustration_on, Illustration score_illustration_off) {
        //this.id_survey = id_survey;
        this.section = section;
        this.id = id;
        this.title = title;
        this.score_image_on = score_image_on;
        this.score_image_off = score_image_off;
        this.questions = questions;
        this.fields = fields;
        this.score_illustration_on = score_illustration_on;
        this.score_illustration_off = score_illustration_off;
    }
}