package com.android.accenture.aemm.express.updataservice;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import com.android.accenture.aemm.express.HallMessageManager;
import com.android.accenture.aemm.express.R;
import com.android.accenture.aemm.express.updataservice.ProfileContent.Profiles;
import com.android.accenture.aemm.express.updataservice.ProfileContent.RootProfile;
import com.android.accenture.aemm.express.updataservice.ProfileContent.RootProfileColumns;
import com.android.accenture.aemm.express.updataservice.ProfileContent.WebClipProfileContent;
import com.android.accenture.aemm.express.updataservice.configParser.ProfileType;

public class profile {
	public static final String TAG = "profile";
	
	public static String  node_value_identifier = "PayloadIdentifier";
	public static String  node_value_payloadcontent = "PayloadContent";
	public static String  node_value_desc = "PayloadDescription";
	public static String  node_value_diplayname = "PayloadDisplayName";
	public static String  node_value_organization = "PayloadOrganization";
	public static String  node_value_removalDisallowed  = "PayloadRemovalDisallowed";
	public static String  node_value_payloadtype = "PayloadType";
	public static String  node_value_payloaduuid = "PayloadUUID";
	public static String  node_value_payloadversion = "PayloadVersion";
	
	
	public String Indentifier;
	public String DisplayName;
	public String Type;
	public String Description;
	public String Version;
	public String Uuid;
	public String Organzation;
	
	
	public static profile createProfile(ProfileType type) {
		Log.i(TAG, "createProfile : " + type.toString());
		
		profile pp = null;
		switch (type) {
		case Profile_ROOT:
			break;
		case Profile_Email:
			pp = new EmailProfile();
			break;
		case Profile_Exchange:
			pp = new ExchangeProfile();
			break;
		case Profile_APN:
			pp = new ApnProfile();
			break;
		case Profile_Wifi:
			pp = new WifiProfile();
			break;
		case Profile_WebClip:
			pp = new WebClipProfile();
			break;
		case Profile_PassPolicy:
			pp = new PwPolicyProfile();
			break;
		case Profile_RootCertificate:
		case Profile_PkcsCertificate:
			pp = new CertificateProfile();
			break;
		case Profile_VPN:
			pp = new vpnProfile();
			break;
		case Profile_Restrictions:
			pp = new restrictionProfile();
		default:
			break;
		}

		return pp;
	}
	
	//set
	public void setIndentifier(String indentifier)
	{
		this.Indentifier = indentifier;
	}
	public void setDisplayName(String displayName)
	{
		this.DisplayName = displayName;
	}
	public void setType(String type)
	{
		this.Type = type;
	}
	public void setDescription(String description)
	{
		this.Description = description;
	}
	public void setVersion(String version)
	{
		this.Version = version;
	}
	public void setUuid(String uuid)
	{
		this.Uuid = uuid;
	}
	public void setOrganzation(String organzation)
	{
		this.Organzation = organzation;
	}
	
	//get
	public String getIndentifier()
	{
		return Indentifier;
	}
	public String getDisplayName()
	{
		return DisplayName;
	}
	public String getType()
	{
		return Type;
	}
	public String getDescription()
	{
		return Description;
	}
	public String getVersion()
	{
		return Version;
	}
	public String getUuid()
	{
		return Uuid;
	}
	public String getOrganzation()
	{
		return Organzation;
	}
	
	//save
	public int saveProfile(Context context )
	{
		Log.i(TAG,"root1 save");
		
		//
		RootProfile c = RootProfile.restoreRootProfileWithType(context, this.Type);
		if (c != null)
		{
			//update
			ContentValues value = c.toContentValues();
			value.put(RootProfileColumns.VERSION, this.Version);
			RootProfile.update(context, RootProfile.CONTENT_URI, c.mId, value);
			
		}else{
			RootProfile rp = new RootProfile();
			rp.mDescription = getDescription();
			rp.mIndentifier = this.getIndentifier();
			rp.mOrganzation = this.getOrganzation();
			rp.mType = this.getType();
			rp.mUuid = this.getUuid();
			rp.mVersion = this.getVersion();
         
			rp.save(context);
		}
		return 0;
	}

	public int deleteProfile()
	{
		return 0;
	}
	public int setArray(ArrayList<BasicNameValuePair> list)
	{
		int ret = -1;
		
		return ret;
	}
	public int setValue(String keyValue, String value)
	{
		//Log.i(TAG,"set value " + keyValue + value);
		if (keyValue.equals(profile.node_value_identifier))
		{
			setIndentifier(value);
		}
		else if (keyValue.equals(profile.node_value_payloaduuid))
		{
			setUuid(value);
		}
			
		return 0;
	}
	
	public void printValue()
	{
		Log.i(TAG, "Indentifier: " +this.getIndentifier());
		Log.i(TAG, "displayname: " +this.getDisplayName());
		Log.i(TAG, "type: " +this.getType());
		Log.i(TAG, "organzation: " +this.getOrganzation());
		Log.i(TAG, "version: " +this.getVersion());
		Log.i(TAG, "uuid: " +this.getUuid());
	}

	public int clearProfile(Context context) {
		return 0;
	}

}
/**
 * WebClipProfile class extends from profile
 * @author vivian.yun.feng
 *
 */
class WebClipProfile extends profile{
	
	
	public static String  node_value_icon = "Icon";
	public static String  node_value_isremovable = "IsRemovable";
	public static String  node_value_url = "URL";
	public static String  node_value_name = "Label";
	public static String  node_value_indentifier = "PayloadIdentifier";
	
	
	
	private boolean isRemovable;
	private String webClipName = null;
	private String webClipUrl= null;
	private String webClipIcon= null;
	private String webClipIdentifier= null;
	
	//fix bug2734 by cuixiaowei 20110725
	public static ArrayList<WebClipProfileContent> newWebClipList 	= new ArrayList<WebClipProfileContent>();
	public static ArrayList<WebClipProfileContent> oldWebClipList 	= new ArrayList<WebClipProfileContent>();
	
