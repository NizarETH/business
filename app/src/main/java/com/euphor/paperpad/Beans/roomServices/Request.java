package com.euphor.paperpad.Beans.roomServices;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Request extends RealmObject {

    @PrimaryKey
    private int id_r;

private int id;

private int date;

private String status;

private String title;

private int total_amount;

private RealmList<Product> products = new RealmList<Product>();

    public int getId_r() {
        return id_r;
    }

    public void setId_r(int id_r) {
        this.id_r = id_r;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public RealmList<Product> getProducts() {
        return products;
    }

    public void setProducts(RealmList<Product> products) {
        this.products = products;
    }

    public Request() {
    }

    public Request(int id_r, int id, int date, String status, String title, int total_amount, RealmList<Product> products) {
        this.id_r = id_r;
        this.id = id;
        this.date = date;
        this.status = status;
        this.title = title;
        this.total_amount = total_amount;
        this.products = products;
    }
}