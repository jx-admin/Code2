package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class PhoneContractColumn extends DatabaseColumn {
	public static final String TAG=PhoneContractColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"phone_contract";
	
	public static final String ad_desc				=	"ad_desc";
	public static final String name					=	"name";
	public static final String type					=	"type";
	public static final String price				=	"price";
	public static final String attr_name1			=	"attr_name1";
	public static final String attr_value1			=	"attr_value1";
	public static final String attr_name2			=	"attr_name2";
	public static final String attr_value2			=	"attr_value2";
	public static final String attr_name3			=	"attr_name3";
	public static final String attr_value3			=	"attr_value3";
	public static final String attr_name4			=	"attr_name4";
	public static final String attr_value4			=	"attr_value4";
	public static final String attr_name5			=	"attr_name5";
	public static final String attr_value5			=	"attr_value5";
	public static final String attr_name6			=	"attr_name6";
	public static final String attr_value6 			=	"attr_value6";
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table phone_contract( _id integer primary key autoincrement, name text, type int, price float, attr_name1 text, attr_value1 text, attr_name2 text, attr_value2 text, attr_name3 text, attr_value3 text, attr_name4 text, attr_value4 text, attr_name5 text, attr_value5 text, attr_name6 text, attr_value6 text )";
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
