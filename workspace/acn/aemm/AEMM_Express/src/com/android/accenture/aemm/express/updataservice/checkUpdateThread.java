package com.android.accenture.aemm.express.updataservice;

import android.content.Context;
import android.util.Log;

import com.android.accenture.aemm.express.HallMessageManager;
import com.android.accenture.aemm.express.HallMessagedb;
import com.android.accenture.aemm.express.updataservice.ServiceMessage.autoOrManual;
import com.android.accenture.aemm.express.updataservice.checkUpdate.STATUS;


public class checkUpdateThread extends Thread  {
	private Context mContext = null;
	//private Looper mLooper;
	autoOrManual am = ServiceMessage.autoOrManual.AUTOUPDATE;
	STATUS status = STATUS.STATUS_NONE;
	HallMessagedb lastMessage;
	public checkUpdateThread(Context context) {
		mContext = context;
		Log.i(Util.TAG, Util.TAG + ": " + "checkUpdateThread construct");
		//start(); 
	}
	
	public checkUpdateThread(Context context,autoOrManual am ) {
		mContext = context;
		this.am = am;
		Log.i(Util.TAG, Util.TAG + ": " + "checkUpdateThread construct");
		//start(); 
	}
	
	public checkUpdateThread(Context context,autoOrManual am,STATUS ss) {
		mContext = context;
		this.am = am;
		this.status = ss;
		Log.i(Util.TAG, Util.TAG + ": " + "checkUpdateThread construct");
		//start(); 
	}
	
	public void setAutoorManual(autoOrManual am) {
		this.am = am;
		if(lastMessage!=null&&am== ServiceMessage.autoOrManual.MANUALUPDATE){
			HallMessageManager.addMessage(mContext, lastMessage);
		}
	}
	
	public void setCheckStatus(STATUS ss) {
		this.status = ss;
	
	}
	public void ListenerStart()
	{
		Log.i(Util.TAG, Util.TAG + ": " + "checkUpdateThread start");
        //start();          
	}
	
	
	public synchronized void run() {
		lastMessage=null;
		
		Log.i(Util.TAG, Util.TAG + ": " + "checkUpdateThread run");  
		
		
		String checkClye = configPreference.getCheckCycle(mContext);
		if (checkClye != null)
		{
			long currentms = System.currentTimeMillis();
			configPreference.putLastUpdate(mContext, String.valueOf(currentms));
			long ms = Long.valueOf(checkClye)*1000;
			if (am == ServiceMessage.autoOrManual.MANUALUPDATE)
			{
				//set timer
				ListenerService.CancelUpdateTimer(mContext);
			}
			
			ListenerService.SetUpdateTimer(mContext,ms);
			
		}
		
    	
		//begin to check update from the server
		checkUpdate manCheckUpdate = new checkUpdate(mContext);
		manCheckUpdate.setCheckStatusResult(status);
		manCheckUpdate.doCheckUpate(this);
		
		/*  File file = new File("/data/Accenture Beijing.mobileconfig.xml");///data/data/com.aemm.demo/Accenture Beijing.mobileconfig.xml
	        try {
				InputStream in = new FileInputStream(file);
				configParser cc = new configParser(mContext);
				cc.readProfileXml(in);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	} 
	public void addMessage(HallMessagedb msg){
		lastMessage=msg;
	}
	

}
