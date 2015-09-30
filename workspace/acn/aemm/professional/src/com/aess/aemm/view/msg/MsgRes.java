package com.aess.aemm.view.msg;

import java.util.HashMap;

public class MsgRes {
	public HashMap<String, String> userMap = new HashMap<String, String>();
	public HashMap<String, String> typeMap = new HashMap<String, String>();
	public HashMap<String, String> busTypeMap = new HashMap<String, String>();
	public String CommandId;
	
	public static class MsgInfo {
		public String mTitile;
		public String mContent;
		public String mTypeKey;
		public String mTypeValue;
		public String mBegin;
		public String mEnd;
		public String mOrg;
		public String mPublishDate;
		public int mEventLevel = -1;
		public int mPlanState = -1;
		public boolean mIsValid = false;
		public String mValidNum;
		public boolean mIsRevTime = false;
		public String mValidBegin;
		public String mValidEnd;
		public HashMap<String, String> mAcceptUser = new HashMap<String, String>();
		public String mAgreeUserKey;
		public String mAgreeUserName;
		public String mBusType;
		public String mBusName;
		public String mSentType;
		public String mStartURI;
		public String mCommandId;

	}
};


