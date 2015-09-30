package wu.a.template.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wu.a.lib.app.PackageManagerUtils;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class PackageInfoProvider {
	private static final String tag = "GetappinfoActivity";

	private static Map<String, String> USED_ICON = new HashMap<String, String>();
	private Context context;
	private List<AppInfo> appInfos;
	private AppInfo appInfo;
	PackageManager pm;

	public PackageInfoProvider(Context context) {
		super();
		this.context = context;
		pm = context.getPackageManager();
	}

	public List<ResolveInfo> getAppInfo() {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		return pm.queryIntentActivities(mainIntent, 0);
	}
	
	public List<ResolveInfo> getAudioAppInfo(){
		final Intent mainIntent = new Intent(/* Intent.ACTION_MAIN */);
		// mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// mainIntent.addCategory(Intent.CATEGORY_APP_MUSIC);
		mainIntent.setDataAndType(Uri.parse("file://sdcard"), "audio/*");
		// mainIntent.setDataAndType(Uri.parse("http://www.w.www"), "audio/*");
		// mainIntent.setType("audio/*");
//		mainIntent.addCategory(Intent.CATEGORY_DEFAULT);
		return pm.queryIntentActivities(mainIntent, 0);
	}
	
	
	public List<AppInfo> getToAppInfo(List<ResolveInfo>apps){
		appInfos = new ArrayList<AppInfo>();

		for (ResolveInfo resolve : apps) {
			String pkg = resolve.activityInfo.applicationInfo.packageName;
			String cln = resolve.activityInfo.name;
			appInfo = new AppInfo();
			appInfo.setPackageName(pkg);
			appInfo.setClassName(cln);
			if (appInfo.getClassName() == null) {
				continue;
			}
			CharSequence label = resolve.activityInfo.loadLabel(pm);
			if (!TextUtils.isEmpty(label)) {
				appInfo.setAppName(label.toString());
			} else {
				CharSequence appLable = resolve.activityInfo.applicationInfo
						.loadLabel(pm);
				appInfo.setAppName(appLable.toString());
			}
			Log.d("app",appInfo.getAppName()+"\t"+appInfo.getPackageName()+"/\t"+appInfo.getClassName());
			appInfos.add(appInfo);
		}
		// for(ComponentName cn:SHOW_PACKAGENAMES){
		// String pkg = cn.getPackageName();
		// String cln = cn.getClassName();
		// if (HIDDING_PACKAGENAMES.contains(pkg)
		// ||
		// (USED_ICON.containsKey(pkg) && USED_ICON.containsValue(cln))
		// ) {
		// continue;
		// }
		// appInfo = new AppInfo();
		// appInfo.setPackageName(cn.getPackageName());
		// appInfo.setClassName(cn.getClassName());
		// appInfos.add(appInfo);
		// }
		return appInfos;
	}


	public Drawable getAppIcon(String packageName) {
		return PackageManagerUtils.getIcon(pm, packageName);
	}

	/*
	 * public static Bitmap getBitmapInLauncher(Context context, ComponentName
	 * cn) { // Integer iconId= ICON_FILTER_PACKAGENAMES.get(cn); //
	 * if(iconId!=null){ // return
	 * BitmapFactory.decodeResource(context.getResources(),iconId); // } //
	 * Log.d("ddd", cn.getPackageName() + "   ---------  " + cn.getClassName());
	 * Intent intent = new Intent(Intent.ACTION_MAIN);
	 * intent.addCategory(Intent.CATEGORY_LAUNCHER);
	 * intent.setPackage(cn.getPackageName()); intent.setComponent(cn); //
	 * intent.putExtra("commer", 1);
	 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
	 * Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED); return
	 * IconBlur.current().getBitmap(intent); }
	 */

	public static boolean startApp(Context context, String apkPackageName,
			String className) {
		boolean result = false;
		if (TextUtils.isEmpty(apkPackageName)) {
			return result;
		}
		PackageManager pm = context.getPackageManager();
		// if (!TextUtils.isEmpty(className)) {
		// Intent intent = new Intent();
		// intent.setClassName(apkPackageName, className);
		// List<ResolveInfo> resolveInfo = pm.queryIntentActivities(intent, /*
		// * PackageManager
		// * .
		// * GET_ACTIVITIES
		// */
		// PackageManager.GET_ACTIVITIES);
		// if (resolveInfo != null && resolveInfo.size() > 0) {
		// context.startActivity(intent);
		// result = true;
		// return result;
		// }
		// }
		// start by package
		Intent mLaunchIntent = pm.getLaunchIntentForPackage(apkPackageName);
		if (mLaunchIntent != null) {
			List<ResolveInfo> list = pm.queryIntentActivities(mLaunchIntent, 0);
			if (list != null && list.size() > 0) {
				context.startActivity(mLaunchIntent);
				result = true;
				return result;
			}
		}

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.setPackage(apkPackageName);

		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
		if (list != null && list.size() > 0) {
			Intent intent = new Intent();
			String packname = list.get(0).activityInfo.applicationInfo.packageName;
			intent.setClassName(packname, list.get(0).activityInfo.name);
			context.startActivity(intent);
			result = true;
			return result;
		}
		// Toast.makeText(context, "无法启动", Toast.LENGTH_SHORT).show();
		return result;
	}

	/*
	 * 
	 * Utility method to get application information for a given packageURI
	 */

	/*
	 * public ApplicationInfo getApplicationInfo(Uri packageURI) {
	 * 
	 * final String archiveFilePath = packageURI.getPath();
	 * 
	 * PackageParser packageParser = new PackageParser(archiveFilePath);
	 * 
	 * File sourceFile = new File(archiveFilePath);
	 * 
	 * DisplayMetrics metrics = new DisplayMetrics();
	 * 
	 * metrics.setToDefaults();
	 * 
	 * PackageParser.Package pkg = packageParser.parsePackage(sourceFile,
	 * archiveFilePath, metrics, 0);
	 * 
	 * if (pkg == null) {
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * return pkg.applicationInfo;
	 * 
	 * }
	 */

}
