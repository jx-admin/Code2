package com.mog.EncDemo;

import com.mog.EncDemo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mog.EncDemo.GetInputPass;
import com.mog.EncDemo.SelectFile;
/*
 * this file is used to select the file to be decrypted . 
 */
public class DecFile extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private static final String TAG = "DecFile";
	private EditText edtPath ; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filedec); 
		Button buttonEnc = (Button) findViewById(R.id.decfileButton);
		buttonEnc.setOnClickListener(this);
		buttonEnc = (Button)findViewById(R.id.decfileDecode) ; 
		buttonEnc.setOnClickListener(this) ; 
		edtPath = (EditText)findViewById(R.id.decfileFname) ; 
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.decfileButton:
			selectFile() ; 
			break;
		case R.id.decfileDecode:
			setPassword();
			break;
		}
	}
	
	private void selectFile()
	{
		Intent iStart = new Intent(DecFile.this , SelectFile.class ) ; 
    	startActivity(iStart);
	}
	private void setPassword()
	{
		String strTem = edtPath.getText().toString(); 
		if( EncUtil.isEmpty(strTem) )
		{
			EncUtil.showMessage(this , "错误","请选择要加密的文件","返回") ; 
			return ; 
		}
		Intent iStart = new Intent(DecFile.this , GetInputPass.class ) ; 
    	startActivity(iStart);
	}
	private void startEncProgress(int i) 
    {
		Log.d(TAG, "clicked on " + i);
    }
}