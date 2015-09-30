package wu.a.lib.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * 获取手机应用信息
 * 
 * @author junxu.wang
 *
 */
public class PackageManagerUtils {

	public static PackageManager getPackageManager(Context context) {
		return context.getPackageManager();
	}

	/**
	 * 查询手机内所有应用包括系统应用
	 * 
	 * @param context
	 */
	public static List<PackageInfo> getAllApps(PackageManager pManager) {
		// 获取手机内所有应用
		return pManager.getInstalledPackages(0);
	}

	/**
	 * 查询手机内非系统应用
	 * 
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAllAppsNoSystem(PackageManager pManager) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		// 获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			// 判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}

	/**
	 * 查询手机内所有支持分享的应用
	 * 
	 * @param context
	 * @return
	 */
	public static List<ResolveInfo> getShareApps(PackageManager pManager) {
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		return pManager.queryIntentActivities(intent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
	}

	/*
	 * 备注： 通过 PackageInfo 获取具体信息方法： 包名获取方法：packageInfo.packageName
	 * icon获取获取方法：packageManager.getApplicationIcon(applicationInfo)
	 * 应用名称获取方法：packageManager.getApplicationLabel(applicationInfo)
	 * 使用权限获取方法：packageManager
	 * .getPackageInfo(packageName,PackageManager.GET_PERMISSIONS
	 * ).requestedPermissions 通过 ResolveInfo 获取具体信息方法：
	 * 包名获取方法：resolve.activityInfo.packageName
	 * icon获取获取方法：resolve.loadIcon(packageManager)
	 * 应用名称获取方法：resolve.loadLabel(packageManager).toString()
	 */

	public static Drawable getIcon(PackageManager pManager, String packageName) {
		Drawable iconDra = null;
		try {
			iconDra = pManager.getApplicationIcon(packageName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iconDra;
	}

	public static Drawable getIcon(PackageManager pManager, ApplicationInfo info) {
		Drawable iconDra = pManager.getApplicationIcon(info);
		return iconDra;
	}

	public static Drawable getIcon(PackageManager pManager,
			ResolveInfo resolveInfo) {
		Drawable iconDra = resolveInfo.loadIcon(pManager);
		return iconDra;
	}
}
