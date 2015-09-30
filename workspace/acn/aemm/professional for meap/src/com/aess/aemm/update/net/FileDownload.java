package com.aess.aemm.update.net;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.aess.aemm.networkutils.NetUtils;

public class FileDownload  extends Thread implements NetFlag{

		public static final String TAG = "DownloadThread";
		
		private Context mContext = null;
		private HttpURLConnection mConn = null;
		private HttpsURLConnection mConns = null;
		private String addr ;
		private String fileName;
		private Message msg;
		private int contentLength;
		private int receivedBytes = 0;
		private int resultFlag = FLAG_OPENFILE_FAILED;
		public Object flag;
		public View vFlag;

		/**
		 * @param context
		 * @param addr
		 * @param fileName
		 * @param msg you must set msg'what,
		 * @param v
		 */
		public FileDownload(Context context,String addr,String fileName, Message msg,View v) {
			mContext = context;
			this.addr=addr;
			this.msg=msg;
			this.fileName=fileName;
			this.vFlag=v;
			setName("DownloadThread");
		}
		
		public int getContentLength(){
			return contentLength;
		}
		
		public int getReceivedBytes(){
			return receivedBytes;
		}

		@SuppressLint("WorldReadableFiles")
		public void run() {
			Log.i(TAG, "Thread Begin");
			
//			try {
//
//				contentLength=100;
//				receivedBytes = 0;
//				for(int i=0;i<10;i++){
//					Thread.sleep(1000);
//					receivedBytes+=10;
//						msg.arg1=DOWNLOAD_STATE_ING;
////						msg.arg2=receivedBytes;
//						msg.obj=this;
////						msg.sendToTarget();
//						Log.d(TAG,"download send msg "+i);
//						Message newMsg=new Message();
//						newMsg.copyFrom(msg);
//						msg.getTarget().sendMessage(newMsg);
//				}
//			} catch (InterruptedException e1) {
//				e1.printStackTrace();
//			}

			URL url;
//			if (AEMMConfig.VER >= AEMMConfig.NUM) {
//				InputStream is = HttpHelp.aemmHttpGet(mContext, addr, "attachmentadr");
//			}
			
			if (null != addr) {
				Log.i(TAG, addr);
				
				try {
					url = new URL(addr);

					InputStream in;
					int responseCode;
					if (NetUtils.isHttps(addr)) {
						trustAllHosts();
						mConns = (HttpsURLConnection) url.openConnection();
						mConns.setHostnameVerifier(DO_NOT_VERIFY);
						in = mConns.getInputStream();
						contentLength = mConns.getContentLength();
						responseCode = mConns.getResponseCode();
					} else {
						mConn = (HttpURLConnection) url.openConnection();
						in = mConn.getInputStream();
						contentLength = mConn.getContentLength();
						responseCode = mConn.getResponseCode();
					}
					
					if(contentLength<=0){
						resultFlag = FLAG_DOWNLOAD_ZERO;
						Log.i(TAG, "contentLength is£º " + contentLength);
					}else if (HttpURLConnection.HTTP_OK == responseCode) {
						// Try using external media, then internal media
//						File f = mContext.getExternalFilesDir(null);
						FileOutputStream fo;
//						if (fileName == null) {
//							fileName = Thread.currentThread().toString()+ System.currentTimeMillis();
//						}
//						if (f != null) {
//							fileName = f.getAbsolutePath() + "/" + fileName;
//							fo = new FileOutputStream(fileName);
//						} else {
							fo = mContext.openFileOutput(fileName,Context.MODE_WORLD_READABLE);
//							fileName = mContext.getFilesDir() + "/" + fileName;
//						}
						int length;
						int notifyBytes = 0;
						byte[] buffer = new byte[1024];
						while ((length = in.read(buffer)) > 0) {
							fo.write(buffer, 0, length);
							receivedBytes += length;
							notifyBytes += length;
							if (msg!=null&&notifyBytes >= 150 * 1024) {
								Log.d(TAG,contentLength+" / "+receivedBytes);
								msg.arg1=FLAG_DOWNLOAD_ING;
//								msg.arg2=receivedBytes;
								msg.obj=this;
								Message newMsg=new Message();
								newMsg.copyFrom(msg);
								msg.getTarget().sendMessage(newMsg);
								notifyBytes = 0;
							}
						}
						fo.close();
						resultFlag = FLAG_DOWNLOAD_SUCCESS;
					}
					in.close();
				} catch (MalformedURLException e) {
					resultFlag = FLAG_URL_EXCEPTION;
				} catch (FileNotFoundException e) {
					resultFlag = FLAG_OPENFILE_FAILED;
				} catch (IOException e) {
					resultFlag = FLAG_IO_EXCEPTION;
				} catch (Exception e) {
					resultFlag = FLAG_DOWNLOAD_EXCEPTION;
				}
			}

			Log.i(TAG, "resultFlag " + resultFlag+":"+fileName);
			if(msg!=null){
				msg.obj=this;
				msg.arg1=resultFlag;
				msg.sendToTarget();
			}
		}
		private static void trustAllHosts() {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {

				}
			} };

			try {
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection
						.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

}