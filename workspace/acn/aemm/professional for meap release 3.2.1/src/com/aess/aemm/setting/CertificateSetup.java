package com.aess.aemm.setting;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.aess.aemm.db.SettingsContent;
import com.aess.aemm.setting.util.Base64;
import com.aess.aemm.setting.util.Utils;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.Credentials;
import android.security.KeyStore;
import android.util.Log;

public class CertificateSetup {
	public static final String TAG = "CertificateSetup";

	public CertificateSetup(Context context) {
		if (null != context) {
			mContext = context;
		}
	}

	public void setValues(String data, String name, String password, String type) {
		byte[] value = Base64.decode(data);
		certName = name;
		String cType = null;
		Intent intent = new Intent();
		intent.putExtra(CredentialHelper.CERT_NAME_KEY, certName);
		if (Credentials.CERTIFICATE.equals(type)) {
			intent.putExtra(Credentials.CERTIFICATE, value);
			cType = Credentials.CERTIFICATE;
		} else if (Credentials.PKCS12.equals(type)) {
			pksPass = password;
			intent.putExtra(Credentials.PKCS12, value);
			cType = Credentials.PKCS12;
		}

		mCredentials = new CredentialHelper(intent, cType);
	}

	public static void deleteCert(String name) {
		KeyStore.getInstance().delete(name);
	}

	int AddCert() {
		Log.d(TAG, "add certificate");
		int ret = -1;
		if (!mCredentials.containsAnyRawData()) {

		} else if (mCredentials.hasPkcs12KeyStore()) {
			Log.d(TAG, "hasPkcs12KeyStore");
			boolean success = mCredentials.extractPkcs12(pksPass);
			if (success) {
				mCredentials.setName(certName);
				{
					try {
						Log.w(TAG, "createTestCredentailInstallIntent ");
						installKeyStore();

					} catch (ActivityNotFoundException e) {
						Log.w(TAG, "ActivityNotFoundException");
						e.printStackTrace();
					}
				}
				ret = 1;

			}
		} else {
			MyAction action = new InstallOthersAction();
			if (needsKeyStoreAccess()) {
				sendUnlockKeyStoreIntent();
				mNextAction = action;
			} else {
				Log.d(TAG, "installOthers");
				installOthers();
			}
			ret = 0;
		}
		return ret;
	}

	private boolean isKeyStoreUnlocked() {
		return (mKeyStore.test() == KeyStore.NO_ERROR);
	}

	private void install() {

		Intent intent = mCredentials.createTestCredentailInstallIntent();
		Bundle bundle = (intent == null) ? null : intent.getExtras();
		if (bundle == null)
			return;
		for (String key : bundle.keySet()) {
			byte[] data = bundle.getByteArray(key);
			if (data == null)
				continue;
			boolean success = mKeyStore.put(key.getBytes(), data);
			Log.d(TAG, "install " + key + ": " + data.length + "  success? "
					+ success);
			if (!success)
				return;
		}

	}

