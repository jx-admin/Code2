package wu.a.lib.view;

import wu.a.lib.view.dialog.CustomDialog;
import wu.a.lib.view.dialog.CustomDialogDemo;
import wu.a.lib.view.dialog.CustomPopu;
import wu.a.template.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class CustomDialogActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		testCustomerDatePickerDialog(this);
	}
	
	private void testCustomerDatePickerDialog(final Context context){
		new CustomDialogDemo(context).show();
		CustomDialog.createDialog(context,"hello,here is message...");
		
		final CustomDialog cd = new CustomDialog(context);
		cd.setCancelable(false);
		cd.show();
		cd.setMessage("hi");
		cd.addPositiveButton(android.R.string.ok, new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
			}
		});
		cd.addNegativeButton(android.R.string.cancel, new View.OnClickListener() {
			public void onClick(View v) {
				cd.dismiss();
				showPopDialog();
			}
		});
		
		cd.addNeutralButton("neutral", new View.OnClickListener() {
			public void onClick(View v) {
				cd.setTitle("new title");
			}
		});
	}
	
	private void showPopDialog(){
		CustomPopu popuWin = new CustomPopu(this);
		popuWin.setOutsideTouchable(true);
		popuWin.setFocusable(false);
		popuWin.setOnClickListener(this);

		View rooView = LayoutInflater.from(this).inflate(R.layout.pwin_aemm,
				null);
		popuWin.showPopu(rooView, 200, 200);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
