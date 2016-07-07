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

public class Languages extends RealmObject {

    @PrimaryKey
    private int id;
    private RealmList<Language> languages = new RealmList<Language>();



    public Languages() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Languages(int id, RealmList<Language> languages) {
        this.id = id;
        this.languages = languages;
    }


    public RealmList<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(RealmList<Language> languages) {
        this.languages = languages;
    }





}