	void installOthers() {
		if (mCredentials.hasKeyPair()) {
			saveKeyPair();
			// finish();
		} else {
			X509Certificate cert = mCredentials.getUserCertificate();
			if (cert != null) {
				// find matched private key
				String key = Utils.toMd5(cert.getPublicKey().getEncoded());
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
					// toastErrorAndFinish(R.string.cert_not_saved);
				}
			}
		}
	}

	private void installKeyStore() {
		if (isKeyStoreUnlocked()) {
			if (!mKeyStore.password(tickerpassword))
				mKeyStore.reset();
			mKeyStore.password(tickerpassword);

			install();
		} else if (!mUnlocking) {
			mUnlocking = true;
			Log.d(TAG, "unlock here");
			// mKeyStore.unlock(password);
			// mKeyStore.password(password);
			// install();
			int mState = mKeyStore.test();
			if (mState == KeyStore.UNINITIALIZED) {
				// set password
				Log.d(TAG, "UNINITIALIZED");
				mKeyStore.unlock(tickerpassword);
				mKeyStore.password(tickerpassword);
				install();
			} else if (mState == KeyStore.LOCKED) {
				//
				// Credentials.getInstance().unlock(this.mContext);
			}
			// Credentials.getInstance().unlock(this);
			Log.d(TAG, "unlock here after");
			return;
		}
	}

	private boolean needsKeyStoreAccess() {
		return ((mCredentials.hasKeyPair() || mCredentials.hasUserCertificate()) && (mKeyStore
				.test() != KeyStore.NO_ERROR));
	}

	private void sendUnlockKeyStoreIntent() {
		Credentials.getInstance().unlock(this.mContext);
	}

	private void nameCredential() {
		/*
		 * if (!mCredentials.hasAnyForSystemInstall()) {
		 * toastErrorAndFinish(R.string.no_cert_to_saved); } else {
		 * showDialog(NAME_CREDENTIAL_DIALOG); }
		 */
	}

	private void saveKeyPair() {
		byte[] privatekey = mCredentials.getData(Credentials.PRIVATE_KEY);
		String key = Utils.toMd5(mCredentials.getData(Credentials.PUBLIC_KEY));
		Map<String, byte[]> map = getPkeyMap();
		map.put(key, privatekey);
		savePkeyMap(map);
		Log.d(TAG, "save privatekey: " + key + " --> #keys:" + map.size());
	}

	private void savePkeyMap(Map<String, byte[]> map) {
		byte[] bytes = Utils.toBytes((Serializable) map);
		if (!mKeyStore.put(PKEY_MAP_KEY, bytes)) {
			Log.w(TAG, "savePkeyMap(): failed to write pkey map");
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, byte[]> getPkeyMap() {
		byte[] bytes = mKeyStore.get(PKEY_MAP_KEY);
		if (bytes != null) {
			Map<String, byte[]> map = (Map<String, byte[]>) Utils
					.fromBytes(bytes);
			if (map != null)
				return map;
		}
		return new MyMap();
	}

	private static class MyMap extends LinkedHashMap<String, byte[]> implements
			Serializable {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			// Note: one key takes about 1300 bytes in the keystore, so be
			// cautious about allowing more outstanding keys in the map that
			// may go beyond keystore's max length for one entry.
			return (size() > 3);
		}
	}

	void extractPkcs12InBackground(final String password) {
		// show progress bar and extract certs in a background thread
		// showDialog(PROGRESS_BAR_DIALOG);
		new Thread(new Runnable() {
			public void run() {
				mCredentials.extractPkcs12(password);

				/*
				 * runOnUiThread(new Runnable() { public void run() { MyAction
				 * action = new OnExtractionDoneAction(success); if (mState ==
				 * STATE_PAUSED) { // activity is paused; run it in next
				 * onResume() mNextAction = action; } else {
				 * action.run(CertInstaller.this); } } });
				 */
			}
		}).start();
	}

	void onExtractionDone(boolean success) {
		mNextAction = null;
		// removeDialog(PROGRESS_BAR_DIALOG);
		if (success) {
			// removeDialog(PKCS12_PASSWORD_DIALOG);
			nameCredential();
		} else {
			// mView.setText(R.id.credential_password, "");
			// mView.showError(R.string.password_error);
			// showDialog(PKCS12_PASSWORD_DIALOG);
		}
	}

	private interface MyAction extends Serializable {
		void run(CertificateSetup host);
	}

//	private static class Pkcs12ExtractAction implements MyAction {
//		private String mPassword;
//		private transient boolean hasRun;
//
//		Pkcs12ExtractAction(String password) {
//			mPassword = password;
//		}
//
//		public void run(CertificateSetup host) {
//			if (hasRun)
//				return;
//			hasRun = true;
//			host.extractPkcs12InBackground(mPassword);
//		}
//
//	}

	@SuppressWarnings("serial")
	private static class InstallOthersAction implements MyAction {
		public void run(CertificateSetup host) {
			host.mNextAction = null;
			host.installOthers();
		}

	}

	@SuppressWarnings({ "unused", "serial" })
	private static class OnExtractionDoneAction implements MyAction {
		private boolean mSuccess;

		OnExtractionDoneAction(boolean success) {
			mSuccess = success;
		}

		public void run(CertificateSetup host) {
			host.onExtractionDone(mSuccess);
		}
	}

	public static String getCertName(Context mContext, String uuid) {
		String userCertname = null;

		SettingsContent pc = SettingsContent.queryContentByUUID(mContext, uuid);
		if (pc != null)
			userCertname = pc.getName();

		// look up the db and find
		return userCertname;
	}

	private static final byte[] PKEY_MAP_KEY = "PKEY_MAP".getBytes();
	private static final String tickerpassword = "qqqqqqqq";

	private CredentialHelper mCredentials = null;
	private Context mContext = null;
	@SuppressWarnings("unused")
	private MyAction mNextAction = null;
	private KeyStore mKeyStore = KeyStore.getInstance();

	private boolean mUnlocking = false;
	private String certName = null;
	private String pksPass = null;

}
