package com.euphor.paperpad.utils;

import android.util.Log;

import com.euphor.paperpad.Beans.Application;

import java.sql.Timestamp;

/**
 * Created by Mounia on 21/06/2015.
 */
public class  isAfterCurrent {
    public static boolean _isAfterCurrent(Application older, int newer) {

        Log.i("COMPARAISON", " older : " + older.getParameters().getModification_date() + ",   newer : " + newer);

        boolean result = true;
        if (older.getParameters().getModification_date() != 0 && newer != 0) {
            Timestamp value1 = new Timestamp(older.getParameters().getModification_date());
            Timestamp value2 = new Timestamp(newer);
            result = value2.after(value1);
            //result = value1.after(value2);
        }else if (newer == 0) {
            result = true;
        }
        return result;


    }

}
