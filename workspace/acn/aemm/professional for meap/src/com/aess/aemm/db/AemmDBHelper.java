package com.aess.aemm.db;

import com.aess.aemm.view.msg.Attachment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AemmDBHelper extends SQLiteOpenHelper {
//	protected static final String PROVIDER_NAME = "com.aess.meap.db";
//
//	protected static final String DATABASE_NAME = "meap.db";
//	protected static final int DATABASE_VERSION = 14;

	public AemmDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		ApkTable.create(database);
		SettingsTable.create(database);
		FunctionTable.create(database);
		WebclipTable.create(database);
		GPSTable.create(database);
		NewsTable.create(database);
		Attachment.DBTable.create(database);
		TrafficTable.create(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {
		database.execSQL("drop table " + ApkTable.APKTABLE);
		database.execSQL("drop table " + SettingsTable.SETTINGS);
		database.execSQL("drop table " + FunctionTable.FUNCTION);
		database.execSQL("drop table " + WebclipTable.WEBCLIP);
		database.execSQL("drop table " + GPSTable.GPS);
		database.execSQL("drop table " + NewsTable.NEWS);
		database.execSQL("drop table " + Attachment.DATABASE_NAME);
		database.execSQL("drop table " + TrafficTable.TRAFFIC);
		onCreate(database);
	}
}
