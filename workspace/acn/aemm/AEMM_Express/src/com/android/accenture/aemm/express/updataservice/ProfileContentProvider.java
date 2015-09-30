package com.android.accenture.aemm.express.updataservice;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileColumns;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;
import com.android.accenture.aemm.express.updataservice.ProfileContent.CertificateColumns;
import com.android.accenture.aemm.express.updataservice.ProfileContent.CertificateContent;
import com.android.accenture.aemm.express.updataservice.ProfileContent.EmailColumns;
import com.android.accenture.aemm.express.updataservice.ProfileContent.EmailProfileContent;
import com.android.accenture.aemm.express.updataservice.ProfileContent.Profiles;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ProfilesColumns;
import com.android.accenture.aemm.express.updataservice.ProfileContent.RootProfile;
import com.android.accenture.aemm.express.updataservice.ProfileContent.RootProfileColumns;
import com.android.accenture.aemm.express.updataservice.ProfileContent.WebClipProfileContent;
import com.android.accenture.aemm.express.updataservice.ProfileContent.WebclipColumns;



import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.util.Log;



public class ProfileContentProvider extends ContentProvider {
	static final String PROVIDER_NAME = "com.android.accenture.aemm.express.cp.profile";

	//public static final Uri CONTENT_URI = Uri.parse("content://"
			//+ PROVIDER_NAME + "/RootProfile");
	
	
	public static final String _ID = "_id";
	
	
	//public static final String createTable
	private static SQLiteDatabase database;

	//private static final String TABLE_ROOT_PROFILE = "RootProfile";
	//private static final String TABLE_EMAIL_PROFILE = "EmailProfile";
	
	private static final String DATABASE_NAME = "profile.db";

	private static final int ITEMS = 1;

	private static final int ITEM = 2;

	private static UriMatcher uriMatcher;

	private static final int DATABASE_VERSION = 3;
	
	 private static final String[] TABLE_NAMES = {
		 
		   ApkProfileContent.TABLE_NAME,
		   RootProfile.TABLE_NAME,	
		   Profiles.TABLE_NAME,
		   WebClipProfileContent.TABLE_NAME
	    //   EmailProfileContent.TABLE_NAME
	       
	    };
	 
	 private static final int APKPORFILE_BASE = 0;
	 private static final int APKPORFILE = APKPORFILE_BASE;
	 private static final int APKPORFILE_ID = APKPORFILE_BASE + 1;
	 
	 private static final int ROOTPROFILE_BASE = 0x1000;
	 private static final int ROOTPROFILE = ROOTPROFILE_BASE;
	 private static final int ROOTPROFILE_ID = ROOTPROFILE_BASE + 1;
	    
	 private static final int PROFILES_BASE = 0x2000;
	 private static final int PROFILES = PROFILES_BASE;
	 private static final int PROFILES_ID = PROFILES_BASE + 1;
	 
	 
	 private static final int WEBCLIPPROFILEBASE = 0x3000;
	 private static final int WEBCLIPPROFILE = WEBCLIPPROFILEBASE;
	 private static final int WEBCLIPPROFILE_ID = WEBCLIPPROFILEBASE + 1;
	 
	 
	 private static final int CERTIFICATEBASE = 0x4000;
	 private static final int CERTIFICATE = CERTIFICATEBASE;
	 private static final int CERTIFICATEBASE_ID = CERTIFICATEBASE + 1;
	 
	 
	 private static final int BASE_SHIFT = 12;  // 12 bits to the base type: 0, 0x1000, 0x2000, etc.

	    
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		uriMatcher.addURI(PROVIDER_NAME, ApkProfileContent.TABLE_NAME, APKPORFILE);
		uriMatcher.addURI(PROVIDER_NAME, ApkProfileContent.TABLE_NAME + "/#", APKPORFILE_ID);
		
		uriMatcher.addURI(PROVIDER_NAME, RootProfile.TABLE_NAME, ROOTPROFILE);
		uriMatcher.addURI(PROVIDER_NAME, RootProfile.TABLE_NAME + "/#", ROOTPROFILE_ID);
		
		uriMatcher.addURI(PROVIDER_NAME, Profiles.TABLE_NAME, PROFILES_BASE);
		uriMatcher.addURI(PROVIDER_NAME, Profiles.TABLE_NAME + "/#", PROFILES_ID);
	
