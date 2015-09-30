package com.act.mbanking.manager;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.act.mbanking.receiver.DeviceAdminMyBankingReceiver;

public class DeviceAdminManager {
	public static final String TAG = DeviceAdminManager.class.getSimpleName();
	static final int REQUEST_CODE_ENABLE_ADMIN = 1;
	static final int REQUEST_CODE_START_ENCRYPTION = 2;

	Context cxt;
	DevicePolicyManager mDPM;
	ActivityManager mAM;
	ComponentName mDeviceAdminSample;

	// Password quality spinner choices
	// This list must match the list found in
	// samples/ApiDemos/res/values/arrays.xml
	final static int mPasswordQualityValues[] = new int[] {
			DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED,
			DevicePolicyManager.PASSWORD_QUALITY_SOMETHING,
			DevicePolicyManager.PASSWORD_QUALITY_NUMERIC,
			DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC,
			DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC,
			DevicePolicyManager.PASSWORD_QUALITY_COMPLEX };
	private int CUR_PASSWORD_QUALITY = DevicePolicyManager.PASSWORD_QUALITY_SOMETHING;

	public DeviceAdminManager(Context context) {
		cxt = context;
		mDPM = (DevicePolicyManager) cxt
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mAM = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		mDeviceAdminSample = new ComponentName(cxt,
				DeviceAdminMyBankingReceiver.class);
	}

	public void openSetPsw(Activity act) {
		Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
		act.startActivity(intent);
	}

	public void enable(Activity act) {
		// Launch the activity to have the user enable our admin.
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"Additional text explaining why this needs to be added.");
		act.startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
	}

	private void disable() {
		mDPM.removeActiveAdmin(mDeviceAdminSample);
	}

	public boolean isActivity() {
		return mDPM.isAdminActive(mDeviceAdminSample);
	}
	
	public boolean resetPwd(String pwd){
		 return mDPM.resetPassword(pwd,
                 DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
	}

	public boolean isActivePasswordSufficient() {
		return mDPM.isActivePasswordSufficient();
	}

	public void checkPasswordQuality() {
		int curPwsQuality = mDPM.getPasswordQuality(mDeviceAdminSample);
		if (curPwsQuality == DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED) {
			mDPM.setPasswordQuality(mDeviceAdminSample, CUR_PASSWORD_QUALITY);
		}
	}

	private String statusCodeToString(int newStatusCode) {
		String newStatus = "unknown";
		switch (newStatusCode) {
		case DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED:
			newStatus = "unsupported";
			break;
		case DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE:
			newStatus = "inactive";
			break;
		case DevicePolicyManager.ENCRYPTION_STATUS_ACTIVATING:
			newStatus = "activating";
			break;
		case DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE:
			newStatus = "active";
			break;
		}
		return newStatus;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_ENABLE_ADMIN:
			if (resultCode == Activity.RESULT_OK) {
				Log.i(TAG, "Admin enabled!");
			} else {
				Log.i(TAG, "Admin enable FAILED!");
			}
			return;
		case REQUEST_CODE_START_ENCRYPTION:
			return;
		}

	}
}
