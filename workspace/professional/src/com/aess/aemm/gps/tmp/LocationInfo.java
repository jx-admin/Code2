package com.aess.aemm.gps.tmp;
//package com.aess.aemm.gps;
//
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import com.aess.aemm.data.ProfileContent.GPSLocationContent;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.telephony.TelephonyManager;
//import android.telephony.cdma.CdmaCellLocation;
//import android.telephony.gsm.GsmCellLocation;
//import android.util.Log;
//
//@Deprecated
//public class LocationInfo {
//	public static final String LOGCAT="LOCATION";
//	public static final String REPORT_LOCATION_CHANGED = "report_location_changed";
//	private LocationManager locationManager;
//	private android.location.LocationListener llistener;
//	private String provider;
//	private Context context;
//	private double longitude;
//	private double latitude;
//	private String nowTime;
////	private static final double LATITUDE_DISC = 0.006148;
////	private static final double LONGITUDE_DISC = 0.013051;
//	
//	private LocationListener gpsListener=null;
//	private LocationListener networkListner=null;
//	
//	public LocationInfo(Context context){
//		this.context=context;
//	}
//	
//	public void onCreate(){
////		final IntentFilter filter = new IntentFilter();
////		filter.addAction(LOCATION_CHANGED);
////		filter.addAction(LOCATION_LOGIN);
////        context.registerReceiver(mReceiver, filter);
//	}
//	
//	public void onDestroy() {
//		if (provider != null) {
//			locationManager.removeUpdates(llistener);
//			Settings.System.putString(context.getContentResolver(),
//					Settings.System.LOCATION_PROVIDERS_ALLOWED, "");
//		}
////		context.unregisterReceiver(mReceiver);
//	}
//	
//	/*
//	 * Initial GPS location manager.
//	 */
//	public void init(){		
//		Log.i("location","init");
//		Settings.Secure.putString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED, "network,gps");
//		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
//		criteria.setAltitudeRequired(false);
//		criteria.setBearingRequired(false);
//		criteria.setCostAllowed(true);
//		criteria.setPowerRequirement(Criteria.POWER_LOW);
//		provider = locationManager.getBestProvider(criteria, true);
//		if (provider == null) {
//			AlertDialog.Builder builder = new Builder(context);
//			builder.setMessage("GPS unable");
//			builder.setPositiveButton(android.R.string.ok,
//					new OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			builder.create().show();
//		}
//		
//		Location location = locationManager.getLastKnownLocation(provider);
//		if (location != null) {
//			Log.d(LOGCAT, "Get Location From GPS");
//			update(location);
//			saveNewLocationInfo();
//			println("lastKnownLocation");
//			println("longitude:"+ location.getLongitude());
//			println("latitude:"+ location.getLatitude());
//			println("altitude:"+ location.getAltitude());
//		} else {
//			Log.d(LOGCAT, "Get Location From Internet");
//			TelephonyManager tm  = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
//			GsmCellLocation gcl = (GsmCellLocation)tm.getCellLocation();
//			if (null != gcl) {
//				final int cellId = gcl.getCid();
//				final int locationAreaCode = gcl.getLac();
//				int primaryScramblingCode  = gcl.getPsc();
//				Log.d(LOGCAT, "GSM BSC DATA : ");
//				Log.d(LOGCAT, "Cell Id " + cellId);
//				Log.d(LOGCAT, "Location Code " + locationAreaCode);
//				Log.d(LOGCAT, "Primary Code " + primaryScramblingCode);
//				
//			    new Thread(new Runnable() {
//					   public void run() {
//						   getGPSInfo(cellId,locationAreaCode);
//				}}).start();
//			} else {
//				Log.d(LOGCAT, "GSM isn't open");
//			}
//		}
//
//		gpsListener = new MyLocationListner();
//		networkListner = new MyLocationListner();
//		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, (float) 100.0,networkListner);
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, (float) 100.0,gpsListener);
//
//	}
//    
//	public class CellIDInfo {
//		public int cellId;
//		public String mobileCountryCode;
//		public String mobileNetworkCode;
//		public int locationAreaCode;
//		public String radioType;
//	}
//	
//	private CellIDInfo getCellIDInfo() {
//		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//		int type = tm.getNetworkType();
//		CellIDInfo info = null;
//		
//		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A || type == TelephonyManager.NETWORK_TYPE_CDMA || type ==TelephonyManager.NETWORK_TYPE_1xRTT){
//            info = new CellIDInfo();
//            CdmaCellLocation location = (CdmaCellLocation)tm.getCellLocation();
//            info.cellId = location.getBaseStationId();
//            info.locationAreaCode = location.getNetworkId();;
//            info.mobileNetworkCode = String.valueOf(location.getSystemId());
//            info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
//            info.radioType = "cdma";
//		} else if(type == TelephonyManager.NETWORK_TYPE_EDGE) {
//			info = new CellIDInfo();
//			GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();
//            info.cellId = location.getCid();
//            info.locationAreaCode = location.getLac();
//            info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);
//            info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
//            info.radioType = "gsm";
//		} else if(type == TelephonyManager.NETWORK_TYPE_GPRS) {
//			info = new CellIDInfo();
//			GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();  
//            info.cellId = location.getCid(); 
//            info.locationAreaCode = location.getLac();
//            //ç»�è¿‡æµ‹è¯•ï¼ŒèŽ·å�–è��?é€šæ•°æ�®ä»¥ä¸‹ä¸¤è¡Œå¿…é¡»åŽ»æŽ‰ï¼Œå�¦åˆ™ä¼šå‡ºçŽ°é�?™è¯¯ï¼Œé�?™è¯¯ç±»åž‹ä¸ºJSON Parsing Error
//            //info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);   
//            //info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
//            info.radioType = "gsm";
//		}
//		return info;
//	}
//	
//	private Location getLocationByNet(CellIDInfo info) {
//		Location loc = null;
//		
//		try {
//			String Send = getSendString(info);
//			
//			StringBuffer sb = getHttpResponse(Send);
//			if(sb.length() >= 1) {
//				JSONObject data = new JSONObject(sb.toString());
//				data = (JSONObject) data.get("location");
//
//				loc = new Location(LocationManager.NETWORK_PROVIDER);
//				loc.setLatitude((Double) data.get("latitude"));
//				loc.setLongitude((Double) data.get("longitude"));
//				loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		return loc;
//	}
//
//	private StringBuffer getHttpResponse(String Send) throws UnsupportedEncodingException,
//			IOException, ClientProtocolException {
//    	DefaultHttpClient client = new DefaultHttpClient();
//		HttpPost post = new HttpPost("http://www.google.com/loc/json");
//		
//		StringEntity se = new StringEntity(Send);
//		post.setEntity(se);
//		HttpResponse resp = client.execute(post);
//		HttpEntity entity = resp.getEntity();
//		BufferedReader br = new BufferedReader(
//				new InputStreamReader(entity.getContent()));
//		StringBuffer sb = new StringBuffer();
//		String result = br.readLine();
//		while (result != null) {
//			sb.append(result);
//			result = br.readLine();
//		}
//		Log.d(LOGCAT, "Read Date " + sb.toString());
//		return sb;
//	}
//
//	private String getSendString(CellIDInfo info) throws JSONException {
//		JSONObject holder = new JSONObject();
//		holder.put("version", "1.1.0");
//		holder.put("host", "maps.google.com");
//		holder.put("home_mobile_country_code", info.mobileCountryCode);
//		holder.put("home_mobile_network_code", info.mobileNetworkCode);
//		holder.put("radio_type", info.radioType);
//		holder.put("request_address", true);
//		if ("460".equals(info.mobileCountryCode)) {
//			holder.put("address_language", "zh_CN");
//		} else {
//			holder.put("address_language", "en_US");
//		}
//		
//		JSONObject cell_towers;
//		JSONArray array = new JSONArray();
//		cell_towers = new JSONObject();
//		cell_towers.put("cell_id", info.cellId);
//		cell_towers.put("location_area_code", info.locationAreaCode);
//		cell_towers.put("mobile_country_code", info.mobileCountryCode);
//		cell_towers.put("mobile_network_code", info.mobileNetworkCode);
//		cell_towers.put("age", 0);
//		array.put(cell_towers);
//		
//		holder.put("cell_towers", array);
//		Log.d(LOGCAT, "Send Date " + holder.toString());
//		return holder.toString();
//	}
//	
//	private boolean getGPSInfo(int cellID, int lac) {
//		boolean rlt = false;
//		Location loc = null;
//		
//		CellIDInfo info = getCellIDInfo();
//		if (null != info) {
//			loc = getLocationByNet(info);
//			if (null != loc) {
//				longitude = loc.getLongitude();
//				latitude = loc.getLatitude();
//				saveNewLocationInfo();
//			}
//
//		} else {
//			Log.d(LOGCAT, "Cell ID Not Found");
//		}
//		return rlt;
//	}
//	
////	private boolean displayMap(int cellID, int lac) throws Exception 
////    {
////        String urlString = "http://www.google.com/glm/mmap";
////		Log.d(LOGCAT, "GPS SP " + urlString);
////		
////        //---open a connection to Google Maps API---
////        URL url = new URL(urlString); 
////        URLConnection conn = url.openConnection();
////        HttpURLConnection httpConn = (HttpURLConnection) conn;        
////        httpConn.setRequestMethod("POST");
////        httpConn.setDoOutput(true); 
////        httpConn.setDoInput(true);
////		httpConn.setConnectTimeout(ProtocolDownload.CONNECT_TIMEOUT);
////		httpConn.setReadTimeout(ProtocolDownload.READ_TIMEOUT);
////        httpConn.connect(); 
////        
////        if (HttpURLConnection.HTTP_OK == httpConn.getResponseCode()) {
////        	//---write some custom data to Google Maps API---
////            OutputStream outputStream = httpConn.getOutputStream();
////            WriteData(outputStream, cellID, lac);       
////            
////            //---get the response---
////            InputStream inputStream = httpConn.getInputStream();  
////            DataInputStream dataInputStream = new DataInputStream(inputStream);
////            
////            //---interpret the response obtained---
////            dataInputStream.readShort();
////            dataInputStream.readByte();
////            int code = dataInputStream.readInt();
////            if (code == 0) {
////            	latitude = (double) dataInputStream.readInt() / 1000000D;
////                longitude = (double) dataInputStream.readInt() / 1000000D;
////                //use for baidu map
////                //latitude = latitude + LATITUDE_DISC;
////                //longitude = longitude + LONGITUDE_DISC;
////                dataInputStream.readInt();
////                dataInputStream.readInt();
////                dataInputStream.readUTF();
////                println("latitude is "+latitude+" langitude is "+longitude);
////    			saveNewLocationInfo();
////                return true;
////            } else {
////            	Log.d(LOGCAT, "Location result is error");
////            	return false;
////            }
////        } else {
////        	Log.d(LOGCAT, "HTTP Error " + httpConn.getResponseCode());
////			return false;
////        }
////    }  
//	
//
////	private void WriteData(OutputStream out, int cellID, int lac) throws IOException
////    {   	
////        DataOutputStream dataOutputStream = new DataOutputStream(out);
////        dataOutputStream.writeShort(21);
////        dataOutputStream.writeLong(0);
////        dataOutputStream.writeUTF("en");
////        dataOutputStream.writeUTF("Android");
////        dataOutputStream.writeUTF("1.0");
////        dataOutputStream.writeUTF("Web");
////        dataOutputStream.writeByte(27);
////        dataOutputStream.writeInt(0);
////        dataOutputStream.writeInt(0);
////        dataOutputStream.writeInt(3);
////        dataOutputStream.writeUTF("");
////
////        dataOutputStream.writeInt(cellID);  
////        dataOutputStream.writeInt(lac);     
////
////        dataOutputStream.writeInt(0);
////        dataOutputStream.writeInt(0);
////        dataOutputStream.writeInt(0);
////        dataOutputStream.writeInt(0);
////        dataOutputStream.flush();    	
////    }
//	
//	private void update(Location location){
//		longitude=location.getLongitude();
//		latitude=location.getLatitude();
//	}
//	
//	private void println(String info) {
//		Log.v(LOGCAT, "v:" + info);
//	}
//	
//	public double getLongitude(){
//		return longitude;
//	}
//	
//	public double getLatitude(){
//		return latitude;
//	}
//	
//	/*
//	 * get now time with the format, "YY-MM-DD hh:mm:ss"
//	 */
//	private String getDataTime(){
//		Date now = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String time = sdf.format(now);
//		return time;
//	}
//	
//	/*
//	 * save new location in database.
//	 * if save success, send message for sending the data to service.
//	 */
//	private void saveNewLocationInfo(){
//		nowTime = getDataTime();
//		GPSLocationContent gpsdb = new GPSLocationContent();
//		gpsdb.mGPSLongitude = String.format("%f", longitude);
//		gpsdb.mGPSLatitude = String.format("%f", latitude);
//		gpsdb.mGPSTime = nowTime;
//		Uri uri = gpsdb.save(this.context);
//		if(uri != null){
//	     	//CommUtils.setGPS(context, true);
//		}
//	}
//	
//	/*
//	 * create broadcast receiver for receive "LOCATION_CHANGER" and "HALL_LOGIN" message.
//	 */
////	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
////        @Override
////        public void onReceive(Context context, Intent intent) {
//////        	new LocationThread(intent).start();
//////        	Intent gpsintent = new Intent();
//////        	gpsintent.setAction(LOCATION_CHANGED);
//////        	context.sendBroadcast(gpsintent)
////        }
////	};
////	private class LocationThread extends Thread{
//////		private Intent intent;
////		public LocationThread(Intent intent){
//////			this.intent=intent;
////		}
////		public void run(){
//////			final String action = intent.getAction();
//////            String locationChangedReqXml = "";
//////            boolean hasUpdated = false;
//////        	InformationXmlParser infoXmlPaser = new InformationXmlParser(context);
//////            if(LOCATION_CHANGED.equals(action)){
//////                locationChangedReqXml = infoXmlPaser.buildAppInfoXml();
//////                hasUpdated = ProtocolDownload.updateData(context, Update.UPDATE_URL, locationChangedReqXml);
//////                if(hasUpdated){
//////                	GPSLocationContent.deleteAllGPSLocationBeRead(context);
//////                }
//////            }else if(LOCATION_LOGIN.equals(action)){
//////            	locationChangedReqXml = infoXmlPaser.buildAppInfoXml();
//////            	hasUpdated = ProtocolDownload.updateData(context, Update.UPDATE_URL, locationChangedReqXml);
//////            	if(hasUpdated){
//////                	GPSLocationContent.deleteAllGPSLocationBeRead(context);
//////            	}
//////            }     
////		}
////	}
//	
//	private class MyLocationListner implements LocationListener{
//		public void onLocationChanged(android.location.Location location) {
//			Log.d(LOGCAT, "on Location Changed");
//			if (location != null) {
//				update(location);
//				saveNewLocationInfo();
//			}
//		}
//
//		public void onProviderDisabled(String provider) {
//			println("onProviderDisabled");
//		}
//
//		public void onProviderEnabled(String provider) {
//			println("onProviderEnabled");
//		}
//
//		public void onStatusChanged(String provider, int status,Bundle extras) {
//			println("onStatusChanged: "+provider+" "+status+" "+extras);
//		}
//	};
//}
