package com.act.sctc.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.sctc.R;

public class PhoneParameterItemView extends LinearLayout {
	private TextView titleTv;
	private TextView textContent;
	private Context context;
	private String title;

	public PhoneParameterItemView(Context context) {
		this(context, null);
	}

	public PhoneParameterItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PhoneParameterItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		iniController(attrs);
	}

	private void iniController(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.PhoneParameterItemView);
			title = type.getString(R.styleable.PhoneParameterItemView_title_text);
		}
		if (title == null) {
			title = "";
		}
		LayoutInflater lInflater = LayoutInflater.from(context);
		titleTv = (TextView) lInflater.inflate(R.layout.phone_parameter_item_title_model, null);
		titleTv.setText(title);
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		addView(titleTv, lParams);
		lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textContent = (TextView) lInflater.inflate(R.layout.phone_parameter_item_content_model, null);
		addView(textContent, lParams);
	}

	public void setTitle(String str) {
		titleTv.setText(str);
	}

	public void setContext(String str) {
		textContent.setText(str);
	}

}
