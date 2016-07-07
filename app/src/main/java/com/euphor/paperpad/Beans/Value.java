/**
 * 
 */
package com.euphor.paperpad.Beans;


import io.realm.RealmObject;


public class Value extends RealmObject {

   /* @PrimaryKey*/
    private int id;
	//private int id_v;

	private Field field;

	private String text;

/*    public int getId_v() {
        return id_v;
    }

    public void setId_v(int id_v) {
        this.id_v = id_v;
    }*/

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Value() {
    }

    public Value(/*int id_v,*/ Field field, int id, String text) {
        //this.id_v = id_v;
        this.field = field;
        this.id = id;
        this.text = text;
    }
}