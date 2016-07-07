package com.euphor.paperpad.activities.main;


import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.crashlytics.android.Crashlytics;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.euphor.paperpad.Beans.AgendaGroup;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.AllowedDay;
import com.euphor.paperpad.Beans.Allowed_period_day;
import com.euphor.paperpad.Beans.Allowed_period_weekdays;
import com.euphor.paperpad.Beans.Cart;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Center_location;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.CoffretID;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.Coordinates;
import com.euphor.paperpad.Beans.Disable_period;
import com.euphor.paperpad.Beans.Disallowed_period_everyday;
import com.euphor.paperpad.Beans.ElementSwipe;
import com.euphor.paperpad.Beans.Event;
import com.euphor.paperpad.Beans.ExtraField;
import com.euphor.paperpad.Beans.Extra_fields;
import com.euphor.paperpad.Beans.Field;
import com.euphor.paperpad.Beans.FieldFormContact;
import com.euphor.paperpad.Beans.FormValue;
import com.euphor.paperpad.Beans.Formule;
import com.euphor.paperpad.Beans.FormuleElement;
import com.euphor.paperpad.Beans.Languages;
import com.euphor.paperpad.Beans.Link;
import com.euphor.paperpad.Beans.Linked;
import com.euphor.paperpad.Beans.Location;
import com.euphor.paperpad.Beans.Locations_group;
import com.euphor.paperpad.Beans.MyArrayList;
import com.euphor.paperpad.Beans.MyBox;
import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Parameters_;
import com.euphor.paperpad.Beans.Parameters__;
import com.euphor.paperpad.Beans.Parameters_section;
import com.euphor.paperpad.Beans.Parameters_swipe;
import com.euphor.paperpad.Beans.PeriodString;
import com.euphor.paperpad.Beans.Phone_home_grid;
import com.euphor.paperpad.Beans.Photo;
import com.euphor.paperpad.Beans.Price;
import com.euphor.paperpad.Beans.Question;
import com.euphor.paperpad.Beans.Radio;
import com.euphor.paperpad.Beans.Related;
import com.euphor.paperpad.Beans.RelatedCatIds;
import com.euphor.paperpad.Beans.RelatedContactForm;
import com.euphor.paperpad.Beans.RelatedLocation;
import com.euphor.paperpad.Beans.RelatedPageId;
import com.euphor.paperpad.Beans.Score;
import com.euphor.paperpad.Beans.Street_view_default_position;
import com.euphor.paperpad.Beans.StringImagesBox;
import com.euphor.paperpad.Beans.StringScore;
import com.euphor.paperpad.Beans.StringValidityBox;
import com.euphor.paperpad.Beans.Survey_;
import com.euphor.paperpad.Beans.Tab;
import com.euphor.paperpad.Beans.Tablet_home_grid;
import com.euphor.paperpad.Beans.Tile;
import com.euphor.paperpad.Beans.Tile_;
import com.euphor.paperpad.Beans.Value;
import com.euphor.paperpad.Beans.ValueSatisfaction;
import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.ApplicationBean;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;


import com.euphor.paperpad.utils.AlertDialogManager;
import com.euphor.paperpad.utils.ConnectionDetector;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.Installation;
import com.euphor.paperpad.utils.MyBoxCat;
import com.euphor.paperpad.utils.MyBox_App;
import com.euphor.paperpad.utils.RandomString;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.Utils1;
import com.euphor.paperpad.utils.WakeLocker;
import com.euphor.paperpad.utils.isAfterCurrent;
import com.todddavies.components.progressbar.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


import io.realm.Realm;
import io.realm.RealmResults;

public class SplashActivity extends FragmentActivity{

    private final String LOG_TAG = getClass().getSimpleName();
    public static final String EXTRA_MESSAGE = "message";


    /**
     * You'll need this in your class to cache the helper in the class.
     */
//	private DatabaseHelper appController = null;

    private String URL_UPDATE = "http://backoffice.paperpad.fr/api/application/lastupdate/id/1410/lang/fr";
    private static String URL = "http://backoffice.paperpad.fr/api/application/compiled_app/id/28/lang/fr"; // 449-28-503-499-651-476
    String dev_prod = Constants.BASE_URL;
    SharedPreferences wmbPreference;
    EditText editText;
    LinearLayout submit;
    private DownloadDataAsyncTask task;
    protected ConnectionDetector cd;
    protected AlertDialogManager alert = new AlertDialogManager();
    protected int prevOrientation;
    protected String language;
    public boolean langChanged = false;
    private int id_menu;
    boolean doDownloading = true;
    private boolean isProductionVersion;
    public UIHandler uiHandler;
    Context context;
    /** progress dialog to show user that the backup is processing. */
//	public ProgressDialog progress;
    private ProgressWheel pw;

    private ImportAsyncTask asyncTask;
    public LinearLayout gridImgDownloaded;
    private int versionCodePrecedent;
    private Timer timer;
    protected static final int MY_SOCKET_TIMEOUT_MS = 10000;
    public static int[][] imageRowColumn;
    public Realm realm;
    AtomicBoolean isRunning = new AtomicBoolean(true);
    public static boolean taskFinished = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        if(wmbPreference.getBoolean("isFirstRunning", true)) {
            if(realm != null) {
                realm.close();
            }
            Realm.deleteRealmFile(getApplicationContext());
        }




        MyBoxCat.activityCat = this;
        MyBox_App.activityBox = this;
        realm = Realm.getInstance(getApplicationContext());

        Utils1.activity = this;
        setContentView(R.layout.activity_main);


