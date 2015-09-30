package com.baidu.lbsapi.album.util.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * @author wangziji
 * @version V1.0
 * @Description 内存缓存类，为室内景相册提内存缓存，对外封装对室内景相册的添加，读取等方法
 * @date 13-12-13 下午5:35
 */
public class ImageMemoryCache {

    private static ImageMemoryCache imageMemoryCache;
    /*缓存大小60k*/
    static final int CACHE_SIZE = 60 * 1024;
    /*android提供的LRU缓存实例*/
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * 构造函数
     */
    private ImageMemoryCache() {
        mMemoryCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number of items.
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    /**
     * 对外提供单实例接口
     *
     * @return 文件缓存单实例
     */
    public static ImageMemoryCache getInstance() {
        if (imageMemoryCache == null) {
            imageMemoryCache = new ImageMemoryCache();
        }
        return imageMemoryCache;
    }

    /**
     * 向缓存中添加位图实例
     *
     * @param url 该位图唯一url标识
     * @param bitmap 该位图实例
     */
    public void put(String url, Bitmap bitmap) {
        if (url != null) {
            mMemoryCache.put(url, bitmap.copy(Bitmap.Config.ARGB_4444, false));
        }
    }

    /**
     * 从缓存中读取位图
     *
     * @param url 所要被读取的位图url
     * @return 位图实例
     */
    public Bitmap get(String url) {
        return mMemoryCache.get(url);
    }

    /**
     * 获得缓存大小
     *
     * @return 缓存大小，单位kb
     */
    public int getSize() {
        return mMemoryCache.size();
    }

    /**
     * 清除所有内存缓存
     */
    public void clear() {
        mMemoryCache.evictAll();
    }

}
