package com.android.log;

import android.util.Log;

import com.android.ring.Constant;

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
		if(Constant.IS_DEBUG){
			Log.d(tag, info);
		}
	}
	public static void print(String tag,String info){
		if(Constant.IS_DEBUG){
			Log.v(tag, info);
		}
	}

}
