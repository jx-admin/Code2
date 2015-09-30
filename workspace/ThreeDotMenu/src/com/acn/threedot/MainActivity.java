package com.acn.threedot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {
	Button button1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button1=(Button) findViewById(R.id.button1);
		button1.setAlpha(0.5f);
		button1.setOnClickListener(this);
		button1.setOnTouchListener(mOnConfirmTouchListener);
		View view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
int times;
OnTouchListener mOnConfirmTouchListener=new OnTouchListener() {
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			times++;
			
			
		case MotionEvent.ACTION_UP:
			break;
		}
		if(times<10){
			return true;
		}else {
			return v.onTouchEvent(event);
		}
	}
};
@Override
public void onClick(View arg0) {
	Toast.makeText(this, "onclick", 0).show();
}

}
