package com.custom.view.utils;

import com.act.sctc.R;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuBottomArrow extends MenuLinearManager{
	private ViewGroup layout;
	private OnItemClickListener onItemClickListener;
	public MenuBottomArrow(ViewGroup layout){
		super(layout);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v.getId()>0)
		setSelectFalse(true);
	}

	public void setSelectFalse(boolean focus){
		if(focus){
			Resources resources=currentView.getResources();
			Drawable downDrawable=resources.getDrawable(R.drawable.down_menu_arrow_up);
			downDrawable.setBounds(0, 0, 7,6);
			((Button)currentView).setCompoundDrawables(null, null, null, downDrawable);
		}else{
			Resources resources=currentView.getResources();
			Drawable downDrawable=resources.getDrawable(R.drawable.down_menu_arrow_donw);
			downDrawable.setBounds(0, 0, 7,6);
			((Button)currentView).setCompoundDrawables(null, null, null, downDrawable);
		}
	}
}
