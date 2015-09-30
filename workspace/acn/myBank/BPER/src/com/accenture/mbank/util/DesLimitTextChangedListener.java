package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;

public class DesLimitTextChangedListener implements TextWatcher{
    public static final int DES_LENGTH_ON=105,DES_LENGTH_OFF=140;
    int description_of_payment_etriptionLenth=DES_LENGTH_OFF;
    final Pattern p = Pattern.compile(Contants.PATTERN);
	 
	boolean isAuto;
	boolean charLimit;
	String lastText;
	
	Context context;
	TextView des_length_alerter_tv;
	EditText description_of_payment_et;
	public DesLimitTextChangedListener(Context context, EditText description_of_payment_et,TextView des_length_alerter_tv){
		this.context=context;
		this.des_length_alerter_tv=des_length_alerter_tv;
		this.description_of_payment_et=description_of_payment_et;
		description_of_payment_et.addTextChangedListener(this);
		setDestriptionMaxLenth(DES_LENGTH_OFF);
	}
	
	public void setCharLimit(boolean charLimit){
		this.charLimit=charLimit;
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		if(!isAuto){

			String a = null;
			String string =null;
			string = s.toString();
			//length manager
			if(s.length()>description_of_payment_etriptionLenth){
				if(lastText!=null){
					isAuto=true;
					description_of_payment_et.setText(lastText);
					Selection.setSelection(description_of_payment_et.getText(), description_of_payment_et.getText().length());
					return;
				}else{
					string=string.substring(0,description_of_payment_etriptionLenth);
				}
			}
			//char filter manager
			if(!isAuto&&charLimit){
				for (int i = 0; i < string.length(); i ++) {
					char c = string.charAt(i);
					Matcher m = p.matcher(Character.toString(c));
					if (!m.find()) {
						a = string.replace(Character.toString(c), "");
					}
				}
				if(a!=null){
					isAuto=true;
					s.clear();
					description_of_payment_et.setText(a);
					Selection.setSelection(description_of_payment_et.getText(), description_of_payment_et.getText().length());
					ViewUtil.showDialog(((BaseActivity)context), true);
					return;
				}
			}
		}else{
			isAuto=false;
		}
		lastText=s.toString();
	updataDescriptionLength();
	        
	}
	
	public void setDestriptionMaxLenth(int max){
		description_of_payment_etriptionLenth=max;
		updataDescriptionLength();
	}
	
	private void updataDescriptionLength(){
		des_length_alerter_tv.setText(context.getString(R.string.des_length_alerter, description_of_payment_etriptionLenth-description_of_payment_et.getText().length()));
	}
}
