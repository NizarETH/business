/**
 * 
 */
package com.euphor.paperpad.Beans;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class FormuleElement extends RealmObject {
	
   @PrimaryKey
	private int id;

	private String price;
	private Formule formule;

	private String name;
	private int page_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public FormuleElement() {
    }


    public FormuleElement(String price, Formule formule, String name, int page_id) {
        this.price = price;
        this.formule = formule;
        this.name = name;
        this.page_id = page_id;
    }

    public FormuleElement(int id, String price, Formule formule, String name, int page_id) {
        this.id = id;
        this.price = price;
        this.formule = formule;
        this.name = name;
        this.page_id = page_id;
    }
}
