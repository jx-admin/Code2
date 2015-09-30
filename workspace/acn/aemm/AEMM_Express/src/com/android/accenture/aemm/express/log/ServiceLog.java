package com.android.accenture.aemm.express.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServiceLog {
	public static final String uri     = "//sdcard//ServiceLog.xml";
	public static final int    maxsize = 5*1024*1024;
	private static FileWriter writer   = null;
	private static long size           = 0;
	public static void writeLog(String line) {
		try {
			if (null == writer) {
				writer = new FileWriter(uri);
			}
			if (null!=line) {
				writer.append(line);
				writer.flush();
				size += line.length();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void clean() {
		
		if (size>maxsize) {
			File file = new File(uri);
			if(file.exists()) {
				file.delete();
				file  = null;
			}
			if (null!= writer) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				writer = null;
			}
			size = 0;
		}
	}
}
