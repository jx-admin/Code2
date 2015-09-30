package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class GoodsCartColumn extends DatabaseColumn {
	public static final String TAG=GoodsCartColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"goods_cart";
	
	/**只能设置三种值:
	 */
	public static final String user_id				=	"user_id";
	
	public static final String product_id			=	"product_id";
	
	public static final String product_type			=	"product_type";
	
	public static final String count				=	"count";
	
	public static final String mark					=	"mark";
	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table goods_cart( _id integer primary key autoincrement, user_id integer, product_id integer, product_type interger, count int, mark text )";
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
