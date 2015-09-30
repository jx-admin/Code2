package com.aess.aemm.appmanager;

import java.io.File;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.os.Process;  

import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.function.ProfessionalFunction;
import com.aess.aemm.installservice.IRemoteService;
import com.aess.aemm.installservice.IRemoteServiceCallback;
import com.aess.aemm.view.data.Appdb;

public class AppInstallUninstall {
	private final static String TAG = "com.aess.aemm.appmanager.AppInstallUninstall"; 
	private IRemoteService mService = null;
	private Context mContext;

	public AppInstallUninstall(Context context) {
		mContext = context;
    	context.bindService(new Intent("android.service.package.addpackage"),
	            mConnection, Context.BIND_AUTO_CREATE);
	}

    public void destroy() {
   		mContext.unbindService(mConnection);
    }

	public static interface InstallCallBack {
		public void installResult(String packageName, int error);
		public void uninstallResult(String packageName, boolean succeeded);
	}

	public class RemoteServiceCallback extends IRemoteServiceCallback.Stub {
		InstallCallBack mCallBack;
		long mAppId;
		String mFileName;
		public RemoteServiceCallback(InstallCallBack installCallBack, long appId, String fileName) {
			mCallBack = installCallBack;
			mAppId = appId;
			mFileName = fileName;
		}

		@Override
		public void installResult(String packageName, int error)
				throws RemoteException {
			Log.i(TAG, "Install result: " + packageName + ", " + error);
			ApkContent apkContent = ApkContent.queryContentById(mContext, mAppId);
			if(error >= 0) {
				error = 0;
				//apkContent.mApkFlag = String.valueOf(Appdb.INSTALLED);
				//apkContent.update(mContext);
			} else {
				apkContent.mApkFlag = String.valueOf(Appdb.UNINSTALLED);
				apkContent.update(mContext);
			}
			mCallBack.installResult(packageName, error);
			deleteFile(mFileName);
		}

		@Override
		public void uninstallResult(String packageName, boolean succeeded)
				throws RemoteException {
			Log.i(TAG, "Uninstall result: " + succeeded);
			if(succeeded) {
				//ApkProfileContent apkContent = ApkProfileContent.restoreApkProfileContentWithId(mContext, mAppId);
				//apkContent.mApkFlag = String.valueOf(Appdb.UNINSTALLED);
				//apkContent.update(mContext);
			}
		}
	}

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                IBinder service) {
            mService = IRemoteService.Stub.asInterface(service);
            if(ProfessionalFunction.isAemmProviderPresented(mContext)) {
            	CommUtils.setProjectVersion(mContext, CommUtils.PROFESSIONAL);
            }
        }
 
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    public static boolean deleteFile(String filePath) {
    	File file;
    	try {
    		file = new File(filePath);
    	} catch (Exception e) {
    		file = null;
    	}
    	if ((file != null) && !file.delete()) {
            return false;
        } else {
            return true;
        }
    }
    
    public void installApplication(String fileName, InstallCallBack installCallBack, long appId) throws android.os.RemoteException {
    	if(Process.myUid() != Process.SYSTEM_UID) {
    		Uri uri = Uri.parse(fileName);
    		Intent it = new Intent(Intent.ACTION_VIEW, uri);
    		it.setData(uri);
    		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    		it.setClassName("com.android.packageinstaller",
    				"com.android.packageinstaller.PackageInstallerActivity");
    		mContext.startActivity(it);	
    	} else {
	    	IRemoteServiceCallback callback = new RemoteServiceCallback(installCallBack, appId, fileName);
	    	mService.installApplication(fileName, callback);
    	}
    }

    public void uninstallApplication(String packageName, InstallCallBack installCallBack, long appId) throws android.os.RemoteException {
    	if(Process.myUid() != Process.SYSTEM_UID) {
        	Uri uri = Uri.fromParts("package", packageName, null);         
        	Intent it = new Intent(Intent.ACTION_DELETE, uri);         
        	mContext.startActivity(it);
    	} else {
	    	IRemoteServiceCallback callback = new RemoteServiceCallback(installCallBack, appId, null);
	    	mService.uninstallApplication(packageName, callback);
    	}
    }
}
