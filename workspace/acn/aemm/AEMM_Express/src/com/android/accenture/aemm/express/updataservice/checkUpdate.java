package com.android.accenture.aemm.express.updataservice;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.android.accenture.aemm.express.Appdb;
import com.android.accenture.aemm.express.HallMessageManager;
import com.android.accenture.aemm.express.HallMessagedb;
import com.android.accenture.aemm.express.Main;
import com.android.accenture.aemm.express.MoshinInformation;
import com.android.accenture.aemm.express.R;
import com.android.accenture.aemm.express.ServiceDia;
import com.android.accenture.aemm.express.Utils;
import com.android.accenture.aemm.express.log.Logger;
import com.android.accenture.aemm.express.log.LoggerFactory;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;
import com.android.accenture.aemm.upload.AppList;

public class checkUpdate {
	Logger log;
	public static final int NONE_UPDATE = 0;
	public static final int APP_UPDATE = 1;
	public static final int CONFIG_UPDATE = 2;
	public static final int BOTH_APP_CONFIG_UPDATE = 3;

	public static final String TAG = "checkUpdate";
	public static final String checkUpdateUrl = "http://"+socketclient.HOST+":8003/AndroidDeviceUpdate.aspx";
	public static final String UpdataAppAction = "updateApp";
	public static final String bundleKey = "apkids";
	public static final String updateTypeKey = "type";
	public static final String updateTypeValue = "apk";
	public static final int NUM = 10;
	public static final int CONNECT_TIMEOUT = 60000;
	public static final int READ_TIMEOUT = 300000;

	public static final String STATE_OK = "ok";
	public static final String STATE_ERRID = "errid";
	public static final String STATE_ERRACCOUNT = "erraccount";
	public static final String STATE_ERRDEVICE = "errdevice";
	public static final String STATE_ERRUNKNOW= "errunknow";
	public static final String STATE_ERREQUIPMENT = "errequipment";
	
//	public static final String errorLoginStr= "用户名或密码错误";
//	public static final String errorDeviceStr= "用户名或密码错误";
	
	public static enum STATUS{
		STATUS_NONE,
		STATUS_ERRACCOUNT,
		STATUS_SESSION_EXPIRED
	}

	public class configUpdateContent {
		String configId;
		String configVer;
		String configUrl;
	}

//	private autoOrManual am;
	public class appUpdateContent {
		String appName;
		String appDownloadUrl;
		String appDesc;
		String colorIconData;
		String grayIconData;
	}

	Context context = null;
	STATUS checkStatus ;
	public checkUpdate(Context context)
	{
		this.context = context;
		//result = new curCheckUpdateResult();
		checkStatus = STATUS.STATUS_NONE;
		log=LoggerFactory.getLogger(this.getClass(), null);
	}

	public void setCheckStatusResult(STATUS str)
	{
		checkStatus  = str;
	}
	public  class curCheckUpdateResult {
		public  int  mUpdateContentType;
		public  int mCheckCycle;
		public  ArrayList<configUpdateContent> configUpdateList = new ArrayList<configUpdateContent>();
		public  int configUpdateCnt;
		public  long[] appIds;
		public  int appUpdateCnt;
		public  String status;
		public  String errorMsg;
		public  String sessionId;
		public int newAppCnt;
		public int changeCnt;
	}

