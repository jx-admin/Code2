package com.android.accenture.aemm.express;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.accenture.aemm.express.GridAdapter.GridHolder;
import com.android.accenture.aemm.express.updataservice.ListenerService;

public class ApkLoader  extends Thread {
	public static final int stempSize=2048;
	public static final int MAXPERSENT=1000;
	public static final byte CANTSAVE=-1,CANCEL=-2;
	public static final String LOGCAT="loader";
//	private static Object apkruninglock = new Object();
//	private static boolean apkruning = false;
	private boolean isCancel;
	private Handler handler;
	private AppItem appdb;
	// private int current=0;
	private int max=100;
	private ProgressBar  pBar;
	private Context context;
	
	public ApkLoader(Context context,AppItem app,Handler handler){
		this.context=context;
		appdb=app;
		this.handler=handler;
		GridAdapter.GridHolder mGridHolder=(GridHolder) appdb.getView().getTag();
		this.pBar=mGridHolder.progressBar;
		
		pBar.setMax(max);
		pBar.setVisibility(View.VISIBLE);
		pBar.setProgress(0);
	}
	
//	public static boolean apkLoaderIsRuning() {
//		boolean alt = false;
//		synchronized (apkruninglock) {
//			alt = apkruning;
//		}
//		return alt;
//	}
	
