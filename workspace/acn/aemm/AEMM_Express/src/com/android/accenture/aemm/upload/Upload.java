package com.android.accenture.aemm.upload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

@Deprecated
public class Upload {
	public static final String LOGCAT="upLoad";
	String uploadUrl = "http://aemm.imolife.com/echo.aspx";
	public Upload(String url){
		uploadUrl=url;
	}
	
	public void connect(InputStream is){
		try{
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			//httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			//httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type","text/xml");
		
			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
			//dos.writeBytes(twoHyphens + boundary + end);
			//dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+ filename.substring(filename.lastIndexOf("/") + 1)+ "\"" + end);
			//dos.writeBytes(end);

			//FileInputStream fis = new FileInputStream(filename);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = is.read(buffer)) != -1){
				dos.write(buffer, 0, count);
				Log.v(LOGCAT,"count :"+count);

			}
			is.close();
			//dos.writeBytes(end);
			//dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			Log.v(LOGCAT,"flush");
			dos.flush();
			dos.close();


			InputStream bis = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(bis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();
	
			Log.i(LOGCAT,result);
			bis.close();
		}catch (Exception e){
			Log.e(LOGCAT,e.getMessage());
		}
	}
}
