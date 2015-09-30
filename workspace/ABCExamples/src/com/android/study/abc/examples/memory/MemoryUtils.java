package com.android.study.abc.examples.memory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import a.w.utils.Logger;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
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
import android.text.TextUtils;
import android.text.format.Formatter;

import com.android.internal.util.MemInfoReader;

public class MemoryUtils {
	static final String TAG = MemoryUtils.class.getSimpleName();

	ActivityManager mAm = null;
	private PackageManager mPackageManager = null;
	MemInfoReader mMemInfoReader = new MemInfoReader();
	ActivityManager.MemoryInfo meminfo = new ActivityManager.MemoryInfo();
	/** * 程序包占用内存大小 */
	private long totalCacheSize = 0l;

	public void initMemoryInfo(Context context) {
		mMemInfoReader.readMemInfo();
		if (mAm == null) {
			mAm = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
		}
		mAm.getMemoryInfo(meminfo);
	}

	public long getTotalSize() {
		return mMemInfoReader.getTotalSize();
	}

	public long getFreeSize() {
		return mMemInfoReader.getFreeSize();
	}

	public long getCachedSize() {
		return mMemInfoReader.getCachedSize();
	}

	public ActivityManager.MemoryInfo getMemoryInfo() {
		return meminfo;
	}

	public long getAvalable() {
		return mMemInfoReader.getFreeSize() + mMemInfoReader.getCachedSize();
	}

	public long getTatal() {
		return mMemInfoReader.getTotalSize() - meminfo.threshold;
	}

	/**
	 * 获取最近进程数
	 * 
	 * @return
	 */
	public int getRecentTasks() {
		List<ActivityManager.RecentTaskInfo> recentTasks = mAm.getRecentTasks(
				21, ActivityManager.RECENT_WITH_EXCLUDED
						| ActivityManager.RECENT_IGNORE_UNAVAILABLE);
		return recentTasks.size();
	}

	public static String format(long size) {
		StringBuilder sb = new StringBuilder();
		char[] name = { 'G', 'M', 'K' };
		int i = name.length - 1;
		for (; i >= 1 && size > 0; i--) {
			sb.insert(0, name[i]).insert(0, size % 1024);
			size /= 1024;
		}
		sb.insert(0, name[i]).insert(0, size);
		return sb.toString();
	}

