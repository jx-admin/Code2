package com.aess.aemm.function;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.aess.aemm.commonutils.CommUtils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

public class ProfessionalFunction {
	private IProfessionalFunctionService mBinder;
	private Context mContext;

	// bind service in constructor.
	public ProfessionalFunction(Context context) {
		mContext = context;
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBinder = IProfessionalFunctionService.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName className) {
			mBinder = null;
		}
	};

	public void beginService() {
		Intent intent = new Intent();
		intent.setAction("com.aess.aemm.function.ProfessionalFunctionService");
		mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public void stopService() {
		mContext.unbindService(mConnection);
	}

	// Following interface called used for AEMM client
	public void enableDial(boolean enabled) throws RemoteException {
		if (null != mBinder) {
			mBinder.enableDial(enabled);
		}
	}

	public void enableCamera(boolean enabled) throws RemoteException {
		if (null != mBinder) {
			mBinder.enableCamera(enabled);
		}
	}

	public void enableAudio(boolean enabled) throws RemoteException {
		if (null != mBinder) {
			mBinder.enableAudio(enabled);
		}
	}

	public void enableVideo(boolean enabled) throws RemoteException {
		if (null != mBinder) {
			mBinder.enableVideo(enabled);
		}
	}

	public void enableSMS(boolean enabled) throws RemoteException {
		if (null != mBinder) {
			mBinder.enableSMS(enabled);
		}
	}

	public void enableMMS(boolean enabled) throws RemoteException {
		if (null != mBinder) {
			mBinder.enableMMS(enabled);
		}
	}

	public void addProtectedAEMMComponent(String component)
			throws RemoteException {
		if (null != mBinder) {
			mBinder.addProtectedAEMMComponent(component);
		}
	}

	public void deleteProtectedAEMMComponent(String component)
			throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteProtectedAEMMComponent(component);
		}
	}

	public void addProhibitedApk(String apkName) throws RemoteException {
		if (null != mBinder) {
			mBinder.addProhibitedApk(apkName);
		}
	}

	public void deleteProhibitedApk(String apkName) throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteProhibitedApk(apkName);
		}
	}

	public void addProhibitedApplication(String application)
			throws RemoteException {
		if (null != mBinder) {
			mBinder.addProhibitedApplication(application);
		}
	}

	public void deleteProhibitedApplication(String application)
			throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteProhibitedApplication(application);
		}
	}

	public void addProhibitedActivity(String actyName) throws RemoteException {
		if (null != mBinder) {
			mBinder.addProhibitedActivity(actyName);
		}
	}

	public void deleteProhibitedActivity(String actyName)
			throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteProhibitedActivity(actyName);
		}
	}

	public void addAemmWifi(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.addAemmWifi(key);
		}
	}

	public void deleteAemmWifi(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteAemmWifi(key);
		}
	}

	public void addAemmVPN(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.addAemmVPN(key);
		}
	}

	public void deleteAemmVPN(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteAemmVPN(key);
		}
	}

	public void addAemmAPN(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.addAemmAPN(key);
		}
	}

	public void deleteAemmAPN(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteAemmAPN(key);
		}

	}

	public void addAemmEmail(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.addAemmEmail(key);
		}
	}

	public void deleteAemmEmail(String key) throws RemoteException {
		if (null != mBinder) {
			mBinder.deleteAemmEmail(key);
		}
	}

	public void enableAllApkInstalled(boolean enabled) throws RemoteException {
		CommUtils.setBlockInstall(mContext, enabled);
		if (null != mBinder) {
			mBinder.enableAllApkInstall(enabled);
		}
	}

	public static boolean isAllApkInstalledEnabled(Context context) {
		if (CommUtils.getProjectVersion(context) != CommUtils.EXPRESS) {
			FileInputStream fis;
			Uri uri = new Uri.Builder().build();
			uri = Uri
					.parse("content://com.android.accenture.aemm.AemmProvider/aemminstall");
			try {
				byte[] benable = new byte[1];
				fis = (FileInputStream) context.getContentResolver()
						.openInputStream(uri);
				benable[0] = 0;
				fis.read(benable);
				fis.close();
				return (benable[0] == 0);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} catch (Exception e) {
			}
			return true;
		}
		return CommUtils.getBlockInstall(context);
	}

	public static void enableVoiceDialer(Context context, boolean enable) {
		FileOutputStream fos;
		FileInputStream fis;
		Uri uri = new Uri.Builder().build();
		uri = Uri
				.parse("content://com.android.accenture.aemm.AemmProvider/aemmvoicedialer");
		try {
			byte[] benable = new byte[1];
			fis = (FileInputStream) context.getContentResolver()
					.openInputStream(uri);
			benable[0] = -1;
			fis.read(benable);
			fis.close();
			fos = (FileOutputStream) context.getContentResolver()
					.openOutputStream(uri, "rw");
			benable[0] = enable ? 0x00 : (byte) 0x01;
			fos.write(benable);
			fos.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}

	public static boolean isAemmProviderPresented(Context context) {
		FileOutputStream fos;
		Uri uri = new Uri.Builder().build();
		uri = Uri
				.parse("content://com.android.accenture.aemm.AemmProvider/aemmprovider");
		try {
			fos = (FileOutputStream) context.getContentResolver()
					.openOutputStream(uri, "rw");
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return false;
	}
}