	//set
	public void setWebClipName(String name)
	{
		this.webClipName = name;
	}
	public void setWebClipUrl(String url)
	{
		this.webClipUrl = url;
	}
	public void setWebClipIcon(String icon)
	{
		this.webClipIcon = icon;
	}
	public void setWebClipIdentifier(String identifier)
	{
		this.webClipIdentifier = identifier;
	}
	public void setIsRemovable(boolean b)
	{
		this.isRemovable = b;
	}
	
	//get
	public String gettWebClipName()
	{
		return this.webClipName;
	}
	public String getWebClipUrl()
	{
		return this.webClipUrl;
	}
	public boolean getIsRemovable()
	{
		return this.isRemovable;
	}
	public String getWebClipIcon()
	{
		return this.webClipIcon;
	}
	public String getWebClipIdentifier()
	{
		return this.webClipIdentifier;
	}
	
	
	public int setValue(String keyValue, String value)
	{
		//Log.i(TAG,"set value " + keyValue + value);
		if (keyValue.equals(WebClipProfile.node_value_indentifier))
		{
			Log.i(TAG,"set value " + keyValue + value);
			setWebClipIdentifier(value);
		}
		else if (keyValue.equals(WebClipProfile.node_value_name))
		{
			setWebClipName(value);
		}
		else if (keyValue.equals(WebClipProfile.node_value_url))
		{
			setWebClipUrl(value);
		}
		else if (keyValue.equals(WebClipProfile.node_value_isremovable))
		{
			setIsRemovable(value.equals("true")  ? true : false);
		}
		else if (keyValue.equals(WebClipProfile.node_value_icon))
		{
			setWebClipIcon(value);
		}
		else
		{
			super.setValue(keyValue, value);
		}
		
		return 0;
	}
	
	public int saveProfile(Context context )
	{
		Log.i(TAG,"Webclip Profile Save");
		int ret = -1;

		clearProfile2(context);

		if (this.webClipUrl != null && this.webClipName != null)
		{
			Log.i(TAG,"create a new webclip Profile");
			//WebClipLocalSetUp.addUrlShort(context,this.webClipName,this.webClipUrl,this.webClipIcon);
			
			WebClipProfileContent newWc = new WebClipProfileContent();
			newWc.mWebClipIdentifier = this.webClipIdentifier;
			newWc.mWebClipIcon = this.webClipIcon;
			newWc.mWebClipName = this.webClipName;
			newWc.mWebClipUrl = this.webClipUrl;
			newWc.mWebClipVersion = this.Version;
			newWc.mWebClipFlag = 1;
			//fixed bug2700, 2708,2707 by cuixiaowei 20110721
			//if icon of webclick is null, use special icon instead.
			if(this.webClipIcon == null)
			{
				BitmapDrawable bmpDraw=(BitmapDrawable)context.getResources().getDrawable(R.drawable.webclipicon);				
				Bitmap icon=bmpDraw.getBitmap();
                newWc.mWebClipIcon = Bitmap2String(icon);
			}
			Log.i(TAG,"Webclip Profile  add new WebClipProfileContent in newWebClipList");
			newWebClipList.add(newWc); //fix bug2734 by cuixiaowei 20110725
			
			Uri uri = newWc.save(context);
			if (uri != null)
				ret = 0;
		}
		
		return ret;
	}
	
	@Override
	public int clearProfile(Context context) {
		Log.i(TAG,"webclip clear");
		
		String version = getVersion();
		if (version == null) {
			//delete all Webclip Profile in db
			version = Integer.toString(-1);
		}

		ArrayList<WebClipProfileContent> list = WebClipProfileContent.restoreWebClipWithVersion(context, version);
		if (list != null)
		{
			for (int i=0 ;i< list.size(); i++)
			{
				WebClipProfileContent item = list.get(i);
				WebClipLocalSetUp.delUrlShort(context,item.mWebClipName,item.mWebClipUrl,item.mWebClipIcon);
				WebClipProfileContent.deleteWebClipWithId(context,item.mId);
			}
		}
		return 0;
	}
	
	public int clearProfile2(Context context) {
		Log.i(TAG,"webclip clear");
		
		String version = getVersion();
		if (version == null) {
			//delete all Webclip Profile in db
			version = Integer.toString(-1);
		}

		ArrayList<WebClipProfileContent> list = WebClipProfileContent.restoreWebClipWithVersion(context, version);
		if (list != null)
		{
			for (int i=0 ;i< list.size(); i++)
			{
				WebClipProfileContent item = list.get(i);
				oldWebClipList.add(item);//fix bug2734 by cuixiaowei 20110725
				//WebClipLocalSetUp.delUrlShort(context,item.mWebClipName,item.mWebClipUrl,item.mWebClipIcon);
				WebClipProfileContent.deleteWebClipWithId(context,item.mId);
			}
		}
		return 0;
	}
	
	//fixed bug2700, 2708,2707 by cuixiaowei 20110721
	private String Bitmap2String(Bitmap bm){
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		 bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
		 
		 return new String( Base64.encode(baos.toByteArray(),0));
	}
	
	//fix bug2734 by cuixiaowei 2011025
	//filter same webclip which needed not to be deleted.
	public static void refreshWebClipShort(Context context){
		if(newWebClipList != null){
			for(int i=0;i<oldWebClipList.size();i++)
			{
				WebClipProfileContent oldItem = oldWebClipList.get(i);
				int j = 0;
				for( j=0;j<newWebClipList.size();j++){
					WebClipProfileContent newItem = newWebClipList.get(j);
					if(oldItem.mWebClipIcon.equals(newItem.mWebClipIcon)&&
						oldItem.mWebClipName.equals(newItem.mWebClipName) &&
						oldItem.mWebClipUrl.equals(newItem.mWebClipUrl))
						{
						    newItem.mWebClipFlag = 0;
							break;
						}						
				}
				if(j == newWebClipList.size())
				{
					Log.i(TAG,"Webclip Profile:  delete WebClipShort "+ oldItem.mWebClipName + " " + oldItem.mWebClipUrl);
					WebClipLocalSetUp.delUrlShort(context,oldItem.mWebClipName,oldItem.mWebClipUrl,oldItem.mWebClipIcon);
				}
				else
				{
					continue;
				}
				
			}
			
			for( int j=0;j<newWebClipList.size();j++){
				WebClipProfileContent newItem = newWebClipList.get(j);
				//fix bug 2385 by cuixiaowei 20110825
				if((newItem.mWebClipFlag == 1) || !WebClipLocalSetUp.hasShortcut(context, newItem.mWebClipName)) 
				{
					Log.i(TAG,"Webclip Profile:  add WebClipShort "+ newItem.mWebClipName + " " + newItem.mWebClipUrl);
					WebClipLocalSetUp.addUrlShort(context,newItem.mWebClipName,newItem.mWebClipUrl,newItem.mWebClipIcon);
				}	
			}
			newWebClipList.clear();
			oldWebClipList.clear();
			}
	}
}

