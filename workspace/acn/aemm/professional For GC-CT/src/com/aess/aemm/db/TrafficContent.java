package com.aess.aemm.db;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class TrafficContent extends AContent implements TrafficTableColumns {
	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI
			+ "/" + TrafficTable.TRAFFIC);

	public static final String[] CONTENT_PROJECTION = new String[] { ID,
		NEWFLOW, OLDFLOW, PAK_NAME, APK_UID, APK_TIME };
	
	public static final int    Other_UID = -1;
	public static final String Other_NAME = "com.aess.aemm.traffic.Other";
	public static final int    Monble_UID = -2;
	public static final String Monble_NAME = "com.aess.aemm.traffic.Monble";

	public TrafficContent() {
		mBaseUri = CONTENT_URI;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(NEWFLOW, mNewFlow);
		values.put(OLDFLOW, mOldFlow);
		values.put(PAK_NAME, mName);
		values.put(APK_UID, mUid);
		values.put(APK_TIME, mTime);
		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AContent> T restore(Cursor cursor) {
		mNewFlow = cursor.getLong(COLUMN_NEWFLOW);
		mOldFlow = cursor.getLong(COLUMN_OLDFLOW);
		mName = cursor.getString(COLUMN_NAME);
		mUid = cursor.getInt(COLUMN_UID);
		mTime = cursor.getLong(COLUMN_TIME);
		return (T) this;
	}

	public static int deleteTrafficById(Context context, long id) {
		int numofRows = 0;
		Uri u = ContentUris.withAppendedId(CONTENT_URI, id);

		numofRows = context.getContentResolver().delete(u, null, null);
		return numofRows;
	}
	
	

	public static void deleteTrafficContentByUid(Context context, int Uid) {
		
		if (0 == Uid) {
			return;
		}
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					TrafficContent data = getContent(cursor, TrafficContent.class);
					if (Uid == data.mUid) {
						deleteTrafficById(context, data.mId);
						break;
					}
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
	}

	public static void deleteAllTrafficContent(Context context) {
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					TrafficContent data = getContent(cursor, TrafficContent.class);
					deleteTrafficById(context, data.mId);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
	}
	
	public static TrafficContent queryTrafficsByUid(Context context, int Uid) {
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					TrafficContent data = getContent(cursor, TrafficContent.class);
					if (null != data) {
						return data;
					}
				} while (cursor.moveToNext());
				return null;
			} else {
				return null;
			}
		} finally {
			cursor.close();
		}
	}

	public static ArrayList<TrafficContent> queryAllTraffics(Context context) {
		ArrayList<TrafficContent> appList = new ArrayList<TrafficContent>();
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					TrafficContent data = getContent(cursor, TrafficContent.class);
					appList.add(data);
				} while (cursor.moveToNext());
				return appList;
			} else {
				return null;
			}
		} finally {
			cursor.close();
		}
	}

	public long mNewFlow;
	public long mOldFlow;
	public String mName;
	public int mUid;
	public long mTime;
}