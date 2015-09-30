package com.example.sensordemo;

import java.util.List;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
public class TelManager{
	private static TelephonyManager tm;
	public TelManager(Context context){
		if(tm==null){
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		}
	}
	
	public String getInfo(){
		StringBuilder sb=new StringBuilder();
		
		/*
		 * 电话状态：
		 * 1.tm.CALL_STATE_IDLE=0            无活动
		 * 2.tm.CALL_STATE_RINGING=1     响铃
		 * 3.tm.CALL_STATE_OFFHOOK=2  摘机
		 */ 
//  tm.getCallState(); //int
		switch(tm.getCallState()){
		case TelephonyManager.CALL_STATE_IDLE:
			sb.append("无活动");
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			sb.append("响铃");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			sb.append("摘机");
			break;
		}  
		sb.append('\n');
		
		/*
		 * 手机类型：
		 * 例如： PHONE_TYPE_NONE  无信号
		 * PHONE_TYPE_GSM   GSM信号
		 * PHONE_TYPE_CDMA  CDMA信号
		 */ 
		int phoneType=tm.getPhoneType(); //int 
		if(phoneType==TelephonyManager.PHONE_TYPE_NONE){
			sb.append('\n');
			sb.append("无信号");
		}
		
		
		/*
		 * 电话方位：
		 */ 
		CellLocation mCellLocation=tm.getCellLocation(); //CellLocation 
		sb.append(getLastKnownLocation(phoneType,mCellLocation));
		sb.append('\n');
		
		/*
		 * 唯一的设备ID：
		 * GSM手机的 IMEI 和 CDMA手机的 MEID. 
		 * Return null if device ID is not available.
		 */ 
		String deviceId=tm.getDeviceId(); //String 
		sb.append("deviceId: "+deviceId);
		sb.append('\n');
		
		
		/*
		 * 设备的软件版本号：
		 * 例如：the IMEI/SV(software version) for GSM phones.
		 * Return null if the software version is not available. 
		 */ 
		String deviceSoftwareVersion=tm.getDeviceSoftwareVersion(); //String 
		sb.append("deviceId: "+deviceSoftwareVersion);
		sb.append('\n');
		
		/*
		 * 手机号：
		 * GSM手机的 MSISDN.
		 * Return null if it is unavailable. 
		 */ 
		String line1Number=tm.getLine1Number(); //String 
		sb.append("line1Number: "+line1Number);
		sb.append('\n');
		
		/*
		 * 附近的电话的信息:
		 * 类型：List<NeighboringCellInfo> 
		 * 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
		 */
		List<NeighboringCellInfo> mNeighboringCellInfos=tm.getNeighboringCellInfo(); //List<NeighboringCellInfo> 
		for(NeighboringCellInfo mNeighboringCellInfo:mNeighboringCellInfos){
			sb.append("附近电话的信息：");
			sb.append(mNeighboringCellInfo.toString());
			sb.append('\n');
		}
		
		
		/*
		 * 获取ISO标准的国家码，即国际长途区号。
		 * 注意：仅当用户已在网络注册后有效。
		 *       在CDMA网络中结果也许不可靠。
		 */ 
		String mNetworkCountryIso=tm.getNetworkCountryIso(); //String 
		sb.append("CountryIso: "+mNetworkCountryIso);
		
		/*
		 * MCC+MNC(mobile country code + mobile network code)
		 * 注意：仅当用户已在网络注册时有效。
		 *    在CDMA网络中结果也许不可靠。
		 */ 
		String mNetworkOperator=tm.getNetworkOperator(); //String 
		sb.append("MCC+MNC: "+mNetworkOperator);
		
		/*
		 * 按照字母次序的current registered operator(当前已注册的用户)的名字
		 * 注意：仅当用户已在网络注册时有效。
		 *    在CDMA网络中结果也许不可靠。
		 */ 
		String mNetworkOperatorName=tm.getNetworkOperatorName(); //String
		sb.append("etworkOperatorName: "+mNetworkOperatorName);
		
		/*
		 * 当前使用的网络类型：
		 * 例如： NETWORK_TYPE_UNKNOWN  网络类型未知  0
		 * NETWORK_TYPE_GPRS     GPRS网络  1
		 * NETWORK_TYPE_EDGE     EDGE网络  2
		 * NETWORK_TYPE_UMTS     UMTS网络  3
		 * NETWORK_TYPE_HSDPA    HSDPA网络  8 
		 * NETWORK_TYPE_HSUPA    HSUPA网络  9
		 * NETWORK_TYPE_HSPA     HSPA网络  10
		 * NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4
		 * NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5
		 * NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6
		 * NETWORK_TYPE_1xRTT    1xRTT网络  7
		 */ 
		int mNetworkType=tm.getNetworkType(); //int
		sb.append("网络类型:");
		switch(mNetworkType){
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			sb.append("网络类型未知 ");
			break;
		case TelephonyManager.NETWORK_TYPE_GPRS:
			sb.append("GPRS网络 ");
			break;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			sb.append("EDGE网络");
			break;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			sb.append("UMTS网络");
			break;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			sb.append("HSDPA网络");
			break;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			sb.append("HSUPA网络");
			break;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			sb.append("HSPA网络");
			break;
		case TelephonyManager.NETWORK_TYPE_CDMA:
			sb.append("CDMA网络,IS95A 或 IS95B.");
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			sb.append("EVDO网络, revision 0");
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			sb.append("EVDO网络, revision A");
			break;
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			sb.append("1xRTT网络");
			break;
		}
		sb.append('\n');
		
		/*
		 * Returns the ISO country code equivalent for the SIM provider's country code.
		 * 获取ISO国家码，相当于提供SIM卡的国家码。
		 */ 
		String mSimCountryIso=tm.getSimCountryIso(); //String 
		sb.append("SIM卡的国家码: "+mSimCountryIso);
		sb.append('\n');
		
		/*
		 * Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits.
		 * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
		 * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
		 */ 
		String mSimOperator=tm.getSimOperator(); //String 
		sb.append("mSimOperator: "+mSimOperator);
		sb.append('\n');
		
		/*
		 * 服务商名称：
		 * 例如：中国移动、联通
		 * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
		 */ 
		String mSimOperatorName=tm.getSimOperatorName(); //String 
		sb.append("SimOperatorName: "+mSimOperatorName);
		sb.append('\n');
		
		/*
		 * SIM卡的序列号：
		 * 需要权限：READ_PHONE_STATE
		 */ 
		String mSimSerialNumber=tm.getSimSerialNumber(); //String 
		sb.append("SimSerialNumber: "+mSimSerialNumber);
		sb.append('\n');
		
		/*
		 * SIM的状态信息：
		 *  SIM_STATE_UNKNOWN             未知状态 0
		 *  SIM_STATE_ABSENT                 没插卡 1
		 *  SIM_STATE_PIN_REQUIRED     锁定状态，需要用户的PIN码解锁 2
		 *  SIM_STATE_PUK_REQUIRED    锁定状态，需要用户的PUK码解锁 3
		 *  SIM_STATE_NETWORK_LOCKED   锁定状态，需要网络的PIN码解锁 4
		 *  SIM_STATE_READY            就绪状态 5
		 */ 
		int mSimState=tm.getSimState(); //int 
		sb.append("SIM的状态:");
		switch(mSimState){
		case TelephonyManager.SIM_STATE_UNKNOWN:
			sb.append("未知状态");
			break;
		case TelephonyManager.SIM_STATE_ABSENT:
			sb.append("没插卡");
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			sb.append("锁定状态，需要用户的PIN码解锁");
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			sb.append("锁定状态，需要用户的PUK码解锁");
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			sb.append("锁定状态，需要网络的PIN码解锁");
			break;
		case TelephonyManager.SIM_STATE_READY:
			sb.append("就绪状态");
			break;
			
		}
		sb.append('\n');
		
		/*
		 * 唯一的用户ID：
		 * 例如：IMSI(国际移动用户识别码) for a GSM phone.
		 * 需要权限：READ_PHONE_STATE
		 */ 
		String mSubscriberId=tm.getSubscriberId(); //String 
		sb.append("IMSI(国际移动用户识别码): "+mSubscriberId);
		sb.append('\n');
		
		
		/*
		 * 取得和语音邮件相关的标签，即为识别符
		 * 需要权限：READ_PHONE_STATE
		 */ 
		String mVoiceMailAlphaTag=tm.getVoiceMailAlphaTag(); //String
		sb.append("识别符: "+mVoiceMailAlphaTag);
		sb.append('\n'); 
		
		/*
		 * 获取语音邮件号码：
		 * 需要权限：READ_PHONE_STATE
		 */ 
		String mVoiceMailNumber=tm.getVoiceMailNumber(); //String 
		sb.append("语音邮件号码: "+mVoiceMailNumber);
		sb.append('\n'); 
		
		/*
		 * ICC卡是否存在
		 */ 
		boolean mIccCard=tm.hasIccCard(); //boolean 
		sb.append("ICC卡是否存在: "+mIccCard);
		sb.append('\n');  
		
		/*
		 * 是否漫游:
		 * (在GSM用途下)
		 */ 
		boolean isNetworkRoaming=tm.isNetworkRoaming(); // boolean
		sb.append("isNetworkRoaming: "+isNetworkRoaming);
		sb.append('\n');  
		
		return sb.toString();
	}
	public String getLastKnownLocation(int mPhoneType,CellLocation mLocation) {  
		
		if (mLocation == null) return "{}";  
		
		String rtn ="";  
		
		rtn += "\"operator\":\"" + tm.getNetworkOperator() +"\"";  
		
		rtn += ",\"operatorName\":\"" + tm.getNetworkOperatorName() +"\"";  
		
		if (mPhoneType == TelephonyManager.PHONE_TYPE_GSM) {  
			
			GsmCellLocation gsm = (GsmCellLocation) mLocation;  
			
			rtn +=",\"type\":\"GSM\"";  
			
			rtn += ",\"cid\":" + gsm.getCid();  
			
			rtn += ",\"lac\":" + gsm.getLac();  
			
		} else if (mPhoneType == TelephonyManager.PHONE_TYPE_CDMA) {  
			
			CdmaCellLocation cdma = (CdmaCellLocation) mLocation;  
			
			rtn +=",\"type\":\"CDMA\"";  
			
			rtn += ",\"baseStationId\":" + cdma.getBaseStationId();  
			
			rtn += ",\"baseStationLatitude\":" + cdma.getBaseStationLatitude();  
			
			rtn += ",\"baseStationLongitude\":" + cdma.getBaseStationLongitude();  
			
			rtn += ",\"networkId\":" + cdma.getNetworkId();  
			
			rtn += ",\"systemId\":" + cdma.getSystemId();  
			
		}  
		
		return "{" + rtn +"}";  
		
	}  
	
}

