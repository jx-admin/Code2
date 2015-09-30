
package com.accenture.mbank.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class ReHeightImageButton extends ImageButton {

    public ReHeightImageButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ReHeightImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        int width = getDrawable().getIntrinsicWidth();
        int height = getDrawable().getIntrinsicHeight();
        int currentWidth = getMeasuredWidth();

        // System.out.println("<------------");
        int currentHeight = getMeasuredHeight();
        // System.out.println(currentWidth + ":" + currentHeight);
        float xx = (float)currentWidth / (float)width;
        currentHeight = (int)(xx * height);
        // System.out.println(currentWidth + ":" + currentHeight);
        // System.out.println("------------>");
        setMeasuredDimension(currentWidth, currentHeight);

    }

    Paint paint = new Paint();

    Drawable focusDrawable;

    public void setFocusDrawable(Drawable focusDrawable) {
        this.focusDrawable = focusDrawable;
    }

    boolean touchAble = true;

    public void setTouchAble(boolean flag) {
        touchAble = flag;
        // System.out.println(this + "touchAble" + touchAble);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        // System.out.println(this + "touchAble" + touchAble);
        if (touchAble) {
            return super.onTouchEvent(event);
        } else {
            // System.out.println("false");
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        // super.onDraw(canvas);
        // paint.setAntiAlias(true);
        // 方案二
        Drawable drawable = getDrawable();
        float scale = (float)getMeasuredWidth() / (float)drawable.getIntrinsicWidth();
        canvas.save();
        canvas.scale(scale, scale);
        drawable.draw(canvas);
        canvas.restore();

        // 方案一:这个方案为什么图像会失真？
        // Matrix matrix = new Matrix();
        // matrix.setScale(0.5f, 0.5f);
        // canvas.drawBitmap(drawable.getBitmap(), matrix, paint);

    }
}
