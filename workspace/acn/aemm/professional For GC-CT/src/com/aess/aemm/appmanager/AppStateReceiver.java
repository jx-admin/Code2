package com.aess.aemm.appmanager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.aess.aemm.apkmag.ApkInfoMag;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.db.FunctionContent;
import com.aess.aemm.db.TrafficContent;
import com.aess.aemm.view.ViewUtils;
import com.aess.aemm.view.data.Appdb;
import com.aess.aemm.view.data.MsgType;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class AppStateReceiver extends BroadcastReceiver {
	final static String TAG = "com.aess.aemm.appmanager.AppStateReceiver";
	public static final String APP_START_ACTION="com.aess.aemm.appmanager.AppStateReceiver.Start";
	public static final String APP_STOP_ACTION="com.aess.aemm.appmanager.AppStateReceiver.Stop";

	public static final String PACKAGENAME="packageName";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        String packageName=null;
        if(APP_START_ACTION.equals(action)){
        	packageName=intent.getStringExtra(PACKAGENAME);

        	ApkContent apkContent = ApkContent.queryContentBy_PKG_FLAG(context, packageName, Appdb.INSTALLED);
        	if(apkContent != null) {
	        	apkContent.mApkLastStartTime = getDataTime();
	        	apkContent.update(context);
        	}
        }else if(APP_STOP_ACTION.equals(action)){
        	packageName=intent.getStringExtra(PACKAGENAME);
        	ApkContent apkContent = ApkContent.queryContentBy_PKG_FLAG(context, packageName, Appdb.INSTALLED);
        	if(apkContent != null) {
        		apkContent.mApkLastExitTime = getDataTime();
        		apkContent.update(context);
        	}
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
        	boolean found = false;
        	packageName = intent.getDataString().substring(8);
        	ArrayList<ApkContent> apkList = ApkContent.queryAllContents(context);
        	if(apkList != null && apkList.size() > 0) {
	        	for(ApkContent apkContent : apkList) {
	        		if(!TextUtils.isEmpty(apkContent.mApkPackageName) && apkContent.mApkPackageName.equals(packageName)) {
	        			if(apkContent.mApkPublished == 1) {
			        		apkContent.mApkFlag = String.valueOf(Appdb.INSTALLED);
			        		apkContent.mApkInstalledTime = getDataTime();
			        		apkContent.update(context);
			        		delteDownloadedFile(context, "" + apkContent.mId);
			        		ApkInfoMag.sendIntentForApkEnt(context);
	        			} else {
	        				apkContent.delete(context);
	        			}
		        		ViewUtils.update(context, MsgType.APP_UPATE_SILLENCE, null);

	        			found = true; 
	        		}
	        	}
        	} 
        	if(!found) {
	        	FunctionContent fpContent = FunctionContent.queryContentBy_PKG_Version(context, packageName, "");
	        	if(fpContent == null) {
	        		fpContent = new FunctionContent();
	        		fpContent.mFunctionId = packageName;
	        		fpContent.mFunctionInstalledTime = getDataTime();
	        		fpContent.add(context);
	        	} else {
	        		fpContent.mFunctionInstalledTime = getDataTime();
	        		fpContent.update(context);
	        	}
        	}
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
			boolean found = false;
			packageName = intent.getDataString().substring(8);
			
			int uid = intent.getIntExtra(Intent.EXTRA_UID, 0);
			TrafficContent.deleteTrafficById(context, uid);
			
        	ArrayList<ApkContent> apkList = ApkContent.queryAllContents(context);
        	if(apkList != null && apkList.size() > 0) {
	        	for(ApkContent apkContent : apkList) {
	        		if(!TextUtils.isEmpty(apkContent.mApkPackageName) && apkContent.mApkPackageName.equals(packageName)) {
		        		if(apkContent.mApkPublished == 1 || apkContent.mApkDisabled == 1) {
			        		apkContent.mApkFlag = "" + Appdb.UNINSTALLED;
			        		apkContent.mApkInstalledTime = null;
			        		apkContent.mApkLastStartTime = null;
			        		apkContent.mApkLastExitTime = null;
			        		apkContent.update(context);
			        		ApkInfoMag.sendIntentForApkEnt(context);
		        		} else {
		        			apkContent.delete(context);
		        		}
		        		ViewUtils.update(context, MsgType.APP_UPATE_SILLENCE, null);
		        		ApkInfoMag.sendIntentForApkEnt(context);
		        		found = true;
	        		}
	        	}
        	}
        	if(!found) {
        		FunctionContent fpContent = FunctionContent.queryContentBy_PKG_Version(context, packageName, "");
	        	if(fpContent != null) {
	        		FunctionContent.deleteFuncContentwithId(context, fpContent.mId);
	        	}
        	}
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)){
			packageName = intent.getDataString().substring(8);
			ApkContent apkContent = ApkContent.queryContentBy_PKG_FLAG(context, packageName, Appdb.INSTALLED);
        	if(apkContent != null) {
        		apkContent.mApkFlag = String.valueOf(Appdb.INSTALLED);
        		apkContent.mApkInstalledTime = getDataTime();
        		apkContent.update(context);
        		ViewUtils.update(context, MsgType.APP_UPATE_SILLENCE, null);
        		delteDownloadedFile(context, "" + apkContent.mId);
        	} else {
        		FunctionContent fpContent = FunctionContent.queryContentBy_PKG_Version(context, packageName, "");
	        	if(fpContent == null) {
	        		fpContent = new FunctionContent();
	        		fpContent.mFunctionId = packageName;
	        		fpContent.mFunctionInstalledTime = getDataTime();
	        		fpContent.add(context);
	        	} else {
	        		fpContent.mFunctionInstalledTime = getDataTime();
	        		fpContent.update(context);
	        	}
        	}
		}
	}

    void delteDownloadedFile(Context context, String path) {
    	File f = context.getExternalFilesDir(null);
	    if(f != null) {
	        path = f.getAbsolutePath() + "/" + path;
	    } else {
	    	path = context.getFilesDir() + "/" + path; 
	    }
		AppInstallUninstall.deleteFile(path);
    }

	private String getDataTime(){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(now);
		return time;
	}
}