		uriMatcher.addURI(PROVIDER_NAME, WebClipProfileContent.TABLE_NAME, WEBCLIPPROFILE);
		uriMatcher.addURI(PROVIDER_NAME, WebClipProfileContent.TABLE_NAME + "/#", WEBCLIPPROFILE_ID);
	
		
		uriMatcher.addURI(PROVIDER_NAME, CertificateContent.TABLE_NAME, PROFILES_BASE);
		uriMatcher.addURI(PROVIDER_NAME, CertificateContent.TABLE_NAME + "/#", PROFILES_ID);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;
		long rowId = 0;
		switch (match) {
		case APKPORFILE_ID:
		case ROOTPROFILE_ID:
		case PROFILES_ID:
		case WEBCLIPPROFILE_ID:
		case CERTIFICATEBASE_ID:
			if (selection != null)
				return database.delete(TABLE_NAMES[table],
						_ID + "=" + uri.getPathSegments().get(1) + " and ("
								+ selection + ")", selectionArgs);
			else
				return database.delete(TABLE_NAMES[table],
						_ID + "=" + uri.getPathSegments().get(1), null);
		case APKPORFILE:
		case ROOTPROFILE:
		case PROFILES:
		case WEBCLIPPROFILE:
		case CERTIFICATEBASE:

			return database.delete(TABLE_NAMES[table], selection, selectionArgs);
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;
		long rowId = 0;
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			return "vnd.android.cursor.dir/vnd.easymorse.mycp";
		case ITEM:
			return "vnd.android.cursor.item/vnd.easymorse.mycp";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		// TODO Auto-generated method stub
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;
		long rowId = 0;
		Uri resultUri = null;
		switch (match) {
		case APKPORFILE:
		case ROOTPROFILE:
		case PROFILES:
		case WEBCLIPPROFILE:
		case CERTIFICATEBASE:
			rowId = database.insert(TABLE_NAMES[table], "", contentValues);
			resultUri = ContentUris.withAppendedId(uri, rowId);
			break;
		 }
	
		if (!(rowId > 0)) {
			throw new SQLException("failed to insert row into " + uri);
		}

		getContext().getContentResolver().notifyChange(resultUri, null);
		return resultUri;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		database = new MyDatabaseHelper(getContext(), DATABASE_NAME, null,
				DATABASE_VERSION).getWritableDatabase();
		return database != null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;
				
		// TODO Auto-generated method stub
		switch (match) {
		case APKPORFILE:
		case ROOTPROFILE:
		case PROFILES:
		case WEBCLIPPROFILE:
		case CERTIFICATEBASE:
			return database.query(TABLE_NAMES[table], projection, selection,
					selectionArgs, null, null, sortOrder);
			
		case APKPORFILE_ID:
		case ROOTPROFILE_ID:
		case PROFILES_ID:
		case WEBCLIPPROFILE_ID:
		case CERTIFICATEBASE_ID:
			return database.query(TABLE_NAMES[table], projection, _ID + "="
					+ uri.getPathSegments().get(1), selectionArgs, null, null,
					null);
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int match = uriMatcher.match(uri);
		int table = match >> BASE_SHIFT;
		switch (match) {
		case APKPORFILE_ID:
		case ROOTPROFILE_ID:
		case PROFILES_ID:
		case WEBCLIPPROFILE_ID:
		case CERTIFICATEBASE_ID:
			if (selection != null)
				return database.update(TABLE_NAMES[table], contentValues,
					_ID + "=" + uri.getPathSegments().get(1) + " and ("
							+ selection + ")", selectionArgs);
			else
				return database.update(TABLE_NAMES[table], contentValues,
					_ID + "=" + uri.getPathSegments().get(1),null);
			
		case APKPORFILE:
		case ROOTPROFILE:
		case PROFILES:
		case WEBCLIPPROFILE:
		case CERTIFICATEBASE:
			Log.i("Test","ITEMS" + uri);
			return database.update(TABLE_NAMES[table], contentValues, selection,
					selectionArgs);
		default:
			throw new IllegalArgumentException("unknown uri: " + uri);
		}
	}
	
	private static class MyDatabaseHelper extends SQLiteOpenHelper {

		private Context context; 

		public MyDatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			 this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			
			createApkProfiledb(database);
		
			createWebClipProfiledb(database);
		    createProfilesdb(database);
			//createCertificateProfiledb(database);
			//createEmailProfiledb(database);
		
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w("mycp", "updating database from version " + oldVersion
					+ " to " + newVersion);
			database.execSQL("drop table " + Profiles.TABLE_NAME);
			database.execSQL("drop table " + WebClipProfileContent.TABLE_NAME);
			//database.execSQL("drop table if exists emperors");
			onCreate(database);
		}
		
		void createRootProfiledb(SQLiteDatabase database)
		{
			String s = " (" +  "_id integer primary key autoincrement, "
            + RootProfileColumns.INDENTIFIER + " text, "
            + RootProfileColumns.DISPLAYNAME + " text, "
            + RootProfileColumns.DESCRIPTION + " text, "
            + RootProfileColumns.VERSION + " text, "
            + RootProfileColumns.UUID + " text, "
            + RootProfileColumns.TYPE + " text, "
            + RootProfileColumns.ORGANZATION + " text "
            + ");";
			database.execSQL("create table if not exists " + RootProfile.TABLE_NAME + s);
			
		
		}
		
		void createProfilesdb(SQLiteDatabase database)
		{
			String s = " (" +  "_id integer primary key autoincrement, "
            + ProfilesColumns.CID + " text, "
            + ProfilesColumns.UUID + " text, "
            + ProfilesColumns.NAME + " text, "
            + ProfilesColumns.TYPE + " text, "
            + ProfilesColumns.VERSION + " text, "
            + ProfilesColumns.NOTE + " text "
            + ");";
			database.execSQL("create table if not exists " + Profiles.TABLE_NAME + s);
			
		
		}
	
		void createEmailProfiledb(SQLiteDatabase database)
		{
			String s = " (" +  "_id integer primary key autoincrement, "
            + EmailColumns.EMAILADDRESS + " text, "
            + EmailColumns.HOST + " text, "
            + EmailColumns.PASSWORD + " text, "
            + EmailColumns.SSL + " boolean, "
            + EmailColumns.USERNAME + " text "
            + ");";
			database.execSQL("create table if not exists " +  EmailProfileContent.TABLE_NAME + s);
		}
		void createApkProfiledb(SQLiteDatabase database)
		{
			String s = " (" +  "_id integer primary key autoincrement, "
            + ApkProfileColumns.APK_ID + " text, "
            + ApkProfileColumns.APK_NAME + " text, "
            + ApkProfileColumns.APK_URL + " text, "
            + ApkProfileColumns.APK_DESC + " text, "
            + ApkProfileColumns.APK_VERSION + " text, "
            + ApkProfileColumns.APK_VERSION_CLIENT + " text, "
            + ApkProfileColumns.APK_FLAG + " integer, "
            + ApkProfileColumns.ICONCOLOR + " blob ,"
            + ApkProfileColumns.ICONGREY + " blob ,"
            + ApkProfileColumns.APK_FILE_NAME + " text, "
            + ApkProfileColumns.APK_FILE_PATH + " text, "
            + ApkProfileColumns.APK_PACKAGE_NAME + " text ,"
            + ApkProfileColumns.APK_INSTALLENABLED + " text "
            + ");";
			database.execSQL("create table if not exists " + ApkProfileContent.TABLE_NAME + s);
		}
		
		void createWebClipProfiledb(SQLiteDatabase database)
		{
			String s = " (" +  "_id integer primary key autoincrement, "
				+ WebclipColumns.IDENTIFIER + " text, "
				+ WebclipColumns.DISPAYNAME + " text, "
				+ WebclipColumns.URL + " text, "
				+ WebclipColumns.ICON + " blob, "
				+ WebclipColumns.ISREMOVEABLE + " integer, "
				+ WebclipColumns.VERSION + " text "
				+ ");";
			database.execSQL("create table if not exists " + WebClipProfileContent.TABLE_NAME + s);
			
		}
		
		void createCertificateProfiledb(SQLiteDatabase database)
		{
			String s = " (" +  "_id integer primary key autoincrement, "
				+ CertificateColumns.UUID + " text, "
				+ CertificateColumns.NAME + " text, "
				+ CertificateColumns.TYPE + " text, "
				+ CertificateColumns.PASSWORD + " text, "
				+ CertificateColumns.DATA + " blob "
				+ ");";
			database.execSQL("create table if not exists " + CertificateContent.TABLE_NAME + s);
			
		}
	}

}
