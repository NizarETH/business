/**
 * 
 */
package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.Increment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev02
 * 
 */


public class Allowed_period_day extends RealmObject {

   @PrimaryKey
   private int id_generated;

	private Field field;

	private int day;

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
        if(field !=null) {
            int i=0;
            i++;
            id_generated = Increment.Primary_Allowed_period_Day(i);
        }
        this.field = field;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public Allowed_period_day() {
    }

    public Allowed_period_day(int id_generated, Field field, int day, String start_hour, String end_hour) {
        this.id_generated = id_generated;
        this.field = field;
        this.day = day;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
    }
}