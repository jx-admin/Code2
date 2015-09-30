package com.lefter.cleaner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 负责创建悬浮窗口，在销毁时保存悬浮窗口的坐标信息。
 * 
 * Notification未完善
 */
public class FloatService extends Service {
	public static final int NOTIFICATION_ID = 1988;
	private WindowManager mWindowManager = null;
	private LayoutParams mLayoutParams = null;
	private MyFloatView mFloatView = null;

	private SharedPreferences mPref;// 用于保存关闭悬浮窗口时悬浮窗口的坐标
	private Editor mEditor;
	private final String PREF_NAME = "pref";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		// 创建悬浮窗口
		createView();
		Notification n = new Notification();
		// TODO 可以完善Notification

		startForeground(NOTIFICATION_ID, n);// 提高程序优先级，防止被杀
		return Service.START_REDELIVER_INTENT;
	}

	private void createView() {
		Log.e("tag", "createView()");
		if (mFloatView == null) {// 防止重复创建
			// 设置LayoutParams(全局变量）相关参数
			mLayoutParams = new LayoutParams();

			/**
			 * 以下都是WindowManager.LayoutParams的相关属性 具体用途可参考SDK文档
			 */
			mLayoutParams.type = LayoutParams.TYPE_SYSTEM_ALERT; // 设置window
																	// type
			mLayoutParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

			// 设置Window flag
			mLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL// 允许window以外的区域接受点击
					| LayoutParams.FLAG_NOT_FOCUSABLE;// 不能获得焦点

			mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP; // 以屏幕左上角为原点

			// 设置x、y初始值
			mLayoutParams.x = mPref.getInt("x", 0);
			mLayoutParams.y = mPref.getInt("y", 0);

			// 设置悬浮窗口长、宽
			mLayoutParams.width = 72;
			mLayoutParams.height = 72;

			mFloatView = new MyFloatView(getApplicationContext(), mLayoutParams);
			mFloatView.setImageResource(R.drawable.icon_clear);

			// 获取WindowManager
			mWindowManager = (WindowManager) getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);

			// 显示myFloatView图像
			mWindowManager.addView(mFloatView, mLayoutParams);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在service结束时保存悬浮窗口的位置
		mEditor = mPref.edit();
		mEditor.putInt("x", mLayoutParams.x);
		mEditor.putInt("y", mLayoutParams.y);
		mEditor.commit();
		// 在service结束时销毁悬浮窗口
		mWindowManager.removeView(mFloatView);
		// service结束时销毁Notification
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_ID);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}