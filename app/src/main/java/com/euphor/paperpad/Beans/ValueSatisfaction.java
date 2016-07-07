/**
 * 
 */
package com.euphor.paperpad.Beans;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class    ValueSatisfaction extends RealmObject {

   @PrimaryKey
   private int id;
	//private int id_generated;

	private FieldSatisfaction field;

	private String text;

   /* public int getId_generated() {
        return id_generated;
    }

    public void setId_generated(int id_generated) {
        this.id_generated = id_generated;
    }*/

    public FieldSatisfaction getField() {
        return field;
    }

    public void setField(FieldSatisfaction field) {
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

    public ValueSatisfaction() {
    }

    public ValueSatisfaction(/*int id_generated,*/ FieldSatisfaction field, int id, String text) {
      //  this.id_generated = id_generated;
        this.field = field;
        this.id = id;
        this.text = text;
    }
}