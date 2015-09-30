package com.aess.aemm.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.aess.aemm.db.SettingsContent;
import com.aess.aemm.setting.VpnProfile.VpnArg;
import com.aess.aemm.setting.util.Utils;
import android.content.Context;
import android.net.vpn.L2tpIpsecProfile;
import android.net.vpn.L2tpIpsecPskProfile;
import android.net.vpn.L2tpProfile;
import android.net.vpn.PptpProfile;
import android.net.vpn.VpnManager;
import android.net.vpn.VpnProfile;
import android.net.vpn.VpnType;
import android.security.Credentials;
import android.security.KeyStore;
import android.util.Log;

public class VpnSetup {
	public static final String TAG = "VpnSetup";
	
	public static final String PROFILES_ROOT = String.format("/data%s/", VpnManager.PROFILES_PATH);

	public VpnSetup(Context context) {
		if (null != context) {
			mVpnManager = new VpnManager(context);
		}
	}
	
	public String getProfileId() {
		String id = null;
		if (mProfile != null) {
			id = mProfile.getId();
		}
		return id;
	}

	public String getVPNFilePath() {
		String path = null;
		if (null != mProfile) {
			path = PROFILES_ROOT + mProfile.getId();
		}
		return path;
	}

	public int addVPN(Context context, VpnArg vpnarg) {
		int ret = -1;
		if (null!= vpnarg) {
			setValues(context, vpnarg);
		}
		if (null != mProfile) {
			try {
				saveProfileToStorage(this.mProfile);
				ret = 1;
			} catch (Exception e) {
				 e.printStackTrace();
			}
		}
		return ret;
	}
	
	public static void delVPNByRemoteAddress(String remoteAddress) {
		if (null != remoteAddress) {
			VpnProfile vpnfile = systemHaveSameConfig(VpnSetup.PROFILES_ROOT,
					remoteAddress);
			if (null != vpnfile) {
				deleteFile(getProfileDir(vpnfile));
			}
		}
	}
	
	public static int delVPN(SettingsContent ccontent) {
		Log.i(TAG, "delVPN");
		
		int ret = -1;
		File root = new File(PROFILES_ROOT);
		
		String dbname = ccontent.getName();
		
		String[] dirs = root.list();
		if (dirs != null) {

			for (String dir : dirs) {
				File file = new File(new File(root, dir), PROFILE_OBJ_FILE);
				if (!file.exists()) {
					Log.i(TAG, "VPN: !f.exists ");
					continue;
				}

				VpnProfile p = deserialize(file);
				if (p == null) {
					Log.i(TAG, "deserialize(file) err");
					continue;
				}
				String filename = p.getName();

				if (null != filename && filename.equals(dbname)) {
					removeProfileFromStorage(p);
				}
			}
		} else {
			Log.i(TAG, "VPN: !root.list ");
		}

		return ret;
	}
	
	@SuppressWarnings("unused")
	public static VpnProfile systemHaveSameConfig(String vpnpath, String name) {
		VpnProfile vpnfile = null;
		if (null != vpnpath && null != name) {

			File rootfile = new File(vpnpath);
			if (rootfile.exists()) {
				if (rootfile.isDirectory()) {
					String[] dirs = rootfile.list();
					if (dirs != null) {
						for (String dir : dirs) {
							File txtfile = new File(new File(rootfile, dir),
									PROFILE_OBJ_FILE);
							if (!txtfile.exists()) {
								Log.i(TAG, "VPN: !f.exists ");
								continue;
							}
							
							VpnProfile tmpvpnfile = deserialize(txtfile);
							if (vpnfile == null) {
								Log.i(TAG, "deserialize(file) err");
								continue;
							} else {
								if (vpnfile.getName().equals(name)) {
									vpnfile = tmpvpnfile;
								}
							}
						}
					}
				} else {
					File isfile = new File(rootfile, PROFILE_OBJ_FILE);
					VpnProfile tmpvpnfile = (VpnProfile) deserialize(isfile);
					if (vpnfile != null) {
						if (vpnfile.getName().equals(name)) {
							vpnfile = tmpvpnfile;
						}
					}
				}
			}
		}
		return vpnfile;
	}
	
