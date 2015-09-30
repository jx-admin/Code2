package com.android.accenture.aemm.express;

import java.io.File;
import java.util.List;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.util.Log;

public class Utils {
	public static final String HOMEDATA = "/sdcard/data/aemm";
//	public static final String HOMEDATA = "/mnt/sdcard/aemm";
	public static final String ICONPATH = HOMEDATA + "/icons";
	public static final String APKPATH = HOMEDATA + "/apk";
	 /**run cativityInfo.
     * @param activityInfo
     * @return
     */
    public static Intent startApplication(final ActivityInfo activityInfo){
    	if(activityInfo==null){
    		return null;
    	}
      Intent i = new Intent(); 
      ComponentName cn = new ComponentName(activityInfo.packageName, activityInfo.name); 
      i.setComponent(cn); 
      i.setAction("android.intent.action.MAIN"); 
//      startActivityForResult(i, RESULT_OK); 
      return i;
    }
    /**install Application
     * @param apkFile
     * @return
     */
    public static Intent installApplication(String apkFile){
    	Uri uri = Uri.parse(apkFile);        
    	Intent it = new Intent(Intent.ACTION_VIEW, uri);        
    	it.setData(uri);
    	it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);        
    	it.setClassName("com.android.packageinstaller",        
    	               "com.android.packageinstaller.PackageInstallerActivity");        
    	return it;
    	//make sure the url_of_apk_file is readable for all users   
    	/*Uri installUri = Uri.fromParts("package", "", null);
    	returnIt = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);
*/
    }
    /**uninstall application
     * @param activityInfo  activityInfo.packageName
     * @return is the activityInfo is null.
     * true if the param activityInfo isn't null else return false.
     */
    public static Intent uninstallApplication(String packageName){
    	if(packageName==null){
    		return null;
    	}
    	Uri uri = Uri.fromParts("package", packageName, null);         
    	Intent it = new Intent(Intent.ACTION_DELETE, uri);         
    	return it;
    }
    public static boolean installApplicationSilence(Context context,String apkFile){
		Intent i=new Intent("android.service.package.addpackage");
		i.addCategory("android.intent.category.packageservices");
		i.putExtra("apkUri", apkFile);
		context.startService(i);
		return true;
    }
    public static boolean unInstallApplicationSilence(Context context,String packageName){
    	Intent i;
		i=new Intent("android.service.package.deletepackage");
		i.addCategory("android.intent.category.packageservices");
		i.putExtra("package",packageName);
		context.startService(i);
		return true;
    }
    /** get APK info    
     * @param context    
     * @param archiveFilePath APK文件的路径。如：/sdcard/download/XX.apk    
     */     
    public static PackageInfo getUninatllApkInfo(Context context, String archiveFilePath){      
        PackageManager pm = context.getPackageManager();      
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);      
//        if(info != null){      
//            ApplicationInfo appInfo = info.applicationInfo;      
//            String appName = pm.getApplicationLabel(appInfo).toString();      
//            String packageName = appInfo.packageName;      
//            Drawable icon = pm.getApplicationIcon(appInfo);  
//            Log.v("V","apK:"+appName+ " "+packageName+" "+icon.toString());
//        }
        return info;
    }
    public static void checkIntalled(Context context,Appdb app){
    	if(app.getPackageName()==null){
    		return;
    	}
    	PackageManager pm = context.getPackageManager();
	    List<PackageInfo> packs = pm.getInstalledPackages(0);  
	    int i=0;
	    for(;i<packs.size();i++) {  
	        PackageInfo p = packs.get(i);  
	        if ((p.packageName == null)) {  
	            continue ;  
	        }
	        if(app.getPackageName().endsWith(p.packageName)){
	        	app.setFlag(context,Appdb.INSTALLED);
	        	break;
	        }
	    }  
	    if(i>=packs.size()&&app.getFlag()==Appdb.INSTALLED){
	    	app.setFlag(context,Appdb.UNINSTALLED);
	    }
    }
    public static void checkIntalled(Context context,List<Appdb>list){
    	PackageManager pm = context.getPackageManager();
	    List<PackageInfo> packs = pm.getInstalledPackages(0);  
	    for(int i=0;i<packs.size();i++) {  
	        PackageInfo p = packs.get(i);  
	        if ((p.packageName == null)) {  
	            continue ;  
	        }
	        boolean isContinue=false;
	        for(Appdb app:list){
	        	if(app.getFlag()==Appdb.INSTALLED){
	        		continue;
	        	}
	        	if(app.getPackageName()==null){
	        		continue;
	        	}
	        	if(app.getPackageName()==null){
	        		continue;
	        	}
	        	isContinue=true;
	        	if(app.getPackageName().endsWith(p.packageName)){
	        		app.setFlag(context,Appdb.INSTALLED);
	        		break;
	        	}
	        }
	        if(isContinue){
	        	break;
	        }
//	        PInfo newInfo = new PInfo();  
//	        newInfo.appname = p.applicationInfo.loadLabel(pm).toString();  
//	        newInfo.pname = p.packageName;  
//	        newInfo.versionName = p.versionName;  
//	        newInfo.versionCode = p.versionCode;  
//	        newInfo.icon = p.applicationInfo.loadIcon(pm);  
//	        res.add(newInfo);  
	    }  
    }

    static public long getAvailableInternalMemorySize() {      
        File path = Environment.getDataDirectory();      
        StatFs stat = new StatFs(path.getPath());      
        long blockSize = stat.getBlockSize();      
        long availableBlocks = stat.getAvailableBlocks();      
        return availableBlocks * blockSize;      
    }
    public static boolean isScreenOn(Context context){
		PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
}
    public static boolean CheckNetwork(final Context context) {
        boolean flag = false;
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager.getActiveNetworkInfo() != null){
            flag = cwjManager.getActiveNetworkInfo().isAvailable();
        Log.d("p","getSubtype="+ cwjManager.getActiveNetworkInfo().getSubtype());
        }
//        if (!flag) {
//            Builder b = new AlertDialog.Builder(context).setTitle("没有可用的网络").setMessage("请开启GPRS或WIFI网络连接");
//            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    Intent mIntent = new Intent("/");
//                    ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
//                    mIntent.setComponent(comp);
//                    mIntent.setAction("android.intent.action.VIEW");
//                    context.startActivity(mIntent);
//                }
//            }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    dialog.cancel();
//                }
//            }).create();
//            b.show();
//        } 
        return flag;
    }

//    static DevicePolicyManager mDPM;
//    static ActivityManager mAM;
//    static ComponentName mDeviceComponentName;
//    public static  boolean toClock(Context context) {
//		
//		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//		mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		mDeviceComponentName = new ComponentName(context,deviceAdminReceiver.class);
//
//		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceComponentName);
//		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"");
//		context.startActivity(intent);
//		return true;
//		
//	}
//    public static boolean clockScreem(Context context){
//    	mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//		mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		mDeviceComponentName = new ComponentName(context,deviceAdminReceiver.class);
//		if (!mDPM.isAdminActive(mDeviceComponentName) || mAM.isUserAMonkey()) {
//			return false;
//		}
//		mDPM.resetPassword("123123", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
//		mDPM.lockNow();
//		return true;
//    }
//    public static boolean unClockScreem(Context context){
//    	mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//		mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		mDeviceComponentName = new ComponentName(context,deviceAdminReceiver.class);
//		if (!mDPM.isAdminActive(mDeviceComponentName) || mAM.isUserAMonkey()) {
//			return false;
//		}
//		mDPM.resetPassword("", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
//		return true;
//    }
    
}
