package com.act.sctc;

import com.act.sctc.db.DBHelper;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;

public class App extends Application {
	public static final String DATA_DIR = "/sdcard/sctc/";
	public static final String IMG_DIR = DATA_DIR + "imgs/";
	public static final String VIDEO_DIR = DATA_DIR + "video/";
	private static User currentUser;

	public static final int tabColorIds[] = new int[] { R.color.tab1, R.color.tab2, R.color.tab3, R.color.tab4,
			R.color.tab5, R.color.tab6 };

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void updateCurrentUser(Context context, User user, boolean saveToDB) {
		currentUser = user;
		if (saveToDB) {
			ContentValues values = new ContentValues();
			values.put("_id", user.userId);
			values.put("eid", user.eid);
			values.put("username", user.username);
			values.put("password", user.password);
			values.put("token", user.token);
			DBHelper dbHelper = DBHelper.getInstance(context);
			int count = dbHelper.update("user", values, "_id=?", new String[] { Integer.toString(user.userId) });
			if (count == 0) {
				dbHelper.insert("user", values);
			}
		}
	}

}
