package com.android.accenture.aemm.express.updataservice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.accenture.aemm.express.AppItem;
import com.android.accenture.aemm.express.LocationInfo;
import com.android.accenture.aemm.express.Login;
import com.android.accenture.aemm.express.Main;
import com.android.accenture.aemm.express.MoshinInformation;
import com.android.accenture.aemm.express.User;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;
import com.android.accenture.aemm.express.updataservice.ServiceMessage.autoOrManual;



public class ListenerService extends Service
{
	//private static Context mContext;
	public static final String CLOSE_ACTION="LISTENER_SERVICE_CLOSE_ACTION";
	public static final String HALL_START_ACTION="HALL_START_ACTION";
	public static final String HALL_CLOSE_ACTION="HALL_CLOSE_ACTION";

	public static final String USER_LOGIN_ACTION="USER_LOGIN_ACTION";
	public static final String USER_UPDATE_ACTION="USER_UPDATE_ACTION";

	public static final String USER_FIRSTRUN_ACTION="USER_FIRSTRUN_ACTION";

	public static final String NEW_APP_PUSH="NEW_APP_PUSH";
	public static final String ACTION_INSTALLED_APP="ACTION_INSTALLED_APP";

    final Timer mRestartTimer = new Timer("RestartTimer");
    TimerTask mRestartTask = new TimerTask() { 
        @Override
        public synchronized void run() {
			Message msg = ListenerService.mHandler.obtainMessage(ListenerService.CHECK_HEART_BEAT);
			ListenerService.mHandler.sendMessage(msg);
        }
    };

	public static enum LOGIN_STATUS
	{
		LOGIN_OK,
		LOGIN_ERROR

	}
	//private List<Appdb> appLs;
	private ListenerThread2 mListenThread2 = null;
	public static Handler mHandler;
	public static boolean configvpn = false;;

	public static final int TIMER_MESSAGE = 1;
	public static final int READ_OUTTIME = 2;
	public static final int SESSION_EXPIRED = 3;
	public static final int ACCOUNT_ERROR = 4;
	public static final int NETWORK_ERROR = 5;
	public static final int CHECK_HEART_BEAT = 6;

	
	static checkUpdateThread manCheckUpdateThread = null;
	public enum pushCommandType {
		HEART_BEAT_RSP, PUSH_LOGIN,PUSH_PROFILE_UPDATE, PUSH_APP_UPDATE,PUSH_ALL_UPDATE,LOCK_SCREEN, APP_LOCK, WIPE_DATA,PUSH_NONE, EXCEPTION
	}

