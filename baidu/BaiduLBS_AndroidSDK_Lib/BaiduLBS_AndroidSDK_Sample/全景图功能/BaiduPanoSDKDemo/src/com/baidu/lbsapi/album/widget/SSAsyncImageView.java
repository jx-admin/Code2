package com.baidu.lbsapi.album.widget;

import com.baidu.lbsapi.album.util.ImageLoader;
import com.baidu.lbsapi.album.util.cache.ImageMemoryCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: wangziji
 * Date: 13-12-12
 * Time: 上午10:55
 * To change this template use File | Settings | File Templates.
 */
public class SSAsyncImageView extends ImageView implements ImageLoader.ImageLoaderCallback {

    public String url;
    private Bitmap mBitmap;

    public SSAsyncImageView(Context context) {
        super(context);
    }

    public SSAsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SSAsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String imageUrl) {
        url = imageUrl;
    }

    public void show() {
        if (mBitmap == null || mBitmap.isRecycled()) {
            ImageLoader.getInstance().setBitmapDecoder(this.getWidth(), this.getHeight());
            ImageLoader.getInstance().execute(url, this);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        //    	mBitmap = bitmap;
        super.setImageBitmap(bitmap);
    }

    public void hide(Bitmap defaultBitmap) {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            ImageMemoryCache.getInstance().put(url, mBitmap);
            mBitmap.recycle();
            mBitmap = null;
            this.setImageBitmap(defaultBitmap);
            this.setScaleType(ScaleType.CENTER);
            //Log.i("Demo", "recycled==========================================================");
        }
    }

    @Override
    public void bitmapReady(final Bitmap bitmap) {

        this.post(new Runnable() {
            @Override
            public void run() {
                mBitmap = bitmap;
                if (bitmap != null && !bitmap.isRecycled()) {
                    SSAsyncImageView.this.setImageBitmap(bitmap);
                    SSAsyncImageView.this.setScaleType(ScaleType.FIT_XY);
                }
            }
        });

    }
}
