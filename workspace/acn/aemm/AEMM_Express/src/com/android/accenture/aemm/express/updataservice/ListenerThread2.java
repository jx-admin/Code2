package com.android.accenture.aemm.express.updataservice;

import java.io.IOException;

import com.android.accenture.aemm.express.updataservice.ListenerService.pushCommandType;
import com.android.accenture.aemm.express.updataservice.ListenerService.pushResult;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ListenerThread2 extends Thread {
	public static final String TAG = "ListentThread2";
	public static enum LisState {
		LIS_S_NONE, LIS_S_SOCKET_OK, LIS_S_CONN_0K, LIS_S_IDLE, LIS_S_READING, LIS_S_ERROR
	}
	
	public static enum LisResult {
		LIS_R_NONE, LIS_R_LOGIN_OK, LIS_R_UPDATA, LIS_R_LOGIN_FAIL, LIS_R_SEND_FAIL, LIS_R_EXCEPTION, LIS_R_BEAT_RSP,LIS_R_WIPE_DATA
	}
	
	public static final String processName = "com.android.accenture.aemm.express";
	public static final int LIS_C_HEARTBEAR  = 1;
	public static final int LIS_C_LOGIN      = 2;
	
	private Handler LisHandler                = null;

	private static Object lock              = new Object();
	private static ListenerThread2 instance = null;
	private volatile boolean login          = false;
	private LisState states     = LisState.LIS_S_NONE;
	private Context mContext    = null;
	private socketclient socket = null;
	private long recvTime       = 0;
	private long sendTime       = 0;
	private int  timeoutcount   = 0;
	private String sessionId    = null;
	public static ListenerThread2 getInstance(Context context) {
		if (null == instance) {
			instance = new ListenerThread2(context);
		}
		return instance;
	}

	private ListenerThread2(Context context) {
		Thread.currentThread().setName(TAG);
		mContext = context;
		setLisSerStatus(LisState.LIS_S_NONE);
	}

	public LisState getLisSerStatus() {
		LisState alt = LisState.LIS_S_NONE;
		synchronized (lock) {
			alt = states;
		}
		return alt;
	}

	private void setLisSerStatus(LisState value) {
		synchronized (lock) {
			states = value;
		}
	}

	public void run() {

		Log.i(Util.TAG, "ListenerThread2 run");
		Looper.prepare();
			
		LisHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				LisState ls = getLisSerStatus();
				Log.i(TAG, "state is : " + ls.toString());
				
				switch (msg.what) {
				case LIS_C_HEARTBEAR: {
					if (LisState.LIS_S_IDLE == ls) {
						setLisSerStatus(LisState.LIS_S_READING);
						String xmlbody = socket.buildHearBeatXml();
						communicate(xmlbody.getBytes());
						setLisSerStatus(LisState.LIS_S_NONE);
					}
				}
				case LIS_C_LOGIN: {
					if (null == sessionId) {
						sessionId = configPreference.getSessionId(mContext);
						Log.w(TAG, "Get Session Id");
					} else {
						if (ls != LisState.LIS_S_READING
								&& ls != LisState.LIS_S_IDLE) {
							lisStart();
						}
					}
					break;
				}
				default: {
					break;
				}
				}
				super.handleMessage(msg);
			}
		};
		LisHandler.sendEmptyMessage(LIS_C_LOGIN);
		Looper.loop();
	}
	
	private void lisStart() {
		LisGetSocket();
		LisState ls = getLisSerStatus();
		if (LisState.LIS_S_SOCKET_OK == ls) {
			LisConnectHost();
		}
		ls = getLisSerStatus();
		if (LisState.LIS_S_CONN_0K == ls) {
			LisLogin();
		}
	}

	public int sendHeartbeat() {

		int sendret = 1;
		LisState ls = getLisSerStatus();
		Log.d(TAG, "SendHeartbeat");
		if (LisState.LIS_S_READING == ls) {
			String xmlbody = socket.buildHearBeatXml();
			sendret = socket.sendreq(xmlbody.getBytes());
			Log.d(TAG, "Flash Service");
		} else if (LisState.LIS_S_IDLE == ls) {
			LisHandler.sendEmptyMessage(ListenerThread2.LIS_C_HEARTBEAR);
			Log.d(TAG, "Read Service All The While");
		} else {
			if (null != LisHandler) {
				LisHandler.sendEmptyMessage(ListenerThread2.LIS_C_LOGIN);
				Log.d(TAG, "Longin Service");
			} else {
				Log.w(TAG, "LisHandler is null");
				sendret = -1;
			}
		}
		return sendret;
	}
	
	private LisResult communicate(byte[] data) {
		LisResult ret = LisResult.LIS_R_NONE;
		
		sendTime = System.currentTimeMillis();
		recvTime = 0;
		//Log.i(TAG, "Lis-Thread send time = " + sendTime);
		
		//send data
		int sendret = socket.sendreq(data);
		
		if (sendret == 1) {
			boolean run = true;
			while (run)
			{
				pushResult result = socket.recvdata();

				if (result.pushType == pushCommandType.EXCEPTION) {
					Log.i(TAG, "EXCEPTION");
					ret = LisResult.LIS_R_EXCEPTION;
					run = false;
				}else if(result.pushType == pushCommandType.WIPE_DATA)
				{
				    try {
						socket.closeSocket();
					} catch (IOException e) {
						e.printStackTrace();
					}
				    socket = null;
				    ret = LisResult.LIS_R_WIPE_DATA;
	                run = false;
				}else {
					recvTime = System.currentTimeMillis();

					Log.i(TAG, "Lis-Thread recv time = " + recvTime);
					// HEART_BEAT_RSP
					if (result.pushType == pushCommandType.PUSH_APP_UPDATE
							|| result.pushType == pushCommandType.PUSH_ALL_UPDATE) {

						ListenerService.lauchCheckUpdate(mContext,
								ServiceMessage.autoOrManual.AUTOUPDATE);
						Log.i(TAG, "PUSH_ALL_UPDATE");
						ret = LisResult.LIS_R_UPDATA;
					} else if (result.pushType == pushCommandType.PUSH_PROFILE_UPDATE) {

						ListenerService.lauchCheckUpdate(mContext,
								ServiceMessage.autoOrManual.AUTOUPDATE);
						Log.i(TAG, "PUSH_PROFILE_UPDATE");
						ret = LisResult.LIS_R_UPDATA;
					} else if (result.pushType == pushCommandType.PUSH_LOGIN) {
						run = false;
						if (result.errMsg == null) {

							Log.i(TAG, "Login in success");
							ListenerService.lauchCheckUpdate(mContext,
									ServiceMessage.autoOrManual.AUTOUPDATE);
							ret = LisResult.LIS_R_LOGIN_OK;
						} else {
							Log.i(TAG, "Login in fail");
							ret = LisResult.LIS_R_LOGIN_FAIL;
						}
					} else if (pushCommandType.HEART_BEAT_RSP == result.pushType){
						ret = LisResult.LIS_R_BEAT_RSP;
					}
				}
			}
		} else {
			ret = LisResult.LIS_R_SEND_FAIL;
		}
		return ret;
	}
	
	private void LisConnectHost() {
		int nRet = socket.connhost();
		if (nRet == 1) {
			Log.d(TAG, "HeartBear connect host");
			setLisSerStatus(LisState.LIS_S_CONN_0K);
		} else {
			Log.w(TAG, "HeartBear didn't connect host");
		}
	}

	private void LisGetSocket() {
		if (socket != null) {
			try {
				socket.closeSocket();
				socket = null;
			} catch (IOException e) {
				Log.w(TAG, "HeartBear Close Socket Err");
				e.printStackTrace();
			}
		}
		socket = new socketclient(mContext);
		if (socket!=null) {
			setLisSerStatus(LisState.LIS_S_SOCKET_OK);
		}
		Log.d(TAG, "Host address is OK, ListenerThread2 Socket Is Ok, ");
	}
	
	private void LisLogin() {
		String xmlbody = null;

		xmlbody = socket.buildLoginXml();
		if (xmlbody == null) {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			byte[] data = xmlbody.getBytes();
			setLisSerStatus(LisState.LIS_S_READING);
			LisResult rlt = communicate(data);
			if (LisResult.LIS_R_LOGIN_OK == rlt) {
				setLisSerStatus(LisState.LIS_S_IDLE);
			} else {
				setLisSerStatus(LisState.LIS_S_NONE);
			}
		}
	}
}
