package com.android.ring.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.log.CLog;
import com.android.ring.devutils.IntentDemo;
import com.android.ring.services.RingService;

public class RingReciver extends BroadcastReceiver {
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		CLog.print("RingReciver","onReceive "+action);
		if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
			RingService.start(context, RingService.ACTION_BOOT_COMPLETE);
		}else if("android.intent.action.PHONE_STATE".equals(action)){
			RingService.start(context, RingService.PHONE_STATE);

		}else if(/*Intent.ACTION_NEW_OUTGOING_CALL*/"android.intent.action.NEW_OUTGOING_CALL".equals(action)){
			RingService.start(context, RingService.CALL_OUT);
			
		}
	}
	
}
