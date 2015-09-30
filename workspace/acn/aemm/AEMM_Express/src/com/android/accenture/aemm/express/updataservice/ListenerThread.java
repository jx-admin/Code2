package com.android.accenture.aemm.express.updataservice;

import java.net.InetSocketAddress;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.accenture.aemm.express.R;
import com.android.accenture.aemm.express.updataservice.ListenerService.pushCommandType;
import com.android.accenture.aemm.express.updataservice.ListenerService.pushResult;

public class ListenerThread extends Thread {

	private Context mContext = null;
	private Looper mLooper;
	private boolean isRun = true;
	public static final int heartBeatTimeout = 20000;
	public static final String processName = "com.android.accenture.aemm.express";
	// Bug #2750 shxn
	public static enum LisState {
		LIS_NONE,
		LIS_INIT_OK,
		LIS_CON_0K,
	}
	private static Object lock  = new Object();

	private LisState states = LisState.LIS_NONE;
	// Bug #2750 shxn
	public ListenerThread(Context context) {
		mContext = context;
		setLisSerStatus(LisState.LIS_NONE);
		// Bug #2750 shxn delete 2
	}

	public void ListenerStart() {
		
		Log.i(Util.TAG, Util.TAG + ": " + "Thread start");
		start();
	}
	// Bug #2750 shxn
	public LisState getLisSerStatus() {
		LisState alt = LisState.LIS_NONE;
		synchronized(lock){
		  alt = states;
		}
		return alt;
	}
	
	private void setLisSerStatus(LisState value) {
		synchronized(lock){
			states = value;
		}
	}
	// Bug #2750 shxn
	@Deprecated //shxn É¾³ýÎÞÓÃ´úÂë
	class HeartBeatThread extends Thread {
		long lastTime;

		public void run() {
			Thread.currentThread().setName("HeartBeatThread");

			while (isRun) {
				String xmlbody = sockcl.buildHearBeatXml();
				//sockcl.sendreq((byte) -2);
				sockcl.sendreq(xmlbody.getBytes());
				lastTime = System.currentTimeMillis();
				try {
					Thread.sleep(heartBeatTimeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//Log.i(Util.TAG, Util.TAG + ": " + "HeartBeatThread send cast:"
					//	+ (receveTime - lastTime));
				long receTime = getReceiveTime();
				if (receTime < lastTime) {
					Log.i(Util.TAG, Util.TAG + ": "
							+ "HeartBeatThread timeout" + receTime);
					Log.i(Util.TAG, receTime+ ": "
							+ lastTime);
					stopListenerThread();
					Message msg = ListenerService.mHandler
					.obtainMessage(ListenerService.READ_OUTTIME);
					ListenerService.mHandler.sendMessage(msg);
					break;
				}
			}
		}
	}
	
	public void checkHeartBeat() {
		LisState rlt = getLisSerStatus();
		if (rlt == LisState.LIS_NONE) {
			Log.w(Util.TAG, "-- ListenerThread Block --, Socket Address isn't ok");
		}
		else {
			int netok = 1;
			if (rlt == LisState.LIS_INIT_OK) {
				netok = sockcl.connhost();
			}
			if (netok == 1) {
				if (!mHeartBeatRequestSent) {
					// 1. send request to server
					mHeartBeatRequestSent = true;
					String xmlbody = sockcl.buildHearBeatXml();
					sockcl.sendreq(xmlbody.getBytes());
					lastTime = System.currentTimeMillis();
				} else {
					// 2. check the result from server. reset the state to step
					// 1.
					mHeartBeatRequestSent = false;
					long receTime = getReceiveTime();
					if (receTime < lastTime) {
						Log.i(Util.TAG, Util.TAG + ": "
								+ "HeartBeatThread timeout" + receTime);
						Log.i(Util.TAG, receTime + ": " + lastTime);
						stopListenerThread();
						Message msg = ListenerService.mHandler
								.obtainMessage(ListenerService.READ_OUTTIME);
						ListenerService.mHandler.sendMessage(msg);
					}
				}
			}
		}
	}

	socketclient sockcl;
	long receveTime;
	long lastTime;
	boolean mHeartBeatRequestSent = false;

	public synchronized void setReceiveTime(long ms)
	{
		receveTime = ms;
	}
	public synchronized long getReceiveTime()
	{
		return receveTime ;
	}
	
	public void run() {
		Log.i(Util.TAG, Util.TAG + ": " + "Thread run");
		// Bug #2750 shxn
		sockcl = new socketclient(mContext);
		setLisSerStatus(LisState.LIS_INIT_OK);
		// Bug #2750 shxn
		isRun = true;
		Thread.currentThread().setName("ListentThread");
		do {
			int nRet = sockcl.connhost();

			try {

				if (nRet == socketclient.CONNECT_ERROR)
				{
					////connect error
					stopListenerThread();
					//notify UI
				}

				//connect successfully
				if (nRet == 1) {
					setLisSerStatus(LisState.LIS_CON_0K);
					
					//first need to send login msg
					while(isRun)
					{
						String xmlbody = sockcl.buildLoginXml();
						if (xmlbody != null)
						{
							byte [] data = xmlbody.getBytes();
							sockcl.sendreq(data);
							break;
						}
						else
						{
							try {
								Thread.sleep(3000);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}//end while

					//new HeartBeatThread().start();

					while (isRun) {
						pushResult result = sockcl.recvdata();

						if (result.pushType != pushCommandType.EXCEPTION) 
						{
							long ms = System.currentTimeMillis();
							setReceiveTime(ms);
							Log.i(Util.TAG, "ListenerThread : push time  ="
									+ ms);
							if(result.pushType == pushCommandType.PUSH_APP_UPDATE ||
									result.pushType == pushCommandType.PUSH_ALL_UPDATE) {
//   								if (!MessageNotification.getInstance(mContext).IsForeground(processName))
//								{
//									String info = mContext.getResources().getString(R.string.SerPushInfo);
//									MessageNotification.getInstance(mContext).showNotification(info);
//								}
								//start update info
								//push can be regarded as auto update
								ListenerService.lauchCheckUpdate(mContext,ServiceMessage.autoOrManual.AUTOUPDATE);
						
							}
							else if(result.pushType == pushCommandType.PUSH_PROFILE_UPDATE)
							{
								//don't show notifications
								//start update info
								//push can be regarded as auto update
								ListenerService.lauchCheckUpdate(mContext,ServiceMessage.autoOrManual.AUTOUPDATE);
						
							}
							else if (result.pushType == pushCommandType.PUSH_LOGIN)
							{
								if (result.errMsg != null)
								{
									//login error
									Log.i("ListenThread", "Login in error and cant't get push from server");
									break;
								}
								//when login every time, update config and app. modified by cuixiaowei 20110718
								Log.i(Util.TAG, "Login in success");
								ListenerService.lauchCheckUpdate(mContext,ServiceMessage.autoOrManual.AUTOUPDATE);
							}
						} 
						else 
						{
							//pushCommandType.EXCEPTION
							Log.i("ListenThread", "EXCEPTION");
							stopListenerThread();
							break;
						}
					}//end while

				}//end if
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		} while (isRun);
		//close socket
		if (sockcl != null) {
			sockcl.close();
		}

		Message msg = ListenerService.mHandler.obtainMessage(ListenerService.NETWORK_ERROR);
		ListenerService.mHandler.sendMessage(msg);
	}
	public synchronized boolean isRuning() {
		return isRun;
	}
	public synchronized void stopListenerThread() {
		Log.i(Util.TAG, "onStop");
		isRun = false;
	}
}
