/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.euphor.paperpad;

import static com.euphor.paperpad.utils.CommonUtilities.SERVER_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.android.volley.RequestQueue;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.utils.Installation;
import com.euphor.paperpad.utils.WakefulBroadcastReceiver;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.utils.jsonUtils.AppJsonWriter;
import com.euphor.paperpad.utils.jsonUtils.AppSession;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.os.Build;
//import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


/**
 * This {@code WakefulBroadcastReceiver} takes care of creating and managing a
 * partial wake lock for your app. It passes off the work of processing the GCM
 * message to an {@code IntentService}, while ensuring that the device does not
 * go back to sleep in the transition. The {@code IntentService} calls
 * {@code GcmBroadcastReceiver.completeWakefulIntent()} when it is ready to
 * release the wake lock.
 */

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	
	private RequestQueue mRequestQueue;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	//get GCM registration id if failed to do it directly
    	try {
    		String regId = intent.getExtras().getString("registration_id");
    		if(regId != null && !regId.equals("")) {
    			sendRegistrationIdToBackend(context, regId);
    		}

    	} catch (Exception e) {
			// TODO: handle exception
		}
    	
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
    
    
    /**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
     * @param regId 
	 */
	private void sendRegistrationIdToBackend(Context context, String regId) {
		int id_menu = MainActivity.params.getId();
		String application_unique_identifier = Installation.id(context);
		String application_version = "0.2.8";
		try {
			application_version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {

			e.printStackTrace();
		};

		String device_type = "";
		if (MainActivity.isTablet) {
			device_type = "tablet";
		}else {
			device_type = "smartphone";
		}
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

			display.getSize(size);
		}else {
			size.x = display.getWidth();  // deprecated
			size.y = display.getHeight();
		}


		String device_screen_resolution = size.x+"x"+size.y;

		ArrayList<AppHit> hits = new ArrayList<AppHit>();
		AppSession appSession = new AppSession(id_menu,
				MainActivity.prod_or_sand , 
				"sales", 
				application_unique_identifier, 
				application_version , 
				regId, 
				Build.MANUFACTURER,
				Build.MODEL,
				"android", 
				device_screen_resolution, 
				5, 
				Build.VERSION.SDK_INT+"",
				device_type, 
				"", 
				System.currentTimeMillis()/1000,
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
			Log.e("GcmBroadcastReceiver", "request couldn't be sent "+status);
			e.printStackTrace();
		}
	}
}
