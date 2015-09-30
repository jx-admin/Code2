package com.xuye;

import java.lang.String;
import java.lang.Object;
import java.io.ByteArrayOutputStream;
import android.os.RemoteException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.os.ParcelFileDescriptor;
import android.app.IActivityManager;
import android.app.ActivityManagerNative;
import android.net.Uri;
import android.util.Log;

public class TestJNI {
    private static String TAG = "TestJNI_JAVA";
    public static native void helloJNI(int value);
    public static native void helloJNI2(int value);
    public static native boolean isEnableDial();
    public static boolean isEnableDial2(){
        boolean enabled = false;
        IActivityManager am = ActivityManagerNative.getDefault();
        try {
            Uri uri = Uri.parse("content://com.xuye.TestContentProvider/aemmdial");
            ParcelFileDescriptor fd = am.openContentUri(uri);
            FileInputStream fis = new FileInputStream(fd.getFileDescriptor());
            byte[] benable = new byte[1];
            benable[0] = 0x00;
            //fos.write("camera is not permitted".getBytes());
            fis.read(benable);
            fis.close();
            if(benable[0] == 0)
                enabled = true;
        } catch (RemoteException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return enabled;
    }
    public static native int openFile(String fileName);
    public static native Object openFile2(String filename);
    public static native int readFile(int fd, byte[] buffer);
    public static native void closeFile(int fd);
    public static boolean isAemmComponent(String component) {
        int fd = openFile("aemmcomponent");
        byte[] buffer = new byte[1];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while(readFile(fd, buffer) > 0) {
            if(buffer[0] == (byte)'\n') {
                String line = new String(bos.toByteArray());
                Log.i(TAG, line);
                if(line.compareTo(component) == 0) {
                    closeFile(fd);
                    return true;
                }
                Log.i(TAG, "NOT matched");
                bos = new ByteArrayOutputStream();
                continue;
            }
            try {
                bos.write(buffer);
            } catch (IOException e) {
                break;
            }
        }
        closeFile(fd);
        return false;
    }
}