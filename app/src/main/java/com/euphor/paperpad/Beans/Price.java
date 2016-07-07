/**
 * 
 */
package com.euphor.paperpad.Beans;


import io.realm.RealmObject;

/**
 * @author euphordev02
 *
 */

public class Price extends RealmObject {

    /*@PrimaryKey*/
    private int id_price;

	private Child_pages child_pages;
    private String id;

	private String label;

	private String amount;

	private String currency;

    public int getId_price() {
        return id_price;
    }

    public void setId_price(int id_price) {
        this.id_price = id_price;
    }

    public Child_pages getChild_pages() {
        return child_pages;
    }

    public void setChild_pages(Child_pages child_pages) {
        this.child_pages = child_pages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Price() {
    }

    public Price(int id_price, Child_pages child_pages, String id, String label, String amount, String currency) {
        this.id_price = id_price;
        this.child_pages = child_pages;
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.currency = currency;
    }
}
