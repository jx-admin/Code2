package com.accenture.mbank.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
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
           return imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
		return false;
	}
	public static boolean hideSoftInputFromWindow(Activity context) {
		InputMethodManager imm = (InputMethodManager) context .getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View curFv=context.getCurrentFocus();
			if(curFv!=null){
				return imm.hideSoftInputFromWindow(curFv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
        }
		return false;
	}
}
