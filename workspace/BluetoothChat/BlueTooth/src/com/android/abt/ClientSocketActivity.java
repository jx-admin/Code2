package com.android.abt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ClientSocketActivity extends Activity {
	private static final String TAG = ClientSocketActivity.class
			.getSimpleName();
	private static final int REQUEST_DISCOVERY = 0x1;
	private Handler _handler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			msgAdapter.notifyDataSetChanged();
		};
	};
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	
	private ListView msg_ls;
	private ArrayAdapter msgAdapter;
	private List<String> msgs=new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		if (!_bluetooth.isEnabled()) {
			Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		setContentView(R.layout.client_socket);
		msg_ls=(ListView) findViewById(R.id.msg_ls);
		msgAdapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1, msgs);
		msg_ls.setAdapter(msgAdapter);
		Intent intent = new Intent(this, DiscoveryActivity.class);
		/* 提示选择一个要连接的服务器 */
		Toast.makeText(this, "select device to connect", Toast.LENGTH_SHORT)
				.show();
		/* 跳转到搜索的蓝牙设备列表区，进行选择 */
		startActivityForResult(intent, REQUEST_DISCOVERY);
	}
	
	private void addMessage(String msg){
		_handler.sendEmptyMessage(1);
		msgs.add(msg);
	}

	/* 选择了服务器之后进行连接 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REQUEST_DISCOVERY) {
			return;
		}
		if (resultCode != RESULT_OK) {
			return;
		}
		final BluetoothDevice device = data
				.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if(device==null){
			return;
		}
		addMessage(device.getAddress()+"---"+device.getName());
		new Thread() {
			public void run() {
				/* 连接 */
				connect(device);
			};
		}.start();
	}

	protected void connect(BluetoothDevice device) {
		BluetoothSocket socket = null;
		try {
			// 创建一个Socket连接：只需要服务器在注册时的UUID号
			// socket =
			// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
			socket = device.createRfcommSocketToServiceRecord(UUID
					.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
			if(Constant.DEBUG){
				Log.d(TAG,"waiting to connect server...");
			}
			addMessage("waiting to connect server...");
			// 连接
			socket.connect();
			if(Constant.DEBUG){
				Log.d(TAG,"connected server and write will...");
			}
			addMessage("connected server successful");
			OutputStream os=socket.getOutputStream();
			os.write("Hello,server!".getBytes());
			addMessage("s-->Hello,server!");
			os.flush();
			byte[] red=new byte[100]; 
			InputStream is=socket.getInputStream();
			is.read(red);
			addMessage("r<--"+new String(red));
			Log.d(TAG,"receve:"+new String(red));
			is.close();
			os.close();
		} catch (IOException e) {
			Log.e(TAG, "", e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
			}
		}
		Log.d(TAG,"disconnected");
	}
}
