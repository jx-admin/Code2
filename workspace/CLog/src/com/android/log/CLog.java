package com.android.log;

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
		Log.d(tag, info);
//		mes += "\n" + info;
//		tv.setText(mes);
	}
	public static void print(String tag,String info){
		Log.v(tag, info);
	}

}
