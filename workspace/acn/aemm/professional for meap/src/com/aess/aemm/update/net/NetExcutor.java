package com.aess.aemm.update.net;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.aess.aemm.R;
import com.aess.aemm.networkutils.HttpHelp;

public class NetExcutor extends AsyncTask<Params, Progress, Result> {
	private static final String TAG=NetExcutor.class.getName();
	private static final String LOGFILE=NetExcutor.class.getName()+".txt";
	public static final int NET_REPONSE_NULL=-1;

	ProgressDialog progressDialog;
	private Message msg;
	Context mContext;
	
	public NetExcutor(Context c,ProgressDialog progressDialog){
		mContext=c;
		this.progressDialog=progressDialog;
	}
		
	@Override
	protected Result doInBackground(Params... arg0) {
		msg=arg0[0].getMessage();
		   
		Result result=new Result();
		result.setFlag(arg0[0].getFlag());
		InputStream is=null;
		Log.d(TAG, "http(s) post-> "+arg0[0].getUrl()+" <- "+(String)arg0[0].getData());
		if(arg0[0].getData()==null){
			try {
				HttpURLConnection httpConn = HttpHelp.getHttpConnection(arg0[0].getUrl());
				if (HttpURLConnection.HTTP_OK == httpConn.getResponseCode()) {
					is = httpConn.getInputStream();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(arg0[0].getData() instanceof String){
				is = HttpHelp.aemmHttpPost(arg0[0].getContext(), arg0[0].getUrl(), (String)arg0[0].getData(),LOGFILE);
			}
		if (null == is) {
			result.setResultCode(NET_REPONSE_NULL);
			Log.d(TAG, "respons is null. <-"+arg0[0].getUrl());
//			Log.d(TAG, "respons data size:"+is.available()+" <-"+arg0[0].getUrl());
		}
		result.setData(is);
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Result result) {
		if(msg!=null){
			msg.obj=result;
			msg.sendToTarget();
		}
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		Log.d(TAG,"onPreExecute-");
		super.onPreExecute();
		if(progressDialog==null){
			progressDialog = new ProgressDialog(mContext);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(mContext.getString(R.string.downloading));
		}
		progressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
	}
}
