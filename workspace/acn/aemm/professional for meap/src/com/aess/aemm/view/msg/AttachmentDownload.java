package com.aess.aemm.view.msg;

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

import com.aess.aemm.db.NewsContent;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.DownloadHandler;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.protocol.DomXmlBuilder;

public class AttachmentDownload  extends Thread{

		public static final String TAG = "DownloadThread";
		
		private Context mContext = null;
		private HttpURLConnection mConn = null;
		private HttpsURLConnection mConns = null;
		String addr ;
		String fileName;
		DownloadHandler mHandler;

		AttachmentDownload(Context context,String addr,String fileName, DownloadHandler mHandler) {
			mContext = context;
			this.addr=addr;
			this.mHandler=mHandler;
			this.fileName=fileName;
			setName("DownloadThread");
		}

		@SuppressLint("WorldReadableFiles")
		public void run() {
			Log.i(TAG, "Thread Begin");

			URL url;
//			if (AEMMConfig.VER >= AEMMConfig.NUM) {
//				InputStream is = HttpHelp.aemmHttpGet(mContext, addr, "attachmentadr");
//			}
			
			int resultFlag = DownloadHandler.FLAG_OPENFILE_FAILED;
			if (null != addr) {
				Log.i(TAG, addr);
				
				try {
					url = new URL(addr);

					InputStream in;
					int contentLength;
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

					if (HttpURLConnection.HTTP_OK == responseCode) {
						// Try using external media, then internal media
//						File f = mContext.getExternalFilesDir(null);
						FileOutputStream fo;
						if (fileName == null) {
							fileName = Thread.currentThread().toString()+ System.currentTimeMillis();
						}
//						if (f != null) {
//							fileName = f.getAbsolutePath() + "/" + fileName;
//							fo = new FileOutputStream(fileName);
//						} else {
							fo = mContext.openFileOutput(fileName,Context.MODE_WORLD_READABLE);
//							fileName = mContext.getFilesDir() + "/" + fileName;
//						}
						int length;
						int receivedBytes = 0;
						int notifyBytes = 0;
						byte[] buffer = new byte[1024];
						while ((length = in.read(buffer)) > 0) {
							fo.write(buffer, 0, length);
							receivedBytes += length;
							notifyBytes += length;
							if (notifyBytes >= 150 * 1024) {
								Message msg=new Message();
								msg.what=DownloadHandler.FLAG_DOWNLOAD_ING;
								msg.arg1=contentLength;
								msg.arg2=receivedBytes;
								msg.obj=this;
								mHandler.sendMessage(msg);
								notifyBytes = 0;
							}
						}
						fo.close();
						resultFlag = DownloadHandler.FLAG_DOWNLOAD_SUCCESS;
					} else {
						AutoAdress.intentForNewUpdateURL(mContext);
					}
					in.close();
				} catch (MalformedURLException e) {
					resultFlag = DownloadHandler.FLAG_URL_EXCEPTION;
				} catch (FileNotFoundException e) {
					resultFlag = DownloadHandler.FLAG_OPENFILE_FAILED;
				} catch (IOException e) {
					resultFlag = DownloadHandler.FLAG_IO_EXCEPTION;
				} catch (Exception e) {
					resultFlag = DownloadHandler.FLAG_DOWNLOAD_EXCEPTION;
				}
			}

			Log.i(TAG, "resultFlag " + resultFlag+":"+fileName);
			if ( DownloadHandler.FLAG_DOWNLOAD_SUCCESS == resultFlag) {
//				Update.responseCommandId(mContext, cmdid);
			}else{
				AutoAdress address = AutoAdress.getInstance(mContext);
				String addr = address.getUpdateURL();
				mHandler.attch.mState=NewsContent.MSG_LOADFAIL;

				Log.d(TAG, "submit conment to -> " + addr);
				if (null != addr) {
					String lgInfo = DomXmlBuilder.buildInfo(mContext,false, DomXmlBuilder.ATTACHMENTRES,mHandler.attch);
					Log.d(TAG, "sendAttachment XmlBuilder.buildInfo == " + lgInfo);
					InputStream upResult = HttpHelp.aemmHttpPost(mContext, addr, lgInfo,"/sdcard/comment.txt");
				}
			}
			Message msg=mHandler.obtainMessage(resultFlag);
			msg.obj=this;
			mHandler.sendMessage(msg);
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