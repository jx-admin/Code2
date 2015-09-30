package com.accenture.mbank.util;

import java.text.NumberFormat;
import java.util.Locale;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**Amount InputFilter for Italy
 * @author junxu.wang
 *
 */
public class AmountItalyInputFilter{
	public static final String TAG=AmountItalyInputFilter.class.getSimpleName();
	public static final String EMPYTY="",COMMA=",",DOT=".";
	public static final int FROM_USER=0,FROM_WATCHER=1;
	public int fromUser;
	String ch = null;
	EditText amount_et;
	TextWatcher feedbackTextWatcher;
	public AmountItalyInputFilter(EditText amount_et,TextWatcher feedbackTextWatcher){
		this.feedbackTextWatcher=feedbackTextWatcher;
		this.amount_et=amount_et;
		amount_et.setOnFocusChangeListener(amountOnFocusChangeListener);
		amount_et.setFilters(mInputFilter);
		fromUser=0;
//		amount_et.addTextChangedListener(mTextWatcherDigit);
	}
	
	
	InputFilter [] mInputFilter=new InputFilter[] { new InputFilter() {
		
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if(fromUser==FROM_WATCHER){
//			 Log.d("inputFilter","user source:"+source+" start:"+start+" end:"+end+" dest:"+dest+" dstart:"+dstart+" dend:"+dend);
				return source;
			}else{
//			 Log.d("inputFilter","source:"+source+" start:"+start+" end:"+end+" dest:"+dest+" dstart:"+dstart+" dend:"+dend);
			}
			
			String s = source.toString().replace('.',',');
			int sourceCommaIndex = s.indexOf(',');
			int sourceCommaIndex2 = s.lastIndexOf(',');
			if (sourceCommaIndex2 != sourceCommaIndex) {// more one ',' in source
				s = s.replace(COMMA, EMPYTY);
				sourceCommaIndex=-1;
			}else if (sourceCommaIndex >= 0) {
				int destCommaIndex = dest.toString().indexOf(',');
				if (destCommaIndex >= 0) {
					s = s.replace(COMMA, EMPYTY);// delete all ',',because of ',' is alread in dest.
				}
				int curSelectionPos=Selection.getSelectionEnd(amount_et.getText());
				if(curSelectionPos<amount_et.getText().length()&&amount_et.getText().toString().charAt(curSelectionPos)==','){
					Selection.setSelection((Spannable) amount_et.getText(), curSelectionPos+1);
				}
			}
			source = s;
			// Log.d(TAG,"return:"+source);
			return source;
		}
	} };

	
	TextWatcher mTextWatcherDigit=new TextWatcher() {
		int lastDigitCount=0;
		int minDigits=2;
		int maxdigits=2;
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (fromUser == FROM_USER) {
				int realLength=s.length();
				if (realLength > 0) {
					int curSelectionIndex = Selection.getSelectionEnd(s);
					
					int curCommaIndex=s.toString().indexOf(',');
					int digitNeed=0;
					if(curCommaIndex<0){
						if(lastDigitCount<=0){
							digitNeed=3;
						}else{
							digitNeed=1;
							s.insert(s.length()-lastDigitCount,COMMA);
						}
					}else{
						int curDigitCount=s.length()-1-curCommaIndex;
						digitNeed=lastDigitCount-curDigitCount;
						if(curDigitCount>maxdigits){
							s.delete(realLength-curDigitCount+maxdigits,realLength);
						}
					}
					
					String newStr = formateAmount(s.toString().trim(), minDigits, maxdigits);
					fromUser = FROM_WATCHER;
					amount_et.setText(newStr);
					curSelectionIndex+=newStr.length()-realLength-digitNeed;
					if(curSelectionIndex<0){
						curSelectionIndex=0;
					}else if(curSelectionIndex>newStr.length()){
						curSelectionIndex=newStr.length();
					}
					Selection.setSelection((Spannable) amount_et.getText(), curSelectionIndex);
//					Log.d(TAG, "fromUser" + s.toString() + " " + newStr + " s" + ss);
					return;
				}
				lastDigitCount=s.length()-1-s.toString().indexOf(',');
				if(feedbackTextWatcher!=null){
					feedbackTextWatcher.afterTextChanged(s);
				}
			} else {
				lastDigitCount=s.length()-1-s.toString().indexOf(',');
				if(feedbackTextWatcher!=null){
					feedbackTextWatcher.afterTextChanged(s);
				}
				fromUser = FROM_USER;
//				Log.d(TAG, "no fromUser" + s.toString());
			}
		}
	};
	
	TextWatcher mTextWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (fromUser == FROM_USER) {
				int realLength=s.length();
				if (realLength > 0) {
					int dotPos=s.toString().indexOf(',');
					if(dotPos>=0){
						if(dotPos==realLength-1){
							if(feedbackTextWatcher!=null){
								feedbackTextWatcher.afterTextChanged(s);
							}
							return;
						}else if(realLength-dotPos>3){
							fromUser=1;
							amount_et.setText(s.subSequence(0, dotPos+3));
							Selection.setSelection((Spannable) amount_et.getText(),amount_et.getText().length());
							return;
						}
					}
					int ss = Selection.getSelectionEnd(s);
					boolean isEnd=false;
					if(ss>=realLength-1){
						isEnd=true;
					}
					String newStr = formateAmount(s.toString().trim(), 0, 2);
//					Log.d(TAG, "fromUser" + s.toString() + " " + newStr + " s" + ss);
					fromUser = 1;
					amount_et.setText(newStr);
					if(isEnd){
						Selection.setSelection((Spannable) amount_et.getText(),amount_et.getText().length());
					}else{
						ss += newStr.length() - realLength;
						if(ss>amount_et.getText().length()){
							ss=amount_et.getText().length();
						}else if(ss<0){
							ss=0;
						}
						Selection.setSelection((Spannable) amount_et.getText(), ss);
					}
					return;
				}
				if(feedbackTextWatcher!=null){
					feedbackTextWatcher.afterTextChanged(s);
				}
			} else {
				if(feedbackTextWatcher!=null){
					feedbackTextWatcher.afterTextChanged(s);
				}
				fromUser = 0;
//				Log.d(TAG, "no fromUser" + s.toString());
			}
		}
	};
	
	
	
	public static final Locale locale = Locale.ITALY;
	public static String formateAmount(String str, int minimumFractionDigits,
			int maximumFractionDigits) {
		NumberFormat format = NumberFormat.getInstance(locale);
		try {
			str=str.replace(DOT,EMPYTY);
			Number number = format.parse(str);
			double amount = number.doubleValue();
			format.setMinimumFractionDigits(minimumFractionDigits);
			format.setMaximumFractionDigits(maximumFractionDigits);
			str = format.format(amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	OnFocusChangeListener amountOnFocusChangeListener=new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(!hasFocus){
				String newStr = formateAmount(amount_et.getText().toString().trim(), 2, 2);
				fromUser = FROM_WATCHER;
				amount_et.setText(newStr);
				fromUser = FROM_USER;
			}
		}
	};
	
}

