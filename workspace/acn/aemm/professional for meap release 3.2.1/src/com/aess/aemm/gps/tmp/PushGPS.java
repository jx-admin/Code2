package com.aess.aemm.gps.tmp;
//package com.aess.aemm.gps;
//
//import java.io.IOException;
//import java.io.InputStream;
//import android.content.Context;
//import android.util.Log;
//import com.aess.aemm.data.ProfileContent.GPSLocationContent;
//import com.aess.aemm.networkutils.HttpHelp;
//import com.aess.aemm.networkutils.SystemConfig;
//import com.aess.aemm.protocol.XmlBuilder;
//
//@Deprecated
//public class PushGPS implements Runnable {
//	public static final String TAG = "PushGPS";
//	public static final String DEBUGFILENAME = "GPS.xml";
//	
//	public PushGPS(Context context) {
//		if (null != context) {
//			mContext = context;
//		}
//	}
//
//	@Override
//	public void run() {
//		if (null != mContext) {
//			Log.d(TAG, "Report GPS");
//			SystemConfig net = SystemConfig.getInstance(mContext);
//			String url = net.getUpdateURL();
//			if (null != url) {
//				boolean hasUpdated = false;
//				String locationChangedReqXml = XmlBuilder.buildInfo(mContext, XmlBuilder.LOCATION);
//				hasUpdated = postLocation(mContext, url, locationChangedReqXml);
//				if (hasUpdated) {
//					GPSLocationContent.deleteAllGPSLocationBeRead(mContext);
//				}
//			}
//		}
//	}
//	
//	private boolean postLocation(Context cxt, String uri, String data) {
//		InputStream fis = HttpHelp.aemmHttpPost(mContext, uri, data, DEBUGFILENAME);
//		if (null != fis) {
//			try {
//				fis.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return true;
//		}
//		return false;
//	}
//
//	private Context mContext = null;
//}
