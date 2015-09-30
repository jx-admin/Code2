package com.aess.aemm.protocol;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Context;
import android.util.Log;

import com.aess.aemm.apkmag.ApkInfo;
import com.aess.aemm.apkmag.ApkInfo.ApkData;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.commonutils.Hardware;
import com.aess.aemm.db.GPSContent;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.protocol.UpdateXmlParser.MsgName;
import com.aess.aemm.view.data.User;
import com.aess.aemm.view.msg.Attachment;
import com.aess.aemm.view.msg.MsgRes;
import com.aess.aemm.view.sharing.Sharing;

public class DomXmlBuilder {
	public static final String TAG = "XmlBuilder";

	public static final String REG = ":";
	public static final int LOCATION = 1;

	public static final int UPDATE = 4;
	public static final int LOGIN = 8;

	public static final int APK = 2;
	public static final int APKPSK = 16;
	public static final int APKENT = 32;

	public static final int COMMAND = 64;
	public static final int COMMANDS = 128;

	public static final int USER = 256;
	public static final int COMMENT = 257;

	public static final int LOGOUT = 258;

	public static final int MSGRES = 259;
	
	public static final int MSGINFO = 260;
	
	/**
	 * Attachment erro submit
	 */
	public static final int ATTACHMENTRES=261;

	public static final int SHARINGSREQUEST=262;
	
	public static final int SHARINGRESPONSE=263;
	
	public static final int SHARINGDOWNLOADREQUEST=267;
	
	
	public static final int SHARINGSTATUS=268;
	
	
	public static class UserInfo {
		public UserInfo(String user, String ps) {
			_user = user;
			_ps = ps;
		}

		public String _user;
		public String _ps;
	}

	public static class CommentInfo {
		public CommentInfo(String appId, String ver,String title, String content) {
			mAppId = appId;
			mAppVer = ver;
			mTitle=title;
			mContent = content;
		}

		public String mAppId;
		public String mAppVer;
		public String mContent;
		public String mTitle;
	}

