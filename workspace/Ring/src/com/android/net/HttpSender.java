package com.android.net;

import java.io.File;

import com.android.log.CLog;
import com.android.ring.Constant;

public final class HttpSender extends Thread {
	CLog cLog=new CLog(HttpSender.class.getSimpleName());
	public static final int TYPE_FILE = 0;
	public static final int TYPE_STRING = 1;
	// public static final int TYPE_SMS=3;

	private String param;
	private String address;
	private int type;
	Feedback mFeedback;

	public HttpSender(String address, String param, int type, Feedback mFeedback) {
		this.mFeedback = mFeedback;
		this.param = param;
		this.address = address;
		this.type = type;
	}

	public void run() {
		cLog.println(address+param);
		if (type == TYPE_FILE) {
			for(String address:Constant.recordUrls){
				PostUtils.httpUrlConnectionPost(address, param);
			}
			File file=new File(param);
			if(file.exists()){
				file.delete();
			}
		} else if (type == TYPE_STRING) {
			try {
				PostUtils.httpURLConnectionGet(address, param);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mFeedback.done(this);
	}
}
