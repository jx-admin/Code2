package com.aess.aemm.commonutils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;

public class HelpUtils {
	public static final String TAG = "HelpUtils";
	
	public final static String PSACTIVE  = "com.android.settings";
	public final static String AEMMACTIVE = "com.aess.aemm";
	
	public static String getTopActivityPackageName(Context cxt) {
		ActivityManager am = (ActivityManager)cxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> infolist = am.getRunningTasks(1);
		if (null != infolist && infolist.size() > 0) {

			RunningTaskInfo info = infolist.get(0);
			//android.os.Process.killProcess(android.os.Process.myPid());
			String topname = info.topActivity.getPackageName();
			Log.i(TAG, topname);
			return topname;
		}
		return null;
	}
	
	public static int saveSession(Context cxt, String session) {
		if (null != session && session.length() > 1) {
			String old = CommUtils.getSessionId(cxt);
			if (!session.equals(old)) {
				CommUtils.setSessionId(cxt, session);
				return 1;
			}
		}
		return 0;
	}
	
    public static boolean isRooted() {
	      try{
            Process process = Runtime.getRuntime().exec("su");
            process.destroy();
            return true;
	      } catch (Exception e) {
	      }
	      return false;
  }  
}
