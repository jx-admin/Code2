package com.android.accenture.aemm.express;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	private static final String MSG="msg";
	private static final String CANCELABLE="cancelAble";
//	private 
	private TextView messageTv;
	private TextView titleTv;
	private Button login_sure_btn;
	private Button login_cancel_btn;

	public CustomDialog(Context context) {
		super(context);
	}
	private void init(){
		messageTv=(TextView) findViewById(R.id.message_tv);
		titleTv=(TextView) findViewById(R.id.title_tv);
	     login_sure_btn=((Button) findViewById(R.id.login_sure_btn));
	     login_cancel_btn=(Button) findViewById(R.id.login_cancel_btn);
	}
	public void setMessage(CharSequence text){
		messageTv.setText(text);
	}
	public void setMessage(int resid){
		messageTv.setText(resid);
	}
	public void setTitle(CharSequence text){
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(text);
	}
	public void setTitle(int resid){
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(resid);
	}
	public void addPositiveButton(CharSequence text,View.OnClickListener onClickListener){
		login_sure_btn.setVisibility(View.VISIBLE);
		login_sure_btn.setText(text);
		login_sure_btn.setOnClickListener(onClickListener);
	}
	public void addPositiveButton(int resid,View.OnClickListener onClickListener){
		login_sure_btn.setVisibility(View.VISIBLE);
		login_sure_btn.setText(resid);
		login_sure_btn.setOnClickListener(onClickListener);
	}
	public void addNegativeButton(CharSequence text,View.OnClickListener onClickListener){
		login_cancel_btn.setVisibility(View.VISIBLE);
		login_cancel_btn.setText(text);
		login_cancel_btn.setOnClickListener(onClickListener);
		setCancelable(true);
	}
	public void addNegativeButton(int resid,View.OnClickListener onClickListener){
		login_cancel_btn.setVisibility(View.VISIBLE);
		login_cancel_btn.setText(resid);
		login_cancel_btn.setOnClickListener(onClickListener);
		setCancelable(true);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&!login_cancel_btn.isClickable()){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}//	  alertDialog.setCancelable(false);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);//不显示程序的标题栏   
	     getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);//不显示系统的标题栏 
		setContentView(R.layout.dialog);
		Window w = getWindow();
		w.setBackgroundDrawableResource(R.drawable.login_dialog_bk);
		w.setTitle(null);
		WindowManager.LayoutParams wl = w.getAttributes();
//		wl.alpha = 100;
		w.setAttributes(wl);
		setCancelable(false);
        init();
	}
	public static void createDialog(Context context,int resId){
		final CustomDialog cd=new CustomDialog(context);
        cd.setCancelable(false);
        cd.show();
        cd.setMessage(resId);
        cd.addPositiveButton(R.string.sure,  new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    cd.dismiss();
			}
	  });
	}
	public static void createDialog(Context context,String message){
		final CustomDialog cd=new CustomDialog(context);
        cd.setCancelable(false);
        cd.show();
        cd.setMessage(message);
        cd.addPositiveButton(R.string.sure,  new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    cd.dismiss();
			}
	  });
//        cd.addNegativeButton(R.string.cancel, new View.OnClickListener() {
//			   @Override
//			   public void onClick(View v) {
//			   		cd.dismiss();
//			   }
//		});
	}
}
