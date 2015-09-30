package com.aess.aemm.networkutils;

import com.aess.aemm.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	public static final int CONNECT_TIMEOUT = 1000*60*2;
	public static final int READ_TIMEOUT = 1000*60*3;
	public final static String PROFIX = "https";
	
	public final static int E_USER_ALREADY_EXIST = 1103;
	public final static int E_INVALID_USERNAME_PASSWORD = 1100;
	public final static int E_USER_DOESNOT_EXIST = 1101;
	public final static int E_DEVICE_DOESNOT_EXIST = 9101;
	public final static int E_DEVICE_ISREGISTEED = 2414;
	public final static int E_CANCEL_ACTIVE = 2417;
	public final static int E_SERVER_LIMIT = 3035;

	
	public final static int F_UNKNOW = 10000;
	public final static int F_LOGIN_ADDRESS_ERROR = 10001;
	public final static int SERVICE_ERROR = 10002;
	public final static int LOGIN_ERROR_IMSI_WRONG = 10003;
	public final static int LOGIN_ERROR_TRFFIC_LIMIT = 10004;
	public final static int ERROR_DOMAIN = -10005;

	
	public static String getErrorString(Context cxt, int code) {
		String info = null;
		if (code == NetUtils.E_USER_ALREADY_EXIST) {
			info = cxt.getString(R.string.login_error_user_already_exist);
		} else if (code == NetUtils.E_INVALID_USERNAME_PASSWORD) {
			info = cxt.getString(R.string.login_error_invalid_username_password);
		} else if (code == NetUtils.E_USER_DOESNOT_EXIST) {
			info = cxt.getString(R.string.login_error_user_doesnot_exist);
		} else if (code == NetUtils.E_DEVICE_DOESNOT_EXIST) {
			info = cxt.getString(R.string.login_error_device_doesnot_exist);
		} else if (code == NetUtils.E_CANCEL_ACTIVE) {
			info = cxt.getString(R.string.login_error_cancel_active);
		} else if (code == NetUtils.E_DEVICE_ISREGISTEED) {
			info = cxt.getString(R.string.login_error_device_isregistered);
		} else if (code == NetUtils.ERROR_DOMAIN) {
			info = cxt.getString(R.string.domainerr);
		} else if (code == NetUtils.LOGIN_ERROR_IMSI_WRONG) {
			info = cxt.getString(R.string.inputimsi_hint);
		} else if (code == NetUtils.LOGIN_ERROR_TRFFIC_LIMIT) {
			info = cxt.getString(R.string.login_error_limit);
		} else if (code == NetUtils.E_SERVER_LIMIT) {
			info = cxt.getString(R.string.server_limit_hint);
		} else if (code == NetUtils.F_LOGIN_ADDRESS_ERROR) {
			info = cxt.getString(R.string.server_address_error);
		} else if (code == NetUtils.SERVICE_ERROR) {
			info = cxt.getString(R.string.service_fail);
		} else {
			info = cxt.getString(R.string.login_activity_loginfail_text_both);
		}
		return info;
	}
	
	
	public static boolean isNetOK(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info == null){
			return false;
		}
		return info.isConnected();
	}
	
	public static boolean isHttps(String uri) {
		if (null == uri) {
			return false;
		}
		String temp = new String(uri);
		temp.toLowerCase();
		return temp.startsWith(PROFIX);
	}
}
