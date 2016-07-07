/**
 * 
 */
package com.euphor.paperpad.Beans;


import android.util.Log;

import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.JsonSI;
import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MyBox extends RealmObject {

    @PrimaryKey
    private int id_mb;

    private int id_coffret;

   // private int id;


    private Section section;
    private String titre_coffret;
    private String lien_web;
    private String introduction;
    private String description;
    private int prix;
    private int nombre_personnes;
    private int id_categorie;
    private Illustration illustration;
    private String validitee ;
    private RealmList<MyString> list_validitee=new RealmList<MyString>();
    private String lien_images ;
    private String duree_de_validitee;
    private RealmList<MyString> list=new RealmList<MyString>();
    private RealmList<StringValidityBox> validitee1=new RealmList<>();




    public int getId_mb() {
        return id_mb;
    }

    public RealmList<StringValidityBox> getValiditee1() {
        return validitee1;
    }

    public void setValiditee1(RealmList<StringValidityBox> validitee1) {
        this.validitee1 = validitee1;
    }

    public void setId_mb(int id_mb) {
        this.id_mb = id_mb;
    }

    public int getId_coffret() {
        return id_coffret;
    }

    public void setId_coffret(int id_coffret) {
        id_mb= Increment.Primary_MyBox(id_coffret);
        this.id_coffret = id_coffret;
    }



    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;


    }

    public String getTitre_coffret() {
        return titre_coffret;
    }

    public void setTitre_coffret(String titre_coffret) {
        this.titre_coffret = titre_coffret;
    }

    public String getLien_web() {
        return lien_web;
    }

    public void setLien_web(String lien_web) {
        this.lien_web = lien_web;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getNombre_personnes() {
        return nombre_personnes;
    }

    public void setNombre_personnes(int nombre_personnes) {
        this.nombre_personnes = nombre_personnes;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getValiditee() {
        return validitee;
    }

    public void setValiditee(String validitee) {
        list_validitee= JsonSI.JsonToString(validitee);
        this.validitee = validitee;
    }

    public String getLien_images() {
        return lien_images;
    }

    public void setLien_images(String lien_images) {
        list = JsonSI.JsonToStringImageFromMyBOX(lien_images);

        this.lien_images = lien_images;
        String paths = null;
        if (list.size() > 0) {
            for (MyString l : list) {
                try {

                    if ( l.getMyString() != null && !l.getMyString().isEmpty()) {
                        Log.e("l.getMyString() ::::>",""+l.getMyString());
                        illustration= Utils1.Download_images(l.getMyString(),illustration);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

}

    public String getDuree_de_validitee() {
        return duree_de_validitee;
    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public void setDuree_de_validitee(String duree_de_validitee) {
        this.duree_de_validitee = duree_de_validitee;
    }

    public MyBox() {
    }

    public RealmList<MyString> getList_validitee() {
        return list_validitee;
    }

    public void setList_validitee(RealmList<MyString> list_validitee) {
        this.list_validitee = list_validitee;
    }

    public RealmList<MyString> getList() {
        return list;
    }

    public void setList(RealmList<MyString> list) {
        this.list = list;
    }

    public MyBox(int id_coffret, Section section, String titre_coffret, String lien_web, String introduction, String description, int prix, int nombre_personnes, int id_categorie, Illustration illustration, String validitee, RealmList<MyString> list_validitee, String lien_images, String duree_de_validitee, RealmList<MyString> list) {

        this.id_coffret = id_coffret;
        this.section = section;
        this.titre_coffret = titre_coffret;
        this.lien_web = lien_web;
        this.introduction = introduction;
        this.description = description;
        this.prix = prix;
        this.nombre_personnes = nombre_personnes;
        this.id_categorie = id_categorie;
        this.illustration = illustration;
        this.validitee = validitee;
        this.list_validitee = list_validitee;
        this.lien_images = lien_images;
        this.duree_de_validitee = duree_de_validitee;
        this.list = list;
    }
}