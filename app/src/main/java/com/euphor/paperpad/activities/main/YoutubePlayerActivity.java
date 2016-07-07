/**
 * 
 */
package com.euphor.paperpad.activities.main;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.TabsFragment;
import com.euphor.paperpad.activities.fragments.TabsSupportFragment.ActionCallBack;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.Beans.Tab;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.InfoActivity;
import com.euphor.paperpad.utils.Installation;
import com.euphor.paperpad.utils.RandomString;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.utils.jsonUtils.AppJsonWriter;
import com.euphor.paperpad.utils.jsonUtils.AppSession;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

import static com.euphor.paperpad.utils.CommonUtilities.SENDER_ID;
import static com.euphor.paperpad.utils.CommonUtilities.SERVER_URL;

/**
 * @author euphordev02
 *
 */
public class YoutubePlayerActivity extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener, ActionCallBack{

	public TabsFragment tabsFragment;
	private SharedPreferences wmbPreference;

	private OnSharedPreferenceChangeListener listner;
	public Colors colors;
	public ImageLoader imageLoader;
	public InfoActivity infoActivity;
	private Bundle extras;
	private boolean passedOncreate = false;
	private String link;
	public SlidingMenu menu;
	public LinearLayout cartTagContainer;
	public TextView totalSum;
	private boolean bottomNav;
	
	private final String LOG_TAG = getClass().getSimpleName();
	private boolean isTablet;
	private Parameters params;
	private Context context;
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    GoogleCloudMessaging gcm;
    String regid;
	private long tsLong;
	private String session_id;
	private long start;
	private PriceCallBack priceCallBack;
    private Realm realm;

	/**
	 * 
	 */
	public YoutubePlayerActivity() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

        start = System.currentTimeMillis();
		RandomString rd = new RandomString(28);
		session_id = rd.nextString();
		context = getApplicationContext();
		//initialization
		passedOncreate = true;
		ActionBar actionBar = getActionBar();
		actionBar.hide(); // hided actionbar
		realm = Realm.getInstance(getApplicationContext());

		wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(this);

		params = null;

        params = realm.where(Parameters.class).findFirst();

