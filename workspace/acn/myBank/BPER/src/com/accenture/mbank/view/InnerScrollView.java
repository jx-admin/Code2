
package com.accenture.mbank.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

public class InnerScrollView extends ScrollView {

    Handler handler;

    /**
     */
    public ScrollView parentScrollView;

    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();

    }

    private int lastScrollDelta = 0;

    private int maxHeight = -1;

    public void setMaxHeight(int maxHeight) {

        this.maxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > -1) {

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childHeight = getChildAt(0).getMeasuredHeight();
    }

    int childHeight = 0;

    int mTop = 10;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        System.out.println("onscroll_onlayout");
    }

    /**
     * 将targetView滚到最顶端
     */
    public void scrollTo(final View targetView) {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                int oldScrollY = getScrollY();
                int top = targetView.getTop() - mTop;
                final int delatY = top - oldScrollY;
                lastScrollDelta = delatY;
                smoothScrollTo(0, top);
                System.out.println("scroll" + top);
            }
        }, 50);

    }

    /**
     * 将targetView滚到最顶端
     */
    public void scrollTo(final int y) {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                smoothScrollTo(0, y);
            }
        }, 50);

    }

    int currentY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // System.out.println("inter");
        if (parentScrollView == null) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 将父scrollview的滚动事件拦截
                currentY = (int)ev.getY();
            }
            return super.onInterceptTouchEvent(ev);
        } else {
            View child = getChildAt(0);
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 将父scrollview的滚动事件拦截
                currentY = (int)ev.getY();
                setParentScrollAble(false);
                return super.onInterceptTouchEvent(ev);
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                // 把滚动事件恢复给父Scrollview
                setParentScrollAble(true);
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

                if (scroll_mode_3 == scroll_mode) {

                    boolean flag = super.onInterceptTouchEvent(ev);

                }
            }
        }
        return super.onInterceptTouchEvent(ev);

    }

    int scroll_mode = scroll_mode_2;

    /**
     * 无论如何都不把滚动权交给父view
     */
    public static final int scroll_mode_1 = 0;

    /**
     * 子view滚到头就把滚动权交给父view
     */
    public static final int scroll_mode_2 = 1;

    /**
     * 子view滚动到头把滚动权暂时交给父view.如果能滚动，又夺回滚动权限
     */
    public static final int scroll_mode_3 = 2;

    /**
     * 0:未知状态<br>
     * 1:手指向下滑<br>
     * -1:手指向上滑
     */
    int touchState = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (parentScrollView == null && scroll_mode_3 == scroll_mode) {

            if (parentScrollView == null) {
                // LogManager.d("parent scroll view=null");
            }
            // LogManager.d("super.dispatchTouchEvent(ev);" +
            // super.dispatchTouchEvent(ev));

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (parentScrollView != null) {
            // LogManager.d("touch");
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE: {

                if (scroll_mode == scroll_mode_2 || scroll_mode == scroll_mode_3) {
                    View child = getChildAt(0);
                    if (parentScrollView != null) {
                        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

                            // LogManager.d("child-height=" + childHeight +
                            // "this" + this.getId());

                            int height = childHeight - getMeasuredHeight();
                            // LogManager.d("height=" + height);

                            // System.out.println("height=" + height);
                            int scrollY = getScrollY();
                            // System.out.println("scrollY" + scrollY);
                            int y = (int)ev.getY();

                            // LogManager.d("scrollY=" + scrollY + "height" +
                            // height);
                            // 手指向下滑动
                            if (currentY < y) {
                                if (scrollY <= 0) {
                                    // 如果向下滑动到头，就把滚动交给父Scrollview
                                    scrollToEnd();
                                    return false;
                                } else {
                                    // setParentScrollAble(false);

                                }
                            } else if (currentY > y) {
                                if (scrollY >= height) {
                                    // 如果向上滑动到头，就把滚动交给父Scrollview
                                    scrollToEnd();
                                    return false;
                                } else {
                                    // setParentScrollAble(false);

                                }

                            }
                            currentY = y;
                        }
                    }
                    // 根scrollview
                    if (parentScrollView == null && scroll_mode == scroll_mode_3) {

                        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                            int height = child.getMeasuredHeight();
                            height = height - getMeasuredHeight();

                            // System.out.println("height=" + height);
                            int scrollY = getScrollY();
                            // System.out.println("scrollY" + scrollY);
                            int y = (int)ev.getY();

                            // 手指向下滑动
                            if (currentY < y) {
                                currentY = y;
                                if (this.touchState == -1) {

                                    System.out.println("huan");
                                    requestDisallowInterceptTouchEvent(true);
                                    this.touchState = 1;
                                    return false;
                                } else {
                                    this.touchState = 1;
                                }

                            } else if (currentY > y) {
                                currentY = y;
                                if (this.touchState == 1) {
                                    System.out.println("huan");
                                    requestDisallowInterceptTouchEvent(true);
                                    this.touchState = -1;
                                    return false;
                                } else {
                                    this.touchState = -1;
                                }
                            }

                        }
                    }

                }
            }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {

            }

            default:
                break;
        }

        boolean flag = super.onTouchEvent(ev);

        return flag;
    }

    private void scrollToEnd() {

        if (scroll_mode == scroll_mode_2 || scroll_mode == scroll_mode_3) {

            setParentScrollAble(true);
        } else if (scroll_mode == scroll_mode_1) {
            setParentScrollAble(false);
        }

    }

    /**
     * 是否把滚动事件交给父scrollview
     * 
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {

        parentScrollView.requestDisallowInterceptTouchEvent(!flag);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub

        super.onScrollChanged(l, t, oldl, oldt);
        if (onInnerScrollListener != null) {
            onInnerScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    OnInnerScrollListener onInnerScrollListener;

    public OnInnerScrollListener getOnInnerScrollListener() {
        return onInnerScrollListener;
    }

    public void setOnInnerScrollListener(OnInnerScrollListener onInnerScrollListener) {
        this.onInnerScrollListener = onInnerScrollListener;
    }

    public static interface OnInnerScrollListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);

    }
    
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
