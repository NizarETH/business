package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.RelatedTitle1;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Linked extends RealmObject implements RelatedTitle1 {

	/*@PrimaryKey*/
    private String title;
    private boolean isLink = false;
	private Child_pages child_pages;

	private RealmList<Link> links = new RealmList<Link>();
   private  String relatedTitle;
/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public boolean getisLink() {
        return isLink;
    }

    public void setLink(boolean isLink) {
        this.isLink = isLink;
    }

    public Child_pages getChild_pages() {
        return child_pages;
    }

    public void setChild_pages(Child_pages child_pages) {
        this.child_pages = child_pages;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<Link> getLinks() {
        return links;
    }

    public void setLinks(RealmList<Link> links) {
        this.links = links;
    }

    public Linked() {
    }

    public Linked(/*int id,*/ Child_pages child_pages, String title, RealmList<Link> links) {
      /*  this.id = id;*/
        this.child_pages = child_pages;
        this.title = title;
        this.links = links;
    }

    public void setRelatedTitle(String relatedTitle) {
        this.relatedTitle = relatedTitle;
    }

    @Override
    public String getRelatedTitle() {

            return getTitle();


    }
}
