package com.accenture.mbank.view;

import android.content.Context;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
/**
 */
public class GoogleAnalya {
	private GoogleAnalytics mGaInstance;
	private Tracker mGaTracker;
    static final GoogleAnalya INSTANCE = new GoogleAnalya();  
   
    public static GoogleAnalya getInstance() {  
        return INSTANCE;  
    }  
	 public void getGaInstance(Context cxt,String appScreen) {
		 
		  mGaInstance = GoogleAnalytics.getInstance(cxt);
		  mGaTracker = mGaInstance.getTracker("UA-42551791-1");
		  mGaTracker.sendView(appScreen);
	    }
}
        