package com.android.accenture.aemm.dome;

import com.aemm.config_demo.ListenerService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;

public class ApkHall extends Activity {
	public static ApkHall apkHall;
	public boolean isPause=true;
	public static final String dataname="apkhall";
	ApkManager mApkManager;
	ApkReceiver mBootReceiver;
	View roomV;
	GridView gv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("VV","apkHall onCreate");
		setContentView(R.layout.room);
		gv = (GridView) findViewById(R.id.installed_gv);
		mApkManager = new ApkManager(this, gv);
		roomV = findViewById(R.id.room_linear);
		init();
		Utils.toClock(this);
		apkHall=this;
        ListenerService.startListen(this);
	}
	private void init(){
		mBootReceiver = new ApkReceiver(mApkManager);
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		mIntentFilter.addDataScheme("package");
		registerReceiver(mBootReceiver, mIntentFilter);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			roomV.setBackgroundResource(R.drawable.bgw);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			roomV.setBackgroundResource(R.drawable.bgh);
		}
	}
	@Override
	protected void onResume() {
		Log.v("VV","apkHall onResume");
		isPause=false;
		super.onResume();
	}
	protected void onPause(){
		Log.v("VV","apkHall onPause");
		isPause=true;
		SharedPreferences preferences = getSharedPreferences(dataname, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=preferences.edit();
		editor.clear();
		editor.commit();
		mApkManager.toSave(editor);
		editor.commit();
		super.onPause();
	}
	
	protected void onStop(){
		Log.v("VV","apkHall onStop");
		super.onStop();
	}
	
	
	@Override
	protected void onDestroy() {
		Log.v("VV","apkHall ondestroy");
		unregisterReceiver(mBootReceiver);
		Log.v("VV","kill");
//		android.os.Process.killProcess(android.os.Process.myPid());
//		ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);   
//		manager.restartPackage(getPackageName());
//		manager.forceStopPackage(getPackageName());
		super.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v("VV","apkHall onConfigurationChanged");
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){   
			roomV.setBackgroundResource(R.drawable.bgw);
		}else{   
			roomV.setBackgroundResource(R.drawable.bgh);
		} 
		mApkManager.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}
	public static boolean addApp(Appdb app){
		Log.v("VV","apkHall addApp");
		if(ApkHall.apkHall.mApkManager!=null){
			ApkHall.apkHall.mApkManager.addApp(app);
			return true;
		}
		return false;
	}
	public static boolean toLock(Context c){
		return Utils.clockScreem( c);
	}
	public static boolean unLock(Context c){
		return Utils.unClockScreem(c);
	}
	public static void startHall(Context c){
		c.startActivity(new Intent(c,ApkHall.class));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(mApkManager.onKeyDown( keyCode, event)){
			return true;
		}else if(keyCode==android.view.KeyEvent.KEYCODE_BACK){
			Log.v("VV","key back");
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public static boolean addWifiConfig(Context context,String ssid,String word){
		boolean result=false;
		WifiManager mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\""+ssid+"\"";
		conf.preSharedKey = "\""+word+"\"";
		conf.status = WifiConfiguration.Status.ENABLED;
		conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		boolean needClose=false;
		if(!mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(true);
			Log.v("VV","wifi is close to open");
			needClose=true;
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int id=mWifiManager.addNetwork(conf);
		Log.v("VV","add conf id:"+id);
		if(id>=0){
			result=mWifiManager.saveConfiguration();
			Log.v("VV","save conf:"+result);
		}
		
		if(needClose){
			mWifiManager.setWifiEnabled(false);
			Log.v("VV","close wifi");
		}
		return result;
	}
}
