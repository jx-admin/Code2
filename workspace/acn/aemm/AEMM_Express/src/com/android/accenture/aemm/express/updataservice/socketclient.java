package com.android.accenture.aemm.express.updataservice;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.android.accenture.aemm.express.HallMessageManager;
import com.android.accenture.aemm.express.LocationInfo;
import com.android.accenture.aemm.express.Main;
import com.android.accenture.aemm.express.ScrollAdapter;
import com.android.accenture.aemm.express.updataservice.ListenerService.pushCommandType;
import com.android.accenture.aemm.express.updataservice.ListenerService.pushResult;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;

public class socketclient {

	public static final String APPLOCK = "applock";
	public static final String APPIDS = "appids";
	public static final String APPENABLES = "appEnables";
    public static final int TIME_OUT = -3;
    public static final int CONNECT_ERROR = -4;
//    public static final String HOST = "aemm.imolife.com";
//	int PORT                        = 10086;
//    public static final String HOST = "aemm-dev.imolife.com";
//	int PORT                        = 10080;
//    public static final String HOST = "124.205.250.28";
//	int PORT                        = 10086;
    public static final String HOST = "114.255.7.85";
	int PORT                        = 10086;
	private  Socket   			mSocket = null;
	private  OutputStream   	mOut = null;
	private  InputStream   		mIn = null;	
	SocketAddress address;
	Context context;
	
	public socketclient(Context c){
		if(mSocket!=null)
		{
			Log.i(Util.TAG,"connhost close");
			close();
			mSocket = null; // Bug #2750 shxn
		}
		// Bug #2750 shxn
		address = new InetSocketAddress(HOST, PORT);
        mSocket  = new Socket();
        // Bug #2750 shxn
        context = c;
	}
	
