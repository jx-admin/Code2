package com.aess.aemm.update.net;

import java.io.InputStream;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.aess.aemm.networkutils.HttpHelp;

public class PostThread  extends Thread{
	Context mContext;
	String url;
	String data;
	Handler handler;
	int type;
	
	public PostThread(Context context,String url,String data,Handler handler,int type){
		this.mContext=context;
		this.url=url;
		this.data=data;
		this.handler=handler;
		this.type=type;
	}
	public void run(){
		InputStream upResult = HttpHelp.aemmHttpPost(mContext, url, data,"sharing.txt");
		Message msg=handler.obtainMessage(type, upResult);
		handler.sendMessage(msg);
	}
	
}
