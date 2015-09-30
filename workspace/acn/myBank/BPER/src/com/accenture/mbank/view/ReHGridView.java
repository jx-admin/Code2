package com.accenture.mbank.view;

import com.accenture.mbank.util.LogManager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ReHGridView extends GridView {

	public ReHGridView(Context context) {
		super(context);
		super.getViewTreeObserver().addOnGlobalLayoutListener(this);
		// TODO Auto-generated constructor stub
	}

	public ReHGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.getViewTreeObserver().addOnGlobalLayoutListener(this);
		// TODO Auto-generated constructor stub
	}

	public ReHGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.getViewTreeObserver().addOnGlobalLayoutListener(this);
		// TODO Auto-generated constructor stub
	}
	
	int itemCount=0;
	public void onGlobalLayout() {
		LogManager.d("getMeasuredHeight:"+ this.getMeasuredHeight() + " getHeight: " + this.getHeight()+" getMeasuredWidth:"+this.getMeasuredWidth()+ " getCount:"+this.getCount());
		if(this.getCount()<=0){
			return;
		}
		if(itemCount==getCount()){
			return;
		}
		itemCount=getCount();
		View cv=this.getChildAt(0);
		LogManager.d(" chmh:"+cv.getMeasuredHeight()+" chmw:"+cv.getMeasuredWidth()+" ch:"+cv.getHeight());
		
		int column=this.getMeasuredWidth()/cv.getMeasuredWidth();
		int row=(getCount()+column-1)/column;
		LogManager.d(" column:"+column+" row:"+row);
		
		int newHeight=row*(cv.getMeasuredHeight()+this.getMeasuredWidth()/column-cv.getMeasuredWidth()+2);
		LogManager.d(" newHeight:"+newHeight);
		
		ViewGroup.LayoutParams params = this.getLayoutParams();  
		params.height =newHeight;  
		this.setLayoutParams(params);
	}
	

}
