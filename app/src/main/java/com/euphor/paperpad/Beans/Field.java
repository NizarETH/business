/**
 * 
 */
package com.euphor.paperpad.Beans;

import com.euphor.paperpad.utils.Increment;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev02
 *
 */


public class Field  extends RealmObject {

  @PrimaryKey
  private int id;
  private int id_f;


	/*private Cart cart;*/
	private Contact contact;

	private String label;

	private String placeholder;
	private String type;

	private boolean optional;
	private RealmList<Value> values = new RealmList<Value>();
   private Allowed_period_day allowed_period_day;
	private Allowed_period_weekdays allowed_period_weekdays;

	private Disallowed_period_everyday disallowed_period_everyday;
	private String show;

    public int getId_f() {
        return id_f;
    }

    public void setId_f(int id_f) {
        this.id_f = id_f;
    }

    public boolean isOptional() {
        return optional;
    }



/*    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }*/

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_f= Increment.Primary_f(id);
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public RealmList<Value> getValues() {
        return values;
    }

    public void setValues(RealmList<Value> values) {
        this.values = values;
    }

    public Allowed_period_day getAllowed_period_day() {
        return allowed_period_day;
    }

    public void setAllowed_period_day(Allowed_period_day allowed_period_day) {
        this.allowed_period_day = allowed_period_day;
    }

    public Allowed_period_weekdays getAllowed_period_weekdays() {
        return allowed_period_weekdays;
    }

    public void setAllowed_period_weekdays(Allowed_period_weekdays allowed_period_weekdays) {
        this.allowed_period_weekdays = allowed_period_weekdays;
    }

    public Disallowed_period_everyday getDisallowed_period_everyday() {
        return disallowed_period_everyday;
    }

    public void setDisallowed_period_everyday(Disallowed_period_everyday disallowed_period_everyday) {
        this.disallowed_period_everyday = disallowed_period_everyday;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public Field() {
    }

   /* public int getId_field() {
        return id_field;
    }

    public void setId_field(int id_field) {
        this.id_field = id_field;
    }*/



    public Field(/*int id_field,*/ /*Cart cart,*/ Contact contact, int id, String label, String placeholder, String type, boolean optional, RealmList<Value> values, Allowed_period_day allowed_period_day, Allowed_period_weekdays allowed_period_weekdays, Disallowed_period_everyday disallowed_period_everyday, String show) {
       /* this.id_field = id_field;*/
      /*  this.cart = cart;*/
        this.contact = contact;
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
        this.type = type;
        this.optional = optional;
        this.values = values;
        this.allowed_period_day = allowed_period_day;
        this.allowed_period_weekdays = allowed_period_weekdays;
        this.disallowed_period_everyday = disallowed_period_everyday;
        this.show = show;
    }
}