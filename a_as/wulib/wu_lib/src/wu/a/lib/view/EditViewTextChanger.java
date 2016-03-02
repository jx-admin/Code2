package wu.a.lib.view;

import wu.a.lib.utils.AmountItalyInputFilter;
import wu.a.lib.utils.AmountTextWatcher;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditViewTextChanger{
	public static final String TAG = "EditViewTextChanger";
	AmountTextWatcher mItalyAmountWatcher;

	public EditViewTextChanger(EditText amount_et) {
//		amount_et.addTextChangedListener(new AmountTextWatcher(amount_et));
		amount_et.setFilters(new InputFilter[] { new AmountItalyInputFilter() });
		
		amount_et.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) {
                    Log.d(TAG, "onEditorAction is null ,onEditorAction " + actionId);
                } else {
                    Log.d(TAG,
                            "onEditorAction:onEditorAction" + actionId + "key:"
                                    + event.getKeyCode());
                }
                return false;
            }
        });

	}

}
