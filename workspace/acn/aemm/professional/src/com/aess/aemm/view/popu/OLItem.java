package com.aess.aemm.view.popu;

import com.aess.aemm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OLItem extends RelativeLayout {
	
	public OLItem(Context context) {
		super(context);
	}
	
	public OLItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OLItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		cb = (CheckBox) findViewById(R.id.oliCheckBox);
		iv = (ImageView)findViewById(R.id.oliImageView);
		tv = (TextView)findViewById(R.id.oliTextView);
	}

	public CheckBox cb;
	public ImageView iv;
	public TextView tv;
}
