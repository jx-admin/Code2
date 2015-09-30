//package com.jfo.common;
package net.ds.effect.framework;

import net.ds.effect.utils.CommonUtils;
import net.ds.effect.utils.Constants;
import net.ds.effect.utils.ViewUtils;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

public class MySlideView2 extends ViewGroup {

    protected int mMinimumVelocity;
    protected int mMaximumVelocity;
    protected int mTouchSlop;
    protected float mDensityScale;
    protected int mTotalWidth;
    protected OnScrollListener mOnScrollListener;
    protected OnPageChangedListener mOnPageChangedListener;
    protected int mLastScrollState = OnScrollListener.SCROLL_STATE_IDLE;
    protected int mCurrChildIndex = 0;
    protected int mCurrHalfChildIndex = 0;
    protected int mCurrChildX = 0;
    protected int mChildInterval = 0;
    protected int mVelocity = 150;

    private Configuration mConfig;

    protected static final int DIRECTION_LEFT = -1;
    protected static final int DIRECTION_NONE = 0;
    protected static final int DIRECTION_RIGHT = 1;

    private int mMoveDirection = DIRECTION_NONE;

    protected float mDownMotionX = 0;

    protected Params mScrollingParams = new Params();

    private int mTranslationObjWidth;

    private int mTranslationObjHeight;

    public interface OnScrollListener {
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_PASS_HALF = 3;
        public static final int SCROLL_STATE_PASS_WHOLE = 4;

        public void onScrollStateChanged(MySlideView2 view, int scrollState, int childIndex);
        public void onScroll(MySlideView2 view, int delta);
    }

    public interface OnPageChangedListener {
        public void onPageChanged(int oldPage, int newPage);
    }

    public MySlideView2(Context context) {
        this(context, null);
    }

    public MySlideView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySlideView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mConfig = new Configuration(this.getResources().getConfiguration());

