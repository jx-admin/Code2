package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class PhoneColumn extends DatabaseColumn {
	public static final String TAG=PhoneColumn.class.getSimpleName();

	/**表名*/
	public static final String TABLE_NAME			= 	"phone";
	
	public static final String ad_desc				=	"ad_desc";
	public static final String thumbnail 			=	"thumbnail";
	public static final String sale_icon			=	"sale_icon";
	public static final String price				=	"price";
	public static final String sale_price 			=	"sale_price";
	public static final String ad_desc1				=	"ad_desc1";
	public static final String ad_desc2				=	"ad_desc2";
	public static final String ad_desc3				=	"ad_desc3";
	public static final String ad_desc4				=	"ad_desc4";
	public static final String series				=	"series";
	public static final String brand				=	"brand";
	public static final String type					=	"type";
	public static final String start_time			=	"start_time";
	public static final String look_design			=	"look_design";
	public static final String os					=	"os";
	public static final String smartphone			=	"smartphone";
	public static final String cpu_core				=	"cpu_core";
	public static final String cpu_rate				=	"cpu_rate";
	public static final String keyborad				=	"keyborad";
	public static final String input				=	"input";
	public static final String op_sign				=	"op_sign";
	public static final String net_standard			=	"net_standard";
	public static final String net_rate				=	"net_rate";
	public static final String browser				=	"browser";
	public static final String device_mem			=	"device_mem";
	public static final String run_mem				=	"run_mem";
	public static final String card_mem				=	"card_mem";
	public static final String extend_mem			=	"extend_mem";
	public static final String screen_szie			=	"screen_szie";
	public static final String screen_color			=	"screen_color";
	public static final String screen_resolution	=	"screen_resolution";
	public static final String gravity				=	"gravity";
	public static final String touch				=	"touch";
	public static final String music				=	"music";
	public static final String video				=	"video";
	public static final String ebook				=	"ebook";
	public static final String camera				=	"camera";
	public static final String sensor				=	"sensor";
	public static final String video_maker			=	"video_maker";
	public static final String photo_mode			=	"photo_mode";
	public static final String continue_photo		=	"continue_photo";
	public static final String resolution_photo		=	"resolution_photo";
	public static final String camera_other			=	"camera_other";
	public static final String sub_camera			=	"sub_camera";
	public static final String auto_focus			=	"auto_focus";
	public static final String gps					=	"gps";
	public static final String wifi					=	"wifi";
	public static final String bluetooth			=	"bluetooth";
	public static final String office				=	"office";
	public static final String email				=	"email";
	public static final String calculator			=	"calculator";
	public static final String device_size			=	"device_size";
	public static final String device_quality		=	"device_quality";
	public static final String device_matierial		=	"device_matierial";
	public static final String battery_category		=	"battery_category";
	public static final String battery_capacity		=	"battery_capacity";
	public static final String speek_time			=	"speek_time";
	public static final String idle_time			=	"idle_time";
	public static final String headset				=	"headset";
	public static final String attr1				=	"attr1";
	public static final String attr2				=	"attr2";
	public static final String attr3				=	"attr3";
	public static final String attr4				=	"attr4";
	public static final String attr5				=	"attr5";
	public static final String attr6				=	"attr6";
	public static final String attr7				=	"attr7";
	public static final String attr8				=	"attr8";
	public static final String attr9				=	"attr9";
	public static final String attr10				=	"attr10";

	
	
	public static final Uri		CONTENT_URI			=	Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	
	public String getTableCreateor() {
		return "create table phone( _id integer primary key autoincrement, ad_desc text, thumbnail integer REFERENCES resource(_id), sale_icon boolean, price FLOAT, sale_price FLOAT, ad_desc1 text, ad_desc2 text, ad_desc3 text, ad_desc4 text, series text, brand text, type text, start_time INT8, look_design text, os text, smartphone text, cpu_core int, cpu_rate text, keyborad text, input text, op_sign text, net_standard text, net_rate text, browser text, device_mem text, run_mem text, card_mem text, extend_mem text, screen_szie text, screen_color text, screen_resolution text, gravity text, touch text, music text, video text, ebook text, camera text, sensor text, video_maker text, photo_mode text, continue_photo text, resolution_photo text, camera_other text, sub_camera text, auto_focus text, gps text, wifi text, bluetooth text, office text, email text, calculator text, device_size text, device_quality text, device_matierial text, battery_category text, battery_capacity text, speek_time text, idle_time text, headset text, attr1 text, attr2 text, attr3 text, attr4 text, attr5 text, attr6 text, attr7 text, attr8 text, attr9 text, attr10 text )	";
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