/**
 * ApnProfile class extends from profile
 * @author vivian.yun.feng
 *
 */
class ApnProfile extends profile{
	
	public static final String  node_value_apns = "apns";
	public static final String  node_value_apn = "apn";
	public static final String  node_value_username = "username";
	public static final String  node_value_password = "password";
	public static final String  node_value_proxy = "proxy";
	public static final String  node_value_proxyPort = "proxyPort";
	public static final String  node_value_DefaultsDomainName = "DefaultsDomainName";


	String apnApn = null;
	String apnName  = null;
	String apnType  = null;

	String userName  = null;
	String password  = null;
	String proxy  = null;
	String proxyPort  = null;



	String defaultsDomainName;

	public ApnProfile ()
	{
		
	}
	public String getApnName()
	{
		return apnName;
	}
	public String getApnApn()
	{
		return apnApn;
	}
	public String getApnType()
	{
		return apnType;
	}

	public int setValue(String key, String value)
	{
		
		if (key.equals(node_value_apn))
		{
			apnApn = value;
		}
		else if (key.equals(node_value_username))
		{
			userName = value;
		}
		else if (key.equals(node_value_password))
		{
			password = value;
		}
		else if (key.equals(node_value_proxy))
		{
			proxy = value;
		}
		else if (key.equals(node_value_proxyPort))
		{
			proxyPort = value;
		}
		else if (key.equals(node_value_DefaultsDomainName))
		{
			defaultsDomainName = value;
		}
		else
		{
			super.setValue(key, value);
		}
		return 0;

	}
	public int saveProfile(Context context)
	{
		StringBuilder info = new StringBuilder("Webclip Profile Save");
		info.append(" : ");
		info.append(apnName);
		info.append(" : ");
		info.append(Version);
		Log.i(TAG, info.toString());
		if (apnName == null)
			apnName = apnApn;

//		int ret = -1;

		clearProfile(context);
		
		//just insert a record
		//first add in local
		ApnLocalSetup apnLocal = new ApnLocalSetup(context);
		ContentValues values = apnLocal.toContentValues(this.apnName,
				this.apnApn,
				this.userName,
				this.password,
				this.proxy,
				this.proxyPort);
		
		if (values == null)
			return -1;
		int cId = apnLocal.AddApnSetting(values);
		if (cId > 0)
		{
			String type = String.valueOf(configParser.ProfileType.Profile_APN);
			Profiles wp = new Profiles();
			wp.mName = apnName;
			wp.mUuid = Uuid;
			wp.mCid = String.valueOf(cId);
			wp.mVersion = Version;
			wp.mType = type;
			wp.save(context);
		}

		return 0;
		
	}
	
	@Override
	public int clearProfile(Context context) {
		Log.i(TAG,"Apn Profile Clear");
		if (null == Version) {
			//delete all Apn Profile in db
			Version = Integer.toString(-1);
		}
		String type = String.valueOf(configParser.ProfileType.Profile_APN);
		ArrayList<Profiles> list = Profiles.restoreProfilesWithTypeandVersion(context, type , Version);

		if (list != null)
		{
			for (int i =0; i< list.size() ;i++)
			{
				Profiles wp = list.get(i);
				if (wp != null)
				{
					int cid = Integer.valueOf(wp.mCid);
					//the id in local setting db
					ApnLocalSetup.deleteApnSetting(context, cid);
					Profiles.deleteProfileswithId(context,wp.mId);
				}
			}
		}
		return 0;
	}
	
}

/**
 * WebClipProfile class extends from profile
 * @author vivian.yun.feng
 *
 */
class PwPolicyProfile extends profile{
	
	
	public static final String  node_value_allowSimple = "allowSimple";
	public static final String  node_value_forcePIN = "forcePIN";
	public static final String  node_value_maxFailedAttempts = "maxFailedAttempts";
	public static final String  node_value_maxGracePeriod = "maxGracePeriod";
	public static final String  node_value_maxInactivity = "maxInactivity"; 
	public static final String  node_value_minComplexChars = "minComplexChars";
	public static final String  node_value_minLength = "minLength";
	public static final String  node_value_requireAlphanumeric = "requireAlphanumeric";
	
	public static final String  node_value_maxPINAgeInDays = "maxPINAgeInDays";
	public static final String  node_value_pinHistory = "pinHistory";
	
	private boolean bAllowSimple;
	private boolean bRequireAlphanumeric;
	private boolean bForecPIN;
	//private int  pwPolicy;
	private int  pwLength;
	private int  mxFailPw;
	private int  mxTimeOut;
	private int  mxGracePeriod;
	private int  minComplexNum;
	private int  maxPINAgeInDays;
	private int  pinHistory;
	//private int  maxFailedAttempts;
	//private int  maxInactivity;
	
//	public void setPwPolicy(int policy)
//	{
//		pwPolicy = policy;
//	}
	public void setPwLength(int len)
	{
		pwLength = len;
	}
	public void setMaxFailPw(int times)
	{
		mxFailPw = times;
	}
	public void setMaxTimeout(int minutes)
	{
		mxTimeOut = minutes;
	}
	public int setValue(String keyValue, String value)
	{
		int ret = 0;
		Log.i("PwPolicyProfile", "key: " + keyValue + ", value: " + value);

		if (keyValue.equals(PwPolicyProfile.node_value_allowSimple))
		{
			bAllowSimple = value.equals("true")  ? true : false;
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_forcePIN))
		{
			bForecPIN = value.equals("true")  ? true : false;
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_maxFailedAttempts))
		{
			mxFailPw  = Integer.valueOf(value);
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_maxGracePeriod))
		{
			mxGracePeriod = Integer.valueOf(value);
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_maxInactivity))
		{
			mxTimeOut = Integer.valueOf(value);
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_requireAlphanumeric))
		{
			bRequireAlphanumeric =  value.equals("true")  ? true : false;
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_minLength))
		{
			pwLength = Integer.valueOf(value);
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_minComplexChars))
		{
			minComplexNum = Integer.valueOf(value);
		}
