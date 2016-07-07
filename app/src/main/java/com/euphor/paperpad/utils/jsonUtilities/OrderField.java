/**
 * 
 */
package com.euphor.paperpad.utils.jsonUtilities;

import java.util.Date;

/**
 * @author euphordev02
 *
 */
public class OrderField {
	private int field_id;
	private String value;
	private String type;
	private Date value_date;
	private Date value_date_time;
	private ValuePeriod value_period;
    private int value_id;


    public int getValue_id() {
        return value_id;
    }

    public void setValue_id(int value_id) {
        this.value_id = value_id;
    }


	/**
	 * @return the field_id
	 */
	public int getField_id() {
		return field_id;
	}
	/**
	 * @param field_id the field_id to set
	 */
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the value_date
	 */
	public Date getValue_date() {
		return value_date;
	}
	/**
	 * @param value_date the value_date to set
	 */
	public void setValue_date(Date value_date) {
		this.value_date = value_date;
	}
	/**
	 * @return the value_date_time
	 */
	public Date getValue_date_time() {
		return value_date_time;
	}
	/**
	 * @param value_date_time the value_date_time to set
	 */
	public void setValue_date_time(Date value_date_time) {
		this.value_date_time = value_date_time;
	}
	/**
	 * @return the value_period
	 */
	public ValuePeriod getValue_period() {
		return value_period;
	}
	/**
	 * @param value_period the value_period to set
	 */
	public void setValue_period(ValuePeriod value_period) {
		this.value_period = value_period;
	}

    public OrderField(int field_id, String value, String type, Date value_date, Date value_date_time, ValuePeriod value_period, int value_id) {
        this.field_id = field_id;
        this.value = value;
        this.type = type;
        this.value_date = value_date;
        this.value_date_time = value_date_time;
        this.value_period = value_period;
        this.value_id = value_id;
    }

    public OrderField(int field_id) {
		super();
		this.field_id = field_id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
