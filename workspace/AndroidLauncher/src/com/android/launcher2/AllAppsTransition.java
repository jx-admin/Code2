package com.android.launcher2;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class AllAppsTransition {
	private Launcher mLauncher;
    private View mAllAppsView;
    private Rect mRect;
    private float mInitialY;
    private int mTouchSlop;

    private final static int INVALID_POINTER = -1;
    private int mPointerId;
    
    private final static int IDLE = 0;
    private final static int INTERPRETING_GESTURE = 1;
    private final static int TRANSITIONING_TO_ALLAPPS = 2;
    private final static int TRANSITIONING_FROM_ALLAPPS = 3;
    private int mState;
    
    public AllAppsTransition(Launcher launcher, View allAppsView) {
        mLauncher = launcher;
        mAllAppsView = allAppsView;

        final ViewConfiguration configuration = ViewConfiguration.get(launcher);
        mTouchSlop = configuration.getScaledTouchSlop();
    }
    
    public void setRect(Rect rect) {
    	mRect = rect;
    	moveAllAppsView(mRect.bottom);
    }
    
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	boolean intercept = false;
    	
    	AllAppsView allAppsView = (AllAppsView)mAllAppsView;
    	if (!allAppsView.isVisible() || (allAppsView.isVisible() && allAppsView.isScrolledToTop())) {
	    	final int pointerId = event.getPointerId(0);
	    	
	    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
	
	    	case MotionEvent.ACTION_DOWN:
	    		mInitialY = event.getY();
	    		mPointerId = pointerId;
	    		mState = INTERPRETING_GESTURE;
	    		break;
	
	    	case MotionEvent.ACTION_MOVE:
				if (mPointerId != pointerId) {
					// We're only interested in single finger gestures, so abort if a second finger is detected.
					abort();
				}
				else if (mState == INTERPRETING_GESTURE) {
		            final int pointerIndex = event.findPointerIndex(mPointerId);
		            final float y = event.getY(pointerIndex);
		            final float deltaY = mInitialY - y;
		            final boolean allAppsIsVisible = ((AllAppsView)mAllAppsView).isVisible();
		
		            if (Math.abs(deltaY) > mTouchSlop) {
		            	if ((deltaY < 0) && allAppsIsVisible) {
		            		mState = TRANSITIONING_FROM_ALLAPPS;
		            	}
		            	else if ((deltaY > 0) && !allAppsIsVisible) {
		            		mState = TRANSITIONING_TO_ALLAPPS;
		            		prepareAllAppsView();
		            	}
		            }
		            
		            if (mState != INTERPRETING_GESTURE) {
		            	intercept = true;
		            }
				}
				break;
	
	    	case MotionEvent.ACTION_UP:
	        case MotionEvent.ACTION_CANCEL:
	        	abort();
	        	break;
	    	}
    	}
    	else {
    		mPointerId = INVALID_POINTER;
    		mState = IDLE;
    	}
    		
   	return intercept;
    }
   
    public boolean onTouchEvent(MotionEvent event) {
    	final int pointerId = event.getPointerId(0);
		if (mPointerId != pointerId) {
			// We're only interested in single finger gestures, so abort if a second finger is detected.
			abort();
			return false;
		}
    	
    	switch (event.getAction() & MotionEvent.ACTION_MASK) {

    	case MotionEvent.ACTION_DOWN:
    		abort();
    		break;

    	case MotionEvent.ACTION_MOVE:
			if (mPointerId != pointerId) {
				// We're only interested in single finger gestures, so abort if a second finger is detected.
				abort();
			}
			else {
	            final int pointerIndex = event.findPointerIndex(mPointerId);
	            final float y = event.getY(pointerIndex);
				
				if (mState == TRANSITIONING_FROM_ALLAPPS) {
					moveAllAppsView((int)y);
					mAllAppsView.invalidate();
				}
				else if (mState == TRANSITIONING_TO_ALLAPPS) {
					moveAllAppsView((int)y);
					mAllAppsView.invalidate();
				}
			}
			break;

    	case MotionEvent.ACTION_UP:
            
			if ((mState == TRANSITIONING_FROM_ALLAPPS) || (mState == TRANSITIONING_TO_ALLAPPS)) {
	            final int pointerIndex = event.findPointerIndex(mPointerId);
	            final int y = (int)event.getY(pointerIndex);
				if (y > (mRect.height() / 2)) {
					hideAllAppsView();
				}
				else {
					fullyShowAllAppsView();
				}
				mPointerId = INVALID_POINTER;
				mState = IDLE;
			}
    		break;
    		
        case MotionEvent.ACTION_CANCEL:
        	abort();
        	break;
    	}

   	return true;
    }
    
    void prepareAllAppsView() {
    	moveAllAppsView(mRect.bottom);
    	mLauncher.showAllApps(false);
    }
    
    void hideAllAppsView() {
    	mLauncher.closeAllApps(false);
    }
    
    void fullyShowAllAppsView() {
    	moveAllAppsView(mRect.top);
		mLauncher.showAllApps(false);
		mAllAppsView.invalidate();
    }
    
    void moveAllAppsView(int newY) {
    	if ((newY >= mRect.top) && (newY <= mRect.bottom)) {
    		mAllAppsView.layout(mAllAppsView.getLeft(), newY, mAllAppsView.getRight(), newY + mAllAppsView.getHeight());
    	}
    }
    
    void abort() {
		if (mState == TRANSITIONING_FROM_ALLAPPS) {
			fullyShowAllAppsView();
		}
		else if (mState == TRANSITIONING_TO_ALLAPPS) {
			hideAllAppsView();
		}
		mPointerId = INVALID_POINTER;
		mState = IDLE;
    }

}
