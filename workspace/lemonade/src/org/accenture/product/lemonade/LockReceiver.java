package org.accenture.product.lemonade;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockReceiver extends BroadcastReceiver
{

	KeyguardManager keyguardManager;
	KeyguardLock keyguardLock; 
	
	@Override
	public void onReceive(Context context, Intent intent)
	{		
		if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
						
			if (keyguardManager == null) {
	        	keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			}
			if (keyguardLock == null) {
				keyguardLock = keyguardManager.newKeyguardLock("MOGXCross");
				
			}
			
			keyguardLock.disableKeyguard();
			
			
			Intent lockActivity=new Intent(context,LockActivity.class); 
			lockActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(lockActivity);
		}
		else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
			if (keyguardManager == null) {
	        	keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			}
						
			if (keyguardLock == null) {
				keyguardLock = keyguardManager.newKeyguardLock("MOGXCross");
			}
			
			keyguardLock.reenableKeyguard();
		}
		
	}
	
	
}