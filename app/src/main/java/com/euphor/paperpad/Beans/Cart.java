/**
 * 
 */
package com.euphor.paperpad.Beans;



import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Cart extends RealmObject {
	
@PrimaryKey
private int id;


private String title;
private String tab_title;
private String empty_label;
private String alert_message;
private String form_label;
private String validation_button;
private String tab_icon;
private Disable_period disable_period;
private RealmList<Field> fields = new RealmList<Field>();
private Application application;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTab_title() {
        return tab_title;
    }

    public void setTab_title(String tab_title) {
        this.tab_title = tab_title;
    }

    public String getEmpty_label() {
        return empty_label;
    }

    public void setEmpty_label(String empty_label) {
        this.empty_label = empty_label;
    }

    public String getAlert_message() {
        return alert_message;
    }

    public void setAlert_message(String alert_message) {
        this.alert_message = alert_message;
    }

    public String getForm_label() {
        return form_label;
    }

    public void setForm_label(String form_label) {
        this.form_label = form_label;
    }

    public String getValidation_button() {
        return validation_button;
    }

    public void setValidation_button(String validation_button) {
        this.validation_button = validation_button;
    }

    public String getTab_icon() {
        return tab_icon;
    }

    public void setTab_icon(String tab_icon) {
        this.tab_icon = tab_icon;
    }

    public Disable_period getDisable_period() {
        return disable_period;
    }

    public void setDisable_period(Disable_period disable_period) {
        this.disable_period = disable_period;
    }

    public RealmList<Field> getFields() {
        return fields;
    }

    public void setFields(RealmList<Field> fields) {
        this.fields = fields;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Cart() {
    }

    public Cart(int id, String title, String tab_title, String empty_label, String alert_message, String form_label, String validation_button, String tab_icon, Disable_period disable_period, RealmList<Field> fields, Application application) {
        this.id = id;
        this.title = title;
        this.tab_title = tab_title;
        this.empty_label = empty_label;
        this.alert_message = alert_message;
        this.form_label = form_label;
        this.validation_button = validation_button;
        this.tab_icon = tab_icon;
        this.disable_period = disable_period;
        this.fields = fields;
        this.application = application;
    }
}
