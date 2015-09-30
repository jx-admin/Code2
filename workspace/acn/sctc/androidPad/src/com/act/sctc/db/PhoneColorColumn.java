package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class PhoneColorColumn extends DatabaseColumn {
	public static final String TAG=PhoneColorColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"phone_color";
	
	/**只能设置三种值:
	 */
	public static final String phone_id				=	"phone_id";
	
	public static final String value				=	"value";
	
	public static final String color				=	"color";
	
	public static final String comment				=	"comment";
	
	public static final String img1					=	"img1";
	
	public static final String img2					=	"img2";
	
	public static final String img3					=	"img3";
	
	public static final String img4					=	"img4";
	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() { return "create table phone_color( _id integer primary key autoincrement, phone_id integer REFERENCES phone(_id), color int not null, value text not null,comment text , img1 integer REFERENCES resource(_id), img2 integer REFERENCES resource(_id), img3 integer REFERENCES resource(_id), img4 integer REFERENCES resource(_id) )";
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
