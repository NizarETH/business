package com.euphor.paperpad.utils;

import android.util.Log;

import com.euphor.paperpad.Beans.Illustration;

import io.realm.RealmList;

/**
 * Created by euphor on 13/11/15.
 */
public class Images {
    public static RealmList<Illustration> pages(Illustration illustration, RealmList<Illustration> illustrations)
    {

        if(illustration != null) {
            Log.e("illustration >>>>" + illustration.getPath(), "");
            illustrations.add(illustration);
        }
        return illustrations;
    }
}
