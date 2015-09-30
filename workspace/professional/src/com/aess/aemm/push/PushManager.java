package com.aess.aemm.push;

import com.aess.aemm.networkutils.SocketClient;
import com.aess.aemm.protocol.PushXmlParser;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class PushManager {
	public final static String TAG = "PushManager";
	
	// enum
	public static enum PushState {
		PSH_NONE, PSH_SOCKET_INIT, PSH_SOCKET_OK, PSH_LOGIN_OK, PSH_LINK_OK, PSH_ERR
	}
	
	public PushManager(Context arg, Handler handler) {
		if (null != arg) {
			mContext = arg;
		}
		if (null != handler) {
			mHandler = handler;
		}
	}
	
	public int work() {
		int rlt = 0;
		if (true == enpush) {
			PushState tState = getPushState();
			if (PushState.PSH_LOGIN_OK == tState) {
				linkServer();
			} else if (PushState.PSH_LINK_OK == tState) {
				sendHeart();
			} else if (PushState.PSH_NONE == tState
					|| PushState.PSH_ERR == tState) {
				loginServer();
			} else {
				dog++;
				if (dog > 20) {
					setPushState(PushState.PSH_ERR);
					dog = 0;
				}
			}

			rlt = 1;
		}
		return rlt;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public SocketClient getSocket(String host, int port) {
		if (null == host) {
			return null;
		}
		if (null == aemmSocket) {
			aemmSocket =  new SocketClient(mContext, host, port);
			if (aemmSocket.isInitOk() == false) {
				aemmSocket = null;
			}
		}
		return aemmSocket;
	}
	
	public void delSocket() {
		if (null != aemmSocket) {
			aemmSocket.close();
		}
		aemmSocket = null;
		setPushState(PushState.PSH_NONE);
	}
	
	public void setPushState(PushState value) {
		synchronized (slock) {
			state = value;
		}
	}
	
	public PushState getPushState() {
		PushState alt = PushState.PSH_NONE;
		synchronized (slock) {
			alt = state;
		}
		return alt;
	}
	
	public void setPushDisable() {
		enpush = false;
	}
	
	private int sendHeart() {
		int rlt = 0;
		if (null != mContext) {
//			Log.i(TAG, "Send Heart");
			String xmlbody = new PushXmlParser(mContext).buildHeartbeatXml();
			rlt = aemmSocket.sendReq(xmlbody.getBytes());
			if (rlt < 1) {
				setPushState(PushManager.PushState.PSH_ERR);
			}
		}
		return rlt;
	}
	
	public void sendMessageToLink() {
		if (null != mHandler) {
			PushState tpstate = getPushState();
			if (PushState.PSH_LOGIN_OK == tpstate) {
				Log.d(TAG, "Send Message To Link");
				mHandler.sendEmptyMessage(PushService.FLASH_LINK);
			}
		}
	}
	
	public void sendMessageToUpdate() {
		if (null != mHandler) {
			mHandler.sendEmptyMessage(PushService.AUTO_UPDATE);
		}
	}
	
	private int loginServer() {
		int rlt = 0;
		if (null != mContext) {
			mLogin = new PushLogin(this);
			Thread lthread = new Thread(mLogin);
			lthread.setName("PushLogin");
			lthread.start();
		}
		return rlt;
	}
	
	private int linkServer() {
		int rlt = 0;
		if (null != mContext) {
			mLink = new PushLink(this);
			Thread lthread = new Thread(mLink);
			lthread.setName("PushLink");
			lthread.start();
		}
		return rlt;
	}
	
	
	
	private PushLink mLink = null;
	private PushLogin mLogin = null;
	private Context mContext = null;
	private SocketClient aemmSocket = null;
	private Handler mHandler = null;
	private volatile boolean enpush = true;
	
	private static Object slock = new Object();
	private PushState state = PushState.PSH_NONE;
	private int dog = 0;
}
