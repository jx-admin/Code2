package com.aess.aemm.view.sso;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SSO {
	public static final String FriendApk = "com.ccssoft";
	public static final String FriendActivity = "com.ccssoft.framework.Launcher";
	public static final String ACTION = "com.ccssoft.subsystem.KEY_LOGIN";
	public static final String FRIEND = "com.ccssoft";
	
	public static boolean isSSO(String packageName) {
		if (FriendApk.equals(packageName)) {
			return true;
		}
		return false;
	}
	
	public static void startActivity(Context cxt) {
//		String action = "com.ccssoft.subsystem.KEY_LOGIN";
		Uri uri = Uri.parse("query://"); 
		Intent intent = new Intent(ACTION, uri);
		intent.putExtra("workercode", "gzadmin");
		try {
			cxt.startActivity(intent);
		} catch(ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void startActivity(Context cxt, String user) {
		Uri uri = Uri.parse("query://"); 
		Intent intent = new Intent(ACTION, uri);
		intent.putExtra("workercode", user);
		try {
			cxt.startActivity(intent);
		} catch(ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
}
