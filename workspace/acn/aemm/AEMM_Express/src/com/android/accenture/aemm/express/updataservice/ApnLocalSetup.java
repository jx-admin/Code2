package com.android.accenture.aemm.express.updataservice;

import com.android.internal.telephony.TelephonyProperties;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemProperties;

public class ApnLocalSetup {
	public static final Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
	//colunms
	public static final String _ID = "_id";
	public static final String APN_NAME = "name";
	public static final String APN_NUMERIC = "numeric";
	public static final String APN_MCC = "mcc";
	public static final String APN_MNC = "mnc";
	public static final String APN_APN = "apn";
	
	public static final String APN_USER = "user";
	public static final String APN_PASS = "password";
	public static final String APN_PROXY = "proxy";
	public static final String APN_PROXYPORT = "port";
	
	public static final String APN_TYPE = "type";
	public static final String APN_CURRENT = "current";
	
	
	String mcc = null;
	String mnc = null;
	String numeric;
	
	public ApnLocalSetup(Context context)
	{
		mContext = context;
		setMccandMnc();
	}
	private int setMccandMnc()
	{
		//hide
		//need to use source code
		numeric =
            SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC);
        // MCC is first 3 chars and then in 2 - 3 chars of MNC
        if (numeric != null && numeric.length() > 4) {
            // Country code
             mcc = numeric.substring(0, 3);
            // Network code
             mnc = numeric.substring(3);
             return 0;
        }
        else
        {
        	return -1;
        }
	}
	Context mContext;
	
	public  ContentValues toContentValues(String apnName,
										  String apnApn,
										  String user,
			                              String apnPassword,
			                              String proxy,
			                              String proxyPort)
	{
		if (mcc == null || mnc == null)
			return null;
		 ContentValues values = new ContentValues();
		
		 values.put(APN_NAME, apnName);
		 values.put(APN_APN, apnApn);
		 values.put(APN_USER, user);
		 values.put(APN_PASS, apnPassword);
		 values.put(APN_NUMERIC, numeric);
		 values.put(APN_MCC, mcc);
		 values.put(APN_MNC, mnc);
		 values.put(APN_PROXY, proxy);
		 values.put(APN_PROXYPORT, proxyPort);
		// values.put(APN_TYPE, profile.getApnType());
		 values.put(APN_CURRENT, 1); //visiable in Setting UI
		 return values;
	}
	public int AddApnSetting(ContentValues values)
	{
		int rowId = 0;
		Uri newRow = mContext.getContentResolver().insert(
				 CONTENT_URI, values);
		String sId = newRow.getLastPathSegment();
		rowId = Integer.valueOf(sId);
		return rowId;
	}
	
	public static int deleteApnSetting(Context context, int rowId)
	{
		Uri u = ContentUris.withAppendedId(CONTENT_URI,rowId);
		int num = context.getContentResolver().delete(u,null,null);
		return num;
	}
	
	public int queryApnSetting(int rowId)
	{
		Uri u = ContentUris.withAppendedId(CONTENT_URI,rowId);
		Cursor cursor = mContext.getContentResolver().query(u,null,null,null,null);
		
		return 0;
	}
	
	
	public int updateApnSetting(int rowId,ContentValues values)
	{
		Uri u = ContentUris.withAppendedId(CONTENT_URI,rowId);
		mContext.getContentResolver().update(u,values,null,null);
		return 0;
	}


}
