/**
 * 
 */
package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.JsonSI;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Allowed_period_weekdays extends RealmObject {

    @PrimaryKey
	private int id_generated;

	private Field field;

	/*private RealmList<AllowedDay>  allowed_days = new RealmList<AllowedDay>();*/
	private String allowed_days;
    private RealmList<MyString> list_allowed_days = new RealmList<>();

	private String start_hour;

	private String end_hour;


    public int getId_generated() {
        return id_generated;
    }

    public void setId_generated(int id_generated) {
        this.id_generated = id_generated;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        if(field!=null) {
         int i=0;
            i++;
         id_generated= Increment.Primary_Allowed_period_weekdays(i);
        }
        this.field = field;
    }

    public String getAllowed_days() {
        return allowed_days;
    }

    public void setAllowed_days(String allowed_days) {
        list_allowed_days= JsonSI.JsonToString(allowed_days);
        this.allowed_days = allowed_days;
    }

    public RealmList<MyString> getList_allowed_days() {
        return list_allowed_days;
    }

    public void setList_allowed_days(RealmList<MyString> list_allowed_days) {
        this.list_allowed_days = list_allowed_days;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public Allowed_period_weekdays() {
    }


}