/**
 * 
 */
package com.euphor.paperpad.Beans;





import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.JsonSI;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class CategoriesMyBox extends RealmObject  {
	
    @PrimaryKey
    private int id_CatMyBox;

    private int id_categorie;

	private String name_categorie;
    private boolean selected = false;
	private Section section;
	private RealmList<MyInteger> list_id_coffret = new RealmList<MyInteger>();

    private String id_coffret;
    private CategoriesMyBox categoriesMyBox;

    public int getId_CatMyBox() {
        return id_CatMyBox;
    }

    public void setId_CatMyBox(int id_CatMyBox) {
        this.id_CatMyBox = id_CatMyBox;
    }

    public CategoriesMyBox getCategoriesMyBox() {
        return categoriesMyBox;
    }

    public void setCategoriesMyBox(CategoriesMyBox categoriesMyBox) {
        this.categoriesMyBox = categoriesMyBox;
    }

    public String getName_categorie() {
        return name_categorie;
    }

    public void setName_categorie(String name_categorie) {
        this.name_categorie = name_categorie;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setId_categorie(int id_categorie) {
    id_CatMyBox = Increment.Primary_CatMyBox(id_categorie);
        this.id_categorie = id_categorie;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public RealmList<MyInteger> getList_id_coffret() {
        return list_id_coffret;
    }

    public void setList_id_coffret(RealmList<MyInteger> list_id_coffret) {
        this.list_id_coffret = list_id_coffret;
    }


    public String getId_coffret() {
        return id_coffret;
    }

    public void setId_coffret(String id_coffret) {
      list_id_coffret= JsonSI.JsonToInt(id_coffret);
        this.id_coffret = id_coffret;
    }

    public CategoriesMyBox() {
    }

    public CategoriesMyBox(int id_categorie, boolean selected) {
        this.id_categorie = id_categorie;
        this.selected = selected;
    }

    public CategoriesMyBox( String name_categorie, int id_categorie, Section section, RealmList<MyInteger> lis_id_coffret, boolean selected, String id_coffret) {
        this.name_categorie = name_categorie;
        this.id_categorie = id_categorie;
        this.section = section;
        this.list_id_coffret = lis_id_coffret;
        this.id_coffret = id_coffret;
    }
}