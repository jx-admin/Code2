package com.android.test.packageservices;

import android.app.Service;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PackageServices extends Service {
	public static final String LOGCAT="PackageServices";
	public final static String ADD_PACKAGE="android.service.package.addpackage";
	public final static String DELETE_PACKAGE="android.service.package.deletepackage";
	public final static String CATEGORY="android.intent.category.packageservices";
	public final static String apkUriKey="apkUri";
	public final static String appPackageKey="package";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		String action=intent.getAction();
		if(ADD_PACKAGE.equals(action)){
			String apkPath=(String) intent.getStringExtra(apkUriKey);
			Log.v(LOGCAT, "onStart uri:"+apkPath);
//			if(uri!=null)
			installApp(apkPath);
		}else if(DELETE_PACKAGE.equals(action)){
			String pkg=(String) intent.getStringExtra(appPackageKey);
			Log.v(LOGCAT,"onStart pkg:"+pkg);
			if(pkg!=null)
			unInstallApp(pkg);
		}
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	public void installApp(String file) {
		PackageManager pm=getPackageManager();
		 PackageInfo info = pm.getPackageArchiveInfo(file, PackageManager.GET_ACTIVITIES);
		 if(info==null){
	        	Log.e("VV","installApp apk file erro :"+file);
			 return;
		 }
		 int flags=0;
		 try {
	            PackageInfo pi = pm.getPackageInfo(info.packageName, 
	                    PackageManager.GET_UNINSTALLED_PACKAGES);
	            if(pi != null) {
	            	flags |= PackageManager.INSTALL_REPLACE_EXISTING;
	            }
	        } catch (NameNotFoundException e) {
	        }
		PackageInstallObserver iobserver = new PackageInstallObserver();
		PackageManagerUtils.installPackage(pm, file, iobserver,
				flags, "hhhh");
	}

	public void unInstallApp(String pkg) {
		PackageDeleteObserver dobserver = new PackageDeleteObserver();
		PackageManagerUtils.deletePackage(getPackageManager(),
				pkg, dobserver, 0);

	}

    class PackageInstallObserver extends IPackageInstallObserver.Stub {
        public void packageInstalled(String packageName, int returnCode) {
        	Log.v("VV",packageName+" install return :"+returnCode);
//        	Toast.makeText(getApplicationContext(),packageName+" install return :"+returnCode,Toast.LENGTH_LONG).show();
//            Message msg = mHandler.obtainMessage(INSTALL_COMPLETE);
//            msg.arg1 = returnCode;
//            mHandler.sendMessage(msg);
        }
    }
    class PackageDeleteObserver extends IPackageDeleteObserver.Stub {
        public void packageDeleted(boolean succeeded) {
        	Log.v("VV","PackageDeleteObserver return :"+succeeded);
//        	Toast.makeText(getApplicationContext(),"PackageDeleteObserver return :"+succeeded,Toast.LENGTH_LONG).show();
//            Message msg = mHandler.obtainMessage(UNINSTALL_COMPLETE);
//            msg.arg1 = succeeded?SUCCEEDED:FAILED;
//            mHandler.sendMessage(msg);
       }
    }
}
