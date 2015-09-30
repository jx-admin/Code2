package wu.a.lib.view;

import wu.a.template.R;
import android.app.Activity;
import android.os.Bundle;

public class SwitchButtonActivity extends Activity {

	/** Called when the activity is first created. */
	CoverFlow cf;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchbutton_layout);
	}

}
