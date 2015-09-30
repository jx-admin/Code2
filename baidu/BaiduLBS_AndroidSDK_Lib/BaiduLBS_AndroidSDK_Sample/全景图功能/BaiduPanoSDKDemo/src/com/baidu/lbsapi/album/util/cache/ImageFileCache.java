package com.baidu.lbsapi.album.util.cache;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;

import com.baidu.lbsapi.album.util.DiskLruCache;
import com.baidu.lbsapi.album.util.EnvironmentUtilities;

public class ImageFileCache {

    /*文件缓存大小2M*/
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 2;
    private static ImageFileCache imageFileCache;
    /*磁盘缓存同步锁*/
    private final Object mDiskCacheLock = new Object();
    /*实现LRU算法的磁盘缓存实例*/
    private DiskLruCache mDiskLruCache;
    /*磁盘缓存初始化标识*/
    private boolean mDiskCacheStarting = true;

    /**
     * 构造函数，添加同步锁防止多线程异步操作
     */
    private ImageFileCache() {
        synchronized (mDiskCacheLock) {
            File cacheDir = new File(EnvironmentUtilities.getAlbumCachePath());
            mDiskLruCache = DiskLruCache.openCache(null, cacheDir, DISK_CACHE_SIZE);
            mDiskCacheStarting = false; // Finished initialization
            mDiskCacheLock.notifyAll(); // Wake any waiting threads
        }
    }

    /**
     * 对外提供单实例接口
     *
     * @return 文件缓存单实例
     */
    public static ImageFileCache getInstance() {
        if (imageFileCache == null) {
            imageFileCache = new ImageFileCache();
        }
        return imageFileCache;
    }

    /**
     * 添加位图到文件缓存
     *
     * @param key    该位图的唯一url标识
     * @param bitmap 该位图实例
     */
    public void addBitmapToCache(String key, Bitmap bitmap) {
        //锁定资源
        synchronized (mDiskCacheLock) {
            //将url转换成文件名
            String urlAfterDecode = getLocalFileName(key);
            //检查该文件名是否合法，并且不存在于已有缓存目录中
            if (mDiskLruCache != null && mDiskLruCache.get(urlAfterDecode) == null) {
                //写入缓存
                mDiskLruCache.put(urlAfterDecode, bitmap);
            }
        }
    }

    /**
     * 从文件缓存中获得位图实例
     *
     * @param key 该位图的唯一url标识
     * @return 该位图实例
     */
    public Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskLruCache != null) {
                //将url转换成文件名
                String urlAfterDecode = getLocalFileName(key);
                //读取缓存
                return mDiskLruCache.get(urlAfterDecode);
            }
        }
        return null;
    }

    /**
     * 生成文件名
     *
     * @param string 文件唯一标识
     * @return 文件名
     */
    protected String getLocalFileName(String string) {

        if (string == null) {
            return null;
        }

        String result = "";
        try {
            //利用MD5算法生成文件名
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] s = digest.digest(string.getBytes());
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException e) {
            // 如果未找到MD5算法，则取最后一个‘/’之后的串作为文件名称
            String[] array = string.split("/");
            if (array != null && array.length > 0) {
                result = array[array.length - 1];
            }
        }
        return result;
    }
}
