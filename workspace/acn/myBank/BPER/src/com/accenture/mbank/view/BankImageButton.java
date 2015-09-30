
package com.accenture.mbank.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**
 * 考虑图片文件的伸缩，导致view的区域大于图片的区域，当手指点击图片以外的区域时，会响应事件，这是我们不希望看到的，因此做了一些处理
 * 
 * @author seekting.x.zhang
 */
public class BankImageButton extends ImageButton {

    public BankImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(Color.YELLOW);
        // TODO Auto-generated constructor stub
    }

    float imageWidth;

    float imageHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getDrawable() == null) {
            return;
        }
        int imageWidth = getDrawable().getIntrinsicWidth();
        int imageHeight = getDrawable().getIntrinsicHeight();
        int currentWidth = getMeasuredWidth();

        int currentHeight = getMeasuredHeight();
        // 如果有拉伸
        if (imageWidth != currentWidth || imageHeight != currentHeight) {

            // 高度缩小了xx倍
            float xx = (float)imageHeight / (float)currentHeight;
            this.imageWidth = (float)imageWidth / xx;
            this.imageHeight = currentHeight;
        }
        // xx = (float)currentWidth / (float)width;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getDrawable() == null) {
            return super.onTouchEvent(event);
        }

        int action = event.getAction();

        float x = event.getX();
        float y = event.getY();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // System.out.println("<------------------------------");
        // System.out.print("width=" + width);
        // System.out.print("height=" + height);
        // System.out.print("x=" + x);
        // System.out.print("y=" + y);
        // System.out.print("imageWidth=" + imageWidth);
        // System.out.print("imageHeight=" + imageHeight);
        // System.out.println("------------------------------>");
        ScaleType scaleType = getScaleType();
        if (scaleType == ScaleType.FIT_START) {

            if (x > imageWidth) {
                if (action == MotionEvent.ACTION_DOWN) {
                    return false;
                } else if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onTouchEvent(event);
                }
            }
        } else if (scaleType == ScaleType.FIT_END) {
            if (x < width - imageWidth) {
                if (action == MotionEvent.ACTION_DOWN) {
                    return false;
                } else if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onTouchEvent(event);
                }
            }
        } else if (scaleType == ScaleType.FIT_CENTER) {
            float left = (width - imageWidth) / 4;
            float right = 3 * left + imageWidth;
            float top = (height - imageHeight) / 2;
            float bottom = top + imageHeight;
            if (x < left || x > right) {
                if (action == MotionEvent.ACTION_DOWN) {
                    return false;
                } else if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onTouchEvent(event);
                }
            }
        }

        return super.onTouchEvent(event);
    }
}
