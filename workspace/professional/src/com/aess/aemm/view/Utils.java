package com.aess.aemm.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.util.Log;
import android.view.WindowManager;

import com.aess.aemm.view.data.Appdb;

public class Utils {
	public static final String HOMEDATA = Environment
			.getExternalStorageDirectory().getPath() + "/aemm";
	public static final String ICONPATH = HOMEDATA + "/icons";
	public static final String APKPATH = HOMEDATA + "/apk";

	/**
	 * get APK info
	 * 
	 * @param context
	 * @param archiveFilePath
	 *            APK�ļ���·�����磺/sdcard/download/XX.apk
	 */
	public static PackageInfo getUninstallApkInfo(Context context,
			String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			String appName = pm.getApplicationLabel(appInfo).toString();
			String packageName = appInfo.packageName;
			Drawable icon = pm.getApplicationIcon(appInfo);
			Log.v("V",
					"apK:" + appName + " " + packageName + " "
							+ icon.toString());
		}
		return info;
	}

	@SuppressWarnings("unused")
	private static void checkIntalled(Context context, Appdb app) {
		if (app.getApkPackageName() == null) {
			return;
		}
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		int i = 0;
		for (; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((p.packageName == null)) {
				continue;
			}
			if (app.getApkPackageName().endsWith(p.packageName)) {
				app.setApkFlag(context, Appdb.INSTALLED);
				break;
			}
		}
		if (i >= packs.size() && app.getApkFlag() == Appdb.INSTALLED) {
			app.setApkFlag(context, Appdb.UNINSTALLED);
		}
	}

	@SuppressWarnings("unused")
	private static void checkIntalled(Context context, List<Appdb> list) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((p.packageName == null)) {
				continue;
			}
			boolean isContinue = false;
			for (Appdb app : list) {
				if (app.getApkFlag() == Appdb.INSTALLED) {
					continue;
				}
				if (app.getApkPackageName() == null) {
					continue;
				}
				isContinue = true;
				if (app.getApkPackageName().endsWith(p.packageName)) {
					app.setApkFlag(context, Appdb.INSTALLED);
					break;
				}
			}
			if (!isContinue) {
				break;
			}
			// PInfo newInfo = new PInfo();
			// newInfo.appname = p.applicationInfo.loadLabel(pm).toString();
			// newInfo.pname = p.packageName;
			// newInfo.versionName = p.versionName;
			// newInfo.versionCode = p.versionCode;
			// newInfo.icon = p.applicationInfo.loadIcon(pm);
			// res.add(newInfo);
		}
	}

	static public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	public boolean startApp(Context context, String packageName) {
		Intent mLaunchIntent = context.getPackageManager()
				.getLaunchIntentForPackage(packageName);
		boolean enabled = false;
		if (mLaunchIntent != null) {
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(mLaunchIntent, 0);
			if (list != null && list.size() > 0) {
				enabled = true;
				context.startActivity(mLaunchIntent);
			}
		}
		return enabled;
	}

	public static void brightnessMax(Activity mActivity) {
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
		lp.screenBrightness = 1.0f;
		mActivity.getWindow().setAttributes(lp);
	}

	public static void writeFile(Context context, String filename, String text) {
		FileOutputStream fo = null;
		try {
			fo = context.openFileOutput(filename, Context.MODE_PRIVATE);
			fo.write(text.getBytes("utf-8"));
			fo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
