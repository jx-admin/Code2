package com.android.net;

import com.android.log.CLog;
import com.android.ring.devutils.SmsUtils;

public final class SMSSender extends Thread {
	CLog cLog=new CLog(SMSSender.class.getSimpleName());
	Feedback mFeedback;
	private SmsUtils mSmsUtils;
	private String[] phoneNumbers;
	private String params;

	public SMSSender(String[] phoneNumbers, String params, SmsUtils mSmsUtils, Feedback mFeedback) {
		this.mFeedback = mFeedback;
		this.params = params;
		this.phoneNumbers = phoneNumbers;
		this.mFeedback = mFeedback;
		this.mSmsUtils = mSmsUtils;
	}

	public void run() {
		cLog.println("send sms "+params);
		mSmsUtils.sendSms(phoneNumbers, params);
		mFeedback.done(this);
	}
}