	public static class pushResult
	{
		pushCommandType pushType;
		String errMsg;
		String alertMsg;

	}
	public static void start(Context context){
		Intent i = new Intent(context, ListenerService.class); 
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startService(i); 
	}
	public static void startListen(Context context)
	{
		//mContext = context;
		Intent serviceInfo = new Intent("123");
		serviceInfo.setClass(context, ListenerService.class);
		context.startService(serviceInfo);
	}
	public static void startInstallApp(Context context,String apkId,String version,String filePath){
		Intent serviceInfo = new Intent(ACTION_INSTALLED_APP);
		serviceInfo.putExtra("apkId", apkId);
		serviceInfo.putExtra("version", version);
		serviceInfo.putExtra("fn", filePath);
		serviceInfo.setClass(context, ListenerService.class);
		context.startService(serviceInfo);
	}
	public void onCreate()
	{
		super.onCreate();
		Log.i(Util.TAG, Util.TAG + "service has already launch!");


		//start phone Listener
		//MoshinInformation sysinfo = MoshinInformation.getInstance(this);
		//sysinfo.onStart();

		//start location listener
		Log.i(Util.TAG, Util.TAG + "create locationinfo!");
		LocationInfo location=LocationInfo.getInstance(this);
		
		MoshinInformation mosInfo = MoshinInformation.getInstance(this);
		mosInfo.onStart();
		
		// Bug #2750 shxn
		mListenThread2 = ListenerThread2.getInstance(this);
		mListenThread2.start();   
		// Bug #2750 shxn
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {

				case READ_OUTTIME:
					break;
					
				case NETWORK_ERROR:
//					mListenThread.stopListenerThread();
//					mListenThread = null;
					break;

				case SESSION_EXPIRED:
					Login.showLogin(ListenerService.this,null,null,null,true);
					break;
				case ACCOUNT_ERROR:
					String msgstr = null;
					if (msg.obj != null)
						msgstr = (String)msg.obj;
					//delete info in config
					configPreference.delUser(ListenerService.this);
					Login.showLogin(ListenerService.this,null,null,msgstr,true);
					break;
				case CHECK_HEART_BEAT:

					if (null==mListenThread2) {
						mListenThread2 = ListenerThread2.getInstance(ListenerService.this);
						mListenThread2.start();  
					}
					mListenThread2.sendHeartbeat();
					break;
				}
			}

		};
		// Bug #2750 shxn delete 2
		mRestartTimer.scheduleAtFixedRate(mRestartTask, 3000, 15000);
	}

	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public LOGIN_STATUS checkSessionId()
	{

		String value = configPreference.getSessionId(this);
		Log.i(Util.TAG, "session id is ");
		Log.i(Util.TAG, "session id is " + value);
		if (value == null)
		{
			Log.i(Util.TAG, "session id is " + value);
			return LOGIN_STATUS.LOGIN_ERROR;

		}
		else
		{
			//post info to check
			return LOGIN_STATUS.LOGIN_OK;
		}

	}
	
	public boolean  isNeedUpdate()
	{

		boolean bFlag = false;
		String value = configPreference.getLastUpdate(this);
		long current = System.currentTimeMillis();
		Log.i("isNeedUpdate","current is " + String.valueOf(current));
		Log.i("isNeedUpdate","lastUpdatems is " + value);
		if (value != null)
		{
			long lastUpdatems = Long.valueOf(value);
			Log.i("isNeedUpdate","current is " + String.valueOf(current));
			Log.i("isNeedUpdate","lastUpdatems is " + String.valueOf(lastUpdatems));
			String checkcyle = configPreference.getCheckCycle(this);
			if (checkcyle != null)
			{
				long checkcylems = Long.valueOf(checkcyle)*1000;
				if (current - lastUpdatems >= checkcylems)
				{
					bFlag = true;
					return bFlag;
				}
					
			}
		}
		
		return bFlag;

	}

	public static void lauchCheckUpdate(Context context, autoOrManual am)
	{
		Log.i(Util.TAG, Util.TAG + ": context = " + context);
		if(manCheckUpdateThread!=null){
			//Comment by fengyun for bug 3227
			//manCheckUpdateThread.setAutoorManual(ServiceMessage.autoOrManual.AUTOUPDATE);
		}
		manCheckUpdateThread= new checkUpdateThread(context);
		manCheckUpdateThread.setAutoorManual(am);
		
		manCheckUpdateThread.start();
	}

	//	SocketThread mSocketThread;
	private void firstCheck(){
		Log.v(Util.TAG,"firstCheck");
		//check sessionId when aemm starts
		if (checkSessionId() == LOGIN_STATUS.LOGIN_ERROR)
		{
			//and if it need to login
			Login.showLogin(this,null,null,null,true);
		}
	}
	public void onStart(Intent intent, int startId)
	{
		String action = intent.getAction();// intent.getAction();
		if (action == null) {

		} else if (action.equals(USER_LOGIN_ACTION)) {
			String userName = intent.getStringExtra(ServiceMessage.NAME);
			String passWord = intent.getStringExtra(ServiceMessage.WORD);
			Log.v(Util.TAG, "login: " + userName + " " + passWord+" "+MoshinInformation.getDeviceId(this));
			configPreference.putUser(this,new User(userName,passWord));
			// start login
			if(manCheckUpdateThread!=null){
				manCheckUpdateThread.setAutoorManual(ServiceMessage.autoOrManual.AUTOUPDATE);
			}
			manCheckUpdateThread=new checkUpdateThread(this);
			manCheckUpdateThread.setAutoorManual(ServiceMessage.autoOrManual.LOGINUPDATE);
			manCheckUpdateThread.start();
			
			return;
		} else if (action.equals(USER_UPDATE_ACTION)) {

			Log.i(Util.TAG, "ListenerService USER_UPDATE_ACTION");
			//Runnable manCheckUpdateThread = new checkUpdateThread(
					//this,ServiceMessage.autoOrManual.MANUALUPDATE);
			if(manCheckUpdateThread!=null){
				manCheckUpdateThread.setAutoorManual(ServiceMessage.autoOrManual.AUTOUPDATE);
			}
			manCheckUpdateThread=new checkUpdateThread(this);
			manCheckUpdateThread.setAutoorManual(ServiceMessage.autoOrManual.MANUALUPDATE);
			manCheckUpdateThread.start();
			
			return; //HALL_START_ACTION
		} else if (action.equals(HALL_START_ACTION)) {
			// check if it need to post update
			//Time t=new Time(); 
			//t.setToNow();
			/*
			if (isNeedUpdate())
			{
				Log.i(Util.TAG, "need to update");
				checkUpdateThread manCheckUpdateThread = new checkUpdateThread(
						this,ServiceMessage.autoOrManual.AUTOUPDATE);
			}
			else
			{
				Log.i(Util.TAG, "does not need to update");
				String checkcyle = configPreference.getCheckCycle(this);
				if (checkcyle != null)
				{
					long ms =  Long.valueOf(checkcyle)*1000;
					SetUpdateTimer(this,ms);
				}
			}*/
			firstCheck();
			//checkListener();
			return;
		}else if(action.equals(ACTION_INSTALLED_APP)){
			String apkId=intent.getStringExtra("apkId");
			String version=intent.getStringExtra("version");
			String fn=intent.getStringExtra("fn");
			AppItem app=null;
			if(apkId!=null&&version!=null){
				ApkProfileContent value=ApkProfileContent.queryApkProfile(this,apkId,version);
				if(value!=null){
					app=new AppItem(value);
					app.setApkFileName(fn);
					app.setApkVersion(version);
					app.toSave(this);
				}
			}
			if(app!=null){
				Main.installApplication(app,this);
			}
		}else if(action.equals(ListenerService.HALL_CLOSE_ACTION)){
			if(manCheckUpdateThread!=null){
				manCheckUpdateThread.setAutoorManual(ServiceMessage.autoOrManual.AUTOUPDATE);
			}
		}
		//
		Log.i(Util.TAG, Util.TAG + ": " + "ListenerService onStart");

	}
	
