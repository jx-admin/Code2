package com.android.test.wifi;

import java.util.ArrayList;
import java.util.List;

import com.android.test.wifi.WifiProfile.WifiArg;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.util.Log;

public class WifiSetup {
	public static final String TAG = "WifiSetup";
	public static final String KEYSTORE_SPACE = "keystore://";
    
	// WPA is not used; plaintext or static WEP could be used
	public static final int NONE = 0;
	public static final int WEP = 1;

	// WPA pre-shared key (requires preSharedKey to be specified)
	public static final int PSK = 2;

	// using EAP authentication. Generally used with an external authentication
	// server
	public static final int WEAP = 3;

	// using EAP authentication and dynamically generated WEP keys
	// public static final int IEEE8021X = 4;

	public static enum EapValue {
		PEAP, TLS, TTLS
	}

	public WifiSetup(Context cxt) {
		Log.i(TAG, "WifiSetup");
//		if (null != cxt) {
//			context = cxt;
//		}
	}

	public static void setWifiValues(WifiArg arg, WifiConfiguration config, Context context) {
		Log.i(TAG, "setWifiValues");
		if (null != arg) {
			if (null == config) {
				config = new WifiConfiguration();
			}
			
			setConfiguration(context, config, arg);
		}
	}

	public static boolean addWiFi(Context context, WifiConfiguration config) {
		Log.i(TAG, "addWifi");

		boolean rlt = false;
		WifiManager wifiMag = (WifiManager)context
				.getSystemService(Context.WIFI_SERVICE);

		int NetId = wifiMag.addNetwork(config);

		if (NetId != -1) {
//			wifiMag.enableNetwork(NetId, false);
//			wifiMag.reconnect();
			rlt = true;
		}

		return rlt;
	}
	
