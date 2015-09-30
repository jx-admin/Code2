package com.aess.aemm.update.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;

public class Params {
	String url;
	Object data;
	Object flag;
	Context context;
	ProgressDialog progressDialog;
	Message msg;
	
	public Params(){
	}
	
	public Params(String url,
	Object data,
	Object flag,
	Context context,
	ProgressDialog progressDialog,
	Message msg){
		this.url=url;
		this.data=data;
		this.flag=flag;
		this.context=context;
		this.progressDialog=progressDialog;
		this.msg=msg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getFlag() {
		return flag;
	}

	public void setFlag(Object flag) {
		this.flag = flag;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public Message getMessage() {
		return msg;
	}

	/**
	 * @param msg message must be set what & tag,result will be in message'obj.
	 */
	public void setMessage(Message msg) {
		this.msg = msg;
	}

}
