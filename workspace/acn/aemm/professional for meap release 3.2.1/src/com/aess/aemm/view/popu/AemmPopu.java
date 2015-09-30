package com.aess.aemm.view.popu;

import com.aess.aemm.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AemmPopu extends PopupWindow implements OnTouchListener {

	public int white = 0x00FFFFFF;
	public int huise = 0xb0000000;

	public AemmPopu() {
		super();
	}

	public AemmPopu(Context cxt) {
		super(cxt);
		_cxt = cxt;
	}

	public AemmPopu(Context cxt, AttributeSet attrs) {
		super(cxt, attrs);
		_cxt = cxt;
	}

	public void setOnClickListener(OnClickListener listener) {
		_listener = listener;
	}

	public void showPopu(View view, int weight, int height) {
		initWin(view);

		setWidth(weight);
		setHeight(height);

		ColorDrawable dw = new ColorDrawable(white);
		setBackgroundDrawable(dw);

		int[] loc = new int[2];
		view.getLocationOnScreen(loc);

		showAtLocation(view, Gravity.LEFT | Gravity.TOP,
				loc[0] - weight + view.getWidth(), loc[1] - height);
	}

	private void initWin(View v) {
		View rooView = LayoutInflater.from(_cxt).inflate(R.layout.pwin_aemm,
				null);
		TextView tv = (TextView) rooView.findViewById(R.id.pupdate);

		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pmessage);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pinfo);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.ppsword);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.ppsword);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.ppsword);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pderma);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pmore);
		setText(tv);

		setContentView(rooView);
	}

	private void setText(TextView tv) {
		if (null != _listener)
			tv.setOnClickListener(_listener);

		tv.setOnTouchListener(this);
	}

	Context _cxt;
	OnClickListener _listener;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v instanceof TextView) {
			TextView tv = (TextView) v;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				tv.setTextColor(0xFFFFFF00);
				break;
			}
			case MotionEvent.ACTION_UP: {
				tv.setTextColor(0xFFFFFFFF);
				break;
			}
			default: {
				;
			}
			}
		}
		return false;
	}
}
