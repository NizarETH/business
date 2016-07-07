/**
 * 
 */
package com.euphor.paperpad.utils;

import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
 
/**
 * @author euphordev02
 *
 */
public final class CommonUtilities {
 
    // give your server registration url here
	public static final String SERVER_URL = "http://backoffice.paperpad.fr/api/statistic/save"; 
 
    // Google project id
    public static final String SENDER_ID = "315637457665"; 
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";
 
    public static final String DISPLAY_MESSAGE_ACTION = "com.euphor.paperpad.DISPLAY_MESSAGE";
 
    public static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
    
    public static String getDateStringCurrentTimeZone(long timestamp) {

        Calendar calendar = Calendar.getInstance();
        TimeZone t = TimeZone.getDefault();

        calendar.setTimeInMillis(timestamp * 1000);
        calendar.add(Calendar.MILLISECOND, t.getOffset(calendar.getTimeInMillis()));


        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.FRANCE);
    }
    
    
}
