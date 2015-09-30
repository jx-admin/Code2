package com.android.accenture.aemm.express;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;


public class AppListenerManager {
	public static final String LOGCAT="ducmentOperator";
	
	private Document document;
	private File file;
	private String defautFileName="/sdcard/applist.xml";
	private String rootTag="root";
	Element appListElement;
	public AppListenerManager(){
		init();
		//test();
		XmlUtils.writeToSDcard(document,file);
	}
	private void init(){
		file=new File(defautFileName);
		document=XmlUtils.loadFromSdcard(file);
		if(document==null){
			document=createDocument(rootTag);
		}
		//Log.v(LOGCAT,document.toString());
		checkNode();
	}
	
	private void checkNode(){
		NodeList links;
		//get node
		if(appListElement==null){
			links=document.getElementsByTagName("applist");
			if (links.getLength() > 0) { // 节点已存在
				appListElement=(Element) links.item(0);
			}else{
				links=document.getElementsByTagName(rootTag);
				Element rootElement=(Element) links.item(0);
				appListElement=XmlUtils.addNode(document, rootElement, "applist", null);
			}
		}
	}
	
	/**Create a Document object
	 * @return
	 */
	public Document createDocument(String rootNode){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc=null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			
			Node root = doc.createElement(rootNode);
//			添加根节点
			doc.appendChild(root);
			XmlUtils.writeToSDcard(doc,file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public void removeApp(String packageName){
		Element e=findElement(packageName,"id");
		e.getParentNode().removeChild(e);
	}
	public void addApp(String packageName){
		Element e=XmlUtils.addNode(document, appListElement, "app", null);
		e.setAttribute("id", packageName);
	}

	public void addEvent(String packageName,String state,long time){
		Element appElement=findElement(packageName, "id");
		if(appElement==null){
			appElement=XmlUtils.addNode(document, appListElement, "app", null);
			appElement.setAttribute("id", packageName);
		}
		Element eventElement=XmlUtils.addNode(document, appElement, "event",null);
		eventElement.setAttribute("type", state);
		eventElement.setAttribute("time",""+time);
	}
	
	public Element findElement(String packageName,String id){
		Element appElement=null;
		NodeList links;
		//find elment
		links=appListElement.getElementsByTagName("app");
		if (links.getLength() > 0) { // 节点已存在
			for (int i = 0; i < links.getLength(); i++) {
				Element nd = (Element) links.item(i);
				if(packageName.equals(nd.getAttribute(id))){
					appElement=nd;
					break;
				}
			}
		}
		return appElement;
	}
	public String getEvent(String packageName,String id){
		Element element=findElement(packageName,id);
		if (element == null)
			return null;
		NodeList links=element.getChildNodes();
		String result="";
		for(int i=0;i<links.getLength();i++){
			Element nd = (Element) links.item(i);
			result+="<event type=\""+nd.getAttribute("type")+"\" time=\""+nd.getAttribute("time")+"\"/>\n";
		}
		element.getParentNode().removeChild(element);
		XmlUtils.writeToSDcard(document,file);
		return result;
	}
	
	public void test(){
		
//		addEvent("com.android.aa","start",1000302);
		
//		removeApp("com.android.aa");
		
		addApp("com.android.appnew");
		addEvent("com.android.appnew","start",1000302);
		addEvent("com.android.appnew","start",1000302);
	}

}
