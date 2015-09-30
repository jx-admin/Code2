package com.custom.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class CustomPopu extends PopupWindow{

	public int white = 0xffFFFFFF;
	public int huise = 0xb0000000;

	public CustomPopu() {
		super();
	}

	public CustomPopu(Context cxt) {
		super(cxt);
		_cxt = cxt;
	}

	public CustomPopu(Context cxt, AttributeSet attrs) {
		super(cxt, attrs);
		_cxt = cxt;
	}

	public void setOnClickListener(OnClickListener listener) {
		_listener = listener;
	}

	public void showPopu(View view, int weight, int height,int x, int y) {
//		setContentView(view);
//		
//		setWidth(LayoutParams.WRAP_CONTENT);
//		setHeight(LayoutParams.WRAP_CONTENT);

//		ColorDrawable dw = new ColorDrawable(0xffff0000);
//		setBackgroundDrawable(dw);

//		int[] loc = new int[2];
//		view.getLocationOnScreen(loc);
//
//		showAtLocation(view, Gravity.LEFT | Gravity.TOP,
//				loc[0] + x, loc[1]+y);
	}


	Context _cxt;
	OnClickListener _listener;
}
