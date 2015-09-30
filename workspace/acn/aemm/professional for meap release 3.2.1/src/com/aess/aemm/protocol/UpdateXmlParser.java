package com.aess.aemm.protocol;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

import com.aess.aemm.AEMMConfig;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.networkutils.NetUtils;
import com.aess.aemm.view.data.Appdb;
import com.aess.aemm.view.data.User;
import com.aess.aemm.view.msg.Attachment;
import com.aess.aemm.view.msg.MessageType;
import com.aess.aemm.view.msg.MsgRes;
import com.aess.aemm.view.sharing.Sharing;

/*
 * Parse update xml
 **/
public class UpdateXmlParser {
	Context mContext;

	public static final String TAG = "UpdateXmlParser";

	public class ProfileResult {
		public String mConfigId = null;
		public String mConfigVer = null;
		public String mConfigUrl = null;
	}

	public class RemoveResult {
		public String mAppId = null;
		public String mAppVer = null;
		public String mPackageName = null;
		public String mCommandId = null;
	}

	public static class NotifyResult {
		public String AppId = null;
		public Map<String, String> args = null;
		public String mCommandId = null;
	}

	public static UpdateResult getUpdateResultByErr() {
		UpdateResult result = new UpdateResult();
		result.mErrorMsg = NetUtils.SERVICE_ERROR;
		return result;
	}

	public UpdateXmlParser(Context context) {
		 mContext = context;
	}

	public UpdateResult parseUpdateXml(InputStream inStream, int type) {
		Log.i(TAG, "parseUpdateXml");

		UpdateResult result = new UpdateResult();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();

			parseAuth(result, root);
			parseConfig(result, root);
			if (1 == type) {
				parseProfile(result, root);
				parseVpnProfile(result, root);
				parseClient(result, root);
				parseApp(result, root);
				parseBlackList(result, root);
				parseMessagesList(result, root);
				User.parseUserInfo(result, root);
				parseActions(result, root);
			}

			inStream.close();
		} catch (Exception e) {
			Log.e(TAG,e.getMessage());
			result = getUpdateResultByErr();
		}

