package com.android.accenture.aemm.express.app;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.android.accenture.aemm.express.Main;

/**
 * 仿Launcher中的WorkSapce，可以左右滑动切换屏幕的类
 * @author Yao.GUET
 * blog: http://blog.csdn.net/Yao_GUET
 * date: 2011-05-04
 */
public class ScrollLayout extends ViewGroup {
	private final String namespace = "http://www.text.scrolllayout.com";
	Handler pageHandler;
	int pageWhat;
	int row=2;
	int coloum=4;
	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	
	private int mCurScreen;
	private int mDefaultScreen = 0;
	
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	
	private static final int SNAP_VELOCITY = 600;
	
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mScroller = new Scroller(context);
		
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		row = attrs.getAttributeResourceValue(namespace,"row", 1);
		coloum = attrs.getAttributeResourceValue(namespace,"coloum", 1);
		row=(int) getResources().getDimension(row);
		coloum=(int) getResources().getDimension(coloum);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLayout");
//		if (changed) {
			final int childCount = getChildCount();
			int screen=row*coloum;
			int cWidth=(r-l)/coloum;
			int cHeight=(b-t)/row;
			for (int i=0; i<childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					int screenleft=i/screen*(r-l);
					int position=i%screen;
					int screentop=position/coloum*cHeight;
					screenleft+=position%coloum*cWidth;
					Log.v(TAG,"l="+l+" t="+t+" r="+r+" b="+b+" r-l="+(r-l)+" mw="+getMeasuredWidth()+" cmw="+childView.getMeasuredWidth()+" cmh="+childView.getMeasuredHeight()+" cWidth="+cWidth+" cHeight="+cHeight+" screenleft="+screenleft+" screentop="+screentop);

					
					final int childWidth = childView.getMeasuredWidth();
//					childView.layout(screenleft, screentop, 
//							screenleft+childWidth,screentop+childView.getMeasuredHeight());
					childView.layout(screenleft, screentop, 
							screenleft+cWidth,screentop+cHeight);
					childView.invalidate();
					if(Main.debugView){
//						childView.setBackgroundColor(0x55ff00ff+(i<<12));
					}
				}
			}
//		}
	}

	/***protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int width = MeasureSpec.getSize(widthMeasureSpec);   
    	final int widthMode = MeasureSpec.getMode(widthMeasureSpec);   
		int mCellWidth = width / coloum;
		int mCellHeight = widthMode / row;
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth,
				MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight,
				MeasureSpec.AT_MOST);
		int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);
			child.measure(cellWidthSpec, cellHeightSpec);
		}
		// Use the size our parents gave us
		setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
		resolveSize(mCellHeight * count, heightMeasureSpec));
	}*/
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
    	final int width = MeasureSpec.getSize(widthMeasureSpec);  
    	final int height=MeasureSpec.getSize(heightMeasureSpec);
    	final int widthMode = MeasureSpec.getMode(widthMeasureSpec);   
    	Log.d(TAG, "onMeasure"+" widtMeasureSpec="+widthMeasureSpec+" heightMeasureSpec="+heightMeasureSpec+" width="+width+" height="+height+" model="+widthMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
  
        if (widthMode != MeasureSpec.EXACTLY) {   
            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!"); 
        }
  
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);   
        if (heightMode != MeasureSpec.EXACTLY) {   
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        }   
  
        // The children are given the same width and height as the scrollLayout   
        final int count = getChildCount();  

//		int cWidth=widthMeasureSpec/coloum;
//		int cHeight=heightMeasureSpec/row;
		
		int cWidth=width/coloum;
		int cHeight=height/row;
		
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(cWidth,
				MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(cHeight,
				MeasureSpec.AT_MOST);
		Log.v(TAG," cWidth="+cWidth+" cHeight="+cHeight+" cellWidthSpec="+cellWidthSpec+" cellHeightSpec="+cellHeightSpec);
        for (int i = 0; i < count; i++) {   
//            getChildAt(i).measure(cWidth, cHeight);   
            getChildAt(i).measure(cellWidthSpec, cellHeightSpec);   
//            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);   
        }   
        // Log.d(TAG, "moving to screen "+mCurScreen);   
        scrollTo(mCurScreen * width, 0);         
    }  
    
    /**
     * According to the position of current layout
     * scroll to the destination page.
     */
    public void snapToDestination() {
    	final int screenWidth = getWidth();
    	final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;
    	snapToScreen(destScreen);
    }
    
    public void snapToScreen(int whichScreen) {
    	// get the valid layout page
    	whichScreen = Math.max(0, Math.min(whichScreen, getScreen()-1));
    	if (getScrollX() != (whichScreen*getWidth())) {
    		
    		final int delta = whichScreen*getWidth()-getScrollX();
    		mScroller.startScroll(getScrollX(), 0, 
    				delta, 0, Math.abs(delta)*2);
    		mCurScreen = whichScreen;
    		invalidate();		// Redraw the layout
    	}
    	if(pageHandler!=null){
    		Message message = new Message();
			message.what = pageWhat;
			message.arg1=getCurScreen();
			pageHandler.sendMessage(message);
    	}
    	sendPage(whichScreen);
    }
    
    
    public void setToScreen(int whichScreen) {
    	whichScreen = Math.max(0, Math.min(whichScreen,getScreen()-1));
    	mCurScreen = whichScreen;
    	scrollTo(whichScreen*getWidth(), 0);
    	sendPage(whichScreen);
    }
    private void sendPage(int s){
    	if(pageHandler!=null){
    		Message message = new Message();
			message.what = pageWhat;
			message.arg1=s;
			pageHandler.sendMessage(message);
    	}
    }
    public int getScreen(){
    	return (getChildCount()+(row*coloum-1))/(row*coloum);
    }
    public int getCurScreen() {
    	return mCurScreen;
    }
    
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "event down!");
			if (!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
			
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int)(mLastMotionX - x);
			mLastMotionX = x;
			
            scrollBy(deltaX, 0);
			break;
			
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "event : up");   
            // if (mTouchState == TOUCH_STATE_SCROLLING) {   
            final VelocityTracker velocityTracker = mVelocityTracker;   
            velocityTracker.computeCurrentVelocity(1000);   
            int velocityX = (int) velocityTracker.getXVelocity();   

            Log.d(TAG, "velocityX:"+velocityX); 
            
            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {   
                // Fling enough to move left   
            	Log.d(TAG, "snap left");
                snapToScreen(mCurScreen - 1);   
            } else if (velocityX < -SNAP_VELOCITY   
                    && mCurScreen < getChildCount() - 1) {   
                // Fling enough to move right   
            	Log.d(TAG, "snap right");
                snapToScreen(mCurScreen + 1);   
            } else {   
                snapToDestination();   
            }   

            if (mVelocityTracker != null) {   
                mVelocityTracker.recycle();   
                mVelocityTracker = null;   
            }   
            // }   
            mTouchState = TOUCH_STATE_REST;   
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onInterceptTouchEvent-slop:"+mTouchSlop);
		
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && 
				(mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		
		final float x = ev.getX();
		final float y = ev.getY();
		
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int)Math.abs(mLastMotionX-x);
			if (xDiff>mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
			
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished()? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;
			
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		
		return mTouchState != TOUCH_STATE_REST;
	}
	public void setRow(int num){
		row=num;
	}
	public void setColoum(int num){
		coloum=num;
	}
	public void setPageHandler(int what,Handler handler){
		this.pageHandler=handler;
		this.pageWhat=what;
	}
}

