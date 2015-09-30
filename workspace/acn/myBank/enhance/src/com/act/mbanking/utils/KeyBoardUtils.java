package com.act.mbanking.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

public final class KeyBoardUtils {
	/**
     * 隐藏键盘
     * 
     * @param view
     */
	public static boolean hideSoftInputFromWindow(Context context,IBinder windowToken) {
		InputMethodManager imm = (InputMethodManager) context .getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
//           return imm.hideSoftInputFromWindow(windowToken, 0);
           return imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
		return false;
	}
}
