package com.aess.aemm.setting;

import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.commonutils.HelpUtils;
import com.aess.aemm.view.MainView;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;

public class PwPolicyProfile extends Profile {

	public static final String TAG = "PwPolicyProfile";
	public static final String node_value_allowSimple = "allowSimple";
	public static final String node_value_forcePIN = "forcePIN";
	public static final String node_value_maxFailedAttempts = "maxFailedAttempts";
	public static final String node_value_maxGracePeriod = "maxGracePeriod";
	public static final String node_value_maxInactivity = "maxInactivity";
	public static final String node_value_minComplexChars = "minComplexChars";
	public static final String node_value_minLength = "minLength";
	public static final String node_value_requireAlphanumeric = "requireAlphanumeric";
	public static final String node_value_maxPINAgeInDays = "maxPINAgeInDays";
	public static final String node_value_pinHistory = "pinHistory";
	
	private boolean allowSimple;
	private boolean requireAlphanumeric;
	private boolean forecPs;

	private int pwLength;
	private int mxFailTimes;
	private int mxTimeOut;
	private int mxGracePeriod;
	private int miComplexNum;
	private int mxPINAgeInDays;
	private int mxHistory;

	public int setValue(String key, String value) {
		Log.i(TAG, String.format("%s = %s", key, value));

		if (key.equals(node_value_allowSimple)) {
			allowSimple = value.equals("true") ? true : false;
		} else if (key.equals(node_value_forcePIN)) {
			forecPs = value.equals("true") ? true : false;
		} else if (key.equals(node_value_maxFailedAttempts)) {
			mxFailTimes = Integer.valueOf(value);
		} else if (key.equals(node_value_maxGracePeriod)) {
			mxGracePeriod = Integer.valueOf(value);
		} else if (key.equals(node_value_maxInactivity)) {
			mxTimeOut = Integer.valueOf(value);
		} else if (key.equals(node_value_requireAlphanumeric)) {
			requireAlphanumeric = value.equals("true") ? true : false;
		} else if (key.equals(node_value_minLength)) {
			pwLength = Integer.valueOf(value);
		} else if (key.equals(node_value_minComplexChars)) {
			miComplexNum = Integer.valueOf(value);
		} else if (key.equals(node_value_maxPINAgeInDays)) {
			mxPINAgeInDays = Integer.valueOf(value);
		} else if (key.equals(node_value_pinHistory)) {
			mxHistory = Integer.valueOf(value);
		}

		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");
		int rlt = -1;
		
		PwPolicySetup pwpSetup = PwPolicySetup.getInstance(context);
		pwpSetup.setDeviceAdminValues(allowSimple, forecPs, mxFailTimes,
				mxGracePeriod, mxTimeOut, miComplexNum, pwLength,
				requireAlphanumeric, mxPINAgeInDays, mxHistory);

		if (pwpSetup.updatePolicies(context)) {
			checkPolicies(context);
			rlt = 1;
		}
		return rlt;
	}
	
	public static void sendIntentPsAction(Context context) {
		int ret = 0;
		if (null != context) {
			CommUtils.setPasswordRequire(context, 1);
			if (null != MainView.mHall && null != MainView.mHall.handler) {
				String name = HelpUtils.getTopActivityPackageName(context);
				if (!HelpUtils.PSACTIVE.equals(name)) {
					if (HelpUtils.AEMMACTIVE.equals(name)) {
						ret = 1;
						MainView.mHall.handler.sendEmptyMessage(MainView.SET_PASSWORD);
					} else {
						MainView.startHall(context);
						ret = 1;
					}
				}
			} else {
				MainView.startHall(context);
				ret = 1;
			}
			if (1 == ret) {
				Log.d(TAG, "sendIntentPsAction");
			}
		}
	}
	
	public void checkPolicies(Context context) {
		boolean change = true;
		DevicePolicyManager mDPM = (DevicePolicyManager) context
		.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		if (null != mDPM) {
			change = mDPM.isActivePasswordSufficient();
			if (false == change) {
				sendIntentPsAction(context);
			}
		}
	}

	@Override
	public int clearProfile(Context context) {
		Log.i(TAG, "clearProfile");
		PwPolicySetup.getInstance(context).resetPolicy();
		return 0;
	}
}
