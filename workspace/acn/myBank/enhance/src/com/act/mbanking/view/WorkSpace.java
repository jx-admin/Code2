
package com.act.mbanking.view;

import com.act.mbanking.utils.LogManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class WorkSpace extends ViewGroup {

    Scroller mScroller;

    public WorkSpace(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LogManager.d("child"+child.getMeasuredWidth());

        }
    }

    /**
     * 滚动时间
     */
    int snapTime = 800;

    public int getSnapTime() {
        return snapTime;
    }

    public void setSnapTime(int snapTime) {
        this.snapTime = snapTime;
    }

    /**
     * 滚到下一个view
     */
    public void snapToNext() {

        if (scrolling) {
            return;
        }
        int index = getViewIndexByLeft(currentX);

        if (index < getChildCount() - 1) {
            View v = getChildAt(index + 1);
            mScroller.startScroll(currentX, 0, v.getMeasuredWidth(), 0, snapTime);
            postInvalidate();
        }
    };

    public void forceSnapToMain() {

        mScroller.startScroll(currentX, 0, -currentX, 0, snapTime);
        postInvalidate();
    }

    /**
     * 滚到前一个view
     */
    public void snapToPre() {
        if (scrolling) {
            return;
        }
        View view = getViewByRight(currentX);

        mScroller.startScroll(currentX, 0, -view.getMeasuredWidth(), 0, snapTime);
        postInvalidate();
    };

    int currentX;

    boolean scrolling;

    
    
    @Override
    public void computeScroll() {
        super.computeScroll();
        int x = mScroller.getCurrX();

        if (mScroller.computeScrollOffset()) {
            scrolling = true;
            scrollTo(x, 0);

            invalidate();
        } else {

            if (scrolling && workSpaceListener != null) {
                workSpaceListener.onSnapOver();

            }
            scrolling = false;
        }
        currentX = x;
    }

    public int getViewIndexByLeft(int currentX) {

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            int left = child.getLeft();
            int right = child.getRight();
            if (currentX >= left && currentX < right) {

                return i;
            }

        }
        return 0;
    }

    public int getViewIndexByRight(int currentX) {

        currentX = currentX + getMeasuredWidth();
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            int left = child.getLeft();
            int right = child.getRight();
            if (currentX > left && currentX <= right) {

                return i;
            }

        }
        return 0;
    }

    public View getViewByLeft(int currentX) {

        return getChildAt(getViewIndexByLeft(currentX));
    }

    public View getViewByRight(int currentX) {

        return getChildAt(getViewIndexByRight(currentX));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int h = child.getMeasuredHeight();
            int w = l + child.getMeasuredWidth();

            child.layout(l, 0, w, h);
            l = w;

        }
    }

    public int getCurrrentScreenIndexByRight() {

        return getViewIndexByRight(currentX);

    }

    public View getCurrentScreenByRight() {

        return getViewByRight(currentX);
    }

    public int getCurrrentScreenIndexByLeft() {

        return getViewIndexByLeft(currentX);

    }

    public View getCurrentScreenByLeft() {

        return getViewByLeft(currentX);
    }

    WorkSpaceListener workSpaceListener;

    public WorkSpaceListener getWorkSpaceListener() {
        return workSpaceListener;
    }

    public void setWorkSpaceListener(WorkSpaceListener workSpaceListener) {
        this.workSpaceListener = workSpaceListener;
    }

    /**
     * WorkSpace监听器
     * 
     * @author seekting.x.zhang
     */
    public static interface WorkSpaceListener {

        /**
         * 滚动完毕时调用该方法
         */
        void onSnapOver();

    }
}
