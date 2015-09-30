package com.android.ring.devutils;

import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.android.log.CLog;

/**
 * @author junxu.wang
 * 
 * smsManager.sendTextMessage(destinationAddress, scAddress, text, sentIntent, deliveryIntent)
 * 
 * -- destinationAddress：目标电话号码 
 * -- scAddress：短信中心号码，测试可以不填 
 * -- text: 短信内容
 * -- sentIntent：发送 -->中国移动 --> 中国移动发送失败 --> 返回发送成功或失败信号 --> 后续处理   即，这个意图包装了短信发送状态的信息
 * -- deliveryIntent： 发送 -->中国移动 --> 中国移动发送成功 --> 返回对方是否收到这个信息 --> 后续处理  即：这个意图包装了短信是否被对方收到的状态信息（供应商已经发送成功，但是对方没有收到）。
 *
 */
public class SmsUtils {
	private static final String TAG=SmsUtils.class.getSimpleName();
	private CLog cLog=new CLog(TAG);
	private Context context;
	
	String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	Intent sentIntent;
	PendingIntent sentPI;
	
	String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
	// create the deilverIntent parameter
	Intent deliverIntent;
	PendingIntent deliverPI;
	BroadcastReceiver delivered_sms_broadcastReceiver;
	BroadcastReceiver sent_sms_BroadcastReceiver;
	
	
	public SmsUtils(Context context){
		cLog.println("onCreate");
		this.context=context;
		
		
		sentIntent= new Intent(SENT_SMS_ACTION);
		sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,
				0);
		
		deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		deliverPI= PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
		delivered_sms_broadcastReceiver=new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent _intent) {
				Toast.makeText(context, "收信人已经成功接收", Toast.LENGTH_SHORT) .show();
			}
		};
		
		context.registerReceiver(delivered_sms_broadcastReceiver, new IntentFilter(DELIVERED_SMS_ACTION));
		
		sent_sms_BroadcastReceiver=new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				switch (getResultCode()) {
//		        case Activity.RESULT_OK:
//		        	Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT) .show();
//		        break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					break;
				}
			}
		};
		// register the Broadcast Receivers
		context.registerReceiver(sent_sms_BroadcastReceiver, new IntentFilter(SENT_SMS_ACTION));
	}
	public static void sendSms(String phone_number,String sms_content){
		//直接调用短信接口发短信
		SmsManager smsManager = SmsManager.getDefault();
		List<String> divideContents = smsManager.divideMessage(sms_content);  
		for (String text : divideContents) {
				CLog.print(TAG,"sendSms "+phone_number+":"+text);
//				smsManager.sendTextMessage(phone_number, null, text, sentPI, deliverPI); 
		}
	}
	public void sendSms(String []phone_number,String sms_content){
		//直接调用短信接口发短信
		SmsManager smsManager = SmsManager.getDefault();
		List<String> divideContents = smsManager.divideMessage(sms_content);  
		for (String text : divideContents) {
			for(String number:phone_number){
				CLog.print(TAG,"sendSms "+number+":"+text);
				smsManager.sendTextMessage(number, null, text, sentPI, deliverPI); 
			}
		}
	}
	
	public void setSms(){
		Uri uri = Uri.parse("smsto:10010");          
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);          
		it.putExtra("sms_body", "102");          
		context.startActivity(it);
	}	
	public void onDestroy(){
		cLog.println("onDestroy");
		context.unregisterReceiver(delivered_sms_broadcastReceiver);
		context.unregisterReceiver(sent_sms_BroadcastReceiver);
	}
	
}
