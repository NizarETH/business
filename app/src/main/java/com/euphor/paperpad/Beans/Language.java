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

public class Language extends RealmObject {
    @PrimaryKey
	private int id_parameters;
	private Parameters parameters;
	private MyBox myBox;
	private String string;

    public Language() {
    }

    public int getId_parameters() {
        return id_parameters;
    }

    public void setId_parameters(int id_parameters) {
        this.id_parameters = id_parameters;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public MyBox getMyBox() {
        return myBox;
    }

    public void setMyBox(MyBox myBox) {
        this.myBox = myBox;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Language(int id_parameters, Parameters parameters, MyBox myBox, String string) {
        this.id_parameters = id_parameters;
        this.parameters = parameters;
        this.myBox = myBox;
        this.string = string;
    }
}
