package com.android.accenture.aemm.express.updataservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bouncycastle.util.encoders.Base64;

import com.android.accenture.aemm.express.updataservice.ProfileContent.Profiles;

import android.content.Context;
import android.net.vpn.L2tpIpsecProfile;
import android.net.vpn.L2tpIpsecPskProfile;
import android.net.vpn.L2tpProfile;
import android.net.vpn.PptpProfile;
import android.net.vpn.VpnManager;
import android.net.vpn.VpnProfile;
import android.net.vpn.VpnType;
import android.os.Parcel;
import android.security.Credentials;
import android.security.KeyStore;
import android.text.TextUtils;
import android.util.Log;

//Bug #2661 shxn

public class VpnLocalSetup {
	
	private VpnProfile mProfile;
	
	private Context mContext;
	private VpnManager mVpnManager = null;
	private KeyStore mKeyStore = KeyStore.getInstance();
	private static final String KEY_PREFIX_IPSEC_PSK = Credentials.VPN + 'i';
	private static final String KEY_PREFIX_L2TP_SECRET = Credentials.VPN + 'l';
	
	private static final String PROFILES_ROOT = "/data" + VpnManager.PROFILES_PATH + "/";
	private static final String PROFILE_OBJ_FILE = ".pobj";
	
	String tickerpassword = "qqqqqqqq";
	public VpnLocalSetup(Context context)
	{
		mContext = context;
		mVpnManager = new VpnManager(mContext);
	}
	
	public void setvalues(String vpnType,
						  String vpnName,
						  String serverName,
						  String authName,
						  boolean encrption,
						  String secret,
						  String preshareKey,
						  String userCert,
						  String caCert)
	{
		
		this.mName = vpnName;
		this.mServerName = serverName;
		this.mAuthName = authName;
		this.mSecretString = secret;
		this.mEncryption = encrption;
		if (preshareKey != null && preshareKey.length() != 0)
		{
			byte [] key = Base64.decode(preshareKey);
			this.mPreshareKey = new String(key); //fix bug 3156 by fengyun
			isPreshareKey = true;
			   
			
		}
		this.mUserCertificate = userCert;
		this.mCaCertificate = caCert;
		
		this.type = (VpnType)Enum.valueOf(VpnType.class, vpnType);

		
	}
	VpnType type;
	
	boolean isSecret;
	String mSecretString;
	
	String mName;
	String mServerName;
	
	boolean mEncryption;
	
	String mPreshareKey;
	boolean isPreshareKey;
	
	String mUserCertificate;
	String mCaCertificate;
	
	String mAuthName;
	
