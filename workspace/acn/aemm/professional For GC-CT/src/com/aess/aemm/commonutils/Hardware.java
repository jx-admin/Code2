package com.aess.aemm.commonutils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

public class Hardware {

	public int init(Context cxt) {
		registerBatteryRecevie(cxt);
		map = new HashMap<String, String>();
		
		writeBuild();
		writeProperty();
		writeTM(cxt);
		writeMemory(cxt);
		writeMac(cxt);
		writeBatary();
		writeStorage(cxt);
		writePin(cxt);

		unRegisterBatteryRecevie(cxt);
		return 1;
	}
	
	public Map<String, String> getHardwareInfo() {
		return map;
	}

	private void writeBuild() {
		map.put("board", Build.BOARD);
		map.put("bootloader", Build.BOOTLOADER);
		map.put("brand", Build.BRAND);
		map.put("CPU_ABI", Build.CPU_ABI);
		map.put("DEVICE", Build.DEVICE);
		map.put("DISPLAY", Build.DISPLAY);
		map.put("FINGERPRINT", Build.FINGERPRINT);
		map.put("HARDWARE", Build.HARDWARE);
		map.put("HOST", Build.HOST);
		map.put("ID", Build.ID);
		map.put("MANUFACTURER", Build.MANUFACTURER);
		map.put("MODEL", Build.MODEL);
		map.put("PRODUCT", Build.PRODUCT);
		map.put("RADIO", Build.RADIO);
		map.put("TAGS", Build.TAGS);
		map.put("TIME", "" + String.valueOf(Build.TIME));
		map.put("TYPE", Build.TYPE);
		map.put("USER", Build.USER);
		map.put("CODENAME", Build.VERSION.CODENAME);
		map.put("INCREMENTAL", Build.VERSION.INCREMENTAL);
		map.put("RELEASE", Build.VERSION.RELEASE);
		map.put("SDK_INT", "" + String.valueOf(Build.VERSION.SDK_INT));
		map.put("isRoot", "" + HelpUtils.isRooted());
	}

	private void writeProperty() {
		map.put("java.vendor.url", System.getProperty("java.vendor.url"));
		map.put("java.class.path", System.getProperty("java.class.path"));
		map.put("user.home", System.getProperty("user.home"));
		map.put("java.class.version", System.getProperty("java.class.version"));
		map.put("os.version", System.getProperty("os.version"));
		map.put("java.vendor", System.getProperty("java.vendor"));
		map.put("user.dir", System.getProperty("user.dir"));
		map.put("user.timezone", System.getProperty("user.timezone"));
		map.put("path.separator", System.getProperty("path.separator"));
		map.put("os.name", System.getProperty("os.name"));
		map.put("os.arch", System.getProperty("os.arch"));
//		map.put("line.separator", System.getProperty("line.separator"));
		map.put("file.separator", System.getProperty("file.separator"));
		map.put("user.name", System.getProperty("user.name"));
		map.put("java.version", System.getProperty("java.version"));
	}

	private void writeTM(Context cxt) {
		TelephonyManager tm = (TelephonyManager) cxt
				.getSystemService(Context.TELEPHONY_SERVICE);

		map.put("DeviceId", tm.getDeviceId());
		map.put("DeviceSoftwareVersion", tm.getDeviceSoftwareVersion());
		map.put("SubscriberId", tm.getSubscriberId());
		map.put("VoiceMailNumber", tm.getVoiceMailNumber());
		map.put("Line1Number", tm.getLine1Number());
		map.put("NetworkCountryIso", tm.getNetworkCountryIso());
		map.put("PhoneType", "" + String.valueOf(tm.getPhoneType()));
		map.put("SimSerialNumber", tm.getSimSerialNumber());
		map.put("SimOperator", tm.getSimOperator());
		map.put("SimOperatorName", tm.getSimOperatorName());
		map.put("SimCountryIso", tm.getSimCountryIso());
		map.put("NetworkOperator", tm.getNetworkOperator());
		map.put("NetworkOperatorName", tm.getNetworkOperatorName());

		switch (tm.getNetworkType()) {
		/** Network type is unknown */
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:// = 0;
			map.put("NetworkType", "UNKNOW");
			break;
		/** Current network is GPRS */
		case TelephonyManager.NETWORK_TYPE_GPRS:// 1;
			map.put("NetworkType", "GPRS");
			break;
		/** Current network is EDGE */
		case TelephonyManager.NETWORK_TYPE_EDGE:// 2;
			map.put("NetworkType", "EDGE");
			break;
		/** Current network is UMTS */
		case TelephonyManager.NETWORK_TYPE_UMTS:// 3;
			map.put("NetworkType", "UMTS");
			break;
		/** Current network is CDMA: Either IS95A or IS95B */
		case TelephonyManager.NETWORK_TYPE_CDMA:// 4;
			map.put("NetworkType", "CDMA");
			break;
		/** Current network is EVDO revision 0 */
		case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5;
			map.put("NetworkType", "EVDO_0");
			break;
		/** Current network is EVDO revision A */
		case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6;
			map.put("NetworkType", "EVDO_A");
			break;
		/** Current network is 1xRTT */
		case TelephonyManager.NETWORK_TYPE_1xRTT:// 7;
			map.put("NetworkType", "1xRTT");
			break;
		/** Current network is HSDPA */
		case TelephonyManager.NETWORK_TYPE_HSDPA:// 8;
			map.put("NetworkType", "HSDPA");
			break;
		/** Current network is HSUPA */
		case TelephonyManager.NETWORK_TYPE_HSUPA:// 9;
			map.put("NetworkType", "HSUPA");
			break;
		/** Current network is HSPA */
		case TelephonyManager.NETWORK_TYPE_HSPA:// 10;
			map.put("NetworkType", "HSPA");
			break;
		/** Current network is iDen */
		case TelephonyManager.NETWORK_TYPE_IDEN:// 11
			map.put("NetworkType", "IDEN");
			break;
		default:
			map.put("NetworkType", "" + String.valueOf(tm.getNetworkType()));
		}

		switch (tm.getSimState()) {
		case TelephonyManager.SIM_STATE_ABSENT:
			map.put("SimState", "SimAbsent");
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			map.put("SimState", "Unknown");
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			map.put("SimState", "NetworkLocked");
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			map.put("SimState", "PinRequired");
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			map.put("SimState", "PukRequired");
			break;
		case TelephonyManager.SIM_STATE_READY:
			map.put("SimState", "Ready");
			break;
		}
	}

