package com.accenture.mbank.util;

import android.util.Log;

/**
 * We use this class to print the log, by modifying the LogType when the final
 * release, you can log Close(code:LogType=HIDE_LOG), released a beta version
 * you can open the log(code:LogType=SHOW_LOG).
 * 
 * @author seekting.x.zhang
 */
public class LogManager {

	public static final boolean SHOW_LOG = true;

	public static final boolean HIDE_LOG = false;

	public static boolean LogType = SHOW_LOG;
	public static boolean MAXLOG = true;
	public static final String TAG = "MobileBanking";

	static int maxLogSize = 4000;

	public static void v(String msg) {
		/*
		 * print long string
		 */
		if (LogType) {
			if(MAXLOG){
				for (int i = 0; i <= msg.length() / maxLogSize; i++) {
					int start = i * maxLogSize;
					int end = (i + 1) * maxLogSize;
					end = end > msg.length() ? msg.length() : end;
					Log.v(TAG, msg.substring(start, end));
				}
			}else{
				Log.v(TAG, msg);
			}
		}
	}

	public static void d(String msg) {
		if (LogType) {
			if(MAXLOG){
				for (int i = 0; i <= msg.length() / maxLogSize; i++) {
					int start = i * maxLogSize;
					int end = (i + 1) * maxLogSize;
					end = end > msg.length() ? msg.length() : end;
					Log.d(TAG, msg.substring(start, end));
				}
			}else{
				Log.d(TAG, msg);
			}
		}
	}

	public static void i(String msg) {
		if (LogType) {
			if(MAXLOG){
				for (int i = 0; i <= msg.length() / maxLogSize; i++) {
					int start = i * maxLogSize;
					int end = (i + 1) * maxLogSize;
					end = end > msg.length() ? msg.length() : end;
					Log.i(TAG, msg.substring(start, end));
				}
			}else{
				Log.i(TAG, msg);
			}
		}
	}

	public static void w(String msg) {
		if (LogType) {
			if(MAXLOG){
				for (int i = 0; i <= msg.length() / maxLogSize; i++) {
					int start = i * maxLogSize;
					int end = (i + 1) * maxLogSize;
					end = end > msg.length() ? msg.length() : end;
					Log.w(TAG, msg.substring(start, end));
				}
			}else{
				Log.w(TAG, msg);
			}
		}
	}

	public static void e(String msg) {
		if (LogType) {
			if(MAXLOG){
				for (int i = 0; i <= msg.length() / maxLogSize; i++) {
					int start = i * maxLogSize;
					int end = (i + 1) * maxLogSize;
					end = end > msg.length() ? msg.length() : end;
					Log.e(TAG, msg.substring(start, end));
				}
			}else{
				Log.e(TAG, msg);
			}
		}
	}

}