        dialog = new ProgressDialog(this);
        /** Uness Modif **/
        imageRowColumn = new int[][]{{R.id.img_column00, R.id.img_column01, R.id.img_column02, R.id.img_column03},
                {R.id.img_column10, R.id.img_column11, R.id.img_column12, R.id.img_column13},
                {R.id.img_column20, R.id.img_column21, R.id.img_column22, R.id.img_column23}};


        //layout_request_id = (LinearLayout)findViewById(R.id.layout_request_id);
        gridImgDownloaded = (LinearLayout) findViewById(R.id.downloadedImageLayout);

        //        for (int r=1; r<=rowCount; r++){
        //            TableRow tr = new TableRow(this);
        //            for (int c=1; c<=columnCount; c++){
        //                ImageView im = new ImageView (this);
        //                im.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        //                //im.setPadding(0, 0, 0, 0); //padding in each image if needed
        //                //add here on click event etc for each image...
        //                //...
        //                tr.addView(im);//, imageWidth,imageHeight);
        //            }
        //            table.addView(tr);
        //        }

        gridImgDownloaded.setVisibility(View.VISIBLE);

        /** **/

        ActionBar actionBar = getActionBar();
        actionBar.hide();
        context = getApplicationContext();
        //pour specifier est ce que on ait en version de production ou version g?nerique de test

        isProductionVersion = getResources().getBoolean(R.bool.is_product_version);
        if(isProductionVersion){
            id_menu = Integer.parseInt(getString(R.string.application_id));
        }
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        // version code from preference to know if an update was carried out
        versionCodePrecedent = wmbPreference.getInt("VersionCode", 0);
        String lang = wmbPreference.getString(Utils.LANG, "fr");
        //Utils.changeLocale(lang, getApplicationContext());
        if (b!=null) {
            String id_errone = b.getString("id_error");
            if (id_errone != null) {
                alert.showAlertDialog(SplashActivity.this, "Could not load this application ID "+id_errone," Please check your entry or retry later" , false);
            }


            if (b.getString(Utils.LANG)!=null) {
                language = b.getString(Utils.LANG);
                SharedPreferences.Editor editor = wmbPreference.edit();
                editor.putString(Utils.LANG, language);
                Utils.changeLocale(language, getBaseContext());
                langChanged = true;
            }else {
                language = lang;
            }
            if (langChanged) {
                if (!isProductionVersion) {
                    if (b.getInt("ID_MENU") > 0) {
                        id_menu = b.getInt("ID_MENU");
                    }
                }else {
                    //ici menu id
                }

            }
            /*if (b.getString("page_id") != null && !b.getString("page_id").isEmpty()) {
                page_id = b.getString("page_id");
            }*/
            //Utils.changeLocale(language, getBaseContext());
        }else {
            language = lang;
        }
        String uuid = Installation.id(getApplicationContext());
        mHandleMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
                // Waking up mobile if it is sleeping
                WakeLocker.acquire(getApplicationContext());

                /**
                 * Take appropriate action on this message
                 * depending upon your app requirement
                 * For now i am just displaying it on the screen
                 * */

                // Showing received message
//				Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

