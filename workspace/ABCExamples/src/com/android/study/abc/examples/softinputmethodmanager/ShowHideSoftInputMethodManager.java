package com.android.study.abc.examples.softinputmethodmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.study.abc.examples.R;
public class ShowHideSoftInputMethodManager extends Activity implements OnClickListener{
	private InputMethodManager imm;
	EditText tv1;
	EditText tv2;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_hide_softinputmethodmanager);
		imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		((Button)findViewById(R.id.show_softinputmethodmanager_btn)).setOnClickListener(this);
		((Button)findViewById(R.id.hide_softinputmethodmanager_btn)).setOnClickListener(this);
		tv1=(EditText) findViewById(R.id.TextView01);
		tv2 = (EditText) findViewById(R.id.TextView02);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.show_softinputmethodmanager_btn:
			tv2.setFocusable(true);
			tv2.requestFocus();
			tv2.setFocusableInTouchMode(true);
			imm.showSoftInput(tv2, 0);
			break;
		case R.id.hide_softinputmethodmanager_btn:
			imm.hideSoftInputFromWindow(ShowHideSoftInputMethodManager.this.getCurrentFocus().getWindowToken(),   
			            InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		}
	}
}
