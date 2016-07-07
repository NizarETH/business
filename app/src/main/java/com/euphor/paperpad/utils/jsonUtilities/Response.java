/**
 * 
 */
package com.euphor.paperpad.utils.jsonUtilities;

/**
 * @author euphordev02
 *
 */
public class Response {
	private int field_id;
	private Object value;
	private String type;
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
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
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
	public Response(int field_id, Object value, String type) {
		super();
		this.field_id = field_id;
		this.value = value;
		this.type = type;
	}
}
