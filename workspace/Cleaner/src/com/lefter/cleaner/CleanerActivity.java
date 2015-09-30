package com.lefter.cleaner;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * 设置页面
 */
public class CleanerActivity extends PreferenceActivity {
	// 用于取得PreferenceActivity存放在sharedpreference中的值
	public static String KEY_START_WHEN_BOOT_COMPLETED = "start_when_boot_completed";
	public static String KEY_CLEAR_CACHE = "clear_cache";
	public static String KEY_KILL_BGPROCESS = "kill_bgprocess";
	public static String KEY_DOUBLE_CLICK_CLOSE = "double_click_close";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		Intent i = new Intent();
		// 启动service创建悬浮窗口
		i.setClass(this, FloatService.class);
		startService(i);

		// 获取android当前可用内存大小

		/*
		 * ActivityManager am = (ActivityManager) this
		 * .getSystemService(Context.ACTIVITY_SERVICE); MemoryInfo mi = new
		 * MemoryInfo(); am.getMemoryInfo(mi); // mi.availMem; 当前系统的可用内存
		 * Log.e("tag", "getMemoryInfo: " + mi.availMem);
		 */
	}
}