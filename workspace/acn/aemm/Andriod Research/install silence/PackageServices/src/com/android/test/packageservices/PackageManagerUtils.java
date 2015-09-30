package com.android.test.packageservices;

import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;


public class PackageManagerUtils {
	 public static void installPackage(PackageManager pm,String file, IPackageInstallObserver observer, int flags, String installerPackageName){
		 pm.installPackage(Uri.parse(file), observer, flags,installerPackageName);
	 }
	 public static void deletePackage(PackageManager pm,String packageName, IPackageDeleteObserver observer, int flags){
	      pm.deletePackage(packageName, observer, flags);
	 }
}

