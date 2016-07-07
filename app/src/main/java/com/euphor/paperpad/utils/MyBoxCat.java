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
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.MyBox;
import com.euphor.paperpad.Beans.Parameters_section;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;

import io.realm.Realm;

/**
 * Created by Euphor on 19/03/2015.
 */
public class MyBoxCat {
    public static SplashActivity activityCat;
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

        }
        @Override
        protected Integer doInBackground(Void... voids) {

            if (response != null) {
                if(activityCat !=null) {
                    Realm r = Realm.getInstance(activityCat.getApplicationContext());


                    for (int i = 0; i < response.length(); i++) {

                        try {
                            r.beginTransaction();
                            r.createOrUpdateObjectFromJson(CategoriesMyBox.class, response.getJSONObject(i));
                            r.commitTransaction();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }




            }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer)
        {
            super.onPostExecute(integer);
            Realm r = Realm.getInstance(activityCat.getApplicationContext());

            String console_id = r.where(Parameters_section.class).findFirst().getConsole_id();
            if(!console_id.isEmpty() && console_id != null ) {
                MyBox_App.getMyBox_App(console_id);
            }

        }
    }

    public static void getMyBox_Cat(String console_id) {
        if(activityCat !=null)
        mRequestQueue = Volley.newRequestQueue(activityCat.getApplicationContext());

                        String url = "http://consolemybox.apicius.com/services_ios/get_categorie_console?console_id=" + console_id + "&langue=fr";

                        JsonArrayRequest request = new JsonArrayRequest(url,  new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {


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

                            }
                        }
                        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
      //  MyApplication.getInstance().getRequestQueue().add(request);

                    }

                }
          /*  }
        }
    }

}*/