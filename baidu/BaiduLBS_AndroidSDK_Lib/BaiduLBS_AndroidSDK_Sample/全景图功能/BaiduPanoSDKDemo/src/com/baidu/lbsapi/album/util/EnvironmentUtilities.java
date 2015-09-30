package com.baidu.lbsapi.album.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import com.baidu.lbsapi.utils.StorageOptions;

import java.io.File;
import java.io.IOException;

/**
 * 系统环境工具 得到SD卡,屏幕等手机环境信息
 * 
 * @author fuliqiang
 * 
 */
public class EnvironmentUtilities {

    private static final double MIN_FREE_SPACE = 10;

    public static final String SYSTEM_SEPARATOR = System.getProperty("file.separator");
    
    static final String APP_SDCARD_FOLDER_NAME = "BaiduPanoSDK";
    
    static final String APP_PKG_NAME = "com.baidu.BaiduPanoSDK";
    
    static final String DEFAULT_SD_PATH = "/sdcard/";
    
    //缓存大小设置定义
    static final int MAP_MAX_SDCARD = 20 * 1024 * 1024;
    static final int DOM_MAX_SDCARD = 50 * 1024 * 1024;
    static final int ITS_MAX_SDCARD = 5 * 1024 * 1024;
    static final int MAP_MAX = 10 * 1024 * 1024;
    static final int DOM_MAX = 10 * 1024 * 1024;
    static final int ITS_MAX = 5 * 1024 * 1024;
    
    static String sdcardPath; //SD卡路径
    //应用目录及缓存目录
    static String outputDirectory;  // 应用程序目录
    static String outputCache;      // 缓存目录
    static String outputSecondCache;// 次要缓存目录
    //缓存大小
    static int mapTmpStgMax;
    static int domTmpStgMax;
    static int itsTmpStgMax;
    
    private static String strAppInROM;
    
    static String[] paths;
    static String[] labels;
    static String[] sizes;
    
    /**
     * 初始化应用目录及缓存
     */
    public static void initAppDirectory(Context ctx) {
        outputDirectory = ctx.getFilesDir().getAbsolutePath();
        initSDCardList(ctx);
        // 获取缓存路径
        if (StorageCheck.getSdcardState()== StorageCheck.SDCARD_NORMAL) {
            outputCache = getSDCardCachePath();
            outputSecondCache = ctx.getCacheDir().getAbsolutePath();
            mapTmpStgMax = MAP_MAX_SDCARD;
            domTmpStgMax = DOM_MAX_SDCARD;
            itsTmpStgMax = ITS_MAX_SDCARD;
        } else {
            outputCache = ctx.getCacheDir().getAbsolutePath();
            outputSecondCache = "";
            mapTmpStgMax = MAP_MAX;
            domTmpStgMax = DOM_MAX;
            itsTmpStgMax = ITS_MAX;
        }       
        
        judgeAppInRom(ctx);
    }
    
    /**
     * 标准版方案：启动时检测手机上的存储设备，如果只有内置存储就使用内置存储。
                   如果同时存在两个存储器，检测哪个上面有地图数据，就使用哪个。如果都有地图数据就让用户选择并重启后生效。
                   如果都没有地图数据默认使用内置存储设备
     */
    public static final int SDCARD_MODE_STANDARD = 1;
    /**
     * 优先使用外置卡：启动软件时检测是否有外置卡，如果有就直接使用外置卡，没有就使用内置卡。不检测数据，不让用户选择
     */
    public static final int SDCARD_MODE_PRIORITY_EXT = 2;
    /**
     * 使用android默认接口
     */
    public static final int SDCARD_MODE_ANDROID_INTERFACE = 3;

