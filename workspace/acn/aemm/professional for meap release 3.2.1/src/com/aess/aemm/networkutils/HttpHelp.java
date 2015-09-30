package com.aess.aemm.networkutils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.aess.aemm.AEMMConfig;
import com.aess.aemm.commonutils.FileHelp;
import android.content.Context;
import android.util.Log;

public class HttpHelp {

	public final static String POST = "POST";
	public final static String GET = "GET";

	public static String SENGNAME = "post.xml";
	public static String RESPNAME = "responce.xml";
	public static String GETNAME = "responce.xml";
	public static String EXCEPTIONSTRING = "error.info";
	
	
	public static final String PLOGCAT = "aemmHttpPost";
	public static final String GLOGCAT = "aemmHttpGet";
	
	public static final String SSL_PROTOCOL = "TLS";
	public static final String KEY_MANAGER = "X509";
	public static final String KEY_KEYSTORE = "BKS";
	
	public static final String TRUST_MANAGER = "X509";
	
	public static InputStream aemmHttpGet(Context cxt, String uri, String logFile) {

		if (null == uri || null == cxt) {
			return null;
		}
		if (!NetUtils.isNetOK(cxt)) {
			return null;
		}

		Log.d(GLOGCAT, uri);
		
		int debugType = 0;
		if (logFile != null) {
			debugType = AEMMConfig.DEBUG;
		}
		ContextLen = 0;
		return httpGet(cxt, uri, logFile, debugType);
	}
	
	public static int aemmHttpGetContextLen() {
		return ContextLen;
	}

	public static InputStream aemmHttpPost(Context cxt, String uri,
			String data, String debugFileName) {

		if (null == uri || null == data || null == cxt) {
			return null;
		}

		if (!NetUtils.isNetOK(cxt)) {
			return null;
		}

		Log.d(HttpHelp.PLOGCAT, uri);
		
		int debugType = 0;
		if (debugFileName == null) {
			debugType = 0;
		} else {
			debugType = AEMMConfig.DEBUG;
		}
		ContextLen = 0;
		return httpPost(cxt, uri, data, debugFileName, debugType);
	}
	
