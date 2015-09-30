package com.aess.aemm.receiver;

import com.aess.aemm.push.PushService;
import com.aess.aemm.update.Update;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	public static final String TAG = "BootReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (Update.startUpdate(context, Update.AUTO) > 0) {
			Log.w(TAG, "start sync update");
			context.startService(new Intent(PushService.START));
		} else {
			Log.w(TAG, "update fail");
		}
	}

}
