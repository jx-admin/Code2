package com.example.screllmenu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	String []strs=new String[]{"DFDF","DFDFDFF","aaaaa","bbb","cccc","ddddddd","eeeee","ffffff","ggggggg","hhhhhh","iiiii"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout hsv_ll=(LinearLayout) findViewById(R.id.hsv_ll);
		
		MenuUtils mu=new MenuUtils();
		mu.initItem(this, hsv_ll, strs);
		mu.setCurrent(3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
