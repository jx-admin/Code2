package com.aess.aemm.view.sharing;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aess.aemm.R;

public class MenuUtils {
	List<View>menuViews=new ArrayList<View>();
	View current;
	ViewGroup vg;
	public MenuUtils(){
	}
	
	public void initItem(Context context,ViewGroup gp,String []names,OnClickListener l){
		this.vg=gp;
		// 参数设置
		LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT,
				1);
		menuLinerLayoutParames.gravity = Gravity.CENTER_HORIZONTAL;
		
		// 添加TextView控件
		for(int i = 0;i < names.length; i++){
			TextView tvMenu = new TextView(context);
			// 设置标识值
			tvMenu.setTag(i);
//			tvMenu.setLayoutParams(new LayoutParams(100,30)); 
			tvMenu.setPadding(30, 14, 30, 10);
			tvMenu.setText(names[i]);
			tvMenu.setTextColor(Color.WHITE);
			tvMenu.setGravity(Gravity.CENTER_HORIZONTAL);
			tvMenu.setOnClickListener(l);
			gp.addView(tvMenu);
		}
	}
	
	public void setCurrent(View v){
		if(current!=null){
			current.setBackgroundDrawable(null);
		}
		current=v;
		if(v!=null){
			current.setBackgroundResource(R.drawable.classify_down);
		}
	}
	
	public void setCurrent(int index){
		if(index>=0&&index<vg.getChildCount()){
			View c=vg.getChildAt(index);
			setCurrent(c);
		}
	}
	
	public int getItemX(int index){
		int x=0;
		if(index>=0&&index<vg.getChildCount()){
			for(int i=0;i<index;i++){
				x+=vg.getChildAt(i).getWidth();
			}
		}
		
		return x;
	}

	

}
