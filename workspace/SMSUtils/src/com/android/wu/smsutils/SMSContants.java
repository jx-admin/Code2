package com.android.wu.smsutils;

import java.util.HashMap;

import com.android.wx.math.DateFormater;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Android Uris final utils calss
 */
public final class SMSContants {

	/**
	 * sms base uri' string
	 */
	public static final String SMS_URI_ALL = "content://sms/";
	/**
	 * sms inbox uri' string
	 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	/**
	 * sms sent uri' string
	 */
	public static final String SMS_URI_SEND = "content://sms/sent";
	/**
	 * SMs drafter box
	 */
	public static final String SMS_URI_DRAFT = "content://sms/draft";


	public static Cursor getCursorNews(Context context, Uri uri) {
//		long yestday = System.currentTimeMillis() - 1000 * 60 * 5;
//		Log.d("time", "yestday:" + yestday + " Str:" + DateFormater.format(yestday,DateFormater.defaultFomate));
		return context.getContentResolver().query(uri,
				Coloums.SMSLog.SELECTION,
				null, null,
				Coloums.SMSLog.DATE+" desc limit 3");
	}
	
}
