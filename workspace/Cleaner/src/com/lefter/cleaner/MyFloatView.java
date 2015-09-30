package com.lefter.cleaner;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * 二、totalCacheSize出问题;
 * 
 * 三、模拟器上浏览器的cache没清除,造成缓存清理大小出错;
 * 
 * 四、ViewConfiguration.getTapTimeout();
 */
public class MyFloatView extends ImageView {
	/*
	 * getX()是表示相对于控件自身左上角的x坐标,而getRawX()是表示相对于手机屏幕左上角的x坐标值。getY(),getRawY()一样的道理
	 */
	private float actionDownX;// 触发MotionEvent.ACTION_DOWN时通过MotionEvent.getX()函数得到的x坐标
	private float actionDownY;// 触发MotionEvent.ACTION_DOWN时通过MotionEvent.getY()函数得到的y坐标

	private float moveX;// 触发MotionEvent.ACTION_MOVE时的x坐标,MotionEvent.getRawX()
	private float moveY;// 触发MotionEvent.ACTION_MOVE时的y坐标,MotionEvent.getRawY()

	private float downX;// 触发MotionEvent.ACTION_DOWN时的x坐标，mWindowManagerLayoutParams中的x的值
	private float downY;// 触发MotionEvent.ACTION_DOWN时的y坐标，mWindowManagerLayoutParams中的y的值

	private float upX;// 触发MotionEvent.ACTION_UP时的x坐标，mWindowManagerLayoutParams中的x的值
	private float upY;// 触发MotionEvent.ACTION_UP时的y坐标，mWindowManagerLayoutParams中的y的值

	private LayoutParams mLayoutParams;// 用于记录修改后的悬浮图标的位置(xy坐标)
	private WindowManager mWindowManager;// 根据WindowManager.LayoutParams修改悬浮图标的位置

	// 用户触摸悬浮图标时有没有触发MotionEvent.ACTION_MOVE，如果触发了，我们将不进行点击事件的处理
	private boolean isMoveAction = false;

	// 根据两次点击的时间间隔判断是双击还是单击操作
	private boolean isDoubleClick = false;

	private boolean isClearCacheOpen = true;// 是否在设置页面打开清理缓存
	private boolean isKillBgProcessOpen = true;// 是否在设置页面打开杀死进程
	private boolean isDoubleClickCloseOpen = true;// 是否在设置页面双击关闭悬浮窗口

	private long firstTime = 0l;// 用户第一次触摸悬浮图标时的时间
	private long secondTime = 0l;// 用户第二次触摸悬浮图标时的时间

	private Handler mHandler = null;// 用来更新Toast信息
	// Handler中可以处理的Message
	private static final int MSG_UPDATE_TOAST_TEXT = 1;// 更新Toast显示的信息

	// 提示用户正在进行的操作
	private Toast mToast = null;
	private String mText = "";// Toast要显示的信息
	private String[] formats;// 用于格式化Toast信息
	private int clear_set_index = 0;// R.arry.clear_set的index，即使用哪种格式的Toast

	private int processCount = 0;// 记录杀死的进程数
	private long totalCacheSize = 0l;// 记录手机中缓存的大小

	private PackageManager mPackageManager = null;// 在处理缓存相关的函数中使用

