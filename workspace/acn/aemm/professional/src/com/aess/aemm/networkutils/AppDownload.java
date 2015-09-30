package com.aess.aemm.networkutils;

import java.io.File;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.aess.aemm.AEMMConfig;
import com.aess.aemm.update.Update;
import com.aess.aemm.view.HallMessagedb;
import com.aess.aemm.view.ViewUtils;
import com.aess.aemm.view.data.Appdb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class AppDownload {
	public final static int DOWNLOAD_ERROR_SUCCESS = 0;
	public final static int DOWNLOAD_ERROR_EXCEPTION = -1;
	public final static int DOWNLOAD_ERROR_URL_EXCEPTION = -2;
	public final static int DOWNLOAD_ERROR_IO_EXCEPTION = -3;
	public final static int DOWNLOAD_ERROR_OPENFILE_FAILED = -4;
	public final static int DOWNLOAD_ERROR_NO_NETWORK = -5;

	public final static String TAG = "AppDownload";

	public static class DownloadParam {
		public String mFileName;
		public Appdb appdb;

		public DownloadParam(Appdb value, String fileName) {
			appdb = value;
			mFileName = fileName;
		}
	}

	public static abstract class DownloadCallBack {
		public String mFilePath;

		public abstract void downloadProgress(DownloadParam param,
				int totalBytes, int receivedBytes);

		public abstract void downloadResult(DownloadParam param, int error);
	}

	public void cancel() {

	}
	
	public void downloadFile(Context context, DownloadParam param,
			DownloadCallBack callBack) {
		if (!NetUtils.isNetOK(context)) {
			callBack.downloadResult(param, DOWNLOAD_ERROR_NO_NETWORK);

			// when network unable, display network error.
			String info = context
					.getString(com.aess.aemm.R.string.network_erro);
			HallMessagedb msg = new HallMessagedb(info, 0, 0, 5, 0);
			ViewUtils.addResultMessage(msg);
			ViewUtils.finishUpdateFailed(context);
			return;
		}
		new DownloadThread(context, param, callBack).start();
	}

	private class DownloadThread extends Thread {
		public static final String TAG = "DownloadThread";

		DownloadThread(Context context, DownloadParam param,
				DownloadCallBack callBack) {
			mParam = param;
			mCallBack = callBack;
			mContext = context;
			setName("DownloadThread");
		}

		@SuppressLint("WorldReadableFiles")
		public void run() {
			Log.i(TAG, "Thread Begin");

			URL url;
			String add = mParam.appdb.getApkUrl();
			if (AEMMConfig.VER >= AEMMConfig.NUM) {
				InputStream is = HttpHelp.aemmHttpGet(mContext, add, "apkadr");
				urlParser(is);
				add = _appUrl;
			}
			
			int error = DOWNLOAD_ERROR_OPENFILE_FAILED;
			if (null != add) {
				Log.i(TAG, add);
				
				try {
					url = new URL(add);

					InputStream in;
					int contentLength;
					int responseCode;
					if (NetUtils.isHttps(add)) {
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
						File f = mContext.getExternalFilesDir(null);
						String path = mParam.mFileName;
						FileOutputStream fo;
						if (path == null) {
							path = Thread.currentThread().toString()
									+ System.currentTimeMillis();
						}
						if (f != null) {
							path = f.getAbsolutePath() + "/" + path;
							fo = new FileOutputStream(path);
						} else {
							fo = mContext.openFileOutput(path,
									Context.MODE_WORLD_READABLE);
							path = mContext.getFilesDir() + "/" + path;
						}
						mCallBack.mFilePath = path;
						int length;
						int receivedBytes = 0;
						int notifyBytes = 0;
						byte[] buffer = new byte[1024];
						while ((length = in.read(buffer)) > 0) {
							fo.write(buffer, 0, length);
							receivedBytes += length;
							notifyBytes += length;
							if (notifyBytes >= 150 * 1024) {
								mCallBack.downloadProgress(mParam, contentLength,
										receivedBytes);
								notifyBytes = 0;
							}
						}
						fo.close();
						error = DOWNLOAD_ERROR_SUCCESS;
					} else {
						AutoAdress.intentForNewUpdateURL(mContext);
					}
					in.close();
				} catch (MalformedURLException e) {
					error = DOWNLOAD_ERROR_URL_EXCEPTION;
				} catch (FileNotFoundException e) {
					error = DOWNLOAD_ERROR_OPENFILE_FAILED;
				} catch (IOException e) {
					error = DOWNLOAD_ERROR_IO_EXCEPTION;
				} catch (Exception e) {
					error = DOWNLOAD_ERROR_EXCEPTION;
				}
			}

			Log.i("AppDownload", "error " + error);
			if ( DOWNLOAD_ERROR_SUCCESS == error) {
				String cmdid = _appId + ":1";
				Update.responseCommandId(mContext, cmdid);
			}
			mCallBack.downloadResult(mParam, error);
		}
		private DownloadParam mParam;
		private DownloadCallBack mCallBack;
	}
	
	private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

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
	
	private int urlParser(InputStream is) {
		if (null == is) {
			return -1;
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(is);
			Element root = dom.getDocumentElement();

			if (null == root) {
				return -1;
			}
			_appId = null;
			_appUrl = null;
			_appId = root.getAttribute("commandid");
			_appUrl = root.getAttribute("url");

			if (null != _appUrl && _appUrl.length() > 0) {
				return 1;
			}
			NodeList nl = root.getElementsByTagName("app");
			if (null == nl) {
				return -1;
			}
			
			for (int i = 0; i < nl.getLength(); i++) {
				Element node = (Element) nl.item(i);
				_appId = node.getAttribute("commandid");
				_appUrl = node.getAttribute("url");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
		return 1;
	}
	
	private String _appId = null;
	private String _appUrl = null;
	private Context mContext = null;
	private HttpURLConnection mConn = null;
	private HttpsURLConnection mConns = null;
	

//	public FileOutputStream getFileOutputStream(Context cxt) {
//		FileOutputStream fo = null;
//		try {
//			String name = _param.mFileName;
//			if (name == null) {
//				name = Thread.currentThread().toString()
//						+ System.currentTimeMillis();
//			}
//
//			File file = cxt.getExternalFilesDir(null);
//
//			String path = null;
//			if (null != file) {
//				path = file.getAbsolutePath() + "/" + name;
//				fo = new FileOutputStream(path);
//			} else {
//				fo = cxt.openFileOutput(name, Context.MODE_WORLD_READABLE);
//				path = cxt.getFilesDir() + "/" + name;
//			}
//			_callback.mFilePath = path;
//
//		} catch (FileNotFoundException e) {
//		}
//
//		return fo;
//	}
}