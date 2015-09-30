package com.android.ring.devutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

public class OutgoingCallBroadcaster extends Activity {    
//	private static final String PERMISSION = android.Manifest.permission.PROCESS_OUTGOING_CALLS;    
//	private static final String TAG = "OutgoingCallBroadcaster";    
//	private static final boolean LOGV = true;
//	//Config.LOGV;    
//	public static final String EXTRA_ALREADY_CALLED = "android.phone.extra.ALREADY_CALLED";    
//	public static final String EXTRA_ORIGINAL_URI = "android.phone.extra.ORIGINAL_URI";    
////	private Phone mPhone;
//	@Override    
//	protected void onCreate(Bundle icicle) {        
//		super.onCreate(icicle);        
////		mPhone = PhoneApp.getInstance().phone;        
//		Intent intent = getIntent();        
//		if (LOGV) Log.v(TAG, "onCreate: Got intent " + intent + ".");        
//		String action = intent.getAction();
//		String number = PhoneNumberUtils.getNumberFromIntent(intent, this);
//		if (number != null) {
//			number = PhoneNumberUtils.stripSeparators(number);
//			}
//		final boolean emergencyNumber =                
//				(number != null) && PhoneNumberUtils.isEmergencyNumber(number);
//		boolean callNow;        
//		if (getClass().getName().equals(intent.getComponent().getClassName())) {
//			// If we were launched directly from the OutgoingCallBroadcaster,            
//			// not one of its more privileged aliases, then make sure that            
//			// only the non-privileged actions are allowed.            
//			if (!Intent.ACTION_CALL.equals(intent.getAction())) {
//				Log.w(TAG, "Attempt to deliver non-CALL action; forcing to CALL");
//				intent.setAction(Intent.ACTION_CALL);
//				} 
//			}
//		/* Change CALL_PRIVILEGED into CALL or CALL_EMERGENCY as needed. */ 
//		if (Intent.ACTION_CALL_PRIVILEGED.equals(action)) {
//			action = emergencyNumber
//					? Intent.ACTION_CALL_EMERGENCY 
//							: Intent.ACTION_CALL;
//			intent.setAction(action);
//			}        
//		if (Intent.ACTION_CALL.equals(action)) {
//			if (emergencyNumber) {
//				finish();
//				return;
//				}
//			callNow = false;
//			} else if (Intent.ACTION_CALL_EMERGENCY.equals(action)) {
//				if (!emergencyNumber) {
//					finish(); 
//					return;
//					} 
//				callNow = true;
//				} else {
//					finish();
//					return;
//					}        
//			// Make sure the screen is turned on.  This is probably the right        
//			// thing to do, and more importantly it works around an issue in the        
//			// activity manager where we will not launch activities consistently        
//			// when the screen is off (since it is trying to keep them paused       
//			// and has...  issues).        
//		//        
//		// Also, this ensures the device stays awake while doing the following       
//			// broadcast; technically we should be holding a wake lock here        
//			// as well.        PhoneApp.getInstance().wakeUpScreen();               
//			/* If number is null, we're probably trying to call a non-existent voicemail number or
//			 * 
//			 * something else fishy.  Whatever the problem, there's no number, so there's no point         
//			 * in allowing apps to modify the number. */        
//			if (number == null || TextUtils.isEmpty(number)) callNow = true;
//			if (callNow) {           
//				intent.setClass(this, InCallScreen.class);
//				startActivity(intent);
//				}        
//			Intent broadcastIntent = new Intent(Intent.ACTION_NEW_OUTGOING_CALL);        
//			if (number != null) broadcastIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, number);        
//			broadcastIntent.putExtra(EXTRA_ALREADY_CALLED, callNow);        
//			broadcastIntent.putExtra(EXTRA_ORIGINAL_URI, intent.getData().toString());
//			broadcastIntent.putExtra(EXTRA_INTENT_FROM_BT_HANDSFREE, 
//					intent.getBooleanExtra(OutgoingCallBroadcaster.EXTRA_INTENT_FROM_BT_HANDSFREE, false));
//			if (LOGV) Log.v(TAG, "Broadcasting intent " + broadcastIntent + ".");
//			sendOrderedBroadcast(broadcastIntent, PERMISSION, null, null,
//					Activity.RESULT_OK, number, null); 
//			finish();    
//			}
//	}
//
//		}
	}
