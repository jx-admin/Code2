package com.aess.aemm.networkutils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;


public class ImageGet {
	
	public static final String DEBUGFILE = "image.xml";

	public static List<String> getImage(Context cxt, String uri) {

		InputStream is = HttpHelp.aemmHttpGet(cxt, uri, DEBUGFILE);
		
		List<String> imageUriList = imageParser(is);
		return imageUriList;
	}
	
	private static List<String> imageParser(InputStream is) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		List<String> imagelist = new ArrayList<String>();
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(is);
			Element root = dom.getDocumentElement();
			if (null != root) {
				NodeList nl = root.getElementsByTagName("snapshot");
				if (null != nl) {
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						String tt = e.getAttribute("download-url");
						if (null != tt && tt.length() > 0) {
							imagelist.add(tt);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return imagelist;
	}
}
