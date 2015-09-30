package com.aess.aemm.receiver;

import java.util.Date;
import java.util.List;
import com.aess.aemm.apkmag.ApkInfoMag;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.gps.AEMMLocation;
import com.aess.aemm.networkutils.SocketClient;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.push.PushService;
import com.aess.aemm.update.Update;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String TAG = "AlarmReceiver";

	public static final String GPS    = "com.aess.aemm.GPS";
	public final static String UPDATE = "com.aess.aemm.UPDATE";
	public final static String APK    = "com.aess.aemm.APK";
	public final static String TIME   = "com.aess.aemm.TIME";
	
	public final static int LOCTIME   = 1;
	public final static int APKTIME   = 2;
	public final static int ALL       = 3;
	
	public static final String UNLIMIT = "unLimit";
	
	public static void SendIntentForTime(Context cxt, int type) {
		Intent time = new Intent(AlarmReceiver.TIME);
		time.putExtra(CommUtils.TYPE, type);
		cxt.sendBroadcast(time);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final Context cxt = context;
		if (null == cxt) {
			return;
		}

		int flimit = SocketClient.isTrafficLimtOver(cxt);
		int slimit = CommUtils.getServiceLimit(cxt);
		if (flimit < 0 || slimit < 0) {
			int lim = intent.getIntExtra(UNLIMIT, 0);
			String action = intent.getAction();
			if (!TIME.equals(action)) {
				if ( lim < 1) {
					String info = cxt
							.getString(com.aess.aemm.R.string.trafficlimitover);

					Update.broadcastMsg(cxt, info, Update.MANUAL);
					Log.w(TAG, intent.getAction() + " Limit");
					return;
				}
			}
		}
		if (false == pushServiceIsStart(cxt, PushService.SERVICENAME)) {
			Log.w(TAG, "Start Push Service");
			cxt.startService(new Intent(PushService.START));
		}

		if (UPDATE.equals(intent.getAction())) {
			Log.d(TAG, UPDATE);
			Update.startUpdate(cxt, Update.AUTO);
		} else if (GPS.equals(intent.getAction())) {
			Log.d(TAG, GPS);
			
			int ret = intent.getIntExtra(CommUtils.TYPE, AEMMLocation.LOCN);
			final int type = ret;
			
			AEMMLocation.postLocation(cxt, type);
			
		} else if (APK.equals(intent.getAction())) {
			Log.d(TAG, APK);
			
			long newDate = new Date().getTime();
			long oldDate = CommUtils.getTrafficTime(context);
			if ((newDate - oldDate) < 10000) {
				return;
			} else {
				CommUtils.setTrafficTime(context, newDate);
			}
			
			int ret = intent.getIntExtra(CommUtils.TYPE, DomXmlBuilder.APK);
			final int type = ret;
			ApkInfoMag.postApk(cxt, type);
		} else if (TIME.equals(intent.getAction())) {
			
			final int type = intent.getIntExtra(CommUtils.TYPE, -1);
			
			if (LOCTIME == type) {
				Log.d(TAG, TIME + " Location");
				AEMMLocation.cancelGpsTimer(cxt);
				long time = CommUtils.getLocTime(cxt);
				AEMMLocation.setGpsTimer(cxt, time);
			} else if (APKTIME == type) {
				Log.d(TAG, TIME + " Apk");
				ApkInfoMag.cancelApkTimer(cxt);
				long time = CommUtils.getAppTime(cxt);
				ApkInfoMag.setApkTimer(cxt, time);
			} else {
				Log.d(TAG, TIME + " Update Location And Apk");
				AEMMLocation.cancelGpsTimer(cxt);
				long time = CommUtils.getLocTime(cxt);
				AEMMLocation.setGpsTimer(cxt, time);
				
				ApkInfoMag.cancelApkTimer(cxt);
				time = CommUtils.getAppTime(cxt);
				ApkInfoMag.setApkTimer(cxt, time);
				
				Update.CancelUpdateTimer(cxt);
				Update.SetUpdateTimer(cxt);
			}
		} else if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
				Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
			
			AlarmReceiver.SendIntentForTime(context, AlarmReceiver.LOCTIME);
			
			AlarmReceiver.SendIntentForTime(context, AlarmReceiver.APKTIME);

		}
	}

	private boolean pushServiceIsStart(Context context, String className) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningServiceInfo> mServiceList = am
				.getRunningServices(30);

		for (int i = 0; i < mServiceList.size(); i++) {
			if (className.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}

		return false;
	}
}
