/**
 * 
 */
package com.euphor.paperpad.Beans;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev02
 *
 */

public class Formule extends RealmObject {
	

   @PrimaryKey
	private int id;
   private RealmList<FormuleElement> elements =new RealmList<>();
	private String price;

	private String name;

    private Illustration illustration;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public RealmList<FormuleElement> getElements() {
        return elements;
    }

    public void setElements(RealmList<FormuleElement> elements) {
        this.elements = elements;
    }

    public Formule() {
    }

    public Formule( String name,RealmList<FormuleElement> elements, String price) {
        this.elements = elements;
        this.price = price;
        this.name = name;
    }

    public Formule(int id,RealmList<FormuleElement> elements, String price, String name, Illustration illustration) {
        this.id = id;
        this.elements = elements;
        this.price = price;
        this.name = name;
        this.illustration = illustration;
    }

    public Formule(String price, String name, Illustration illustration) {
        this.price = price;
        this.name = name;
        this.illustration = illustration;
    }

    public Formule(int id, String price, String name/*, Illustration illustration*/) {
        this.id = id;
        this.price = price;
        this.name = name;
       /* this.illustration = illustration;*/
    }
}