	public static String readData(InputStream is) {
		if (null == is) {
			return null;
		}
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		StringBuffer bf = new StringBuffer();
		String line = null;
		try {
			while ((line = rd.readLine()) != null) {
				bf.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (bf.length()>0) {
			return bf.toString();
		}
		return null;
	}
	
	public static void trustAllHosts() {
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

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String defaultHttpPost(Context cxt, String uri, String data) {
		final String DLOGCAT = "DefaultHttpPost";

		if (null == uri || null == data || null == cxt) {
			return null;
		}
		Log.d(DLOGCAT, uri);

		if (!NetUtils.isNetOK(cxt)) {
			return null;
		}
		Log.d(DLOGCAT, "URI : " + uri);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(uri);
		StringBuffer sb = null;
		try {
			StringEntity se = new StringEntity(data);
			post.setEntity(se);
			HttpResponse resp = client.execute(post);
			HttpEntity entity = resp.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			sb = new StringBuffer();
			String result = null;
			while ((result = reader.readLine()) != null) {
				sb.append(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d(DLOGCAT, "Read Date " + sb.toString());
		return null;
	}
	
	

	private static InputStream httpGet(Context cxt, String uri, String name,
			int debug) {
		HttpURLConnection httpConn = null;
		InputStream urlStream = null;
		try {
			httpConn = getHttpConnection(uri);
			setHttpConnection(httpConn, GET, -1); // -1 means Get method didn't use Content-length

			int responseCode = httpConn.getResponseCode();
			Log.i(GLOGCAT, "Response Code : " + responseCode);
			ContextLen = httpConn.getContentLength();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				
				urlStream = httpConn.getInputStream();
				InputStream resultIs = null;

				if (1 == debug) {
					FileOutputStream os = cxt.openFileOutput(name,
							Context.MODE_PRIVATE);
					inStreamWrite(urlStream, os);
					resultIs = cxt.openFileInput(name);
					os.close();
				} else if (2 == debug) {
					String filePath = FileHelp.getPathInSDCard(name, true);
					FileOutputStream os = new FileOutputStream(new File(filePath));
					inStreamWrite(urlStream, os);
					resultIs = FileHelp.getFileInputStream(filePath);
					os.close();
				} else {
					BufferedInputStream istream = new BufferedInputStream(urlStream);
					resultIs = istream;
				}
				return resultIs;
			} else {
				AutoAdress.intentForNewUpdateURL(cxt);
			}
			return null;
		} catch (Exception e) {
			if (2 == debug) {
				FileHelp.fileSaveInSDCard(cxt, "err"+name, e.toString()
						.getBytes());
			}
			e.printStackTrace();
			return null;
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
	}

	private static InputStream httpPost(Context cxt, String uri,
			String data, String name, int debug) {
		HttpURLConnection httpConn = null;

		try {
			httpConn = getHttpConnection(uri);
			if (null == httpConn) {
				return null;
			}

			byte[] requestBuf = data.getBytes(FileHelp.CHAR_CODING);
			
			if (2 == debug) {
				FileHelp.fileSaveInSDCard(cxt, "send"+name, requestBuf);
			}

			setHttpConnection(httpConn, POST, requestBuf.length);

			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestBuf);
			outputStream.close();

			int responseCode = httpConn.getResponseCode();
			Log.i(PLOGCAT, "ResponseCode : " + responseCode);
			if (HttpURLConnection.HTTP_OK == responseCode) {

				InputStream urlStream = httpConn.getInputStream();
				InputStream resultIs = null;
				
				String rname = "resp" + name;
				if (1 == debug) {
					FileOutputStream os = cxt.openFileOutput(rname,
							Context.MODE_PRIVATE);
					inStreamWrite(urlStream, os);
					resultIs = cxt.openFileInput(rname);
					os.close();
				} else if (2 == debug) {
					String filePath = FileHelp.getPathInSDCard(rname, true);
					FileOutputStream os = new FileOutputStream(new File(filePath));
					inStreamWrite(urlStream, os);
					resultIs = FileHelp.getFileInputStream(filePath);
					os.close();
				} else {
					resultIs = urlStream;
				}
				return resultIs;
			} else {
				AutoAdress.intentForNewUpdateURL(cxt);
			}
			return null;
		} catch (Exception e) {
			if (2 == debug) {
				FileHelp.fileSaveInSDCard(cxt, "err"+name, e.toString()
						.getBytes());
			}
			e.printStackTrace();
			return null;
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
	}

	private static void inStreamWrite(InputStream is, OutputStream os)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = reader.readLine()) != null) {
			os.write(line.getBytes());
		}
	}

	public static HttpURLConnection getHttpConnection(String uri)
			throws IOException {
		HttpURLConnection httpConn = null;

		URL url = null;
		if (false == NetUtils.isHttps(uri)) {
			url = new URL(uri);
			httpConn = (HttpURLConnection) url.openConnection();
		} else {
			url = new URL(uri);
			trustAllHosts();
			HttpsURLConnection httpsConn = (HttpsURLConnection) url
					.openConnection();
			httpsConn.setHostnameVerifier(DO_NOT_VERIFY);
			httpConn = httpsConn;
		}

		return httpConn;
	}

	private static int setHttpConnection(HttpURLConnection httpConn,
			String type, int contentLen) throws ProtocolException,
			UnsupportedEncodingException {
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setUseCaches(false);
		httpConn.setRequestMethod(type);
		if (contentLen >= 0) {
			httpConn.setRequestProperty("Content-length", "" + contentLen);
		}
		httpConn.setRequestProperty("Content-Type", "application/octet-stream");
		//httpConn.setRequestProperty("Connection", "Keep-Alive");
		httpConn.setRequestProperty("Content-Language", java.util.Locale.getDefault().getLanguage()
				+ "-" + java.util.Locale.getDefault().getCountry());
		httpConn.setRequestProperty("Charset", "UTF-8");
		HttpURLConnection.setFollowRedirects(true);
		httpConn.setConnectTimeout(NetUtils.CONNECT_TIMEOUT);
		httpConn.setReadTimeout(NetUtils.READ_TIMEOUT);
		
		return 1;
	}

	private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
	private static int ContextLen = 0;
	
//	public void aemmHttpDownFile(Context context, DownloadParam param, DownloadCallBack callBack) {
//	if(!NetUtils.isNetOK(context)){
//		callBack.downloadResult(param, DOWNLOAD_ERROR_NO_NETWORK);
//		
//		//when network unable, display network error.
//		String info = context.getString(com.aess.aemm.R.string.network_erro);
//		HallMessagedb msg = new HallMessagedb(info, 0, 0, 5, 0);
//		ViewUtils.addResultMessage(msg);
//		ViewUtils.finishUpdateFailed(context);
//		return;
//	}
//	new DownloadThread(context, param, callBack).start();
//}
	
//	public SSLSocketFactory SSLSocketInstance(Context cxt) {
//	String key_password = "1";
//	String trust_password = "2";
//	try {
//		KeyManagerFactory  kmagf = KeyManagerFactory.getInstance(KEY_MANAGER);
//		InputStream kis = cxt.getResources().openRawResource(com.aess.aemm.R.drawable.client);
//		KeyStore kks = KeyStore.getInstance(KEY_KEYSTORE);
//		kks.load(kis, key_password.toCharArray());
//		kmagf.init(kks, key_password.toCharArray());
//		
//		TrustManagerFactory tmagf = TrustManagerFactory.getInstance(TRUST_MANAGER);
//		InputStream tis = cxt.getResources().openRawResource(com.aess.aemm.R.drawable.client);
//		KeyStore tks = KeyStore.getInstance(KEY_KEYSTORE);
//		tks.load(tis, trust_password.toCharArray());
//		tmagf.init(tks);
//		
//		SSLContext sslCxt = SSLContext.getInstance(SSL_PROTOCOL);
//		sslCxt.init(kmagf.getKeyManagers(), tmagf.getTrustManagers(), new SecureRandom());
//		return sslCxt.getSocketFactory();
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	return null;
//}
}
