//package com.aess.aemm.data;
////import com.aess.aemm.data.ProfileContent.ApkProfileColumns;
////import com.aess.aemm.data.ProfileContent.ApkProfile;
////import com.aess.aemm.data.ProfileContent.ConfigProfileContent;
////import com.aess.aemm.data.ProfileContent.ConfigsColumns;
////import com.aess.aemm.data.ProfileContent.GPSLocationColumns;
////import com.aess.aemm.data.ProfileContent.GPSLocationContent;
////import com.aess.aemm.data.ProfileContent.FunctionProfileContent;
////import com.aess.aemm.data.ProfileContent.FunctionProfileColumns;
////import com.aess.aemm.data.ProfileContent.WebClipProfileContent;
////import com.aess.aemm.data.ProfileContent.WebclipColumns;
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
//import android.net.Uri;
//import android.util.Log;
//
//
//public class ProfileContentProvider extends ContentProvider {
//	static final String PROVIDER_NAME = "com.aess.aemm.data.profile";
//	public static final String _ID = "_id";	
//	private static SQLiteDatabase database;
//	
//	private static final String DATABASE_NAME = "profile.db";
//
//	private static final int ITEMS = 1;
//	private static final int ITEM = 2;
//	private static UriMatcher uriMatcher;
//	private static final int DATABASE_VERSION = 3;
//	
//	private static final String[] TABLE_NAMES = {		 
////		   ApkProfile.TABLE_NAME,
////		   FunctionProfileContent.TABLE_NAME,
////		   ConfigProfileContent.TABLE_NAME,
////		   WebClipProfileContent.TABLE_NAME,
////		   GPSLocationContent.TABLE_NAME,
//	    };
//	 
//	 private static final int APKPORFILE_BASE = 0;
//	 private static final int APKPORFILE = APKPORFILE_BASE;
//	 private static final int APKPORFILE_ID = APKPORFILE_BASE + 1;
//	 
//	 private static final int FUNCTIONPROFILE_BASE = 0x1000;
//	 private static final int FUNCTIONPROFILE = FUNCTIONPROFILE_BASE;
//	 private static final int FUNCTIONPROFILE_ID = FUNCTIONPROFILE_BASE + 1;
//	    
//	 private static final int PROFILES_BASE = 0x2000;
//	 private static final int PROFILES = PROFILES_BASE;
//	 private static final int PROFILES_ID = PROFILES_BASE + 1;
//	 
//	 
//	 private static final int WEBCLIPPROFILE_BASE = 0x3000;
//	 private static final int WEBCLIPPROFILE = WEBCLIPPROFILE_BASE;
//	 private static final int WEBCLIPPROFILE_ID = WEBCLIPPROFILE_BASE + 1;
//	 
//	 
//	 private static final int GPSLOCATION_BASE = 0x4000;
//	 private static final int GPSLOCATION = GPSLOCATION_BASE;
//	 private static final int GPSLOCATION_ID = GPSLOCATION_BASE + 1;
//	 
//	 private static final int BASE_SHIFT = 12;  // 12 bits to the base type: 0, 0x1000, 0x2000, etc.
//
//	    
//	static {
//		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//		
////		uriMatcher.addURI(PROVIDER_NAME, ApkProfile.TABLE_NAME, APKPORFILE);
////		uriMatcher.addURI(PROVIDER_NAME, ApkProfile.TABLE_NAME + "/#", APKPORFILE_ID);
//		
////		uriMatcher.addURI(PROVIDER_NAME, FunctionProfileContent.TABLE_NAME, FUNCTIONPROFILE);
////		uriMatcher.addURI(PROVIDER_NAME, FunctionProfileContent.TABLE_NAME + "/#", FUNCTIONPROFILE_ID);
//		
////		uriMatcher.addURI(PROVIDER_NAME, ConfigProfileContent.TABLE_NAME, PROFILES);
////		uriMatcher.addURI(PROVIDER_NAME, ConfigProfileContent.TABLE_NAME + "/#", PROFILES_ID);
//	
////		uriMatcher.addURI(PROVIDER_NAME, WebClipProfileContent.TABLE_NAME, WEBCLIPPROFILE);
////		uriMatcher.addURI(PROVIDER_NAME, WebClipProfileContent.TABLE_NAME + "/#", WEBCLIPPROFILE_ID);
//		
////		uriMatcher.addURI(PROVIDER_NAME, GPSLocationContent.TABLE_NAME, GPSLOCATION);
////		uriMatcher.addURI(PROVIDER_NAME, GPSLocationContent.TABLE_NAME + "/#", GPSLOCATION_ID);
//	}
//	
//	@Override
//	public int delete(Uri uri, String selection, String[] selectionArgs) {
//		int match = uriMatcher.match(uri);
//		int table = match >> BASE_SHIFT;
//		switch (match) {
//		case APKPORFILE_ID:
//		case WEBCLIPPROFILE_ID:
//		case PROFILES_ID:
//		case FUNCTIONPROFILE_ID:
//		case GPSLOCATION_ID:
//			if (selection != null)
//				return database.delete(TABLE_NAMES[table],
//						_ID + "=" + uri.getPathSegments().get(1) + " and ("
//								+ selection + ")", selectionArgs);
//			else
//				return database.delete(TABLE_NAMES[table],
//						_ID + "=" + uri.getPathSegments().get(1), null);
//		case APKPORFILE:
//		case WEBCLIPPROFILE:
//		case PROFILES:
//		case FUNCTIONPROFILE:
//		case GPSLOCATION:
//			return database.delete(TABLE_NAMES[table], selection, selectionArgs);
//		default:
//			throw new IllegalArgumentException("Unsupported URI: " + uri);
//		}
//	}
//
//	@Override
//	public String getType(Uri uri) {
//		switch (uriMatcher.match(uri)) {
//		case ITEMS:
//			return "vnd.android.cursor.dir/vnd.easymorse.mycp";
//		case ITEM:
//			return "vnd.android.cursor.item/vnd.easymorse.mycp";
//		default:
//			throw new IllegalArgumentException("Unsupported URI: " + uri);
//		}
//	}
//
//	@Override
//	public Uri insert(Uri uri, ContentValues contentValues) {
//		int match = uriMatcher.match(uri);
//		int table = match >> BASE_SHIFT;
//		long rowId = 0;
//		Uri resultUri = null;
//		switch (match) {
//		case APKPORFILE:
//		case WEBCLIPPROFILE:
//		case PROFILES:
//		case FUNCTIONPROFILE:
//		case GPSLOCATION:
//			rowId = database.insert(TABLE_NAMES[table], "", contentValues);
//			resultUri = ContentUris.withAppendedId(uri, rowId);
//			break;
//		 }
//	
//		if (!(rowId > 0)) {
//			throw new SQLException("failed to insert row into " + uri);
//		}
//
//		getContext().getContentResolver().notifyChange(resultUri, null);
//		return resultUri;
//	}
//
//	@Override
//	public boolean onCreate() {
//
//		database = new MyDatabaseHelper(getContext(), DATABASE_NAME, null,
//				DATABASE_VERSION).getWritableDatabase();
//		return database != null;
//	}
//
//	@Override
//	public Cursor query(Uri uri, String[] projection, String selection,
//			String[] selectionArgs, String sortOrder) {
//		
//		int match = uriMatcher.match(uri);
//		int table = match >> BASE_SHIFT;
//				
//
//		switch (match) {
//		case APKPORFILE:
//		case WEBCLIPPROFILE:
//		case PROFILES:
//		case FUNCTIONPROFILE:
//		case GPSLOCATION:
//			return database.query(TABLE_NAMES[table], projection, selection,
//					selectionArgs, null, null, sortOrder);
//			
//		case APKPORFILE_ID:
//		case WEBCLIPPROFILE_ID:
//		case PROFILES_ID:
//		case FUNCTIONPROFILE_ID:
//		case GPSLOCATION_ID:
//			return database.query(TABLE_NAMES[table], projection, _ID + "="
//					+ uri.getPathSegments().get(1), selectionArgs, null, null,
//					null);
//		default:
//			throw new IllegalArgumentException("unknown uri: " + uri);
//		}
//	}
//
//	@Override
//	public int update(Uri uri, ContentValues contentValues, String selection,
//			String[] selectionArgs) {
//
//		int match = uriMatcher.match(uri);
//		int table = match >> BASE_SHIFT;
//		switch (match) {
//		case APKPORFILE_ID:
//		case WEBCLIPPROFILE_ID:
//		case PROFILES_ID:
//		case FUNCTIONPROFILE_ID:
//		case GPSLOCATION_ID:
//			if (selection != null)
//				return database.update(TABLE_NAMES[table], contentValues,
//					_ID + "=" + uri.getPathSegments().get(1) + " and ("
//							+ selection + ")", selectionArgs);
//			else
//				return database.update(TABLE_NAMES[table], contentValues,
//					_ID + "=" + uri.getPathSegments().get(1),null);
//			
//		case APKPORFILE:
//		case WEBCLIPPROFILE:
//		case PROFILES:
//		case FUNCTIONPROFILE:
//		case GPSLOCATION:
//			Log.i("Test","ITEMS" + uri);
//			return database.update(TABLE_NAMES[table], contentValues, selection,
//					selectionArgs);
//		default:
//			throw new IllegalArgumentException("unknown uri: " + uri);
//		}
//	}
//	
//	private static class MyDatabaseHelper extends SQLiteOpenHelper {
//
//		public MyDatabaseHelper(Context context, String name,
//				CursorFactory factory, int version) {
//			super(context, name, factory, version);
//		}
//
//		@Override
//		public void onCreate(SQLiteDatabase database) {			
////			createApkProfiledb(database);		
////		    createProfilesdb(database);		
////		    createFunctionProfiledb(database);
////			createWebClipProfiledb(database);
////			createGPSLocationdb(database);
//			//createAppStatusdb(database);
//		}
//
//		@Override
//		public void onUpgrade(SQLiteDatabase database, int oldVersion,
//				int newVersion) {
//			Log.w("mycp", "updating database from version " + oldVersion
//					+ " to " + newVersion);
////			database.execSQL("drop table " + ConfigProfileContent.TABLE_NAME);
////			database.execSQL("drop table " + WebClipProfileContent.TABLE_NAME);
//			onCreate(database);
//		}
//		
////		void createProfilesdb(SQLiteDatabase database)
////		{
////			String s = " (" +  "_id integer primary key autoincrement, "
////            + ConfigsColumns.CID + " text, "
////            + ConfigsColumns.UUID + " text, "
////            + ConfigsColumns.NAME + " text, "
////            + ConfigsColumns.TYPE + " text, "
////            + ConfigsColumns.VERSION + " text, "
////            + ConfigsColumns.NOTE + " text "
////            + ");";
////			database.execSQL("create table if not exists " + ConfigProfileContent.TABLE_NAME + s);		
////		}
//
//		//Enterprise App table
////		void createApkProfiledb(SQLiteDatabase database)
////		{
////			String s = " (" +  "_id integer primary key autoincrement, "
////            + ApkProfileColumns.APK_ID + " text, "
////            + ApkProfileColumns.APK_NAME + " text, "
////            + ApkProfileColumns.APK_URL + " text, "
////            + ApkProfileColumns.APK_DESC + " text, "
////            + ApkProfileColumns.APK_VERSION + " text, "
////            + ApkProfileColumns.APK_VERSION_CLIENT + " text, "
////            + ApkProfileColumns.APK_FLAG + " integer, "   //new app, install, uninstall, remote-uninstall, downloading
////            + ApkProfileColumns.APK_SCREEN + " integer, "
////            + ApkProfileColumns.APK_ROW + " integer, "
////            + ApkProfileColumns.APK_COLUMN + " integer, "
////            + ApkProfileColumns.ICONCOLOR + " blob, "
////            + ApkProfileColumns.ICONGREY + " blob, "
////            + ApkProfileColumns.APK_FILE_NAME + " text, "
////            + ApkProfileColumns.APK_FILE_PATH + " text, "
////            + ApkProfileColumns.APK_PACKAGE_NAME + " text, "
////            + ApkProfileColumns.APK_DISABLED + " integer, "   //whether the app is in black-list. 1--disable 0--enable
////            + ApkProfileColumns.INSTALLED_TIME + " text, "
////            + ApkProfileColumns.LAST_START_TIME + " text, "
////            + ApkProfileColumns.LAST_EXIT_TIME + " text, "
////            + ApkProfileColumns.PUBLISHED + " interger, "   //whether the app is published.  1--published 0- no published
////            + ApkProfileColumns.IS_REMOVED + " interger "   //whether the installed app is to be removed. default is 0. 1 is gong to be removed.
////            + ");";
////			database.execSQL("create table if not exists " + ApkProfile.TABLE_NAME + s);
////		}
//		
//		//Non-Enterprise App table
////		void createFunctionProfiledb(SQLiteDatabase database)
////		{
////			String s = " (" +  "_id integer primary key autoincrement, "
////				+ FunctionProfileColumns.FUNCTION_ID + " text, "
////				+ FunctionProfileColumns.FUNCTION_NAME + " text, "
////				+ FunctionProfileColumns.FUNCTION_VERSION + " text, "
////				+ FunctionProfileColumns.FUNCTION_DISABLED + " integer, "   //whether the app is in black-list
////				+ FunctionProfileColumns.FUNCTION_INSTALLED_TIME + " text, "
////				+ FunctionProfileColumns.FUNCTION_LAST_START_TIME + " text, "
////				+ FunctionProfileColumns.FUNCTION_LAST_EXIT_TIME + " text "
////				+ ");";
////			database.execSQL("create table if not exists " + FunctionProfileContent.TABLE_NAME + s);
////			
////		}		
//		
////		void createWebClipProfiledb(SQLiteDatabase database)
////		{
////			String s = " (" +  "_id integer primary key autoincrement, "
////				+ WebclipColumns.IDENTIFIER + " text, "
////				+ WebclipColumns.DISPAYNAME + " text, "
////				+ WebclipColumns.URL + " text, "
////				+ WebclipColumns.ICON + " blob, "
////				+ WebclipColumns.ISREMOVEABLE + " integer, "
////				+ WebclipColumns.VERSION + " text "
////				+ ");";
////			database.execSQL("create table if not exists " + WebClipProfileContent.TABLE_NAME + s);
////			
////		}		
//		
////		void createGPSLocationdb(SQLiteDatabase database)
////		{
////			String s = " (" +  "_id integer primary key autoincrement, "
////				+ GPSLocationColumns.GPS_TIME + " text, "
////				+ GPSLocationColumns.GPS_LONGITUDE + " text, "
////				+ GPSLocationColumns.GPS_LATITUDE + " text, "
////				+ GPSLocationColumns.GPS_ISREAD + " int "
////				+ ");";
////			database.execSQL("create table if not exists " + GPSLocationContent.TABLE_NAME + s);
////			
////		}		
//	}
//
//}
