package com.apk.infos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.xml.sax.SAXException;

import test.AXMLPrinter2;

public class GetInfo {

	/** 全部APK包的信息 */
	private static ArrayList<ArrayList<String>> listAPKInfo = null;
	/** 每个APK包的信息 */
	private static ArrayList<String> ApkInfo = null;
	/** 解压缩 */
	private static ZipFile zFile;
//	/** 格式化之前地址 */
//	private final static String strBeforeFilePath = "d:/AndroidManifest.xml";
//	/** 格式化之后地址 */
//	private final static String strAfterFilePath = "d:/cmdAfter.xml";
//	/** 批处理命令 */
//	private static String strcmd = "cmd /c resolveManifest.bat";
	/** 统计获取失败的数量 */
	private static int errCount;
	/** APK包名 */
	public static String strpackage = "";
	/** APK版本 */
	public static String strversion = "";
	/** APK版本号 */
	public static String strVersionCode = "";
	
	public static InputStream stream;
	
	static ArrayList<String> list = new ArrayList<String>();

	public GetInfo() {

	}

	public static ArrayList<ArrayList<String>> GetApkInfoAll(String strpath) throws IOException {
		errCount = 0;
		list = getFiles(strpath);
		listAPKInfo = new ArrayList<ArrayList<String>>();
		if (list != null) {
			int listCount = list.size();
			for (int i = 0; i < listCount; i++) {
				resolve(getApkInfo(list.get(i)), list.get(i));
				stream.close();
			}
			list.clear();
			list = null;
//			File file01 = new File(strBeforeFilePath);
//			if (file01.exists()) {
//				file01.delete();
//			}
//			File file02 = new File(strAfterFilePath);
//			if (file02.exists()) {
//				file02.delete();
//			}
			IndexShowUI.strResult = "共有APK文件：" + listCount + "\n获取成功："
					+ listAPKInfo.size() + "个\n获取失败：" + errCount + "个";
		} else {
			IndexShowUI.strResult = "目标文件夹在没有APK文件";
		}
		return listAPKInfo;
	}

	/**
	 * 得到压缩包信息
	 * 
	 * @param apkpath
	 */
	private static InputStream getApkInfo(String apkpath) {
		try {
			zFile = new ZipFile(apkpath);
			ZipEntry entry = zFile.getEntry("AndroidManifest.xml");
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			stream = zFile.getInputStream(entry);
			return stream;
		} catch (IOException e) {
			errCount++;
			System.out.println("IOEXCEPTIONss");
		}
		return null;
	}

	/**
	 * 解析XML文件，获取APK包名、版本
	 */
	private static void resolve(InputStream stream, String apkpath) {
		try {
			ApkInfo = new ArrayList<String>();
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			parserFactory.setValidating(false);
			parserFactory.setNamespaceAware(false);
			MyXmlResolve MySaxParserInstance = new MyXmlResolve();
			
			String strContent = AXMLPrinter2.parse(stream);
			byte[] by = strContent.getBytes("utf-8");
			ByteArrayInputStream bais = new ByteArrayInputStream(by);
			SAXParser parser = parserFactory.newSAXParser();
			parser.parse(bais, MySaxParserInstance);
			bais.close();
			parser = null;
			ApkInfo.add(apkpath.substring(apkpath.lastIndexOf("\\") + 1));
			ApkInfo.add(strpackage);
			ApkInfo.add(strversion);
			ApkInfo.add(strVersionCode);
			listAPKInfo.add(ApkInfo);
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException");
			errCount++;
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			errCount++;
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
			errCount++;
		} catch (SAXException e) {
			System.out.println("SAXException");
			errCount++;
		} catch (IOException e) {
			System.out.println("IOEXCEPTION");
			errCount++;
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取文件夹下的文件名
	 */
	private static ArrayList<String> getFiles(String path) {
		ArrayList<String> listAPKFiles = null;
		File files = null;
		try {
			files = new File(path);
			String[] strFiles = files.list(new FilterAPKGetWithCMD());
			if (strFiles.length > 0) {
				listAPKFiles = new ArrayList<String>();
				for (int i = 0; i < strFiles.length; i++) {
					String strFile = strFiles[i];
					if (strFile.length() > 0) {
						listAPKFiles.add(path + strFile);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		files = null;
		return listAPKFiles;
	}
}

/**
 * 过滤文件后缀
 * 
 * @author Administrator
 * 
 */
class FilterAPKGetWithCMD implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		boolean isapk = false;
		if (name.indexOf(".apk") != -1) {
			isapk = true;
		}
		return isapk;
	}
}
