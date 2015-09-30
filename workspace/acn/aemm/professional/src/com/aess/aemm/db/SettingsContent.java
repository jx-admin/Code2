package com.aess.aemm.db;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SettingsContent extends AContent implements SettingsTableColumns {

	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI
			+ "/" + SettingsTable.SETTINGS);

	public static final String[] CONTENT_PROJECTION = new String[] { ID, CID,
			UUID, NAME, TYPE, VERSION, NOTE };

	public SettingsContent () {
		mBaseUri = CONTENT_URI;
	}
	
	public void setProfileArg(String name, String uuid, long cid,
			String version, String type) {
		if (null != name) {
			mName = name;
		}
		if (null != uuid) {
			mUuid = uuid;
		}
		if (cid >= 0) {
			mCid = String.valueOf(cid);
		}
		if (null != version) {
			mVersion = version;
		}
		if (null != type) {
			mType = type;
		}
	}
	
	public String getCid() {
		return mCid;
	}

	public String getName() {
		return mName;
	}

	public Long getId() {
		return mId;
	}
	
	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(CID, mCid);
		values.put(UUID, mUuid);
		values.put(NAME, mName);
		values.put(TYPE, mType);
		values.put(VERSION, mVersion);
		values.put(NOTE, mNote);
		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AContent> T restore(Cursor cursor) {
		mCid = cursor.getString(CONTENT_CID_COLUMN);
		mUuid = cursor.getString(CONTENT_UUID_COLUMN);
		mName = cursor.getString(CONTENT_NAME_COLUMN);
		mType = cursor.getString(CONTENT_TYPE_COLUMN);
		mVersion = cursor.getString(CONTENT_VERSION_COLUMN);
		mNote = cursor.getString(CONTENT_NOTE_COLUMN);
		return (T) this;
	}

	public static ArrayList<SettingsContent> queryContentBy_Type_Version(
			Context context, String type, String version) {

		ArrayList<SettingsContent> list = new ArrayList<SettingsContent>();
		Cursor cursor = null;

		try {
			cursor = context.getContentResolver().query(
					SettingsContent.CONTENT_URI,
					SettingsContent.CONTENT_PROJECTION,
					TYPE + "=?" + " and " + VERSION + "!=?",
					new String[] { type, version }, null);
			if (cursor.moveToFirst()) {
				do {
					SettingsContent i = getContent(cursor,
							SettingsContent.class);
					list.add(i);

				} while (cursor.moveToNext());
			}
			return list;
		} finally {
			cursor.close();
		}
	}
	
	public static SettingsContent queryContentBy_Type_Name(Context context,
			String type, String name) {
		SettingsContent sc = null;
		if (null!= type && null!= name ) {
			String where = String.format("%s=? and %s=?", TYPE, NAME);
			Cursor c = null;
			try {
				c = context.getContentResolver().query(
						CONTENT_URI, CONTENT_PROJECTION,
						where, new String[] { type, name }, null);
				sc = queryContentByCursor(c);
			} finally {
				c.close();
			}
		}

		return sc;
	}
	
	public static SettingsContent queryContentByUUID(Context context, String uuid) {
		SettingsContent pc = null;
		if (null != uuid) {
			Cursor c = null;
			try {
				c = context.getContentResolver().query(
						CONTENT_URI, CONTENT_PROJECTION,
						UUID + "=?", new String[] { uuid }, null);
				pc = queryContentByCursor(c);
			} finally {
				c.close();
			}

		}

		return pc;
	}
	
	public static SettingsContent queryContentBy_Type_Cid(Context context,String type, String cid) {
		SettingsContent pc = null;
		if (null != type && null != cid) {
			String where = String.format("%s=? and %s=?", TYPE, CID);
			Cursor c = null;
			try {
				c = context.getContentResolver().query(CONTENT_URI,
						CONTENT_PROJECTION, where, new String[] { type, cid }, null);
				
				pc = queryContentByCursor(c);
				
			} finally {
				c.close();
			}
		}

		return pc;
	}
	
	public static int delContentById(Context context, long id) {
		int numofRows = 0;
		if (id >= 0) {
			Uri u = ContentUris.withAppendedId(CONTENT_URI, id);
			numofRows = context.getContentResolver().delete(u, null, null);
		}
		return numofRows;
	}
	
	private static SettingsContent queryContentByCursor(Cursor cursor) {
		if (cursor.moveToFirst()) {
			return getContent(cursor, SettingsContent.class);
		} else {
			return null;
		}
	}

	public String mCid;
	public String mUuid;
	public String mName;
	public String mType;
	public String mVersion;
	public String mVersionClient;
	public String mNote;
}
