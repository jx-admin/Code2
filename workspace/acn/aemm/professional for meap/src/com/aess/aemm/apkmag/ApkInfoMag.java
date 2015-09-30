package com.aess.aemm.apkmag;

import java.io.InputStream;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.receiver.AlarmReceiver;
import com.aess.aemm.traffic.AemmTraffic;

public class ApkInfoMag {
	public static final String TAG = "ApkInfoMag";
	public static final String DEBUGFILENAME = "Apk.xml";
	
	public static void sendIntentForApk(Context cxt) {
		Intent app = new Intent(AlarmReceiver.APK);
		app.putExtra(CommUtils.TYPE, DomXmlBuilder.APK);
		cxt.sendBroadcast(app);
	}
	
	public static void sendIntentForApkUnlimit(Context cxt) {
		Intent app = new Intent(AlarmReceiver.APK);
		app.putExtra(CommUtils.TYPE, DomXmlBuilder.APK);
		app.putExtra(AlarmReceiver.UNLIMIT, 1);
		cxt.sendBroadcast(app);
	}
	
	public static void sendIntentForApkEnt(Context cxt) {
		Intent intent = new Intent(AlarmReceiver.APK);
		intent.putExtra(CommUtils.TYPE, DomXmlBuilder.APKENT);
		cxt.sendBroadcast(intent);
	}
	
	public static void postApk(final Context cxt, final int type) {
		Thread postApk = new Thread(new Runnable() {
			public void run() {
				if (0 == AemmTraffic.isWork()) {
					AemmTraffic.getInstance(cxt).run();
				}
				ApkInfoMag.postApkPskInfo(cxt, type);
			}
		});
		postApk.setName("postApk");
		postApk.start();
	}
	
	public static void cancelApkTimer(Context cxt) {
		Log.i(TAG, "cancelApkTimer");

		AlarmManager alarmManager = (AlarmManager) cxt
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(cxt, AlarmReceiver.class);
		intent.setAction(AlarmReceiver.APK);
		intent.putExtra(CommUtils.TYPE, DomXmlBuilder.APK);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, 0,
				intent, 0);
		alarmManager.cancel(pendingIntent);
	}

	public static void setApkTimer(Context cxt, long time) {
		Log.i(TAG, String.format("setApkTimer : %s", String.valueOf(time)));
		if (time < 1) {
			return;
		}

		AlarmManager alarmManager = (AlarmManager) cxt
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(cxt, AlarmReceiver.class);
		intent.setAction(AlarmReceiver.APK);
		intent.putExtra(CommUtils.TYPE, DomXmlBuilder.APK);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, 0,
				intent, 0);

		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ time, pendingIntent);
	}
	
	private static int postApkPskInfo(Context cxt, int type) {
		if (null == cxt) {
			return -1;
		}
		Log.d(TAG, "Report Apk Info");
		
		if (false == NetUtils.isNetOK(cxt)) {
			return -1;
		}
		
		if (DomXmlBuilder.APKENT != type) {
			cancelApkTimer(cxt);
		}
		
		int ret = 1;
		
		AutoAdress address = AutoAdress.getInstance(cxt);
		String url = address.getUpdateURL();
		if (null != url) {
			InputStream is = null;
			String ApkInfo = DomXmlBuilder.buildInfo(cxt, false, type, null);
			is = HttpHelp.aemmHttpPost(cxt, url, ApkInfo, DEBUGFILENAME);
			if (null == is) {
				ret = -1;
			}
		} else {
			Log.d(TAG, "Location report, not have Updata URL");
		}
		
		if (DomXmlBuilder.APKENT != type) {
			long timelong = CommUtils.getAppTime(cxt);
			setApkTimer(cxt, timelong);
		}
		return ret;
	}
}
