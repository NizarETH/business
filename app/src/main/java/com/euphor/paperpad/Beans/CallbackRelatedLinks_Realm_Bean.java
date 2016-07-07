package com.euphor.paperpad.Beans;

import android.util.Log;

import com.euphor.paperpad.InterfaceRealm.CallbackRelatedLinks_Realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Euphor on 07/05/2015.
 */
public class CallbackRelatedLinks_Realm_Bean extends RealmObject implements CallbackRelatedLinks_Realm{
    private Child_pages child_pages;
  //  private  PopupWindow pw;
     private RealmList<Child_pages> objects;
    private int indexClicked;
  //  private LinearLayout llRelated;
    private int ChildPageClick;
    @Override
    public int getChildPageClick(Child_pages child_pages, RealmList<Child_pages> objects, int indexClicked) {
        Log.i("child_pages", "" + child_pages.toString());

        objects.remove(indexClicked);
        objects.add(indexClicked, child_pages);

       return 0;
    }

    public int getChildPageClick() {
        return ChildPageClick;
    }

    public void setChildPageClick(int childPageClick) {
        ChildPageClick = childPageClick;
    }

    public Child_pages getChild_pages() {
        return child_pages;
    }

    public void setChild_pages(Child_pages child_pages) {
        this.child_pages = child_pages;
    }



    public RealmList<Child_pages> getObjects() {
        return objects;
    }

    public void setObjects(RealmList<Child_pages> objects) {
        this.objects = objects;
    }

    public int getIndexClicked() {
        return indexClicked;
    }

    public void setIndexClicked(int indexClicked) {
        this.indexClicked = indexClicked;
    }



    public CallbackRelatedLinks_Realm_Bean() {
    }

    public CallbackRelatedLinks_Realm_Bean(Child_pages child_pages, RealmList<Child_pages> objects, int indexClicked) {
        this.child_pages = child_pages;

        this.objects = objects;
        this.indexClicked = indexClicked;

    }
}
