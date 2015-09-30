package com.mog.EncDemo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.util.Log;
/*
 * read and write file . 
 */
public class FileService 
{
    public static final String TAG = "FileService";
    
    public static void save(String fileName, String content) throws Exception 
    {
        // 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
        if (!fileName.endsWith(".mec") && !fileName.endsWith(".txt"))
        {
                return ; 
        }
        String strTem = EncUtil.getSDPath();
        if(EncUtil.isEmpty(strTem))
        {
        	Log.e(TAG, "no SDCard Found");
        	return ; 
        }
        //fileName = strTem + "/" + fileName ; 
        byte[] buf = fileName.getBytes("iso8859-1");
        Log.e(TAG, new String(buf,"utf-8"));
        fileName = new String(buf,"utf-8");
        Log.e(TAG, fileName);
        FileOutputStream fos = new FileOutputStream(fileName) ; 
        fos.write(content.getBytes());
        fos.close();
    }

    public static String read(String fileName) throws Exception 
    {
        // 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
       /* if (!fileName.endsWith(".txt")) 
        {
            fileName = fileName + ".txt";
        }
        String strTem = EncUtil.getSDPath();
        if(EncUtil.isEmpty(strTem))
        {
        	Log.e(TAG, "no SDCard Found");
        	return null; 
        }*/
        FileInputStream fis = new FileInputStream(fileName); 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        //将读取后的数据放置在内存中---ByteArrayOutputStream
        while ((len = fis.read(buf)) != -1) 
        {
            baos.write(buf, 0, len);
        }
        fis.close();
        baos.close();
        //返回内存中存储的数据
        return baos.toString();
    }
}