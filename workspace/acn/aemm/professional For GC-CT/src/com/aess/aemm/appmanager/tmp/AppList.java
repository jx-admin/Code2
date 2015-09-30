package com.aess.aemm.appmanager.tmp;
//package com.aess.aemm.appmanager;
//
//import java.util.ArrayList;
//import java.util.List;
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.IPackageStatsObserver;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageStats;
//import android.text.format.Formatter;
//import android.util.Log;
//
//@Deprecated
//public class AppList{
//	public static final boolean debug=false;; 
//	public static final String LOGCAT="V";
//	private PkgSizeObserver mObserver = new PkgSizeObserver();
//	PackageManager mPm ;
//	List <AppInfo> appInfoList=new ArrayList<AppInfo>();
//	int count;
//	Context c;
//	public AppList(Context context){
//		c=context;
//	}
//
//	static long getTotalSize(PackageStats ps) {
//		if (ps != null) {
//			return ps.cacheSize+ps.codeSize+ps.dataSize;
//		}
//		return 0 ;
//	}
//
//	static CharSequence getSizeStr(Context c,long size) {
//		CharSequence appSize = null;
//		if (size == -1) {
//			return "zero size";
//		}
//		appSize = Formatter.formatFileSize(c, size);
//		return appSize;
//	}
//
//	class PkgSizeObserver extends IPackageStatsObserver.Stub {
//		Object mLock = new Object();
//		public void onGetStatsCompleted(PackageStats pStats, boolean pSucceeded) {
//			if(pSucceeded && pStats != null) {
//				for (AppInfo appInfo : appInfoList) {
//					if (appInfo.info.packageName.equals(pStats.packageName)) {
//						appInfo.pStats = pStats;
//						appInfo.pSucceeded = pSucceeded;
//						break;
//					}
//				}
//
//				if(debug){
//					long total = getTotalSize(pStats);
//					CharSequence sizeStr = getSizeStr(c,total);
//					Log.i(LOGCAT, "onGetStatsCompleted:"+pStats.packageName+", ("+
//							pStats.cacheSize+","+
//							pStats.codeSize+", "+pStats.dataSize+" total = " + sizeStr.toString());
//				}
//
//			} else {
//				Log.w(LOGCAT, "Invalid package stats from PackageManager");
//			}
//			count++;
//			synchronized(mLock) {
//				mLock.notify();
//			}
//
//			/* if(count>=appInfoList.size()){
//            	writeListXML("/sdcard/appInfoList.xml",appInfoList);
//            	Log.v(LOGCAT,"----------------------------");
//            	for(AppInfo appInfo:appInfoList){
//            		Log.v(LOGCAT,appInfo.info.packageName+" "+appInfo.pkgInfo.versionName+
//            				" "+appInfo.pSucceeded+" "+appInfo.pStats.cacheSize+","+
//            				appInfo.pStats.codeSize+", "+appInfo.pStats.dataSize+" "+getSizeStr(c,getTotalSize(appInfo.pStats)).toString());
//            	}
//            }*/
//		}
//
//		public void invokeGetSizeInfo(String packageName) {
//			if (packageName == null) {
//				return;
//			}
//			if(debug){
//				Log.i(LOGCAT, "Invoking getPackageSizeInfo for package:"+packageName);
//			}
//			synchronized(mLock) {	
//				mPm.getPackageSizeInfo(packageName, this);
//				try {
//					mLock.wait();
//				} catch (InterruptedException e) {
//				}
//			}
//		}
//	}
//
//	/**
//	 * ��ȡlist
//	 */
//	@SuppressWarnings("static-access")
//	public void  onDemoAppList() 
//	{
//		String pkgName;
//
//		Log.i(LOGCAT, "onDemoAppList");
//
//		mPm = c.getPackageManager();
//		//Log.i(LOGCAT, "get PackageManager.");
//
//		List<ApplicationInfo> installedAppList = mPm.getInstalledApplications(
//				PackageManager.GET_UNINSTALLED_PACKAGES);
//
//		int N = installedAppList.size();
//		Log.i(LOGCAT, "onDemoAppList: App size = " + N);
//
//		//get the package name and app name
//		for (int i = (N-1); i >= 0; i--) {
//			ApplicationInfo ainfo = installedAppList.get(i);
//			if ((ainfo.flags & ainfo.FLAG_SYSTEM) > 0) {
//				continue;
//			}
//			AppInfo appInfo = new AppInfo();
//			appInfo.info = ainfo;
//			pkgName = appInfo.info.packageName;
//			//Log.i(LOGCAT, "AppList: packageName = " + i + pkgName); 
//
//			//CharSequence label = appInfo.info.loadLabel(mPm);
//			//Log.i(LOGCAT, "AppList: label = " + i + label);
//
//			PackageInfo pkgInfo=null;
//			try {
//				pkgInfo = mPm.getPackageInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
//				//Log.i(LOGCAT, "AppList: ver name = " + pkgInfo.versionName);
//			}
//			catch(Exception e) {
//				Log.e(LOGCAT, "AppList: getPackageInfo: " + e);
//			}
//			mObserver.invokeGetSizeInfo(pkgName);
//
//			appInfo.pkgInfo=pkgInfo;
//
//			appInfoList.add(appInfo);
//		}
//
//		//wait to get the size of app
////		while(true)
////		{
////
////			if(count>=appInfoList.size()){
////				writeListXML("/sdcard/appInfoList.xml",appInfoList);
////				Log.v(LOGCAT,"----------------------------");
////				/*for(AppInfo appInfo:appInfoList){
////                		Log.v(LOGCAT,appInfo.info.packageName+" "+appInfo.pkgInfo.versionName+
////                				" "+appInfo.pSucceeded+" "+appInfo.pStats.cacheSize+","+
////                				appInfo.pStats.codeSize+", "+appInfo.pStats.dataSize+" "+getSizeStr(c,getTotalSize(appInfo.pStats)).toString());
////                	}*/
////				Log.v(LOGCAT, String.valueOf(count));
////				break;
////			}
////			try {
////				Log.i("APPLIST", "sleep to wait for observer");
////				Thread.sleep(1000);
////			} catch (InterruptedException e) {
////				break;
////			}
////		}
//
//	}
//
//	/**��ȡ��ҵӦ��
//	 * @param list
//	 * @return
//	 */
////	private List<AppInfo> getList(List<Appdb> list){
////		List <AppInfo> rList=new ArrayList<AppInfo>();
////		AppInfo appInfo;
////		for(Appdb appdb:list){
////			for(int i=0;i<appInfoList.size();i++){
////				appInfo=appInfoList.get(i);
////				if(appdb.getApkPackageName().equals(appInfo.info.packageName)){
////					rList.add(appInfo);
////					appInfo.apkName=appdb.getApkName();
////					appInfo.packageId=appdb.getApkId();
////					appInfoList.remove(i);
////					break;
////				}
////			}
////		}
////		return rList;
////	}
//
////	private void writeListXML(String file,List<AppInfo> list){
////		XmlSerializer serializer = Xml.newSerializer();
////		StringWriter writer = new StringWriter();
////		try{
////			Log.v(LOGCAT,"write xml.");
////			serializer.setOutput(writer);
////
////			serializer.startDocument("UTF-8",true);
////			serializer.startTag("","applist");
////
////			for(AppInfo appInfo:list){
////				appInfo.writeXml(c, serializer);
////
////			}
////			serializer.endTag("","applist");
////			serializer.endDocument();
////		}catch(Exception e){
////			throw new RuntimeException(e);
////		}
////		write(file,writer.toString());
////	}
//
//
////	private void writeListXML(XmlSerializer serializer){
////
////		try{
////			Log.v(LOGCAT,"write xml.");
////
////			//    serializer.startTag("","applist");
////
////			for(AppInfo appInfo:appInfoList){
////				appInfo.writeXml(c, serializer);
////			}
////			//	serializer.endTag("","applist");
////			//	serializer.endDocument();
////		}catch(Exception e){
////			throw new RuntimeException(e);
////		}
////		//write(file,writer.toString());
////	}
//
//	//app id information plus event
//	public String  writeListXML(){
//		StringBuilder xml = new StringBuilder();
//		try{
//			Log.v(LOGCAT,"write Stringxml.");
//			for(AppInfo appInfo:appInfoList){
//				String appitem = appInfo.writeXml(c);
//				xml.append(appitem);
//			}
//			//	serializer.endTag("","applist");
//			//	serializer.endDocument();
//		}catch(Exception e){
//			throw new RuntimeException(e);
//		}
//		return xml.toString();
//	}
//
//
////	private boolean write(String path, String txt) {
////		try {
////			Log.v(LOGCAT,"write file.");
////			OutputStream os = new FileOutputStream(new File(path));//(path, Activity.MODE_PRIVATE);
////			OutputStreamWriter osw = new OutputStreamWriter(os);
////			osw.write(txt);
////			osw.close();
////			os.close();
////		} catch (FileNotFoundException e) {
////			return false;
////		} catch (IOException e) {
////			return false;
////		}
////		return true;
////	}
//}
