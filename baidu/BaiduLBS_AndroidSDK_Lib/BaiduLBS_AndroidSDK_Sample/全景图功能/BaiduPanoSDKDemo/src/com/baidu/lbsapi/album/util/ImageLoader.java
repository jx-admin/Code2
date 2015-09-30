package com.baidu.lbsapi.album.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.baidu.lbsapi.album.util.cache.ImageFileCache;
import com.baidu.lbsapi.album.util.cache.ImageMemoryCache;

/**
 * @Description TODO
 * @author wangziji
 * @date 13-12-12 上午10:59
 * @version V1.0
 */
public class ImageLoader {

    private static ExecutorService executor;
    private static ImageLoader imageLoader;
    private static BitmapDecoder bitmapDecoder;
    private Map<String, WeakReference<ImageLoaderCallback>> mLoadingList;
    private List<Future<?>> mTaskList;

    private ImageLoader() {

        mLoadingList = Collections.synchronizedMap(new HashMap<String, WeakReference<ImageLoaderCallback>>());
        mTaskList = new ArrayList<Future<?>>();
    }

    public static ImageLoader getInstance() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader();
        }
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            executor = Executors.newFixedThreadPool(3);
        }
        return imageLoader;
    }

    public void shutDownThreadPool() {
        mTaskList.clear();
        mLoadingList.clear();
        executor.shutdownNow();
        //Log.i("wang6","shut down");
    }

    public void cancelAllTask() {
        if (!mTaskList.isEmpty()) {
            for (int i = 0; i < mTaskList.size(); i++) {
                Future<?> f = mTaskList.get(i);
                if (!f.isCancelled() || !f.isDone()) {
                    f.cancel(true);
                }
            }
            mTaskList.clear();
            mLoadingList.clear();
        }
    }

    public void execute(String url, ImageLoaderCallback callback) {
        final Bitmap bitmap = ImageMemoryCache.getInstance().get(url);
        if (bitmap == null || bitmap.isRecycled()) {
            if (!mLoadingList.containsKey(url)) {
                // Log.i("Demo","execute");
                mLoadingList.put(url, new WeakReference<ImageLoaderCallback>(callback));
                mTaskList.add(executor.submit(new LoadingRunnable(url, callback)));
            }

        } else {
            // Log.i("Demo","load from memo");
            callback.bitmapReady(bitmap);
        }
    }

    public void setBitmapDecoder(int width, int height) {
        if (bitmapDecoder == null) {
            bitmapDecoder = new BitmapDecoder(width, height);
        }
    }

    public static interface ImageLoaderCallback {
        void bitmapReady(Bitmap bitmap);
    }

    private class LoadingRunnable implements Runnable {

        private String url;
        private ImageLoaderCallback callback;

        private LoadingRunnable(String url, ImageLoaderCallback callback) {
            this.url = url;
            this.callback = callback;
        }

        @Override
        public void run() {

            Bitmap bitmap = ImageFileCache.getInstance().getBitmapFromDiskCache(url);
            if (bitmap == null) {
                StreetscapeUtil.disableConnectionReuseIfNecessary();
                HttpURLConnection urlConnection = null;

                // Log.i("Demo","load from net");
                try {
                    final URL urls = new URL(url);
                    urlConnection = (HttpURLConnection) urls.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    if (urlConnection.getResponseCode() == 200) {
                        final InputStream in = new BufferedInputStream(urlConnection.getInputStream(),
                                StreetscapeUtil.IO_BUFFER_SIZE);
                        bitmap = BitmapFactory.decodeStream(in);
                        if (bitmap != null) {
                            ImageFileCache.getInstance().addBitmapToCache(url, bitmap);
                        }
                        in.close();
                    }
                } catch (final Exception e) {
                    //Log.e("wang7", "Error in downloadBitmap - " + e);
                    mLoadingList.remove(url);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }

                }
            }

            if (bitmap != null) {
                ImageMemoryCache.getInstance().put(url, bitmap);
                //Log.i("Demo", b - a + "download"+url);
                if (callback != null) {
                    callback.bitmapReady(bitmap);
                }
            }

            mLoadingList.remove(url);
        }
    }

    public BitmapDecoder getButmapDecoder() {
        return bitmapDecoder;
    }

}
