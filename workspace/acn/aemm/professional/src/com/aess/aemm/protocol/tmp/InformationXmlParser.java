package com.aess.aemm.protocol.tmp;
//package com.aess.aemm.protocol;
//
//import java.io.StringWriter;
//import java.util.ArrayList;
//import org.xmlpull.v1.XmlSerializer;
//import com.aess.aemm.commonutils.CommUtils;
//import com.aess.aemm.data.ProfileContent.GPSLocationColumns;
//import com.aess.aemm.data.ProfileContent.GPSLocationContent;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Xml;
//
///*
// * This file is mainly describe the app information including
// * install time and uninstall time ,location information
// * 
// * when hall login in successfully or location changed,
// * client should post this xml to server.
// */
//public class InformationXmlParser {
//
//	/*
//	 * if location changed ,parameter onlyLocation should be set true
//	 * otherwise false
//	 */
//	public String buildInformationXml(boolean onlyLocation)
//	{
//		
//		return null;
//
//	}
//	
//	private Context mContext = null;
//	
//	public InformationXmlParser(Context context) {
//		mContext = context;
//	}
//	
//	//ArrayList<AppStatusContent> appStateList = null;
//	ArrayList<GPSLocationContent> gpslist = null;
//	
//	public int deleteUploaded()
//	{
//		//if updated succeed,delete these records
//		
//		return 0;
//		
//	}
//	public int setUploaded()
//	{
//		//
//		return 0;
//		
//	}
//	
//	public String buildAppInfoXml()
//	{
//		StringBuilder xmlbody = null;
//		try {
//			StringWriter writer = new StringWriter();
//			XmlSerializer serializer = Xml.newSerializer();
//			serializer.setOutput(writer);
//			xmlbody = new StringBuilder();
//			xmlbody.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		SharedPreferences  pp = mContext.getSharedPreferences(CommUtils.PREF_NAME, 0);
//		String sid = pp.getString(CommUtils.KEY_CONFIG_SESSIONID, null);
//		String deviceId = CommUtils.getDeviceId(mContext);
//		
//		xmlbody.append(String.format("<request>\r\n<auth><session device-id=\"%s\" value=\"%s\" /></auth>\r\n", deviceId,sid));
//
//		//location
//		xmlbody.append("<locations>\r\n");
//		gpslist = GPSLocationContent.queryAllLocationContents(mContext);
//		if(gpslist == null){
//			return null;
//		}
//		for (GPSLocationContent gpsinfo : gpslist)
//		{
//			String location  = String.format("<location longitude=\"%s\" latitude=\"%s\" time=\"%s\" />\r\n", 
//					gpsinfo.mGPSLongitude,gpsinfo.mGPSLatitude,gpsinfo.mGPSTime);
//			
//			long id = gpsinfo.mId;
//			int isRead = 1;
//			ContentValues contentValue = gpsinfo.toContentValues();
//			contentValue.put(GPSLocationColumns.GPS_ISREAD, isRead);
//			GPSLocationContent.update(mContext, GPSLocationContent.CONTENT_URI, id, contentValue);
//			xmlbody.append(location);
//			//gpsinfo.mGPSIsRead = 1;
//			//GPSLocationContent.update(mContext,GPSLocationContent.CONTENT_URI, gpsinfo.mId,gpsinfo.toContentValues());
//			
//		}
//		xmlbody.append("</locations>\r\n</request>");
//		return xmlbody.toString();		
//	}
//	
//}
