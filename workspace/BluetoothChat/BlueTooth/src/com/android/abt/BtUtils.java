package com.android.abt;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * <pre>
 * 使用蓝牙设备需要先在Manifest中开放权限
 * // 使用蓝牙设备的权限  
 *     < uses-permission android:name="android.permission.BLUETOOTH" />  
 *     // 管理蓝牙设备的权限  
 *     < uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 * @author junxu.wang
 * 
 */
public class BtUtils {
	BluetoothAdapter adapter;
	String name;
	/**
	 * 打开蓝牙 获得蓝牙适配器（android.bluetooth.BluetoothAdapter），检查该设备是否支持蓝牙，如果支持，就打开蓝牙。
	 * 
	 * @param context
	 */
	public void enableBt(Context context) {
		// 检查设备是否支持蓝牙
		adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			// 设备不支持蓝牙
			return;
		}
		// 打开蓝牙
		if (!adapter.isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 设置蓝牙可见性，最多300秒
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			context.startActivity(intent);
		}
	}

	/**
	 * 获取已配对的蓝牙设备（android.bluetooth.BluetoothDevice）
	 * 首次连接某蓝牙设备需要先配对，一旦配对成功，该设备的信息会被保存，以后连接时无需再配对，所以已配对的设备不一定是能连接的。
	 */
	public void bondedDevices() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		for (int i = 0; i < devices.size(); i++) {
			BluetoothDevice device = (BluetoothDevice) devices.iterator()
					.next();
			System.out.println(device.getName());
		}
	}

	/**
	 * 搜索周围的蓝牙设备
	 * 适配器搜索蓝牙设备后将结果以广播形式传出去，所以需要自定义一个继承广播的类，在onReceive方法中获得并处理蓝牙设备的搜索结果。
	 */
	public void discovery(Context context) {
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 注册广播接收器，接收并处理搜索结果
		context.registerReceiver(receiver, intentFilter);
		// 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
		adapter.startDiscovery();
	}
	
		/**蓝牙设备的配对和状态监视
		 */
		private BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		    		// 获取查找到的蓝牙设备
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					System.out.println(device.getName());
					// 如果查找到的设备符合要连接的设备，处理
					if (device.getName().equalsIgnoreCase(name)) {
						// 搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索
					    adapter.cancelDiscovery();
					    // 获取蓝牙设备的连接状态
					   int  connectState = device.getBondState();
					    switch (connectState) {
		   			        // 未配对
					    	case BluetoothDevice.BOND_NONE:
							    // 配对
							    try {
								    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
								    createBondMethod.invoke(device);
							    } catch (Exception e) { 
								    e.printStackTrace();
							    }
							    break;
						    // 已配对
						    case BluetoothDevice.BOND_BONDED:
							    try {
							    	// 连接
							    	connect(device);
							    } catch (IOException e) {
							    	e.printStackTrace();
							    }
							    break;
					    }
				    }
		       } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
					// 状态改变的广播
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device.getName().equalsIgnoreCase(name)) { 
						int connectState = device.getBondState();
						switch (connectState) {
							case BluetoothDevice.BOND_NONE:
								break;
							case BluetoothDevice.BOND_BONDING:
								break;
							case BluetoothDevice.BOND_BONDED:
							    try {
								    // 连接
		                            connect(device);
		                        } catch (IOException e) {
		                            e.printStackTrace();
		                        }
		                        break;
		                }
		            }
		        }
		    }
		};
		
		/**蓝牙设备的连接
		 * @param device
		 * @throws IOException
		 */
		public void connect(BluetoothDevice device) throws IOException {
			// 固定的UUID
			final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
			UUID uuid = UUID.fromString(SPP_UUID);
		    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
		    socket.connect();
		}

}
