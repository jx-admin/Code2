package com.android.wu.smsutils;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.provider.ContactsContract;

/**
 * @author junxu.wang
 * 
 */
public final class Coloums {
	/** 授权常量 */
	public static final String AUTHORITY = "com.android.text.contacts";
	/** 数据库名称常量,前提是目录必须存在 */
	// public static final String DBNAME = "/sdcard/simcontacts/contacts2.db";
	public static final String DBNAME = "contacts.db";
	/** 数据库版本常量 */
	public static final int VERSION = 3;

	private Coloums() {
	}

	/** 内部类 */
	public static final class SMSLog implements BaseColumns {
		/** System sms URI */
		public static Uri SMS_CONTENT_URI = Uri.parse("content://sms");
		// DB table columns name
		public static final String ADDRESS = "address";
		public static final String DATE = "date";
		public static final String READ = "read";
		public static final String STATUS = "status";
		public static final String TYPE = "type";
		public static final String BODY = "body";
		public static final String TELNAME = "telName";
		public static final String MESSAGE_TYPE_INBOX = "1";
		public static final String MESSAGE_TYPE_SENT = "2";
		public static final String MESSAGE_TYPE_WRITE = "3";
		/** 默认排序常量 */
		public static final String DEFAULT_SORT_ORDER = "date DESC";// 按时间排序

		/** column index */
		public static final byte I_ID = 0, I_ADDRESS = 1, I_DATE = 2,
				I_READ = 3, I_STATUS = 4, I_TYPE = 5, I_BODY = 6,
				I_TELNAME = 7;

		public static final String SELECTION[] = { _ID, ADDRESS, DATE, READ,
				STATUS, TYPE, BODY };
		public static final String SELECTION_BK[] = { _ID, ADDRESS, DATE, READ,
				STATUS, TYPE, BODY, TELNAME };
		// table name
		public static final String TBNAME = "sms";
		/** backup sms Uri */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TBNAME);
		/** sql command: create table */
		public static final String SQL_CREATETABLE = "create table " + TBNAME
				+ "(" + _ID + " INTEGER PRIMARY KEY," + ADDRESS + " text,"
				+ DATE + " text," + READ + " text," + STATUS + " text," + TYPE
				+ " text," + BODY + " text," + TELNAME + " text);";
		/** sql command: drop table */
		public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TBNAME;

		public static HashMap<String, String> getProjectionMap() {
			// 实例化查询列集合
			final HashMap<String, String> SMSProjectionMap = new HashMap<String, String>();
			// 添加查询列
			SMSProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
			SMSProjectionMap
					.put(Coloums.SMSLog.ADDRESS, Coloums.SMSLog.ADDRESS);
			SMSProjectionMap.put(Coloums.SMSLog.DATE, Coloums.SMSLog.DATE);
			SMSProjectionMap.put(Coloums.SMSLog.READ, Coloums.SMSLog.READ);
			SMSProjectionMap.put(Coloums.SMSLog.STATUS, Coloums.SMSLog.STATUS);
			SMSProjectionMap.put(Coloums.SMSLog.TYPE, Coloums.SMSLog.TYPE);
			SMSProjectionMap.put(Coloums.SMSLog.BODY, Coloums.SMSLog.BODY);
			SMSProjectionMap
					.put(Coloums.SMSLog.TELNAME, Coloums.SMSLog.TELNAME);
			return SMSProjectionMap;
		}

	}

}
