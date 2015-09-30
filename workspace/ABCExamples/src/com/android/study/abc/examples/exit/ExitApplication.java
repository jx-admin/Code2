package com.android.study.abc.examples.exit;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.study.abc.examples.R;



public class ExitApplication extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_application);
		Button btnButton=(Button) findViewById(R.id.btn_killProcess);
		Button btnButton2=(Button) findViewById(R.id.btn_restartPackage);
		Button btnButton3=(Button) findViewById(R.id.btn_killBackgroundProcesses);
		btnButton.setOnClickListener(this);
		btnButton2.setOnClickListener(this);
		btnButton3.setOnClickListener(this);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_killProcess:
		android.os.Process.killProcess(android.os.Process.myPid());
			break;
		case R.id.btn_restartPackage:
			ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);   
			manager.restartPackage("com.android.study.abc");//需要声明权限<uses-permission android:name="android.permission.RESTART_PACKAGES"/>
			break;
		case R.id.btn_killBackgroundProcesses:
//			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); 
//			am.killBackgroundProcesses("com.android.study.abc");   // API Level至少为8才能使用
			break;
		default:
			break;
		}
	}
	
}
