package com.android.accenture.aemm.express;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MDialog  extends Activity implements OnClickListener{
	private static final byte HALL_UPDATA=0;
	private static final byte APP_UPDATA=1;
	private byte state=APP_UPDATA;
	private static final String MSG="msg";
	private static final String CANCELABLE="cancelAble";
	private TextView messageTv;
	private Button login_sure_btn;
	private Button login_cancel_btn;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        init();
        initData();
	}
	
	private void init(){
		messageTv=(TextView) findViewById(R.id.message_tv);
	     login_sure_btn=((Button) findViewById(R.id.login_sure_btn));
	     login_cancel_btn=(Button) findViewById(R.id.login_cancel_btn);
	     login_sure_btn.setOnClickListener(this);
	     login_cancel_btn.setOnClickListener(this);
	}
	private void initData(){
		Intent intent=getIntent();
		String msg=intent.getStringExtra(MSG);
		if(msg==null){
			msg="";
		}
		messageTv.setText(msg);
        boolean cancelAble=intent.getBooleanExtra(CANCELABLE, true);
        if(cancelAble){
        	login_cancel_btn.setVisibility(View.GONE);
        }
//        Bundle bundle=intent.getBundleExtra("BUNDLE");
//        View.OnClickListener vc=(OnClickListener) bundle.get("clicker");
//        login_sure_btn.setOnClickListener(vc);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login_sure_btn:
			finish();
			break;
		case R.id.login_cancel_btn:
			finish();
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&!login_cancel_btn.isClickable()){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public static void showMessage(Context context,String msg,boolean cancelAble){
		Intent i=new Intent(context,MDialog.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(MSG, msg);
		i.putExtra(CANCELABLE, cancelAble);
		
//		 Bundle bundle=new Bundle();
//		 bundle.
//	        View.OnClickListener vc=(OnClickListener) bundle.get("clicker");
//	        login_sure_btn.setOnClickListener(vc);
		
		context.startActivity(i);
	}

}
