package com.custom.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.act.sctc.R;

/**
 * @author junxu.wang
 * 
 */
public class CustomDialog extends Dialog {
	// private static final String MSG="msg";
	// private static final String CANCELABLE="cancelAble";
	// private

	/** A TextView for show message content */
	private TextView messageTv;
	/**  */
	private Button positive_btn;
	
	private boolean backKeyAble;

	public ProgressBar pb;

	public CustomDialog(Context context) {
		super(context);
	}

	private void init() {
		messageTv = (TextView) findViewById(R.id.textView1);
		positive_btn = ((Button) findViewById(R.id.button1));
		pb=(ProgressBar) findViewById(R.id.progressBar1);
	}
	
	public void resetView(){
		pb.setVisibility(View.VISIBLE);
		positive_btn.setVisibility(View.INVISIBLE);
	}

	public void setMessage(CharSequence text) {
		messageTv.setText(text);
	}

	public void setMessage(int resid) {
		messageTv.setText(resid);
	}

	public void addPositiveButton(CharSequence text,
			View.OnClickListener onClickListener) {
		positive_btn.setVisibility(View.VISIBLE);
		positive_btn.setText(text);
		positive_btn.setOnClickListener(onClickListener);
	}

	public void addPositiveButton(int resid,
			View.OnClickListener onClickListener) {
		positive_btn.setVisibility(View.VISIBLE);
		positive_btn.setText(resid);
		positive_btn.setOnClickListener(onClickListener);
	}

	public void setBackKeyAble(boolean backKey){
		this.backKeyAble=backKey;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return !backKeyAble;
		}
		return super.onKeyDown(keyCode, event);
	}// alertDialog.setCancelable(false);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����ʾ����ı�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ʾϵͳ�ı�����
		setContentView(R.layout.download_page);
		Window w = getWindow();
		w.setBackgroundDrawableResource(R.color.transparency_color);
		w.setTitle(null);
		WindowManager.LayoutParams wl = w.getAttributes();
		// wl.alpha = 100;
		w.setAttributes(wl);
		setCancelable(false);
		init();
	}

	public static CustomDialog createDialog(Context context, int resId) {
		final CustomDialog cd = new CustomDialog(context);
		cd.setCancelable(false);
		cd.show();
		cd.setMessage(resId);
		cd.addPositiveButton(android.R.string.ok, new View.OnClickListener() {
			public void onClick(View v) {
				cd.dismiss();
			}
		});
		return cd;
	}

	public static CustomDialog createDialog(Context context, String message) {
		final CustomDialog cd = new CustomDialog(context);
		cd.setCancelable(false);
		cd.show();
		cd.setMessage(message);
		cd.addPositiveButton(android.R.string.ok, new View.OnClickListener() {
			public void onClick(View v) {
				cd.dismiss();
			}
		});
		return cd;
	}

	public void done() {
		pb.setVisibility(View.INVISIBLE);
	}
}
