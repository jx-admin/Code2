package com.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

public class CustomEditText extends EditText {
	OnKeyPreIme mOnkeyPreIme;

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if(mOnkeyPreIme!=null){
			return mOnkeyPreIme.onKeyPreIme(event.getKeyCode(), event);
		}
		return super.dispatchKeyEventPreIme(event);

	}

	public boolean onKeyPreIme(int keyCode,KeyEvent event){
		return super.onKeyPreIme(keyCode, event);
	}
	
	public void setOnKeyPreIme(OnKeyPreIme onKeyPreIme){
		mOnkeyPreIme=onKeyPreIme;
	}
	public interface OnKeyPreIme{
		public boolean onKeyPreIme(int keyCode,KeyEvent event);
	}
	
}
