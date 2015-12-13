//package com.android.test.wifi;
//
//import android.content.Context;
//import android.util.Log;
//
//public class Profile {
//	public static enum ProfileType {
//		Profile_NONE, Profile_VPN, Profile_WebClip, 
//		Profile_Password, Profile_APN, Profile_PassPolicy, 
//		Profile_Wifi, Profile_Email, Profile_Exchange,
//		Profile_RootCertificate,
//		Profile_PkcsCertificate,Profile_Restrictions
//	}
//
//	public static final String TAG = "Profile";
//	
//	public static final String TAGSET = "SETTING";
//	
//	public static final String node_value_identifier = "PayloadIdentifier";
//	public static final String node_value_payloaduuid = "PayloadUUID";
//	public static final String node_value_payloadversion = "PayloadVersion";
//	public static final String VERSION = "-1";
//	
//	public static Profile createProfile(ProfileType type) {
//		Profile pp = null;
//		switch (type) {
//
////		case Profile_Email: {
////			pp = new EmailProfile();
////			break;
////		}
////		case Profile_Exchange: {
////			pp = new ExchangeProfile();
////			break;
////		}
////		case Profile_APN: {
////			pp = new ApnProfile();
////			break;
////		}
//		case Profile_Wifi: {
//			pp = new WifiProfile();
//			break;
//		}
////		case Profile_WebClip: {
////			pp = new WebClipProfile();
////			break;
////		}
////		case Profile_PassPolicy: {
////			pp = new PwPolicyProfile();
////			break;
////		}
////		case Profile_VPN: {
////			 pp = new VpnProfile();
////			 break;
////		}
////		case Profile_RootCertificate:
////		case Profile_PkcsCertificate:
////			pp = new CertificateProfile();
////			break;
////		case Profile_Restrictions: {
////			 pp = new RestrictionProfile();
////			 break;
////		}
//		default: {
//			break;
//		}
//		}
//
//		return pp;
//	}
//
//	public int setValue(String key, String value) {
//		Log.i(TAG, String.format("%s = %s", key, value));
//		
//		if (key.equals(node_value_payloaduuid)) {
//			uuid = value;
//		} else if (key.equals(node_value_payloadversion)) {
//			version = value;
//		}
//		return 1;
//	}
//
//	public int printValue() {
//		return 0;
//	}
//
//	public int clearProfile(Context context) {
//		return 0;
//	}
//	public int saveProfile(Context context){
//		return 0;
//	}
//	
//	public int setVersion(String ver) {
//		if (null != ver) {
//			version = ver;
//		}
//		return 0;
//	}
//
//	protected String uuid = null;
//	protected String version = null;
//}
