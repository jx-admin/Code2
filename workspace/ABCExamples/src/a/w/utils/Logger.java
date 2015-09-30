package a.w.utils;

import android.util.Log;

public class Logger {
	
	private static int log_level = 6;
	private static final int E = 1;
	private static final int W = 2;
	private static final int I = 3;
	private static final int D = 4;
	private static final int V = 5;
	
	public static void e(String tag, String msg, Throwable tr) {
		if(log_level > E) {
			Log.e(tag, msg, tr);
		}
		
	}
	
	public static void w(String tag, String msg) {
		if(log_level > W) {
			Log.w(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if(log_level > I) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if(log_level > D) {
			Log.d(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if(log_level > V) {
			Log.v(tag, msg);
		}
	}
}
