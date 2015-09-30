package com.aess.aemm.setting;

import java.util.ArrayList;

import com.aess.aemm.db.SettingsContent;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

class ApnProfile extends Profile {
	public static final String TAG = "ApnProfile";

	public static final String node_value_apn = "apn";
	public static final String node_value_username = "username";
	public static final String node_value_password = "password";
	public static final String node_value_proxy = "proxy";
	public static final String node_value_proxyPort = "proxyPort";
	public static final String node_value_DefaultsDomainName = "DefaultsDomainName";

	public int setValue(String key, String value) {
		Log.i(TAG, String.format("%s = %s", key, value));

		if (key.equals(node_value_apn)) {
			apnApn = value;
		} else if (key.equals(node_value_username)) {
			userName = value;
		} else if (key.equals(node_value_password)) {
			password = value;
		} else if (key.equals(node_value_proxy)) {
			proxy = value;
		} else if (key.equals(node_value_proxyPort)) {
			proxyPort = value;
		} else if (key.equals(node_value_DefaultsDomainName)) {
			// defaultsDomainName = value;
		} else {
			super.setValue(key, value);
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");

		int ret = -1;

		clearProfile(context);
		
		if (apnName == null) {
			apnName = apnApn;
		}

		ApnSetup apnsetup = new ApnSetup();
		ContentValues cValues = apnsetup.toContentValues(this.apnName,
				this.apnApn, this.userName, this.password, this.proxy,
				this.proxyPort);

		int cid = -1;
		ContentValues oldvaules = ApnSetup.systemHaveSameConfig(context, apnName);
		if (null == oldvaules) {
			cid = apnsetup.addApn(context, cValues);
		} else {
			cid = apnsetup.editApn(context, oldvaules, cValues);
		}

		String type = ProfileType.Profile_APN.toString();

		SettingsContent oldprofile = dbHaveSameConfig(context, type, version);
		if (cid >= 0) {
			if (null == oldprofile) {
				SettingsContent pc = new SettingsContent();
				pc.setProfileArg(apnName, uuid, cid, version, type);
				pc.add(context);
				ret = 1;
			} else {
				oldprofile.setProfileArg(apnName, uuid, cid, version, type);
				oldprofile.update(context);
				ret = 1;
			}
		}
		apnApn = null;
		apnName = null;
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

		String type = ProfileType.Profile_APN.toString();

		ArrayList<SettingsContent> dblist = SettingsContent.queryContentBy_Type_Version(
				context, type, ver);

		for (SettingsContent profile : dblist) {
			int cid = Integer.valueOf(profile.getCid());
			String oldname = profile.getName();

			boolean delete = true;
			if (apnName != null && apnName.equals(oldname)) {
				delete = false;
			}

			if (delete) {
				// delete system set
				ApnSetup.deleteApnSetting(context, cid);

				// delete DB record
				SettingsContent.delContentById(context, profile.mId);
			}
		}
		return 0;
	}

	private static SettingsContent dbHaveSameConfig(Context context, String ptype, String pversion) {
		SettingsContent value = null;
		ArrayList<SettingsContent> dblist = SettingsContent.queryContentBy_Type_Version(
				context, ptype, pversion);
		if (dblist != null && dblist.isEmpty() == false) {
			value = dblist.get(0);
		}
		return value;
	}
	
	private String apnApn = null;
	private String apnName = null;
	private String userName = null;
	private String password = null;
	private String proxy = null;
	private String proxyPort = null;
	// private String apnType = null;
	// private String defaultsDomainName = null;
}
