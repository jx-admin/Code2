package com.android.ring.devutils;

import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class PowerUtils {
	Context context;
	PowerManager pm ;
	public PowerUtils(Context context){
		this.context=context;
		 if (wakeLock ==null) {
             Log.d(TAG,"Acquiring wake lock");
             pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
             wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getCanonicalName());
         }
	}
	String TAG=PowerUtils.class.getSimpleName();
	
	WakeLock wakeLock;
	long startTime;
	public void test(){
		startTime=System.currentTimeMillis();
		final Timer mTimer=new Timer();
		mTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				if(System.currentTimeMillis()-startTime>=20000){
					mTimer.cancel();
					releaseWakeLock();
				}
				// TODO Auto-generated method stub
				acquireWakeLock();
			}}, 10000, 1000);
	}

	private void acquireWakeLock() {
        Log.d(TAG,"acquireWakeLock isScreenOn "+pm.isScreenOn());
//        pm.goToSleep(10000);
        
	        
	         wakeLock.acquire();
	        
	    }


	private void releaseWakeLock() {
	        if (wakeLock !=null&& wakeLock.isHeld()) {
                Log.d(TAG,"releaseWakeLock");
	            wakeLock.release();
	            wakeLock =null;
	        }

	    }
}
