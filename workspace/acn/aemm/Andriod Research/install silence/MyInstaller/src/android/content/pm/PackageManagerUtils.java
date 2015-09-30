package android.content.pm;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AndroidException;


public class PackageManagerUtils {
	 public static void installPackage(PackageManager pm,Uri packageURI, IPackageInstallObserver observer, int flags, String installerPackageName){
		 pm.installPackage(packageURI, observer, flags,installerPackageName);
	 }
	 public static void deletePackage(PackageManager pm,String packageName, IPackageDeleteObserver observer, int flags){
	      pm.deletePackage(packageName, observer, flags);
	 }
}

