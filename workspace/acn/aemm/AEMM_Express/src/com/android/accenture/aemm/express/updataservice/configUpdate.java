package com.android.accenture.aemm.express.updataservice;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

import com.android.accenture.aemm.express.log.ServiceLog;

import android.util.Log;

/**
 * 
 * 使用Dom解析xml文件
 * 
 * 
 */

public class configUpdate {

	
	public static void processConfigUpdate(String urlStr, String language) {
		DownloadCfgProfile(urlStr, language);
	}

	static InputStream DownloadCfgProfile(String urlStr, String language) {
		InputStream input = null;
		HttpURLConnection conn = null;
		try {
			/*
			 * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
			 * <uses-permission android:name="android.permission.INTERNET" />
			 */
			URL url = new URL(urlStr);
			Log.i("Download", "1 ");
			conn = (HttpURLConnection) url.openConnection();
			Log.i("Download", "2 ");
			if (null != language) {
				language = "zh";
			}
			conn.setRequestProperty("Accept-Language", language);
			
			// 取得inputStream，并进行读取
			//InputStream input = conn.getInputStream();
			Log.i("Download", "3 ");
			
			// 获得响应状态
			int responseCode = conn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
				Log.i("HttpURL","HTTP ok");
				String line="";
				StringBuffer buffer=new StringBuffer("");
				InputStream l_urlStream;    
		        l_urlStream = conn.getInputStream();    
		        BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));   
		        while ((line = l_reader.readLine()) != null) {
		             buffer.append(line);
		             //Log.i("response",line);
		             //ServiceLog.writeLog(line);
		         } 
		        input = new ByteArrayInputStream(buffer.toString().getBytes());   

			}
			else
			{
				Log.i("HttpURL","HTTP error :" + String.valueOf(responseCode));
			}
			 
			return input;
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			//need to close connection
			if (conn != null)
				conn.disconnect();
		}
		return input;

	}

	private static void readConfigXML(InputStream inStream) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document dom = builder.parse(inStream);

			Element root = dom.getDocumentElement();

			NodeList dicts = root.getElementsByTagName("dict");// 查找所有dict节点

			Log.i("DomXMLReader", "DomXMLReader" + " : " + "dict count = "
					+ dicts.getLength());

			for (int i = 0; i < dicts.getLength(); i++) {
				Node dictNode = (Node) dicts.item(i);
				Log.i("DomXMLReader", "DomXMLReader" + " : "
						+ "dict node Type = " + dictNode.getNodeType());
				Log.i("DomXMLReader", "DomXMLReader" + " : "
						+ "dict node name = " + dictNode.getNodeName());

				parserDictNode(dictNode);
			
			}

			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	private static void parserDictNode(Node headDictNode) {

		NodeList dictChilds = headDictNode.getChildNodes();
		for (int j = 0; j < dictChilds.getLength(); j++) {
			Node dictChildNode = (Node) dictChilds.item(j);

			if ("key".equals(dictChildNode.getNodeName())) {
				Log.i("DomXMLReader", "DomXMLReader" + " : " + "key = "
						+ dictChildNode.getFirstChild().getNodeValue());

				if ("PayloadIdentifier".equals(dictChildNode.getFirstChild()
						.getNodeValue())) {
					Node dictIdkeyNode = dictChildNode;
					Node dictIdStringNode = dictIdkeyNode.getNextSibling()
							.getNextSibling();

					Log.i("DomXMLReader", "DomXMLReader" + " : "
							+ "key's next next node name = "
							+ dictIdStringNode.getNodeName());
					Log.i("DomXMLReader", "DomXMLReader" + " : "
							+ "key's next next node content = "
							+ dictIdStringNode.getFirstChild().getNodeValue());
				}
			}
		}
	}

}
