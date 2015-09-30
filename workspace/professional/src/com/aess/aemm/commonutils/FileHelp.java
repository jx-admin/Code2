package com.aess.aemm.commonutils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.content.Context;
import android.os.Environment;

public class FileHelp {
	public final static int BUFSIZE = 1024;
	public final static String CHAR_CODING = "utf-8";
	public final static String AEMMDIR = "/_AEMM_OUT/";

	
	public static int fileSaveInSDCard(Context cxt, String filename, byte[] databuf) {
		return FileSave(cxt, filename, databuf, true, true);
	}
	
	@SuppressWarnings("unused")
	private static FileOutputStream osFormSDCardFile(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		return new FileOutputStream(file);
	}
	
	public static FileInputStream getFileInputStream(String filePath) throws FileNotFoundException{
		if (null == filePath) {
			return null;
		}
		File file = new File(filePath);
		return new FileInputStream(file);
	}
	
	public final static String TIMEFORMAT = "yyyyMMdd_HHmmss_";
	public static String getPathInSDCard(String fileName, boolean timeMark) {
		if (null == fileName) {
			fileName = "_";
		}
		
		String path = getSDCardPath();

		if (null != path) {
			String mark = null;
			
			if (timeMark) {
				mark = CommUtils.getTimeString(TIMEFORMAT);
			}
			
			if (timeMark && null != mark) {
				path = path + mark + fileName;
			} else {
				path = path + mark + fileName;
			}
		}
		return path;
	}
	
	public static int fileDelete(File file) {
		if (null != file && file.exists()) {
			file.delete();
		}
		return 1;
	}
	
	public static int fileMakeDir(File file) {
		if (null != file) {
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
		}
		return 1;
	}
	
	@SuppressWarnings("unused")
	private static int fileCopyToSDCard(Context cxt, String fromPath, String toName) {
		if (null == cxt || null == fromPath || null == toName) {
			return -1;
		}
		
		File formfile = new File(fromPath);
		if (!formfile.exists() || !formfile.canRead()) {
			return -2;
		}

		String toPath = getPathInSDCard(toName, true);
		if (null == toPath) {
			return -2;
		}
		
		File toFile = new File(toPath);
		fileMakeDir(toFile);
		fileDelete(toFile);

		int ret = -1;
		try {
			FileInputStream fis = new FileInputStream(formfile);
			FileOutputStream fos = new FileOutputStream(toFile);
			ret = saveToFile(fis, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	private static String getSDCardPath() {
		String path = null;
		String state = Environment.getExternalStorageState();  
		if (Environment.MEDIA_MOUNTED.equals(state)) {  
			 if (Environment.getExternalStorageDirectory().canWrite()) { 
				 path = Environment.getExternalStorageDirectory().getPath() + AEMMDIR;
			 }
		}
		return path;
	}
	
	private static int FileSave(Context cxt, String fileName, byte[] databuf, boolean inSDCard, boolean timeMark) {
		if (null == cxt || null == fileName || null == databuf) {
			return -1;
		}
		
		String path = getSDCardPath();
		if (null == path) {
			return -1;
		}
		
		String toPath = getPathInSDCard(fileName, true);
		if (null == toPath) {
			return -2;
		}
		
		File toFile = new File(toPath);
		fileMakeDir(toFile);
		fileDelete(toFile);
				
		try {
			FileOutputStream fos = new FileOutputStream(toFile);
			bufLengthWrite(fos, databuf.length);
			fos.write(databuf);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public final static String LINE = "####################\n";
	static private void bufLengthWrite(FileOutputStream fos, int length) throws Exception {
		fos.write(LINE.getBytes());
		String info = "Post Info length: " + length + "\n";
		fos.write(info.getBytes(CHAR_CODING));
		fos.write(LINE.getBytes());
	}
	
	private static int saveToFile(FileInputStream is, FileOutputStream os) throws Exception {
		if (null == is || null == os) {
			return -1;
		}
		byte buf[] = new byte[BUFSIZE];
		int len = 0;

		while ((len = is.read(buf)) > 0 ) {
			os.write(buf, 0, len);
		}
		is.close();
		os.close();
		return 1;
	}
}
