package net.ds.effect.core;

import net.ds.effect.framework.MySlideView2;
import net.ds.effect.utils.Constants;
import net.ds.effect.utils.HardwareAccelerationUtils2;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;

public class EffectSlideView extends MySlideView2 {

    public static final String TAG = Constants.TAG;

    protected Transformation mChildTransformation;

    private int mCurrentEffectType = 0;

    private PaintFlagsDrawFilter mAntiAliesFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public EffectSlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public EffectSlideView(Context context) {
        this(context, null);
    }

    public EffectSlideView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public PaintFlagsDrawFilter getAntiAliesFilter() {
        return mAntiAliesFilter;
    }

    public void setCurrentEffect(int effectKey) {
        mCurrentEffectType = effectKey;
    }

    public int getCurrentScreenTransitionType() {
        return mCurrentEffectType;
    }

    @Override
    public int getCurrentChildIndex() {
        return mCurrChildIndex;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (mChildTransformation == null) {
            mChildTransformation = new Transformation();
        } else {
            mChildTransformation.clear();
        }

        final EffectInfo effect = getChildTransformation(child, mChildTransformation);

        if (effect != null) {
            canvas.save();

            canvas.translate(child.getLeft(), child.getTop());
            canvas.setDrawFilter(effect.isWorkspaceNeedAntiAlias() ? this.mAntiAliesFilter : null);
            if (mChildTransformation.getTransformationType() == Transformation.TYPE_MATRIX
                    || mChildTransformation.getTransformationType() == Transformation.TYPE_BOTH) {
                canvas.concat(mChildTransformation.getMatrix());
            }
            if (mChildTransformation.getTransformationType() == Transformation.TYPE_ALPHA
                    || mChildTransformation.getTransformationType() == Transformation.TYPE_BOTH) {
                if (mChildTransformation.getAlpha() < 1.0F) {
                    final int sx = child.getScrollX();
                    final int sy = child.getScrollY();
                    final int cl = child.getLeft();
                    final int ct = child.getTop();
                    final int cr = child.getRight();
                    final int cb = child.getBottom();
                    canvas.saveLayerAlpha(sx, sy, sx + cr - cl, sy + cb - ct, (int) (255 * mChildTransformation.getAlpha()),
                            Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
                }
            }
            canvas.translate(-child.getLeft(), -child.getTop());

            boolean ret = super.drawChild(canvas, child, drawingTime);

            if (mChildTransformation.getTransformationType() == Transformation.TYPE_ALPHA
                    || mChildTransformation.getTransformationType() == Transformation.TYPE_BOTH) {
                if (mChildTransformation.getAlpha() < 1.0F) {
                    canvas.restore();
                }
            }
            canvas.restore();

            return ret;
        } else {
            return super.drawChild(canvas, child, drawingTime);
        }
    }

    protected EffectInfo getChildTransformation(View childView, Transformation childTransformation) {
        EffectInfo effect = EffectFactory.getEffectByType(mCurrentEffectType);

        if (HardwareAccelerationUtils2.isHardwareAccelerated(this)) {
            if (childView != null && effect != null && effect.needInvalidateHardwareAccelerated()) {
                childView.invalidate();
            }
        }

        if (childView == null) {
            return null;
        }

        int offset = getOffset(childView);
        float radio = getCurrentScrollRatio(childView, offset);

        if (radio == 0 || Math.abs(radio) > 1) {
            return null;
        }

        return effect == null ? null : effect.getWorkspaceChildStaticTransformation(this, childView, childTransformation, radio, offset, this.mCurrChildIndex, true) ? effect : null;
    }

    protected int getOffset(View childView) {
        return 0;
    }

    protected float getCurrentScrollRatio(View childView, int offset) {
        float childMeasuredWidth = childView.getMeasuredWidth();
        int childLeft = childView.getLeft() + offset;
        float ratio = (this.getScrollX() - childLeft) * 1.0F / childMeasuredWidth;
        
        Log.v(Constants.RATIO_TAG, "scrollX = " + getScrollX() + ",  left = " + childLeft + ", radio = " + ratio + ", child = " + childView);
        return ratio;
        
    }

    private float mLastMotionY;

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        final float y = ev.getY();
        EffectInfo effect = null;

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                effect = EffectFactory.getEffectByType(mCurrentEffectType);
                if (effect != null) {
                    effect.onTouchDown(isScrolling());
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                effect = EffectFactory.getEffectByType(mCurrentEffectType);
                if (effect != null) {
                    effect.onTouchUpCancel(isScrolling());
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private boolean isScrolling() {
        return mLastScrollState == OnScrollListener.SCROLL_STATE_FLING || mLastScrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
    }

    protected float getMotionYRadio() {
        float radioY = (mLastMotionY - this.getTop()) / this.getHeight();
        return radioY > 1F ? 1F : radioY;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        EffectInfo effect = EffectFactory.getEffectByType(mCurrentEffectType);
        boolean reverse = effect.drawChildrenOrderByMoveDirection();

        if (reverse) {
            int index = getCurrentChildIndex();

            if (getMoveDirection() == DIRECTION_LEFT) {
                index = index - 1;
                if (index >= 0 && Math.abs(getCurrentScrollRatio(getChildAt(index), 0)) < 0.5) {
                    return childCount - i - 1;
                }
            } else if (getMoveDirection() == DIRECTION_RIGHT) {
                index = index + 1;
                if (index < getChildCount() && Math.abs(getCurrentScrollRatio(getChildAt(index), 0)) > 0.5) {
                    return childCount - i - 1;
                }
            }
        }
        return super.getChildDrawingOrder(childCount, i);
    }
}
