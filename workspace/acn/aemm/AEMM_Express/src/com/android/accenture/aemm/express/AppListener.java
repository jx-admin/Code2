package com.android.accenture.aemm.express;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class AppListener {
//	public static final String APP_CREATE_ACTION="APP_CREATE_ACTION";
//	public static final String APP_DESTROY_ACTION="APP_DESTROY_ACTION";
	public static final String APP_START_ACTION="APP_START_ACTION";
	public static final String APP_STOP_ACTION="APP_CLOSE_ACTION";
//	public static final String APP_PAUSE_ACTION="APP_PAUSE_ACTION";
//	public static final String APP_RESUME_ACTION="APP_RESUME_ACTION";
	
	public static final String PACKAGENAME="packageName";
	public static final String TIMEMILLIS="timeMillis";
	
    public static void start(Context context){
    	Intent message=new Intent(APP_START_ACTION);
        String pn=context.getApplicationInfo().packageName;
        long time=System.currentTimeMillis();
		message.putExtra(PACKAGENAME,pn);
		message.putExtra(TIMEMILLIS, time);
		context.sendBroadcast(message);
    }
    
    public static void stop(Context context){
    	Intent message=new Intent(APP_STOP_ACTION);
        String pn=context.getApplicationInfo().packageName;
        long time=System.currentTimeMillis();
		message.putExtra(PACKAGENAME,pn);
		message.putExtra(TIMEMILLIS, time);
		context.sendBroadcast(message);
    }
    
    public void receiver(Context context){
		final IntentFilter filter = new IntentFilter();
//		filter.addAction(APP_CREATE_ACTION);
//		filter.addAction(APP_DESTROY_ACTION);
		filter.addAction(APP_START_ACTION);
		filter.addAction(APP_STOP_ACTION);
//		filter.addAction(APP_PAUSE_ACTION);
//		filter.addAction(APP_RESUME_ACTION);
        context.registerReceiver(mReceiver, filter);
	}
    
    public void unReceiver(Context context){
    	context.unregisterReceiver(mReceiver);
    }
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            String packageName=null;
        	long time=0;
//            if (APP_CREATE_ACTION.equals(action)) {
//            	packageName=intent.getStringExtra(PACKAGENAME);
//            	time=intent.getLongExtra(TIMEMILLIS, 0);
//            }else if(APP_DESTROY_ACTION.equals(action)){
//            	packageName=intent.getStringExtra(PACKAGENAME);
//            	time=intent.getLongExtra(TIMEMILLIS, 0);
//            }else
            if(APP_START_ACTION.equals(action)){
            	packageName=intent.getStringExtra(PACKAGENAME);
            	time=intent.getLongExtra(TIMEMILLIS, 0);
            }else if(APP_STOP_ACTION.equals(action)){
            	packageName=intent.getStringExtra(PACKAGENAME);
            	time=intent.getLongExtra(TIMEMILLIS, 0);
            }
//            else if(APP_PAUSE_ACTION.equals(action)){
//            	packageName=intent.getStringExtra(PACKAGENAME);
//            	time=intent.getLongExtra(TIMEMILLIS, 0);
//            }else if(APP_RESUME_ACTION.equals(action)){
//            	packageName=intent.getStringExtra(PACKAGENAME);
//            	time=intent.getLongExtra(TIMEMILLIS, 0);
//            }s
            Log.i("VV","AppListener: "+action+" "+packageName+" "+time);
        }
    };
}
