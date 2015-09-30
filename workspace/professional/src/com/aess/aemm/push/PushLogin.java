package com.aess.aemm.push;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import android.content.Context;
import android.util.Log;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.networkutils.SocketClient;
import com.aess.aemm.protocol.PushXmlParser;
import com.aess.aemm.protocol.PushXmlParser.PushResult;

public class PushLogin implements Runnable {
	public final static String TAG = "PushLogin";
	public final static String INFO = "Redirect";

	public PushLogin(PushManager magarg) {
		if (null != magarg) {
			mag = magarg;
		}
	}

	@Override
	public void run() {
		Log.i(TAG, "Login Service");
		if (null != mag) {
			mag.delSocket();
			
			mContext = mag.getContext();
			
			if(!NetUtils.isNetOK(mContext)){
				Log.i(TAG, "Net Isn't OK");
				return;
			}
			
			String id = CommUtils.getSessionId(mContext);
			if(null == id || id.length() < 1){
				Log.i(TAG, "SessionID == null");
				return;
			}
			
			AutoAdress ad = AutoAdress.getInstance(mag.getContext());
			if (ad.init() <= 0) {
				mag.setPushState(PushManager.PushState.PSH_ERR);
				return;
			}
			
			pushSocket = mag.getSocket(ad.getAddress(), ad.getConnectPort());
			
			if (null == pushSocket || null == mContext) {
				mag.setPushState(PushManager.PushState.PSH_ERR);
				Log.w(TAG, "PushLogin Context Or Socket Is Null");
				return;
			}
			
			mag.setPushState(PushManager.PushState.PSH_SOCKET_INIT);
			
			Log.i(TAG, "Get Socket Ok");
			if (pushSocket.connHost() > 0) {
				
				mag.setPushState(PushManager.PushState.PSH_SOCKET_OK);
				Log.i(TAG, "Connect Host Ok");
				if (sendLogin() > 0) {
					mag.setPushState(PushManager.PushState.PSH_LOGIN_OK);
					Log.i(TAG, "Login Service OK");
					mag.sendMessageToLink();
					mag.sendMessageToUpdate();

				} else {
					Log.w(TAG, "Login Service Fail");
					mag.setPushState(PushManager.PushState.PSH_ERR);
				}
			} else {
				Log.w(TAG, "Connect Host Fail");
				mag.setPushState(PushManager.PushState.PSH_ERR);
				mag.delSocket();
				pushSocket = null;
			}
		}
	}

	private int sendLogin() {
		Log.i(TAG, "sendLogin()");

		int rlt = 0;
		if (null != pushSocket) {
			String sessionId = CommUtils.getSessionId(mContext);

			if (null != sessionId) {
				String longxml = new PushXmlParser(mContext)
						.buildLoginXml(sessionId);

				if (null != longxml && longxml.length() > 0) {
					int ret = dealWithLogin(longxml.getBytes());
					if (ret > 0) {
						rlt = 1;
					}
				}
			} else {
				Log.i(TAG, "Didn't Have Session ID");
			}
		}
		return rlt;
	}

	private int dealWithLogin(byte[] data) {
		int ret = 0;

		// send data
		int sendret = pushSocket.sendReq(data);

		if (sendret == 1) {
			String read = pushSocket.recvData();
			if (null != read && read.length() > 0 ) {
				if (null != read) {
		            Log.d(TAG, read);
					InputStream xmlcontent = new ByteArrayInputStream(read
							.getBytes());

					PushResult rlt = new PushXmlParser(mContext)
							.parsePushCommandXml(xmlcontent);
					
					if (INFO.equals(rlt.errMsg)) {
						Log.w(TAG, rlt.errMsg);
						ret = -1;
					}

					if (rlt.errMsg == null) {
						Log.i(TAG, "errMsg " + rlt.errMsg);
						ret = 1;
					}
				}
			} else {
				mag.setPushState(PushManager.PushState.PSH_ERR);
				ret = -1;
			}
		} else {
			mag.setPushState(PushManager.PushState.PSH_ERR);
			ret = -1;
			// Log.w(TAG, "mySocket.send Fail");
		}
		return ret;
	}

	private PushManager mag = null;
	private SocketClient pushSocket = null;
	private Context mContext = null;
}
