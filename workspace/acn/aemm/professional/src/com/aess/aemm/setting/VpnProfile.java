package com.aess.aemm.setting;

import java.util.ArrayList;

import com.aess.aemm.db.SettingsContent;

import android.content.Context;
import android.util.Log;

public class VpnProfile extends Profile {
	public static final String TAG = "VpnProfile";
	
	public static final String node_value_defineName = "UserDefinedName";
	public static final String node_value_OverridePrimary = "OverridePrimary";
	public static final String node_value_VPNType = "VPNType";

	public static final String node_value_AuthName = "AuthName";
	public static final String node_value_AuthPassword = "AuthPassword";
	public static final String node_value_CommRemoteAddress = "CommRemoteAddress";
	public static final String node_value_AuthEAPPlugins = "AuthEAPPlugins";
	public static final String node_value_AuthProtocol = "AuthProtocol";
	public static final String node_value_CCPMPPE40Enabled = "CCPMPPE40Enabled";
	public static final String node_value_CCPMPPE128Enabled = "CCPMPPE128Enabled";
	public static final String node_value_CCPEnabled = "CCPEnabled";

	public static final String node_value_RemoteAddress = "RemoteAddress";
	public static final String node_value_AuthenticationMethod = "AuthenticationMethod";
	public static final String node_value_XAuthName = "XAuthName";
	public static final String node_value_XAuthEnabled = "XAuthEnabled";
	public static final String node_value_LocalIdentifier = "LocalIdentifier";
	public static final String node_value_LocalIdentifierType = "LocalIdentifierType";
	public static final String node_value_SharedSecret = "SharedSecret";

	public static final String node_value_PayloadCertificateUUID = "PayloadCertificateUUID";


	public class VpnArg {
		public String vpnDefineName = null;
		public String vpnType = null; // "L2TP", "PPTP", or "IPSec",
		public boolean overridePrimary = false;
		
		//PPP
		public String authName = null;
		public String authPassword = null;
		public String commRemoteAddress = null;
		public String authEAPPlugins = null;
		public boolean isSecret = false;
		
		// IPSec
		public String remoteAddress = null; // VPN server name
		public String authenticationMethod = null; // Either "SharedSecret" or "Certificate"
		public String xAuthName = null; // space separated list
		public boolean xAuthEnabled = false;
		public String localIdentifier = null;
		public String localIdentifierType = null;
		public String sharedSecret = null;
		public String userCertUuid = null;
	}

	public int setValue(String keyValue, String value) {
		Log.i(TAG, String.format("%s = %s", keyValue, value));
		
		if (keyValue.equals(node_value_defineName)) {
			vpnarg.vpnDefineName = value;
		} else if (keyValue.equals(node_value_OverridePrimary)) {
			vpnarg.overridePrimary = value.equals("1") ? true : false;
		} else if (keyValue.equals(node_value_VPNType)) {
			vpnarg.vpnType = value; //"L2TP", "PPTP", or "IPSec",
		} else if (keyValue.equals(node_value_AuthName)) {
			vpnarg.authName = value;
		} else if (keyValue.equals(node_value_AuthPassword)) {
			vpnarg.authPassword = value;
		} else if (keyValue.equals(node_value_CommRemoteAddress)) {
			vpnarg.commRemoteAddress = value;
		} else if (keyValue.equals(node_value_AuthEAPPlugins)) {
			vpnarg.authEAPPlugins = value;
		} else if (keyValue.equals(node_value_AuthProtocol)) {

		} else if (keyValue.equals(node_value_CCPMPPE40Enabled)) {

		} else if (keyValue.equals(node_value_CCPMPPE128Enabled)) {

		} else if (keyValue.equals(node_value_CCPEnabled)) {
			vpnarg.isSecret = value.equals("1") ? true : false;
		} else if (keyValue.equals(node_value_RemoteAddress)) {
			vpnarg.remoteAddress = value;
		} else if (keyValue.equals(node_value_AuthenticationMethod)) {
			vpnarg.authenticationMethod = value;
		} else if (keyValue.equals(node_value_XAuthName)) {
			vpnarg.xAuthName = value;
		} else if (keyValue.equals(node_value_XAuthEnabled)) {
			vpnarg.xAuthEnabled = value.equals("1") ? true : false;
		} else if (keyValue.equals(node_value_LocalIdentifier)) {
			vpnarg.localIdentifier = value;
		} else if (keyValue.equals(node_value_LocalIdentifierType)) {
			vpnarg.localIdentifierType = value;
		} else if (keyValue.equals(node_value_SharedSecret)) {
			vpnarg.sharedSecret = value;
		} else if (keyValue.equals(node_value_PayloadCertificateUUID)) {
			vpnarg.userCertUuid = value;
		} else {
			super.setValue(keyValue, value);
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");
		int ret = -1;

		clearProfile(context);
		
		if (null != vpnarg && null != vpnarg.vpnType) {
			if (vpnarg.vpnType.equals("IPSec")) {
				//Either "SharedSecret" or "Certificate".
				if (vpnarg.authenticationMethod.equals("SharedSecret")) {
					vpnarg.vpnType = "L2TP_IPSEC_PSK";
				} else if (vpnarg.authenticationMethod.equals("Certificate")) {
					vpnarg.vpnType = "L2TP_IPSEC";
				}
			} else {
				//only support PPTP and L2TP
				if (vpnarg.vpnType.equals("PPTP")) {
					vpnarg.vpnType = "PPTP";
				}
				else if (vpnarg.vpnType.equals("L2TP")) {
					if(vpnarg.sharedSecret != null) {
						vpnarg.vpnType = "L2TP_IPSEC_PSK";
					} else {
						vpnarg.vpnType = "L2TP";
					}
				} else {
					return -1;
				}	
			}
			
			VpnSetup vpnSetup = new VpnSetup(context);
			
			ret = vpnSetup.addVPN(context, vpnarg);
			
			if (ret > 0) {
				String type = ProfileType.Profile_VPN.toString();
				SettingsContent pc = new SettingsContent();
				pc.setProfileArg(vpnarg.vpnDefineName, uuid, -1, version, type);
				pc.add(context);
				ret = 1;
			}
		}
		return ret;
	}

	@Override
	public int clearProfile(Context context) {
		Log.i(TAG, "clearProfile");
		
		String ver = null;
		if (null != version) {
			ver = version;
		} else {
			//clean all
			ver = "-1";
		}

		String type = ProfileType.Profile_VPN.toString();
		
		ArrayList<SettingsContent> dblist =SettingsContent.queryContentBy_Type_Version(context, type, ver);
		
		for (SettingsContent profile :dblist) {
			// system delete
			VpnSetup.delVPN(profile);
			// DB delete
			SettingsContent.delContentById(context, profile.mId);
		}
		return 0;
	}
	public VpnArg vpnarg = new VpnArg();
}
