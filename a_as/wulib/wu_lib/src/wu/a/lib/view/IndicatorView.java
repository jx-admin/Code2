package wu.a.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import wu.a.lib.config.AppConfig;
import wu.a.lib.utils.Logger;

/**
 * Created by Administrator on 2015/5/5.
 */
public class IndicatorView extends View {

    private int mIndex;
    private int mTotal;
    private int mRadius = 5;
    private int mMargin = 10;
    private int mWidth;
    private int mHeight;

    private int mCheckedColor = Color.WHITE;
    private int mBackgroundColor = Color.GRAY;

    private Paint p = new Paint();

    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTotalPage(int total) {
        mTotal = total;
        postInvalidate();
    }

    public void setIndex(int i) {
        if (mTotal < 1) return;
        mIndex = i;
        postInvalidate();
    }

    public void setRadius(int i) {
        mRadius = i;
        postInvalidate();
    }

    public void setCheckedColor(int color) {
        mCheckedColor = color;
        invalidate();
    }

    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        p.setAntiAlias(true);
        if (AppConfig.DEBUG) {
            Logger.d("IndicatorView", "ondraw");
        }
        if (mWidth == 0 || mHeight == 0) {
            mHeight = getMeasuredHeight();
            mWidth = getMeasuredWidth();
        }
        int x = (mWidth - mTotal * (mRadius * 2 + mMargin * 2) - mMargin * 2) / 2;
        int y = mHeight / 2 - mRadius;
        for (int i = 0; i < mTotal; i++) {
            x += (mRadius * 2 + mMargin * 2);
            p.setColor(i == mIndex ? mCheckedColor : mBackgroundColor);
            canvas.drawCircle(x + mRadius, y, mRadius, p);
        }
//        canvas.drawCircle(x, y, mRadius, p);
        super.onDraw(canvas);
    }
}
