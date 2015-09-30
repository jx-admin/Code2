package com.act.sctc.ui;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.act.sctc.R;
import com.act.sctc.util.Utils;


public class ImageBrowserActivity extends Activity {
	public static void start(Context context,Serializable obj){
		Intent intent=new Intent(context,ImageBrowserActivity.class);
		intent.putExtra("pic_uri", obj);
		context.startActivity(intent);
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent intent=getIntent();
		ImageView imgView=new ImageView(this);
		imgView.setImageResource(R.drawable.icon);
		imgView.setScaleType(ScaleType.FIT_CENTER);
		String uristr=(String) intent.getSerializableExtra("pic_uri");
		if(uristr!=null){
			imgView.setImageBitmap(Utils.getOptimizedBitmap(uristr));
		}
		setContentView(imgView);
	}

}
