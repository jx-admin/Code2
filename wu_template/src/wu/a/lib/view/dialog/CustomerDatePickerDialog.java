package wu.a.lib.view.dialog;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class CustomerDatePickerDialog extends DatePickerDialog {
    private final static String empytStr="",mDatePicker="mDatePicker";

    public CustomerDatePickerDialog(Context context,
            OnDateSetListener callBack, int year, int monthOfYear,
            int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    	setTitle((monthOfYear) + "��" + dayOfMonth + "��");
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
    	setTitle((month + 1) + "��" + day + "��");
    }
    
    
    
    public static void test(Context activity,OnDateSetListener callBack){
    	final Calendar cal = Calendar.getInstance();
    	DatePickerDialog mDialog = new CustomerDatePickerDialog(activity, callBack,
    	    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
    	    cal.get(Calendar.DAY_OF_MONTH));
    	Field field;
		try {
			field = DatePickerDialog.class.getDeclaredField(mDatePicker);
			field.setAccessible(true); 
			Object obj = field.get(mDialog); 
			
			DatePicker dp=(DatePicker) obj;
			View v=dp;
			while(v instanceof ViewGroup){
			    v=((ViewGroup)v).getChildAt(0);
			}
//			((View)v.getParent()).setVisibility(View.GONE);
			((ViewGroup)v.getParent().getParent()).getChildAt(2).setVisibility(View.GONE);
//			((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	mDialog.show();
//    	((CustomerDatePickerDialog) mDialog).setYearGone();
    	
    } 
    
}