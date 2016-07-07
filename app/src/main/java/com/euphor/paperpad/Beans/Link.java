package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.RelatedItem1;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Link  extends RealmObject implements RelatedItem1 {


 /*   @PrimaryKey*/
    private String title;

  /*  private	int id;*/

	private Linked linked;

	private String details;

	private String url;

	private String icon;

    private RealmList<RelatedCatIds> RelatedCategories1;
    private int ItemExtra1;
    private String ItemTitle1;
    private String ItemIntro1;
    private String ItemIllustration1;
    private String ItemType1;
    private RelatedContactForm relatedContactForm;
    private RelatedLocation relatedLocation;
   /* public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public void setRelatedCategories1(RealmList<RelatedCatIds> relatedCategories1) {
        RelatedCategories1 = relatedCategories1;
    }

    public void setItemExtra1(int itemExtra1) {
        ItemExtra1 = itemExtra1;
    }

    public void setItemTitle1(String itemTitle1) {
        ItemTitle1 = itemTitle1;
    }

    public void setItemIntro1(String itemIntro1) {
        ItemIntro1 = itemIntro1;
    }

    public void setItemIllustration1(String itemIllustration1) {
        ItemIllustration1 = itemIllustration1;
    }

    public void setItemType1(String itemType1) {
        ItemType1 = itemType1;
    }

    public void setRelatedContactForm(RelatedContactForm relatedContactForm) {
        this.relatedContactForm = relatedContactForm;
    }

    public void setRelatedLocation(RelatedLocation relatedLocation) {
        this.relatedLocation = relatedLocation;
    }

    public Linked getLinked() {
        return linked;
    }

    public void setLinked(Linked linked) {
        this.linked = linked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Link() {
    }

    public Link(/*int id,*/ Linked linked, String title, String details, String url, String icon) {
   /*     this.id = id;*/
        this.linked = linked;
        this.title = title;
        this.details = details;
        this.url = url;
        this.icon = icon;
    }

    @Override
    public String getItemTitle1() {
        return getTitle();
    }

    @Override
    public String getItemIntro1() {
        return getDetails();
    }

    @Override
    public String getItemIllustration1() {
        return getIcon();
    }

    @Override
    public String getItemType1() {
        return "Link";
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
