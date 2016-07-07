package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FieldSatisfaction extends RealmObject {

        @PrimaryKey
        private int id_generated;

    private Survey_ survey_;
    private int id;


		private String label;
		private String placeholder;

		private String type;
		private boolean optional;

		private RealmList<ValueSatisfaction> values = new RealmList<ValueSatisfaction>();

    public int getId_generated() {
        return id_generated;
    }

    public void setId_generated(int id_generated) {
        this.id_generated = id_generated;
    }

    public Survey_ getSurvey_() {
        return survey_;
    }

    public void setSurvey_(Survey_ survey_) {
        this.survey_ = survey_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_generated= Increment.Primary_FieldSatisfaction(id);
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public RealmList<ValueSatisfaction> getValues() {
        return values;
    }

    public void setValues(RealmList<ValueSatisfaction> values) {
        this.values = values;
    }

    public FieldSatisfaction() {
    }

    public FieldSatisfaction(/*int id_generated,*/ Survey_ survey_, int id, String label, String placeholder, String type, boolean optional, RealmList<ValueSatisfaction> values) {
      /*  this.id_generated = id_generated;*/
        this.survey_ = survey_;
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
        this.type = type;
        this.optional = optional;
        this.values = values;
    }
}