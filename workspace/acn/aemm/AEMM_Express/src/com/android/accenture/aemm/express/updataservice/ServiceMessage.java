package com.android.accenture.aemm.express.updataservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.accenture.aemm.express.R;
import com.android.accenture.aemm.express.updataservice.configParser.ProfileType;


public class ServiceMessage {
	
	public enum messagType{
		APPMSG,
		STATUSMSG,
		RESULTMSG,
		LOGINMSG
	}
	
	public enum autoOrManual{
		AUTOUPDATE,
		MANUALUPDATE,
		LOGINUPDATE
	}
	public static final String Key_MsgType = "msgType";
	
	public static final String Key_MaxTime = "maxTime";
	public static final String Key_DelayTime = "delayTime";
	public static final String Key_MsgInfo= "msgInfo";
	
	public static final String Key_AppIds = "appIds";
	
//	public static final String statusCheckUpdateMsg = "正在检查更新";
//	public static final String statusFinishUpdateMsg = "Update done";
//	public static final String statusErrorUpdateMsg = "Update error";
	
//	public static final String statusResultUpdateMsg = "已经更新";
//	public static final String statusResultUpdateMsgError = "更新失败";
	
	//action
	public static final String AppAction = "appInfo"; //
	public static final String StatusAction = "statusInfo"; //status infomation
	public static final String ResultAction = "resultInfo"; //status infomation
	
	
	static final String ACTIONTYPE="actionType";
	static final String NAME="name";
	static final String WORD="word";
	static final String MSG="msg";
	static final String CANCELABLE="cancelAble";
	
	
	public static String getMessageString(Context context,ProfileType type)
	{
		String str = null;
		switch(type)
		{
		case Profile_VPN:
			str = "Vpn";
			break;
		case Profile_WebClip:
			str = "WebClip";
			break;
		case Profile_PassPolicy:
			str = (String) context.getText(R.string.passpolicy);// "密码策略";
			break;
		case Profile_APN:
			str = "Apn";
			break;
		case Profile_Wifi:
			str = "Wifi";
			break;
		case Profile_Email:
			str = "Email";
			break;
		case Profile_Exchange:
			str = "Exchange";
			break;
		case Profile_RootCertificate:
			str = (String) context.getText(R.string.rootcertificate);// "ca 证书";
			break;
		case Profile_PkcsCertificate:
			str = (String) context.getText(R.string.pkcscertificate);//"psk 证书";
			break;
		case Profile_Restrictions:
			str = (String) context.getText(R.string.restrictions);//"限制";
			break;
		default:
			break;
		}
		return str;
		
		
	}
	public static Intent buildAppMsgIntent(long [] appIds)
	{
		 Intent intent = new Intent(); 
		 Bundle bundle = new Bundle();
	
		 bundle.putString(Key_MsgType, String.valueOf(messagType.APPMSG));
		 bundle.putLongArray(Key_AppIds, appIds);
		 intent.putExtras(bundle);    
		 intent.setAction(AppAction);  
		 return intent;
	}
	
	
	public static Intent buildStatusMsgIntent(String info)
	{
		//auto update
		int maxTime = 3; //seconds
		int delayTime = 0;
		
		 Intent intent = new Intent(); 
		 Bundle bundle = new Bundle();
	
		 bundle.putString(Key_MsgType, String.valueOf(messagType.STATUSMSG));
		 bundle.putString(Key_MsgInfo, info);
		 bundle.putInt(Key_MaxTime, maxTime);
		 bundle.putInt(Key_DelayTime, delayTime);
		
		 //Itent就是我们要发送的内容  07   
		 intent.putExtras(bundle);    
		 intent.setAction(StatusAction);  
		 
		 return intent;
		// mcontext.sendBroadcast(intent);
		 
	}
	
	
	public static Intent buildResultMsgIntent(String []result)
	{
		//auto update
		int maxTime = 60; //seconds
		int delayTime = 0;
		if (result.length > 1)
		{
			maxTime = 60;
			delayTime = 10;
		}
		else
		{
			maxTime = 60;
			delayTime = 0;
		}
		
		 Intent intent = new Intent(); 
		 Bundle bundle = new Bundle();
	
		 bundle.putString(Key_MsgType, String.valueOf(messagType.STATUSMSG));
		 bundle.putStringArray(Key_MsgInfo, result);
		 bundle.putInt(Key_MaxTime, maxTime);
		 bundle.putInt(Key_DelayTime, delayTime);
	
		 intent.putExtras(bundle);    
		 intent.setAction(ResultAction); 
		 return intent;
	}
	//if login error
	//send this intent to UI
	//if login succeed, dont send
	public static Intent buildLoginMsgIntent(String name,String password,String msg, boolean bcancel)
	{
		Intent intent = new Intent(); 
		intent.putExtra(ACTIONTYPE, ListenerService.USER_LOGIN_ACTION);
		intent.putExtra(NAME,name);
		intent.putExtra(WORD, password);
		intent.putExtra(MSG, msg);
		intent.putExtra(CANCELABLE, bcancel);
		 
		 return intent;
	}
	
}
