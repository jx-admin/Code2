package com.tab.view;

import com.tab.view.demo1.FirstActivity;
import com.tab.view.demo2.SecondActivity;
import com.tab.view.demo3.ThirdActivity;
import com.tab.view.demo4.FourthActivity;
import com.tab.view.demo5.FifthActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author jacksen
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	private Button btn1, btn2, btn3, btn4, btn5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
	}

	private void init() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		btn5 = (Button) findViewById(R.id.btn5);

		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn1:
			intent.setClass(this, FirstActivity.class);
			break;
		case R.id.btn2:
			intent.setClass(this, SecondActivity.class);
			break;
		case R.id.btn3:
			intent.setClass(this, ThirdActivity.class);
			break;
		case R.id.btn4:
			intent.setClass(this, FourthActivity.class);
			break;
		case R.id.btn5:
			intent.setClass(this, FifthActivity.class);
			break;
		}
		startActivity(intent);
	}

}
