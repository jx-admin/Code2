/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aemm.config_demo;

//import com.android.settings.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.android.accenture.aemm.dome.ApkHall;
import com.android.accenture.aemm.dome.ServiceDia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

class Util {
public static void  onDownloadWifiCfg(Context context) {
    	
        String urlStr="http://aemm-dev.imolife.com/androiddemo/tictactoe.ashx?config=wifi.txt";   
        try {   
            /*  
             * 通过URL取得HttpURLConnection  
             * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置  
             * <uses-permission android:name="android.permission.INTERNET" />  
             */  
            URL url=new URL(urlStr);   
            Log.i("WiFi",  "create url");
            
            HttpURLConnection conn=(HttpURLConnection)url.openConnection(); 
            Log.i("WiFi",  "create httpUrlconnection ");
            
            //取得inputStream，并进行读取   
            InputStream input=conn.getInputStream();   
            Log.i("WiFi",  "get inputstream");
            
            BufferedReader in=new BufferedReader(new InputStreamReader(input));   
            String line=null;   
            StringBuffer sb=new StringBuffer();   
            while((line=in.readLine())!=null){   
                sb.append(":"+line);  
                Log.i(Util.TAG, Util.TAG +" : "+ line);
            }
            Log.i("WiFi",sb.length()+"_"+ sb.toString());   
              String []strLs=sb.toString().split(":");
//              for(int i=0;i<strLs.length;i++){
            	  //if("".strLs[i])
              Log.v("VV","Wifi:"+strLs[2]+":"+ strLs[6]);
            	 if( ApkHall.addWifiConfig(context, strLs[2], strLs[6])){
            		 ServiceDia.showWifiOk(context);
            	 }
//              }
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
}

	/**
	 * 控制台日志，输出标签
	 */
	public static final String TAG = "AEMM_Demo";
	
	
    static void showShortToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static void showShortToastMessage(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    static void showLongToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static void showLongToastMessage(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show();
    }

    static void showErrorMessage(Context c, String message) {
        createErrorDialog(c, message, null).show();
    }

    static void showErrorMessage(Context c, String message,
            DialogInterface.OnClickListener listener) {
        createErrorDialog(c, message, listener).show();
    }

    static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    static void deleteFile(String path, boolean toDeleteSelf) {
        deleteFile(new File(path), toDeleteSelf);
    }

    static void deleteFile(File f) {
        deleteFile(f, true);
    }

    static void deleteFile(File f, boolean toDeleteSelf) {
        if (f.isDirectory()) {
            for (File child : f.listFiles()) deleteFile(child, true);
        }
        if (toDeleteSelf) f.delete();
    }

    static boolean isFileOrEmptyDirectory(String path) {
        File f = new File(path);
        if (!f.isDirectory()) return true;

        String[] list = f.list();
        return ((list == null) || (list.length == 0));
    }

    static boolean copyFiles(String sourcePath , String targetPath)
            throws IOException {
        return copyFiles(new File(sourcePath), new File(targetPath));
    }

    // returns false if sourceLocation is the same as the targetLocation
    static boolean copyFiles(File sourceLocation , File targetLocation)
            throws IOException {
        if (sourceLocation.equals(targetLocation)) return false;

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyFiles(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else if (sourceLocation.exists()) {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        return true;
    }

    public static AlertDialog createErrorDialog(Context c, String message,
            DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder b = new AlertDialog.Builder(c)
                .setTitle(android.R.string.dialog_alert_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message);
        if (okListener != null) {
            b.setPositiveButton(android.R.string.no, okListener);
        } else {
            b.setPositiveButton(android.R.string.ok, null);
        }
        
        b.setNegativeButton(android.R.string.no, null);
        return b.create();
    }

    private Util() {
    }
}
