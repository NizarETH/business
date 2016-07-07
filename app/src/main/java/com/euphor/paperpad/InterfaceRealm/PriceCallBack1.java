package com.euphor.paperpad.InterfaceRealm;

import io.realm.RealmObject;

/**
 * Created by Euphor on 23/04/2015.
 */
public class PriceCallBack1 extends RealmObject {
   private String notify1;
   private int notifyNumber1;

    public String getNotify1() {
        return notify1;
    }

    public void setNotify1(String notify1) {
        this.notify1 = notify1;
    }

    public int getNotifyNumber1() {
        return notifyNumber1;
    }

    public void setNotifyNumber1(int notifyNumber1) {
        this.notifyNumber1 = notifyNumber1;
    }

    public PriceCallBack1() {
    }

    public PriceCallBack1(String notify1, int notifyNumber1) {
        this.notify1 = notify1;
        this.notifyNumber1 = notifyNumber1;
    }

}
