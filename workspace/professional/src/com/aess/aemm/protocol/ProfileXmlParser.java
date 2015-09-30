package com.aess.aemm.protocol;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.aess.aemm.R;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.setting.Profile;
import com.aess.aemm.setting.Profile.ProfileType;
import com.aess.aemm.setting.WebClipProfile;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/*
 * This class parse profile xml
 */
@SuppressLint("UseSparseArrays")
public class ProfileXmlParser {
	public static final String TAG = "ProfileXmlParser";

	// profile id
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

	// node
	String node_key = "key";
	String node_dict = "dict";
	String node_array = "array";
	String node_value_profileIndentifier = "PayloadIdentifier";
	String node_value_profileType = "PayloadType";
	String node_value_profileVersion = "PayloadVersion";
	String node_value_profileContent = "PayloadContent";

	Context mContext;
	boolean configvpn;

	String profileId;
	String RootVersion;
	String RootType;
	String RootIndentifier;

	public ProfileXmlParser(Context context) {
		mContext = context;
	}

	private Node get_nextSibling(Node node) {
		Node next = null;
		if (node.getNextSibling() != null) {
			next = node.getNextSibling();
		} else {
			return null;
		}
		
		while (next.getNodeType() != Node.ELEMENT_NODE) {
			if (next.getNextSibling() != null) {
				next = next.getNextSibling();
			} else {
				return null;
			}
		}
		return next;
	}

	private ProfileType getDictType(Node rootDictNode) {
		ProfileType type = ProfileType.Profile_NONE;
		// parse the first dictNode
		NodeList dictChilds = rootDictNode.getChildNodes();
		int j = 0;
		Node dictChildNode = (Node) dictChilds.item(j);
		do {

			// key
			if (node_key.equals(dictChildNode.getNodeName())) {
				String nodeValue = dictChildNode.getTextContent();

				if (node_value_profileType.equals(nodeValue)) {

					// type
					// get_nextsibing to get value
					dictChildNode = get_nextSibling(dictChildNode);
					String profileValue = dictChildNode.getTextContent();
					if (vpn_profile.equals(profileValue)) {
						type = ProfileType.Profile_VPN;

					} else if (webclip_profile.equals(profileValue)) {
						type = ProfileType.Profile_WebClip;
					} else if (password_profile.equals(profileValue)) {
						type = ProfileType.Profile_Password;

					} else if (apn_profile.equals(profileValue)) {
						type = ProfileType.Profile_APN;

					} else if (passpolicy_profile.equals(profileValue)) {
						type = ProfileType.Profile_PassPolicy;

					} else if (wifi_profile.equals(profileValue)) {
						type = ProfileType.Profile_Wifi;

					} else if (email_profile.equals(profileValue)) {
						type = ProfileType.Profile_Email;

					} else if (exchange_profile.equals(profileValue)) {
						type = ProfileType.Profile_Exchange;

					} else if (rootCertificate_profile.equals(profileValue)) {
						type = ProfileType.Profile_RootCertificate;
					} else if (pkcsCertificate_profile.equals(profileValue)) {
						type = ProfileType.Profile_PkcsCertificate;
					} else if (restriction_profile.equals(profileValue)) {
						type = ProfileType.Profile_Restrictions;
					}

					return type;
				}

			}
			dictChildNode = get_nextSibling(dictChildNode);

		} while (dictChildNode != null);
		return null;

	}

	private int getRootDictType(Node rootDict) {
		int ret = -1;

		// dict
		if (node_dict.equals(rootDict.getNodeName())) {

			Node dictChildNode = (Node)rootDict.getChildNodes().item(0);
			do {
				// key
				if (node_key.equals(dictChildNode.getNodeName())) {
					String keyValue = dictChildNode.getTextContent();
					if (keyValue.equals(node_value_profileContent)) {
						ret = 0;
						dictChildNode = get_nextSibling(dictChildNode);
						continue;
					} else if (keyValue.equals(node_value_profileVersion)) {
						dictChildNode = get_nextSibling(dictChildNode);
					} else if (keyValue.equals(node_value_profileType)) {
						dictChildNode = get_nextSibling(dictChildNode);
					} else if (keyValue.equals(node_value_profileIndentifier)) {
						dictChildNode = get_nextSibling(dictChildNode);
//						if("com.accenture.profile.vpnconfig".equals(dictChildNode.getTextContent())) {
//							if (!AEMMConfig.AUTO.equals(AEMMConfig.SIGN)) {
//								configvpn = true;
//							}
//							
//						}
					}

				}
				dictChildNode = get_nextSibling(dictChildNode);
			} while (dictChildNode != null);
		}

		// get the profile version version from preference file
		SharedPreferences pp = mContext.getSharedPreferences(
				CommUtils.PREF_NAME, 0);
		
		String novalue = "-1";
		String value = pp.getString(this.profileId, novalue);

		if (value != null) {
			RootVersion = this.profileId + value;
		}
		return ret;
	}

