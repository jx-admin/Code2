package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MyAbsoluteLayoutScroll extends AbsoluteLayout {

	private int mStartY, mMoveY, flagY;

	LinearLayout headView;

	private int mHeadViewHeight;

	private int mNavViewHeight;

	private boolean flagDown = false;

	private Context context;

	private ListView listView;

	public MyAbsoluteLayoutScroll(Context context) {
		super(context);
		this.context = context;
	}

	public MyAbsoluteLayoutScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
//		init();
	}

	public void init() {
//		listView = (ListView) findViewById(R.id.main_list);
//		headView = (LinearLayout) findViewById(R.id.head_view);
		measureView(headView);
		measureView(listView);
		mHeadViewHeight = headView.getMeasuredHeight();
		
//		initData();
		initY();
	}

	public void setHeadView(LinearLayout headView) {
		this.headView = headView;
	}
	
	public void setListView(ListView listView){
		this.listView = listView;
	}

	public void setListViewGone(boolean isGone){
		if(isGone){
			this.listView.setVisibility(View.GONE);
		}else{
			this.listView.setVisibility(View.VISIBLE);
		}
	}
	
	private void initY() {
		AbsoluteLayout.LayoutParams params = ((AbsoluteLayout.LayoutParams) headView.getLayoutParams());
		params.x = 0;
		params.y = mNavViewHeight;
		headView.setLayoutParams(params);

		AbsoluteLayout.LayoutParams params2 = ((AbsoluteLayout.LayoutParams) listView.getLayoutParams());
		params2.x = 0;
		params2.y = mHeadViewHeight + mNavViewHeight;
		listView.setLayoutParams(params2);
	}

	private synchronized void initUp(int y) {
		AbsoluteLayout.LayoutParams params = ((AbsoluteLayout.LayoutParams) headView.getLayoutParams());
		AbsoluteLayout.LayoutParams params2 = ((AbsoluteLayout.LayoutParams) listView.getLayoutParams());

		if (y > 0) {
			if (flagDown)
				flagDown = false;
			if ((params2.y + y) >= (mNavViewHeight + mHeadViewHeight)) {
				initY();
				flagDown = true;
				return;
			}
		} else {
			if (params2.y + y <= 0) {
				if (flagDown == false) {
					params.x = 0;
					params.y = (mNavViewHeight - mHeadViewHeight);
					headView.setLayoutParams(params);
					params2.x = 0;
					params2.y = mNavViewHeight;
					listView.setLayoutParams(params2);
					flagDown = true;
				}

				return;
			}
		}
		params.x = 0;
		params.y += y;
		headView.setLayoutParams(params);

		params2.x = 0;
		params2.y += y;
		listView.setLayoutParams(params2);
	}

//	private void initData() {
//		ArrayList<String> data = new ArrayList<String>();
//		for (int i = 0; i < 30; i++) {
//			data.add("aa" + i);
//		}
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, data);
//		listView.setAdapter(adapter);
//	}

	/***
	 * 测量 headView的宽和高.
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			doActionDown(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			doActionMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			doActionUp(ev);
			break;
		default:
			break;
		}

		if (flagDown)
			return super.dispatchTouchEvent(ev);
		else
			return true;
	}

	private void doActionDown(MotionEvent event) {
		mStartY = (int) event.getY();
		flagY = 0;
	}

	private void doActionMove(MotionEvent event) {
		mMoveY = (int) event.getY();
		int offset = (mMoveY - mStartY);
		initUp(offset - flagY);
		flagY = offset;
	}

	private void doActionUp(MotionEvent event) {
	}

}
