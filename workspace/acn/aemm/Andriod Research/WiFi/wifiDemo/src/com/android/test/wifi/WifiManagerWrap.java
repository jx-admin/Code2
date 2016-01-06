package com.android.test.wifi;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

/**
 * @author abc
 * 
 *         <pre>
 * 其实操作Wifi也是很简单的，主要使用以下几个对象或变量：
 * 
 * private WifiManager wifiManager;// 声明管理对象OpenWifi
 * private WifiInfo wifiInfo;// Wifi信息
 * private List&lt;ScanResult&gt; scanResultList; // 扫描出来的网络连接列表
 * private List&lt;WifiConfiguration&gt; wifiConfigList;// 网络配置列表
 * private WifiLock wifiLock;// Wifi锁
 * WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE);
 * 
 *         //获取Wi-Fi配置接口的属性 List wifiConfig =wifi_service.getConfiguredNetworks();
 *         wifiConfig中包含四个属性：
 *         BSSID：BSS是一种特殊的Ad-hoc LAN(一种支持点对点访问的无线网络应用模式)的应用，
 *         一个无线网络至少由一个连接到有线网络的AP和若干无线 工作站组成，这种配置称为一个基本服务装置。
 *         一群计算机设定相同的BSS名称，即可自成一个group，而此BSS名称，即所谓BSSID。
 *         通常，手 机WLAN中，bssid其实就是无线路由的MAC地址。
 *         networkid：网络ID。 PreSharedKey：无线网络的安全认证模式。
 *          SSID：SSID(Service SetIdentif)用于标识无线局域网，SSID不同的无线网 络是无法进行互访的。
 * 
 *         //系统服务参数和平台系统信息
 * 
 *         WifiInfo wifiinfo = wifi_service.getConnectionInfo();
 *         wifiinfo.getBSSID()：获取BSSIS(上面已说明)。
 *         wifiinfo.getSSID()：获取SSID(上面已说明)。
 *         wifiinfo.getIpAddress()：获取IP地址。
 *         wifiinfo.getMacAddress()：获取MAC地址。
 *         wifiinfo.getNetworkId()：获取网络ID。
 *         wifiinfo.getLinkSpeed()：获取连接速度，可以让用户获知这一信息。
 *         wifiinfo.getRssi()：获取RSSI，RSSI就是接受信号强度指示。在这可以直
 *         接和华为提供的Wi-Fi信号阈值进行比较来提供给用户，让用户对网络 或地理位置做出调整来获得最好的连接效果。
 * 
 *         //获取DHCP信息 DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
 *         ipAddress：获取IP地址。
 *         gateway：获取网关。 
 *         netmask：获取子网掩码。 
 *         dns1：获取DNS。
 *         dns2：获取备用DNS。 
 *         serverAddress：获取服务器地址。
 * 
 *         //获取扫描信息 List scanResult = wifi_service.getScanResults();
 *         BSSID：获取BSSID(上面已说明)。 
 *         SSID：获取网络名(上面已说明)。 
 *         level：获取信号等级。
 *         frequency：获取频率。 
 *         capabilites：对该访问点安全方面的描述。
 * 
 *         //获取Wi-Fi的网络状态 int wifiState = wifi_service.getWifiState();
 *         WIFI_STATE_DISABLING：常量0，表示停用中。
 *         WIFI_STATE_DISABLED：常量1，表示不可用。
 *         WIFI_STATE_ENABLING：常量2，表示启动中。
 *          WIFI_STATE_ENABLED：常量3，表示准备就绪。
 *         WIFI_STATE_UNKNOWN：常量4，表示未知状态。 
 *         说明：进行网络连接的时候，这些状态都会被显示在Notification上，
 *         直接可以通过此处获取各个状态来完成华为的Notification中Wi- Fi 状态显示的需求。
 * 
 * 当然操作Wifi不能在模拟器中进行，必须要放到带有Wifi的真机上进行，
 * 还有此类没有对可能存在的错误进行相应的捕获与处理，希望参考的朋友注意这一点，
 * 否则可能容易被突然出来的错误误导，也找不到问题，我在开发的时候就遇到过这样的问题！
 * 因此对可能存在的问题，一定要进行相应的处理！
 * 下面是操作这些所需要的权限，当然根据操作的内容不同，可能权限也不同，
 * 下面的权限仅供参考：
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 * <uses-permission android:name="adnroid.permission.ACCESS_CHECKIN_PROPERTTES"/>
 * <uses-permission android:name="android.permission.WAKE_LOCK"/>
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="adnroid.permission.CHANGE_WIFI_STATE"/>
 * <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>
 *         </pre>
 */
