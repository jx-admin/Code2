package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class CategoryColumn extends DatabaseColumn {
	public static final String TAG=CategoryColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"category";
	/**只能设置三种值:
	 */
	public static final String business_id			=	"business_id";
	
	public static final String subclass				=	"subclass";
	
	public static final String name					=	"name";
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table category( _id integer primary key autoincrement, business_id integer not null REFERENCES business(_id), subclass integer not null, name text )";
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
