package com.aess.aemm.setting;

import java.util.ArrayList;

import com.aess.aemm.db.SettingsContent;



import android.content.Context;
import android.util.Log;

class ExchangeProfile extends Profile {
	public static final String TAG = "ExchangeProfile";

	public static String node_value_emailAddress = "EmailAddress";
	public static String node_value_host = "Host";
	public static String node_value_mailnumofpdts = "MailNumberOfPastDaysToSync";
	public static String node_value_password = "Password";
	public static String node_value_ssl = "SSL";
	public static String node_value_userName = "UserName";

	// current filter after recover
	public static final int SYNC_WINDOW_1_DAY = 1;
	public static final int SYNC_WINDOW_3_DAYS = 2;
	public static final int SYNC_WINDOW_1_WEEK = 3;
	public static final int SYNC_WINDOW_2_WEEKS = 4;
	public static final int SYNC_WINDOW_1_MONTH = 5;
	public static final int SYNC_WINDOW_ALL = 6;

	// original filter
	public static final int ORIGINAL_SYNC_WINDOW_1_DAY = 1;
	public static final int ORIGINAL_SYNC_WINDOW_3_DAYS = 3;
	public static final int ORIGINAL_SYNC_WINDOW_1_WEEK = 7;
	public static final int ORIGINAL_SYNC_WINDOW_2_WEEKS = 14;
	public static final int ORIGINAL_SYNC_WINDOW_1_MONTH = 31;
	public static final int ORIGINAL_SYNC_WINDOW_ALL = 0;

	public static class ExchangeArg {
		public String address = null;
		public String password = null;
		public String host = null;
		public String userName = null;
		public int syncday = 0;
		public boolean bSSL;
	}

	public int setValue(String keyValue, String value) {
		Log.i(TAG, String.format("%s = %s", keyValue, value));

		if (keyValue.equals(node_value_emailAddress)) {
			exarg.address = value;
		} else if (keyValue.equals(node_value_host)) {
			exarg.host = value;
		} else if (keyValue.equals(node_value_password)) {
			exarg.password = value;
		} else if (keyValue.equals(node_value_userName)) {
			exarg.userName = value;
		} else if (keyValue.equals(node_value_ssl)) {
			exarg.bSSL = value.equals("true") ? true : false;
		}else if (keyValue.equals(node_value_mailnumofpdts)) {
			exarg.syncday = getSyncDaysById(Integer.valueOf(value).intValue());
		} else {
			super.setValue(keyValue, value);
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");
		int ret = -1;

		clearProfile(context);
		
		if (null != exarg.address) {

			if (exarg.userName == null) {
				exarg.userName = exarg.address;
			}
			
			ExchangeSetup es = new ExchangeSetup();

			es.deleteExchangeAccount(context, exarg.address);
			
			es.newAccount(context, exarg);
			
			ret = es.addExchangeAccount(context);
			
			if (ret >= 0) {
				String pType = ProfileType.Profile_Exchange.toString();

				SettingsContent pc = dbHaveSameConfig(context, pType,
						exarg.address);
				
				if (pc == null) {
					pc = new SettingsContent();
					pc.setProfileArg(exarg.address, uuid, -1, version, pType);
					pc.add(context);
				} else {
					pc.setProfileArg(null, null, -1, version, pType);
					pc.update(context);
				}
			}
		}
		return ret;
	}

	@Override
	public int clearProfile(Context context) {
		Log.i(TAG, "clearProfile");

		String ver = null;
		if (null != version) {
			ver = version;
		} else {
			// clean all
			ver = "-1";
		}

		String type = ProfileType.Profile_Exchange.toString();
		ArrayList<SettingsContent> list = SettingsContent.queryContentBy_Type_Version(context, type, ver);

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				SettingsContent wp = list.get(i);
				if (wp != null) {

					ExchangeSetup setUp = new ExchangeSetup();
					if (setUp.deleteExchangeAccount(context, wp.getName()) == true) {
						SettingsContent.delContentById(context, wp.mId);
					}
				}
			}
		}
		return 0;
	}

	private int getSyncDaysById(int id) {
		int filter = ExchangeProfile.SYNC_WINDOW_3_DAYS;
		switch (id) {
		case ExchangeProfile.ORIGINAL_SYNC_WINDOW_ALL: {
			filter = ExchangeProfile.SYNC_WINDOW_ALL;
			break;
		}
		case ExchangeProfile.ORIGINAL_SYNC_WINDOW_1_DAY: {
			filter = ExchangeProfile.SYNC_WINDOW_1_DAY;
			break;
		}
		case ExchangeProfile.ORIGINAL_SYNC_WINDOW_3_DAYS: {
			filter = ExchangeProfile.SYNC_WINDOW_3_DAYS;
			break;
		}
		case ExchangeProfile.ORIGINAL_SYNC_WINDOW_1_WEEK: {
			filter = ExchangeProfile.SYNC_WINDOW_1_WEEK;
			break;
		}
		case ExchangeProfile.ORIGINAL_SYNC_WINDOW_2_WEEKS: {
			filter = ExchangeProfile.SYNC_WINDOW_2_WEEKS;
			break;
		}
		case ExchangeProfile.ORIGINAL_SYNC_WINDOW_1_MONTH: {
			filter = ExchangeProfile.SYNC_WINDOW_1_MONTH;
			break;
		}
		}
		return filter;
	}

	private static SettingsContent dbHaveSameConfig(Context context,
			String ptype, String name) {
		SettingsContent value = null;

		value = SettingsContent.queryContentBy_Type_Name(context, ptype, name);

		return value;
	}

	private ExchangeArg exarg = new ExchangeArg();
}
