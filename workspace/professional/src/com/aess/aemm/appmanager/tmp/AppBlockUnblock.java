package com.aess.aemm.appmanager.tmp;
//package com.aess.aemm.appmanager;
//
//import com.aess.aemm.commonutils.CommUtils;
//import com.aess.aemm.data.ProfileContent.ApkProfileContent;
//import com.aess.aemm.function.ProfessionalFunction;
//import com.aess.aemm.view.data.Appdb;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.SharedPreferences.Editor;
//import android.os.RemoteException;
//
//public class AppBlockUnblock {
//	public void blockStart(Context context, String appName) {
//		try {
//			new ProfessionalFunction(context).addProhibitedApplication(appName);
//			ApkProfileContent apkContent = ApkProfileContent.queryApkProfileByPackageFlag(context, appName, Appdb.INSTALLED);
//			apkContent.mApkDisabled = 1;
//			apkContent.save(context);
//		} catch (RemoteException e) {
//		}
//	}
//
//	public void unblockStart(Context context, String appName) {
//		try {
//			new ProfessionalFunction(context).deleteProhibitedApplication(appName);
//			ApkProfileContent apkContent = ApkProfileContent.queryApkProfileByPackageFlag(context, appName, Appdb.INSTALLED);
//			apkContent.mApkDisabled = 0;
//			apkContent.save(context);
//		} catch (RemoteException e) {
//		}
//	}
//
//	void blockInstall(Context context, boolean block) {
//		try {
//			new ProfessionalFunction(context).enableAllApkInstalled(true);
//        	Editor editor = context.getSharedPreferences(CommUtils.AEMM_PACKAGE_ID, 0).edit();
//        	editor.putString(CommUtils.KEY_CONFIG_HALL_ENABLED, "true");
//        	editor.commit();
//		} catch (RemoteException e) {
//		}
//	}
//
//	public void parseUpdateResult(Context context,
//			boolean isRemoved,
//			boolean isPublished,
//			boolean isEnabled,
//			ApkProfileContent apkContent) {
//		
//		long rowId = 0;
//		ApkProfileContent apkvalues = ApkProfileContent.queryApkProfile(context, apkContent.mApkId, apkContent.mApkVersion);
//
//		if (apkvalues != null) {
//			
//			rowId = apkvalues.mId;
//			String apkFlag = apkvalues.mApkFlag;
//			if (isRemoved)
//			{
//				//if the app is installed ,client should call silence install module
//				//to uninstall it.
//				if (apkFlag.equals(String.valueOf(Appdb.INSTALLED)))
//				{
//					//notify to uninstall app
//				}
//				//delete this record
//				ApkProfileContent.deleteApkContentwithId(context, rowId);
//			}
//			if (isPublished == false)
//			{
//				//if the app is in not installed,client should delete the record
//				//because there is no such an app on server.
//				if (apkFlag.equals(String.valueOf(Appdb.NEWAPP)) ||
//						apkFlag.equals(String.valueOf(Appdb.UNINSTALLED)))
//				{
//					//delete the record
//					ApkProfileContent.deleteApkContentwithId(context, rowId);
//				}
//			}
//			//update it's status
//			ContentValues values = apkvalues.toContentValues();
//			//values.put(ApkProfileColumns.APK_RUNNING_ENABLED,isEnabled);
//			//values.put(ApkProfileColumns.APK_IS_ONSHELF,isPublished);
//			ApkProfileContent.updateApkContentwithRowId(context, rowId, values);
//			
//		} else {
//			// insert
//			// step 1.save in db
//			apkContent.mApkFlag = String.valueOf(Appdb.NEWAPP);
//			apkContent.save(context);
//		}
//	}
//	
//}