	private void writeMemory(Context cxt) {
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);

		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);

		map.put("FreeMemory", String.valueOf(outInfo.availMem >> 10) + "k");
		map.put("FreeMemory", String.valueOf(outInfo.availMem >> 20) + "M");
		map.put("isLowMemoryState", "" + String.valueOf(outInfo.lowMemory));
	}

	private void writeMac(Context cxt) {
		WifiManager wifiManager = (WifiManager) cxt
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		if (!wifiManager.isWifiEnabled()) {
			return;
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		map.put("mac", wifiInfo.getMacAddress());
	}

	private void writeStorage(Context cxt) {
		String state = Environment.getExternalStorageState();
		map.put("ExternalStorageState", Environment.getExternalStorageState());
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			
			long blockCount = sf.getBlockCount();
			long freeCount = sf.getFreeBlocks();

			String free = Formatter.formatFileSize(cxt, blockSize * freeCount);
			String total = Formatter.formatFileSize(cxt, blockSize * blockCount);
			map.put("EStorageUsedSize", free);
			map.put("EStorageFreeSize", total);
		}

		File data = Environment.getDataDirectory();
		
		StatFs sf = new StatFs(data.getPath());
		long BlockSize = sf.getBlockSize();
		long TotalCount = sf.getBlockCount();
		long FreeCount = sf.getFreeBlocks();
		
		String free = Formatter.formatFileSize(cxt, FreeCount * BlockSize);
		String total = Formatter.formatFileSize(cxt, TotalCount * BlockSize);

		map.put("RStorageFreeSize", free);
		map.put("RStorageTotalSize", total);
	}
	
	private void writePin(Context cxt) {
		DevicePolicyManager dpm = (DevicePolicyManager)cxt
		.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		boolean PasswordSufficient = dpm.isActivePasswordSufficient();
		map.put("PasswordSufficient", PasswordSufficient + "");
	}
	
	private void writeBatary() {
		map.put("batteryLevel", "" + batteryLevel);
		map.put("batteryScale", "" + batteryScale);
		map.put("batteryTemperature", "" + batteryTemperature);
		map.put("batteryVoltage", "" + batteryVoltage);
		map.put("BatteryStatus", batteryStatus);
		map.put("BatteryStatus2", batteryStatus2);
		map.put("BatteryTemp", batteryTemp);
	}

	private void registerBatteryRecevie(Context cxt) {
		cxt.registerReceiver(mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	private void unRegisterBatteryRecevie(Context cxt) {
		cxt.unregisterReceiver(mBatInfoReceiver);
	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				batteryLevel = intent.getIntExtra("level", 0);
				batteryScale = intent.getIntExtra("scale", 100);
				batteryTemperature = intent.getIntExtra("temperature", 0);
				batteryVoltage = intent.getIntExtra("voltage", 0);
				switch (intent.getIntExtra("status",
						BatteryManager.BATTERY_STATUS_UNKNOWN)) {
				case BatteryManager.BATTERY_STATUS_CHARGING:
					batteryStatus = "ChargingStatus";
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
					batteryStatus = "DischargingStatus";
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					batteryStatus = "NotChargingStatus";
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					batteryStatus = "FullStaus";
					break;
				case BatteryManager.BATTERY_STATUS_UNKNOWN:
					batteryStatus = "UnknownStatus";
					break;
				default:
					batteryStatus = "";
				}
				switch (intent.getIntExtra("plugged",
						BatteryManager.BATTERY_PLUGGED_AC)) {
				case BatteryManager.BATTERY_PLUGGED_AC:
					batteryStatus2 = "PluggedWithAC";
					break;
				case BatteryManager.BATTERY_PLUGGED_USB:
					batteryStatus2 = "PluggedWithUSB";
					break;
				default:
					batteryStatus2 = "";
				}

				switch (intent.getIntExtra("health",
						BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
				case BatteryManager.BATTERY_HEALTH_UNKNOWN:
					batteryTemp = "UnknownError";
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD:
					batteryTemp = "GoodStatus";
					break;
				case BatteryManager.BATTERY_HEALTH_DEAD:
					batteryTemp = "NoBattery";
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
					batteryTemp = "VoltageOver";
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT:
					batteryTemp = "OverHot";
					break;
				default:
					batteryTemp = "";
				}
			}
		}
	};

	private int batteryLevel = 0;
	private int batteryScale = 0;
	private int batteryTemperature = 0;
	private int batteryVoltage = 0;
	private String batteryStatus = null;
	private String batteryStatus2 = null;
	private String batteryTemp = null;

	private Map<String, String> map = null;
}
