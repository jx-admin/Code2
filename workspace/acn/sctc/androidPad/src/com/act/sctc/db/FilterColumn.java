package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class FilterColumn extends DatabaseColumn {
	public static final String TAG=FilterColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"filter";


	public static final String name					=	"name";
	
	public static final String category_id			=	"category_id";
	
	public static final String used					=	"used";
	
	public static final String sort					=	"sort";
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "CREATE TABLE filter( _id integer primary key autoincrement, name text not null, category_id integer not null REFERENCES category(_id), used integer default 0, sort integer default 0 )";
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
