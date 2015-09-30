package com.aess.aemm.db;

import java.util.ArrayList;

import com.aess.aemm.commonutils.CommUtils;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class WebclipContent extends AContent implements WebclipTableColumns {
	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI
			+ "/" + WebclipTable.WEBCLIP);

	public static final String[] CONTENT_PROJECTION = new String[] { ID,
			IDENTIFIER, DISPAYNAME, URL, ICON, ISREMOVEABLE, VERSION };

	public WebclipContent() {
		mBaseUri = CONTENT_URI;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(IDENTIFIER, mWebClipIdentifier);
		values.put(DISPAYNAME, mWebClipName);
		values.put(URL, mWebClipUrl);
		if (mWebClipIcon != null) {
			values.put(ICON, CommUtils.getImageData(mWebClipIcon));
		}
		values.put(ISREMOVEABLE, mIsRemovable);
		values.put(VERSION, mWebClipVersion);

		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AContent> T restore(Cursor cursor) {
		mWebClipIdentifier = cursor.getString(CONTENT_IDENTIFIER_COLUMN);
		mWebClipName = cursor.getString(CONTENT_DISPAYNAME_COLUMN);
		mWebClipUrl = cursor.getString(CONTENT_URL_COLUMN);
		mWebClipIcon = new String(cursor.getBlob(CONTENT_ICON_COLUMN));
		mIsRemovable = cursor.getInt(CONTENT_ISREMOVEABLE_COLUMN);
		mWebClipVersion = cursor.getString(CONTENT_VERSION_COLUMN);
		return (T) this;
	}

	public static ArrayList<WebclipContent> restoreWebClipWithVersion(
			Context context, String version) {
		Cursor cursor = context.getContentResolver().query(CONTENT_URI,
				CONTENT_PROJECTION, VERSION + "!=?", new String[] { version },
				null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		} else {
			ArrayList<WebclipContent> list = new ArrayList<WebclipContent>();
			try {
				if (cursor.moveToFirst()) {
					do {
						WebclipContent i = getContent(cursor,
								WebclipContent.class);
						list.add(i);
					} while (cursor.moveToNext());
					return list;
				} else {

					return null;
				}
			} finally {
				cursor.close();
			}
		}
	}

	public static int delWebclipById(Context context, long id) {
		int numofRows = 0;
		Uri u = ContentUris.withAppendedId(CONTENT_URI, id);

		numofRows = context.getContentResolver().delete(u, null, null);
		return numofRows;
	}

	public static ArrayList<WebclipContent> getWebClipByUnVer(Context context,
			String version) {
		String where = String.format("%s!=?", VERSION);

		Cursor cursor = context.getContentResolver().query(CONTENT_URI,
				CONTENT_PROJECTION, where, new String[] { version }, null);

		ArrayList<WebclipContent> list = new ArrayList<WebclipContent>();
		try {
			if (cursor.moveToFirst()) {
				do {
					WebclipContent i = getContent(cursor, WebclipContent.class);
					list.add(i);
				} while (cursor.moveToNext());
				return list;
			} else {
				return null;
			}
		} finally {
			cursor.close();
		}
	}

	@SuppressWarnings("unused")
	private static WebclipContent queryContentByCursor(Cursor cursor) {
		if (cursor.moveToFirst()) {
			return getContent(cursor, WebclipContent.class);
		} else {
			return null;
		}
	}

	public int mIsRemovable;
	public String mWebClipName;
	public String mWebClipUrl;
	public String mWebClipIcon;
	public String mWebClipIdentifier;
	public String mWebClipVersion;
	public int mWebClipFlag;
}
