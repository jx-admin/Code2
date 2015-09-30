package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class BusinessColumn extends DatabaseColumn {
	public static final String TAG=BusinessColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"business";
	
	/**只能设置三种值:
	 */
	public static final String name					=	"name";
		
	public static final String img					=	"img";
	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table business( _id integer primary key autoincrement, name text not null, img integer REFERENCES resource(_id) )";
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
