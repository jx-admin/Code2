package org.accenture.product.lemonade;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LockService extends Service
{

	LockReceiver lockReceiver;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		if (lockReceiver != null) {
			unregisterReceiver(lockReceiver);
		}else{
			lockReceiver=new LockReceiver();
		}
		
		//注册锁屏接收器
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
	    registerReceiver(lockReceiver, intentFilter);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (lockReceiver != null) {
			unregisterReceiver(lockReceiver);
			lockReceiver=null;
		}
		
	}
	
}
