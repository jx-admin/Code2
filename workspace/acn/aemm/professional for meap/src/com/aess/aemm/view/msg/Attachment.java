package com.aess.aemm.view.msg;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.w3c.dom.Element;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.aess.aemm.db.AContent;
import com.aess.aemm.db.AemmProvider;
import com.aess.aemm.db.NewsContent;

public class Attachment extends AContent{
	
	public static final String TAG="Attachment";
	
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final String VIDEO_UNSPECIFIED = "video/*";
	public static final String DOC_PDF="application/pdf";

	
	public final static String DATABASE_NAME = "attachment";
	// public static final String DATEFORMAT = "yyyy-MM-dd:HH";
	public static final Uri CONTENT_URI = Uri.parse(AemmProvider.CONTENT_URI+ "/" + DATABASE_NAME);

	public static final class DBTable implements BaseColumns {
		public static final String TYPE = "type";
		public static final String MIME = "mime";
		public static final String NAME = "name";
		public static final String URI = "uri";
		public static final String STATE = "state";
		public static final String ICON64 = "icon64";
		public static final String COMMANDID="commandId";
		public static final String FILEPATH="filepath";

		public static final int ID_COLUMN = 0;
		public static final int TYPE_COLUMN = 1;
		public static final int MIME_COLUMN = 2;
		public static final int NAME_COLUMN = 3;
		public static final int URI_COLUMN = 4;
		public static final int STATE_COLUMN = 5;
		public static final int ICON64_COLUMN=6;
		public static final int COMMANDID_COLUMN=7;
		public static final int FILEPATH_COLUMN=8;
		
		public static void create(SQLiteDatabase database) {
			String s = " (" + DBTable._ID+" integer primary key autoincrement, "
					+ DBTable.TYPE + " text, "
					+ DBTable.MIME + " text, "
					+ DBTable.NAME + " text, "
					+ DBTable.URI + " text, "
					+ DBTable.STATE + " interger, "
					+ DBTable.ICON64+" blob," 
					+ DBTable.COMMANDID+" text,"
					+ DBTable.FILEPATH+" text);";
			database.execSQL("create table if not exists " + DATABASE_NAME + s);
		}
		
		public static final String[] CONTENT_PROJECTION = new String[] { DBTable._ID, DBTable.TYPE,
			DBTable.MIME, DBTable.NAME, DBTable.URI, DBTable.STATE ,DBTable.ICON64,DBTable.COMMANDID,DBTable.FILEPATH};
	}
	
	//protocol
	public static final String type="type";
	public static final String mime="mime";
	public static final String name="name";
	public static final String download_url="download-url";
	
	public Uri mDownloadUri;
	public String mType;
	public String mMimeType;
	public String mName;
	public int mState=NewsContent.MSG_UNREADED;
	public byte[] mIcon64;
	public String mCommandId;
	public String mFile;
	
	public static final String DataFormat = "yyyy-MM-dd";
	
	public Attachment() {
		mBaseUri = this.CONTENT_URI;
	}
	
	public Bitmap getIcon(){
		if(mIcon64==null||mIcon64.length<=0){
			return null;
		}
		byte[] temp = Base64.decode(mIcon64,0);
		InputStream is  = new ByteArrayInputStream(temp);
		return BitmapFactory.decodeStream(is);
	}
	
