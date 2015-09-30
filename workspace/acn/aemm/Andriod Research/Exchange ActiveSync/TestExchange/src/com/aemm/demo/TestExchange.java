package com.aemm.demo;

import com.android.email.ExchangeUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class TestExchange extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         Log.i("TestExchange", "TestExchange enter ii");
                 
        Button addCountBtn = (Button)findViewById(R.id.addCount);
        addCountBtn.setOnClickListener(addCountListener);
        
        Button addEmailCountBtn = (Button)findViewById(R.id.addEmailCount);
        addEmailCountBtn.setOnClickListener(addEmailCountListener);
     //   ExchangeUtils.startExchangeService(this);
    }
        
    
    public static final int REQUEST_ADD_ACCOUNT = 1;
    public static final int REQUEST_ADD_EMAIL_ACCOUNT = 2;
  
    private OnClickListener addCountListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		//switch to addcount
    		Intent intent = new Intent();
    		intent.setClass(TestExchange.this, AddCount.class);
    		startActivityForResult(intent,REQUEST_ADD_ACCOUNT);
    		
    	}

		
    };
    private OnClickListener addEmailCountListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		//switch to addcount
    		Intent intent = new Intent();
    		intent.setClass(TestExchange.this, AddEmailCount.class);
    		startActivityForResult(intent,REQUEST_ADD_EMAIL_ACCOUNT);
    		
    	}

		
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	Log.i("TestExchage","onActivityResult");
    	if (resultCode == 1)
    	{
    		Log.i("TestExchage","result is ok");
    		Toast.makeText(TestExchange.this, "Add account ok", Toast.LENGTH_SHORT).show();   
    		 
    	}
    	
    }
   
}