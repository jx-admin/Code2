package com.act.sctc.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * @author junxu.wang
 *
 */
public class DBProvider extends ContentProvider {
	public static final String TAG=DBProvider.class.getSimpleName();

	/** 数据库帮助类 */
	private DBHelper		 mDBHelper	=	null;
	
	private ContentResolver  contentResolver;// =getContext().getContentResolver();
	
	/**
	 * Init DB
	 */
	@Override
	public boolean onCreate() {
		// 实例化数据库帮助类
		mDBHelper = DBHelper.getInstance(getContext());
		contentResolver = getContext().getContentResolver();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		return db!=null;
	}
	
	
	// 定义MIME类型，访问单个记录  
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.provider.cstc";  
	// 访问数据集  
	public static final String CONTENT_ITEM = "vnd.android.cursor.dir/vnd.providers.cstc";  

	@Override
	public String getType(Uri uri) {
		switch (uri.getPathSegments().size()) {
		// 根据指定条件删除
		case 1:
			return CONTENT_ITEM_TYPE;
		case 2:
			return CONTENT_ITEM;
		}
		return null;
	}

	
	private void notifyChange(Uri uri) {
		contentResolver.notifyChange(uri, null);
	}
	
	/**
	 * 添加方法(non-Javadoc) [这里有一个疑问：从Uri中获取表名，是否用
	 * UriMatcher匹配一下哪个表的路径，获取表明是用uri.getLastPathSegment
	 * ()还是uri.getPathSegments().get(0)？]
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 *      android.content.ContentValues)
	 */
	public Uri insert(Uri uri, ContentValues values) {
		// 获得数据库实例
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		// 插入数据，返回行ID
		long rowId = db.insert(uri.getLastPathSegment(), null, values);
		// 如果插入成功返回uri
		if (rowId > 0) {
			Uri empUri = ContentUris.withAppendedId(uri, rowId);
			contentResolver.notifyChange(empUri, null);
			notifyChange(uri);
			return empUri;
		}
		return null;
	}
	
	/**
	 * 删除方法 (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 *      java.lang.String, java.lang.String[])
	 */
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// 获得数据库实例
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		// 获得数据库实例
		int count;
		switch (uri.getPathSegments().size()) {
		// 根据指定条件删除
		case 1:
//		case PRODUCT_PHONE:
			count = db.delete(uri.getLastPathSegment(), selection,
					selectionArgs);
			notifyChange(uri);
			break;
		// 根据指定条件和ID删除
		case 2:
//		case PRODUCT_PHONE_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(uri.getPathSegments().get(0), BaseColumns._ID
					+ "="
					+ noteId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			notifyChange(uri);
			break;
		default:
			throw new IllegalArgumentException("unkown URI " + uri);
		}
		contentResolver.notifyChange(uri, null);
		return count;
	}
	
	/**
	 * 更新方法 (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 *      android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// 获得数据库实例
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int count;
		switch (uri.getPathSegments().size()) {
		// 根据指定条件更新
		case 1:
//		case PRODUCT_PHONE:
			count = db.update(uri.getLastPathSegment(), values, selection,
					selectionArgs);
			notifyChange(uri);
			break;
		// 根据指定条件和ID更新
		case 2:
//		case PRODUCT_PHONE_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(
					uri.getPathSegments().get(0),
					values,
					BaseColumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : "")
									, selectionArgs);
			notifyChange(uri);
			break;
		default:
			throw new IllegalArgumentException("错误的 URI " + uri);
		}
		contentResolver.notifyChange(uri, null);
		return count;
	}
	
	/**
	 * 查询方法 [疑问：qb.setProjectionMap(callProjectionMap);？] (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 *      java.lang.String[], java.lang.String, java.lang.String[],
	 *      java.lang.String)
	 */
	public Cursor query(Uri uri, String[] columns, String selection,
			String[] selectionArgs, String orderBy) {
		// 获得数据库实例
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		Cursor c =null;
		switch(uri.getPathSegments().size()){
		case 1:
			c =db.query(uri.getLastPathSegment(), columns, selection, selectionArgs, null, null, orderBy);
			break;
		case 2:
			c =db.query(uri.getPathSegments().get(0), columns, BaseColumns._ID
					+ "="
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? " AND ("
							+ selection + ')' : ""), selectionArgs,null, null,  orderBy);
			break;
		}
		return c;
		/*
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (uri.getPathSegments().size()) {
		// 查询所有
		case PRODUCT_PHONE:
			qb.setTables(uri.getLastPathSegment());
			qb.setProjectionMap(callProjectionMap);
			break;
		// 根据ID查询
		case PRODUCT_PHONE_ID:
			qb.setTables(uri.getLastPathSegment());
			qb.setProjectionMap(callProjectionMap);
			qb.appendWhere(BaseColumns._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Uri错误！ " + uri);
		}

		// 返回游标集合
		Cursor c = qb.query(db, columns, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(contentResolver, uri);
		return c;
		*/
	}

}
