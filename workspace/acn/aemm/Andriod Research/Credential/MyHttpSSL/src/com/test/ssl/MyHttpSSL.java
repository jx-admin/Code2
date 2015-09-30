package com.test.ssl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Certificate;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyHttpSSL extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView text = (TextView)findViewById(R.id.textView1);
        //String answer = testconnetc();
        //text.setText(answer);]
        String answer =  addCert();
        text.setText(answer);
			
			
    }
    String addCert()
    {
    	String returnbuffer = null;
    	try{
    		
    		InputStream ins = new FileInputStream("/data/ca.crt");
    		InputStream ins1 = new FileInputStream("/data/tester.p12");
    		
    		//InputStream ins2 = new FileInputStream("/system/etc/security/cacerts.bks");
    		
    		 KeyStore keyStore = KeyStore.getInstance("BKS");  
    		 keyStore.load(null,null);
    		 Log.i("test","load bks ok");
    		 
    		 CertificateFactory cf = CertificateFactory.getInstance("X.509");
    		 X509Certificate cert[] = new X509Certificate[2];
    		 cert[0] = (X509Certificate) cf.generateCertificate(ins);

    		 KeyStore p12KeyStore = KeyStore.getInstance("PKCS12");  
    		 p12KeyStore.load(ins1, "123".toCharArray());
    		 Key privateKey =  p12KeyStore.getKey("le-c85cb6c0-6863-4b8d-a7da-5549660dc1d5", "123".toCharArray());

    		 java.security.cert.Certificate[] certResNull  = p12KeyStore.getCertificateChain("le-c85cb6c0-6863-4b8d-a7da-5549660dc1d5");

    		
    		 keyStore.setCertificateEntry("700", cert[0]);
    		 keyStore.setKeyEntry("700",privateKey,null,certResNull);
    		// ins2.close();
    		
    		 
    		 	KeyManagerFactory tmf = KeyManagerFactory.getInstance("X509");    
				tmf.init(keyStore, null);  
				Log.i("test","gagagaga");
				ByteArrayOutputStream out = null;
				out = new ByteArrayOutputStream();

				keyStore.store(out,null);
				out.close();

				InputStream is = new ByteArrayInputStream(out.toByteArray());   


				SSLContext context = SSLContext.getInstance("TLS");    
				context.init(tmf.getKeyManagers(),new X509TrustManager[] { 
					new MyX509TrustManager(is,null) }, new SecureRandom()); 
				HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());    
				URL url = new URL("https://aemm1.imolife.com/auth");    
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(); 
				connection.setRequestMethod("GET");    
		    
				BufferedReader bin = new BufferedReader(new InputStreamReader(connection.getInputStream()));   
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = bin.readLine()) != null) {   
					Log.i("test",line);
					sb.append(line);  
					
					}  
				returnbuffer = sb.toString();
				ins.close();  
				ins1.close();    
				is.close();  
    		
    	}catch (Exception e) { 
			// should never happen 
			e.printStackTrace();    
			Log.d("Err", e.toString());    
		}
    	return returnbuffer; 
    }
    String  testconnetc()
    {
    	String returnbuffer = null;
    	 try{         
				System.setProperty("http.keepAlive", "false");  
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { 
					@Override
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						return true;
					}                 
					});          
				char[] passwKey = "123456".toCharArray(); 
				
				    KeyStore ts = KeyStore.getInstance("BKS");  
				    /*
				InputStream in = getResources().openRawResource(R.raw.mkeystore);  
				InputStream is = getResources().openRawResource(R.raw.mkeystore);*/
				
				InputStream in = new FileInputStream("/sdcard/mkeystore.bks");
				InputStream is = new FileInputStream("/sdcard/mkeystore.bks");
				ts.load(in, passwKey);      
				KeyManagerFactory tmf = KeyManagerFactory.getInstance("X509");    
				tmf.init(ts, passwKey);  
				Log.i("test","gagagaga");
				SSLContext context = SSLContext.getInstance("TLS");    
				context.init(tmf.getKeyManagers(),new X509TrustManager[] { 
					new MyX509TrustManager(is,"123456".toCharArray()) }, new SecureRandom()); 
				HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());    
				URL url = new URL("https://aemm1.imolife.com/auth");    
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(); 
				connection.setRequestMethod("GET");    
		    
				BufferedReader bin = new BufferedReader(new InputStreamReader(connection.getInputStream()));   
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = bin.readLine()) != null) {   
					Log.i("test",line);
					sb.append(line);  
					
					}  
				returnbuffer = sb.toString();
				in.close();    
				is.close();     
				} catch (Exception e) { 
					// should never happen 
					e.printStackTrace();    
					Log.d("Err", e.toString());    
				}
		return returnbuffer; 
    	
    }
  
}
    	
