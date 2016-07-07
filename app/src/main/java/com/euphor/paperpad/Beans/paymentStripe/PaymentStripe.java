package com.euphor.paperpad.Beans.paymentStripe;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PaymentStripe extends RealmObject {

 @PrimaryKey
 private int id;

   private String result;
     private Payment_info payment_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Payment_info getPayment_info() {
        return payment_info;
    }

    public void setPayment_info(Payment_info payment_info) {
        this.payment_info = payment_info;
    }

    public PaymentStripe() {
    }

    public PaymentStripe(int id, String result, Payment_info payment_info) {
        this.id = id;
        this.result = result;
        this.payment_info = payment_info;
    }
}