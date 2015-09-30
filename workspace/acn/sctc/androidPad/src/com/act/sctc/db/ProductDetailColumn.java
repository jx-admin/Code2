package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class ProductDetailColumn extends DatabaseColumn {
	public static final String TAG=ProductDetailColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"product_detail";
	
	public static final String business_id			=	"business_id";
	public static final String title 				=	"title";
	public static final String img_id				=	"img_id";
	public static final String phone_id				=	"phone_id";
	public static final String sort					=	"sort";

	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table product_detail( _id integer primary key autoincrement, business_id integer not null REFERENCES business(_id), title text not null, img_id integer REFERENCES resource(_id), phone_id integer REFERENCES phone(_id), sort integer )";
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
