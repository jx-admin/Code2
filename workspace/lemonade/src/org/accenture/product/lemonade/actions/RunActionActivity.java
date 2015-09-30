package org.accenture.product.lemonade.actions;

import android.app.Activity;
import android.os.Bundle;

public class RunActionActivity extends Activity {

	public static final String ACTION_LAUNCHERACTION = "org.accenture.product.lemonade.ACTION_LAUNCHERACTION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LauncherActions.getInstance().launch(getIntent());
		finish();
	}
}
