package com.aess.aemm.function;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ProfessionalFunctionService extends Service {
    private static final String TAG = "AidlService";
    private static final String PACKAGE_AEMM = "content://com.android.accenture.aemm.AemmProvider/";
    
    private void Log(String str) {
		Log.d(TAG, "------ " + str + "------");
	}

	@Override
	public void onCreate() {
		Log("service create");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log("service start id=" + startId);
	}
	
	@Override
	public IBinder onBind(Intent t) {
		Log("service on bind");
		return mBuilder;
	}

	@Override
	public void onDestroy() {
		Log("service on destroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log("service on unbind");
		return super.onUnbind(intent);
	}

	public void onRebind(Intent intent) {
		Log("service on rebind");
		super.onRebind(intent);
	}
	
	//implement interface for client
    private IProfessionalFunctionService.Stub mBuilder = new IProfessionalFunctionService.Stub() {
		
		public void enableVideo(boolean enabled) throws RemoteException {
			FileOutputStream fos;
			Uri uri = new Uri.Builder().build();
	        uri = Uri.parse(PACKAGE_AEMM+"aemmvideo");
	        try {
	        	fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
	        	byte[] benable = new byte[1];
	        	if(enabled)  //enable
	            {
	        		benable[0] = 0x00;
	        		Log("enableVideo........true");
	        	}
	        	else   //disable
	        	{
	        		benable[0] = 0x01;
	        		Log("enableVideo........false");
	        	}
	        	fos.write(benable);
	        	fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				
			}
		}
		
		public void enableSMS(boolean enabled) throws RemoteException {
			FileOutputStream fos;
			Uri uri = new Uri.Builder().build();
	        uri = Uri.parse(PACKAGE_AEMM+"aemmsms");
	        try {
	        	fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
	        	byte[] benable = new byte[1];
	        	if(enabled)  //enable
	            {
	        		benable[0] = 0x00;
	        		Log("enableSMS........true");
	        	}
	        	else   //disable
	        	{
	        		benable[0] = 0x01;
	        		Log("enableSMS........false");
	        	}
	        	fos.write(benable);
	        	fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				
			}
		}
		
		public void enableMMS(boolean enabled) throws RemoteException {
			FileOutputStream fos;
			Uri uri = new Uri.Builder().build();
	        uri = Uri.parse(PACKAGE_AEMM+"aemmmms");
	        try {
	        	fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
	        	byte[] benable = new byte[1];
	        	if(enabled)  //enable
	            {
	        		benable[0] = 0x00;
	        		Log("enableMMS........true");
	        	}
	        	else   //disable
	        	{
	        		benable[0] = 0x01;
	        		Log("enableMMS........false");
	        	}
	        	fos.write(benable);
	        	fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				
			}
		}
		
		public void enableDial(boolean enabled) throws RemoteException {
			FileOutputStream fos;
			Uri uri = new Uri.Builder().build();
	        uri = Uri.parse(PACKAGE_AEMM+"aemmdial");
	        try {
	        	fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
	        	byte[] benable = new byte[1];
	        	if(enabled)  //enable
	            {
	        		benable[0] = 0x00;
	        		Log("enableDial........true");
	        	}
	        	else   //disable
	        	{
	        		benable[0] = 0x01;
	        		Log("enableDial........false");
	        	}
	        	fos.write(benable);
	        	fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
		
		public void enableCamera(boolean enabled) throws RemoteException {
			FileOutputStream fos;
			Uri uri = new Uri.Builder().build();
	        uri = Uri.parse(PACKAGE_AEMM+"aemmcamera");
	        try {
	        	fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
	        	byte[] benable = new byte[1];
	        	if(enabled)  //enable
	            {
	        		benable[0] = 0x00;
	        		Log("enableCamera........true");
	        	}
	        	else   //disable
	        	{
	        		benable[0] = 0x01;
	        		Log("enableCamera........false");
	        	}
	        	fos.write(benable);
	        	fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				
			}
		}
		
		public void enableAudio(boolean enabled) throws RemoteException {
			FileOutputStream fos;
			Uri uri = new Uri.Builder().build();
	        uri = Uri.parse(PACKAGE_AEMM+"aemmaudio");
	        try {
	        	fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
	        	byte[] benable = new byte[1];
	        	if(enabled)  //enable
	            {
	        		benable[0] = 0x00;
	        		Log("enableAudio........true");
	        	}
	        	else   //disable
	        	{
	        		benable[0] = 0x01;
	        		Log("enableAudio........false");
	        	}
	        	fos.write(benable);
	        	fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				
			}
		}
		
		public void addProtectedAEMMComponent(String component)
				throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmcomponent");
        	ContentValues values = new ContentValues();
        	values.put("Rule", component);
        	Log("addProtectedAEMMComponent......."+component);
        	getContentResolver().insert(uri, values);        	
		}
		
		public void deleteProtectedAEMMComponent(String component)
				throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmcomponent");
			Log("deleteProtectedAEMMComponent.................."+component);
        	getContentResolver().delete(uri, component, null);			
		}
		
		public void addProhibitedApplication(String application)
				throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmapp");
        	ContentValues values = new ContentValues();
        	values.put("Rule", application);
        	Log("addProhibitedApplication..............."+application);
        	getContentResolver().insert(uri, values);			
		}
		
		public void deleteProhibitedApplication(String application)
				throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmapp");
			Log("deleteProhibitedApplication................."+application);
        	getContentResolver().delete(uri, application, null);			
		}
		
		public void addProhibitedApk(String apkName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmapk");
        	ContentValues values = new ContentValues();
        	values.put("Rule", apkName);
        	Log("addProhibitedApk......."+apkName);
        	getContentResolver().insert(uri, values);			
		}
		
		public void deleteProhibitedApk(String apkName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmapk");
			Log("deleteProhibitedApk................"+apkName);
        	getContentResolver().delete(uri, apkName, null);			
		}
		
		public void addProhibitedActivity(String actyName) throws RemoteException {
	    	Log("addProhibitedActivity..............."+actyName);
		}
		
		public void deleteProhibitedActivity(String actyName) throws RemoteException {
	    	Log("deleteProhibitedActivity..............."+actyName);
		}
		
		public void addAemmWifi(String wifiName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmwifi");
	    	ContentValues values = new ContentValues();
	    	values.put("Rule", wifiName);
	    	Log("addAemmAPN..............."+wifiName);
	    	getContentResolver().insert(uri, values);
		}
		
		public void deleteAemmWifi(String wifiName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmwifi");
			Log("deleteAemmWifi................"+wifiName);
        	getContentResolver().delete(uri, wifiName, null);	
		}
		
		public void addAemmVPN(String vpnName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmvpn");
	    	ContentValues values = new ContentValues();
	    	values.put("Rule", vpnName);
	    	Log("addAemmVPN..............."+vpnName);
	    	getContentResolver().insert(uri, values);
		}
		
		public void deleteAemmVPN(String vpnName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmvpn");
			Log("deleteAemmVPN................"+vpnName);
        	getContentResolver().delete(uri, vpnName, null);	
		}
		
		public void addAemmEmail(String emailName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmemail");
	    	ContentValues values = new ContentValues();
	    	values.put("Rule", emailName);
	    	Log("addAemmEmail..............."+emailName);
	    	getContentResolver().insert(uri, values);
		}
		
		public void deleteAemmEmail(String emailName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmemail");
			Log("deleteAemmEmail................"+emailName);
        	getContentResolver().delete(uri, emailName, null);	
		}
		
		public void addAemmAPN(String apnName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmapn");
	    	ContentValues values = new ContentValues();
	    	values.put("Rule", apnName);
	    	Log("addAemmAPN..............."+apnName);
	    	getContentResolver().insert(uri, values);	
		}
		
		public void deleteAemmAPN(String apnName) throws RemoteException {
			Uri uri = Uri.parse(PACKAGE_AEMM+"aemmapn");
			Log("deleteAemmAPN................"+apnName);
        	getContentResolver().delete(uri, apnName, null);	
		}
		
		public void enableAllApkInstall(boolean enabled){
			FileOutputStream fos;
			Uri uri = new Uri.Builder().build();
	        uri = Uri.parse(PACKAGE_AEMM+"aemminstall");
	        try {
	        	fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
	        	byte[] benable = new byte[1];
	        	if(enabled)  //enable
	            {
	        		benable[0] = 0x00;
	        		Log("enableAllApkInstall........true");
	        	}
	        	else   //disable
	        	{
	        		benable[0] = 0x01;
	        		Log("enableAllApkInstall........false");
	        	}
	        	fos.write(benable);
	        	fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				
			}
		}
	};
	
}
