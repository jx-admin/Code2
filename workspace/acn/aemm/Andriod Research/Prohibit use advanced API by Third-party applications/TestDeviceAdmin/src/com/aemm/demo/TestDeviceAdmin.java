package com.aemm.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestDeviceAdmin extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Intent result = new Intent();
       // result.setClassName("com.aemm.demo", "com.aemm.demo.DeviceAdminSample$Controller");
        
       // startActivity(result);
        Button addCountBtn = (Button)findViewById(R.id.btnActive);
        addCountBtn.setOnClickListener(deviceAdminActiveListener);
        
    }
    private OnClickListener deviceAdminActiveListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		//switch to addcount
    		 Intent result = new Intent();
    	     result.setClassName("com.aemm.demo", "com.aemm.demo.MyDeviceAdminSample$Controller");
    	        
    	     startActivity(result);
    	}

		
    };
}