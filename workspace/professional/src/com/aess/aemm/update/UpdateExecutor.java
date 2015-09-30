package com.aess.aemm.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.aess.aemm.R;
import com.aess.aemm.protocol.UpdateResult;
import com.aess.aemm.view.InfoMainView;

public class UpdateExecutor extends AsyncTask<Object,String,UpdateResult>{
	public final static String TAG=UpdateExecutor.class.getName();
	public final static byte GETUSER=0;
	public final static byte POSTUSER=1;
	public final static byte POSPWD=2;
	private Context mContext;
	private byte type;
	private Handler mHandler;
	ProgressDialog progressDialog;
	
	public UpdateExecutor(Context mContext,byte type,Handler mHandler){
		this.mContext=mContext;
		this.type=type;
		this.mHandler=mHandler;	
	}
	
	public UpdateExecutor(Context mContext,byte type){
		this(mContext, type, null);
	}

	@Override
	protected UpdateResult doInBackground(Object... arg0) {
		Log.d(TAG,"doInBackground-"+type);
		switch(type){
		case GETUSER:
		case POSTUSER:
		case POSPWD:
			return	Update.sendUser(mContext);
		}
		return null;
	}



	@Override
	protected void onPostExecute(UpdateResult result) {
		Log.d(TAG,"onPostExecute-"+type);
		switch(type){
		case GETUSER:
			InfoMainView.start(mContext,InfoMainView.USER_INFOMATION);;
			break;
		case POSTUSER:
		case POSPWD:
			Message msg=mHandler.obtainMessage(type);
			msg.obj=result;
			msg.sendToTarget();
			break;
		}
		progressDialog.dismiss();
		super.onPostExecute(result);
	}



	@Override
	protected void onPreExecute() {
		Log.d(TAG,"onPreExecute-"+type);
		progressDialog = new ProgressDialog(mContext);
		   progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		   progressDialog.setMessage(mContext.getString(R.string.downloading));
		   progressDialog.show();

		// TODO Auto-generated method stub
		super.onPreExecute();
	}

}