        /*if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
//            registerInBackground();
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(LOG_TAG, "No valid Google Play Services APK found.");
        }*/
		listner = new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if (key.equals("input_id")) {

					Intent intent = new Intent(getApplicationContext(),
							SplashActivity.class);
					Bundle b = new Bundle();
					b.putBoolean("REQUEST_UPDATE", true);
					intent.putExtras(b);
					startActivity(intent);

				}

			}
		};
		
		wmbPreference.registerOnSharedPreferenceChangeListener(listner);
		
		Parameters parameters = null;

        parameters = realm.where(Parameters.class).findFirst();

        if (parameters != null) {
			if (parameters.getNavigation_type()!= null) {
				if (parameters.getNavigation_type().contains("bottom")) {
					bottomNav = true;
				}else {
					bottomNav = false;
				}
			}else {
				bottomNav = true;
			}
		}

        isTablet = Utils.isTablet(getApplicationContext());
		if (isTablet) {
			if (bottomNav) {

				setContentView(R.layout.youtube_fragment_horizontal);
			}else {
				setContentView(R.layout.youtube_fragment);
			}
		}else {
			setContentView(R.layout.youtube_fragment_horizontal);
		}
		
		changeLocale("fr");
        Parameters params = null;
        if (realm.where(Parameters.class).findAll().size()>0) {
            params = realm.where(Parameters.class).findFirst();
        }
        Parameters ParamColor = realm.where(Parameters.class).findFirst();

        colors = new Colors(ParamColor);

        //colors = new Colors(params);

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View menuView = inflater.inflate(R.layout.cart_layout_new, null, false);
        cartTagContainer = (LinearLayout)menuView.findViewById(R.id.cartTagContainer);
        totalSum = (AutoResizeTextView)menuView.findViewById(R.id.sum);
        menu.setMenu(menuView);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r.getDimension(R.dimen.swipe_aligned_image_height), r.getDisplayMetrics());
        menu.setBehindWidth((int) px);
        
        String pattern = "(?<=watch\\?v=|v\\/|/videos/|embed\\/)[^#\\&\\?]*"; // "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*" // "\b(?<=v.|/)[a-zA-Z0-9_-]{11,}\b"
	    Pattern compiledPattern = Pattern.compile(pattern);
		String url = getIntent().getStringExtra("link");
		if (url == null || url.isEmpty()) {
			List<Section> sections = new ArrayList<Section>();
            sections = realm.where(Section.class).equalTo("type", "video").findAll();
            if (sections.size()>0) {
				Section sectionVideo = sections.get(0);
				String url_section = sectionVideo.getRoot_url();
//				url = "http://www.youtube.com/watch?v=384IUU43bfQ";
				link = "wKJ9KzGQq0w";
			    Matcher matcher = compiledPattern.matcher(url_section);

			    if(matcher.find()){
			        link = matcher.group();
			    }
			}
		}else {
			
		    Matcher matcher = compiledPattern.matcher(url);

		    if(matcher.find()){
		        link = matcher.group();
		    }
		}
		
		
		//end initialization
		infoActivity = getIntent().getParcelableExtra("InfoActivity");
		tabsFragment = new TabsFragment();
		priceCallBack = (PriceCallBack)tabsFragment;
		Bundle extrasTabsFrag =  new Bundle();
		extrasTabsFrag.putInt("highlighted_tab", infoActivity.getIdCurrentTab());
		extrasTabsFrag.putBoolean("orientation", true);
		tabsFragment.setArguments(extrasTabsFrag);
		YoutubePlayerActivity.this.getFragmentManager().beginTransaction().replace(R.id.tabsFragment, tabsFragment).commit();
		
		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
	    youTubeView.initialize(Constants.DEVELOPER_KEY, this);
	    
	    ((MyApplication)getApplication()).hits.add(new AppHit(System.currentTimeMillis()/1000, start/1000, "sales_web_section", 0));
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		 ((MyApplication)getApplication()).hits.add(new AppHit(System.currentTimeMillis()/1000, start/1000, "sales_web_section", 0));
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onPause()
	 */
	@Override
	protected void onPause() {
		((MyApplication)getApplication()).hits.add(new AppHit(System.currentTimeMillis()/1000, start/1000, "sales_web_section", 0));
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		if (!passedOncreate) {
			tabsFragment = new TabsFragment();
			Bundle extrasTabsFrag =  new Bundle();
			extrasTabsFrag.putInt("highlighted_tab", infoActivity.getIdPreviousTab());
			extrasTabsFrag.putBoolean("orientation", true);
			tabsFragment.setArguments(extrasTabsFrag);
			YoutubePlayerActivity.this.getFragmentManager().beginTransaction().replace(R.id.tabsFragment, tabsFragment).commit();
		}
		passedOncreate = false;
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onStop()
	 */
	@Override
	protected void onStop() {
		 ((MyApplication)getApplication()).hits.add(new AppHit(System.currentTimeMillis()/1000, start/1000, "sales_web_section", 0));
//		sendInBackground();
		super.onStop();
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {
			player.cueVideo(link);
		} else {
			player.play();
		}
		
	}

	@Override
	public void onTabClick(Tab tab, int index) {
		infoActivity.switchCurrentPrev();
		infoActivity.setIdCurrentTab(index);
		//type = bundle.getString("extra");
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		
		Section section = null;
        List<Section> sections = realm.where(Section.class).equalTo("id", tab.getSection_id()).findAll();
        if (sections.size() > 0) {
            section = sections.get(0);
        }
        if (section != null) {

			String type = section.getType();
			extras = new Bundle();
			// filter based on the type of the section
			extras.putParcelable("InfoActivity", infoActivity);
			if (!tab.getIsHomeGrid()) {
				extras.putString("extra", type);
			} else if (tab.getIsHomeGrid()) {
				extras.putString("extra", type);
				extras.putBoolean("isHomeGrid", true);
			}
			intent.putExtras(extras);
		}
		startActivity(intent);
		overridePendingTransition(0, 0);
		
	}
	
	 /**this method is used to change in the course of the application the {@link Locale} we do this because we have two places 
     * where we get the I18N resources ... from the BO and from android I18N strings
     * @param string this string is the locale name for example "it" for Italic "fr" for French ...
     */
    private void changeLocale(String string) {
		Configuration config = new Configuration();
		Locale locale = new Locale(string);
		Locale.setDefault(locale);

		config.locale = locale;
		getBaseContext().getResources()
				.updateConfiguration(
						config, null);
		
	}

	@Override
	public void onLanguageChanged() {
		// TODO Auto-generated method stub
		
	}
	
	
		
		
	/*
	 * all methods here are for the GCM function
	 */

	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(LOG_TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(LOG_TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(SplashActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                mDisplay.append(msg + "\n");
            	Log.i(LOG_TAG, msg);
            }
        }.execute(null, null, null);
    }
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void sendInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                
                // You should send the registration ID to your server over HTTP, so it
                // can use GCM/HTTP or CCS to send messages to your app.
                sendRegistrationIdToBackend();

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                mDisplay.append(msg + "\n");
            	Log.i(LOG_TAG, msg);
            }
        }.execute(null, null, null);
    }
	
	/**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(LOG_TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
	    
	    /**
	     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	     * or CCS to send messages to your app. Not needed for this demo since the
	     * device sends upstream messages to a server that echoes back the message
	     * using the 'from' address in the message.
	     */
	    private void sendRegistrationIdToBackend() {
	    	int id_menu = params.getId();
			String application_unique_identifier = Installation.id(getApplicationContext());
			String application_version = "0.2.8";
			try {
				application_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				
				e.printStackTrace();
			};
			
			String device_type = "";
			if (isTablet) {
				device_type = "tablet";
			}else {
				device_type = "smartphone";
			}
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			String device_screen_resolution = size.x+"x"+size.y;
			ArrayList<AppHit> hits = ((MyApplication)getApplication()).hits;
			AppSession appSession = new AppSession(id_menu,
					"production", 
					"sales", 
					application_unique_identifier, 
					application_version , 
					regid, 
					android.os.Build.MANUFACTURER, 
					android.os.Build.MODEL,
					"android", 
					device_screen_resolution, 
					5, 
					android.os.Build.VERSION.SDK_INT+"", 
					device_type, 
					session_id, 
					tsLong,
					System.currentTimeMillis()/1000 , 
					hits );
			ArrayList<AppSession> appSessions = new ArrayList<AppSession>();
			appSessions.add(appSession);
			AppJsonWriter appJsonWriter = new AppJsonWriter();
			String str = appJsonWriter.writeJson(appSessions);
			String endpoint = SERVER_URL;
			String body = str;
			int status = 0;
			try {
				status = AppJsonWriter.post(endpoint, body);
			} catch (IOException e) {
				Log.e(LOG_TAG, "request couldn't be sent "+status);
				e.printStackTrace();
			}
	    }

}
