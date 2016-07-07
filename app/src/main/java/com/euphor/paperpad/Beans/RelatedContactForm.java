package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.RelatedItem1;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RelatedContactForm extends RealmObject implements RelatedItem1 {

    @PrimaryKey
    private int id_con;

    private int id;
	private String title;
    private String ItemTitle1;
    private String ItemIntro1;
    private Illustration ItemIllustration1;
    private String ItemType1;
    private RealmList<RelatedCatIds> RelatedCategories1;
    private int ItemExtra1;
    /****************************/
    private RelatedContactForm relatedContactForm;
    private RelatedLocation relatedLocation;

    public void setItemTitle1(String itemTitle1) {
        ItemTitle1 = itemTitle1;
    }

    public void setItemIntro1(String itemIntro1) {
        ItemIntro1 = itemIntro1;
    }

    public void setItemIllustration1(Illustration itemIllustration1) {
        ItemIllustration1 = itemIllustration1;
    }

    public void setItemType1(String itemType1) {
        ItemType1 = itemType1;
    }

    public void setRelatedCategories1(RealmList<RelatedCatIds> relatedCategories1) {
        RelatedCategories1 = relatedCategories1;
    }

    public void setItemExtra1(int itemExtra1) {
        ItemExtra1 = itemExtra1;
    }

    public void setRelatedContactForm(RelatedContactForm relatedContactForm) {
        this.relatedContactForm = relatedContactForm;
    }

    public void setRelatedLocation(RelatedLocation relatedLocation) {
        this.relatedLocation = relatedLocation;
    }

    public int getId_con() {
        return id_con;
    }

    public void setId_con(int id_con) {
        this.id_con = id_con;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_con= Increment.Primary_cf(id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RelatedContactForm() {
    }

    public RelatedContactForm(/*int id_generated,*/ int id, String title) {
       // this.id_generated = id_generated;
        this.id = id;
        this.title = title;
    }

    @Override
    public String getItemTitle1() {
        return this.title;
    }

    @Override
    public String getItemIntro1() {
        return null;
    }

    @Override
    public Illustration getItemIllustration1() {
        return null;
    }

    @Override
    public String getItemType1() {
        return null;
    }

    @Override
    public RealmList<RelatedCatIds> getRelatedCategories1() {
        return null;
    }

    @Override
    public int getItemExtra1() {
        return 0;
    }

    @Override
    public RelatedContactForm getRelatedContactForm() {
        return null;
    }

    @Override
    public RelatedLocation getRelatedLocation() {
        return null;
    }
}
