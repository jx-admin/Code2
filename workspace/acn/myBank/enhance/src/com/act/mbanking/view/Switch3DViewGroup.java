
package com.act.mbanking.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.Scroller;

public class Switch3DViewGroup extends ViewGroup {

    Scroller mScroller;

    public Switch3DViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
        this.setChildrenDrawingOrderEnabled(true);
        mScroller = new Scroller(context);
        camera = new Camera();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            lastX = (int)ev.getX();
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        // TODO Auto-generated method stub
        // return super.getChildStaticTransformation(child, t);

        doRotate1(child, t);
        return true;

    }

    int lastTop = 0;

    int maxRotate = 85;

    private void doRotate1(View child, Transformation t) {

        int childWidth = child.getMeasuredWidth();
        int scrollX = getScrollX();

        int index = indexOfChild(child);
        if (top > index) {
            camera.save();
            camera.translate(childWidth / 2, 0, 0);
            camera.rotateY(30);
            camera.translate(-childWidth / 2, 0, 0);
            camera.getMatrix(t.getMatrix());
            camera.restore();
        } else if (top < index) {
            camera.save();
            camera.translate(childWidth / 2, 0, 0);
            camera.rotateY(-30);
            camera.translate(-childWidth / 2, 0, 0);
            camera.getMatrix(t.getMatrix());
            camera.restore();

        }

    }

    private void doRotate(View child, Transformation t) {

        float mid = getMeasuredWidth() / 2;
        float childX = 0;
        float max = 180;
        float xx = max / getMeasuredWidth();

        float rotate = 0;
        int index = indexOfChild(child);

        int width = cellWidth / 2;
        camera.save();

        if (index < top) {
            childX = (float)(getScrollX() + mid - child.getLeft() - 0.5 * cellWidth);

            // System.out.println(childX);
            if (index == 1) {
                // System.out.println(childX);
            }
            rotate = (childX) * xx;
            camera.translate(width, 0, 0);

            if (rotate > maxRotate) {
                rotate = maxRotate;
            }
            camera.rotateY(rotate);
            if (index == 0) {
                // System.out.println(rotate + "rotate");
            }
            camera.translate(-width, 0, 0);
            camera.getMatrix(t.getMatrix());
            camera.restore();
        } else if (index > top) {

            childX = (float)(child.getLeft() - getScrollX() - mid);
            if (index == 3) {
                // System.out.println(childX);
            }
            // System.out.println(childX);
            rotate = (childX) * xx;
            if (rotate > maxRotate) {
                rotate = maxRotate;
            }
            camera.translate(width, 0, 0);
            camera.rotateY(-rotate);
            camera.translate(-width, 0, 0);
            camera.getMatrix(t.getMatrix());
            camera.restore();
        }
        if (index == top) {

            int xxxx = (int)(getChildAt(top).getLeft() - getScrollX() - mid);

            camera.translate(-xxxx - (cellWidth * zhezhao) / 2, 0, 0);
            camera.getMatrix(t.getMatrix());
            camera.restore();

            // child.setBackgroundColor(Color.rgb(212, 212, 212));
        } else {
            // child.setBackgroundColor(Color.rgb(43, 53, 89));
        }
        // System.out.println(max);

        // camera.translate(0, 0, -200);

    }

    Camera camera;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        currentIndex = getChildCount() / 2;
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

        }

    }

    int currentIndex;

    float zhezhao = 0.6f;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        // System.out.println("onLayout");
        int count = getChildCount();
        for (int i = 0; i <= count - 1; i++) {

            View child = getChildAt(i);
            int h = child.getMeasuredHeight();

            cellWidth = child.getMeasuredWidth();
            int left = l;
            int right = l + child.getMeasuredWidth();

            child.layout(left, 0, right, h);

            l = (int)(l + zhezhao * child.getMeasuredWidth());

        }
    }

    int top = 0;

    int lastX = 0;

    int cellWidth = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int)event.getX();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                lastX = x;
                return true;
            }
            case MotionEvent.ACTION_MOVE: {

                int distance = lastX - x;
                scrollBy(distance, 0);
                lastX = x;
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP:
                // mScroller.startScroll(0, 0, 100, 0);
                // invalidate();
                return true;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {

        // 01234-> 01432
        // 0123456->0126543
        // 123465 016543

        generateTop();
        // i=3,count=5,mid=2 result=3
        if (i == top) {
            return childCount - 1;
        } else {
            if (i > top) {
                return childCount - 1 - i + top;
            }
            return i;
        }
    }

    void noticeChange() {

        // System.out.println("change");
    }

    private void generateTop() {

        int x = getScrollX();
        x = x + getMeasuredWidth() / 2;
        int cellWidth = (int)(zhezhao * this.cellWidth);
        int i = x / cellWidth;

        if (i >= 0 && i < getChildCount()) {

            if (top != i) {
                top = i;
                noticeChange();
            } else {

            }

        } else {

        }
        // System.out.println("x" + x);

    }

    protected void dispatchDraw(Canvas canvas) {
        canvas.setDrawFilter(filter);
        super.dispatchDraw(canvas);

    };

    PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {

            int x = mScroller.getCurrX();
            scrollTo(x, 0);

            invalidate();
        } else {

        }
    }

}
