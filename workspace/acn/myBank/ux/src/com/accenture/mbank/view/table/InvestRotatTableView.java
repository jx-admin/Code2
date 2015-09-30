
package com.accenture.mbank.view.table;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.accenture.mbank.BaseActivity;

public class InvestRotatTableView extends View {

    private BitmapDrawable bgBitmapDrawable;

    private BitmapDrawable btnDetailsDrawable;

    private BitmapDrawable btnDetailsOverDrawable;

    private Bitmap bg;

    private Bitmap btnDetails;

    private double maxwidth;

    private double maxHeight;

    private String totalTitle = "";

    private String totalAmount = "";

    /**
     * 各项所在圆球的百分比位置
     */
    public double detailsX = 1 / 3d;

    public double detailsY = 3 / 4d;

    public double totalAmountY = 1 / 2d;

    public double totalTitleY = 2 / 7d;

    private double xx;

    private int width;

    private int height;

    // button的宽高
    public double detailsWidth;

    public double detailsHeight;

    public double textSize = 20d / 480d;

    public InvestRotatTableView(Context context) {
        super(context);
        initSize();
    }

    /**
     * 设置
     * 
     * @param slider
     * @param bg
     */
    public void setRotatResource(int bg, int details, int detailsOver) {
        bgBitmapDrawable = (BitmapDrawable)getContext().getResources().getDrawable(bg);
        btnDetailsDrawable = (BitmapDrawable)getContext().getResources().getDrawable(details);
        btnDetailsOverDrawable = (BitmapDrawable)getContext().getResources().getDrawable(
                detailsOver);
        this.bg = bgBitmapDrawable.getBitmap();
        this.btnDetails = btnDetailsDrawable.getBitmap();
        initSize();
    }

    void initSize() {
        if (bg == null) {
            return;
        }
        width = bg.getWidth();
        height = bg.getHeight();

        maxwidth = BaseActivity.screen_width * (540d / 720d);
        xx = maxwidth / width;

        maxHeight = height * xx;

        bgBitmapDrawable.setBounds(new Rect(0, 0, (int)bg.getWidth(), (int)bg.getHeight()));
        btnDetailsDrawable.setBounds(new Rect(0, 0, (int)btnDetails.getWidth(), (int)btnDetails
                .getHeight()));
        btnDetailsOverDrawable.setBounds(new Rect(0, 0, (int)btnDetails.getWidth(), (int)btnDetails
                .getHeight()));

        detailsWidth = btnDetails.getWidth();
        detailsHeight = btnDetails.getHeight();

        detailsX = detailsX * xx * width;
        detailsY = detailsY * xx * height;

        totalAmountY = totalAmountY * xx * height;
        totalTitleY = totalTitleY * xx * height;
    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale((float)xx, (float)xx);
        bgBitmapDrawable.draw(canvas);
        canvas.restore();

        paint.setTypeface(Typeface.create("", Typeface.BOLD));
        paint.setTextSize((int)(textSize * BaseActivity.screen_width));
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(100, 100, 100));
        float totalTitleX = getStringWidth(paint, totalTitle);
        canvas.drawText(totalTitle, (float)(maxwidth - (float)totalTitleX) / 2, (float)totalTitleY,
                paint);

        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize((int)(textSize * BaseActivity.screen_width));
        paint.setColor(Color.BLACK);
        float totalAmountX = getStringWidth(paint, totalAmount);
        canvas.drawText(totalAmount, (float)(maxwidth - (float)totalAmountX) / 2,
                (float)totalAmountY, paint);

        canvas.save();
        canvas.translate((float)detailsX, (float)detailsY);
        canvas.scale((float)xx, (float)xx);
        if (isTouchButton && btnDetailsOverDrawable != null) {
            btnDetailsOverDrawable.draw(canvas);
        } else {
            btnDetailsDrawable.draw(canvas);
        }
        canvas.restore();
    }

    boolean isTouchButton = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchButton(touchX, touchY)) {
                    isTouchButton = true;
                    invalidate();
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isTouchButton = false;
                invalidate();
                if (isTouchButton(touchX, touchY)) {
                    performButtonClick();
                    return true;
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isTouchButton(float touchX, float touchY) {
        double yStart = detailsY * maxHeight;
        double yEnd = yStart + width;
        double xStart = detailsX;
        double xEnd = detailsX + detailsWidth;
        if ((touchY > detailsY && touchY < yEnd) && (touchX >= xStart && touchX <= xEnd)) {
            return true;
        }
        return false;
    }

    public void performButtonClick() {
        if (this.onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {

        void onClick(View v);
    }

    public int getStringWidth(Paint mPaint, String str) {
        return (int)mPaint.measureText(str);
    }

    public int getFontHeight(Paint mPaint, float fontSize) {
        FontMetrics fm = mPaint.getFontMetrics();
        paint.setTextSize(fontSize);
        return (int)Math.ceil(fm.descent - fm.top) + 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 它的宽高不是图片的宽高，而是以宽高为直角的矩形的对角线的长度
        setMeasuredDimension((int)maxwidth, (int)maxwidth);
    }

    /**
     * @param totalAmountTitle the totalAmountTitle to set
     */
    public void setTotalAmountTitle(int totalAmountTitle) {
        this.totalTitle = getResources().getString(totalAmountTitle);
    }

    public void setTotalAmountTitle(String totalAmountTitle) {
        this.totalTitle = totalAmountTitle;
    }
    
    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

}
