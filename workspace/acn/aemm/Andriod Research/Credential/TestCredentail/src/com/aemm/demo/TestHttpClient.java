package com.aemm.demo;

import java.io.BufferedReader;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLPeerUnverifiedException;
//import java.security.cert.Certificate;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.security.Credentials;
import android.security.KeyStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TestHttpClient extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Instantiate the custom HttpClient 
        Button btn_goto = (Button)findViewById(R.id.testSSL);
        btn_goto.setOnClickListener(gotoListener);
        
        Button btn_saw= (Button)findViewById(R.id.testSaw);
        btn_saw.setOnClickListener(checkKeystoreListener);
        
    
        
    }
    private KeyStore mKeyStore;
    public static final String INSTALL_ACTION = "android.credentials.INSTALL";
        private OnClickListener gotoListener = new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		 Intent intent = new Intent(INSTALL_ACTION);
        	        intent.setClassName("com.aemm.demo",
        	                "com.aemm.demo.CertInstallerMain");
        	        startActivity(intent);
        		 
        		
        	
        		/*try{
                	DefaultHttpClient client = new MyHttpClient(getApplicationContext());
                	//DefaultHttpClient client = new DefaultHttpClient();
                	Log.i("TestHttpClient","new");
                	HttpGet get = new HttpGet("https://aemm1.imolife.com/auth"); //https://aemm1.imolife.com/auth
                	Log.i("TestHttpClient","HttpGet");
                	// Execute the GET call and obtain the response 
                	 //ResponseHandler  

                	ResponseHandler<String> responseHandler = new BasicResponseHandler();  
                	
                	//HttpResponse getResponse = client.execute(get);
                	String content = client.execute(get,responseHandler);
                	Log.i("TestHttpClient","HttpResponse");
                	/*HttpEntity responseEntity = getResponse.getEntity(); 
                	Log.i("TestHttpClient","HttpResponse1");
                	InputStream st = responseEntity.getContent();
                	Log.i("TestHttpClient",st.toString());
                	BufferedReader reader = new BufferedReader(new InputStreamReader(st));
                	String line = null;
                	while ((line = reader.readLine()) != null) {
                		Log.i("TestHttpClient","line" + line);
                	//System.out.println(line);
                	}
                	if (responseEntity != null) {
                		responseEntity.consumeContent();
                	}

                }catch(Exception e)
                {
                	e.printStackTrace();
                }*/
        		
        		
        		
        				
        		
        	}

    		
        };
    
        private OnClickListener checkKeystoreListener = new OnClickListener()
        {
        	public void onClick(View v)
        	{
        	
        		mKeyStore = KeyStore.getInstance();
        		String [] buffer = mKeyStore.saw(Credentials.CA_CERTIFICATE);
        		Log.i("printf",buffer[0]);
        		Log.i("printf",buffer[1]);
        		
           		
        	}
    		
        };
     void installFromSDCard()
     {
    	 
    	 
     }
    
}
    //DefaultHttpClient client = 