	private static void removeProfileFromStorage(VpnProfile p) {
		deleteFile(getProfileDir(p));
	}

	private static void deleteFile(String path) {
		deleteFile(new File(path), true);
	}

	private static void deleteFile(File f, boolean toDeleteSelf) {
		if (f.isDirectory()) {
			for (File child : f.listFiles())
				deleteFile(child, true);
		}
		if (toDeleteSelf)
			f.delete();
	}
	
	// Randomly generates an ID for the profile.
	// The ID is unique and only set once when the profile is created.
	private void setProfileId(VpnProfile profile) {
		String id;

		while (true) {
			id = String.valueOf(Math
					.abs(Double.doubleToLongBits(Math.random())));
			if (id.length() >= 8)
				break;
		}

		profile.setId(id);
	}

	private static VpnProfile deserialize(File file){
		VpnProfile vpnfile = null;
		
		if (null != file && file.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(file));
				try {
					vpnfile = (VpnProfile) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				ois.close();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vpnfile;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean producePassword(VpnProfile p) {
		Log.i(TAG, "producePassword");
		boolean rlt = false;
		switch (p.getType()) {
		case L2TP_IPSEC_PSK:
			L2tpIpsecPskProfile pskProfile = (L2tpIpsecPskProfile) p;
			String presharedKey = pskProfile.getPresharedKey();
			String key = KEY_PREFIX_IPSEC_PSK + p.getId();
			if (presharedKey.length() > 0) {
				if (!mKeyStore.put(key, presharedKey)) {
					Log.w(TAG, "mKeyStore.put error");
				}
			} else {
				Log.w(TAG, "pskProfile.getPresharedKey() error");
			}
			pskProfile.setPresharedKey(key);
			rlt = true;
		case L2TP_IPSEC:
		case L2TP:
			L2tpProfile l2tpProfile = (L2tpProfile) p;
			key = KEY_PREFIX_L2TP_SECRET + p.getId();
			if (l2tpProfile.isSecretEnabled()) {
				String secret = l2tpProfile.getSecretString();

//				Log.i(TAG, "VPN: secret =" + secret);
//				Log.i(TAG, "VPN: TextUtils.isEmpty ="
//						+ TextUtils.isEmpty(secret));
				if (!mKeyStore.password(tickerpassword)) {
					mKeyStore.reset();
				}
				mKeyStore.password(tickerpassword);

//				if (!TextUtils.isEmpty(secret) && !mKeyStore.put(key, secret)) {
//					Log.i(TAG, "VPN: keystore write failed: key=" + key);
//				}
				
				if (secret.length() > 0) {
					if (!mKeyStore.put(key, secret)) {
						Log.w(TAG, "mKeyStore.put error");
					}
				} else {
					Log.w(TAG, "l2tpProfile.getSecretString() error");
				}
				
				l2tpProfile.setSecretString(key);
				//Log.i(TAG, "VPN: key=" + key);
				rlt = true;
			} else {
				mKeyStore.delete(key);
			}
			break;
		}
		return rlt;
	}

	private static String getProfileDir(VpnProfile p) {
		return PROFILES_ROOT + p.getId();
	}

	private void saveProfileToStorage(VpnProfile p) throws IOException {
		File f = new File(getProfileDir(p));
		if (!f.exists()) {
			boolean ret = f.mkdirs();
			Log.i(TAG, "mkdirs =" + String.valueOf(ret));
			Log.i(TAG, "mkdirs =" + getProfileDir(p));

		}
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File(f, PROFILE_OBJ_FILE)));
		oos.writeObject(p);
		oos.close();
	}
	