	public String getState(){
		if(mState==0){
			return "已读";
		}else if(mState==1){
			return "记载失败";
		}else{
			return "无法打开";
		}
	}
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
//		values.put(DBColumns._ID, _id);
		values.put(DBTable.TYPE, mType);
		values.put(DBTable.MIME, mMimeType);
		values.put(DBTable.NAME, mName);
		values.put(DBTable.URI, mDownloadUri.toString());
		values.put(DBTable.STATE, mState);
		values.put(DBTable.ICON64, mIcon64);
		values.put(DBTable.COMMANDID,mCommandId);
		values.put(DBTable.FILEPATH,mFile);
		return values;
	}

	public <T extends AContent> T restore(Cursor cursor) {
			mId = cursor.getLong(DBTable.ID_COLUMN);
			mType = cursor.getString(DBTable.TYPE_COLUMN);
			mMimeType = cursor.getString(DBTable.MIME_COLUMN);
			mName = cursor.getString(DBTable.NAME_COLUMN);
			mDownloadUri = Uri.parse(cursor.getString(DBTable.URI_COLUMN));
			mState = cursor.getInt(DBTable.STATE_COLUMN);
			mIcon64 =cursor.getBlob(DBTable.ICON64_COLUMN); 
			mCommandId=cursor.getString(DBTable.COMMANDID_COLUMN);
			mFile=cursor.getString(DBTable.FILEPATH_COLUMN);
		return (T) this;
	}

	public static int delByConmandId(Context context, String conmandId) {
		int numofRows = 0;
		if(conmandId!=null){
			numofRows=context.getContentResolver().delete(CONTENT_URI, Attachment.DBTable.COMMANDID+" = "+conmandId, null);
		}
		return numofRows;
	}
	
	public static int deleteAllContent(Context context) {
		return context.getContentResolver().delete(CONTENT_URI, null, null);
	}

	public static Cursor queryMsgByURI(Context context, Uri uri) {
		return context.getContentResolver().query(uri, DBTable.CONTENT_PROJECTION,
				null, null, null);
	}

	public static Attachment getContentByCursor(Cursor cursor) {
		Attachment nc = null;
		if (null != cursor) {
			if (cursor.moveToFirst()) {
				nc = getContent(cursor, Attachment.class);
			}
			cursor.close();
		}
		return nc;
	}
	

	public static Cursor getAttachments(Context context, String commandId) {
		return context.getContentResolver().query(CONTENT_URI, DBTable.CONTENT_PROJECTION,
				DBTable.COMMANDID+ " = "+commandId, null, null);
	}
	
	
	public static Attachment getContentByURI(Context cxt, Uri uri) {
		Cursor cursor = Attachment.queryMsgByURI(cxt, uri);
		Attachment nc = null;
		if (null != cursor) {
			nc = Attachment.getContentByCursor(cursor);
		}
		return nc;
	}
	
	public static int deleteAllFiles(Context cxt){
		int c=0;
		Cursor cursor = cxt.getContentResolver().query(CONTENT_URI,new String[]{DBTable.FILEPATH},
				null, null, null);
		if(cursor!=null&&cursor.moveToFirst()){
			String file;
			do{
				file=cursor.getString(0);
				if(!TextUtils.isEmpty(file))
				cxt.deleteFile(file);
			}while(cursor.moveToNext());
		}
		c= cursor.getCount();
		cursor.close();
		return c;
	}
	
	public static Attachment xmlParser(Element element,String mCommandId){
		Attachment attach=new Attachment();
		attach.mType=element.getAttribute(Attachment.type);
		attach.mMimeType=element.getAttribute(Attachment.mime);
		attach.mName=element.getAttribute(Attachment.name);
		attach.mDownloadUri=Uri.parse(element.getAttribute(Attachment.download_url));
		attach.mCommandId=mCommandId;
		Log.d(TAG,"xmlParser:"+element.toString());
		return attach;
	}
	
	public String getInfo(){
		String info="%d,%s,%s,%s,%s,%d,%d,%s;";
		return String.format(info,mId,
				mType,mMimeType,mName,mDownloadUri.toString(),mIcon64.length,mState,mCommandId);
	}
	
	public static void testAdd(Context context){
		Attachment attach=new Attachment();
		attach.mType="type";
		attach.mMimeType="mimetype";
		attach.mName="attach";
		attach.mDownloadUri=Uri.parse("http://www.baidu.com");
		attach.mState=0;
		attach.mIcon64=new byte[]{3,3,2};
		attach.mCommandId="commandid";
		attach.add(context);
		Log.d(TAG,"save");
	}
	
	public static void testQuery(Context context){
		Cursor mCursor=getAttachments(context,"commandid");
		if(mCursor!=null&&mCursor.moveToFirst()){
		Attachment attach=new Attachment();
		attach.restore(mCursor);
		Log.d(TAG,"query:"+attach.getInfo());}
		else Log.d(TAG,"not find with commandid");
	}
}
