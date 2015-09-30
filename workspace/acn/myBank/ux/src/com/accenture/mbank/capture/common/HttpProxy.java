package com.accenture.mbank.capture.common;


import java.net.HttpURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class HttpProxy {
	
	public static final int HTTP_TIMEOUT = 30 * 1000; // ms

	private static HttpClient mHttpClient = new DefaultHttpClient();

	static {
		final HttpParams params = mHttpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
		ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
	}

	// HttpPost
	public synchronized static String requestByHttpPost(String url, String postdata) {
		HttpPost httpRequest = new HttpPost(url);
		try {
			StringEntity stringEntity = new StringEntity(postdata, HTTP.UTF_8);
			httpRequest.setEntity(stringEntity);
			httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			HttpResponse rsp = mHttpClient.execute(httpRequest);
			int code = rsp.getStatusLine().getStatusCode();
			if (code == HttpURLConnection.HTTP_OK) {
				HttpEntity httpEntity = rsp.getEntity();
				String s=EntityUtils.toString(httpEntity);
				return s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpRequest.abort();
		}
		return null;
	}
}