	public String getProfileId()
	{
		if (mProfile != null)
			return mProfile.getId();
		return null;
	}
	public void setVpnValue(vpnProfile p)
	{
		//todo
		
	}
	public int setVpn()
	{
		int ret = 0;
		VpnProfile mProfile = null;
	 switch (type) {
	  	case L2TP:
	  		
	  		mProfile = mVpnManager.createVpnProfile(VpnType.L2TP); 
	  		L2tpProfile l2tpProfile = (L2tpProfile) mProfile;
	  		if (mPreshareKey != null)
	  			isSecret = true;
	  		if (isSecret)
	  		{
	  			l2tpProfile.setSecretEnabled(true);
	  			//l2tpProfile.setSecretString(mSecretString);
	  			l2tpProfile.setSecretString(mPreshareKey);
	  		}
	  		l2tpProfile.setSavedUsername(mAuthName);
	  		l2tpProfile.setName(mName);
	  		l2tpProfile.setServerName(mServerName);
	  		
	 		break;
	 	case PPTP:
	 		mProfile = mVpnManager.createVpnProfile(VpnType.PPTP);
	 		PptpProfile pptpProfile = (PptpProfile)mProfile;
	 		if (mEncryption)
	 			pptpProfile.setEncryptionEnabled(true);
	 		else 
	 			pptpProfile.setEncryptionEnabled(false);
	 		
	 		pptpProfile.setSavedUsername(mAuthName);
	 		pptpProfile.setName(mName);
	 		pptpProfile.setServerName(mServerName);
	 		break;
         case L2TP_IPSEC_PSK:
        	 mProfile = mVpnManager.createVpnProfile(VpnType.L2TP_IPSEC_PSK);
        	 L2tpIpsecPskProfile lpp = (L2tpIpsecPskProfile)mProfile;
        	 if (isPreshareKey)
        	 {
        		 lpp.setPresharedKey(mPreshareKey);
        		     
        	 }
        	 if (isSecret)
 	  		 {
        		 lpp.setSecretEnabled(true);
        		 lpp.setSecretString(mSecretString);
 	  		 }
        	 lpp.setSavedUsername(mAuthName);
        	 lpp.setName(mName);
        	 lpp.setServerName(mServerName);
        	 break;
         case L2TP_IPSEC:
        	 mProfile = mVpnManager.createVpnProfile(VpnType.L2TP_IPSEC);
        	 L2tpIpsecProfile pp = (L2tpIpsecProfile)mProfile;
        	 if (isSecret)
 	  		 {
        		 pp.setSecretEnabled(true);
        		 pp.setSecretString(mSecretString);
 	  		 }
        	 String cacertValue = null;
        	 if (mCaCertificate != null)
        		 cacertValue = CertificateLocalSetup.getCertName(mContext,mCaCertificate);
        	 if (cacertValue != null)
        		 pp.setCaCertificate(cacertValue);
        	 String userCertValue = null;
        	 if (mUserCertificate != null)
        		 userCertValue = CertificateLocalSetup.getCertName(mContext,mUserCertificate);
        	 if (userCertValue != null)
        		 pp.setCaCertificate(userCertValue);
        	 
        	 pp.setSavedUsername(mAuthName);
        	 pp.setName(mName);
        	 pp.setServerName(mServerName);
        	 
        	 break;
	 }
         
		 //set
	 	this.mProfile = mProfile;
	 	setProfileId(this.mProfile);
	 	Log.i("VPN setting demo", "VPN: VPNprofile ID = " + this.mProfile.getId());    
     
	 	processSecrets(this.mProfile);
     
	 	try {
	 		saveProfileToStorage(this.mProfile);
	 	}catch (IOException e) {
	 		ret = -1;
	 		e.printStackTrace();
	 	}
		return ret;
	}
	
