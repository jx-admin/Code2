package com.android.abt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Recevier extends BroadcastReceiver{
	public static final String TAG="Recevier";
	@Override
	public void onReceive(Context context, Intent intent) {
		if(Constant.DEBUG)Log.d(TAG,"recever <-"+intent.getAction());		
	}

}
