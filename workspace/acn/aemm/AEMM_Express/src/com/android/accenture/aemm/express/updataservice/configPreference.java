package com.android.accenture.aemm.express.updataservice;

import com.android.accenture.aemm.express.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class configPreference {
	
	public static final String ROOT_INDENTIFIER = "com.accenture.profile";
	public static final String VPN_INDENTIFIER = "com.accenture.vpn.profile";
	
	public static final String SESSIONID = "SessionId";
	public static final String PREF_NAME = "config";
	public static final String LASTUPDATE = "lastupdate";
	public static final String HALL_ENABLED = "hallEnable";
	public static final String IMEI = "imei";

	public static final String USERNAME = "UserName";
	public static final String PASSWORD = "Password";
	public static final String VERSION = "version";
	public static final String CHECKCYCLE = "checkcycle";
	public static final String PSINPUT = "psinput";
	
	public static String getSessionId(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(SESSIONID, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	
	public static String getLastUpdate(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(LASTUPDATE, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	
	
	public static String getCheckCycle(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(CHECKCYCLE, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	
	public static String getUser(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(USERNAME, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	
	public static String getPass(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(PASSWORD, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	
	public static String getImei(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(IMEI, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	
	public static String getVpnVersion(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(VPN_INDENTIFIER, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	public static String getKeyValue(Context context,String key)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(key, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	public static String getRootVersion(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(ROOT_INDENTIFIER, null);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	
	public static boolean getHallEnalbed(Context context)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getString(HALL_ENABLED, null);
		if (value != null && value.length() > 0)
			return Boolean.valueOf(value);
		else
			return true;
	}
	public static void putSessionId(Context context,String sessionId)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.putString(SESSIONID, sessionId);
		d.commit();
	}
	
	public static void putCheckCycle(Context context,String cycle)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.putString(CHECKCYCLE, cycle);
		d.commit();
	}
	
	public static void putLastUpdate(Context context,String time)
	{
		String value = null;
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.putString(LASTUPDATE, time);
		d.commit();
	}
	public static void putKeyValue(Context context ,String key ,String value)
	{
		
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.putString(key, value);
		d.commit();
	}
	public static void putHallEnabled(Context context ,boolean value)
	{
		
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.putString(HALL_ENABLED, String.valueOf(value));
		d.commit();
	}
	public static void putUser(Context context,User user){
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.putString(USERNAME, user.getUserName());
		d.putString(PASSWORD, user.getPassWord());
		d.commit();
	}
	
	public static void putImei(Context context,String imei){
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.putString(IMEI, imei);
		d.commit();
	}
	
	public static void delUser(Context context){
		SharedPreferences  pp = context.getSharedPreferences(PREF_NAME, 0);
		Editor d = pp.edit();
		d.remove(USERNAME);
		d.remove(PASSWORD);
		d.commit();
	}
	
	public static Boolean getPSInput(Context context) {
		Boolean value = false;
		SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
		value = pp.getBoolean(PSINPUT, false);
		return value;
	}

	public static void setPSInput(Context context, boolean value) {
		if (null != context ) {
			SharedPreferences pp = context.getSharedPreferences(PREF_NAME, 0);
			Editor d = pp.edit();
			d.putBoolean(PSINPUT, value);
			d.commit();
		}
	}
}
