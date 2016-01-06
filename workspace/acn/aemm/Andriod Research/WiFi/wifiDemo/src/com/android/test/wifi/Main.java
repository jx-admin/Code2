package com.android.test.wifi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author abc
 *
 */
public class Main extends Activity implements OnClickListener {
	private Button del_btn, mod_btn, add_btn;
	private TextView meg_tv;
	private EditText old_ssid_et;
	private EditText ssid_et;
	private EditText word_et;
	private Button offon_btn;
	private WifiManagerWrap mWifiManageWrap;
	private String info;
	NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
mNetworkConnectChangedReceiver=new NetworkConnectChangedReceiver();
mNetworkConnectChangedReceiver.registerReceiver(this);
	}

	private void init() {
		meg_tv = (TextView) findViewById(R.id.meg_tv);
		old_ssid_et = (EditText) findViewById(R.id.old_ssid_et);
		ssid_et = (EditText) findViewById(R.id.ssid_et);
		word_et = (EditText) findViewById(R.id.word_et);
		offon_btn = (Button) findViewById(R.id.offon_btn);
		offon_btn.setOnClickListener(this);
		del_btn = ((Button) findViewById(R.id.del_btn));
		del_btn.setOnClickListener(this);
		mod_btn = ((Button) findViewById(R.id.mod_btn));
		mod_btn.setOnClickListener(this);
		add_btn = ((Button) findViewById(R.id.add_btn));
		add_btn.setOnClickListener(this);
		info = "";
		mWifiManageWrap = new WifiManagerWrap(this);
		print(R.string.load_success);

		mWifiManageWrap.startScan();
		print("WifiInfo:\n" + mWifiManageWrap.toString(mWifiManageWrap.getwifiInfo()));
		print(mWifiManageWrap.toString(mWifiManageWrap.mWifiManager.getDhcpInfo()));
		print("扫描结果:\n" + mWifiManageWrap.scanResultListToString(mWifiManageWrap.getScanResults()));
		print("配置列表:\n" + mWifiManageWrap.wifiConfigurationListToString(mWifiManageWrap.getConfiguredNetworks()));
	}

	private void fresh(boolean state) {
		if (state) {
			del_btn.setEnabled(true);
			mod_btn.setEnabled(true);
			add_btn.setEnabled(true);
		} else {
			del_btn.setEnabled(false);
			mod_btn.setEnabled(false);
			add_btn.setEnabled(false);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setButtonState(mWifiManageWrap.isWifiEnabled());
	}

	private void setButtonState(boolean state) {
		if (state) {
			offon_btn.setText(R.string.close);
			print(R.string.open_success);
		} else {
			offon_btn.setText(R.string.open);
			print(R.string.close_success);
		}
		fresh(state);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.offon_btn:
			if (mWifiManageWrap.isWifiEnabled()) {
				mWifiManageWrap.mWifiManager.setWifiEnabled(false);
				setButtonState(false);
			} else {
				mWifiManageWrap.mWifiManager.setWifiEnabled(true);
				setButtonState(true);
			}
			break;
		case R.id.del_btn:
			WifiConfiguration conf = mWifiManageWrap.getWifiConfig(ssid_et
					.getText().toString().trim());
			if (conf != null) {
				print("网络移除:"
						+ mWifiManageWrap.mWifiManager
								.removeNetwork(conf.networkId));
				mWifiManageWrap.mWifiManager.saveConfiguration();
			} else {
				print(R.string.not_find);
			}
			break;
		case R.id.mod_btn:
			WifiManagerUtils.getScanResult(this);
			conf = mWifiManageWrap.getWifiConfig(old_ssid_et.getText()
					.toString().trim());
			if (conf != null) {
				WifiConfiguration newCong = new WifiConfiguration();
				newCong.SSID = "\"" + ssid_et.getText().toString().trim()
						+ "\"";
				newCong.preSharedKey = "\"" + word_et.getText() + "\"";
				newCong.networkId = conf.networkId;
				print("网络配置修改:"
						+ mWifiManageWrap.mWifiManager.updateNetwork(newCong));
				mWifiManageWrap.mWifiManager.saveConfiguration();
				old_ssid_et.setText(ssid_et.getText());
			} else {
				print(R.string.not_find);
			}
			break;
		case R.id.add_btn:
			if (mWifiManageWrap.addNetWordLink(mWifiManageWrap.createWifiInfo(ssid_et.getText().toString(), word_et.getText().toString(),100)/*getWifiConfiguration()*/)) {
				print(R.string.add_success);
				old_ssid_et.setText(ssid_et.getText());
			} else {
				print(R.string.add_fail);
			}
			WifiManagerUtils.getData(this);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		mNetworkConnectChangedReceiver.unregisterReceiver(this);
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void print(String mes) {
		info += mes + "\n";
		meg_tv.setText(info);
	}

	private void print(int id) {
		print(getString(id));
	}

	/** WifiConfiguration 对象 */
	public WifiConfiguration getWifiConfiguration() {
		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + ssid_et.getText().toString().trim() + "\"";
		conf.preSharedKey = "\"" + word_et.getText() + "\"";
		conf.status = WifiConfiguration.Status.ENABLED;
		conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		return conf;
	}
	
	/**
	 * <pre/>
	 1.XML中声明

	 <receiver android:name=".NetworkConnectChangedReceiver" >
	            <intent-filter>
	                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
	                <action android:name="android.net.wifi.WIFI_STATE_CHANGED" />
	                <action android:name="android.net.wifi.STATE_CHANGE" />
	            </intent-filter>
	        </receiver>

	 

	2.代码中注册

	 IntentFilter filter = new IntentFilter();
	 filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
	 filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
	 filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	 registerReceiver(new NetworkConnectChangedReceiver(), filter);
	 * 
	 * 
	 * </pre>
	 * 
	 * @author Administrator
	 *
	 */
	public class NetworkConnectChangedReceiver extends BroadcastReceiver {
		private static final String TAG = "Network";
		
		public final void registerReceiver(Context context){
			IntentFilter filter = new IntentFilter();
			 filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
			 filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
			 filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			 context.registerReceiver(this, filter);
		}
		
		public final void unregisterReceiver(Context context){
			context.unregisterReceiver(this);
		}

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			print("onReceive\n" + intent.getAction());
//			switch(intent.getAction()){
//			case WifiManager.ACTION_PICK_WIFI_NETWORK:
//				break;
//			case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
//				break;
//			case WifiManager.RSSI_CHANGED_ACTION:// 有可能是正在获取，或者已经获取了
//				break;
//			}
			if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 这个监听wifi的打开与关闭，与wifi的连接无关
				int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
				print(WifiManager.WIFI_STATE_CHANGED_ACTION + "\n" + wifiState + WifiManagerWrap.wifiStateToString(wifiState));
				switch (wifiState) {
				case WifiManager.WIFI_STATE_DISABLED:
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					break;
				}
			}
			// 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
			// 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
			if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
				Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				print( "###isConnected" + parcelableExtra);
				if (null != parcelableExtra) {
					NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
					State state = networkInfo.getState();
					// 当然，这边可以更精确的确定状�?
					print(WifiManager.NETWORK_STATE_CHANGED_ACTION + "\n" + NetworkInfoWrap.toString(networkInfo));
				} else {
					print( WifiManager.NETWORK_STATE_CHANGED_ACTION + " null");
				}
			}
			// 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
			// 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
			// 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				print("%%%网络状态改变:" + wifi.isConnected() + " 3g:" + gprs.isConnected());
				NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				if (info != null) {
					print(ConnectivityManager.CONNECTIVITY_ACTION + "\n" + NetworkInfoWrap.toString(info));
				}
			}
		}
	}
}