package com.aess.aemm.db;

import android.database.sqlite.SQLiteDatabase;

interface WebclipTableColumns {
	public static final String ID = "_id";

	public static final String IDENTIFIER = "identifier";
	public static final String DISPAYNAME = "dispayName";
	public static final String URL = "url";
	public static final String ICON = "icon";
	public static final String ISREMOVEABLE = "flag";
	public static final String VERSION = "version";

	public static final int CONTENT_ID_COLUMN = 0;
	public static final int CONTENT_IDENTIFIER_COLUMN = 1;
	public static final int CONTENT_DISPAYNAME_COLUMN = 2;
	public static final int CONTENT_URL_COLUMN = 3;
	public static final int CONTENT_ICON_COLUMN = 4;
	public static final int CONTENT_ISREMOVEABLE_COLUMN = 5;
	public static final int CONTENT_VERSION_COLUMN = 6;

}

public class WebclipTable {
	protected final static String WEBCLIP = "webclip";

	public static void create(SQLiteDatabase database) {
		String s = " (" + "_id integer primary key autoincrement, "
				+ WebclipTableColumns.IDENTIFIER + " text, "
				+ WebclipTableColumns.DISPAYNAME + " text, "
				+ WebclipTableColumns.URL + " text, "
				+ WebclipTableColumns.ICON + " blob, "
				+ WebclipTableColumns.ISREMOVEABLE + " integer, "
				+ WebclipTableColumns.VERSION + " text " + ");";
		database.execSQL("create table if not exists " + WEBCLIP + s);
	}
}