	//check result will be saved in this variable
	private void messageDelay(long startTime){
		startTime=minDelay-(System.currentTimeMillis()-startTime);
		if(startTime>0){
			try {
				Thread.sleep(startTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// check if there is app or configure update on the server.
	int error=0,localerro=101,none=1,confi=2,app=3,all=4;
	int minDelay=6000;
	long startTime=0;
	public Message doCheckUpate(checkUpdateThread thread) {
		boolean isConfigUpdata=false;
		Message upState=new Message();
		upState.what=error;
		// compose the http post request(xml format)
		if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
			HallMessageManager.startUpdata(context);
			startTime=System.currentTimeMillis();
		}
		String checkUpdateReqXml = "";
		InputStream updateRspXml = null;
		try {
			checkUpdateReqXml = buildCheckUpateXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (checkUpdateReqXml == null){
			if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE || 
					thread.am == ServiceMessage.autoOrManual.LOGINUPDATE ) {
				messageDelay(startTime);
				HallMessageManager.updataFinish(context, (String) context.getText(R.string.network_erro));
			}
			upState.what=localerro;
			return upState;
		}

		// post the check update request to server. xml is err!
		//ServiceLog.clean();
		updateRspXml = httpUrlConnection(checkUpdateReqXml, thread);
		if (updateRspXml == null){
			if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE || 
					thread.am == ServiceMessage.autoOrManual.LOGINUPDATE) {
				messageDelay(startTime);
				HallMessageManager.updataFinish(context, (String) context.getText(R.string.network_erro));
			}				
			upState.what=localerro;
			return upState;
		}
		// parser the update result --- xml format
		curCheckUpdateResult result = readUpdateCheckXML(updateRspXml,thread);
		if(result==null||result.status==null){
			if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE ||
					thread.am == ServiceMessage.autoOrManual.LOGINUPDATE) {
				messageDelay(startTime);
				HallMessageManager.updataFinish(context, (String) context.getText(R.string.network_erro));
			}
			upState.what=localerro;
			return upState;
		}

		if (result.status.equals(STATE_OK)) {
			// check if has sessionID
			Log.i(TAG, STATE_OK);
			if (result.sessionId != null && result.sessionId.length() > 0) {
				// save it
				// delete username and pass in pref
				Log.i(TAG, result.sessionId);
				/*
				 * SharedPreferences pp =
				 * context.getSharedPreferences(ListenerService.PREF_NAME, 0);
				 * Editor d = pp.edit(); d.putString(ListenerService.SESSIONID,
				 * result.sessionId); d.commit();
				 */
				configPreference.putSessionId(context, result.sessionId);
			}
			if (result.mUpdateContentType == NONE_UPDATE) {
				Log.i(TAG, "NONE_UPDATE");
				if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
//					HallMessageManager.sendMessage(context, (String) context.getText(R.string.updata_finish),0,10000,10000, 11);
					try {
						Thread.sleep(3000); //fix bug2784 by cuixiaowei
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
						HallMessageManager.updataFinish(context, (String) context.getText(R.string.updata_finish));
					}
				}
			} else {
				Log.i(TAG, "HAS_UPDATE");
				// if it is background show notification and don't show if
				// foreground
				if ((result.mUpdateContentType & CONFIG_UPDATE) != 0) {
					// profile update
					Log.i(TAG, "CONFIG_UPDATE");
					// SharedPreferences pp =
					// context.getSharedPreferences(ListenerService.PREF_NAME,
					// 0);
					// Editor d = pp.edit();
					for (configUpdateContent config : result.configUpdateList) {
						String url = config.configUrl;
						String id = config.configId;
						String version = config.configVer;

						if (id != null && version != null) {
							configPreference.putKeyValue(context, id, version);
						}
						// ConfigLog.clean();
						InputStream input = configUpdate
								.DownloadCfgProfile(url, getLang());
						// parser it
						configParser cp = new configParser(context);

						cp.setProfileId(id);
						String hallMessage = cp.readProfileXml(input);
						// Log.v(TAG,hallMessage);
						if (hallMessage != null) {
							// notify ui
							if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
//								HallMassagedb msg = new HallMassagedb(
//										HallMassagedb.statusCheckUpdateMsg, 0,
//										0, 0, HallMassagedb.STATUSMSG);
//								HallMessageManager.addMessage(context, msg);
//								HallMessageManager.finishUpdate(context);
//								HallMassagedb resultmsg = new HallMassagedb(
//										hallMessage, 0,
//										HallMassagedb.result_maxTime,
//										HallMassagedb.result_delayTime,
//										HallMassagedb.RESULTMSG);
//								HallMessageManager.addMessage(context,
//										resultmsg);
								Log.v(TAG, hallMessage);

								messageDelay(startTime);
								if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
									HallMessageManager.cancelMessage(context,HallMessagedb.STATUSMSG);
//								HallMessageManager.finishUpdate(context);
									HallMessageManager.sendMessage(context, hallMessage,HallMessagedb.RESULTMSG);
							    	HallMessageManager.finishUpdate(context);
									isConfigUpdata=true;
								}
							}
						} else {
						}
					}

				}
				if ((result.mUpdateContentType & APP_UPDATE) != 0) {
					// construct notify message
					int numofApps = result.appUpdateCnt;
					String msginfo = String.format((String) context.getText(R.string.updata_app_push), numofApps);
					Log.i(TAG, msginfo);
					if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE&&!isConfigUpdata) {
//						HallMessageManager.sendMessage(context, (String) context.getText(R.string.updata_finish),0,10000,10000, 11);
						if (result.newAppCnt > 0) {
							messageDelay(startTime);
							HallMessageManager.cancelMessage(context,HallMessagedb.STATUSMSG);
					    	HallMessageManager.finishUpdate(context);
						}else{
							if(!isConfigUpdata){
								messageDelay(startTime);
								if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
									HallMessageManager.updataFinish(context, (String) context.getText(R.string.updata_finish));
								}
							}
						}
					}
					// don't show app msg here
					/*
					 * HallMassagedb msgr = new HallMassagedb(msginfo,0,
					 * HallMassagedb.result_maxTime,
					 * HallMassagedb.result_delayTime, HallMassagedb.APPMSG);
					 * HallMessageManager.addMessage(context, msgr);
					 */
					Log.i(ServiceDia.LOGCAT, "APP_UPDATE ");
					Log.v("newAppCnt", "newAppCnt=" + result.newAppCnt);
					if (result.newAppCnt > 0) {
						if(!Utils.isScreenOn(context)){
							ServiceDia.showAddApp(context, context
									.getResources().getString(
											R.string.new_app_load));
						} else if (Main.mHall == null || Main.mHall.isPause) {
							if (!MessageNotification.getInstance(context).IsForeground(ListenerThread2.processName)) {
								String info = context.getResources()
											.getString(R.string.SerPushInfo);
								MessageNotification.getInstance(context)
											.showNotification(info);
							}
						}
						if(Main.mHall!=null&&thread.am != ServiceMessage.autoOrManual.MANUALUPDATE&&!isConfigUpdata){
							HallMessageManager.startUpdata(context);
							messageDelay(System.currentTimeMillis());
							HallMessageManager.clearMessage(context);
							HallMessageManager.finishUpdate(context);
						}
						HallMessageManager.newAppLoader(context,result.appIds);
						
						result.newAppCnt = 0;
					}else if(result.changeCnt>0){
						if(Main.mHall!=null)
						HallMessageManager.newAppLoader(context,result.appIds);
					}
				}
			}
		} else {
			// error
			if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
				HallMessageManager.updataFinish(context, (String) context.getText(R.string.updata_erro));
			}
			if (result.status.equals(STATE_ERRACCOUNT)
					|| result.status.equals(STATE_ERRID)
					|| result.status.equals(STATE_ERREQUIPMENT)
//					|| result.status.equals(STATE_ERRUNKNOW)
					|| result.status.equals(STATE_ERRDEVICE)) {
				Message msgr = ListenerService.mHandler
						.obtainMessage(ListenerService.ACCOUNT_ERROR);
				msgr.obj = result.errorMsg;

				ListenerService.mHandler.sendMessage(msgr);
			}
		}
		upState.what=localerro;
		return upState;
	}

	@Deprecated
	String buildLoginXMLpart(String user, String password, String imei) {
		/*
		<info>
  		<login user="user_name" passcode="xxxxxx" udid="IMEI No." />
		</info>
		 */
		String login = "<info>\r\n<login user=\"";
		//getIMei
//		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//		String imei = tm.getDeviceId();
		//serializer.startDocument("utf-8", true);
		//serializer.startTag(null, "info");
		login += user;
		login += "\" ";
		login += "passcode=\"";
		login += password;
		login += "\" udid= \"";
		login += imei;
		login += "\"";
		login += " />\r\n";
		Log.v(TAG,login);
		return login;
	}
	
	

	@SuppressWarnings("unchecked")
	private String buildCheckUpateXML() throws Exception {
		
		Log.v(TAG,"buildCheckUpateXML");
		/*  new xml file like this
		 <info sid ="xxxx-xxxxx-xxxxxx-xxxxx">
				<profile id="com.accenture.profile" version="1" />
				<profile id="com.accenture.vpn.profile" version="1" />
			<app id="com.accenture.hall" version =”1” name="应用大厅" installed="true" />
			<app id="com.accenture.someapp" version=”1” name="其他应用" installed ="false" />
		    <sysinfo>
				<applist>
				    <app id="com.xxx.xxx1" name="应用1" size="100K" datasize="100K" />
				    <app id="com.xxx.xxx2" name="应用2" size="100K" datasize="100K" />
				</applist>
				<info id="key1" enterprise="true">value1</info>
			    <info id="key2">value2</info>
			     …
		    </sysinfo>
			<location coordinate="30.000,40.000" />
		 </info>
		*/
		
		StringWriter writer = new StringWriter();
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(writer);
		StringBuilder xmlbody = new StringBuilder();
		xmlbody.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
		//xmlbody.append("<info");
		
		// get sessionID
		String sessionId = configPreference.getSessionId(context);
		if (sessionId != null) {
			xmlbody.append("<info sid = \"");
			xmlbody.append(sessionId);
			xmlbody.append("\">\r\n");
		} else {
			// get password and username
			String username = configPreference.getUser(context);
			String password = configPreference.getPass(context);
			String imei = MoshinInformation.getDeviceId(context);
			if (null == imei) {
				imei = configPreference.getImei(context);
			}
			if (username != null && password != null && imei != null) {
				
				xmlbody.append("<info>\r\n<login user=\"");
				xmlbody.append(username);
				xmlbody.append("\" ");
				
				xmlbody.append("passcode=\"");
				xmlbody.append(password);
				
				xmlbody.append("\" udid= \"");
				xmlbody.append(imei);
				
				xmlbody.append("\"");
				xmlbody.append(" />\r\n");
			} else {
				return null;
			}
		}
		
		String rootVersion = configPreference.getRootVersion(context);
		if ( rootVersion!= null)
		{
			//<profile id="com.accenture.profile" version="1" />
			xmlbody.append(String.format("<profile id=\"%s\" version=\"%s\"/>", configPreference.ROOT_INDENTIFIER,rootVersion));
			xmlbody.append("\r\n");
		}
		
		String vpnVersion  = configPreference.getVpnVersion(context);
		if ( vpnVersion!= null)
		{
			xmlbody.append(String.format("<profile id=\"%s\" version=\"%s\"/>", configPreference.VPN_INDENTIFIER,vpnVersion));
			xmlbody.append("\r\n");
		}

		ArrayList<ApkProfileContent> hallApkList = ApkProfileContent.queryAllApkContents(context);
		if (hallApkList != null)
		{
			Iterator<ApkProfileContent> it = hallApkList.iterator();
			while(it.hasNext())
			{
				//<app id="com.accenture.hall" version =”1” name="应用大厅" installed="true" />
				ApkProfileContent apk = (ApkProfileContent)it.next();
				String bInstall = apk.mApkFlag.equals(String.valueOf(Appdb.INSTALLED)) ? "true":"false";
				String app = String.format("<app id=\"%s\" version=\"%s\" name=\"%s\" installed=\"%s\" />", 
						apk.mApkId,apk.mApkVersion,apk.mApkName,bInstall);
				Log.d("APPPUSH",app);
				xmlbody.append(app);
				xmlbody.append("\r\n");
			}
		}

		xmlbody.append("<sysinfo>\r\n");
		
		//applist
		AppList applist = new AppList(context);
		applist.onDemoAppList();
		xmlbody.append(applist.writeListXML());
		
		//SysInfo
		MoshinInformation mosInfo = MoshinInformation.getInstance(context);
	    if (mosInfo != null)
		{
			Map<String, String> infoMap = mosInfo.write();

			Set<Entry<String, String>> infoSet = infoMap.entrySet();
			Iterator<Entry<String, String>> iter = infoSet.iterator();
			while(iter.hasNext())
			{
				Map.Entry entry = (Map.Entry)iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();

				String info = String.format("<info id =\"%s\">%s</info>\r\n", key, value);
				xmlbody.append(info);
			}
		}
		xmlbody.append("</sysinfo>\r\n");
		
		//location
		//<location coordinate="30.000,40.000" />
		
		xmlbody.append("</info>\r\n");

		return xmlbody.toString();
	}

	public boolean writeFile(String path, String txt) {
		try {
			Log.v(TAG,"write file.");
			OutputStream os = new FileOutputStream(new File(path));//(path, Activity.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(txt);
			osw.close();
			os.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private InputStream httpUrlConnection(String requestString,checkUpdateThread thread) {
		InputStream updateXmlContent = null;
		HttpURLConnection httpConn = null;
		try {
			String pathUrl = checkUpdateUrl;
			// 建立连接
			URL url = new URL(pathUrl);
			httpConn = (HttpURLConnection) url.openConnection();

			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法
			// String requestString = "客服端要以以流方式发送到服务端的数据...";

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes("utf-8");
			httpConn.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			//String lang = getLang();
			httpConn.setRequestProperty("Accept-Language", getLang());
			httpConn.setRequestProperty("Content-Type",
			"application/octet-stream");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//
			//String name = URLEncoder.encode("黄武艺", "utf-8");
			//httpConn.setRequestProperty("NAME", name);
			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
			httpConn.setReadTimeout(READ_TIMEOUT);
			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
				Log.i(TAG, "HTTP State OK");
				String line="";
				StringBuffer buffer=new StringBuffer("");
				// InputStream l_urlStream;    
				InputStream l_urlStream = httpConn.getInputStream();    
				BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));   

				while ((line = l_reader.readLine()) != null) {
					//Log.i("HttpURL",line);
					buffer.append(line);
					//ServiceLog.writeLog(line);
				}
				if (buffer.toString().length() > 0) {
					updateXmlContent = new ByteArrayInputStream(buffer.toString().getBytes());
				}
			} else {
				if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
					messageDelay(startTime);
					HallMessageManager.updataFinish(context, (String) context
							.getText(R.string.updata_finish));
				}
			}
		} catch (Exception ex) {
			if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE ||
					thread.am == ServiceMessage.autoOrManual.LOGINUPDATE) {
				Log.w(TAG, "Update Error");
				messageDelay(startTime);
				HallMessageManager.updataFinish(context, (String) context
						.getText(R.string.network_erro));
			}
			ex.printStackTrace();
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
			}
		}
		return updateXmlContent;
	}

	@SuppressWarnings("unused")
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private curCheckUpdateResult readUpdateCheckXML(InputStream inStream,checkUpdateThread thread) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		curCheckUpdateResult result = new curCheckUpdateResult();
		try {
			// -----ready to parser the xml-------
			DocumentBuilder builder = factory.newDocumentBuilder();
						
			Document dom = builder.parse(inStream);

			Element root = dom.getDocumentElement();

			// <info state="ok" message="error message" >
			// <config checkcycle="1800" />
			// get state
			String state = null;
			String msg = null;
			String sessionId = null;
			//in case server dispatch a message which the root node's name is not info.
			if(!root.getNodeName().equals("info")) {
				NodeList info = root.getElementsByTagName("info");
				if(info != null && info.getLength() > 0) {
					Element infoNode = (Element) info.item(0);
					state = infoNode.getAttribute("state");
					msg = infoNode.getAttribute("message");
					sessionId = infoNode.getAttribute("sid");
				} 
			}else {
				state = root.getAttribute("state");
				msg = root.getAttribute("message");
				sessionId = root.getAttribute("sid");
			}

			result.status = state;
			if (sessionId != null)
				result.sessionId = sessionId;
			if (!state.equals(STATE_OK)) {
				// if error happens,just return
				result.errorMsg = msg;
				return result;
			}
			Log.i("checkcycle", "state" + state);
			// get checkcycle
			String checkcycle = null;
			NodeList config = root.getElementsByTagName("config");
			for (int i = 0; i < config.getLength(); i++) {
				Element infoNode = (Element) config.item(i);
				checkcycle = infoNode.getAttribute("checkcycle");
				// write to pref
				/*
				 * SharedPreferences pp =
				 * context.getSharedPreferences(ListenerService.PREF_NAME, 0);
				 * Editor d = pp.edit();
				 * 
				 * d.putString(CHECKCYCLE, checkcycle); d.commit();
				 */
				configPreference.putCheckCycle(context, checkcycle);

				Log.i("checkcycle", "checkcycle" + checkcycle);

			}

			// ------judge the xml content type------
			NodeList configNodes = root.getElementsByTagName("profile");// 查找所有profile节点
			Log.i("DomXMLReader", "DomXMLReader" + " : " + "profile count = "
					+ configNodes.getLength());
			// int configNodeCnt = configNodes.getLength();
			result.configUpdateCnt = configNodes.getLength();
			// save to pref

			NodeList appNodes = root.getElementsByTagName("app");// 查找所有app节点
			// int appNodeCnt = appNodes.getLength();
			result.appUpdateCnt = appNodes.getLength();

			if ((result.configUpdateCnt == 0) && (result.appUpdateCnt == 0))
				result.mUpdateContentType = NONE_UPDATE;
			else if ((result.configUpdateCnt == 0) && (result.appUpdateCnt > 0))
				result.mUpdateContentType = APP_UPDATE;
			else if ((result.configUpdateCnt > 0) && (result.appUpdateCnt == 0))
				result.mUpdateContentType = CONFIG_UPDATE;
			else
				result.mUpdateContentType = BOTH_APP_CONFIG_UPDATE;

			// ---parser the config profile---

			for (int i = 0; i < configNodes.getLength(); i++) {
				Element profileNode = (Element) configNodes.item(i);

				configUpdateContent item = new configUpdateContent();
				item.configId = profileNode.getAttribute("id");
				item.configUrl = profileNode.getAttribute("url");
				item.configVer = profileNode.getAttribute("version");

				//
				result.configUpdateList.add(item);

				// Log.i(Util.TAG, Util.TAG_PREFIX + "profile id = "
				// + item.configId + " profile ver = " + item.configVer
				// + " profile url = " + item.configUrl);
			}

			// ---parser the app update list---

			result.newAppCnt=0;
			result.changeCnt=0;
			result.appIds = new long[appNodes.getLength()];
			for (int i = 0; i < appNodes.getLength(); i++) {
				Element appNode = (Element) appNodes.item(i);

				// Appdb data = new Appdb();
				ApkProfileContent apkContent = new ApkProfileContent();
				String apkId = appNode.getAttribute("id");
				String apkName = appNode.getAttribute("name");
				String apkUrl = appNode.getAttribute("url");
				String apkDesc = appNode.getAttribute("desc");
				String apkVersion = appNode.getAttribute("version");
				String apkColorIcon;
				String apkGreyIcon;

				if (apkId.length() != 0)
					apkContent.mApkId = apkId;
				if (apkName.length() != 0)
					apkContent.mApkName = apkName;
				if (apkUrl.length() != 0)
					apkContent.mApkUrl = apkUrl;
				if (apkDesc.length() != 0)
					apkContent.mApkDesc = apkDesc;
				if (apkVersion.length() != 0){
					apkContent.mApkVersion = apkVersion;
				}

				// apkContent.mApkId = "com.aemm.test" + String.valueOf(index);
				NodeList appChildsNodes = appNode.getChildNodes();

				Log.i("vesionvesionvesion", String
						.valueOf(apkContent.mApkVersion));

				for (int j = 0; j < appChildsNodes.getLength(); j++) {
					Node appChildNode = (Node) appChildsNodes.item(j);

					// 判断是否为元素类型
					if (appChildNode.getNodeType() == Node.ELEMENT_NODE) {
						// Log.i(Util.TAG, Util.TAG_PREFIX +
						// "is ELEMENT_NODE ");
						if ("color".equals(appChildNode.getNodeName())) {
							NodeList colorChildsNodes = appChildNode
									.getChildNodes();
							for (int k = 0; k < colorChildsNodes.getLength(); k++) {
								Node colorChildNode = (Node) colorChildsNodes
										.item(k);

								if (colorChildNode.getNodeType() == Node.CDATA_SECTION_NODE) {

									apkColorIcon = colorChildNode
											.getNodeValue();
									if (apkColorIcon.length() > 0) {
										apkContent.mIconColor = apkColorIcon;
										Log.i(Util.TAG, apkColorIcon);
									}
								}
							}
						} else if ("gray".equals(appChildNode.getNodeName())) {
							NodeList grayChildsNodes = appChildNode
									.getChildNodes();

							for (int k = 0; k < grayChildsNodes.getLength(); k++) {
								Node grayChildNode = (Node) grayChildsNodes
										.item(k);

								if (grayChildNode.getNodeType() == Node.CDATA_SECTION_NODE) {
									// Log.i(Util.TAG, Util.TAG_PREFIX
									// + "gray child node Type = "
									// + grayChildNode.getNodeType());

									apkGreyIcon = grayChildNode.getNodeValue();
									if (apkGreyIcon.length() > 0) {
										apkContent.mIconGrey = apkGreyIcon;
										// Log.i(Util.TAG,apkGreyIcon);
									}

								}
							}
						}

					}
				}

				// curCheckUpdateResult.appUpdateList.add(data);
				Log.i(TAG, "Start to record app update...."+apkContent.mApkName+" version:"+apkContent.mApkVersion);

				apkContent.mApkFlag = String.valueOf(Appdb.NEWAPP);
				apkContent.mApkInstallEnabled = "true";
				log.d("get apk from server:"+apkContent.getInfo());
				long rowId = 0;
				// step 1. query data in db to see if exist record with the same
				// apkid and not installed
				 ApkProfileContent valueInstalled =
					  ApkProfileContent.restoreApkProfileContentWithApkId( context,
					 apkContent.mApkId,apkContent.mApkVersion,Appdb.INSTALLED);
				 
				 ApkProfileContent valueUinstall =
					 ApkProfileContent.restoreApkProfileContentWithApkId( context,
							 apkContent.mApkId, Appdb.UNINSTALLED, Appdb.NEWAPP);
				 if (valueInstalled != null)
				 {
					 log.d("exist in installed area:"+valueInstalled.getInfo());
					 if(valueUinstall!=null){
						 ApkProfileContent.deleteApkContentwithNameandFlag(context,valueUinstall.mId);
						 result.appIds[result.changeCnt] = valueUinstall.mId;
						 result.changeCnt++;
						 log.d("and delete from uninstall:"+valueUinstall.getInfo());

					 }
				 }else{
					 if(valueUinstall!=null){
						 //replace it
							if(valueUinstall.mApkVersion.equals(apkContent.mApkVersion)){
//								apkContent.mApkFlag=String.valueOf(Appdb.UNINSTALLED);
//								ApkProfileContent.updateApkContentwithRowId(context, valueUinstall.mId, apkContent.toContentValues());
//								rowId = valueUinstall.mId;
								log.d("exist in uninstalled area:"+valueUinstall.getInfo()+"\n and updata");
								continue;
							}else{
								ApkProfileContent.updateApkContentwithRowId(context, valueUinstall.mId, apkContent.toContentValues());
								result.appIds[result.changeCnt] = valueUinstall.mId;
								result.changeCnt++;
								if(!valueUinstall.mApkFlag.equals(String.valueOf(Appdb.NEWAPP))){
									result.newAppCnt++;
								}
								 
								log.d("exist in uninstalled area:"+valueUinstall.getInfo()+"\n and updata");
							}
					 }else{
						 apkContent.save(context);
						 result.appIds[result.changeCnt] = apkContent.mId;
						 result.changeCnt++;
						 result.newAppCnt++;
						 log.d("to add new apk:"+apkContent.mId);
					 }
				 }
					 log.d("new apk number:"+result.newAppCnt);
				// change by wjx 2011.7.19 14:05
				/*ApkProfileContent value = ApkProfileContent.queryApkProfile(
						context, apkContent.mApkId,apkContent.mApkVersion);

				if (value != null) {
					// update
					// rowId = value.mId;
					// int num =
					// ApkProfileContent.updateApkContentwithRowId(context,
					// rowId, apkContent.toContentValues());
					// Log.i(TAG, String.valueOf(num)+
					// " record has been updated and id is " +
					// String.valueOf(rowId));
				} else {
					// insert
					// step 1.save in db

					apkContent.save(context);
					rowId = apkContent.mId;
					Log.i(TAG, "insert a new record id is"
							+ String.valueOf(rowId));
					result.newAppCnt++;
				}*/


			}

			inStream.close();
			// //step 2. notify ui
			// app needs to be updated

		} catch (Exception e) {
			e.printStackTrace();
			// error happens
			//String hallMessage;
//			if (thread.am == ServiceMessage.autoOrManual.MANUALUPDATE) {
//				hallMessage = e.getMessage();
//			
//				HallMessageManager.cancelMessage(context,HallMessagedb.RESULTMSG);
//				HallMessageManager.finishUpdate(context);
//			
//				Log.i("NetError", hallMessage);
//			}
		}
		return result;
	}

	private String getLang() {
		String loc = "zh";
		if (null != context) {
			loc = context.getResources().getConfiguration().locale.getLanguage();
		}
		return loc;
	}
}
