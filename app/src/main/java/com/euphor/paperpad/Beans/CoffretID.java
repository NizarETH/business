/**
 * 
 */
package com.euphor.paperpad.Beans;



import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev02
 *
 */

public class CoffretID extends RealmObject {
	
    @PrimaryKey
	private int id;
	
	private int id_coffret;

	private CategoriesMyBox categoriesMyBox;

    public CoffretID(int id, int id_coffret, CategoriesMyBox categoriesMyBox) {
        this.id = id;
        this.id_coffret = id_coffret;
        this.categoriesMyBox = categoriesMyBox;
    }

    public CoffretID() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_coffret() {
        return id_coffret;
    }

    public void setId_coffret(int id_coffret) {
        this.id_coffret = id_coffret;
    }

    public CategoriesMyBox getCategoriesMyBox() {
        return categoriesMyBox;
    }

    public void setCategoriesMyBox(CategoriesMyBox categoriesMyBox) {
        this.categoriesMyBox = categoriesMyBox;
    }
}
