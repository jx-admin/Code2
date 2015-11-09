package com.android.test.wifi;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.format.Formatter;
import android.util.Log;

/**
 * @author abc
 * 
 *         <pre>
 * 通过网络书籍与文档,经过整理, 做出来的一个类.
 * 
 * 其实操作Wifi也是很简单的，主要使用以下几个对象或变量：
 * 
 * private WifiManager wifiManager;// 声明管理对象OpenWifi
 * private WifiInfo wifiInfo;// Wifi信息
 * private List&lt;ScanResult&gt; scanResultList; // 扫描出来的网络连接列表
 * private List&lt;WifiConfiguration&gt; wifiConfigList;// 网络配置列表
 * private WifiLock wifiLock;// Wifi锁
 * 
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
 * </pre>
 */
public class WifiManageWrap {
	// 声明管理对象
	WifiManager mWifiManager;
	// Wifi信息
	private WifiInfo mWifiInfo;
	// 扫描出来的网络连接列表
	private List<ScanResult> scanResultList;
	// 网络配置列表
	private List<WifiConfiguration> wifiConfigList;
	// Wifi锁
	private WifiLock mWifiLock;

	public WifiManageWrap(Context context) {
		// 获取Wifi服务
		this.mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 得到Wifi信息
		this.mWifiInfo = mWifiManager.getConnectionInfo();
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
			return false;
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
		// 扫描配置列表
		wifiConfigList = mWifiManager.getConfiguredNetworks();
	}

	/** ScanResult List */
	public List<ScanResult> getWifiList() {
		return scanResultList;
	}

	/** WifiConfiguration list */
	public List<WifiConfiguration> getWifiConfigList() {
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
	public String getwifiInfo() {
		return (mWifiInfo == null) ? null : mWifiInfo.toString();
	}

	/** 获取IP地址 */
	public int getIP() {
		return (mWifiInfo == null) ? null : mWifiInfo.getIpAddress();
	}

	/** 添加一个连接 */
	public boolean addNetWordLink(WifiConfiguration config) {
		Log.v("V", "start add WifiConfiguration");
		int NetId = mWifiManager.addNetwork(config);
		Log.v("V", "start save WifiConfiguration.." + NetId);
		return mWifiManager.saveConfiguration();
		// return wifiManager.enableNetwork(NetId, true);
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
		String findSsid = new StringBuilder().append("\"").append(ssid)
				.append("\"").toString();
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
	public static WifiConfiguration copyWifiConfiguration(
			WifiConfiguration srcWifiConfiguration) {
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
	public String scanResultListToString() {
		scanResultList.size();
		if (scanResultList == null) {
			return null;
		}
		StringBuffer scanBuilder = new StringBuffer();
		int i = 0;
		for (ScanResult sr : scanResultList) {
			scanBuilder.append(i);
			scanBuilder.append('\n');
			// 所有信息
			scanBuilder.append(scanResultList.get(i).toString());
			scanBuilder.append('\n');
		}
		return scanBuilder.toString();
	}

	/** wifiConfigList信息 */
	public String wifiConfigurationListToString() {
		wifiConfigList = mWifiManager.getConfiguredNetworks();
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
			sb.append(wifiConfigurationToString(wifiConfig));
			sb.append('\n');
		}
		return sb.toString();
	}

	public String wifiConfigurationToString(
			WifiConfiguration srcWifiConfiguration) {
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
	public String wifiInfoToString() {
		if (mWifiInfo == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SSID：").append(mWifiInfo.getSSID()).append('\n');
		sb.append("BSSID").append(mWifiInfo.getBSSID()).append('\n');
		sb.append("describeContents：").append(mWifiInfo.describeContents())
				.append('\n');
		sb.append("HiddenSSID：").append(mWifiInfo.getHiddenSSID()).append('\n');
		sb.append("IpAddress：")
				.append(Formatter.formatIpAddress(mWifiInfo.getIpAddress()))
				.append('\n');
		sb.append("LinkSpeed：").append(mWifiInfo.getLinkSpeed()).append('\n');
		sb.append("MacAddress：").append(mWifiInfo.getMacAddress()).append('\n');
		sb.append("NetworkId：").append(mWifiInfo.getNetworkId()).append('\n');
		sb.append("Rssi：").append(mWifiInfo.getRssi()).append('\n');
		sb.append("SupplicantState：").append(mWifiInfo.getSupplicantState())
				.append('\n');
		return sb.toString();
	}
}
