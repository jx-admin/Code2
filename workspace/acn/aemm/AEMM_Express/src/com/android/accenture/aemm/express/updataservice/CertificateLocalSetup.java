package com.android.accenture.aemm.express.updataservice;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.util.encoders.Base64;

import com.android.accenture.aemm.express.updataservice.ProfileContent.Profiles;







import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.Credentials;
import android.security.KeyStore;
import android.util.Log;


public class CertificateLocalSetup {
	
	//public static final String ROOTCERTIFICATE = "com.apple.security.root";
	//public static final String PSKCERTIFICATE = "com.apple.security.pkcs12";
	
	private static final String TAG = "CertificateLocalSetup";
	private KeyStore mKeyStore = KeyStore.getInstance();
	private CredentialHelper mCredentials;
	
	String certName;
	String pksPass;
	Context mContext;
	String type;
	// key to KeyStore
	private static final byte[] PKEY_MAP_KEY = "PKEY_MAP".getBytes();
	private MyAction mNextAction;
	private static final int REQUEST_SYSTEM_INSTALL_CODE = 1;

	private boolean mUnlocking = false;
	String tickerpassword = "qqqqqqqq";
	public CertificateLocalSetup(Context mContext)
	{
		this.mContext = mContext;
	}
	public void setValues(String data,String name,String password,String type)
	{
		byte [] value = Base64.decode(data);
		certName = name;
		
		//Log.d(TAG,  value);
		Intent intent = new Intent();
		intent.putExtra(CredentialHelper.CERT_NAME_KEY, certName);
		if (type.equals(String.valueOf(configParser.ProfileType.Profile_RootCertificate)))
		{
			Log.d(TAG,  type);
			intent.putExtra(Credentials.CERTIFICATE, value);
		}
		else if (type.equals(String.valueOf(configParser.ProfileType.Profile_PkcsCertificate)))
		{
			pksPass = password;
			intent.putExtra(Credentials.PKCS12, value);
			Log.d(TAG,  type + pksPass);
		}
		
		mCredentials = new CredentialHelper(intent);

	}
	//Bug #2661 shxn
	public static void  deleteCert(String name)
	{
		KeyStore.getInstance().delete(name);//("CACERT_ca");
	}
	//Bug #2661 shxn
	int setup()
	{
		Log.d(TAG,  "certificate");
		int ret = -1;
		 if (!mCredentials.containsAnyRawData()) {
            // toastErrorAndFinish(R.string.no_cert_to_saved);
            // finish();
			 return ret;
         } else if (mCredentials.hasPkcs12KeyStore()) {
         	//show password dailaog
         	Log.d(TAG,  "hasPkcs12KeyStore");
            boolean success = mCredentials.extractPkcs12(pksPass);
         	if (success)
         	{
         		mCredentials.setName(certName);
    			{
    				try {
    					Log.w(TAG, "createTestCredentailInstallIntent ");
    					installKeyStore();
    					
    				} catch (ActivityNotFoundException e) {
    					Log.w(TAG, "systemInstall(): " + e);
    					//toastErrorAndFinish(R.string.cert_not_saved);
    				}
    			}
    			ret = 0;
    			
         	}
         	else
         	{
         		return ret;
         	}
         } else {
        	 MyAction action = new InstallOthersAction();
     		if (needsKeyStoreAccess()) {
     			sendUnlockKeyStoreIntent();
     			mNextAction = action;
     		} else {
     			Log.d(TAG,  "installOthers");
     			installOthers();
     			//action.run(this);
     		}
         }
		return ret;
		
	}

	private boolean isKeyStoreUnlocked() {
		return (mKeyStore.test() == KeyStore.NO_ERROR);
	}

	private void install() {

		Intent intent =      mCredentials.createTestCredentailInstallIntent();
		Bundle bundle = (intent == null) ? null : intent.getExtras();
		if (bundle == null) return;
		for (String key : bundle.keySet()) {
			byte[] data = bundle.getByteArray(key);
			if (data == null) continue;
			boolean success = mKeyStore.put(key.getBytes(), data);
			Log.d(TAG, "install " + key + ": " + data.length + "  success? " + success);
			if (!success) return;
		}

	}

