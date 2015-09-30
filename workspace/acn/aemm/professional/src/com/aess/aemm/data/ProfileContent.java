//package com.aess.aemm.data;
//
//
////import android.content.ContentUris;
////import android.content.ContentValues;
////import android.content.Context;
////import android.database.Cursor;
////import android.net.Uri;
//
//
//public abstract class ProfileContent {
////	public static final String AUTHORITY = ProfileContentProvider.PROVIDER_NAME;
////	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
////
////	// Newly created objects get this id
////	private static final int NOT_SAVED = -1;
////	// All classes share this
////	public static final String RECORD_ID = "_id";
////	// Lazily initialized uri for this Content
////	private Uri mUri = null;
////	public long mId = NOT_SAVED;
////	public static final String[] ID_PROJECTION = new String[] { RECORD_ID };
////	public static final int ID_PROJECTION_COLUMN = 0;
////
////	// Write the Content into a ContentValues container
////	public abstract ContentValues toContentValues();
////
////	// Read the Content from a ContentCursor
////	public abstract <T extends ProfileContent> T restore(Cursor cursor);
////
////	// The base Uri that this piece of content came from
////	public Uri mBaseUri;
////
////	// The Content sub class must have a no-arg constructor
////	@SuppressWarnings("unchecked")
////	static public <T extends ProfileContent> T getContent(Cursor cursor,
////			Class<T> klass) {
////		try {
////			T content = klass.newInstance();
////			content.mId = cursor.getLong(0);
////			return (T) content.restore(cursor);
////		} catch (IllegalAccessException e) {
////			e.printStackTrace();
////		} catch (InstantiationException e) {
////			e.printStackTrace();
////		}
////		return null;
////	}
////
////	// The Uri is lazily initialized
////	private Uri getUri() {
////		if (mUri == null) {
////			mUri = ContentUris.withAppendedId(mBaseUri, mId);
////		}
////		return mUri;
////	}
////
////	private boolean isSaved() {
////		return mId != NOT_SAVED;
////	}
////
////	public Uri save(Context context) {
////		if (isSaved()) {
////			throw new UnsupportedOperationException();
////		}
////
////		Uri res = context.getContentResolver().insert(mBaseUri,
////				toContentValues());
////		mId = Long.parseLong(res.getPathSegments().get(1));
////		return res;
////	}
////
////	public int update(Context context, ContentValues contentValues) {
////		if (!isSaved()) {
////			throw new UnsupportedOperationException();
////		}
////		return context.getContentResolver().update(getUri(), contentValues,
////				null, null);
////	}
////	
////	public int update(Context context) {
////		return update(context, this.toContentValues());
////	}
////
////	static public int update(Context context, Uri baseUri, long id,
////			ContentValues contentValues) {
////		return context.getContentResolver().update(
////				ContentUris.withAppendedId(baseUri, id), contentValues, null,
////				null);
////	}
//
///***************************************************************Enterprise Application database*************************************************/
//	
//	/**
//	 * ApkProfile Interface for Application in db
//	 * 
//	 */
////	public interface ApkProfileColumns {
////		public static final String _ID = "_id";
////		public static final String APK_ID = "apk_id";
////		public static final String APK_NAME = "apk_name";
////		public static final String APK_URL = "apk_url";
////		public static final String APK_DESC = "apk_desc";
////		public static final String APK_VERSION = "apk_version";
////		public static final String APK_VERSION_CLIENT = "apk_version_client";
////		public static final String APK_FLAG = "flag";
////		public static final String APK_SCREEN = "screen";
////		public static final String APK_ROW = "row";
////		public static final String APK_COLUMN = "column";
////		public static final String ICONCOLOR = "icon_color";
////		public static final String ICONGREY = "icon_grey";
////		public static final String APK_FILE_NAME = "apk_file_name";
////		public static final String APK_FILE_PATH = "apk_file_path";
////		public static final String APK_PACKAGE_NAME = "apk_package_name";
////		public static final String APK_DISABLED = "apk_disabled";
////		public static final String INSTALLED_TIME = "installed_time";
////		public static final String LAST_START_TIME = "last_start_time";
////		public static final String LAST_EXIT_TIME = "last_exit_time";
////		public static final String PUBLISHED = "published";
////		public static final String IS_REMOVED = "is_removed";
////	}
////
////	public static final class ApkProfile extends ProfileContent
////			implements ApkProfileColumns {
////		public static final String ALLVERSION="0";
////		public static final String TABLE_NAME = "ApkProfile";
////		public static final Uri CONTENT_URI = Uri
////				.parse(ProfileContent.CONTENT_URI + "/ApkProfile");
////
////		public static final int CONTENT_ID_COLUMN = 0;
////		public static final int CONTENT_APKID_COLUMN = 1;
////		public static final int CONTENT_APKNAME_COLUMN = 2;
////		public static final int CONTENT_APKURL_COLUMN = 3;
////		public static final int CONTENT_APKDESC_COLUMN = 4;
////		public static final int CONTENT_APKVERSION_COLUMN = 5;
////		public static final int CONTENT_APKVERSIONCLIENT_COLUMN = 6;
////		public static final int CONTENT_APKFLAG_COLUMN = 7;
////		public static final int CONTENT_SCREENS_COLUMN = 8;
////		public static final int CONTENT_ROW_COLUMN = 9;
////		public static final int CONTENT_COLUMN_COLUMN = 10;
////		public static final int CONTENT_ICONCOLOR_COLUMN = 11;
////		public static final int CONTENT_ICONGREY_COLUMN = 12;
////		public static final int CONTENT_APKFILENAME_COLUMN = 13;
////		public static final int CONTENT_APKFILEPATH_COLUMN = 14;
////		public static final int CONTENT_APKPACKAGENAME_COLUMN = 15;
////		public static final int CONTENT_DISABLED_COLUMN = 16;
////		public static final int CONTENT_INSTALLED_TIME_COLUMN = 17;
////		public static final int CONTENT_LAST_START_TIME_COLUMN = 18;
////		public static final int CONTENT_LAST_EXIT_TIME_COLUMN = 19;
////		public static final int CONTENT_PUBLISHED_COLUMN = 20;
////		public static final int CONTENT_ISREMOVED_COLUMN = 21;
////
////		public static final String[] CONTENT_PROJECTION = new String[] {
////				RECORD_ID, ApkProfileColumns.APK_ID,
////				ApkProfileColumns.APK_NAME, ApkProfileColumns.APK_URL,
////				ApkProfileColumns.APK_DESC, ApkProfileColumns.APK_VERSION,
////				ApkProfileColumns.APK_VERSION_CLIENT,
////				ApkProfileColumns.APK_FLAG, ApkProfileColumns.APK_SCREEN,
////				ApkProfileColumns.APK_ROW, ApkProfileColumns.APK_COLUMN,
////				ApkProfileColumns.ICONCOLOR, ApkProfileColumns.ICONGREY,
////				ApkProfileColumns.APK_FILE_NAME,
////				ApkProfileColumns.APK_FILE_PATH,
////				ApkProfileColumns.APK_PACKAGE_NAME,
////				ApkProfileColumns.APK_DISABLED,
////				ApkProfileColumns.INSTALLED_TIME,
////				ApkProfileColumns.LAST_START_TIME,
////				ApkProfileColumns.LAST_EXIT_TIME, ApkProfileColumns.PUBLISHED,
////				ApkProfileColumns.IS_REMOVED};
////
////		public static final String[] CONTENT_ONLY_APK_ID = new String[] {
////				RECORD_ID, ApkProfileColumns.APK_ID };
////
////		public String mApkId;
////		public String mApkName;
////		public String mApkUrl;
////		public String mApkDesc;
////		public String mApkVersion;
////		public String mApkVersionClient;
////		public String mApkFlag;
////		public int mApkScreen;
////		public int mApkRow;
////		public int mApkColumn;
////		public String mIconColor;
////		public String mIconGrey;
////		public String mApkFileName;
////		public String mApkFilePath;
////		public String mApkPackageName;
////		public int mApkDisabled;
////		public String mApkInstalledTime;
////		public String mApkLastStartTime;
////		public String mApkLastExitTime;
////		public int mApkPublished;
////		public int mApkIsRemoved;
////		public boolean mIsDeal = false;
////
////		public ApkProfile() {
////			mBaseUri = CONTENT_URI;
////		}
////
////		@SuppressWarnings("unchecked")
////		@Override
////		public ApkProfile restore(Cursor cursor) {
////			mApkId = cursor.getString(CONTENT_APKID_COLUMN);
////			mApkName = cursor.getString(CONTENT_APKNAME_COLUMN);
////			mApkUrl = cursor.getString(CONTENT_APKURL_COLUMN);
////			mApkDesc = cursor.getString(CONTENT_APKDESC_COLUMN);
////			mApkVersion = cursor.getString(CONTENT_APKVERSION_COLUMN);
////			mApkFlag = cursor.getString(CONTENT_APKFLAG_COLUMN);
////			mApkScreen = cursor.getInt(CONTENT_SCREENS_COLUMN);
////			mApkRow = cursor.getInt(CONTENT_ROW_COLUMN);
////			mApkColumn = cursor.getInt(CONTENT_COLUMN_COLUMN);
////			mApkVersionClient = cursor.getString(CONTENT_APKVERSIONCLIENT_COLUMN);
////			byte[] iconcolor = cursor.getBlob(CONTENT_ICONCOLOR_COLUMN);
////			byte[] icongrey = cursor.getBlob(CONTENT_ICONGREY_COLUMN);
////			if(iconcolor!=null){
////				mIconColor = new String(iconcolor);
////			}
////			if(icongrey!=null){
////				mIconGrey = new String (icongrey);
////			}
////			mApkFileName = cursor.getString(CONTENT_APKFILENAME_COLUMN);
////			mApkFilePath = cursor.getString(CONTENT_APKFILEPATH_COLUMN);
////			mApkPackageName = cursor.getString(CONTENT_APKPACKAGENAME_COLUMN);
////			mApkDisabled = cursor.getInt(CONTENT_DISABLED_COLUMN);
////			mApkInstalledTime = cursor.getString(CONTENT_INSTALLED_TIME_COLUMN);
////			mApkLastStartTime = cursor.getString(CONTENT_LAST_START_TIME_COLUMN);
////			mApkLastExitTime = cursor.getString(CONTENT_LAST_EXIT_TIME_COLUMN);
////			mApkPublished = cursor.getInt(CONTENT_PUBLISHED_COLUMN);
////			mApkIsRemoved = cursor.getInt(CONTENT_ISREMOVED_COLUMN);
////			return this;
////		}
////
////		@Override
////		public ContentValues toContentValues() {
////			ContentValues values = new ContentValues();
////			// Assign values for each row.
////			values.put(ApkProfileColumns.APK_ID, mApkId);
////			values.put(ApkProfileColumns.APK_NAME, mApkName);
////			values.put(ApkProfileColumns.APK_URL, mApkUrl);
////			values.put(ApkProfileColumns.APK_DESC, mApkDesc);
////			values.put(ApkProfileColumns.APK_VERSION, mApkVersion);
////			values.put(ApkProfileColumns.APK_VERSION_CLIENT, mApkVersionClient);
////			values.put(ApkProfileColumns.APK_FLAG, mApkFlag);
////			values.put(ApkProfileColumns.APK_SCREEN, mApkScreen);
////			values.put(ApkProfileColumns.APK_ROW, mApkRow);
////			values.put(ApkProfileColumns.APK_COLUMN, mApkColumn);
////			if(mIconColor!=null){
////			values.put(ApkProfileColumns.ICONCOLOR, CommUtils
////					.getImageData(mIconColor));
////			}
////			if(mIconGrey!=null){
////			values.put(ApkProfileColumns.ICONGREY, CommUtils
////					.getImageData(mIconGrey));
////			}
////			values.put(ApkProfileColumns.APK_FILE_NAME, mApkFileName);
////			values.put(ApkProfileColumns.APK_FILE_PATH, mApkFilePath);
////			values.put(ApkProfileColumns.APK_PACKAGE_NAME, mApkPackageName);
////			values.put(ApkProfileColumns.APK_VERSION, mApkVersion);
////			values.put(ApkProfileColumns.APK_VERSION_CLIENT, mApkVersionClient);
////			values.put(ApkProfileColumns.APK_DISABLED, mApkDisabled);
////			values.put(ApkProfileColumns.INSTALLED_TIME, mApkInstalledTime);
////			values.put(ApkProfileColumns.LAST_START_TIME, mApkLastStartTime);
////			values.put(ApkProfileColumns.LAST_EXIT_TIME, mApkLastExitTime);
////			values.put(ApkProfileColumns.PUBLISHED, mApkPublished);
////			values.put(ApkProfileColumns.IS_REMOVED,mApkIsRemoved);
////			return values;
////		}
////
////		private static ApkProfile restoreApkProfileContentWithCursor(
////				Cursor cursor) {
////			try {
////				if (cursor.moveToFirst()) {
////					return getContent(cursor, ApkProfile.class);
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////
////		/**
////		 * Get get the content through apkId and flag
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param apkId
////		 *            , flag
////		 * @return the record
////		 */
////		public static ApkProfile restoreApkProfileContentWithApkId(
////				Context context, String apkId, byte flag) {
////			String sFlag = String.valueOf(flag);
////			Cursor c = context.getContentResolver().query(
////					ApkProfile.CONTENT_URI,
////					ApkProfile.CONTENT_PROJECTION,
////					ApkProfileColumns.APK_ID + "=?" + " and "
////							+ ApkProfileColumns.APK_FLAG + "=?",
////					new String[] { apkId, String.valueOf(sFlag) }, null);
////			return restoreApkProfileContentWithCursor(c);
////		}
////
////		/**
////		 * Get get the content through apkId and version
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param apkId
////		 *            , version
////		 * @return the record
////		 */
////		public static ApkProfile queryApkProfile(Context context,
////				String pkgId, String version) {
////			Cursor c = context.getContentResolver().query(
////					ApkProfile.CONTENT_URI,
////					ApkProfile.CONTENT_PROJECTION,
////					ApkProfileColumns.APK_ID + "=?" + " and "
////							+ ApkProfileColumns.APK_VERSION + "=?",
////					new String[] { pkgId, version }, null);
////			return restoreApkProfileContentWithCursor(c);
////		}
////
////		/**
////		 * Get get the content through packageName and flag
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param packageName
////		 *            , flag
////		 * @return the record
////		 */
////		public static ApkProfile queryApkProfileByPackageFlag(
////				Context context, String pkg, byte flag) {
////			Cursor c = context.getContentResolver().query(
////					ApkProfile.CONTENT_URI,
////					ApkProfile.CONTENT_PROJECTION,
////					ApkProfileColumns.APK_PACKAGE_NAME + "=?" + " and "
////							+ ApkProfileColumns.APK_FLAG + "=?",
////					new String[] { pkg, String.valueOf(flag) }, null);
////			return restoreApkProfileContentWithCursor(c);
////		}
////
////		/*
////		 * Get the content through name of package and version.
////		 * 
////		 * @param context
////		 * 
////		 * @param pkg name of package
////		 * 
////		 * @param clientVersion
////		 * 
////		 * @return the record
////		 */
////		public static ApkProfile queryApkProfileByPackageCVersion(
////				Context context, String pkg, String clientVersion) {
////			Cursor c = context.getContentResolver().query(
////					ApkProfile.CONTENT_URI,
////					ApkProfile.CONTENT_PROJECTION,
////					ApkProfileColumns.APK_PACKAGE_NAME + "=?" + " and "
////							+ ApkProfileColumns.APK_VERSION_CLIENT + "=?",
////					new String[] { pkg, clientVersion }, null);
////			return restoreApkProfileContentWithCursor(c);
////		}
////
////		/**
////		 * Get get the content through apkId
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param rowId
////		 * @return the record
////		 */
////		public static ApkProfile restoreApkProfileContentWithId(
////				Context context, long id) {
////			Uri u = ContentUris.withAppendedId(ApkProfile.CONTENT_URI,
////					id);
////			Cursor c = context.getContentResolver().query(u,
////					ApkProfile.CONTENT_PROJECTION, null, null, null);
////			return restoreApkProfileContentWithCursor(c);
////		}
////
////		/**
////		 * Get all records in db
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @return the arraylist of records
////		 */
////		public static ArrayList<ApkProfile> queryAllApkContents(
////				Context context) {
////			ArrayList<ApkProfile> appList = new ArrayList<ApkProfile>();
////			Uri u = ApkProfile.CONTENT_URI;
////			Cursor cursor = context.getContentResolver().query(u,
////					ApkProfile.CONTENT_PROJECTION, null, null, null);
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						ApkProfile data = getContent(cursor,
////								ApkProfile.class);// restoreContent(cursor);
////						appList.add(data);
////					} while (cursor.moveToNext());
////					return appList;
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////
////		public static ArrayList<String> queryAllApkID(Context context) {
////			ArrayList<String> apkList = new ArrayList<String>();
////
////			Uri uri = ApkProfile.CONTENT_URI;
////			Cursor cursor = context.getContentResolver().query(uri,
////					ApkProfile.CONTENT_ONLY_APK_ID, null, null, null);
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						int columnIndex = cursor
////								.getColumnIndex(ApkProfileColumns.APK_ID);
////						String value = cursor.getString(columnIndex);
////						apkList.add(value);
////					} while (cursor.moveToNext());
////
////				}
////			} finally {
////				cursor.close();
////			}
////
////			return apkList;
////		}
////
////		/**
////		 * Get get the records which flag equals certain value
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @return the ArrayList of the records
////		 */
////		public static ArrayList<ApkProfile> queryApkContentswithFlag(
////				Context context, byte flag) {
////			ArrayList<ApkProfile> appList = new ArrayList<ApkProfile>();
////			String flagvalue = String.valueOf(flag);
////			Cursor cursor = context.getContentResolver().query(
////					ApkProfile.CONTENT_URI,
////					ApkProfile.CONTENT_PROJECTION,
////					ApkProfileColumns.APK_FLAG + "=?",
////					new String[] { flagvalue }, null);
////			if (cursor == null)
////				return null;
////
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						ApkProfile data = getContent(cursor,
////								ApkProfile.class);
////						appList.add(data);
////					} while (cursor.moveToNext());
////					return appList;
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////
////		/**
////		 * update a record in db
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param value
////		 *            ContentValues to update
////		 * @return the num of Rows updated
////		 */
////		public static int updateApkContentwithRowId(Context context, long id,
////				ContentValues value) {
////			int numofRows = 0;
////			Uri u = ContentUris.withAppendedId(ApkProfile.CONTENT_URI,
////					id);
////			numofRows = context.getContentResolver().update(u, value, null,
////					null);
////			return numofRows;
////		}
////
////		public static int updateApkContentEnalbedwithApkId(Context context,
////				String[] ids, boolean[] enables) {
////			if (ids.length <= 0 || enables.length <= 0)
////				return -1;
////			int i = 0;
////			while (i < ids.length) {
////				Log.i("dbtest", String.valueOf(ids.length));
////				Cursor cursor = context.getContentResolver().query(
////						ApkProfile.CONTENT_URI,
////						ApkProfile.CONTENT_PROJECTION,
////						ApkProfileColumns.APK_ID + "=?",
////						new String[] { ids[i] }, null);
////				try {
////					if (cursor.moveToFirst()) {
////						Log.i("dbtest", "find it " + ids[i] + enables[i]);
////						do {
////							long id = cursor.getInt(cursor
////									.getColumnIndex(ApkProfileColumns._ID));
////							ContentValues data = new ContentValues();
////							data.put(ApkProfileColumns.APK_DISABLED, String
////									.valueOf(enables[i]));
////
////							Uri updateUri = ContentUris.withAppendedId(
////									ApkProfile.CONTENT_URI, id);
////							Log.i("dbtest", "id is" + String.valueOf(id));
////							int num = context.getContentResolver().update(
////									updateUri, data, null, null);
////							Log.i("dbtest", "num of " + String.valueOf(num));
////
////						} while (cursor.moveToNext());
////
////					}
////				} finally {
////					cursor.close();
////				}
////				i++;
////
////			}
////			return 0;
////		}
////
////		/**
////		 * delete a record in db
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param apkName
////		 * @param flag
////		 * @return the num of deleted row in db
////		 */
////		public static int deleteApkContentwithNameandFlag(Context context,
////				String apkName, int flag) {
////			int numofRows = 0;
////			String sFlag = String.valueOf(flag);
////			numofRows = context.getContentResolver().delete(
////					ApkProfile.CONTENT_URI,
////					ApkProfileColumns.APK_ID + "=?" + " and "
////							+ ApkProfileColumns.APK_FLAG + "=?",
////					new String[] { apkName, sFlag });
////			return numofRows;
////		}
////
////		/**
////		 * delete a record in db
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param id
////		 * @return the num of deleted row in db
////		 */
////		public static int deleteApkContentwithId(Context context, long id) {
////			int numofRows = 0;
////			Uri u = ContentUris.withAppendedId(ApkProfile.CONTENT_URI,
////					id);
////
////			numofRows = context.getContentResolver().delete(u, null, null);
////			return numofRows;
////		}
////		
////		public int delete(Context context) {
////			int numofRows = 0;
////			long id = mId;
////			Uri u = ContentUris.withAppendedId(ApkProfile.CONTENT_URI,
////					id);
////
////			numofRows = context.getContentResolver().delete(u, null, null);
////			return numofRows;
////		}
////	}
//
///*******************************************************System Profile Database*************************************************************************/
//	
//	/**
//	 * Profile
//	 * 
//	 * 
//	 */
////	public interface ConfigsColumns {
////		public static final String ID = "_id";
////
////		public static final String CID = "cid";
////		public static final String UUID = "Uuid";
////		public static final String NAME = "name";
////		public static final String TYPE = "profileType";
////		public static final String VERSION = "version";
////		public static final String NOTE = "note";
////	}
////
////	public static final class ConfigProfileContent extends ProfileContent implements
////	ConfigsColumns {
////		public static final String TABLE_NAME = "Config";
////		public static final Uri CONTENT_URI = Uri
////				.parse(ProfileContent.CONTENT_URI + "/Config");
////
////		public static final int CONTENT_ID_COLUMN = 0;
////		public static final int CONTENT_CID_COLUMN = 1;
////		public static final int CONTENT_UUID_COLUMN = 2;
////		public static final int CONTENT_NAME_COLUMN = 3;
////		public static final int CONTENT_TYPE_COLUMN = 4;
////
////		public static final int CONTENT_VERSION_COLUMN = 5;
////		public static final int CONTENT_NOTE_COLUMN = 6;
////
////		public static final String[] CONTENT_PROJECTION = new String[] {
////				RECORD_ID, ConfigsColumns.CID, ConfigsColumns.UUID,
////				ConfigsColumns.NAME, ConfigsColumns.TYPE,
////				ConfigsColumns.VERSION, ConfigsColumns.NOTE };
////
////		public ConfigProfileContent() {
////			mBaseUri = CONTENT_URI;
////			// mCid = "-1";
////		}
////
////		public String mCid;
////		public String mUuid;
////		public String mName;
////		public String mType;
////		public String mVersion;
////		public String mVersionClient;
////		public String mNote;
////
////		@SuppressWarnings("unchecked")
////		@Override
////		public ConfigProfileContent restore(Cursor cursor) {
////			mCid = cursor.getString(CONTENT_CID_COLUMN);
////			mUuid = cursor.getString(CONTENT_UUID_COLUMN);
////			mName = cursor.getString(CONTENT_NAME_COLUMN);
////			mType = cursor.getString(CONTENT_TYPE_COLUMN);
////			mVersion = cursor.getString(CONTENT_VERSION_COLUMN);
////			mNote = cursor.getString(CONTENT_NOTE_COLUMN);
////			return this;
////		}
////
////		@Override
////		public ContentValues toContentValues() {
////			ContentValues values = new ContentValues();
////
////			// Assign values for each row.
////			values.put(ConfigsColumns.CID, mCid);
////			values.put(ConfigsColumns.UUID, mUuid);
////			values.put(ConfigsColumns.NAME, mName);
////			values.put(ConfigsColumns.TYPE, mType);
////			values.put(ConfigsColumns.VERSION, mVersion);
////			values.put(ConfigsColumns.NOTE, mNote);
////			return values;
////		}
////
////		private static ConfigProfileContent restoreProfilesWithCursor(Cursor cursor) {
////			try {
////				if (cursor.moveToFirst()) {
////					return getContent(cursor, ConfigProfileContent.class);
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////
////		public static ConfigProfileContent restoreProfilesWithId(Context context, long id) {
////			Uri u = ContentUris.withAppendedId(ConfigProfileContent.CONTENT_URI, id);
////			Cursor c = context.getContentResolver().query(u,
////					ConfigProfileContent.CONTENT_PROJECTION, null, null, null);
////			return restoreProfilesWithCursor(c);
////		}
////
////		public static ConfigProfileContent restoreProfilesWithUuid(Context context,
////				String id) {
////			Cursor c = context.getContentResolver().query(ConfigProfileContent.CONTENT_URI,
////					ConfigProfileContent.CONTENT_PROJECTION, ConfigsColumns.UUID + "=?",
////					new String[] { id }, null);
////			return restoreProfilesWithCursor(c);
////		}
////
////		public static ConfigProfileContent restoreProfilesWithName(Context context,
////				String type, String name) {
////
////			Cursor c = context.getContentResolver().query(
////					ConfigProfileContent.CONTENT_URI,
////					ConfigProfileContent.CONTENT_PROJECTION,
////					ConfigsColumns.TYPE + "=?" + " and "
////							+ ConfigsColumns.NAME + "=?",
////					new String[] { type, name }, null);
////			return restoreProfilesWithCursor(c);
////
////		}
////
////		// query all record which version does not match parameter
////		public static ArrayList<ConfigProfileContent> restoreProfilesWithTypeandVersion(
////				Context context, String type, String version) {
////
////			ArrayList<ConfigProfileContent> list = new ArrayList<ConfigProfileContent>();
////			Cursor cursor = context.getContentResolver().query(
////					ConfigProfileContent.CONTENT_URI,
////					ConfigProfileContent.CONTENT_PROJECTION,
////					ConfigsColumns.TYPE + "=?" + " and "
////							+ ConfigsColumns.VERSION + "!=?",
////					new String[] { type, version }, null);
////
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						ConfigProfileContent i = getContent(cursor, ConfigProfileContent.class);// restoreProfilesWithCursor(cursor);
////						list.add(i);
////
////					} while (cursor.moveToNext());
////				} 
////				return list;
////			} finally {
////				cursor.close();
////
////			}
////		}
////
////		public static ConfigProfileContent restoreProfilesWithType(Context context,
////				String type, long cid) {
////
////			String sCID = String.valueOf(cid);
////			Cursor c = context.getContentResolver().query(
////					ConfigProfileContent.CONTENT_URI,
////					ConfigProfileContent.CONTENT_PROJECTION,
////					ConfigsColumns.TYPE + "=?" + " and " + ConfigsColumns.CID
////							+ "=?", new String[] { type, sCID }, null);
////			return restoreProfilesWithCursor(c);
////
////		}
////
////		public static int deleteProfileswithVersion(Context context,
////				String version) {
////			int numofRows = 0;
////			numofRows = context.getContentResolver().delete(
////					ConfigProfileContent.CONTENT_URI, ConfigsColumns.VERSION + "=?",
////					new String[] { version });
////			return numofRows;
////		}
////
////		public static int deleteProfileswithId(Context context, long id) {
////			Uri u = ContentUris.withAppendedId(ConfigProfileContent.CONTENT_URI, id);
////			int numofRows = 0;
////			numofRows = context.getContentResolver().delete(u, null, null);
////			return numofRows;
////		}
////		
////		///new interface
////		public void setProfileArg(String name, String uuid, long cid,
////				String version, String type) {
////			if (null != name) {
////				mName = name;
////			}
////			if (null != uuid) {
////				mUuid = uuid;
////			}
////			if (cid >= 0) {
////				mCid = String.valueOf(cid);
////			}
////			if (null != version) {
////				mVersion = version;
////			}
////			if (null != type) {
////				mType = type;
////			}
////		}
////
////		public String getCid() {
////			return mCid;
////		}
////
////		public String getName() {
////			return mName;
////		}
////
////		public Long getId() {
////			return mId;
////		}
////		
////		public static ConfigProfileContent getProfileByTypeAndName(Context context,
////				String type, String name) {			
////			ConfigProfileContent pc = null;
////			if (null!= type && null!= name ) {
////				String where = String.format("%s=? and %s=?", TYPE, NAME);
////				
////				Cursor c = context.getContentResolver().query(
////						CONTENT_URI, CONTENT_PROJECTION,
////						where, new String[] { type, name }, null);
////				pc = getPbyCursor(c);
////			}
////
////			return pc;
////		}
////
////		public static ConfigProfileContent getProfileByUUID(Context context, String uuid) {
////			ConfigProfileContent pc = null;
////			if (null != uuid) {
////				Cursor c = context.getContentResolver().query(
////						CONTENT_URI, CONTENT_PROJECTION,
////						ConfigsColumns.UUID + "=?", new String[] { uuid }, null);
////				pc = getPbyCursor(c);
////			}
////
////			return pc;
////		}
////		
////		public static ConfigProfileContent getProfileByTypeCID(Context context,String type, String cid) {
////			ConfigProfileContent pc = null;
////			if (null != type && null != cid) {
////				String where = String.format("%s=? and %s=?", TYPE, CID);
////
////				Cursor c = context.getContentResolver().query(CONTENT_URI,
////						CONTENT_PROJECTION, where, new String[] { type, cid }, null);
////				pc = getPbyCursor(c);
////			}
////
////			return pc;
////		}
////		
////
//////		public static int delPByVersion(Context context, String version) {
//////			int numofRows = 0;
//////			numofRows = context.getContentResolver().delete(
//////					ProfileContent.CONTENT_URI, ProfileContent.VERSION + "=?",
//////					new String[] { version });
//////			return numofRows;
//////		}
////
////		public static int delProfileByID(Context context, long id) {
////			int numofRows = 0;
////			if (id >= 0) {
////				Uri u = ContentUris.withAppendedId(ConfigProfileContent.CONTENT_URI, id);
////				numofRows = context.getContentResolver().delete(u, null, null);
////			}
////			return numofRows;
////		}
////
////		// public static ProfileContent restoreProfilesWithType(Context context,
////		// String type, long cid) {
////		//
////		// String sCID = String.valueOf(cid);
////		// Cursor c = context.getContentResolver().query(
////		// ProfileContent.CONTENT_URI,
////		// ProfileContent.CONTENT_PROJECTION,
////		// ProfileContent.TYPE + "=?" + " and " + ProfileContent.CID
////		// + "=?", new String[] { type, sCID }, null);
////		// return restoreProfilesWithCursor(c);
////		//
////		// }
////
////		private static ConfigProfileContent getPbyCursor(Cursor cursor) {
////			try {
////				if (cursor.moveToFirst()) {
////					return getContent(cursor, ConfigProfileContent.class);
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////	}
//
///********************************************************Web Clip Database**********************************************************************/
//	
//	/**
//	 * WebclipProfile This is a table about webclip.
//	 * 
//	 * 
//	 */
////	public interface WebclipColumns {
////		public static final String ID = "_id";
////
////		public static final String IDENTIFIER = "identifier";
////		public static final String DISPAYNAME = "dispayName";
////		public static final String URL = "url";
////		public static final String ICON = "icon";
////		public static final String ISREMOVEABLE = "flag";
////		public static final String VERSION = "version";
////	}
//
////	public static final class WebClipProfileContent extends ProfileContent
////			implements WebclipColumns {
////		public static final String TABLE_NAME = "WebClipProfile";
////		public static final Uri CONTENT_URI = Uri
////				.parse(ProfileContent.CONTENT_URI + "/WebClipProfile");
////
////		public static final int CONTENT_ID_COLUMN = 0;
////		public static final int CONTENT_IDENTIFIER_COLUMN = 1;
////		public static final int CONTENT_DISPAYNAME_COLUMN = 2;
////		public static final int CONTENT_URL_COLUMN = 3;
////		public static final int CONTENT_ICON_COLUMN = 4;
////		public static final int CONTENT_ISREMOVEABLE_COLUMN = 5;
////		public static final int CONTENT_VERSION_COLUMN = 6;
////
////		public static final String[] CONTENT_PROJECTION = new String[] {
////				RECORD_ID, WebclipColumns.IDENTIFIER,
////				WebclipColumns.DISPAYNAME, WebclipColumns.URL,
////				WebclipColumns.ICON, WebclipColumns.ISREMOVEABLE,
////				WebclipColumns.VERSION };
////
////		public int mIsRemovable;
////		public String mWebClipName;
////		public String mWebClipUrl;
////		public String mWebClipIcon;
////		public String mWebClipIdentifier;
////		public String mWebClipVersion;
////		public int mWebClipFlag; // fix bug2734 by cuixiaowei 20110725
////
////		public WebClipProfileContent() {
////			mBaseUri = CONTENT_URI;
////		}
////
////		@SuppressWarnings("unchecked")
////		@Override
////		public WebClipProfileContent restore(Cursor cursor) {
//
////			mWebClipIdentifier = cursor.getString(CONTENT_IDENTIFIER_COLUMN);
////			mWebClipName = cursor.getString(CONTENT_DISPAYNAME_COLUMN);
////			mWebClipUrl = cursor.getString(CONTENT_URL_COLUMN);
////			mWebClipIcon = new String(cursor.getBlob(CONTENT_ICON_COLUMN));
////			mIsRemovable = cursor.getInt(CONTENT_ISREMOVEABLE_COLUMN);
////			mWebClipVersion = cursor.getString(CONTENT_VERSION_COLUMN);
////
////			return this;
////		}
////
////		@Override
////		public ContentValues toContentValues() {
////			ContentValues values = new ContentValues();
////
////			// Assign values for each row.
////			values.put(WebclipColumns.IDENTIFIER, mWebClipIdentifier);
////			values.put(WebclipColumns.DISPAYNAME, mWebClipName);
////			values.put(WebclipColumns.URL, mWebClipUrl);
////			if(mWebClipIcon != null){
////				values.put(WebclipColumns.ICON, CommUtils
////						.getImageData(mWebClipIcon));
////			}
////			values.put(WebclipColumns.ISREMOVEABLE, mIsRemovable);
////			values.put(WebclipColumns.VERSION, mWebClipVersion);
////
////			return values;
////		}
////
////		private static WebClipProfileContent restoreBodyWithCursor(Cursor cursor) {
////			try {
////				if (cursor.moveToFirst()) {
////					return getContent(cursor, WebClipProfileContent.class);
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////
////		public static WebClipProfileContent restoreWebClipProfileWithId(
////				Context context, long id) {
////			Uri u = ContentUris.withAppendedId(
////					WebClipProfileContent.CONTENT_URI, id);
////			Cursor c = context.getContentResolver().query(u,
////					WebClipProfileContent.CONTENT_PROJECTION, null, null, null);
////			return restoreBodyWithCursor(c);
////		}
////
////		public static ArrayList<WebClipProfileContent> restoreWebClipWithVersion(
////				Context context, String version) {
////			Cursor cursor = context.getContentResolver().query(
////					WebClipProfileContent.CONTENT_URI,
////					WebClipProfileContent.CONTENT_PROJECTION,
////					WebclipColumns.VERSION + "!=?", new String[] { version },
////					null);
////
////			if (cursor.getCount() == 0) {
////				cursor.close();
////				return null;
////			} else {
////				ArrayList<WebClipProfileContent> list = new ArrayList<WebClipProfileContent>();
////				try {
////					if (cursor.moveToFirst()) {
////						do {
////							WebClipProfileContent i = getContent(cursor,
////									WebClipProfileContent.class);// restoreBodyWithCursor(cursor);
////							list.add(i);
////						} while (cursor.moveToNext());
////						return list;
////					} else {
////						
////						return null;
////					}
////				} finally {
////					cursor.close();
////				}
////			}
////		}
////
////		public static WebClipProfileContent restoreWebClipWithContent(
////				Context context, String name, String url, String icon) {
////			Cursor c = context.getContentResolver().query(
////					WebClipProfileContent.CONTENT_URI,
////					WebClipProfileContent.CONTENT_PROJECTION,
////					WebclipColumns.DISPAYNAME + "=?" + " and "
////							+ WebclipColumns.URL + "=?" + " and "
////							+ WebclipColumns.ICON + "=?",
////					new String[] { name, url, icon }, null);
////			return restoreBodyWithCursor(c);
////		}
////
////		public static int deleteWebClipWithVersion(Context context,
////				String Version) {
////			int numofRows = 0;
////			numofRows = context.getContentResolver().delete(
////					WebClipProfileContent.CONTENT_URI,
////					WebclipColumns.VERSION + "=?", new String[] { Version });
////			return numofRows;
////		}
////
////		public static int deleteWebClipWithId(Context context, long id) {
////			int numofRows = 0;
////			Uri u = ContentUris.withAppendedId(
////					WebClipProfileContent.CONTENT_URI, id);
////
////			numofRows = context.getContentResolver().delete(u, null, null);
////			return numofRows;
////		}
////		
////		//new interface
////		public static WebClipProfileContent dbHaveSameConfig(Context context, String pname) {
////			WebClipProfileContent value = null;
////			if (null != context && null != pname) {
////				value = WebClipProfileContent.getWebClipByName(context, pname);
////			}
////			return value;
////		}
////
////		public static ArrayList<WebClipProfileContent> getWebClipByUnVer(Context context,
////				String version) {
////			String where = String.format("%s!=?", VERSION);
////			
////			Cursor cursor = context.getContentResolver().query(CONTENT_URI,
////					CONTENT_PROJECTION, where, new String[] { version }, null);
////
////			ArrayList<WebClipProfileContent> list = new ArrayList<WebClipProfileContent>();
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						WebClipProfileContent i = getContent(cursor,
////								WebClipProfileContent.class);
////						list.add(i);
////					} while (cursor.moveToNext());
////					return list;
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////		
////		public static WebClipProfileContent getWebClipByName(Context context, String name) {
////			String where = String.format("%s=?", DISPAYNAME);
////			
////			Cursor c = context.getContentResolver().query(
////					WebClipProfileContent.CONTENT_URI, WebClipProfileContent.CONTENT_PROJECTION,
////					where, new String[] { name }, null);
////			return getContentByCursor(c);
////		}
////		
////		private static WebClipProfileContent getContentByCursor(Cursor cursor) {
////			try {
////				if (cursor.moveToFirst()) {
////					return getContent(cursor, WebClipProfileContent.class);
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////	}
//	
//	
///****************************************************Non-Enterprise Application database*******************************************************/
//	
//	/*
//	 * This is a table about application of system. used to record the status of
//	 * system app which indicate whether it can start.
//	 */
////	public interface FunctionProfileColumns {
////		public static final String ID = "_id";
////
////		public static final String FUNCTION_ID = "function_idd";
////		public static final String FUNCTION_NAME = "function_name";
////		public static final String FUNCTION_VERSION = "function_version";
////		public static final String FUNCTION_DISABLED = "function_disabled";
////		public static final String FUNCTION_INSTALLED_TIME = "function_install_time";
////		public static final String FUNCTION_LAST_START_TIME = "function_last_install_time";
////		public static final String FUNCTION_LAST_EXIT_TIME = "function_last_exit_time";
////	}
////
////	public static final class FunctionProfileContent extends ProfileContent
////			implements FunctionProfileColumns {
////		public static final Uri CONTENT_URI = Uri.parse(ProfileContent.CONTENT_URI + "/funcProfile");
////		
////		public static final String TABLE_NAME = "funcProfile";
////		public static final String[] CONTENT_PROJECTION = new String[] {
////			RECORD_ID,FunctionProfileColumns.FUNCTION_ID,FunctionProfileColumns.FUNCTION_NAME,
////			FunctionProfileColumns.FUNCTION_VERSION,FunctionProfileColumns.FUNCTION_DISABLED,
////			FunctionProfileColumns.FUNCTION_INSTALLED_TIME,FunctionProfileColumns.FUNCTION_LAST_START_TIME,
////			FunctionProfileColumns.FUNCTION_LAST_EXIT_TIME
////		};
////
////		public String mFunctionId;
////		public String mFunctionName;
////		public String mFunctionVersion;
////		public int mFunctionDisabled;
////		public String mFunctionInstalledTime;
////		public String mFunctionLastStartTime;
////		public String mFunctionLastExitTime;
////
////		public static final int CONTENT_ID_COLUMN = 0;
////		public static final int CONTENT_IDENTIFIER_COLUMN = 1;
////		public static final int CONTENT_DISPLAYNAME_COLUMN = 2;
////		public static final int CONTENT_VERSION_COLUMN = 3;
////		public static final int CONTENT_DISABLED_COLUMN = 4;
////		public static final int CONTENT_INSTALLED_TIME = 5;
////		public static final int CONTENT_LAST_START_TIME = 6;
////		public static final int CONTENT_LAST_EXIT_TIME = 7;
////
////		public FunctionProfileContent() {
////			mBaseUri = CONTENT_URI;
////		}
////
////		@Override
////		public ContentValues toContentValues() {
////			ContentValues values = new ContentValues();
////
////			// Assign values for each row.
////			values.put(FunctionProfileColumns.FUNCTION_ID, mFunctionId);
////			values.put(FunctionProfileColumns.FUNCTION_NAME, mFunctionName);
////			values.put(FunctionProfileColumns.FUNCTION_VERSION, mFunctionVersion);
////			values.put(FunctionProfileColumns.FUNCTION_DISABLED,
////					mFunctionDisabled);
////			values.put(FunctionProfileColumns.FUNCTION_INSTALLED_TIME,
////					mFunctionInstalledTime);
////			values.put(FunctionProfileColumns.FUNCTION_LAST_START_TIME,
////					mFunctionLastStartTime);
////			values.put(FunctionProfileColumns.FUNCTION_LAST_EXIT_TIME,
////					mFunctionLastExitTime);
////			return values;
////		}
////
////		@SuppressWarnings("unchecked")
////		@Override
////		public FunctionProfileContent restore(Cursor cursor) {
////			mFunctionId = cursor.getString(CONTENT_IDENTIFIER_COLUMN);
////			mFunctionName = cursor.getString(CONTENT_DISPLAYNAME_COLUMN);
////			mFunctionVersion = cursor.getString(CONTENT_VERSION_COLUMN);
////			mFunctionDisabled = cursor.getInt(CONTENT_DISABLED_COLUMN);
////			mFunctionInstalledTime = cursor.getString(CONTENT_INSTALLED_TIME);
////			mFunctionLastStartTime = cursor.getString(CONTENT_LAST_START_TIME);
////			mFunctionLastExitTime = cursor.getString(CONTENT_LAST_EXIT_TIME);
////			return this;
////		}
////		
////		private static FunctionProfileContent restoreApkFunctionContentWithCursor(
////				Cursor cursor) {
////			try {
////				if (cursor.moveToFirst()) {
////					return getContent(cursor, FunctionProfileContent.class);
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////
////		public static int updateFunctionEnalbedwithId(Context context,
////				String[] ids, boolean[] enables) {
////			if (ids.length <= 0 || enables.length <= 0)
////				return -1;
////			int i = 0;
////			while (i < ids.length) {
////				Cursor cursor = context.getContentResolver().query(
////						FunctionProfileContent.CONTENT_URI,
////						FunctionProfileContent.CONTENT_PROJECTION,
////						FunctionProfileContent.FUNCTION_ID + "=?",
////						new String[] { ids[i] }, null);
////				try {
////					if (cursor.moveToFirst()) {
////						do {
////							long id = cursor.getInt(cursor
////									.getColumnIndex(ApkProfileColumns._ID));
////							ContentValues data = new ContentValues();
////							data.put(FunctionProfileColumns.FUNCTION_DISABLED,
////									String.valueOf(enables[i]));
////							Uri updateUri = ContentUris.withAppendedId(
////									ApkProfile.CONTENT_URI, id);
////							context.getContentResolver().update(updateUri,
////									data, null, null);
////						} while (cursor.moveToNext());
////					}
////				} finally {
////					cursor.close();
////				}
////				i++;
////
////			}
////			return 0;
////		}
////
////		/**
////		 * Get all records in db
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @return the arraylist of records
////		 */
////		public static ArrayList<FunctionProfileContent> queryAllFuncProfileContents(
////				Context context) {
////			ArrayList<FunctionProfileContent> appList = new ArrayList<FunctionProfileContent>();
////			Uri u = FunctionProfileContent.CONTENT_URI;
////			Cursor cursor = context.getContentResolver()
////					.query(u, FunctionProfileContent.CONTENT_PROJECTION, null,
////							null, null);
////
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						FunctionProfileContent data = getContent(cursor,
////								FunctionProfileContent.class);// restoreContent(cursor);
////						appList.add(data);
////					} while (cursor.moveToNext());
////					return appList;
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////		
////		/*
////		 * Get the content through package name and version.
////		 * 
////		 * @param context
////		 * 
////		 * @param pkg name of package
////		 * 
////		 * @param version
////		 * 
////		 * @return the record
////		 */
////		public static FunctionProfileContent queryFuncProfileByPackageVersion(
////				Context context, String pkg, String version) {
////			Cursor c = context.getContentResolver().query(
////					FunctionProfileContent.CONTENT_URI,
////					FunctionProfileContent.CONTENT_PROJECTION,
////					FunctionProfileColumns.FUNCTION_ID + "=?" + " and "
////							+ FunctionProfileColumns.FUNCTION_VERSION + "=?",
////					new String[] { pkg, version }, null);
////			return restoreApkFunctionContentWithCursor(c);
////		}
////
////		/**
////		 * delete a record in db
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param apkName
////		 * @param flag
////		 * @return the num of deleted row in db
////		 */
////		public static int deleteFuncContentwithName(Context context,
////				String FuncName) {
////			int numofRows = 0;
////			numofRows = context.getContentResolver().delete(
////					FunctionProfileContent.CONTENT_URI,
////					FunctionProfileColumns.FUNCTION_NAME + "=?",
////					new String[] { FuncName });
////			return numofRows;
////		}
////
////		/**
////		 * delete a record in db
////		 * 
////		 * @param context
////		 *            context to access DB
////		 * @param id
////		 * @return the num of deleted row in db
////		 */
////		public static int deleteFuncContentwithId(Context context, long id) {
////			int numofRows = 0;
////			Uri u = ContentUris.withAppendedId(
////					FunctionProfileContent.CONTENT_URI, id);
////
////			numofRows = context.getContentResolver().delete(u, null, null);
////			return numofRows;
////		}
////	}
//
///************************************************GPS Location database************************************************************************/
//	
//	/*
//	 * This is GPS location table. used to record location of the device every
//	 * time. include time, longitude, latitude
//	 */
////	public interface GPSLocationColumns {
////		public static final String ID = "_id";
////
////		public static final String GPS_TIME = "time";
////		public static final String GPS_LONGITUDE = "longitude";
////		public static final String GPS_LATITUDE = "latitude";
////		public static final String GPS_ISREAD = "flag";
////	}
//
////	public static final class GPSLocationContent extends ProfileContent
////			implements GPSLocationColumns {
////		public static final String TABLE_NAME = "gpslocation";
////		public static final Uri CONTENT_URI = Uri
////				.parse(ProfileContent.CONTENT_URI + "/gpslocation");
////
////		public static final String[] CONTENT_PROJECTION = new String[] {
////			    RECORD_ID, GPSLocationColumns.GPS_TIME,
////				GPSLocationColumns.GPS_LONGITUDE,
////				GPSLocationColumns.GPS_LATITUDE,GPSLocationColumns.GPS_ISREAD };
////
////		public String mGPSTime;
////		public String mGPSLongitude;
////		public String mGPSLatitude;
////		public int mGPSIsRead;
////
////		public static final int CONTENT_ID_COLUMN = 0;
////		public static final int CONTENT_TIME_COLUMN = 1;
////		public static final int CONTENT_LONGITUDE_COLUMN = 2;
////		public static final int CONTENT_LATITUDE_COLUMN = 3;
////		public static final int CONTENT_ISREAD_COLUMN = 4;
////
////		public GPSLocationContent() {
////			mBaseUri = CONTENT_URI;
////		}
////
////		public ContentValues toContentValues() {
////			ContentValues values = new ContentValues();
////
////			// Assign values for each row.
////			values.put(GPSLocationColumns.GPS_TIME, mGPSTime);
////			values.put(GPSLocationColumns.GPS_LONGITUDE, mGPSLongitude);
////			values.put(GPSLocationColumns.GPS_LATITUDE, mGPSLatitude);
////			values.put(GPSLocationColumns.GPS_ISREAD, mGPSIsRead);
////			return values;
////		}
////
////		@SuppressWarnings("unchecked")
////		public GPSLocationContent restore(Cursor cursor) {
////			mGPSTime = cursor.getString(CONTENT_TIME_COLUMN);
////			mGPSLongitude = cursor.getString(CONTENT_LONGITUDE_COLUMN);
////			mGPSLatitude = cursor.getString(CONTENT_LATITUDE_COLUMN);
////			mGPSIsRead = cursor.getInt(CONTENT_ISREAD_COLUMN);
////			return this;
////		}
////
////		public static int deleteGPSLocationWithId(Context context, long id) {
////			int numofRows = 0;
////			Uri u = ContentUris.withAppendedId(GPSLocationContent.CONTENT_URI,
////					id);
////
////			numofRows = context.getContentResolver().delete(u, null, null);
////			return numofRows;
////		}
////
////		public static void deleteAllGPSLocationBeRead(Context context) {
////			Uri u = GPSLocationContent.CONTENT_URI;
////			Cursor cursor = context.getContentResolver().query(u,
////					GPSLocationContent.CONTENT_PROJECTION, null, null, null);
////
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						GPSLocationContent data = getContent(cursor,
////								GPSLocationContent.class);
////						if (data.mGPSIsRead == 1) {
////							deleteGPSLocationWithId(context, data.mId);
////						}
////					} while (cursor.moveToNext());
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////		
////		public static void deleteAllGPSLocation(Context context) {
////			Uri u = GPSLocationContent.CONTENT_URI;
////			Cursor cursor = context.getContentResolver().query(u,
////					GPSLocationContent.CONTENT_PROJECTION, null, null, null);
////
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						GPSLocationContent data = getContent(cursor,
////								GPSLocationContent.class);
////						deleteGPSLocationWithId(context, data.mId);
////					} while (cursor.moveToNext());
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////
////		public static ArrayList<GPSLocationContent> queryAllLocationContents(
////				Context context) {
////			ArrayList<GPSLocationContent> appList = new ArrayList<GPSLocationContent>();
////			Uri u = GPSLocationContent.CONTENT_URI;
////			Cursor cursor = context.getContentResolver().query(u,
////					GPSLocationContent.CONTENT_PROJECTION, null, null, null);
////			// StringBuilder builder = new StringBuilder();
////
////			try {
////				if (cursor.moveToFirst()) {
////					do {
////						GPSLocationContent data = getContent(cursor,
////								GPSLocationContent.class);
////						appList.add(data);
////					} while (cursor.moveToNext());
////					return appList;
////				} else {
////					return null;
////				}
////			} finally {
////				cursor.close();
////			}
////		}
////	}
//}
