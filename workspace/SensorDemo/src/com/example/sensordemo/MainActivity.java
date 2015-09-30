package com.example.sensordemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	ToggleButton tbtn_sensor,tbtn_bettery;
	TextView sensor_tv,bettery_tv;
	
	SensorUtils mSensorUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		mSensorUtils=new SensorUtils(this);
	}
	
	private void initView(){
		sensor_tv=(TextView) findViewById(R.id.sensor_tv);
		bettery_tv=(TextView) findViewById(R.id.bettery_tv);
		
		tbtn_sensor=(ToggleButton) findViewById(R.id.toggle_sensor);
		tbtn_bettery=(ToggleButton) findViewById(R.id.toggle_bettery);
		
		tbtn_sensor.setOnCheckedChangeListener(onCheckedChangeListener);
		tbtn_bettery.setOnCheckedChangeListener(onCheckedChangeListener);
	}
	OnCheckedChangeListener onCheckedChangeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(buttonView==tbtn_sensor){
				if(isChecked){
					mSensorUtils.startListener(MainActivity.this,mHandler);
				}else{
					mSensorUtils.stopListener(MainActivity.this);
				}
				
			}else if(buttonView==tbtn_bettery){
				startService(new Intent(MainActivity.this, MonitorService.class));  
			}
		}
	};
	
	Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			sensor_tv.setText((CharSequence) msg.obj);
		};
	};
}
