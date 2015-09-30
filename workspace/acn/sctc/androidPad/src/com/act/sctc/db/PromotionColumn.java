package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class PromotionColumn extends DatabaseColumn {
	public static final String TAG=PromotionColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"promotion";
	
	public static final String img					=	"img";
	public static final String detail_img 			=	"detail_img";
	public static final String business_id			=	"business_id";
	public static final String filter_item_id		=	"filter_item_id";
	public static final String phone_id				=	"phone_id";
	public static final String name					=	"name";

	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table promotion( _id integer primary key autoincrement, img integer REFERENCES resource(_id), detail_img integer REFERENCES resource(_id), business_id	 integer REFERENCES business(_id), filter_item_id integer REFERENCES item(_id), phone_id integer REFERENCES phone(_id), name text )";
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
