package com.aess.aemm.view.msg;

import com.aess.aemm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class MsgItem extends RelativeLayout {

	public MsgItem(Context context) {
		super(context);
	}

	public MsgItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MsgItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		cb = (CheckBox) findViewById(R.id.msgisel);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN: {
//			if (check_area < 0) {
//				initCheckArea();
//			}
//			if (check_area < 0) {
//				return true;
//			}
//		}
//		}
//		return super.onInterceptTouchEvent(ev);
//	}
	
//	public long getMsgId() {
//		return msgId;
//	}
	public String getConmandId(){
		return conmandId;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean handled = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (check_area < 0) {
				initCheckArea();
			}
			if (check_area > 0) {
				if (event.getX() > check_area) {
					mIsInMsgEvent = true;
					handled = true;
				}
			}
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			mIsInMsgEvent = false;
			break;
		}
		case MotionEvent.ACTION_UP: {
			if (check_area > 0) {
				if (mIsInMsgEvent && event.getX() > check_area) {
					toggleCheckMark();
					handled = true;
				}
				mIsInMsgEvent = false;
			}
			break;
		}
		}

		if (handled) {
			postInvalidate();
		} else {
			handled = super.onTouchEvent(event);
		}

		return handled;
	}

	public void setOnMsgSelectListener(MsgSelectListener listener) {
		ilistener = listener;
	}

	private void toggleCheckMark() {
		cb.toggle();
		ilistener.onItemsSelectedListener(conmandId, cb.isChecked());
	}

//	public void setMsdId(long id) {
//		msgId = id;
//	}
	
	public void setConmandId(String conmand){
		conmandId=conmand;
	}

	private void initCheckArea() {
		int[] loc = new int[2];
		loc[0] = -1;
		getLocationInWindow(loc);
		if (loc[0] >= 0) {
			check_area = loc[0] + getWidth() - MsgSelectListener.CHECKMARK_AREA;
		}
	}

	private int check_area = -1;
	private boolean mIsInMsgEvent;
	private CheckBox cb;
	private MsgSelectListener ilistener;
	private String conmandId;
//	private long msgId;
}
