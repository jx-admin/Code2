package com.aess.aemm.networkutils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aess.aemm.AEMMConfig;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.push.PushService;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoAdress {
	public static final String TAG = "AutoAdress";
	public static final String DEBUGFILE = "address.xml";
	
	public static final String DEV   = "DEV";
	public static final String TEST  = "TEST";
	public static final String T_SSL = "TEST_SSL";
	public static final String SSD   = "SSD";
	public static final String UK    = "UK";
	public static final String CLOUD = "CLOUD";
	public static final String YC    = "YC";
	public static final String GCCT  = "GC-CT";
	public static final String AUTO  = "AUTO";
	
	public static void intentForNewUpdateURL(Context con) {
		if (AEMMConfig.SIGN.equals(AUTO)) {
			Intent message = new Intent(PushService.NEWDOMAIN);
			con.startService(message);
		}
	}
	
	public static AutoAdress getInstance(Context cxt) {
		if (null != cxt) {
			if (null == __autoAdress) {
				__autoAdress = new AutoAdress(cxt);
			}
		}
		return __autoAdress;
	}
	
	public String updateUrl() {

		if (null == updateURL) {
			intentForNewUpdateURL(_cxt);
			Log.w(TAG, "Not Have Update URL");
		}
		return updateURL;
	}
	
	public int init() {
		int ret = 1;
		if (DEV.equals(AEMMConfig.SIGN)) {
			socketIp = "124.205.250.21";
			socketPort = 10090;
			updateURL = "https://124.205.250.21:8001/DeviceUpdate.aspx";
		} else if (TEST.equals(AEMMConfig.SIGN)) {
			socketIp = "bjaemm.oicp.net";
			socketPort = 16090;
			updateURL = "http://bjaemm.oicp.net:16001/DeviceUpdate.aspx";
		} else if (T_SSL.equals(AEMMConfig.SIGN)) {
			socketIp = "bjaemm.oicp.net";
			socketPort = 14090;
			updateURL = "https://bjaemm.oicp.net:14001/DeviceUpdate.aspx";
		} else if (SSD.equals(AEMMConfig.SIGN)) {
			socketIp = "bjaemm.oicp.net";
			socketPort = 12090;
			updateURL = "http://bjaemm.oicp.net:12001/DeviceUpdate.aspx";
		} else if (YC.equals(AEMMConfig.SIGN)) {
			socketIp = "114.255.7.85";
			socketPort = 10090;
			updateURL = "http://114.255.7.85:8002/DeviceUpdate.aspx";
		} else if (UK.equals(AEMMConfig.SIGN)) {
			socketIp = "192.168.16.214";
			socketPort = 10090;
			updateURL = "http://192.168.16.214:8002/DeviceUpdate.aspx";
		} else if (CLOUD.equals(AEMMConfig.SIGN)) {
			socketIp = "50.112.130.13";
			socketPort = 10090;
			updateURL = "http://50.112.130.13:8002/DeviceUpdate.aspx";
		} else if (GCCT.equals(AEMMConfig.SIGN)) {
			socketIp = "50.112.130.13";
			socketPort = 10090;
			updateURL = "http://50.112.130.13:8002/DeviceUpdate.aspx";
		} else if (AUTO.equals(AEMMConfig.SIGN)) {
			getConfigFromFile();
			if ( null == updateURL) {
				String domain = CommUtils.getDomain(_cxt);
				if (null == domain) {
					Log.w(TAG, "Error, Didn't have domain");
					ret = -1;
				} else {
					ret = getConfigFromServer(domain);
					if (ret > 0) {
						saveConfigInFile();
						ret = 1;
					}
				}
			}
		} else {
			ret = -1;
		}
		return ret;
	}
	
	public String getUpdateURL() {
		return updateURL;
	}
	
	public int getConnectPort() {
		return socketPort;
	}
	
	public String getAddress() {
		return socketIp;
	}
	
	public boolean ifInit() {
		boolean rlt = false;
		if (null != updateURL)
		{
			rlt = true;
		}
		return rlt;
	}
	
	public void setNetConfig(String address, int pport, String update) {
		if (null != address) {
			this.socketIp = address;
		}
		if (0 != pport) {
			this.socketPort = pport;
		}
		if (null != update) {
			this.updateURL = update;
		}
	}
	
	public void clear() {
		socketIp = null;
		socketPort =0;
		updateURL = null;
		saveConfigInFile();
	}
	
	private int getConfigFromServer(String domain) {
		int ret = -1;
		if (null != _cxt) {
			if (false == work) {
				work = true;
				try {
					String url = AEMMConfig.GuideAddress + "?domainName=" + domain;
					ret = addressUrlGet(_cxt, url, this);
				} catch (Exception e) {
					work = false;
					e.printStackTrace();
				}
				work = false;
			}

		}
		return ret;
	}
	
	private int  getConfigFromFile() {
		int ret = -1;
		if (null != _cxt) {
			String ip = CommUtils.getSocketIp(_cxt);
			String up = CommUtils.getUpdateURL(_cxt);
			int pp = CommUtils.getPushPort(_cxt);
			setNetConfig(ip, pp, up);
			ret = 1;
		}
		return ret;
	}
	
	private void saveConfigInFile() {
		if (null != _cxt) {
			CommUtils.setNetConfig(_cxt, this);
		}
	}
	
	private static int addressUrlGet(Context context, String urlPath, AutoAdress arg) throws Exception {
		int ret = 0;
		String info = null;
		
		InputStream is = HttpHelp.aemmHttpGet(context, urlPath, DEBUGFILE);
		info = HttpHelp.readData(is);
		
		if (info != null && info.length() > 0) {
			ret = addressParser(info, arg);
		} else {
			ret = -1;
			Log.w(TAG, "Didn't get update url");
		}

		return ret;
	}
	
	private static int addressParser(String info, AutoAdress arg) {
		int rlt = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(info.getBytes());
			Document dom = builder.parse(is);
			Element root = dom.getDocumentElement();
			if (null != root) {
				String address = null;
				int pport      = 0;
				String tcplink = root.getAttribute("tcplink");
				if (null != tcplink && tcplink.length()>1) {
					Log.i(TAG, tcplink);
					int start = AEMMConfig.TCP.length();
					int end = tcplink.length();
					tcplink = (String) tcplink.subSequence(start, end);
					String[] args = tcplink.split(":");
					if (args.length > 1) {
						address = args[0];
						pport = Integer.valueOf(args[1]);
					}
					
				}
				String hearturl = root.getAttribute("hearturl");
				if (null != hearturl && null != address && pport > 0) {
					Log.i(TAG, hearturl);
					arg.setNetConfig(address, pport, hearturl);
					rlt = 1;
				}
				
				String message = root.getAttribute("message");
				if (null != message && message.length() > 0) {
					Log.i(TAG, message);
					rlt = NetUtils.ERROR_DOMAIN;
				}
			}

		} catch (Exception e) {
			rlt = NetUtils.ERROR_DOMAIN;;
			e.printStackTrace();
		}
		return rlt;
	}
	
	private AutoAdress(Context cxt) {
		_cxt = cxt;
	}
	
	private static AutoAdress __autoAdress  = null;
	private volatile boolean work = false;
	private Context _cxt  = null;
	private String updateURL = null;
	private String socketIp  = null;
	private int socketPort   = 0;
}