//		else if (keyValue.equals(PwPolicyProfile.node_value_maxFailedAttempts))
//		{
//			maxFailedAttempts = Integer.valueOf(value);
//		}
//		else if (keyValue.equals(PwPolicyProfile.node_value_maxInactivity))
//		{
//			maxInactivity = Integer.valueOf(value);
//		}
		else if (keyValue.equals(PwPolicyProfile.node_value_maxPINAgeInDays))
		{
			maxPINAgeInDays = Integer.valueOf(value);
		}
		else if (keyValue.equals(PwPolicyProfile.node_value_pinHistory))
		{
			pinHistory = Integer.valueOf(value);
		}

		return ret;
	}
	
	public int saveProfile( Context context ) {
		DeviceAdminLocalSetup da = DeviceAdminLocalSetup.getInstance(context);
		int rlt = -1;
		
		da.setDeviceAdminValues(bAllowSimple,bForecPIN,
				mxFailPw,mxGracePeriod,mxTimeOut,minComplexNum,pwLength,bRequireAlphanumeric,
				maxPINAgeInDays, pinHistory);
		
		//da.enableDeviceAdmin(context);

		if (da.updatePolicies(context)) {
			da.checkPolicies(context);
			rlt = 0;
		}
		return rlt;
	}
	
	//Bug #2650
	@Override
	public int clearProfile(Context context) {
		int ret  = -1;

		DeviceAdminLocalSetup.resetPolicy(context);
		return ret;
	}
	//Bug #2650
}
/**
 * VpnProfile class extends from profile
 * @author vivian.yun.feng
 *
 */
class vpnProfile extends profile{
	
	
	
	public static final String  node_value_defineName = "UserDefinedName";
	public static final String  node_value_OverridePrimary = "OverridePrimary";
	public static final String  node_value_VPNType = "VPNType";
	//ppp
	public static final String  node_value_AuthName = "AuthName";
	public static final String  node_value_AuthPassword = "AuthPassword";
	public static final String  node_value_CommRemoteAddress = "CommRemoteAddress";
	public static final String  node_value_AuthEAPPlugins = "AuthEAPPlugins";
	public static final String  node_value_AuthProtocol = "AuthProtocol";
	public static final String  node_value_CCPMPPE40Enabled = "CCPMPPE40Enabled";
	public static final String  node_value_CCPMPPE128Enabled = "CCPMPPE128Enabled";
	public static final String  node_value_CCPEnabled = "CCPEnabled";
	
	//Ipsec
	public static final String  node_value_RemoteAddress = "RemoteAddress";
	public static final String  node_value_AuthenticationMethod = "AuthenticationMethod";
	public static final String  node_value_XAuthName = "XAuthName";
	public static final String  node_value_XAuthEnabled = "XAuthEnabled";
	public static final String  node_value_LocalIdentifier = "LocalIdentifier";
	public static final String  node_value_LocalIdentifierType = "LocalIdentifierType";
	public static final String  node_value_SharedSecret = "SharedSecret";
	
	public static final String  node_value_PayloadCertificateUUID = "PayloadCertificateUUID";
	String mVpnType; //"L2TP", "PPTP", or "IPSec",
	String mVpnDefineName;
	boolean mOverridePrimary;
	
	//ppp
	String mAuthName;
	String mAuthPassword; 
	String mCommRemoteAddress; 
	String mAuthEAPPlugins;
	boolean mIsSecret;
	
	//L2TP
	String mSecretString;
	
	//Ipesc
	String mRemoteAddress;        // VPN server name
	String mAuthenticationMethod; // Either "SharedSecret" or "Certificate". Used for L2TP and Cisco IPSec
	String mXAuthName;            // space separated list
	String mLocalIdentifier;
	String mLocalIdentifierType;  // Present only if AuthenticationMethod = SharedSecret. The value is "KeyID"
	boolean mXAuthEnabled;
	String mSharedSecret;

