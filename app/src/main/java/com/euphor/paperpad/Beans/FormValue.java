/**
 * 
 */
package com.euphor.paperpad.Beans;

import com.euphor.paperpad.utils.Increment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FormValue extends RealmObject {

  @PrimaryKey
  private int id_generated;

    private int id;
	private FieldFormContact field;
	private String text;

    public int getId_generated() {
        return id_generated;
    }

    public void setId_generated(int id_generated) {
        this.id_generated = id_generated;
    }

    public FieldFormContact getField() {
        return field;
    }

    public void setField(FieldFormContact field) {
        this.field = field;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_generated= Increment.Primary_FormeValye(id);
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FormValue() {
    }

    public FormValue(int id_generated, FieldFormContact field, int id, String text) {
       /* this.id_generated = id_generated;*/
        this.field = field;
        this.id = id;
        this.text = text;
    }
}