public class WifiManagerWrap {
	private static final String TAG = WifiManagerWrap.class.getSimpleName();
	// 声明管理对象
	WifiManager mWifiManager;
	// Wifi信息
	private WifiInfo mWifiInfo;

	private DhcpInfo mDhcpInfo;
	// 扫描出来的网络连接列表
	private List<ScanResult> scanResultList;
	// 网络配置列表
	private List<WifiConfiguration> wifiConfigList;
	// Wifi锁
	private WifiLock mWifiLock;

	public WifiManagerWrap(Context context) {
		// 获取Wifi服务
		this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 得到Wifi信息
		this.mWifiInfo = mWifiManager.getConnectionInfo();
		mDhcpInfo = mWifiManager.getDhcpInfo();
	}

	/** WIfi是否已经开启 */
	public boolean isWifiEnabled() {
		return mWifiManager.isWifiEnabled();
	}

	/** 打开wifi */
	public boolean openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			return mWifiManager.setWifiEnabled(true);
		} else {
			return true;
		}
	}

	/** 关闭 wifi */
	public boolean closeWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			return true;
		} else {
			return mWifiManager.setWifiEnabled(false);
		}
	}

	// 其实锁定WiFI就是判断wifi是否建立成功，在这里使用的是held，握手的意思acquire 得到！
	/** 锁定wifi */
	public void lockWifi() {
		mWifiLock.acquire();
	}

	/** 解锁wifi */
	public void unLockWifi() {
		if (!mWifiLock.isHeld()) {
			// 释放资源
			mWifiLock.release();
		}
	}

	/** Wifi 锁 */
	public WifiLock createWifiLock() {
		if (mWifiLock == null) {
			// 创建一个锁的标志
			mWifiLock = mWifiManager.createWifiLock("flyfly");
		}
		return mWifiLock;
	}

	/** 扫描网络 */
	public void startScan() {
		mWifiManager.startScan();
		// 扫描返回结果列表
		scanResultList = mWifiManager.getScanResults();
	}

	/** ScanResult List */
	public List<ScanResult> getScanResults() {
		scanResultList = mWifiManager.getScanResults();
		return scanResultList;
	}

	/** WifiConfiguration list */
	public List<WifiConfiguration> getConfiguredNetworks() {
		// 扫描配置列表
		wifiConfigList = mWifiManager.getConfiguredNetworks();
		return wifiConfigList;
	}

	/** 获取指定信号的强度 */
	public int getLevel(int NetId) {
		return scanResultList.get(NetId).level;
	}

	/** 获取本机Mac地址 */
	public String getMac() {
		return (mWifiInfo == null) ? "" : mWifiInfo.getMacAddress();
	}

	/** 获取BSSID */
	public String getBSSID() {
		return (mWifiInfo == null) ? null : mWifiInfo.getBSSID();
	}

	public String getSSID() {
		return (mWifiInfo == null) ? null : mWifiInfo.getSSID();
	}

	/** 返回当前连接的网络的ID */
	public int getCurrentNetId() {
		return (mWifiInfo == null) ? null : mWifiInfo.getNetworkId();
	}

	/** 返回WifiInfo信息 */
	public WifiInfo getwifiInfo() {
		return mWifiInfo;
	}

	/** 获取IP地址 */
	public int getIP() {
		return (mWifiInfo == null) ? null : mWifiInfo.getIpAddress();
	}

	/** 添加一个连接 */
	public boolean addNetWordLink(WifiConfiguration config) {
		int NetId = mWifiManager.addNetwork(config);
		mWifiManager.saveConfiguration();
		return mWifiManager.enableNetwork(NetId, true);
	}

	// WPA is not used; plaintext or static WEP could be used
	public static final int NONE = 0;
	public static final int WEP = 1;
	public static final int WEP_OLD = 11;// old

	// WPA pre-shared key (requires preSharedKey to be specified)
	public static final int WPA = 2;
	public static final int WPA2 = 21;// 待验证
	public static final int WPA_OLD = 22;// accenture

	// using EAP authentication. Generally used with an external authentication
	// server
	public static final int WEAP = 3;

	// using EAP authentication and dynamically generated WEP keys
	// public static final int IEEE8021X = 4;


	/**
	 * These values are matched in string arrays -- changes must be kept in sync
	 */
	static final int SECURITY_NONE = 0;
	static final int SECURITY_WEP = 1;
	static final int SECURITY_PSK = 2;
	static final int SECURITY_EAP = 3;

	static int getSecurity(WifiConfiguration config) {
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return SECURITY_PSK;
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
			return SECURITY_EAP;
		}
		return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
	}

	public int getCipherType(ScanResult scResult) {
		String capabilities = scResult.capabilities;
		Log.i("hefeng", "capabilities=" + capabilities);
		if (!TextUtils.isEmpty(capabilities)) {
			if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
				Log.i("hefeng", "wpa");
				return WPA;
			} else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
				Log.i("hefeng", "wep");
				return WEP;
			} else {
				Log.i("hefeng", "no");
				return NONE;
			}
		}
		return NONE;
	}
	
	public WifiConfiguration createWifiInfo(String SSID, String Password, int type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		switch (type) {
		case NONE:// WIFICIPHER_NOPASS
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
			break;
		case WEP:
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
			break;
		case WEP_OLD:
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			if (!TextUtils.isEmpty(Password)) {
				config.preSharedKey = "\"" + Password + "\"";
			}
			break;
		case WPA:
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
			break;
		case WPA2:
			if (!TextUtils.isEmpty(Password)) {
				config.preSharedKey = "\"" + Password + "\"";
			}
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.status = WifiConfiguration.Status.ENABLED;
			break;
		case WPA_OLD:
			if (!TextUtils.isEmpty(Password)) {
				config.preSharedKey = "\"" + Password + "\"";
			}
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			break;
		default:
		}
		return config;
	}

	/**
	 * 连接ap
	 * 
	 * @param sr
	 * @param pw
	 */
	public void connectToScanResult(ScanResult sr, String pw) {
		WifiConfiguration c = getWifiConfig(sr.SSID);
		if (c != null) {
			mWifiManager.enableNetwork(c.networkId, true);
		} else {
			// 如果没有输入密码 且配置列表中没有该WIFI
			/* WIFICIPHER_WPA 加密 */
			if (sr.capabilities.contains("WPA-PSK")) {
				// Log.i(TAG, "config----WPA-PSK");
				int netid = mWifiManager.addNetwork(createWifiInfo(sr.SSID, pw, WPA));
				mWifiManager.enableNetwork(netid, true);
			} else if (sr.capabilities.contains("WEP")) {
				/* WIFICIPHER_WEP 加密 */
				Log.i(TAG, "config----WEP");
				int netid = mWifiManager.addNetwork(createWifiInfo(sr.SSID, pw, WEP));
				mWifiManager.enableNetwork(netid, true);
			} else {
				/* WIFICIPHER_OPEN NOPASSWORD 开放无加密 */
				int netid = mWifiManager.addNetwork(createWifiInfo(sr.SSID, "", NONE));
				mWifiManager.enableNetwork(netid, true);

			}
		}
	}

	public static final int WIFI_CONNECTED = 0x01;
	public static final int WIFI_CONNECT_FAILED = 0x02;
	public static final int WIFI_CONNECTING = 0x03;

	public int isWifiContected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		Log.v(TAG, "isConnectedOrConnecting = " + wifiNetworkInfo.isConnectedOrConnecting());
		Log.d(TAG, "wifiNetworkInfo.getDetailedState() = " + wifiNetworkInfo.getDetailedState());
		if (wifiNetworkInfo.getDetailedState() == DetailedState.OBTAINING_IPADDR
				|| wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTING) {
			return WIFI_CONNECTING;
		} else if (wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTED) {
			return WIFI_CONNECTED;
		} else {
			Log.d(TAG, "getDetailedState() == " + wifiNetworkInfo.getDetailedState());
			return WIFI_CONNECT_FAILED;
		}

	}

	/** 禁用一个链接 */
	public boolean disableNetWordLick(int NetId) {
		mWifiManager.disableNetwork(NetId);
		return mWifiManager.disconnect();
	}

	/** 移除一个链接 */
	public boolean removeNetworkLink(int NetId) {
		return mWifiManager.removeNetwork(NetId);
	}

	/** 不显示SSID */
	public void hiddenSSID(int NetId) {
		wifiConfigList.get(NetId).hiddenSSID = true;
	}

	/** 显示SSID */
	public void displaySSID(int NetId) {
		wifiConfigList.get(NetId).hiddenSSID = false;
	}

	/** wifiConfigList 去指定的WifiConfiguration */
	public WifiConfiguration getWifiConfig(String ssid) {
		WifiConfiguration reWifiConfiguration = null;
		wifiConfigList = mWifiManager.getConfiguredNetworks();
		String findSsid = new StringBuilder().append("\"").append(ssid).append("\"").toString();
		if (wifiConfigList != null) {
			for (WifiConfiguration conf : wifiConfigList) {
				if (conf.SSID.equals(findSsid)) {
					reWifiConfiguration = conf;
					break;
				}
			}
		}
		return reWifiConfiguration;
	}

	/** WifiConfiguration复制 */
	public static WifiConfiguration copyWifiConfiguration(WifiConfiguration srcWifiConfiguration) {
		WifiConfiguration newWifiConfiguration = new WifiConfiguration();
		if (srcWifiConfiguration != null) {
			newWifiConfiguration.SSID = "new" + srcWifiConfiguration.SSID;
			newWifiConfiguration.preSharedKey = srcWifiConfiguration.preSharedKey;
			newWifiConfiguration.hiddenSSID = srcWifiConfiguration.hiddenSSID;
			newWifiConfiguration.networkId = srcWifiConfiguration.networkId;
			newWifiConfiguration.priority = srcWifiConfiguration.priority;
			newWifiConfiguration.BSSID = srcWifiConfiguration.BSSID;
			newWifiConfiguration.status = srcWifiConfiguration.status;
			newWifiConfiguration.wepKeys = srcWifiConfiguration.wepKeys;
			newWifiConfiguration.wepTxKeyIndex = srcWifiConfiguration.wepTxKeyIndex;

			newWifiConfiguration.allowedAuthAlgorithms = srcWifiConfiguration.allowedAuthAlgorithms;
			newWifiConfiguration.allowedGroupCiphers = srcWifiConfiguration.allowedGroupCiphers;
			newWifiConfiguration.allowedKeyManagement = srcWifiConfiguration.allowedKeyManagement;
			newWifiConfiguration.allowedPairwiseCiphers = srcWifiConfiguration.allowedPairwiseCiphers;
			newWifiConfiguration.allowedProtocols = srcWifiConfiguration.allowedProtocols;
		}
		return newWifiConfiguration;
	}

	/** 获取扫描列表 */
	public static String scanResultListToString(List<ScanResult> scanResultList) {
		if (scanResultList == null) {
			return null;
		}
		StringBuffer scanBuilder = new StringBuffer();
		int i = 0;
		scanBuilder.append("ScanResult ");
		scanBuilder.append(scanResultList.size());
		for (ScanResult sr : scanResultList) {
			scanBuilder.append(i);
			scanBuilder.append('\n');
			// 所有信息
			scanBuilder.append(toString(sr));
			scanBuilder.append('\n');
			++i;
		}
		return scanBuilder.toString();
	}

	/** wifiConfigList信息 */
	public static String wifiConfigurationListToString(List<WifiConfiguration> wifiConfigList) {
		if (wifiConfigList == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("wifiConfigList-");
		sb.append(wifiConfigList.size());
		sb.append('\n');
		int i = 0;
		for (WifiConfiguration wifiConfig : wifiConfigList) {
			sb.append(i);
			sb.append('\n');
			sb.append(toString(wifiConfig));
			sb.append('\n');
			++i;
		}
		return sb.toString();
	}

	public static String toString(WifiConfiguration srcWifiConfiguration) {
		if (srcWifiConfiguration == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SSID=");
		sb.append(srcWifiConfiguration.SSID);
		sb.append('\n');
		sb.append("preSharedKey=");
		sb.append(srcWifiConfiguration.preSharedKey);
		sb.append('\n');
		sb.append("hiddenSSID=");
		sb.append(srcWifiConfiguration.hiddenSSID);
		sb.append('\n');
		sb.append("networkId=");
		sb.append(srcWifiConfiguration.networkId);
		sb.append('\n');
		sb.append("priority=");
		sb.append(srcWifiConfiguration.priority);
		sb.append('\n');
		sb.append("BSSID=");
		sb.append(srcWifiConfiguration.BSSID);
		sb.append('\n');
		sb.append("status=");
		sb.append(srcWifiConfiguration.status);
		sb.append('\n');
		sb.append("wepKeys=");
		stringArrayToString(sb, srcWifiConfiguration.wepKeys);
		sb.append('\n');
		sb.append("wepTxKeyIndex=");
		sb.append(srcWifiConfiguration.wepTxKeyIndex);
		sb.append('\n');

		sb.append("allowedAuthAlgorithms=");
		sb.append(srcWifiConfiguration.allowedAuthAlgorithms);
		sb.append('\n');
		sb.append("allowedGroupCiphers=");
		sb.append(srcWifiConfiguration.allowedGroupCiphers);
		sb.append('\n');
		sb.append("allowedKeyManagement=");
		sb.append(srcWifiConfiguration.allowedKeyManagement);
		sb.append('\n');
		sb.append("allowedPairwiseCiphers=");
		sb.append(srcWifiConfiguration.allowedPairwiseCiphers);
		sb.append('\n');
		sb.append("allowedProtocols=");
		sb.append(srcWifiConfiguration.allowedProtocols);
		sb.append('\n');
		return sb.toString();
	}

	private static void stringArrayToString(StringBuilder sb, String[] strs) {
		sb.append('[');
		if (strs != null) {
			for (String str : strs) {
				sb.append(str);
				sb.append(',');
			}
		}
		sb.append(']');
	}

	@SuppressLint("NewApi")
	public static String toString(WifiInfo mWifiInfo) {
		if (mWifiInfo == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SSID：").append(mWifiInfo.getSSID()).append('\n');
		sb.append("BSSID").append(mWifiInfo.getBSSID()).append('\n');
		sb.append("describeContents：").append(mWifiInfo.describeContents()).append('\n');
		sb.append("HiddenSSID：").append(mWifiInfo.getHiddenSSID()).append('\n');
		sb.append("IpAddress：").append(Formatter.formatIpAddress(mWifiInfo.getIpAddress())).append('\n');
		sb.append("LinkSpeed：").append(mWifiInfo.getLinkSpeed()).append('\n');
		sb.append("MacAddress：").append(mWifiInfo.getMacAddress()).append('\n');
		sb.append("NetworkId：").append(mWifiInfo.getNetworkId()).append('\n');
		sb.append("Rssi：").append(mWifiInfo.getRssi()).append('\n');
		sb.append("SupplicantState：").append(mWifiInfo.getSupplicantState()).append('\n');
		return sb.toString();
	}

	public static String toString(ScanResult sr) {
		if (sr == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SSID:").append(sr.SSID).append('\n');
		sb.append("BSSID:").append(sr.BSSID).append('\n');
		sb.append("level:").append(sr.level).append('\n');
		sb.append("frequency:").append(sr.frequency).append('\n');
		sb.append("capabilities:").append(sr.capabilities).append('\n');
		sb.append("describeContents:").append(sr.describeContents()).append('\n');
		return sb.toString();
	}

	public static String toString(DhcpInfo dhcpInfo) {
		if (dhcpInfo == null) {
			return null;
		}
		return dhcpInfo.toString();
	}

	public static final String wifiStateToString(int wifiState) {
		String name = null;
		switch (wifiState) {
		case WifiManager.WIFI_STATE_DISABLED:
			name = "Wifi不可用";
			break;
		case WifiManager.WIFI_STATE_DISABLING:
			name = "Wifi停用中";
			break;
		case WifiManager.WIFI_STATE_ENABLED:
			name = "Wifi准备就绪";
			break;
		case WifiManager.WIFI_STATE_ENABLING:
			name = "Wifi启动中";
			break;
		case WifiManager.WIFI_STATE_UNKNOWN:
		default:
			name = "Wifi未知状态";
			break;
		}
		return name;
	}
}
