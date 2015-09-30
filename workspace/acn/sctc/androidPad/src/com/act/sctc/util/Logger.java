package com.act.sctc.util;

import android.util.Log;

public class Logger {
	public final static boolean DEBUG = false;
	private final static String tag = "SCTC_Android";

	public static void debug(String log) {
		if (DEBUG) {
			Log.d(tag, log);
		}
	}

	public static void info(String log) {
		if (DEBUG) {
			Log.i(tag, log);
		}
	}

	public static void warn(String log) {
		if (DEBUG) {
			Log.w(tag, log);
		}
	}

	public static void error(String log) {
		if (DEBUG) {
			Log.e(tag, log);
		}
	}
}