                // Releasing wake lock
                WakeLocker.release();
            }
        };
        pw = (ProgressWheel)findViewById(R.id.pw_spinner);
        pw.startSpinning();

        if (!isProductionVersion) {
            editText = (EditText)findViewById(R.id.id_menu);
            submit = (LinearLayout)findViewById(R.id.submit);

            task = new DownloadDataAsyncTask(this);
            if(!langChanged){



                editText.setOnEditorActionListener(new OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            Log.i(LOG_TAG, "Enter pressed");

                            ///// Check connection first
                            cd = new ConnectionDetector(getApplicationContext());

                            // Check if Internet present
                            if (!cd.isConnectingToInternet()) {



                                List<Application> applications  = realm.where(Application.class).findAll(); //appController.getApplicationDataDao().queryForAll();
                                if (applications.size()>0) {
                                   /* Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(main);*/
                                }else {
                                    // Internet Connection is not present and no content present
                                    alert .showAlertDialog(SplashActivity.this,
                                            "Internet Connection Error",
                                            "Please connect to working Internet connection and retry", false);
                                }

                                // stop executing code by return

                                // stop executing code by return
                                return false;
                            }
                            //Lock orientation
                            prevOrientation = getRequestedOrientation();
                            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            } else {
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                            }
                            if(wmbPreference.getBoolean("isFirstRunning", true)) {
                                SharedPreferences.Editor editor = wmbPreference
                                        .edit();
                                editor.putBoolean("isFirstRunning", false);
                                editor.commit();
                                language = "fr";
                            }
                            try {
                                id_menu = Integer.parseInt(v.getText().toString());
                            } catch (NumberFormatException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            URL = dev_prod+"/api/application/compiled_app/id/"+v.getText().toString()+"/lang/"+language;
                            URL_UPDATE = dev_prod+"/api/application/lastupdate/id/"+v.getText().toString()+"/lang/"+language;
                            //							URL = "http://backofficedev.paperpad.fr/api/application/compiled_app/id/"+v.getText().toString()+"/lang/"+"fr";
//							task.execute(URL);

                            jsonParsingFunction(getApplicationContext(), URL, URL_UPDATE, langChanged);

                        }
                        return false;
                    }
                });
                submit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        cd = new ConnectionDetector(getApplicationContext());
                        if (editText.getText().toString().isEmpty()) {

                        }else {
                            // Check if Internet present
                            if (!cd.isConnectingToInternet()) {

                                //Application oldApp;

                                List<Application> applications  = realm.where(Application.class).findAll();
                                //appController.getApplicationDataDao().queryForAll();
                                if (applications.size()>0) {
                                  /*  Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(main);*/
                                }else {
                                    // Internet Connection is not present and no content present
                                    alert .showAlertDialog(SplashActivity.this,
                                            "Internet Connection Error",
                                            "Please connect to working Internet connection and retry", false);
                                }

                                // stop executing code by return

                                return;
                            }
                        }

                        //Lock orientation
                        prevOrientation = getRequestedOrientation();
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        } else {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                        }

						/*if (editText.getText().toString().isEmpty()) {
							URL = "http://192.168.1.14/dash/app.json";
						}else {*/
                        try {
                            id_menu = Integer.parseInt(editText.getText().toString());
                        } catch (NumberFormatException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        URL = dev_prod+"/api/application/compiled_app/id/"+editText.getText().toString()+"/lang/"+language; //publish
                        URL_UPDATE = dev_prod+"/api/application/lastupdate/id/"+editText.getText().toString()+"/lang/"+language;
                        //						URL = "http://backofficedev.paperpad.fr/api/application/compiled_app/id/"+editText.getText().toString()+"/lang/"+"fr";// dev
                        //						}

                        jsonParsingFunction(getApplicationContext(), URL, URL_UPDATE, langChanged);
//						task.execute(URL);


                    }
                });
            }else {//Language changed
                findViewById(R.id.splachRL).setVisibility(View.GONE);

                task = new DownloadDataAsyncTask(this);

                ///// Check connection first
                cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    //Application oldApp;

                    List<Application> applications  = realm.where(Application.class).findAll();
                    //appController.getApplicationDataDao().queryForAll();
                    if (applications.size()>0) {
                        /*Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);*/
                    }else {
                        // Internet Connection is not present and no content present
                        alert .showAlertDialog(SplashActivity.this,
                                "Internet Connection Error",
                                "Please connect to working Internet connection and retry", false);
                    }

                    // stop executing code by return
                    return;
                }
                //Lock orientation
                prevOrientation = getRequestedOrientation();
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                }

                URL = dev_prod+"/api/application/compiled_app/id/"+id_menu+"/lang/"+language;//publish
                URL_UPDATE = dev_prod+"/api/application/lastupdate/id/"+id_menu+"/lang/"+language;
                //				URL = "http://backofficedev.paperpad.fr/api/application/compiled_app/id/"+id_menu+"/lang/"+language;//dev

                //				if (appController.checkDataBase()) {
                try {

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {

                        @Override
                        public void run() {

                            extracted();

                        }
                    }, 0, 5000);
                   /* }*/
                } catch (Exception e) {
                    //	Crashlytics.logException(e);
                }

                //				}
                Log.e(" Realm : 3"," parsing de JSON de 2 eme lang");

                jsonParsingFunction(getApplicationContext(), URL, URL_UPDATE, langChanged);
//				task.execute(URL);
            }


        }else {

            findViewById(R.id.splachRL).setVisibility(View.GONE);
            task = new DownloadDataAsyncTask(this);
            if(!langChanged){

                cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    Application oldApp;

                    List<Application> applications  = realm.where(Application.class).findAll();//appController.getApplicationDataDao().queryForAll();
                    if (applications.size()>0) {
                      /*  Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);*/
                    }else {
                        // Internet Connection is not present and no content present
                        alert .showAlertDialog(SplashActivity.this,
                                "Internet Connection Error",
                                "Please connect to working Internet connection and retry", false);
                    }

                    // stop executing code by return
                    return;
                }
                //Lock orientation
                prevOrientation = getRequestedOrientation();
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                }

                if(wmbPreference.getBoolean("isFirstRunning", true)) {
                    SharedPreferences.Editor editor = wmbPreference
                            .edit();
                    editor.putBoolean("isFirstRunning", false);
                    editor.commit();
                    language = "fr";
                }

                URL = dev_prod+"/api/application/compiled_app/id/"+id_menu+"/lang/"+language;
                URL_UPDATE = dev_prod+"/api/application/lastupdate/id/"+id_menu+"/lang/"+language;
//				task.execute(URL);

                jsonParsingFunction(getApplicationContext(), URL, URL_UPDATE, langChanged);
            }else {
                /*******************/
                //changer la lang
                findViewById(R.id.splachRL).setVisibility(View.GONE);

                task = new DownloadDataAsyncTask(this);


                ///// Check connection first
                cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    //Application oldApp;

                    List<Application> applications  = realm.where(Application.class).findAll();//appController.getApplicationDataDao().queryForAll();
                    if (applications.size()>0) {
                    /*    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);*/
                    }else {
                        // Internet Connection is not present and no content present
                        alert .showAlertDialog(SplashActivity.this,
                                "Internet Connection Error",
                                "Please connect to working Internet connection and retry", false);
                    }

                    // stop executing code by return
                    return;
                }
                //Lock orientation
                prevOrientation = getRequestedOrientation();
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                }

                URL = dev_prod+"/api/application/compiled_app/id/"+id_menu+"/lang/"+language;//publish
                URL_UPDATE = dev_prod+"/api/application/lastupdate/id/"+id_menu+"/lang/"+language;
                //				URL = "http://backofficedev.paperpad.fr/api/application/compiled_app/id/"+id_menu+"/lang/"+language;//dev
                //				if (appController.checkDataBase()) {
                try {

                    //appController.getIllustrationDao().queryForAll();
                  /*  if (illustrations.size()>0) {*/
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {

                        @Override
                        public void run() {

                            extracted();

                        }
                    }, 0, 5000);
                   /* }*/
                } catch (Exception e) {
                    //	Crashlytics.logException(e);
                }
                jsonParsingFunction(getApplicationContext(), URL, URL_UPDATE, langChanged);

