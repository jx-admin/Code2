package com.xuye.ThridPartyApp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ThirdPartyProvider2 extends ContentProvider {
	public static final String TAG = "ThirdPartyProvider";
    public static final String EMAIL_AUTHORITY = "com.xuye.ThridPartyService.ThirdPartyProvider";
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		Log.i(TAG, "delete enter");
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		Log.i(TAG, "getType enter");
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		Log.i(TAG, "getType insert");
		return null;
	}

	@Override
	public boolean onCreate() {
		Log.i(TAG, "getType onCreate");
		return true;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		Log.i(TAG, "query onCreate");
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		Log.i(TAG, "update onCreate");
		return 0;
	}
	
}