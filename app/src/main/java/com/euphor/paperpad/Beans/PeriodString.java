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

public class PeriodString extends RealmObject {
	@PrimaryKey
	 private int id;

	private Disable_period disable_period;
	private String day;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Disable_period getDisable_period() {
        return disable_period;
    }

    public void setDisable_period(Disable_period disable_period) {
        this.disable_period = disable_period;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public PeriodString() {
    }

    public PeriodString(int id, Disable_period disable_period, String day) {
        this.id = id;
        this.disable_period = disable_period;
        this.day = day;
    }
}
