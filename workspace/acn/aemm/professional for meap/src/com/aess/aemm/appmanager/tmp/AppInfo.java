package com.aess.aemm.appmanager.tmp;
//package com.aess.aemm.appmanager;
//
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageStats;
//import com.aess.aemm.data.ProfileContent.ApkProfileContent;
//import com.aess.aemm.data.ProfileContent.FunctionProfileContent;
//import com.aess.aemm.view.data.Appdb;
//
//@Deprecated
//public class AppInfo {
////	public String packageId = null;
////	public String apkName   = null;
//	public ApplicationInfo info = null;
//	public PackageInfo pkgInfo  = null;
//	public PackageStats pStats  = null;
//	public boolean pSucceeded   = false;
//
//	// private void writeXml(Context c,XmlSerializer serializer) throws
//	// IllegalArgumentException, IllegalStateException, IOException{
//	// serializer.startTag("","app");
//	// if(packageId!=null){
//	// serializer.attribute("","app id",packageId);
//	// }
//	// if(apkName!=null){
//	// serializer.attribute("","name",this.apkName);
//	// }
//	// if(info!=null&&info.packageName!=null){
//	// serializer.attribute("", "packageName",info.packageName);
//	// }
//	// if(pkgInfo!=null&&pkgInfo.versionName!=null){
//	// serializer.attribute("", "version", pkgInfo.versionName);
//	// }
//	// if(pStats!=null){
//	// serializer.attribute("", "cacheSize",
//	// AppList.getSizeStr(c,pStats.cacheSize).toString());
//	// serializer.attribute("", "codeSize",
//	// AppList.getSizeStr(c,pStats.codeSize).toString());
//	// serializer.attribute("", "dataSize",
//	// AppList.getSizeStr(c,pStats.dataSize).toString());
//	// serializer.attribute("", "TotalSize",
//	// AppList.getSizeStr(c,AppList.getTotalSize(pStats)).toString());
//	// }
//	// serializer.endTag("", "app");
//	// }
//	
//	public String writeXml(Context c) {
//		ApkProfileContent apkContent = null;
//		StringBuilder str = new StringBuilder();
//		if (info != null && info.packageName != null) {
//			str.append("<app ");
//			Context context = c;
//			apkContent = ApkProfileContent.queryApkProfileByPackageFlag(
//					context, info.packageName, Appdb.INSTALLED);
//			if (apkContent == null) {
//
//				String displayName = info.loadLabel(c.getPackageManager())
//						.toString();
//				if (null == displayName) {
//					displayName = info.packageName;
//				}
//				str.append("name=\"");
//				str.append(displayName);
//				str.append("\" ");
//
//				str.append("app-id=\"");
//				str.append(info.packageName);
//				str.append("\" ");
//
//				str.append("version=\"");
//				if (pkgInfo != null) {
//					str.append(pkgInfo.versionName);
//				}
//				str.append("\" ");
//
//				str.append("enterprise=\"0\" ");
//
//				str.append("disabled=\"0\" ");
//
//				FunctionProfileContent fpContent = FunctionProfileContent
//						.queryFuncProfileByPackageVersion(context,
//								info.packageName, "");
//				if (fpContent == null) {
//					str.append("install-time=\"\" ");
//				} else {
//					str.append("install-time=\"");
//					str.append(fpContent.mFunctionInstalledTime);
//					str.append("\" ");
//				}
//
//				if (fpContent == null) {
//					str.append("last-start-time=\"\" ");
//				} else {
//					str.append("last-start-time=\"");
//					str.append(fpContent.mFunctionLastStartTime);
//					str.append("\" ");
//				}
//
//				if (fpContent == null) {
//					str.append("last-exit-time=\"\" ");
//				} else {
//					str.append("last-exit-time=\"");
//					str.append(fpContent.mFunctionLastExitTime);
//					str.append("\" ");
//				}
//			} else {
//				str.append("name=\"");
//				str.append(apkContent.mApkName);
//				str.append("\" ");
//
//				str.append("app-id=\"");
//				str.append(apkContent.mApkId);
//				str.append("\" ");
//
//				str.append("version=\"");
//				str.append(apkContent.mApkVersion);
//				str.append("\" ");
//
//				str.append("enterprise=\"1\" ");
//
//				str.append("disabled=\"");
//				str.append(apkContent.mApkDisabled == 0 ? "0" : "1");
//				str.append("\" ");
//
//				str.append("install-time=\"");
//				str.append(apkContent.mApkInstalledTime == null ? ""
//						: apkContent.mApkInstalledTime);
//				str.append("\" ");
//
//				str.append("last-start-time=\"");
//				str.append(apkContent.mApkLastStartTime == null ? ""
//						: apkContent.mApkLastStartTime);
//				str.append("\" ");
//
//				str.append("last-exit-time=\"");
//				str.append(apkContent.mApkLastExitTime == null ? ""
//						: apkContent.mApkLastExitTime);
//				str.append("\" ");
//			}
//
//			str.append("/>\r\n");
//		}
//		return str.toString();
//	}
//}