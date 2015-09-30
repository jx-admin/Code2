package com.android.accenture.aemm.express.log;

public class LoggerFactory {
	@SuppressWarnings("unchecked")
	public static Logger getLogger(Class cla) {
		return new Logger(cla);
	}
	@SuppressWarnings("unchecked")
	public static Logger getLogger(Class cla,String logFile) {
		return new Logger(cla,logFile);
	}
}
