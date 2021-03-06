package j.wu.utils;


public class Logger {
	
	private static int logLevel = 6;
	private static final int E = 1;
	private static final int W = 2;
	private static final int I = 3;
	private static final int D = 4;
	private static final int V = 5;
	
	public static void e(String tag, String msg, Throwable tr) {
		if(logLevel > E) {
			Log.e(tag, msg, tr);
		}
		
	}
	
	public static void w(String tag, String msg) {
		if(logLevel > W) {
			Log.w(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if(logLevel > I) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if(logLevel > D) {
			Log.d(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if(logLevel > V) {
			Log.v(tag, msg);
		}
	}
	
	public static StringBuilder toString(int[]a){
		StringBuilder sb = new StringBuilder();
		for (int d : a) {
			sb.append(d).append(',');
		}
		return sb;
	}
}
