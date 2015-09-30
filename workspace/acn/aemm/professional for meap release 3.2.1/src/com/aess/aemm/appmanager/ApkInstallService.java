package com.aess.aemm.appmanager;

import java.util.ArrayList;
import java.util.List;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.aess.aemm.appmanager.AppInstallUninstall.InstallCallBack;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.networkutils.AppDownload;
import com.aess.aemm.push.PushService;
import com.aess.aemm.view.data.Appdb;

public class ApkInstallService extends Service {
	public final static String TAG = "ApkInstallService";
	
	public static interface UpdateCallBack {
		public void updateProgress(Appdb app, int totalBytes, int receivedBytes);

		public void updateFinished(Appdb app, int error);
	}

	private class DownloadCallBack extends AppDownload.DownloadCallBack
			implements InstallCallBack {

		DownloadCallBack(final Context context, final Appdb app,
				final UpdateCallBack callBack) {
			mContext = context;
			mApp = app;
			mCallBack = callBack;
		}

		@Override
		public void downloadProgress(AppDownload.DownloadParam param,
				int totalBytes, int receivedBytes) {
			synchronized (mUpdateLock) {
				mCallBack.updateProgress(mApp, totalBytes
						+ (int) (totalBytes * 0.1), receivedBytes);
			}
		}

		@Override
		public void downloadResult(AppDownload.DownloadParam param, int error) {
			synchronized (mUpdateLock) {
				if (error != 0) {
					mCallBack.updateFinished(mApp, error);
					AppInstallUninstall.deleteFile(mFilePath);
				} else {
					try {
						PackageManager pm = getPackageManager();
						PackageInfo info = pm.getPackageArchiveInfo(mFilePath,
								PackageManager.GET_ACTIVITIES);
						if (info == null) {
							mCallBack.updateFinished(mApp, -1);
							AppInstallUninstall.deleteFile(mFilePath);
						} else {
							ApkContent apkContent = ApkContent
									.queryContentById(
											ApkInstallService.this,
											mRequestList.get(0).mApp.getId());
							if (apkContent != null) {
								apkContent.mApkPackageName = info.packageName;
								apkContent.update(ApkInstallService.this);
							}
							mAppInstallUninstall.installApplication(mFilePath,
									this, mApp.getId());
						}
					} catch (Exception e) {
						mCallBack.updateFinished(mApp, -1);
						AppInstallUninstall.deleteFile(mFilePath);
						e.printStackTrace();
					}
				}
				if (Process.myUid() != Process.SYSTEM_UID) {
					mCallBack.updateFinished(mApp, error);
					mRequestList.remove(0);
					nextUpdate();
				}
			}
		}

		private void nextUpdate() {
			if (!mRequestList.isEmpty()) {
				
				ApkContent apkContent = ApkContent
							.queryContentById(
									ApkInstallService.this,
									mRequestList.get(0).mApp.getId());
			 
		        if (0 == PushService.autoinstall) {
					if (apkContent != null) {
						apkContent.mApkFlag = String.valueOf(Appdb.UNINSTALLED);
						apkContent.update(ApkInstallService.this);
					}
				}
				DownloadCallBack downloadCallBack = mRequestList.get(0);
				mAppDownload = new AppDownload();

				mAppDownload
						.downloadFile(mContext,
								new AppDownload.DownloadParam(downloadCallBack.mApp,
										apkContent == null ? null : ""
												+ apkContent.mId),
								downloadCallBack);
			} else {
				mUpdateStarted = false;
			}
		}

		@Override
		public void installResult(String packageName, int error) {
			synchronized (mUpdateLock) {
				mCallBack.updateFinished(mApp, error);
				mRequestList.remove(0);
				nextUpdate();
			}
		}

		@Override
		public void uninstallResult(String packageName, boolean succeeded) {
			
		}
		
		private final Context mContext;
		private final Appdb mApp;
		private UpdateCallBack mCallBack;
	}

	public void install(final Appdb app, final UpdateCallBack callBack) {
		final Context context = ApkInstallService.this;
		synchronized (mUpdateLock) {
			if (ApkContent.queryContentBy_PKG_FLAG(context,
					app.getApkName(), Appdb.INSTALLED) != null) {
				callBack.updateFinished(app, 0);
				Log.d(TAG, "flag == Appdb.INSTALLED");
				return;
			}
			
			for (DownloadCallBack listCallBack : mRequestList) {
				if (listCallBack.mApp.getApkId().equals(app.getApkId())) {
					listCallBack.mCallBack = callBack;
					Log.d(TAG, "had added");
					return;
				}
			}
			
			mRequestList.add(new DownloadCallBack(context, app, callBack));
			if (!mUpdateStarted) {
				Log.d(TAG, "Down List add");
				
				mUpdateStarted = true;
				DownloadCallBack downloadCallBack = new ApkInstallService.DownloadCallBack(
						context, app, callBack);
				
				ApkContent apkContent = ApkContent.queryContentById(ApkInstallService.this,
								app.getId());
				
				if (apkContent != null) {
					apkContent.mApkFlag = String.valueOf(Appdb.UNINSTALLED);
					apkContent.update(ApkInstallService.this);
				}
				String fname = null;
				if (null != apkContent) {
					fname = "" + apkContent.mId;
				}
				mAppDownload = new AppDownload();
				mAppDownload.downloadFile(context,
						new AppDownload.DownloadParam(app, fname),
						downloadCallBack);
			}
		}
	}

	public List<Appdb> getPendingDownload() {
		synchronized (mUpdateLock) {
			List<Appdb> appList = new ArrayList<Appdb>();
			for (DownloadCallBack callBack : mRequestList) {
				appList.add(callBack.mApp);
			}
			return appList;
		}
	}
	
	public int cleanDownList() {
		synchronized (mUpdateLock) {
			mRequestList.clear();
			return 1;
		}
	}

	public void cancel() {
		synchronized (mUpdateLock) {
			if (null != mAppDownload) {
				mAppDownload.cancel();
			}
		}
	}

	public class LocalBinder extends Binder {
		public ApkInstallService getService() {
			return ApkInstallService.this;
		}
	}

	@Override
	public void onCreate() {
		mAppInstallUninstall = new AppInstallUninstall(this);
	}

	@Override
	public void onDestroy() {
		mAppInstallUninstall.destroy();
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "onBind");
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		if ("com.aess.aemm.appmanager.DeletePackage".equals(action)) {
			String packageName = intent.getStringExtra("PACKAGE_NAME");
			long packageId = intent.getLongExtra("PACKAGE_ID", -1);
			try {
				mAppInstallUninstall.uninstallApplication(packageName, null,
						packageId);
			} catch (RemoteException e) {
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	

	private final static Object mUpdateLock = new Object();
	private static boolean mUpdateStarted = false;
	
	private List<DownloadCallBack> mRequestList = new ArrayList<DownloadCallBack>();
	AppInstallUninstall mAppInstallUninstall;
	AppDownload mAppDownload;
}
