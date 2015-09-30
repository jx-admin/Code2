package com.aess.aemm.db;

import android.database.sqlite.SQLiteDatabase;

interface TrafficTableColumns {
	public static final String ID       = "_id";
	public static final String NEWFLOW   = "newflow";
	public static final String OLDFLOW   = "oldflow";
	public static final String PAK_NAME = "pname";
	public static final String APK_UID  = "uid";
	public static final String APK_TIME = "time";
	
	public static final int COLUMN_ID   = 0;
	public static final int COLUMN_NEWFLOW = 1;
	public static final int COLUMN_OLDFLOW = 2;
	public static final int COLUMN_NAME = 3;
	public static final int COLUMN_UID  = 4;
	public static final int COLUMN_TIME = 5;
}

public class TrafficTable {
	protected final static String TRAFFIC = "traffic";
	
	public static void create(SQLiteDatabase database) {
		String s = " (" + "_id integer primary key autoincrement, "
				+ TrafficTableColumns.NEWFLOW + " LARGEINT, "
				+ TrafficTableColumns.OLDFLOW + " LARGEINT, "
				+ TrafficTableColumns.PAK_NAME + " text, "
				+ TrafficTableColumns.APK_UID + " int, "
				+ TrafficTableColumns.APK_TIME + " LARGEINT " + ");";
		database.execSQL("create table if not exists " + TRAFFIC + s);
	}
}