package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;







import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUtil {
	public static final String ViewArgu1 = "ViewArgu1";
	public static final String ViewArgu2 = "ViewArgu2";
	public static final String ViewArgu3 = "ViewArgu3";
	public static final String ViewArgu4 = "ViewArgu4";
	public static final String ViewArguBundle = "ViewArguBundle";

	public static final String formatString = "yyyy-MM-dd";
	public static final String formatString2 = "yyyy��MM��dd��";

	public static void goToView(Context cxt, Class<?> targetClass) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		cxt.startActivity(intent);
	}

	public static void goToViewAndNewTask(Context cxt, Class<?> targetClass) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		cxt.startActivity(intent);
	}

	public static void goToViewForResult(Activity acy, Class<?> targetClass,
			int code,int requestCode) {
		Intent intent = new Intent(acy, targetClass);
		intent.putExtra(ViewArgu1, String.valueOf(code));
		acy.startActivityForResult(intent, requestCode);
	}

	public static void goToViewByArgu(Context cxt, Class<?> targetClass,
			String argu1) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ViewArgu1, argu1);
		cxt.startActivity(intent);
	}

	public static void goToViewByArgu(Context cxt, Class<?> targetClass,
			String argu1, String argu2) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ViewArgu1, argu1);
		intent.putExtra(ViewArgu2, argu2);
		cxt.startActivity(intent);
	}

	public static String Argu1(Intent intent) {
		if (null != intent) {
			return intent.getStringExtra(ViewArgu1);
		}
		return null;
	}

	public static String Argu2(Intent intent) {
		if (null != intent) {
			return intent.getStringExtra(ViewArgu2);
		}
		return null;
	}
	
	public static String Argu3(Intent intent) {
		if (null != intent) {
			return intent.getStringExtra(ViewArgu3);
		}
		return null;
	}

	public static void showToast(Context cxt, int resId) {
		Toast.makeText(cxt, cxt.getString(resId), Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context cxt, String info) {
		Toast.makeText(cxt, info, Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("SimpleDateFormat")
	public static String getFormatString(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		if (null == date) {
			return sdf.format(new Date());
		} else {
			return sdf.format(date);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String DateString2DateString(String value, String fromFormat,
			String toFormat) {
		SimpleDateFormat fsdf = new SimpleDateFormat(fromFormat);
		SimpleDateFormat tsdf = new SimpleDateFormat(toFormat);
		Date date = null;
		try {
			date = fsdf.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return tsdf.format(date);
	}

	public static void dealFail(Activity activity, Message msg) {
		TextView tv = (TextView) activity.findViewById(R.id.cTetMsg2);
		String tmp = (String) msg.obj;
		
		if (!TextUtils.isEmpty(tmp)) {
			tv.setText(tmp);
		} else if (msg.arg2 > 0) {
			try {
				tmp = activity.getString(msg.arg2);
				tv.setText(tmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tv.setVisibility(View.VISIBLE);
	}
	
	public static void showDialog(Activity activity, boolean mandatoryFlag) {
		AlertDialog.Builder builder = new Builder(activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
		LinearLayout linearLahyout = (LinearLayout) inflater.inflate(R.layout.message_dialog_layout, null);
		builder.setView(linearLahyout);
		final AlertDialog alertDialog = builder.create();
		Button imageButton = (Button) linearLahyout.findViewById(R.id.ok_btn);
		imageButton.setText(activity.getString(R.string.ok));
		Button cancel_ibtn = (Button) linearLahyout.findViewById(R.id.cancel_btn);
		cancel_ibtn.setText(activity.getString(R.string.remind_me_later));
		if(mandatoryFlag){
			cancel_ibtn.setVisibility(View.GONE);
		}else {
			cancel_ibtn.setVisibility(View.VISIBLE);
		}
		
		TextView text = (TextView) linearLahyout.findViewById(R.id.message_text);
		text.setText(activity.getResources().getString(R.string.error_description_popup));
		imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		cancel_ibtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		setDialogWidth(alertDialog, activity);
		alertDialog.show();
	}
	
	private static void setDialogWidth(final AlertDialog alertDialog, Context context) {
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()) * 3 / 4; // 设置宽度
		lp.height = (int) (display.getWidth()) * 2 / 4;
		alertDialog.getWindow().setAttributes(lp);
	}
}
