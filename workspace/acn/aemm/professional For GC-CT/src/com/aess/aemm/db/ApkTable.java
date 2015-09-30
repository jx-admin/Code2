package com.aess.aemm.db;

import android.database.sqlite.SQLiteDatabase;

// ---------- APK ----------
// APK_FLAG :: new app, install, uninstall, remote-uninstall, downloading
// APK_DISABLED :: whether the app is in black-list. 1--disable 0--enable
// PUBLISHED :: whether the app is published. 1--published 0- no published
// IS_REMOVED :: whether the installed app is to be removed. default is 0. 1
// is gong to be removed.
interface ApkTableColumns {
	public static final String _ID = "_id";
	public static final String APK_ID = "apk_id";
	public static final String APK_NAME = "apk_name";
	public static final String APK_URL = "apk_url";
	public static final String APK_DESC = "apk_desc";
	public static final String APK_UID = "apk_uid";
	public static final String APK_VERSION = "apk_version";
	public static final String APK_VERSION_CLIENT = "apk_version_client";
	public static final String APK_FLAG = "flag";
	public static final String APK_SCREEN = "screen";
	public static final String APK_ROW = "row";
	public static final String APK_COLUMN = "column";
	public static final String ICONCOLOR = "icon_color";
	public static final String ICONGREY = "icon_grey";
	public static final String APK_FILE_NAME = "apk_file_name";
	public static final String APK_FILE_PATH = "apk_file_path";
	public static final String APK_PACKAGE_NAME = "apk_package_name";
	public static final String APK_DISABLED = "apk_disabled";
	public static final String INSTALLED_TIME = "installed_time";
	public static final String LAST_START_TIME = "last_start_time";
	public static final String LAST_EXIT_TIME = "last_exit_time";
	public static final String PUBLISHED = "published";
	public static final String IS_REMOVED = "is_removed";
	public static final String TRAFFIC = "traffic";
	public static final String APPTYPE = "apptype";
	public static final String SNAPSHOT = "snapshot";
	public static final String TYPENAME = "typename";
	public static final String SSOACCOUNT = "ssoaccount";
	
	public static final int CONTENT_ID_COLUMN = 0;
	public static final int CONTENT_APKID_COLUMN = 1;
	public static final int CONTENT_APKNAME_COLUMN = 2;
	public static final int CONTENT_APKURL_COLUMN = 3;
	public static final int CONTENT_APKDESC_COLUMN = 4;
	public static final int CONTENT_APKUID_COLUMN = 5;
	public static final int CONTENT_APKVERSION_COLUMN = 6;
	public static final int CONTENT_APKVERSIONCLIENT_COLUMN = 7;
	public static final int CONTENT_APKFLAG_COLUMN = 8;
	public static final int CONTENT_SCREENS_COLUMN = 9;
	public static final int CONTENT_ROW_COLUMN = 10;
	public static final int CONTENT_COLUMN_COLUMN = 11;
	public static final int CONTENT_ICONCOLOR_COLUMN = 12;
	public static final int CONTENT_ICONGREY_COLUMN = 13;
	public static final int CONTENT_APKFILENAME_COLUMN = 14;
	public static final int CONTENT_APKFILEPATH_COLUMN = 15;
	public static final int CONTENT_APKPACKAGENAME_COLUMN = 16;
	public static final int CONTENT_DISABLED_COLUMN = 17;
	public static final int CONTENT_INSTALLED_TIME_COLUMN = 18;
	public static final int CONTENT_LAST_START_TIME_COLUMN = 19;
	public static final int CONTENT_LAST_EXIT_TIME_COLUMN = 20;
	public static final int CONTENT_PUBLISHED_COLUMN = 21;
	public static final int CONTENT_ISREMOVED_COLUMN = 22;
	public static final int CONTENT_TRAFFIC_COLUMN = 23;
	public static final int CONTENT_APPTYPE_COLUMN = 24;
	public static final int CONTENT_SNAPSHOT_COLUMN = 25;
	public static final int CONTENT_TYPENAME_COLUMN = 26;
	public static final int CONTENT_SSOACC_COLUMN = 27;
}

public class ApkTable {
	protected final static String APKTABLE = "apk";
	
	static void create(SQLiteDatabase database) {
		String s = " (" + "_id integer primary key autoincrement, "
				+ ApkTableColumns.APK_ID + " text, " + ApkTableColumns.APK_NAME
				+ " text, " + ApkTableColumns.APK_URL + " text, "
				+ ApkTableColumns.APK_DESC + " text, "
				+ ApkTableColumns.APK_UID + " text, "
				+ ApkTableColumns.APK_VERSION + " text, "
				+ ApkTableColumns.APK_VERSION_CLIENT + " text, "
				+ ApkTableColumns.APK_FLAG + " integer, "
				+ ApkTableColumns.APK_SCREEN + " integer, "
				+ ApkTableColumns.APK_ROW + " integer, "
				+ ApkTableColumns.APK_COLUMN + " integer, "
				+ ApkTableColumns.ICONCOLOR + " blob, "
				+ ApkTableColumns.ICONGREY + " blob, "
				+ ApkTableColumns.APK_FILE_NAME + " text, "
				+ ApkTableColumns.APK_FILE_PATH + " text, "
				+ ApkTableColumns.APK_PACKAGE_NAME + " text, "
				+ ApkTableColumns.APK_DISABLED + " integer, "
				+ ApkTableColumns.INSTALLED_TIME + " text, "
				+ ApkTableColumns.LAST_START_TIME + " text, "
				+ ApkTableColumns.LAST_EXIT_TIME + " text, "
				+ ApkTableColumns.PUBLISHED + " interger, "
				+ ApkTableColumns.IS_REMOVED + " interger, "
				+ ApkTableColumns.TRAFFIC + " LARGEINT, "
				+ ApkTableColumns.APPTYPE + " interger, "
				+ ApkTableColumns.SNAPSHOT + " text, "
				+ ApkTableColumns.TYPENAME + " text, "
				+ ApkTableColumns.SSOACCOUNT + " text " + ");";
		database.execSQL("create table if not exists " + APKTABLE + s);
	}
}
