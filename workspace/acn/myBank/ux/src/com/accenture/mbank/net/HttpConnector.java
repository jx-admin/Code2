
package com.accenture.mbank.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class HttpConnector {
    public static Object lock = new Object();

    public static final int HTTP_TIMEOUT = 30 * 1000; // ms

    private static HttpClient mHttpClient = new DefaultHttpClient();

    static {
        final HttpParams params = mHttpClient.getParams();

        HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
        ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
    }

    // HttpPost方式请求
    public String requestByHttpPost(String url, String postdata, Context context) {

        LogManager.d("post" + postdata);
        synchronized (lock) {

            HttpPost httpRequest = new HttpPost(url);
            try {
                StringEntity stringEntity = new StringEntity(postdata, HTTP.UTF_8);
                httpRequest.setEntity(stringEntity);
                httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
                HttpResponse rsp = mHttpClient.execute(httpRequest);

                int code = rsp.getStatusLine().getStatusCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    HttpEntity httpEntity = rsp.getEntity();
                    String s = EntityUtils.toString(httpEntity);

                    if (s != null && s.contains("errorCode")) {
                        if (s.contains("errorDescription") && s.contains("Missing sessionId")) {
                            BaseActivity baseActivity = (BaseActivity)context;
                            baseActivity.displayErrorMessage("Missing sessionId");
                        } else if (s.contains("91400")) {
                            BaseActivity baseActivity = (BaseActivity)context;
                            baseActivity.displayErrorMessage("Not valid token");
                        }
                    }
                    // System.out.println("s----------:" + s);
                    LogManager.d("response:" + s);
                    return s;
                }
            } catch (Exception e) {
                if (context instanceof BaseActivity) {
                    BaseActivity baseActivity = (BaseActivity)context;

                    if (postdata != null && postdata.contains(ServiceType.getAdvNews)) {

                        return null;
                    }
                    if (e instanceof ConnectTimeoutException) {
                        baseActivity.displayErrorMessage(baseActivity.getString(R.string.time_out));
                    } else if (e instanceof HttpHostConnectException) {
                        baseActivity.displayErrorMessage(baseActivity
                                .getString(R.string.server_refused));
                    }
                }
                LogManager.e("requestByHttpPost fail:" + e.getLocalizedMessage());
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
}
