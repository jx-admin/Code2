package com.aess.aemm.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AemmProvider extends ContentProvider {
	static final String PROVIDER_NAME = "com.accenture.aemm.db";
	static final String PROVIDER_DB = "aemm.db";
	static final int PROVIDER_DB_VERSION = 4;

	public static final String _ID = "_id";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME);

	@Override
	public boolean onCreate() {
		database = new AemmDBHelper(getContext(), PROVIDER_DB, null,
				PROVIDER_DB_VERSION).getReadableDatabase();
		return database != null;
	}

	@Override
	public String getType(Uri arg0) {
		switch (uriMatcher.match(arg0)) {
		case ITEMS:
			return "vnd.android.cursor.dir/com.accenture.aemm.db";
		case ITEM:
			return "vnd.android.cursor.item/com.accenture.aemm.db";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + arg0);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;

		switch (match) {
		case APKTABLE_ID:
		case WEBCLIPTABLE_ID:
		case CONFIGTABLE_ID:
		case FUNCTIONTABLE_ID:
		case GPSTABLE_ID:
		case NEWSTABLE_ID:
		case TRAFFICTABLE_ID: {
			if (selection != null) {
				return database.delete(TABLE_NAMES[table], _ID + "="
						+ uri.getPathSegments().get(1) + " and (" + selection
						+ ")", selectionArgs);
			} else {
				return database.delete(TABLE_NAMES[table], _ID + "="
						+ uri.getPathSegments().get(1), null);
			}
		}
		case APKTABLE:
		case WEBCLIPTABLE:
		case CONFIGTABLE:
		case FUNCTIONTABLE:
		case GPSTABLE:
		case NEWSTABLE:
		case TRAFFICTABLE: {
			return database
					.delete(TABLE_NAMES[table], selection, selectionArgs);
		}
		default: {
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;
		long rowId = 0;
		Uri resultUri = null;

		switch (match) {
		case APKTABLE:
		case WEBCLIPTABLE:
		case CONFIGTABLE:
		case FUNCTIONTABLE:
		case GPSTABLE:
		case NEWSTABLE:
		case TRAFFICTABLE:{
			rowId = database.insert(TABLE_NAMES[table], "", contentValues);
			resultUri = ContentUris.withAppendedId(uri, rowId);
			break;
		}
		}

		if (!(rowId > 0)) {
			throw new SQLException("failed to insert row into " + uri);
		}

		getContext().getContentResolver().notifyChange(resultUri, null);
		return resultUri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;

		switch (match) {
		case APKTABLE:
		case WEBCLIPTABLE:
		case CONFIGTABLE:
		case FUNCTIONTABLE:
		case GPSTABLE:
		case NEWSTABLE:
		case TRAFFICTABLE:{
			return database.query(TABLE_NAMES[table], projection, selection,
					selectionArgs, null, null, sortOrder);
		}
		case APKTABLE_ID:
		case WEBCLIPTABLE_ID:
		case CONFIGTABLE_ID:
		case FUNCTIONTABLE_ID:
		case GPSTABLE_ID:
		case NEWSTABLE_ID:
		case TRAFFICTABLE_ID:{
			return database.query(TABLE_NAMES[table], projection, _ID + "="
					+ uri.getPathSegments().get(1), selectionArgs, null, null,
					null);
		}
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
	}

	public Cursor queryMsg(Uri uri, String[] projection, String selection,
			String[] selectionArgs) {
		return query(uri, projection, selection, selectionArgs,
				NewsTableColumns.PDATE + " DESC");
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String selection,
			String[] selectionArgs) {
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;

		switch (match) {
		case APKTABLE_ID:
		case WEBCLIPTABLE_ID:
		case CONFIGTABLE_ID:
		case FUNCTIONTABLE_ID:
		case GPSTABLE_ID:
		case NEWSTABLE_ID:
		case TRAFFICTABLE_ID:{
			if (selection != null) {
				return database.update(TABLE_NAMES[table], contentValues, _ID
						+ "=" + uri.getPathSegments().get(1) + " and ("
						+ selection + ")", selectionArgs);
			} else {
				return database.update(TABLE_NAMES[table], contentValues, _ID
						+ "=" + uri.getPathSegments().get(1), null);
			}
		}
		case APKTABLE:
		case WEBCLIPTABLE:
		case CONFIGTABLE:
		case FUNCTIONTABLE:
		case GPSTABLE:
		case NEWSTABLE:
		case TRAFFICTABLE:{
			return database.update(TABLE_NAMES[table], contentValues,
					selection, selectionArgs);
		}
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
	}

	private static SQLiteDatabase database = null;
	private static UriMatcher uriMatcher = null;

	private static final String[] TABLE_NAMES = { ApkTable.APKTABLE,
			FunctionTable.FUNCTION, SettingsTable.SETTINGS,
			WebclipTable.WEBCLIP, GPSTable.GPS, NewsTable.NEWS,
			TrafficTable.TRAFFIC };

	// 12 bits to the base type: 0, 0x1000, 0x2000, etc.
	private static final int BASE_SHIFT = 12;

	private static final int APK_BASE = 0x0000;
	private static final int APKTABLE = APK_BASE;
	private static final int APKTABLE_ID = APK_BASE + 1;

	private static final int FUNCTION_BASE = 0x1000;
	private static final int FUNCTIONTABLE = FUNCTION_BASE;
	private static final int FUNCTIONTABLE_ID = FUNCTION_BASE + 1;

	private static final int CONFIG_BASE = 0x2000;
	private static final int CONFIGTABLE = CONFIG_BASE;
	private static final int CONFIGTABLE_ID = CONFIG_BASE + 1;

	private static final int WEBCLIP_BASE = 0x3000;
	private static final int WEBCLIPTABLE = WEBCLIP_BASE;
	private static final int WEBCLIPTABLE_ID = WEBCLIP_BASE + 1;

	private static final int GPS_BASE = 0x4000;
	private static final int GPSTABLE = GPS_BASE;
	private static final int GPSTABLE_ID = GPS_BASE + 1;

	private static final int NEWS_BASE = 0x5000;
	private static final int NEWSTABLE = NEWS_BASE;
	private static final int NEWSTABLE_ID = NEWS_BASE + 1;
	
	private static final int TRAFFIC_BASE = 0x6000;
	private static final int TRAFFICTABLE = TRAFFIC_BASE;
	private static final int TRAFFICTABLE_ID = TRAFFIC_BASE + 1;

	private static final int ITEMS = 1;
	private static final int ITEM = 2;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		uriMatcher.addURI(PROVIDER_NAME, ApkTable.APKTABLE, APKTABLE);
		uriMatcher.addURI(PROVIDER_NAME, ApkTable.APKTABLE + "/#", APKTABLE_ID);

		uriMatcher.addURI(PROVIDER_NAME, FunctionTable.FUNCTION, FUNCTIONTABLE);
		uriMatcher.addURI(PROVIDER_NAME, FunctionTable.FUNCTION + "/#",
				FUNCTIONTABLE_ID);

		uriMatcher.addURI(PROVIDER_NAME, SettingsTable.SETTINGS, CONFIGTABLE);
		uriMatcher.addURI(PROVIDER_NAME, SettingsTable.SETTINGS + "/#",
				CONFIGTABLE_ID);

		uriMatcher.addURI(PROVIDER_NAME, WebclipTable.WEBCLIP, WEBCLIPTABLE);
		uriMatcher.addURI(PROVIDER_NAME, WebclipTable.WEBCLIP + "/#",
				WEBCLIPTABLE_ID);

		uriMatcher.addURI(PROVIDER_NAME, GPSTable.GPS, GPSTABLE);
		uriMatcher.addURI(PROVIDER_NAME, GPSTable.GPS + "/#", GPSTABLE_ID);

		uriMatcher.addURI(PROVIDER_NAME, NewsTable.NEWS, NEWSTABLE);
		uriMatcher.addURI(PROVIDER_NAME, NewsTable.NEWS + "/#", NEWSTABLE_ID);

		uriMatcher.addURI(PROVIDER_NAME, TrafficTable.TRAFFIC, TRAFFICTABLE);
		uriMatcher.addURI(PROVIDER_NAME, TrafficTable.TRAFFIC + "/#",
				TRAFFICTABLE_ID);
	}
}
