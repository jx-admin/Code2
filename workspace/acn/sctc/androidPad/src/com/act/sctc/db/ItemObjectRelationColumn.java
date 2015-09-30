package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class ItemObjectRelationColumn extends DatabaseColumn {
	public static final String TAG=ItemObjectRelationColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"item_object_relation";


	public static final String item_id				=	"item_id";
	
	public static final String object_id			=	"object_id";
	
	public static final String category_id			=	"category_id";

	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table item_object_relation( _id INTEGER PRIMARY KEY autoincrement, item_id integer not null REFERENCES item(_id), object_id integer not null, category_id integer ) ";
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