	String mUserCertUuid;
	String mCaCertUuid;
	public int setValue(String keyValue, String value)
	{
		int ret = 0;
		if (keyValue.equals(node_value_defineName))
		{
			mVpnDefineName = value;
		}
		else if (keyValue.equals(node_value_OverridePrimary))
		{
			mOverridePrimary = value.equals("1")? true: false;
		}
		else if (keyValue.equals(node_value_VPNType))
		{
			mVpnType = value; //"L2TP", "PPTP", or "IPSec",
		}
		else if (keyValue.equals(node_value_AuthName))
		{
			mAuthName = value;
		}
		else if (keyValue.equals(node_value_AuthPassword))
		{
			mAuthPassword = value;
		}
		else if (keyValue.equals(node_value_CommRemoteAddress))
		{
			mCommRemoteAddress = value;
		}
		else if (keyValue.equals(node_value_AuthEAPPlugins))
		{
			mAuthEAPPlugins = value;
		}
		else if (keyValue.equals(node_value_AuthProtocol))
		{
			
		}
		else if (keyValue.equals(node_value_CCPMPPE40Enabled))
		{
			
		}
		else if (keyValue.equals(node_value_CCPMPPE128Enabled))
		{
			
		}
		else if (keyValue.equals(node_value_CCPEnabled))
		{
			mIsSecret = value.equals("1")?true:false;
		}
		else if (keyValue.equals(node_value_RemoteAddress))
		{
			mRemoteAddress = value;
		}
		else if (keyValue.equals(node_value_AuthenticationMethod))
		{
			mAuthenticationMethod = value;
		}
		else if (keyValue.equals(node_value_XAuthName))
		{
			mXAuthName = value;
		}
		else if (keyValue.equals(node_value_XAuthEnabled))
		{
			mXAuthEnabled = value.equals("1")? true:false;
		}
		else if (keyValue.equals(node_value_LocalIdentifier))
		{
			mLocalIdentifier = value;
		}
		else if (keyValue.equals(node_value_LocalIdentifierType))
		{
			mLocalIdentifierType = value;
		}
		else if (keyValue.equals(node_value_SharedSecret))
		{
			mSharedSecret = value;
		}
		else if (keyValue.equals(node_value_PayloadCertificateUUID))
		{
			mUserCertUuid = value;
		}
		else
		{
			super.setValue(keyValue, value);
		}
		return ret;
	}
	public int saveProfile(Context context)
	{
		
		//String secret = null;
		//String userCert = null;
		//String caCert = null;
		
		if (mVpnType.equals("IPSec"))
		{
			//Either "SharedSecret" or "Certificate".
			/*if (mAuthenticationMethod.equals("SharedSecret"))
				mVpnType = "L2TP_IPSEC_PSK";
			
			else if (mAuthenticationMethod.equals("Certificate"))
				mVpnType = "L2TP_IPSEC";
			
			serverName = mRemoteAddress;
			secret = mLocalIdentifierType;*/
		}
		else
		{
			//only support PPTP and L2TP
			if (mVpnType.equals("PPTP"))
				mVpnType = "PPTP";
			else if (mVpnType.equals("L2TP"))
			{
				//change by fengyun for bug 3156
				if(mSharedSecret != null)
					mVpnType = "L2TP_IPSEC_PSK";
				else
					mVpnType = "L2TP";
			}
			else
				return -1;
				//error type
				//mVpnType = "L2TP_IPSEC";
				
		}
		VpnLocalSetup vpnSetup = new VpnLocalSetup(context);
		
		clearProfile(context);
		
		vpnSetup.setvalues(mVpnType,
						   mVpnDefineName,
						   mCommRemoteAddress,
						   mAuthName,
						   mIsSecret,
						   mSecretString,
						   mSharedSecret,
						   mUserCertUuid,
						   mCaCertUuid);
		
		if (vpnSetup.setVpn() == 0)
		{
			//add ok
			String type = String.valueOf(configParser.ProfileType.Profile_VPN);
			Profiles wp = new Profiles();
			wp.mName = vpnSetup.mServerName;
			wp.mUuid = Uuid;
			wp.mCid = "-1";//String.valueOf(cId);
			wp.mVersion = Version;
			wp.mType = type;
			wp.save(context);
			return 0;
		}
		
		return -1;
	}
	
	@Override
	public int clearProfile(Context context) {
		
		if (null == Version) {
			//delete all Profile in db
			Version = Integer.toString(-1);
		}

		String type = String.valueOf(configParser.ProfileType.Profile_VPN);
		ArrayList<Profiles> vpnProfiles  = Profiles.restoreProfilesWithTypeandVersion(
									context, type , Version);
		Log.i(TAG,"Vpn Profile Clear");
		Log.v(TAG,"Version" + Version);

		if (vpnProfiles != null)
		{
			for (int i =0; i< vpnProfiles.size() ;i++)
			{
				Profiles item = vpnProfiles.get(i);
				if (item != null)
				{
					Log.i(TAG,"delete");
					String  id  = item.mName;
					VpnLocalSetup.deleteVpnProfile(item);
					//the id in local setting db
					//apnLocal.deleteApnSetting(cid);
					int num = Profiles.deleteProfileswithId(context,item.mId);//wp.mId);

					Log.i(TAG,  id  + "has been deleted" + "delete num is" + String.valueOf(num));
				}
			}
		}
		return 0;
	}
	
	
}
/**
 * WifiProfile class extends from profile
 * @author vivian.yun.feng
 *
 */
class WifiProfile extends profile{

	static final int SECURITY_NONE = 0;
	static final int SECURITY_WEP = 1;
	static final int SECURITY_PSK = 2;
	static final int SECURITY_EAP = 3;

	class EAPClientConfiguration{
		boolean EAPFASTProvisionPAC;
		boolean EAPFASTProvisionPACAnonymously;
		boolean EAPFASTUsePAC;
		String[] PayloadCertificateAnchorUUID;
		boolean TLSAllowTrustExceptions;
		String[] TLSTrustedServerNames;
		String UserName;
		String UserPassword;
	}
	String Security;
	String EncryptionType;
	String SSID_STR;
	String Password;
	boolean HIDDEN_NETWORK;
	//EAPClientConfiguration eapClient;
	String userCertsUuid;
	String caCertUuid;
	String eapValue;
	String phase2;
	String anonymous_identity;
	String identity;
	String userPass;
	String eap;
	
	public WifiProfile()
	{
		//eapClient = new EAPClientConfiguration();
	}
	public int setValue(String keyValue, String nodeValue)
	{
		if (keyValue.equals("EncryptionType")) {
			EncryptionType = nodeValue;
			if (EncryptionType.equals("WEP")) {
				Security = nodeValue;
			} else if (EncryptionType.equals("WPA")) {
				Security = nodeValue;
			} else {
				Security = "NONE";
			}
		}
		if (keyValue.equals("Password"))
		{
			Password = nodeValue;
		}
		if (keyValue.equals("SSID_STR"))
		{
			SSID_STR = nodeValue;
		}
		if (keyValue.equals("HIDDEN_NETWORK"))
		{
			HIDDEN_NETWORK = nodeValue.equals("true")? true: false;
		}
		/*if (keyValue.equals("EAPFASTProvisionPACAnonymously"))
		{
			if (nodeValue.equals("true"))
			{
				eapClient.EAPFASTProvisionPACAnonymously = true;
			}
			else 
			{
				eapClient.EAPFASTProvisionPACAnonymously = false;
			}
				
		}*/
		if (keyValue.equals("PayloadCertificateAnchorUUID"))
		{
			caCertUuid = nodeValue;
		}
		if (keyValue.equals("PayloadCertificateUUID"))
		{
			userCertsUuid = nodeValue;
		}
		
		if (keyValue.equals("TTLSInnerAuthentication"))
		{
			//eapValue = "TTLS";
		}
		if (keyValue.equals("EAPClientConfiguration"))
		{
			eap = "EAP";
		}
		if (keyValue.equals("AcceptEAPTypes"))
		{
			//eap type
			//13 = TLS '
			//17 = LEAP
			//21 = TTLS'
			//25 = PEAP'
			//43 = EAP-FAST
			//only support TLS,TTLS,PEAP on android
			if (nodeValue.equals("13"))
				eapValue = "TLS";
			else if (nodeValue.equals("21"))
				eapValue = "TTLS";
			else if (nodeValue.equals("25"))
				eapValue = "PEAP";
			else 
				eapValue = "NONE";
				
		}
		if (keyValue.equals("OuterIdentity"))
		{
			anonymous_identity = nodeValue;
		}
		if (keyValue.equals("TTLSInnerAuthentication"))
		{
			phase2 = nodeValue;
		}
		if (keyValue.equals("UserName"))
		{
			identity = nodeValue;
		}
		if (keyValue.equals("UserPassword"))
		{
			userPass = nodeValue;
		}
		else
		{
			super.setValue(keyValue, nodeValue);
		}
		return 0;

	}
	
