package com.aess.aemm.receiver;

import com.aess.aemm.commonutils.CommUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AemmPhoneStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context cxt, Intent intent) {
		if (null != intent) {
			if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
				CommUtils.setPhoneReset(cxt, 1);
			}
		}
	}

}