        init(context);
    }

    public void setTranslationObj(int width, int height) {
        this.mTranslationObjWidth = width;
        this.mTranslationObjHeight = height;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (this.mTranslationObjWidth != 0 && this.mTranslationObjHeight != 0) {
            float width = this.mTranslationObjWidth * 1.0f * child.getHeight() / this.mTranslationObjHeight;
            if (child.getWidth() > width) {
                canvas.save();

                float factor = (child.getWidth() - width) * 4 / 5 / child.getWidth();
                canvas.translate(-(child.getLeft() - getScrollX()) * factor, 0);
                boolean ret = super.drawChild(canvas, child, drawingTime);

                canvas.restore();

                return ret;
            } else {
                return super.drawChild(canvas, child, drawingTime);
            }
        } else {
            return super.drawChild(canvas, child, drawingTime);
        }
    }

    @Override
    public void dispatchConfigurationChanged(Configuration newConfig) {
        if (mConfig == null || newConfig == null || mConfig.compareTo(newConfig) != 0) {
            mConfig = new Configuration(newConfig);
            try {
                super.dispatchConfigurationChanged(newConfig);
            } catch (Throwable e) {
                // ignore
            }
        }
    }

    private void init(Context context) {
        setFocusable(true);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mDensityScale = getContext().getResources().getDisplayMetrics().density;
        mChildInterval = (int) (mDensityScale * 16);

        setChildrenDrawnWithCacheEnabled(true);
        setAlwaysDrawnWithCacheEnabled(true);
    }

    private int getScreenWidth() {
        return this.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public void setVelocityScale(double percent) {
        mVelocity *= percent;
        mBounceDuration *= percent;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        mOnPageChangedListener = onPageChangedListener;
    }

    public OnPageChangedListener getOnPageChangedListener() {
        return mOnPageChangedListener;
    }

    public void reportScrollStateChange(int newState) {
        if (newState != mLastScrollState) {
            mLastScrollState = newState;
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(this, newState, getCurrentChildIndex());
            }

            if (newState == OnScrollListener.SCROLL_STATE_IDLE) {
                mMoveDirection = DIRECTION_NONE;
            }
//            if (newState == OnScrollListener.SCROLL_STATE_IDLE)
//                setCurrentChildIndex(getScrollX() / mScreenWidth);
        }
    }

    public void reportScrollStatePassHalf(int newState, int index) {
        mCurrHalfChildIndex = index;
        mLastScrollState = newState;
        if (mOnScrollListener != null && mCurrHalfChildIndex != index) {
            mOnScrollListener.onScrollStateChanged(this, newState, index);
        }
    }

    public void reportScrollStatePassWhole(int newState, int index) {
        setCurrentChildIndex(index);
        mLastScrollState = newState;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(this, newState, index);
        }
    }

    public void scrollToPreviousChild() {
        int index = getCurrentChildIndex();
        if (index > 0) {
            scrollToChild(index - 1);
        }
    }

    public void scrollToNextChild() {
        int index = getCurrentChildIndex();
        if (index >= 0 && index < getChildCount() - 1) {
            scrollToChild(index + 1);
        }
    }

    public void scrollToChild(int index) {
        if (index >= 0 && index < getChildCount()) {
            setCurrentChildIndex(index);
            int width = getChildAt(index).getWidth();
            scrollTo(mCurrChildX + (width - getScreenWidth()) / 2, 0);
        }
    }

    public int getCurrentChildIndex() {
        return mCurrChildIndex;
    }

    public void setCurrentChildIndex(int index) {
        if (index >= 0 && index < getChildCount()) {
//            if (mCurrChildIndex != index) {
            if (mCurrChildIndex != index) {
                if (this.mOnPageChangedListener != null) {
                    this.mOnPageChangedListener.onPageChanged(mCurrChildIndex, index);
                }
            }
            mCurrChildIndex = index;
            mCurrHalfChildIndex = index;
            updateCurrentChildX();
            getChildAt(index).requestFocus();
//            }
        }
    }

    private void updateCurrentChildX() {
        int width = 0;
        int currChildIndex = getCurrentChildIndex();
        int interval = getChildInterval();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (i == currChildIndex) {
                mCurrChildX = width;
            }
            if (child.getVisibility() != View.GONE) {
                width += child.getWidth() + interval;
            }
        }
    }

    public void setChildInterval(int interval) {
        mChildInterval = interval;
    }

    public int getChildInterval() {
        return mChildInterval;
    }

    public void reset() {
        removeAllViews();
        scrollTo(0, 0);
        mLastScrollState = OnScrollListener.SCROLL_STATE_IDLE;
        mTotalWidth = 0;
        if (mCurrChildIndex != 0) {
            if (this.mOnPageChangedListener != null) {
                this.mOnPageChangedListener.onPageChanged(mCurrChildIndex, 0);
            }
        }
        mCurrChildIndex = 0;
        mCurrHalfChildIndex = 0;
        mCurrChildX = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        // Find out how big everyone wants to be
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
//            if (i == getCurrentChildIndex())
//                mCurrChildX = width;
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                width += child.getMeasuredWidth();
                height = Math.max(height, child.getMeasuredHeight());
            }
        }
//        mTotalWidth = width;

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childLeft = 0;

        final int interval = getChildInterval();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (i == getCurrentChildIndex()) {
                mCurrChildX = childLeft;
            }
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, 0, childLeft + childWidth, child
                        .getMeasuredHeight());
                childLeft += childWidth + interval;
            }
        }
        mTotalWidth = childLeft - interval;
        scrollToChild(getCurrentChildIndex());
    }


//        layoutChildren();
//
//        if( mSpringModel ) {// AplyLai@ F0X.B-2728 Should do bounce if necessary when onLayout
//            int overstep = getOverstep();
//            if(overstep != 0) {
//                if (mBounceRunnable == null) {
//                    mBounceRunnable = new BounceRunnable();
//                  }
//                 reportScrollStateChange(OnScrollListener.SCROLL_STATE_FLING);
//                   mBounceRunnable.start(overstep);
//            }
//        }
//        // AplyLai@ F0X.B-2728 Should do bounce if necessary when onLayout
//        mInLayout = false;

