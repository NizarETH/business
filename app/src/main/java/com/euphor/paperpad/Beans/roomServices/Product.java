package com.euphor.paperpad.Beans.roomServices;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Product extends RealmObject {

    @PrimaryKey
    private int id;

private int page_id;

private int quantity;

private int price_id;

private String title;

private int unit_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice_id() {
        return price_id;
    }

    public void setPrice_id(int price_id) {
        this.price_id = price_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public Product() {
    }

    public Product(int id, int page_id, int quantity, int price_id, String title, int unit_price) {
        this.id = id;
        this.page_id = page_id;
        this.quantity = quantity;
        this.price_id = price_id;
        this.title = title;
        this.unit_price = unit_price;
    }
}