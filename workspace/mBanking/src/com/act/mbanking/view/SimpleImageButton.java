
package com.act.mbanking.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.act.mbanking.R;
import com.act.mbanking.utils.LogManager;

/**
 * 
 <style> textarea { width:90%; height:100; } </style> <body> example:<br>
 * <textarea> <com.act.mbanking.view.SimpleImageButton
 * xmlns:bank="http://schemas.android.com/apk/res/com.act.mbanking"
 * android:layout_width="wrap_content" android:layout_height="wrap_content"
 * android:layout_margin="@dimen/margin_large" android:background="@null"
 * android:src="@drawable/safety_btn" bank:autoAddFocusImg="true"
 * bank:focusMode="round" /></textarea></textarea> <br>
 * com.act.mbanking.view.SimpleImageButton <br>
 * 美工不做有焦点的图片,为了省美工的时间，在这里用代码画图片 <br>
 * .注：请不要为该view配多张image </body>
 * 
 * @author seekting.x.zhang
 */
public class SimpleImageButton extends ImageButton {

    private boolean isTouchDown;

    /**
     * 是否自动加focus背景图片
     */
    private boolean autoAddFocusImg = true;

    int foucsColorBolder = Color.argb(33, 55, 55, 55);

    int focusColorCenter = Color.argb(99, 55, 55, 55);

    Bitmap focusBitmap;

    public static final String ROUND = "round";

    public static final String RECT = "rect";

    public static final int rect = 1;

    public static final int round = 0;

    /**
     * 0代表透明区域不画(round) <br>
     * 1代表画矩形 (rect)<br>
     */
    int focusMode = 0;

    public boolean isAutoAddFocusImg() {
        return autoAddFocusImg;
    }

    public void setAutoAddFocusImg(boolean autoAddFocusImg) {
        this.autoAddFocusImg = autoAddFocusImg;
    }

    public SimpleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        focusPaint = new Paint();
        mPaint = new Paint();
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.SimpleImageButton);
        autoAddFocusImg = type.getBoolean(R.styleable.SimpleImageButton_autoAddFocusImg, false);
        String focusMode = type.getString(R.styleable.SimpleImageButton_focusMode);
        if (focusMode == null || focusMode.equals("")) {
            this.focusMode = rect;
        } else if (focusMode.equals(rect)) {
            this.focusMode = rect;
        } else if (focusMode.equals(ROUND)) {

            this.focusMode = round;
        } else {
            this.focusMode = rect;
        }
        type.recycle();
    }

    Paint focusPaint;

    Paint mPaint;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    boolean isFirst = false;

    Bitmap bitmap;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!autoAddFocusImg) {
            return;
        }
        if (!isFirst) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable)getDrawable();
            bitmap = bitmapDrawable.getBitmap();

            Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                    Config.ARGB_4444);
            isFirst = true;
            Canvas canvas1 = new Canvas(bitmap);
            draw(canvas1);
            MyAsyncTask asyncTask = new MyAsyncTask(bitmap);
            asyncTask.execute();
        }
        if (isTouchDown && focusBitmap != null && !focusBitmap.isRecycled()) {

            canvas.drawBitmap(focusBitmap, 0, 0, mPaint);
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogManager.d("onAttachedToWindow" + "SimpleImageButton");
    }

    @Override
    protected void onDetachedFromWindow() {
        LogManager.d("onDetachedFromWindow" + "SimpleImageButton");
        super.onDetachedFromWindow();
        if (focusBitmap != null && !focusBitmap.isRecycled()) {
            focusBitmap.recycle();
        }
    }

    private void generateFocusBitmap(Bitmap bitmap) {
        focusPaint.setColor(focusColorCenter);
        if (focusBitmap != null) {
            if (!focusBitmap.isRecycled()) {

                focusBitmap.recycle();
            }
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // LogManager.d("width" + width + "height" + height);
        focusBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Canvas canvas = new Canvas(focusBitmap);
        Drawable drawable = getDrawable();
        if (focusMode == 0) {

            for (int y = 0; y < getMeasuredHeight(); y++) {
                int beginX = getMeasuredWidth();
                int beginY = getMeasuredHeight();
                int endX = 0;
                int endY = 0;
                for (int x = 0; x < getMeasuredWidth(); x++) {

                    int color = bitmap.getPixel(x, y);

                    if (color != Color.TRANSPARENT) {
                        beginX = Math.min(beginX, x);
                        endX = Math.max(endX, x);

                    }

                }

                beginX--;
                if (beginX < 0) {
                    beginX = 0;
                }
                endX++;
                if (endX > getMeasuredWidth()) {
                    endX = getMeasuredWidth();
                }
                if (beginX < endX) {
                    LinearGradient linearGradient = new LinearGradient(beginX, y,
                            (beginX + endX) / 2, y, foucsColorBolder, focusColorCenter,
                            TileMode.MIRROR);
                    focusPaint.setShader(linearGradient);
                    canvas.drawLine(beginX, y, endX, y, focusPaint);

                    // linearGradient = new LinearGradient((beginX + endX) / 2,
                    // y, endX, y,
                    // focusColor, Color.TRANSPARENT, TileMode.CLAMP);
                    // focusPaint.setShader(linearGradient);
                    // canvas.drawLine((beginX + endX) / 2, y, endX, y,
                    // focusPaint);
                }
            }
        } else if (focusMode == 1) {
            Rect rect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRect(rect, focusPaint);
        }
        bitmap.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        boolean flag = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (flag && autoAddFocusImg) {
                    isTouchDown = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (autoAddFocusImg) {
                    isTouchDown = false;
                }
                invalidate();
                break;
            default:
                break;
        }
        return flag;
    }

    private class MyAsyncTask extends AsyncTask {

        Bitmap bitmap;

        public MyAsyncTask(Bitmap bitmap) {

            this.bitmap = bitmap;
        }

        @Override
        protected Object doInBackground(Object... params) {
            // LogManager.d("这里是异步" + "generateFocusBitmap");
            generateFocusBitmap(bitmap);
            return null;
        }
    }
}
