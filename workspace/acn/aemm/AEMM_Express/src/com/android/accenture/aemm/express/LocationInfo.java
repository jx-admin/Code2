package com.android.accenture.aemm.express;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;


public class LocationInfo {
	public static final String LOGCAT="LOCATION";
	private LocationManager locationManager;
	private android.location.LocationListener llistener;
	private String provider;
//	private String mes;
	private Context context;
	private double longitude;
	private double latitude;
	protected LocationInfo(Context context){
		this.context=context;
	}
	private static LocationInfo sInstance = null;

	
	public  static LocationInfo getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new LocationInfo(context);
			try {
				sInstance.init();
			} catch (Exception e) {
			}
		}
		return sInstance;
	}
	private void init(){
		
		Log.i("location","init");
		Settings.Secure.putString(context.getContentResolver(),
				 Settings.Secure.LOCATION_PROVIDERS_ALLOWED, "gps"/*"network,gps"*/);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Log.i("location","locationManager");
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		//		locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
		provider = locationManager.getBestProvider(criteria, true);
		Log.i("location","locationManager" + provider);
		println(provider);
		if (provider == null) {
			AlertDialog.Builder builder = new Builder(context);
			builder.setMessage(R.string.gps_unable);
			builder.setPositiveButton(android.R.string.ok,
					new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					//							Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
					//							context.startActivityForResult(intent, 0);
					// Main.this.finish();
				}
			});
			builder.create().show();
			//			return;
		}
		android.location.Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			update(location);
			println("lastKnownLocation");
			println("longitude:"+ location.getLongitude());
			println("latitude:"+ location.getLatitude());
			println("altitude:"+ location.getAltitude());
		}

		llistener = new android.location.LocationListener(){
			@Override
			public void onLocationChanged(android.location.Location location) {
				println("onLocationChanged");
				if (location != null) {
					update(location);
					println("longitude:"+ location.getLongitude());
					println("latitude:"+ location.getLatitude());
					println("altitude:"+ location.getAltitude());
				}
				//				locationManager.removeUpdates(this);
				// locationManager.setTestProviderEnabled(provider, false);
			}

			@Override
			public void onProviderDisabled(String provider) {
				println("onProviderDisabled");
			}

			@Override
			public void onProviderEnabled(String provider) {
				println("onProviderEnabled");
			}

			@Override
			public void onStatusChanged(String provider, int status,Bundle extras) {
				println("onStatusChanged: "+provider+" "+status+" "+extras);
			}
		};
		locationManager.requestLocationUpdates(provider, 1000, (float) 1000.0,llistener);

	}
	public void onDestroy() {
		if (provider != null) {
			locationManager.removeUpdates(llistener);
			Settings.System.putString(context.getContentResolver(),
					Settings.System.LOCATION_PROVIDERS_ALLOWED, "");
			// locationManager.setTestProviderEnabled(provider, false);
		}
	}
	private void update(android.location.Location location){
		longitude=location.getLongitude();
		latitude=location.getLatitude();
	}
	private void println(String info) {
		Log.v(LOGCAT, "v:" + info);
	}
	public double getLongitude(){
		return longitude;
	}
	public double getLatitude(){
		return latitude;
	}
}
