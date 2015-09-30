package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class PhoneLocalColumn extends DatabaseColumn {
	public static final String TAG=PhoneLocalColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"phone_local";
	
	
	public static final String PRICE				= 	"price";
	
	/**只能设置三种值:
	 */
	public static final String VISIBILITY			=	"visibility";
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table phone_local( _id integer primary key, price float, visibility boolean )";
	}
	
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
		return null;
	}
	
}