//				task.execute(URL);

            }


        }



    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class DownloadDataAsyncTask extends AsyncTask<String, String, String>{



        public int count = 0;
        public boolean[] b = new boolean[]{
                false, false, false, false,
                false, false, false, false,
                false, false, false, false};

        private Context context;
        int progressFactor = 100;
        /** progress dialog to show user that the backup is processing. */
        public ProgressDialog dialog;



        @Override
        protected String doInBackground(String... params) {

            String url = params[0];
            long time = System.currentTimeMillis();
            String unparsedData = Utils.retrieveJson(url);
            time = System.currentTimeMillis() - time;
            Log.i("TIME_CHECK", "time get data "+time+"");

            publishProgress(2 + "/" + getResources().getString(R.string.starting));
            if (unparsedData != null) {


				/*String dashString = Utils.retrieveJson("http://192.168.1.12:8000/dashboard.json");
				try {
					DashboardMain dashboard = mapper.readValue(dashString, DashboardMain.class);
					Log.i(LOG_TAG, dashboard.getDashboard().getInner_padding()+"");
				} catch (JsonParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
                // can reuse, share globally
                try {
                    time = System.currentTimeMillis();

                    if (asyncTask != null) {
                        asyncTask.cancel(true);
                    }

                    asyncTask = new ImportAsyncTask(unparsedData);
                    asyncTask.execute();

                    List<Parameters> results = realm.where(Parameters.class).findAll();
                    //JsonNode results1 = input.get("parameters");
                    // loop until token equal to "}"
                    int id = 0, account_id = 0, date_modif = 0;
                    if (results != null){
                        for (Parameters element : results) {
                            // JsonNode fieldname = element.get("id");
                            id = element.getId();
                            System.out.println("id  :  " + id);
                            //fieldname = element.get("account_id");
                            account_id = element.getAccount_id();
                            Log.e("account_id  :  " + account_id, "");
                            //fieldname = element.get("modification_date");
                            date_modif = element.getModification_date();
                            Log.e("modification_date  :  " + date_modif,"");

                            break;
                        }

                    }

                    ApplicationBean app = null;
                    //					ApplicationBean app = mapper.readValue(unparsedData, ApplicationBean.class);
                    //					time = System.currentTimeMillis() - time;
                    //					Log.i("TIME_CHECK", "time mapper "+time+"");


                    Application oldApp;
					/*
                     * verify application update (versionCode)
                     */
                    oldApp = realm.where(Application.class).findFirst();
                    //appController.getApplicationDataDao().queryForId(1);
                    //						Log.i(" oldApp "," "+oldApp);
                    //						Log.i(" Condition : "," oldApp.getParameters().getAccount_id() : "+oldApp.getParameters().getAccount_id()
                    //								+" == Integer.parseInt(account_id) "+Integer.parseInt(account_id)+"  && oldApp.getParameters().getId() : "
                    //								+oldApp.getParameters().getId()+"  ==  id : "+id);
                    if (oldApp != null) {
                        time = System.currentTimeMillis();
                        if (oldApp.getParameters().getAccount_id() == (account_id) && oldApp.getParameters().getId() ==(id)) {
                            doDownloading = isAfterCurrent._isAfterCurrent(oldApp, date_modif);

                            //								if(doDownloading || langChanged){
                            //									app = mapper.readValue(unparsedData, ApplicationBean.class);
                            //									time = System.currentTimeMillis() - time;
                            //									Log.i("TIME_CHECK", "time mapper "+time+"");
                            //
                            //								}
                        }
                        //							if (oldApp.getParameters().getAccount_id() == app.getApplication().getParameters().getAccount_id() && oldApp.getParameters().getId().equals(app.getApplication().getParameters().getId())) {
                        //								doDownloading = app.getApplication().isAfterCurrent(oldApp);
                        //							}
                        time = System.currentTimeMillis() - time;
                        Log.i("TIME_CHECK", "time check need to update "+time+"");
                        PackageInfo packageInfo = getPackageManager()
                                .getPackageInfo(getPackageName(), 0);
                        int versionCode = packageInfo.versionCode;
                        if (versionCode > versionCodePrecedent) {
                            doDownloading = true;
                            SharedPreferences.Editor editor = wmbPreference
                                    .edit();
                            editor.putInt("VersionCode", versionCode);
                            editor.commit();
                        }
                    }


                    //Log.i(LOG_TAG, "ApplicationBean : " + text);
                    publishProgress(9 + "/" + getResources().getString(R.string.downloading_content));

                    if (doDownloading || langChanged) {
                        time = System.currentTimeMillis();
                        if (asyncTask != null) {
                            asyncTask.cancel(true);
                        }

                        asyncTask = new ImportAsyncTask(unparsedData);
                        asyncTask.execute();
                        Log.e("realm 4","");
                        /*app = mapper.readValue(unparsedData, ApplicationBean.class);*/
                        time = System.currentTimeMillis() - time;
                        Log.i("TIME_CHECK", "time mapper "+time+"");
                        time = System.currentTimeMillis();
						/*appController.populateDatabase(app.getApplication(), mRequestQueue);*/
                        time = System.currentTimeMillis() - time;
                        Log.i("TIME_CHECK", "time save to database "+time+"");
                    }

                    time = System.currentTimeMillis();

                    time = System.currentTimeMillis() - time;
                    Log.i("TIME_CHECK", "time empty Cart Items "+time+"");


                } catch (Exception e) {
                    Log.e(LOG_TAG, "Exception 1 : " + e.getMessage());
                    e.printStackTrace();
                    erreur = true;
                    return "error";
                }

                // Download pics




            }else {


                erreur = true;
                return "error";
            }
            return unparsedData;
        }

        boolean erreur = false;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            } catch (IllegalArgumentException e) {
                //Crashlytics.logException(e);
                e.printStackTrace();
            }
            setRequestedOrientation(prevOrientation);
            //			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            if (erreur) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                if (!isProductionVersion) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id_error", editText.getText().toString());
                    intent.putExtras(bundle);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else {
               /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("page_id", page_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }


        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {

            this.dialog.setMessage(getResources().getString(R.string.waiting));
            this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //            this.dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_bar_test));
            this.dialog.setIndeterminate(false);
            this.dialog.setCancelable(false);
            this.dialog.show();

            (SplashActivity.this).findViewById(R.id.splachRL).setVisibility(View.GONE);


        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... values) {
            String[] str = values[0].split("/");
            Double tmp = Double.parseDouble(str[0]);
            this.dialog.setMessage(/*getResources().getString(R.string.downloading_content)+" : "+*/str[1]);
            int progress = tmp.intValue();
            dialog.setProgress((int)((double)(progress*100)/(double)(progressFactor+1)));
            if (progressFactor!=100) {
                dialog.setProgressNumberFormat(progress+"/"+progressFactor);
                //				NumberFormat format = NumberFormat.getInstance();
                //				dialog.setProgressPercentFormat(progress*100/progress+"%");
            }else {
                dialog.setProgressNumberFormat("%1d/%2d");
            }

        }

        /**
         *
         */
        public DownloadDataAsyncTask() {
            super();
            dialog = new ProgressDialog(getApplicationContext());

        }

        public DownloadDataAsyncTask(Context context) {
            dialog = new ProgressDialog(context);
            this.context = context;
        }




    }




    public final class UIHandler extends Handler
    {
        public static final int DISPLAY_UI_TOAST = 0;
        public static final int DISPLAY_UI_DIALOG = 1;

        public UIHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case UIHandler.DISPLAY_UI_TOAST:
                {
                    Context context = getApplicationContext();
                    Toast t = Toast.makeText(context, (String)msg.obj, Toast.LENGTH_LONG);
                    t.show();
                }
                case UIHandler.DISPLAY_UI_DIALOG:
                    final AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(
                            SplashActivity.this).create();
                    alertDialog.setTitle(getString(R.string.error));
                    alertDialog.setMessage(getString(R.string.error));
                    alertDialog.setButton(AlertDialog.BUTTON1, "Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    alertDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                    Bundle b = new Bundle();
                                    b.putBoolean("REQUEST_UPDATE", true);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            });
                default:
                    break;
            }
        }
    }

    protected void handleUIRequest(String message)
    {
        Message msg = uiHandler.obtainMessage(UIHandler.DISPLAY_UI_DIALOG);
        msg.obj = message;
        uiHandler.sendMessage(msg);
    }


    /**
     * Receiving push messages
     * */
    private BroadcastReceiver mHandleMessageReceiver ;
    private Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeMessages(0);

        handler.removeCallbacks(switch_runnable);
        handler.removeCallbacksAndMessages(null);
        //if(handler.obtainMessage() != null)
        // Log.e("handler.obtainMessage()","value"+ handler.obtainMessage());
        isRunning.set(false);
        try {
            //handler.sendEmptyMessage(1000);

            unregisterReceiver(mHandleMessageReceiver);

            //	GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> onDestroy " + e.getMessage());
        }
    }

    /**
     * You'll need this in your class to get the helper from the manager once per class.

     private DatabaseHelper getHelper() {
     if (appController == null) {
     appController = OpenHelperManager.getHelper(this, DatabaseHelper.class);
     }
     return appController;
     return ((MyApplication)getApplication()).getHelper();
     }*/

    Runnable switch_runnable = new Runnable() {

        @Override
        public void run() {


            Realm r = Realm.getInstance(getApplicationContext());
            final List<Illustration> illustrations = r.where(Illustration.class).findAll();

            Random rd = new Random();
            int size = illustrations.size();

            // rd.setSeed(size);

            if (size > 0 && isRunning.get()) {
                int i = Math.abs(rd.nextInt(size));

                final Illustration illustration = illustrations.get(i);

                //  Log.e("affichage d' illustrations : "+illustration.getPath(),"");
                //if (handler.hasMessages(1000)) return;
                Log.e("affichage de : (illustrations.size()) ", "" + size);
                if (illustration == null) return;

                ImageView img = (ImageView) findViewById(R.id.imageLang);
                img.setScaleType(ScaleType.CENTER_CROP);
                try {
                    ViewPropertyAnimation.Animator grow_animation = new ViewPropertyAnimation.Animator() {
                        @Override
                        public void animate(View view) {
                           /* int[] anims = new int[]{R.anim.grow, R.anim.grow_from_bottomleft_to_topright, R.anim.grow_from_bottomright_to_topleft};
                            int index_anim = new Random(3).nextInt(3);*/
                            Animation grow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow);
                            view.setAnimation(grow);
                        }
                    };

                    if (!illustration.getPath().isEmpty()) {
                        Glide.with(getApplicationContext()).load(new File(illustration.getPath())).animate(grow_animation).into(img);
                    } else {
                        Glide.with(getApplicationContext()).load(illustration.getLink()).animate(grow_animation).into(img);
                    }
                } catch (Exception e) {
                    extracted();
                    e.printStackTrace();
                }
                /*Animation grow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow);
                img.startAnimation(grow);*/
            }

        }
    };
    public void extracted(){

        /*Handler handler = new Handler();*/
        /*Looper.prepare();*/
        handler.post(switch_runnable);

    }


    public interface PostCommentResponseListener {
        public void requestStarted();
        public void requestCompleted();
        public void requestEndedWithError(VolleyError error);
        public void requestTime();
    }

    private static PostCommentResponseListener mPostCommentResponse;
    ProgressDialog dialog;
    private RequestQueue mRequestQueue;
    protected Runnable runToMain;

    public void jsonParsingFunction(final Context context, String URL, String url_update, final boolean langChanged) {

        (SplashActivity.this).findViewById(R.id.splachRL).setVisibility(View.GONE);
        findViewById(R.id.progress_wheel).setVisibility(View.VISIBLE);
        mPostCommentResponse = new PostCommentResponseListener() {

            long timeReq = System.currentTimeMillis();
            @Override
            public void requestStarted() {
                Log.e(" requestStarted "," <<<>>>>");


            }

            @Override
            public void requestEndedWithError(VolleyError error) {
                Log.e(" requestEndedWithError "," <<<< error : "+error+" >>>>");
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                if (!isProductionVersion) {
                    Bundle bundle = new Bundle();
                    //bundle.putString("id_error", editText.getText().toString());
                    intent.putExtras(bundle);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }

            @Override
            public void requestCompleted() {
                Log.e(" requestCompleted "," <<<>>>>");
                dialog.dismiss();
                try {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (IllegalArgumentException e) {
                    //Crashlytics.logException(e);
                    e.printStackTrace();
                }

                timeReq = System.currentTimeMillis();
                //					setRequestedOrientation(prevOrientation);

//				try {
//					events = appController.getEventDao().queryForAll();
//					Parameters params = appController.getParametersDao().queryForId(1);
//					colors = new Colors(params);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				progress.setProgress(100);
                pw.incrementProgress(360);
//				imgProgress = (ImageView) findViewById(R.id.imageProgess);
//				timeReq = System.currentTimeMillis() - timeReq;
                Log.i("TIME_CHECK get events i don't know why", " "+timeReq+"");



            }

            @Override
            public void requestTime() {

                timeReq = System.currentTimeMillis() - timeReq;
                Log.i("TIME_CHECK get json by volley", " "+timeReq+"");

            }


        };
        String langUrl = dev_prod+"/api/application/languages/id/"+id_menu;

        final JsonObjectRequest langJson = new JsonObjectRequest(langUrl, null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Locale local = getApplication().getResources().getConfiguration().locale;
                SharedPreferences.Editor editor = wmbPreference.edit();
                boolean supportedLang = false;
                try {
                      /* realm=Realm.getInstance(getApplicationContext());
                    realm.beginTransaction();
                    realm.createOrUpdateObjectFromJson(com.euphor.paperpad.BeansCEP.ApplicationBean.class,response);
                    realm.commitTransaction();*/

                    JSONArray array = response.getJSONArray("languages");
                    for (int i = 0; i < array.length(); i++) {

                        if (local.getLanguage().equalsIgnoreCase(array.getString(i))) {
                            supportedLang = true;
                        }
                    }
                    if (supportedLang) {
                        editor.putString(Utils.LANG, local.getLanguage());
                        language = local.getLanguage();
                    }else {
                        editor.putString(Utils.LANG, "fr");
                        language = "fr";

                    }
                    editor.commit();
                    Utils.changeLocale(language, getBaseContext());

                } catch (JSONException e) {
                    editor.putString(Utils.LANG, "fr");
                    language = "fr";
                    editor.commit();
                    Utils.changeLocale(language, getBaseContext());
                    e.printStackTrace();
                }

                final StringRequest loadJsonRequest = new StringRequest(dev_prod+"/api/application/compiled_app/id/"+id_menu+"/lang/"+language, new Listener<String>() {


                    //		    public ProgressDialog dialog;


                    @Override
                    public void onResponse(String response) {
                        Log.i("response", " >>> "+response);
                        mPostCommentResponse.requestTime();
                        long time = System.currentTimeMillis();


                        if (response != null) {


                            try {
                                ApplicationBean app = null;


                                //Log.i(LOG_TAG, "ApplicationBean : " + text);

                                if (doDownloading || langChanged) {
                                    if (asyncTask != null) {
                                        asyncTask.cancel(true);
                                    }

                                    asyncTask = new ImportAsyncTask(response);
                                    asyncTask.execute();
                                    Log.e("realm 1","");
                                    pw.incrementProgress(0);
                                    time = System.currentTimeMillis();
                                    /*app = mapper.readValue(response, ApplicationBean.class);*/
                                    time = System.currentTimeMillis() - time;
                                    Log.i("TIME_CHECK", "time mapper "+time+"");
                                    time = System.currentTimeMillis();
                                    pw.incrementProgress(108);
									/*appController.populateDatabase(app.getApplication(), mRequestQueue);*/

                                    time = System.currentTimeMillis() - time;
                                    Log.i("TIME_CHECK", "time save to database "+time+"");
                                    pw.incrementProgress(345);
                                }


                                //appController.getIllustrationDao().queryForAll();

                                time = System.currentTimeMillis();
                           /*     realm.beginTransaction();
                                realm.where(CartItem.class).findAll().clear();
                                realm.commitTransaction();*/
                              /*  appController.emptyCartItems();*/
                                time = System.currentTimeMillis() - time;
                                Log.i("TIME_CHECK", "time empty Cart Items "+time+"");


                            } catch (Exception e) {
                                Log.e(LOG_TAG, "Exception2 : " + e.getMessage());
                                e.printStackTrace();

                                return ;
                            }

                            //					if (doDownloading || langChanged) {
                            //						DownloadAsync async = new DownloadAsync();
                            //						async.execute();
                            //					}

                        }
                        //				else {
                        //					return ;
                        //				}

                        mPostCommentResponse.requestCompleted();

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG error json parser", "an error" );
                        //mRequestQueue.add(loadJsonRequest);
                        mPostCommentResponse.requestEndedWithError(error);
                    }
                });

                loadJsonRequest.setShouldCache(false);
                loadJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequestQueue.add(loadJsonRequest);
                mRequestQueue.getCache().clear();
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                SharedPreferences.Editor editor = wmbPreference.edit();
                editor.putString(Utils.LANG, "fr");
                language = "fr";
                editor.commit();
                Utils.changeLocale(language, getBaseContext());

            }
        });
        langJson.setShouldCache(false);




        mRequestQueue = Volley.newRequestQueue(context);
//		mPostCommentResponse.requestStarted();
        mRequestQueue.getCache().clear();
        mPostCommentResponse.requestStarted();


        final JsonObjectRequest modifDateRequest = new JsonObjectRequest(url_update, null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {



                try {


                    int modif_date = response.getInt("updated");
                    Log.i("tetttt", modif_date+"");
//					ApplicationBean app = null;


                    Application oldApp;
					/*
                     * verify application update (versionCode)
                     */
                    oldApp = realm.where(Application.class).findFirst();//appController.getApplicationDataDao().queryForId(1);
                    if (oldApp != null) {
//							progressValue = new setProgressValue(1);
//							showNextEvent();
                        if (oldApp.getParameters().getId() != id_menu) {
                            doDownloading = true;
                        }else {
                            doDownloading = isAfterCurrent._isAfterCurrent(oldApp, modif_date);
                        }


                        PackageInfo packageInfo;
                        try {
                            packageInfo = getPackageManager()
                                    .getPackageInfo(getPackageName(), 0);
                            int versionCode = packageInfo.versionCode;
                            if (versionCode > versionCodePrecedent) {
                                doDownloading = true;
                                SharedPreferences.Editor editor = wmbPreference
                                        .edit();
                                editor.putInt("VersionCode", versionCode);
                                editor.commit();
                            }
                        } catch (NameNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }else {
                        doDownloading = true;
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (doDownloading || langChanged) {
//					mRequestQueue.add(loadLanguages);
                    if (langChanged) {
                        Log.e(" rqt : >> "+dev_prod+"/api/application/compiled_app/id/"+id_menu+"/lang/"+language,"");
                        final StringRequest loadJsonRequest = new StringRequest(dev_prod+"/api/application/compiled_app/id/"+id_menu+"/lang/"+language, new Listener<String>() {


                            @Override
                            public void onResponse(String response) {
//								Log.i("response", " >>> "+response+"");
                                mPostCommentResponse.requestTime();

                                long time = System.currentTimeMillis();

                                if (response != null) {


                                    try {
                                        ApplicationBean app = null;

                                        /**************************************/
                                        mRequestQueue.getCache().clear();

                                        if (doDownloading || langChanged) {
                                            realm= Realm.getInstance(getApplicationContext());


                                            if (asyncTask != null) {
                                                asyncTask.cancel(true);
                                            }

                                            asyncTask = new ImportAsyncTask(response);
                                            asyncTask.execute();
                                            Log.e(" lang changed : ",""+langChanged);
                                            Log.e("realm 2",""+response);
                                            pw.incrementProgress(0);
                                            time = System.currentTimeMillis();
                                           /* app = mapper.readValue(response, ApplicationBean.class);*/
                                            time = System.currentTimeMillis() - time;
                                            Log.i("TIME_CHECK", "time mapper "+time+"");
                                            time = System.currentTimeMillis();
                                            pw.incrementProgress(108);
									/*		appController.populateDatabase(app.getApplication(), mRequestQueue);*/
                                            time = System.currentTimeMillis() - time;
                                            Log.i("TIME_CHECK", "time save to database "+time+"");
                                            pw.incrementProgress(345);
                                        }


                                        // appController.getIllustrationDao().queryForAll();

                                        time = System.currentTimeMillis();
                                       /* realm.beginTransaction();
                                        realm.where(CartItem.class).findAll().clear();
                                        realm.commitTransaction();*/
                                        time = System.currentTimeMillis() - time;
                                        Log.i("TIME_CHECK", "time empty Cart Items "+time+"");


                                    } catch (Exception e) {
                                        Log.e(LOG_TAG, "Exception3 : " + e.getMessage());
                                        e.printStackTrace();

                                        return ;
                                    }

                                    //					if (doDownloading || langChanged) {
                                    //						DownloadAsync async = new DownloadAsync();
                                    //						async.execute();
                                    //					}

                                }
                                //				else {
                                //					return ;
                                //				}

                                mPostCommentResponse.requestCompleted();

                            }
                        }, new ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("TAG error json parser", "an error" );
//								mRequestQueue.add(loadJsonRequest);
                                mPostCommentResponse.requestEndedWithError(error);
                            }
                        });
                        loadJsonRequest.setShouldCache(false);
                        loadJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                                MY_SOCKET_TIMEOUT_MS,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        mRequestQueue.add(loadJsonRequest);
                    }else {
                        langJson.setRetryPolicy(new DefaultRetryPolicy(
                                MY_SOCKET_TIMEOUT_MS,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        mRequestQueue.add(langJson);
                        mRequestQueue.getCache().clear();
                    }



                }else {
                    pw.incrementProgress(260);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    pw.incrementProgress(340);

                }

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e(" ERROR_TAG", " volley error response" );
            }
        });
        modifDateRequest.setShouldCache(false);
        modifDateRequest.setRetryPolicy(new DefaultRetryPolicy( MY_SOCKET_TIMEOUT_MS,  DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(modifDateRequest);
        mRequestQueue.getCache().clear();


    }


    //	public setProgressValue progressValue;
    private  class ImportAsyncTask extends AsyncTask<Void, Integer, Integer> {
        String jsonObject;

        private ImportAsyncTask(String jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Realm r = Realm.getInstance(getApplicationContext());
            r.beginTransaction();
            long time = System.currentTimeMillis();
            r.where(ApplicationBean.class).findAll().clear();
            r.where(AgendaGroup.class).findAll().clear();
            r.where(Album.class).findAll().clear();
            r.where(Allowed_period_day.class).findAll().clear();
            r.where(Allowed_period_weekdays.class).findAll().clear();
            r.where(AllowedDay.class).findAll().clear();
            r.where(Application.class).findAll().clear();
            r.where(ApplicationBean.class).findAll().clear();
            r.where(Cart.class).findAll().clear();
            /* r.where(CartItem.class).findAll().clear();*/
            r.where(CategoriesMyBox.class).findAll().clear();
            r.where(Category.class).findAll().clear();
            r.where(Center_location.class).findAll().clear();
            r.where(Child_pages.class).findAll().clear();
            r.where(CoffretID.class).findAll().clear();
            r.where(Contact.class).findAll().clear();
            r.where(Coordinates.class).findAll().clear();
            r.where(Disable_period.class).findAll().clear();
            r.where(Disallowed_period_everyday.class).findAll().clear();
            r.where(ElementSwipe.class).findAll().clear();
            r.where(Event.class).findAll().clear();
            r.where(Extra_fields.class).findAll().clear();
            r.where(ExtraField.class).findAll().clear();
            r.where(Field.class).findAll().clear();
            r.where(FieldFormContact.class).findAll().clear();
            r.where(FormValue.class).findAll().clear();
            r.where(Formule.class).findAll().clear();
            r.where(FormuleElement.class).findAll().clear();
            r.where(Languages.class).findAll().clear();
            r.where(com.euphor.paperpad.Beans.Language.class).findAll().clear();
            r.where(Link.class).findAll().clear();
            r.where(Linked.class).findAll().clear();
            r.where(Location.class).findAll().clear();
            r.where(Locations_group.class).findAll().clear();
            r.where(MyArrayList.class).findAll().clear();
            r.where(MyBox.class).findAll().clear();
            r.where(MyInteger.class).findAll().clear();
            r.where(MyString.class).findAll().clear();
            r.where(Parameters.class).findAll().clear();
            r.where(Parameters_.class).findAll().clear();
            r.where(Parameters__.class).findAll().clear();
            r.where(Parameters_section.class).findAll().clear();
            r.where(Parameters_swipe.class).findAll().clear();
            r.where(PeriodString.class).findAll().clear();
            r.where(Illustration.class).findAll().clear();
            r.where(Phone_home_grid.class).findAll().clear();
            r.where(Photo.class).findAll().clear();
            r.where(Price.class).findAll().clear();
            r.where(Question.class).findAll().clear();
            r.where(Radio.class).findAll().clear();
            r.where(Related.class).findAll().clear();
            r.where(RelatedCatIds.class).findAll().clear();
            r.where(RelatedContactForm.class).findAll().clear();
            r.where(RelatedLocation.class).findAll().clear();
            r.where(RelatedPageId.class).findAll().clear();
            r.where(Score.class).findAll().clear();
            r.where(Section.class).findAll().clear();
            r.where(Street_view_default_position.class).findAll().clear();
            r.where(StringImagesBox.class).findAll().clear();
            r.where(StringScore.class).findAll().clear();
            r.where(StringValidityBox.class).findAll().clear();
            r.where(Survey_.class).findAll().clear();
            r.where(Tab.class).findAll().clear();
            r.where(Tablet_home_grid.class).findAll().clear();
            r.where(Tile.class).findAll().clear();
            r.where(Tile_.class).findAll().clear();
            r.where(Value.class).findAll().clear();
            r.where(ValueSatisfaction.class).findAll().clear();
            r.removeAllChangeListeners();
            time = System.currentTimeMillis() - time;
            Log.e("le temp de pour vider la base :" + time / 1000, " (s)");
            r.commitTransaction();
            Log.e("size d' applicationBean :" + r.where(ApplicationBean.class).findAll().size(), "");


        }

        @Override
        protected Integer doInBackground(Void... voids) {


            Realm r = Realm.getInstance(getApplicationContext());

            r.beginTransaction();

            long time = System.currentTimeMillis();
            r.createOrUpdateObjectFromJson(ApplicationBean.class, jsonObject);
            time = System.currentTimeMillis() - time;
            Log.e("le temp de parsing de JSON et stockage des données :" + time / 1000, "(s)");

            r.commitTransaction();


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // pw.incrementProgressByValue(values[0]);


        }

        @Override
        protected void onPostExecute(Integer integer) {
            Realm r = Realm.getInstance(getApplicationContext());

            Parameters_section ps = r.where(Parameters_section.class).findFirst();

            if (ps != null) {
                String console_id = ps.getConsole_id();
                if (!console_id.isEmpty() && console_id != null) {
                    MyBoxCat.getMyBox_Cat(console_id);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }




        }

    }






}
