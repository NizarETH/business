package com.euphor.paperpad.utils;

import com.euphor.paperpad.Beans.paymentStripe.Payment_info;
import com.euphor.paperpad.Beans.paymentStripe.ProduitStripe;

import java.util.Iterator;

import io.realm.Realm;

/**
 * Created by Mounia on 27/06/2015.
 */
public class Stripe {
    public static void SaveStripeInfo(Payment_info info , Realm realm) {
        realm.beginTransaction();
        realm.where(Payment_info.class).findAll().clear();
        realm.where(ProduitStripe.class).findAll().clear();


        for (Iterator<ProduitStripe> iterator = info.getProducts().iterator(); iterator.hasNext();) {
            ProduitStripe produit = (ProduitStripe) iterator.next();
            realm.copyToRealmOrUpdate(produit);
        }
        realm.copyToRealmOrUpdate(info);

        realm.commitTransaction();
    }
}
