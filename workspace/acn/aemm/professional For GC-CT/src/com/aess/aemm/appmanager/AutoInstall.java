package com.aess.aemm.appmanager;

import java.util.List;

import com.aess.aemm.db.ApkContent;
import com.aess.aemm.push.PushService;
import com.aess.aemm.view.MainView;
import com.aess.aemm.view.UpdateCallBack;
import com.aess.aemm.view.data.Appdb;
import android.content.Context;
import android.util.Log;

public class AutoInstall extends Thread {
	public final static String TAG = "AutoInstall";
	public final static int AUTOINSTALL = 1;

	public static int state() {
		synchronized (__lock) {
			if (0 == PushService.autoinstall) {
				__work = 0;
			}
			return __work;
		}
	}

	public AutoInstall(Context cxt) {
		_cxt = cxt;
//		setState(0);
	}

	@Override
	public void run() {
		setState(1);
		
		setName("AutoInstall");
		

		List<ApkContent> apkList = ApkContent.queryAllContents(_cxt);
		if (null == apkList || apkList.size() < 1) {
			setState(0);
			return;
		}
		
		Log.d(TAG, "AutoInstall Begin");
		
		ApkInstall ai = new ApkInstall(_cxt);
		int count = 0;
		while(!ai.serviceIsOk()) {
			try {
				Thread.sleep(500);
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count > 19) {
				setState(0);
				Log.w(TAG, "bindService(ApkInstallService.class) fail");
				return;
			}
		}
		

		
		for (int x = 0; x < apkList.size(); x++) {
			_apc = apkList.get(x);
			if (null == _apc.mApkUrl || null == _apc.mApkFlag) {
				continue;
			}
			int flag = Integer.valueOf(_apc.mApkFlag);
			if (flag > Appdb.UNINSTALLED) {
				continue;
			}
			if (null == _apc.mApkUrl) {
				continue;
			}
			
			UpdateCallBack ucb = new UpdateCallBack();

			ai.install(new Appdb(_apc), ucb);
		}
		setState(0);
		MainView.sendAutoInsEndMessage();
	}

	private static void setState(int value) {
		synchronized (__lock) {
			__work = value;
		}
	}

	private Context _cxt;
	private ApkContent _apc;
	private static int __work = 0;
	private static Object __lock = new Object();
	
//	private int autoInstall(String url) {
//	Log.d(TAG, "autoInstall");
//	AppDownload downwork = new AppDownload();
//	AppDownload.DownloadParam param = new AppDownload.DownloadParam(url,
//			null);
//	DownloadCallBack callback = new DownloadCallBack();
//	downwork.downloadFile(_cxt, param, callback);
//	return 0;
//}
//
//private class DownloadCallBack extends AppDownload.DownloadCallBack
//		implements InstallCallBack {
//
//	@Override
//	public void installResult(String packageName, int error) {
//		Log.d(TAG, "downloadResult: " + error);
//		_apc.mApkFlag = String.valueOf(Appdb.INSTALLED);
//		AppInstallUninstall.deleteFile(mFilePath);
//		ViewUtils.update(_cxt, MsgType.APP_UPATE_SILLENCE, null);
//	}
//
//	@Override
//	public void uninstallResult(String packageName, boolean succeeded) {
//
//	}
//
//	@Override
//	public void downloadProgress(DownloadParam param, int totalBytes,
//			int receivedBytes) {
//
//	}
//
//	@Override
//	public void downloadResult(DownloadParam param, int error) {
//		Log.d(TAG, "downloadResult: " + error);
//		if (error != 0) {
//			AppInstallUninstall.deleteFile(mFilePath);
//		} else {
//			try {
//				PackageManager pm = _cxt.getPackageManager();
//				PackageInfo info = pm.getPackageArchiveInfo(mFilePath,
//						PackageManager.GET_ACTIVITIES);
//				if (info == null) {
//					AppInstallUninstall.deleteFile(mFilePath);
//				} else {
//					_apc.mApkPackageName = info.packageName;
//					_apc.update(_cxt);
//					_appInstallUninstall.installApplication(mFilePath,
//							this, 0);
//				}
//			} catch (Exception e) {
//				AppInstallUninstall.deleteFile(mFilePath);
//				e.printStackTrace();
//			}
//		}
//	}
//
//}
}
