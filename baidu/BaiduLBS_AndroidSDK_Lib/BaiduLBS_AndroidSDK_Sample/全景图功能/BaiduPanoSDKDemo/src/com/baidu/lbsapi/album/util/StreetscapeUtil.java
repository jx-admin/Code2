package com.baidu.lbsapi.album.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author yangzhaohui
 * @version V1.0
 * @Description: 街景工具类，提供String转换为Int和Double,Url解析等功能
 * @date 2013-9-26 下午7:28:42
 */
public class StreetscapeUtil {

    private static StreetscapeUtil sInstance;
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    static {
        sInstance = new StreetscapeUtil();
    }

    private StreetscapeUtil() {

    }

    public static StreetscapeUtil getInstance() {
        return sInstance;
    }

     /**
     * 将String转换为int
     *
     * @param str          输入的String
     * @param defaultValue 默认值
     * @return 转换后的int
     */
    public int getIntFromString(String str, int defaultValue) {
        int ret = defaultValue;
        try {
            ret = Integer.valueOf(str);
        } catch (Exception e) {
            ret = defaultValue;
        }
        return ret;
    }

    public static boolean isChinese(char c) throws UnsupportedEncodingException {
        return String.valueOf(c).getBytes("UTF-8").length == 3;

    }

    /**
     * 将String转换为double
     *
     * @param str          输入的String
     * @param defaultValue 默认值
     * @return 转换后的double
     */
    public double getDoubleFromString(String str, double defaultValue) {
        double ret = defaultValue;
        try {
            ret = Double.valueOf(str);
        } catch (Exception e) {
            ret = defaultValue;
        }
        return ret;
    }

    /**
     * 将Url的Query部分转换为HashMap的形式，以便读取
     *
     * @param url 输入的Url
     * @return 解析完毕的HashMap
     */
    public HashMap<String, String> parseUrlQueryPart(String url) {
        // 每个键值为一组
        HashMap<String, String> urlMap = new HashMap<String, String>();
        String[] arrSplit = url.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1 && !"".equals(arrSplitEqual[0]) && !"".equals(arrSplitEqual[1])) {
                // 正确解析
                urlMap.put(arrSplitEqual[0], arrSplitEqual[1]);
            }
        }

        return urlMap;
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (hasHttpConnectionBug()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap
     * @return size in bytes
     */
    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    @SuppressLint("NewApi")
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @SuppressLint("NewApi")
    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @SuppressLint("NewApi")
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * Get the memory class of this device (approx. per-app memory limit)
     *
     * @param context
     * @return
     */
    public static int getMemoryClass(Context context) {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    /**
     * Check if OS version has a http URLConnection bug. See here for more information:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     *
     * @return
     */
    public static boolean hasHttpConnectionBug() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if OS version has built-in external cache dir method.
     *
     * @return
     */
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if ActionBar is available.
     *
     * @return
     */
    public static boolean hasActionBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static int calculateBestDirForPoi(double poiX, double poiY, double centerX, double centerY){
        if(centerY-poiY==0){
            if(poiX>centerX){
                return 90;
            }else if(poiX<centerX){
                return 270;
            }else {
                return 0;
            }
        }
        int degree = (int)(Math.toDegrees(Math.atan(Math.abs(centerX-poiX)/Math.abs(centerY-poiY)))+0.5);
        if(poiX>=centerX){
            if(poiY>centerY){
                return degree;
            }else{
                return 180-degree;
            }
        }else {
            if(poiY>centerY){
                return 360-degree;
            }else{
                return 180+degree;
            }
        }
    }
    
	/**
	 * 从Assets中读取图片
	 */
	public static  Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}
	
    /**
     * WIFI是否连接
     * @param context
     * @return WIFI已连接 返回 true,否则 false
     */
    public static boolean isWifiConnected(Context context) {
        
        if(context == null)
            return false;
        boolean isWifiConnected = false;
        
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);        
        if(connectivityManager!=null) {
            try{
                NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            
                if (activeNetInfo != null) {
        
                    if (ConnectivityManager.TYPE_WIFI == activeNetInfo.getType() && activeNetInfo.isConnected()) {
                        isWifiConnected = true;
                    } else {
                        isWifiConnected = false;
                    }
                }
            }catch(Exception e) {
                
            }
        }
        
        return isWifiConnected;
    }

}
