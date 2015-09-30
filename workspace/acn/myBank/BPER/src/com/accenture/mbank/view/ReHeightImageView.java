
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ReHeightImageView extends ImageView {
    float imageWidth;

    float imageHeight;

    public ReHeightImageView(Context context) {
        super(context);
    }

    public ReHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReHeightImageView(Context context, AttributeSet attrs, int defStyle) {
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
        
        float xx = (float)currentWidth / (float)width;
        currentHeight=(int)(xx*height);
        setMeasuredDimension(currentWidth, currentHeight);
    }
}
