package wu.a.lib.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wu.a.lib.utils.Logger;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Debug;

public class ActivityManagerUtils {
	// private ActivityManager mActivityManager = null;

	/**
	 * <pre>
	 * 获得ActivityManager服务的对象
	 * @param context
	 * @return
	 * </pre>
	 */
	public static ActivityManager getActivityManager(Context context) {
		return (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/**
	 * <pre>
	 * 获得系统进程信息
	 * @param am
	 * @return
	 * </pre>
	 */
	public static ArrayList<ProcessInfo> getRunningAppProcessInfo(
			ActivityManager am) {
		// ProcessInfo Model类 用来保存所有进程信息
		ArrayList processInfoList = new ArrayList<ProcessInfo>();

		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = am
				.getRunningAppProcesses();

		// 获得该进程占用的内存
		int[] myMempid = new int[1];
		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
			// 进程ID号
			int pid = appProcessInfo.pid;
			// 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
			int uid = appProcessInfo.uid;
			// 进程名，默认是包名或者由属性android：process=""指定
			String processName = appProcessInfo.processName;
			// 获得该进程占用的内存
			myMempid[0] = pid;
			// 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
			Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
			// 获取进程占内存用信息 kb单位
			int memSize = memoryInfo[0].dalvikPrivateDirty;
			// 构造一个ProcessInfo对象
			ProcessInfo processInfo = new ProcessInfo();
			processInfo.setPid(pid);
			processInfo.setUid(uid);
			processInfo.setMemSize(memSize);
			processInfo.setPocessName(processName);
			// 保存所有运行在该应用程序的包名
			processInfo.pkgnameList = appProcessInfo.pkgList;
			processInfoList.add(processInfo);

			if (Logger.DEBUG) {
				// 获取进程占内存用信息 kb单位
				Logger.log("processName: " + processName + "  pid: " + pid
						+ " uid:" + uid + " memorySize is -->"
						+ processInfo.getMemSize() + " KB");

				// 获得每个进程里运行的应用程序(包),即每个应用程序的包名
				String[] packageList = appProcessInfo.pkgList;
				Logger.log("process id is " + pid + "has " + packageList.length);
				for (String pkg : packageList) {
					Logger.log("packageName " + pkg + " in process id is -->"
							+ pid);
				}
			}
		}
		return processInfoList;
	}

	/**
	 * <pre>杀死别人进程的方法（不能杀死自己）
	 * 杀死进程
	 * 需要权限：android.permission.KILL_BACKGROUND_PROCESSES
	 * <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"></uses-permission>
	 * @param am
	 * @param packageName
	 * </pre>
	 */
	public static void killBackgroundProcesses(ActivityManager am,
			String packageName) {
		am.killBackgroundProcesses(packageName);
		// am.restartPackage(packageName);
	}

	/**
	 * <pre>
	 * 1： 杀死自己进程的方法
	 * android.os.Process.killProcess(Process.myPid());
	 * </pre>
	 */
	public static void killProcess(int pid) {
		android.os.Process.killProcess(pid);
	}

	/**
	 * <pre>杀死别人进程的方法（不能杀死自己）
	 * 需要加入权限< uses-permission android:name="android.permission.RESTART_PACKAGES"/>
	 *  @param am
	 *  @param packageName
	 * </pre>
	 * @deprecated
	 */
	public static void restartPackage(ActivityManager am, String packageName) {
		am.restartPackage(packageName);
	}

	/**
	 * <pre>
	 * 查询所有正在运行的应用程序信息： 包括他们所在的进程id和进程名
	 * 这儿我直接获取了系统里安装的所有应用程序，然后根据报名pkgname过滤获取所有真正运行的应用程序
	 * @param pm
	 * @param am
	 * @return
	 * </pre>
	 */
	public static List<RunningAppInfo> queryAllRunningAppInfo(
			PackageManager pm, ActivityManager am) {
		// 查询所有已经安装的应用程序
		List<ApplicationInfo> listAppcations = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(listAppcations,
				new ApplicationInfo.DisplayNameComparator(pm));// 排序

		// 保存所有正在运行的包名 以及它所在的进程信息
		Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = am
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
			int pid = appProcess.pid; // pid
			String processName = appProcess.processName; // 进程名
			if (Logger.DEBUG) {
				Logger.log("processName: " + processName + "  pid: " + pid);
			}

			String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包

			// 输出所有应用程序的包名
			for (int i = 0; i < pkgNameList.length; i++) {
				String pkgName = pkgNameList[i];
				if (Logger.DEBUG) {
					Logger.log("packageName " + pkgName + " at index " + i
							+ " in process " + pid);
				}
				// 加入至map对象里
				pgkProcessAppMap.put(pkgName, appProcess);
			}
		}
		// 保存所有正在运行的应用程序信息
		List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // 保存过滤查到的AppInfo

		for (ApplicationInfo app : listAppcations) {
			// 如果该包名存在 则构造一个RunningAppInfo对象
			if (pgkProcessAppMap.containsKey(app.packageName)) {
				// 获得该packageName的 pid 和 processName
				int pid = pgkProcessAppMap.get(app.packageName).pid;
				String processName = pgkProcessAppMap.get(app.packageName).processName;
				runningAppInfos.add(getAppInfo(pm, app, pid, processName));
			}
		}

		return runningAppInfos;

	}

	/**
	 * <pre>
	 * 某一特定经常里所有正在运行的应用程序
	 * @param pm
	 * @param intent
	 * @param pid
	 * @return
	 * </pre>
	 */
	public static List<RunningAppInfo> querySpecailPIDRunningAppInfo(
			PackageManager pm, String[] pkgNameList, String processName, int pid) {

		// 保存所有正在运行的应用程序信息
		List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // 保存过滤查到的AppInfo

		ApplicationInfo appInfo;
		for (int i = 0; i < pkgNameList.length; i++) {
			try {
				// 根据包名查询特定的ApplicationInfo对象
				appInfo = pm.getApplicationInfo(pkgNameList[i], 0);
				runningAppInfos.add(getAppInfo(pm, appInfo, pid, processName));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} // 0代表没有任何标记;
		}
		return runningAppInfos;
	}

	/**
	 * <pre>
	 * 用进程和app构造一个RunningAppInfo对象 ，并赋值
	 * @param pm
	 * @param app
	 * @param pid
	 * @param processName
	 * @return
	 * </pre>
	 */
	public static RunningAppInfo getAppInfo(PackageManager pm,
			ApplicationInfo app, int pid, String processName) {
		RunningAppInfo appInfo = new RunningAppInfo();
		appInfo.setAppLabel((String) app.loadLabel(pm));
		appInfo.setAppIcon(app.loadIcon(pm));
		appInfo.setPkgName(app.packageName);

		appInfo.setPid(pid);
		appInfo.setProcessName(processName);

		return appInfo;
	}

	/**
	 * <pre>
	 * 获得系统可用内存信息
	 * @param am
	 * @return
	 * </pre>
	 */
	public static long getSystemAvaialbeMemorySize(ActivityManager am) {
		// 获得MemoryInfo对象
		MemoryInfo memoryInfo = new MemoryInfo();
		// 获得系统可用内存，保存在MemoryInfo对象上
		am.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
		// 字符类型转换
		// Formatter.formatShortFileSize(MainActivity.this, memSize);
	}

}
