package com.android.accenture.aemm.express.updataservice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

import com.android.accenture.aemm.express.R;

public class configParser {

	public static enum ProfileType {
		Profile_NONE,Profile_ROOT,Profile_VPN,Profile_WebClip, 
		Profile_Password, Profile_APN, Profile_PassPolicy,
		Profile_Wifi,Profile_Email,Profile_Exchange,Profile_RootCertificate,
		Profile_PkcsCertificate,Profile_Restrictions
	}



	String node_value_profileIndentifier = "PayloadIdentifier";
	String node_value_profileType = "PayloadType";
	String node_value_profileVersion = "PayloadVersion";
	String node_value_profileContent = "PayloadContent";

	String node_key = "key";
	String node_dict = "dict";
	String node_array = "array";

	//exchage
	String value_exchange = "com.accenture.profile.eas1";
	Context mContext;
	public configParser(Context context)
	{
		mContext = context;
	}
	public static final String TAG = "configParser";
	//profile
	public static final String root_profile = "Configuration";
	public static final String vpn_profile = "com.apple.vpn.managed";

	public static final String aess_profile = "com.accenture.profile";
	public static final String webclip_profile = "com.apple.webClip.managed";
	public static final String password_profile = "com.accenture.profile";
	public static final String apn_profile = "com.apple.apn.managed";
	public static final String passpolicy_profile = "com.apple.mobiledevice.passwordpolicy";
	public static final String wifi_profile = "com.apple.wifi.managed";
	public static final String email_profile = "com.apple.mail.managed";
	public static final String exchange_profile = "com.apple.eas.account";
	public static final String pkcsCertificate_profile = "com.apple.security.pkcs12";
	public static final String rootCertificate_profile = "com.apple.security.root";
	public static final String restriction_profile = "com.apple.applicationaccess";

	//credentail
	public static final String credentail_profile = "com.accenture.profile.credential2";



	String profileId;
	String RootVersion ;
	String RootType;
	String RootIndentifier;

	public void setProfileId(String id)
	{
		profileId = id;
	}
	Node get_nextSibling(Node node)
	{
		Node next = null;
		if (node.getNextSibling() != null)
			next = node.getNextSibling();
		else 
		{
			Log.i("DomXMLReader","null");
			return null;
		}
		while (next.getNodeType()!= Node.ELEMENT_NODE)
		{
			if (next.getNextSibling() != null)
				next = next.getNextSibling();
			else
				return null;
		}
		return next;
	}
	public ProfileType getDictType(Node rootDictNode)
	{
		ProfileType type = ProfileType.Profile_NONE;
		//parse the first dictNode
		NodeList dictChilds = rootDictNode.getChildNodes();
		int j = 0;
		Node dictChildNode = (Node) dictChilds.item(j);
		do {
			//	Log.i("DomXMLReader", "DomXMLReader" + " : "
			//	+ "key's next next node name = "
			//	+ dictChildNode.getNodeName());
			//key
			if (node_key.equals(dictChildNode.getNodeName()))
			{
				//Log.i("DomXMLReader1", "DomXMLReader" + " : "
				//	+ "key's next next node name = "
				//	+ dictChildNode.getTextContent());
				String nodeValue = dictChildNode.getTextContent();//getFirstChild().getNodeValue();
				//Log.i("DomXMLReader", "nodeValue" + nodeValue );
				if (node_value_profileType.equals(nodeValue))
				{

					//type
					//get_nextsibing to get value
					dictChildNode = get_nextSibling(dictChildNode);
					String profileValue = dictChildNode.getTextContent();//getFirstChild().getNodeValue();
					//Log.i("DomXMLReader2", "DomXMLReader" + profileValue);
					if (aess_profile.equals(profileValue))
					{
						//aess profile
						//Log.i("DomXMLReader3", "DomXMLReader" + node_value_profileType);
						type = ProfileType.Profile_ROOT;

					}
					else if (vpn_profile.equals(profileValue))
					{
						type = ProfileType.Profile_VPN;

					}
					else if (webclip_profile.equals(profileValue))
					{
						type = ProfileType.Profile_WebClip;
					}
					else if (password_profile.equals(profileValue))
					{
						type = ProfileType.Profile_Password;

					}
					else if (apn_profile.equals(profileValue))
					{
						type = ProfileType.Profile_APN;

					}
					else if (passpolicy_profile.equals(profileValue))
					{
						type = ProfileType.Profile_PassPolicy;

					}
					else if (wifi_profile.equals(profileValue))
					{
						type = ProfileType.Profile_Wifi;

					}
					else if (email_profile.equals(profileValue))
					{
						type = ProfileType.Profile_Email;

					}
					else if (exchange_profile.equals(profileValue))
					{
						type = ProfileType.Profile_Exchange;

					}
					else if (rootCertificate_profile.equals(profileValue))
					{
						type = ProfileType.Profile_RootCertificate;
					}
					else if (pkcsCertificate_profile.equals(profileValue))
					{
						type = ProfileType.Profile_PkcsCertificate;
					}
					else if (restriction_profile.equals(profileValue))
					{
						type = ProfileType.Profile_Restrictions;
					}

					return type;
				}

			}
			dictChildNode = get_nextSibling(dictChildNode);


		}while(dictChildNode != null);
		return null;

	}




