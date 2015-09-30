package com.aess.aemm.commonutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.aess.aemm.networkutils.AutoAdress;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

public class CommUtils {
	// "com.accenture.aemm_professional";
	public static final String AEMM_PACKAGE_ID = "com.accenture.aemm_professional";
	public static final String TAG = "CommUtils";
	public static final boolean debug = false;

	// LOGCAT NAME
	public static final String LOG_COMMON_UTILS = "commonutils";
	public static final String LOG_DATA = "data";
	public static final String LOG_AUTHENTICATOR = "authenticator";
	public static final String LOG_DEVICEADMIN = "deviceadmin";
	public static final String LOG_APPMANAGER = "appmanager";
	public static final String LOG_NETWORK_UTILS = "networkutils";
	public static final String LOG_PROTOCOL = "protocol";
	public static final String LOG_PUSH = "push";
	public static final String LOG_UPDATE = "update";
	public static final String LOG_SETTING = "setting";
	public static final String LOG_FUNCTION = "function";
	public static final String LOG_VIEW = "view";

	// define Preference Key
	public static final String PREF_NAME = "config";
	public static final String KEY_CONFIG_SESSIONID = "sessionId";
	public static final String KEY_CONFIG_IMSI = "imsi";
	public static final String KEY_CONFIG_DOMAIN = "domainName";
	public static final String KEY_CONFIG_LASTUPDATE = "lastUpdate";

	public static final String KEY_CONFIG_CHECKCYCLE = "checkCycle";
	public static final String KEY_CONFIG_LOCCYCLE = "locCycle";
	public static final String KEY_CONFIG_RANGE = "locRange";
	public static final String KEY_CONFIG_APPCYCLE = "appCycle";

//	public static final String KEY_CONFIG_USERNAME = "userName";
//	public static final String KEY_CONFIG_PASSWORD = "password";
	public static final String KEY_CONFIG_IMEI = "imei";
	public static final String KEY_CONFIG_HALL_ENABLED = "hallEnable";

	// define Version data
	private static final String KEY_APP_LIST_VERSION = "AppListVersion";
	private static final String KEY_BLACK_LIST_VERSION = "BlackListVersion";
	private static final String KEY_PROFILE_VERSION = "ProfileVersion";
	private static final String KEY_VPN_PROFILE_VERSION = "VpnProfileVersion";
	private static final String KEY_CURRENT_CLIENT_NAME = "CurrentClientName";
	private static final String KEY_CURRENT_CLIENT_VERSION = "CurrentClientVersion";

	// define New Client Version info
	private static final String KEY_NEW_CLIENT_VERSION = "NewClientVersion";
	private static final String KEY_NEW_CLIENT_URL = "NewClientUrl";
	private static final String KEY_NEW_CLIENT_REQUIRE = "NewClientRequire";

	private static final String KEY_PROJECT_VERSION = "ProjectVersion";

	public static final String PROVIDER = "LocationProv";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String LOCATIME = "LocationTime";

	public static final String SOCKETIP = "SocketIp";
	public static final String SOCKPORT = "SocketPort";
	public static final String UPDATURL = "UpataUrl";

	public static final String REQPASSWORD = "RequirePassword";
	
	public final static String TYPE = "type";
	
	public final static String SILENT_INSTALL = "silentinstall";
	

	
	public final static String DERMA_ID="dermaId";
	
	public final static String PHONE_RESET = "phonereset";
	
	public final static String TRAFFIC_LIMIT = "trafficlimit";
	public final static String TRAFFIC_TIME = "tratime";
	
	public final static String SERVICE_LIMIT = "servicelimit";

	public static String getDeviceId(Context context) {
		String imsi = null;
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imsi = tm.getDeviceId();
		if (null == imsi) {
			imsi = CommUtils.getIMEI(context);
		}
		return imsi;
	}

	public static String getTimeString(String formatString) {
		if (null == formatString) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(new Date());
	}

	public static String getSysLanguage(Context context) {
		String language = "";
		language = Locale.getDefault().getLanguage();
		return language;
	}

	public static byte[] getImageData(String str) {
		InputStream inputStream = new ByteArrayInputStream(str.getBytes());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024 * 100];

