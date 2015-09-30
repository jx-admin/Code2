package com.aess.aemm.installservice;


import android.app.Service;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PackageService extends Service {
	private final static String TAG = "PackageService"; 
	public final static String ADD_PACKAGE="android.service.package.addpackage";
	public final static String DELETE_PACKAGE="android.service.package.deletepackage";
	public final static String CATEGORY="android.intent.category.packageservices";
	public final static String apkUriKey="apkUri";
	public final static String appPackageKey="package";

	@Override
	public IBinder onBind(Intent intent) {
        return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		Log.i(TAG, "onStartCommand: " + action);
		if(ADD_PACKAGE.equals(action)){
			String apkPath=(String) intent.getStringExtra(apkUriKey);
			installApp(apkPath, null);
		}else if(DELETE_PACKAGE.equals(action)){
			String pkg=(String) intent.getStringExtra(appPackageKey);
			unInstallApp(pkg, null);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

    private IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        @Override
		public
        void installApplication(String appPath, IRemoteServiceCallback callback) {
        	installApp(appPath, callback);
        }

		@Override
		public void uninstallApplication(String packageName,
				IRemoteServiceCallback callback) throws RemoteException {
			unInstallApp(packageName, callback);
		}
    };

	public void installApp(String file, IRemoteServiceCallback callback) {
		PackageManager pm=getPackageManager();
		 PackageInfo info = pm.getPackageArchiveInfo(file, PackageManager.GET_ACTIVITIES);
		 if(info==null){
			 try {
				 if(callback != null)
					callback.installResult(null, -1);
			 } catch (RemoteException e) {
			 } catch (Exception e) {
			 }
			 Log.e(TAG,"installApp apk file erro :"+file);
			 return;
		 }
		 int flags=0;
		 try {
	            PackageInfo pi = pm.getPackageInfo(info.packageName, 
	                    PackageManager.GET_UNINSTALLED_PACKAGES);
	            if(pi != null) {
	            	flags |= PackageManager.INSTALL_REPLACE_EXISTING;
	            }
	        } catch (NameNotFoundException e) {
	        }
		PackageInstallObserver iobserver = new PackageInstallObserver(callback);
		PackageManagerUtils.installPackage(pm, file, iobserver,
				flags, "");
	}

	public void unInstallApp(String pkg, IRemoteServiceCallback callback) {
		PackageDeleteObserver dobserver = new PackageDeleteObserver(pkg, callback);
		PackageManagerUtils.deletePackage(getPackageManager(),
				pkg, dobserver, 0);
	}

    class PackageInstallObserver extends IPackageInstallObserver.Stub {
    	IRemoteServiceCallback mCallback;
    	PackageInstallObserver(IRemoteServiceCallback callback) {
    		mCallback = callback;
    	}
        public void packageInstalled(String packageName, int returnCode) {
        	Log.i(TAG,packageName+" install return :"+returnCode);
        	try {
        		if(mCallback != null)
        			mCallback.installResult(packageName, returnCode);
			} catch (RemoteException e) {
			}
        }
    }
    class PackageDeleteObserver extends IPackageDeleteObserver.Stub {
    	String mPackageName;
    	IRemoteServiceCallback mCallback;
    	PackageDeleteObserver(String packageName, IRemoteServiceCallback callback) {
    		mCallback = callback;
    		mPackageName = packageName;
    	}
    	public void packageDeleted(boolean succeeded) {
        	Log.i(TAG,"PackageDeleteObserver return :"+succeeded);
        	try {
        		if(mCallback != null)
        			mCallback.uninstallResult(mPackageName, succeeded);
			} catch (RemoteException e) {
			}	
       }
    }
}
