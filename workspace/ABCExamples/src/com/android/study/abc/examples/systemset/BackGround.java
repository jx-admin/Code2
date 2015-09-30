package com.android.study.abc.examples.systemset;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.study.abc.examples.R;

/**
 * @author Administrator
 *	android±≥æ∞Õº∆¨µƒ…Ë÷√
 */
public class BackGround extends Activity implements OnClickListener{
	// TODO Auto-generated constructor stub
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setbackground);
		((Button)findViewById(R.id.btn_setBackground)).setOnClickListener(this);
		((Button)findViewById(R.id.btn_defaultBackground)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Bitmap bitmap=null;
		switch (v.getId()) {
		case R.id.btn_setBackground:
			bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.icon);
			break;
		case R.id.btn_defaultBackground:
			bitmap=null;
			break;

//		default:
//			break;
		}
		setBackGround(bitmap);
	}
	public void setBackGround(Bitmap bitmap){
		try {
			if(bitmap==null){
				//ƒ¨»œ«Ω÷Ω
				clearWallpaper();
			}else {
				//…Ë÷√«Ω÷Ω
				setWallpaper(bitmap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
