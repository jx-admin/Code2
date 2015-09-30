package com.accenture.mbank.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

//无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
class NoLineClickSpan extends ClickableSpan {
	String text;

	public NoLineClickSpan(String text) {
		super();
		this.text = text;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(ds.linkColor);
	}
	
	@Override
	public void onClick(View widget) {

	}
}