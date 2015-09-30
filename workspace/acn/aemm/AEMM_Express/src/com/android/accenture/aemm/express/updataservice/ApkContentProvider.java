package com.android.accenture.aemm.express.updataservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

public class ApkContentProvider extends ContentProvider {
	private static final String PROVIDER_NAME = "com.aemm.demo.cp.apk";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/apktable");

	public static final String _ID = "id";
	
	public static final String APK_ID = "apk_id";

	public static final String APK_NAME = "apk_name";

	public static final String APK_URL = "apk_url";
	
	public static final String APK_DESC = "apk_desc";
	
	public static final String APK_VERSION = "apk_version";
	
	public static final String APK_VERSION_CLIENT="apk_version_client";
	
	public static final String APK_FLAG = "flag";

	public static final String ICONCOLOR = "icon_color";
	
	public static final String ICONGREY = "icon_grey";
	
	public static final String APK_PACKAGE_NAME = "apk_package_name";
	public static final String APK_FILE_NAME = "apk_file_name";
	
	public static final String APK_FILE_PATH = "apk_file_path";
	
	
	private static SQLiteDatabase database;

	private static final String TABLE_EMPERORS = "apktable";
	private static final String DATABASE_NAME = "data.db";

	private static final int ITEMS = 1;

	private static final int ITEM = 2;

	private static UriMatcher uriMatcher;

	private static final int DATABASE_VERSION = 3;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, TABLE_EMPERORS, ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, TABLE_EMPERORS + "/#", ITEM);
	}



	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case ITEM:
			return database.delete(TABLE_EMPERORS,
					_ID + "=" + uri.getPathSegments().get(1) + " and ("
							+ selection + ")", selectionArgs);
		case ITEMS:
			return database.delete(TABLE_EMPERORS, selection, selectionArgs);
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
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
		long rowId = database.insert(TABLE_EMPERORS, "", contentValues);

		if (!(rowId > 0)) {
			throw new SQLException("failed to insert row into " + uri);
		}

		Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
		getContext().getContentResolver().notifyChange(_uri, null);
		return _uri;

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
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			return database.query(TABLE_EMPERORS, projection, selection,
					selectionArgs, null, null, sortOrder);
		case ITEM:
			return database.query(TABLE_EMPERORS, projection, _ID + "="
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
		switch (uriMatcher.match(uri)) {
		case ITEM:
			return database.update(TABLE_EMPERORS, contentValues,
					_ID + "=" + uri.getPathSegments().get(1) + " and ("
							+ selection + ")", selectionArgs);
		case ITEMS:
			return database.update(TABLE_EMPERORS, contentValues, selection,
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
			database.execSQL("create table if not exists apktable("
					+ " id integer primary key autoincrement," + " apk_id text,"
					+ " apk_name text," + " apk_url text,"
					+  "apk_desc text," + " apk_version text," + "apk_version_client text," + " flag integer,"
					+ " apk_file_name text," + "apk_file_path text,"
					+ " icon_color blob," + " icon_grey blob"+ ");");

			SQLiteStatement statement = database
					.compileStatement("insert into apktable(apk_id,apk_name,apk_url,apk_desc,apk_version,apk_version_client,flag,apk_file_name,apk_file_path,icon_color,icon_grey) " +
													"values(?,?,?,?,?,?,?,?,?,?)");
			int index = 1;
			statement.bindString(index++, "com.accenture.app1");
			statement.bindString(index++, "app1");
			statement.bindString(index++, "http://Download_URL1");
			statement.bindString(index++, "Some descriptions1");
			statement.bindString(index++, "1.0");
			
			statement.bindLong(index++, 0);
			statement.bindNull(index++);
			statement.bindNull(index++);
			//statement.bindBlob(index++, getImageData(R.drawable.icon)); 
			//statement.bindBlob(index++, getImageData(R.drawable.icon)); 
			
			statement.execute();

			
			
			index = 1;
			statement.bindString(index++, "com.accenture.app2");
			statement.bindString(index++, "app2");
			statement.bindString(index++, "http://Download_URL2");
			statement.bindString(index++, "Some descriptions2");
			statement.bindString(index++, "1.1");
			statement.bindLong(index++, 0);
			statement.bindNull(index++);
			statement.bindNull(index++);
			//statement.bindBlob(index++, getImageData(R.drawable.icon)); 
			//statement.bindBlob(index++, getImageData(R.drawable.icon)); 
			statement.execute();
			
			statement.close();
			
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w("mycp", "updating database from version " + oldVersion
					+ " to " + newVersion);
			database.execSQL("drop table if exists emperors");
			onCreate(database);
		}
		
		private byte[] getImageData(int rawId) { 
		    InputStream inputStream = context.getResources().openRawResource( 
		            rawId); 
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



	}


}
