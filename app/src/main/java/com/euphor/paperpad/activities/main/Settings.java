/**
 * 
 */
package com.euphor.paperpad.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.euphor.paperpad.R;

/**
 * @author euphordev02
 *
 */
public class Settings extends PreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState); 
//		ExceptionHandler.register(this, "http://www.zeitgeistmorocco.com/testAnd"); 
//        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
//        		Environment.getExternalStorageDirectory().getPath()+"/Paperpad", "http://www.zeitgeistmorocco.com/testAnd/upload.php"));
        addPreferencesFromResource(R.xml.prefs);       
        
//        ListView v = getListView();
//        v.addFooterView(new Button(this));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	 getMenuInflater().inflate(R.menu.setting_menu, menu);
         return true;
         }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_refresh:
            // app icon in action bar clicked; go home
        	item.setActionView(R.layout.indeterminate_progress_action);
        	Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
	        Bundle b = new Bundle();
			b.putBoolean("REQUEST_UPDATE", true);
			intent.putExtras(b);
	        startActivity(intent);
            return true;
        }
        return false;
    }
}
