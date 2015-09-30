package com.custom.view.utils;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

public class MenuLinearManager implements OnClickListener {
	private ViewGroup layout;
	private OnItemClickListener onItemClickListener;
	protected View currentView;
	public MenuLinearManager(ViewGroup layout){
		this.layout=layout;
		for(int i=layout.getChildCount()-1;i>=0;i--){
			layout.getChildAt(i).setOnClickListener(this);
		}
	}
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		this.onItemClickListener=onItemClickListener;
	}
	
	public void addView(View child){
		layout.addView(child);
		child.setOnClickListener(this);
	}

	public void addView(View child, LayoutParams lp) {
		layout.addView(child,lp);
		child.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		currentView=v;
		if(onItemClickListener!=null){
			onItemClickListener.onItemClick(layout,v);
		}
	}
	
	public interface OnItemClickListener{
		public abstract void onItemClick(ViewGroup layout,View childView);
	}

}
