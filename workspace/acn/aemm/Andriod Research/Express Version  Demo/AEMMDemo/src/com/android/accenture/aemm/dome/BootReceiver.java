package com.android.accenture.aemm.dome;

import com.aemm.config_demo.ListenerService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("VV","receive:"+intent.getAction());
		// 接收广播：系统启动完成后运行程序
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//			Intent newIntent = new Intent(context, ApkHall.class);
//			newIntent.setAction("android.intent.action.MAIN");
//			newIntent.addCategory("android.intent.category.LAUNCHER");
//			newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(newIntent);
			
			Intent i = new Intent(context, ListenerService.class); 
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            context.startService(i); 
			
		}else if(intent.getAction().equals(ListenerService.CLOSE_ACTION)){
			 ListenerService.startListen(context);
		}
	}
}
