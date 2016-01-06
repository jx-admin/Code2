package wu.batteryaidl.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import wu.batteryaidl.help.Utils;

/**
 * Created by Administrator on 2015/12/29.
 */
public class TileView extends View {

    private DrawFilter mDrawFilter;
    private Bitmap mIcon;
    private int mCount = 0;
    private Rect mIconRect;
    private Rect mTileRect;
    private PaintFlagsDrawFilter mPaintFilter;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix matrix;

    public TileView(Context context) {
        this(context, null, 0);
    }

    public TileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaintFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
//        mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.charge_model_quick_icon);
    }


    public void setCount(int c) {
        mCount = c;
        invalidate();
    }

    public void render(final Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIcon = Utils.getIconByPackageName(getContext(), intent);
                TileView.this.postInvalidate();
            }
        }).start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawFilter == null) {
            mDrawFilter = canvas.getDrawFilter();
            mIconRect = new Rect(getPaddingLeft(), getPaddingTop(), getWidth(), getHeight());
            mTileRect = new Rect(getWidth() - getPaddingLeft() - getPaddingRight(), 0, getWidth(), getPaddingTop() + getPaddingBottom());
        }
        canvas.setDrawFilter(mPaintFilter);
        if (mIcon != null) {
            if (matrix == null) {
                matrix = new Matrix();
                float scale = (float) (getWidth()) / mIcon.getWidth();
                matrix.setScale(scale, scale);
            }
            canvas.drawBitmap(mIcon, matrix, null);
        }
        mPaint.setColor(Color.RED);
        canvas.drawCircle(mTileRect.centerX(), mTileRect.centerY(), mTileRect.width() / 2, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTileRect.width() * 2 / 3);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = mTileRect.top + (mTileRect.bottom - mTileRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(String.valueOf(mCount), mTileRect.centerX(), baseline, mPaint);
        super.onDraw(canvas);
        //归位，防止下面性能差
        canvas.setDrawFilter(mDrawFilter);
        super.dispatchDraw(canvas);
    }
}
