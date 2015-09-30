package com.aess.androidsso;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

//import com.aess.androidsso.IRemoteServiceCallback;

public class SSOService extends Service {
	private final static String TAG = "PackageService"; 
	public final static String ADD_PACKAGE="android.service.package.addpackage";
	public final static String DELETE_PACKAGE="android.service.package.deletepackage";
	public final static String CATEGORY="android.intent.category.packageservices";
	public final static String apkUriKey="apkUri";
	public final static String appPackageKey="package";

	public static int mLoginResult = 0;

	static IRemoteServiceCallback mCallBack;
	static boolean mActivityStarted = false;

	@Override
	public IBinder onBind(Intent intent) {
        return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		Log.i(TAG, "onStartCommand: " + action);
		if(ADD_PACKAGE.equals(action)){
			//String apkPath=(String) intent.getStringExtra(apkUriKey);
			//installApp(apkPath, null);
		}else if(DELETE_PACKAGE.equals(action)){
			//String pkg=(String) intent.getStringExtra(appPackageKey);
			//unInstallApp(pkg, null);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	Handler mHandler = new Handler(); 
	private void onLogin() {
		Log.i(TAG, "Start vactivity");
		
		Intent it = new Intent();
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		it.setComponent(new ComponentName("com.aess.androidsso",
				"com.aess.androidsso.LoginActivity"));

		startActivity(it);
	}


    private IRemoteService.Stub mBinder = new IRemoteService.Stub() {

		@Override
		public
		String login(String appid)  throws RemoteException {
			mLoginResult = 0;
			if(mActivityStarted) {
				//return "";
			}

			String sessionid = SSOService.this.getSharedPreferences("AEMM_CLIENT", 0).getString("AUTHEN_TOKEN", "");
			if(TextUtils.isEmpty(sessionid)) {
				mHandler.post(new Runnable() {
			        public void run() {
			            onLogin();
			        }
			    });
				/*
				Log.i(TAG, "Start vactivity");
				
				Intent it = new Intent();
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.setComponent(new ComponentName("com.aess.androidsso",
						"com.aess.androidsso.LoginActivity"));
				startActivity(it);

/*				
				mActivityStarted = true;
				Intent it = new Intent(SSOService.this, LoginActivity.class);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.putExtra("appid", appid);
				SSOService.this.startActivity(it);
				*/
			} else {
				if(mCallBack != null) mCallBack.loginResult(0);
			}
			return sessionid;
		}

		@Override
		public
		void logout(String appid)  throws RemoteException {
			Editor editor = SSOService.this
			.getSharedPreferences("AEMM_CLIENT", 0).edit();
			editor.putString("AUTHEN_TOKEN", "");
			editor.commit();
		}

		@Override
		public String getToken() throws RemoteException {
			String sessionid = SSOService.this.getSharedPreferences("AEMM_CLIENT", 0).getString("AUTHEN_TOKEN", "");
			if(TextUtils.isEmpty(sessionid)) {
				//if(mLoginResult != 0)
				//	throw new RemoteException();
			}
			return sessionid;
		}

		@Override
		public
		int getLoginResult() {
			return mLoginResult;
		}

		@Override
		public void setLoginCallback(IRemoteServiceCallback callback) {
			mLoginResult = 0;
			mCallBack = callback;
		}
    };
}
