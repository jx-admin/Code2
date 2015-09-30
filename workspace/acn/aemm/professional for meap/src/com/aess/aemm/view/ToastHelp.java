package com.aess.aemm.view;

import android.content.Context;
import android.widget.Toast;

import com.aess.aemm.R;
import com.aess.aemm.networkutils.NetUtils;

public class ToastHelp {
	public static final int TOAST_HINT_USER_QUITE = 0;
	
	public static void SendUserQuiteFaile(int value) {
		MainView.sendHintMes(TOAST_HINT_USER_QUITE, value);
	}
	
	public static void showToast(Context cxt, int x, int y) {
		switch(x) {
		case TOAST_HINT_USER_QUITE: {
			String info = NetUtils.getErrorString(cxt, y);
			String head = cxt.getString(R.string.logout_fail);
			head = head + " :¡¡" + info;
			Toast.makeText(cxt, head, Toast.LENGTH_SHORT).show();
		}
		}
	}
}
