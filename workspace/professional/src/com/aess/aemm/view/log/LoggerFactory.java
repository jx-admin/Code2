package com.aess.aemm.view.log;

public class LoggerFactory {
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class cla) {
		return new Logger(cla);
	}
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class cla,String logFile) {
		return new Logger(cla,logFile);
	}
	public static Logger getLogger(String logcat){
		return new Logger(logcat);
	}
}
