package com.android.accenture.aemm.express.updataservice;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import java.util.List;

import com.android.accenture.aemm.express.Main;
import com.android.accenture.aemm.express.ServiceDia;
import com.android.accenture.aemm.express.deviceAdminReceiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

public class DeviceAdminLocalSetup {

	public static final int RESET_PASSWORD_REQUIRE_ENTRY = 1;
	public static final String TOPACTIVITYSIGN = "com.android.settings.ChooseLockGeneric";

//	final static int mPasswordQualityValues[] = new int[] {
//			DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED,
//			DevicePolicyManager.PASSWORD_QUALITY_SOMETHING,
//			DevicePolicyManager.PASSWORD_QUALITY_NUMERIC,
//			DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC,
//			DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC,
//			0x60000 // DevicePolicyManager.PASSWORD_QUALITY_COMPLEX
//	};

	public static DeviceAdminLocalSetup deviceAdmin = null;
	DevicePolicyManager mDPM;
	ActivityManager mAM;
	ComponentName mDeviceComponentName;

	boolean allowSimple;
	int pwQuality;
	int pwLength;
	int maxFailedPw;
	long timeMs; // setMaximumTimeToLock
	int minComplexNum;
	int maxPINAgeInMs;
	int pinHistory;

	public void setDeviceAdminValues(boolean allowSimple, boolean forcePin,
			int maxFailedAttempts, int maxGracePeriod, int maxInactivity,
			int minComplexChars, int minLength, boolean requireAlphanumeric,
			int maxPINAgeInDays, int pinHistory) {
		// set default value
		this.allowSimple = allowSimple;
		this.pwLength = minLength;
		this.maxFailedPw = maxFailedAttempts;
		this.timeMs = 60 * 1000 * maxInactivity;
		this.minComplexNum = minComplexChars;
		this.maxPINAgeInMs = maxPINAgeInDays * 86400 * 1000;
		this.pinHistory = pinHistory;
		this.pwQuality = DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED;

		if (forcePin) {
			pwQuality = DevicePolicyManager.PASSWORD_QUALITY_SOMETHING;
		}
		
		if (allowSimple) {
			pwQuality = DevicePolicyManager.PASSWORD_QUALITY_NUMERIC;
		}
		
		if (requireAlphanumeric) {
			pwQuality = DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC;
		}
		
//		if (minComplexNum > 0) {
//			pwQuality = 393216; //DevicePolicyManager.PASSWORD_QUALITY_COMPLEX;
//		}
	}

	// Bug #2650 shxn
	public static void resetPolicy(Context context) {
		if (context != null) {

			DevicePolicyManager dpm = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);

			ComponentName cName = new ComponentName(context,
					deviceAdminReceiver.class);

			boolean active = dpm.isAdminActive(cName);

			if (active) {
				dpm.setPasswordQuality(cName,
						DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
				dpm.setPasswordMinimumLength(cName, 0);
				// dpm.resetPassword("",
				// DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
			}
		}
	}

	// Bug #2650 shxn

	protected DeviceAdminLocalSetup() {

	}

	public static DeviceAdminLocalSetup getInstance(Context mContext) {
		if (deviceAdmin == null) {
			deviceAdmin = new DeviceAdminLocalSetup();
			deviceAdmin.initDeviceAdmin(mContext);
		}
		return deviceAdmin;
	}