	public String readProfileXml(InputStream inStream)
	{
//		String version = null;
//		String profileType = null;
//		String profileIndentifier = null;
		Log.i(TAG, "readProfileXml");
		String resultStr = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();
			Node rootDict = root.getChildNodes().item(0);
			if (rootDict.getNodeType()!= Node.ELEMENT_NODE)
			{
				rootDict = rootDict.getNextSibling();
			}
			//getRootDictType
			if (getRootDictType(rootDict) != 0)
				return resultStr;
			resultStr = parseChildDicts(rootDict);
			inStream.close();
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			
		}
		//	Log.i(TAG,resultStr);
		return resultStr;
	}

	public int getRootDictType(Node rootDict)
	{
		int ret = -1;
		if (node_dict.equals(rootDict.getNodeName()))
		{
			//dict
			Log.i("testXML", "dict");
			NodeList dictChilds = rootDict.getChildNodes();
			int j = 0;
			Node dictChildNode = (Node) dictChilds.item(j);
			do {
				//key
				if (node_key.equals(dictChildNode.getNodeName())) 
				{
					String keyValue = dictChildNode.getTextContent();//getFirstChild().getNodeValue();
					if (keyValue.equals(node_value_profileContent))
					{
						ret = 0;
						dictChildNode= get_nextSibling(dictChildNode);
						//array
						Log.i("testXml",  dictChildNode.getNodeName() + " : ");
						continue;
					}
					else if (keyValue.equals(node_value_profileVersion))
					{
						dictChildNode= get_nextSibling(dictChildNode);

					}
					else if (keyValue.equals(node_value_profileType))
					{
						dictChildNode= get_nextSibling(dictChildNode);
					}
					else if (keyValue.equals(node_value_profileIndentifier))
					{
						dictChildNode= get_nextSibling(dictChildNode);
					}

				}
				dictChildNode = get_nextSibling(dictChildNode);
			}while(dictChildNode != null);

		}

		/*SharedPreferences  pp = mContext.getSharedPreferences(ListenerService.PREF_NAME, 0);
		Editor d = pp.edit();
		String novalue = "-1";
		String value = pp.getString(this.profileId,novalue);*/
		String value = configPreference.getKeyValue(mContext,this.profileId);
		if (value != null)
			RootVersion = this.profileId+value;
		Log.v("RootVersion", RootVersion + profileId);
	
		return ret;
	}


	public String parseChildDicts(Node rootDict)
	{
		Log.i(TAG, "parseChildDicts");
		int ret = -1;
		String hallUpdateMsg = null;
		boolean bhasError = false; //
		//shxn 解决Bug #2650 begin
		ArrayList<ProfileType> listtype = new ArrayList<ProfileType>();
		for (ProfileType pt:ProfileType.values()) {
			listtype.add(pt);
		}
		if (true == ListenerService.configvpn) {
			listtype.remove(ProfileType.Profile_VPN); //shxn 解决Bug #2650 
			ListenerService.configvpn = false;
		}
		//shxn 解决Bug #2650 end
		if (node_dict.equals(rootDict.getNodeName()))
		{
			//dict
			Log.i("testXML", "dict");
			HashMap <Integer,ProfileType> items = new HashMap<Integer,ProfileType>();

			NodeList dictChilds = rootDict.getChildNodes();
			int j = 0;
			Node dictChildNode = (Node) dictChilds.item(j);
			do {
				//key
				if (node_key.equals(dictChildNode.getNodeName())) 
				{
					String keyValue = dictChildNode.getTextContent();//getFirstChild().getNodeValue();
					if (keyValue.equals(node_value_profileContent))
					{
						dictChildNode = get_nextSibling(dictChildNode);
						//array
						Log.i("PayloadContent",  dictChildNode.getNodeName() + " : ");
						//array
						int length = dictChildNode.getChildNodes().getLength();
						Log.i("PayloadContent",  "length is : " + String.valueOf(length));
						boolean isWebClipType = false;  // fix bug2734 cuixiaowei 20110725
						for (int i = 1;i <length ; i += 2 )
						{
							Node dictNode = dictChildNode.getChildNodes().item(i);
							Log.i("testXml",  dictNode.getNodeName() + " : ");
							ProfileType type = getDictType(dictNode);
							listtype.remove(type); //shxn 解决Bug #2650 
							Log.i("testXml",  "type : " + String.valueOf(type));
							if ((type == ProfileType.Profile_Wifi) ||
									(type == ProfileType.Profile_VPN))
							{
								//don't parse because may need certificate
								//record item
								Log.i("paserxml ",  "miss type : " + String.valueOf(type));
								if (type == ProfileType.Profile_VPN) {
									listtype.clear();
									ListenerService.configvpn = true;
								}
								items.put(i,type);
								continue;
							}
							if(type == ProfileType.Profile_WebClip){ //fix bug2734 by cuixiaowei 20110725
								isWebClipType = true;
							}
							profile pp = profile.createProfile(type);
							if (pp == null)
								continue;
							pp.setVersion(RootVersion);
							Log.i("RootVersion","RootVersion"  + RootVersion);
							parserDictNode(pp,dictNode);

							ret = pp.saveProfile(mContext);

							if (ret != 0)
							{
								bhasError = true;
							}
						}
						//fix bug2734 by cuixiaowei 20110725 Begin
						if(isWebClipType){
							WebClipProfile.refreshWebClipShort(mContext);
							isWebClipType = false;
						}
						//fix bug2734 by cuixiaowei 20110725 end
						break;
					}
				}
				dictChildNode = get_nextSibling(dictChildNode);
			}while(dictChildNode != null);

			//check if has wifi or vpn nodes
			Iterator iter = items.entrySet().iterator(); 
			while (iter.hasNext()) { 
				Map.Entry entry = (Map.Entry) iter.next(); 
				Object key = entry.getKey(); 
				Object val = entry.getValue(); 
				int index = (Integer)key;
				ProfileType type = (ProfileType) val;
				Log.i("parser","wifi or vpn nodes" );
				Log.i("parser","key is " + String.valueOf(index) );
				Log.i("parser","value is " + String.valueOf(type) );
				Log.i("testXml1111",  dictChildNode.getNodeName() + " : ");
				Node node = (Node) dictChildNode.getChildNodes().item(index);
				Log.i("testXml",  node.getNodeName() + " : ");

				profile pp = profile.createProfile(type);
				if (pp == null)
					continue;
				pp.setVersion(RootVersion);
				Log.i("RootVersion","RootVersion"  + RootVersion);
				parserDictNode(pp,node);

				ret = pp.saveProfile(mContext);
				if (ret != 0)
				{
					bhasError = true;
				}
			} 
			//shxn 解决Bug #2650 begin
			for(ProfileType ptype:listtype) {
				profile pp = profile.createProfile(ptype);
				if (pp != null) {
					pp.clearProfile(mContext);
				}
			}
			//shxn 解决Bug #2650 end
		}
		
		//fix bug 3274 and 3279 cuixiaowei 20110905
		if (bhasError)
		{
			hallUpdateMsg = (String)(mContext.getText(R.string.update_failed));
			return hallUpdateMsg;
		}
		else{
			hallUpdateMsg = (String)(mContext.getText(R.string.update_success));
			return hallUpdateMsg;
		}
	}

	public <T extends profile> profile parserDictNode(T profile, Node node)
	{

		profile pp = profile;
		Log.i("testXml in parserDictNode",  node.getNodeName() + " : ");
		//dict
		NodeList dicts = node.getChildNodes();
		int j = 0;
		Node dictChildNode = (Node) dicts.item(j);
		String keyValue = null;
		String nodeValue = null;
		do {
			if (node_key.equals(dictChildNode.getNodeName())) 
			{
				//Log.i("testXml",  dictChildNode.getNodeName() + " : "+  dictChildNode.getTextContent() );
				keyValue = dictChildNode.getTextContent();
			}
			else if ("string".equals(dictChildNode.getNodeName()))
			{
				nodeValue = dictChildNode.getTextContent();
				pp.setValue(keyValue, nodeValue);
				Log.i("testXml+string",  keyValue + " : "+  nodeValue );
			}
			else if ("data".equals(dictChildNode.getNodeName()))
			{

				nodeValue = dictChildNode.getTextContent();
				pp.setValue(keyValue, nodeValue);
				Log.i("testXml+string",  keyValue + " : "+  nodeValue );
			}
			else if ("integer".equals(dictChildNode.getNodeName()))
			{
				nodeValue = dictChildNode.getTextContent();
				pp.setValue(keyValue, nodeValue);
				Log.i("testXml+string",  keyValue + " : "+  nodeValue );
			}
			else if ("false".equals(dictChildNode.getNodeName()) ||
					"true".equals(dictChildNode.getNodeName()))
			{
				nodeValue = dictChildNode.getNodeName();
				pp.setValue(keyValue, nodeValue);
				Log.i("testXml+string",  keyValue + " : "+  nodeValue );
			}
			else if (node_dict.equals(dictChildNode.getNodeName()))
			{
				//	Log.i("testXMl ",  "child dict" );
				if (keyValue != null)
				{
					//ticker
					if (keyValue.equals("EAPClientConfiguration"))
						pp.setValue(keyValue, nodeValue);
				}
				if (dictChildNode.getChildNodes() != null && dictChildNode.getChildNodes().getLength() != 0)
					parserDictNode(pp,dictChildNode);
			}
			else if (node_array.equals(dictChildNode.getNodeName()))
			{
				Node arrayChild = dictChildNode.getChildNodes().item(1);
				if (node_dict.equals(arrayChild.getNodeName()))
				{	
					Log.i("testXMl ",  dictChildNode.getTextContent());
					parserDictNode(pp,dictChildNode);
				}else if ("string".equals(arrayChild.getNodeName()))
				{
					Log.i("testXMl ",  dictChildNode.getTextContent());
				}
			}
			dictChildNode = get_nextSibling(dictChildNode);
		}while(dictChildNode != null);
		return pp;


	}

	public void parserArrayNodes(Node node, ArrayList<BasicNameValuePair> pair)
	{
		if (pair == null)
			pair = new ArrayList<BasicNameValuePair>();
		Log.i("testXml",  node.getNodeName() + " : ");
		//dict
		NodeList dicts = node.getChildNodes();
		int j = 0;
		Node dictChildNode = (Node) dicts.item(j);
		String keyValue = null;
		String nodeValue = null;
		do {
			Log.i("testXml111",  dictChildNode.getNodeName() + " : ");

			if (node_key.equals(dictChildNode.getNodeName())) 
			{
				keyValue = dictChildNode.getTextContent();

			}
			if ("string".equals(dictChildNode.getNodeName()))
			{

				nodeValue = dictChildNode.getTextContent();
				Log.i("testXml parserArrayNodes", keyValue + " : " + nodeValue);
				BasicNameValuePair item = new BasicNameValuePair(keyValue,nodeValue);
				pair.add(item);

			}
			if ("dict".equals(dictChildNode.getNodeName()))
			{
				Log.i("testXml parserArrayNodes",  dictChildNode.getNodeName() + " : ");
				parserArrayNodes(dictChildNode,pair);
			}
			dictChildNode = get_nextSibling(dictChildNode);

		}while(dictChildNode != null);

		return ;

	}
	//array signal node
	//dict key and value pair
	public void TestParseDict(Node dictNode)
	{
		//Log.i("testXml",  dictNode.getNodeName() + " : "+  dictNode.getTextContent() );
		NodeList dictChilds = dictNode.getChildNodes();
		int j = 0;
		Node dictChildNode = (Node) dictChilds.item(j);
		do {
			//key
			if (node_key.equals(dictChildNode.getNodeName())) 
			{
				String keyValue = dictChildNode.getTextContent();//getFirstChild().getNodeValue();

				if (keyValue.equals("PayloadContent"))
				{
					//dict
					dictChildNode= get_nextSibling(dictChildNode);
					//array
					Log.i("testXml",  dictChildNode.getNodeName() + " : ");
					//array
					Node dictNode1 = dictChildNode.getChildNodes().item(19);
					Log.i("testXml",  dictNode1.getNodeName() + " : ");

					ApnProfile pp = (ApnProfile) profile.createProfile(ProfileType.Profile_APN);
					if (pp == null)
						return;
					parserDictNode(pp,dictNode1);
					pp.saveProfile(mContext);

				}
				else
				{

				}

			}
			dictChildNode = get_nextSibling(dictChildNode);
		}while(dictChildNode != null);
	}
}
