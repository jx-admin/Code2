package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class BusinessDetailColumn extends DatabaseColumn {
	public static final String TAG=BusinessDetailColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"business_detail";
	
	/**只能设置三种值:
	 */
	public static final String business_id			=	"business_id";
	
	public static final String RESOURCE_ID			=	"resource_id";
	
	public static final String TITLE				=	"title";
	
	public static final String SUBTITLE				=	"subtitle";
	
	public static final String ICON					=	"icon";
	
	public static final String SORT					=	"sort";
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table business_detail( _id integer primary key autoincrement, business_id integer not null REFERENCES business(_id), resource_id	integer REFERENCES resource(_id), title text not null, subtitle text not null, icon integer  REFERENCES resource(_id), sort integer )";
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
