package com.android.wjx.call;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.android.log.CLog;

public class IntentUtils {
	private static final String DES_PKG="com.android.phone";
	Context context;
	ComponentName cn;
	Timer callTimer;
	
	public IntentUtils(Context context){
		this.context=context;
	}
	
	public static void goHome(Context context){
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
         mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                         | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
         context.startActivity(mHomeIntent);
	}
	
	public static ComponentName getActivity(Context context){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;		
		CLog.print("ComponentName", cn.getPackageName()+"---"+cn.getClassName());
		return cn;
	}
	
	public static void sartActivity(Context context,ComponentName cn){
		CLog.print("sartActivity", cn.getPackageName()+"---"+cn.getClassName());
		Intent i=new Intent();
		i.setComponent(cn);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(i);
	}
	
	public void start(){
		stop();
		callTimer = new Timer();
		callTimer.schedule(new TimerTask() {
			@Override  
			public void run() {
				ComponentName  newCn=IntentUtils.getActivity(context);
				if(DES_PKG.equals(newCn.getPackageName())
//						"com.android.phone.InCallScreen".equals(newCn.getClassName())
						//||newCn.getClassName().equals("com.android.contacts.activities.DialtactsActivity")
						){
					if(cn==null){
						IntentUtils.goHome(context);
					}else{
						IntentUtils.sartActivity(context, cn);
					}
				}else{
					cn=newCn;
				}
			}
		},0,1000);  
	}
	
	public void stop(){
		if(callTimer!=null){
			callTimer.cancel();
			callTimer=null;
		}
	}

}
