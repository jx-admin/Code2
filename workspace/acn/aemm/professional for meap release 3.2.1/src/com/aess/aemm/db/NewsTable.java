package com.aess.aemm.db;

import android.database.sqlite.SQLiteDatabase;

public class NewsTable {
	protected final static String NEWS = "news";
	
	public static void create(SQLiteDatabase database) {
		String s = " (" + "_id integer primary key autoincrement, "
				+ NewsTableColumns.TITLE + " text, "
				+ NewsTableColumns.CONTENT + " text, "
				+ NewsTableColumns.PUBLISH + " text, "
				+ NewsTableColumns.PDATE + " LARGEINT, "
				+ NewsTableColumns.TYPE + " int, "
				+ NewsTableColumns.PLANSTATE + " int, "
				+ NewsTableColumns.LEVEL + " int, "
				+ NewsTableColumns.TBEGIN + " text, "
				+ NewsTableColumns.TEND + " text, "
				+ NewsTableColumns.ISREAD + " int, "
				+ NewsTableColumns.ISADD + " int, "
			    + NewsTableColumns.EVENTID + " int, "
				+ NewsTableColumns.TYPENAME + " text, "
				+ NewsTableColumns.BUSTYPE + " int, "
				+ NewsTableColumns.BUSNAME + " text, "
				+ NewsTableColumns.COMMANDID+" text,"
				+ NewsTableColumns.HAS_ATTACHMENT+" int,"
				+ NewsTableColumns.STATE+" int"
				+ ");";
		database.execSQL("create table if not exists " + NEWS + s);
	}
}
