package com.aess.aemm.setting;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;
import com.aess.aemm.receiver.AemmDeviceAdminReceiver;
import com.aess.aemm.view.ViewUtils;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class PwPolicySetup {
	public static final int RESET_PASSWORD_REQUIRE_ENTRY = 1;
	public static final String TAG = "PwPolicySetup";
	public static final int TIME = (86400 * 1000);
//	public static final int TIME = 60 * 1000;

	public static PwPolicySetup getInstance(Context cxt) {
		if (pwsetup == null) {
			pwsetup = new PwPolicySetup(cxt);
		}
		return pwsetup;
	}

	public static boolean isNumber(String info) {
		boolean alt = true;
		for (char x : info.toCharArray()) {
			if (!Character.isDigit(x)) {
				alt = false;
				break;
			}
		}
		return alt;
	}

	public static int clearDevicePs(Context context) {
		Log.i(TAG, "clearDevicePs");
		int ret = 0;
		if (null != context) {
			PwPolicySetup da = PwPolicySetup.getInstance(context);
			ret = da.clearPasswordPolicy(context);
		}
		return ret;
	}
	
//	public int testfun() {
//		long x = mDPM.getPasswordExpiration(admin);
//		Log.d(TAG, x);
//		return 0;
//	}

	public static int lockDevice(Context context, String password, boolean info) {
		Log.i(TAG, "lockDevice");
		int ret = 0;
		boolean result = false;
		if (null != context && null != password) {
			PwPolicySetup da = PwPolicySetup.getInstance(context);
			if (null != password && password.length() > 0) {
				result = da.setPassword(password);
			} else {
				result = true;
	
			}
			if (true == result) {
				ret = 1;
			}
			if (result && info) {
				ViewUtils.update(context,
						com.aess.aemm.view.data.MsgType.CLIENT_LOCK, null);
			}
		}
		return ret;
	}

	public int setDeviceAdminValues(boolean allowSimple, boolean forcePs,
			int maxFailedAttempts, int maxGracePeriod, int maxInactivity,
			int minComplexChars, int minLength, boolean requireAlphanumeric,
			int maxPINAgeInDays, int pinHistory) {

		pwQuality = DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED;
		pwLength = minLength;
		maxFailedPw = maxFailedAttempts;
		timeMs = 60 * 1000 * maxInactivity;
		miComplexNum = minComplexChars;
		mxHistory = pinHistory;
		mxPINAgeInDays = maxPINAgeInDays * TIME;

		if (maxFailedPw > 0) {
			maxFailedPw++;
		}
		
		if (pwLength > 0) {
			pwQuality = DevicePolicyManager.PASSWORD_QUALITY_NUMERIC;
		}
		if (forcePs) {
			pwQuality = DevicePolicyManager.PASSWORD_QUALITY_NUMERIC;
		}
		if (requireAlphanumeric) {
			pwQuality = DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC;
		}
		if (miComplexNum > 0) {
			if (android.os.Build.VERSION.SDK_INT < 11) {
				pwQuality = DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC;
			} else {
				pwQuality = DevicePolicyManager.PASSWORD_QUALITY_COMPLEX;
			}
		}
		return pwQuality;
	}

	public void resetPolicy() {
		if (isDeviceAdminEnabled() == true) {
			mDPM.setPasswordQuality(admin,
					DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
			mDPM.setPasswordMinimumLength(admin, 0);
		}
	}

	public boolean isDeviceAdminEnabled() {
		return mDPM.isAdminActive(admin);
	}

	public int getPasswordMinLength() {
		boolean active = mDPM.isAdminActive(admin);
		if (active) {
			return mDPM.getPasswordMinimumLength(admin);
		}
		return 0;
	}

	public void forceLock() {
		boolean active = mDPM.isAdminActive(admin);
		if (active) {
			mDPM.lockNow();
		}
	}

	public boolean updatePolicies(Context context) {
		boolean active = mDPM.isAdminActive(admin);
		if (active) {
			Log.d(TAG, "updatePolicies");
			mDPM.setPasswordQuality(admin, pwQuality);
			try {
				if (android.os.Build.VERSION.SDK_INT >= 11) {
					mDPM.setPasswordMinimumSymbols(admin, miComplexNum);
					
					mDPM.setPasswordHistoryLength(admin, mxHistory);
					
					mDPM.setPasswordExpirationTimeout(admin, mxPINAgeInDays);
				}
			} catch (Exception e) {
				
			}
			
			mDPM.setPasswordMinimumLength(admin, pwLength);
			
			mDPM.setMaximumFailedPasswordsForWipe(admin, maxFailedPw);

			try {
				if (timeMs > 0) {
					Settings.System.putInt(context.getContentResolver(),
							SCREEN_OFF_TIMEOUT, (int) timeMs);
				}
			} catch (Exception e) {
			}

			return true;
		}
		return false;
	}

	public int clearPasswordPolicy(Context context) {
		int ret = 0;
		boolean active = mDPM.isAdminActive(admin);
		if (active) {
			int quality = mDPM.getPasswordQuality(admin);

			boolean value = false;
			int update = 0;
			if (DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED != quality) {
				update = getDefaultSet();
			} else {
				ret = 1;
			}
						
			value = clearPassword();
			if (ret == 1) {
				value = false;
			}
			
			if (update > 0) {
				if (DevicePolicyManager.PASSWORD_QUALITY_SOMETHING == quality) {
					quality = DevicePolicyManager.PASSWORD_QUALITY_NUMERIC;
				}
				value = value & updatePolicies(context);
			}
			if (value) {
				PwPolicyProfile.sendIntentPsAction(context);
				ret = 2;
			}
		}
		return ret;
	}
	
	private int getDefaultSet() {
		if (null != mDPM) {
			Log.d(TAG, "getDefaultSet");
			pwQuality = mDPM.getPasswordQuality(admin);
			
			pwLength = mDPM.getPasswordMinimumLength(admin);

			maxFailedPw = mDPM.getMaximumFailedPasswordsForWipe(admin);
			
			try {
				if (android.os.Build.VERSION.SDK_INT >= 11) {
					miComplexNum = mDPM.getPasswordMinimumSymbols(admin);
					mxHistory = mDPM.getPasswordHistoryLength(admin);
					mxPINAgeInDays = mDPM.getPasswordExpirationTimeout(admin);
				} else {
					miComplexNum = 0;
					mxHistory = 0;
					mxPINAgeInDays = 0;
				}
			} catch (Exception e) {
			}
			timeMs = 0;
		}
		return 1;
	}

	private boolean clearPassword() {
		boolean rlt = false;
		boolean active = mDPM.isAdminActive(admin);
		if (active) {
			mDPM.setPasswordQuality(admin, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
			mDPM.setPasswordMinimumLength(admin, 0);
			rlt = setPassword("");
		}
		return rlt;
	}

	public boolean setPassword(String password) {
		boolean rlt = false;
		boolean active = mDPM.isAdminActive(admin);
		if (active) {
			try {
				rlt = mDPM.resetPassword(password, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
			} catch (Exception e) {
				rlt = false;
			}

			if (!rlt) {
				Log.w(TAG, "Password set error!");
			}
		}
		return rlt;
	}

	public void wipeData() {
		Log.i(TAG, "wipeData");
		boolean active = mDPM.isAdminActive(admin);
		if (active) {
			mDPM.wipeData(0);
		}
	}

	private PwPolicySetup(Context cxt) {
		mDPM = (DevicePolicyManager) cxt
				.getSystemService(Context.DEVICE_POLICY_SERVICE);

		admin = new ComponentName(cxt,
				AemmDeviceAdminReceiver.class);
	}

	private static PwPolicySetup pwsetup = null;
	private DevicePolicyManager mDPM = null;
	private ComponentName admin = null;
	private int pwQuality = 0;
	private int pwLength = 0;
	private int maxFailedPw = 0;
	private int miComplexNum = 0;
	private int mxHistory = 0;
	private long mxPINAgeInDays;
	private long timeMs = 0;
//	private Context context = null;
	// public void setPasswordMinimumLength(Context mContext, int len) {
	// boolean active = mDPM.isAdminActive(mComponentName);
	// if (active) {
	// mDPM.setPasswordMinimumLength(mComponentName, len);
	// }
	// }

	// public void enableDeviceAdmin(Context mContext) {
	// boolean active = mDPM.isAdminActive(mComponentName);
	// if (!active) {
	//
	// }
	// }

	// public void disableDeviceAdmin() {
	// mDPM.removeActiveAdmin(mComponentName);
	// }
	
	// public void setMaximumTimeToLock() {
	// boolean active = mDPM.isAdminActive(mComponentName);
	// if (active) {
	// mDPM.setMaximumTimeToLock(mComponentName, timeMs);
	// }
	// }

	// public static boolean isNumber(String str) {
	// String strTemp = "0123456789"; // ���ֵ�ƥ��ģʽ
	// int i = 0;
	// int j = 0;
	// for (i = 0; i < str.length(); i++) {
	// j = strTemp.indexOf(str.charAt(i));
	// if (j == -1) { // ˵�����в������ֵ��ַ�
	// return false;
	// }
	// }
	// return true;
	// }
	//
	// public static boolean isAlphabetic(String str) {
	//
	// int i = 0;
	// int j = 0;
	// String strTemp = "abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ";
	// // �ַ��ƥ��ģʽ
	// if (str.equals(" "))
	// return true;
	// for (i = 0; i < str.length(); i++) {
	// j = strTemp.indexOf(str.charAt(i));
	// if (j == -1) {// ˵�����в������ֵ��ַ�
	// return false;
	// }
	// }
	// return true;
	// }
	
	// show activity input password
	// public void inputPassword(Context context) {
	// boolean change = true;
	//
	// change = mDPM.isActivePasswordSufficient();
	// if (false == change) {
	// if (null != Main.mHall && null != Main.mHall.handler) {
	// Main.mHall.handler.sendEmptyMessage(Main.mHall.SET_PASSWORD);
	// } else {
	// Intent ps = new Intent(context, ServiceDia.class);
	// ps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// ps.putExtra(ServiceDia.COMMAND, ServiceDia.SETPASSWORD);
	// context.startActivity(ps);
	// }
	// }
	// }
	

	// public void unforceLock() {
	// boolean active = mDPM.isAdminActive(mComponentName);
	// if (active) {
	// mDPM.lockNow();
	// }
	// }
}
