package com.aess.example2;


import java.io.File;

import com.aess.exmaple2.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
//import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class main extends Activity {
	/** Called when the activity is first created. */
	private Context l_context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button installButton,uninstallButton,disableInstallButton,enableInstallButton;
		
		installButton = (Button)findViewById(R.id.install_button);
		uninstallButton = (Button)findViewById(R.id.uninstall_button);
		disableInstallButton = (Button)findViewById(R.id.button1);
		enableInstallButton = (Button)findViewById(R.id.button2);
		l_context = this.getApplicationContext();
		
		installButton.setOnClickListener(new Button.OnClickListener(){
			
			public void onClick(View v)
			{
				String fileName = Environment.getExternalStorageDirectory() + "/ApiDemos.apk";
				File apkFile = new File(fileName);
				if(apkFile.exists())
				{
					installApk(Uri.fromFile(apkFile));
				}
				else
				{
					Toast.makeText(main.this, fileName + "not exists",
		                    Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		uninstallButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				uninstallApk(Uri.parse("package:com.example.android.apis"));
			}
		});
		
		disableInstallButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				PackageManager pkManager = l_context.getPackageManager();
				pkManager.setApplicationEnabledSetting("com.android.packageinstaller", PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
			}
		});
		
		enableInstallButton.setOnClickListener(new Button.OnClickListener(){
			
			public void onClick(View v)
			{
				PackageManager pkManager = l_context.getPackageManager();
				pkManager.setApplicationEnabledSetting("com.android.packageinstaller", PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
			}
		});
		
	}

	private void installApk(Uri packageUri)
	{
		//String fileName = Environment.getExternalStorageDirectory() + "/myApp.apk";     
		int result = Settings.Secure.getInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0); 
		if(result == 1)
		{
			Intent intent = new Intent(Intent.ACTION_VIEW);   

			intent.setDataAndType(packageUri,"application/vnd.android.package-archive");   
			startActivity(intent); 
		}
		else
		{
			Toast.makeText(main.this, R.string.install_unsetting_notify,
                    Toast.LENGTH_SHORT).show();
		}
	}

	private void uninstallApk(Uri packageName)
	{  
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageName);      
		startActivity(uninstallIntent);
	}
}