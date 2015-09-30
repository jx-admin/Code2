package com.android.accenture.aemm.dome;

public class LoggerFactory {
	public static Logger getLogger(Class cla) {
		return new Logger(cla);
	}
}