	public void run() {

		File saveFile = null;
		FileOutputStream out = null;
		//byte[] bytes = null;
		// int size = 0;
		int flag = 0;
		byte[] bytes = new byte[stempSize];
		
		//setApkRuning(true);
		
		try {
			/*
			 * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
			 * <uses-permission android:name="android.permission.INTERNET" />
			 */
			Log.v(LOGCAT, "url:" + appdb.getApkUrl());
			URL url = new URL(appdb.getApkUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// for(int i=0;i<conn.getHeaderFields().size();i++){
			// Log.v(LOGCAT,"head:"+conn.getHeaderField(i)+" conn.getContentLength():"+conn.getContentLength());
			// }
			// 取得inputStream，并进行读取
			InputStream in = conn.getInputStream();
			Log.i(LOGCAT, "get inputstream");

			appdb.deleteApkFile();
			String state = Environment.getExternalStorageState();
			if (!Environment.MEDIA_MOUNTED.equals(state)) {
				flag = CANTSAVE;
				if (handler != null) {
					Message message = new Message();
					message.what = Main.FINISH_DOLOAD;
					message.arg1 = flag;
					message.obj = appdb;
					appdb.setFlag(Main.mHall, Appdb.UNINSTALLED);
					handler.sendMessage(message);
				} else {
					appdb.setFlag(Main.mHall, Appdb.UNINSTALLED);
				}

				appdb.setLock(false);
				return;
			}

			appdb.setApkFileName(getFileName());
			out = Main.mHall.openFileOutput(appdb.getApkFileName(),
					Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
			Log.v(LOGCAT, "getDir="
					+ Main.mHall.getFileStreamPath(appdb.getApkFileName())
							.getAbsolutePath());
			appdb.setApkFileName(Main.mHall.getFileStreamPath(
					appdb.getApkFileName()).getAbsolutePath());

			int romeSize = conn.getContentLength();
			Log.v(LOGCAT, "app size:" + romeSize + " memorySize:"
					+ Utils.getAvailableInternalMemorySize());
			if (romeSize >= Utils.getAvailableInternalMemorySize()) {
				appdb.setFlag(Main.mHall, Appdb.UNINSTALLED);
				// CustomDialog.createDialog(Main.mHall, R.string.lowermemerry);
				Toast.makeText(context, R.string.lowermemerry, 1).show();

				appdb.setLock(false);
				return;
			}
			int localSize = 0;
			int persent = 1;
			if (romeSize > MAXPERSENT) {
				persent = romeSize / MAXPERSENT;
			}
			int process = 0;
			int nowProcess = 0;
			int c = 0;
			pBar.setMax(MAXPERSENT);
			Log.v(LOGCAT, "romeSize:" + romeSize + " stemp:" + stempSize
					+ " maxpersent:" + MAXPERSENT + " onePersent:" + persent);
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				nowProcess = localSize / persent;
				if (nowProcess > process) {
					// Log.v(LOGCAT,"下载进度1000\\：" +
					// process+" "+localSize+"/"+romeSize+"="+(localSize*100/romeSize)+"%");
					process = nowProcess;

					pBar.setProgress(process);// When
												// state==1:Description==下载进度(0－1000)
				}
				if (isCancel && null != saveFile) {
					if (saveFile.exists()) {
						saveFile.delete();
					}
					break;
				}
				// if(Main.mHall==null){
				// in.close();
				// out.close();
				// appdb.deleteApkFile();
				// return;
				// }
			}
			in.close();
			out.close();
			// File file = new File(appdb.getApkFile() + "/" +
			// att.getRealname());
			// saveFile.renameTo(file);

			if (handler != null) {
				Message message = new Message();
				message.what = Main.FINISH_DOLOAD;
				message.obj = appdb;
				handler.sendMessage(message);
				Log.v(LOGCAT, "handler message is send");
			} else {
				appdb.setFlag(Main.mHall, Appdb.UNINSTALLED);
				Log.v(LOGCAT, "handler is null");
				ListenerService.startInstallApp(context, appdb.getApkId(),
						appdb.getApkVersion(), appdb.getApkFileName());
			}
		} catch (Exception e) {

			appdb.setLock(false);
			if (handler != null) {
				Message message = new Message();
				message.what = Main.Fail_DOLOAD;
				message.obj = appdb;
				handler.sendMessage(message);
				Log.v(LOGCAT, "handler message is send");
			} else {
				appdb.setFlag(Main.mHall, Appdb.UNINSTALLED);
				Log.v(LOGCAT, "handler is null");
			}
			if (saveFile != null && saveFile.exists()) {
				saveFile.delete();
			}
			e.printStackTrace();
		}
		appdb.setLock(false);
	}
	
	private String getFileName(){
//		 String value=conn.getHeaderField("Content-Disposition");
//         if(value!=null){
//	            Log.v(LOGCAT,"Content-Disposition:"+value);
//	            value=value.substring(value.lastIndexOf('=')+1);
//	            Log.v(LOGCAT,"Content-Disposition fileName:"+value);
//         }
//         if(value==null){
//         	value=appdb.getApkUrl().substring(appdb.getApkUrl().lastIndexOf('/')+1);
//	            Log.v(LOGCAT,"Url fileName:"+value);
//         }
//         if(value==null){
//         	value=appdb.getApkName()+".apk";
//	            Log.v(LOGCAT,"apkName fileName:"+value);
//         }
//         appdb.setApkFileName(value);
		
		// saveFile = new File(path + "/"+ att.getRealname());
//        File dir=new File("/sdcard/data");//new File(appdb.getApkFilePath());
//        if(!dir.exists()){
//        	dir.mkdirs();
//        }
//        String fileName=null;
//        if(fileName==null){
//        	Log.i(LOGCAT,"fileName is null");
//        	 fileName = appdb.getApkUrl().substring(appdb.getApkUrl().lastIndexOf('/') + 1);
//        }
//        Log.i(LOGCAT,"fileName:"+fileName);
//		saveFile = new File(dir,appdb.getApkFileName());
//		Log.v(LOGCAT,"file write:"+saveFile.canWrite());
//		out= new FileOutputStream(saveFile);
//		Log.v(LOGCAT,"temp:" + saveFile.getName());
		return System.currentTimeMillis()+".apk";
	}

//	private void setApkRuning(boolean value) {
//		synchronized (apkruninglock) {
//			apkruning = value;
//		}
//	}
}
