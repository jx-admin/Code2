package com.mog.EncDemo;
import java.io.IOException;

import android.app.Activity;
/*import android.app.AlertDialog;
import android.content.DialogInterface;*/
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * the start of this programme . 
 */
public class EncDemo extends Activity implements OnClickListener {
    
	/** Called when the activity is first created. */
	private static final String TAG = "EncDemo";
	static
	{
		System.loadLibrary("EncDemo") ; 
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button buttonEnc = (Button) findViewById(R.id.encButton);
		buttonEnc.setOnClickListener(this);
		buttonEnc = (Button)findViewById(R.id.decButton) ; 
		buttonEnc.setOnClickListener(this) ; 
		buttonEnc = (Button)findViewById(R.id.mainexitButton); 
		buttonEnc.setOnClickListener(this) ;
		try {
			EncUtil.InitFile() ;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.encButton:
			try {
				startEncProgress(0) ;
			} catch (IOException e) {
				e.printStackTrace();
			} 
			break;
		case R.id.decButton:
			selectDecFile();
			break;
		case R.id.mainexitButton:
			finish();
			break;
		}
	}
	
/*	private void selectEncType()
	{
		new AlertDialog.Builder(this).setTitle(R.string.selectenctype).setItems(R.array.enctype 
    			, new DialogInterface.OnClickListener() 
    			{
    				public void onClick(DialogInterface dialoginterface,int i) 
    				{
    					try {
							startEncProgress(i);
						} catch (IOException e) {
							e.printStackTrace();
						}
    				}
    			}).show();
	}*/
	private void startEncProgress(int i) throws IOException 
    {
		Log.d(TAG, "clicked on " + i);
		switch(i)
		{
		case 0:
			{
				Intent iStart = new Intent(EncDemo.this , SetPassword.class ) ; 
		    	startActivity(iStart);
			}
			break;
		case 1:
			{
		    	Intent iStart = new Intent(EncDemo.this , ContentEnc.class ) ; 
		    	startActivity(iStart);
			}
			break;
		}
    }
	
	private void selectDecFile()
	{
		Intent iStart = new Intent(EncDemo.this , GetInputPass.class ) ; 
    	startActivity(iStart);
	}
}