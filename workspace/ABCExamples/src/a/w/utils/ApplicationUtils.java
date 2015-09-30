package a.w.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * @author Administrator 在Android里，app和process和task是三个不同的概念.
 * 
 *         process是一个继承自Linux的概念，一般一个app会有一个uid，一般会独立地开一个process。
 * 
 *         但是，也会有多个app共享一个process或者uid的，这个可以自己指定。
 * 
 *         task是一个activity的栈，其中"可能"含有来自多个App（不一定在同一process中）中的activity。
 * 
 *         ActivityManager类可以获取运行信息，如下：
 * 
 *         1.getRecentTasks() 最近开的task，HOME键长按会看到这个 2.getRunningAppProcesses()
 *         运行中的作为app容器的process 3.getRunningServices() 运行中的后台服务
 *         4.getRunningTasks() 运行中的任务 如果一个Activity “mainActivity”不是作为task的root
 *         activity打开的
 *         ，而是被别的Task中Activity调用，那么mainActivity对应的process是开着的，这时我们是否要显示它呢？
 * 
 *         另外，如果一个app只有service开着而在Task中不存在这个app的Activity，算不算开着呢？
 */
public class ApplicationUtils {
	/**
	 * 判断当前程序是否在栈顶
	 * 
	 * @param packageName
	 * @param context
	 * @return
	 */
	public static boolean isTopActivy(String packageName, Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();

		return (currentPackageName != null && currentPackageName
				.equals(packageName));
	}

	/**
	 * 判断Android应用是否在前台
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();
		List<RecentTaskInfo> appTask = activityManager.getRecentTasks(
				Integer.MAX_VALUE, 1);

		if (appTask == null) {
			return false;
		}

		if (appTask.get(0).baseIntent.toString().contains(packageName)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取最近运行的程序列表（近期任务），长按home键所示效果
	 * 
	 * @param context
	 * @return
	 */
	public static String getTaskList(Context context) {
		String apps = "";
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		try {
			List<RecentTaskInfo> list = am.getRecentTasks(Integer.MAX_VALUE, 0);
			for (RecentTaskInfo ti : list) {
				Intent intent = ti.baseIntent;
				ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
				if (resolveInfo != null) {
					// resolveInfo.loadIcon(pm);
				}
			}
			return apps;
		} catch (SecurityException se) {
			se.printStackTrace();
			return apps;
		}
	}

	/**
	 * 获取Android手机内安装的所有桌面
	 * 
	 * @param context
	 * @return
	 */
	private static List<String> getAllTheLauncher(Context context) {
		List<String> names = null;
		PackageManager pkgMgt = context.getPackageManager();
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> ra = pkgMgt.queryIntentActivities(it, 0);
		if (ra.size() != 0) {
			names = new ArrayList<String>();
		}
		for (int i = 0; i < ra.size(); i++) {
			String packageName = ra.get(i).activityInfo.packageName;
			names.add(packageName);
		}
		return names;
	}

	/**
	 * Android 判断程序前后台状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isLauncherRunnig(Context context) {
		boolean result = false;
		List<String> names = getAllTheLauncher(context);
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appList = mActivityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo running : appList) {
			if (running.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				for (int i = 0; i < names.size(); i++) {
					if (names.get(i).equals(running.processName)) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	/**
	 * 获取正在运行的程序信息
	 * 
	 * @param context
	 * @return
	 */
	// public static List<Programe> getRunningProcess(Context context){
	// PackagesInfo pi = PackagesInfo.getInstance(context);
	//
	// ActivityManager am = (ActivityManager)
	// context.getSystemService(Context.ACTIVITY_SERVICE);
	// List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
	// PackageManager pm =context.getPackageManager();
	// List<Programe> list = new ArrayList<Programe>();
	// Programe pr = new Programe();
	// try {
	// for(RunningAppProcessInfo ra : run){
	// if(ra.processName.equals("system") ||
	// ra.processName.equals("com.Android.phone")){ //可以根据需要屏蔽掉一些进程
	// continue;
	// }
	// pr = new Programe();
	// pr.setIcon(pi.getInfo(ra.processName).loadIcon(pm));
	// pr.setName(pi.getInfo(ra.processName).loadLabel(pm).toString());
	// Log.v("tag","icon = " + pr.getIcon() +"name=" + pr.getName());
	// list.add(pr);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return list;
	// }
}
