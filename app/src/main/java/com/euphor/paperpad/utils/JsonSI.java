package com.euphor.paperpad.utils;



import android.util.Log;

import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;

import io.realm.RealmList;

/**
 * Created by Euphor on 18/03/2015.
 */
public class JsonSI {
    public static RealmList<MyString> JsonToString(String str)

    {
        RealmList<MyString> list=new RealmList<MyString>();

        String tab[] = str.split("[\",\\[\\]]+");
        for (int i = 0; i < tab.length; i++) {
            if (!tab[i].isEmpty()) {

                MyString myString = new MyString();
                myString.setMyString(tab[i]);
                list.add(myString);
            }
        }
        return list;
    }

    public static RealmList<MyString> JsonToStringImage(String str)

    {
        RealmList<MyString> list=new RealmList<MyString>();
        //String tab[] = str.split("\"");
        Log.e( ">>>>>>>",str+"");
        String tab[] = str.split("[\",\\[\\]]+");
        for (int i = 0; i < tab.length; i++) {
            if (!tab[i].isEmpty() && !tab[i].equals("[]") ) {
                //  tab[i] =  tab[i].replace("[]"," ");
                tab[i] = tab[i].replace("\\/","/");

                MyString myString = new MyString();
                myString.setMyString(tab[i]);
                list.add(myString);
                Log.e( ">>>>>>>",myString.getMyString()+"");
            }
        }
        return list;
    }
    public static RealmList<MyString> JsonToStringImageFromMyBOX(String str)
    {
        RealmList<MyString> list=new RealmList<MyString>();
        String tab[] = str.split("\"[\",\\[\\]]+");
        for (int i = 0; i < tab.length; i++) {
            if (!tab[i].isEmpty() && !tab[i].equals("[]") ) {
                //  tab[i] =  tab[i].replace("[]"," ");
                tab[i] = tab[i].replace("\\/","/");
                tab[i] = tab[i].replace("[\"","");
                tab[i] = tab[i].replace("\"]","");
                MyString myString = new MyString();
                myString.setMyString(tab[i]);
                list.add(myString);
            }
        }
        return list;
    }


    public static RealmList<MyInteger> JsonToInt(String str)

    {
        RealmList<MyInteger> list=new RealmList<MyInteger>();

        String tab[] = str.split("[\",\\[\\]]+");
        for (int i = 0; i < tab.length; i++) {
            if (!tab[i].isEmpty())  {
                MyInteger myInteger = new MyInteger();
                myInteger.setMyInt(Integer.valueOf(tab[i]));
                list.add(myInteger);
            }

        }
        return list;
    }
}