		try {
			for (int i = inputStream.read(data); i > 0; i = inputStream
					.read(data)) {
				outputStream.write(data, 0, i);
			}

			inputStream.close();
			data = outputStream.toByteArray();
			outputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return data;
	}

	public static String getSessionId(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getString(
				CommUtils.KEY_CONFIG_SESSIONID, null);
	}

	public static void setSessionId(Context context, String sessionId) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_CONFIG_SESSIONID, sessionId);
		editor.commit();
	}
	
	public static String getIMSI(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getString(
				CommUtils.KEY_CONFIG_IMSI, null);
	}

	public static void setIMSI(Context context, String imsi) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_CONFIG_IMSI, imsi);
		editor.commit();
	}

	public static String getDomain(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getString(
				CommUtils.KEY_CONFIG_DOMAIN, null);
	}

	public static void setDomain(Context context, String DomainName) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_CONFIG_DOMAIN, DomainName);
		editor.commit();
	}
	
	public static String getIMEI(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getString(
				CommUtils.KEY_CONFIG_IMEI, null);
	}

	public static void setIMEI(Context context, String imei) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0).edit();
		editor.putString(CommUtils.KEY_CONFIG_IMEI, imei);
		editor.commit();
	}

	public static String getAppListVersion(Context context) {
		String version = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.getString(CommUtils.KEY_APP_LIST_VERSION, "0");
		return version;
	}

	public static void setAppListVersion(Context context, String version) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_APP_LIST_VERSION, version);
		editor.commit();
	}

	public static String getBlackListVersion(Context context) {
		String version = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.getString(CommUtils.KEY_BLACK_LIST_VERSION, "0");
		return version;
	}

	public static void setBlackListVersion(Context context, String version) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_BLACK_LIST_VERSION, version);
		editor.commit();
	}

	public static String getProfileVersion(Context context) {
		String version = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.getString(CommUtils.KEY_PROFILE_VERSION, "0");
		return version;
	}

	public static void setProfileVersion(Context context, String version) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_PROFILE_VERSION, version);
		editor.commit();
	}

	public static String getVpnProfileVersion(Context context) {
		String version = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.getString(CommUtils.KEY_VPN_PROFILE_VERSION, "0");
		return version;
	}

	public static void setVpnProfileVersion(Context context, String version) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_VPN_PROFILE_VERSION, version);
		editor.commit();
	}

	private static String getInitialClientVersion(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi;
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = String.valueOf(pi.versionCode);// .versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			// Log.e(TAG,"Exception",e);
		}
		return versionName;
	}

	// get current client version for comparing it with new client.
	public static String getCurrentClientVersion(Context context) {
		String version = getInitialClientVersion(context);
		return version;
	}

	// update current client version
	public static void setCurrentClientVersion(Context context, String version) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_CURRENT_CLIENT_VERSION, version);
		editor.commit();
	}

	// get current client version for comparing it with new client.
	public static String getCurrentClientName(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getString(
				CommUtils.KEY_CURRENT_CLIENT_NAME, "0");
	}

	// update current client version
	public static void setCurrentClientName(Context context, String name) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_CURRENT_CLIENT_NAME, name);
		editor.commit();
	}

	// get new client version for comparing it with current client.
	public static String getNewClientVersion(Context context) {
		String version = getCurrentClientVersion(context);
		version = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.getString(CommUtils.KEY_NEW_CLIENT_VERSION, version);
		return version;
	}

	// save new client version after data sync from server.
	public static void setNewClientVersion(Context context, String version) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_NEW_CLIENT_VERSION, version);
		editor.commit();
	}

	// get current client URL.
	public static String getNewClientURL(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getString(
				CommUtils.KEY_NEW_CLIENT_URL, "");
	}

	// update current client URL
	public static void setNewClientURL(Context context, String url) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putString(CommUtils.KEY_NEW_CLIENT_URL, url);
		editor.commit();
	}

	// get current client enforcement property
	public static int getNewClientRequire(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getInt(
				CommUtils.KEY_NEW_CLIENT_REQUIRE, 1);
	}

	// save new client enforcement property. 1--enforce; 0--non-enforce
	public static void setNewClientRequire(Context context, int enforce) {
		Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME, 0)
				.edit();
		editor.putInt(CommUtils.KEY_NEW_CLIENT_REQUIRE, enforce);
		editor.commit();
	}

	final public static int EXPRESS = 1;
	final public static int EXPRESS_PLUS = 2;
	final public static int PROFESSIONAL = 3;

	public static int getProjectVersion(Context context) {
		return context.getSharedPreferences(CommUtils.PREF_NAME, 0).getInt(
				KEY_PROJECT_VERSION, EXPRESS);
	}

	public static void setProjectVersion(Context context, int version) {
		Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putInt(KEY_PROJECT_VERSION, version);
		editor.commit();
	}

	public static int getPasswordRequire(Context context) {
		int value = 0;
		SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getInt(REQPASSWORD, 0);
		return value;
	}

	public static void setPasswordRequire(Context context, int value) {
		if (null != context) {
			SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
			Editor d = pp.edit();
			d.putInt(REQPASSWORD, value);
			d.commit();
		}
	}

	public static String getSocketIp(Context context) {
		SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
		return pp.getString(SOCKETIP, null);
	}

	public static int getPushPort(Context context) {
		SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
		return pp.getInt(SOCKPORT, 0);
	}

	public static String getUpdateURL(Context context) {
		SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
		return pp.getString(UPDATURL, null);
	}

	public static void setNetConfig(Context cxt, AutoAdress arg) {
		SharedPreferences pp = cxt.getSharedPreferences(PREF_NAME, 0);
		Editor ed = pp.edit();
		ed.putString(SOCKETIP, arg.getAddress());
		ed.putString(UPDATURL, arg.getUpdateURL());
		ed.putInt(SOCKPORT, arg.getConnectPort());
		ed.commit();
	}
	
	public static void updateLinkIP(Context cxt, String ip, String port) {
		SharedPreferences pp = cxt.getSharedPreferences(PREF_NAME, 0);
		Editor ed = pp.edit();
		ed.putString(SOCKETIP, ip);
		ed.putInt(SOCKPORT, Integer.valueOf(port));
		ed.commit();
	}

	public static void setBlockInstall(Context context, boolean value) {
		Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putBoolean(KEY_CONFIG_HALL_ENABLED, value);
		editor.commit();
	}

	public static boolean getBlockInstall(Context context) {
		return context.getSharedPreferences(PREF_NAME, 0).getBoolean(
				KEY_CONFIG_HALL_ENABLED, true);
	}

	public static void setLatitude(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(LONGITUDE, value);
		editor.commit();
	}
	
	public static String getLocaName(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0)
				.getString(PROVIDER, null);
	}
	
	public static void setLocaName(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(PROVIDER, value);
		editor.commit();
	}
	

	public static String getLatitude(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0)
				.getString(LONGITUDE, null);
	}
	
	public static void setLongitude(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(LATITUDE, value);
		editor.commit();
	}

	public static String getLongitude(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getString(LATITUDE, null);
	}

	public static void setLocaTime(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(LOCATIME, value);
		editor.commit();
	}

	public static String getLocaTime(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getString(LOCATIME, null);
	}

	public static void setCheckCycle(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(KEY_CONFIG_CHECKCYCLE, value);
		editor.commit();
	}

	public static String getCheckCycle(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getString(
				KEY_CONFIG_CHECKCYCLE, null);
	}
	
	public static long getCheckTime(Context cxt) {
		long ret = 0;
		String cycle = CommUtils.getCheckCycle(cxt);
		if (null != cycle) {
			ret = Integer.valueOf(cycle);
			ret = ret * 1000;
		}
		return ret;
	}
	
	public static void setLocRange(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(KEY_CONFIG_RANGE, value);
		editor.commit();
	}

	public static String getLocRange(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getString(
				KEY_CONFIG_RANGE, "100");
	}

	public static void setLocCycle(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(KEY_CONFIG_LOCCYCLE, value);
		editor.commit();
	}

	static final String defaultCycle = "86400";
	public static String getLocCycle(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getString(
				KEY_CONFIG_LOCCYCLE, defaultCycle);
	}
	
	public static long getLocTime(Context cxt) {
		long ret = 0;
		String cycle = CommUtils.getLocCycle(cxt);
		if (null != cycle) {
			ret = Integer.valueOf(cycle);
			ret = ret * 1000;
		}
		return ret;
	}

	public static void setAppCycle(Context cxt, String value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(KEY_CONFIG_APPCYCLE, value);
		editor.commit();
	}

	public static String getAppCycle(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getString(
				KEY_CONFIG_APPCYCLE, defaultCycle);
	}
	
	public static long getAppTime(Context cxt) {
		long ret = 0;
		String cycle = CommUtils.getAppCycle(cxt);
		if (null != cycle) {
			ret = Integer.valueOf(cycle);
			ret = ret * 1000;
		}
		return ret;
	}
	
	public static void setSilInstall(Context cxt, int value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putInt(SILENT_INSTALL, value);
		editor.commit();
	}

	public static int getSilInstall(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getInt(
				SILENT_INSTALL, 0);
	}
	
	public static void setDermaId(Context cxt, int value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putInt(DERMA_ID, value);
		editor.commit();
	}

	public static int getDermaId(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getInt(DERMA_ID, -1);
	}
	
	public static void setTrafficLimit(Context cxt, Long value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putLong(TRAFFIC_LIMIT, value);
		editor.commit();
	}

	public static Long getTrafficLimit(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getLong(
				TRAFFIC_LIMIT, 0);
	}
	
	public static void setTrafficTime(Context cxt, long time) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putLong(TRAFFIC_TIME, time);
		editor.commit();
	}

	public static long getTrafficTime(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getLong(TRAFFIC_TIME, 0);
	}
	
	public static void setPhoneReset(Context cxt, int value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putInt(PHONE_RESET, value);
		editor.commit();
	}

	public static int getPhoneReset(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getInt(PHONE_RESET, 0);
	}
	
	public static void setServiceLimit(Context cxt, int value) {
		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putInt(SERVICE_LIMIT, value);
		editor.commit();
	}

	public static int getServiceLimit(Context cxt) {
		return cxt.getSharedPreferences(PREF_NAME, 0).getInt(SERVICE_LIMIT, 0);
	}
	
