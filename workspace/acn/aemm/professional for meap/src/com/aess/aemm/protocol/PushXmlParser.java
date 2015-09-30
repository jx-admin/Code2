package com.aess.aemm.protocol;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.commonutils.HelpUtils;
import com.aess.aemm.push.PushLogin;

import android.content.Context;

public class PushXmlParser {

	public final static int NotifyConfigUpate    = 1;
	public final static int NotifyAppListUpate   = 2;
	public final static int NotifyBlackListUpate = 4;
	public final static int NotifyHallUpate      = 8;
	public final static int NotifyOtherAction    = 16;
	public final static int NotifyWipeDevice     = 32;
	public final static int NotifyDeviceInfo     = 64;

	public final static long NotifyHeartBeat      = 0x80000000;
	public final static long NotifySession        = 0x40000000;
	public final static long NotifyException      = 0x20000000;
	
	public static class PushResult {
		//public PushCommandType pushType = PushCommandType.PUSH_NONE;
		public long pushType = 0;
		public String errMsg = null;
		public String alertMsg = null;
		public String msgId = null;
	}

	public static final String TAG = "PushXmlParser";
	
	public static final String STATENODENAME = "state";
	public static final String STATENODEVALUE = "ok";
	
	public static final String HEARTBEAT = "heartbeat";
	
	public PushXmlParser(Context context) {
		mContext = context;
	}

	public PushResult parsePushCommandXml(InputStream inStream) {
//		Log.i(TAG, "parsePushCommandXml");
		
		PushResult result = new PushResult();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			// -----ready to parser the xml-------
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();

			// find info
			NodeList infoNodes = root.getElementsByTagName(HEARTBEAT);
			String name = root.getNodeName();
			if (HEARTBEAT.equals(name) || infoNodes.getLength() > 0) {
				result.pushType = NotifyHeartBeat;//PushCommandType.HEART_BEAT_RSP;
				return result;
			}

			// notify
			NodeList loginNodes = root.getElementsByTagName("auth");
			if (loginNodes.getLength() > 0) {
				result.pushType = NotifySession;//PushCommandType.PUSH_LOGIN;
				
				Element node = (Element) loginNodes.item(0);
				loginNodes = node.getElementsByTagName("session");
				if(loginNodes.getLength() > 0) {
					String session = ((Element)loginNodes.item(0)).getAttribute("value");
					HelpUtils.saveSession(mContext, session);
					//CommUtils.setSessionId(mContext, ((Element)loginNodes.item(0)).getAttribute("value"));
				}
				loginNodes = node.getElementsByTagName("error");
				if(loginNodes.getLength() > 0) {
					result.errMsg = ((Element)loginNodes.item(0)).getAttribute("number");
					//Log.i(TAG, String.format("%s : %s", info, result.errMsg));
				}
				
				loginNodes = node.getElementsByTagName("redirect");
				if(loginNodes.getLength() > 0) {
					String ip = ((Element)loginNodes.item(0)).getAttribute("ip");
					String port = ((Element)loginNodes.item(0)).getAttribute("port");
					CommUtils.updateLinkIP(mContext, ip, port);
					result.errMsg = PushLogin.INFO;
				}
				return result;
			}

			// find notify
			Element notifyNode = null;
			int command = 0;
			NodeList notifyNodes = root.getElementsByTagName("notify");
			command = Integer.valueOf(root.getAttribute("type"));
			if (command < 1 && notifyNodes.getLength() >= 1) {
				notifyNode = (Element) notifyNodes.item(0);
				command = Integer.parseInt(notifyNode.getAttribute("type"));
			}

			result.pushType = command;
			inStream.close();
			return result;
		} catch (Exception e) {
			result.pushType = NotifyException;
			return result;
		}
	}

	public String buildHeartbeatXml() {
		// <heart>
		String xmlbody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<heartbeat />";
		return xmlbody;
	}

	public String buildLoginXml(String sessionId) {
		String deviceId = CommUtils.getDeviceId(mContext);
		//sessionId = CommUtils.getSessionId(mContext);
		StringBuilder xmlbody = new StringBuilder();
		xmlbody.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");

		xmlbody.append("<request>\r\n");
			// authenticate information
			xmlbody.append("<auth>\r\n");
				xmlbody.append(String.format("<session device-id=\"%s\" value=\"%s\" />\r\n", deviceId, sessionId));
			xmlbody.append("</auth>\r\n");
		xmlbody.append("</request>");
		return xmlbody.toString();
	}

	private Context mContext = null;
}