//    protected void layoutChildren() {
//        if( mSpringModel ) {// AplyLai@ F0X.B-2728 Should do bounce if necessary when onLayout
//            if ( mTouchMode == TOUCH_MODE_SCROLL || mTouchMode == TOUCH_MODE_FLING ) {
//                mOverstepSyncValue = getOverstep();
//                // Stop bouncing
//                if ( mOverstepSyncValue != 0 && mTouchMode == TOUCH_MODE_FLING && mBounceRunnable!=null) {
//                    removeCallbacks(mBounceRunnable);
//                    if (mTouchMode == TOUCH_MODE_FLING) {
//                        mTouchMode = TOUCH_MODE_REST;
//                        int overstep = getOverstep();
//                        if (overstep != 0) {
//                            offsetChildrenTopAndBottom(-overstep);
//                        }
//                    }
//                    mOverstepSyncValue = 0;
//                }
//            }
//        }
//    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);
        Log.d(TOUCH_TAG, "dispatchTouchEvent ev = " + CommonUtils.getEventAction(ev) + " -- " + ret);
        return ret;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean ret = super.onInterceptTouchEvent(ev);
        Log.w(TOUCH_TAG, "onInterceptTouchEvent ev = " + CommonUtils.getEventAction(ev) + " -- " + ret);
        return ret;
    }

    protected static final int TOUCH_MODE_REST = -1;
    protected static final int TOUCH_MODE_DOWN = 0;
    protected static final int TOUCH_MODE_TAP = 1;
    protected static final int TOUCH_MODE_DONE_WAITING = 2;
    public static final int TOUCH_MODE_SCROLL = 3;
    protected static final int TOUCH_MODE_FLING = 4;

    protected int mTouchMode = TOUCH_MODE_REST;

    public void setTouchMode(int mode) {
        mTouchMode = mode;
    }

    public int getTouchMode() {
        return mTouchMode;
    }

    private static final int INVALID_POINTER = -1;
    private static final String TAG = Constants.TAG;
    private static final String TOUCH_TAG = Constants.TOUCH_TAG;
    protected int mActivePointerId = INVALID_POINTER;
    protected int mMotionX;
    protected int mMotionY;

    protected VelocityTracker mVelocityTracker;
    protected FlingRunnable mFlingRunnable;
    protected BrakeRunnable mBrakeRunnable;
    protected BounceRunnable mBounceRunnable;

    protected boolean mBounceEffect = true;
    protected boolean mSpringModel = true;

    protected int mBounceDurationScaler = 0;
    protected int mBounceDuration = 120;
    protected int mBrakeDecelerateTime = 70;

    private boolean mSildeDisabled;

    private boolean mSildeLeftDisabled;

    private boolean mSildeRightDisabled;

    public void setSlideDisabled(boolean value) {
        mSildeDisabled = value;
    }

    public void setSildeLeftDisabled(boolean value) {
        mSildeLeftDisabled = value;
    }

    public void setSildeRightDisabled(boolean value) {
        mSildeRightDisabled = value;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        
        Log.e(TOUCH_TAG, "onTouchEvent, action = " + CommonUtils.getEventAction(ev));

        if (getChildCount() <= 0) {
            return true;
        }

        if (mSildeDisabled) {
            return true;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mActivePointerId = ev.getPointerId(0);
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                mTouchMode = TOUCH_MODE_DOWN;

                mMotionX = x;
                mMotionY = y;

                mDownMotionX = x;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                final int x = (int) ev.getX(pointerIndex);
                int deltaX = x - mMotionX;

                if (mDownMotionX - x < 0) {
                    mMoveDirection = DIRECTION_LEFT;
                } else if (mDownMotionX - x > 0) {
                    mMoveDirection = DIRECTION_RIGHT;
                } else {
                    mMoveDirection = 0;
                }

                if (mMoveDirection == DIRECTION_LEFT && this.mSildeLeftDisabled) {
                    break;
                }

                if (mMoveDirection == DIRECTION_RIGHT && this.mSildeRightDisabled) {
                    break;
                }

                switch (mTouchMode) {
                    case TOUCH_MODE_DOWN:
                        // Check if we have moved far enough that it looks more like a
                        // scroll than a tap
                        if (!startScrollIfNeeded(deltaX)) {
                            break;
                        }
                    case TOUCH_MODE_SCROLL:
                        trackMotionScroll(-deltaX, 0);
                        mMotionX = x;
                        break;
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                switch (mTouchMode) {
                    case TOUCH_MODE_DOWN:
                    case TOUCH_MODE_TAP:
                    case TOUCH_MODE_DONE_WAITING:
                        mTouchMode = TOUCH_MODE_REST;
                        if (!performClick()) {
                            ((View) this.getParent()).performClick();
                        }
                        slideToScreenBound();
                        break;
                    case TOUCH_MODE_SCROLL: {
                        final VelocityTracker velocityTracker = mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                        final int initialVelocity = (int) velocityTracker.getXVelocity();

                        int overstep = getOverstep2();
                        if (overstep != 0) {
                            // scrolling released, but overstepped. Don't care about velocity.  Bounce back.
                            if (mBounceRunnable == null) {
                                mBounceRunnable = new BounceRunnable();
                            }
                            mBounceRunnable.start(overstep);
                        } else {
                            if (Math.abs(initialVelocity) > mMinimumVelocity) {
                                int distance = getFlingDistance2(initialVelocity);
                                if (mFlingRunnable == null) {
                                    mFlingRunnable = new FlingRunnable();
                                }
//                                mFlingRunnable.start(-initialVelocity);
                                mFlingRunnable.startScroll(distance, mVelocity);
                            } else {
                                slideToScreenBound();
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }

                mDownMotionX = 0;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                slideToScreenBound();
                break;
            }
        }

        return true;
    }

    private boolean startScrollIfNeeded(int deltaX) {
        final int distance = Math.abs(deltaX);
        if (distance > mTouchSlop) {
            // createScrollingCache();
            mTouchMode = TOUCH_MODE_SCROLL;
            // mMotionCorrection = deltaY;
            // requestDisallowInterceptTouchEvent(true);
            reportScrollStateChange(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
            return true;
        }

        return false;
    }

    public int getMoveDirection() {
        return mMoveDirection;
    }

    public class Params {

        Params() {
        }

        public int scrollX;
        public int scrollXCenter;
        public int currChildIndex;
        public int width;
        public int widthLeft;
        public int widthRight;
        public int boundLeft;
        public int boundRight;
        public int center;
        public int centerLeft;
        public int centerRight;
        public Params update() {
            scrollX = getScrollX();
            scrollXCenter = scrollX + getScreenWidth() / 2;
            currChildIndex = getCurrentChildIndex();
            width = currChildIndex >= getChildCount() ? 0 : getChildAt(currChildIndex).getWidth();
            widthLeft = (currChildIndex - 1 < getChildCount() && currChildIndex - 1 >= 0) ? getChildAt(currChildIndex - 1).getWidth() : -1;

            widthRight = (currChildIndex + 1 < getChildCount() && currChildIndex + 1 >= 0) ? getChildAt(currChildIndex + 1).getWidth() : -1;

            boundLeft = mCurrChildX;
            boundRight = mCurrChildX + width;
            center = mCurrChildX + width / 2;
            centerLeft = widthLeft < 0 ? Integer.MIN_VALUE : mCurrChildX - widthLeft + widthLeft / 2 - getChildInterval();
            centerRight = widthRight < 0 ? Integer.MAX_VALUE : mCurrChildX + width + widthRight / 2 + getChildInterval();
            return this;
        }
    }
    /**
     * Track a motion scroll
     *
     * @param deltaX Amount to offset mMotionView. This is the accumulated delta since the motion
     *        began. Positive numbers mean the user's finger is moving down the screen.
     * @param incrementalDeltaX Change in deltaX from the previous event.
     * @return true if we're already at the beginning/end of the list and have nothing to do.
     */
    public boolean trackMotionScroll(int deltaX, int incrementalDeltaX) {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(this, deltaX);
        }

        scrollBy(deltaX, 0);

        Params params = mScrollingParams.update();

        int index = -1;
        if (deltaX > 0 && params.scrollXCenter > params.boundRight) {
            index = params.currChildIndex + 1;
        } else if (deltaX < 0 && params.scrollXCenter < params.boundLeft) {
            index = params.currChildIndex - 1;
        }
        if (index >= 0 && index != mCurrHalfChildIndex) {
            reportScrollStatePassHalf(OnScrollListener.SCROLL_STATE_PASS_HALF, index);
        }

        index = -1;
        if (deltaX > 0 && params.scrollXCenter >= params.centerRight) {
            index = params.currChildIndex + 1;
        } else if (deltaX < 0 && params.scrollXCenter <= params.centerLeft) {
            index = params.currChildIndex - 1;
        }
        if (index >= 0 && index != params.currChildIndex) {
            reportScrollStatePassWhole(OnScrollListener.SCROLL_STATE_PASS_WHOLE, index);
        }

        return getOverstep2() != 0;
    }

//    public boolean trackMotionScroll(int deltaX, int incrementalDeltaX) {
//        final int childCount = getChildCount();
//        if (childCount == 0) {
//            return true;
//        }
//
//        if (mOnScrollListener != null)
//            mOnScrollListener.onScroll(this, deltaX);
//
//        scrollBy(deltaX, 0);
//
//        final int scrollX = getScrollX();
//        final int indexCurr = scrollX / mScreenWidth;
//
//        final int indexHalf = scrollX / (mScreenWidth / 2);
//        int index = -1;
//        if (deltaX > 0 && indexHalf % 2 == 1)
//            index = indexCurr + 1;
//        else if (deltaX < 0 && indexHalf % 2 == 0)
//            index = indexCurr;
//        // sroll over half of child width
//        if (index >= 0 && index != mCurrHalfChildIndex)
//            reportScrollStatePassHalf(OnScrollListener.SCROLL_STATE_PASS_HALF, index);
//
//        final int lastCurrChildIndex = getCurrentChildIndex();
//        int d = scrollX - lastCurrChildIndex * mScreenWidth;
//        if (Math.abs(d) / mScreenWidth >= 1)
//            setCurrentChildIndex(indexCurr);
//        if (lastCurrChildIndex != getCurrentChildIndex())
//            reportScrollStatePassWhole(OnScrollListener.SCROLL_STATE_PASS_WHOLE, getCurrentChildIndex());
//
//        return params.scrollX <= 0 || params.scrollX >= mTotalWidth - mScreenWidth;
//    }

    private int getFlingDistance2(int velocity) {
        int distance = 0;

        Params params = mScrollingParams.update();
        if (velocity < 0 && params.widthRight >= 0) { // scroll content to the left
            if (params.scrollXCenter >= params.center) {
                distance = params.centerRight - params.scrollXCenter;
            } else {
                distance = params.center - params.scrollXCenter;
            }
        } else if ((velocity > 0 && params.widthLeft >= 0)) {
            if (params.scrollXCenter <= params.center) {
                distance = params.centerLeft - params.scrollXCenter;
            } else {
                distance = params.center - params.scrollXCenter;
            }
        }

        return distance;
    }

    private int getOverstep2() {
        int overstep = 0;

        Params params = mScrollingParams.update();
        if (params.widthLeft < 0 && params.widthRight < 0) {
            overstep = params.center - params.scrollXCenter;
        } else if (params.widthLeft < 0) {
            if (params.scrollXCenter < params.center) {
                overstep = params.center - params.scrollXCenter;
            }
        } else if (params.widthRight < 0) {
            if (params.scrollXCenter > params.center) {
                overstep = params.center - params.scrollXCenter;
            }
        }

        return overstep;
    }


//    private void createScrollingCache() {
//        if (mScrollingCacheEnabled && !mCachingStarted) {
//            setChildrenDrawnWithCacheEnabled(true);
//            setChildrenDrawingCacheEnabled(true);
//            mCachingStarted = true;
//        }
//    }
//
//    private void clearScrollingCache() {
//        if (mClearScrollingCache == null) {
//            mClearScrollingCache = new Runnable() {
//                public void run() {
//                    if (mCachingStarted) {
//                        mCachingStarted = false;
//                        setChildrenDrawnWithCacheEnabled(false);
//                        if ((mPersistentDrawingCache & PERSISTENT_SCROLLING_CACHE) == 0) {
//                            setChildrenDrawingCacheEnabled(false);
//                        }
//                        if (!isAlwaysDrawnWithCacheEnabled()) {
//                            invalidate();
//                        }
//                    }
//                }
//            };
//        }
//        post(mClearScrollingCache);
//    }

    private int getBoundDistance2() {
        Params params = mScrollingParams.update();
        int distance = params.center - params.scrollXCenter;
        int d = 0;
        if (params.scrollXCenter > params.center && params.widthRight >= 0) {
            d = params.centerRight - params.scrollXCenter;
        } else if (params.scrollXCenter < params.center && params.widthLeft >= 0) {
            d = params.centerLeft - params.scrollXCenter;
        }
        if (Math.abs(d) < Math.abs(distance)) {
            distance = d;
        }
        return distance;
    }

    private void slideToScreenBound() {
        int distance = getBoundDistance2();
        if (mBounceRunnable == null) {
            mBounceRunnable = new BounceRunnable();
        }
        mBounceRunnable.start(distance);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//        case KeyEvent.KEYCODE_DPAD_LEFT:
//            scrollToPreviousChild();
//            return true;
//        case KeyEvent.KEYCODE_DPAD_RIGHT:
//            scrollToNextChild();
//            return true;
//
//        case KeyEvent.KEYCODE_DPAD_CENTER:
//        case KeyEvent.KEYCODE_ENTER:
//            // fallthrough to default handling
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }



    /**
     * Responsible for fling behavior. Use {@link #start(int)} to
     * initiate a fling. Each frame of the fling is handled in {@link #run()}.
     * A FlingRunnable will keep re-posting itself until the fling is done.
     *
     */
    private class FlingRunnable implements Runnable {
        /**
         * Tracks the decay of a fling scroll
         */
        private final Scroller mScroller;

        /**
         * Y value reported by mScroller on the previous fling
         */
        private int mLastFlingX;
        private int mInitVelocity;

        FlingRunnable() {
            Interpolator interpolator = new LinearInterpolator();
            mScroller = new Scroller(getContext(), interpolator);
        }

        /**
         *
         * @param distance Horizontal distance to travel. Positive numbers will scroll the content to the left.
         * @param duration
         */
        void startScroll(int distance, int duration) {
            int initialX = distance < 0 ? Integer.MAX_VALUE : 0;
            mLastFlingX = initialX;
            mScroller.startScroll(initialX, 0, distance, 0, duration);
            mTouchMode = TOUCH_MODE_FLING;
            post(this);
        }

        private void endFling() {
            mTouchMode = TOUCH_MODE_REST;

            reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
//            clearScrollingCache();

            removeCallbacks(this);
        }

        private void endFlingIfNeeded() {
            if (getBoundDistance2() != 0) {
                slideToScreenBound();
            } else {
                endFling();
            }
        }

        public void run() {
            switch (mTouchMode) {
                case TOUCH_MODE_FLING: {
                    final Scroller scroller = mScroller;
                    boolean more = scroller.computeScrollOffset();
                    final int x = scroller.getCurrX();

                    // Flip sign to convert finger direction to list items direction
                    // (e.g. finger moving down means list is moving towards the top)
                    int delta = mLastFlingX - x;

                    // Pretend that each frame of a fling scroll is a touch scroll
                    if (delta > 0) {
                        // List is moving towards the top. Use first view as mMotionPosition

                        // Don't fling more than 1 screen
                        delta = Math.min(getWidth() - getPaddingRight() - getPaddingLeft() - 1, delta);
                    } else {
                        // List is moving towards the bottom. Use last view as mMotionPosition

                        // Don't fling more than 1 screen
                        delta = Math.max(-(getWidth() - getPaddingRight() - getPaddingLeft() - 1), delta);
                    }

                    final boolean atEnd = trackMotionScroll(-delta, 0);

                    if (more && !atEnd) {
                        invalidate();
                        mLastFlingX = x;
                        ViewUtils.postOnAnimation(MySlideView2.this, this);
                        /* for sping mode begin */
                    } else if (atEnd) {
                        if (mBounceEffect && mSpringModel) {
                            int overrun = getOverstep2();
//                            overrun = 0;
                            int leftVelocity = mInitVelocity
                                    * (mScroller.getDuration() - mScroller.timePassed())
                                    / mScroller.getDuration();
                            if (mBrakeRunnable == null) {
                                mBrakeRunnable = new BrakeRunnable();
                            }

                            if (overrun != 0) {
                                mBrakeRunnable.start(leftVelocity,
                                        (getWidth() - getPaddingRight() - getPaddingLeft() - 1) / 2,
                                        overrun);
                            } else {
                                endFlingIfNeeded();
                            }
                        } else {
                            endFlingIfNeeded();
                        }
                    } else {
                        endFlingIfNeeded();
                    }
                    break;
                }
                default:
                    return;
            }

        }
    }

    /* for spring mode begin */
    protected class BounceRunnable implements Runnable {
        private Scroller mScroller;
        /**
         * Y value reported by mScroller on the previous fling
         */
        private int mLastFlingX;

        public BounceRunnable() {
            Interpolator interpolator = new LinearInterpolator();
            mScroller = new Scroller(getContext(), interpolator);
        }

        /**
         *
         * @param distance distance > 0: bounce back to the left end;
         *              distance < 0: bounce back to the right end.
         */
        public void start(int distance) {
            int duration = Math.abs(distance) * mBounceDurationScaler + mBounceDuration;

            int startX = distance < 0 ? Integer.MAX_VALUE : 0;
            mLastFlingX = startX;
            mTouchMode = TOUCH_MODE_FLING;

            mScroller.startScroll(startX, 0, distance, 0, duration);
            if (false) {
                Log.w("AbsListView", "Bounce Start: distance=" + distance);
            }

            post(this);
        }

        private void endFling() {
            mTouchMode = TOUCH_MODE_REST;
            reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
//            clearScrollingCache();
        }

        public void run() {
            if (mTouchMode != TOUCH_MODE_FLING) {
                return;
            }

            if (getChildCount() == 0) {
                endFling();
                return;
            }

            Scroller scroller = mScroller;
            boolean more = scroller.computeScrollOffset();
            final int x = scroller.getCurrX();

            int delta = mLastFlingX - x;

            if (false) {
                Log.w("AbsListView", "Bounce Step: delta=" + delta + " pos=" + getOverstep2());
            }

//            mBlockLayoutRequests = true;
//            invokeOnItemScrollListener();
//            offsetChildrenTopAndBottom(delta);
            trackMotionScroll(-delta, 0);
            invalidate();
//            mBlockLayoutRequests = false;

            if (more) {
                mLastFlingX = x;
                ViewUtils.postOnAnimation(MySlideView2.this, this);
            } else {
                endFling();
            }
        }
    }

    private class BrakeRunnable implements Runnable, Interpolator {
        /**
         * Tracks the decay of a fling scroll
         */
        private Scroller mScroller;
        int mOverrun;
        /**
         * Y value reported by mScroller on the previous fling
         */
        private int mLastFlingX;
        private float mPassedTimeRatio;

        public BrakeRunnable() {
            mScroller = new Scroller(getContext(), this);
        }

        public float getInterpolation(float x) {
            // shm deceleration
            x += mPassedTimeRatio;
            return (float) Math.sin(x * Math.PI / 2);
        }

        public void start(int velocity, int maxOffset, int overrun) {

            boolean positive = velocity > 0 ? true : false;

            velocity = Math.abs(velocity);

            /* shm: 2(PI)r / V = T = 4t, t=mBrakeDecelerateTime, v=initial velocity,
             *      r = 4tV/2(PI) = 2tV/(PI)
             */

            int distance = (int) (2 * mBrakeDecelerateTime * velocity / Math.PI / 1000);
            if (distance > maxOffset) {
                // scale down velocity to fit desired t
                velocity = velocity * distance / maxOffset;
                distance = maxOffset;
            }

            overrun = Math.abs(overrun);
            if (overrun * 2 > distance) {
                overrun = distance / 2;
            }

            if (positive) {
                mOverrun = overrun; // first delta modifier
            } else {
                mOverrun = -overrun; // first delta modifier
            }

            mPassedTimeRatio = (float) (Math.asin((float) overrun / distance) * 2 / Math.PI);

            if (!positive) {
                distance = -distance;
            }

            // deceleration
            int startX = distance < 0 ? Integer.MAX_VALUE : 0;

            mLastFlingX = startX;
            mTouchMode = TOUCH_MODE_FLING;

            mScroller.startScroll(startX, 0, distance, 0, mBrakeDecelerateTime);

            if (false) {
                Log.w("AbsListView", "Brake Start: distance=" + distance + " overrun=" + overrun + " passTime=" + mPassedTimeRatio);
            }
            post(this);
        }

        private void endFling() {
            mTouchMode = TOUCH_MODE_REST;
            reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
//            clearScrollingCache();
        }

        public void run() {
            if (mTouchMode != TOUCH_MODE_FLING) {
                return;
            }

            if (getChildCount() == 0) {
                endFling();
                return;
            }

            Scroller scroller = mScroller;
            boolean more = scroller.computeScrollOffset();
            final int x = scroller.getCurrX();

            int delta = mLastFlingX - x;
            if (false) {
                Log.w("AbsListView", "Brake Step: delta=" + delta + " pos=" + getOverstep2());
            }

            if (mOverrun != 0) {
                delta += mOverrun;
                mOverrun = 0;
            }

//            mBlockLayoutRequests = true;
//            invokeOnItemScrollListener();
//            offsetChildrenTopAndBottom(delta);
            trackMotionScroll(-delta, 0);
            invalidate();
//            mBlockLayoutRequests = false;

            if (more) {
                mLastFlingX = x;
                ViewUtils.postOnAnimation(MySlideView2.this, this);
            } else {
                /* sliding back */
                if (mBounceRunnable == null) {
                    mBounceRunnable = new BounceRunnable();
                }
                mBounceRunnable.start(getOverstep2());
            }
        }
    }

}