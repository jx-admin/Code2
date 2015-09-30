package com.aess.aemm.view;

import android.content.Context;
import android.widget.FrameLayout.LayoutParams;

public class MessageView extends TextMarquee{// TextView{
	HallMessagedb message;
	public MessageView(Context context) {
		super(context);
		LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,100);
		this.setLayoutParams(lp);
//		this.setSingleLine();
	}

//	public MessageView(Context context, AttributeSet attrs) {
//		super(context, attrs);
////		this.setEllipsize(TruncateAt.END);
//		this.setSingleLine();
//	}

//	public MessageView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		
//	}
	public MessageView(Context context,HallMessagedb msg){
		super(context);
		LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,100);
		this.setLayoutParams(lp);
		setMessage(msg); 
//		this.setSingleLine();
	}
	public void setMessage(HallMessagedb msg) {
		this.message=msg;
		setText(msg.getMessage());
//		LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		this.setLayoutParams(lp);
//		this.setEllipsize(TruncateAt.END);
//		this.setSingleLine();
//		this.setEllipsize(TruncateAt.MARQUEE);
//		this.setFocusable(true);
//		this.setMarqueeRepeatLimit(1);
//		this.setFocusableInTouchMode(true);
//		this.setSingleLine();
	}
	public boolean isOutTime(){
		return state==STOP&&message.isOutTime();
	}
	public void nextTime() {
		message.nextTime();
	}
}
