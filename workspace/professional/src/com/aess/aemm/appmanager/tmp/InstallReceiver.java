package com.aess.aemm.appmanager.tmp;
//package com.aess.aemm.appmanager;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
////import com.android.accenture.aemm.express.updataservice.ListenerService;
////import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;
//
//public class InstallReceiver extends BroadcastReceiver {
//		public static final String TAG = "InstallReceiver";
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Log.i(TAG, intent.getAction());
//			//Log.i(TAG, intent.getStringExtra("id"))
//			/*
//			if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//				Intent newIntent = new Intent(context, Main.class);
//				newIntent.setAction("android.intent.action.MAIN");
//				newIntent.addCategory("android.intent.category.LAUNCHER");
//				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(newIntent);
//			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
//				String pkg = intent.getDataString().substring(8);
//				Intent newIntent = new Intent();
//				newIntent.setClassName(pkg, pkg + ".MainActivity");
//				newIntent.setAction("android.intent.action.MAIN");
//				newIntent.addCategory("android.intent.category.LAUNCHER");
//				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				context.startActivity(newIntent);
//				if(Main.mHall != null){
//					Message message = new Message();  
//					message.what = Main.INSTALL;
//					message.obj = pkg;
//					Main.mHall.handler.sendMessage(message);
//				}else{
//					installApp(context,pkg);
//				}
//				Log.v(TAG, "receve PACKAGE_ADDED " + pkg + " uri:" + intent.getData().toString() + "\n" + intent.toString());
//			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
//				String pkg = intent.getDataString().substring(8);
//				Log.v(TAG, "remve Main.mhall = " + (Main.mHall! = null));
//				if(Main.mHall != null){
//					Message message = new Message();  
//		            message.what = Main.UNINSTALL;
//		            message.obj = intent.getDataString().substring(8);
//		            Main.mHall.handler.sendMessage(message);
//				} else {
//					PackageManager pm = context.getPackageManager();
//
//					if(pkg! = null){
//						ApkProfileContent apkPfc = ApkProfileContent.queryApkProfileByPackageFlag(context,pkg,AppItem.INSTALLED);
//						Log.v(TAG, "apkPfc = " + (apkPfc! = null));
//						
//						if(apkPfc! = null){
//							apkPfc.mApkFlag = String.valueOf(AppItem.UNINSTALLED);
//							ApkProfileContent.updateApkContentwithRowId(context, apkPfc.mId, apkPfc.toContentValues());
////							apkPfc.save(context);
//						}
//					}
//				}
//				Log.v(TAG, "receve package_removed " + pkg + " uri:" + intent.getData().toString() + "\n" + intent.toString());
//			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)){
//				String pkg = intent.getDataString().substring(8);
//				if(Main.mHall != null){
//					Message message = new Message();  
//		            message.what = Main.REPLACED;
//		            message.obj = intent.getDataString().substring(8);
//		            Main.mHall.handler.sendMessage(message);
//				} else {
//					installApp(context,pkg);
//				}
//	            Log.v(TAG, "receve package_removed " + pkg+" uri:" + intent.getData().toString()+"\n" + intent.toString());
//			} else if (intent.getAction().equals(ListenerService.NEW_APP_PUSH)){
//				Log.v(TAG, "NEW_APP_PUSH");
//				
//				if(Main.mHall != null){
//					Log.v(TAG, "NEW_APP_PUSH hall");
//					Message message = new Message();  
//		            message.what = Main.NEWAPPPUSH;
//		            Main.mHall.handler.sendMessage(message);
//				}
//			}
//			*/
//		}
///*
//		private void installApp(Context context,String pkg){
//			ApkProfileContent apkPfc = ApkProfileContent.queryApkProfileByPackageFlag(context, pkg, AppItem.INSTALLED);
//			if(apkPfc! = null){
//				apkPfc.mApkFlag = "" + AppItem.UNINSTALLED;
//				ApkProfileContent.updateApkContentwithRowId(context, apkPfc.mId, apkPfc.toContentValues());
//			}
//			
//			String version = null;
//			if(apkPfc == null||apkPfc.mApkVersion == null){
//				PackageManager pm=context.getPackageManager();
//				 try {
//					version = pm.getPackageInfo(pkg, 0).versionName;
//				} catch (NameNotFoundException e) {
//					e.printStackTrace();
//				}
//			} else {
//				version = apkPfc.mApkVersionClient;
//			}
//			if(apkPfc != null && version != null){
//				apkPfc=ApkProfileContent.queryApkProfileByPackageCVersion(context, pkg, version);
//				apkPfc.mApkFlag=String.valueOf(AppItem.INSTALLED);
//				ApkProfileContent.updateApkContentwithRowId(context, apkPfc.mId, apkPfc.toContentValues());
//			}
//		}
//	}
//	*/
//}
