package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class UserColumn extends DatabaseColumn {
	public static final String TAG=UserColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"user";
	
	/**只能设置三种值:
	 */
	public static final String USERNAME				=	"username";
	
	public static final String password				=	"password";
	
	public static final String eid					=	"eid";
	
	public static final String token				=	"token";
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "CREATE TABLE user( _id integer primary key autoincrement, username text, password text not null, eid text,token text )";
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
