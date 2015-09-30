package com.android.accenture.aemm.express.updataservice;

import java.util.List;


import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.Status;
import android.security.Credentials;
import android.util.Log;
public class WifiLocalSetup {
	
	private static final String KEYSTORE_SPACE = "keystore://";
	static final int NONE = 0;
	static final int WEP = 1;
	static final int PSK = 2;
	static final int EAP = 3;
	static final int WPA = 4;
	
	public static enum EapValue{
		
		PEAP,TLS,TTLS
	}
	int protocol; //
	

	public void setWifiValues(String ssidValue,
			String securityValue,
			boolean hidden_network,
			String passwordValue,
			String eapValue,
			String cacertValue,
			String usercacertValue,
			String phase2Value,
			String anonymousidentityValue,
			String identityValue,
			String userPass,
			String eap)
	{
		this.ssidValue = "\""+ssidValue+"\"";//ssidValue;//"aemm-ap";//ssidValue; //none,wep,psk,eap
		this.securityValue = securityValue;//"PSK";//securityValue;
		this.hidden_network = hidden_network;
		this.passwordValue= "\""+passwordValue+"\"";//passwordValue;// "aemm-ap";//passwordValue;;

		this.eapValue = eapValue;;
		this.cacertUuid = cacertValue;
		this.usercacertUuid = usercacertValue;
		this.phase2Value = phase2Value;;
	

		this.anonymousidentityValue= anonymousidentityValue;
		this.identityValue = identityValue;
		this.userPassword = userPass;
		
		this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (eap!=null && eap.equals("EAP") == true)
		   this.eap = true;
		else
		   this.eap = false;
	}
	String securityValue; //none,wep,psk,eap
	String ssidValue;
	String passwordValue;
	boolean hidden_network;
	
	
	String eapValue;
	String cacertUuid;
	String usercacertUuid;
	String phase2Value;
	
	String anonymousidentityValue;
	String identityValue;
	String userPassword;
	
	
	
	boolean ssid = false;
    boolean security = false;
    boolean password = false;
    boolean ip = false;
    boolean subnetmask = false;
    boolean gateway = false;
    boolean dns = false;
    boolean eap = false;
    boolean phase2 = false;
    boolean identity = false;
    boolean anonymousidentity = false;
    boolean cacert = false;
    boolean usercert = false;
    WifiConfiguration config = null;
    int securityType = NONE;
    
	private List<WifiConfiguration> wifiConfigList;

	WifiManager mWifiManager;
	Context context;
	public  WifiLocalSetup(Context context)
	{
		config = new WifiConfiguration();
		this.context = context;
		this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	private int getSecurityType (String security) {
		if (security == null) {
			return NONE;
		} else {
			if (security.equalsIgnoreCase("NONE")) {
				return NONE;
			} else if (security.equalsIgnoreCase("WEP")) {
				if (eap == true)
					return EAP;
				else
					return WEP;
			} else if (security.equalsIgnoreCase("PSK")) {
				return PSK;
			} else if (security.equalsIgnoreCase("EAP")) {
				return EAP;
			} else if (security.equalsIgnoreCase("WPA")) {
				if (eap == true)
					return EAP;
				else
					return PSK;
			}
			else {
				return -1;
			}
		}
	}

	public WifiConfiguration getWifiConfig(String ssid){
		
		WifiConfiguration reWifiConfiguration=null;
		wifiConfigList = mWifiManager.getConfiguredNetworks();
		if(wifiConfigList!=null){
			for(WifiConfiguration conf:wifiConfigList){
				if(conf.SSID.equals("\""+ssid+"\"")){
					reWifiConfiguration=conf;
					break;
				}
			}
		}
		return reWifiConfiguration;
	}
	
	

	public boolean addNetWordLink(WifiConfiguration config) {
		boolean rlt = true;
		boolean rep = false;
		Log.v("V","start add WifiConfiguration");
		//mWifiManager.addNetwork(config)
		//Bug #2661 shxn
		List<WifiConfiguration> wifiList = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration wifi: wifiList) {
			if (wifi.SSID == config.SSID) {
				rep = true;
				break;
			}
		}
		int NetId = 0;
		if (!rep) {
			NetId = mWifiManager.addNetwork(config);
			//change by cuixiaowei on bug3374
			 if (NetId != -1) {
                 mWifiManager.enableNetwork(NetId, false);
                 mWifiManager.reconnect();
     		     rlt  = mWifiManager.saveConfiguration();
			 }
			Log.v("V","start save WifiConfiguration.."+NetId);

		}
		//Bug #2661 end
		if (NetId == -1){
			rlt = false;
		}
		return rlt;

	}

