package com.aess.aemm.view.msg;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class MsgGroup extends RelativeLayout {

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		cb = (CheckBox) findViewById(R.id.msggsel);
	}

	public MsgGroup(Context context) {
		super(context);
	}

	public MsgGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MsgGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnClickListener(OnClickListener listener) {
		cb.setOnClickListener(listener);
	}

	public void setPosition(int x) {
		if (null !=cb) {
			cb.setTag(x);
		}
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		boolean handled = false;
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			if (check_area > 0) {
//				if (event.getX() < check_area) {
//					mIsInDownEvent = true;
//					handled = true;
//				}
//			} else {
//				initCheckArea();
//			}
//			break;
//
//		case MotionEvent.ACTION_CANCEL:
//			mIsInDownEvent = false;
//			break;
//
//		case MotionEvent.ACTION_UP:
//			if (check_area > 0) {
//				if (mIsInDownEvent && event.getX() < check_area) {
//					toggleCheckMark();
//					handled = true;
//				}
//				mIsInDownEvent = false;
//			} else {
//
//			}
//			break;
//		}
//
//		if (handled) {
//			postInvalidate();
//		} else {
//			handled = super.onTouchEvent(event);
//		}
//
//		return handled;
//	}

//	public void initCheckArea() {
//		if (check_area < 0) {
//			int[] loc = new int[2];
//			loc[0] = -1;
//			getLocationInWindow(loc);
//			if (loc[0] > 0) {
//				check_area = loc[0] + getWidth()
//						- MsgSelected.CHECKMARK_AREA;
//			}
//		}
//	}
	
//    private void toggleCheckMark() {
//    	cb.toggle();
//    	listener.onDownloadSelectionChanged(mDownloadId, mCheckBox.isChecked());
//    }

	private CheckBox cb;
//	private int check_area = -1;
//	private boolean mIsInDownEvent = false;
//	private MsgSelected listener;
}
