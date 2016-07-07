package com.euphor.paperpad.Beans.paymentStripe;



import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Payment_info extends RealmObject {
   @PrimaryKey
	private int id_pi;


	private String order_id;
	private String key;
	private int amount;
	private double shipping;
	private RealmList<ProduitStripe> products = new RealmList<ProduitStripe>();

    public int getId_pi() {
        return id_pi;
    }

    public void setId_pi(int id_pi) {
        this.id_pi = id_pi;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public RealmList<ProduitStripe> getProducts() {
        return products;
    }

    public void setProducts(RealmList<ProduitStripe> products) {
        this.products = products;
    }

    public Payment_info() {

    }

    public Payment_info(int id_pi, String order_id, String key, int amount, double shipping, RealmList<ProduitStripe> products) {
        this.id_pi = id_pi;
        this.order_id = order_id;
        this.key = key;
        this.amount = amount;
        this.shipping = shipping;
        this.products = products;
    }
}