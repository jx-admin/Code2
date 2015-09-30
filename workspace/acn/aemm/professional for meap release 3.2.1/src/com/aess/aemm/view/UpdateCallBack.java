package com.aess.aemm.view;

import android.os.Message;
import android.util.Log;
import com.aess.aemm.view.data.Appdb;


public class UpdateCallBack implements com.aess.aemm.appmanager.ApkInstallService.UpdateCallBack {


	@Override
	public void updateFinished(Appdb app, int error) {
		if(app==null){
			return;
		}
		//if(true) return;
		//Log.d("load",app.getApkName()+" error="+error);
		if(MainView.mHall!=null){
			Message message = new Message();  
            message.what =MainView.DOWNLOAD_FINISH;
            message.obj=app;
            message.arg1=error;
			MainView.mHall.handler.sendMessage(message);
		}
	}

	@Override
	public void updateProgress(Appdb app, int totalBytes,
			int receivedBytes) {
		if(app==null){
			return;
		}
		//if(true) return;
		//Log.d("load",app.getApkName()+" size="+totalBytes+" receive="+receivedBytes);
		if(MainView.mHall!=null){
			Message message = new Message();  
            message.what =MainView.PROGRESSBAR_UPDATE;
            message.obj=app;
            message.arg1=totalBytes;
            message.arg2=receivedBytes;
			MainView.mHall.handler.sendMessage(message);
		}else{
			Log.d("load","mhall is null");
		}
	}
}
