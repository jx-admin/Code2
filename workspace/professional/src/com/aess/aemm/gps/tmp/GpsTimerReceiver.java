package com.aess.aemm.gps.tmp;
//package com.aess.aemm.gps;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//public class GpsTimerReceiver extends BroadcastReceiver {
//	public static final String TAG = "GpsTimerReceiver";
//	public static final String GPS = "com.aess.aemm.GPS";
//	
//	@Override
//	public void onReceive(Context arg0, Intent arg1) {
//
//		if (arg1.getAction().equals(GPS)) {
//			AEMMLocation.cancelGpsTimer(arg0);
//			location = AEMMLocation.getInstance(arg0);
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					location.postLoctionInfo();
//				}
//			}).start();
//		}
//	}
//	AEMMLocation location = null;
//}
