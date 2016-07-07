package com.euphor.paperpad.InterfaceRealm;

import com.euphor.paperpad.Beans.Child_pages;

import io.realm.RealmList;


/**
 * Created by Euphor on 07/05/2015.
 */
public interface CallbackRelatedLinks_Realm {

       public int getChildPageClick(Child_pages child_pages, RealmList<Child_pages> objects, int indexClicked);

}
