package wu.a.lib.net;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * android中判断网络连接是否可用
 * 
 * @author Administrator
 *
 */
public class NetWorkUtils {
	
	/**是否联网
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo != null) {
			return networkInfo.isAvailable() && networkInfo.isConnected();
		} else {
			return false;
		}
	}
	
	/**
	 * @param context
	 * @return @see #android.net.NetworkInfo.getType()
	 */
	public static int getAvailableNetWorkType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo != null&&networkInfo.isAvailable() && networkInfo.isConnected()) {
			return networkInfo.getType();
		}
		return -1;
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = mgrConn.getActiveNetworkInfo();
		return ((networkINfo != null && networkINfo.getState() == NetworkInfo.State.CONNECTED) && networkINfo
				.getType() == ConnectivityManager.TYPE_WIFI);
	}
	
	/**是否移动网络连接
	 * @param context
	 * @return
	 */
	public static boolean isMobileNetWorkConnected(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = mgrConn.getActiveNetworkInfo();
		return ((networkINfo != null && networkINfo.getState() == NetworkInfo.State.CONNECTED) && networkINfo
				.getType() == ConnectivityManager.TYPE_MOBILE);
	}
	/**
	 * 判断网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			// 如果仅仅是用来判断网络连接
			// 则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}

		}
		return false;
	}

	/**
	 * 判断是否是3G网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkINfo = cm.getActiveNetworkInfo();
			if (networkINfo != null
					&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是wifi还是3g网络,用户的体现性在这里了，wifi就可以建议下载或者在线播放。
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkINfo = cm.getActiveNetworkInfo();
			return (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI);
		}
		return false;
	}
}