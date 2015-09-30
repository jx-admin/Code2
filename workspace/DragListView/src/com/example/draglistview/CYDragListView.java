package com.example.draglistview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;


public class CYDragListView extends ListView implements android.widget.AbsListView.OnScrollListener{
	public interface CYDragListener{
		void onDragFinsh(int position);
	}
	private final int STATE_NORMAL = 0;
	private final int STATE_SCROLL_VERTICAL = 1;
	private final int STATE_SCROLL_HORIZONTAL = 2;
	private final int STATE_SNAP_OUT = 3;
	private int state;
	private Context mContext;
	private int mTouchSlop;
	private CYDragListener listener;
	// 用于滑动的类
	private Scroller mScroller;
	private View snapView;
	private int firstVisibleItem = 0;
	private int listItem = 0;
	private float alpha;
	public CYDragListView(Context context) {
		this(context, null, 0);
	}

	public CYDragListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CYDragListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnScrollListener(this);
		mScroller = new Scroller(context);
		this.mContext = context;
		this.snapView = null;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	public void setDragListener(CYDragListener listener){
		this.listener = listener;
	}
	private void snapToDestination(){
//		if(listItem > this.getChildCount() || listItem < 0)
//			return;
		if(snapView == null)
			return;
		int width = this.getMeasuredWidth();
		int scroll = snapView.getScrollX();
		System.out.println("scroll:" + scroll);
		if(Math.abs(scroll) > width/3){
			snapToOut();
		}else{
			snapToBack();
		}
			
		
	}
	private void snapToBack(){
		System.out.println("snapToBack");
		int delta = snapView.getScrollX();
		System.out.println("snapToBack:" + delta);
		mScroller.startScroll(delta, 0, -delta, 0,Math.abs(delta) * 2);
	}
	private void snapToOut(){
		if(snapView == null)
			return;
		state = STATE_SNAP_OUT;
		//listener.onDragFinsh(listItem);
		System.out.println("snapToOut");
		final int delta = (int) (getMeasuredWidth() - snapView.getScrollX());
		mHandler.sendEmptyMessageDelayed(0, (long) (Math.abs(delta) * 2 / 3.0));
		System.out.println("snapToOut:" + delta);
		int flag = snapView.getScrollX() > 0 ? 1 : -1;
		mScroller.startScroll(snapView.getScrollX(), 0, flag * delta, 0,Math.abs(delta) * 2);
	}
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(listener != null)
			listener.onDragFinsh(listItem);
		};
	};
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset() && snapView != null) {//如果返回true，表示动画还没有结束
			System.out.println("computeScroll");
			snapView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}else{
			System.out.println("compute");
			if(state == STATE_SNAP_OUT && listener != null){
				state = STATE_NORMAL;
				//listener.onDragFinsh(listItem);
				snapView = null;
		}
		}
	}
	
	
	int x,y = 0;
	int sum;
	int temp;
	boolean mFlag = false;
	VelocityTracker mVelocityTracker;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null) {
			// 使用obtain方法得到VelocityTracker的一个对象
			mVelocityTracker = VelocityTracker.obtain();
		}
		// 将当前的触摸事件传递给VelocityTracker对象
		mVelocityTracker.addMovement(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			state = STATE_NORMAL;
			x = (int) event.getX();
			y = (int) event.getY();
			snapView = null;
			mScroller.setFinalX(0);
			break;
		case MotionEvent.ACTION_MOVE:
				
			int dx = (int) (event.getX() - x);
			int dy = (int) (event.getY() - y);
			alpha = 255 - Math.abs(dx/2);
			
			if(Math.abs(dy) > mTouchSlop){//没有进行纵向滑动
				state = STATE_SCROLL_VERTICAL;
				}else{
					if(Math.abs(dx) > mTouchSlop)
						state = STATE_SCROLL_HORIZONTAL;
				}
			
			if(state == STATE_SCROLL_HORIZONTAL){
				listItem = pointToPosition(x,  (int)event.getY()) ;
				//position为visibleItem
				snapView = getChildAt(listItem - firstVisibleItem);
				if(snapView != null && !mScroller.computeScrollOffset()){
					snapView.scrollBy(-dx, 0);
					if(android.os.Build.VERSION.SDK_INT > 10)
						snapView.setAlpha(alpha);
					
					
					System.out.println("onTouchEvent:" + snapView.getScrollX());
				}
					
			x = (int) event.getX();
			}
			
			break;
		case MotionEvent.ACTION_UP:
			//if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = mVelocityTracker;
			// 计算当前的速度
			velocityTracker.computeCurrentVelocity(1000);
			// 获得当前的速度
			int velocityX = (int) velocityTracker.getXVelocity();
			System.out.println("velocityX:" + velocityX);
			if (velocityX > 600 ) {
				// Fling enough to move left
				snapToOut();
			} else if ( velocityX < -600 ) {
				// Fling enough to move right
				snapToOut();
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;

		default:
			break;
		}
	return super.onTouchEvent(event);
}
	
@Override
public void onScrollStateChanged(AbsListView view, int scrollState) {
}

@Override
public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
	this.firstVisibleItem = firstVisibleItem;
}
	

}
