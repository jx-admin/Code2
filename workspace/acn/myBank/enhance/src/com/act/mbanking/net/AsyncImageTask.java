package com.act.mbanking.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
/**
 * 
 * @author yang.c.li
 *
 */
public class AsyncImageTask extends AsyncTask<Object, Object, Bitmap> {
	private final String TAG = "AsyncImageTask";
	private ImageView imgView;
	private String url;

	@SuppressWarnings("finally")
	@Override
	protected Bitmap doInBackground(Object... params) {
		Bitmap bmp = null;
		imgView = (ImageView) params[1];
		try {
			url = (String) params[0];
			bmp = BitmapFactory.decodeStream(new URL(url).openStream());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
			bmp = null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			bmp = null;
		} finally {
			return bmp;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result == null) {
			//Ĭ��ͼƬ
			imgView.setImageResource(R.drawable.btn_default);
		} else {
			imgView.setImageBitmap(result);
		}
	}
}
