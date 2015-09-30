package com.aess.aemm.gps.tmp;
//package com.aess.aemm.gps;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//@Deprecated
//public class GPSLocationService extends Service {
//	
////	private static String TAG = "GPSLocationService";
//	private LocationInfo locationInfo = null;
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//	public void onCreate(){
//		try{
//	      locationInfo = new LocationInfo(this);
//	      locationInfo.onCreate();
//	      locationInfo.init();}
//	    catch(Exception e){
//		  e.printStackTrace();
//		
//	    }
//	      super.onCreate();
//	}
//	
//	public void onDestroy(){
//		if(locationInfo != null){
//			locationInfo.onDestroy();
//		}
//	}
//
//}
