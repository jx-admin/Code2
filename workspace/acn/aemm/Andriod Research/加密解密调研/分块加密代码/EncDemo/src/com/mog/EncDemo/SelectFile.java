package com.mog.EncDemo;

import com.mog.EncDemo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mog.EncDemo.SetPassword;
/*
 * this file is used to select files 
 */
public class SelectFile extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private static final String TAG = "FileEnc";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileselect); 
		Button temButton = (Button)findViewById(R.id.selfileOK) ; 
		temButton.setOnClickListener(this) ; 
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.selfileOK:
			getCheckedFile();
			break;
		}
	}
	
	private void getCheckedFile()
	{
		// get the choosen file
		finish();
	}
	private void setPassword()
	{
	}
	private void startEncProgress(int i) 
    {
		Log.d(TAG, "clicked on " + i);
    }
}