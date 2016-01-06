package com.android.test.wifi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiManagerUtils {
	private static final String FILE_WF = "wf_dat";
	private static final String FILE_WFN = "wfn_dat";
	private static final String KEY_STR = "\"key\":\"";
	private static final String VALUE_STR = "\",\"value\":\"";

	private static String tag = "WifiManagerUtils";

	private static String passWord = "we_123_!@#A?>DFJK314asdfe&*^";

	public static synchronized void getScanResult(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		SharedPreferences sp = context.getSharedPreferences(FILE_WF, Context.MODE_PRIVATE);
		SharedPreferences spn = context.getSharedPreferences(FILE_WFN, Context.MODE_PRIVATE);
		List<ScanResult> scanResultList = mWifiManager.getScanResults();
		if (scanResultList != null) {
			String bssid;
			String ssid;
			Editor edit = spn.edit();
			for (ScanResult sr : scanResultList) {
				bssid = encode(sr.BSSID);
				if (sp.contains(bssid)) {
					Log.d(tag, "saved old");
					continue;
				} else if (spn.contains(bssid)) {
					Log.d(tag, "saved in new");
					continue;
				}
				Log.d(tag, "new");
				ssid = encode(sr.SSID);
				edit.putString(bssid, ssid);
			}
			edit.commit();

		}
	}

	public static synchronized String getData(Context context) {
		Log.d(tag, "getData");
		SharedPreferences spn = context.getSharedPreferences(FILE_WFN, Context.MODE_PRIVATE);
		Map<String, String> map = (Map<String, String>) spn.getAll();
		if (map == null || map.isEmpty()) {
			return null;
		}
		SharedPreferences sp = context.getSharedPreferences(FILE_WF, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		String key;
		String value;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			sb.append('{');
			sb.append(KEY_STR);
			sb.append(key);
			sb.append(VALUE_STR);
			sb.append(value);
			sb.append('"');
			sb.append('}');
			sb.append(',');
			edit.putString(key, value);
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(']');
		edit.commit();
		spn.edit().clear().commit();

		Log.d(tag, sb.toString());
		return sb.toString();
	}

	private static String encode(String content) {
		Log.d(tag, "str:" + content);
		String enCodeStr = eu.encryptAES(content, passWord);
		Log.d(tag, "enCodeStr:" + enCodeStr);
		String decode=eu.decryptAES(enCodeStr,passWord);
		Log.d(tag, "decode:" + decode);
		return enCodeStr;
	}
	static EncryptionUtil eu=new EncryptionUtil();

}
