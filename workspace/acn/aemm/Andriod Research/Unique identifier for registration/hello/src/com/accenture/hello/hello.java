package com.accenture.hello;

import android.app.Activity;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hello extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final String deviceId = "355921043311370";
        final MessageDigest sha;
        try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		byte[] deviceIdUtf8;
           try {
			deviceIdUtf8 = deviceId.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}  

		sha.update(deviceIdUtf8);
       final int hash = getSmallHashFromSha1(sha.digest());
       String id = Integer.toString(hash);
       String id2 = id;
    }
    static int getSmallHashFromSha1(byte[] sha1) {
        final int offset = sha1[19] & 0xf; // SHA1 is 20 bytes.
        return ((sha1[offset]  & 0x7f) << 24)
	    | ((sha1[offset + 1] & 0xff) << 16)
	    | ((sha1[offset + 2] & 0xff) << 8)
	    | ((sha1[offset + 3] & 0xff));
    }
}