	public int deleteWifiConfiguration(String ssid)
    {
    	int ret = -1;
    	WifiConfiguration conf= getWifiConfig(ssid);
		if(conf!=null){
			//fix bug3374 by cuixiaowei 20110920
			mWifiManager.removeNetwork(conf.networkId);
			mWifiManager.saveConfiguration();
			ret = 0;
		}else{
			
		}
		return ret;
	}
	
	public boolean SetWifiConfiguration()
	{
		
		WifiConfiguration config = new WifiConfiguration();

		config.SSID = ssidValue;
		// If the user adds a network manually, assume that it is hidden.
		config.hiddenSSID = true;

		int mSecurity = getSecurityType(securityValue);
		Log.i("wifi",securityValue +  String.valueOf(mSecurity));
		switch (mSecurity) {
		case NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;
		case WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			if (passwordValue != null) {
				//fix bug3103 by cuixiaowei 20110818 begin
				config.wepKeys[0] = passwordValue;
				//int length = passwordValue.length();
				//String password = passwordValue;
				// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
				//if ((length == 10 || length == 26 || length == 58) &&
				//		password.matches("[0-9A-Fa-f]*")) {
					//config.wepKeys[0] = password;
				//} else {
				//	config.wepKeys[0] = '"' + password + '"';
				//}
				//fix bug3103 by cuixiaowei 20110818 end
			}
			break;
		case PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
            config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			if (passwordValue != null) {
				//fix bug3103 by cuixiaowei 20110818 begin
				config.preSharedKey = passwordValue;
				//String password = passwordValue;
				//if (password.matches("[0-9A-Fa-f]{64}")) {
					//config.preSharedKey = password;
				//} else {
				//	config.preSharedKey = '"' + password + '"';
				//}
				//fix bug3103 by cuixiaowei 20110818 end
			}
			break;
		case EAP:
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
			config.eap.setValue(eapValue);

			config.phase2.setValue(phase2Value == null ? "" :
				"auth=" + phase2Value);
			
			String cacertValue = null;
			if (cacertUuid != null)
			{
				cacertValue = CertificateLocalSetup.getCertName(context,cacertUuid);
			}
			config.ca_cert.setValue(cacertValue == null ? "" :
				KEYSTORE_SPACE + Credentials.CA_CERTIFICATE +
				cacertValue);
			String usercacertValue = null;
			if (usercacertUuid != null)
			{
				usercacertValue = CertificateLocalSetup.getCertName(context,usercacertUuid);
			}
			config.client_cert.setValue(usercacertValue == null ? "" :
				KEYSTORE_SPACE + Credentials.USER_CERTIFICATE +
				usercacertValue);
			config.private_key.setValue(usercacertValue == null ? "" :
				KEYSTORE_SPACE + Credentials.USER_PRIVATE_KEY +
				usercacertValue);
			config.identity.setValue(identityValue == null ? "" :
				identityValue);
			config.anonymous_identity.setValue(anonymousidentityValue == null ? "" :
				anonymousidentityValue);

			if (userPassword != null) {
				config.password.setValue(userPassword);
			}
			break;
		}
		
		return addNetWordLink(config);
	}
}