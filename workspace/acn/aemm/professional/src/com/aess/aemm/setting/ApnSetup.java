package com.aess.aemm.setting;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ApnSetup {
	public static final String TAG = "ApnSetup";
	public static final Uri CONTENT_URI = Uri
			.parse("content://telephony/carriers");
	public static final String _ID = "_id";
	public static final String APN_NAME = "name";
	public static final String APN_APN = "apn";

	public static final String APN_USER = "user";
	public static final String APN_PASS = "password";

	public static final String APN_PROXY = "proxy";
	public static final String APN_PROXYPORT = "port";

	public static final String APN_NUMERIC = "numeric";
	public static final String APN_MCC = "mcc";
	public static final String APN_MNC = "mnc";

	// public static final String APN_TYPE = "type";
	public static final String APN_CURRENT = "current";

	public static final String[] APN_LINE = new String[] { APN_NAME, APN_APN,
			APN_USER, APN_PASS, APN_PROXY, APN_PROXYPORT, APN_MCC, APN_MNC,
			APN_NUMERIC, APN_CURRENT };

	public static ContentValues systemHaveSameConfig(Context context, String apnname) {

		ContentValues value = null;
		if (null != apnname) {
			List<ContentValues> apnlist = ApnSetup.getSysApnList(context,
					apnname);
			if (null != apnlist && apnlist.isEmpty() == false) {
				value = apnlist.get(0);
			}
		}
		return value;
	}

	public ContentValues toContentValues(String apnName, String apnApn,
			String user, String apnPassword, String proxy, String proxyPort) {

		ContentValues values = new ContentValues();

		values.put(APN_NAME, apnName);
		values.put(APN_APN, apnApn);
		values.put(APN_USER, user);
		values.put(APN_PASS, apnPassword);
		values.put(APN_PROXY, proxy);
		values.put(APN_PROXYPORT, proxyPort);
		values.put(APN_CURRENT, 1);

		return values;
	}

	public int addApn(Context context, ContentValues values) {
		int rowId = -1;
		if (null != context && null != values) {
			Log.i(TAG, "addApn");
			if (setMccAndMnc(context, values) > 0) {

				Uri newRow = context.getContentResolver().insert(CONTENT_URI,
						values);
				String sId = newRow.getLastPathSegment();
				rowId = Integer.valueOf(sId);
			}
		}
		return rowId;
	}

	public int editApn(Context context, ContentValues apnvalues,
			ContentValues values) {
		int rowId = -1;
		if (null != context && null != apnvalues && null != values) {
			Log.i(TAG, "editApn");
			if (setMccAndMnc(context, values) > 0) {
				
				String apn = values.getAsString(APN_NAME);

				String where = String.format("%s == ?", APN_NAME);
				
				apnvalues = values;

				rowId = context.getContentResolver().update(CONTENT_URI,
						apnvalues, where, new String[] { apn });
			}
		}
		return rowId;
	}

	public static List<ContentValues> getSysApnList(Context context,
			String apnname) {

		ArrayList<ContentValues> list = null;
		if (null != context) {
			if (apnname != null && apnname.length() > 0) {

				String where = String.format("%s == ?", APN_NAME);

				Cursor cursor = context.getContentResolver().query(CONTENT_URI,
						APN_LINE, where, new String[] { apnname }, null);

				try {
					if (cursor.getCount() > 0) {
						list = new ArrayList<ContentValues>();
						if (cursor.moveToFirst()) {
							do {

								ContentValues value = cursorToContentValues(cursor);
								if (null != value) {
									list.add(value);
								}

							} while (cursor.moveToNext());
						}
					}
				} finally {
					cursor.close();
				}
			}
		}
		return list;
	}

	private static ContentValues cursorToContentValues(Cursor cursor) {
		ContentValues value = null;
		if (null != cursor) {

			int index = cursor.getColumnIndex(APN_NAME);
			String name = cursor.getString(index);
			index = cursor.getColumnIndex(APN_APN);
			String apn = cursor.getString(index);
			index = cursor.getColumnIndex(APN_USER);
			String user = cursor.getString(index);
			index = cursor.getColumnIndex(APN_PASS);
			String psword = cursor.getString(index);
			index = cursor.getColumnIndex(APN_PROXY);
			String proxy = cursor.getString(index);
			index = cursor.getColumnIndex(APN_PROXYPORT);
			String port = cursor.getString(index);
			index = cursor.getColumnIndex(APN_NUMERIC);
			String numeric = cursor.getString(index);
			index = cursor.getColumnIndex(APN_MCC);
			String mcc = cursor.getString(index);
			index = cursor.getColumnIndex(APN_MNC);
			String mnc = cursor.getString(index);
			index = cursor.getColumnIndex(APN_CURRENT);
			String current = cursor.getString(index);

			value = new ContentValues();

			value.put(APN_NAME, name);
			value.put(APN_APN, apn);
			value.put(APN_USER, user);
			value.put(APN_PASS, psword);
			value.put(APN_PROXY, proxy);
			value.put(APN_PROXYPORT, port);
			value.put(APN_CURRENT, current);
			value.put(APN_NUMERIC, numeric);
			value.put(APN_MCC, mcc);
			value.put(APN_MNC, mnc);
		}
		return value;
	}

	public static int deleteApnSetting(Context context, int rowId) {
		Log.i(TAG, "deleteApnSetting");
		Uri u = ContentUris.withAppendedId(CONTENT_URI, rowId);
		int num = context.getContentResolver().delete(u, null, null);
		return num;
	}
	
	private int setMccAndMnc(Context context, ContentValues values) {
		int rlt = -1;
		String imei = null;
		String mcc = null;
		String mnc = null;
		TelephonyManager telephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (null != telephonyMgr && null != values) {
			imei = telephonyMgr.getSubscriberId();
			
			// String Imei2 = telephonyMgr.getSubscriberId();
			// imei = SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC);
			// MCC is first 3 chars and then in 2 - 3 chars of MNC
			if (imei != null && imei.length() > 4) {
				imei = imei.substring(0, 5);
				// Country code
				mcc = imei.substring(0, 3);
				// Network code
				mnc = imei.substring(3, 5);
				
				values.put(APN_NUMERIC, imei);
				values.put(APN_MCC, mcc);
				values.put(APN_MNC, mnc);
				rlt = 1;
			}
		}
		return rlt;
	}
}