/**
 * 
 */
package com.euphor.paperpad.Beans;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev02
 *
 */

public class AllowedDay extends RealmObject {
	
    @PrimaryKey
	private int id_generated;

	private String day;
	private Allowed_period_weekdays allowed_period_weekdays;

    public int getId_generated() {
        return id_generated;
    }

    public void setId_generated(int id_generated) {
        this.id_generated = id_generated;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Allowed_period_weekdays getAllowed_period_weekdays() {
        return allowed_period_weekdays;
    }

    public void setAllowed_period_weekdays(Allowed_period_weekdays allowed_period_weekdays) {
        this.allowed_period_weekdays = allowed_period_weekdays;
    }

    public AllowedDay() {
    }

    public AllowedDay(int id_generated, String day, Allowed_period_weekdays allowed_period_weekdays) {
        this.id_generated = id_generated;
        this.day = day;
        this.allowed_period_weekdays = allowed_period_weekdays;
    }
}
