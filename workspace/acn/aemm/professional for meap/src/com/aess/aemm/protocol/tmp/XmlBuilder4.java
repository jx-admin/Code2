//package com.aess.aemm.protocol.tmp;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.aess.aemm.apkmag.ApkInfo;
//import com.aess.aemm.apkmag.ApkInfo.ApkData;
//import com.aess.aemm.commonutils.CommUtils;
//import com.aess.aemm.commonutils.Hardware;
//import com.aess.aemm.db.GPSContent;
//import com.aess.aemm.protocol.UpdateResult;
//import com.aess.aemm.view.data.User;
//
//public class XmlBuilder4 {
//	public static final String TAG = "XmlBuilder";
//
//	public static final String REG = ":";
//	public static final int LOCATION = 1;
//
//	public static final int UPDATE = 4;
//	public static final int LOGIN = 8;
//
//	public static final int APK = 2;
//	public static final int APKPSK = 16;
//	public static final int APKENT = 32;
//
//	public static final int COMMAND = 64;
//	public static final int COMMANDS = 128;
//	public static final int USER=256;
//
//	public static class UserInfo {
//		public String user;
//		public String ps;
//	}
//
//	public static class Info {
//		public static final int U = 1;
//		public static final int I = 2;
//
//		public static Info getInsByUser(String user, String ps) {
//			if (null == user || null == ps) {
//				return null;
//			}
//			Info info = new Info(U);
//			info.u.user = user;
//			info.u.ps = ps;
//			return info;
//		}
//
//		public static Info getInsByString(String data) {
//			if (null == data) {
//				return null;
//			}
//			Info info = new Info(I);
//			info.i = data;
//			return info;
//		}
//
//		private Info(int type) {
//			if (1 == type) {
//				u = new UserInfo();
//			}
//		}
//
//		public UserInfo u = null;
//		public String i = null;
//		public boolean d = false;
//	}
//
//	public static String buildInfo(Context cxt, int type, Info info) {
//		if (null == cxt) {
//			return null;
//		}
//
//		StringBuilder xmlbody = new StringBuilder();
//
//		xmlbody.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n<request>\r\n");
//
//		String auth = null;
//		if (LOGIN == type) {
//			if (null == info || null == info.u) {
//				return null;
//			}
//			if (null == info.u.user || null == info.u.ps) {
//				return null;
//			}
//
//			auth = buildAuthLogin(cxt, info.u.user, info.u.ps);
//		} else {
//			auth = buildAuthDevice(cxt);
//		}
//
//		if (null == auth) {
//			return null;
//		}
//
//		xmlbody.append(auth);
//
//		buildVersion(cxt, xmlbody);
//
//		if (LOGIN == (type & LOGIN)) {
//			buildDeviceInfo(cxt, xmlbody);
//		}
//		
//		if (COMMAND == (type & COMMAND)) {
//			if (true == info.d) {
//				buildDeviceInfo(cxt, xmlbody);
//			}
//		}
//
//		if (LOCATION == (type & LOCATION)) {
//			int x = buildLocation(cxt, xmlbody);
//			if (x < 1) {
//				Log.d(TAG, "no location need posted");
//				return null;
//			}
//		}
//
//		if (APK == (type & APK)) {
//			int x = buildAppInfo2(cxt, xmlbody, APK);
//			if (x < 1) {
//				Log.d(TAG, "no location need posted");
//				return null;
//			}
//		}
//
//		if (APKPSK == (type & APKPSK)) {
//			int x = buildAppInfo2(cxt, xmlbody, APKPSK);
//			if (x < 1) {
//				Log.d(TAG, "no apk was installed by person");
//				return null;
//			}
//		}
//
//		if (APKENT == (type & APKENT)) {
//			int x = buildAppInfo2(cxt, xmlbody, APKENT);
//			if (x < 1) {
//				Log.d(TAG, "no apk was installed by person");
//				return null;
//			}
//		}
//
//		if (COMMAND == (type & COMMAND)) {
//			String cm = info.i;
//			if (null == cm) {
//				Log.d(TAG, "no apk was installed by person");
//				return null;
//			}
//			xmlbody.append("<commands>\r\n");
//			xmlbody.append(cm);
//			xmlbody.append("</commands>\r\n");
//		}
//
//
//		User.buildUserInfo(cxt, xmlbody);
//
//		xmlbody.append("</request>");
//		return xmlbody.toString();
//	}
//
//	public static String getCommandResult(UpdateResult r) {
//		StringBuilder sb = new StringBuilder();
//		String tmp = null;
//		if (null == r) {
//			return null;
//		}
//		if (null != r.mWipeId) {
//			r.mWipeId = r.mWipeId+":1";
//		}
//		tmp = getCommandString(r.mWipeId);
//		if (null != tmp) {
//			sb.append(tmp);
//		}
//
//		tmp = getCommandString(r.mLockId);
//		if (null != tmp) {
//			sb.append(tmp);
//		}
//
//		tmp = getCommandString(r.mCleanId);
//		if (null != tmp) {
//			sb.append(tmp);
//		}
//
//		tmp = getCommandString(r.mAppListId);
//		if (null != tmp) {
//			sb.append(tmp);
//		}
//
//		tmp = getCommandString(r.mBlackListId);
//		if (null != tmp) {
//			sb.append(tmp);
//		}
//
//		tmp = getCommandString(r.mProfileId);
//		if (null != tmp) {
//			sb.append(tmp);
//		}
//		
//		tmp = getCommandString(r.mDeviceId);
//		if (null != tmp) {
//			sb.append(tmp);
//		}
//
//		for (int x = 0; x < r.mRemoveList.size(); x++) {
//			tmp = getCommandString(r.mRemoveList.get(x).mCommandId);
//			if (null != tmp) {
//				sb.append(tmp);
//			}
//		}
//		
//		for (int x = 0; x < r.mMessagesList.size(); x++) {
//			tmp = getCommandString(r.mMessagesList.get(x).mCommandId);
//			if (null != tmp) {
//				sb.append(tmp);
//			}
//		}
//
//		return sb.toString();
//	}
//
//	public static String getCommandString(String info) {
//		String tmp = null;
//		if (null == info) {
//			return tmp;
//		}
//		String arr[] = info.split(REG);
//		if (null == arr) {
//			return tmp;
//		}
//
//		if (arr.length > 1) {
//			if (null == arr[0] || arr[0].length() < 1) {
//				return tmp;
//			}
//
//			tmp = String.format("<command id=\"%s\" result=\"%s\" />\r\n",
//					arr[0], arr[1]);
//		} else {
//			if (null == arr[0] || arr[0].length() < 1) {
//				return tmp;
//			}
//
//			tmp = String.format("<command id=\"%s\" result=\"%s\" />\r\n",
//					arr[0], "2");
//		}
//		return tmp;
//	}
//	
//	public static String[] getCommandValue(String info) {
//		if (null == info) {
//			return null;
//		}
//		String arr[] = info.split(REG);
//		if (null == arr || arr.length < 1) {
//			return null;
//		}
//		
//		if (null == arr[0]) {
//			return null;
//		}
//		
//		if (arr.length < 2) {
//			String tt[] = new String[2];
//			tt[0] = arr[0];
//			tt[1] = "2";
//			return tt;
//		}
//		
//		int x = Integer.parseInt(arr[1]);
//		if (2 != x && 1 != x) {
//			arr[1] = "2";
//		}
//
//		return arr;
//	}
//
//	public static int buildDeviceInfo(Context cxt, StringBuilder xmlbody) {
//		xmlbody.append("<hardware>\r\n");
//		Hardware hware = new Hardware();
//		hware.init(cxt);
//		Map<String, String> infoMap = hware.getHardwareInfo();
//
//		Set<Entry<String, String>> infoSet = infoMap.entrySet();
//		Iterator<Entry<String, String>> iter = infoSet.iterator();
//		while (iter.hasNext()) {
//			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
//					.next();
//			Object key = entry.getKey();
//			Object value = entry.getValue();
//			String info = String.format(
//					"<info name=\"%s\" value=\"%s\" />\r\n", key, value);
//			xmlbody.append(info);
//		}
//		xmlbody.append("</hardware>\r\n");
//		return 1;
//	}
//	
//	public static Element buildDeviceInfo(Document document) {
////		xmlbody.append("<hardware>\r\n");
//		Element hardware = document.createElement("hardware");
//		Hardware hware = new Hardware();
//		hware.init(_cxt);
//		Map<String, String> infoMap = hware.getHardwareInfo();
//
//		Set<Entry<String, String>> infoSet = infoMap.entrySet();
//		Iterator<Entry<String, String>> iter = infoSet.iterator();
//		while (iter.hasNext()) {
//			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
//					.next();
//			Object key = entry.getKey();
//			Object value = entry.getValue();
//			Element info = document.createElement("info");
//			info.setAttribute("name", key.toString());
//			info.setAttribute("value", value.toString());
//			hardware.appendChild(info);
////			String info = String.format(
////					"<info name=\"%s\" value=\"%s\" />\r\n", key, value);
////			xmlbody.append(info);
//		}
////		xmlbody.append("</hardware>\r\n");
//		return hardware;
//	}
//	
//	private static int buildVersion(Context cxt, StringBuilder xmlbody) {
//		xmlbody.append("<versions>\r\n");
//		String ver = String.format("<app-list version=\"%s\" />\r\n"
//				+ "<black-list version=\"%s\" />\r\n"
//				+ "<client name=\"%s\" version=\"%s\" />\r\n"
//				+ "<profile version=\"%s\" />\r\n"
//				+ "<vpn-profile version=\"%s\" />\r\n",
//				CommUtils.getAppListVersion(cxt),
//				CommUtils.getBlackListVersion(cxt),
//				CommUtils.getCurrentClientName(cxt),
//				CommUtils.getCurrentClientVersion(cxt),
//				CommUtils.getProfileVersion(cxt),
//				CommUtils.getVpnProfileVersion(cxt));
//		xmlbody.append(ver);
//		xmlbody.append("</versions>\r\n");
//		return 1;
//	}
//	
//	private static int buildLocation(Context cxt, StringBuilder xmlbody) {
//
//		ArrayList<GPSContent> gpslist = GPSContent
//				.queryAllLocationContents(cxt);
//
//		if (null == gpslist || gpslist.size() < 1) {
//			return 0;
//		}
//
//		xmlbody.append("<locations>\r\n");
//
//		for (GPSContent gpsinfo : gpslist) {
//			if (null == gpsinfo.mGPSLongitude || null == gpsinfo.mGPSLatitude) {
//				continue;
//			}
//			if (gpsinfo.mGPSLongitude.length() < 1
//					|| gpsinfo.mGPSLatitude.length() < 1) {
//				continue;
//			}
//			String location = String
//					.format("<location longitude=\"%s\" latitude=\"%s\" time=\"%s\" />\r\n",
//							gpsinfo.mGPSLongitude, gpsinfo.mGPSLatitude,
//							gpsinfo.mGPSTime);
//			xmlbody.append(location);
//		}
//		xmlbody.append("</locations>\r\n");
//		return 1;
//	}
//	
//	private static int buildAppInfo2(Context cxt, StringBuilder xmlbody, int type) {
//		ApkInfo apk = new ApkInfo(cxt);
//
//		ArrayList<ApkData> apkList = apk.getApkDataList(cxt, type);
//
//		String head = null;
//
//		String headf = "<apps type=\"%s\" totalflow=\"%s\" otherflow=\"%s\" >\r\n";
//		if (APKENT == (type & APKENT)) {
//			head = "<apps type=\"1\" >\r\n";
//		} else if (APKPSK == (type & APKPSK)) {
//			head = "<apps type=\"2\" >\r\n";
//		} else if (APK == (type & APK)) {
//			head = String.format(headf, "3", String.valueOf(apk.getTrafficTotle()), String.valueOf(apk.getTrafficOther()));
//		} else {
//			head = "<apps type=\"1\" >\r\n";
//		}
//
//		xmlbody.append(head);
//
//		for (int x = 0; x < apkList.size(); x++) {
//			ApkData data = apkList.get(x);
//			if (APKENT == (type & APKENT)) {
//				if (APKENT != data.type) {
//					continue;
//				}
//			}
//			if (APKPSK == (type & APKPSK)) {
//				if (APKPSK != data.type) {
//					continue;
//				}
//			}
//			String info = data.toString();
//			xmlbody.append(info);
//		}
//
//		String end = "</apps>\r\n";
//		xmlbody.append(end);
//		return 1;
//	}
//
//	private static String buildAuthDevice(Context cxt) {
//		String sessionId = CommUtils.getSessionId(cxt);
//		String deviceId = CommUtils.getDeviceId(cxt);
//
//		if (null == sessionId || null == deviceId) {
//			return null;
//		}
//
//		String auth = String
//				.format("<auth>\r\n<session device-id=\"%s\" value=\"%s\" />\r\n</auth>\r\n",
//						deviceId, sessionId);
//		return auth;
//	}
//	
//	private static String buildAuthLogin(Context cxt, String user, String ps) {
//		String deviceId = CommUtils.getDeviceId(cxt);
//
//		if (null == user || null == deviceId) {
//			return null;
//		}
//
//		String auth = String
//				.format("<auth>\r\n<login account=\"%s\" password=\"%s\" device-id=\"%s\" os=\"android\" />\r\n</auth>\r\n",
//						user, ps, deviceId);
//		return auth;
//	}
//	
//	static Context _cxt;
//}
