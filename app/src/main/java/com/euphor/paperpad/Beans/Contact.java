package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.RelatedItem1;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Contact extends RealmObject implements RelatedItem1 {

    @PrimaryKey
    private int id_con;
    private int id;

	private Section section;
	

	private String type;
	private String title;
	private String details;
	private String link;
	private String icon;
    private String text_intro;

	private RealmList<FieldFormContact> fields = new RealmList<FieldFormContact>();

	private String alert_message;

    private  String ItemTitle1;
    private  String ItemIntro1;
    private String ItemIllustration1;
    private String ItemType1;
    private RealmList<RelatedCatIds> RelatedCategories1;
    private int ItemExtra1;
    private RelatedContactForm RelatedContactForm;
    private RelatedLocation RelatedLocation;

    public int getId_con() {
        return id_con;
    }

    public void setId_con(int id_con) {
        this.id_con = id_con;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_con= Increment.Primary_con(id);
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getText_intro() {
        return text_intro;
    }

    public void setText_intro(String text_intro) {
        this.text_intro = text_intro;
    }

    public RealmList<FieldFormContact> getFields() {
        return fields;
    }

    public void setFields(RealmList<FieldFormContact> fields) {
        this.fields = fields;
    }

    public String getAlert_message() {
        return alert_message;
    }

    public void setAlert_message(String alert_message) {
        this.alert_message = alert_message;
    }

    public Contact() {
    }

    public Contact(/*int id_contact,*/ Section section, int id, String type, String title, String details, String link, String icon, String text_intro, RealmList<FieldFormContact> fields, String alert_message) {
        /*this.id_contact = id_contact;*/
        this.section = section;
        this.id = id;
        this.type = type;
        this.title = title;
        this.details = details;
        this.link = link;
        this.icon = icon;
        this.text_intro = text_intro;
        this.fields = fields;
        this.alert_message = alert_message;
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

    public void setRelatedCategories1(RealmList<RelatedCatIds> relatedCategories1) {
        RelatedCategories1 = relatedCategories1;
    }

    public void setItemExtra1(int itemExtra1) {
        ItemExtra1 = itemExtra1;
    }

    public void setRelatedContactForm(RelatedContactForm relatedContactForm) {
        RelatedContactForm = relatedContactForm;
    }

    public void setRelatedLocation(RelatedLocation relatedLocation) {
        RelatedLocation = relatedLocation;
    }

    @Override
    public String getItemTitle1() {
        return getTitle();
    }

    @Override
    public String getItemIntro1() {
        return getTitle();
    }

    @Override
    public String getItemIllustration1() {
        return getIcon();
    }

    @Override
    public String getItemType1() {
        return "form";
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
