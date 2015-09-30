package com.android.accenture.aemm.express;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class XmlUtils {
	public static Document loadFromSdcard(File file){
		Document doc=null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(file);
		}catch(Exception e){
			e.printStackTrace();
		}
		return doc;
	}
	 public static Element addNode(Document doc,Node node,String name,String text){
		 Element childNode=doc.createElement(name);
			if(text!=null){
				childNode.appendChild(doc.createTextNode(text));
			}
			node.appendChild(childNode);
			return childNode;
	    }
	    public static boolean writeToSDcard(Document doc,File file){
	    	boolean result=true;
	    	try{
	    		TransformerFactory tf = TransformerFactory.newInstance(); 
			    Transformer tfer= tf.newTransformer();
			    DOMSource dsource = new DOMSource(doc); 
			    StreamResult sr = new StreamResult(file);
			    tfer.transform(dsource, sr);
	    	} catch (Exception e) {
	    		result=false;
				e.printStackTrace();
			} 
	    	return result;
	    }
}
