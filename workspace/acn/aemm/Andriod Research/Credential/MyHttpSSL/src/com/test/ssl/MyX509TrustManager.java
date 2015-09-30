package com.test.ssl;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager {
	 X509TrustManager pkixTrustManager;
	 public MyX509TrustManager(InputStream trustStore, char[] password) throws Exception {  
		 // create a "default" JSSE X509TrustManager.  
		 KeyStore ks = KeyStore.getInstance("BKS");   
		 ks.load(trustStore, password);         
		 TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");  
		 tmf.init(ks);        
		 TrustManager tms[] = tmf.getTrustManagers();          
		 /*          * Iterate over the returned trustmanagers, look for an instance of      
		  *     * X509TrustManager. If found, use that as our "default" trust manager.       
		  *        */       
		 for (int i = 0; i < tms.length; i++) {   
			 if (tms[i] instanceof X509TrustManager) {    
				 pkixTrustManager = (X509TrustManager) tms[i];      
				 return;            
				 }        
			 }          
		 /*          * Find some other way to initialize, or else we have to fail the    
		  *       * constructor.          */        
		 throw new Exception("Couldn't initialize");    
		 } 
	
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		   try {         
			   pkixTrustManager.checkClientTrusted(chain, authType);   
			   } catch (CertificateException excep) {   
				   // do any special handling here, or rethrow exception.        
				 } 
	}


	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		 try {            
			 pkixTrustManager.checkServerTrusted(chain, authType); 
		 } 
		 catch (CertificateException excep) { 
			 /*              * Possibly pop up a dialog box asking whether to trust the cert     
			  *          * chain.              */         
			 }  

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return pkixTrustManager.getAcceptedIssuers(); 
	
	}

}