	private String setValues(Context context, VpnArg arg) {
		Log.i(TAG, "setValues");
		String filename = null;
		if (null != context && null != arg) {
			
			VpnArg vpnarg = arg;
			String PreshareKey = null;
			boolean isPreshareKey = false;
			boolean isSecret = false;
			
			if (vpnarg.sharedSecret != null
					&& vpnarg.sharedSecret.length() != 0) {
				byte[] key = Utils.decode(vpnarg.sharedSecret);
				PreshareKey = new String(key);
				isPreshareKey = true;
			}
			
			VpnType type = (VpnType) Enum.valueOf(VpnType.class, vpnarg.vpnType);
			
			VpnProfile tprofile = null;

			switch (type) {
			case L2TP: {
				tprofile = mVpnManager.createVpnProfile(VpnType.L2TP);
				L2tpProfile l2tpProfile = (L2tpProfile) tprofile;
				if (PreshareKey != null) {
					isSecret = true;
				}
				if (isSecret) {
					l2tpProfile.setSecretEnabled(true);
					l2tpProfile.setSecretString(PreshareKey);
				}
				l2tpProfile.setSavedUsername(vpnarg.authName);
				l2tpProfile.setName(vpnarg.vpnDefineName);
				l2tpProfile.setServerName(vpnarg.commRemoteAddress);
				break;
			}
			case PPTP: {
				tprofile = mVpnManager.createVpnProfile(VpnType.PPTP);
				PptpProfile pptpProfile = (PptpProfile) tprofile;

				if (vpnarg.isSecret) {
					pptpProfile.setEncryptionEnabled(true);
				} else {
					pptpProfile.setEncryptionEnabled(false);
				}

				pptpProfile.setSavedUsername(vpnarg.authName);
				pptpProfile.setName(vpnarg.vpnDefineName);
				pptpProfile.setServerName(vpnarg.commRemoteAddress);
				break;
			}
			case L2TP_IPSEC_PSK: {
				tprofile = mVpnManager.createVpnProfile(VpnType.L2TP_IPSEC_PSK);
				L2tpIpsecPskProfile lpp = (L2tpIpsecPskProfile) tprofile;

				if (isPreshareKey) {
					lpp.setPresharedKey(PreshareKey);
				}
				if (isSecret) {
					lpp.setSecretEnabled(true);
				}
				lpp.setSavedUsername(vpnarg.authName);
				lpp.setName(vpnarg.vpnDefineName);
				if (null != vpnarg.remoteAddress) {
					lpp.setServerName(vpnarg.remoteAddress);
				} else {
					lpp.setServerName(vpnarg.vpnDefineName);
				}
				break;
			}
			case L2TP_IPSEC: {
				tprofile = mVpnManager.createVpnProfile(VpnType.L2TP_IPSEC);
				L2tpIpsecProfile pp = (L2tpIpsecProfile) tprofile;
				if (isSecret) {
					pp.setSecretEnabled(true);
				}
				// String cacertValue = null;
				// if (mCaCertificate != null)
				// cacertValue = CertificateSetup.getCertName(context,
				// mCaCertificate);
				// if (cacertValue != null)
				// pp.setCaCertificate(cacertValue);
				String userCertValue = null;
				if (vpnarg.userCertUuid != null) {
					userCertValue = CertificateSetup.getCertName(context,
							vpnarg.userCertUuid);
				}
				if (userCertValue != null) {
					pp.setCaCertificate(userCertValue);
				}
				pp.setSavedUsername(vpnarg.authName);
				pp.setName(vpnarg.vpnDefineName);
				pp.setServerName(vpnarg.remoteAddress);

				break;
			}
			}

			mProfile = tprofile;
			
			setProfileId(mProfile);
			
			String id = mProfile.getId();
			
			Log.i(TAG, "vpn id : " + id);
			
			if (producePassword(this.mProfile)) {
				if (id.length() > 0) {
					filename = id;
				}
			} else {
				Log.w(TAG, "producePassword fail");
			}
		}
		return filename;
	}

	private VpnProfile mProfile = null;
	private VpnManager mVpnManager = null;
	private KeyStore mKeyStore = KeyStore.getInstance();
	
	private static final String KEY_PREFIX_IPSEC_PSK = Credentials.VPN + 'i';
	private static final String KEY_PREFIX_L2TP_SECRET = Credentials.VPN + 'l';
	
	private static final String PROFILE_OBJ_FILE = ".pobj";

	private static final String tickerpassword = "qqqqqqqq";
}
