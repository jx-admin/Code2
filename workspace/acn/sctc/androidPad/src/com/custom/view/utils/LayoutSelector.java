package com.custom.view.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.act.sctc.R;

public class LayoutSelector implements OnTouchListener {
	private View pressView,normalView,focusView,disAbleView;
	private ViewGroup vg;
	private OnClickListener l;
	private boolean enable=true;
	public LayoutSelector(){
		
	}
	
	public void setLayout(ViewGroup vg){
		pressView=vg.findViewById(R.id.pressview);
		normalView=vg.findViewById(R.id.normalview);
		focusView=vg.findViewById(R.id.focusview);
		this.vg=vg;
		vg.setOnTouchListener(this);
	}
	
	public View getView(){
		return vg;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!enable){
			return true;
		}
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			setPress();
			break;
		case MotionEvent.ACTION_UP:
			if(isPress&&l!=null){
				l.onClick(vg);
			}
			setNormal();
			break;
		case MotionEvent.ACTION_CANCEL:
			setNormal();
			return false;
		case MotionEvent.ACTION_OUTSIDE:
			setNormal();
			return false;
		case MotionEvent.ACTION_MOVE:
			if(event.getX()<0||event.getX()>vg.getWidth()||event.getY()<0||event.getY()>vg.getHeight()){
				setNormal();
			}else{
				setPress();
			}
			break;
		}
		return true;
	}
	
	private boolean isPress;
	public void setPress(){
		if(isPress){
			return ;
		}isPress=!isPress;
		if(pressView!=null){
			pressView.setVisibility(View.VISIBLE);
		}
		if(normalView!=null){
			normalView.setVisibility(View.GONE);
		}
	}
	public void setNormal(){
		if(!isPress){
			return ;
		}
		isPress=!isPress;
		if(pressView!=null){
			pressView.setVisibility(View.GONE);
		}
		if(normalView!=null){
			normalView.setVisibility(View.VISIBLE);
		}
	}
	public void setOnclickListener(OnClickListener l){
		this.l=l;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
		if(!enable){
			if(disAbleView!=null){
				disAbleView.setVisibility(View.VISIBLE);
				if(pressView!=null){
					pressView.setVisibility(View.GONE);
				}
				if(normalView!=null){
					normalView.setVisibility(View.GONE);
				}
			}
		}
	}

}