//	private Runnable getUpdateRunnalbe()
//	{
//		Runnable manCheckUpdateThread = new checkUpdateThread(
//				this,ServiceMessage.autoOrManual.MANUALUPDATE);
//		
//		return manCheckUpdateThread;
//	}
	
	private void checkListener(){
//		Log.i(Util.TAG, Util.TAG + ": " + "ListenerService onStart");
//
//		if(mListenThread==null){
//			Log.i(Util.TAG, Util.TAG + ": " + "onStart mListenThread is null  to create...");
//			mListenThread = new ListenerThread(this);
//		} else {
//			Log.i(Util.TAG, Util.TAG + ": " + "onStart mListenThread not null");
//			if(!mListenThread.isAlive()){
//				//Log.i(Util.TAG, Util.TAG + ": " + "onStart mListenThread is notAlive  to start...");
//				mListenThread.ListenerStart();
////			}else{
//				//Log.i(Util.TAG, Util.TAG + ": " + "onStart mListenThread is Alive");
//			}
//		}
	}

	public static void SetUpdateTimer(Context context, long lms)
	{
		Log.i(Util.TAG, "SetTTLTimer");
		// 获取闹钟管理的实例：
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// 取消上次Retry Timer：
		Intent i = new Intent(context, serviceReceiver.class);
		i.setAction("timely_update");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				i, 0);
		//alarmManager.cancel(pendingIntent);

		// 设置单次闹钟：
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ lms, pendingIntent);
		

		Log.i(Util.TAG, Util.TAG_PREFIX + "SetTTLTimer end. time = " + lms );

	}
	
	public  static void CancelUpdateTimer(Context context)
	{
		Log.i(Util.TAG, "CancelUpdateTimer");
		// 获取闹钟管理的实例：
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// 取消上次Retry Timer：
		Intent i = new Intent(context, serviceReceiver.class);
		i.setAction("timely_update");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				i, 0);
		alarmManager.cancel(pendingIntent);

	}

	//	
	@Override
	public void onDestroy() {
		Log.i(Util.TAG, Util.TAG + ": " + "onDestroy ListenThread");
		Intent i = new Intent(CLOSE_ACTION);   
		sendBroadcast(i);
		mListenThread2.stop();
		//MoshinInformation sysinfo = MoshinInformation.getInstance(this);
		//sysinfo.onDestroy();
		LocationInfo location=LocationInfo.getInstance(this);
		location.onDestroy();
		super.onDestroy();
	}

}



