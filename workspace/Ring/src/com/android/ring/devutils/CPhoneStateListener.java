package com.android.ring.devutils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.log.CLog;
import com.android.ring.Constant;
import com.android.wx.math.DateFormater;

public class CPhoneStateListener  extends PhoneStateListener{
	
	CLog clog=new CLog(CPhoneStateListener.class.getSimpleName());
	boolean isWorking=false;
	private int callId;
	Context context ;
	TelephonyManager mTelephonyManager;
	private long callStartTime;
	private AudioManagerUtils mAudioManagerUtils;
	private IntentUtils mIntentUtils;
	public List<String>outNumbers=new ArrayList<String>();
	
	public CPhoneStateListener(Context context){
		this.context=context;
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		mAudioManagerUtils=new AudioManagerUtils(context);
		mIntentUtils=new IntentUtils(context);
	}
	
	public boolean isWorking(){
		return isWorking;
	}
	
	public long getStartTime(){
		return callStartTime;
	}
	
	public void start(){
		callId=0;
		isWorking=true;
		callStartTime=System.currentTimeMillis();
		mTelephonyManager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
		outCall();
	}
	
	public void endCall(){
		clog.println("endCall");
		isWorking=false;
		TeleUtils.endCall(context);
	}
	
	public void outCall(){
		if(Constant.callNumbers==null||Constant.callNumbers.length==0){
			return ;
		}
		if(callId<0){
			callId=Constant.callNumbers.length-1;
		}else if(callId>=Constant.callNumbers.length){
			callId=0;
		}  

		mAudioManagerUtils.silent();
		clog.println("call->"+Constant.callNumbers[callId]);
		outNumbers.add(Constant.callNumbers[callId]);
		IntentDemo.call(context,Constant.callNumbers[callId],true);
		callId++;
	}
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		
		switch (state) {
		case TelephonyManager.CALL_STATE_RINGING:   //来电 
//			this.incomeNumber = incomingNumber;
			clog.println(DateFormater.format(System.currentTimeMillis(), DateFormater.defaultFomate)+" 来电（RINGING） "+incomingNumber);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:   //接通电话或拨出电话
			mIntentUtils.start();
			clog.println(DateFormater.format(System.currentTimeMillis(), DateFormater.defaultFomate)+" 接通过话（OFFHOOK） "+incomingNumber);
			break;
			
		case TelephonyManager.CALL_STATE_IDLE:  //挂掉电话或注册
			if(mIntentUtils!=null){
				mIntentUtils.stop();
			}
			if(isWorking){
				outCall();
			}else{
				mAudioManagerUtils.reset();
				mTelephonyManager.listen(this, PhoneStateListener.LISTEN_NONE); 
			}
			clog.println(DateFormater.format(System.currentTimeMillis(), DateFormater.defaultFomate)+" 挂电话（IDLE） "+incomingNumber);
			break;
		default:
			clog.println("default unkown!!!");
			break;
		}
	}
}
