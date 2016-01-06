package com.android.test.wifi;

import android.annotation.SuppressLint;
import android.net.NetworkInfo;

/**
 * @author Administrator
 * 
 *         <pre>
 *         连接管理： ConnectivityManager connectionManager = (ConnectivityManager)
 *         getSystemService(CONNECTIVITY_SERVICE); //获取网络的状态信息，有下面三种方式
 *         NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
 *         NetworkInfo wifiInfo =
 *         connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
 *         NetworkInfo mobileInfo =
 *         connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
 *         getDetailedState()：获取详细状态。
 *         getExtraInfo()：获取附加信息。
 *         getReason()：获取连接失败的原因。
 *         getType()：获取网络类型(一般为移动或Wi-Fi)。
 *         getTypeName()：获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
 *         isAvailable()：判断该网络是否可用。
 *         isConnected()：判断是否已经连接。
 *         isConnectedOrConnecting()：判断是否已经连接或正在连接。
 *         isFailover()：判断是否连接失败。
 *         isRoaming()：判断是否漫游。
 *         </pre>
 */
public class NetworkInfoWrap {

	@SuppressLint("NewApi")
	public static String toString(NetworkInfo networkInfo) {
		if (networkInfo == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("NetworkInfo:").append('\n');
		sb.append("isAvailable:").append(networkInfo.isAvailable()).append('\n');
		sb.append("isConnected:").append(networkInfo.isConnected()).append('\n');
		sb.append("isConnectedOrConnecting:").append(networkInfo.isConnectedOrConnecting()).append('\n');
		sb.append("getTypeName:").append(networkInfo.getTypeName()).append('\n');
		sb.append("getType:").append(networkInfo.getType()).append('\n');
		sb.append("getDetailedState:").append(networkInfo.getDetailedState()).append('\n');
		sb.append("getExtraInfo:").append(networkInfo.getExtraInfo()).append('\n');
		sb.append("isFailover:").append(networkInfo.isFailover()).append('\n');
		sb.append("getReason:").append(networkInfo.getReason()).append('\n');
		sb.append("isRoaming:").append(networkInfo.isRoaming()).append('\n');
		return sb.toString();
	}

}
