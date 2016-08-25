package wu.a.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.view.TextureView;

import wu.videodemo.R;


/**
 * This extension of {@link TextureView} is created to isolate scaling of this view.
 */
public abstract class ScalableTextureView extends TextureView {

    private Integer mContentWidth;
    private Integer mContentHeight;

    private float mPivotPointX = 0f;
    private float mPivotPointY = 0f;

    private float mContentScaleX = 1f;
    private float mContentScaleY = 1f;

    private float mContentRotation = 0f;

    private float mContentScaleMultiplier = 1f;

    private int mContentX = 0;
    private int mContentY = 0;

    private final Matrix mTransformMatrix = new Matrix();

    private float heightMultiplier;

    private ScaleType mScaleType;

    public enum ScaleType {
        CENTER_CROP, TOP, BOTTOM, FILL
    }

    public ScalableTextureView(Context context) {
        super(context);
    }

    public ScalableTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScalableTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ScalableTextureViewStyle, defStyle, 0);
        if (null != a) {
            try {
                heightMultiplier = a.getFloat(R.styleable.ScalableTextureViewStyle_heightMultiplier, 0);
            } finally {
                a.recycle();
            }
        }
    }

    public void setScaleType(ScaleType scaleType) {
        mScaleType = scaleType;
    }

    public void setHeightMultiplier(float hscale) {
        this.heightMultiplier = hscale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (heightMultiplier != 0) {
            int width = getMeasuredWidth();
            int height = (int) (width * heightMultiplier);
            setMeasuredDimension(width, height);
        }
        if (mContentWidth != null && mContentHeight != null) {
            updateTextureViewSize();
        }
    }

    public void updateTextureViewSize() {
        if (mContentWidth == null || mContentHeight == null) {
            throw new RuntimeException("null content size");
        }

        float viewWidth = getMeasuredWidth();
        float viewHeight = getMeasuredHeight();

        float contentWidth = mContentWidth;
        float contentHeight = mContentHeight;


        float scaleX = 1.0f;
        float scaleY = 1.0f;

        switch (mScaleType) {
            case FILL:
                if (viewWidth > viewHeight) {   // device in landscape
                    scaleX = (viewHeight * contentWidth) / (viewWidth * contentHeight);
                } else {
                    scaleY = (viewWidth * contentHeight) / (viewHeight * contentWidth);
                }
                break;
            case BOTTOM:
            case CENTER_CROP:
            case TOP:
                if (contentWidth > viewWidth && contentHeight > viewHeight) {
                    scaleX = contentWidth / viewWidth;
                    scaleY = contentHeight / viewHeight;
                } else if (contentWidth < viewWidth && contentHeight < viewHeight) {
                    scaleY = viewWidth / contentWidth;
                    scaleX = viewHeight / contentHeight;
                } else if (viewWidth > contentWidth) {
                    scaleY = (viewWidth / contentWidth) / (viewHeight / contentHeight);
                } else if (viewHeight > contentHeight) {
                    scaleX = (viewHeight / contentHeight) / (viewWidth / contentWidth);
                }
                break;
        }

        // Calculate pivot points, in our case crop from center
        float pivotPointX;
        float pivotPointY;

        switch (mScaleType) {
            case TOP:
                pivotPointX = 0;
                pivotPointY = 0;
                break;
            case BOTTOM:
                pivotPointX = viewWidth;
                pivotPointY = viewHeight;
                break;
            case CENTER_CROP:
                pivotPointX = viewWidth / 2;
                pivotPointY = viewHeight / 2;
                break;
            case FILL:
                pivotPointX = mPivotPointX;
                pivotPointY = mPivotPointY;
                break;
            default:
                throw new IllegalStateException("pivotPointX, pivotPointY for ScaleType " + mScaleType + " are not defined");
        }

        float fitCoef = 1;
        switch (mScaleType) {
            case FILL:
                break;
            case BOTTOM:
            case CENTER_CROP:
            case TOP:
                if (mContentHeight > mContentWidth) { //Portrait video
                    fitCoef = viewWidth / (viewWidth * scaleX);
                } else { //Landscape video
                    fitCoef = viewHeight / (viewHeight * scaleY);
                }
                break;
        }

        mContentScaleX = fitCoef * scaleX;
        mContentScaleY = fitCoef * scaleY;

        mPivotPointX = pivotPointX;
        mPivotPointY = pivotPointY;

        updateMatrixScaleRotate();
    }

    private void updateMatrixScaleRotate() {
        mTransformMatrix.reset();
        mTransformMatrix.setScale(mContentScaleX * mContentScaleMultiplier, mContentScaleY * mContentScaleMultiplier, mPivotPointX, mPivotPointY);
        mTransformMatrix.postRotate(mContentRotation, mPivotPointX, mPivotPointY);
        setTransform(mTransformMatrix);
    }

    private void updateMatrixTranslate() {
        float scaleX = mContentScaleX * mContentScaleMultiplier;
        float scaleY = mContentScaleY * mContentScaleMultiplier;

        mTransformMatrix.reset();
        mTransformMatrix.setScale(scaleX, scaleY, mPivotPointX, mPivotPointY);
        mTransformMatrix.postTranslate(mContentX, mContentY);
        setTransform(mTransformMatrix);
    }

    @Override
    public void setRotation(float degrees) {
        mContentRotation = degrees;

        updateMatrixScaleRotate();
    }

    @Override
    public float getRotation() {
        return mContentRotation;
    }

    @Override
    public void setPivotX(float pivotX) {
        mPivotPointX = pivotX;
    }

    @Override
    public void setPivotY(float pivotY) {
        mPivotPointY = pivotY;
    }

    @Override
    public float getPivotX() {
        return mPivotPointX;
    }

    @Override
    public float getPivotY() {
        return mPivotPointY;
    }

    public float getContentAspectRatio() {
        return mContentWidth != null && mContentHeight != null
                ? (float) mContentWidth / (float) mContentHeight
                : 0;
    }

    /**
     * Use it to animate TextureView content x position
     *
     * @param x
     */
    public final void setContentX(float x) {
        mContentX = (int) x - (getMeasuredWidth() - getScaledContentWidth()) / 2;
        updateMatrixTranslate();
    }

    /**
     * Use it to animate TextureView content x position
     *
     * @param y
     */
    public final void setContentY(float y) {
        mContentY = (int) y - (getMeasuredHeight() - getScaledContentHeight()) / 2;
        updateMatrixTranslate();
    }

    protected final float getContentX() {
        return mContentX;
    }

    protected final float getContentY() {
        return mContentY;
    }

    /**
     * Use it to set content of a TextureView in the center of TextureView
     */
    public void centralizeContent() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int scaledContentWidth = getScaledContentWidth();
        int scaledContentHeight = getScaledContentHeight();

        mContentX = 0;
        mContentY = 0;

        updateMatrixScaleRotate();
    }

    public Integer getScaledContentWidth() {
        return (int) (mContentScaleX * mContentScaleMultiplier * getMeasuredWidth());
    }

    public Integer getScaledContentHeight() {
        return (int) (mContentScaleY * mContentScaleMultiplier * getMeasuredHeight());
    }

    public float getContentScale() {
        return mContentScaleMultiplier;
    }

    public void setContentScale(float contentScale) {
        mContentScaleMultiplier = contentScale;
        updateMatrixScaleRotate();
    }

    protected final void setContentHeight(int height) {
        mContentHeight = height;
    }

    protected final Integer getContentHeight() {
        return mContentHeight;
    }

    protected final void setContentWidth(int width) {
        mContentWidth = width;
    }

    protected final Integer getContentWidth() {
        return mContentWidth;
    }

}
