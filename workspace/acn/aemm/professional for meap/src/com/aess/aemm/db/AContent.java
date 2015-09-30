package com.aess.aemm.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public abstract class AContent {
	public static final int NOT_SAVED = -1;
	public static final String AUTHORITY = AemmProvider.PROVIDER_NAME;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	public abstract ContentValues toContentValues();

	public abstract <T extends AContent> T restore(Cursor cursor);

	static public <T extends AContent> T getContent(Cursor cursor,
			Class<T> klass) {
		try {
			T content = klass.newInstance();
			content.mId = cursor.getLong(0);
			return content.restore(cursor);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

//	private Uri getUri() {
//		if (mUri == null) {
//			mUri = ContentUris.withAppendedId(mBaseUri, mId);
//		}
//		return mUri;
//	}

	public Uri add(Context context) {
		if (isSaved()) {
			throw new UnsupportedOperationException();
		}

		Uri res = context.getContentResolver().insert(mBaseUri,
				toContentValues());
		mId = Long.parseLong(res.getPathSegments().get(1));
		return res;
	}

	public int update(Context context) {
		return update(context, toContentValues());
	}
	
	private int update(Context context, ContentValues contentValues) {
		return context.getContentResolver().update(ContentUris.withAppendedId(mBaseUri, mId), contentValues,
				null,null);
	}
	
	private boolean isSaved() {
		return mId != NOT_SAVED;
	}

	protected Uri mBaseUri = null;
	protected Uri mUri = null;
	public long mId = NOT_SAVED;
}
