package com.aess.aemm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aess.aemm.R;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.protocol.UpdateXmlParser.NotifyResult;
import com.aess.aemm.view.msg.MsgActivity;
import com.aess.aemm.view.sharing.SharingActivity;
import com.aess.aemm.view.sso.SSO;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import com.aess.aemm.view.MainView;

public class NotificationUtils {
	private static Notification notification = null;
	private static NotificationManager manager = null;

	public static void sentNotification(Context context, int strId) {
		if (notification == null) {
			notification = new Notification(R.drawable.icon,
					context.getText(strId), System.currentTimeMillis());
		} else {
			notification.tickerText = context.getText(strId);
		}
		PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, MainView.class), 0);
		notification.setLatestEventInfo(context, "Hi!", context.getText(strId),
				mPendingIntent);
		manager = (NotificationManager) context
				.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
		manager.notify(R.string.app_name, notification);
	}
	public static void sentNotificationForSharing(Context context) {
		if(SharingActivity.isVisiable){
			return;
		}
		if (notification == null) {// 2.实例化一个通知，指定图标、概要、时间
			notification = new Notification(R.drawable.icon,context.getText(R.string.update), System.currentTimeMillis());
		} else {
			notification.tickerText = context.getText(R.string.update);
		}
		// 3.指定通知的标题、内容和intent
		PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,new Intent(context, SharingActivity.class), 0);
		notification.setLatestEventInfo(context, "Hi!", context.getText(R.string.sharing_push),mPendingIntent);
		// 1.得到NotificationManager
		manager = (NotificationManager) context.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
		// 指定声音
		// n.defaults = Notification.DEFAULT_SOUND;
		// 4.发送通知
		manager.notify(R.string.app_name, notification);
	}
	
	public static void sentNotificationForMsg(Context context, int msgCount,
			Uri uri) {
		String msg = context.getString(R.string.msgnum);
		msg = String.format(msg, msgCount);

		if (notification == null) {
			notification = new Notification(R.drawable.icon, msg,
					System.currentTimeMillis());
		} else {
			notification.tickerText = msg;
			notification.when = System.currentTimeMillis();
		}

		PendingIntent mPendingIntent = null;
		if (msgCount > 1) {
			Intent intent = new Intent(context, MsgActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			mPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		} else {
			Intent intent = new Intent("android.intent.action.AEMMMSGDETIAL");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		}

		notification.setLatestEventInfo(context, "Hi!", msg, mPendingIntent);
		manager = (NotificationManager) context
				.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
		manager.notify(R.string.app_name, notification);
	}

	public static int sentNotificationForApp(Context context, String packageName) {
		if (null == packageName) {
			return -1;
		}

		ArrayList<ApkContent> apklist = ApkContent.queryAllContents(context);
		for (ApkContent ac : apklist) {
			if (packageName.equals(ac.mApkPackageName)) {
				if (1 == ac.mApkDisabled) {
					return 0;
				}
			}
		}

		String msg = context.getString(R.string.msgnum);
		msg = String.format(msg, 1);

		Notification appn = new Notification(R.drawable.icon, msg,
				System.currentTimeMillis());
		appn.flags = Notification.FLAG_AUTO_CANCEL;

		Intent queryIntent = new Intent(Intent.ACTION_MAIN, null);
		queryIntent.setPackage(packageName);

		List<ResolveInfo> list = context.getPackageManager()
				.queryIntentActivities(queryIntent, 0);

		PendingIntent pendingIntent = null;
		String packname = null;
		if (list != null && list.size() > 0) {
			Intent startIntent = new Intent();
			packname = list.get(0).activityInfo.applicationInfo.packageName;
			startIntent.setClassName(packname, list.get(0).activityInfo.name);
			startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			pendingIntent = PendingIntent.getActivity(context, 0, startIntent,
					0);
		}
		if (null != pendingIntent) {
			appn.setLatestEventInfo(context, "Hi!", msg, pendingIntent);
			manager = (NotificationManager) context
					.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
			manager.notify(packname.hashCode(), appn);
		}
		return 1;
	}

	public static int sentNotificationForApp2(Context context, int type,
			Object value) {

		PendingIntent pendIntent = null;
		String code = null;
		if (1 == type) {
			String packageName = (String) value;
			if (null == packageName) {
				return -1;
			}
			code = packageName;
			Intent queryIntent = new Intent(Intent.ACTION_MAIN, null);
			queryIntent.setPackage(packageName);

			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(queryIntent, 0);

			if (list != null && list.size() > 0) {
				Intent startIntent = new Intent();
				String pname = list.get(0).activityInfo.applicationInfo.packageName;
				startIntent.setClassName(pname, list.get(0).activityInfo.name);
				startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				pendIntent = PendingIntent.getActivity(context, 0, startIntent,
						0);
			}
		} else if (2 == type) {
			code = SSO.FRIEND;
			@SuppressWarnings("unchecked")
			ArrayList<Map<String, String>> msgList = (ArrayList<Map<String, String>>) value;
			if (null == msgList) {
				return -1;
			}
			Intent startIntent = new Intent(
					"com.ccssoft.cn.intent.announcement");
			startIntent.putExtra("contentList", msgList);
			startIntent.addCategory(Intent.CATEGORY_DEFAULT);
			pendIntent = PendingIntent.getActivity(context, 0, startIntent, 0);
		} else {
			return -3;
		}

		String msg = context.getString(R.string.msgnum);
		msg = String.format(msg, 1);

		Notification appn = new Notification(R.drawable.icon, msg,
				System.currentTimeMillis());
		appn.flags = Notification.FLAG_AUTO_CANCEL;

		if (null != pendIntent) {
			appn.setLatestEventInfo(context, "Hi!", msg, pendIntent);
			manager = (NotificationManager) context
					.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
			manager.notify(code.hashCode(), appn);
		}
		return 1;
	}

	public static void sentNotificationForNotity(Context context,
			NotifyResult nr) {
		if (null == nr) {
			return;
		}
		String msg = nr.args.get("message");
		if (null == msg || msg.length() < 1) {
			msg = context.getString(R.string.msgnum);
			msg = String.format(msg, 1);
		}

		String activename = nr.args.get("acctivityName");
		String user = nr.args.get("users");
		String ps = nr.args.get("password");

		// activename = "com.ccssoft.business.bill.BillMainTabHostActivity";
		// activename = "business.bill.BillListMainActivity";
		// user = "gzadmin";
		// ps = "mss_pwd_666";
		// msg = "您有一张新的工单,请及时处理!";

		if (null == activename) {
			return;
		}

		Notification appn = new Notification(R.drawable.icon, msg,
				System.currentTimeMillis());

		appn.flags = Notification.FLAG_AUTO_CANCEL;

		final String action = "com.ccssoft.subsystem.Login";

		Intent startIntent = new Intent(action);
		startIntent.addCategory(Intent.CATEGORY_DEFAULT);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startIntent.putExtra("loginName", user);
		startIntent.putExtra("password", ps);
		startIntent.putExtra("type", activename);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				startIntent, 0);

		appn.setLatestEventInfo(context, "Hi!", msg, pendingIntent);
		manager = (NotificationManager) context
				.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
		manager.notify(activename.hashCode(), appn);
	}

	public static void sentNotificationForNotityTest(Context context) {

		// String activename =
		// "com.ccssoft.business.bill.BillMainTabHostActivity";

		String activename = "com.ccssoft.business.bill.BillListMainActivity";
		String user = "gzadmin";
		String ps = "mss_pwd_666";
		String msg = "您有一张新的工单,请及时处理!";

		Notification appn = new Notification(R.drawable.icon, msg,
				System.currentTimeMillis());

		appn.flags = Notification.FLAG_AUTO_CANCEL;
		String action = "com.ccssoft.subsystem.Login";
		Intent startIntent = new Intent(action);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startIntent.addCategory(Intent.CATEGORY_DEFAULT);
		// startIntent.putExtra("users", user);
		// startIntent.putExtra("password", ps);
		// startIntent.putExtra("ccssoft.activityName", activename);

		startIntent.putExtra("loginName", user);
		startIntent.putExtra("password", ps);
		startIntent.putExtra("type", activename);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				startIntent, 0);

		appn.setLatestEventInfo(context, "Hi!", msg, pendingIntent);
		manager = (NotificationManager) context
				.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
		manager.notify(activename.hashCode(), appn);
	}

	public static void cancelNotification(Context context) {
		manager = (NotificationManager) context
				.getSystemService(android.app.Activity.NOTIFICATION_SERVICE);
		manager.cancel(R.string.app_name);
	}

}
