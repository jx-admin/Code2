package com.aess.aemm.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.aess.aemm.db.ApkContent;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.protocol.UpdateXmlParser.NotifyResult;
import com.aess.aemm.protocol.UpdateXmlParser.ProfileResult;
import com.aess.aemm.protocol.UpdateXmlParser.RemoveResult;
import com.aess.aemm.view.data.User;

public class UpdateResult {
	public int mErrorMsg = 0;
	public static final int RESPONSE_FAIL=-1;
	public static final int SUCCESSFULL=200;

	// client update
	public String mClientName = null;
	public String mClientVersion = null;
	public String mClientUpdateUrl = null;
	public boolean mClientForceUpdate = false;

	public int mCheckCycle = 0;
	public int mLocationCycle = 0;
	public String mLocationRange = null;
	public int mAppCycle = 0;
	public int mSilentInstall = -1;
	public Long mTraffic = (long) -1;
	public String mSessionId = null;

	public ArrayList<ProfileResult> mProfileList = new ArrayList<ProfileResult>();
	public ArrayList<ApkContent> mAppList = new ArrayList<ApkContent>();
	public Map<String, String> mAppAccount = new HashMap<String, String>();
	public ArrayList<ApkContent> mBlackList = new ArrayList<ApkContent>();
	public ArrayList<RemoveResult> mRemoveList = new ArrayList<RemoveResult>();
	public ArrayList<NewsContent> mMessagesList = new ArrayList<NewsContent>();
	public ArrayList<NotifyResult> mNotifyList = new ArrayList<NotifyResult>();
	
	public boolean mWipeDevice       = false;
	public String mWipeId            = null;
	
	public boolean mLockDevice       = false;
	public String mLockId            = null;
	public String mLockPassword      = null;
	
	public boolean mCleanLock        = false;
	public String mCleanId           = null;
	
	public boolean mDevice           = false;
	public String mDeviceId           = null;

	public String mAppListVersion    = null;
	public String mAppListId         = null;
	
	public String mBlackListVersion  = null;
	public String mBlackListId       = null;
	
	public String mProfileVersion    = null;
	public String mProfileId         = null;
	
	public String mVpnProfileVersion = null;
	
	public String mStartAppPName     = null;
	public String mStartAppId        = null;
	
	public boolean mRestart          = false;
	public String mRestartId         = null;
	
	public User mUser;
//	public String mUserName,mUserMail,mUserIM,mUserAddress;
	
}
