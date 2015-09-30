package com.android.study.abc.examples;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Pass extends Activity implements OnClickListener {
	EditText word_et;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass);
        word_et=(EditText) findViewById(R.id.word_et);
        ((Button)findViewById(R.id.sure_btn)).setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sure_btn:
			String word_str=word_et.getText().toString();
			Log.v("[mylog]","word:"+word_str+"  time:"+new java.text.SimpleDateFormat("yykkmm").format(new java.util.Date()));
			final Calendar c = Calendar.getInstance();
			if(new java.text.SimpleDateFormat("yykkmm").format(new java.util.Date()).equals(word_str)){
				startActivity(new Intent(this,ExamplesActivity.class));
			}
			break;
		}
	}
}