package com.accenture.mbank.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class OnViewClickListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		if(!(v instanceof EditText)){
			Context context=v.getContext();
			if(context instanceof Activity){
				KeyBoardUtils.hideSoftInputFromWindow((Activity) context);
			}else{
				KeyBoardUtils.hideSoftInputFromWindow(context, v.getWindowToken());
			}
		}
	}
	public void onViewClick(View v){
		
	}

}
