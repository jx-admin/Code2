package com.xuye.TestContentProvider;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import android.content.Context;
import java.io.FileOutputStream;

public class TestContentProvider extends ContentProvider {
	public final static String AUTHORTITY = "com.xuye.TestContentProvider";
	final static String TAG = "TestContentProvider";
	static String CAMERA_RULE_FILE_NAME = "camera";

	@Override
    public ParcelFileDescriptor openFile(Uri uri, String mode)
    	throws FileNotFoundException {
		Log.e( TAG, "uri: " + uri.toString() );

		URI fileURI = URI.create( "file://" + uri.getPath());
		//File file = new File("/data/data/com.xuye.TestContentProvider/files/abc.txt" ); //fileURI
		File file = new File("/data/data/com.xuye.TestContentProvider/" + uri.getPathSegments().get(0));
		Log.i(TAG, "////////////////////");
		Log.i(TAG, file.getPath());
		Log.i(TAG, uri.getPath());
		Log.i(TAG, mode);
		Log.i(TAG, "camera file: " + CAMERA_RULE_FILE_NAME);
		Log.i(TAG, "////////////////////");

		ParcelFileDescriptor parcel = null;
		try {
			if(mode.compareTo("rwt") == 0) {
				parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_TRUNCATE|ParcelFileDescriptor.MODE_CREATE|ParcelFileDescriptor.MODE_READ_WRITE);
			} else if(mode.compareTo("rw") == 0) {
				parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_CREATE|ParcelFileDescriptor.MODE_READ_WRITE);
			} else {
				parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_CREATE|ParcelFileDescriptor.MODE_READ_ONLY);
			}
		} catch (FileNotFoundException e) {
			Log.e( TAG, "Error finding: " + fileURI + "\n" + e.toString() );
		}
		return parcel;
	}

	private boolean matchRule(File file, String rule)
	{
		
		return false;
	}
	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		Log.e( TAG, "uri: " + uri.toString() );
		Log.i(TAG, "////////////////////");
		Log.i(TAG, arg1);
		//Log.i(TAG, arg2[0]);
		Log.i(TAG, "////////////////////");

		String fileName = uri.getPathSegments().get(0);
		File tempFile = new File("/data/data/com.xuye.TestContentProvider/" + fileName + ".temp");
		File file = new File("/data/data/com.xuye.TestContentProvider/" + fileName);
        try {
    		RandomAccessFile rf = new RandomAccessFile(tempFile, "rwd");
    		rf.setLength(0);
    		//rf.write(buffer);
    		
    		FileInputStream fis = new FileInputStream(file);
    		DataInputStream dis = new DataInputStream(fis);
    		String line;
    		while((line = dis.readLine()) != null) {
    			if(line.compareTo(arg1) != 0) {
    				rf.write((line + '\n').getBytes());
    			}
    		}

    		rf.close();
    		dis.close();
    		fis.close();
    		//FileOutputStream fos = new FileOutputStream(file);
        	//fos.write("com.xuye.TestContentProvider 123\n".getBytes());
        	//fos.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		tempFile.renameTo(file);

		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.i( TAG, "insert uri: " + uri.toString() );
		Log.i(TAG, values.toString());
		//Log.i(TAG, values.getAsString("Rule"));

		Log.i(TAG, "calling name: " + getContext().getPackageManager().getNameForUid(Binder.getCallingUid())); 
		int permission = getContext().checkCallingUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		String callingAppName = getContext().getPackageManager().getNameForUid(Binder.getCallingUid());
		if(callingAppName.compareTo("com.xuye.AemmEnableDisable") == 0) {
			Log.i(TAG, "denied");
			//return null;
		}
		//PackageManager
		//Context.this.grantUriPermission(toPackage, uri, modeFlags)
		Log.i(TAG, "permission = " + permission);
		Log.i(TAG, "pid = " + Binder.getCallingPid() + " uid = " + Binder.getCallingUid());
		File file = new File("/data/data/com.xuye.TestContentProvider/" + uri.getPathSegments().get(0));
        try {
        	FileWriter fw = new FileWriter(file, true);
    		fw.write(values.getAsString("Rule") +  "\n");
        	fw.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		//getContext().grantUriPermission("com.xuye.AemmEnableDisable", Uri.parse(AUTHORTITY), Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}