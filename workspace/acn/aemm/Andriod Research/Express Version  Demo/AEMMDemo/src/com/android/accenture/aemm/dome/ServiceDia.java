package com.android.accenture.aemm.dome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.AlertDialog.Builder;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ServiceDia extends Activity{
	
	KeyguardLock kl;
	PowerManager.WakeLock wl;
	public final static String COMMAND="command";
	public final static	String MESSAGE="message";
	public final static byte UNKOWN=-1;
	public final static byte WIFIOK=0;
	public final static byte ADDAPP=1;
	 
	 public static void showWifiOk(Context context){
		 Intent diaIntent=(new Intent(context,ServiceDia.class));
		 diaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 diaIntent.putExtra(COMMAND, WIFIOK);
		 context.startActivity(diaIntent);
	 }
	 public static void showAddApp(Context context,String name){
		 Intent diaIntent;
		 diaIntent=(new Intent(context,ServiceDia.class));
		 diaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 diaIntent.putExtra(COMMAND,ADDAPP);
		  Bundle _Bundle = new Bundle();  
          _Bundle.putString(MESSAGE,name);   
          diaIntent.putExtras(_Bundle);                                    
		 context.startActivity(diaIntent);
	 }
	 private void brightnessMax() {    
	     WindowManager.LayoutParams lp = getWindow().getAttributes();    
	     lp.screenBrightness = 1.0f;    
	     getWindow().setAttributes(lp);    
	}   

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.room);
			wakeUp();
			brightnessMax();
			Intent i=getIntent();
			byte com=i.getByteExtra(COMMAND,UNKOWN);
			if(com==0){
				  AlertDialog.Builder builder = new Builder(this);
				builder.setOnKeyListener(keyListener);//.setOnCancelListener(cancelListener);
				  
				  builder.setMessage(R.string.wifi_ok);

				  builder.setTitle("提示");

				  builder.setPositiveButton("确定", new OnClickListener() {

				   @Override
				   public void onClick(DialogInterface dialog, int which) {
					   if(kl!=null)
					   kl.reenableKeyguard();
					   if(wl!=null)
					   wl.release();
					   ServiceDia.this.finish();
				    dialog.dismiss();
				   }
				  });
				  builder.create().show();
			}else if(com==1){
				  AlertDialog.Builder builder = new Builder(this);
				  builder.setOnKeyListener(keyListener);
				  builder.setMessage("您收到了新的应用："+i.getStringExtra(MESSAGE));

				  builder.setTitle("提示");

				  builder.setPositiveButton("打开", new OnClickListener() {

				   @Override
				   public void onClick(DialogInterface dialog, int which) {
					   ApkHall.startHall(ServiceDia.this);
					   if(kl!=null)
						   kl.reenableKeyguard();
						   if(wl!=null)
						   wl.release();
						   ServiceDia.this.finish();
				    dialog.dismiss();
				   }
				  });

				  builder.setNegativeButton("忽略", new OnClickListener() {

				   @Override
				   public void onClick(DialogInterface dialog, int which) {
					   if(kl!=null)
						   kl.reenableKeyguard();
						   if(wl!=null)
						   wl.release();
					   dialog.dismiss();
					    ServiceDia.this.finish();
				   }
				  });
				  builder.create().show();
			}
			
	}
	 public void wakeUp(){
	        PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
	        wl=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager. SCREEN_BRIGHT_WAKE_LOCK, "WakeLock");
	        Log.v("VV","isHeld:"+wl.isHeld());
	        Log.v("VV","isScreenOn："+pm.isScreenOn());
	        if(!pm.isScreenOn()){
	        	wl.acquire();
	        }else{
	        	wl=null;
	        }
//	        wl.release();
	        KeyguardManager km=(KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	        Log.v("VV","inKeyguardRestrictedInputMode:"+km.inKeyguardRestrictedInputMode());
	        if(km.inKeyguardRestrictedInputMode()){
	        	kl=km.newKeyguardLock("KeyguardLock");
	        	kl.disableKeyguard();
	        }else{
	        	kl=null;
	        }
//	        kl.reenableKeyguard();
	    }


		OnKeyListener keyListener=new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_BACK)	{
					ServiceDia.this.finish();
				}
				return false;
			}
		};
}
