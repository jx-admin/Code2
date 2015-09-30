package com.android.wjx.call;

import com.android.log.CLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverProcess extends BroadcastReceiver{
	CLog clog=new CLog(BroadcastReceiverProcess.class.getSimpleName());

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		clog.println(intent.getAction());
		Intent service = new Intent(context, CallService.class);  
		context.startService(service);   //Æô¶¯·þÎñ  

	}

}
