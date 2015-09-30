package com.aemm.config_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Activity2 extends Activity {
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RelativeLayout relativeLayout = new RelativeLayout(this);
		Button button = new Button(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			button.setText("Item name = " + bundle.getString("NAME")
					+ " --- Go Back ");
		} else {
			button.setText("Go Back");
		}
		context = this;
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, Splash.class));
				finish();
			}
		});
		relativeLayout.addView(button);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		setContentView(relativeLayout, params);
	}

}
