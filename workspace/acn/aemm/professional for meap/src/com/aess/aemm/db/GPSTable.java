package com.aess.aemm.db;

import android.database.sqlite.SQLiteDatabase;


interface GPSTableColumns {
	public static final String ID = "_id";
	public static final String GPS_TIME = "time";
	public static final String GPS_LONGITUDE = "longitude";
	public static final String GPS_LATITUDE = "latitude";
	public static final String GPS_ISREAD = "flag";
	
	public static final int CONTENT_ID_COLUMN = 0;
	public static final int CONTENT_TIME_COLUMN = 1;
	public static final int CONTENT_LONGITUDE_COLUMN = 2;
	public static final int CONTENT_LATITUDE_COLUMN = 3;
	public static final int CONTENT_ISREAD_COLUMN = 4;
}

public class GPSTable {
	protected final static String GPS = "gps";
	
	public static void create(SQLiteDatabase database) {
		String s = " (" + "_id integer primary key autoincrement, "
				+ GPSTableColumns.GPS_TIME + " text, "
				+ GPSTableColumns.GPS_LONGITUDE + " text, "
				+ GPSTableColumns.GPS_LATITUDE + " text, "
				+ GPSTableColumns.GPS_ISREAD + " int " + ");";
		database.execSQL("create table if not exists " + GPS + s);
	}
}