//	public static void setLastPost(Context cxt, int value) {
//		Editor editor = cxt.getSharedPreferences(PREF_NAME, 0).edit();
//		editor.putInt(LAST_TRAFFIC, value);
//		editor.commit();
//	}
//
//	public static int getLastPost(Context cxt) {
//		return cxt.getSharedPreferences(PREF_NAME, 0).getInt(LAST_TRAFFIC, 0);
//	}

	// @SuppressWarnings("unused")
	// @Deprecated
	// private static Boolean getReportGPS(Context context) {
	// Boolean value = false;
	// SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
	// // value = pp.getBoolean(REPOGPS, false);
	// return value;
	// }
	//
	// @Deprecated
	// public static void setGPS(Context context, boolean value) {
	// if (null != context ) {
	// SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
	// Editor d = pp.edit();
	// // d.putBoolean(REPOGPS, value);
	// d.commit();
	// }
	// }
	//
	// @SuppressWarnings("unused")
	// @Deprecated
	// private static void setProtocolType(Context context, String type) {
	// Editor editor = context.getSharedPreferences(CommUtils.PREF_NAME,
	// 0).edit();
	// // editor.putString(CommUtils.KEY_PROTOCOL_TYPE, type);
	// editor.commit();
	// }

	/*
	 * construct data protocol about aemm device system.
	 */
	// public Map<String, String> write() {
	// Map<String, String> map=new HashMap<String,String>();
	// writeBuild(map);
	// writeTM(map);
	// writeBatary(map);
	// writeMemory(map);
	// writeProperty(map);
	// writeMac(map);
	// return map;
	// }
	// public static CommUtils getInstance(Context context) {
	// if (sInstance == null) {
	// sInstance = new CommUtils(context);
	// sInstance.onStart();
	// }
	// return sInstance;
	// }
	//
	// private void onStart(){
	// tm = (TelephonyManager)
	// context.getSystemService(Context.TELEPHONY_SERVICE);
	// }
	//
	// public void registerBatteryRecevie(){
	// context.registerReceiver(mBatInfoReceiver, new
	// IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	// }
	//
	// public void unRegisterBatteryRecevie(){
	// context.unregisterReceiver(mBatInfoReceiver);
	// }
	//
	// private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
	// public void onReceive(Context context, Intent intent) {
	// String action = intent.getAction();
	// if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
	// batteryLevel = intent.getIntExtra("level", 0);
	// batteryScale = intent.getIntExtra("scale", 100);
	// batteryTemperature=intent.getIntExtra("temperature", 0);
	// batteryVoltage=intent.getIntExtra("voltage", 0);
	// switch (intent.getIntExtra("status",
	// BatteryManager.BATTERY_STATUS_UNKNOWN)) {
	// case BatteryManager.BATTERY_STATUS_CHARGING:
	// batteryStatus = "ChargingStatus";
	// break;
	// case BatteryManager.BATTERY_STATUS_DISCHARGING:
	// batteryStatus = "DischargingStatus";
	// break;
	// case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
	// batteryStatus = "NotChargingStatus";
	// break;
	// case BatteryManager.BATTERY_STATUS_FULL:
	// batteryStatus = "FullStaus";
	// break;
	// case BatteryManager.BATTERY_STATUS_UNKNOWN:
	// batteryStatus = "UnknownStatus";
	// break;
	// default:
	// batteryStatus="";
	// }
	// switch (intent.getIntExtra("plugged", BatteryManager.BATTERY_PLUGGED_AC))
	// {
	// case BatteryManager.BATTERY_PLUGGED_AC:
	// batteryStatus2 = "PluggedWithAC";
	// break;
	// case BatteryManager.BATTERY_PLUGGED_USB:
	// batteryStatus2 = "PluggedWithUSB";
	// break;
	// default:
	// batteryStatus2="";
	// }
	//
	// switch (intent.getIntExtra("health",
	// BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
	// case BatteryManager.BATTERY_HEALTH_UNKNOWN:
	// batteryTemp = "UnknownError";
	// break;
	// case BatteryManager.BATTERY_HEALTH_GOOD:
	// batteryTemp = "GoodStatus";
	// break;
	// case BatteryManager.BATTERY_HEALTH_DEAD:
	// batteryTemp = "NoBattery";
	// break;
	// case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
	// batteryTemp = "VoltageOver";
	// break;
	// case BatteryManager.BATTERY_HEALTH_OVERHEAT:
	// batteryTemp = "OverHot";
	// break;
	// default:
	// batteryTemp="";
	// }
	// }
	// }
	// };
	// for get information of battery
	// private int batteryLevel = 0;
	// private int batteryScale = 0;
	// private int batteryTemperature;
	// private int batteryVoltage;
	// private String batteryStatus;
	// private String batteryStatus2;
	// private String batteryTemp=null;

	// for get information of system.
	// private TelephonyManager tm;
	// private Context context;
	//
	//
	//
	// private static CommUtils sInstance = null;
	//
	// protected CommUtils(Context context) {
	// this.context=context;
	// }

	/*
	 * 
	 */
	// private void writeProperty(Map<String, String>map){
	// map.put("java.vendor.url",System.getProperty("java.vendor.url"));
	// map.put("java.class.path",System.getProperty("java.class.path"));
	// map.put("user.home",System.getProperty("user.home"));
	// map.put("java.class.version",System.getProperty("java.class.version"));
	// map.put("os.version",System.getProperty("os.version"));
	// map.put("java.vendor",System.getProperty("java.vendor"));
	// map.put("user.dir",System.getProperty("user.dir"));
	// map.put("user.timezone",System.getProperty("user.timezone"));
	// map.put("path.separator",System.getProperty("path.separator"));
	// map.put("os.name",System.getProperty("os.name"));
	// map.put("os.arch",System.getProperty("os.arch"));
	// map.put("line.separator",System.getProperty("line.separator"));
	// map.put("file.separator",System.getProperty("file.separator"));
	// map.put("user.name",System.getProperty("user.name"));
	// map.put("java.version",System.getProperty("java.version"));
	// }

	/*
	 * 
	 */
	// private void writeMemory(Map<String, String>map){
	// final ActivityManager activityManager = (ActivityManager) context
	// .getSystemService(Context.ACTIVITY_SERVICE);
	//
	// ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
	// activityManager.getMemoryInfo(outInfo);
	//
	// map.put("FreeMemory",String.valueOf(outInfo.availMem >> 10)+"k");
	// map.put("FreeMemory",String.valueOf(outInfo.availMem >> 20)+"M");
	// map.put("isLowMemoryState",""+ String.valueOf(outInfo.lowMemory));
	// }

	/*
	 * build battry info
	 */
	// private void writeBatary(Map<String, String>map){
	// map.put("batteryLevel" , ""+batteryLevel);
	// map.put("batteryScale" ,""+batteryScale);
	// map.put("batteryTemperature" ,""+batteryTemperature);
	// map.put("batteryVoltage" , ""+batteryVoltage);
	// map.put("BatteryStatus" ,batteryStatus);
	// map.put("BatteryStatus2" , batteryStatus2);
	// map.put("BatteryTemp" , batteryTemp);
	// }

	/*
	 * build some part data protocol about aemm device system.
	 */
	// private void writeTM(Map<String, String>map){
	// map.put("DeviceId" , tm.getDeviceId());
	// map.put("DeviceSoftwareVersion" , tm.getDeviceSoftwareVersion());
	// map.put("SubscriberId" , tm.getSubscriberId());
	// map.put("VoiceMailNumber" , tm.getVoiceMailNumber());
	// map.put("Line1Number" , tm.getLine1Number());
	// map.put("NetworkCountryIso" , tm.getNetworkCountryIso());
	// map.put("PhoneType" , ""+ String.valueOf(tm.getPhoneType()));
	// map.put("SimSerialNumber" , tm.getSimSerialNumber());
	// map.put("SimOperator" , tm.getSimOperator());
	// map.put("SimOperatorName" , tm.getSimOperatorName());
	// map.put("SimCountryIso" , tm.getSimCountryIso());
	// map.put("NetworkOperator" , tm.getNetworkOperator());
	// map.put("NetworkOperatorName" , tm.getNetworkOperatorName());
	// switch (tm.getNetworkType()) {
	// /** Network type is unknown */
	// case TelephonyManager.NETWORK_TYPE_UNKNOWN:// = 0;
	// map.put("NetworkType","UNKNOW");
	// break;
	// /** Current network is GPRS */
	// case TelephonyManager.NETWORK_TYPE_GPRS:// 1;
	// map.put("NetworkType","GPRS");
	// break;
	// /** Current network is EDGE */
	// case TelephonyManager.NETWORK_TYPE_EDGE:// 2;
	// map.put("NetworkType","EDGE");
	// break;
	// /** Current network is UMTS */
	// case TelephonyManager.NETWORK_TYPE_UMTS:// 3;
	// map.put("NetworkType","UMTS");
	// break;
	// /** Current network is CDMA: Either IS95A or IS95B */
	// case TelephonyManager.NETWORK_TYPE_CDMA:// 4;
	// map.put("NetworkType","CDMA");
	// break;
	// /** Current network is EVDO revision 0 */
	// case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5;
	// map.put("NetworkType","EVDO_0");
	// break;
	// /** Current network is EVDO revision A */
	// case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6;
	// map.put("NetworkType","EVDO_A");
	// break;
	// /** Current network is 1xRTT */
	// case TelephonyManager.NETWORK_TYPE_1xRTT:// 7;
	// map.put("NetworkType","1xRTT");
	// break;
	// /** Current network is HSDPA */
	// case TelephonyManager.NETWORK_TYPE_HSDPA:// 8;
	// map.put("NetworkType","HSDPA");
	// break;
	// /** Current network is HSUPA */
	// case TelephonyManager.NETWORK_TYPE_HSUPA:// 9;
	// map.put("NetworkType","HSUPA");
	// break;
	// /** Current network is HSPA */
	// case TelephonyManager.NETWORK_TYPE_HSPA:// 10;
	// map.put("NetworkType","HSPA");
	// break;
	// /** Current network is iDen */
	// case TelephonyManager.NETWORK_TYPE_IDEN:// 11
	// map.put("NetworkType","IDEN");
	// break;
	// default:
	// map.put("NetworkType",""+ String.valueOf(tm.getNetworkType()));
	// }
	//
	// map.put("SimState" ,""+ tm.getSimState());
	// switch (tm.getSimState()) {
	// case TelephonyManager.SIM_STATE_ABSENT:
	// map.put("SimState" ,"SimAbsent");
	// break;
	// case TelephonyManager.SIM_STATE_UNKNOWN:
	// map.put("SimState" ,"Unknown");
	// break;
	// case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
	// map.put("SimState" ,"NetworkLocked");
	// break;
	// case TelephonyManager.SIM_STATE_PIN_REQUIRED:
	// map.put("SimState" ,"PinRequired");
	// break;
	// case TelephonyManager.SIM_STATE_PUK_REQUIRED:
	// map.put("SimState" ,"PukRequired");
	// break;
	// case TelephonyManager.SIM_STATE_READY:
	// map.put("SimState" ,"Ready");
	// break;
	// }
	// }

	/*
	 * 
	 */
	// private void writeBuild(Map<String, String>map){
	// map.put( "board",Build.BOARD);
	// map.put( "bootloader",Build.BOOTLOADER);
	// map.put( "brand",Build.BRAND);
	// map.put( "CPU_ABI",Build.CPU_ABI);
	// map.put( "DEVICE",Build.DEVICE);
	// map.put( "DISPLAY",Build.DISPLAY);
	// map.put( "FINGERPRINT",Build.FINGERPRINT);
	// map.put( "HARDWARE",Build.HARDWARE);
	// map.put( "HOST",Build.HOST);
	// map.put( "ID",Build.ID);
	// map.put( "MANUFACTURER",Build.MANUFACTURER);
	// map.put( "MODEL",Build.MODEL);
	// map.put( "PRODUCT",Build.PRODUCT);
	// map.put( "RADIO",Build.RADIO);
	// map.put( "TAGS",Build.TAGS);
	// map.put( "TIME",""+ String.valueOf(Build.TIME)); //
	// map.put( "TYPE",Build.TYPE);
	// map.put( "USER",Build.USER);
	// map.put( "CODENAME",Build.VERSION.CODENAME);
	// map.put( "INCREMENTAL",Build.VERSION.INCREMENTAL);
	// map.put( "RELEASE",Build.VERSION.RELEASE);
	// map.put( "SDK",String.valueOf(Build.VERSION.SDK));
	// map.put( "SDK_INT",""+String.valueOf(Build.VERSION.SDK_INT));
	// map.put( "isRoot", "" + DeviceRoot.isRooted());
	// }

	/*
	 * construct mac address for data protocol.
	 */
	// private void writeMac(Map<String, String>map){
	// WifiManager wifiManager =
	// (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	// if (!wifiManager.isWifiEnabled()) {
	// wifiManager.setWifiEnabled(true);
	// }
	// if (!wifiManager.isWifiEnabled()) {
	// return ;
	// }
	// WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	// map.put("mac",wifiInfo.getMacAddress());
	// }

	/*
	 * Get software version of device.
	 */
	// public static String getDeviceSoftwareVersion(Context context){
	// TelephonyManager tm;
	// String version = null;
	// tm =
	// (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	// version = tm.getDeviceSoftwareVersion();
	// return version;
	// }

	/*
	 * Get mac address of aemm device.
	 */
	// public static String getMacAddress(Context context){
	// WifiManager wifiManager =
	// (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	// if (!wifiManager.isWifiEnabled()) {
	// wifiManager.setWifiEnabled(true);
	// }
	// if (!wifiManager.isWifiEnabled()) {
	// return null;
	// }
	// WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	// return wifiInfo.getMacAddress();
	// }

	/*
	 * Get memory info about free memeory.
	 */
	// public static String getMemoryInfo(Context context){
	// final ActivityManager activityManager = (ActivityManager)
	// context.getSystemService(Context.ACTIVITY_SERVICE);
	// ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
	// activityManager.getMemoryInfo(outInfo);
	// String memInfo = "";
	// memInfo += String.valueOf(outInfo.availMem >> 10)+"k";
	// memInfo += String.valueOf(outInfo.availMem >> 20)+"M";
	// memInfo += String.valueOf(outInfo.lowMemory);
	// return memInfo;
	// }
}
