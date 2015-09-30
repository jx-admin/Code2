package wu.a.lib.utils;

import java.text.NumberFormat;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class AmountTextWatcher implements TextWatcher {
	private final static String TAG = "AmountTextWatcher";
	boolean myself;

	int oldPos;

	String beforStr = "";

	String newStr;

	EditText mEditText;

	public AmountTextWatcher(EditText mEditText) {
		this.mEditText = mEditText;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		Log.d(TAG, "beforeTextChanged s=" + s + " start=" + start + " count="
				+ count + " after=" + after);

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.d(TAG, "onTextChanged s=" + s + " start=" + start + " before="
				+ before + " count=" + count);
		if (s.toString().equals(beforStr)) {// this is fixed last time
			Log.d(TAG, "no change & return.");
			return;
		}
		if (TextUtils.isEmpty(s)) {// chars is empty,needn't fix
			return;
		}

		String str = s.toString();
		int digit = str.indexOf(',');
		if (digit != str.lastIndexOf(',')) {// has two ",",fix to only one
			mEditText.setText(beforStr);
			Spannable spannable = mEditText.getText();
			if (oldPos > 0 && oldPos < beforStr.length())
				Selection.setSelection(spannable, start);
			myself = true;
			oldPos = start;

			Log.d(TAG, "need fix <- double ','");
			return;
		}

		if (count > 0 || before > 0) {// spylit by '.'
			newStr = s.toString().substring(start - before, start + count);
			Log.d(TAG, "newStr:" + newStr);

			oldPos = start + count - before;
			if (before > 0) {
				oldPos++;
			}
			StringBuffer sb = new StringBuffer(s);
			int j = s.toString().indexOf(",") - 1;
			if (j < 0) {
				j = s.length() - 1;
			}
			for (int tmpPos = 0; j > 0; j--) {// > not >=

				if (s.charAt(j) == '.') {
					if (tmpPos != 3) {
						sb.deleteCharAt(j);
						if (j <= oldPos) {
							oldPos--;
						}
						myself = true;
					} else {
						tmpPos = 0;
					}
				} else {
					if (tmpPos == 2) {
						sb.insert(j, '.');
						if (j < oldPos) {
							oldPos++;
						}
						myself = true;
						tmpPos = 0;
					} else {
						tmpPos++;
					}
				}
			}
			beforStr = sb.toString();
			if (myself) {
				mEditText.setText(beforStr);
				if (oldPos > 0 && oldPos < beforStr.length())
					Selection.setSelection(mEditText.getText(), oldPos);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		Log.d(TAG, "afterTextChanged s=" + s);
		// TODO Auto-generated method stub
		if (myself) {
			myself = false;
		}
	}

	private String formate(String str) {
		if (str == null) {
			return null;
		}
		// translate to uausal digits
		str = str.replace(".", "");
		str = str.replace(',', '.');
		try {// is need very times?no
			double amount = Double.parseDouble(str);
			amount += 0.001;// to fix for two digits ,not better here
			NumberFormat format = NumberFormat.getInstance();
			format.setMinimumFractionDigits(2);
			format.setMaximumFractionDigits(2);
			str = format.format(amount);

			// reset formate
			str = str.replace(".", ";");
			str = str.replace(',', '.');
			str = str.replace(';', ',');
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
