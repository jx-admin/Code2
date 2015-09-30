
package com.accenture.mbank.net;

import it.gruppobper.ams.android.bper.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class HttpConnector {
    public static Object lock = new Object();

    public static final int HTTP_TIMEOUT = 30 * 1000; // ms

//    private static HttpClient mHttpClient = new DefaultHttpClient();
    private static HttpClient mHttpClient = null;

    
    static {

    	mHttpClient = new DefaultHttpClient();

        final HttpParams params = mHttpClient.getParams();

        HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
        ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        
        
        if(Contants.IS_SSL_TO_BYPASS) {

        	SchemeRegistry sSupportedSchemes = new SchemeRegistry();
	        
	        sSupportedSchemes.register(new Scheme("http",
	                PlainSocketFactory.getSocketFactory(), 80));
	            sSupportedSchemes.register(new Scheme("https",
	                TrustAllSSLSocketFactory.getSocketFactory(), 443));
	
	        SingleClientConnManager mgr = new SingleClientConnManager(params, sSupportedSchemes);
	        mHttpClient = new DefaultHttpClient(mgr, params);
        
        }
    }
    

    // HttpPost方式请求
    public String requestByHttpPost(String url, String postdata, Context context) {
        LogManager.d(url);
        LogManager.d("post" + postdata);
        synchronized (lock) {
            BasicHttpContext localContext = new BasicHttpContext();    
            localContext.setAttribute(ClientContext.COOKIE_STORE, Contants.cookieStore);
            HttpPost httpRequest = new HttpPost(url);
            try {
                StringEntity stringEntity = new StringEntity(postdata, HTTP.UTF_8);
                httpRequest.setEntity(stringEntity);
                httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpRequest.setHeader("iv-user",Contants.userName);
                HttpResponse rsp = mHttpClient.execute(httpRequest,localContext);
                
                int code = rsp.getStatusLine().getStatusCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    HttpEntity httpEntity = rsp.getEntity();
                    String s = EntityUtils.toString(httpEntity);
                    try{
	                    JSONObject jsonObj = new JSONObject(s);
	                    
	                    Iterator jsonObjIterator = jsonObj.keys();
		                JSONObject eventManagementObject = jsonObj.getJSONObject((String) jsonObjIterator.next());
		                int resultCode = eventManagementObject.getInt("resultCode");
		                if(resultCode != 0){
		                    JSONObject eventManagement=	eventManagementObject.getJSONObject("eventManagement");
		                    if (!url.contains("/login")) {
		                    	try{
		                    		jsonObj.getJSONObject("LoginResponse");
			                    	 BaseActivity baseActivity = (BaseActivity)context;
			                         baseActivity.displayErrorMessage(Contants.ERR_SESSION_EXPIRED_1,context.getResources().getString(R.string.session_expired));
	
			                    }
			                    catch(Exception e){
			                    	String errorCode = eventManagement.getString("errorCode");
				                    if (s != null && errorCode != null && !errorCode.equals("")) {
				                        if (errorCode.contains(Contants.ERR_SESSION_EXPIRED_1) || errorCode.contains(Contants.ERR_SESSION_EXPIRED_2)) {
				                            BaseActivity baseActivity = (BaseActivity)context;
				                            baseActivity.displayErrorMessage(Contants.ERR_SESSION_EXPIRED_1,context.getResources().getString(R.string.session_expired));
				                        }
				                    }
			                    }
							} else{
								String errorCode = eventManagement.getString("errorCode");
			                    if (s != null && errorCode != null && !errorCode.equals("")) {
			                        if (errorCode.contains(Contants.ERR_SESSION_EXPIRED_1) || errorCode.contains(Contants.ERR_SESSION_EXPIRED_2)) {
			                            BaseActivity baseActivity = (BaseActivity)context;
			                            baseActivity.displayErrorMessage(Contants.ERR_SESSION_EXPIRED_1,context.getResources().getString(R.string.session_expired));
			                        }
			                    }
							}
	                    
	                    }
                    } catch(Exception e){
                    	
                    	LogManager.e("Exception " + e);
//                    	if (s != null && s.contains("errorCode")) {
//                            if (s.contains(Contants.ERR_SESSION_EXPIRED_1) || s.contains(Contants.ERR_SESSION_EXPIRED_2)) {
                                BaseActivity baseActivity = (BaseActivity)context;
                                baseActivity.displayErrorMessage(Contants.ERR_SESSION_EXPIRED_1,context.getResources().getString(R.string.session_expired));
//                            }
                
//                        }
                    }
                    

					if (s != null && s.contains("errorCode") && s.contains("Service execution failed"))
						LogManager.e("response:" + s);
					else
						LogManager.d("response:" + s);
					return s;
                }
                else {
					LogManager.e("HTTP Status:" + code);
					if (context instanceof BaseActivity) {
						BaseActivity baseActivity = (BaseActivity) context;

						switch (code) {
						case HttpURLConnection.HTTP_BAD_REQUEST: // 400
						case HttpURLConnection.HTTP_UNAUTHORIZED: // 401
						case HttpURLConnection.HTTP_FORBIDDEN: // 403
						case HttpURLConnection.HTTP_UNAVAILABLE: // 503
							baseActivity.displayErrorMessage("",baseActivity.getString(R.string.service_unavailable));
							break;

						case HttpURLConnection.HTTP_INTERNAL_ERROR: // 500
						case HttpURLConnection.HTTP_NOT_FOUND: // 404
							baseActivity.displayErrorMessage("",baseActivity.getString(R.string.server_connect_error));
							break;
						}
					}
				}
            } catch (Exception e) {
            	
            	LogManager.e("Exception " + e);
            	
                if (context instanceof BaseActivity) {
                    BaseActivity baseActivity = (BaseActivity)context;
                    if (postdata != null && postdata.contains(ServiceType.getAdvNews)) {
                        return null;
                    }
                    if ((e instanceof ConnectTimeoutException) || (e instanceof SocketTimeoutException)) {
                        baseActivity.displayErrorMessage("",baseActivity.getString(R.string.server_connect_error));
                    } else if (e instanceof HttpHostConnectException || e instanceof UnknownHostException) {
                        baseActivity.displayErrorMessage("",baseActivity.getString(R.string.network_not_available));
                    } else if (e instanceof SSLException) {
                    	baseActivity.displayErrorMessage("",baseActivity.getString(R.string.service_unavailable));
                    }
                }
                if (e != null) {
                	LogManager.e("requestByHttpPost fail:" + e.getLocalizedMessage());
                }
            } finally {
                httpRequest.abort();
            }
        }
        return null;
    }

    public String requestByHttpGet(String url) {
        String strResult = null;
        HttpGet httpRequest = new HttpGet(url);
        try {
            // 取得HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();
            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                // 取得返回的字符串
                strResult = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            } else {
                return null;
            }
        } catch (ClientProtocolException e) {
            e.getLocalizedMessage();
        } catch (IOException e) {
            e.getLocalizedMessage();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return strResult;
    }

    /**
     * Get image from newwork
     * 
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * Get image from newwork
     * 
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    /**
     * Get data from stream
     * 
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }
    
    private static final class TrustAllSSLSocketFactory implements
    LayeredSocketFactory {

    private static final TrustAllSSLSocketFactory DEFAULT_FACTORY = new TrustAllSSLSocketFactory();

    public static TrustAllSSLSocketFactory getSocketFactory() {
        return DEFAULT_FACTORY;
    }

    private SSLContext sslcontext;
    private javax.net.ssl.SSLSocketFactory socketfactory;

    private TrustAllSSLSocketFactory() {
        super();
        TrustManager[] tm = new TrustManager[] { new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
                // do nothing
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
                // do nothing
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        } };
        try {
            this.sslcontext = SSLContext.getInstance(SSLSocketFactory.TLS);
            this.sslcontext.init(null, tm, new SecureRandom());
            this.socketfactory = this.sslcontext.getSocketFactory();
        } catch ( NoSuchAlgorithmException e ) {
            LogManager.e(
                "Failed to instantiate TrustAllSSLSocketFactory!" + e);
        } catch ( KeyManagementException e ) {
            LogManager.e(
                "Failed to instantiate TrustAllSSLSocketFactory!" + e);
        }
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
        boolean autoClose) throws IOException, UnknownHostException {
        SSLSocket sslSocket = (SSLSocket) this.socketfactory.createSocket(
            socket, host, port, autoClose);
        return sslSocket;
    }

    @Override
    public Socket connectSocket(Socket sock, String host, int port,
        InetAddress localAddress, int localPort, HttpParams params)
        throws IOException, UnknownHostException, ConnectTimeoutException {
        if ( host == null ) {
            throw new IllegalArgumentException(
                "Target host may not be null.");
        }
        if ( params == null ) {
            throw new IllegalArgumentException(
                "Parameters may not be null.");
        }

        SSLSocket sslsock = (SSLSocket) ( ( sock != null ) ? sock
            : createSocket() );

        if ( ( localAddress != null ) || ( localPort > 0 ) ) {

            // we need to bind explicitly
            if ( localPort < 0 ) {
                localPort = 0; // indicates "any"
            }

            InetSocketAddress isa = new InetSocketAddress(localAddress,
                localPort);
            sslsock.bind(isa);
        }

        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        int soTimeout = HttpConnectionParams.getSoTimeout(params);

        InetSocketAddress remoteAddress;
        remoteAddress = new InetSocketAddress(host, port);

        sslsock.connect(remoteAddress, connTimeout);

        sslsock.setSoTimeout(soTimeout);

        return sslsock;
    }

    @Override
    public Socket createSocket() throws IOException {
        // the cast makes sure that the factory is working as expected
        return (SSLSocket) this.socketfactory.createSocket();
    }

    @Override
    public boolean isSecure(Socket sock) throws IllegalArgumentException {
        return true;
    }

}
    
}
