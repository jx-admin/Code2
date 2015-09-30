package com.example.jxdigitpasswordkeypad;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtil {
	private static InputMethodManager imm;

	// 显示输入法
	public static void show(Context context, View focusView) {
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
	}

	// 隐藏输入发
	public static void hide(Context context) {
		View view = ((Activity) context).getWindow().peekDecorView();
		if (view != null && view.getWindowToken() != null) {
			imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	// 调用该方法；键盘若显示则隐藏; 隐藏则显示
	public static void toggle(Context context) {
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// 判断InputMethod的当前状态
	public static boolean isShow(Context context, View focusView) {
		Object obj = context.getSystemService(Context.INPUT_METHOD_SERVICE);
		System.out.println(obj);
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean bool = imm.isActive(focusView);
		List<InputMethodInfo> mInputMethodProperties = imm
				.getEnabledInputMethodList();

		// Configuration cfg = context.getResources().getConfiguration();
		// int ii = cfg.hardKeyboardHidden;
		// int or = cfg.orientation;
		// Log.e("xxxxxxxxxxxxxx", ii+" "+or);
		final int N = mInputMethodProperties.size();
		for (int i = 0; i < N; i++) {
			InputMethodInfo imi = mInputMethodProperties.get(i);
			if (imi.getId().equals(
					Settings.Secure.getString(context.getContentResolver(),
							Settings.Secure.DEFAULT_INPUT_METHOD))) {
				// imi contains the information about the keyboard you are using
				break;
			}
		}
		return bool;
	}

}
