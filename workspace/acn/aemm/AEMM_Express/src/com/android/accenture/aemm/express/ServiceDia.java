package com.android.accenture.aemm.express;



import com.android.accenture.aemm.express.updataservice.DeviceAdminLocalSetup;
import com.android.accenture.aemm.express.updataservice.configPreference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.AlertDialog.Builder;
import android.app.KeyguardManager.KeyguardLock;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ServiceDia extends Activity{
	
	KeyguardLock kl;
	PowerManager.WakeLock wl; //auto free£¬ didn't free by code
	public final static String COMMAND="command";
	public final static	String MESSAGE="message";
	public final static byte UNKOWN=-1;
	public final static byte WIFIOK=0;
	public final static byte ADDAPP=1;
	public final static byte SETPASSWORD=2;
	public final static String LOGCAT="SERVICDDIA";
	
	public final int SetPasswordFlag = 1;
	 
	 public static void showWifiOk(Context context){
		 Intent diaIntent=(new Intent(context,ServiceDia.class));
		 diaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 diaIntent.putExtra(COMMAND, WIFIOK);
		 context.startActivity(diaIntent);
	 }
	 public static void showAddApp(Context context,String name){
		 Log.d(LOGCAT,"showAddApp dialog");
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
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED); 
			setContentView(R.layout.room);
			wakeUp();
			brightnessMax();
			Intent i=getIntent();
			byte com=i.getByteExtra(COMMAND,UNKOWN);
			if(com==WIFIOK){
				  AlertDialog.Builder builder = new Builder(this);
				  
				  builder.setMessage("");

				  builder.setTitle(R.string.alert);

				  builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

				   @Override
				   public void onClick(DialogInterface dialog, int which) {
					   if(kl!=null)
					   kl.reenableKeyguard();
//                       //auto free£¬ didn't free by code
//					   if(wl!=null)
//					   wl.release();
				    dialog.dismiss();
				    ServiceDia.this.finish();
				   }
				  });
				  builder.create().show();
			}else if(com==ADDAPP){
				final CustomDialog cd=new CustomDialog(this);
		        cd.setCancelable(false);
		        cd.show();
		        cd.setMessage(i.getStringExtra(MESSAGE));
		        cd.addPositiveButton(R.string.open, new View.OnClickListener() {

					   @Override
					   public void onClick(View v) {
						   Main.startHall(ServiceDia.this);
						   if(kl!=null)
							   kl.reenableKeyguard();
//						       //auto free£¬ didn't free by code
//							   if(wl!=null)
//							   wl.release();
					    cd.dismiss();
					    ServiceDia.this.finish();
					   }
					  });
		        cd.addNegativeButton(R.string.ignore, new View.OnClickListener() {
					   @Override
					   public void onClick(View v) {
						   if(kl!=null)
							   kl.reenableKeyguard();
//						       //auto free£¬ didn't free by code
//							   if(wl!=null)
//							   wl.release();
							   cd.dismiss();
						    ServiceDia.this.finish();
					   }
				});
			} else if (SETPASSWORD == com) {
//				Intent intent = new Intent(
//						DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
//				startActivityForResult(intent, Main.SetPasswordFlag);
				configPreference.setPSInput(this, true);
	        	Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD); 
	        	startActivityForResult(intent, SetPasswordFlag);
			}
	}
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SetPasswordFlag) {
			DevicePolicyManager dpm = (DevicePolicyManager)this
			.getSystemService(Context.DEVICE_POLICY_SERVICE);
			ComponentName mDeviceComponentName = new ComponentName(this,
		                deviceAdminReceiver.class);

			boolean active=dpm.isAdminActive(mDeviceComponentName);
			boolean result =false;
			if(active){
				result = dpm.isActivePasswordSufficient();
			}else{
				 Intent intent = new Intent(
		                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
		                    mDeviceComponentName);
		            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
		                    R.string.aemm_device_admin_description);
		            startActivityForResult(intent, SetPasswordFlag);
			}
			
			if ((result == false )) {
				Intent intent = new Intent(
						DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
				startActivityForResult(intent, SetPasswordFlag);
			} else {
				configPreference.setPSInput(this, false);
				this.finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void wakeUp(){
	        PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
	        wl=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager. SCREEN_BRIGHT_WAKE_LOCK, "WakeLock");
	        Log.v("VV","isHeld:"+wl.isHeld());
	        Log.v("VV","isScreenOn£º"+pm.isScreenOn());
	        if(!pm.isScreenOn()){
	        	wl.acquire(1000*10);
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
	 public static  void test(final Context c){
		 new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				showAddApp(c, (String) c.getText(R.string.new_app_load));
			}
			 
		 }, 4000);
	 }

//	TimerTask closeLight = new TimerTask() {
//		@Override
//		public synchronized void run() {
//			if (wl != null)
//				wl.release();
//		}
//	};
}
