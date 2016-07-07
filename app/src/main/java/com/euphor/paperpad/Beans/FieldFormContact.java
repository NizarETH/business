/**
 * 
 */
package com.euphor.paperpad.Beans;

/**
 * @author euphordev02
 *
 */


import com.euphor.paperpad.utils.Increment;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class FieldFormContact extends RealmObject {


    @PrimaryKey
    private int id_generated;

    private int id;
	private Cart cart;
	private Contact contact;


	private String label;
	private String placeholder;
	private boolean optional;

	private String type;

	private RealmList<FormValue> values = new RealmList<FormValue>();

    public FieldFormContact() {
    }

    public int getId_generated() {
        return id_generated;
    }

    public void setId_generated(int id_generated) {
        this.id_generated = id_generated;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

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
        id_generated= Increment.Primary_fieldFormeContact(id);
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

    public boolean getOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RealmList<FormValue> getValues() {
        return values;
    }

    public void setValues(RealmList<FormValue> values) {
        this.values = values;
    }

    public FieldFormContact(/*int id_generated,*/ Cart cart, Contact contact, int id, String label, String placeholder, boolean optional, String type, RealmList<FormValue> values) {
        /*this.id_generated = id_generated;*/
        this.cart = cart;
        this.contact = contact;
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
        this.optional = optional;
        this.type = type;
        this.values = values;
    }
}