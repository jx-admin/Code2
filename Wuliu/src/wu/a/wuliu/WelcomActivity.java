package wu.a.wuliu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import baidumapsdk.demo.R;

public class WelcomActivity extends Activity{
	private Handler mHandler=new Handler();
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(WelcomActivity.this,BookActivity.class));
				finish();
			}
		}, 3000);
		
	}

}
