package com.aess.aemm.commonutils.tmp;
//package com.aess.aemm.commonutils;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class ServiceLog {
//	public static final String uri     = "//sdcard//ServiceLog.xml";
//	private static final String headerline = "encoding=\"utf-8\"";
//	private static final String suffix = "\r\n\r\n";
//	public static final int    maxsize = 5*1024*1024;
//	private static FileWriter writer   = null;
//	private static long size           = 0;
//	public static void writeLog(String line) {
//		try {
//			if (null == writer) {
//				writer = new FileWriter(uri);
//			}
//			if (null!=line) {
//				if(line.indexOf(headerline)>=0)
//				{
//					writer.append(suffix);
//				}
//				writer.append(line);
//				writer.flush();
//				size += line.length();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void clean() {
//		
//		if (size>maxsize) {
//			File file = new File(uri);
//			if(file.exists()) {
//				file.delete();
//				file  = null;
//			}
//			if (null!= writer) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				writer = null;
//			}
//			size = 0;
//		}
//	}
//}