	public int saveProfile(Context context)
	{
		//check if it already exist
		int ret = -1;
		Profiles wifiProfile = null;
		
		clearProfile(context);
		
		WifiLocalSetup ws  = new WifiLocalSetup(context);

		ws.setWifiValues(this.SSID_STR,
												 this.Security,
												 this.HIDDEN_NETWORK,
												 this.Password,
												 this.eapValue,
												 this.caCertUuid,
												 this.userCertsUuid,
												 this.phase2,
												 this.identity,
												 this.anonymous_identity,
												 this.userPass,
												 this.eap);
		if (ws.SetWifiConfiguration() == true)
		{
			String type = String.valueOf(configParser.ProfileType.Profile_Wifi);
			wifiProfile = new Profiles();
		
			wifiProfile.mName = this.SSID_STR;
			wifiProfile.mUuid = this.Uuid;
			wifiProfile.mCid = String.valueOf(-1); //not used for wifi
			wifiProfile.mVersion = Version;
			wifiProfile.mType = type;
			wifiProfile.save(context);
			ret = 0;
		}
		return ret;
	}
	@Override
	public int clearProfile(Context context) {
		Log.i(TAG,"WIFI Profile Clear");
		if (null == Version) {
			//delete all Profile in db
			Version = Integer.toString(-1);
		}
		String type = String.valueOf(configParser.ProfileType.Profile_Wifi);
		ArrayList<Profiles> list = Profiles.restoreProfilesWithTypeandVersion(context, type , Version);

		if (list != null)
		{
			for (int i =0; i< list.size() ;i++)
			{
				Profiles wp = list.get(i);
				if (wp != null)
				{
					Log.i(TAG,"delete");
					String ssid = wp.mName;
					//fix bug2657 by cuixiaowei 20110719
					//if (ssid.equals(SSID_STR))
					WifiLocalSetup ws  = new WifiLocalSetup(context);
					ws.deleteWifiConfiguration(ssid);
					Profiles.deleteProfileswithId(context, wp.mId);
										
				}
			}
		}
		return 0;
	}
}
class restrictionProfile extends profile {
	String allowAppInstallation;
	
	public restrictionProfile() {

	}
	
	public int setValue(String key,String value)
	{
		if (key.equals("allowAppInstallation"))
		{
			allowAppInstallation = value;
		}
		return 0;
	}
	
	public int saveProfile(Context context)
	{
//		int ret = -1;
		//update apkprofile
		clearProfile(context);
		boolean bfag = allowAppInstallation.equals("true")?true:false;
		HallMessageManager.HallAppInstallEnabled(context,bfag);
		
		//
		configPreference.putHallEnabled(context,bfag);
		return 0;
	}
	
	@Override
	public int clearProfile(Context context) {

		HallMessageManager.HallAppInstallEnabled(context,true);
		configPreference.putHallEnabled(context,true);
		return 0;
	}
}
/**
 * WifiProfile class extends from profile
 * @author vivian.yun.feng
 *
 */
class CertificateProfile extends profile{

	public static final String ROOTCERTIFICATE = "com.apple.security.root";
	public static final String PSKCERTIFICATE = "com.apple.security.pkcs12";
	String data;
	String Password;
	String Certificatetype;
	
	String displayName;


	public int setValue(String key,String value)
	{
		if (key.equals("PayloadContent"))
		{
			data = value;
		}
		else if (key.equals("PayloadType"))
		{
			Certificatetype = value;
		}
		else if (key.equals("Password"))
		{
			Password = value;
		}
		else if (key.equals("PayloadDisplayName"))
		{
			displayName = value;
		}
		else
		{
			return super.setValue(key, value);
		}
		return 0;
	}
	public int saveProfile(Context context)
	{
//		int ret = -1;
		String type = null;
		Log.v(TAG,Certificatetype);
		

		
		clearProfile(context);
		
		if (Certificatetype != null)
		{
			if (Certificatetype.equals(ROOTCERTIFICATE))
				type = String.valueOf(configParser.ProfileType.Profile_RootCertificate);
			else if (Certificatetype.equals(PSKCERTIFICATE))
				type = String.valueOf(configParser.ProfileType.Profile_PkcsCertificate);
		}		
		CertificateLocalSetup ss = new CertificateLocalSetup(context);
		ss.setValues(data,displayName,Password,type);
		ss.setup();
		
		//
		//just insert a record
		//first add in local
	

		{
			Profiles wp = new Profiles();
			wp.mName = displayName;
			wp.mUuid = Uuid;
			//wp.mCid = ""-1;
			wp.mVersion = Version;
			wp.mType = type;
			wp.save(context);
		}
		return 0;

	}
	@Override
	public int clearProfile(Context context) {
		String type = null;
		
		if (Certificatetype != null)
		{
			if (Certificatetype.equals(ROOTCERTIFICATE))
				type = String.valueOf(configParser.ProfileType.Profile_RootCertificate);
			else if (Certificatetype.equals(PSKCERTIFICATE))
				type = String.valueOf(configParser.ProfileType.Profile_PkcsCertificate);
		} else {
			type = String.valueOf(configParser.ProfileType.Profile_PkcsCertificate);
		}
		if (null == Version) {
			//delete all Profile in db
			Version = Integer.toString(-1);
		}
		ArrayList<Profiles> list = Profiles.restoreProfilesWithTypeandVersion(context, type , Version);

		if (list != null)
		{
			for (int i =0; i< list.size() ;i++)
			{
				Profiles wp = list.get(i);
				if (wp != null)
				{
					Log.i(TAG,"delete");
					String certname = wp.mName;
					//the id in local setting db
					CertificateLocalSetup.deleteCert(certname);
					int num = Profiles.deleteProfileswithId(context,wp.mId);//wp.mId);
					Log.i(TAG,  String.valueOf(num) + "cert record has been deleted");
				}
			}
		}
		return 0;
	}
}

