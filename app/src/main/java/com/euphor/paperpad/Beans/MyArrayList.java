package com.euphor.paperpad.Beans;



import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Euphor on 17/03/2015.
 */
public class MyArrayList extends RealmObject {

    @PrimaryKey

    private int id;
    private MyString str;
    private RealmList<MyString> list=new RealmList<MyString>();


    public void setList(RealmList<MyString> list) {
        this.list = list;
    }

    public MyString getStr() {
        return str;
    }

    public RealmList<MyString> getList() {
        return list;
    }

    public void setStr(MyString str) {
        //fonction
        MyString myString=new MyString();
      String tab[]= str.toString().split("\"");
        for (int i = 0; i <tab.length ; i++) {
            if(tab[i]!="[" || tab[i]!="]" || tab[i]!=",")
            {
             myString.setMyString(tab[i]);
             list.add(myString);

            }
            else
                this.str = str;
        }




    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public MyArrayList() {
    }

    public MyArrayList(int id, MyString str, RealmList<MyString> list) {
        this.id = id;
        this.str = str;
        this.list = list;
    }
}