	private String parseChildDicts(Node rootDict, UpdateResult result) {
		int ret = -1;
		
		StringBuilder hallMessage = new StringBuilder(mContext
				.getText(R.string.update_over));
		
		hallMessage.append(": ");

		boolean bhasError = false;
		boolean bHasOK = false;

		ArrayList<ProfileType> allProfileList = newProfileList();

		if (node_dict.equals(rootDict.getNodeName())) {

			HashMap<Integer, ProfileType> items = new HashMap<Integer, ProfileType>();

			Node dictChildNode = (Node) rootDict.getChildNodes().item(0);
			do {
				// key
				if (node_key.equals(dictChildNode.getNodeName())) {
					
					String keyValue = dictChildNode.getTextContent();
					
					if (keyValue.equals(node_value_profileContent)) {
						
						dictChildNode = get_nextSibling(dictChildNode);

						int length = dictChildNode.getChildNodes().getLength();
						boolean isWebClipType = false;
						for (int i = 1; i < length; i += 2) {
							Node dictNode = dictChildNode.getChildNodes().item(
									i);

							ProfileType type = getDictType(dictNode);
							allProfileList.remove(type);

							if ((type == ProfileType.Profile_Wifi)
									|| (type == ProfileType.Profile_VPN)) {

								items.put(i, type);

//								if (type == ProfileType.Profile_VPN) {
//									if (!AEMMConfig.AUTO.equals(AEMMConfig.SIGN)) {
//										allProfileList.clear();
//									}
//								}
								
								continue;
							}
							if(type == ProfileType.Profile_WebClip){ 
								isWebClipType = true;
							}
							Profile pp = Profile.createProfile(type);
							if (pp == null) {
								continue;
							}

							parserDictNode(pp, dictNode);

							try {
								
								if (type == ProfileType.Profile_VPN) {
									pp.setVersion(result.mProfileVersion);
//									if (AEMMConfig.AUTO.equals(AEMMConfig.SIGN)) {
//										pp.setVersion(result.mProfileVersion);
//									} else {
//										pp.setVersion(result.mVpnProfileVersion);
//									}
								} else {
									pp.setVersion(result.mProfileVersion);
								}

								ret = pp.saveProfile(mContext);
							} catch(Exception e) {
								e.printStackTrace();
								ret = 1;
							}

							if (ret >= 0) {
								bHasOK = true;
								String tStr = getMessageString(mContext, type);
								if (hallMessage.indexOf(tStr) < 0) {
									hallMessage.append(tStr);
									hallMessage.append(",");
								}
							} else {
								bhasError = true;
							}
						}
						if(isWebClipType){
							WebClipProfile.refreshWebClipShort(mContext);
							isWebClipType = false;
						}
						break;
					}
				}
				dictChildNode = get_nextSibling(dictChildNode);
			} while (dictChildNode != null);

			// check if has wifi or vpn nodes
			Iterator<?> iter = items.entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry)iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				int index = (Integer) key;
				ProfileType type = (ProfileType) val;

				Node node = (Node) dictChildNode.getChildNodes().item(index);

				Profile pp = Profile.createProfile(type);
				if (pp == null)
					continue;

				parserDictNode(pp, node);
				
//				if (type == ProfileType.Profile_VPN) {
//					if (AEMMConfig.AUTO.equals(AEMMConfig.SIGN)) {
//						pp.setVersion(result.mProfileVersion);
//					} else {
//						pp.setVersion(result.mVpnProfileVersion);
//					}
//				} else {
//					pp.setVersion(result.mProfileVersion);
//				}
				pp.setVersion(result.mProfileVersion);

				ret = pp.saveProfile(mContext);
				
				if (ret >= 0) {
					bHasOK = true;
					hallMessage.append(getMessageString(mContext, type));
					hallMessage.append(",");
				} else {
					bhasError = true;
				}
			}

