/**
 * 
 */
package com.euphor.paperpad.Beans.paymentStripe;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev02
 * 
 */

public class ProduitStripe extends RealmObject {

    @PrimaryKey
	private int id_produit_stripe;

	private Payment_info info;

	private String id;
	private String price;

    public ProduitStripe() {
    }

    public ProduitStripe(int id_produit_stripe, Payment_info info, String id, String price) {
        this.id_produit_stripe = id_produit_stripe;
        this.info = info;
        this.id = id;
        this.price = price;
    }

    public int getId_produit_stripe() {
        return id_produit_stripe;
    }

    public void setId_produit_stripe(int id_produit_stripe) {
        this.id_produit_stripe = id_produit_stripe;
    }

    public Payment_info getInfo() {
        return info;
    }

    public void setInfo(Payment_info info) {
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
