package com.euphor.paperpad.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

//import com.crashlytics.android.Crashlytics;

import java.util.Timer;
import java.util.TimerTask;

public class MyLocation {
	Timer timer1;
	LocationManager lm;
	LocationResult locationResult;
	boolean gps_enabled=false;
	boolean network_enabled=false;
	FragmentActivity activity;

	public boolean getLocation(FragmentActivity context, LocationResult result)
	{
		this.activity = context;
		//I use LocationResult callback class to pass location value from MyLocation to user code.
		locationResult=result;
		if(lm==null)
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		
		String allowedProvider = Settings.System.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if(allowedProvider == null) allowedProvider = "";
		if(!allowedProvider.contains(LocationManager.GPS_PROVIDER)){
 
			try {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setIcon(android.R.drawable.ic_dialog_info);
				alert.setTitle(" Activer la location ");
				alert.setNegativeButton(" Ignorer ", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) { 
						
						dialog.dismiss();
					}
				});  
				
				alert.setPositiveButton(" Paramètres ", new DialogInterface.OnClickListener() {

					@Override 
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						activity.startActivity(intent);
						dialog.dismiss();
					}
				});  
				alert.setMessage("Voulez-vous activer le GPS et les réseaux sans fil dans les paramètres de localisation.").show();
			} catch (Exception e) {
				//Crashlytics.logException(e);
				//Crashlytics.log("Alert Dialog crashed for asking for Gps");
				e.printStackTrace();
			}  
			
		} 

		//exceptions will be thrown if provider is not permitted. 
		try{
			gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			}catch(Exception ex){}
		try{
			network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			}
		catch(Exception ex){}

		//don't start listeners if no provider is enabled
		if(!gps_enabled && !network_enabled)
			return false; 

		if(gps_enabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		if(network_enabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		timer1=new Timer();
		timer1.schedule(new GetLastLocation(), 20000);
		return true;
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
	public int index = 0;

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location net_loc=null, gps_loc=null;
			if(gps_enabled)
				gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(network_enabled)
				net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			//if there are both values use the latest one
			if(gps_loc!=null && net_loc!=null){
				if(gps_loc.getTime()>net_loc.getTime())
					locationResult.gotLocation(gps_loc);
				else
					locationResult.gotLocation(net_loc);
				return;
			}

			if(gps_loc!=null){
				locationResult.gotLocation(gps_loc);
				return;
			}
			if(net_loc!=null){
				locationResult.gotLocation(net_loc);
				return;
			}
			if (index < 2) {
				timer1.schedule(new GetLastLocation(), 20000);
				index ++;
			}else {
				locationResult.gotLocation(null);
				index = 0;
			}
			
		}
	}

	public static abstract class LocationResult{
		public abstract void gotLocation(Location location);
	}
}