package com.aemm.config_demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.accenture.aemm.dome.ApkHall;
import com.android.accenture.aemm.dome.Appdb;
import com.android.accenture.aemm.dome.Logger;
import com.android.accenture.aemm.dome.LoggerFactory;
import com.android.accenture.aemm.dome.ServiceDia;
import com.android.accenture.aemm.dome.Utils;

public class ListenerService extends Service
{
	private Logger logger;
	public static final String CLOSE_ACTION="LISTENER_SERVICE_CLOSE_ACTION";
	private List<Appdb> appLs;
	private HeartBeatSocketClient mHeartBeatSocketClient;
	private String host="aemm-dev.imolife.com";
	private int port=10086;
//	private int port=10087;
	public Handler mHandler;
	
	public static final int TIMER_MESSAGE = 1;
	public static final int READ_OUTTIME=2;
	
	public void onCreate()
	{
		logger=LoggerFactory.getLogger(this.getClass());
		appLs=new ArrayList<Appdb>();
		appLs.add(new Appdb("别碰我",Utils.ICONPATH+"/biepengwo.png",Utils.ICONPATH+"/biepengwo_1.png",Utils.APKPATH+"/biepengwo.apk"));
	    appLs.add(new Appdb("火影五子棋",Utils.ICONPATH+"/huoyingfive.png",Utils.ICONPATH+"/huoyingfive_1.png",Utils.APKPATH+"/huoyingfive.apk"));
	    appLs.add(new Appdb("ACV",Utils.ICONPATH+"/net.androidcomics.acv.png",Utils.ICONPATH+"/net.androidcomics.acv_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
		
	    super.onCreate();
	    logger.i("ListenerService has created");
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
            	logger.i("mHandler receve message: "+msg.what);
            	
            	switch (msg.what) {
        		case TIMER_MESSAGE: {
        			logger.i("accept:"+msg.obj);
//        			Intent intent = new Intent(ListenerService.this, Activity2.class);
//        			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//        			startActivity(intent);
        			
    				
        			//onNoteAppPush(Splash.mContext);
        			//onNoteAppPush(getBaseContext());
        			//Splash.onLockScreen(ListenerService.this);
        			//Util.showShortToastMessage(ListenerService.this, "app push ok");
        			
        			//Intent i = new Intent();
        			//i.setAction("app_push_command");
        			//ListenerService.this.sendBroadcast(i);
        			command((String) msg.obj);
        			break;
        		}
        		case READ_OUTTIME:
        			logger.i("handler to restart command thread");
        			reOnstart();
        			break;
            }
            	
            }
    	};
	     
	}
	 public void command(String man){
		 if("A".equalsIgnoreCase(man)){
			 ApkHall.toLock(this);
		 }else if("B".equalsIgnoreCase(man)){
			 ApkHall.unLock(this);
		 }else if("C".equalsIgnoreCase(man)){
			 Util.onDownloadWifiCfg(this); 
		 }else if("1".equals(man)){
			 toSave(appLs.get(0));
		 }else if("2".equals(man)){
			 toSave(appLs.get(1));
		 }else if("3".equals(man)){
			 toSave(appLs.get(2));
		 }
	 }
	 public void toSave(Appdb app){
		 
		 app.setFlag(Appdb.NEVER_SETUP);
		 if(ApkHall.apkHall==null||ApkHall.apkHall.isPause){
			 SharedPreferences preferences = getSharedPreferences(ApkHall.dataname, Activity.MODE_PRIVATE);
			 
			 
			 int count = preferences.getInt("count", 0);
			 Log.v("VV", " check Appdb is exist..." + count);
			 int i=0;
			 for (; i < count; i++) {
				 Appdb appS = new Appdb();
				 appS.formSharedPreferences(preferences, i);
				 if(appS.getApkName().equalsIgnoreCase(app.getApkName())){
					 Log.v("VV", ".. exist");
					 if(appS.getFlag()==Appdb.INSTALLED){
						 return;
					 }
					 break;
				 }
			 }
			 if(i>=count){
			 SharedPreferences.Editor editor=preferences.edit();
			 editor.putInt("count",count+1);
			 app.toSharedPreferences(editor, count);
			 editor.commit();
			ApkHall.addApp(app);
			 }
			ServiceDia.showAddApp(this,app.getApkName());
		}else{
			ApkHall.addApp(app);
		}
	 }
	public IBinder onBind(Intent intent)
    {
        return null;
    }
	
	public static void startListen(Context context)
	{
		Intent serviceInfo = new Intent("123");
		serviceInfo.setClass(context, ListenerService.class);
        context.startService(serviceInfo);
	}
	
	public void onStart(Intent intent, int startId)
    {
		Log.i(Util.TAG, Util.TAG + ": " + "ListenerService onStart");
		checkListener();
    }
	
	private void reOnstart(){
		mHeartBeatSocketClient.onStop();
		mHeartBeatSocketClient = new HeartBeatSocketClient(mHandler,host,port);
		mHeartBeatSocketClient.start();
	}
	
	private void checkListener(){
		if(mHeartBeatSocketClient==null){
			logger.i("onStart mHeartBeatSocketClient is null  to create...");
			mHeartBeatSocketClient = new HeartBeatSocketClient(mHandler,host,port);
		}else{
			logger.i("onStart mHeartBeatSocketClient not null");
		}
		if(!mHeartBeatSocketClient.isAlive()){
			logger.i("onStart mHeartBeatSocketClient is notAlive  to start...");
			mHeartBeatSocketClient.start();
		}else{
			logger.i("onStart mHeartBeatSocketClient is Alive");
		}
	}
	@Override
	public void onDestroy() {
		logger.i("onDestroy ListenThread");
		Intent i = new Intent(CLOSE_ACTION);   
        sendBroadcast(i);
		mHeartBeatSocketClient.onStop();
		super.onDestroy();
	}
	
}



