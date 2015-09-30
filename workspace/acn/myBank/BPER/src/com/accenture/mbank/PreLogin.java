package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.accenture.mbank.util.Contants;

public class PreLogin extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.prelogin);

		for (int i = 0; i < Contants.idBankButton.length; i++) {
			ImageButton btn = (ImageButton) findViewById(Contants.idBankButton[i]);
			btn.setTag(Contants.strBankCode[i]);
			btn.setOnClickListener(this);
		}
	}

	public void onClick(View v) {
		String strBankCode = (String) v.getTag();
		final SharedPreferences settings = this.getSharedPreferences(
				Contants.SETTING_FILE_NAME, MODE_PRIVATE);
		settings.edit().putString(Contants.BANK_CODE, strBankCode).commit();

		Intent intent = new Intent(PreLogin.this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);

		finish();
	}
}
