
package net.ds.effect.core;

import net.ds.effect.utils.Constants;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PaintFlagsDrawFilter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.Scroller;

/**
 * <p>
 * Represents the infos for the effect of the workspace.
 * </p>
 *
 * @author huangninghai
 * @version 1.0
 */
public abstract class EffectInfo {

    public static final String KEY_APPLY_EFFECT_TYPE = "theme_effect_type";

    public static final String ACTION_APPLY_EFFECT = "theme_effect_preview_apply_inform";

    public static final int EFFECT_ON_ALL = 1 << 32;
    public static final int EFFECT_ON_HOME = 1;
    public static final int EFFECT_ON_DRAWER = 2;

    public interface Callback {
        public boolean onEffectApplied(Canvas canvas, View child, long drawingTime);
    }

    public final int id;

    public final int type;

    public final String key;

    public final String title;

    public final int resourceID;

    protected Interpolator interpolator;

    protected int mEffcetOn = EFFECT_ON_HOME | EFFECT_ON_DRAWER; // 默认在workspace 和 drawer中均可用

    public EffectInfo(int id, int type, String key, String title, int resID) {
        this.id = id;
        this.type = type;
        this.key = key;
        this.title = title;
        this.resourceID = resID;
    }

    @Override
    public String toString() {
        return "id: " + id + "  key: " + key + "  title: " + title;
    }

    protected boolean apply(Canvas canvas, View child, long drawingTime, PaintFlagsDrawFilter antiAliesFilter, Matrix matrix, float alpha, Callback callback) {
        canvas.save();

        canvas.translate(child.getLeft(), child.getTop());
        if (Constants.RENDER_PERFORMANCE_MODE_PREFER_QUALITY) {
            canvas.setDrawFilter(antiAliesFilter);
        }
        if (matrix != null) {
            canvas.concat(matrix);
        }
        if (alpha < 1.0F) {
            final int cl = child.getLeft();
            final int ct = child.getTop();
            final int cr = child.getRight();
            final int cb = child.getBottom();
            canvas.saveLayerAlpha(0, 0, cr - cl, cb - ct, (int) (255 * alpha), Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        }
        canvas.translate(-child.getLeft(), -child.getTop());

        boolean ret = callback.onEffectApplied(canvas, child, drawingTime);

        if (alpha < 1.0F) {
            canvas.restore();
        }
        canvas.restore();

        return ret;
    }

    public abstract boolean canEnableWholePageDrawingCache();

    public abstract boolean isWorkspaceNeedAntiAlias();

    public abstract boolean getWorkspaceChildStaticTransformation(ViewGroup parentView, View childView, Transformation childTransformation,
            float radio, int offset, int currentScreen, boolean isPortrait);

    public abstract boolean isCellLayoutNeedAntiAlias();

    public abstract Boolean applyCellLayoutChildTransformation(ViewGroup parentView, Canvas canvas, View childView, long drawingTime,
            Callback callback, float radioX, int offset, float radioY, int currentScreen, boolean isPortrait);

    public abstract boolean needInvalidateHardwareAccelerated();

    /**
     * 4.0机器上一些耗时特效暂时不能用{@link FixFPSScroller}}，而是用默认的{@link Scroller}，下一版优化这里
     * @return
     */
    public boolean useDefaultScroller() {
        return false;
    }

    public boolean drawChildrenOrderByMoveDirection() {
        return false;
    }

    public void onTouchDown(boolean isScrolling) {

    }

    public void onTouchUpCancel(boolean isScrolling) {

    }

    public boolean isEffectOn(int effectOn) {
        return (mEffcetOn & effectOn) != 0;
    }

    public void onEffectCheckChanged(boolean checked, EffectInfo otherEffectInfo) {

    }

    public void onEffectEnd(int screenCount, int previousPage, int currentPage) {

    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * 特效是否会将两个不同的屏叠加
     * */
    public boolean isScreenFused() {
        return false;
    }

    /**
     * 用于随机特效选中之后
     * */
    public void onRefresh() {
    }
}
