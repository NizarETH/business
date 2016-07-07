package com.euphor.paperpad.utils;

import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.MyBox;
import com.euphor.paperpad.Beans.Parameters;

/**
 * Created by Euphor on 05/05/2015.
 */
public class Increment {
    static  int  id_score=0;
    static  int  id_key=0;
    static  int  id_point=0;
    static  int  id_cp=0;
    static  int  id_c=0;
    static  int  id_s=0;
    static  int  id_i=0;
    static int id_cf=0;
    static int id_rl=0;
    static int id_con=0;
    static  int  id_f=0;
    static  int  id_alpd=0;
    static  int  id_alpw=0;
    static  int  id_dpwd=0;
    static  int  id_al=0;
    static  int  id_mb=0;
    static  int  id_fs=0;
    static  int  id_question=0;
    static  int  id_surevy=0;
    static  int  id_formeValue=0;
    static  int  id_fieldFormeContact=0;
    static  int  id_Cof_Cadeau=0;
    static int id_StringValidity=0;
    static int id_MyString=0;
    static int id_CatMyBox=0;
    static int id_Cart=0;
    static int id_ExtraFiels=0;
    static int id_AppBean=0;
    private static int id_Param =0;

    public static  int Primary_score(String option)
    {
        if(option != null)
        {
            id_score++;
        }
        return id_score;
    }
    public static int Primary_AppBean(Application application){
        if(application !=null) {
            id_AppBean++;
        }
        return id_AppBean;
    }
    public static int Primary_Parameters(Parameters p){

        if(p !=null) {
            id_Param++;
        }
        return id_Param;
    }
    public static int Primary_Key(int id){
        if(id>0) {
            id_key++;
        }
        return id_key;
    }
    public static int Primary_Point(int id){
        if(id>0) {
            id_point++;
        }
        return id_point;
    }
    public static int Primary_ExtraFiels(String id){
        if(!id.isEmpty()) {
            id_ExtraFiels++;
        }
        return id_ExtraFiels;
    }


    public static int Primary_Cart(int id){
        if(id>0) {
            id_Cart++;
        }
        return id_Cart;
    }
    public static int Primary_CatMyBox(int id){
        if(id>0) {
            id_CatMyBox++;
        }
        return id_CatMyBox;
    }

    public static int Primary_MyString(int id){
        if(id==0) {
            id_MyString++;
        }
        return id_MyString;
    }
    public static int Primary_coff_Cadeau(int id){
        if(id>0) {
            id_Cof_Cadeau++;
        }
        return id_Cof_Cadeau;
    }
    public static int Primary_fieldFormeContact(int id){
        if(id>0) {
            id_fieldFormeContact++;
        }
        return id_fieldFormeContact;
    }
    public  static int Primary_FormeValye(int id) {
        if(id>0) {
            id_formeValue++;
        }
        return id_formeValue;
    }
    public  static int Primary_Survey(int id) {
        if(id>0) {
            id_surevy++;
        }
        return id_surevy;
    }
    public  static int Primary_Question(int id) {
        if(id>0) {
            id_question++;
        }
        return id_question;
    }
    public  static int Primary_FieldSatisfaction(int id) {
        if(id>0) {
            id_fs++;
        }
        return id_fs;
    }

    public  static int Primary_MyBox(int id) {
        if(id>0) {
            id_mb++;
        }
        return id_mb;
    }
    public  static int Primary_Album(int id) {
        if(id>0) {
            id_al++;
        }
        return id_al;
    }

    public  static int Primary_Disallowed_period_everyday(int id) {
        if(id>0) {
            id_dpwd++;
        }
        return id_dpwd;
    }


    public  static int Primary_Allowed_period_weekdays(int id) {
        if(id>0) {
            id_alpw++;
        }
        return id_alpw;
    }

    public  static int Primary_Allowed_period_Day(int id) {
        if(id>0) {
            id_alpd++;
        }
        return id_alpd;
    }

    public  static int Primary_f(int id) {
        if(id>0) {
            id_f++;
        }
        return id_f;
    }
    public  static int Primary_cp(int id) {
        if(id>0) {
            id_cp++;
        }
        return id_cp;
    }
    public  static int Primary_c(int id) {
        if(id>0) {
            id_c++;
        }
        return id_c;
    }
    public  static int Primary_s(int id) {
        if(id>0) {
            id_s++;
        }
        return id_s;
    }
    public  static int Primary_i(String Link) {
        if( Link !=null && !Link.isEmpty()) {
            id_i++;
        }
        return id_i;
    }
    public  static int Primary_cf(int id) {
        if(id>0) {
            id_cf++;
        }
        return id_cf;
    }
    public  static int Primary_rl(int id) {
        if(id>0) {
            id_rl++;
        }
        return id_rl;
    }
    public  static int Primary_con(int id) {
        if(id>0) {
            id_con++;
        }
        return id_con;
    }
    public  static int Primary_StringValidity(MyBox myBox) {
        if(myBox !=null) {
            id_StringValidity++;
        }
        return id_StringValidity;
    }
}