	public Intent buildAddDeviceAdminIntent() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceComponentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"Additional text explaining why this needs to be added.");
		return intent;
	}

	public void initDeviceAdmin(Context mContext) {
		mDPM = (DevicePolicyManager) mContext
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		mDeviceComponentName = new ComponentName(mContext,
				deviceAdminReceiver.class);
	}

	public boolean isDeviceAdminEnabled(Context mContext) {
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		return active;
	}

	public int getPasswordMinimumLength(Context mContext) {
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (active) {
			int len = mDPM.getPasswordMinimumLength(mDeviceComponentName);
			return len;
		}
		return 0;
	}

	public void setPasswordMinimumLength(Context mContext, int len) {

		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (active) {
			mDPM.setPasswordMinimumLength(mDeviceComponentName, len);
		}

	}

	public void enableDeviceAdmin(Context mContext) {
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (!active) {

			// cant start form service
			/*
			 * Intent intent = new Intent(
			 * DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			 * intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
			 * mDeviceComponentName);
			 * intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
			 * "Additional text explaining why this needs to be added.");
			 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * 
			 * mContext.startActivity(intent);
			 */

		}

	}

	public void disableDeviceAdmin() {
		mDPM.removeActiveAdmin(mDeviceComponentName);
	}

	public void forceLock() {
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (active) {
			mDPM.lockNow();

		}
	}

	public boolean updatePolicies(Context context) {
		// int aa = DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED;
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (active) {

			mDPM.setPasswordQuality(mDeviceComponentName, pwQuality);
			mDPM.setPasswordMinimumLength(mDeviceComponentName, pwLength);
			if (maxFailedPw > 0) {
				maxFailedPw++;
			}
			mDPM.setMaximumFailedPasswordsForWipe(mDeviceComponentName,
					maxFailedPw);
			

			try {
				if (timeMs > 0) {
					Settings.System.putInt(context.getContentResolver(),
							SCREEN_OFF_TIMEOUT, (int) timeMs);
				}
				// mDPM.setPasswordMinimumSymbols(mDeviceComponentName,minComplexNum);
				if (maxPINAgeInMs > 0) {
					// mDPM.setPasswordExpirationTimeout(mDeviceComponentName,maxPINAgeInMs);
				}
				// mDPM.setPasswordHistoryLength(mDeviceComponentName,
				// pinHistory);
			} catch (NoSuchMethodError e) {
			} catch (Exception e) {
			}

			return true;

		}
		return false;
	}

	public void checkPolicies(Context context) {
		boolean change = true;
		if (null != mDPM) {
			change = mDPM.isActivePasswordSufficient();
			if (false == change) {
				SendSetPsIntent(context);
			}
		}
	}

	public static void SendSetPsIntent(Context context) {
		if (null != context) {
			Intent ps = new Intent(context, ServiceDia.class);
			ps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ps.putExtra(ServiceDia.COMMAND, ServiceDia.SETPASSWORD);
			context.startActivity(ps);
		}
	}

	public void reSetPasswordPolicy(String password) {
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (active) {
			mDPM.resetPassword(password,
					DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
		}
	}

	public void wipeData() {
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (active) {

			mDPM.wipeData(0);
			Log.i(Util.TAG, "wipeData(0)");
		}
	}

	public void setMaximumTimeToLock() {
		boolean active = mDPM.isAdminActive(mDeviceComponentName);
		if (active) {
			mDPM.setMaximumTimeToLock(mDeviceComponentName, timeMs);
		}
	}

	public boolean isNumber(String str) {
		String strTemp = "0123456789 "; // 数字的匹配模式
		int i = 0;
		int j = 0;
		// String strTemp =
		// "abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ ";//字符的匹配模式
		if (str.equals(" "))
			return true;
		for (i = 0; i < str.length(); i++) {
			j = strTemp.indexOf(str.charAt(i));
			if (j == -1) {// 说明含有不是数字的字符
				return false;
			}
		}
		return true;
	}

	public boolean isAlphabetic(String str) {

		int i = 0;
		int j = 0;
		String strTemp = "abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ";// 字符的匹配模式
		if (str.equals(" "))
			return true;
		for (i = 0; i < str.length(); i++) {
			j = strTemp.indexOf(str.charAt(i));
			if (j == -1) {// 说明含有不是数字的字符
				return false;
			}
		}
		return true;
	}

	// public static void showPSSetActiviy(Activity activity, final int flag) {
	//
	// if (null != activity) {
	// //// ActivityManager am =
	// (ActivityManager)activity.getSystemService(Activity.ACTIVITY_SERVICE);
	// ////
	// //// int maxNum = 40;
	// // boolean show = true;
	// //// List<RunningTaskInfo> rtis = am.getRunningTasks(maxNum);
	// //// for (RunningTaskInfo info : rtis) {
	// //// String aname = info.baseActivity.getClassName();
	// //// if (true == aname.equals(TOPACTIVITYSIGN)) {
	// //// show = false;
	// //// break;
	// //// }
	// //// }
	// // if (show) {
	// //
	// // }
	// Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
	// activity.startActivityForResult(intent, flag);
	// }
	// }

	/*
	 * public void setPasswordExpirationTimeout(int days) { long timeMs = days *
	 * 86400 * 1000; boolean active = mDPM.isAdminActive(mDeviceComponentName);
	 * if (active) {
	 * 
	 * mDPM.setPasswordExpirationTimeout(mDeviceComponentName, timeMs); } }
	 * 
	 * public void setPasswordMinimumSymbols(int num) { boolean active =
	 * mDPM.isAdminActive(mDeviceComponentName); if (active) {
	 * 
	 * mDPM.setPasswordMinimumSymbols(mDeviceComponentName, num); } } public
	 * void setPasswordHistoryLength(int len) { boolean active =
	 * mDPM.isAdminActive(mDeviceComponentName); if (active) {
	 * 
	 * mDPM.setPasswordHistoryLength(mDeviceComponentName, len); } }
	 */
}
