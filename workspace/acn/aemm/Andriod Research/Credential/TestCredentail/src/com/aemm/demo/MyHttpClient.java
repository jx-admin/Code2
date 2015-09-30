package com.aemm.demo;
import java.io.InputStream;
import java.security.KeyStore;



import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


public class MyHttpClient extends DefaultHttpClient {
	final Context context; 
	public MyHttpClient(Context context)
	{ 
		this.context = context;    
	}   
	@Override 
	protected ClientConnectionManager createClientConnectionManager() { 
		SchemeRegistry registry = new SchemeRegistry();   
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80)); 
		registry.register(new Scheme("https", newSslSocketFactory(), 443)); 
		Log.i("TestHttpClient","new 443 ok");
	 
		return new SingleClientConnManager(getParams(), registry); 
	} 
	private SSLSocketFactory newSslSocketFactory() {  
		try {        
			Log.i("TestHttpClient","newSslSocketFactory");
			KeyStore trusted = KeyStore.getInstance("BKS");

			InputStream in = context.getResources().openRawResource(R.raw.k1);     
			try {          
				Log.i("TestHttpClient","laod");
				trusted.load(in, "123456".toCharArray());  
				Log.i("TestHttpClient","laod ok");
				} finally {            
					in.close();       
				}       
		SSLSocketFactory sf = new SSLSocketFactory(trusted);
		sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER); 
		Log.i("TestHttpClient","new ok");
		// Hostname verification from certificate         // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506         sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);         
		return sf;   
		} catch (Exception e) {  
			Log.i("TestHttpClient","error");
			throw new AssertionError(e);    
			} 
		} 

}
