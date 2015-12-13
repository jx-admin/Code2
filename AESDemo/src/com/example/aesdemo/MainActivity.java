package com.example.aesdemo;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private static final String KEY_ALGORITHM = "AES";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Log.d("dddd", "-------");
		try {
			AESUtil.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AESA2P.main(null);

//		for (int i = 0; i < 10; i++) {
//
//			byte[] key = getKey();
//			printArray(key);
//		}
	}

	private byte[] getKey() {
		KeyGenerator kgen = null;
		try {
			kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kgen.init(128);
		SecretKey secretKey = kgen.generateKey();
		return secretKey.getEncoded();
	}

	private void printArray(byte[] data) {

		Log.d("dddd", "secretKey" + data.length);

		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(b).append(',');
		}
		Log.d("dddd", sb.toString());
	}
}
