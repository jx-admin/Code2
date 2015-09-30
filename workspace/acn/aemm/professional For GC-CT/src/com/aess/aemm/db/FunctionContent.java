package com.aess.aemm.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class FunctionContent extends AContent implements FunctionTableColumns {
	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI
			+ "/" + FunctionTable.FUNCTION);

	public static final String[] CONTENT_PROJECTION = new String[] { ID,
			FUNCTION_ID, FUNCTION_NAME, FUNCTION_VERSION, FUNCTION_DISABLED,
			FUNCTION_INSTALLED_TIME, FUNCTION_LAST_START_TIME,
			FUNCTION_LAST_EXIT_TIME };

	public FunctionContent() {
		mBaseUri = CONTENT_URI;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(FUNCTION_ID, mFunctionId);
		values.put(FUNCTION_NAME, mFunctionName);
		values.put(FUNCTION_VERSION, mFunctionVersion);
		values.put(FUNCTION_DISABLED, mFunctionDisabled);
		values.put(FUNCTION_INSTALLED_TIME, mFunctionInstalledTime);
		values.put(FUNCTION_LAST_START_TIME, mFunctionLastStartTime);
		values.put(FUNCTION_LAST_EXIT_TIME, mFunctionLastExitTime);
		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AContent> T restore(Cursor cursor) {
		mFunctionId = cursor.getString(CONTENT_IDENTIFIER_COLUMN);
		mFunctionName = cursor.getString(CONTENT_DISPLAYNAME_COLUMN);
		mFunctionVersion = cursor.getString(CONTENT_VERSION_COLUMN);
		mFunctionDisabled = cursor.getInt(CONTENT_DISABLED_COLUMN);
		mFunctionInstalledTime = cursor.getString(CONTENT_INSTALLED_TIME);
		mFunctionLastStartTime = cursor.getString(CONTENT_LAST_START_TIME);
		mFunctionLastExitTime = cursor.getString(CONTENT_LAST_EXIT_TIME);
		return (T) this;
	}

	public static FunctionContent queryContentBy_PKG_Version(Context context,
			String pkg, String version) {

		Cursor c = null;
		FunctionContent fc = null;
		try {
			c = context.getContentResolver().query(CONTENT_URI,
					CONTENT_PROJECTION,
					FUNCTION_ID + "=?" + " and " + FUNCTION_VERSION + "=?",
					new String[] { pkg, version }, null);
			fc = queryContentByCursor(c);
		} finally {
			c.close();
		}

		return fc;
	}

	public static int deleteFuncContentwithId(Context context, long id) {
		int numofRows = 0;
		Uri u = ContentUris.withAppendedId(CONTENT_URI, id);

		numofRows = context.getContentResolver().delete(u, null, null);
		return numofRows;
	}

	private static FunctionContent queryContentByCursor(Cursor cursor) {
		if (cursor.moveToFirst()) {
			return getContent(cursor, FunctionContent.class);
		} else {
			return null;
		}
	}

	public String mFunctionId;
	public String mFunctionName;
	public String mFunctionVersion;
	public int mFunctionDisabled;
	public String mFunctionInstalledTime;
	public String mFunctionLastStartTime;
	public String mFunctionLastExitTime;
}
