package com.euphor.paperpad.utils;

import com.euphor.paperpad.Beans.RelatedCatIds;
import com.euphor.paperpad.Beans.RelatedContactForm;
import com.euphor.paperpad.Beans.RelatedLocation;

import io.realm.RealmList;

/**
 * Created by Euphor on 30/04/2015.
 */
public interface  RelatedItem1 {
    public  String getItemTitle1();
    public  String getItemIntro1();
    public Object getItemIllustration1();
    public String getItemType1();
    public RealmList<RelatedCatIds> getRelatedCategories1();
    public int getItemExtra1();
    public RelatedContactForm getRelatedContactForm();
    public RelatedLocation getRelatedLocation();
}

