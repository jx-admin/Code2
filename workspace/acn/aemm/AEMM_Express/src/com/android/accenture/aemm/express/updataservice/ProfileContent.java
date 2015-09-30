package com.android.accenture.aemm.express.updataservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public abstract class ProfileContent {
	public static final String AUTHORITY = ProfileContentProvider.PROVIDER_NAME;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	public static final String TAG = "ProfileContent";
	// Newly created objects get this id
	private static final int NOT_SAVED = -1;

	// All classes share this
	public static final String RECORD_ID = "_id";
	// Lazily initialized uri for this Content
	private Uri mUri = null;
	public long mId = NOT_SAVED;

	private static final String[] COUNT_COLUMNS = new String[]{"count(*)"};
	public static final String[] ID_PROJECTION = new String[] {
		RECORD_ID
	};
	public static final int ID_PROJECTION_COLUMN = 0;

	private static final String ID_SELECTION = RECORD_ID + " =?";

	// Write the Content into a ContentValues container
	public abstract ContentValues toContentValues();
	// Read the Content from a ContentCursor
	public abstract <T extends ProfileContent> T restore (Cursor cursor);


	// The base Uri that this piece of content came from
	public Uri mBaseUri;

	// The Content sub class must have a no-arg constructor
	static public <T extends ProfileContent> T getContent(Cursor cursor, Class<T> klass) {
		try {
			T content = klass.newInstance();
			content.mId = cursor.getLong(0);
			return (T)content.restore(cursor);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	// The Uri is lazily initialized
	public Uri getUri() {
		if (mUri == null) {
			mUri = ContentUris.withAppendedId(mBaseUri, mId);
		}
		return mUri;
	}
	public boolean isSaved() {
		return mId != NOT_SAVED;
	}
	public Uri save(Context context) {
		if (isSaved()) {
			throw new UnsupportedOperationException();
		}

		Uri res = context.getContentResolver().insert(mBaseUri, toContentValues());
		mId = Long.parseLong(res.getPathSegments().get(1));
		return res;
	}

	public int update(Context context, ContentValues contentValues) {
		if (!isSaved()) {
			throw new UnsupportedOperationException();
		}
		return context.getContentResolver().update(getUri(), contentValues, null, null);
	}

	static public int update(Context context, Uri baseUri, long id, ContentValues contentValues) {
		return context.getContentResolver()
		.update(ContentUris.withAppendedId(baseUri, id), contentValues, null, null);
	}


	public byte[] getImageData(String str) { 
		Log.i("test", str);
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
	/**
	 * ApkProfile
	 * @author vivian.yun.feng
	 *
	 */
	public interface ApkProfileColumns {
		public static final String _ID = "_id";
		public static final String APK_ID = "apk_id";
		public static final String APK_NAME = "apk_name";
		public static final String APK_URL = "apk_url";
		public static final String APK_DESC = "apk_desc";
		public static final String APK_VERSION = "apk_version";
		public static final String APK_VERSION_CLIENT = "apk_version_client";
		public static final String APK_FLAG = "flag";
		public static final String ICONCOLOR = "icon_color";
		public static final String ICONGREY = "icon_grey";
		public static final String APK_FILE_NAME = "apk_file_name";
		public static final String APK_FILE_PATH = "apk_file_path";
		public static final String APK_PACKAGE_NAME = "apk_package_name";
		public static final String APK_INSTALLENABLED = "apk_install_enabled";
	}
	public static final class ApkProfileContent extends ProfileContent implements ApkProfileColumns
	{
		public static final String TABLE_NAME = "ApkProfile";
		public static final Uri CONTENT_URI = Uri.parse(ProfileContent.CONTENT_URI + "/ApkProfile");

		public static final int CONTENT_ID_COLUMN = 0;
		public static final int CONTENT_APKID_COLUMN = 1;
		public static final int CONTENT_APKNAME_COLUMN = 2;
		public static final int CONTENT_APKURL_COLUMN = 3;
		public static final int CONTENT_APKDESC_COLUMN = 4;
		public static final int CONTENT_APKVERSION_COLUMN = 5;
		public static final int CONTENT_APKVERSIONCLIENT_COLUMN = 6;
		public static final int CONTENT_APKFLAG_COLUMN = 7;
		public static final int CONTENT_ICONCOLOR_COLUMN = 8;
		public static final int CONTENT_ICONGREY_COLUMN = 9;
		public static final int CONTENT_APKFILENAME_COLUMN = 10;
		public static final int CONTENT_APKFILEPATH_COLUMN = 11;
		public static final int CONTENT_APKPACKAGENAME_COLUMN = 12;
		public static final int CONTENT_APKINSTALLENABLED_COLUMN = 13;
		
		public static final String[] CONTENT_PROJECTION = new String[] {
			RECORD_ID, ApkProfileColumns.APK_ID, ApkProfileColumns.APK_NAME,
			ApkProfileColumns.APK_URL, ApkProfileColumns.APK_DESC,ApkProfileColumns.APK_VERSION,ApkProfileColumns.APK_VERSION_CLIENT,ApkProfileColumns.APK_FLAG,
			ApkProfileColumns.ICONCOLOR,ApkProfileColumns.ICONGREY,ApkProfileColumns.APK_FILE_NAME,
			ApkProfileColumns.APK_FILE_PATH,ApkProfileColumns.APK_PACKAGE_NAME,ApkProfileColumns.APK_INSTALLENABLED

		};
		//shxn �޸����ⵥ2686 begin
		public static final String[] CONTENT_ONLY_APK_ID = new String[] {
			RECORD_ID, ApkProfileColumns.APK_ID
		};
		//shxn �޸����ⵥ2686 end
		public String mApkId;
		public String mApkName;
		public String mApkUrl;
		public String mApkDesc;
		public String mApkVersion;
		public String mApkVersionClient;
		public String mApkFlag;
		public String mIconColor;
		public String mIconGrey;
		public String mApkFileName;
		public String mApkFilePath;
		public String mApkPackageName;
		public String mApkInstallEnabled;
		
		public ApkProfileContent()
		{
			mBaseUri = CONTENT_URI;
		}
		@Override
		public ApkProfileContent restore(Cursor cursor) {
			// TODO Auto-generated method stub
			mApkId = cursor.getString(CONTENT_APKID_COLUMN);
			mApkName = cursor.getString(CONTENT_APKNAME_COLUMN);
			mApkUrl = cursor.getString(CONTENT_APKURL_COLUMN);
			mApkDesc = cursor.getString(CONTENT_APKDESC_COLUMN);
			mApkVersion = cursor.getString(CONTENT_APKVERSION_COLUMN);
			mApkFlag = cursor.getString(CONTENT_APKFLAG_COLUMN);
			mApkVersionClient = cursor.getString(CONTENT_APKVERSIONCLIENT_COLUMN);
			//mIconColor = cursor.getString(CONTENT_ICONCOLOR_COLUMN);
			//mIconGrey = cursor.getString(CONTENT_ICONGREY_COLUMN);

			byte[] iconcolor = cursor.getBlob(CONTENT_ICONCOLOR_COLUMN);
			byte[] icongrey = cursor.getBlob(CONTENT_ICONGREY_COLUMN);
			if(iconcolor!=null){
				mIconColor = new String(iconcolor);
			}
			if(icongrey!=null){
				mIconGrey = new String (icongrey);
			}
			mApkFileName = cursor.getString(CONTENT_APKFILENAME_COLUMN);
			mApkFilePath = cursor.getString(CONTENT_APKFILEPATH_COLUMN);
			mApkPackageName = cursor.getString(CONTENT_APKPACKAGENAME_COLUMN);
			mApkInstallEnabled =  cursor.getString(CONTENT_APKINSTALLENABLED_COLUMN);
			return this;
		}

		@Override
		public ContentValues toContentValues() {
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();
			// Assign values for each row.
			values.put(ApkProfileColumns.APK_ID, mApkId);
			values.put(ApkProfileColumns.APK_NAME, mApkName);
			values.put(ApkProfileColumns.APK_URL, mApkUrl);
			values.put(ApkProfileColumns.APK_DESC, mApkDesc);
			values.put(ApkProfileColumns.APK_VERSION, mApkVersion);
			values.put(ApkProfileColumns.APK_VERSION_CLIENT, mApkVersionClient);
			values.put(ApkProfileColumns.APK_FLAG, mApkFlag);

			if(mIconColor!=null){
				values.put(ApkProfileColumns.ICONCOLOR,getImageData(mIconColor));
			}
			if(mIconGrey!=null){
				values.put(ApkProfileColumns.ICONGREY, getImageData(mIconGrey));
			}
			// values.put(ApkProfileColumns.ICONCOLOR, mIconColor);
			// values.put(ApkProfileColumns.ICONGREY, mIconGrey);

			values.put(ApkProfileColumns.APK_FILE_NAME, mApkFileName);
			values.put(ApkProfileColumns.APK_FILE_PATH, mApkFilePath);
			values.put(ApkProfileColumns.APK_PACKAGE_NAME, mApkPackageName);
			values.put(ApkProfileColumns.APK_INSTALLENABLED, mApkInstallEnabled);
			values.put(ApkProfileColumns.APK_VERSION, mApkVersion);
			values.put(ApkProfileColumns.APK_VERSION_CLIENT, mApkVersionClient);
			return values;
		}

		private static ApkProfileContent restoreApkProfileContentWithCursor(Cursor cursor) 
		{
			try {
				if (cursor.moveToFirst()) {
					return getContent(cursor, ApkProfileContent.class);
				} else {
					return null;
				}
			} finally {
				cursor.close();
			}
		}
		/**
		 * Get get the content through apkId and flag
		 * @param context context to access DB
		 * @param apkId 
		 * @return the record
		 */
		public static ApkProfileContent restoreApkProfileContentWithApkId(Context context, String apkId, byte flag) {
			String sFlag = String.valueOf(flag);
			Cursor c = context.getContentResolver().query(ApkProfileContent.CONTENT_URI, ApkProfileContent.CONTENT_PROJECTION,
					ApkProfileColumns.APK_ID+ "=?" + " and "  + ApkProfileColumns.APK_FLAG + "=?", 
					new String[]{apkId,sFlag}, null);
			return restoreApkProfileContentWithCursor(c);
		}

		public static ApkProfileContent restoreApkProfileContentWithApkId(Context context, String apkId, byte uflag, byte nflag) {
			String suFlag = String.valueOf(uflag);
			String snFlag = String.valueOf(nflag);
			Cursor c = context.getContentResolver().query(ApkProfileContent.CONTENT_URI, ApkProfileContent.CONTENT_PROJECTION,
					ApkProfileColumns.APK_ID+ "=?" + " and " + "(" +
					ApkProfileColumns.APK_FLAG + "=?" + " or " + 
					ApkProfileColumns.APK_FLAG + "=?" + ")",
					new String[]{apkId,suFlag,snFlag}, null);
			return restoreApkProfileContentWithCursor(c);
		}
		public static ApkProfileContent queryApkProfile(Context context,String pkgId,String version){
			Cursor c = context.getContentResolver().query(ApkProfileContent.CONTENT_URI, ApkProfileContent.CONTENT_PROJECTION,
					ApkProfileColumns.APK_ID+ "=?" + " and " +  
					ApkProfileColumns.APK_VERSION + "=?",
					new String[]{pkgId,version}, null);
			return restoreApkProfileContentWithCursor(c);
		}
		
		public static ApkProfileContent restoreApkProfileContentWithApkId(Context context,String pkgId,String version,byte uflag){
			String suFlag = String.valueOf(uflag);
			Cursor c = context.getContentResolver().query(ApkProfileContent.CONTENT_URI, ApkProfileContent.CONTENT_PROJECTION,
					ApkProfileColumns.APK_ID+ "=?" + " and " +  
					ApkProfileColumns.APK_FLAG+ "=?" + " and " +
					ApkProfileColumns.APK_VERSION + "=?",
					new String[]{pkgId,suFlag,version}, null);
			return restoreApkProfileContentWithCursor(c);
		}
		/**added by wjx 2011.7.19 14:05 
		 * @param context
		 * @param pkgId
		 * @param version
		 * @return
		 */
		public static ApkProfileContent queryApkProfileByPackageFlag(Context context,String pkg,byte flag){
			Cursor c = context.getContentResolver().query(ApkProfileContent.CONTENT_URI, ApkProfileContent.CONTENT_PROJECTION,
					ApkProfileColumns.APK_PACKAGE_NAME+ "=?" + " and " +  
					ApkProfileColumns.APK_FLAG + "=?",
					new String[]{pkg,String.valueOf(flag)}, null);
			return restoreApkProfileContentWithCursor(c);
		}
		/**added by wjx 2011.7.26 16:52 
		 * 
		 * @param context
		 * @param pkg
		 * @param clientVersion
		 * @return
		 */
		public static ApkProfileContent queryApkProfileByPackageCVersion(Context context,String pkg,String clientVersion){
			Cursor c = context.getContentResolver().query(ApkProfileContent.CONTENT_URI, ApkProfileContent.CONTENT_PROJECTION,
					ApkProfileColumns.APK_PACKAGE_NAME+ "=?" + " and " +  
					ApkProfileColumns.APK_VERSION_CLIENT + "=?",
					new String[]{pkg,clientVersion}, null);
			return restoreApkProfileContentWithCursor(c);
		}
		/**added by wjx 2011.7.19 14:05 
		 * @param context
		 * @param pkgId
		 * @param version
		 * @return
		 */
//		public static ApkProfileContent queryApkProfile(Context context,String pkgId,String version,byte flag){
//			Cursor c = context.getContentResolver().query(ApkProfileContent.CONTENT_URI, ApkProfileContent.CONTENT_PROJECTION,
//					ApkProfileColumns.APK_ID+ "=?" + " and " +  
//					ApkProfileColumns.APK_VERSION + "=?"+" and "+
//					ApkProfileColumns.APK_FLAG + "=?" ,
//					new String[]{pkgId,version,String.valueOf(flag)}, null);
//			return restoreApkProfileContentWithCursor(c);
//		}
		/**
		 * Get get the content through apkId
		 * @param context context to access DB
		 * @param rowId 
		 * @return the record
		 */
		public static ApkProfileContent restoreApkProfileContentWithId(Context context, long id) {
			Uri u = ContentUris.withAppendedId(ApkProfileContent.CONTENT_URI, id);
			Cursor c = context.getContentResolver().query(u, ApkProfileContent.CONTENT_PROJECTION,
					null, null, null);
			return restoreApkProfileContentWithCursor(c);
		}

		/**
		 * Get all records in db
		 * @param context context to access DB
		 * @return the arraylist of records
		 */			  
		public static ArrayList<ApkProfileContent> queryAllApkContents(Context context){
			ArrayList<ApkProfileContent> appList = new ArrayList<ApkProfileContent>();
			Uri u = ApkProfileContent.CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(u, ApkProfileContent.CONTENT_PROJECTION,
					null, null, null);
			StringBuilder builder = new StringBuilder();

			try{
				if (cursor.moveToFirst())
				{
					do 
					{
						ApkProfileContent data = getContent(cursor,ApkProfileContent.class);//restoreContent(cursor);
						appList.add(data);
						builder.append(cursor.getString(CONTENT_APKID_COLUMN))
						.append(" | ")
						.append(cursor.getString(CONTENT_APKNAME_COLUMN))
						.append("\n");
					}while(cursor.moveToNext());

					Log.i(TAG, builder.toString());
					return appList;
				}
				else
				{
					return null;
				}
			}finally {
				cursor.close();
			}


		}
		//shxn �޸����ⵥ2686 begin
		public static ArrayList<String> queryAllApkID(Context context) {
			ArrayList<String> apkList = new ArrayList<String>();

			Uri uri = ApkProfileContent.CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(uri,
					ApkProfileContent.CONTENT_ONLY_APK_ID, null, null, null);
			try {
				if (cursor.moveToFirst()) {
					do {
						int columnIndex = cursor.getColumnIndex(ApkProfileColumns.APK_ID);
						String value = cursor.getString(columnIndex);
						apkList.add(value);
					} while (cursor.moveToNext());

				}
			} finally {
				cursor.close();
			}

			return apkList;
		}
		//shxn �޸����ⵥ2686 end
		/**
		 * Get get the records which flag equals certain value
		 * @param context context to access DB
		 * @return the ArrayList of the records
		 */
		public static ArrayList<ApkProfileContent> queryApkContentswithFlag(Context context, byte flag)
		{
			ArrayList<ApkProfileContent> appList = new ArrayList<ApkProfileContent>();

			String flagvalue = String.valueOf(flag);
			StringBuilder builder = new StringBuilder();
			Cursor cursor = context.getContentResolver().query(ApkProfileContent.CONTENT_URI,
					ApkProfileContent.CONTENT_PROJECTION,
					ApkProfileColumns.APK_FLAG + "=?", new String[]{flagvalue}, null);

			try
			{
				if (cursor.moveToFirst())
				{
					do {
						ApkProfileContent data = getContent(cursor, ApkProfileContent.class);
						appList.add(data);
						builder.append(cursor.getString(CONTENT_APKID_COLUMN))
						.append(" | ")
						.append(cursor.getString(CONTENT_APKNAME_COLUMN))
						.append("\n");
					}while(cursor.moveToNext());
					Log.i(TAG, builder.toString());
					return appList;
				}
				else
				{
					return null;
				}
			} finally {
				cursor.close();
			}


		}

		/**
		 * update a record in db
		 * @param context context to access DB
		 * @param value ContentValues to update
		 * @return the num of Rows updated
		 */
		public static int updateApkContentwithRowId(Context context, long id, ContentValues value)
		{
			int numofRows = 0;
			if(context==null){
				return numofRows;
			}
			Uri u = ContentUris.withAppendedId(ApkProfileContent.CONTENT_URI, id);
			numofRows = context.getContentResolver().update(u, value,null,null);
			//numofRows = context.getContentResolver().update(ApkProfileContent.CONTENT_URI, value,
			//	ProfileContentProvider._ID + "=?", new String[] { strid});
			return numofRows;
		}
		
		public static int updateApkContentEnalbedwithApkId(Context context, String[] ids, boolean[] enables)
		{
			if (ids.length <= 0 || enables.length <= 0)
				return -1;
			int i = 0;
			while(i< ids.length)
			{
				Log.i("dbtest",String.valueOf(ids.length));
				Cursor cursor = context.getContentResolver().query(ApkProfileContent.CONTENT_URI,
						ApkProfileContent.CONTENT_PROJECTION,
						ApkProfileColumns.APK_ID + "=?", new String[]{ids[i]}, null);
				try{
				if (cursor.moveToFirst())
				{
					Log.i("dbtest","find it " + ids[i] + enables[i]);
					do {
						long id = cursor.getInt(cursor.getColumnIndex(ApkProfileColumns._ID));  
						ContentValues data = new ContentValues();
						data.put(ApkProfileColumns.APK_INSTALLENABLED, String.valueOf(enables[i]));
						
						Uri updateUri = ContentUris.withAppendedId(ApkProfileContent.CONTENT_URI, id); 
						Log.i("dbtest","id is" + String.valueOf(id));
						int num = context.getContentResolver().update(updateUri, data,null,null);
						Log.i("dbtest","num of " + String.valueOf(num));
						
					}while(cursor.moveToNext());
					
				}
				}
				finally{
					cursor.close();
				}
				i++;
				
			}
			return 0;

			

		}

		/**
		 * delete a record in db
		 * @param context context to access DB
		 * @param apkName 
		 * @param flag 
		 * @return the num of deleted row in db
		 */

		public static int deleteApkContentwithNameandFlag(Context context, String apkName, int flag)
		{
			int numofRows = 0;
			String sFlag = String.valueOf(flag);
			numofRows = context.getContentResolver().delete(ApkProfileContent.CONTENT_URI,
					ApkProfileColumns.APK_ID+ "=?" + " and " + ApkProfileColumns.APK_FLAG + "=?", 
					new String[]{apkName,sFlag});
			return numofRows;
		}
		public static int deleteApkContentwithNameandFlag(Context context, String apkName, int flag,int flag2)
		{
			int numofRows = 0;
			String sFlag = String.valueOf(flag);
			String sFlag2=String .valueOf(flag2);
			numofRows = context.getContentResolver().delete(ApkProfileContent.CONTENT_URI,
					ApkProfileColumns.APK_ID+ "=?" + " and (" + ApkProfileColumns.APK_FLAG + "=? or "+ApkProfileColumns.APK_FLAG + "=? )", 
					new String[]{apkName,sFlag,sFlag2});
			return numofRows;
		}

		/**
		 * delete a record in db
		 * @param context context to access DB
		 * @param id  
		 * @return the num of deleted row in db
		 */
		public static int deleteApkContentwithNameandFlag(Context context, long id)
		{
			int numofRows = 0;
			Uri u = ContentUris.withAppendedId(ApkProfileContent.CONTENT_URI, id);

			numofRows = context.getContentResolver().delete(u,null,null);
			return numofRows;
		}
		public String getInfo() {
			String ss="mApkId:"+mApkId
			+" mApkName:"+mApkName
			+" mApkUrl:"+mApkUrl
			+" mApkDesc:"+mApkDesc
			+" mApkVersion:"+mApkVersion
			+" mApkFlag:"+mApkFlag
			+" mIconColor:"+mIconColor
			+" mIconGrey:"+mIconGrey
			+" mApkFileName:"+mApkFileName
			+" mApkFilePath:"+mApkFilePath
			+" mApkPackageName:"+mApkPackageName
			+" mApkVersionClient:"+mApkVersionClient;
			return ss;
		}

	}

	/**
	 * RootProfile
	 * @author vivian.yun.feng
	 *
	 */
	public interface RootProfileColumns {
		public static final String ID = "_id";

		public static final String INDENTIFIER = "Indentifier";
		public static final String DISPLAYNAME = "displayName";
		public static final String TYPE = "type";
		public static final String DESCRIPTION = "description";
		public static final String UUID = "uuid";
		public static final String ORGANZATION = "orgnaztion";
		public static final String VERSION = "version";


	}
	public static final class RootProfile extends ProfileContent implements RootProfileColumns
	{
		public static final String TABLE_NAME = "RootProfile";
		public static final Uri CONTENT_URI = Uri.parse(ProfileContent.CONTENT_URI + "/RootProfile");

		public static final int CONTENT_ID_COLUMN = 0;
		public static final int CONTENT_INDENTIFIER_COLUMN = 1;
		public static final int CONTENT_DISPLAYNAME_COLUMN = 2;
		public static final int CONTENT_DESCRIPTION_COLUMN = 3;
		public static final int CONTENT_VERSION_COLUMN = 4;
		public static final int CONTENT_VERSION_CLIENT_COLUMN = 5;
		public static final int CONTENT_UUID_KEY_COLUMN = 6;
		public static final int CONTENT_TYPE_COLUMN = 7;
		public static final int CONTENT_ORGANZATION_COLUMN = 8;

		public static final String[] CONTENT_PROJECTION = new String[] {
			RECORD_ID, RootProfileColumns.INDENTIFIER, RootProfileColumns.DISPLAYNAME,
			RootProfileColumns.DESCRIPTION, RootProfileColumns.VERSION,RootProfileColumns.UUID, RootProfileColumns.TYPE,
			RootProfileColumns.ORGANZATION

		};

		public String mIndentifier;
		public String mDisplayName;
		public String mType;
		public String mDescription;
		public String mVersion;
		public String mUuid;
		public String mOrganzation;

		public RootProfile()
		{
			mBaseUri = CONTENT_URI;
		}
		@SuppressWarnings("unchecked")
		@Override
		public  RootProfile restore(Cursor cursor) {
			// TODO Auto-generated method stub
			mIndentifier = cursor.getString(CONTENT_INDENTIFIER_COLUMN);
			mDisplayName = cursor.getString(CONTENT_DISPLAYNAME_COLUMN);
			mType = cursor.getString(CONTENT_TYPE_COLUMN);
			mDescription = cursor.getString(CONTENT_DESCRIPTION_COLUMN);
			mVersion = cursor.getString(CONTENT_VERSION_COLUMN);
			mUuid = cursor.getString(CONTENT_UUID_KEY_COLUMN);
			mOrganzation = cursor.getString(CONTENT_ORGANZATION_COLUMN);

			return this;
		}

		@Override
		public ContentValues toContentValues() 
		{
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();

			// Assign values for each row.
			values.put(RootProfileColumns.INDENTIFIER, mIndentifier);
			values.put(RootProfileColumns.DISPLAYNAME, mDisplayName);
			values.put(RootProfileColumns.TYPE, mType);
			values.put(RootProfileColumns.DESCRIPTION, mDescription);
			values.put(RootProfileColumns.VERSION, mVersion);
			values.put(RootProfileColumns.UUID, mUuid);
			values.put(RootProfileColumns.ORGANZATION, mOrganzation);
			return values;

		}

		private static RootProfile restoreBodyWithCursor(Cursor cursor) 
		{
			try {
				if (cursor.moveToFirst()) {
					return getContent(cursor, RootProfile.class);
				} else {
					return null;
				}
			} finally {
				cursor.close();
			}
		}

		public static RootProfile restoreRootProfileWithId(Context context, long id) {
			Uri u = ContentUris.withAppendedId(RootProfile.CONTENT_URI, id);
			Cursor c = context.getContentResolver().query(u, RootProfile.CONTENT_PROJECTION,
					null, null, null);
			return restoreBodyWithCursor(c);
		}
		public static RootProfile restoreRootProfileWithType(Context context, String Type) {
			// Uri u = ContentUris.withAppendedId(RootProfile.CONTENT_URI, id);
			Cursor c = context.getContentResolver().query(RootProfile.CONTENT_URI, RootProfile.CONTENT_PROJECTION,
					RootProfileColumns.TYPE+ "=?",
					new String[]{Type}, null);
			return restoreBodyWithCursor(c);


		}
	}
	/**
	 * RootProfile
	 * @author vivian.yun.feng
	 *
	 */
	public interface ProfilesColumns {
		public static final String ID = "_id";

		public static final String CID = "cid";
		public static final String UUID = "Uuid";
		public static final String NAME = "name";
		public static final String TYPE = "profileType";

		public static final String VERSION = "version";
//		 public static final String VERSIONCLIENT = "version_client";
		// public static final String ICON = "icon";
		public static final String NOTE = "note";


	}
	public static final class Profiles extends ProfileContent implements ProfilesColumns
	{
		public static final String TABLE_NAME = "Profiles";
		public static final Uri CONTENT_URI = Uri.parse(ProfileContent.CONTENT_URI + "/Profiles");

		public static final int CONTENT_ID_COLUMN = 0;
		public static final int CONTENT_CID_COLUMN = 1;
		public static final int CONTENT_UUID_COLUMN = 2;
		public static final int CONTENT_NAME_COLUMN = 3;
		public static final int CONTENT_TYPE_COLUMN = 4;

		public static final int CONTENT_VERSION_COLUMN = 5;
		public static final int CONTENT_NOTE_COLUMN = 6;

		public static final String[] CONTENT_PROJECTION = new String[] {
			RECORD_ID, ProfilesColumns.CID,ProfilesColumns.UUID, 
			ProfilesColumns.NAME,ProfilesColumns.TYPE,
			RootProfileColumns.VERSION,ProfilesColumns.NOTE
		};
		public Profiles()
		{
			mBaseUri = CONTENT_URI;
			//mCid = "-1";
		}

		public String mCid;
		public String mUuid;
		public String mName;
		public String mType;
		public String mVersion;
		public String mVersionClient;
		public String mNote;

		@Override
		public Profiles restore(Cursor cursor) {
			// TODO Auto-generated method stub
			mCid = cursor.getString(CONTENT_CID_COLUMN);
			mUuid = cursor.getString(CONTENT_UUID_COLUMN);
			mName = cursor.getString(CONTENT_NAME_COLUMN);
			mType = cursor.getString(CONTENT_TYPE_COLUMN);
			mVersion = cursor.getString(CONTENT_VERSION_COLUMN);
			mNote = cursor.getString(CONTENT_NOTE_COLUMN);
			return this;
		}

		@Override
		public ContentValues toContentValues() {
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();

			// Assign values for each row.
			values.put(ProfilesColumns.CID, mCid);
			values.put(ProfilesColumns.UUID, mUuid);
			values.put(ProfilesColumns.NAME, mName);
			values.put(ProfilesColumns.TYPE, mType);
			values.put(ProfilesColumns.VERSION, mVersion);
			values.put(ProfilesColumns.NOTE, mNote);
			return values;
		}
		private static Profiles restoreProfilesWithCursor(Cursor cursor) 
		{
			try {
				if (cursor.moveToFirst()) {
					return getContent(cursor, Profiles.class);
				} else {
					return null;
				}
			} finally {
				cursor.close();
			}
		}

		public static Profiles restoreProfilesWithId(Context context, long id) {
			Uri u = ContentUris.withAppendedId(Profiles.CONTENT_URI, id);
			Cursor c = context.getContentResolver().query(u, Profiles.CONTENT_PROJECTION,
					null, null, null);
			return restoreProfilesWithCursor(c);
		}
		
		public static Profiles restoreProfilesWithUuid(Context context, String id) {
			//Uri u = ContentUris.withAppendedId(Profiles.CONTENT_URI, id);
			Cursor c = context.getContentResolver().query(Profiles.CONTENT_URI, Profiles.CONTENT_PROJECTION,
					ProfilesColumns.UUID + "=?", new String[]{id}, null);
			return restoreProfilesWithCursor(c);
		}
		

		public static Profiles restoreProfilesWithName(Context context, String type, String name) {

			Cursor c = context.getContentResolver().query(Profiles.CONTENT_URI, Profiles.CONTENT_PROJECTION,
					ProfilesColumns.TYPE + "=?" + " and " + ProfilesColumns.NAME + "=?", new String[]{type,name}, null);
			return restoreProfilesWithCursor(c);


		}

		//query all record which version does not match parameter
		public static ArrayList<Profiles> restoreProfilesWithTypeandVersion(Context context, String type, String version) {

			ArrayList<Profiles> list = new ArrayList<Profiles>();
			Cursor cursor = context.getContentResolver().query(Profiles.CONTENT_URI, Profiles.CONTENT_PROJECTION,
					ProfilesColumns.TYPE + "=?" + " and " + ProfilesColumns.VERSION + "!=?", new String[]{type,version}, null);

			try{
				if(cursor.moveToFirst())
				{
					do 
					{
						Profiles i =  getContent(cursor, Profiles.class);//restoreProfilesWithCursor(cursor);
						list.add(i);

					}while(cursor.moveToNext());
					return list;
				}
				else
				{
					return null;
				}
			}finally{
				cursor.close();

			}
		}

		public static Profiles restoreProfilesWithType(Context context, String type, long cid) {

			String sCID = String.valueOf(cid);
			Cursor c = context.getContentResolver().query(Profiles.CONTENT_URI, Profiles.CONTENT_PROJECTION,
					ProfilesColumns.TYPE + "=?" + " and " + ProfilesColumns.CID + "=?", new String[]{type,sCID}, null);
			return restoreProfilesWithCursor(c);


		}
		public static int deleteProfileswithVersion(Context context, String version)
		{
			int numofRows = 0;
			numofRows = context.getContentResolver().delete(Profiles.CONTENT_URI,
					ProfilesColumns.VERSION+ "=?", 
					new String[]{version});
			return numofRows;
		}

		public static int deleteProfileswithId(Context context,  long id)
		{
			Uri u = ContentUris.withAppendedId(Profiles.CONTENT_URI, id);
			int numofRows = 0;
			numofRows = context.getContentResolver().delete(u,
					null, 
					null);
			return numofRows;
		}

	}
	/**
	 * EmailProfile
	 * @author vivian.yun.feng
	 *
	 */
	public interface EmailColumns {
		public static final String ID = "_id";

		public static final String EMAILADDRESS = "EmailAddress";
		public static final String HOST = "Host";
		public static final String PASSWORD = "Password";
		public static final String SSL = "SSL";
		public static final String USERNAME = "UserName";



	}
	public static final class EmailProfileContent extends ProfileContent implements EmailColumns
	{
		public static final String TABLE_NAME = "EmailProfile";
		public static final Uri CONTENT_URI = Uri.parse(ProfileContent.CONTENT_URI + "/EmailProfile");

		public static final int CONTENT_ID_COLUMN = 0;
		public static final int CONTENT_EMAILADDRESS_COLUMN = 1;
		public static final int CONTENT_HOST_COLUMN = 2;
		public static final int CONTENT_PASSWORD_COLUMN = 3;
		public static final int CONTENT_SSL_COLUMN = 4;
		public static final int CONTENT_USERNAME_COLUMN = 5;


		public static final String[] CONTENT_PROJECTION = new String[] {
			RECORD_ID,EmailColumns.EMAILADDRESS, EmailColumns.HOST,EmailColumns.PASSWORD,
			EmailColumns.SSL,EmailColumns.USERNAME

		};

		public String mEmailAddress;
		public String mHost;
		public String mPassword;
		public String mSSL;
		public String mUserName;


		public EmailProfileContent()
		{
			mBaseUri = CONTENT_URI;
		}
		@SuppressWarnings("unchecked")
		@Override
		public  EmailProfileContent restore(Cursor cursor) {
			// TODO Auto-generated method stub
			mEmailAddress = cursor.getString(CONTENT_EMAILADDRESS_COLUMN);
			mHost = cursor.getString(CONTENT_HOST_COLUMN);
			mPassword = cursor.getString(CONTENT_PASSWORD_COLUMN);
			mSSL = cursor.getString(CONTENT_SSL_COLUMN);
			mUserName = cursor.getString(CONTENT_USERNAME_COLUMN);
			return this;
		}

		@Override
		public ContentValues toContentValues() 
		{
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();

			// Assign values for each row.
			values.put(EmailColumns.EMAILADDRESS, mEmailAddress);
			values.put(EmailColumns.HOST, mHost);
			values.put(EmailColumns.PASSWORD, mPassword);
			values.put(EmailColumns.SSL, mSSL);
			values.put(EmailColumns.USERNAME, mUserName);

			return values;

		}

		private static EmailProfileContent restoreBodyWithCursor(Cursor cursor) 
		{
			try {
				if (cursor.moveToFirst()) {
					return getContent(cursor, EmailProfileContent.class);
				} else {
					return null;
				}
			} finally {
				cursor.close();
			}
		}

		public static EmailProfileContent restoreRootProfileWithId(Context context, long id) {
			Uri u = ContentUris.withAppendedId(EmailProfileContent.CONTENT_URI, id);
			Cursor c = context.getContentResolver().query(u, EmailProfileContent.CONTENT_PROJECTION,
					null, null, null);
			return restoreBodyWithCursor(c);
		}



	}

	/**
	 * CertificateProfile
	 * @author vivian.yun.feng
	 *
	 */
	public interface CertificateColumns {
		public static final String ID = "_id";

		public static final String UUID = "UUID";
		public static final String NAME = "name";
		public static final String TYPE = "type";
		public static final String PASSWORD = "password";
		public static final String DATA = "data";


	}
	public static final class CertificateContent extends ProfileContent implements CertificateColumns
	{
		public static final String TABLE_NAME = "Certificate";
		public static final Uri CONTENT_URI = Uri.parse(ProfileContent.CONTENT_URI + "/Certificate");

		public static final int CONTENT_ID_COLUMN = 0;
		public static final int CONTENT_UUID_COLUMN = 1;
		public static final int CONTENT_NAME_COLUMN = 2;
		public static final int CONTENT_TYPE_COLUMN = 3;
		public static final int CONTENT_PASSWORD_COLUMN = 4;
		public static final int CONTENT_DATA_COLUMN = 5;


		public static final String[] CONTENT_PROJECTION = new String[] {
			RECORD_ID,CertificateColumns.UUID, CertificateColumns.NAME,CertificateColumns.TYPE,
			CertificateColumns.PASSWORD,CertificateColumns.DATA

		};
		public String mUUID;
		public String mName;
		public String mType;
		public String mPassword;
		public String mData;

		public CertificateContent()
		{
			mBaseUri = CONTENT_URI;
		}
		@Override
		public  CertificateContent restore(Cursor cursor) {
			// TODO Auto-generated method stub
			mUUID = cursor.getString(CONTENT_UUID_COLUMN);
			mName = cursor.getString(CONTENT_NAME_COLUMN);
			mType = cursor.getString(CONTENT_TYPE_COLUMN);
			mPassword = cursor.getString(CONTENT_PASSWORD_COLUMN);
			mData = cursor.getString(CONTENT_DATA_COLUMN);

			return this;
		}
		@Override
		public ContentValues toContentValues() {
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();

			// Assign values for each row.
			values.put(CertificateColumns.UUID, mUUID);
			values.put(CertificateColumns.NAME, mName);
			values.put(CertificateColumns.TYPE, mType);
			values.put(CertificateColumns.PASSWORD, getImageData(mPassword));
			values.put(CertificateColumns.DATA, mData);

			return values;
		}





	}
	/**
	 * WebclipProfile
	 * @author vivian.yun.feng
	 *
	 */
	public interface WebclipColumns {
		public static final String ID = "_id";

		public static final String IDENTIFIER = "identifier";
		public static final String DISPAYNAME = "dispayName";
		public static final String URL = "url";
		public static final String ICON = "icon";
		public static final String ISREMOVEABLE = "flag";
		public static final String VERSION = "version";
	}
	public static final class WebClipProfileContent extends ProfileContent implements WebclipColumns
	{
		public static final String TABLE_NAME = "WebClipProfile";
		public static final Uri CONTENT_URI = Uri.parse(ProfileContent.CONTENT_URI + "/WebClipProfile");

		public static final int CONTENT_ID_COLUMN = 0;
		public static final int CONTENT_IDENTIFIER_COLUMN = 1;
		public static final int CONTENT_DISPAYNAME_COLUMN = 2;
		public static final int CONTENT_URL_COLUMN = 3;
		public static final int CONTENT_ICON_COLUMN = 4;
		public static final int CONTENT_ISREMOVEABLE_COLUMN = 5;
		public static final int CONTENT_VERSION_COLUMN = 6;

		public static final String[] CONTENT_PROJECTION = new String[] {
			RECORD_ID,WebclipColumns.IDENTIFIER, WebclipColumns.DISPAYNAME,WebclipColumns.URL,
			WebclipColumns.ICON,WebclipColumns.ISREMOVEABLE,WebclipColumns.VERSION
		};
		
		public int mIsRemovable;
		public String mWebClipName;
		public String mWebClipUrl;
		public String mWebClipIcon;
		public String mWebClipIdentifier;
		public String mWebClipVersion;
		public int    mWebClipFlag; //fix bug2734 by cuixiaowei 20110725
		public WebClipProfileContent()
		{
			mBaseUri = CONTENT_URI;
		}
		@Override
		public  WebClipProfileContent restore(Cursor cursor) {
			// TODO Auto-generated method stub
			mWebClipIdentifier = cursor.getString(CONTENT_IDENTIFIER_COLUMN);
			mWebClipName = cursor.getString(CONTENT_DISPAYNAME_COLUMN);
			mWebClipUrl = cursor.getString(CONTENT_URL_COLUMN);
			mWebClipIcon = new String(cursor.getBlob(CONTENT_ICON_COLUMN));
			mIsRemovable = cursor.getInt(CONTENT_ISREMOVEABLE_COLUMN);
			mWebClipVersion  = cursor.getString(CONTENT_VERSION_COLUMN);

			return this;
		}
		@Override
		public ContentValues toContentValues() {
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();

			// Assign values for each row.
			values.put(WebclipColumns.IDENTIFIER, mWebClipIdentifier);
			values.put(WebclipColumns.DISPAYNAME, mWebClipName);
			values.put(WebclipColumns.URL, mWebClipUrl);
			values.put(WebclipColumns.ICON, getImageData(mWebClipIcon));
			values.put(WebclipColumns.ISREMOVEABLE, mIsRemovable);
			values.put(WebclipColumns.VERSION, mWebClipVersion);

			return values;
		}
		private static WebClipProfileContent restoreBodyWithCursor(Cursor cursor) 
		{
			try {
				if (cursor.moveToFirst()) {
					return getContent(cursor, WebClipProfileContent.class);
				} else {
					return null;
				}
			} finally {
				cursor.close();
			}
		}

		public static WebClipProfileContent restoreWebClipProfileWithId(Context context, long id) {
			Uri u = ContentUris.withAppendedId(WebClipProfileContent.CONTENT_URI, id);
			Cursor c = context.getContentResolver().query(u, WebClipProfileContent.CONTENT_PROJECTION,
					null, null, null);
			return restoreBodyWithCursor(c);
		}
		public static ArrayList<WebClipProfileContent> restoreWebClipWithVersion(Context context, String version) {
			Cursor cursor = context.getContentResolver().query(WebClipProfileContent.CONTENT_URI, 
					WebClipProfileContent.CONTENT_PROJECTION, WebclipColumns.VERSION+ "!=?",
					new String[]{version}, null);

			if (cursor.getCount() == 0)
			{
				return null;
			}
			else
			{
				ArrayList<WebClipProfileContent> list = new ArrayList<WebClipProfileContent>();
				try{
					if (cursor.moveToFirst())
					{
						do 
						{
							WebClipProfileContent i =  getContent(cursor, WebClipProfileContent.class);// restoreBodyWithCursor(cursor);
							list.add(i);
						}while(cursor.moveToNext());
						return list;
					}
					else
					{
						return null;
					}
				}finally {
					cursor.close();
				}
			}
		}

		public static WebClipProfileContent restoreWebClipWithContent(Context context, String name,String url, String icon) {
			Cursor c = context.getContentResolver().query(WebClipProfileContent.CONTENT_URI, 
					WebClipProfileContent.CONTENT_PROJECTION, 
					WebclipColumns.DISPAYNAME+ "=?" + " and " + 
					WebclipColumns.URL+ "=?" + " and " +
					WebclipColumns.ICON + "=?",
					new String[]{name,url,icon}, null);
			return restoreBodyWithCursor(c);
		}

		public static int deleteWebClipWithVersion(Context context, String Version)
		{
			int numofRows = 0;
			numofRows = context.getContentResolver().delete(WebClipProfileContent.CONTENT_URI,
					WebclipColumns.VERSION + "=?",
					new String[]{Version});
			return numofRows;
		}
		public static int deleteWebClipWithId(Context context, long id)
		{
			int numofRows = 0;
			Uri u = ContentUris.withAppendedId(WebClipProfileContent.CONTENT_URI, id);

			numofRows = context.getContentResolver().delete(u,
					null,null);
			return numofRows;
		}

	}





}
