package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * 
 * @author yang.c.li
 * 
 */
public class MyScrollHideTitleView extends ListView implements
		OnScrollListener, OnClickListener {

	private float y;// 坐标

	private OnMoveListener onMoveListener;

	private boolean flag = true;

	private View view;

	public MyScrollHideTitleView(Context context) {
		super(context);

	}

	public MyScrollHideTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 注入下拉刷新接口
	public void setOnMoveListener(OnMoveListener onMoveListener) {
		this.onMoveListener = onMoveListener;
	}

	public void setView(View view) {
		this.view = view;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int viewHeight = view.getHeight();
		switch (ev.getAction()) {
		// 按下
		case MotionEvent.ACTION_DOWN:
			y = ev.getY();// 获取点击y坐标
			break;
		// 移动
		case MotionEvent.ACTION_MOVE:
			float nowY = ev.getY();
			int deltaY = (int) (y - nowY);// 获取滑动距离
			if (deltaY > viewHeight) {
				if (flag) {
					flag = false;
					onMoveListener.onMoveUp();
				}
			} else if (deltaY < -viewHeight) {
				if (!flag) {
					flag = true;
					onMoveListener.onMoveDown();
				}
			}
			break;
		// 抬起
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	public interface OnMoveListener {

		void onMoveUp();

		void onMoveDown();
	}

}