	public void closeSocket() throws IOException
	{
		if (mSocket != null)
		{
			if (mOut != null)
				mOut.close();
			mSocket.close();
		}
	}
	public int connhost(){
		Log.i(Util.TAG, "socketclient connhost this = "+this.toString());

		try{
	        mSocket.connect( address, 60000);
//	        mSocket.setSoTimeout(60000);
	        Log.i(Util.TAG,"connhost success");
	    }
		catch(SocketTimeoutException e)
	    {
	    	Log.e(Util.TAG,"socket connect " + HOST);
	        Log.e(Util.TAG,"socket connect TIME_OUT",e);
	        return TIME_OUT;
	    }
		catch (ConnectException e)
		{
			Log.e(Util.TAG,"socket ConnectException ",e);
			return CONNECT_ERROR;
		}
		catch (NoRouteToHostException e)
		{
			Log.e(Util.TAG,"socket NoRouteToHostException ",e);
			return CONNECT_ERROR;
		}
		catch (SocketException e)
		{
			Log.e(Util.TAG,"socket SocketException ",e);
			return CONNECT_ERROR;
		}
	    catch(Exception e){
            Log.e(Util.TAG,"socket connect: ", e);
            return 0;
        } 
	    return 1;
	}
	public String buildHearBeatXml()
	{
		double longitude = 0,latitude = 0;
		LocationInfo location=LocationInfo.getInstance(context);
		longitude = location.getLongitude();
		latitude = location.getLatitude();
		
		String xmlbody = String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<location coordinate=\"+%f,+%f\"/>", longitude,latitude);
		return xmlbody;
	}
	public String buildLoginXml()
	{
		String sessionId = configPreference.getSessionId(context);
		if (sessionId != null)
		{
		String xmlbody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		//<?xml version="1.0" encoding="utf-8" ?>
		//<login id="xxxx-xxxxx-xxxx��(session id)" />
		xmlbody += "<login id=\"";
		xmlbody += sessionId;
		xmlbody += "\"/>";
		return xmlbody;
		}
		else
		{
			return null;
		}
	}
	public int parserLoginResult(InputStream inStream,String errmsg)
	{
		int ret = -1;
		//<?xml version="1.0" encoding="utf-8" ?>
		//<login-ret state="ok" message="error message or empty string when the state is ok." />
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			// -----ready to parser the xml-------
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document dom = builder.parse(inStream);

			Element root = dom.getDocumentElement();
			//String alertmassge = null;
			// ------judge the xml content type------
			NodeList nodes = root.getElementsByTagName("login-ret");
			if (nodes.getLength() > 0)
			{
				Element node = (Element) nodes.item(0);
				String state = node.getAttribute("state");
				errmsg = null;
				if (!state.equals("ok"))
				{
					errmsg = node.getAttribute("message");
				}
				 Log.e(Util.TAG,"socket connect: ");
			}
		}catch (Exception e) {

			e.printStackTrace();
		
		}
		
		return ret;
	}
	public int sendreq(byte[] data) 
	{
		Log.i(Util.TAG, "socketclient sendreq this = "+this.toString());
		try{
			String str = new String(data);
		    mOut = mSocket.getOutputStream();
		    if(mOut != null)
		    {
				Log.i(Util.TAG,str);
				mOut.write(data);
				mOut.flush();
		    }
		}
		catch(SocketTimeoutException e)
        {
		    Log.e(Util.TAG,"TIME_OUT",e);
            return TIME_OUT;
        }
		
		catch(Exception e)
		{
	
			Log.e(Util.TAG,"sendreq",e);
	        return 0;
	    }	
	    return 1;
		
	}
	public int sendreq(byte data) 
	{
		Log.i(Util.TAG, "socketclient sendreq this = "+this.toString());
		try{
		    mOut = mSocket.getOutputStream();
		    if(mOut != null)
		    {
				Log.i(Util.TAG,"WRIT DATA:"+data);
				mOut.write(data);
				mOut.flush();
		    }
		}
		catch(SocketTimeoutException e)
        {
		    Log.e(Util.TAG,"TIME_OUT",e);
            return TIME_OUT;
        }
		
		catch(Exception e)
		{
	
			Log.e(Util.TAG,"sendreq",e);
	        return 0;
	    }	
	    return 1;
		
	}
	
	public pushResult recvdata() 
	{
		Log.i(Util.TAG, "socketclient recvdata");
		
		//int Len=0;	
		pushResult result = null;
		//result.pushType= pushCommandType.HEART_BEAT_RSP;  
		
		try{
			mIn = mSocket.getInputStream();
			if(mIn != null)
			{
				Log.i(Util.TAG,"socketclient recvdata getInputStream ok");
				
				BufferedReader l_reader=new BufferedReader(new InputStreamReader(mIn));   
	             
	            StringBuffer buffer =new StringBuffer();  
	            
	            String line = "";
	            int i = 0;
	            while(i<10)
	            {
	            	i++;
	            	line = l_reader.readLine();
	            	if (line == null)
	            	{
	            		break;
	            	}
	            	else
	            	{
	            		if (line.equals(""))
	            			break;
	            		Log.i(Util.TAG, "line is  " + line + String.valueOf(i));
	            	}
	            	buffer.append(line);
	            	//ServiceLog.writeLog(line);
	            }
	            /*
	            char[] buf = new char[1024];
	            int num = l_reader.read(buf);
	            if (num > 0)
	            {
	            	String xml = String.valueOf(buf);
	 	           
	 	            InputStream xmlcontent = new ByteArrayInputStream(xml.getBytes());//buffer.toString().getBytes());
	 	           // Log.i(Util.TAG, "socketclient recvdata ok: " + String.valueOf(buf) );
	 	            result = readPushCommandXML(xmlcontent);	
	            }
	            else
	            {
	            	result = new pushResult();
					result.pushType = pushCommandType.EXCEPTION;
	            	Log.i(Util.TAG, "num ,< 0 " );
	            }*/
	            if (buffer.length() > 0)
	            {
	            	 InputStream xmlcontent = new ByteArrayInputStream(buffer.toString().getBytes());//buffer.toString().getBytes());
		 	           // Log.i(Util.TAG, "socketclient recvdata ok: " + String.valueOf(buf) );
		 	         result = readPushCommandXML(xmlcontent);
	            }
	            else
	            {
	            	result = new pushResult();
					result.pushType = pushCommandType.EXCEPTION;
	            	Log.i(Util.TAG, "num ,< 0 " );
	            }
			}
			else
			{
				result = new pushResult();
				result.pushType = pushCommandType.EXCEPTION;
				Log.i(Util.TAG,"recvdata mIn = null");
				return result;
			}
		}
		catch(SocketTimeoutException e)
        {
			result = new pushResult();
			result.pushType = pushCommandType.EXCEPTION;
		    Log.e(Util.TAG,"socket read TIME_OUT",e);
            return result;
        }
		catch(Exception e)
		{
			result = new pushResult();
			Log.e(Util.TAG,"recvdata: ", e);
			result.pushType = pushCommandType.EXCEPTION;
	        return result;
	    }	
	    return result;
		
	}	

	public int close() 
	{
		Log.i(Util.TAG, "socketclient close this = "+this.toString());
		try{
			Log.i("DCD","close");
			//mSocket.shutdownInput();
			//mSocket.shutdownOutput();
			if(mSocket==null)
				return 0;
			if (mOut != null)
				mOut.close();
			if (mIn != null)
				mIn.close();
			mSocket.close();
			mSocket=null;
			}catch(Exception e)
			{
		
				//Log.i(Util.TAG,e.getMessage());
				Log.e(Util.TAG,"close",e);
		        return 0;
		    }	
		    return 1;		
	}
	

	public void shutdown() 
	{
		Log.i("DCD","socketclient shutdown");
		if(mSocket==null || mSocket.isClosed())
			return;
		try
		{
			if(mSocket.isInputShutdown())
			{
				mSocket.shutdownInput();		
			}
			if(mSocket.isOutputShutdown())
			{
				mSocket.shutdownOutput();		
			}	
		}catch(Exception e)
		{
			Log.e(Util.TAG,"shutdown",e);
		}
	}
	/**
	 * parse the push xml
	 * @param inStream
	 * @return pushCommandType
	 */
	private pushResult readPushCommandXML(InputStream inStream) {
		pushResult result = new pushResult();
		result.errMsg = null;
		result.pushType = pushCommandType.PUSH_NONE;
		result.alertMsg = null;
		Log.i(Util.TAG,"readPushCommandXML" + inStream.toString());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			// -----ready to parser the xml-------
			DocumentBuilder builder = factory.newDocumentBuilder();
			Log.i(Util.TAG,"readPushCommandXML1");
			Document dom = builder.parse(inStream);
			Log.i(Util.TAG,"readPushCommandXML2");
			Element root = dom.getDocumentElement();
			String alertmassge = null;
			
			//find info
			NodeList infoNodes = root.getElementsByTagName("info");
			Log.i("DomXMLReader", "DomXMLReader" + " : " + "info count = "
					+ infoNodes.getLength());
			
			
			//---find out not the notify node---
			if((infoNodes.getLength() > 0) )
			{
				Element node = (Element) infoNodes.item(0);
				String state = node.getAttribute("state");
				result.pushType = pushCommandType.HEART_BEAT_RSP;
				if (!state.equals("ok"))
				{
					result.errMsg = state;
				}
				return result;
			}
					
			NodeList loginNodes = root.getElementsByTagName("login-ret");
			if (loginNodes.getLength() > 0)
			{
				Log.i(Util.TAG,"find login-ret");
				Element node = (Element) loginNodes.item(0);
				String state = node.getAttribute("state");
				//for PUSH_LOGIN . modified by cuixiaowei 20110718
				//String errmsg = null;
				result.errMsg = null;
				if (!state.equals("ok"))
				{
					Log.i(Util.TAG,"errormsg");
					//errmsg = node.getAttribute("message");
					result.errMsg = node.getAttribute("message");
				}
				result.pushType = pushCommandType.PUSH_LOGIN;
				return result;
			}
			
			// ------judge the xml content type------
			NodeList notifyNodes = root.getElementsByTagName("notify");
//			//---find out not the notify node---
//			if(notifyNodes.getLength() < 1)
//			{
//				Log.i(Util.TAG,"no notifyNodes");
//				return result;
//			}
//			
//				
//			Log.i("DomXMLReader", "DomXMLReader" + " : " + "notify count = "
//					+ notifyNodes.getLength());
//			// ---judge the push command type---
//			Element notifyNode = (Element) notifyNodes.item(0);
//			String command = notifyNode.getAttribute("type");
//			Log.i(Util.TAG, Util.TAG_PREFIX + "push command = "	+ command);
			
			String command     = null;
			Element notifyNode = null;
			if (notifyNodes.getLength() < 1) {
				command = root.getAttribute("type");
				if (root.hasChildNodes()) {
					notifyNodes = root.getChildNodes();
					notifyNode = (Element) notifyNodes.item(0);
				}
			} else {
				notifyNode = (Element) notifyNodes.item(0);
				command = notifyNode.getAttribute("type");
			}

			if (command == null) {
				return result;
			}
			
			//get alert massage
			NodeList alertNodes = root.getElementsByTagName("alert");
			if (alertNodes.getLength() > 0)
			{
				//no alert massage
				Log.i(Util.TAG, Util.TAG_PREFIX + "alert =  "	+ command);
				Element alertNode = (Element)alertNodes.item(0);
				
				alertmassge = alertNode.getTextContent();
				result.alertMsg = alertmassge;
			}
			if(command.equalsIgnoreCase("profile")) {
				result.pushType = pushCommandType.PUSH_PROFILE_UPDATE;
				
				
			}
			else if(command.equalsIgnoreCase("app")) {
				result.pushType = pushCommandType.PUSH_APP_UPDATE;
				
				
				Log.i("DomXMLReader", "DomXMLReader" + " : " + pushCommandType.PUSH_APP_UPDATE);
				
			}
			else if(command.equalsIgnoreCase("profile,app")){
				result.pushType = pushCommandType.PUSH_ALL_UPDATE;
				//get profile nodes and checkupdate
				//notify ui
				
			}
			else if(command.equalsIgnoreCase("applock")){
				result.pushType = pushCommandType.APP_LOCK;
				//Intent intent = new Intent(APPLOCK);
				//tell ui which apps are locked
				NodeList appNodes = root.getElementsByTagName("app");
				int len = appNodes.getLength();
				if (len > 0)
				{
					String [] ids = new String [len];
					boolean [] enables = new boolean[len];
					for (int i = 0; i < len; i++) {
						Element infoNode = (Element) appNodes.item(i);
						ids[i] = infoNode.getAttribute("id");
						enables[i] = infoNode.getAttribute("enable").equals("true")?true:false;
						Log.i(ScrollAdapter.APPLOCK,"socketclient receive broadcast appLock.");
						if(Main.debugView){
						for(int j=0;j<ids.length&&j<enables.length;j++){
				      		Log.i(ScrollAdapter.APPLOCK,"socketclient receve appLock "+ids[j]+"->"+enables[j]);
				      	}}
					}
					
					HallMessageManager.HallAppLock(context,ids,enables);
					
					//update db
					ApkProfileContent.updateApkContentEnalbedwithApkId(context,ids,enables);
					
				}
				
				
			}
			else if(command.equalsIgnoreCase("lock")){
				result.pushType = pushCommandType.LOCK_SCREEN;
				//may get password
				String password = null;
				if (notifyNode.hasChildNodes()) {
					NodeList passwordNode = notifyNode.getChildNodes();
					password = passwordNode.item(0).getTextContent();
				} else {
					password = notifyNode.getNodeValue();
				}

				int len = password.length();
				
				//set password
				DeviceAdminLocalSetup da = DeviceAdminLocalSetup.getInstance(context);
				int policylen = da.getPasswordMinimumLength(context);
				if (len < policylen)
					da.pwLength = len;
				if (da.isNumber(password) && da.isAlphabetic(password))
					da.pwQuality = DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC;
				else if (da.isNumber(password))
					da.pwQuality = DevicePolicyManager.PASSWORD_QUALITY_NUMERIC;
				else if (da.isAlphabetic(password))
					da.pwQuality = DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC;
				da.updatePolicies(context);
				da.reSetPasswordPolicy(password);
				da.forceLock();
				
			}
			else if(command.equalsIgnoreCase("wipe"))
			{
				Log.i(Util.TAG, "wipe");
				result.pushType = pushCommandType.WIPE_DATA;
				
				DeviceAdminLocalSetup da =  DeviceAdminLocalSetup.getInstance(context);
				da.wipeData();
			}
			else
				result.pushType = pushCommandType.HEART_BEAT_RSP;

			inStream.close();

		} catch (Exception e) {

			e.printStackTrace();
			result.pushType = pushCommandType.EXCEPTION;
		}
		
		Log.i(Util.TAG, Util.TAG_PREFIX + "push command type = " + result.pushType);
		return result;
	}
	
}