	// from util
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
			for (File child : f.listFiles())
				deleteFile(child, true);
		}
		if (toDeleteSelf)
			f.delete();
	}

	private static void removeProfileFromStorage(VpnProfile p) {
		deleteFile(getProfileDir(p));

	}
	
	public static int deleteVpnProfile(Profiles file) {
		// check list
		int ret = -1;
		File root = new File(PROFILES_ROOT);

		String[] dirs = root.list();
		if (dirs != null) {

			for (String dir : dirs) {
				File f = new File(new File(root, dir), PROFILE_OBJ_FILE);
				if (!f.exists()) {
					Log.i("VPN setting demo", "VPN: !f.exists ");
					continue;
				}
				try {
					VpnProfile p = (VpnProfile) deserialize(f);
					if (p == null) {
						Log.i("VPN setting demo", "VPN: This dir is null. "
								+ dir);
						continue;
					}

					Log.i("VPN setting demo", "VPN: Id = " + p.getId());
					Log.i("VPN setting demo", "VPN: Name = " + p.getName());
					Log.i("VPN setting demo", "VPN: DomainSuffices = "
							+ p.getDomainSuffices());
					Log.i("VPN setting demo", "VPN: RouteList = "
							+ p.getRouteList());
					Log.i("VPN setting demo", "VPN: ServerName = "
							+ p.getServerName());
					Log.i("VPN setting demo", "VPN: SavedUsername = "
							+ p.getSavedUsername());

					if ((p.getType() == VpnType.L2TP)
							|| (p.getType() == VpnType.L2TP_IPSEC)) {

						Log.i("VPN setting demo", "VPN: SecretString = "
								+ ((L2tpProfile) p).getSecretString());
					} else if (p.getType() == VpnType.L2TP_IPSEC_PSK) {
						Log.i("VPN setting demo", "VPN: preshared key = "
								+ ((L2tpIpsecPskProfile) p).getPresharedKey());
					}

					if (p.getType() == VpnType.L2TP_IPSEC) {
						Log.i("VPN setting demo", "VPN: user cert = "
								+ ((L2tpIpsecProfile) p).getUserCertificate());
						Log.i("VPN setting demo", "VPN: ca cert = "
								+ ((L2tpIpsecProfile) p).getCaCertificate());
					}

					Parcel parcel = Parcel.obtain();
					p.writeToParcel(parcel, 0);
					byte[] mOriginalProfileData = parcel.marshall();
					Log.i("VPN setting demo", "VPN: parcel.marshall = "
							+ mOriginalProfileData.toString());
					Log.i("VPN setting demo", "VPN: parcel.dataSize = "
							+ parcel.dataSize());
					if (p.getName().equals(file.mName)) {
						// find it
						removeProfileFromStorage(p);
					}

				} catch (IOException e) {
					Log
							.i("VPN setting demo",
									"retrieveVpnListFromStorage()", e);
				}
			}
		} else {
			Log.i("VPN setting demo", "VPN: !root.list ");
		}

		return ret;
	}
	 // Randomly generates an ID for the profile.
    // The ID is unique and only set once when the profile is created.
    private void setProfileId(VpnProfile profile) {
        String id;

        while (true) {
            id = String.valueOf(Math.abs(
                    Double.doubleToLongBits(Math.random())));
            if (id.length() >= 8) break;
        }
        
        /*
        for (VpnProfile p : mVpnProfileList) {
            if (p.getId().equals(id)) {
                setProfileId(profile);
                return;
            }
        }
        */
        Log.i("VPN setting demo", "VPN: dir name = " + id);
        profile.setId(id);
    }
    
    private static VpnProfile deserialize(File profileObjectFile) throws IOException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    profileObjectFile));
            VpnProfile p = (VpnProfile) ois.readObject();
       
            ois.close();
            return p;
        } catch (ClassNotFoundException e) {
            Log.i("VPN setting demo", "deserialize a profile", e);
            return null;
        }
    }
    
	  private void processSecrets(VpnProfile p) {
	        switch (p.getType()) {
	            case L2TP_IPSEC_PSK:
	                L2tpIpsecPskProfile pskProfile = (L2tpIpsecPskProfile) p;
	                String presharedKey = pskProfile.getPresharedKey();
	                String key = KEY_PREFIX_IPSEC_PSK + p.getId();
	                if (!TextUtils.isEmpty(presharedKey) &&
	                        !mKeyStore.put(key, presharedKey)) {
	                    Log.i("VPN setting demo", "VPN: keystore write failed: key=" + key);
	                }
	                pskProfile.setPresharedKey(key);
	           case L2TP_IPSEC:
	            case L2TP:
	                L2tpProfile l2tpProfile = (L2tpProfile) p;
	                key = KEY_PREFIX_L2TP_SECRET + p.getId();
	                if (l2tpProfile.isSecretEnabled()) {
	                    String secret = l2tpProfile.getSecretString();
	                    
	                    Log.i("VPN setting demo", "VPN: secret =" + secret);
	                    Log.i("VPN setting demo", "VPN: TextUtils.isEmpty =" + TextUtils.isEmpty(secret));
	                    if (!mKeyStore.password(tickerpassword))
	        				mKeyStore.reset();
	        			mKeyStore.password(tickerpassword);
	                    	
	                    if (!TextUtils.isEmpty(secret) &&
	                            !mKeyStore.put(key, secret)) {
	                        Log.i("VPN setting demo", "VPN: keystore write failed: key=" + key);
	                    }
	                    l2tpProfile.setSecretString(key);
	                    Log.i("VPN setting demo", "VPN: key=" + key);
	                } else {
	                    mKeyStore.delete(key);
	                }
	                break;
	        }
	    }
	  
	  private static String getProfileDir(VpnProfile p) {
	        return PROFILES_ROOT + p.getId();
	    }
	  private void saveProfileToStorage(VpnProfile p) throws IOException {
	        File f = new File(getProfileDir(p));
	        if (!f.exists()) 
	        {
	        	boolean ret = f.mkdirs();
	        	Log.i("VPN setting demo", "mkdirs =" + String.valueOf(ret));
	        	Log.i("VPN setting demo", "mkdirs =" + getProfileDir(p));
	        	//if (ret)
	        }
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
	                new File(f, PROFILE_OBJ_FILE)));
	        oos.writeObject(p);
	        oos.close();
	    }
}
