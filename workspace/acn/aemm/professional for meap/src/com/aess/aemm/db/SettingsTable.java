package com.aess.aemm.db;

import android.database.sqlite.SQLiteDatabase;

interface SettingsTableColumns {
	public static final String ID = "_id";
	public static final String CID = "cid";
	public static final String UUID = "Uuid";
	public static final String NAME = "name";
	public static final String TYPE = "profileType";
	public static final String VERSION = "version";
	public static final String NOTE = "note";
	
	public static final int CONTENT_ID_COLUMN = 0;
	public static final int CONTENT_CID_COLUMN = 1;
	public static final int CONTENT_UUID_COLUMN = 2;
	public static final int CONTENT_NAME_COLUMN = 3;
	public static final int CONTENT_TYPE_COLUMN = 4;
	public static final int CONTENT_VERSION_COLUMN = 5;
	public static final int CONTENT_NOTE_COLUMN = 6;
}

public class SettingsTable {
	protected final static String SETTINGS = "settings";
	
	public static  void create(SQLiteDatabase database) {
		String s = " (" + "_id integer primary key autoincrement, "
				+ SettingsTableColumns.CID + " text, " + SettingsTableColumns.UUID
				+ " text, " + SettingsTableColumns.NAME + " text, "
				+ SettingsTableColumns.TYPE + " text, " + SettingsTableColumns.VERSION
				+ " text, " + SettingsTableColumns.NOTE + " text " + ");";
		database.execSQL("create table if not exists " + SETTINGS + s);
	}
}
