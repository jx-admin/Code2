package com.aess.aemm.push;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.SocketClient;
import com.aess.aemm.protocol.PushXmlParser;
import com.aess.aemm.protocol.PushXmlParser.PushResult;
import com.aess.aemm.update.Update;
import com.aess.aemm.view.NotificationUtils;

public class PushLink implements Runnable {
	public final static String TAG = "PushLink";

	public PushLink(PushManager magarg) {
		if (null != magarg) {
			mag = magarg;
		}
	}

	@Override
	public void run() {
		int ret = 0;

		AutoAdress ad = AutoAdress.getInstance(mag.getContext());

		if (null != mag) {
			pushSocket = mag.getSocket(ad.getAddress(), ad.getConnectPort());
			mContext = mag.getContext();
			if (null != mContext && null != pushSocket) {
				String xmlbody = new PushXmlParser(mContext)
						.buildHeartbeatXml();
				if (null != xmlbody) {
					ret = linkWithService(xmlbody.getBytes());
				}
			} else {
				Log.w(TAG, "PushLogin Context Or Socket Is Null");
			}
		}

		Log.w(TAG, "Push Link Err");
		if (ret <= 1) {
			mag.setPushState(PushManager.PushState.PSH_ERR);
		}
	}

	private int linkWithService(byte[] data) {
		Log.i(TAG, "linkWithService()");
		int ret = 0;

		int sendret = pushSocket.sendReq(data);

		if (sendret == 1) {
			mag.setPushState(PushManager.PushState.PSH_LINK_OK);
			Log.i(TAG, "Push Link OK");
			boolean run = true;
			while (run) {
				String read = null;

				read = pushSocket.recvData();
				if (null != read) {
					if (read.length() > 0) {

						Log.d(TAG, read);

						InputStream xmlcontent = new ByteArrayInputStream(
								read.getBytes());

						// BufferedReader reader = new BufferedReader(new
						// InputStreamReader(xmlcontent));
						// String line = null;
						// StringBuffer sb = new StringBuffer();
						// try {
						// while ((line = reader.readLine()) != null) {
						// sb.append(line);
						// }
						// } catch (IOException e) {
						//
						// e.printStackTrace();
						// }
						// xmlcontent = new
						// ByteArrayInputStream(sb.toString().getBytes());

						PushResult rlt = new PushXmlParser(mContext)
								.parsePushCommandXml(xmlcontent);
						
						if(rlt.pushType==256){
							NotificationUtils.sentNotificationForSharing(mContext);
						}
						if ((PushXmlParser.NotifyException & rlt.pushType) != 0) {
							mag.delSocket();
							pushSocket = null;

							run = false;
							ret = 1;
							Log.i(TAG, "Push Link Exception");
						} else {
							if (rlt.pushType > 0
									&& rlt.pushType < PushXmlParser.NotifyException) {
								Log.i(TAG, "Auto Update");
								Update.startUpdate(mContext, Update.AUTO);
							}

//							if ((PushXmlParser.NotifyConfigUpate & rlt.pushType) != 0
//									|| (PushXmlParser.NotifyAppListUpate & rlt.pushType) != 0
//									|| (PushXmlParser.NotifyBlackListUpate & rlt.pushType) != 0
//									|| (PushXmlParser.NotifyHallUpate & rlt.pushType) != 0
//									|| (PushXmlParser.NotifyOtherAction & rlt.pushType) != 0
//									|| (PushXmlParser.NotifySession & rlt.pushType) != 0
//									|| (PushXmlParser.NotifyWipeDevice & rlt.pushType) != 0
//									|| (PushXmlParser.NotifyDeviceInfo & rlt.pushType) != 0) {
//
//							}
						}
					}
				} else {
					run = false;
					mag.setPushState(PushManager.PushState.PSH_ERR);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			ret = -1;
			Log.w(TAG, "pushSocket send Fail");
		}

		return ret;
	}

	private PushManager mag = null;
	private SocketClient pushSocket = null;
	private Context mContext = null;
}
