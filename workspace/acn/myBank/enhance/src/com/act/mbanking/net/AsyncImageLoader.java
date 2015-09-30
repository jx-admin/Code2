package com.act.mbanking.net;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @author yang.c.li
 * 
 */
public class AsyncImageLoader {

	private Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback callback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				callback.imageLoaded((Drawable) softReference.get(), imageUrl);
				return null;
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
                callback.imageLoaded((Drawable) msg.obj, imageUrl);
			}
		};
		new Thread() {
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				handler.sendMessage(handler.obtainMessage(0, drawable));
			};
		}.start();
		return null;
	}

	protected Drawable loadImageFromUrl(String imageUrl) {
		try {
			return Drawable.createFromStream(new URL(imageUrl).openStream(),"src");
		} catch (Exception e) {
			Log.e("loadingImage", "LoadingImageFail", e);
//			throw new RuntimeException(e);
			return null;
		}
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
