package com.aess.aemm.view.popu;

import com.aess.aemm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class OLLayout extends LinearLayout {
	
	public OLLayout(Context context) {
		super(context);
		_cxt = context;
	}

	public OLLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		_cxt = context;
	}

	public OLLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		_cxt = context;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		lv = (ListView) findViewById(R.id.oliCheckBox);
		OLAdapter adapter = new OLAdapter(_cxt);
		lv.setAdapter(adapter);
		lv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				
			}
		});
	}
	
	private ListView lv;
	private Context _cxt;
}
