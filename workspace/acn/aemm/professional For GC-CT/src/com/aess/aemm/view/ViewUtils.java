package com.aess.aemm.view;

import java.io.IOException;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.aess.aemm.R;
import com.aess.aemm.appmanager.ApkInstall;
import com.aess.aemm.appmanager.AppInstallUninstall;
import com.aess.aemm.appmanager.AppInstallUninstall.InstallCallBack;
import com.aess.aemm.authenticator.Constants;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.view.data.Appdb;
import com.aess.aemm.view.data.MsgType;

public class ViewUtils {
	private Activity context;
	private AppInstallUninstall mAppInstallUninstall;
	private ApkInstall mAppUpdateClient;
	private UpdateCallBack mUpdateCallBack;

	public ViewUtils(Activity ac) {
		this.context = ac;
		mAppInstallUninstall = new AppInstallUninstall(context);
		mAppUpdateClient = new ApkInstall(context);
		mUpdateCallBack = new UpdateCallBack();
	}
	
	public static void checkAccount(final Activity context) {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				// boolean result = false;
				AccountManager am = AccountManager.get(context);
				Account[] accounts = am
						.getAccountsByType(Constants.ACCOUNT_TYPE);
				
				if (null == accounts || accounts.length < 1) {
					am.addAccount(Constants.ACCOUNT_TYPE, null, null, null,
							context, (AccountManagerCallback<Bundle>) context, null);
					return;
				}
				String id = CommUtils.getSessionId(context);

				if (!TextUtils.isEmpty(id)) {

					String authtoken = null;
					try {
						authtoken = am.blockingGetAuthToken(accounts[0],
								Constants.AUTHTOKEN_TYPE, true);
					} catch (OperationCanceledException e) {
						e.printStackTrace();
					} catch (AuthenticatorException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (authtoken != null) {
						Message message = new Message();
						message.what = MainView.AUTHEN_RESULT;
						message.obj = Boolean.valueOf(true);
						((MainView) context).handler.sendMessage(message);
						return;
					}
				} else {
					for (Account account : accounts) {
						am.removeAccount(account, null, null);
					}
				}
				return;
			}
		}.start();
	}

	public static void toSaveApp(Context context, Appdb appdb) {
		if (appdb.getId() < 0) {
			return;
		}
		ApkContent.updateContentById(context, appdb.getId(),
				appdb.toApkProfileContent().toContentValues());
	}

	public static List<ApkContent> readAllApp(Context context, byte flag) {
		return ApkContent.queryContentsByFlag(context, flag);
	}

	/**
	 * Manual update
	 * 
	 * @param activity
	 */
	public static void manualUpdate(Activity activity) {
		// Log.v("click", "start updataHandle service");
		// Intent i = new Intent(DebugConstant.USER_UPDATE_ACTION);
		// i.setClass(activity, ListenerService.class);
		// activity.startService(i);
	}

	/**
	 * Download File
	 * 
	 * @param activity
	 * @param app
	 * @param handler
	 */
	public void startDownloadFile(AppItem app) {
		mAppUpdateClient.install(app, mUpdateCallBack);
	}

	/**
	 * refresh Download apk File
	 * 
	 * @param apkAdapterSl
	 */
	public void refreshUpdata(ScrollAdapter apkAdapterSl) {
		List<Appdb> appLs = mAppUpdateClient.getPendingDownload();
		if (appLs == null) {
			return;
		}
		for (Appdb app : appLs) {
			app = apkAdapterSl.getItemById(app.getApkPackageName(),
					app.getApkVersion());
			if (app != null) {
				mAppUpdateClient.install(app, mUpdateCallBack);
			}
		}
	}

	/**
	 * tell the services that hall view is start.
	 * 
	 * @see #android.app.Activity.onStart()
	 */
	public static void onStart(Context context) {
		// Intent serviceInfo = new Intent(ListenerService.HALL_START_ACTION);
		// serviceInfo.setClass(context, ListenerService.class);
		// context.startService(serviceInfo);
	}

	/**
	 * tall the services hall view is stop
	 * 
	 * @see #android.app.Activity.onStop()
	 */
	public static void onStop(Context context) {
		// Intent serviceInfo = new Intent(ListenerService.HALL_CLOSE_ACTION);
		// serviceInfo.setClass(this, ListenerService.class);
		// startService(serviceInfo);
	}

	public void onDestroy() {
		mAppInstallUninstall.destroy();
		mAppUpdateClient.destroy();
	}

	/**
	 * updata NewApp Message
	 * 
	 * @param context
	 */
	public static void updateNewAppMessage(Context context) {
		List<ApkContent> apkUninstallList = ApkContent.queryContentsByFlag(context, Appdb.NEWAPP);
		int newCnt = 0;
		if (apkUninstallList != null) {
			newCnt = apkUninstallList.size();
		}
		UpdateMessageHandler.addMessage(context,
				ViewUtils.getNewAppMessage(context, newCnt));
	}

	public static HallMessagedb getNewAppMessage(Context context, int count) {
		if (count > 0) {
			return new HallMessagedb(String.format(
					(String) context.getText(R.string.updata_app_push), count),
					0, -1, 10000, HallMessagedb.APPMSG);
		} else {
			return new HallMessagedb("", 0, 0, 0, HallMessagedb.APPMSG);
		}
	}

	/**
	 * Send a message to UI
	 * 
	 * @param context
	 * @param msg
	 */
	@Deprecated
	public static void addMessage(Context context, HallMessagedb msg) {
		UpdateMessageHandler.addMessage(context, msg);
	}

	public static void addResultMessage(HallMessagedb message) {
		UpdateMessageHandler.addResultMessage(message);
	}

	/**
	 * Removal of an update message
	 * 
	 * @param context
	 * @param kind
	 */
	public static void removeMessage(Context context, int kind) {
		UpdateMessageHandler.removeMessage(context, kind);
	}

	/**
	 * remove all the update message from hall ui.
	 * 
	 * @param context
	 */
	public static void clearAllMessage(Context context) {
		UpdateMessageHandler.clearAllMessage(context);
	}

	/**
	 * Notification to the UI to update
	 * 
	 * @param context
	 */
	public static void startUpdate(Context context) {
		clearAllMessage(context);
		UpdateMessageHandler.addMessage(context, new HallMessagedb(
				(String) context.getText(R.string.updata_executing), 0,
				HallMessagedb.EVER, HallMessagedb.status_delayTime,
				HallMessagedb.STATUSMSG));
	}

	/**
	 * Notification to the UI that update finish
	 * 
	 * @param context
	 */
	public static void finishUpdate(Context context) {
		Intent message = new Intent(UpdateMessageHandler.UPDATE_FINISH_MESSAGE);
		context.sendBroadcast(message);
		if (null != UpdateMessageHandler.Log) {
			UpdateMessageHandler.Log.d("send finishUpdateBroadcast");
		}
	}

	public static void finishUpdateFailed(Context context) {
		Intent message = new Intent(
				UpdateMessageHandler.UPDATE_NOT_FINISH_MESSAGE);
		context.sendBroadcast(message);
	}


	public static void startUser(Context context) {
		Intent message = new Intent(UpdateMessageHandler.USER);
		context.sendBroadcast(message);
	}

	/**
	 * 告知大厅应用可用性更新
	 * 
	 * @param context
	 * @param ids
	 * @param flags
	 */
	@Deprecated
	public static void HallAppLock(Context context, String[] ids,
			boolean[] flags) {
		UpdateMessageHandler.HallAppLock(context, ids, flags);
	}

	/**
	 * run cativityInfo.
	 * 
	 * @param activityInfo
	 * @return
	 */
	@Deprecated
	public static Intent startApplication(final ActivityInfo activityInfo) {
		if (activityInfo == null) {
			return null;
		}
		Intent i = new Intent();
		ComponentName cn = new ComponentName(activityInfo.packageName,
				activityInfo.name);
		i.setComponent(cn);
		i.setAction("android.intent.action.MAIN");
		// startActivityForResult(i, RESULT_OK);
		return i;
	}

	/**
	 * install Application
	 * 
	 * @param apkFile
	 * @return
	 */
	// @Deprecated
	public void installApplication(AppItem item) {
		try {
			mAppInstallUninstall.installApplication(item.getApkFullPath(),
					null, item.getId());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * uninstall application
	 * 
	 * @param activityInfo
	 *            activityInfo.packageName
	 * @return is the activityInfo is null. true if the param activityInfo isn't
	 *         null else return false.
	 */
	@Deprecated
	public void uninstallApplication(AppItem item) {
		if (item == null) {
			return;
		}
		if (item.getApkPackageName() == null) {
			return;
		}
		try {
			mAppInstallUninstall.uninstallApplication(item.getApkPackageName(),
					null, item.getId());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public static Intent installApplication(String file) {
		Uri uri = Uri.parse(file);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		it.setData(uri);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		it.setClassName("com.android.packageinstaller",
				"com.android.packageinstaller.PackageInstallerActivity");
		return it;
	}

	public void uninstallApplication(String packageName,
			InstallCallBack installCallBack, long appId) {
		try {
			mAppInstallUninstall.uninstallApplication(packageName,
					installCallBack, appId);
		} catch (RemoteException e) {

			e.printStackTrace();
		}
		// if(packageName==null){
		// return null;
		// }
		// Uri uri = Uri.fromParts("package", packageName, null);
		// Intent it = new Intent(Intent.ACTION_DELETE, uri);
		// return it;

	}

	@Deprecated
	public static boolean installApplicationSilence(Context context,
			String apkFile) {
		Intent i = new Intent("android.service.package.addpackage");
		i.addCategory("android.intent.category.packageservices");
		i.putExtra("apkUri", apkFile);
		context.startService(i);
		return true;
	}

	public static boolean unInstallApplicationSilence(Context context,
			String packageName) {
		Intent i;
		i = new Intent("android.service.package.deletepackage");
		i.addCategory("android.intent.category.packageservices");
		i.putExtra("package", packageName);
		context.startService(i);
		return true;
	}

	/**
	 * @param context
	 * @return
	 */
	public static boolean isProfessionalVertion(Context context) {
		return CommUtils.getProjectVersion(context) == CommUtils.PROFESSIONAL;
	}

	public static String getHallUpdateAdrress(Context context) {
		Log.d("MainView", "cur:" + CommUtils.getCurrentClientVersion(context)
				+ " new:" + CommUtils.getNewClientVersion(context));
		if (!CommUtils.getCurrentClientVersion(context).equals(
				CommUtils.getNewClientVersion(context))) {
			return CommUtils.getNewClientURL(context);
		}
		return null;
	}

	public static boolean isHallupdataMust(Context context) {
		return CommUtils.getNewClientRequire(context) == 1 ? true : false;
	}

	/**
	 * 客户端更新通知接口：
	 * com.aess.aemm.view.Utils.updata(com.aess.aemm.view.data.Messagedb
	 * .CLIENT_UPDATE，null);
	 * 
	 * app更新通知接口：
	 * com.aess.aemm.view.Utils.updata(com.aess.aemm.view.data.Messagedb
	 * .APP_UPDATE，null); 或
	 * com.aess.aemm.view.Utils.updata(com.aess.aemm.view.data
	 * .Messagedb.APP_UPDATE，com.aess.aemm.view.data.Messagedb data);
	 * 
	 * @param flag
	 * @param data
	 */
	
	public static void update(Context context, int flag, MsgType data) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		switch (flag) {
		case com.aess.aemm.view.data.MsgType.APP_UPATE_SILLENCE:
			if (MainView.mHall != null) {
				MainView.mHall.handler.sendEmptyMessage(MainView.App_UPDATE);
			}
			break;
		case com.aess.aemm.view.data.MsgType.APP_UPDATE:
			if (MainView.mHall != null) {
				MainView.mHall.handler.sendEmptyMessage(MainView.App_UPDATE);
			}
			if (!pm.isScreenOn()) {
				if (getHallUpdateAdrress(context) != null) {
					ServiceDia.showAddApp(context, R.string.newapp_client);
				} else {
					ServiceDia.showAddApp(context, R.string.newapp);
				}
			} else if (MainView.mHall == null
					|| MainView.mHall.atvState != MainView.RESUME) {
				if (getHallUpdateAdrress(context) != null) {
					NotificationUtils.sentNotification(context,
							R.string.newapp_client);
				} else {
					NotificationUtils
							.sentNotification(context, R.string.newapp);
				}
			}
			break;
		case com.aess.aemm.view.data.MsgType.CLIENT_UPDATE:
			if (!pm.isScreenOn()) {
				ServiceDia.showAddApp(context, R.string.hall_updata_title);
			} else if (MainView.mHall == null
					|| MainView.mHall.atvState != MainView.RESUME) {
				NotificationUtils.sentNotification(context,
						R.string.hall_updata_title);
			} else {
				MainView.mHall.handler.sendEmptyMessage(MainView.CLIENT_UPDATE);
			}
			break;

		case com.aess.aemm.view.data.MsgType.CLIENT_LOCK:
			if (!pm.isScreenOn()) {
				ServiceDia.showHallLockScreenOn(context, R.string.lock_client);
			} else if (MainView.mHall == null
					|| MainView.mHall.atvState != MainView.RESUME) {
				ServiceDia.showHallLockScreenOn(context, R.string.lock_client);
				// NotificationUtils.sentNotification(context,
				// R.string.lock_client);
			} else {
				MainView.mHall.handler.sendEmptyMessage(MainView.CLIENT_LOCK);
			}
			break;
		default:

		}
	}

	/**
	 * clear hall Notification
	 * 
	 * @param context
	 */
	public static void cancelNotification(Context context) {
		NotificationUtils.cancelNotification(context);
	}
}