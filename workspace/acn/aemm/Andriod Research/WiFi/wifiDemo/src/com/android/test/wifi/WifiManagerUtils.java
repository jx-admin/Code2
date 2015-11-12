package com.android.test.wifi;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiManagerUtils {
	private static final String FILE_NAME_SCANRESULT = "ScanResult.data";

	public static synchronized ScanResult getScanResult(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME_SCANRESULT, Context.MODE_PRIVATE);
		List<ScanResult> scanResultList = mWifiManager.getScanResults();
		if (scanResultList != null) {
			for (ScanResult sr : scanResultList) {
				if (sp.contains(sr.BSSID)) {
					continue;
				}
				saveScanResult(context,sr);
				return sr;
			}

		}
		return null;
	}

	public static void saveScanResult(Context context, ScanResult sr) {
		if (sr != null) {
			SharedPreferences sp = context.getSharedPreferences(FILE_NAME_SCANRESULT, Context.MODE_PRIVATE);
			sp.edit().putString(sr.BSSID, sr.SSID).commit();
		}
	}

}
