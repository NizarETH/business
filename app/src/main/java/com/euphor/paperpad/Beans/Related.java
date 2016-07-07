package com.euphor.paperpad.Beans;




import com.euphor.paperpad.utils.JsonSI;
import com.euphor.paperpad.utils.RelatedTitle1;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Related extends RealmObject implements RelatedTitle1 {
    /*@PrimaryKey*/
    private int id;
    private String title;
    private RealmList<MyString> list_pages ;
    private String pages ;
    private String title_categories;
    private String categories ;
    private RealmList<MyInteger> list=new RealmList<MyInteger>();
    private RelatedContactForm contact_form;
    private RelatedLocation relatedLocation;
    private RealmList<RelatedCatIds> relatedCatIds;
    private RealmList<RelatedPageId> relatedPageIds;
    private String relatedTitle;
    private boolean isPage = false;

    public boolean isPage() {
        return isPage;
    }

    public void setPage(boolean isPage) {
        this.isPage = isPage;
    }

    public void setRelatedTitle(String relatedTitle) {
        this.relatedTitle = relatedTitle;
    }

    public RealmList<RelatedPageId> getRelatedPageIds() {
        return relatedPageIds;
    }

    public RealmList<MyString> getList_pages() {
        return list_pages;
    }

    public void setList_pages(RealmList<MyString> list_pages) {
        this.list_pages = list_pages;
    }

    public void setRelatedPageIds(RealmList<RelatedPageId> relatedPageIds) {
        this.relatedPageIds = relatedPageIds;
    }

    public RealmList<RelatedCatIds> getRelatedCatIds() {
        return relatedCatIds;
    }

    public void setRelatedCatIds(RealmList<RelatedCatIds> relatedCatIds) {

        this.relatedCatIds = relatedCatIds;
    }

    public RelatedLocation getRelatedLocation() {
        return relatedLocation;
    }

    public void setRelatedLocation(RelatedLocation relatedLocation) {
        this.relatedLocation = relatedLocation;
    }

    public Related() {
    }

    public String getTitle_categories() {
        return title_categories;
    }

    public void setTitle_categories(String title_categories) {
        this.title_categories = title_categories;
    }

    public RelatedContactForm getContact_form() {
        return contact_form;
    }

    public void setContact_form(RelatedContactForm contact_form) {
        this.contact_form = contact_form;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        if(!title.isEmpty()) {
            setPage(true);
        this.title = title; }
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {

        list_pages=JsonSI.JsonToString(pages);
        this.pages = pages;
    }




    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        //fonction
        list=  JsonSI.JsonToInt(categories);
        this.categories = categories;

        /*RelatedCatIds relatedCatIds1 = new RelatedCatIds();
        relatedCatIds1.setId(list.first().getMyInt());
        relatedCatIds.add(relatedCatIds1);
        relatedCatIds.size();*/
    }

    public RealmList<MyInteger> getList() {
        return list;
    }

    public void setList(RealmList<MyInteger> list) {
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Related(int id, String title, String pages, String title_categories, String categories, RealmList<MyInteger> list) {
       this.id = id;
        this.title = title;
        this.pages = pages;
        this.title_categories = title_categories;
        this.categories = categories;
        this.list = list;
    }

    @Override
    public String getRelatedTitle() {
        if (isPage()) {
            return getTitle();
        } else {
            return getTitle_categories();
        }
    }
}