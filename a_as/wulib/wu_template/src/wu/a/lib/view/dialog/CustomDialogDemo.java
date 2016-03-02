package wu.a.lib.view.dialog;

import wu.a.template.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class CustomDialogDemo extends Dialog {

	public CustomDialogDemo(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_custom_demo);
		Window w = getWindow();
		w.setBackgroundDrawable(null);
		w.setTitle(null);
		WindowManager.LayoutParams wl = w.getAttributes();
		wl.alpha = 70;
		w.setAttributes(wl);

		ImageButton close = (ImageButton) findViewById(R.id.ImageButton01);
		close.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

}
