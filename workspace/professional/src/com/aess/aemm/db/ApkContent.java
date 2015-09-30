package com.aess.aemm.db;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.aess.aemm.commonutils.CommUtils;

public class ApkContent extends AContent implements ApkTableColumns {

	public static final String[] CONTENT_PROJECTION = new String[] { _ID,
			APK_ID, APK_NAME, APK_URL, APK_DESC, APK_UID, APK_VERSION,
			APK_VERSION_CLIENT, APK_FLAG, APK_SCREEN, APK_ROW, APK_COLUMN,
			ICONCOLOR, ICONGREY, APK_FILE_NAME, APK_FILE_PATH,
			APK_PACKAGE_NAME, APK_DISABLED, INSTALLED_TIME, LAST_START_TIME,
			LAST_EXIT_TIME, PUBLISHED, IS_REMOVED, TRAFFIC, APPTYPE, SNAPSHOT,
			TYPENAME, SSOACCOUNT };

	public static final String[] CONTENT_ONLY_APK_ID = new String[] { _ID,
			APK_ID };

	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI
			+ "/" + ApkTable.APKTABLE);

	public ApkContent() {
		mBaseUri = CONTENT_URI;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(APK_ID, mApkId);
		values.put(APK_NAME, mApkName);
		values.put(APK_URL, mApkUrl);
		values.put(APK_DESC, mApkDesc);
		values.put(APK_UID, mUid);
		values.put(APK_VERSION, mApkVersion);
		values.put(APK_VERSION_CLIENT, mApkVersionClient);
		values.put(APK_FLAG, mApkFlag);
		values.put(APK_SCREEN, mApkScreen);
		values.put(APK_ROW, mApkRow);
		values.put(APK_COLUMN, mApkColumn);
		if (mIconColor != null) {
			values.put(ICONCOLOR, CommUtils.getImageData(mIconColor));
		}
		if (mIconGrey != null) {
			values.put(ICONGREY, CommUtils.getImageData(mIconGrey));
		}
		values.put(APK_FILE_NAME, mApkFileName);
		values.put(APK_FILE_PATH, mApkFilePath);
		values.put(APK_PACKAGE_NAME, mApkPackageName);
		values.put(APK_VERSION, mApkVersion);
		values.put(APK_VERSION_CLIENT, mApkVersionClient);
		values.put(APK_DISABLED, mApkDisabled);
		values.put(INSTALLED_TIME, mApkInstalledTime);
		values.put(LAST_START_TIME, mApkLastStartTime);
		values.put(LAST_EXIT_TIME, mApkLastExitTime);
		values.put(PUBLISHED, mApkPublished);
		values.put(IS_REMOVED, mApkIsRemoved);
		values.put(TRAFFIC, mApkTraffic);
		values.put(APPTYPE, mApkType);
		values.put(SNAPSHOT, mSnapShot);
		values.put(TYPENAME, mTypeName);
		values.put(SSOACCOUNT, mSSOAccount);
		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AContent> T restore(Cursor cursor) {
		mApkId = cursor.getString(CONTENT_APKID_COLUMN);
		mApkName = cursor.getString(CONTENT_APKNAME_COLUMN);
		mApkUrl = cursor.getString(CONTENT_APKURL_COLUMN);
		mApkDesc = cursor.getString(CONTENT_APKDESC_COLUMN);
		mUid = cursor.getInt(CONTENT_APKUID_COLUMN);
		mApkVersion = cursor.getString(CONTENT_APKVERSION_COLUMN);
		mApkFlag = cursor.getString(CONTENT_APKFLAG_COLUMN);
		mApkScreen = cursor.getInt(CONTENT_SCREENS_COLUMN);
		mApkRow = cursor.getInt(CONTENT_ROW_COLUMN);
		mApkColumn = cursor.getInt(CONTENT_COLUMN_COLUMN);
		mApkVersionClient = cursor.getString(CONTENT_APKVERSIONCLIENT_COLUMN);
		byte[] iconcolor = cursor.getBlob(CONTENT_ICONCOLOR_COLUMN);
		byte[] icongrey = cursor.getBlob(CONTENT_ICONGREY_COLUMN);
		if (iconcolor != null) {
			mIconColor = new String(iconcolor);
		}
		if (icongrey != null) {
			mIconGrey = new String(icongrey);
		}
		mApkFileName = cursor.getString(CONTENT_APKFILENAME_COLUMN);
		mApkFilePath = cursor.getString(CONTENT_APKFILEPATH_COLUMN);
		mApkPackageName = cursor.getString(CONTENT_APKPACKAGENAME_COLUMN);
		mApkDisabled = cursor.getInt(CONTENT_DISABLED_COLUMN);
		mApkInstalledTime = cursor.getString(CONTENT_INSTALLED_TIME_COLUMN);
		mApkLastStartTime = cursor.getString(CONTENT_LAST_START_TIME_COLUMN);
		mApkLastExitTime = cursor.getString(CONTENT_LAST_EXIT_TIME_COLUMN);
		mApkPublished = cursor.getInt(CONTENT_PUBLISHED_COLUMN);
		mApkIsRemoved = cursor.getInt(CONTENT_ISREMOVED_COLUMN);
		mApkTraffic = cursor.getLong(CONTENT_TRAFFIC_COLUMN);
		mApkType = cursor.getInt(CONTENT_APPTYPE_COLUMN);
		mSnapShot = cursor.getString(CONTENT_SNAPSHOT_COLUMN);
		mTypeName = cursor.getString(CONTENT_TYPENAME_COLUMN);
		mSSOAccount = cursor.getString(CONTENT_SSOACC_COLUMN);
		return (T) this;
	}

	public static ApkContent queryContentById(Context context, long id) {

		ApkContent content = null;
		Cursor c = null;

		Uri u = ContentUris.withAppendedId(CONTENT_URI, id);

		try {
			c = context.getContentResolver().query(u, CONTENT_PROJECTION, null,
					null, null);

			content = queryContentByCursor(c);
		} finally {
			if (null != c) {
				c.close();
			}
		}
		return content;
	}

	public static ApkContent queryContentBy_PKG_FLAG(Context context,
			String pkg, byte flag) {

		ApkContent content = null;
		Cursor c = null;

		try {
			c = context.getContentResolver().query(CONTENT_URI,
					CONTENT_PROJECTION,
					APK_PACKAGE_NAME + "=?" + " and " + APK_FLAG + "=?",
					new String[] { pkg, String.valueOf(flag) }, null);

			content = queryContentByCursor(c);
		} finally {
			if (null != c) {
				c.close();
			}
		}

		return content;
	}
	
	/**Envaluete
	 * @param context
	 * @return
	 */
	public static Cursor queryAppForEnvaluete(Context context){
		return context.getContentResolver().query(CONTENT_URI, new String[]{ _ID,
				APK_ID, APK_NAME, ICONCOLOR, TYPENAME, SNAPSHOT,APK_VERSION},
				null, null, TYPENAME+" DESC");
	}

	public static ArrayList<ApkContent> queryAllContents(Context context) {
		ArrayList<ApkContent> appList = new ArrayList<ApkContent>();
		Uri u = CONTENT_URI;
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(u, CONTENT_PROJECTION,
					null, null, null);

			if (cursor.moveToFirst()) {
				do {
					ApkContent data = getContent(cursor, ApkContent.class);
					appList.add(data);
				} while (cursor.moveToNext());
				return appList;
			} else {
				return null;
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	public static ArrayList<ApkContent> queryContentsByFlag(Context context,
			byte flag) {
		ArrayList<ApkContent> appList = new ArrayList<ApkContent>();
		String flagvalue = String.valueOf(flag);
		Cursor cursor = context.getContentResolver().query(CONTENT_URI,
				CONTENT_PROJECTION, APK_FLAG + "=?",
				new String[] { flagvalue }, null);
		if (cursor == null)
			return null;

		try {
			if (cursor.moveToFirst()) {
				do {
					ApkContent data = getContent(cursor, ApkContent.class);
					appList.add(data);
				} while (cursor.moveToNext());
				return appList;
			} else {
				return null;
			}
		} finally {
			cursor.close();
		}
	}

	public static int updateContentById(Context context, long id,
			ContentValues value) {
		int numofRows = 0;
		Uri u = ContentUris.withAppendedId(CONTENT_URI, id);
		numofRows = context.getContentResolver().update(u, value, null, null);
		return numofRows;
	}

	public int delete(Context context) {
		int numofRows = 0;
		long id = mId;
		Uri u = ContentUris.withAppendedId(CONTENT_URI, id);

		numofRows = context.getContentResolver().delete(u, null, null);
		return numofRows;
	}

	private static ApkContent queryContentByCursor(Cursor c) {
		if (null == c) {
			return null;
		}
		if (c.moveToFirst()) {
			return getContent(c, ApkContent.class);
		} else {
			return null;
		}
	}

	public String mApkId;
	public String mApkName;
	public String mApkUrl;
	public String mApkDesc;
	public int mUid;
	public String mApkVersion;
	public String mApkVersionClient;
	public String mApkFlag;
	public int mApkScreen;
	public int mApkRow;
	public int mApkColumn;
	public String mIconColor;
	public String mIconGrey;
	public String mApkFileName;
	public String mApkFilePath;
	public String mApkPackageName;
	public int mApkDisabled;
	public String mApkInstalledTime;
	public String mApkLastStartTime;
	public String mApkLastExitTime;
	public int mApkPublished;
	public int mApkIsRemoved;
	public long mApkTraffic;
	public int mApkType;
	public String mSnapShot;
	public String mTypeName;
	public String mSSOAccount;

	public boolean mIsDeal = false;
}
