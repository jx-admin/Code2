package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class CustomerColumn extends DatabaseColumn {
	public static final String TAG=CustomerColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"customer";
	
	/**只能设置三种值:
	 */
	public static final String name					=	"name";
	
	public static final String phone				=	"phone";
	
	public static final String licence				=	"licence";
	
	public static final String address				=	"address";
	
	public static final String mark					=	"mark";
	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table customer( _id integer primary key autoincrement, name text not null, phone	text, licence	text, address	text, mark text )";
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