	void installOthers() {
		if (mCredentials.hasKeyPair()) {
			saveKeyPair();
			//finish();
		} else {
			X509Certificate cert = mCredentials.getUserCertificate();
			if (cert != null) {
				// find matched private key
				String key = Util.toMd5(cert.getPublicKey().getEncoded());
				Map<String, byte[]> map = getPkeyMap();
				byte[] privatekey = map.get(key);
				if (privatekey != null) {
					Log.d(TAG, "found matched key: " + privatekey);
					map.remove(key);
					savePkeyMap(map);

					mCredentials.setPrivateKey(privatekey);
				} else {
					Log.d(TAG, "didn't find matched private key: " + key);
				}
			}
			mCredentials.setName(certName);
			{
				try {
					Log.w(TAG, "createTestCredentailInstallIntent ");
					installKeyStore();
					
				} catch (ActivityNotFoundException e) {
					Log.w(TAG, "systemInstall(): " + e);
					//toastErrorAndFinish(R.string.cert_not_saved);
				}
			}
		}
	}
	private void installKeyStore()
	{
		if (isKeyStoreUnlocked()) {
			if (!mKeyStore.password(tickerpassword))
				mKeyStore.reset();
			mKeyStore.password(tickerpassword);

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
				mKeyStore.unlock(tickerpassword);
				mKeyStore.password(tickerpassword);
				install();
			}
			else if (mState == KeyStore.LOCKED)
			{
				//
				//Credentials.getInstance().unlock(this.mContext);
			}
			// Credentials.getInstance().unlock(this);
			Log.d(TAG,  "unlock here after");
			return;
		}
	}
	private boolean needsKeyStoreAccess() {
		return ((mCredentials.hasKeyPair() || mCredentials.hasUserCertificate())
				&& (mKeyStore.test() != KeyStore.NO_ERROR));
	}

	private void sendUnlockKeyStoreIntent() {
		Credentials.getInstance().unlock(this.mContext);
	}

	private void nameCredential() {
		/*  if (!mCredentials.hasAnyForSystemInstall()) {
	            toastErrorAndFinish(R.string.no_cert_to_saved);
	        } else {
	            showDialog(NAME_CREDENTIAL_DIALOG);
	        }*/
	}

	private void saveKeyPair() {
		byte[] privatekey = mCredentials.getData(Credentials.PRIVATE_KEY);
		String key = Util.toMd5(mCredentials.getData(Credentials.PUBLIC_KEY));
		Map<String, byte[]> map = getPkeyMap();
		map.put(key, privatekey);
		savePkeyMap(map);
		Log.d(TAG, "save privatekey: " + key + " --> #keys:" + map.size());
	}

	private void savePkeyMap(Map<String, byte[]> map) {
		byte[] bytes = Util.toBytes((Serializable) map);
		if (!mKeyStore.put(PKEY_MAP_KEY, bytes)) {
			Log.w(TAG, "savePkeyMap(): failed to write pkey map");
		}
	}

	private Map<String, byte[]> getPkeyMap() {
		byte[] bytes = mKeyStore.get(PKEY_MAP_KEY);
		if (bytes != null) {
			Map<String, byte[]> map =
				(Map<String, byte[]>) Util.fromBytes(bytes);
			if (map != null) return map;
		}
		return new MyMap();
	}
	private static class MyMap extends LinkedHashMap<String, byte[]>
	implements Serializable {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Map.Entry eldest) {
			// Note: one key takes about 1300 bytes in the keystore, so be
			// cautious about allowing more outstanding keys in the map that
			// may go beyond keystore's max length for one entry.
			return (size() > 3);
		}
	}

	void extractPkcs12InBackground(final String password) {
		// show progress bar and extract certs in a background thread
		//  showDialog(PROGRESS_BAR_DIALOG);
		new Thread(new Runnable() {
			public void run() {
				final boolean success = mCredentials.extractPkcs12(password);

				/* runOnUiThread(new Runnable() {
	                    public void run() {
	                        MyAction action = new OnExtractionDoneAction(success);
	                        if (mState == STATE_PAUSED) {
	                            // activity is paused; run it in next onResume()
	                            mNextAction = action;
	                        } else {
	                            action.run(CertInstaller.this);
	                        }
	                    }
	                });*/
			}
		}).start();
	}

	void onExtractionDone(boolean success) {
		mNextAction = null;
		//  removeDialog(PROGRESS_BAR_DIALOG);
		if (success) {
			//   removeDialog(PKCS12_PASSWORD_DIALOG);
			nameCredential();
		} else {
			//  mView.setText(R.id.credential_password, "");
			//  mView.showError(R.string.password_error);
			//  showDialog(PKCS12_PASSWORD_DIALOG);
		}
	}

	private interface MyAction extends Serializable {
		void run(CertificateLocalSetup host);
	}

	private static class Pkcs12ExtractAction implements MyAction {
		private String mPassword;
		private transient boolean hasRun;

		Pkcs12ExtractAction(String password) {
			mPassword = password;
		}

		public void run(CertificateLocalSetup host) {
			if (hasRun) return;
			hasRun = true;
			host.extractPkcs12InBackground(mPassword);
		}


	}

	private static class InstallOthersAction implements MyAction {
		public void run(CertificateLocalSetup host) {
			host.mNextAction = null;
			host.installOthers();
		}


	}

	private static class OnExtractionDoneAction implements MyAction {
		private boolean mSuccess;

		OnExtractionDoneAction(boolean success) {
			mSuccess = success;
		}

		public void run(CertificateLocalSetup host) {
			host.onExtractionDone(mSuccess);
		}
	}
	public static String getCertName(Context mContext,String uuid)
	{
		String userCertname = null;
		
		Profiles p = new Profiles();
		p = Profiles.restoreProfilesWithUuid(mContext,uuid);
		if (p != null)
			userCertname = p.mName;
		
		//look up the db and find
		return userCertname;
	}
}