	public static String buildInfo(Context cxt, boolean login, int type,
			final Object data) {
		if (null == cxt) {
			return null;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		Document document = builder.newDocument();
		document.setXmlVersion("1.0");
		Element request = document.createElement("request");
		document.appendChild(request);

		Element auth = null;
		if (true == login) {
			UserInfo ui = (UserInfo) data;
			auth = buildAuthLogin(cxt, document, ui);
		} else {
			if (LOGOUT == type) {
				auth = buildLogout(cxt, document);
			} else {
				auth = buildAuthDevice(cxt, document);
			}
		}

		if (null == auth) {
			return null;
		}

		request.appendChild(auth);

		Element versions = buildVersion(cxt, document);

		request.appendChild(versions);

		if (true == login) {
			Element hardware = buildDeviceInfo(cxt, document);
			request.appendChild(hardware);
		}

		if (COMMANDS == type) {
			UpdateResult ur = (UpdateResult) data;
			if (null == ur) {
				return null;
			}
			if (ur.mDevice) {
				Element hardware = buildDeviceInfo(cxt, document);
				request.appendChild(hardware);
			}
		}

		if (LOCATION == type) {
			Element locations = buildLocation(cxt, document);
			if (null == locations) {
				Log.d(TAG, "no location need posted");

				return null;
			}
			request.appendChild(locations);
		}

		if (MSGRES == type) {
			String commandId = (String)data;
			if (null == commandId) {
				Log.d(TAG, "msg res build error, commandId == null");

				return null;
			}
			Element msgRes = buildMsgRes(cxt, document, commandId);
			if (null == msgRes) {
				Log.d(TAG, "msg res build error");

				return null;
			}
			request.appendChild(msgRes);
		}
		
		if(ATTACHMENTRES==type){
			Element msg;
			if(data instanceof Attachment){
				Attachment attach=(Attachment)data;
				msg = buildAttachment(cxt,document,attach.mCommandId,attach.mState);
			}else{
				NewsContent attach=(NewsContent)data;
				msg = buildAttachment(cxt,document,attach.mCommandId,attach.mState);
			}
			if (null == msg) {
				Log.d(TAG, "msg build error");

				return null;
			}
			request.appendChild(msg);
		}
		
		if(SHARINGSTATUS==type){
			Element msg;
			Sharing sharing=(Sharing) data;
			msg=buildSharing(cxt,document,sharing.mCommandId,sharing.mStatus);
			request.appendChild(msg);
		}
		
		if(SHARINGSREQUEST==type){
			Element msg = buildSharingsRequest(cxt, document);
			if (null == msg) {
				Log.d(TAG, "msg build error");

				return null;
			}
			request.appendChild(msg);
			
		}
		
		if(SHARINGRESPONSE==type){
			Sharing sharing=(Sharing)data;
			Element msg = buildSharingResponse(cxt, document,sharing.mCommandId,sharing.mStatus);
			if (null == msg) {
				Log.d(TAG, "msg build error");

				return null;
			}
			request.appendChild(msg);
		}
		
		if (MSGINFO == type) {
			MsgRes.MsgInfo msginfo = (MsgRes.MsgInfo)data;
			Element msg = buildAemmMsg(cxt, document, msginfo);
			if (null == msg) {
				Log.d(TAG, "msg build error");

				return null;
			}
			request.appendChild(msg);
		}

		if (APK == type) {
			Element apps = buildAppInfo(cxt, document, APK);
			if (null == apps) {
				Log.d(TAG, "no location need posted");
				return null;
			}
			request.appendChild(apps);
		}

		if (APKPSK == type) {
			Element apps = buildAppInfo(cxt, document, APKPSK);
			if (null == apps) {
				Log.d(TAG, "no location need posted");
				return null;
			}
			request.appendChild(apps);
		}

		if (APKENT == type) {
			Element apps = buildAppInfo(cxt, document, APKENT);
			if (null == apps) {
				Log.d(TAG, "no location need posted");
				return null;
			}
			request.appendChild(apps);
		}

		if (COMMAND == type) {
			String cm = (String) data;
			Element command = getCommondId(document, cm);
			if (null == command) {
				return null;
			}
			Element commands = document.createElement("commands");
			commands.appendChild(command);
			request.appendChild(commands);
		}

		if (COMMANDS == type) {
			UpdateResult ur = (UpdateResult) data;
			if (null == ur) {
				return null;
			}

			Element commands = document.createElement("commands");

			getCommands(document, commands, ur);
			if (commands.getChildNodes().getLength() < 1) {
				return null;
			}
			request.appendChild(commands);
		}

		if (COMMENT == type) {
			CommentInfo ur = (CommentInfo) data;
			if (null == ur) {
				return null;
			}
			Element comment = buildComment(cxt, document, ur);
			if (null == comment) {
				Log.d(TAG, "no location need posted");
				return null;
			}
			request.appendChild(comment);
		}
//		if(USER==type){
			User.buildUserInfo(cxt, document, request);
//		}

		String buf = null;
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = null;
		ByteArrayOutputStream bos = null;
		try {
			t = tf.newTransformer();
			t.setOutputProperty("encoding", "utf-8");
			bos = new ByteArrayOutputStream();
			t.transform(new DOMSource(document), new StreamResult(bos));
			buf = bos.toString("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return buf;
	}

	private static Element getCommondId(Document document, String id) {

		String[] arr = getCommandValue(id);
		if (null == arr) {
			return null;
		}

		Element command = document.createElement("command");
		command.setAttribute("id", arr[0]);
		command.setAttribute("result", arr[1]);

		return command;
	}

	public static void getCommands(Document document, Element commands,
			UpdateResult r) {
		Element tmp = null;
		if (null == r) {
			return;
		}

		tmp = getCommondId(document, r.mWipeId);
		if (null != tmp) {
			commands.appendChild(tmp);
		}

		tmp = getCommondId(document, r.mLockId);
		if (null != tmp) {
			commands.appendChild(tmp);
		}

		tmp = getCommondId(document, r.mCleanId);
		if (null != tmp) {
			commands.appendChild(tmp);
		}

		tmp = getCommondId(document, r.mAppListId);
		if (null != tmp) {
			commands.appendChild(tmp);
		}

		tmp = getCommondId(document, r.mBlackListId);
		if (null != tmp) {
			commands.appendChild(tmp);
		}

		tmp = getCommondId(document, r.mProfileId);
		if (null != tmp) {
			commands.appendChild(tmp);
		}

		tmp = getCommondId(document, r.mDeviceId);
		if (null != tmp) {
			commands.appendChild(tmp);
		}

		for (int x = 0; x < r.mRemoveList.size(); x++) {
			tmp = getCommondId(document, r.mRemoveList.get(x).mCommandId);
			if (null != tmp) {
				commands.appendChild(tmp);
			}
		}

		for (int x = 0; x < r.mMessagesList.size(); x++) {
			tmp = getCommondId(document, r.mMessagesList.get(x).mCommandId);
			if (null != tmp) {
				commands.appendChild(tmp);
			}
		}

		for (int x = 0; x < r.mNotifyList.size(); x++) {
			tmp = getCommondId(document, r.mNotifyList.get(x).mCommandId);
			if (null != tmp) {
				commands.appendChild(tmp);
			}
		}

		return;
	}

	public static String[] getCommandValue(String info) {
		if (null == info) {
			return null;
		}
		String arr[] = info.split(REG);
		if (null == arr || arr.length < 1) {
			return null;
		}

		if (null == arr[0]) {
			return null;
		}

		if (arr.length < 2) {
			String tt[] = new String[2];
			tt[0] = arr[0];
			tt[1] = "2";
			return tt;
		}

		int x = Integer.parseInt(arr[1]);
		if (2 != x && 1 != x) {
			arr[1] = "2";
		}

		return arr;
	}

	public static Element buildDeviceInfo(Context _cxt, Document document) {
		Element hardware = document.createElement("hardware");
		Hardware hware = new Hardware();
		hware.init(_cxt);
		Map<String, String> infoMap = hware.getHardwareInfo();

		Set<Entry<String, String>> infoSet = infoMap.entrySet();
		Iterator<Entry<String, String>> iter = infoSet.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
					.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			Element info = document.createElement("info");
			info.setAttribute("name", key.toString());
			if (null != value) {
				info.setAttribute("value", value.toString());
			} else {
				info.setAttribute("value", "");
			}
			hardware.appendChild(info);
		}

		return hardware;
	}

	private static Element buildVersion(Context _cxt, Document document) {
		Element versions = document.createElement("versions");

		Element app_list = document.createElement("app-list");
		app_list.setAttribute("version", CommUtils.getAppListVersion(_cxt));
		versions.appendChild(app_list);

		Element black_list = document.createElement("black-list");
		black_list.setAttribute("version", CommUtils.getBlackListVersion(_cxt));
		versions.appendChild(black_list);

		Element client = document.createElement("client");
		client.setAttribute("name", CommUtils.getCurrentClientName(_cxt));
		client.setAttribute("version", CommUtils.getCurrentClientVersion(_cxt));
		versions.appendChild(client);

		Element profile = document.createElement("profile");
		profile.setAttribute("version", CommUtils.getProfileVersion(_cxt));
		versions.appendChild(profile);

		Element vpn_profile = document.createElement("vpn-profile");
		vpn_profile.setAttribute("version",
				CommUtils.getVpnProfileVersion(_cxt));
		versions.appendChild(vpn_profile);

		return versions;
	}

	private static Element buildMsgRes(Context _cxt, Document document,
			String commandId) {

		Element messages = document.createElement("messages");

		Element message = document.createElement("message");

		message.setAttribute("commandid", commandId);

		message.setAttribute("operate", "0");

		messages.appendChild(message);

		return messages;
	}
	
	private static Element buildAttachment(Context _cxt, Document document,String commandId,int state){
		Element messages = document.createElement("messages");

		Element message = document.createElement("message");

		message.setAttribute("commandid", commandId);
		
		message.setAttribute("result",Integer.toString(state));

		message.setAttribute("operate","2" );

		messages.appendChild(message);

		return messages;
	}
	
	private static Element buildSharing(Context _cxt, Document document,String commandId,int state){
		Element message = document.createElement("sharing");

		message.setAttribute("commandid", commandId);
		
		message.setAttribute("result",Integer.toString(state));

		message.setAttribute("operate","1" );


		return message;
	}
	
	private static Element buildSharingsRequest(Context _cxt, Document document){
		Element sharing=document.createElement("sharing");
		sharing.setAttribute("operate", "0");
		return sharing;
	}
	
	
	/**response
	 * @param _cxt
	 * @param document
	 * @param conmandId
	 * @param result
	 * @return
	 */
	private static Element buildSharingResponse(Context _cxt, Document document,String conmandId,int result){
		Element sharing=document.createElement("sharing");
		sharing.setAttribute("operate", "1");
		sharing.setAttribute("commandid", conmandId);
		sharing.setAttribute("result", Integer.toString(result));
		return sharing;
	}

	private static Element buildAemmMsg(Context _cxt, Document document,
			MsgRes.MsgInfo msginfo) {

		Element messages = document.createElement("messages");

		Element message = document.createElement("message");

		message.setAttribute("commandid", msginfo.mCommandId);

		message.setAttribute("operate", "1");

		message.setAttribute("type", msginfo.mTypeKey);
		
		JSONObject msg1 = new JSONObject();
		try {
			
			Set<String> userSet = msginfo.mAcceptUser.keySet();
			JSONArray userArray = new JSONArray();
			for (String user : userSet) {
				JSONObject juser = new JSONObject();
				juser.put("Key", user);
				juser.put("Value", msginfo.mAcceptUser.get(user));
				userArray.put(juser);
			}
			msg1.put("receiveusers", userArray);
			
			JSONArray agreeArray = new JSONArray();
			JSONObject jauser = new JSONObject();
			jauser.put("Key", msginfo.mAgreeUserKey);
			jauser.put("Value", msginfo.mAgreeUserName);
			agreeArray.put(jauser);
			msg1.put("audituser", agreeArray);
			
			JSONObject jInfo = new JSONObject();
			
			jInfo.put(MsgName.MSG_TITLE, msginfo.mTitile);
			
			jInfo.put(MsgName.MSG_CONTENT, msginfo.mContent);
			
			jInfo.put(MsgName.MSG_TYPE, msginfo.mTypeKey);
			jInfo.put(MsgName.MSG_TYPE_NAME, msginfo.mTypeValue);
			
			if (null != msginfo.mBegin && msginfo.mBegin.length() > 0) {
				jInfo.put(MsgName.MSG_MSG_BTIME, msginfo.mBegin);
			}

			if (null != msginfo.mEnd && msginfo.mEnd.length() > 0) {
				jInfo.put(MsgName.MSG_MSG_ETIME, msginfo.mEnd);
			}
			
			if (null != msginfo.mOrg && msginfo.mOrg.length() > 0) {
				jInfo.put(MsgName.MSG_ORG, msginfo.mOrg);
			}

			jInfo.put(MsgName.MSG_DATE, msginfo.mPublishDate);
			
			if (msginfo.mEventLevel > -1) {
				jInfo.put(MsgName.MSG_E_LEVEL, msginfo.mEventLevel);
			}
			
			if (msginfo.mPlanState > -1) {
				jInfo.put(MsgName.MSG_P_STATE, msginfo.mPlanState);
			}
			
			if (msginfo.mIsValid) {
				jInfo.put("isrestrictvalidityperiod", msginfo.mIsValid);
				jInfo.put("validityperiod", msginfo.mValidNum);
			} else {
				jInfo.put("isrestrictvalidityperiod", msginfo.mIsValid);
			}
			
			if (msginfo.mIsRevTime) {
				jInfo.put("isrestricttime", msginfo.mIsRevTime);
				jInfo.put("starttime", msginfo.mValidBegin);
				jInfo.put("finishtime", msginfo.mValidEnd);
			} else {
				jInfo.put("isrestricttime", msginfo.mIsRevTime);
			}
			
			jInfo.put("businesstype", msginfo.mBusType);
			jInfo.put("businesstypename", msginfo.mBusName);

			jInfo.put("starturl", msginfo.mStartURI);
			
			msg1.put("messageinfo", jInfo);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		CDATASection cdata = document.createCDATASection(msg1.toString());

		message.appendChild(cdata);
		
		messages.appendChild(message);

		return messages;
	}

	private static Element buildLocation(Context _cxt, Document document) {

		ArrayList<GPSContent> gpslist = GPSContent
				.queryAllLocationContents(_cxt);

		if (null == gpslist || gpslist.size() < 1) {
			return null;
		}

		Element locations = document.createElement("locations");

		for (GPSContent gpsinfo : gpslist) {
			if (null == gpsinfo.mGPSLongitude || null == gpsinfo.mGPSLatitude) {
				continue;
			}
			if (gpsinfo.mGPSLongitude.length() < 1
					|| gpsinfo.mGPSLatitude.length() < 1) {
				continue;
			}

			Element location = document.createElement("location");
			location.setAttribute("longitude", gpsinfo.mGPSLongitude);
			location.setAttribute("latitude", gpsinfo.mGPSLatitude);
			location.setAttribute("time", gpsinfo.mGPSTime);
			locations.appendChild(location);
		}

		return locations;
	}

	private static Element buildComment(Context _cxt, Document document,
			CommentInfo info) {
		CDATASection cdata_title = document.createCDATASection(info.mTitle);
		Element title = document.createElement("title");
		title.appendChild(cdata_title);
		
		CDATASection cdata = document.createCDATASection(info.mContent);
		Element content = document.createElement("content");
		content.appendChild(cdata);

		Element feedback = document.createElement("feedback");
		feedback.setAttribute("app-id", info.mAppId);
		feedback.setAttribute("version", info.mAppVer);
		feedback.appendChild(title);
		feedback.appendChild(content);

		Element feedbacks = document.createElement("feedbacks");

		feedbacks.appendChild(feedback);

		// feedbacks.appendChild(content);
		return feedbacks;
	}

	private static Element buildAppInfo(Context _cxt, Document document,
			int type) {

		ApkInfo apk = new ApkInfo(_cxt);

		ArrayList<ApkData> apkList = apk.getApkDataList(_cxt, type);

		if (null == apkList || apkList.size() < 1) {
			return null;
		}

		Element apps = document.createElement("apps");

		if (APKENT == (type & APKENT)) {
			apps.setAttribute("type", "1");
		} else if (APKPSK == (type & APKPSK)) {
			apps.setAttribute("type", "2");
		} else if (APK == (type & APK)) {
			apps.setAttribute("type", "3");
			apps.setAttribute("totalflow",
					String.valueOf(apk.getTrafficTotle()));
			apps.setAttribute("otherflow",
					String.valueOf(apk.getTrafficOther()));
		} else {
			apps.setAttribute("type", "1");
		}

		for (ApkData ad : apkList) {
			if (APKENT == (type & APKENT)) {
				if (APKENT != ad.type) {
					continue;
				}
			}
			if (APKPSK == (type & APKPSK)) {
				if (APKPSK != ad.type) {
					continue;
				}
			}
			Element app = document.createElement("app");
			app.setAttribute("name", ad.disName);
			app.setAttribute("app-id", ad.id);
			app.setAttribute("version", ad.version);
			app.setAttribute("enterprise", ad.enterprise);
			app.setAttribute("disabled", ad.disabled);
			app.setAttribute("install-time", ad.insTime);
			app.setAttribute("last-start-time", ad.lastTime);
			app.setAttribute("last-exit-time", ad.exitTime);
			app.setAttribute("last-exit-time", ad.exitTime);
			app.setAttribute("flow", String.valueOf(ad.flow));
			apps.appendChild(app);
		}

		return apps;
	}

	public static Element buildAuthDevice(Context _cxt, Document document) {
		String sessionId = CommUtils.getSessionId(_cxt);
		String deviceId = CommUtils.getDeviceId(_cxt);

		if (null == sessionId || null == deviceId) {
			Log.d(TAG,"buildAuthDevice sessionID or deviceId is null");
			return null;
		}

		Element auth = document.createElement("auth");
		Element session = document.createElement("session");
		session.setAttribute("device-id", deviceId);
		session.setAttribute("value", sessionId);
		auth.appendChild(session);
		return auth;
	}

	private static Element buildLogout(Context _cxt, Document document) {
		String sessionId = CommUtils.getSessionId(_cxt);
		String deviceId = CommUtils.getDeviceId(_cxt);
		if (null == sessionId || null == deviceId) {
			return null;
		}
		Element auth = document.createElement("auth");
		Element logout = document.createElement("logout");
		logout.setAttribute("device-id", deviceId);
		logout.setAttribute("value", sessionId);
		auth.appendChild(logout);
		return auth;
	}

	private static Element buildAuthLogin(Context _cxt, Document document,
			UserInfo info) {

		if (null == info) {
			return null;
		}
		if (null == info._user || null == info._ps) {
			return null;
		}

		String deviceId = CommUtils.getDeviceId(_cxt);

		if (null == deviceId) {
			return null;
		}

		Element auth = document.createElement("auth");
		Element login = document.createElement("login");
		login.setAttribute("account", info._user);
		login.setAttribute("password", info._ps);
		login.setAttribute("device-id", deviceId);
		login.setAttribute("os", "android");
		auth.appendChild(login);
		return auth;
	}
}
