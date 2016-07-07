/**
 * 
 */
package com.euphor.paperpad.activities.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

//import com.crashlytics.android.Crashlytics;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.crashlytics.android.Crashlytics;
import com.euphor.paperpad.utils.VolleyApplication;
import com.euphor.paperpad.utils.jsonUtils.AppHit;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

import java.util.ArrayList;


public class MyApplication extends Application {


	public ArrayList<AppHit> hits = new ArrayList<AppHit>();
	public SplashActivity splashActivity;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private static MyApplication sInstance;

    private RequestQueue mRequestQueue;

	@Override
	public void onCreate() {

		super.onCreate();
  //	Fabric.with(this, new Crashlytics());

        boolean delele_realm_file = Realm.deleteRealmFile(getApplicationContext());
        Log.e("delele_realm_file", ""+delele_realm_file);

        mRequestQueue = Volley.newRequestQueue(this);

        sInstance = this;

	}


    public synchronized static MyApplication getInstance( ){
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }




























    /**
     * You'll need this in your class to get the helper from the manager once per class.

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }*/

	/**
	 * @return the appController
	 */



}
