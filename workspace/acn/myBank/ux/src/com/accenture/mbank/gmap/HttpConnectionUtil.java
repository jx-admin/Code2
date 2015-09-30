
package com.accenture.mbank.gmap;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.google.android.maps.GeoPoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpConnectionUtil extends Thread {

    private Context mContext;

    private Handler mHandler;

    private String mUrl; // 请求地址

    private Map<String, String> mParamMap; // 请求参数

    public static final int SUCCESS = 0;

    public static final int ERROR = 1;
    
    public static final int SUCCESS_GEOCODER = 2;

    public HttpConnectionUtil(Context context, Handler handler, String url,
            Map<String, String> paramMap) {
        this.mContext = context;
        this.mHandler = handler;
        this.mUrl = url;
        this.mParamMap = paramMap;
    }

    public GeoPoint doStart() {
        // mProgressDialog = new ProgressDialog(mContext);
        // mProgressDialog.setMessage("loading");
        // mProgressDialog.show();
        return getGeoPoint();
    }

    public GeoPoint getGeoPoint() {
        try {
            String response = callWebService();
            Message message = mHandler.obtainMessage();
            Bundle b = new Bundle();
            message.what = SUCCESS; // 这里是消息的类型
            b.putString("response", response);
            GeocodeResult geocodeResult = new GeocodeResult();
            geocodeResult.setValues(response);
            message.setData(b);
            mHandler.sendMessage(message);
            return geocodeResult.getGeoPoint();

        } catch (Exception ex) {
            Message message = mHandler.obtainMessage();
            Bundle b = new Bundle();
            message.what = ERROR;
            message.setData(b);
            mHandler.sendMessage(message);
            return null;
        }

    }

    private String callWebService() throws Exception {
        String requestUrl = parserReqParam(mUrl, mParamMap);
        Log.d("map", requestUrl);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        String bodyString = "";
        try {
            setConnectionTimeOut(httpClient);
            response = httpClient.execute(new HttpGet(requestUrl));
            System.out.println("status: " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                bodyString = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("HTTP Proxy ：" + response.getStatusLine().getStatusCode(), e);
        }
        return bodyString;
    }

    /**
     * Desc: 设置请求超时
     * 
     * @param httpClient
     */
    private void setConnectionTimeOut(HttpClient httpClient) {
        HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
    }

    /**
     * Desc:拼装请求参数
     * 
     * @param url
     * @param paramMap
     * @return
     */
    private String parserReqParam(String url, Map<String, String> paramMap) {
        StringBuffer requestUrl = new StringBuffer();
        requestUrl.append(url);
        for (String key : paramMap.keySet()) {
            String value = paramMap.get(key);
            requestUrl.append(key).append("=").append(value).append("&");
        }
        return requestUrl.substring(0, requestUrl.length() - 1);

    }
}