			for (ProfileType ptype : allProfileList) {
				Profile pp = Profile.createProfile(ptype);
				if (pp != null) {
					pp.clearProfile(mContext);
				}
			}
		}

		if (bHasOK) 
		{
			hallMessage.deleteCharAt(hallMessage.length() - 1);
		}
		if (bhasError)
		{
			hallMessage.append(mContext.getText(R.string.update_failed));
		}
		Log.i(TAG, hallMessage.toString());
		return hallMessage.toString();
	}

	private ArrayList<ProfileType> newProfileList() {
		ArrayList<ProfileType> profileList = new ArrayList<ProfileType>();

//		if(true != configvpn) {
//			for (ProfileType pt : ProfileType.values()) {
//				profileList.add(pt);
//			}
//			if (!AEMMConfig.AUTO.equals(AEMMConfig.SIGN)) {
//				profileList.remove(ProfileType.Profile_VPN);
//			}
//		} else {
//			profileList.add(ProfileType.Profile_VPN);
//		}
		return profileList;
	}

	public String parseProfileXml(InputStream inStream, UpdateResult result) {
		String resultStr = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();
			Node rootDict = root.getChildNodes().item(0);
			if (rootDict.getNodeType() != Node.ELEMENT_NODE) {
				rootDict = rootDict.getNextSibling();
			}
			// getRootDictType
			if (getRootDictType(rootDict) != 0) {
				return resultStr;
			}
			resultStr = parseChildDicts(rootDict, result);
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return resultStr;

	}

	public <T extends Profile> Profile parserDictNode(T profile, Node node) {

		Profile pp = profile;
		Log.i(TAG, node.getNodeName() + " : ");
		// dict
		NodeList dicts = node.getChildNodes();
		int j = 0;
		Node dictChildNode = (Node) dicts.item(j);
		String keyValue = null;
		String nodeValue = null;
		do {
			if (node_key.equals(dictChildNode.getNodeName())) {
				keyValue = dictChildNode.getTextContent();
			} else if ("string".equals(dictChildNode.getNodeName())) {
				nodeValue = dictChildNode.getTextContent();
				pp.setValue(keyValue, nodeValue);
				Log.i(TAG, keyValue + " : " + nodeValue);
			} else if ("data".equals(dictChildNode.getNodeName())) {

				nodeValue = dictChildNode.getTextContent();
				pp.setValue(keyValue, nodeValue);
				Log.i(TAG, keyValue + " : " + nodeValue);
			} else if ("integer".equals(dictChildNode.getNodeName())) {
				nodeValue = dictChildNode.getTextContent();
				pp.setValue(keyValue, nodeValue);
				Log.i(TAG, keyValue + " : " + nodeValue);
			} else if ("false".equals(dictChildNode.getNodeName())
					|| "true".equals(dictChildNode.getNodeName())) {
				nodeValue = dictChildNode.getNodeName();
				pp.setValue(keyValue, nodeValue);
				Log.i(TAG, keyValue + " : " + nodeValue);
			} else if (node_dict.equals(dictChildNode.getNodeName())) {
				if (keyValue != null) {
					// ticker
					if (keyValue.equals("EAPClientConfiguration"))
						pp.setValue(keyValue, nodeValue);
				}
				if (dictChildNode.getChildNodes() != null
						&& dictChildNode.getChildNodes().getLength() != 0)
					parserDictNode(pp, dictChildNode);
			} else if (node_array.equals(dictChildNode.getNodeName())) {
				Node arrayChild = dictChildNode.getChildNodes().item(1);
				if(arrayChild != null) {
					if (node_dict.equals(arrayChild.getNodeName())) {
						Log.i(TAG, dictChildNode.getTextContent());
						parserDictNode(pp, dictChildNode);
					} else {
						parseArray(profile, keyValue, dictChildNode);
					}
				}
			}
			dictChildNode = get_nextSibling(dictChildNode);
		} while (dictChildNode != null);
		return pp;

	}

	public <T extends Profile> void parseArray(Profile profile, String key, Node node) {
		NodeList nl = node.getChildNodes();
		for(int i = 0; i < nl.getLength(); i ++) {
			Node n = nl.item(i);
			String nodeName = n.getNodeName();
			if ("string".equals(nodeName)
				|| "integer".equals(nodeName)) {
				String value = n.getTextContent();
				profile.setValue(key, value);
			}
		}
	}
	
	public static String getMessageString(Context context, ProfileType type) {
		String str = null;
		switch (type) {
		case Profile_VPN:
			str = "Vpn";
			break;
		case Profile_WebClip:
			str = "WebClip";
			break;
		case Profile_PassPolicy:
			str = (String) context.getText(R.string.passpolicy);// "锟斤拷锟斤拷锟斤拷锟�;
			break;
		case Profile_APN:
			str = "Apn";
			break;
		case Profile_Wifi:
			str = "Wifi";
			break;
		case Profile_Email:
			str = "Email";
			break;
		case Profile_Exchange:
			str = "Exchange";
			break;
		case Profile_RootCertificate:
			str = (String) context.getText(R.string.rootcertificate);// "ca 证锟斤拷";
			break;
		case Profile_PkcsCertificate:
			str = (String) context.getText(R.string.pkcscertificate);// "psk 证锟斤拷";
			break;
		case Profile_Restrictions:
			str = (String) context.getText(R.string.restrictions);// "锟斤拷锟斤拷";
			break;
		default:
			break;
		}
		return str;

	}
}