	public static boolean editWiFi(Context context, WifiConfiguration config) {
		Log.i(TAG, "editWifi");

		boolean rlt = false;
		
		WifiManager wifiMag = (WifiManager) context
		.getSystemService(Context.WIFI_SERVICE);

		try {
			if (wifiMag.updateNetwork(config) >= 0) {
				rlt = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rlt;
	}
	
	public static int deleteWiFiConfiguration(WifiManager wifi, String ssid) {
		Log.i(TAG, "deleteWifiConfiguration");
		int ret = -1;
		List<WifiConfiguration> wifilist = getWifiConfig(wifi, ssid);

		for (WifiConfiguration config : wifilist) {
			wifi.removeNetwork(config.networkId);
			ret = 1;
		}
		return ret;
	}
	
//	public List<WifiConfiguration> ifRepeatInSystem(WifiArg wifiarg) {
//		Log.i(TAG, "ifRepeatInSystem");
//		List<WifiConfiguration> wifilist = new ArrayList<WifiConfiguration>();
//		List<WifiConfiguration> rltlist = new ArrayList<WifiConfiguration>();
//		
//		WifiManager wifiMag = (WifiManager)context
//				.getSystemService(Context.WIFI_SERVICE);
//		
//		wifilist = wifiMag.getConfiguredNetworks();
//		for (WifiConfiguration config: wifilist) {
//			if (true == config.SSID.equals(wifiarg.ssid_str)) {
//				rltlist.add(config);
//			}
//		}
//		return rltlist;
//	}
	
//	public static List<WifiConfiguration> ifRepeatInSystem(Context context, String ssid, WifiArg wifiarg) {
//		Log.i(TAG, "ifRepeatInSystem");
//		List<WifiConfiguration> wifilist = null;
//		if (null != ssid) {
//			WifiManager wifiMag = (WifiManager)context
//					.getSystemService(Context.WIFI_SERVICE);
//			wifilist = getWifiConfig(wifiMag, ssid);
//		}
//
//		return wifilist;
//	}
	
	public static WifiConfiguration systemHaveSameConfig(Context context, String ssid) {
		Log.i(TAG, "systemHaveSameConfig");
		WifiConfiguration wifi = null;
		if (null != ssid) {
			WifiManager wifiMag = (WifiManager)context
					.getSystemService(Context.WIFI_SERVICE);
			List<WifiConfiguration> wifilist = getWifiConfig(wifiMag, ssid);
			if (wifilist.isEmpty() == false) {
				wifi = wifilist.get(0);
			}
		}

		return wifi;
	}
	
	private static void setConfiguration(Context context, WifiConfiguration config, WifiArg wifiarg) {
		Log.i(TAG, "setConfiguration");
		
		// If the user adds a network manually, assume that it is hidden.
		if (null != config && null != wifiarg) {
			config.hiddenSSID = true;
			config.SSID = wifiarg.ssid_str;

			int mSecurity = getSecurityType(wifiarg.security, wifiarg.eap);

			switch (mSecurity) {
			case NONE: {
				config.allowedKeyManagement.set(KeyMgmt.NONE);
				break;
			}
			case WEP:{
                config.allowedKeyManagement.set(KeyMgmt.NONE);
                config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
                config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
				if (wifiarg.password != null) {
					config.wepKeys[0] = wifiarg.password;
				}
				break;
			}
			case PSK: {
				config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
				config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
				if (wifiarg.password != null) {
					config.preSharedKey = wifiarg.password;
				}
				break;
			}
			case WEAP: {
				config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
				config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);

				config.eap.setValue(wifiarg.eapValue);

				config.phase2.setValue(wifiarg.phase2 == null ? "" : "auth="
						+ wifiarg.phase2);

				String cacertValue = null;
				if (wifiarg.cacertUuid != null) {
					cacertValue = CertificateSetup.getCertName(context,
							wifiarg.cacertUuid);
				}

				config.ca_cert.setValue(cacertValue == null ? ""
						: KEYSTORE_SPACE + Credentials.CA_CERTIFICATE
								+ cacertValue);

				String usercacertValue = null;
				if (wifiarg.userCertsUuid != null) {
					usercacertValue = CertificateSetup.getCertName(context,
							wifiarg.userCertsUuid);
				}

				config.client_cert.setValue(usercacertValue == null ? ""
						: KEYSTORE_SPACE + Credentials.USER_CERTIFICATE
								+ usercacertValue);

				config.private_key.setValue(usercacertValue == null ? ""
						: KEYSTORE_SPACE + Credentials.USER_PRIVATE_KEY
								+ usercacertValue);

				config.identity.setValue(wifiarg.identity == null ? ""
						: wifiarg.identity);

				config.anonymous_identity
						.setValue(wifiarg.anonymous_identity == null ? ""
								: wifiarg.anonymous_identity);

				if (wifiarg.password != null) {
					config.password.setValue(wifiarg.userPass);
				}
				break;
			}
			}
		}
	}
	
	private static int getSecurityType(String security, boolean eap) {
		int rlt = NONE;
		
		if (null != security) {

			if (true == eap) {
				rlt = WEAP;
			} else {
				if (security.equals("WEP") || 
					security.equals("Any")) {
					rlt = WEP;
				} else if (security.equals("WPA") || security.equals("WPA/WPA2")) {
					rlt = PSK;
				} else if (security.equals("WPA/WPA2") ||
						security.equals("WEP") || 
						security.equals("Any") ) {
				}
			}
			
//			if (security.equalsIgnoreCase("WEP")) {
//			rlt = WEP;
//		} else {
//			if (security.equalsIgnoreCase("PSK")) {
//				rlt = WPA_PSK;
//			} else if (security.equalsIgnoreCase("WPA")) {
//				if (true == eap) {
//					rlt = WPA_EAP;
//				} else {
//					rlt = WPA_PSK;
//				}
//			}
//		}
		}
		return rlt;
	}
	
	private static List<WifiConfiguration> getWifiConfig(WifiManager wifiMag,
			String ssid) {
		Log.i(TAG, "getWifiConfig");

		List<WifiConfiguration> configlist = new ArrayList<WifiConfiguration>();
//		List<ScanResult> scanlist = wifiMag.getScanResults();
		List<WifiConfiguration> oldlist = wifiMag.getConfiguredNetworks();

		for (WifiConfiguration config : oldlist) {
			if (config.SSID.equals(ssid)
					|| config.SSID.equals(String.format("\"%s\"", ssid))) {
				configlist.add(config);
			}
		}

//		if (configlist.isEmpty()) {
//			
//			for (ScanResult result : scanlist) {
//				if (result.SSID.equals(ssid)
//						|| result.SSID.equals(String.format("\"%s\"", ssid))) {
//					//wificonfig = config;
//				}
//			}
//		}
		return configlist;
	}

//	private static int deleteWiFiList(WifiManager wifi,
//			List<WifiConfiguration> wifilist) {
//		int ret = 0;
//		for (WifiConfiguration config : wifilist) {
//			wifi.removeNetwork(config.networkId);
//			ret = 1;
//		}
//		return ret;
//	}

	// private Context context = null;
	// private WifiConfiguration config = null;
	// private boolean eap = false;
}
