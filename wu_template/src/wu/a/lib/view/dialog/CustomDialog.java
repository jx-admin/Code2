package wu.a.lib.view.dialog;

import wu.a.template.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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
	/** A TextView for show title Message */
	private TextView titleTv;
	/**  */
	private Button positive_btn;
	/**  */
	private Button negative_btn;
	
	private Button neutral_btn;
	
	private boolean backKeyAble;

	public CustomDialog(Context context) {
		super(context);
	}

	private void init() {
		messageTv = (TextView) findViewById(R.id.message_tv);
		titleTv = (TextView) findViewById(R.id.title_tv);
		positive_btn = ((Button) findViewById(R.id.positive_btn));
		negative_btn = (Button) findViewById(R.id.negative_btn);
		neutral_btn=(Button) findViewById(R.id.neutral_btn);
	}

	public void setMessage(CharSequence text) {
		messageTv.setText(text);
	}

	public void setMessage(int resid) {
		messageTv.setText(resid);
	}

	public void setTitle(CharSequence text) {
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(text);
	}

	public void setTitle(int resid) {
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(resid);
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

	public void addNegativeButton(CharSequence text,
			View.OnClickListener onClickListener) {
		negative_btn.setVisibility(View.VISIBLE);
		negative_btn.setText(text);
		negative_btn.setOnClickListener(onClickListener);
		setCancelable(true);
	}

	public void addNegativeButton(int resid,
			View.OnClickListener onClickListener) {
		negative_btn.setVisibility(View.VISIBLE);
		negative_btn.setText(resid);
		negative_btn.setOnClickListener(onClickListener);
		setCancelable(true);
	}
	
	public void addNeutralButton(CharSequence text,
			View.OnClickListener onClickListener) {
		neutral_btn.setVisibility(View.VISIBLE);
		neutral_btn.setText(text);
		neutral_btn.setOnClickListener(onClickListener);
	}
	
	public void addNeutralButton(int resid,
			View.OnClickListener onClickListener){
		neutral_btn.setVisibility(View.VISIBLE);
		neutral_btn.setText(resid);
		neutral_btn.setOnClickListener(onClickListener);
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
		setContentView(R.layout.dialog);
		Window w = getWindow();
		w.setBackgroundDrawableResource(R.drawable.dialog_bk);
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
}
