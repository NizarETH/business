/**
 * 
 */
package com.euphor.paperpad.Beans;



import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Disable_period extends RealmObject {

    @PrimaryKey
	private int id;

    private Cart cart;
	private String start;

	private String end;

	private RealmList<MyString> days = new RealmList<MyString>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public RealmList<MyString> getDays() {
        return days;
    }

    public void setDays(RealmList<MyString> days) {
        this.days = days;
    }

    public Disable_period() {
    }

    public Disable_period(int id, Cart cart, String start, String end, RealmList<MyString> days) {
        this.id = id;
        this.cart = cart;
        this.start = start;
        this.end = end;
        this.days = days;
    }
}