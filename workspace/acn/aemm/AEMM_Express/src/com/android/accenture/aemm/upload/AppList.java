package com.android.accenture.aemm.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Xml;

import com.android.accenture.aemm.express.AppListenerManager;
import com.android.accenture.aemm.express.Appdb;

/**
 * @author junxu.wang
 * Applist 应用大小是要需要回调的，企业应用可以再全应用里挑选。
 */
public class AppList{
	public static final boolean debug=true;; 
	public static final String LOGCAT="V";
	private PkgSizeObserver mObserver = new PkgSizeObserver();
	PackageManager mPm ;
	List <AppInfo> appInfoList=new ArrayList<AppInfo>();
	int count;
	Context c;
	public AppList(Context context){
		c=context;
	}

	static long getTotalSize(PackageStats ps) {
		if (ps != null) {
			return ps.cacheSize+ps.codeSize+ps.dataSize;
		}
		return 0 ;
	}

	static CharSequence getSizeStr(Context c,long size) {
		CharSequence appSize = null;
		if (size == -1) {
			return "zero size";
		}
		appSize = Formatter.formatFileSize(c, size);
		return appSize;
	}

	class PkgSizeObserver extends IPackageStatsObserver.Stub {
		Object mLock = new Object();
		public void onGetStatsCompleted(PackageStats pStats, boolean pSucceeded) {
			if(pSucceeded && pStats != null) {
				for(AppInfo appInfo:appInfoList){
					if(appInfo.info.packageName.equals(pStats.packageName)){
						appInfo.pStats=pStats;
						appInfo.pSucceeded=pSucceeded;
						break;
					}
				}

				if(debug){
					long total = getTotalSize(pStats);
					CharSequence sizeStr = getSizeStr(c,total);
					Log.i(LOGCAT, "onGetStatsCompleted:"+pStats.packageName+", ("+
							pStats.cacheSize+","+
							pStats.codeSize+", "+pStats.dataSize+" total = " + sizeStr.toString());
				}

			} else {
				Log.w(LOGCAT, "Invalid package stats from PackageManager");
			}
			count++;
			synchronized(mLock) {
				mLock.notify();
			}

			/* if(count>=appInfoList.size()){
            	writeListXML("/sdcard/appInfoList.xml",appInfoList);
            	Log.v(LOGCAT,"----------------------------");
            	for(AppInfo appInfo:appInfoList){
            		Log.v(LOGCAT,appInfo.info.packageName+" "+appInfo.pkgInfo.versionName+
            				" "+appInfo.pSucceeded+" "+appInfo.pStats.cacheSize+","+
            				appInfo.pStats.codeSize+", "+appInfo.pStats.dataSize+" "+getSizeStr(c,getTotalSize(appInfo.pStats)).toString());
            	}
            }*/
		}

