package wu.a.lib.view;

import wu.a.lib.view.dialog.CustomerDatePickerDialog;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

public class DatePickerDialogActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		testCustomerDatePickerDialog(this);
	}
	
	private void testCustomerDatePickerDialog(final Context context){
		CustomerDatePickerDialog.test(context,new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Toast.makeText(context, year+"/"+monthOfYear+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
		}
	});
	}

}
