package com.android.accenture.aemm.dome;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    /** get APK info    
     * @param context    
     * @param archiveFilePath APK文件的路径。如：/sdcard/download/XX.apk    
     */     
    public static PackageInfo getUninatllApkInfo(Context context, String archiveFilePath){      
        PackageManager pm = context.getPackageManager();      
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);      
        if(info != null){      
            ApplicationInfo appInfo = info.applicationInfo;      
            String appName = pm.getApplicationLabel(appInfo).toString();      
            String packageName = appInfo.packageName;      
            Drawable icon = pm.getApplicationIcon(appInfo);  
            Log.v("V","apK:"+appName+ " "+packageName+" "+icon.toString());
        }    
        return info;
    }
    public static void checkIntalled(Context context,Appdb app){
    	if(app.packageInfo==null
    			||app.packageInfo.packageName==null){
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
	        if(app.packageInfo.packageName.endsWith(p.packageName)){
	        	app.setFlag(Appdb.INSTALLED);
	        	break;
	        }
	    }  
	    if(i>=packs.size()&&app.getFlag()==Appdb.INSTALLED){
	    	app.setFlag(Appdb.UNINSTALLED);
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
	        	if(app.packageInfo==null){
	        		continue;
	        	}
	        	if(app.packageInfo.packageName==null){
	        		continue;
	        	}
	        	isContinue=true;
	        	if(app.packageInfo.packageName.endsWith(p.packageName)){
	        		app.setFlag(Appdb.INSTALLED);
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
    static DevicePolicyManager mDPM;
    static ActivityManager mAM;
    static ComponentName mDeviceComponentName;
    public static  boolean toClock(Context context) {
		if(mDPM==null)mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		if(mAM==null)mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if(mDeviceComponentName==null)mDeviceComponentName = new ComponentName(context,deviceAdminReceiver.class);

		 boolean active = mDPM.isAdminActive(mDeviceComponentName);
		 if(!active){
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceComponentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"");
			if(context instanceof Service){
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		 }
		return true;
	}
    public static boolean clockScreem(Context context){
    	toClock(context);
    	if (!mDPM.isAdminActive(mDeviceComponentName) || ActivityManager.isUserAMonkey()) {
			return false;
		}
    	boolean active = mDPM.isAdminActive(mDeviceComponentName);
		 if(active){
			 mDPM.resetPassword("123456", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
			 mDPM.lockNow();
			 return true;
		 }else{
			 return false;
		 }
    }
    public static boolean unClockScreem(Context context){
    	toClock(context);
    	if (!mDPM.isAdminActive(mDeviceComponentName) || ActivityManager.isUserAMonkey()) {
			return false;
		}
    	boolean active = mDPM.isAdminActive(mDeviceComponentName);
		 if(active){
			 mDPM.resetPassword("", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
			 return true;
		 }else{
			 return false;
		 }
    }
    public static void print(Class c,String info){
    	
    }
    
}
