package com.android.study.abc.examples.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.study.abc.examples.R;

public class AndroidShare extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_page);
	}
	
	public void onShare(View view){
			Intent intent=new Intent(Intent.ACTION_SEND); 
			intent.setType("image/*"); 
			intent.setType("text/*"); 
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
			intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");  
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(Intent.createChooser(intent, getTitle())); 

	}
	
	public void onShare1(View view){
		
	}
}