	public MyFloatView(Context context,
			WindowManager.LayoutParams wmLayoutParams) {
		super(context);
		mWindowManager = (WindowManager) getContext().getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		mLayoutParams = wmLayoutParams;// 创建对象时需要传入对象的布局参数，本类负责更新布局参数

		formats = context.getResources().getStringArray(R.array.clear_set);// 初始化

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// 处理msg
				switch (msg.what) {
				case MSG_UPDATE_TOAST_TEXT:
					mText = String.format(formats[clear_set_index],
							processCount, convertStorage(totalCacheSize),
							convertStorage(getRamFreeMemSize()));
					mToast.setText(mText);
					break;
				}
			}
		};
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
			// 用户移动前的xy坐标
			downX = mLayoutParams.x;
			downY = mLayoutParams.y;

			// 获取坐标，以此View左上角为原点
			actionDownX = event.getX();
			actionDownY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
			// 获取坐标，以屏幕左上角为原点
			moveX = event.getRawX();
			moveY = event.getRawY() - 25; // 25是系统状态栏的高度

			updateViewPosition(false);
			isMoveAction = true;
			break;

		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
			// 用户移动结束时的xy坐标
			upX = mLayoutParams.x;
			upY = mLayoutParams.y;

			updateViewPosition(true);

			// 初始化
			firstTime = System.currentTimeMillis();
			if (!isMoveAction) {// 不是移动悬浮图标，是点击操作
				if (mToast == null) {// 初始化Toast对象
					mToast = Toast.makeText(getContext(), mText,
							Toast.LENGTH_LONG);
				}
				if (firstTime - secondTime < 600) {// 双击

					isDoubleClick = true;// 两次点击的时间间隔小，按双击处理

					isDoubleClickCloseOpen = PreferenceManager
							.getDefaultSharedPreferences(getContext())
							.getBoolean(CleanerActivity.KEY_DOUBLE_CLICK_CLOSE,
									true);
					if (isDoubleClickCloseOpen) {// 是否打开双击关闭
						clear_set_index = 4;
						stopFloatService();
					}
				} else {// 单击

					isDoubleClick = false;// 时间间隔足够大，按单击处理

					isClearCacheOpen = PreferenceManager
							.getDefaultSharedPreferences(getContext())
							.getBoolean(CleanerActivity.KEY_CLEAR_CACHE, true);
					isKillBgProcessOpen = PreferenceManager
							.getDefaultSharedPreferences(getContext())
							.getBoolean(CleanerActivity.KEY_KILL_BGPROCESS,
									true);

					if (isClearCacheOpen && isKillBgProcessOpen) {// 全部打开
						clearCacheSchedule();
						killProcessSchedule();
						clear_set_index = 3;
					} else {// 单开或不开
						if (!(isClearCacheOpen || isKillBgProcessOpen)) {// 不开
							clear_set_index = 0;
						} else {// 单开
							if (isClearCacheOpen) {
								clearCacheSchedule();
								clear_set_index = 2;
							}
							if (isKillBgProcessOpen) {
								killProcessSchedule();
								clear_set_index = 1;
							}
						}
					}
				}
				mHandler.sendEmptyMessage(MSG_UPDATE_TOAST_TEXT);
				mToast.show();
			}
			secondTime = firstTime;// 记录上一次触摸的时间
			// 还原flag的状态
			isMoveAction = false;

			break;
		}
		return true;
	}

	/**
	 * 触发MotionEvent.ACTION_UP时，不判断移动范围的大小，直接改变悬浮窗口的坐标；触发MotionEvent.
	 * ACTION_UP时判断移动的范围是不是太小，如果太小就返回移动前的位置
	 * 
	 * @param isActionUp
	 *            是否为MotionEvent.ACTION_UP
	 */
	private void updateViewPosition(boolean isActionUp) {
		if (!isActionUp) {// MotionEvent.ACTION_MOVE
			// 更新浮动窗口位置参数
			mLayoutParams.x = (int) (moveX - actionDownX);
			mLayoutParams.y = (int) (moveY - actionDownY);
		} else {// MotionEvent.ACTION_UP
			if (!(Math.abs(upX - downX) > 50 || Math.abs(upY - downY) > 50)) {// 移动范围太小,返回初始位置
				isMoveAction = false;// 作为点击事件进行处理
				// 更新浮动窗口位置参数，返回触摸前的位置
				mLayoutParams.x = (int) (downX);
				mLayoutParams.y = (int) (downY);
			}
		}
		mWindowManager.updateViewLayout(this, mLayoutParams); // 更新悬浮窗口
	}

	/**
	 * 终止Service
	 */
	private void stopFloatService() {
		Intent i = new Intent();
		i.setClass(getContext(), FloatService.class);
		getContext().stopService(i);
	}

	/**
	 * 杀进程函数
	 */
	private void killBackgroundProcess() {
		processCount = 0;
		ActivityManager am = (ActivityManager) getContext()
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		// 获得正在运行的所有进程
		List<ActivityManager.RunningAppProcessInfo> processes = am
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo info : processes) {
			if (info != null && info.processName != null
					&& info.processName.length() > 0) {
				String pkgName = info.processName;
				if (!("system".equals(pkgName)
						|| "android.process.media".equals(pkgName)
						|| "android.process.acore".equals(pkgName)
						|| "com.android.phone".equals(pkgName) || pkgName
							.startsWith("com.lefter"))) {
					am.killBackgroundProcesses(pkgName);// 杀进程
					processCount++;// 更新被杀进程的数量
					mHandler.sendEmptyMessage(MSG_UPDATE_TOAST_TEXT);
				}
			}
		}
	}

	/**
	 * 执行清除缓存操作
	 */
	private void clearCache() {
		totalCacheSize = 0l;// 初始化
		queryToatalCache();// 给cacheSize赋值
		try {
			if (mPackageManager == null) {
				mPackageManager = getContext().getPackageManager();// 得到被反射调用函数所在的类对象
			}
			String methodName = "freeStorageAndNotify";// 想通过反射机制调用的方法名
			Class<?> parameterType1 = Long.TYPE;// 被反射的方法的第一个参数的类型
			Class<?> parameterType2 = IPackageDataObserver.class;// 被反射的方法的第二个参数的类型
			Method freeStorageAndNotify = mPackageManager.getClass().getMethod(
					methodName, parameterType1, parameterType2);
			/*
			 * freeStorageSize ： The number of bytes of storage to be freed by
			 * the system. Say if freeStorageSize is XX, and the current free
			 * storage is YY, if XX is less than YY, just return. if not free
			 * XX-YY number of bytes if possible.
			 */
			Long freeStorageSize = Long.valueOf(getDataDirectorySize());

			freeStorageAndNotify.invoke(mPackageManager, freeStorageSize,
					new IPackageDataObserver.Stub() {
						@Override
						public void onRemoveCompleted(String packageName,
								boolean succeeded) throws RemoteException {
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Timer mTimer = new Timer();

	private TimerTask mClearCacheTask;
	private TimerTask mKillProcessTask;

	class ClearCacheTask extends TimerTask {
		@Override
		public void run() {
			try {
				Thread.currentThread().sleep(550);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!isDoubleClick) {
				clearCache();
			}
		}
	}

	class KillProcessTask extends TimerTask {
		@Override
		public void run() {
			try {
				Thread.currentThread().sleep(550);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!isDoubleClick) {
				killBackgroundProcess();
			}
		}
	}

	private void clearCacheSchedule() {
		if (mClearCacheTask != null) {
			mClearCacheTask.cancel();
		}
		mClearCacheTask = new ClearCacheTask();
		mTimer.schedule(mClearCacheTask, 0);
	}

	private void killProcessSchedule() {
		if (mKillProcessTask != null) {
			mKillProcessTask.cancel();
		}
		mKillProcessTask = new KillProcessTask();
		mTimer.schedule(mKillProcessTask, 0);
	}

	/**
	 * 获得RAM中的可用内存大小
	 */
	private long getRamFreeMemSize() {
		// 获取android当前可用内存大小
		MemInfoReader miReader = new MemInfoReader();
		miReader.readMemInfo();
		return (miReader.getFreeSize() + miReader.getCachedSize());
	}

	/**
	 * 返回/data目录的大小。
	 */
	private long getDataDirectorySize() {
		File tmpFile = Environment.getDataDirectory();
		if (tmpFile == null) {
			return 0l;
		}
		String strDataDirectoryPath = tmpFile.getPath();
		StatFs localStatFs = new StatFs(strDataDirectoryPath);
		long size = localStatFs.getBlockSize() * localStatFs.getBlockCount();
		return size;
	}

	/**
	 * 获得手机中所有程序的缓存
	 */
	private void queryToatalCache() {
		if (mPackageManager == null) {
			mPackageManager = getContext().getPackageManager();
		}
		List<ApplicationInfo> apps = mPackageManager
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_ACTIVITIES);
		String pkgName = "";
		for (ApplicationInfo info : apps) {
			pkgName = info.packageName;
			try {
				queryPkgCacheSize(pkgName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 取得指定包名的程序的缓存大小
	 */
	private void queryPkgCacheSize(String pkgName) throws Exception {
		if (!TextUtils.isEmpty(pkgName)) {// pkgName不能为空
			// 使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
			if (mPackageManager == null) {
				mPackageManager = getContext().getPackageManager();// 得到被反射调用函数所在的类对象
			}
			try {
				// the requested method's name.
				String strGetPackageSizeInfo = "getPackageSizeInfo";
				// 通过反射机制获得该隐藏函数
				Method getPackageSizeInfo = mPackageManager.getClass()
						.getDeclaredMethod(strGetPackageSizeInfo, String.class,// getPackageSizeInfo方法的参数类型
								IPackageStatsObserver.class);// getPackageSizeInfo方法的参数类型
				// 调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
				getPackageSizeInfo.invoke(mPackageManager,// 方法所在的类
						pkgName, mStatsObserver);// 方法使用的参数

			} catch (Exception ex) {
				Log.wtf("tag", "queryPkgSize()-->NoSuchMethodException");
				ex.printStackTrace();
				throw ex; // 抛出异常
			}
		}
	}

	/**
	 * 使用Android系统中的AIDL文件，获得指定程序的大小。AIDL文件形成的Binder机制
	 */
	private IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long tmp = totalCacheSize;
			totalCacheSize += pStats.cacheSize;// 累加
			if (tmp != totalCacheSize) {// 总缓存大小有变化
				mHandler.sendEmptyMessage(MSG_UPDATE_TOAST_TEXT);
			}
		}
	};

	/**
	 * 将参数转化为GB、MB、KB、B的形式
	 */
	public static String convertStorage(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}
}
