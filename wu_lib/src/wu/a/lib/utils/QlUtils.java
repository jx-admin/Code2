package wu.a.lib.utils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
import static android.content.pm.PackageManager.DONT_KILL_APP;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;

public final class QlUtils {
	private final static String TAG = "QlUtils";
	private final static boolean DEBUG = true;

	/**
	 * Returns a string that represents the symbolic name of the specified
	 * unmasked action such as "ACTION_DOWN", "ACTION_POINTER_DOWN(3)" or an
	 * equivalent numeric constant such as "35" if unknown.
	 *
	 * @param action
	 *            The unmasked action.
	 * @return The symbolic name of the specified action.
	 */
	public static String motionActionToString(int action) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return MotionEvent.actionToString(action);
		}

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			return "ACTION_DOWN";
		case MotionEvent.ACTION_UP:
			return "ACTION_UP";
		case MotionEvent.ACTION_CANCEL:
			return "ACTION_CANCEL";
		case MotionEvent.ACTION_OUTSIDE:
			return "ACTION_OUTSIDE";
		case MotionEvent.ACTION_MOVE:
			return "ACTION_MOVE";
		case MotionEvent.ACTION_HOVER_MOVE:
			return "ACTION_HOVER_MOVE";
		case MotionEvent.ACTION_SCROLL:
			return "ACTION_SCROLL";
		case MotionEvent.ACTION_HOVER_ENTER:
			return "ACTION_HOVER_ENTER";
		case MotionEvent.ACTION_HOVER_EXIT:
			return "ACTION_HOVER_EXIT";
		}
		int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:
			return "ACTION_POINTER_DOWN(" + index + ")";
		case MotionEvent.ACTION_POINTER_UP:
			return "ACTION_POINTER_UP(" + index + ")";
		default:
			return Integer.toString(action);
		}
	}

	public static <T> void sparseArrayToArray(SparseArray<T> sparseArray, T[] array) {
		for (int i = 0, n = sparseArray.size(); i < n; i++) {
			array[sparseArray.keyAt(i)] = sparseArray.valueAt(i);
		}
	}

	public static <T> void fillArray(SparseArray<T> sparseArray, T[] array) {
		for (int i = 0, n = sparseArray.size(); i < n; i++) {
			array[i] = sparseArray.valueAt(i);
		}
	}

	public static <T> void fillList(SparseArray<T> sparseArray, List<T> array) {
		for (int i = 0, n = sparseArray.size(); i < n; i++) {
			array.add(sparseArray.valueAt(i));
		}
	}

	public static String joinStringArray(String prefix, String[] array, String separator, String suffix) {
		if (null == array || array.length <= 0)
			return null;

		if (array.length == 1)
			return array[0];

		StringBuilder sb = new StringBuilder();

		sb.append(prefix);

		joinStringArray(sb, array, separator);

		sb.append(suffix);

		return sb.toString();
	}

	public static String joinStringArray(String prefix, String[] array, char separator, String suffix) {
		if (null == array || array.length <= 0)
			return null;

		if (array.length == 1)
			return array[0];

		StringBuilder sb = new StringBuilder();

		sb.append(prefix);

		joinStringArray(sb, array, separator);

		sb.append(suffix);

		return sb.toString();
	}

	public static String joinStringArray(char prefix, String[] array, char separator, char suffix) {
		if (null == array || array.length <= 0)
			return null;

		if (array.length == 1)
			return array[0];

		StringBuilder sb = new StringBuilder();

		sb.append(prefix);

		joinStringArray(sb, array, separator);

		sb.append(suffix);

		return sb.toString();
	}

	public static String joinStringArray(String[] array, String separator) {
		return joinStringArray(null, array, separator);
	}

	public static String joinStringArray(String[] array, char separator) {
		return joinStringArray(null, array, separator);
	}

	public static String joinStringArray(StringBuilder result, String[] array, String separator) {
		boolean returnString = false;
		if (null == result) {
			result = new StringBuilder();
			returnString = true;
		}
		doJoin(result, array, separator);
		return returnString ? result.toString() : null;
	}

	public static String joinStringArray(StringBuilder result, String[] array, char separator) {
		boolean returnString = false;
		if (null == result) {
			result = new StringBuilder();
			returnString = true;
		}
		doJoin(result, array, separator);
		return returnString ? result.toString() : null;
	}

	private static void doJoin(StringBuilder result, String[] array, char separator) {
		for (String s : array) {
			result.append(s).append(separator);
		}
		result.deleteCharAt(result.length() - 1);
	}

	private static void doJoin(StringBuilder result, String[] array, String separator) {
		for (String s : array) {
			result.append(s).append(separator);
		}
		int len = result.length();
		result.delete(len - separator.length(), len);
	}

	public static <T> String joinList(String prefix, List<T> list, String separator, String suffix) {
		int N;
		if (null == list || (N = list.size()) <= 0)
			return null;

		if (N == 1)
			return list.get(0).toString();

		StringBuilder sb = new StringBuilder();

		sb.append(prefix);

		joinList(sb, list, separator);

		sb.append(suffix);

		return sb.toString();
	}

	public static <T> String joinList(String prefix, List<T> list, char separator, String suffix) {
		int N;
		if (null == list || (N = list.size()) <= 0)
			return null;

		if (N == 1)
			return list.get(0).toString();

		StringBuilder sb = new StringBuilder();

		sb.append(prefix);

		joinList(sb, list, separator);

		sb.append(suffix);

		return sb.toString();
	}

	public static <T> String joinList(char prefix, List<T> list, char separator, char suffix) {
		int N;
		if (null == list || (N = list.size()) <= 0)
			return null;

		if (N == 1)
			return list.get(0).toString();

		StringBuilder sb = new StringBuilder();

		sb.append(prefix);

		joinList(sb, list, separator);

		sb.append(suffix);

		return sb.toString();
	}

	public static <T> String joinList(List<T> list, String separator) {
		return joinList(null, list, separator);
	}

	public static <T> String joinList(List<T> list, char separator) {
		return joinList(null, list, separator);
	}

	public static <T> String joinList(StringBuilder result, List<T> list, String separator) {
		boolean returnString = false;
		if (null == result) {
			result = new StringBuilder();
			returnString = true;
		}
		doJoin(result, list, separator);
		return returnString ? result.toString() : null;
	}

	public static <T> String joinList(StringBuilder result, List<T> list, char separator) {
		boolean returnString = false;
		if (null == result) {
			result = new StringBuilder();
			returnString = true;
		}
		doJoin(result, list, separator);
		return returnString ? result.toString() : null;
	}

	private static <T> void doJoin(StringBuilder result, List<T> list, char separator) {
		for (Object o : list) {
			result.append(o).append(separator);
		}
		result.deleteCharAt(result.length() - 1);
	}

	private static <T> void doJoin(StringBuilder result, List<T> list, String separator) {
		for (Object o : list) {
			result.append(o).append(separator);
		}
		int len = result.length();
		result.delete(len - separator.length(), len);
	}

	public static String getSystemProperty(String name) {
		try {
			Method m = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
			return (String) m.invoke(null, name);
		} catch (Exception ignored) {
		}
		return null;
	}

	public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				if (false) {
					Log.v(TAG, "Service running: " + service.service);
				}
				return true;
			}
		}
		return false;
	}

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	/**
	 * 获取随机数
	 *
	 * @return 大于0的随机整数
	 */
	public static int getRandomInt() {
		int a = RANDOM.nextInt();
		return a < 0 ? -a : a;
	}

	/**
	 * 获取0~max以内的随机整数
	 *
	 * @param max
	 *            最大值（不含）
	 * @return 随机整数
	 */
	public static int getRandomInt(int max) {
		if (max == 0)
			return 0;
		return getRandomInt() % max;
	}

	/**
	 * 获取0~max的随机
	 *
	 * @param max
	 * @return
	 */
	public static int getAbsRandomInt(int max) {
		return Math.abs(getRandomInt(max));
	}

	/**
	 * 获取一个范围内的随机数
	 *
	 * @param min
	 *            下限（含）
	 * @param max
	 *            上限（不含）
	 * @return 随机整数
	 */
	public static int getRandomInt(int min, int max) {
		if (min == max) {
			return min;
		}
		return min + getRandomInt(max - min);
	}

	/**
	 * 震动一下下咯
	 */
	public static void vibrate(Context context, int time) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(time);
	}

	public static int parseInt(CharSequence charSequence) {
		return parseInt(charSequence.toString(), 0);
	}

	public static int parseInt(CharSequence charSequence, int def) {
		return parseInt(charSequence.toString(), def);
	}

	public static int parseInt(String string) {
		return parseInt(string, 0);
	}

	public static int parseInt(String string, int def) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			return def;
		}
	}

	public static long parseLong(String string) {
		return parseLong(string, 0);
	}

	public static long parseLong(String string, long def) {
		try {
			return Long.parseLong(string);
		} catch (Exception e) {
			return def;
		}
	}

	public static String getTopActivityPackage(Context context) {
		ComponentName cn = getTopActivity(context);
		return null != cn ? cn.getPackageName() : null;
	}

	public static ComponentName getTopActivity(Context context) {
		return getRunningActivity(context, 0);
	}

	public static String getShortStringOfTopActivity(Context context) {
		return getRunningActivity(context, 0).flattenToShortString();
	}

	public static String getShortStringOfRunningActivity(Context context, int index) {
		return getRunningActivity(context, index).flattenToShortString();
	}

	public static ComponentName getRunningActivity(Context context, int index) {
		List<ActivityManager.RunningTaskInfo> tasks;
		try {
			tasks = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1 + index);
		} catch (Exception ignored) {
			tasks = null;
		}
		if (null == tasks || index >= tasks.size()) {
			return new ComponentName("", "");
		}

		ActivityManager.RunningTaskInfo info = tasks.get(index);
		if (null == info)
			return null;

		if (DEBUG) {
			Log.d(TAG, "Top activity: " + info.topActivity.toShortString());
		}

		return info.topActivity;
	}

	public static boolean isSelfShowing(Context context) {
		try {
			return getRunningActivity(context, 0).getPackageName().equals(context.getPackageName());
		} catch (Exception ignored) {
		}
		return false;
	}

	public static boolean isRunningOnTop(Context context, ComponentName component) {
		ComponentName topComponent = getTopActivity(context);
		return null != topComponent && topComponent.equals(component);
	}

	public static boolean isRunningOnTop(Context context, String packageName, String className) {
		ComponentName topComponent = getTopActivity(context);
		return null != topComponent && null != packageName && null != className
				&& packageName.equals(topComponent.getPackageName()) && className.equals(topComponent.getClassName());
	}

	public static String getIntentHandlerPackage(Context context, Intent intent) {
		try {
			return context.getPackageManager().resolveActivity(intent, 0).activityInfo.packageName;
		} catch (Throwable ignored) {
		}
		return null;
	}

	public static Intent intentForCategoryHome() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN)
				// .addCategory(Intent.CATEGORY_DEFAULT)
				.addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	/**
	 * @deprecated Please use {@link #intentForCategoryHome()}
	 */
	public static Intent createHomeIntent() {
		return intentForCategoryHome();
	}

	public static String getDefaultHomePackageName(Context context) {
		try {
			return context.getPackageManager().resolveActivity(createHomeIntent(), 0).activityInfo.packageName;
		} catch (Throwable ignored) {
		}
		return null;
	}

	public static Intent intentForCategoryHome(String packageName, String className) {
		return intentForCategoryHome().setClassName(packageName, className);
	}

	/**
	 * @deprecated Please use {@link #intentForCategoryHome(String, String)}
	 */
	public static Intent createHomeIntent(String packageName, String className) {
		return intentForCategoryHome(packageName, className);
	}

	public static List<ResolveInfo> getRecentApps(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> list = new ArrayList<ResolveInfo>();
		List<ActivityManager.RecentTaskInfo> re = am.getRecentTasks(64, ActivityManager.RECENT_WITH_EXCLUDED);
		if (null != re && re.size() > 0) {
			ResolveInfo resolveInfo;
			for (ActivityManager.RecentTaskInfo r : re) {
				Intent intent = r.baseIntent;
				resolveInfo = pm.resolveActivity(intent, 0);
				list.add(resolveInfo);
			}
		}
		return list;
	}

	public static boolean packageExists(Context context, String packageName) {
		return null != getPackageInfo(context, packageName);
	}

	public static PackageInfo getPackageInfo(Context context) {
		return getPackageInfo(context, context.getPackageName());
	}

	public static PackageInfo getPackageInfo(Context context, String packageName) {
		try {
			return context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (Exception ex) {

		}
		return null;
	}

	public static PackageInfo getPackageInfo(PackageManager pm, String packageName) {
		try {
			return pm.getPackageInfo(packageName, 0);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * queryIntentActivities通过解析所有应用程序中含有如下Intent-filter的App
	 * <intent-filter> <action android:name="android.intent.action.MAIN" />
	 * <category android:name="android.intent.category.LAUNCHER" />
	 * </intent-filter>
	 */
	public static List<ResolveInfo> getLauncherApps(Context context) {
		return context.getPackageManager()
				.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
	}

	public static List<String> getLauncherPackages(Context context) {
		List<String> list = new ArrayList<String>();
		List<ResolveInfo> infos = getLauncherApps(context);
		for (ResolveInfo info : infos) {
			list.add(info.activityInfo.packageName.trim());
		}
		return list;
	}

	/**
	 * 查询所有已经安装的应用程序
	 */
	public static List<ApplicationInfo> getInstalledApps(Context context) {
		return getInstalledApps(context, true);
	}

	public static List<ApplicationInfo> getInstalledApps(Context context, boolean sortByName) {
		try {
			PackageManager pm = context.getPackageManager();
			List<ApplicationInfo> apps = pm.getInstalledApplications(0);
			if (sortByName) {
				Collections.sort(apps, new ApplicationInfo.DisplayNameComparator(pm));
			}
			return apps;

		} catch (Throwable e) {
			Trace.e(TAG, "getInstalledApps failed", e);
			return null;
		}
	}

	public static boolean isThirdApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
			// 非系统程序
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			// 本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
			return true;
		}
		return false;
	}

	/**
	 * App是否存在且可被launch
	 */
	public static boolean appExists(Context context, String packageName) {
		return !TextUtils.isEmpty(packageName)
				&& null != context.getPackageManager().getLaunchIntentForPackage(packageName);
	}

	/**
	 * 判断包名所对应的应用是否安装在SD卡上
	 * 
	 * @param packageName
	 * @return true if install on SD card
	 */
	public static boolean isAppOnSDCard(Context context, String packageName) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
			return ((ai.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0);
		} catch (Exception e) {
			Trace.w(TAG, "Could not determine if app " + packageName + " is installed on SDCard", e);
		}
		return false;
	}

	/**
	 * 判断Intent是否能有相应的应用处理
	 */
	public static boolean intentHandlerExists(Context context, Intent intent) {
		return context.getPackageManager().queryIntentActivities(intent, 0).size() > 0;
	}

	/**
	 * 获取APP name
	 */
	public static String getAppName(Context context) {
		return getAppName(context, getPackageInfo(context));
	}

	public static String getAppName(Context context, PackageInfo info) {
		return info.applicationInfo.loadLabel(context.getPackageManager()).toString();
	}

	public static String getAppName(Context context, ResolveInfo info) {
		PackageManager pm = context.getPackageManager();
		return info.loadLabel(pm).toString();
	}

	public static String getAppName(Context context, String packageName, String className) {
		PackageManager pm = context.getPackageManager();
		String name = null;
		if (TextUtils.isEmpty(className)) {
			ApplicationInfo ai;
			try {
				ai = pm.getApplicationInfo(packageName, 0);
				if (ai != null) {
					name = ai.loadLabel(pm).toString();
					if (packageName.equals(name)) {
						Intent intent = new Intent(Intent.ACTION_MAIN, null);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						intent.setPackage(packageName);
						List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
						if (apps != null && apps.size() > 0) {
							ResolveInfo ri = apps.iterator().next();
							if (ri != null) {
								name = ri.loadLabel(pm).toString();
							}
						}
					}
				}
			} catch (PackageManager.NameNotFoundException ignored) {
			}
			return name;
		} else {
			ComponentName componentName = new ComponentName(packageName, className);
			return getAppName(pm, componentName);
		}
	}

	public static String getAppName(PackageManager pm, ComponentName componentName) {
		String name;
		try {
			name = pm.getActivityInfo(componentName, 0).loadLabel(pm).toString();
		} catch (Throwable e) {
			Trace.w(TAG, "Could not get app Name", e);
			name = componentName.getPackageName();
		}
		return name;
	}

	public static String getAppName(Context context, ComponentName componentName) {
		PackageManager pm = context.getPackageManager();
		return getAppName(pm, componentName);
	}

	public static Drawable getAppIcon(Context context, ResolveInfo info) {
		return info.loadIcon(context.getPackageManager());
	}

	public static Drawable getAppIcon(Context context, String packageName, String className) {
		PackageManager pm = context.getPackageManager();
		Drawable icon = null;
		try {
			if (TextUtils.isEmpty(className) || className.equals("null")) {
				icon = pm.getApplicationIcon(packageName);
			} else {
				ComponentName cn = new ComponentName(packageName, className);
				icon = pm.getActivityInfo(cn, ActivityInfo.FLAG_STATE_NOT_NEEDED).loadIcon(pm);
			}

		} catch (Throwable e) {
			Trace.w(TAG, "Could not get app icon", e);
		}
		return icon;
	}

	/**
	 * 获取APP的icon
	 */
	public static Drawable getAppIcon(Context context, PackageInfo info) {
		return info.applicationInfo.loadIcon(context.getPackageManager());
	}

	// /**
	// * 获取APP的icon
	// */
	// public static Bitmap getAppIconBitmap(Context context, PackageInfo info)
	// {
	// Drawable icon = getAppIcon(context, info);
	// return null != icon ? BitmapUtils.openDrawable(icon) : null;
	// }
	//
	// public static Bitmap getAppIconBitmap(Context context, String
	// packageName, String className) {
	// Drawable icon = getAppIcon(context, packageName, className);
	// return null != icon ? BitmapUtils.openDrawable(icon) : null;
	// }

	/**
	 * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
	 * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
	 *
	 * @param packageName
	 *            应用程序的包名
	 * @throws android.content.ActivityNotFoundException
	 */
	public static void showInstalledAppDetails(Context context, String packageName) throws ActivityNotFoundException {
		Intent intent = new Intent();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts("package", packageName, null);
			intent.setData(uri);
		} else {
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同
			String pkgName = (Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO ? "pkg"
					: "com.android.settings.ApplicationPkgName");
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			intent.putExtra(pkgName, packageName);
		}
		try {
			context.startActivity(intent);
		} catch (Throwable e) {
			throw new ActivityNotFoundException("installedAppDetails");
		}
	}

	public static Parcel parcelFromString(String string) {
		byte[] bytes = string.getBytes();
		Parcel parcel = Parcel.obtain();
		parcel.unmarshall(bytes, 0, bytes.length);
		parcel.setDataPosition(0);
		return parcel;
	}

	public static String parcelToString(Parcelable object) {
		Parcel parcel = Parcel.obtain();
		object.writeToParcel(parcel, 0);
		String result = new String(parcel.marshall());
		parcel.recycle();
		return result;
	}

	public static String getProcessNameByPid(Context context, int pid) {
		try {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
			if (runningApps == null) {
				return null;
			}
			for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
				if (procInfo.pid == pid) {
					return procInfo.processName;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getProcessName() {
		BufferedInputStream bis = null;
		byte[] tmpBuffer = new byte[256];
		try {
			bis = new BufferedInputStream(new FileInputStream("/proc/self/cmdline"));
			// noinspection ResultOfMethodCallIgnored
			bis.read(tmpBuffer, 0, 255);
		} catch (Exception ignored) {
		} finally {
			if (null != bis) {
				try {
					bis.close();
				} catch (Exception ignored) {
				}
			}
		}

		int i;
		for (i = 0; i < 256; ++i) {
			if (tmpBuffer[i] == 0)
				break;
		}
		byte[] buffer = new byte[i];
		System.arraycopy(tmpBuffer, 0, buffer, 0, i);

		return new String(buffer);
		/*
		 * try { return new java.util.Scanner(new FileInputStream(
		 * "/proc/self/cmdline"), "UTF-8").useDelimiter("\\A").next(); } catch
		 * (Exception ignored) { } return null;
		 */
	}

	/**
	 * 屏幕宽度
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	// /**
	// * 屏幕高度不带通知栏
	// */
	// public static int getScreenHeight(Context context) {
	// return getScreenHeight(context, false);
	// }

	// /**
	// * 屏幕高度
	// *
	// * @param fullscreen 是否计算上状态栏高度
	// */
	// public static int getScreenHeight(Context context, boolean fullscreen) {
	// return context.getResources().getDisplayMetrics().heightPixels
	// - (!fullscreen ? SystemBarsHelper.getStatusBarHeight(context) : 0);
	// }

	public static void startSettingsActivity(Context context, String action) {
		Intent intent = new Intent(action);
		startSettingsActivity(context, intent);
	}

	public static void startSettingsActivity(Context context, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			context.startActivity(intent);
		} catch (Throwable e) {
			Trace.w(TAG, "Could not launch Settings " + intent, e);
		}
	}

	public static boolean isAccelerometerRotationOn(Context context) {
		return 0 != Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
	}

	public static boolean setAccelerometerRotation(Context context, boolean on) {
		return Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, on ? 1 : 0);
	}

	public static boolean isGpsLocationOn(Context context) {
		return isGpsLocationOn((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
	}

	public static boolean isGpsLocationOn(LocationManager lm) {
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static boolean isNetworkLocationOn(LocationManager lm) {
		return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * 判断是否开启了自动亮度调节
	 */
	public static boolean isScreenBrightnessModeAuto(Context context) {
		return Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC == Settings.System.getInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}

	/**
	 * 开启自动调节功能
	 */
	public static boolean setScreenBrightnessMode(Context context, boolean auto) {
		return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, auto
				? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 * 获取屏幕的亮度
	 */
	public static int getScreenBrightness(Context context) {
		return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
	}

	/**
	 * 保存亮度设置状态
	 */
	public static boolean setScreenBrightness(Context context, int value) {
		return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
	}

	public static void setComponentEnabled(Context context, String className, boolean enabled) {
		setComponentEnabled(context, context.getPackageName(), className, enabled);
	}

	public static void setComponentEnabled(Context context, String packageName, String className, boolean enabled) {
		PackageManager pm = context.getPackageManager();
		setComponentEnabled(pm, packageName, className, enabled);
	}

	public static void setComponentEnabled(PackageManager pm, String packageName, String className, boolean enabled) {
		setComponentEnabled(pm, new ComponentName(packageName, className), enabled);
	}

	public static void setComponentEnabled(Context context, ComponentName componentName, boolean enabled) {
		setComponentEnabled(context.getPackageManager(), componentName, enabled);
	}

	public static void setComponentEnabled(PackageManager pm, ComponentName componentName, boolean enabled) {
		pm.setComponentEnabledSetting(componentName,
				enabled ? COMPONENT_ENABLED_STATE_ENABLED : COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
	}

	public static final String PACKAGE_NAME_ANDROID = "android";
	public static final String CLASS_NAME_RESOLVER_ACTIVITY = "com.android.internal.app.ResolverActivity";

	public static ComponentName nameOfAndroidResolverActivity() {
		return new ComponentName(PACKAGE_NAME_ANDROID, CLASS_NAME_RESOLVER_ACTIVITY);
	}

	public static String getNewStrByNumber(long number, String originalStr) {
		String numberstr = String.valueOf(number);
		BigInteger srcb = new BigInteger(numberstr);// 转换为BigInteger类型
		String binaryStr = srcb.toString(2);// 转换为2进制并输出结果
		String getIsOneAllPositionStr = getIsOneAllPositionStr(binaryStr.toCharArray());
		String convertNewStr = getNewStrFromOriginalstrByOnepostion(getIsOneAllPositionStr, originalStr);
		return convertNewStr;
	}

	/**
	 * 根据1的下标,从原始字符串得到新的字符串
	 */
	private static String getNewStrFromOriginalstrByOnepostion(String onePosition, String originalStr) {
		char[] originalCharArray = originalStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		String[] tempArray = onePosition.split("-");
		for (int i = 0; i < tempArray.length; i++) {
			String temp = tempArray[i];
			if (temp != null && !(temp.trim().equals(""))) {
				sb.append(originalCharArray[Integer.parseInt(temp)]);
			}
		}
		return sb.toString();
	}

	/**
	 * 得到是1的字符串在char bytes位置,中间以-分开
	 */
	private static String getIsOneAllPositionStr(char[] charBytes) {
		StringBuffer sbIsOnePosition = new StringBuffer();
		for (int i = 0; i < charBytes.length; i++) {
			char c = charBytes[i];
			switch (c) {
			case '1':
				sbIsOnePosition.append(i).append("-");
				break;
			}
		}
		return sbIsOnePosition.toString();
	}

	public static String calculateMD5(String key) {
		return calculateMD5(key, null);
	}

	public static String calculateMD5(String key, String def) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = Bytes.toHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = def;
		}
		return cacheKey;
	}

	public static String calculateMD5(File file) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			Trace.e(TAG, "Exception while getting MD5 digest", e);
			return null;
		}

		InputStream is;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Trace.e(TAG, "Exception while getting FileInputStream " + file, e);
			return null;
		}

		byte[] buffer = new byte[8192];
		int read;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			return Bytes.toHexString(digest.digest());
		} catch (IOException e) {
			Trace.e(TAG, "Unable to process file for MD5", e);
			return null;
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	public static Intent intentForActionView(String uriString) {
		return intentForActionView(uriString, null);
	}

	public static Intent intentForActionView(String uriString, String type) {
		return intentForActionView(Uri.parse(uriString), type);
	}

	public static Intent intentForActionView(Uri uri) {
		return intentForActionView(uri, null);
	}

	public static Intent intentForActionView(Uri uri, String type) {
		return new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setDataAndType(uri, type);
	}

	/**
	 * 安装某个文件
	 */
	public static void installApk(Context context, File file) {
		view(context, Uri.fromFile(file), "application/vnd.android.package-archive");
	}

	public static void installApk(Context context, Uri uri) {
		view(context, uri, "application/vnd.android.package-archive");
	}

	public static boolean view(Context context, String uriString) {
		return view(context, Uri.parse(uriString));
	}

	public static boolean view(Context context, Uri uri) {
		return view(context, uri, null);
	}

	public static boolean view(Context context, Uri uri, String mimeType) {
		try {
			context.startActivity(intentForActionView(uri, mimeType));
		} catch (Exception e) {
			Trace.w(TAG, "view failed", e);
			return false;
		}
		return true;
	}

	public static boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	public static boolean startActivity(Context context, Intent intent) {
		try {
			if (!(context instanceof Activity) && 0 == (intent.getFlags() & FLAG_ACTIVITY_NEW_TASK)) {
				intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
			return true;
		} catch (Throwable t) {
			if (DEBUG) {
				Log.w(TAG, "Failed to start activity: " + intent, t);
			}
		}
		return false;
	}

	/**
	 * @return 0 -- ACTION_USAGE_ACCESS_SETTINGS launched successfully 1 --
	 *         ACTION_SECURITY_SETTINGS launched successfully 2 -- Settings
	 *         launched -1 -- No settings activity launched
	 */
	public static int startUsageAccessSettings(Context context) {
		// TutorialService.startUsageAccessCheck(context);

		if (startSystemSettings(context, "android.settings.USAGE_ACCESS_SETTINGS"))
			return 0;

		if (startSystemSettings(context, Settings.ACTION_SECURITY_SETTINGS))
			return 1;

		if (startSystemSettings(context))
			return 2;

		return -1;
	}

	public static boolean startSystemSettings(Context context) {
		return startSystemSettings(context, Settings.ACTION_SETTINGS);
	}

	public static boolean startSystemSettings(Context context, String action) {
		return startActivity(context, getSystemSettingsIntent(action));
	}

	public static Intent getSystemSettingsIntent(String action) {
		return new Intent(action).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	}

}
