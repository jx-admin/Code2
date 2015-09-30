package com.aess.aemm.push;

import java.util.Timer;
import java.util.TimerTask;
import com.aess.aemm.R;
import com.aess.aemm.AEMMConfig;
import com.aess.aemm.apkmag.ApkInfoMag;
import com.aess.aemm.appmanager.AutoInstall;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.gps.AEMMLocation;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.SocketClient;
import com.aess.aemm.push.PushManager.PushState;
import com.aess.aemm.receiver.AlarmReceiver;
import com.aess.aemm.update.Update;
import com.android.accenture.aemm.Aemm;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class PushService extends Service {

	// common constant
	public final static int TIME_PERIOD = 15000;
	public final static int TIME_DELAY = 5000;
	public final static String TAG = "PushService";
	public final static String SERVICENAME = "com.aess.aemm.push.PushService";

	public final static String START = "com.aess.aemm.push.START";
	public final static String NEWDOMAIN = "com.aess.aemm.push.NEWDOMAIN";
	public final static int TOAST_MESSAGE = 1;
	

	// event
	// public final static int TIME_TICK = 1;
	public final static int FLASH_LINK = 2;
	public final static int AUTO_UPDATE = 3;
	public final static int LOCA_CYCLE = 4;
	public final static int APP_CYCLE = 5;
	public final static int AUTO_INS_B = 6;
	// public final static int AUTO_INS_E = 7;
	public final static int RESET_PUSH = 8;
	public final static int TRAFFIC_LIMIT = 9;
	public final static int SERVICE_LIMIT = 10;
	public final static int RESET_TIME = 11;

	public static void sendAutoInsBeginMessage() {
		if (null != handler) {
			handler.sendEmptyMessage(AUTO_INS_B);
		}
	}

	public static void resetPushManagerMsg() {
		if (null != handler) {
			handler.sendEmptyMessage(RESET_PUSH);
		}
	}

	public static void readPushLimitMsg() {
		if (null != handler) {
			handler.sendEmptyMessage(TRAFFIC_LIMIT);
		}
	}
	
	public static void readServiceLimit() {
		if (null != handler) {
			handler.sendEmptyMessage(SERVICE_LIMIT);
		}
	}
	
	public static void sendTimeSet() {
		if (null != handler) {
			handler.sendEmptyMessage(RESET_TIME);
		}
	}

	// public static void sendAutoInsEndMessage() {
	// if (null != handler) {
	// handler.sendEmptyMessage(AUTO_INS_E);
	// }
	// }

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		new ToastThread().start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case FLASH_LINK: {
					Log.i(TAG, "Push State " + mag.getPushState().toString());
					if (tlimit < 0) {
						Update.hitFlowLimit(PushService.this);
						Log.w(TAG, "Flow Limit");
					}
					if (slimit < 0) {
						Log.w(TAG, "Service People Limit");
					}
					
					if (tlimit >= 0 && slimit >= 0) {
						mag.work();
					}
					break;
				}
				case AUTO_UPDATE: {
					if (tlimit >= 0 && slimit >= 0) {
						Update.startUpdate(PushService.this, Update.AUTO);
					}
					break;
				}
				case AUTO_INS_B: {
					if (AutoInstall.state() < 1) {
						AutoInstall ai = new AutoInstall(PushService.this);
						ai.start();
					} else {
						Log.d(TAG, "AutoInstall is work");
					}
					break;
				}
				case RESET_PUSH: {
					mag.setPushState(PushState.PSH_NONE);
					break;
				}
				case TRAFFIC_LIMIT: {
					int newtlimit = SocketClient.isTrafficLimtOver(PushService.this);
					if (tlimit != newtlimit) {
						tlimit = newtlimit;
						if (tlimit < 0) {
							mag.setPushState(PushState.PSH_ERR);
							mag.delSocket();
							ApkInfoMag.sendIntentForApkUnlimit(PushService.this);
						} else {
							sendTimeSet();
						}
					}

					break;
				}
				case SERVICE_LIMIT: {
					int newslimit = CommUtils.getServiceLimit(PushService.this);
					if (slimit != newslimit) {
						slimit = newslimit;
						if (slimit >= 0) {
							sendTimeSet();
						}
					}

					break;
				}
				case RESET_TIME: {
					AEMMLocation.intentResetLocation(PushService.this);

					AlarmReceiver.SendIntentForTime(PushService.this, AlarmReceiver.ALL);
					
					AEMMLocation.intentResetLocation(PushService.this);
					break;
				}
				default: {
					break;
				}
				}
				super.handleMessage(msg);
			}

		};

		
		tlimit = SocketClient.isTrafficLimtOver(PushService.this);
		
		slimit = CommUtils.getServiceLimit(PushService.this);
		
		mag = new PushManager(this, handler);

		flashPush.schedule(pushTask, TIME_DELAY, TIME_PERIOD);

		sendTimeSet();
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		startService(new Intent(PushService.START));
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null != intent) {
			String action = intent.getAction();

			if (NEWDOMAIN.equals(action)) {
				Log.d(TAG, action);
				if (AEMMConfig.SIGN.equals(AutoAdress.AUTO)) {
					AutoAdress ad = AutoAdress.getInstance(this);
					ad.clear();
					mag.setPushState(PushState.PSH_ERR);
				}
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private Timer flashPush = new Timer("flashPush");
	private TimerTask pushTask = new TimerTask() {
		@Override
		public void run() {
			if (null != handler) {
				handler.sendEmptyMessage(FLASH_LINK);
			}
		}
	};

	private class ToastThread extends Thread {
		public void run() {
			setName("Toast Thread");
			try {
				Looper.prepare();
				mToastHandler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case TOAST_MESSAGE: {
							onToast(msg.arg1);
						}
							break;
						}
					}
				};
				Aemm.waitForNotify(PushService.class);
				Looper.loop();
			} catch (Error e) {
			} catch (Exception e) {
			}
		}
	}

	// The function was called from framework.
	@SuppressWarnings("unused")
	private static void postEventFromNative(final int message) {
		Log.i(TAG, "" + message);
		mToastHandler.post(new Runnable() {
			public void run() {
				mToastHandler.dispatchMessage(mToastHandler.obtainMessage(
						TOAST_MESSAGE, message, 0));
			}
		});
	}

	private void onToast(int message) {
		Toast toast = null;
		switch (message) {
		case MESSAGE_AUDIOW_DISABLE:
			toast = Toast.makeText(this, getText(R.string.audio_disabled),
					Toast.LENGTH_SHORT);
			break;
		case MESSAGE_CAMERA_DISABLE:
			toast = Toast.makeText(this, getText(R.string.camera_disabled),
					Toast.LENGTH_SHORT);
			break;
		case MESSAGE_SMS_DISABLE:
			toast = Toast.makeText(this, getText(R.string.sms_disabled),
					Toast.LENGTH_SHORT);
			break;
		case MESSAGE_MMS_DISABLE:
			toast = Toast.makeText(this, getText(R.string.mms_disabled),
					Toast.LENGTH_SHORT);
			break;
		case MESSAGE_INSTALLAPK_DISABLE:
			toast = Toast.makeText(this,
					getText(R.string.install_apk_disabled), Toast.LENGTH_SHORT);
			break;
		case MESSAGE_DIAL_DISABLE:
			toast = Toast.makeText(this, getText(R.string.dial_disabled),
					Toast.LENGTH_SHORT);
			break;
		}
		if (toast != null)
			toast.show();
	}

	private final static int MESSAGE_CAMERA_DISABLE = 1;
	private final static int MESSAGE_AUDIOW_DISABLE = 3;
	private final static int MESSAGE_MMS_DISABLE = 4;
	private final static int MESSAGE_SMS_DISABLE = 5;
	private final static int MESSAGE_INSTALLAPK_DISABLE = 6;
	private final static int MESSAGE_DIAL_DISABLE = 8;

	private static Handler handler = null;
	private PushManager mag = null;
	private static Handler mToastHandler;
	private int tlimit = 1;
	private int slimit = 1;

	public static int autoinstall = 1;
}