    /**
     * 初始化sd卡列表,包括内置和外置.
     */
    private static void initSDCardList(Context ctx) {
        StorageOptions.determineStorageOptions(ctx);
        paths = StorageOptions.paths;
        labels = StorageOptions.labels;
        sizes = StorageOptions.sizes;

        if (1 == SDCARD_MODE_STANDARD) {
            // 配置的路径可读写时,默认使用配置路径.
            SharedPreferences sp = ctx.getSharedPreferences("map_pref", Context.MODE_PRIVATE);
            if (sp.contains("selected_sdcard")) {
                sdcardPath = sp.getString("selected_sdcard", DEFAULT_SD_PATH);
                if (EnvironmentUtilities.writeTestFileToSdcard(sdcardPath)) {
                    return;
                }
            }
        }
        try {
            // 如果sd卡列表非空, 则默认选第一个, 内置sd卡.
            if (paths != null && paths.length > 0) {
                if (1 == SDCARD_MODE_STANDARD
                        || paths.length == 1) {
                    sdcardPath = paths[0];
                } else if (1 == SDCARD_MODE_PRIORITY_EXT
                        && paths.length > 1) {
                    sdcardPath = paths[1];
                } else {
                    sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
            } else {
                sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        } catch (Exception e) {
            sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        if (sdcardPath == null || sdcardPath.trim().length() < 1) {
            sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    /**
     * 保存使用的sd卡路径.
     * @param path 为空时,删除保存的路径.
     */
    public static void saveSDCardSelection(Context ctx,String path) {
        SharedPreferences sp = ctx.getSharedPreferences("map_pref", Context.MODE_PRIVATE);

        Editor editor = sp.edit();
        if (path == null || path.length() == 0) {
            editor.remove("selected_sdcard");
        } else {
            editor.putString("selected_sdcard", path);
        }
        editor.commit();
    }
    
    /**
     * 获取sd卡的路径
     * @return
     */
    public static String getSDCardPath() {
        return sdcardPath;
    }

    
    /**
     * 获取SDCard目录
     */
    public static String getAppSDCardPath() {
        String strPath = EnvironmentUtilities.sdcardPath;
        strPath += "/"+APP_SDCARD_FOLDER_NAME;

        if (strPath.length() != 0) {
            File fout = new File(strPath);
            if (!fout.exists()) {
                fout.mkdirs();
            }
        }

        return strPath;
    }
    /**
     * 获取相册SDCard目录
     */
    public static String getAlbumCachePath() {
        String strPath = EnvironmentUtilities.sdcardPath;
        strPath += "/"+APP_SDCARD_FOLDER_NAME + "/album";

        if (strPath.length() != 0) {
            File fout = new File(strPath);
            if (!fout.exists()) {
                fout.mkdirs();
            }
        }

        return strPath;
    } 
    
    
    /**
     * 获取SDCard cache目录
     */
    private static String getSDCardCachePath() {
        String strPath = "";
        if (EnvironmentUtilities.externalStorageEnable()) {
            strPath = EnvironmentUtilities.getExternalStoragePath();
            strPath += "/"+APP_SDCARD_FOLDER_NAME+"/cache";
        }
        if (strPath.length() != 0) {
            File fout = new File(strPath);
            if (!fout.exists()) {
                fout.mkdirs();
            }
        }

        return strPath;
    }

    public static String getAppCachePath() {
        return outputCache;
    }

    public static String getAppSecondCachePath() {
        return outputSecondCache;
    }

    public static int getMapTmpStgMax() {
        return mapTmpStgMax;
    }

    public static int getDomTmpStgMax() {
        return domTmpStgMax;
    }

    public static int getItsTmpStgMax() {
        return itsTmpStgMax;
    }
    
    /*
     * 方法说明 ：判断应用是否刷在ROM中 ：
     *
     * @param context 返回值 ：void,strAppInROM = "1"表示应用是刷在ROM中，strAppInROM = "0"则是安装在手机上
     * strAppInROM = ""表示不进行该统计
     */
    private static void judgeAppInRom(Context context) {
        boolean bCount = false;
        if(bCount){
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = pm.getApplicationInfo(APP_PKG_NAME, 0);
                // 如果applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM 的值不等于0.
                // 则该应用是刷在ROM中
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    strAppInROM = "1";
                } else {
                    strAppInROM = "0";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
                    strAppInROM = "";
        }

    }


    public static String getStrAppInROM() {
        return strAppInROM;
    }

    

    /**
     * 判断推送服务是否可用
     * @return
     */
    public static boolean isPushServiceEnable(Context context) {
        boolean flag = false;
        flag = false;
        return flag;
    }

    /**
     * SD卡剩余空间的百分比
     * 
     * @return
     */
    public static double freePercentage() {
        StatFs stat = new StatFs(EnvironmentUtilities.getExternalStoragePath());
        long availableBlocks = stat.getAvailableBlocks();
        long totalBlocks = stat.getBlockCount();
        return (double) availableBlocks / (double) totalBlocks * 100;
    }

    // /**
    // * 判断SD卡访问状态,是否已经映射并且有足够的剩余空间
    // *
    // * @return
    // */
    // public static String assertSdCard() {
    // if (!android.os.Environment.getExternalStorageState().equals(
    // android.os.Environment.MEDIA_MOUNTED)) {
    // return "Your SD card is not mounted. You'll need it for caching thumbs.";
    // }
    // return null;
    // }

    public static String checkFreeSpaceSdCard() {

        if (freePercentage() < MIN_FREE_SPACE) {
            return "You need to have more than " + MIN_FREE_SPACE
                    + "% of free space on your SD card.";
        }
        return null;
    }

    /**
     * 得到SD卡全部存储空间
     * 
     * @return
     */
    public static long totalSpace() {
        StatFs stat = new StatFs(EnvironmentUtilities.getExternalStoragePath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 检查SD卡剩余空间
     * 
     * @return
     */
    public static long freeSpace() {
        StatFs stat = new StatFs(EnvironmentUtilities.getExternalStoragePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 得到存储设备</br>
     * 如果不强制使用外置存储设备，直接使用标准接口获取</br>
     * 如果强制使用外置存储设备，则使用配置文件读取的路径
     * @return
     */
    public static File getExternalStorageFile() {
        return new File(EnvironmentUtilities.sdcardPath);
    }

    public static String getExternalStorageState() {
        return Environment.getExternalStorageState();
    }

    public static String getExternalStoragePath() {
        String path = null;
        try {
            path = getExternalStorageFile().getAbsolutePath();
        } catch (Exception e) {
            path = null;
        }
        return path;
    }

    public static String getDownloadCachePath() {
        return EnvironmentUtilities.getExternalStoragePath();
    }

    public static boolean externalStorageEnable() {
        boolean flag = false;
        if(StorageCheck.getSdcardState()!= StorageCheck.SDCARD_NORMAL)
            return flag;
        flag = writeTestFileToSdcard(getExternalStoragePath());
        return flag;
    }

    public static boolean writeTestFileToSdcard(String path) {
        boolean flag = false;
        try {
            File testFile = new File(path + "/test.0");
            if (testFile.exists()) {
                testFile.delete();
            }
            flag = testFile.createNewFile();
            if (testFile.exists()) {
                testFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 得到软件版本名称
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 得到软件版本号
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 得到当前系统的API level
     * 
     * @return
     */
    public static int getSystemVersion() {
        return Integer.parseInt(Build.VERSION.SDK);
    }

    /** 
     * 判断是否模拟器。如果返回TRUE，则当前是模拟器
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei == null || imei.equals("000000000000000")) {
            return true;
        }
        return false;
    }

    /**
     * 判断GPS硬件是否开启
     * @param context
     * @return
     */
    public static boolean getGPSStatus(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断手机是否处于打电话状态
     * @param context
     * @return
     */
    public static boolean isCalling(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonymanager.getCallState()) {
            case TelephonyManager.CALL_STATE_OFFHOOK:
            case TelephonyManager.CALL_STATE_RINGING:
                return true;
            default: {
                return false;
            }
        }
    }
    
    
}