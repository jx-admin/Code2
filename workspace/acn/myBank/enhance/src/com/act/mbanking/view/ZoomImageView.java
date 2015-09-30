
package com.act.mbanking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {
    float imageWidth;

    float imageHeight;

    public ZoomImageView(Context context) {
        super(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取图片的宽高
        int width = getDrawable().getIntrinsicWidth();
        int height = getDrawable().getIntrinsicHeight();
        
        int currentWidth = getMeasuredWidth();
        int currentHeight = getMeasuredHeight();
        
        float xx = (float)currentHeight / (float)height;
        currentWidth = (int)(xx * width);
        setMeasuredDimension(currentWidth, currentHeight);
    }
}