class EmailProfile extends profile{
	
	String emailAddress = null;
	String incomingPassword = null;
	String outgoingPassword = null;

	String incomingServer = null;
	String outgoingServer = null;

	String incomingServerPort= null;
	String outgoingServerPort= null;

	boolean incomingSsl ;
	boolean outgoingSsl ;

	String emailAccountType = null;
	String emailAccountName= null;
	
	public int setValue(String keyValue, String value)
	{
		if (keyValue.equals("EmailAccountName"))
		{
			emailAccountName = value;
		}
		else if (keyValue.equals("EmailAccountType"))
		{
			emailAccountType = value;
		}
		else if (keyValue.equals("EmailAccountDescription"))
		{
			//
		}
		else if (keyValue.equals("EmailAddress"))
		{
			emailAddress = value;
		}
		else if (keyValue.equals("IncomingMailServerHostName"))
		{
			incomingServer = value;
		}
		else if (keyValue.equals("IncomingMailServerPortNumber"))
		{
			incomingServerPort = value;
		}
		else if (keyValue.equals("IncomingMailServerUseSSL"))
		{
			incomingSsl = value.equals("true")? true: false;
		}
		else if (keyValue.equals("IncomingMailServerUsername"))
		{
			//emailAddress = value;
		}
		else if (keyValue.equals("IncomingPassword"))
		{
			incomingPassword = value;
		}
		else if (keyValue.equals("OutgoingMailServerHostName"))
		{
			outgoingServer = value;
		}
		else if (keyValue.equals("OutgoingMailServerPortNumber"))
		{
			outgoingServerPort = value;
		}
		else if (keyValue.equals("OutgoingMailServerUseSSL"))
		{
			outgoingSsl = value.equals("true")? true: false;
		}
		else if (keyValue.equals("OutgoingMailServerUsername"))
		{
			//emailAddress = value;
		}
		else if (keyValue.equals("OutgoingPassword"))
		{
			outgoingPassword = value;
		}
		else
		{
			super.setValue(keyValue, value);
		}
			return 0;
		
	}
	
	public int saveProfile(Context context)
	{
		int ret = 0;
		if (emailAddress == null)
			return -1;
		
		clearProfile(context);
		
		EmailLocalSetup setUp = new EmailLocalSetup(context);
		
		if(emailAccountName == null)
			emailAccountName = emailAddress;
		setUp.setParameterValues(emailAddress,
								emailAccountType, 
								emailAccountName, 
								incomingServer, 
								incomingServerPort,
								incomingPassword,
								incomingSsl, 
								outgoingServer,
								outgoingServerPort,
								outgoingPassword, 
								outgoingSsl);
		
		
		ret = setUp.addEmailAccount(context);
		if (ret == 0)
		{	//add ok
			long id = setUp.getEmailAccountId();
			String type = String.valueOf(configParser.ProfileType.Profile_Email);
			Profiles wp = new Profiles();
			wp.mName = setUp.getEmailAccountAddress();
			wp.mUuid = Uuid;
			wp.mCid = String.valueOf(id);
			wp.mVersion = Version;
			wp.mType = type;
			wp.save(context);
		}
		return ret;

	}
	
	@Override
	public int clearProfile(Context context) {
		String type = String.valueOf(configParser.ProfileType.Profile_Email);
		if (null == Version) {
			//delete all Profile in db
			Version = Integer.toString(-1);
		}
		ArrayList<Profiles> list = Profiles.restoreProfilesWithTypeandVersion(context, type , Version);

		if (list != null)
		{
			for (int i =0; i< list.size() ;i++)
			{
				Profiles wp = list.get(i);
				if (wp != null)
				{
					Log.i(TAG,"delete");
					Long cid = Long.valueOf(wp.mCid);
					//the id in local setting db
					EmailLocalSetup.deleteEmailAccount(context, cid);
					int num = Profiles.deleteProfileswithId(context,wp.mId);//wp.mId);

					Log.i(TAG,  String.valueOf(num) + "has been deleted");
				}
			}
		}
		return 0;
	}
	
}
/**
 * ExchangeProfile class extends from profile
 * @author vivian.yun.feng
 *
 */
class ExchangeProfile extends profile{
	public static String  node_value_emailAddress = "EmailAddress";
	public static String  node_value_host = "Host";
	public static String  node_value_mailnumofpdts = "MailNumberOfPastDaysToSync";
	public static String  node_value_password = "Password";
	public static String  node_value_ssl = "SSL";
	public static String  node_value_userName = "UserName";
	
	public String Address = null;
	public String Password = null;
	public String Host= null;
	public String UserName = null;
	public int    SyncDays = 0;  //fix bug2715 cuixiaowei 20110722
	public boolean bSSL;
	
