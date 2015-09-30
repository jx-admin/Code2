
package com.act.mbanking.manager.view;

import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.utils.LogManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class LegendLinearLayout extends ViewGroup {

    int minWidth;

    public LegendLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void measureChildes() {

        if (getChildCount() == 0) {
            return;
        }
        ViewGroup parent = (ViewGroup)getParent();
        int parentWidth = parent.getMeasuredWidth();
        int parentHeight = parent.getMeasuredHeight();
        int widthSum = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            widthSum = childWidth + widthSum;

        }

        // 这个地方会有点问题，不知道为啥第一次parentWidth会是0，导致第一次打开legenditem会有移位的现象，如果客户报此bug，可以把parentWidth改成baseactivity.screenWidth
        if (parentWidth == 0 || parentWidth == BaseActivity.screen_height) {

            parentWidth = BaseActivity.screen_width;
        }

        if (widthSum < parentWidth) {
            this.setMeasuredDimension(parentWidth, getMeasuredHeight());
            float data = (parentWidth - widthSum) / getChildCount();
            int widthOver = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);

                float childWidth = child.getMeasuredWidth();
                childWidth = childWidth + data;
                int childHeight = child.getMeasuredHeight();
                // 如果是最后一个
                if (i == getChildCount() - 1) {
                    childWidth = parentWidth - widthOver;
                }
                child.measure(MeasureSpec.makeMeasureSpec((int)childWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
                widthOver = child.getMeasuredWidth() + widthOver;

            }

        } else if (widthSum >= parentWidth) {

            this.setMeasuredDimension(widthSum, getMeasuredHeight());
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        LogManager.d("size" + size);
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            height = Math.max(height, child.getMeasuredHeight());
            width = width + child.getMeasuredWidth();
        }

        setMeasuredDimension(width, height);
        measureChildes();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            int w = l + child.getMeasuredWidth();
            child.layout(l, 0, w, child.getMeasuredHeight());
            l = w;
        }
    }

}
