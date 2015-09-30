package com.aemm.demo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.security.Credentials;
import android.security.KeyStore;
import android.util.Log;


public class TestCredentailInstaller extends Activity {
	 private static final String TAG = "CredentialInstaller";
	    private static final String UNLOCKING = "ulck";

	    private KeyStore mKeyStore = KeyStore.getInstance();
	    private boolean mUnlocking = false;
	    String password = "11111111";
	    @Override
	    protected void onResume() {
	        super.onResume();

	       // if (!"com.android.certinstaller".equals(getCallingPackage())) finish();
	        
	        if (isKeyStoreUnlocked()) {
	        	mKeyStore.password(password);
	        	
	            install();
	        } else if (!mUnlocking) {
	            mUnlocking = true;
	            Log.d(TAG,  "unlock here");
	            //mKeyStore.unlock(password);
	            //mKeyStore.password(password);
	            //install();
	            int mState = mKeyStore.test();
	            if (mState == KeyStore.UNINITIALIZED)
	            {
	            	//set password
	            	Log.d(TAG,  "UNINITIALIZED");
	            	mKeyStore.unlock(password);
	            	mKeyStore.password(password);
	            	install();
	            }
	            else if (mState == KeyStore.LOCKED)
	            {
	            	Credentials.getInstance().unlock(this);
	            }
	           // Credentials.getInstance().unlock(this);
	            Log.d(TAG,  "unlock here after");
	            return;
	        }
	        finish();
	    }
	    @Override
	    protected void onSaveInstanceState(Bundle outStates) {
	        super.onSaveInstanceState(outStates);
	        outStates.putBoolean(UNLOCKING, mUnlocking);
	    }

	    @Override
	    protected void onRestoreInstanceState(Bundle savedStates) {
	        super.onRestoreInstanceState(savedStates);
	        mUnlocking = savedStates.getBoolean(UNLOCKING);
	    }

	    private void install() {
	        Intent intent = getIntent();
	        Bundle bundle = (intent == null) ? null : intent.getExtras();
	        if (bundle == null) return;
	        for (String key : bundle.keySet()) {
	            byte[] data = bundle.getByteArray(key);
	            if (data == null) continue;
	            boolean success = mKeyStore.put(key.getBytes(), data);
	            Log.d(TAG, "install " + key + ": " + data.length + "  success? " + success);
	            if (!success) return;
	        }
	        setResult(RESULT_OK);
	    }

	    private boolean isKeyStoreUnlocked() {
	        return (mKeyStore.test() == KeyStore.NO_ERROR);
	    }
}
