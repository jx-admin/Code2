package com.android.accenture.aemm.express;

import java.net.HttpURLConnection;
import java.net.URL;

@Deprecated
public class HttpUtils {

	public void http(){
		URL url = null;
		HttpURLConnection httpurlconnection = null;
		try
		{
		url = new URL("http://aemm2.imolife.com/demo/echo.aspx"/*"http://10.90.3.17:8080/conn/t.txt"*/);

		httpurlconnection = (HttpURLConnection) url.openConnection();
		httpurlconnection.setDoOutput(true);
		httpurlconnection.setRequestMethod("POST");
		httpurlconnection.setRequestProperty("connection", "keep-alive");
		httpurlconnection.setRequestProperty("Charsert", "UTF-8");  
		String username="username=02000001";
		httpurlconnection.getOutputStream().write(username.getBytes());
		httpurlconnection.getOutputStream().flush();
		httpurlconnection.getOutputStream().close();
		int code = httpurlconnection.getResponseCode();
		System.out.println("code " + code);

		}
		catch(Exception e)
		{
		e.printStackTrace();
		}
		finally
		{
		if(httpurlconnection!=null)
		httpurlconnection.disconnect();
		}

	}
}