		return result;
	}

	public UpdateResult parseXml(Context mContext,InputStream inStream, Object data,int type) {
		Log.i(TAG, "parseUpdateXml");

		UpdateResult result = new UpdateResult();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();

			parseAuth(result, root);
			parseConfig(result, root);
			switch(type){
			case DomXmlBuilder.SHARINGSREQUEST:
				Sharing.parseInfo(root);
				break;
			case DomXmlBuilder.SHARINGDOWNLOADREQUEST:
				Sharing mSharing=(Sharing)data;
				Sharing.parseDownLoadInfo(root, mSharing);
				break;
			case DomXmlBuilder.USER:
				User.parseUserInfo(result, root);
				break;
			}
			inStream.close();
		} catch (Exception e) {
			if (result.mErrorMsg ==0) {
				result.mErrorMsg = NetUtils.F_UNKNOW;
			}
			e.printStackTrace();
		}

		return result;
	}
	public static int parseUpdateXmlForError(InputStream inStream) {
		Log.i(TAG, "parseUpdateXmlForError");
		int x = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();

			x = parseError(root);

		} catch (Exception e) {
			e.printStackTrace();
			x = NetUtils.SERVICE_ERROR;
		}

		return x;
	}
	

	public static int parseResult(InputStream inStream){
		Log.i(TAG, "parseUpdateXmlForError");
		int x = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();

			NodeList nl = root.getElementsByTagName("command");
			if (nl != null && nl.getLength() > 0) {
				Element sub_nl = (Element) nl.item(0);
				if (sub_nl != null) {
					String result = sub_nl.getAttribute("result");
						Log.i(TAG, " result is " + result);
						x=Integer.parseInt(result);
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			x = NetUtils.SERVICE_ERROR;
		}

		return x;
	}

	public static int parseUpdateXmlForMsg(InputStream inStream, MsgRes res) {
		Log.i(TAG, "parseUpdateXmlForMsg");
		int x = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();

			x = parseError(root);
			if (0 != x) {
				return x;
			}
			parseMessagesRes(res, root);

		} catch (Exception e) {
			e.printStackTrace();
			x = NetUtils.SERVICE_ERROR;
		}

		return x;
	}

	private static int parseError(Element root) {
		// error
		int rlt = -1;
		NodeList nl = root.getElementsByTagName("auth");
		if (nl != null && nl.getLength() > 0) {
			NodeList sub_nl = ((Element) nl.item(0))
					.getElementsByTagName("error");
			if (sub_nl != null && sub_nl.getLength() > 0) {
				String errorMsg = ((Element) sub_nl.item(0))
						.getAttribute("number");
				rlt = Integer.valueOf(errorMsg);
			} else {
				rlt = 0;
			}
			Log.i(TAG, " Error is " + rlt);
		}
		return rlt;
	}

	private void parseAuth(UpdateResult result, Element root) {
		// authenticate
		NodeList nl = root.getElementsByTagName("auth");
		if (nl != null && nl.getLength() > 0) {
			NodeList sub_nl = ((Element) nl.item(0))
					.getElementsByTagName("session");
			if (sub_nl != null && sub_nl.getLength() > 0) {
				String sessionId = ((Element) sub_nl.item(0))
						.getAttribute("value");
				// if(null != sessionId) {
				// CommUtils.setSessionId(mContext, sessionId);
				// }
				result.mSessionId = sessionId;
			} else {
				sub_nl = ((Element) nl.item(0)).getElementsByTagName("error");
				if (sub_nl != null && sub_nl.getLength() > 0) {
					String errorMsg = ((Element) sub_nl.item(0))
							.getAttribute("number");
					result.mErrorMsg = Integer.valueOf(errorMsg);
				} else {
					result.mErrorMsg = NetUtils.SERVICE_ERROR;
				}
				Log.i(TAG, " " + result.mErrorMsg);
			}
		}
	}

	private void parseConfig(UpdateResult result, Element root) {
		// configuration
		NodeList nl = root.getElementsByTagName("configuration");
		if (nl != null && nl.getLength() > 0) {
			NodeList sub_nl = ((Element) nl.item(0))
					.getElementsByTagName("check");
			if (sub_nl != null && sub_nl.getLength() > 0) {
				String checkCycle = ((Element) sub_nl.item(0))
						.getAttribute("cycle");
				if (null != checkCycle && checkCycle.length() > 1) {
					result.mCheckCycle = Integer.valueOf(checkCycle);
				}
			}

			sub_nl = ((Element) nl.item(0)).getElementsByTagName("location");
			if (sub_nl != null && sub_nl.getLength() > 0) {
				String locationCycle = ((Element) sub_nl.item(0))
						.getAttribute("cycle");
				if (null != locationCycle && locationCycle.length() > 1) {
					result.mLocationCycle = Integer.valueOf(locationCycle);
				}

				String locationRange = ((Element) sub_nl.item(0))
						.getAttribute("range");
				if (null != locationCycle && locationCycle.length() > 1) {
					result.mLocationRange = locationRange;
				}
			}

			sub_nl = ((Element) nl.item(0)).getElementsByTagName("app");
			if (sub_nl != null && sub_nl.getLength() > 0) {
				String appCycle = ((Element) sub_nl.item(0))
						.getAttribute("cycle");
				if (null != appCycle && appCycle.length() > 1) {
					result.mAppCycle = Integer.valueOf(appCycle);
				}

				String silentinstall = ((Element) sub_nl.item(0))
						.getAttribute("silentinstall");
				if (null != silentinstall && silentinstall.length() > 0) {
					result.mSilentInstall = Integer.parseInt(silentinstall);
				}
			}

			sub_nl = ((Element) nl.item(0)).getElementsByTagName("device");
			if (sub_nl != null && sub_nl.getLength() > 0) {
				String traffic = ((Element) sub_nl.item(0))
						.getAttribute("flowthreshold");
				if (null != traffic && traffic.length() > 0) {
					result.mTraffic = Long.valueOf(traffic);
				}
			}
		}
	}

	private void parseApp(UpdateResult result, Element root) {

		NodeList appNodes = root.getElementsByTagName("apps");
		if (appNodes == null || appNodes.getLength() == 0)
			return;

		result.mAppListVersion = ((Element) appNodes.item(0))
				.getAttribute("version");
		result.mAppListId = ((Element) appNodes.item(0))
				.getAttribute("commandid");
		appNodes = ((Element) appNodes.item(0)).getElementsByTagName("app");

		for (int i = 0; i < appNodes.getLength(); i++) {

			Element appNode = (Element) appNodes.item(i);

			ApkContent appResult = new ApkContent();
			appResult.mApkName = appNode.getAttribute("name");
			appResult.mApkId = appNode.getAttribute("id");
			appResult.mApkVersion = appNode.getAttribute("version");
			appResult.mApkDesc = appNode.getAttribute("description");
			appResult.mApkUrl = appNode.getAttribute("install-url");
			appResult.mApkFlag = "" + Appdb.NEWAPP;
			appResult.mApkPublished = 1;
			if (AEMMConfig.v31) {
				appResult.mApkType = Integer.valueOf(appNode
						.getAttribute("category"));
				appResult.mTypeName = appNode.getAttribute("categoryname");

				appResult.mSnapShot = appNode.getAttribute("snapshot-url");
			}

			NodeList appChildsNodes = appNode.getChildNodes();

			String apkColorIcon = null;
			String apkGreyIcon = null;
			for (int j = 0; j < appChildsNodes.getLength(); j++) {
				Node appChildNode = (Node) appChildsNodes.item(j);

				if (appChildNode.getNodeType() == Node.ELEMENT_NODE) {

					if ("color".equals(appChildNode.getNodeName())) {
						NodeList colorChildsNodes = appChildNode
								.getChildNodes();
						for (int k = 0; k < colorChildsNodes.getLength(); k++) {
							Node colorChildNode = (Node) colorChildsNodes
									.item(k);

							if (colorChildNode.getNodeType() == Node.CDATA_SECTION_NODE) {

								apkColorIcon = colorChildNode.getNodeValue();
								if (apkColorIcon.length() > 0) {
									appResult.mIconColor = apkColorIcon;

								}
							}
						}
					} else if ("gray".equals(appChildNode.getNodeName())) {
						NodeList grayChildsNodes = appChildNode.getChildNodes();

						for (int k = 0; k < grayChildsNodes.getLength(); k++) {
							Node grayChildNode = (Node) grayChildsNodes.item(k);

							if (grayChildNode.getNodeType() == Node.CDATA_SECTION_NODE) {
								apkGreyIcon = grayChildNode.getNodeValue();
								if (apkGreyIcon.length() > 0) {
									appResult.mIconGrey = apkGreyIcon;
								}
							}
						}
					}
				}
			}
			result.mAppList.add(appResult);
		}
	}

	private void parseProfile(UpdateResult result, Element root) {
		NodeList nl = root.getElementsByTagName("profile");
		if (nl == null || nl.getLength() == 0)
			return;
		result.mProfileVersion = ((Element) nl.item(0)).getAttribute("version");
		result.mProfileId = ((Element) nl.item(0)).getAttribute("commandid");
		for (int i = 0; i < nl.getLength(); i++) {
			Element profileNode = (Element) nl.item(i);

			ProfileResult item = new ProfileResult();
			item.mConfigUrl = profileNode.getAttribute("install-url");
			item.mConfigVer = profileNode.getAttribute("version");

			result.mProfileList.add(item);
		}
	}

	private void parseVpnProfile(UpdateResult result, Element root) {
		NodeList nl = root.getElementsByTagName("vpn-profile");
		if (nl == null || nl.getLength() == 0)
			return;
		result.mVpnProfileVersion = ((Element) nl.item(0))
				.getAttribute("version");
		for (int i = 0; i < nl.getLength(); i++) {
			Element profileNode = (Element) nl.item(i);

			ProfileResult item = new ProfileResult();
			item.mConfigUrl = profileNode.getAttribute("install-url");
			item.mConfigVer = profileNode.getAttribute("version");

			result.mProfileList.add(item);
		}
	}

	private void parseBlackList(UpdateResult result, Element root) {
		NodeList nl = root.getElementsByTagName("black-list");
		if (nl == null || nl.getLength() == 0)
			return;
		result.mBlackListVersion = ((Element) nl.item(0))
				.getAttribute("version");
		result.mBlackListId = ((Element) nl.item(0)).getAttribute("commandid");
		nl = ((Element) nl.item(0)).getElementsByTagName("app");
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);

			ApkContent item = new ApkContent();
			item.mApkId = e.getAttribute("id");
			item.mApkVersion = e.getAttribute("version");
			item.mApkDisabled = 1;

			result.mBlackList.add(item);
		}
	}

	private void parseMessagesList(UpdateResult result, Element root) {
		NodeList nl = root.getElementsByTagName(NewsContent.messages);

		if (nl == null || nl.getLength() == 0) {
			return;
		}
		NodeList ChildsNodes = nl.item(0).getChildNodes();
		for (int i = 0; i < ChildsNodes.getLength(); i++) {

			Element message = (Element) ChildsNodes.item(i);
			String id = message.getAttribute(NewsContent.commandid);
			NodeList messsageList = message.getChildNodes();
			NewsContent item = new NewsContent();
			for (int l = 0; l < messsageList.getLength(); l++) {

				Node node = messsageList.item(l);

				// String nodeName = node.getNodeName();
				short nodeType = node.getNodeType();

				if (Node.CDATA_SECTION_NODE == nodeType) {
					String jsonstring = node.getNodeValue();
					if (null != jsonstring) {
						Log.d("NEWJsonStr",jsonstring);
						readMsg(result, jsonstring, id,item);
					}
				}else if(NewsContent.attachments.equals(node.getNodeName())){
					NodeList attachmentList=node.getChildNodes();
					if(attachmentList!=null&&attachmentList.getLength()>0){
						item.mHasAttachment=attachmentList.getLength();
						for(int a=attachmentList.getLength()-1;a>=0;a--){
							Attachment attach=Attachment.xmlParser((Element) attachmentList.item(a),item.mCommandId);
							if(attach!=null){
								attach.add(mContext);
							}
						}
					}
				}
			}
		}
	}

	public static void parseMessagesRes(MsgRes res, Element root) {
		NodeList nl = root.getElementsByTagName("messages");

		if (nl == null || nl.getLength() == 0) {
			return;
		}
		NodeList ChildsNodes = nl.item(0).getChildNodes();
		for (int i = 0; i < ChildsNodes.getLength(); i++) {

			Element message = (Element) ChildsNodes.item(i);
			res.CommandId = message.getAttribute("commandid");
			NodeList messsageList = message.getChildNodes();
			
			for (int l = 0; l < messsageList.getLength(); l++) {

				Node node = messsageList.item(l);

				String nodeName = node.getNodeName();
				short nodeType;
				if ("type".equals(nodeName)) {
					NodeList typelist = node.getChildNodes();
					if (null != typelist) {
						for (int x = 0; x < typelist.getLength(); x++) {
							node = typelist.item(x);
							nodeType = node.getNodeType();
							if (Node.CDATA_SECTION_NODE == nodeType) {
								String jsonstring = node.getNodeValue();
								if (null != jsonstring) {
									readJsonString(res.typeMap, jsonstring);
								}
							}
						}
					}
				} else if ("businesstype".equals(nodeName)) {
					NodeList buslist = node.getChildNodes();
					if (null != buslist) {
						for (int x = 0; x < buslist.getLength(); x++) {
							node = buslist.item(x);
							nodeType = node.getNodeType();
							if (Node.CDATA_SECTION_NODE == nodeType) {
								String jsonstring = node.getNodeValue();
								if (null != jsonstring) {
									readJsonString(res.busTypeMap, jsonstring);
								}
							}
						}
					}
				} else if ("users".equals(nodeName)) {
					NodeList userlist = node.getChildNodes();
					if (null != userlist) {
						for (int x = 0; x < userlist.getLength(); x++) {
							node = userlist.item(x);
							nodeType = node.getNodeType();
							if (Node.CDATA_SECTION_NODE == nodeType) {
								String jsonstring = node.getNodeValue();
								if (null != jsonstring) {
									readJsonString(res.userMap, jsonstring);
								}
							}
						}
					}
				}
			}
		}
	}

	interface MsgName {
		public static final String MSG_TYPE = "type";
		public static final String MSG_TITLE = "title";
		public static final String MSG_CONTENT = "content";

		public static final String MSG_DATE = "publishtime";
		public static final String MSG_ORG = "organization";
		public static final String MSG_START_URI = "starturl";
		public static final String MSG_P_STATE = "status";
		public static final String MSG_E_LEVEL = "priority";
		public static final String MSG_MSG_BTIME = "begintime";
		public static final String MSG_MSG_ETIME = "endtime";
		public static final String MSG_TYPE_NAME = "typename";
		public static final String MSG_BUS_TYPE = "businesstype";
		public static final String MSG_BUS_TYPE_N = "businesstypename";
		public static final String MSG_VALID = "valid";
		public static final String MSG_VALID_B_TIME = "validbegtime";
		public static final String MSG_Valid_E_TIME = "validendtime";
		public static final String MSG_RECEIVER = "receiveusers";
		public static final String MSG_AGREE = "audituser";
	}

