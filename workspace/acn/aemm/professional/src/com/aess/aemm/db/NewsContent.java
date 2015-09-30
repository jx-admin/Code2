package com.aess.aemm.db;

import java.util.ArrayList;
import java.util.List;

import com.aess.aemm.view.msg.MessageType;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class NewsContent extends AContent implements NewsTableColumns {
	// public static final String DATEFORMAT = "yyyy-MM-dd:HH";
	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI
			+ "/" + NewsTable.NEWS);

	public static final String[] CONTENT_PROJECTION = new String[] { ID, TITLE,
			CONTENT, PUBLISH, PDATE, TYPE, PLANSTATE, LEVEL, TBEGIN, TEND,
			ISREAD, ISADD, EVENTID, TYPENAME, BUSTYPE, BUSNAME };

	public static final String DataFormat = "yyyy-MM-dd";

	public NewsContent() {
		mBaseUri = CONTENT_URI;
		mType = -1;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(PDATE, mPData);
		values.put(TITLE, mTitile);
		values.put(CONTENT, mContent);
		values.put(PUBLISH, mPublish);
		values.put(PDATE, mPData);
		values.put(TYPE, mType);
		values.put(PLANSTATE, mPlanState);
		values.put(LEVEL, mLevel);
		values.put(TBEGIN, mBegin);
		values.put(TEND, mEnd);
		values.put(ISREAD, mIsRead);
		values.put(ISADD, mIsAdd2);
		values.put(EVENTID, mEventId);
		values.put(TYPENAME, mTypeName);
		values.put(BUSTYPE, mBusType);
		values.put(BUSNAME, mBusName);
		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AContent> T restore(Cursor cursor) {
		mTitile = cursor.getString(TITLE_COLUMN);
		mContent = cursor.getString(CONTENT_COLUMN);
		mPublish = cursor.getString(PUBLISH_COLUMN);
		mPData = cursor.getLong(PDATE_COLUMN);
		mType = cursor.getInt(TYPE_COLUMN);
		mPlanState = cursor.getInt(PLANSTATE_COLUMN);
		mLevel = cursor.getInt(LEVEL_COLUMN);
		mBegin = cursor.getString(TBEGIN_COLUMN);
		mEnd = cursor.getString(TEND_COLUMN);
		mIsRead = cursor.getInt(ISREAD_COLUMN);
		mIsAdd2 = cursor.getInt(ISADD_COLUMN);
		mEventId = cursor.getInt(EVENT_COLUMN);
		mTypeName = cursor.getString(TYPENAME_COLUMN);
		mBusType = cursor.getInt(BUSTYPE_COLUMN);
		mBusName = cursor.getString(BUSNAME_COLUMN);
		return (T) this;
	}

	public static int delContentById(Context context, long id) {
		int numofRows = 0;
		if (id >= 0) {
			Uri u = ContentUris.withAppendedId(CONTENT_URI, id);
			numofRows = context.getContentResolver().delete(u, null, null);
		}
		return numofRows;
	}
	
	public static void deleteAllContent(Context context) {
		Uri u = CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(u,
				CONTENT_PROJECTION, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					NewsContent data = getContent(cursor, NewsContent.class);
					delContentById(context, data.mId);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
	}

	public static Cursor queryMsgByURI(Context context, Uri uri) {
		return context.getContentResolver().query(uri, CONTENT_PROJECTION,
				null, null, null);

	}

	public static Cursor queryContentByPDate(Context context) {
		Cursor cursor = context.getContentResolver().query(CONTENT_URI,
				CONTENT_PROJECTION, null, null, PDATE + " DESC");
		return cursor;
	}

	public static Cursor queryContentByUnRead(Context context) {
		Cursor cursor = context.getContentResolver().query(CONTENT_URI,
				CONTENT_PROJECTION, NewsTableColumns.ISREAD + "=0", null,
				PDATE + " DESC");
		return cursor;
	}

	public static List<NewsContent> getContentByType(Context context) {
		String query = NewsTableColumns.TYPE + "=? or " + NewsTableColumns.TYPE
				+ "=?";
		String[] queryValue = new String[] {
				String.valueOf(MessageType.MSG_EVENT),
				String.valueOf(MessageType.MSG_PLAN) };
		Cursor cursor = context.getContentResolver().query(CONTENT_URI,
				CONTENT_PROJECTION, query, queryValue, null);
		List<NewsContent> nclist = null;
		if (null != cursor && cursor.moveToFirst()) {
			nclist = new ArrayList<NewsContent>();
			NewsContent nc = getContent(cursor, NewsContent.class);
			nclist.add(nc);
			while (cursor.moveToNext()) {
				nc = getContent(cursor, NewsContent.class);
				if (null != nc) {
					nclist.add(nc);
				}
			}
			cursor.close();
		}

		return nclist;
	}

	public static int getUnReadCount(Context context) {
		int count = 0;
		Cursor cursor = queryContentByUnRead(context);
		if (null != cursor && cursor.moveToFirst()) {
			count = cursor.getCount();
			cursor.close();
		}
		return count;
	}

	public static List<NewsContent> getAllContent(Context context) {
		Cursor cursor = queryContentByPDate(context);
		List<NewsContent> nclist = null;
		if (null != cursor) {
			nclist = new ArrayList<NewsContent>();
			while (cursor.moveToFirst()) {
				NewsContent nc = getContent(cursor, NewsContent.class);
				nclist.add(nc);
			}
			cursor.close();
		}
		return nclist;
	}

	public static NewsContent getContentByCursor(Cursor cursor) {
		NewsContent nc = null;
		if (null != cursor) {
			if (cursor.moveToFirst()) {
				nc = getContent(cursor, NewsContent.class);
			}
			cursor.close();
		}
		return nc;
	}
	
	public static NewsContent getContentByURI(Context cxt, Uri uri) {
		Cursor cursor = NewsContent.queryMsgByURI(cxt, uri);
		NewsContent nc = null;
		if (null != cursor) {
			nc = NewsContent.getContentByCursor(cursor);
		}
		return nc;
	}

	public String mTitile;
	public String mContent;
	public String mPublish;
	public String mStartUri;
	public long mPData;
	public int mType;
	public int mPlanState;
	public int mLevel;
	public String mBegin;
	public String mEnd;
	public int mIsRead;
	public int mIsAdd2;
	public int mEventId;
	public String mTypeName;
	public int mBusType;
	public String mBusName;
	
	public String mCommandId;
}
