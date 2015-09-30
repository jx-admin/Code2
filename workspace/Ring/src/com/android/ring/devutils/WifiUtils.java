package com.android.ring.devutils;

import com.android.log.CLog;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiUtils {
	private CLog cLog=new CLog(WifiUtils.class.getSimpleName());
	private Context context;
	private WifiManager mWifiManager ;
	private boolean oldEnable;
	private boolean enable;
	private static final boolean ENABLE=true;
	public WifiUtils(Context context){
		this.context=context;
	}
	
	public void onCreate(){
		cLog.println("onCreate");
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		oldEnable=mWifiManager.isWifiEnabled();
		cLog.println("old wifi "+oldEnable);
		if(oldEnable!=ENABLE) {
			mWifiManager.setWifiEnabled(ENABLE);
		}
		enable=mWifiManager.isWifiEnabled();
		cLog.println("cur wifi "+enable);
	}
	
	public void onDestroy(){
		cLog.println("onDestroy");
		if(oldEnable!=ENABLE){
			mWifiManager.setWifiEnabled(oldEnable); 
		}
		enable=mWifiManager.isWifiEnabled();
		cLog.println("cur wifi "+enable);
	}
	
	
}
