package com.aess.androidsso;

import java.io.File;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.aess.androidsso.*;

public class SSOClient {
	private final static String TAG = "SSOClient"; 
	private IRemoteService mService = null;
	private Context mContext;
	private SSOCallBack mCallBack;

	public SSOClient(Context context, SSOCallBack callback) {
		mContext = context;
		mCallBack = callback;
    	mContext.bindService(new Intent("com.aess.androidsso.SSOService"),
	            mConnection, Context.BIND_AUTO_CREATE);
	}

    public void destroy() {
   		mContext.unbindService(mConnection);
    }

	public static interface InstallCallBack {
		public void installResult(String packageName, int error);
		public void uninstallResult(String packageName, boolean succeeded);
	}

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                IBinder service) {
            mService = IRemoteService.Stub.asInterface(service);
            mCallBack.serviceStarted();
        }
 
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    public static boolean deleteFile(String filePath) {
    	File file;
    	try {
    		file = new File(filePath);
    	} catch (Exception e) {
    		file = null;
    	}
    	if ((file != null) && !file.delete()) {
            return false;
        } else {
            return true;
        }
    }
    
	public String login(Context context, String appid) throws Exception {
		
		String sessionid = mService.getToken();

		Intent it = new Intent("android.intent.action.AEMMMAIN");
		//it.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		//it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//it.setPackage("com.xuye.ThridPartyService");
		it.setClassName("com.aess.androidsso",
			"com.aess.androidsso.LoginActivity");
		it.addCategory("android.intent.category.AEMMLAUNCHER");
		it.setAction("com.aess.androidsso.LoginActivity");
		context.startActivity(it);
		if(true) return sessionid;

		sessionid = mService.login(appid);
		//while(TextUtils.isEmpty(sessionid) && mService.getLoginResult() == 0) {
		//	Log.i(TAG, "wait login");
		//	Thread.sleep(1000);
		//}
		//sessionid = mService.getToken();
		return sessionid;//return mService.getToken();
	}

	public void logout(String appid) {
		try {
			mService.logout(appid);
		} catch (RemoteException e) {
		}
	}

	public String getToken() {
		try {
			return mService.getToken();
		} catch (RemoteException e) {
		}
		return "";
	}

	
	public class CallBack extends IRemoteServiceCallback.Stub {
		@Override
		public void loginResult(int error) throws RemoteException {
			mCallBack.loginResult(error);
		}
	}
	
	public void setCallback() {
		try {
			mService.setLoginCallback(new CallBack());
		} catch (RemoteException e) {
			Log.i("", "");
		}
	}
}
