package com.qin.ammp;


import java.util.ArrayList;
import java.util.List;

import wu.a.lib.app.ActivityManagerUtils;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static String TAG = "AM_MEMORYIPROCESS" ;
	
	private ActivityManager mActivityManager = null ;
	
	private TextView tvAvailMem  ;
	private Button btProcessInfo ;
	private Button btRunningApp ;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tvAvailMem = (TextView)findViewById(R.id.tvAvailMemory) ;
        btProcessInfo =(Button)findViewById(R.id.btProcessInfo);
        //跳转到显示进程信息界面
        btProcessInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,BrowseProcessInfoActivity.class);
				startActivity(intent);
			}
		});               
        
        btRunningApp = (Button)findViewById(R.id.btRunningApp) ;
        btRunningApp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,BrowseRunningAppActivity.class);
				startActivity(intent);
			}
		});
        //获得ActivityManager服务的对象
        mActivityManager = ActivityManagerUtils.getActivityManager(this);
      
        //获得可用内存信息
        String availMemStr =formateFileSize(ActivityManagerUtils.getSystemAvaialbeMemorySize(mActivityManager));
        Log.i(TAG, "The Availabel Memory Size is"+availMemStr); 
        //显示
        tvAvailMem.setText(availMemStr); 
        
    }
  
    //调用系统函数，字符串转换 long -String KB/MB
    private String formateFileSize(long size){
    	return Formatter.formatShortFileSize(MainActivity.this, size); 
    }
    
}