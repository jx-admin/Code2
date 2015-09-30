package com.aess.aemm.setting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public abstract class Content {
	public static final String TAG = "ProfileContent";
	
	public static final String AUTHORITY = "com.aess.aemm.profile";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	public static final String RECORD_ID = "_id";
	// public static final String[] COUNT_COLUMNS = new String[]{"count(*)"};
	// public static final String[] ID_PROJECTION = new String[] { RECORD_ID };
	// public static final int ID_PROJECTION_COLUMN = 0;
	// public static final String PROVIDER_NAME = "com.aess.aemm.profile";
	public static final int NOT_SAVED = -1;
	
	public abstract ContentValues toContentValues();

	public abstract <T extends Content> T restore (Cursor cursor);

	@SuppressWarnings("unchecked")
	public static <T extends Content>T getContent(Cursor cursor, Class<T> klass) {
		try {
			T content = klass.newInstance();
			int inedx = cursor.getColumnIndex(RECORD_ID);
			content.mId = cursor.getLong(inedx);
			return (T)content.restore(cursor);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Uri getUri() {
		if (mUri == null) {
			mUri = ContentUris.withAppendedId(mBaseUri, mId);
		}
		return mUri;
	}

	public boolean isSaved() {
		return mId != NOT_SAVED;
	}

	public Uri add(Context context) {
		Log.i(TAG, "add");
		if (isSaved()) {
			throw new UnsupportedOperationException();
		}

		Uri res = context.getContentResolver().insert(mBaseUri,
				toContentValues());
		
		mId = Long.parseLong(res.getPathSegments().get(1));
		return res;
	}
	
	public int update(Context context) {
		Log.i(TAG, "update");
		return context.getContentResolver()
		.update(getUri(), toContentValues(), null, null);
	}
	
	public int update(Context context, ContentValues contentValues) {
		return context.getContentResolver().update(getUri(), contentValues, null, null);
	}
	


	public byte[] getImageData(String str) {
		Log.i(TAG, str);
		InputStream inputStream = new ByteArrayInputStream(str.getBytes()); 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		byte[] data = new byte[1024 * 100]; 

		try { 
			for (int i = inputStream.read(data); i > 0; i = inputStream 
			.read(data)) { 
				outputStream.write(data, 0, i); 
			} 

			inputStream.close(); 
			data = outputStream.toByteArray(); 
			outputStream.close(); 
		} catch (IOException e) { 
			throw new RuntimeException(e); 
		} 

		return data;
	}
	
	protected Uri mUri = null;
	protected long mId = NOT_SAVED;
	protected Uri mBaseUri = null;
	protected String indentifier = null;
}