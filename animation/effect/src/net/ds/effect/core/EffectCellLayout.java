package net.ds.effect.core;

import net.ds.effect.framework.CellLayout;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class EffectCellLayout extends CellLayout implements EffectInfo.Callback {
    
    private boolean mEffectEabled = true;
    
    public EffectCellLayout(Context context) {
        super(context);
    }

    public EffectCellLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onEffectApplied(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }
    
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (child == null) {
            return true;
        }

        final Boolean result = mEffectEabled ? applyChildTransformation(canvas, child, drawingTime) : null;

        if (result != null) {
            return result.booleanValue();
        } else {
            return super.drawChild(canvas, child, drawingTime);
        }
    }
    
    protected Boolean applyChildTransformation(Canvas canvas, View childView, long drawingTime) {
        if (childView != null && childView.getParent() instanceof EffectCellLayout
                && childView.getParent().getParent() instanceof EffectSlideView) {
            EffectSlideView effectSlideView = (EffectSlideView) childView.getParent().getParent();

            int screenTransitionType = effectSlideView.getCurrentScreenTransitionType();

            EffectInfo effect = EffectFactory.getEffectByType(screenTransitionType);

            if (effect == null) {
                return null;
            }

            int offset = effectSlideView.getOffset(this);
            float radio = effectSlideView.getCurrentScrollRatio(this, offset);
            final float radioY = effectSlideView.getMotionYRadio();

            if (radio == 0 || Math.abs(radio) > 1) {
                return null;
            }

            if (effect.drawChildrenOrderByMoveDirection()) {
                setChildrenDrawingOrderEnabled(true);
            } else {
                setChildrenDrawingOrderEnabled(false);
            }

            return effect.applyCellLayoutChildTransformation(this, canvas, childView, drawingTime, this, radio, offset, radioY, effectSlideView.getCurrentChildIndex(), true);
        }

        return null;
    }

}
