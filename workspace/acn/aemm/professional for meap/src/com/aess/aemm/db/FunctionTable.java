package com.aess.aemm.db;

import android.database.sqlite.SQLiteDatabase;


interface FunctionTableColumns {
	public static final String ID = "_id";

	public static final String FUNCTION_ID = "function_idd";
	public static final String FUNCTION_NAME = "function_name";
	public static final String FUNCTION_VERSION = "function_version";
	public static final String FUNCTION_DISABLED = "function_disabled";
	public static final String FUNCTION_INSTALLED_TIME = "function_install_time";
	public static final String FUNCTION_LAST_START_TIME = "function_last_install_time";
	public static final String FUNCTION_LAST_EXIT_TIME = "function_last_exit_time";
	
	public static final int CONTENT_ID_COLUMN = 0;
	public static final int CONTENT_IDENTIFIER_COLUMN = 1;
	public static final int CONTENT_DISPLAYNAME_COLUMN = 2;
	public static final int CONTENT_VERSION_COLUMN = 3;
	public static final int CONTENT_DISABLED_COLUMN = 4;
	public static final int CONTENT_INSTALLED_TIME = 5;
	public static final int CONTENT_LAST_START_TIME = 6;
	public static final int CONTENT_LAST_EXIT_TIME = 7;

}

public class FunctionTable {
	protected final static String FUNCTION = "function";
	
	public static void create(SQLiteDatabase database) {
		String s = " (" + "_id integer primary key autoincrement, "
				+ FunctionTableColumns.FUNCTION_ID + " text, "
				+ FunctionTableColumns.FUNCTION_NAME
				+ " text, "
				+ FunctionTableColumns.FUNCTION_VERSION
				+ " text, "
				+ FunctionTableColumns.FUNCTION_DISABLED
				+ " integer, " // whether the app is in black-list
				+ FunctionTableColumns.FUNCTION_INSTALLED_TIME + " text, "
				+ FunctionTableColumns.FUNCTION_LAST_START_TIME + " text, "
				+ FunctionTableColumns.FUNCTION_LAST_EXIT_TIME + " text "
				+ ");";
		database.execSQL("create table if not exists " + FUNCTION + s);
	}
}
