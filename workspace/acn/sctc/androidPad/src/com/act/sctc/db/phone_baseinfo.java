package com.act.sctc.db;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;

public class phone_baseinfo extends DatabaseColumn {

	public static final String TABLE_NAME			= 	"phone_baseinfo";
	
	public static final String COLOR="color"				;
	public static final String PHONE_ID="phone_id"				;
	public static final String CATEGORY="category"			;
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	private static final Map<String,String> mColumnMap = new HashMap<String,String>();
	 static {
		 
		 mColumnMap.put(_ID, "integer primary key autoincrement");
		 mColumnMap.put(COLOR, "int not null");
		 mColumnMap.put(PHONE_ID, "int not null");
		 mColumnMap.put(CATEGORY, "int not null");
	 }
	 
	 /** selection all,default */
		public static final String SELECTION[] = { _ID, COLOR,PHONE_ID,CATEGORY };
	 
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	public Uri getTableContent() {
		// TODO Auto-generated method stub
		return CONTENT_URI;
	}

	@Override
	protected Map<String, String> getTableMap() {
		// TODO Auto-generated method stub
		return mColumnMap;
	}

}
