package com.android.log;

import com.accenture.mbank.util.LogManager;

import android.util.Log;

public class CLog {
	private String tag="";
	
	public CLog(){}
	
	public CLog(String tag){
		this.tag=tag;
	}

	public void setTag(String tag){
		this.tag=tag;
	}
	public void println(String info) {
		LogManager.v( info);
//		mes += "\n" + info;
//		tv.setText(mes);
	}

}
