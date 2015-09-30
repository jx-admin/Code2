package com.apk.infos;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXmlResolve extends DefaultHandler{

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (qName.equals("manifest")) {
//			GetInfo getInfo=new GetInfo();
			for (int i = 0; i < attributes.getLength(); i++) {
				String strQname = attributes.getQName(i);
				if (strQname.equals("package")) {
					GetInfo.strpackage = attributes
							.getValue(strQname);
				}
				if (strQname.equals("android:versionName")) {
					GetInfo.strversion = attributes
							.getValue(strQname);
				}
				if (strQname.equals("android:versionCode")) {
					GetInfo.strVersionCode = attributes
							.getValue(strQname);
				}
			}
		}
		super.startElement(uri, localName, qName, attributes);
	}

}
