package com.android.ring.devutils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.android.log.CLog;

public class GpsUtils {
	private CLog clog=new CLog(GpsUtils.class.getSimpleName());
	private Context context;
	private String oldProvider;
	private String provider;
	private final static String PROVIDER="network,gps";
	private LocationManager locationManager;
	private LocationListener gpsLocationListener,networkLocationListener;
	public GpsUtils(Context context){
		this.context=context;
	}
	
	public void onCreate() {
		clog.println("onCreate");
		try {
			oldProvider = Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			clog.println("oldProvider£º"+oldProvider);
			if (!PROVIDER.equals(oldProvider)) {
				Settings.Secure.putString(context.getContentResolver(),
						Settings.Secure.LOCATION_PROVIDERS_ALLOWED, PROVIDER);
			}
		} catch (SecurityException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onDestroy() {
		clog.println("onDestroy");
		if (oldProvider != null) {
			Settings.System.putString(context.getContentResolver(),
					Settings.System.LOCATION_PROVIDERS_ALLOWED, oldProvider);
		}
	}
	
	public void onStart(LocationListener gpsLocationListener,LocationListener networkLocationListener){
		clog.println("onStart");
		this.gpsLocationListener=gpsLocationListener;
		this.networkLocationListener=networkLocationListener;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
//		locationManager.setTestProviderEnabled("gps", true);
		provider = locationManager.getBestProvider(criteria, true);
		clog.println("provider="+provider);
		if (provider == null) {
			AlertDialog.Builder builder = new Builder(context);
			builder.setMessage("GPS is disAble");
			builder.setPositiveButton(android.R.string.ok,
					new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
					context.startActivity(intent);
//							context.startActivityForResult(intent, 0);
					// Main.this.finish();
				}
			});
			builder.create().show();
			return;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			clog.println("LastKnownLocation longitude:"+ location.getLongitude()+" latitude:"+ location.getLatitude()+" altitude:"+ location.getAltitude());
		}else{
			clog.println("location null");
		}
		
		if(networkLocationListener==null)
			networkLocationListener= new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				clog.println("network longitude:"+ location.getLongitude()+" latitude:"+ location.getLatitude()+" altitude:"+ location.getAltitude());
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				clog.println("network onProviderDisabled");
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				clog.println("network onProviderEnabled");
			}
			
			@Override
			public void onStatusChanged(String provider, int status,Bundle extras) {
				clog.println("network onStatusChanged");
			}
		};
		if(gpsLocationListener==null)
			gpsLocationListener= new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				clog.println("gps longitude:"+ location.getLongitude()+" latitude:"+ location.getLatitude()+" altitude:"+ location.getAltitude());
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				clog.println("gps onProviderDisabled");
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				clog.println("gps onProviderEnabled");
			}
			
			@Override
			public void onStatusChanged(String provider, int status,Bundle extras) {
				clog.println("gps onStatusChanged");
			}
		};
		
		locationManager.requestLocationUpdates("network", 10000, (float) 0.0,networkLocationListener);
		locationManager.requestLocationUpdates("gps", 10000, (float) 0.0,gpsLocationListener);
	}
	public void onStop(){
		clog.println("onStop");
		locationManager.removeUpdates(networkLocationListener);
		locationManager.removeUpdates(gpsLocationListener);
	}
//	public void toggleGPS() { 
//		  Intent gpsIntent = new Intent();
//		  gpsIntent.setClassName("com.android.settings",
//		    "com.android.settings.widget.SettingsAppWidgetProvider");
//		  gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
//		  gpsIntent.setData(Uri.parse("custom:3"));
//		  try {
//		   PendingIntent.getBroadcast(context.createPackageContext("com.android.settings", Context.CONTEXT_IGNORE_SECURITY), 0, gpsIntent, 0).send();
//		  } catch (CanceledException e) {
//		   e.printStackTrace();
//		  } catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}
