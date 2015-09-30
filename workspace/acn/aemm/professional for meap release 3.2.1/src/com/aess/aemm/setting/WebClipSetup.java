package com.aess.aemm.setting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.aess.aemm.R;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

public class WebClipSetup {
	public static final String TAG = "WebClipSetup";
	public static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";

	public static final String AUTHORITY = "com.android.launcher2.settings";
	public static final Uri CONTENT_URI = Uri.parse(String.format(
			"content://%s/favorites?notify=true", AUTHORITY));

	public static int addWebClip(Context context, String name, String url,
			String iconStr) {
		Log.i(TAG, "addUrlShort");
		int ret = -1;
		Uri myBlogUri = Uri.parse(url);
		Intent urlIntent = new Intent(Intent.ACTION_VIEW, myBlogUri);

		Intent installShortCut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		installShortCut.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
		Bitmap icon = null;

		if (null == iconStr) {
			BitmapDrawable bmpDraw = (BitmapDrawable) context.getResources()
					.getDrawable(R.drawable.icon);
			icon = bmpDraw.getBitmap();
		} else {
			byte[] temp = Base64.decode(iconStr, 0);
			InputStream is = new ByteArrayInputStream(temp);
			icon = BitmapFactory.decodeStream(is);
		}

		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, urlIntent);
		context.sendBroadcast(installShortCut);
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void delWebClip(Context context, String name, String url) {
		Log.i(TAG, "delUrlShort");
		Uri myBlogUri = Uri.parse(url);
		Intent urlIntent = new Intent(Intent.ACTION_VIEW, myBlogUri);

		Intent delShortCut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		delShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

		delShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, urlIntent);
		context.sendBroadcast(delShortCut);
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int editWebClip(Context context, String name, String iconStr) {
		long _id = -1;
		ContentValues newcv = getSysConfig(context, name, iconStr, _id);
		if (_id >= 0 && null != newcv) {
			Uri uri = ContentUris.withAppendedId(SYS_CONTENT_URI, _id);
			context.getContentResolver().update(uri, newcv, null, null);
		}

		return 1;
	}

	private static ContentValues getSysConfig(Context context, String name,
			String iconStr, long out_id) {
		Bitmap icon = null;
		ContentValues cv = null;
		if (null == iconStr) {
			BitmapDrawable bmpDraw = (BitmapDrawable) context.getResources()
					.getDrawable(R.drawable.icon);
			icon = bmpDraw.getBitmap();
		} else {
			byte[] temp = Base64.decode(iconStr, 0);
			InputStream is = new ByteArrayInputStream(temp);
			icon = BitmapFactory.decodeStream(is);
		}
		if (null != icon) {
			Cursor cursor = getSysDBinfo(context, name);
			try {
				if (cursor.moveToFirst()) {
					int inedx = cursor.getColumnIndex(SYS_ID);
					out_id = cursor.getLong(inedx);
					cv = new ContentValues();
					byte[] date = flattenBitmap(icon);
					cv.put("icon", date);

				}
			} finally {
				cursor.close();
			}
		}
		return cv;
	}

	private static byte[] flattenBitmap(Bitmap bitmap) {

		int size = bitmap.getWidth() * bitmap.getHeight() * 4;
		ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			Log.w("Favorite", "Could not write icon");
			return null;
		}
	}

	public static boolean systemHaveSameConfig(Context context, String name,
			String url) {
		Log.i(TAG, "systemHaveSameConfig");
		boolean repeat = false;

		Cursor c = getSysDBinfo(context, name);
		if (null != c) {
			try {
				if (c.moveToFirst()) {
					do {
						int index = c.getColumnIndex(SYS_INTENT);
						String oldintent = c.getString(index);
						String uristr[] = oldintent.split("#");
						String uri = null;
						if (uristr.length > 0) {
							uri = uristr[0];
						}
						if (uri.equals(url)) {
							repeat = true;
						}
					} while (c.moveToNext());
				}
			} finally {
				c.close();
			}
		}

		return repeat;
	}

	private static Cursor getSysDBinfo(Context context, String name) {
		String where = String.format("%s=?", SYS_TITLE);
		Cursor c = context.getContentResolver().query(SYS_CONTENT_URI,
				new String[] { SYS_ID, SYS_INTENT, SYS_TITLE }, where,
				new String[] { name }, null);
		return c;
	}

	private final static Uri SYS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/favorites?notify=true");
	private final static String SYS_TITLE = "title";
	private final static String SYS_ID = "_id";
	private final static String SYS_INTENT = "intent";
}