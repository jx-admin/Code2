package wu.batteryaidl.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import wu.batteryaidl.R;


/**
 * Created by Administrator on 2015/12/24.
 */
public class StepView extends View implements ValueAnimator.AnimatorUpdateListener {
    private Paint mPaint;
    private RadialGradient mLg;
    private int mColor = Color.GREEN;
    private Rect mRect;
    private Rect mIconRect;
    private int mStrokeWidth = 3;
    private Bitmap mIconRef;
    private boolean mChecked;
    private DrawFilter mDrawFilter;
    private PaintFlagsDrawFilter mPaintFilter;
    private ValueAnimator mAnimator;
    private float mPercent;

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaintFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        int iconResId = arr.getResourceId(R.styleable.StepView_StepViewIcon, 0);
        mIconRef = BitmapFactory.decodeResource(getResources(), iconResId);
        setBackgroundColor(Color.TRANSPARENT);
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.addUpdateListener(this);
    }

    public void setIcon(int icon) {
        mIconRef = ((BitmapDrawable) getResources().getDrawable(icon)).getBitmap();
        invalidate();
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        if (mAnimator.isRunning())
            mAnimator.cancel();
//        mAnimator.start();
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //缓存起来，保证这个背景只是重绘一次
        boolean useSoftLayer = Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN || Build.MODEL.toLowerCase().contains("htc 801e");
        int layerType = useSoftLayer ? LAYER_TYPE_SOFTWARE : LAYER_TYPE_HARDWARE;
        setLayerType(layerType, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawFilter == null) {
            mDrawFilter = canvas.getDrawFilter();
        }
        int x = getWidth() / 2, y = getHeight() / 2;
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLg = new RadialGradient(x, y, x, mColor, Color.TRANSPARENT, Shader.TileMode.CLAMP);
            mRect = new Rect(0, 0, getWidth(), getHeight());
            mIconRect = new Rect(mRect);
            mIconRect.inset(getPaddingLeft(), getPaddingTop());
        }
        super.onDraw(canvas);
        if (mChecked) {
            canvas.save();
            mPaint.setShader(mLg);
            canvas.drawCircle(x, y, getWidth() / 2, mPaint);
            canvas.restore();
        }
        mPaint.setAlpha(255);
        if (mIconRef != null) {
            if (mIconRef != null) {
                canvas.drawBitmap(mIconRef, null, mIconRect, mPaint);
            }
        }
        mPaint.reset();
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.setDrawFilter(mPaintFilter);
        float radius = getWidth() / 2.5f;
        canvas.drawCircle(x, y, radius, mPaint);
        mPaint.reset();
        //归位，防止下面性能差
        canvas.setDrawFilter(mDrawFilter);
        super.dispatchDraw(canvas);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mPercent = (float) valueAnimator.getAnimatedValue();
        invalidate();
    }
}
