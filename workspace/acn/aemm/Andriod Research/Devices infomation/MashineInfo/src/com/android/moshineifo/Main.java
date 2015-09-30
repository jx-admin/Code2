package com.android.moshineifo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class Main extends Activity {
	private TelephonyManager tm;
	private MyPhoneStateListener MyListener;
	private TextView battery_tv;
	private TextView signalStrength_tv;
	private TextView mashineInfo_tv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
		showInfo();
	}
	
	private void init(){
		battery_tv=(TextView) findViewById(R.id.battery_tv);
		signalStrength_tv=(TextView) findViewById(R.id.signalStrength_tv);
		mashineInfo_tv=(TextView) findViewById(R.id.mashineInfo_tv);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		MyListener = new MyPhoneStateListener();
		tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	private void showInfo(){
		mashineInfo_tv.setText("MAC："+getMac() +getBuildInfo() +"\n====\n" + getInfo() + "\n====\n"
				+ new SystemProperty().getInfo(this));
		Log.v("V", mashineInfo_tv.getText().toString());
	}

	@Override
	protected void onPause(){
		super.onPause();
		tm.listen(MyListener, PhoneStateListener.LISTEN_NONE);
	}

	/* Called when the application resumes */
	@Override
	protected void onResume(){
		super.onResume();
		tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}


	/** Start the PhoneState listener */
	private class MyPhoneStateListener extends PhoneStateListener{
		/*
		 * Get the Signal strength from the provider, each tiome there is an
		 * update
		 */
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength){

			super.onSignalStrengthsChanged(signalStrength);
			signalStrength_tv.setText("Go to Firstdroid!!! GSM Cinr = "
					+ String.valueOf(signalStrength.getGsmSignalStrength()));
			Log.v("V","Go to Firstdroid!!! GSM Cinr = "
					+ String.valueOf(signalStrength.getGsmSignalStrength()));
		}
	};/* End of private Class */
	
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
	    int intLevel = 0;
	    int intScale = 0;

	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        /*
	        * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
	        */
	        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
	        	StringBuffer sb=new StringBuffer();
	        	
	            intLevel = intent.getIntExtra("level", 0);
	            intScale = intent.getIntExtra("scale", 100);
	            sb.append("电池Level："+intLevel).append("\nlevel 比例："+intScale);

	            // 电池温度
	            sb.append("\nBattery当前温度为T:" + intent.getIntExtra("temperature", 0));
	            // 电池伏数
	            sb.append("\n当前电压为:" + intent.getIntExtra("voltage", 0));
	           String BatteryStatus=null;
	            switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
	                case BatteryManager.BATTERY_STATUS_CHARGING:
	                    BatteryStatus = "充电状态";
	                    break;
	                case BatteryManager.BATTERY_STATUS_DISCHARGING:
	                    BatteryStatus = "放电状态";
	                    break;
	                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
	                    BatteryStatus = "未充电";
	                    break;
	                case BatteryManager.BATTERY_STATUS_FULL:
	                    BatteryStatus = "充满电";
	                    break;
	                case BatteryManager.BATTERY_STATUS_UNKNOWN:
	                    BatteryStatus = "未知道状态";
	                    break;
	                default:
	                    BatteryStatus="";
	            }
	            sb.append("\n状态:" +BatteryStatus);
	            String BatteryStatus2=null;
	            switch (intent.getIntExtra("plugged", BatteryManager.BATTERY_PLUGGED_AC)) {
	                case BatteryManager.BATTERY_PLUGGED_AC:
	                    BatteryStatus2 = "AC充电";
	                    break;
	                case BatteryManager.BATTERY_PLUGGED_USB:
	                    BatteryStatus2 = "USB充电";
	                    break;
	                    default:
	                    BatteryStatus2="";
	            }
	            sb.append("\n充电:" +BatteryStatus2);
	            String BatteryTemp=null;
	            switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
	                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
	                    BatteryTemp = "未知错误";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_GOOD:
	                    BatteryTemp = "状态良好";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_DEAD:
	                    BatteryTemp = "电池没有电";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
	                    BatteryTemp = "电池电压过高";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
	                    BatteryTemp = "电池过热";
	                    break;
	                default:
	                	BatteryTemp="";
	            }
	            sb.append("\n安全性:" +BatteryTemp);
	            battery_tv.setText(sb.toString());
	        }
	    }
	};
	
	/**
	 * @return <pre>
	 * 在Android中以上机型信息在手机状态Status里 
	 * Settings-&gt;About Phone-&gt;Status 
	 * 各个栏目的功能介绍如下： 
	 * Battery status：电池充电/未充电状态 
	 * Battery level：电池剩余电量 
	 * Phone number：手机序列号 
	 * Network：所处的移动网络 
	 * Signal strength：信号度 
	 * Network type：网络制式 
	 * Service state：所在服务区 
	 * Roaming：漫游/未漫游 
	 * Data access：共访问的数据大小 
	 * IMEI：IMEI码 
	 * IMEI SV：IMEI码的版本 
	 * IMSI：国际移动用户识别码 
	 * Wi-Fi  Mac address：G1无线Wi-Fi网络的Mac地址。 
	 * Bluetooth address：蓝牙地址 
	 * Up time：正常运行时间 
	 * Awake Time：手机唤醒时间
	 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
	 * </pre>
	 */
	public String getInfo() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());

		sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());

		sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());

		sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());

		sb.append("\nLine1Number = " + tm.getLine1Number());

		sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());

		sb.append("\nPhoneType = " + tm.getPhoneType());

		sb.append("\nSimSerialNumber = " /*+ tm.getSimSerialNumber()*/);
		if (tm.getSimSerialNumber() != null) {
			sb.append("@" + tm.getSimSerialNumber().toString());
		} else {
			sb.append("@无法取得SIM卡号");
		}

		sb.append("\nSimOperator = " /*+ tm.getSimOperator()*/);
		if (tm.getSimOperator().equals("")) {
			sb.append("@无法取得供货商代码");
		} else {
			sb.append("@" + tm.getSimOperator().toString());
		}

		sb.append("\nSimOperatorName = " /*+ tm.getSimOperatorName()*/);
		if (tm.getSimOperatorName().equals("")) {
			sb.append("@无法取得供货商");
		} else {
			sb.append("@" + tm.getSimOperatorName().toString());
		}

		sb.append("\nSimCountryIso = " /*+ tm.getSimCountryIso()*/);
		if (tm.getSimCountryIso().equals("")) {
			sb.append("@无法取得国籍");
		} else {
			sb.append("@" + tm.getSimCountryIso().toString());
		}

		sb.append("\nNetworkOperator = " /*+ tm.getNetworkOperator()*/);
		if (tm.getNetworkOperator().equals("")) {
			sb.append("@无法取得网络运营商");
		} else {
			sb.append("@" + tm.getNetworkOperator());
		}

		sb.append("\nNetworkOperatorName = " /*+ tm.getNetworkOperatorName()*/);
		if (tm.getNetworkOperatorName().equals("")) {
			sb.append("@无法取得网络运营商名称");
		} else {
			sb.append("@" + tm.getNetworkOperatorName());
		}

		 sb.append("\nNetworkType = "/* + tm.getNetworkType()*/);
		sb.append("@");
		switch (tm.getNetworkType()) {
		/** Network type is unknown */
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:// = 0;
			sb.append("无法取得网络类型");
			break;
		/** Current network is GPRS */
		case TelephonyManager.NETWORK_TYPE_GPRS:// 1;
			sb.append("GPRS");
			break;
		/** Current network is EDGE */
		case TelephonyManager.NETWORK_TYPE_EDGE:// 2;
			sb.append("EDGE");
			break;
		/** Current network is UMTS */
		case TelephonyManager.NETWORK_TYPE_UMTS:// 3;
			sb.append("UMTS");
			break;
		/** Current network is CDMA: Either IS95A or IS95B */
		case TelephonyManager.NETWORK_TYPE_CDMA:// 4;
			sb.append("CDMA");
			break;
		/** Current network is EVDO revision 0 */
		case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5;
			sb.append("EVDO_0");
			break;
		/** Current network is EVDO revision A */
		case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6;
			sb.append("EVDO_A");
			break;
		/** Current network is 1xRTT */
		case TelephonyManager.NETWORK_TYPE_1xRTT:// 7;
			sb.append("1xRTT");
			break;
		/** Current network is HSDPA */
		case TelephonyManager.NETWORK_TYPE_HSDPA:// 8;
			sb.append("HSDPA");
			break;
		/** Current network is HSUPA */
		case TelephonyManager.NETWORK_TYPE_HSUPA:// 9;
			sb.append("HSUPA");
			break;
		/** Current network is HSPA */
		case TelephonyManager.NETWORK_TYPE_HSPA:// 10;
			sb.append("HSPA");
			break;
		/** Current network is iDen */
		case TelephonyManager.NETWORK_TYPE_IDEN:// 11
			sb.append("IDEN");
			break;
		default:
			sb.append(tm.getNetworkType());
		}

		sb.append("\nSimState = (" + tm.getSimState() + ")");
		switch (tm.getSimState()) {
		case TelephonyManager.SIM_STATE_ABSENT:
			sb.append("无卡");
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			sb.append("未知状态");
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			sb.append("需要NetworkPIN解锁");
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			sb.append("需要PIN解锁");
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			sb.append("需要PUK解锁");
			break;
		case TelephonyManager.SIM_STATE_READY:
			sb.append("良好");
			break;
		}

		Log.v("DeviceInfo", sb.toString());
		return sb.toString();
	}

	public String getBuildInfo(){
		StringBuffer sb=new StringBuffer();
		sb.append("\n底层板的名称:"+Build.BOARD);
		sb.append("\n该系统引导程序的版本号:"+Build.BOOTLOADER);
		sb.append("\n该系统引导程序的版本号:"+Build.BRAND);
		sb.append("\n该指令集的本地代码（CPU类型+ ABI的公约）的名称:"+Build.CPU_ABI);
		sb.append("\n该工业设计名称:"+Build.DEVICE);
		sb.append("\nDisplay id:"+Build.DISPLAY);
		/**一个字符串，它唯一标识此版本。不要试图分析这个值*/
		sb.append("\nFINGERPRINT id:"+Build.FINGERPRINT);
		sb.append("\n硬件的名称（从内核命令行或/触发）:"+Build.HARDWARE);
		sb.append("\nHOST:"+Build.HOST);
		sb.append("\nID:"+Build.ID);
		sb.append("\n制造商/硬件:"+Build.MANUFACTURER);
		sb.append("\n手机型号:"+Build.MODEL);
		sb.append("\nThe name of the overall product:"+Build.PRODUCT);
		sb.append("\nThe radio firmware version number:"+Build.RADIO);
		sb.append("\nComma-separated tags describing the build, like \"unsigned,debug\":"+Build.TAGS);
		sb.append("\nTime:"+Build.TIME);
		sb.append("\nThe type of build, like \"user\" or \"eng\":"+Build.TYPE);
		sb.append("\nUSER:"+Build.USER);
		sb.append("\nCODENAME:"+Build.VERSION.CODENAME);
		sb.append("\nINCREMENTAL:"+Build.VERSION.INCREMENTAL);
		sb.append("\nandroid系统版本号:"+Build.VERSION.RELEASE);
		sb.append("\nSDK号:"+Build.VERSION.SDK);
		sb.append("\nSDK_INT(SDK version of the framework):"+Build.VERSION.SDK_INT);
		
		return sb.toString();
	}
	
	protected void dialog(String info) {
		AlertDialog.Builder builder = new Builder(Main.this);
		builder.setMessage(info);

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				Main.this.finish();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
	
	private String getMac(){
		 //获取wifi服务
	    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    //判断wifi是否开启
	    if (!wifiManager.isWifiEnabled()) { 
	    	wifiManager.setWifiEnabled(true);   
	    }
	    if (!wifiManager.isWifiEnabled()) {
	    	return null;
	    }
	    WifiInfo wifiInfo = wifiManager.getConnectionInfo(); 
	    return wifiInfo.getMacAddress();
	}

}