	private String mComName;
	private Context mContext;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ON_CLEANING:
				Logger.i(TAG, "ON_CLEANING");
				CleanObj clean = (CleanObj) msg.obj;
				CleanEvent.getInstance().fireOnProgressChange(
						clean.currentProgress, clean.maxCount,
						clean.percentage, clean.processName, mComName);
				break;
			case ON_CLEAN_DONE:
				Logger.i(TAG, "ON_CLEAN_DONE");
				CleanObj cleanMb = (CleanObj) msg.obj;
				CleanEvent.getInstance().fireOnCleanDone(mComName, mContext,
						cleanMb.cleanMb, cleanMb.afterMb, cleanMb.beforeMb);
				break;
			case ON_CLEAN_START:
				Logger.i(TAG, "ON_CLEAN_START");
				CleanEvent.getInstance().fireOnStartClean(mComName);
				break;
			default:
				break;
			}
		}
	};

	private final static int ON_CLEAN_START = 0;
	private final static int ON_CLEAN_DONE = 1;
	private final static int ON_CLEANING = 2;

	// current, max, percentage,processName
	private class CleanObj {
		public String percentage;
		public int maxCount;
		public int currentProgress;
		public String processName;
		public long afterMb;
		public long beforeMb;
		public String cleanMb;
	}

	public void killProcesses(final Context context, String comName) {
		Logger.i(TAG, "killProcesses");
		this.mComName = comName;
		this.mContext = context;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = mHandler.obtainMessage();
				msg.what = ON_CLEAN_START;
				mHandler.sendMessage(msg);
				killBackgroundProcessesToIcon(context);
			}
		}).start();
	}

	@SuppressLint("NewApi")
	private void killBackgroundProcessesToIcon(Context context) {
		Logger.i(TAG, "killBackgroundProcessesToIcon");
		int processCount = 0;
		CleanObj ob = new CleanObj();
		ob.beforeMb = getAvalable();
		ActivityManager am = (ActivityManager) context.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processes = am
				.getRunningAppProcesses();
		int maxProcessCount = processes.size();
		for (ActivityManager.RunningAppProcessInfo info : processes) {
			if (info != null && info.processName != null
					&& info.processName.length() > 0) {
				String pkgName = info.processName;
				if (!("system".equals(pkgName)
						|| "android.process.media".equals(pkgName)
						|| "android.process.acore".equals(pkgName)
						|| pkgName.contains("mediatek")
						|| "com.android.systemui".equals(pkgName)
						|| "com.android.phone".equals(pkgName)
						|| "com.android.data".equals(pkgName) || pkgName
							.contains("noxus"))) {

					am.killBackgroundProcesses(pkgName);

					int currentProgress = processCount++;
					CleanObj clean = new CleanObj();
					clean.currentProgress = currentProgress;
					clean.maxCount = maxProcessCount;
					clean.processName = pkgName;
					clean.percentage = getMemPercentage(context);
					Message msg1 = mHandler.obtainMessage();
					msg1.obj = clean;
					msg1.what = ON_CLEANING;
					mHandler.sendMessage(msg1);
				}
			}
		}
		// 清理之后的内Msg
		Message msg2 = mHandler.obtainMessage();
		initMemoryInfo(context);
		ob.afterMb = getAvalable();
		ob.cleanMb = Formatter
				.formatFileSize(context, ob.afterMb - ob.beforeMb);
		msg2.what = ON_CLEAN_DONE;
		msg2.obj = ob;
		mHandler.sendMessage(msg2);
	}

	/**
	 * 关闭后台运行程序
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public int killBackgroundProcesses(Context context) {
		int processCount = 0;
		ActivityManager am = (ActivityManager) context.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processes = am
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : processes) {
			if (info != null && info.processName != null
					&& info.processName.length() > 0) {
				String pkgName = info.processName;
				if (!("system".equals(pkgName)
						|| "android.process.media".equals(pkgName)
						|| "android.process.acore".equals(pkgName)
						|| pkgName.contains("mediatek")
						|| "com.android.systemui".equals(pkgName)
						|| "com.android.phone".equals(pkgName)
						|| "com.android.data".equals(pkgName) || pkgName
							.contains("noxus"))) {

					am.killBackgroundProcesses(pkgName);
					processCount++;
				}
			}
		}
		return processCount;
	}

	/**
	 * 已使用内存百分比
	 * 
	 * @param context
	 * @return
	 */
	public String getMemPercentage(Context context) {
		// 获得当前可用内存
		initMemoryInfo(context);
		double availMem = mMemInfoReader.getFreeSize()
				+ mMemInfoReader.getCachedSize();
		double per = getAvalable() / getTatal() * 100;
		return String.valueOf(100 - (int) per) + "%";
	}

	public void clearCache(Context context) {
		queryToatalCache(context, mStatsObserver);
		try {
			if (mPackageManager == null) {
				mPackageManager = context.getPackageManager();
			}
			String methodName = "freeStorageAndNotify";
			Class<?> parameterType1 = Long.TYPE;
			Class<?> parameterType2 = IPackageDataObserver.class;
			Method freeStorageAndNotify = mPackageManager.getClass().getMethod(
					methodName, parameterType1, parameterType2);
			/*
			 * freeStorageSize (^_^) The number of bytes of storage to be freed
			 * by the system. Say if freeStorageSize is XX, and the current free
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
	 * 回调所有程序大小信息
	 * 
	 * @param context
	 */
	public void queryToatalCache(Context context,
			IPackageStatsObserver.Stub mStatsObserver) {
		if (mPackageManager == null) {
			mPackageManager = context.getPackageManager();
		}
		List<ApplicationInfo> apps = mPackageManager
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_ACTIVITIES);
		String pkgName = "";
		for (ApplicationInfo info : apps) {
			pkgName = info.packageName;
			try {
				queryPkgCacheSize(context, pkgName, mStatsObserver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 请求程序包大小信息
	 * 
	 * @param context
	 * @param pkgName
	 * @throws Exception
	 */
	private void queryPkgCacheSize(Context context, String pkgName,
			IPackageStatsObserver.Stub mStatsObserver) throws Exception {
		if (!TextUtils.isEmpty(pkgName)) {
			if (mPackageManager == null) {
				mPackageManager = context.getPackageManager();
			}
			try {
				// the requested method's name.
				String strGetPackageSizeInfo = "getPackageSizeInfo";
				Method getPackageSizeInfo = mPackageManager.getClass()
						.getDeclaredMethod(strGetPackageSizeInfo, String.class,
								IPackageStatsObserver.class);
				getPackageSizeInfo.invoke(mPackageManager, pkgName,
						mStatsObserver);

			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
	}

	/**
	 * 程序包大小信息回调 public long cachesize 缓存大小
	 * 
	 * public long codesize 应用程序大小
	 * 
	 * public long datasize 数据大小
	 * 
	 * public String packageName 包名
	 */
	private IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			totalCacheSize += pStats.cacheSize;
		}
	};
}
