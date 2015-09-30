package com.act.sctc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.widget.ImageView;

public class Utils {
	// 网上关于android下MD5加密的资料很多，但是测试了下总是跟网站的md5加密不一样， 后来才知道是编码方式不对，于是就自己写了一个。
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	public static String toHexString(byte[] b) {
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}

		return sb.toString();
	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static synchronized Bitmap getOptimizedBitmap(String fileName) {
		if (Logger.DEBUG) {
			Logger.debug("getOptimizedBitmap: " + fileName);
		}
		Bitmap bitmap = null;
		try {
			BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(fileName, true);
			int width = decoder.getWidth();
			int height = decoder.getHeight();
			bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
			Canvas canvas = new Canvas();
			canvas.setBitmap(bitmap);
			canvas.drawColor(Color.WHITE);
			Paint paint = new Paint();
			paint.setDither(true);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.ARGB_8888;
			for (int top = 0; top < height;) {
				int bottom = top + 0x100;
				if (bottom > height) {
					bottom = height;
				}
				Bitmap bmp = decoder.decodeRegion(new Rect(0, top, width, bottom), options);
				canvas.drawBitmap(bmp, 0, top, paint);
				top = bottom;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static void LazyLoadImage(ImageView imageView, String fileName) {
		LazyImageLoaderTask task = new LazyImageLoaderTask(imageView);
		task.execute(fileName);
	}

	private static class LazyImageLoaderTask extends AsyncTask<String, Object, Bitmap> {
		private ImageView imageView;

		public LazyImageLoaderTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String fileName = params[0];
			Bitmap bmp = Utils.getOptimizedBitmap(fileName);
			return bmp;
		}

		protected void onPostExecute(Bitmap bmp) {
			if (bmp != null) {
				imageView.setImageBitmap(bmp);
			}
		}
	}

}