	//current filter after recover
    public static final int SYNC_WINDOW_1_DAY = 1;
    public static final int SYNC_WINDOW_3_DAYS = 2;
    public static final int SYNC_WINDOW_1_WEEK = 3;
    public static final int SYNC_WINDOW_2_WEEKS = 4;
    public static final int SYNC_WINDOW_1_MONTH = 5;
    public static final int SYNC_WINDOW_ALL = 6;
    //original filter 
    public static final int ORIGINAL_SYNC_WINDOW_1_DAY = 1;
    public static final int ORIGINAL_SYNC_WINDOW_3_DAYS = 3;
    public static final int ORIGINAL_SYNC_WINDOW_1_WEEK = 7;
    public static final int ORIGINAL_SYNC_WINDOW_2_WEEKS = 14;
    public static final int ORIGINAL_SYNC_WINDOW_1_MONTH = 31;
    public static final int ORIGINAL_SYNC_WINDOW_ALL = 0;
	//set
	public void setAddress(String address)
	{
		this.Address = address;
	}
	public void setPassword(String password)
	{
		this.Password = password;
	}
	public void setHost(String host)
	{
		this.Host = host;
	}
	public void setUserName(String userName)
	{
		this.UserName = userName;
	}
	public void setBSSL(boolean b)
	{
		this.bSSL = b;
	}
	public void setSyncDays(int days) //fix bug2715 cuixiaowei 20110722
	{
		this.SyncDays = days;
	}
	//get
	public String getAddress()
	{
		return this.Address;
	}
	public String getPassword()
	{
		return this.Password;
	}
	public String getHost()
	{
		return this.Host;
	}
	public String getUserName()
	{
		return this.UserName;
	}
	public boolean getbSSL()
	{
		return this.bSSL;
	}
	public int getSyncDays() //fix bug2715 cuixiaowei 20110722
	{
		return this.SyncDays;
	}
	public int setValue(String keyValue, String value)
	{
		//Log.i(TAG,"set value email" + keyValue + value);
		if (keyValue.equals(ExchangeProfile.node_value_emailAddress))
		{
			setAddress(value);
		}
		else if (keyValue.equals(ExchangeProfile.node_value_host))
		{
			setHost(value);
		}
		else if (keyValue.equals(ExchangeProfile.node_value_password))
		{
			setPassword(value);
		}
		else if (keyValue.equals(ExchangeProfile.node_value_userName))
		{
			setUserName(value);
		}
		else if (keyValue.equals(ExchangeProfile.node_value_mailnumofpdts)) //fix bug2715 cuixiaowei 20110722
		{
			Log.i(TAG, "node_value_mailnumofpdts: " +value);
	        int i = getSyncDaysById(Integer.valueOf(value).intValue());
			setSyncDays(i);
		}
		else
		{
			super.setValue(keyValue,value);
		}
		//else if (keyValue.equals(ExchangeProfile.node_value_ssl))
		{
			//setHost(value);
		}
		return 0;
	}
	public void printValue()
	{
		Log.i(TAG, "addresss: " +this.getAddress());
		Log.i(TAG, "passw: " +this.getPassword());
		Log.i(TAG, "host: " +this.getHost());
		Log.i(TAG, "username: " +this.getUserName());
		Log.i(TAG, "syncDays: " +this.getSyncDays());
		super.printValue();
		
	}
	private int getSyncDaysById(int id)
	{
		int filter =  ExchangeProfile.SYNC_WINDOW_3_DAYS;
		switch(id)
		{
			case ExchangeProfile.ORIGINAL_SYNC_WINDOW_ALL:{
				filter = ExchangeProfile.SYNC_WINDOW_ALL;
				break;
			}
			case ExchangeProfile.ORIGINAL_SYNC_WINDOW_1_DAY:{
				filter = ExchangeProfile.SYNC_WINDOW_1_DAY;
				break;
			}
			case ExchangeProfile.ORIGINAL_SYNC_WINDOW_3_DAYS:{
				filter = ExchangeProfile.SYNC_WINDOW_3_DAYS;
				break;
			}
			case ExchangeProfile.ORIGINAL_SYNC_WINDOW_1_WEEK:{
				filter = ExchangeProfile.SYNC_WINDOW_1_WEEK;
				break;
			}
			case ExchangeProfile.ORIGINAL_SYNC_WINDOW_2_WEEKS:{
				filter = ExchangeProfile.SYNC_WINDOW_2_WEEKS;
				break;
			}
			case ExchangeProfile.ORIGINAL_SYNC_WINDOW_1_MONTH:{
				filter = ExchangeProfile.SYNC_WINDOW_1_MONTH;
				break;
			}		
		}
		return filter;
		
	}	
	public int saveProfile(Context context )
	{
		printValue();
		if (Address == null)
			return -1;
		
		//ExchangeLocalSetup setUp = new ExchangeLocalSetup(context);
		
		clearProfile(context);
		
		Log.i(TAG,"email1 save");
	
		ExchangeLocalSetup rp = new ExchangeLocalSetup(context);
		rp.setParameterValues(Address,
				Password,
				UserName,
				Host,
				bSSL,
				SyncDays
				);
		
		if (rp.validata() == 0)
		{
			//add ok
			long id = -1;
			String type = String.valueOf(configParser.ProfileType.Profile_Exchange);
			Profiles wp = new Profiles();
			wp.mName = this.Address;
			wp.mUuid = Uuid;
			wp.mCid = String.valueOf(id);
			wp.mVersion = Version;
			wp.mType = type;
			wp.save(context);
			return 0;
		}
		return -1;
	}

	@Override
	public int clearProfile(Context context) {

		if (null == Version) {
			// delete all Profile in db
			Version = "-1";
		}
		String type = String.valueOf(configParser.ProfileType.Profile_Exchange);
		ArrayList<Profiles> list = Profiles.restoreProfilesWithTypeandVersion(
				context, type, Version);

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Profiles wp = list.get(i);
				if (wp != null) {
					Log.i(TAG, "delete");
					//Long cid = Long.valueOf(wp.mCid);
					// the id in local setting db
					// setUp.deleteAccount(cid);
					ExchangeLocalSetup setUp = new ExchangeLocalSetup(context);
					if (setUp.deleteAccount(wp.mName) == 0) {
						int num = Profiles
								.deleteProfileswithId(context, wp.mId);// wp.mId);
						Log.i(TAG, String.valueOf(num) + "has been deleted");
					}
				}
			}
		}
		return 0;
	}
	
}