//	private void readNotity(UpdateResult result, String jsonstring,
//			NotifyResult nr) {
//		// NotifyResult item = new NotifyResult();
//		JSONTokener jsonParser = new JSONTokener(jsonstring);
//		JSONObject person = null;
//		try {
//			person = (JSONObject) jsonParser.nextValue();
//			if (null == person) {
//				return;
//			}
//
//			JSONArray keyarray = person.names();
//			Map<String, String> keymap = new HashMap<String, String>();
//			for (int i = 0; i < keyarray.length(); i++) {
//				keymap.put((String) keyarray.get(i),
//						person.getString((String) keyarray.get(i)));
//			}
//			nr.args = keymap;
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return;
//		}
//
//		result.mNotifyList.add(nr);
//	}
	
	private static void readJsonString(Map<String, String> map, String jsonstring) {
		JSONTokener jsonParser = new JSONTokener(jsonstring);
		JSONObject person = null;
		try {
			person = (JSONObject) jsonParser.nextValue();
			if (null == person) {
				return;
			}
			JSONArray keyarray = person.names();
			for (int i = 0; i < keyarray.length(); i++) {
				map.put((String) keyarray.get(i),
						person.getString((String) keyarray.get(i)));
			}
//			Iterator iterator = person.keys();
//			while(iterator.hasNext()) {
//				String key = (String) iterator.next();
//				String value = person.getString(key);
//				map.put(key, value);
//			}

		}catch (JSONException e) {
			e.printStackTrace();
			return;
		}

	}

	private void readMsg(UpdateResult result, String jsonstring,
			String commandId,NewsContent item) {
		item.mCommandId = commandId;
		JSONTokener jsonParser = new JSONTokener(jsonstring);
		JSONObject person = null;
		try {
			person = (JSONObject) jsonParser.nextValue();
			if (null == person) {
				return;
			}

			item.mType = person.getInt(MsgName.MSG_TYPE);
//			String datev = person.getString(MsgName.MSG_DATE);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
//			try {
//				date = sdf.parse(datev);
//			} catch (ParseException e) {
//				e.printStackTrace();
				date = new Date();
//			}

			item.mPData = date.getTime();
			item.mTitile = person.getString(MsgName.MSG_TITLE);
			item.mContent = person.getString(MsgName.MSG_CONTENT);
			if (AEMMConfig.v31) {
				item.mStartUri = person.getString(MsgName.MSG_START_URI);
				item.mBusType = person.getInt(MsgName.MSG_BUS_TYPE);
				item.mBusName = person.getString(MsgName.MSG_BUS_TYPE_N);
				item.mTypeName = person.getString(MsgName.MSG_TYPE_NAME);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		if (MessageType.MSG_POST == item.mType) {
			try {
				item.mPublish = person.getString(MsgName.MSG_ORG);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (MessageType.MSG_PLAN == item.mType) {
			try {
				item.mPlanState = Integer.valueOf(person
						.getString(MsgName.MSG_P_STATE));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (MessageType.MSG_EVENT == item.mType) {
			try {
				item.mLevel = Integer.valueOf(person
						.getString(MsgName.MSG_E_LEVEL));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (MessageType.MSG_PLAN == item.mType
				|| MessageType.MSG_EVENT == item.mType) {
			try {
				item.mBegin = person.getString(MsgName.MSG_MSG_BTIME);
				item.mEnd = person.getString(MsgName.MSG_MSG_ETIME);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		item.mIsRead = 0;
		item.mEventId = -1;
		result.mMessagesList.add(item);
	}

	// private void readMsg(UpdateResult result, String jsonstring) {
	// JsonReader reader = new JsonReader(new. InputStreamReader(
	// new ByteArrayInputStream(jsonstring.getBytes())));
	// NewsContent item = new NewsContent();
	//
	// try {
	// reader.beginObject();
	// while (reader.hasNext()) {
	// String name = reader.nextName();
	// if (MsgName.MSGTIT.equals(name)) {
	// item.mTitile = reader.nextString();
	// } else if (MsgName.MSGCON.equals(name)) {
	// item.mContent = reader.nextString();
	// } else if (MsgName.MSGPUB.equals(name)) {
	// item.mPublish = reader.nextString();
	// } else if (MsgName.MSGDAT.equals(name)) {
	// item.mPData = reader.nextString();
	// } else if (MsgName.MSGTYP.equals(name)) {
	// item.mType = reader.nextInt();
	// } else if (MsgName.MSGSTA.equals(name)) {
	// item.mPlanState = reader.nextString();
	// } else if (MsgName.MSGLEV.equals(name)) {
	// item.mLevel = reader.nextString();
	// } else if (MsgName.MSGBEG.equals(name)) {
	// item.mBegin = reader.nextString();
	// } else if (MsgName.MSGEND.equals(name)) {
	// item.mEnd = reader.nextString();
	// }
	// reader.endObject();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// item.mIsRead = 0;
	// result.mMessagesList.add(item);
	// }

	private void parseActions(UpdateResult result, Element root) {
		NodeList nl = root.getElementsByTagName("actions");
		if (nl == null || nl.getLength() == 0)
			return;
		root = (Element) nl.item(0);
		nl = root.getElementsByTagName("wipe");
		if (nl.getLength() > 0) {
			result.mWipeId = ((Element) nl.item(0)).getAttribute("commandid");
			result.mWipeDevice = true;
		}
		nl = root.getElementsByTagName("lock");
		if (nl.getLength() > 0) {
			result.mLockDevice = true;
			result.mLockPassword = ((Element) nl.item(0))
					.getAttribute("password");
			result.mLockId = ((Element) nl.item(0)).getAttribute("commandid");
		}
		nl = root.getElementsByTagName("clearpasscode");
		if (nl.getLength() > 0) {
			result.mCleanLock = true;
			result.mCleanId = ((Element) nl.item(0)).getAttribute("commandid");
		}
		nl = root.getElementsByTagName("deviceinfo");
		if (nl.getLength() > 0) {
			result.mDevice = true;
			result.mDeviceId = ((Element) nl.item(0)).getAttribute("commandid");
		}
		nl = root.getElementsByTagName("uninstall-app");
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);

			RemoveResult item = new RemoveResult();
			item.mAppId = e.getAttribute("id");
			item.mAppVer = e.getAttribute("version");
			item.mCommandId = e.getAttribute("commandid");

			result.mRemoveList.add(item);
		}

		nl = root.getElementsByTagName("start-app");
		if (nl.getLength() > 0) {
			String value = ((Element) nl.item(0)).getAttribute("id");
			if (null != value && value.length() > 0) {
				result.mStartAppPName = value;
			}
		}

		nl = root.getElementsByTagName("restart");
		if (nl.getLength() > 0) {
			result.mRestart = true;
		}

		nl = root.getElementsByTagName("notify-third");
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			NotifyResult nr = new NotifyResult();
			nr.AppId = e.getAttribute("appid");
			nr.mCommandId = e.getAttribute("commandid");
			nr.args = new HashMap<String, String>();
			
			NodeList messsagevalue = e.getChildNodes();
			for (int l = 0; l < messsagevalue.getLength(); l++) {
				Node value = messsagevalue.item(l);
				if (value.getNodeType() == Node.CDATA_SECTION_NODE) {
					String jsonstring = value.getNodeValue();
					if (null != jsonstring) {
						readJsonString(nr.args, jsonstring);
//						readNotity(result, jsonstring, nr);
					}
				}
			}
			result.mNotifyList.add(nr);
		}
	}

	private void parseClient(UpdateResult result, Element root) {
		NodeList hall = root.getElementsByTagName("client");
		for (int i = 0; i < hall.getLength(); i++) {
			Element hallNode = (Element) hall.item(i);
			result.mClientName = hallNode.getAttribute("name");
			result.mClientVersion = hallNode.getAttribute("version");
			result.mClientUpdateUrl = hallNode.getAttribute("install-url");
			String isForce = hallNode.getAttribute("required");
			if (isForce.equals("1")) {
				result.mClientForceUpdate = true;
			}
		}
	}

	// private Context mContext = null;

	// private void parseRemove(UpdateResult result, Element root) {
	// NodeList nl = root.getElementsByTagName("profile");
	// if(nl == null || nl.getLength() == 0)
	// return;
	// result.mProfileVersion = ((Element)nl.item(0)).getAttribute("version");
	// result.mProfileId = ((Element)nl.item(0)).getAttribute("commandid");
	// for (int i = 0; i < nl.getLength(); i++) {
	// Element profileNode = (Element) nl.item(i);
	//
	// ProfileResult item = new ProfileResult();
	// item.mConfigUrl = profileNode.getAttribute("install-url");
	// item.mConfigVer = profileNode.getAttribute("version");
	//
	// result.mProfileList.add(item);
	// }
	// }

	// @SuppressWarnings("unused")
	// private void parseCheckcycle(UpdateResult result, Element root) {
	// String checkcycle = null;
	// NodeList config = root.getElementsByTagName("configuration");
	// if(config.getLength() == 0)
	// return;
	// config = root.getElementsByTagName("check");
	// if(config.getLength() > 0) {
	// Element infoNode = (Element) config.item(0);
	//
	// checkcycle = infoNode.getAttribute("cycle");
	// SharedPreferences pp = mContext.getSharedPreferences(
	// CommUtils.PREF_NAME, 0);
	// Editor d = pp.edit();
	// d.putString(CommUtils.KEY_CONFIG_CHECKCYCLE, checkcycle);
	// d.commit();
	// result.mCheckCycle = Integer.parseInt(checkcycle);
	// }
	// }

	// private String parserLoginResult(Context context, FileInputStream
	// inStream) {
	// try {
	// DocumentBuilderFactory factory = DocumentBuilderFactory
	// .newInstance();
	// DocumentBuilder builder = factory.newDocumentBuilder();
	// Document dom = builder.parse(inStream);
	//
	// Element root = dom.getDocumentElement();
	// NodeList nodeList;
	// if(root != null) {
	// // return null;
	// nodeList = root.getElementsByTagName("auth");
	// if(nodeList != null && nodeList.getLength() > 0) {
	// NodeList subNodeList =
	// ((Element)nodeList.item(0)).getElementsByTagName("session");
	// if(subNodeList != null && subNodeList.getLength() > 0) {
	// return ((Element)subNodeList.item(0)).getAttribute("value");
	// } else {
	// subNodeList = ((Element)nodeList.item(0)).getElementsByTagName("error");
	// if(subNodeList != null && subNodeList.getLength() > 0) {
	// return ((Element)subNodeList.item(0)).getAttribute("number");
	// }
	// }
	// }
	// }
	// } catch (FileNotFoundException e1) {
	// } catch (Exception e) {
	// Log.v(TAG,e.getMessage());
	// }
	// return null;
	// }

	// private String buildUpdateBody(int updateType, String sessionId) {
	// sessionId = CommUtils.getSessionId(mContext);
	// String deviceId = mContext.getSharedPreferences(CommUtils.PREF_NAME,
	// 0).getString(CommUtils.KEY_CONFIG_IMEI, "");
	//
	// StringBuilder xmlbody = new StringBuilder();
	// xmlbody.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
	//
	// xmlbody.append("<request>\r\n");
	// // authenticate information
	// xmlbody.append("<auth>\r\n");
	// xmlbody.append(String.format("<session device-id=\"%s\" value=\"%s\" />\r\n",
	// deviceId, sessionId));
	// xmlbody.append("</auth>\r\n");
	//
	// // versions
	// xmlbody.append("<versions>\r\n");
	// xmlbody.append(String.format(
	// "<app-list version=\"%s\" />\r\n" +
	// "<black-list version=\"%s\" />\r\n" +
	// "<client name=\"%s\" version=\"%s\" />\r\n" +
	// "<profile version=\"%s\" />\r\n" +
	// "<vpn-profile version=\"%s\" />\r\n",
	// CommUtils.getAppListVersion(mContext),
	// CommUtils.getBlackListVersion(mContext),
	// CommUtils.getCurrentClientName(mContext),
	// CommUtils.getCurrentClientVersion(mContext),
	// CommUtils.getProfileVersion(mContext),
	// CommUtils.getVpnProfileVersion(mContext)
	// ));
	// xmlbody.append("</versions>\r\n");
	//
	// // xmlbody.append("<apps>\r\n");
	// // AppList applist = new AppList(mContext);
	// // applist.onDemoAppList();
	// // xmlbody.append(applist.writeListXML());
	// // xmlbody.append("</apps>\r\n");
	// xmlbody.append("</request>");
	//
	// return xmlbody.toString();
	// }

	// public String buildLoginBody(String user, String password, String
	// deviceID) {
	// String deviceId = mContext.getSharedPreferences(CommUtils.PREF_NAME,
	// 0).getString(CommUtils.KEY_CONFIG_IMEI, "");
	//
	// StringBuilder xmlbody = new StringBuilder();
	// xmlbody.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
	//
	// xmlbody.append("<request>\r\n");
	// // authenticate information
	// xmlbody.append("<auth>\r\n");
	// xmlbody.append(String.format("<login account=\"%s\" password=\"%s\" device-id=\"%s\" />\r\n",
	// user, password, deviceId));
	// xmlbody.append("</auth>\r\n");
	//
	// // hardware
	// xmlbody.append("<hardware>\r\n");
	// CommUtils mosInfo = CommUtils.getInstance(mContext);
	// if (mosInfo != null) {
	// mosInfo.registerBatteryRecevie();
	// Map<String, String> infoMap = mosInfo.write();
	//
	// Set<Entry<String, String>> infoSet = infoMap.entrySet();
	// Iterator<Entry<String, String>> iter = infoSet.iterator();
	// while (iter.hasNext()) {
	// Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
	// .next();
	// Object key = entry.getKey();
	// Object value = entry.getValue();
	//
	// String info = String.format("<info name=\"%s\" value=\"%s\" />\r\n",
	// key, value);
	// xmlbody.append(info);
	// }
	// mosInfo.unRegisterBatteryRecevie();
	// }
	// xmlbody.append("</hardware>\r\n");
	// xmlbody.append("</request>");
	//
	// return xmlbody.toString();
	// }
}
