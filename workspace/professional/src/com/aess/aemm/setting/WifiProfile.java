package com.aess.aemm.setting;

import java.util.ArrayList;

import com.aess.aemm.db.SettingsContent;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

class WifiProfile extends Profile {

	public static final String TAG = "WifiProfile";

	public static final int SECURITY_NONE = 0;
	public static final int SECURITY_WEP = 1;
	public static final int SECURITY_PSK = 2;
	public static final int SECURITY_EAP = 3;

	public static class WifiArg {
		public String security = null;
		// public String encryptionType = null;
		public String ssid_str = null;
		public String password = null;
		public String userCertsUuid = null;
		public String cacertUuid = null;
		public String phase2 = null;
		public String anonymous_identity = null;
		public String identity = null;
		public String userPass = null;
		public String eapValue = null;
		public boolean hidden_network = false;
		public boolean eap = false;
	}

	public int setValue(String key, String value) {
		Log.i(TAG, String.format("%s = %s", key, value));

		if (key.equals("EncryptionType")) {
			wifiarg.security = value;
		} else if (key.equals("Password")) {
			wifiarg.password = String.format("\"%s\"", value);
		} else if (key.equals("SSID_STR")) {
			wifiarg.ssid_str = String.format("\"%s\"", value);
		} else if (key.equals("HIDDEN_NETWORK")) {
			wifiarg.hidden_network = value.equals("true") ? true : false;
		} else if (key.equals("PayloadCertificateAnchorUUID")) {
			wifiarg.cacertUuid = value;
		} else if (key.equals("PayloadCertificateUUID")) {
			wifiarg.userCertsUuid = value;
		} else if (key.equals("EAPClientConfiguration")) {
			//if (value != null && value.equals("EAP") == true) {
				wifiarg.eap = true;
			//} else {
			//	wifiarg.eap = false;
			//}
		} else if (key.equals("AcceptEAPTypes")) {
			if (value.equals("13")) {
				wifiarg.eapValue = "TLS";
			} else if (value.equals("21")) {
				wifiarg.eapValue = "TTLS";
			} else if (value.equals("25")) {
				wifiarg.eapValue = "PEAP";
			} else {
				wifiarg.eapValue = "NONE";
			}
		} else if (key.equals("OuterIdentity")) {
			wifiarg.anonymous_identity = value;
		} else if (key.equals("TTLSInnerAuthentication")) {
			wifiarg.phase2 = value;
		} else if (key.equals("UserName")) {
			wifiarg.identity = value;
		} else if (key.equals("UserPassword")) {
			wifiarg.userPass = value;
		} else {
			super.setValue(key, value);
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");
		int ret = -1;
		
		clearProfile(context);

		if (null != wifiarg.ssid_str) {

			// WifiSetup ws = new WifiSetup(context);

			WifiConfiguration oldconfig = WifiSetup.systemHaveSameConfig(
					context, wifiarg.ssid_str);

			if (null == oldconfig) {
				WifiConfiguration config = new WifiConfiguration();
				WifiSetup.setWifiValues(wifiarg, config, context);
				WifiSetup.addWiFi(context, config);
			} else {
				WifiSetup.setWifiValues(wifiarg, oldconfig, context);
				WifiSetup.editWiFi(context, oldconfig);
			}

			String type = ProfileType.Profile_Wifi.toString();
			SettingsContent oldprofile = dbHaveSameConfig(context, type,
					wifiarg.ssid_str);
			if (null == oldprofile) {
				SettingsContent pc = new SettingsContent();
				pc.setProfileArg(wifiarg.ssid_str, uuid, -1, version, type);
				pc.add(context);
				ret = 1;
			} else {
				oldprofile.setProfileArg(null, null, -1, version, null);
				oldprofile.update(context);
				ret = 1;
			}

			wifiarg.ssid_str = null;
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
			Log.i(TAG, "Clear All");
			ver = VERSION;
		}

		String type = ProfileType.Profile_Wifi.toString();

		ArrayList<SettingsContent> dblist = SettingsContent
				.queryContentBy_Type_Version(context, type, ver);

		for (SettingsContent profile : dblist) {

			String oldname = profile.getName();

			boolean delete = true;
			
			if (wifiarg.ssid_str != null && wifiarg.ssid_str.equals(oldname)) {
				delete = false;
			}

			if (delete) {
				String ssid = profile.getName();

				WifiManager wifiMag = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);

				WifiSetup.deleteWiFiConfiguration(wifiMag, ssid);

				SettingsContent.delContentById(context, profile.mId);
			}
		}
		return 1;
	}

	private static SettingsContent dbHaveSameConfig(Context context,
			String ptype, String pname) {
		SettingsContent value = null;

		value = SettingsContent.queryContentBy_Type_Name(context, ptype, pname);

		return value;
	}

	private WifiArg wifiarg = new WifiArg();
}
