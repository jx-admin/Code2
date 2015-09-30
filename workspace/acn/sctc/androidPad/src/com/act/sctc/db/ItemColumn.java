package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class ItemColumn extends DatabaseColumn {
	public static final String TAG=ItemColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"item";


	public static final String filter_id					=	"filter_id";
	
	public static final String name			=	"name";
	
	public static final String sort					=	"sort";
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table item( _id integer primary key autoincrement, filter_id integer not null REFERENCES filter(_id), name text not null, sort int not null )";
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
