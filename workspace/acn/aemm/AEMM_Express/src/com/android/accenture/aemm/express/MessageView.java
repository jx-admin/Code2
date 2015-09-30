package com.android.accenture.aemm.express;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MessageView extends TextView{
	HallMessagedb message;
	public MessageView(Context context) {
		super(context);
	}

	public MessageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MessageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public MessageView(Context context,HallMessagedb msg){
		super(context);
		setMessage(msg); 
	}
	public void setMessage(HallMessagedb msg) {
		this.message=msg;
		setText(msg.getMessage());
	}
	public boolean isOutTime(){
		return message.isOutTime();
	}

	public void nextTime() {
		message.nextTime();
	}
}
