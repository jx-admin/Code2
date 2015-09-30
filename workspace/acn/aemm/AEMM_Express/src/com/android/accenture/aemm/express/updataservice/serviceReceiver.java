package com.android.accenture.aemm.express.updataservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class serviceReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		// ----get the app push command---
		String action = intent.getAction();
		Log.i(Util.TAG, Util.TAG + ": " + "action:" + action);

		try {
			if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
				Log.i(Util.TAG, Util.TAG + ": ACTION_BOOT_COMPLETED enter");
				// if boot complete,start service
				ListenerService.start(context);

			} else if (action.equals("timely_update")) {
				Log.i(Util.TAG, Util.TAG + ": TTL timeout");
				// auto update
				ListenerService.lauchCheckUpdate(context,
						ServiceMessage.autoOrManual.AUTOUPDATE);
			} else if (action.equals(ListenerService.CLOSE_ACTION)) {
				ListenerService.startListen(context);
			}
		} catch (Exception e) {
			Log.d(Util.TAG, "serviceReceiver error on atciton =" + action);
			e.printStackTrace();
		}
	}

}
