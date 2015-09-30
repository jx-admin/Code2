package com.aess.aemm.installservice;

import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;

public class PackageManagerUtils {
	 public static void installPackage(PackageManager pm,String file, IPackageInstallObserver observer, int flags, String installerPackageName){
		 try {
			 pm.installPackage(Uri.parse(file), observer, flags,installerPackageName);
		 } catch(Exception e) {
			 e.printStackTrace(); 
		 }
	 }
	 public static void deletePackage(PackageManager pm,String packageName, IPackageDeleteObserver observer, int flags){
		  try {
			  pm.deletePackage(packageName, observer, flags);
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
	 }
}

