
package com.accenture.mbank.view;

import java.security.acl.LastOwnerException;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 类似于抽屉的布局
 * 
 * @author seekting.x.zhang
 */
public class DrawerLayout extends ViewGroup {

    private int openIndex = 0;

    boolean isExpand = false;

    Button expander;

    public DrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        expander = new Button(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);

        expander.setLayoutParams(layoutParams);
        expander.setText("open" + System.currentTimeMillis());
        addView(expander);
        expander.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateExpand();
            }
        });

    }

    int widthMeasureSpec, heightMeasureSpec;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;

        float width = 0;
        float height = 0;
        measureChild(expander, widthMeasureSpec, heightMeasureSpec);
        int height1 = MeasureSpec.getSize(heightMeasureSpec);
        System.out.println("height1" + height1);
        // 剩下的
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        for (int i = 1; i < getChildCount(); i++) {
            final int open = i;
            View child = getChildAt(i);
            if (i >= 1) {
                child.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        expandChild(open);
                    }
                });
            }
            if (isExpand) {

                if (i == openIndex) {
                    int resume = (getChildCount() - 1) * expander.getMeasuredHeight();
                    resume = height1 - resume;
                    System.out.println("resume" + resume);
                    int sub = MeasureSpec.makeMeasureSpec(resume, mode);
                    measureChild(child, widthMeasureSpec, sub);
                    System.out.println("openIndex:" + child.getMeasuredHeight());
                } else {
                    measureChild(child, widthMeasureSpec, heightMeasureSpec);
                }

            }
        }

        if (isExpand) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            System.out.println(this + "getMeasuredHeight()-" + getMeasuredHeight());
            return;
        } else {
            width = Math.max(width, getChildAt(0).getMeasuredWidth());
            height = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension((int)width, (int)height);
            System.out.println(this + "getMeasuredHeight()-" + getMeasuredHeight());
            return;
        }

    }

    private void expend() {

        isExpand = true;
        ViewGroup v = (ViewGroup)getParent();
        if (v instanceof DrawerLayout) {
            DrawerLayout drawerLayout = (DrawerLayout)v;
            int position = drawerLayout.indexOfChild(this);
            View lastOpened = drawerLayout.getChildAt(drawerLayout.openIndex);
            if (lastOpened instanceof DrawerLayout) {
                ((DrawerLayout)lastOpened).openIndex = 0;
                ((DrawerLayout)lastOpened).isExpand = false;
            }
            drawerLayout.openIndex = position;
            drawerLayout.remeasure();
        }
        requestLayout();
    }

    private void collapse() {
        isExpand = false;
        View v = (View)getParent();
        if (v instanceof DrawerLayout) {
            DrawerLayout drawerLayout = (DrawerLayout)v;
            drawerLayout.openIndex--;
        }
        requestLayout();
    }

    private void updateExpand() {
        if (isExpand) {
            collapse();
        } else {
            expend();
        }

    }

    private void remeasure() {
        onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    void expandChild(int position) {

        View lastOpend = getChildAt(openIndex);
        if (lastOpend instanceof DrawerLayout) {

            DrawerLayout drawerLayout = (DrawerLayout)lastOpend;
            drawerLayout.isExpand=false;
            drawerLayout.openIndex=0;
            
        }
        if (openIndex == position) {
            openIndex = 0;
        } else {
            openIndex = position;
        }
       
        View child = getChildAt(position);
        remeasure();
        if (child instanceof DrawerLayout) {

            DrawerLayout drawerLayout = (DrawerLayout)child;
            drawerLayout.expend();
        }

        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        System.out.println(this + "layout");
        // 先来居上
        // 0
        // 1
        // 2
        int top = 0;
        int bottom = top + getMeasuredHeight();
        System.out.println("bottom" + bottom);

        expander.layout(l, 0, l + expander.getMeasuredWidth(), 0 + expander.getMeasuredHeight());
        top = top + expander.getMeasuredHeight();
        if (!isExpand) {
            return;
        }

        for (int i = 1; i <= openIndex; i++) {
            // 放上边
            View child = getChildAt(i);

            System.out.println("child.getMeasuredHeight()" + child.getMeasuredHeight());
            // child.layout(l, t, r, b);

            child.layout(l, top, l + child.getMeasuredWidth(), top + child.getMeasuredHeight());
            top = top + child.getMeasuredHeight();
            System.out
                    .println("layout-" + child + "l" + l + "top" + top + "right"
                            + (l + child.getMeasuredWidth()) + "bottom"
                            + (top + child.getMeasuredHeight()));
        }
        for (int i = getChildCount() - 1; i > openIndex; i--) {
            View child = getChildAt(i);

            System.out.println("child.getMeasuredHeight()" + child.getMeasuredHeight());
            int topp = bottom - child.getMeasuredHeight();
            int rightt = l + child.getMeasuredWidth();

            child.layout(l, topp, rightt, bottom);

            System.out.println("layout-" + child + "l=" + l + "top="
                    + (bottom - child.getMeasuredHeight()) + "right="
                    + (l + child.getMeasuredWidth()) + "bottom=" + bottom);
            bottom = bottom - child.getMeasuredHeight();
        }
    }

}
