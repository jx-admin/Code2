package com.accenture.mbank.view;

import com.accenture.mbank.util.LogManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * @author dario.sconocchia
 */
public class GPSListener implements LocationListener {
	private static final String TAG = GPSListener.class.getName();

	private final String[] PROVIDER_STATUS = {
			"Out of Service", 
			"Temporarily Unavailable", 
			"Available"
	};

	private LocationManager manager;
	
	public GPSListener(LocationManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		logLocation(location);
		manager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {
		LogManager.d( "Provider " + provider + " has been disabled.");
	}

	@Override
	public void onProviderEnabled(String provider) {
		LogManager.d( "Provider " + provider + " has been enabled.");		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		LogManager.d("Provider Status Changed: " + provider + 
				", Status="+ PROVIDER_STATUS[status] + ", Extras=" + extras);
	}

	public static void logLocation(Location location) {
		LogManager.d("GPS Location Detected[" + 
				"provider= " + location.getProvider() + ";" +
				"time= " + location.getTime() + ";" +
				"latitude= " + location.getLatitude() + ";" +
				"longitude= " + location.getLongitude() + ";" + 
				"accuracy= " + ((location.hasAccuracy())?location.getAccuracy():"n.a.") + ";" +
				"altitude= " + ((location.hasAltitude())?location.getAltitude():"n.a.") + ";" +
				"speed= " + ((location.hasSpeed())?location.getSpeed():"n.a.") + "]"
		);	
	}
}
