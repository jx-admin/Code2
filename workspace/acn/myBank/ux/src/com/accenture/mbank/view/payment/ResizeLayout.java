
package com.accenture.mbank.view.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class ResizeLayout extends LinearLayout {

    private static final String TAG = ResizeLayout.class.getSimpleName();

    private static int count = 0;

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ResizeLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mListener != null) {
            mListener.OnResize(l, t, r, b);
        }
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG,changed+" onLayout " + count+++ "=>OnLayout called! l=" + l + ", t=" + t + ",r=" + r + ",b="
                + b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d(TAG,"onMeasure " + count+++ "=>onMeasure called! widthMeasureSpec=" + widthMeasureSpec
                + ", heightMeasureSpec=" + heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG,"onSizeChanged " + count+++ "=>onResize called! w=" + w + ",h=" + h + ",oldw=" + oldw
                + ",oldh=" + oldh);

    }

    private OnResizeListener mListener;

    public interface OnResizeListener {
        void OnResize(int w, int h, int oldw, int oldh);
    }

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }

}
