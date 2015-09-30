package com.mog.EncDemo;

import com.mog.EncDemo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/*
 * this file is to get the input of user , 
 * with the input data is the password . 
 */
public class GetInputPass extends Activity implements OnClickListener {
	private EditText edtInput;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputpass);
		Button buttonEnc = (Button) findViewById(R.id.getpassConfirm);
		buttonEnc.setOnClickListener(this);
		edtInput = (EditText)findViewById(R.id.getpassInput) ; 
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getpassConfirm:
			checkPassword();
			break;
		}
	}

	private void checkPassword() 
	{
		// check password 
		String strinput = edtInput.getText().toString();
		if( EncUtil.isEmpty(strinput))
		{
			EncUtil.showMessage(this,"错误","密码不能为空","返回"); 
			return ; 
		}
		else
		{
			Intent iStart = new Intent(GetInputPass.this , DispDecContent.class ) ; 
			iStart.putExtra("inputdata", strinput) ; 
	    	startActivity(iStart);
	    	finish();
		}
	}
}