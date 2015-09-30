package com.aess.aemm.appmanager;

import java.util.List;

import com.aess.aemm.appmanager.ApkInstallService.UpdateCallBack;
import com.aess.aemm.view.data.Appdb;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class ApkInstall {
	public final static String TAG = "ApkInstall"; 

	public ApkInstall(Context context) {
		mContext = context;
    	context.bindService(new Intent(context, ApkInstallService.class), //new Intent("com.aess.aemm.appmanager.AppUpdate"),
	            mConnection, Context.BIND_AUTO_CREATE);
	}
	
    public void destroy() {
    	if(mAppUpdate != null) {
    		mContext.unbindService(mConnection);
    	}
    }
    
    public void install(final Appdb app, final UpdateCallBack callBack) {
    	Log.d(TAG, "install");
    	mAppUpdate.install(app, callBack);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                IBinder binder) {
        	mAppUpdate = ((ApkInstallService.LocalBinder)binder).getService();
        }
 
        public void onServiceDisconnected(ComponentName className) {
        	mAppUpdate = null;
        }
    };
    
    public boolean serviceIsOk() {
    	if(mAppUpdate == null){
    		return false;
    	}
    	return true;
    }
    
    public boolean clearDownList() {
    	if(mAppUpdate == null){
    		return false;
    	}
    	mAppUpdate.cleanDownList();
    	mAppUpdate.cancel();
    	return true;
    }
    
    public List<Appdb> getPendingDownload() {
    	if(mAppUpdate==null){
    		return null;
    	}
    	return mAppUpdate.getPendingDownload();
    }
    
	private ApkInstallService mAppUpdate = null;
	private Context mContext;
}
