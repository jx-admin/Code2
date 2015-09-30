package com.xuye.ThridPartyApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

class NetworkAuthenticate {
	public static final int CONNECT_TIMEOUT = 300000;
	public static final int READ_TIMEOUT = 300000;

	public static int authenticate(String body, Context context) {

		//<login name=¡±xxx¡± pwd=¡±xxx¡± appid=¡±xxx¡± />
		//<login-ret result=¡±xxx¡± message=¡±xxxxx¡± token=¡±xxxxxxx¡± />
		FileInputStream input = downloadProtocol(context, "http://192.168.17.88:8030/TestAppServer.aspx", body);
		int r = parseUpdateXml(input);
		try {
			input.close();
		} catch (IOException e1) {
		}
		return r;
	}
	
	public static FileInputStream downloadProtocol(Context context, String urlPath, String body) {
		if(false) {
			urlPath = urlPath.replace("http://", "https://");
			return httpsDownloadProtocol(context, urlPath, body);
		}

		HttpURLConnection httpConn = null;
		try {
			//String urlPath = checkUpdateUrl;

			URL url = new URL(urlPath);
			httpConn = (HttpURLConnection) url.openConnection();

			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod("POST");

			byte[] requestStringBytes = body.getBytes("utf-8");
			httpConn.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type",
			"application/octet-stream");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("Language", java.util.Locale.getDefault().getLanguage() + "-" + java.util.Locale.getDefault().getCountry());
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
			httpConn.setReadTimeout(READ_TIMEOUT);

			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();

			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				Log.i("HttpURL","HTTP ok");
				FileOutputStream fo = context.openFileOutput("Protocol.xml", Context.MODE_PRIVATE);
				String line="";
				InputStream urlStream;    
				urlStream = httpConn.getInputStream();    
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));   

				while ((line = reader.readLine()) != null) {
					fo.write(line.getBytes());
					Log.d("ProtocolDwonload","response is "+line);
				}
				fo.close();
			}
		} catch (Exception e) {
			Log.d("ProtocolDwonload","response is ");
		} finally {
			//need to close connection
			if (httpConn != null)
				httpConn.disconnect();
		}
		try {
			return context.openFileInput("Protocol.xml");
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// always verify the host - dont check for certificate 
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() { 
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			} 
	}; 
	 
	/** 
	 * Trust every server - dont check for any certificate 
	 */ 
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

	public static FileInputStream httpsDownloadProtocol(Context context, String urlPath, String body) {
		//HttpURLConnection httpConn = null;
		HttpsURLConnection httpConn = null;
		try {
			//String urlPath = checkUpdateUrl;

			URL url = new URL(urlPath);
			trustAllHosts(); 
			httpConn = (HttpsURLConnection) url.openConnection();
			httpConn.setHostnameVerifier(DO_NOT_VERIFY); 

			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod("POST");

			byte[] requestStringBytes = body.getBytes("utf-8");
			httpConn.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type",
			"application/octet-stream");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("Language", java.util.Locale.getDefault().getLanguage() + "-" + java.util.Locale.getDefault().getCountry());
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
			httpConn.setReadTimeout(READ_TIMEOUT);

			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();

			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				Log.i("HttpURL","HTTP ok");
				FileOutputStream fo = context.openFileOutput("Protocol.xml", Context.MODE_PRIVATE);
				String line="";
				InputStream urlStream;    
				urlStream = httpConn.getInputStream();    
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));   

				while ((line = reader.readLine()) != null) {
					fo.write(line.getBytes());
				}
				fo.close();
			}
		} catch (Exception e) {
			Log.i("", "");
		} finally {
			//need to close connection
			if (httpConn != null)
				httpConn.disconnect();
		}
		try {
			return context.openFileInput("Protocol.xml");
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public static int parseUpdateXml(InputStream inStream) {
		//Log.i(TAG, "parseUpdateXml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();

			return parseAuth(root);
			//inStream.close();
		} catch (Exception e) {
		}

		return -1;
	}

	private static int parseAuth(Element root) {
		// authenticate
		//NodeList nl = root.getElementsByTagName("auth");
		
		//<response code="0" />
		
		if(root != null) {
			try {
				return Integer.parseInt(root.getAttribute("code"));
			} catch (Exception e) {
			}
		}
		return -1;
	}
}
