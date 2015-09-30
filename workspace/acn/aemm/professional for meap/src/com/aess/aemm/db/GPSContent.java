package com.aess.aemm.db;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class GPSContent extends AContent implements GPSTableColumns {
	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI
			+ "/" + GPSTable.GPS);

	public static final String[] CONTENT_PROJECTION = new String[] { ID,
			GPS_TIME, GPS_LONGITUDE, GPS_LATITUDE, GPS_ISREAD };

	public GPSContent() {
		mBaseUri = CONTENT_URI;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(GPS_TIME, mGPSTime);
		values.put(GPS_LONGITUDE, mGPSLongitude);
		values.put(GPS_LATITUDE, mGPSLatitude);
		values.put(GPS_ISREAD, mGPSIsRead);
		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AContent> T restore(Cursor cursor) {
		mGPSTime = cursor.getString(CONTENT_TIME_COLUMN);
		mGPSLongitude = cursor.getString(CONTENT_LONGITUDE_COLUMN);
		mGPSLatitude = cursor.getString(CONTENT_LATITUDE_COLUMN);
		mGPSIsRead = cursor.getInt(CONTENT_ISREAD_COLUMN);
		return (T) this;
	}

	public static int deleteGPSLocationWithId(Context context, long id) {
		int numofRows = 0;
		Uri u = ContentUris.withAppendedId(CONTENT_URI, id);

		numofRows = context.getContentResolver().delete(u, null, null);
		return numofRows;
	}

	public static void deleteAllGPSContentByBeRead(Context context) {
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					GPSContent data = getContent(cursor, GPSContent.class);
					if (data.mGPSIsRead == 1) {
						deleteGPSLocationWithId(context, data.mId);
					}
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
	}

	public static void deleteAllGPSContent(Context context) {
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					GPSContent data = getContent(cursor, GPSContent.class);
					deleteGPSLocationWithId(context, data.mId);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
	}

	public static ArrayList<GPSContent> queryAllLocationContents(Context context) {
		ArrayList<GPSContent> appList = new ArrayList<GPSContent>();
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					GPSContent data = getContent(cursor, GPSContent.class);
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

	public String mGPSTime;
	public String mGPSLongitude;
	public String mGPSLatitude;
	public int mGPSIsRead;
}
