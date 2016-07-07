package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.JsonSI;
import com.euphor.paperpad.utils.Utils;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Score extends RealmObject {

    @PrimaryKey
    private int id_score;
    private String type;
    private int count;
	private String options ;
    private RealmList<MyString> list_options =new RealmList<>();
	private Question question;


    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        list_options= JsonSI.JsonToString(options);
        this.options = options;
    }

    public RealmList<MyString> getList_options() {
        return list_options;
    }

    public void setList_options(RealmList<MyString> list_options) {
        this.list_options = list_options;
    }

    public int getId_score() {
        return id_score;
    }

    public void setId_score(int id_score) {
        this.id_score = id_score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        id_score = Increment.Primary_score(type);
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Score() {
    }

    public Score(int id_score, String type, int count,  Question question) {
        this.id_score = id_score;
        this.type = type;
        this.count = count;

        this.question = question;
    }
}