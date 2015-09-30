package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class ResourceColumn extends DatabaseColumn {
	public static final String TAG=ResourceColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"resource";
	
	/**只能设置三种值:
	  	1: 是(主页上半部分可滚动的广告页)
		0: 否(即为下面三个栏目的图片)*/
	public static final String TYPE					=	"type";
	
	/**图片*/
	public static final int TYPE_IMG		 		=	2;
	
	/**视频*/
	public static final int TYPE_VIDEO				=	1;
	
	/**图片下载地址和id*/
	public static final String PATH					=	"path";
	
	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "CREATE TABLE resource( _id integer primary key autoincrement, path text not null, type int default 0 not null )";
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