		public void invokeGetSizeInfo(String packageName) {
			if (packageName == null) {
				return;
			}
			if(debug){
				Log.i(LOGCAT, "Invoking getPackageSizeInfo for package:"+packageName);
			}
			synchronized(mLock) {	
				mPm.getPackageSizeInfo(packageName, this);
				try {
					mLock.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * 获取list
	 */
	public void  onDemoAppList() 
	{
		String pkgName;

		Log.i(LOGCAT, "onDemoAppList");

		mPm = c.getPackageManager();
		//Log.i(LOGCAT, "get PackageManager.");

		List<ApplicationInfo> installedAppList = mPm.getInstalledApplications(
				PackageManager.GET_UNINSTALLED_PACKAGES);

		int N = installedAppList.size();
		Log.i(LOGCAT, "onDemoAppList: App size = " + N);

		//get the package name and app name
		for (int i = (N-1); i >= 0; i--) {
			AppInfo appInfo = new AppInfo();
			appInfo.info = installedAppList.get(i);
			pkgName = appInfo.info.packageName;
			//Log.i(LOGCAT, "AppList: packageName = " + i + pkgName); 

			CharSequence label = appInfo.info.loadLabel(mPm);
			//Log.i(LOGCAT, "AppList: label = " + i + label);

			PackageInfo pkgInfo=null;
			try {
				pkgInfo = mPm.getPackageInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
				//Log.i(LOGCAT, "AppList: ver name = " + pkgInfo.versionName);
			}
			catch(Exception e) {
				Log.e(LOGCAT, "AppList: getPackageInfo: " + e);
			}
			mObserver.invokeGetSizeInfo(pkgName);

			appInfo.pkgInfo=pkgInfo;

			appInfoList.add(appInfo);
		}

		//wait to get the size of app
		while(true)
		{

			if(count>=appInfoList.size()){
				writeListXML("/sdcard/appInfoList.xml",appInfoList);
				Log.v(LOGCAT,"----------------------------");
				/*for(AppInfo appInfo:appInfoList){
                		Log.v(LOGCAT,appInfo.info.packageName+" "+appInfo.pkgInfo.versionName+
                				" "+appInfo.pSucceeded+" "+appInfo.pStats.cacheSize+","+
                				appInfo.pStats.codeSize+", "+appInfo.pStats.dataSize+" "+getSizeStr(c,getTotalSize(appInfo.pStats)).toString());
                	}*/
				Log.v(LOGCAT, String.valueOf(count));
				break;
			}
			try {
				Log.i("APPLIST", "sleep to wait for observer");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}

	}

	/**获取企业应用
	 * @param list
	 * @return
	 */
	public List<AppInfo> getList(List<Appdb> list){
		List <AppInfo> rList=new ArrayList<AppInfo>();
		AppInfo appInfo;
		for(Appdb appdb:list){
			for(int i=0;i<appInfoList.size();i++){
				appInfo=appInfoList.get(i);
				if(appdb.getPackageName().equals(appInfo.info.packageName)){
					rList.add(appInfo);
					appInfo.apkName=appdb.getApkName();
					appInfo.packageId=appdb.getApkId();
					appInfoList.remove(i);
					break;
				}
			}
		}
		return rList;
	}

	public void writeListXML(String file,List<AppInfo> list){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try{
			Log.v(LOGCAT,"write xml.");
			serializer.setOutput(writer);

			serializer.startDocument("UTF-8",true);
			serializer.startTag("","applist");

			for(AppInfo appInfo:list){
				appInfo.writeXml(c, serializer);

			}
			serializer.endTag("","applist");
			serializer.endDocument();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		write(file,writer.toString());
	}


	public void writeListXML(XmlSerializer serializer){

		try{
			Log.v(LOGCAT,"write xml.");

			//    serializer.startTag("","applist");

			for(AppInfo appInfo:appInfoList){
				appInfo.writeXml(c, serializer);
			}
			//	serializer.endTag("","applist");
			//	serializer.endDocument();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		//write(file,writer.toString());
	}

	//app id information plus event
	public String  writeListXML(){
		StringBuilder xml = new StringBuilder("<applist>\r\n");
		try{
			Log.v(LOGCAT,"write Stringxml.");
			for(AppInfo appInfo:appInfoList){
				String appitem = appInfo.writeXml(c);
				xml.append(appitem);
			}
			//	serializer.endTag("","applist");
			//	serializer.endDocument();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		xml.append("</applist>\r\n");
		return xml.toString();
	}


	public boolean write(String path, String txt) {
		try {
			Log.v(LOGCAT,"write file.");
			OutputStream os = new FileOutputStream(new File(path));//(path, Activity.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(txt);
			osw.close();
			os.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
class AppInfo{
	String packageId;
	String apkName;
	ApplicationInfo info;
	PackageInfo pkgInfo;
	PackageStats pStats;
	boolean pSucceeded;
	public void writeXml(Context c,XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException{
		serializer.startTag("","app");
		if(packageId!=null){
			serializer.attribute("","app id",packageId);
		}
		if(apkName!=null){
			serializer.attribute("","name",this.apkName);
		}
		if(info!=null&&info.packageName!=null){
			serializer.attribute("", "packageName",info.packageName);
		}
		if(pkgInfo!=null&&pkgInfo.versionName!=null){
			serializer.attribute("", "version", pkgInfo.versionName);
		}
		if(pStats!=null){
			serializer.attribute("", "cacheSize", AppList.getSizeStr(c,pStats.cacheSize).toString());
			serializer.attribute("", "codeSize", AppList.getSizeStr(c,pStats.codeSize).toString());
			serializer.attribute("", "dataSize", AppList.getSizeStr(c,pStats.dataSize).toString());
			serializer.attribute("", "TotalSize", AppList.getSizeStr(c,AppList.getTotalSize(pStats)).toString());
		}
		serializer.endTag("", "app");
	}

	public String writeXml(Context c) {
		StringBuilder str = new StringBuilder("<app ");
		//    <app id="com.xxx.xxx1" name="应用1" size="100K" datasize="100K" />
		if(info!=null&&info.packageName!=null){
			str.append("id=\"");
			str.append(info.packageName);
			str.append("\" ");
			//serializer.attribute("","app id",packageId);
		}
		if(apkName!=null){
			str.append("name=\"");
			str.append(this.apkName);
			str.append("\" ");

		}

		if(pStats!=null){

			str.append("size=\"");
			str.append(AppList.getSizeStr(c,AppList.getTotalSize(pStats)).toString());
			str.append("\" ");

			str.append("dataSize=\"");
			str.append(AppList.getSizeStr(c,pStats.dataSize).toString());
			str.append("\" ");
			//serializer.attribute("", "cacheSize", AppList.getSizeStr(c,pStats.cacheSize).toString());
			//serializer.attribute("", "codeSize", AppList.getSizeStr(c,pStats.codeSize).toString());
			//serializer.attribute("", "dataSize", AppList.getSizeStr(c,pStats.dataSize).toString());
			//serializer.attribute("", "TotalSize", AppList.getSizeStr(c,AppList.getTotalSize(pStats)).toString());
		}
		//check if it is has event
		/*
		 * AppListenerManager am = new AppListenerManager();
		
		String events = am.getEvent(info.packageName, "id");
		if (events != null)
			str += events;
		*/
		str.append("/>\r\n");
		return str.toString();

	}
}
