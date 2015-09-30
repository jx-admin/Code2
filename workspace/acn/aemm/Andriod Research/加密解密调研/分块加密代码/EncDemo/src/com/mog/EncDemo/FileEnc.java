package com.mog.EncDemo;

import com.mog.EncDemo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mog.EncDemo.SetPassword;
import com.mog.EncDemo.SelectFile;
/*
 * this file is used to choose file and 
 * have a button as : go and Encrypt . 
 */
public class FileEnc extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private EditText edtPath ; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileenc); 
		Button buttonEnc = (Button) findViewById(R.id.chfileButton);
		buttonEnc.setOnClickListener(this);
		buttonEnc = (Button)findViewById(R.id.chfileNext) ; 
		buttonEnc.setOnClickListener(this) ; 
		edtPath = (EditText)findViewById(R.id.chfileName) ; 
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.chfileButton:
			selectFile() ; 
			break;
		case R.id.chfileNext:
			setPassword();
			break;
		}
	}
	
	private void selectFile()
	{
		Intent iStart = new Intent(FileEnc.this , SelectFile.class ) ; 
    	startActivity(iStart);
	}
	private void setPassword()
	{
		String strTem = edtPath.getText().toString(); 
		if( EncUtil.isEmpty(strTem) )
		{
			EncUtil.showMessage(this,"错误","请选择要加密的文件","返回") ; 
			return ; 
		}
		Intent iStart = new Intent(FileEnc.this , SetPassword.class ) ; 
    	startActivity(iStart);
    	finish();
	}
}