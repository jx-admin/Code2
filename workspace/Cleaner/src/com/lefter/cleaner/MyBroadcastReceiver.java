package com.lefter.cleaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			// 是否选择了开机启动
			boolean start = PreferenceManager.getDefaultSharedPreferences(
					context).getBoolean(
					CleanerActivity.KEY_START_WHEN_BOOT_COMPLETED, true);
			if (start) {
				Intent i = new Intent();
				i.setClass(context, FloatService.class);
				context.startService(i);
			}
		}
	}
}
