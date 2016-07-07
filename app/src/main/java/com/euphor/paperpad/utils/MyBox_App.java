package com.euphor.paperpad.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.euphor.paperpad.Beans.MyBox;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.SplashActivity;


import org.json.JSONArray;
import org.json.JSONException;

import io.realm.Realm;

/**
 * Created by Euphor on 19/03/2015.
 */
public class MyBox_App {
    public static SplashActivity activityBox;
    public static MainActivity activityBox_main;
    public static ImportAsyncTask asyncTask;
    public static RequestQueue mRequestQueue;
    private static class ImportAsyncTask extends AsyncTask<Void, Integer, Integer> {
        JSONArray response;

        private ImportAsyncTask(JSONArray jsonArray) {
            this.response = jsonArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Toast.makeText(activityBox.getApplicationContext(), "My Box", Toast.LENGTH_LONG).show();
        }
        @Override

        protected Integer doInBackground(Void... voids) {
            Log.e("activity :"+activityBox.getDeviceName(),"response "+response.toString());
            if (response != null && activityBox !=null) {

                Realm r = Realm.getInstance( activityBox);

                for (int i = 0; i <response.length() ; i++) {

                    try {
                        r.beginTransaction();
                        r.createOrUpdateObjectFromJson(MyBox.class,response.getJSONObject(i));
                        r.commitTransaction();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }


            return null;
        }

        @Override
        protected void onPostExecute(Integer integer)
        {
            super.onPostExecute(integer);

            int myBox_size = activityBox.realm.where(MyBox.class).findAll().size();
            Log.e("MyBox Finished ","Size of mybox :" +myBox_size );
            Intent intent = new Intent(activityBox.getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityBox.startActivity(intent);

        }
    }

    public static void getMyBox_App(String console_id) {
        Log.e("device name "+activityBox.getDeviceName(),"");
        if(activityBox !=null)
            mRequestQueue = Volley.newRequestQueue(activityBox.getApplicationContext());
        Log.e("Before url","");
        String url = "http://consolemybox.apicius.com/services_ios/get_coffret?console_id=" + console_id + "&langue=fr";
        Log.e(" request","") ;
        JsonArrayRequest request = new JsonArrayRequest(url,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("After request onReponse","") ;
                if (response != null) {

                    if (asyncTask != null) {
                        asyncTask.cancel(true);
                    }
                    asyncTask = new ImportAsyncTask(response);
                    asyncTask.execute();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volley Error MyBox App"+ volleyError.toString(),"") ;

            }
        }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);


    }

}
